package com.chen.fy.experiment.ex_9;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.chen.fy.experiment.MainActivity;
import com.chen.fy.experiment.R;
import com.chen.fy.experiment.ex_7.News;
import com.chen.fy.experiment.ex_7.NewsAdapter;

import org.w3c.dom.ls.LSException;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ex7_news_layout);

        MyDbOpenHelper myDbOpenHelper = new MyDbOpenHelper(this);
        SQLiteDatabase db = myDbOpenHelper.getReadableDatabase();   //获取只读数据库

        List<News> newsList = queryDbData(db);
        NewsAdapter newsAdapter = new NewsAdapter(
                this, R.layout.ex7_news_list_item3, newsList
        );

        ListView listView = findViewById(R.id.lv_news_list);
        listView.setAdapter(newsAdapter);
    }

    /**
     * 查询数据库中的数据并返回数据集List
     *
     * @param db 需要查询的数据库
     * @return 查询到的数据集
     */
    private List<News> queryDbData(SQLiteDatabase db) {
        Cursor cursor = db.query(NewsContract.NewsEntry.TABLE_NAME, null, null,
                null, null, null, null);
        List<News> newsList = new ArrayList<>();
        while (cursor.moveToNext()) {   //如果后面还有数据
            News news = new News();

            String title = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_NAME_TITLE));
            String author = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_NAME_AUTHOR));
            String image = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_NAME_IMAGE));

            Bitmap bitmap = BitmapFactory.decodeStream(getClass().getResourceAsStream("/" + image));   //将字符串数据解析成Bitmap对象

            news.setTitle(title);
            news.setAuthor(author);
            news.setImage(bitmap);
            newsList.add(news);
        }
        cursor.close();
        return newsList;
    }
}
