package uk.ac.shef.oak.com4510.typeConverter;

import androidx.room.TypeConverter;

import com.google.android.libraries.maps.model.LatLng;

/**
 * Type converter for {@link com.google.android.libraries.maps.model.LatLng}
 *
 */
public class LocationConverter {
    /**
     * Convert a LatLng object to comma separated string
     *
     * @param latLng The LatLng object
     * @return String The comma separated string
     */
    @TypeConverter
    public static String fromLatLng(LatLng latLng) {
        return latLng.latitude + "," + latLng.longitude;
    }

    /**
     * Convert a comma separated string to LatLng object
     *
     * @param string The comma separated string
     * @return The LatLng object
     */
    @TypeConverter
    public static LatLng toLatLng(String string) {
        final String[] arr = string.split(",");

        return new LatLng(Double.parseDouble(arr[0]), Double.parseDouble(arr[1]));
    }
}
