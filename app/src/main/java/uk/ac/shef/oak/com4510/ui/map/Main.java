package uk.ac.shef.oak.com4510.ui.map;

import java.math.BigDecimal;

/**
 * Main.java
 * @author Feng Li, Ruiqing Xu
 */

public class Main {

    private double temp;
    private double feels_like;
    private double temp_min;
    private double temp_max;
    private int pressure;
    private int humidity;
    public void setTemp(double temp) {
         this.temp = temp;
     }
     public double getTemp() {
         BigDecimal b1 = new BigDecimal(Double.toString(temp));
         BigDecimal b2 = new BigDecimal(Double.toString(273.15));
         return b1.subtract(b2).doubleValue();
     }

    public void setFeels_like(double feels_like) {
         this.feels_like = feels_like;
     }
     public double getFeels_like() {
         return feels_like;
     }

    public void setTemp_min(double temp_min) {
         this.temp_min = temp_min;
     }
     public double getTemp_min() {
         return temp_min;
     }

    public void setTemp_max(double temp_max) {
         this.temp_max = temp_max;
     }
     public double getTemp_max() {
         return temp_max;
     }

    public void setPressure(int pressure) {
         this.pressure = pressure;
     }
     public int getPressure() {
         return pressure;
     }

    public void setHumidity(int humidity) {
         this.humidity = humidity;
     }
     public int getHumidity() {
         return humidity;
     }

}