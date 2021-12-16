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

    @Query("SELECT * FROM cache order by id desc")
    CacheEntity[] findAll();

    @Query("SELECT * FROM cache order by id desc")
    CacheEntity[] findAllByDesc();

    @Query("SELECT * FROM cache order by id  asc")
    CacheEntity[] findAllByAsc();


    @Query("SELECT * FROM cache  WHERE `title` LIKE :title")
    CacheEntity[] findByTitle(String title);
}