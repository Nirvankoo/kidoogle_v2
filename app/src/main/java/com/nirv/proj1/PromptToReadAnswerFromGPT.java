package com.nirv.proj1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PromptToReadAnswerFromGPT {

    public static AlertDialog dialog; // Declare AlertDialog as a class variable

    public static void showCustomDialog(Context context,
                                        String responseBody, DialogInterface.OnClickListener yesClickListener,
                                        DialogInterface.OnClickListener noClickListener) {
        // Inflate custom layout
        View dialogView = LayoutInflater.from(context).inflate(R.layout.read_gpt_amswer_prompt, null);

        // Find views in the custom layout
        TextView promptTextView = dialogView.findViewById(R.id.readGPTanswerPromptTV);
        Button yesButton = dialogView.findViewById(R.id.yesButtonReadGPTanswerPrompt);
        Button noButton = dialogView.findViewById(R.id.noButtonReadGPTanswerPrompt);

        // Set the message to the TextView
        promptTextView.setText("Do you want to play the response?");

        // Create AlertDialog builder only if it's null
        if (dialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(dialogView);
            dialog = builder.create();
        }

        // Set onClickListener to Yes and No buttons
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesClickListener != null) {
                    yesClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                }
                dialog.dismiss();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noClickListener != null) {
                    noClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                }
                dialog.dismiss();
            }
        });

        // Show the dialog
        dialog.show();
    }
}
