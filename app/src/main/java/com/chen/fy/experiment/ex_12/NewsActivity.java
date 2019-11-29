package com.chen.fy.experiment.ex_12;


import android.content.Intent;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chen.fy.experiment.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewsActivity extends AppCompatActivity {

    private NewsAdapter mNewsAdapter;
    private List<News> newsList;
    private ListView lvNewsList;

    private int page = 1;

    private int mCurrentColIndex = 0;

    private int[] mCols = new int[]{Constants.NEWS_COL5, Constants.NEWS_COL7, Constants.NEWS_COL8,
            Constants.NEWS_COL10, Constants.NEWS_COL11};

    //OkHttp回调接口
    private okhttp3.Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {  //请求失败时调用
            e.printStackTrace();

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException { //当请求有响应时调用
            if (response.isSuccessful()) {
                final String body = response.body().string();
                //跳转到主线程中进行操作(其实内部是对Handler的一个再度封装)
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //对服务器返回的json文件进行解析，并将解析结果封装到一个指定的对象集合中
                        Gson gson = new Gson();
                        Type jsonType = new TypeToken<BaseResponse<List<News>>>() {
                        }.getType();
                        BaseResponse<List<News>> newsListResponse = gson.fromJson(body, jsonType);
                        for (News news : newsListResponse.getData()) {
                            mNewsAdapter.add(news);
                        }
                        //添加新闻成功后，通知适配器更新
                        mNewsAdapter.notifyDataSetChanged();
                    }
                });
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ex_12_news_layout);

        initView();
        initData();
    }

    private void initView() {
        lvNewsList = findViewById(R.id.lv_news_list_ex12);
        lvNewsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NewsActivity.this, DetailActivity.class);
                News news = (News) mNewsAdapter.getItem(position);
                intent.putExtra(Constants.NEWS_DETAIL_URL_KEY, news.getContentUrl());
                startActivity(intent);
            }
        });
    }

    private void initData() {
        newsList = new ArrayList<>();
        mNewsAdapter = new NewsAdapter(this, R.layout.ex12_news_list_item3, newsList);
        lvNewsList.setAdapter(mNewsAdapter);
        refreshData(page);
    }

    /**
     * 开启子线程通过okhttp进行网络请求
     *
     * @param page 请求的页面
     */
    private void refreshData(final int page) {

        //构建AsyncTask对象，传入请求需要的参数，再开启AsyncTask
        new NewsListAsyncTask(NewsActivity.this, mNewsAdapter).execute(new Integer[]{
                mCols[mCurrentColIndex],
                Constants.NEWS_NUM, page});

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                //1 设置请求参数，不同的接口请求参数可能不一样
//                NewsRequest requestObj = new NewsRequest();
//                requestObj.setCol(mCols[mCurrentColIndex]);
//                requestObj.setNum(Constants.NEWS_NUM);
//                requestObj.setPage(page);
//                String urlParams = requestObj.toString();
//                //2 创建OkHttp请求对象
//                Request request = new Request.Builder()
//                        .url(Constants.GENERAL_NEWS_URL+urlParams)
//                        .get().build();
//                //3 开始发送异步请求
//                try{
//                    OkHttpClient client = new OkHttpClient();
//                    client.newCall(request).enqueue(callback);
//                }catch (NetworkOnMainThreadException ex){
//                    ex.printStackTrace();
//                }
//            }
//        }).start();
    }
}
