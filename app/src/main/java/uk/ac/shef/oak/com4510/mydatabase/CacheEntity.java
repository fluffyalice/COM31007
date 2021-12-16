package uk.ac.shef.oak.com4510.mydatabase;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity(tableName = "cache")
public class CacheEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String key;
    public String title;
    public long startTime;
    public long stopTime;
    public String temperature;
    public String pressure;

    //ROOM不支持直接存储集合

    public String alists;

    public String imagebean;


    public List<MyImage> getImagebean() {

        Gson g = new Gson();
        List<MyImage> ps = g.fromJson(imagebean, new TypeToken<List<MyImage>>() {
        }.getType());

        if(ps==null)ps=new ArrayList<>();

        return ps;
    }

    public void addImagebean(String imageUrl, String latitude, String longitude) {

        List<MyImage> persons = getImagebean();
        if (persons == null)
            persons = new ArrayList<>();

        Gson gson = new Gson();
        MyImage p = new MyImage();
        p.imageUrl = imageUrl;
        p.latitude = latitude;
        p.longitude = longitude;
        persons.add(p);
        String str = gson.toJson(persons);

        imagebean = str;
    }

    public ArrayList<MyLatLng> getAlists() {

        if(alists==null||alists.length()==0)
            return new ArrayList<MyLatLng>();

        Gson g = new Gson();
        JsonObject obj = g.fromJson(alists, JsonObject.class);
        ArrayList<MyLatLng> amls = new ArrayList<MyLatLng>();

        double latitude = 0d;
        double longitude = 0d;
        for (Map.Entry<String, JsonElement> set : obj.entrySet()) {//通过遍历获取key和value

            latitude = Double.parseDouble(set.getKey());
            longitude = set.getValue().getAsDouble();
            MyLatLng myLatLng = new MyLatLng(latitude, longitude);
            amls.add(myLatLng);
        }


        return amls;
    }

    public void setAlists(ArrayList<MyLatLng> values) {

        JsonObject msgObj = new JsonObject();

        for (int n = 0; n < values.size(); n++) {
            msgObj.addProperty(values.get(n).latitude + "", values.get(n).longitude);
        }

        String msgStr = msgObj.toString();

        alists = msgStr;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getStopTime() {
        return stopTime;
    }

    public void setStopTime(long stopTime) {
        this.stopTime = stopTime;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }


}