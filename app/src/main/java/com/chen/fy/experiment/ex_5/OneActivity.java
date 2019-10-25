package com.chen.fy.experiment.ex_5;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chen.fy.experiment.R;

public class OneActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvCounter;

    private static final String COUNT_VALUE = "count_value";
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.counter_layout);

        initView();
    }

    /**
     * 活动遭到异常销毁后执行的方法，可以在里面保存一些必要的信息
     */
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putInt(COUNT_VALUE,count);  //保存数值
        super.onSaveInstanceState(outState, outPersistentState);
    }

    /**
     * 活动重新执行创建时执行
     * 可以在此方法中恢复因为异常被销毁的活动所保留的信息
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        count = savedInstanceState.getInt(COUNT_VALUE);
        tvCounter.setText(Integer.toString(count));
    }

    private void initView(){

        Button btnCounter = findViewById(R.id.btnCount);
        Button btnShowToast = findViewById(R.id.btnShowToast);
        tvCounter = findViewById(R.id.tvCounter);

        btnCounter.setOnClickListener(this);
        btnShowToast.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnShowToast:
                Toast.makeText(this,"Hello World!",Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnCount:
                tvCounter.setText(Integer.toString(++count));
                break;
        }
    }

}
