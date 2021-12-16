package uk.ac.shef.oak.com4510.ui.map;

/**
 * Sys.java
 *
 * @author Feng Li, Ruiqing Xu
 *
 */
public class Sys {

    private int type;
    private int id;
    private String country;
    private long sunrise;
    private long sunset;
    public void setType(int type) {
        this.type = type;
    }
    public int getType() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    public String getCountry() {
        return country;
    }

    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }
    public long getSunrise() {
        return sunrise;
    }

    public void setSunset(long sunset) {
        this.sunset = sunset;
    }
    public long getSunset() {
        return sunset;
    }

}