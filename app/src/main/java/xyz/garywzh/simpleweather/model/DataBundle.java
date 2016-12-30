package xyz.garywzh.simpleweather.model;

/**
 * Created by garywzh on 2016/7/17.
 */
public class DataBundle {

    private SimpleLocation location;
    private Forecast forecast;

    public DataBundle() {
    }

    public DataBundle(SimpleLocation location, Forecast forecast) {
        this.location = location;
        this.forecast = forecast;
    }

    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }

    public void setLocation(SimpleLocation location) {
        this.location = location;
    }

    public Forecast getForecast() {
        return forecast;
    }

    public SimpleLocation getLocation() {
        return location;
    }
}
