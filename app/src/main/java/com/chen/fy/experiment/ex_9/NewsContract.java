package com.chen.fy.experiment.ex_9;

import android.provider.BaseColumns;

/**
 * 合约类，包含News数据表的数据信息
 */
public final class NewsContract {
    //private 避免实例化该类
    private NewsContract(){}

    public static class NewsEntry implements BaseColumns{ //实现此接口以获得到行的唯一标识ID
        public static final String TABLE_NAME = "tb_news";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_AUTHOR= "author";
        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_NAME_IMAGE = "image";
    }
}
