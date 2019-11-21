package com.chen.fy.experiment.ex_11;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.LongDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chen.fy.experiment.MainActivity;
import com.chen.fy.experiment.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 音乐播放器主活动
 */
public class MusicActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String DATA_URI = "com.chen.fy.data_uri";
    private static final String TITLE = "com.chen.fy.title";
    private static final String ARTIST = "com.chen.fy.artist";
    private final int REQUEST_EXTERNAL_STORAGE = 1;
    public static final int UPDATE_PROGRESS = 11;
    public static final String ACTION_MUSIC_START = "com.chen.fy.action_music_start";
    public static final String ACTION_MUSIC_STOP = "com.chen.fy.action_music_stop";

    // ContentResolver.query ⽅法中的 selection 参数及 selectionArgs 参数
    private final String SELECTION =
            MediaStore.Audio.Media.IS_MUSIC + " = ? " + " AND " +
                    MediaStore.Audio.Media.MIME_TYPE + " LIKE ? ";
    private final String[] SELECTION_ARGS = {
            Integer.toString(1),      //表示音频文件是否属于音乐类型的元数据字段
            "audio/mpeg"                //表示音频文件的MIME类型
    };

    private ContentResolver mContentResolver;
    private ListView mMusicList;
    private CursorAdapter mCursorAdapter;

    private BottomNavigationView navigation;
    private ProgressBar mProgressBar;
    private ImageView ivThumbnail;
    private TextView tvTitle;
    private TextView tvArtist;
    private ImageView ivPlay;

    private boolean mPlayerState = false;

    private Cursor mCursor;

    private MusicService mMusicService;
    //服务是否已经与活动进行了绑定
    private boolean mBound = false;

    private MusicReceiver mMusicReceiver;

    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicServiceBinder binder = (MusicService.MusicServiceBinder) service;
            mMusicService = binder.getService();
            mBound = true;
        }

        /**
         * 在连接正常关闭时不会调用，只有在Service在被异常地破坏了或者杀死后才会调用此放啊
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMusicService = null;
            mBound = false;
        }
    };

    /**
     * 进度条更新Handler
     * 当在子线程中监听到进度条发生改变时，发送消息给此Handler进行UI更新
     */
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_PROGRESS:
                    int position = msg.arg1;
                    mProgressBar.setProgress(position);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 开启一个子线程时刻监听歌曲播放情况，并随时更新进度条
     */
    private class MusicProgressRunnable implements Runnable {

        @Override
        public void run() {
            boolean mThreadWorking = true;
            while (mThreadWorking) {
                try {
                    if (mMusicService != null) {
                        Message message = new Message();
                        message.what = UPDATE_PROGRESS;
                        message.arg1 = mMusicService.getCurrentPosition();
                        mHandler.sendMessage(message);
                    }
                    mThreadWorking = mMusicService.isPlaying();
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();

                }
            }
        }
    }

    /**
     * 1 定义一个广播，实现当音乐开始播放时会发送广播以开启子线程随时监听并更新歌曲进度条
     * 2 之所以要使用广播进行进度条的更新，是因为进度条更新需要在子线程中进行，所以无法控制当开始读取进度
     *   条时歌曲是否已经准备好并已经开始播放，所以使用广播当歌曲开始播放时发送广播，这边接收到后开始
     *   更新进度条，避免了空指针的可能性出现
     */
    private class MusicReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (mMusicService != null) {
                mProgressBar.setMax(mMusicService.getDuration());
                new Thread(new MusicProgressRunnable()).start();
            }
        }
    }

    /**
     * 歌曲Item的点击事件
     * 点击某个歌曲后，开启前台服务进行歌曲播放，同时在活动中进行播放栏状态的更新
     */
    private ListView.OnItemClickListener mItemClickListener =
            new ListView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Cursor cursor = mCursorAdapter.getCursor();
                    if (cursor != null && cursor.moveToPosition(position)) { //将游标往后移动
                        //1 获取歌曲的信息
                        int titleIndex = cursor.getColumnIndex(
                                MediaStore.Audio.Media.TITLE);
                        int artistIndex = cursor.getColumnIndex(
                                MediaStore.Audio.Media.ARTIST);
                        int albumIdIndex = cursor.getColumnIndex(
                                MediaStore.Audio.Media.ALBUM_ID); //专辑ID，查找到专辑的封面图
                        int dataIndex = cursor.getColumnIndex(
                                MediaStore.Audio.Media.DATA); //Uri路径

                        String data = cursor.getString(dataIndex);
                        String title = cursor.getString(titleIndex);
                        String artist = cursor.getString(artistIndex);
                        long albumId = cursor.getLong(albumIdIndex);

                        if (mMusicService != null) {
                            mMusicService.prepareMusic(data, title, artist);
                        }

                        //4 更新歌曲控制栏
                        navigation.setVisibility(View.VISIBLE);
                        if (tvTitle != null) {
                            tvTitle.setText(title);
                        }
                        if (tvArtist != null) {
                            tvArtist.setText(artist);
                        }
                        Uri albumUri = ContentUris.withAppendedId(    //专辑信息地址
                                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, albumId);
                        Cursor albumCursor = mContentResolver.query(
                                albumUri, null, null,
                                null, null);
                        if (albumCursor != null && albumCursor.getCount() > 0) {
                            albumCursor.moveToFirst();
                            int albumArtIndex = albumCursor.getColumnIndex(
                                    MediaStore.Audio.Albums.ALBUM_ART);  //专辑封面图
                            String albumArt = albumCursor.getString(albumArtIndex);
                            Glide.with(MusicActivity.this).load(albumArt).into(ivThumbnail);
                            albumCursor.close();
                        }
                    }
                }
            };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ex11_music_layout);

        //动态申请危险权限
        applyPermission();

        initView();
        initMusicList();
        initReceiver();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //创建播放器对象
        Intent intent = new Intent(MusicActivity.this, MusicService.class);
        bindService(intent, mConn, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        unbindService(mConn);
        mBound = false;
        unregisterReceiver(mMusicReceiver);
        super.onDestroy();
    }

    private void initView() {

        mMusicList = findViewById(R.id.lv_music_list);
        mContentResolver = getContentResolver();
        mCursorAdapter = new MediaCursorAdapter(this, null, false);
        mMusicList.setAdapter(mCursorAdapter);
        mMusicList.setOnItemClickListener(mItemClickListener);

        //初始化音乐控制栏
        navigation = findViewById(R.id.navigation);
        LayoutInflater.from(this).inflate(R.layout.music_navigation_layout, navigation, true);

        //获取控制栏中的控件对象
        mProgressBar = navigation.findViewById(R.id.pb_navigation);
        ivThumbnail = navigation.findViewById(R.id.iv_thumbnail);
        tvTitle = navigation.findViewById(R.id.tv_bottom_title);
        tvArtist = navigation.findViewById(R.id.tv_bottom_artist);
        ivPlay = navigation.findViewById(R.id.iv_play);

        if (ivPlay != null) {
            ivPlay.setOnClickListener(this);
        }

        navigation.setVisibility(View.GONE);
    }

    /**
     * 查询歌曲信息
     */
    private void initMusicList() {
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
     * 广播初始化
     */
    private void initReceiver() {
        mMusicReceiver = new MusicReceiver();
        IntentFilter intentFilter = new IntentFilter();
        //增加广播过滤器
        intentFilter.addAction(ACTION_MUSIC_START);
        intentFilter.addAction(ACTION_MUSIC_STOP);
        //注册广播
        registerReceiver(mMusicReceiver, intentFilter);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_play:
                mPlayerState = !mPlayerState;
                if (mPlayerState) {
                    mMusicService.play();
                    ivPlay.setImageResource(R.drawable.ic_pause_circle_outline_32dp);
                } else {
                    mMusicService.pause();
                    ivPlay.setImageResource(R.drawable.ic_play_circle_outline_32dp);
                }
                break;
        }
    }
}
