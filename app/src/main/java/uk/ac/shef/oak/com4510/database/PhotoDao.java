package uk.ac.shef.oak.com4510.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;

import java.util.List;

import uk.ac.shef.oak.com4510.typeConverter.LocationConverter;

/**
 * Data Acesss Object for Photos entity
 *
 */
@Dao
@TypeConverters({LocationConverter.class})
public interface PhotoDao {

    @Insert
    void insert(Photo image);

    @Delete
    void delete(Photo image);

    @Query("DELETE FROM photos")
    void deleteAll();

    @Query("SELECT * FROM photos")
    List<Photo> getAllPhotos();

//    @Query("SELECT * FROM photos ORDER BY date DESC")
//    LiveData<List<Photo>> getAllDesc();
//
//    @Query("SELECT * FROM photos ORDER BY date ASC")
//    LiveData<List<Photo>> getAllAsc();
//
//    @Query("SELECT * FROM photos WHERE id IN (:ids)")
//    LiveData<List<Photo>> loadAllByIds(int[] ids);
//
//    @Query("SELECT * FROM photos WHERE date LIKE :date")
//    LiveData<List<Photo>> findByDate(final Date date);
//
//    @Query("SELECT * FROM photos WHERE visitId=:visitId")
//    LiveData<List<Photo>> findByVisit(final int visitId);
}
