package com.nirv.proj1;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GPTHandler {

    private Context context;
    private static final String GPT_API_URL = "https://api.openai.com/v1/chat/completions";
    private static String API_KEY = null;

    private String speech;
    private String answer;

    private RequestCount requestCount = new RequestCount();

    TextView userQuestionConfirmTextView;
    Button userQuestionConfirmButtonYes;
    Button userQuestionConfirmButtonNo;

    TextView answerFromGPT;

    public GPTHandler(String speech,
                      TextView userQuestionConfirmTextView,
                      Button userQuestionConfirmButtonYes,
                      Button userQuestionConfirmButtonNo,
                      Context context, TextView answerFromGPT) {
        this.speech = speech;
        retrieveApiKeyFromFirebase();
        this.userQuestionConfirmTextView = userQuestionConfirmTextView;
        this.userQuestionConfirmButtonYes = userQuestionConfirmButtonYes;
        this.userQuestionConfirmButtonNo = userQuestionConfirmButtonNo;
        this.context = context;
        this.answerFromGPT = answerFromGPT;
    }

    private void retrieveApiKeyFromFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("apiKeys/openai/apiKey");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                API_KEY = dataSnapshot.getValue(String.class);
                Log.d("API_KEY", "API Key: " + API_KEY);
                if (API_KEY != null) {
                    try {
                        sendGPTRequest(speech, new GPTResponseListener() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("GPT Response", response);
                                answer = response; // Save the response

                            }



                            @Override
                            public void onError(String error) {
                                Log.e("GPT Error", error);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("API_KEY", "API key is null");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Error retrieving API key: " + databaseError.getMessage());
            }
        });
    }

    public void sendGPTRequest(String speech, final GPTResponseListener listener) throws JSONException {
        try {
            requestCount.incrementRequestCount();
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("text", speech);
        String jsonText = gson.toJson(jsonObject);

        MediaType mediaType = MediaType.parse("application/json");
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "gpt-3.5-turbo");

        JSONArray messagesArray = new JSONArray();

        JSONObject systemMessage = new JSONObject();
        systemMessage.put("role", "system");
        systemMessage.put("content", "You explaining to kids");
        messagesArray.put(systemMessage);

        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", speech);
        messagesArray.put(userMessage);

        requestBody.put("messages", messagesArray);
        requestBody.put("max_tokens", 500);
        requestBody.put("temperature", 0);

        String jsonBody = requestBody.toString();

        RequestBody body = RequestBody.create(mediaType, jsonBody);
        Request request = new Request.Builder()
                .url(GPT_API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                listener.onError("Request failed: " + e.getMessage());
            }

            public void onResponse(Call call, Response response) throws IOException {
                final String responseBody = response.body().string();


                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(responseBody);
                            JSONArray choices = jsonObject.getJSONArray("choices");
                            if (choices.length() > 0) {
                                JSONObject choice = choices.getJSONObject(0);
                                JSONObject message = choice.getJSONObject("message");
                                if (message.has("content")) {
                                    String responseMessage = message.getString("content");
                                    listener.onResponse(responseMessage);
                                    answerFromGPT.setText(responseMessage);





                                    userQuestionConfirmTextView.setVisibility(View.INVISIBLE);
                                    userQuestionConfirmButtonYes.setVisibility(View.INVISIBLE);
                                    userQuestionConfirmButtonNo.setVisibility(View.INVISIBLE);
                                    //TODO
                                    //Ask to play answer

                                    showPromptDialog(responseMessage);

                                } else {
                                    listener.onError("Missing 'content' field in JSON response");
                                }
                            } else {
                                listener.onError("No 'choices' array found in JSON response");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onError("Error parsing JSON response: " + e.getMessage());
                        }

                    }

                    private void showPromptDialog(String responseMessage) {
                        // Show the prompt dialog
                        PromptToReadAnswerFromGPT.showCustomDialog(context, responseMessage,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Handle Yes click
                                        // For example: Start playing the answer
                                    }
                                },
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Handle No click
                                    }
                                });
                    }
                });


                }
        });
    }

    public interface GPTResponseListener {
        void onResponse(String response);
        void onError(String error);
    }
}
