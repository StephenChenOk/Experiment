package com.chen.fy.experiment.ex_4;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.chen.fy.experiment.R;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_layout);

        TextView tvMessage = findViewById(R.id.tvMessage);
        String message;
        if(getIntent()!=null){
            message = getIntent().getStringExtra("message");
            if(message!=null){
                tvMessage.setText(message);
            }
        }
    }
}
