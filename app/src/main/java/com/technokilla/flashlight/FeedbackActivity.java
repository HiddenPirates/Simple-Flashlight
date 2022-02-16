package com.technokilla.flashlight;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FeedbackActivity extends AppCompatActivity {

    Button sendBtn;
    EditText subject_field, message_field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        setTitle("Feedback");

        subject_field = findViewById(R.id.subject_field);
        message_field = findViewById(R.id.message_field);

        sendBtn = findViewById(R.id.feedback_send_btn);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String subject = subject_field.getText().toString();
                String message = message_field.getText().toString();

                if (subject.isEmpty() || message.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please fill the form first", Toast.LENGTH_LONG).show();
                }
                else{
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:nura57764@gmail.com?subject="+subject+"&body="+message));
                    startActivity(intent);
                }
            }
        });

    }
}