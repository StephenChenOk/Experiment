package com.chen.fy.experiment.ex_9;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chen.fy.experiment.R;

/**
 * 当对SQLite数据库进行数据查询时，需要对Cursor游标进行一个遍历，一步步的取出数据再封装数据，比较繁琐
 * 为避免这个繁琐的事情，我们使用了CursorAdapter进行数据的一个适配，只需要直接传入一个Cursor游标对象进来，
 * 就可以直接的为我们遍历数据，适配数据
 */
public class NewsCursorAdapter extends CursorAdapter {

    private Context mContext;
    private LayoutInflater mInflater;

    public NewsCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    /**
     * 负责新的Item布局的加载
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        final View itemView = mInflater.inflate(
                R.layout.ex7_news_list_item3,
                parent,
                false);

        final ViewHolder holder = new ViewHolder();
        holder.tvTitle = itemView.findViewById(R.id.tv_item_ex7_title);
        holder.tvAuthor = itemView.findViewById(R.id.tv_item_ex7_author);
        holder.imageView = itemView.findViewById(R.id.iv_item_ex7_image);

        itemView.setTag(holder);  //ViewHolder与ItemView绑定

        return itemView;
    }

    /**
     * 负责从已加载的布局缓存中重新绑定新数据，显示数据
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ViewHolder holder = (ViewHolder) view.getTag();   //获取ViewHolder对象，进行数据显示

        final String title = cursor.getString(
                cursor.getColumnIndex(
                        NewsContract.NewsEntry.COLUMN_NAME_TITLE));
        final String author = cursor.getString(
                cursor.getColumnIndex(
                        NewsContract.NewsEntry.COLUMN_NAME_AUTHOR));
        final String imageResource = cursor.getString(
                cursor.getColumnIndex(
                        NewsContract.NewsEntry.COLUMN_NAME_IMAGE));
        final String id = cursor.getString(
                cursor.getColumnIndex(
                        NewsContract.NewsEntry._ID));

        holder.tvTitle.setText(title);
        holder.tvAuthor.setText(author);

        Bitmap bitmap = BitmapFactory.decodeStream(
                getClass().getResourceAsStream("/" + imageResource));
        holder.imageView.setImageBitmap(bitmap);
    }

    //创建一个内部类,放着要显示的view控件,通过实例化这个类,把其对象一起放到view中
    class ViewHolder {
        TextView tvTitle;
        TextView tvAuthor;
        ImageView imageView;
    }
}
