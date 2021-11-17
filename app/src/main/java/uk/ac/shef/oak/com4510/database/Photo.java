package uk.ac.shef.oak.com4510.database;

import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.android.libraries.maps.model.LatLng;
import com.google.auto.value.AutoValue;
import com.google.auto.value.AutoValue.CopyAnnotations;

import java.util.ArrayList;
import java.util.Date;

import uk.ac.shef.oak.com4510.typeConverter.LocationConverter;

/**
 *  Photo entity and model for the APP
 */
@AutoValue
@Entity(tableName = "photos",
        indices = @Index("visitId"),
        foreignKeys = @ForeignKey(
                entity = Visit.class,
                parentColumns = "id",
                childColumns = "visitId",
                onDelete = ForeignKey.CASCADE
        )
)
@TypeConverters(LocationConverter.class)
public abstract class Photo implements Parcelable {

    @CopyAnnotations
    @PrimaryKey(autoGenerate = true)
    public abstract int photoId();

    @CopyAnnotations
    public abstract int visitId();

    @CopyAnnotations
    public abstract String visitTitle();

    @CopyAnnotations
    public abstract Date date();

    @Nullable
    @CopyAnnotations
    public abstract LatLng location();

//    @Nullable
//    @CopyAnnotations
//    public abstract float[] sensors();

    public static Photo create(
            int photoId,
            int visitId,
            String visitTitle,
            Date date,
            LatLng location
    ) {
        return new AutoValue_Photo(photoId, visitId, visitTitle, date, location);
    }



}
