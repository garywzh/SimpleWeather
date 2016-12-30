package xyz.garywzh.simpleweather.model;

/**
 * Created by garywzh on 2016/7/22.
 */
public class SimpleLocation {
    public String city;
    public String district;
    public String latitude;
    public String longitude;

    public SimpleLocation(String city, String district, String latitude, String longitude) {
        this.city = city;
        this.district = district;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
