package com.chen.fy.experiment.ex_7;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.chen.fy.experiment.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NewsActivity extends AppCompatActivity {

    private String[] titles = null;
    private String[] authors = null;
    private String[] content = null;

    private static final String NEWS_TITLE = "news_title";
    private static final String NEWS_AUTHOR = "news_author";

    private static final String NEWS_ID = "news_id";
    private List<News> newsList = new ArrayList<>();

    private List<Map<String, String>> dataList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ex7_news_layout);

        initData();

        ListView listView = findViewById(R.id.lv_news_list);
        listView.setAdapter(getNewsAdapter());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent =new Intent(NewsActivity.this, NewsContentActivity.class);
                intent.putExtra("newsContent",newsList.get(position).getContent());
                startActivity(intent);
            }
        });
    }

    private void initData() {
        int length;
        titles = getResources().getStringArray(R.array.titles);
        authors = getResources().getStringArray(R.array.authors);
        content = getResources().getStringArray(R.array.content);

        TypedArray images = getResources().obtainTypedArray(R.array.images);

        if (titles.length > authors.length) {
            length = authors.length;
        } else {
            length = titles.length;
        }

        for (int i = 0; i < length; i++) {
           News news = new News();
           news.setTitle(titles[i]);
           news.setAuthor(authors[i]);
           news.setContent(content[i]);
           news.setImageId(images.getResourceId(i,0));

           newsList.add(news);
        }
    }



    private NewsAdapter getNewsAdapter() {
        return new NewsAdapter(this, R.layout.ex7_news_list_item3, newsList);
    }

    private SimpleAdapter getSimpleAdapter() {
        return new SimpleAdapter(
                this,
                dataList,
                R.layout.lv_news_list_item_2,
                new String[]{NEWS_TITLE, NEWS_AUTHOR},   //将Map中对应的value值绑定到对应的控件中
                new int[]{R.id.tv_news_item2_title, R.id.tv_news_item2_author});
    }

    /**
     * 只适用于Item中只有一个TextView
     */
    private ArrayAdapter getArrayAdapter() {
        return new ArrayAdapter<String>(
                this, R.layout.lv_news_list_item, R.id.tv_news_item, titles);
    }
}
