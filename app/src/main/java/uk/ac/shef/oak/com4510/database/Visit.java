package uk.ac.shef.oak.com4510.database;

import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.room.PrimaryKey;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.google.android.libraries.maps.model.LatLng;
import com.google.auto.value.AutoValue;
import com.google.auto.value.AutoValue.CopyAnnotations;

import java.util.ArrayList;
import java.util.Date;

import uk.ac.shef.oak.com4510.typeConverter.LocationListConverter;

/**
 *  Visit entity and model for the APP
 */
@AutoValue
@Entity(tableName = "visits")
@TypeConverters(LocationListConverter.class)
public abstract class Visit implements Parcelable {

    @CopyAnnotations
    @PrimaryKey(autoGenerate = true)
    public abstract int id();

    public abstract String visitTitle();

    public abstract Date date();

    @Nullable
    public abstract ArrayList<LatLng> latLngList();

    public static Visit create(
            int id,
            String visitTitle,
            Date date,
            ArrayList<LatLng> latLngList
    ) {
        return new AutoValue_Visit(id, visitTitle, date, latLngList);
    }




}
