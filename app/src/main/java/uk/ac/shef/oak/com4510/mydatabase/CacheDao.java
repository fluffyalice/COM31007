package uk.ac.shef.oak.com4510.mydatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CacheDao {
    @Insert
    void insertCaches(CacheEntity... cacheEntities);

    @Update
    void updateCaches(CacheEntity... cacheEntities);

    @Query("SELECT * FROM cache WHERE `key` = :key LIMIT 0,1")
    CacheEntity findByKey(String key);

    @Delete
    void deleteCaches(CacheEntity... cacheEntities);

    @Query("SELECT * FROM cache")
    CacheEntity[] findAll();
}