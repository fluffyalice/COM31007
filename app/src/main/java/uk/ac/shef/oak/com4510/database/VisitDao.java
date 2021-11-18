package uk.ac.shef.oak.com4510.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;

import com.google.android.libraries.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import uk.ac.shef.oak.com4510.typeConverter.LocationListConverter;

/**
 * Data Access Object for Visits entity
 *
 */
@Dao
@TypeConverters({LocationListConverter.class})
public interface VisitDao {

    @Insert
    long insert(Visit visit);

    @Query("UPDATE visits SET visitTitle = :visitTitle WHERE id = :id")
    int update(final long id, final String visitTitle);

    @Query("UPDATE visits SET  latLngList = :latLngList WHERE id = :id")
    void update(final long id, final ArrayList<LatLng> latLngList);


    @Query("SELECT visitTitle FROM visits WHERE id = :id")
    String getVisitTitle(final long id);

//    @Query("SELECT visits.*, photos.file_path AS file_path, COUNT(photos.file_path) AS imageCount" +
//            " FROM visits LEFT JOIN photos ON visits.id = photos.visitId " +
//            "GROUP BY visits.id ORDER BY visits.date DESC")
//    LiveData<List<Visit>> getAllVisits();

    @Query("SELECT * FROM visits WHERE id = :id")
    LiveData<Visit> getLiveVisit(final long id);

    @Delete
    int delete(Visit visit);
}
