package com.chen.fy.experiment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }
    private void initView(){

        Button btnSend = findViewById(R.id.btnCount);
        etMessage = findViewById(R.id.etMessage);

        btnSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSend:
                String message = etMessage.getText().toString();
                Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
                break;

        }
    }
}
