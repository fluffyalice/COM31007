package uk.ac.shef.oak.com4510.mydatabase;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * MediaBeanTypeConverter.java
 * @author Feng Li, Ruiqing Xu
 */

public class MediaBeanTypeConverter {

    Gson gson = new Gson();

    @TypeConverter
    public List<MyLatLng> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<MyLatLng>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public String someObjectListToString(List<MyLatLng> someObjects) {
        return gson.toJson(someObjects);
    }

}