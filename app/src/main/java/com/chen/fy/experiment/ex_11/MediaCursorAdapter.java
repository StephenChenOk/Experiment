package com.chen.fy.experiment.ex_11;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.chen.fy.experiment.R;

/**
 * 适配器
 */
public class MediaCursorAdapter extends CursorAdapter {

    private Context mContent;
    private LayoutInflater mLayoutInflater;

    public MediaCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);

        mContent = context;
        mLayoutInflater = LayoutInflater.from(mContent);
    }

    /**
     * 主要做项视图的加载操作
     * 当ListView控件响应用户上下滑动时，新出现的项需要加载项布局时调用
     * @param cursor    ListView当前项的游标对象
     * @param parent    表示项视图布局的父容器（这里指ListView控件）
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View itemView = mLayoutInflater.inflate(R.layout.ex11_list_item,parent,false);

        if(itemView!=null){
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.tvOrder = itemView.findViewById(R.id.tv_order);
            viewHolder.tvTitle = itemView.findViewById(R.id.tv_title);
            viewHolder.tvArtist = itemView.findViewById(R.id.tv_artist);
            viewHolder.divider = itemView.findViewById(R.id.divider);
            itemView.setTag(viewHolder);
        }
        return itemView;
    }

    /**
     * 负责从已加载的布局缓存中重新绑定新数据，显示数据
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        int titleIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
        int artistIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);

        String title = cursor.getString(titleIndex);
        String artist = cursor.getString(artistIndex);

        int position = cursor.getPosition();

        if(viewHolder != null){
            viewHolder.tvOrder.setText(Integer.toString(position+1));
            viewHolder.tvTitle.setText(title);
            viewHolder.tvArtist.setText(artist);
        }
    }

    private class ViewHolder{
        private TextView tvOrder;
        private TextView tvTitle;
        private TextView tvArtist;
        private View divider;
    }
}
