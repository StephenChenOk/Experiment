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

import java.util.Arrays;
import java.util.List;

public class NewsAdapter extends ArrayAdapter {

    private Context mContext;
    private int mResourceId;
    private List<News> mNewsData;

    private OnItemDeleteListener listener = null;


    public NewsAdapter(@NonNull Context context, int resourceId, @NonNull List<News> newsData) {
        super(context, resourceId, newsData);
        this.mContext = context;
        this.mResourceId = resourceId;
        this.mNewsData = newsData;
    }

    /**
     * 外部传入接口
     */
    public void setOnItemDeleteListener(OnItemDeleteListener listener){
        this.listener = listener;
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
            viewHolder.tvTitle = view.findViewById(R.id.tv_item_ex7_title);
            viewHolder.tvAuthor = view.findViewById(R.id.tv_item_ex7_author);
            viewHolder.imageView = view.findViewById(R.id.iv_item_ex7_image);
            viewHolder.ivDelete = view.findViewById(R.id.iv_delete_ex7);

            view.setTag(viewHolder);
        } else {    //若缓冲池中已经有view则可以直接用holder对象
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tvTitle.setText(news.getTitle());
        viewHolder.tvAuthor.setText(news.getAuthor());
        viewHolder.imageView.setImageResource(news.getImageId());
        viewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //使用接口回调进行删除
                if(listener != null){
                    listener.onDelete(position);
                }
            }
        });
        return view;
    }

    //创建一个内部类,放着要显示的view控件,通过实例化这个类,把其对象一起放到view中
    class ViewHolder {
        TextView tvTitle;
        TextView tvAuthor;
        ImageView imageView;
        ImageView ivDelete;
    }

    /**
     * 点击删除接口
     */
    public interface OnItemDeleteListener{
        void onDelete(int itemId);
    }
}
