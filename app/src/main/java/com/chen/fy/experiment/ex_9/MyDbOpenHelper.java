package com.chen.fy.experiment.ex_9;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.chen.fy.experiment.R;

/**
 * 进行数据库初始化操作
 */
public class MyDbOpenHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_ENTRIES = "create table " + NewsContract.NewsEntry.TABLE_NAME
            + " (" + NewsContract.NewsEntry._ID + " integer primary key, " + NewsContract.NewsEntry.COLUMN_NAME_TITLE
            + " varchar(200), " + NewsContract.NewsEntry.COLUMN_NAME_AUTHOR + " varchar(100), " +
            NewsContract.NewsEntry.COLUMN_NAME_CONTENT + " text, " + NewsContract.NewsEntry.COLUMN_NAME_IMAGE
            + " varchar(100) " + ")";

    private static final String SQL_DELETE_ENTRIES = "drop table if exists " + NewsContract.NewsEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "news.db";

    private Context mContext;

    public MyDbOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);  //执行SQL语句创建/使用数据表
        initDb(db);
    }


    /**
     * 当数据库结构发生改变时，调用方法，同时改变数据库版本
     * 需要手动改变版本号数据库才会进行更新
     *
     * @param db         改变的数据库
     * @param oldVersion 老版本
     * @param newVersion 新版本
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);   //重新加载数据库
    }

    /**
     * 将strings资源文件中的数据存到数据库中
     *
     * @param db 存入的数据库
     */
    private void initDb(SQLiteDatabase db) {
        Resources resources = mContext.getResources();
        String[] titles = resources.getStringArray(R.array.titles);
        String[] authors = resources.getStringArray(R.array.authors);
        String[] contents = resources.getStringArray(R.array.content);
        TypedArray images = resources.obtainTypedArray(R.array.images);

        int length = 0;
        length = Math.min(titles.length, authors.length);
        length = Math.min(length, contents.length);
        length = Math.min(length, images.length());

        for (int i = 0; i < length; i++) {
            ContentValues values = new ContentValues();    //用于数据库储存的键值对
            values.put(NewsContract.NewsEntry.COLUMN_NAME_TITLE, titles[i]);
            values.put(NewsContract.NewsEntry.COLUMN_NAME_AUTHOR, authors[i]);
            values.put(NewsContract.NewsEntry.COLUMN_NAME_CONTENT, contents[i]);
            values.put(NewsContract.NewsEntry.COLUMN_NAME_IMAGE, images.getString(i));
            db.insert(NewsContract.NewsEntry.TABLE_NAME, null, values); //执行SQL的插入语句
        }
    }
}
