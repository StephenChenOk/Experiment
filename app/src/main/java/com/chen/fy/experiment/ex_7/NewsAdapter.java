package com.chen.fy.experiment.ex_7;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        News news = (News) getItem(position);

        View view = LayoutInflater.from(getContext()).inflate(mResourceId,parent,false);

        TextView tvTitle = view.findViewById(R.id.tv_item3_title);
        TextView tvAuthor = view.findViewById(R.id.tv_item3_subtitle);
        ImageView imageView = view.findViewById(R.id.iv_item3_image);

        tvTitle.setText(news.getTitle());
        tvAuthor.setText(news.getAuthor());
        imageView.setImageResource(news.getImageId());

        return view;
    }
}
