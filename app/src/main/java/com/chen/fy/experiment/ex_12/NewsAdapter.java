package com.chen.fy.experiment.ex_12;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chen.fy.experiment.R;

import java.util.List;

public class NewsAdapter extends ArrayAdapter {

    private Context mContext;
    private int mResourceId;
    private List<News> mNewsData;

    public NewsAdapter(@NonNull Context context, int resourceId, @NonNull List<News> newsData) {
        super(context, resourceId, newsData);
        this.mContext = context;
        this.mResourceId = resourceId;
        this.mNewsData = newsData;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        News news = (News) getItem(position);
        final View view;
        ViewHolder viewHolder;
        if (convertView == null) {      //判断缓冲池是否已经有view ,若有则可以直接用,不需要再继续反射
            view = LayoutInflater.from(getContext()).inflate(mResourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvTitle = view.findViewById(R.id.tv_title_ex12);
            viewHolder.tvSubtitle = view.findViewById(R.id.tv_subtitle_ex12);
            viewHolder.ivImage = view.findViewById(R.id.iv_image_ex12);
            viewHolder.ivDelete = view.findViewById(R.id.iv_delete_ex12);
            viewHolder.tvPublishTime = view.findViewById(R.id.tv_publish_time_ex12);

            view.setTag(viewHolder);
        } else {    //若缓冲池中已经有view则可以直接用holder对象
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tvTitle.setText(news.getTitle());
        viewHolder.tvSubtitle.setText(news.getSource());
        viewHolder.ivDelete.setTag(position);
        viewHolder.tvPublishTime.setText(news.getPublishTime());

        Glide.with(mContext).load(news.getPicUrl()).into(viewHolder.ivImage);
        return view;
    }

    //创建一个内部类,放着要显示的view控件,通过实例化这个类,把其对象一起放到view中
    class ViewHolder {
        TextView tvTitle;
        TextView tvSubtitle;
        ImageView ivImage;
        ImageView ivDelete;
        TextView tvPublishTime;
    }
}
