package com.chen.fy.experiment.ex_12;

import android.content.Context;
import android.os.AsyncTask;
import android.os.NetworkOnMainThreadException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewsListAsyncTask extends AsyncTask<Integer,Void,String> {

    private NewsAdapter mAdapter;
    private Context mContext;

    NewsListAsyncTask(Context context,NewsAdapter adapter){
        this.mContext = context;
        this.mAdapter = adapter;
    }

    /**
     * 后台任务开始之前调用，通常用来任务的初始化操作
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * 执行后台耗时操作，已在姿仙尘各种执行
     * @param integers 构造对象时传进来的第一个参数
     * @return 返回结果
     */
    @Override
    protected String doInBackground(Integer... integers) {
        Integer col = integers[0];      //请求API所需的频道
        Integer newsNum = integers[0];  //每页新闻数量
        Integer page = integers[0];     //当前页码

        //1 设置请求参数，不同的接口请求参数可能不一样
        NewsRequest requestObj = new NewsRequest();
        requestObj.setCol(col);
        requestObj.setNum(newsNum);
        requestObj.setPage(page);
        String urlParams = requestObj.toString();

        //2 创建OkHttp请求对象
        Request request = new Request.Builder()
                .url(Constants.GENERAL_NEWS_URL+urlParams)
                .get().build();
        //3 开始发送同步请求,同步回阻塞当前线程直到服务器返回数据为止
        try{
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();

            if(response.isSuccessful()){
                String body = response.body().string();
                return body;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 后台任务执行完毕时调用,此时已经回到UI线程
     * @param s 后台执行任务完成的返回值
     */
    @Override
    protected void onPostExecute(String s) {
        //对服务器返回的json文件进行解析，并将解析结果封装到一个指定的对象集合中
        Gson gson = new Gson();
        Type jsonType = new TypeToken<BaseResponse<List<News>>>(){}.getType();
        BaseResponse<List<News>> newsListResponse = gson.fromJson(s,jsonType);
        for(News news:newsListResponse.getData()){
            mAdapter.add(news);
        }
        //添加新闻成功后，通知适配器更新
        mAdapter.notifyDataSetChanged();
        super.onPostExecute(s);
    }
}
