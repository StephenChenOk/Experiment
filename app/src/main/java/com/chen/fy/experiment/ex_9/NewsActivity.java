package com.chen.fy.experiment.ex_9;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.chen.fy.experiment.R;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private CursorAdapter mCursorAdapter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ex7_news_layout);

        ListView listView = findViewById(R.id.lv_news_list);

        //1 实例化NewsCursorAdapter
        mCursorAdapter = new NewsCursorAdapter(this, null, false);

        //2 进行初始化并移除所有监听事件
        mCursorAdapter.swapCursor(null);

        //3 绑定数据
        listView.setAdapter(mCursorAdapter);

        //4 使用Loader在后台进行数据查询加载
        getSupportLoaderManager().initLoader(0,null,this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new NewsQueryAsyncCursorLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);     //查询完成后，更新新的数据集Cursor，通知重绘ItemView
        mCursorAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) { }
}
