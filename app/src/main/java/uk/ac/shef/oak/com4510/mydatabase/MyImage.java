package uk.ac.shef.oak.com4510.mydatabase;

/**
 * MyImage.java
 * @author Feng Li, Ruiqing Xu
 */

public class MyImage {

    public String imageUrl;
    public String latitude;
    public String longitude;

    public double getDoulbeLatitude()
    {
        if(latitude==null)
            return 0d;
       return Double.parseDouble(latitude);
    }

    public double getDoulbeLongitude()
    {
        if(longitude==null)
            return 0d;
        return Double.parseDouble(longitude);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}