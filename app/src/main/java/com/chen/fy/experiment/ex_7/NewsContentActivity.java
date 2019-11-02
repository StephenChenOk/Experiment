package com.chen.fy.experiment.ex_7;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.chen.fy.experiment.R;

public class NewsContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ex7_news_content_layout);

        TextView tvDetail = findViewById(R.id.tv_news_content);
        if(getIntent() != null){
            tvDetail.setText(getIntent().getStringExtra("newsContent"));
        }
    }
}
