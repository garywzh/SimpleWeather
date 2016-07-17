package xyz.garywzh.simpleweather.model;

import com.tencent.map.geolocation.TencentLocation;

/**
 * Created by garywzh on 2016/7/17.
 */
public class DataRepo {
    private TencentLocation location;
    private Forecast forecast;

    public DataRepo() {
    }

    public DataRepo(TencentLocation location, Forecast forecast) {
        this.location = location;
        this.forecast = forecast;
    }

    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }

    public void setLocation(TencentLocation location) {
        this.location = location;
    }

    public Forecast getForecast() {
        return forecast;
    }

    public TencentLocation getLocation() {
        return location;
    }
}
