package uk.ac.shef.oak.com4510.mydatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * DatabaseConfig.java
 * @author Feng Li, Ruiqing Xu
 */

@Database(entities = {CacheEntity.class}, version = 1, exportSchema = false)
public abstract class DatabaseConfig extends RoomDatabase {
    public abstract CacheDao mCacheDao();
}