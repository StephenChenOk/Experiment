package com.chen.fy.experiment.ex_9;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;

/**
 * 在后台对数据库进行操作
 */
public class NewsQueryAsyncCursorLoader extends CursorLoader {

    private Context mContext;

    public NewsQueryAsyncCursorLoader(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();   //强制执行loadInBackground（）方法
    }

    /**
     * 在子线程中执行耗时操作
     */
    @Override
    public Cursor loadInBackground() {
        MyDbOpenHelper dbOpenHelper = new MyDbOpenHelper(mContext);
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(
                NewsContract.NewsEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                NewsContract.NewsEntry._ID +" DESC");  //降序
        return cursor;
    }
}
