package com.chen.fy.experiment.ex_12;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.chen.fy.experiment.R;
import com.chen.fy.experiment.ex_7.News;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {

    private NewsAdapter mNewsAdapter;
    private List<News> newsList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ex_12_news_layout);

        initView();
        initData();
    }

    private void initView() {

    }


    private void initData() {
        mNewsAdapter = new NewsAdapter(this, R.layout.ex12_news_list_item3, newsList);
    }
}
