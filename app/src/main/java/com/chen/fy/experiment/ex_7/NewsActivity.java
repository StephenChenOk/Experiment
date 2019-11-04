package com.chen.fy.experiment.ex_7;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.chen.fy.experiment.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class NewsActivity extends AppCompatActivity {

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private NewsAdapter mNewsAdapter;

    private String[] mTitles;
    private String[] mAuthors;
    private String[] mContent;
    private TypedArray mImages;

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

        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refreshData();
                        mSwipeRefreshLayout.setRefreshing(false); //取消刷新动作并隐藏控件
                    }
                }
        );

        ListView listView = findViewById(R.id.lv_news_list);

        mNewsAdapter = new NewsAdapter(this, R.layout.ex7_news_list_item3, newsList);

        mNewsAdapter.setOnItemDeleteListener(new NewsAdapter.OnItemDeleteListener() {
            @Override
            public void onDelete(int itemId) {
                Toast.makeText(NewsActivity.this,"删除Item...",Toast.LENGTH_LONG).show();
                removeData(itemId);   //回调删除Item
            }
        });

        listView.setAdapter(mNewsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent =new Intent(NewsActivity.this, NewsContentActivity.class);
                intent.putExtra("newsContent",newsList.get(position).getContent());
                startActivity(intent);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab_add_ex7);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NewsActivity.this,"添加一条记录...",Toast.LENGTH_LONG).show();
                refreshData();
            }
        });
    }

    /**
     * 删除Item
     */
    private void removeData(int id) {
        newsList.remove(id);
        mNewsAdapter.notifyDataSetChanged();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        int length;
        mTitles = getResources().getStringArray(R.array.titles);
        mAuthors = getResources().getStringArray(R.array.authors);
        mContent = getResources().getStringArray(R.array.content);

        mImages = getResources().obtainTypedArray(R.array.images);

        if (mTitles.length > mAuthors.length) {
            length = mAuthors.length;
        } else {
            length = mTitles.length;
        }

        for (int i = 0; i < length; i++) {
           News news = new News();
           news.setTitle(mTitles[i]);
           news.setAuthor(mAuthors[i]);
           news.setContent(mContent[i]);
           news.setImageId(mImages.getResourceId(i,0));

           newsList.add(news);
        }
    }

    /**
     * 刷新界面，新增一条item记录
     */
    private void refreshData() {
        Random random = new Random();
        int index = random.nextInt(7);

        News news = new News();
        news.setTitle(mTitles[index]);
        news.setAuthor(mAuthors[index]);
        news.setContent(mContent[index]);
        news.setImageId(mImages.getResourceId(index, -1));

        mNewsAdapter.insert(news,0);        //插入数据
        mNewsAdapter.notifyDataSetChanged();      //通知Adapter数据发生更新，进行重绘ItemView
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
                this, R.layout.lv_news_list_item, R.id.tv_news_item, mTitles);
    }
}
