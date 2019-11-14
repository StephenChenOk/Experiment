package com.chen.fy.experiment.ex_10;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.chen.fy.experiment.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面
 */
public class MusicActivity extends AppCompatActivity {

    private final int REQUEST_EXTERNAL_STORAGE = 1;

    private ContentResolver mContentResolver;
    private ListView mMusicList;
    private CursorAdapter mCursorAdapter;

    // ContentResolver.query ⽅法中的 selection 参数及 selectionArgs 参数
    private final String SELECTION =
            MediaStore.Audio.Media.IS_MUSIC + " = ? " + " AND " +
                    MediaStore.Audio.Media.MIME_TYPE + " LIKE ? ";
    private final String[] SELECTION_ARGS = {
            Integer.toString(1),      //表示音频文件是否属于音乐类型的元数据字段
            "audio/mpeg"                //表示音频文件的MIME类型
    };
    private Cursor mCursor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ex10_music_layout);

        //动态申请危险权限
        applyPermission();

        initView();
        initMusicList();
    }

    private void initView() {
        mMusicList = findViewById(R.id.lv_music_list);
        mContentResolver = getContentResolver();
        mCursorAdapter = new MediaCursorAdapter(this, null, false);
        mMusicList.setAdapter(mCursorAdapter);
    }

    /**
     * 查询歌曲信息
     */
    private void initMusicList(){
        //获取本地音乐数据游标
        mCursor = mContentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,  //查询MediaStore中保存在外部存储设备中的多媒体音频文件
                null,                              //查询的字段（查询所有字段）
                SELECTION,                                   //查询语句对应的where字句
                SELECTION_ARGS,                              //与selection一起构成where字句
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER    //查询结果的排序条件
        );
        //设置游标，并通知Adapter数据已经更新
        mCursorAdapter.swapCursor(mCursor);
        mCursorAdapter.notifyDataSetChanged();
    }

    /**
     * 动态申请危险权限
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void applyPermission() {
        //权限集合
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MusicActivity.this, Manifest.permission.
                WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(MusicActivity.this, Manifest.permission.
                READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {  //如果有权限没有被授权,则请求权限
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MusicActivity.this,
                    permissions, REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(MusicActivity.this, "必须同意所有权限才可以使用本程序!", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                } else {
                    Toast.makeText(MusicActivity.this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }
}
