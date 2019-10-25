package com.chen.fy.experiment.ex_6;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.chen.fy.experiment.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ex_6_login);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }
}
