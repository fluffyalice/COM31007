package uk.ac.shef.oak.com4510.mydatabase;

import android.content.Context;

import androidx.room.Room;

public class DatabaseInitialize {
    private static DatabaseConfig databaseConfig;

    public DatabaseInitialize(){}

    public static void init(Context context) {
        // 生成数据库实例
        databaseConfig = Room
                .databaseBuilder(context.getApplicationContext(), DatabaseConfig.class, "huiminyougou")
                .allowMainThreadQueries() // 允许主线程中查询
                .build();
    }

    static DatabaseConfig getDatabaseConfig() {
        if (databaseConfig == null) {
            throw new NullPointerException("DatabaseInitialize.init(context) has not call, remember call this function in your Application.class");
        }
        return databaseConfig;
    }
}