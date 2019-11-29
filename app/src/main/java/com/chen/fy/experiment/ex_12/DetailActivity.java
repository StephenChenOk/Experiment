package com.chen.fy.experiment.ex_12;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.chen.fy.experiment.R;

public class DetailActivity extends AppCompatActivity {

    private WebView wvDetail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout_ex12);
        initView();
        initData();
    }

    private void initView() {
        wvDetail = findViewById(R.id.wv_detail);
    }

    private void initData() {
        if (getIntent() != null) {
            //自己生成一个浏览器页面，而不是打开默认浏览器
            wvDetail.setWebViewClient(new WebViewClient());
            //加载网页
            wvDetail.loadUrl(getIntent().getStringExtra(Constants.NEWS_DETAIL_URL_KEY));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(wvDetail!=null){
            wvDetail.destroy();
        }
    }
}
