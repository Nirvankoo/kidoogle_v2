package com.nirv.proj1;

import org.json.JSONException;

public class SendRequestToGPT {
    private String speech;
    private String answer;

    public SendRequestToGPT(String speech) {
        this.speech = speech;
    }

    public void sendRequestToGPT() {
        GPTRequestHandler gptRequestHandler = new GPTRequestHandler();



        // Call the sendGPTRequest method with the speech input
        try {
            gptRequestHandler.sendGPTRequest(speech, new GPTRequestHandler.GPTResponseListener() {
                @Override
                public void onResponse(String response) {
                    // Handle response
                    System.out.println("Response from GPT: " + response);
                    answer = response; // Save the response in the answer variable
                }

                @Override
                public void onError(String error) {
                    // Handle error
                    System.err.println("Error from GPT: " + error);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Method to get the answer
    public String getAnswer() {
        return answer;
    }
}