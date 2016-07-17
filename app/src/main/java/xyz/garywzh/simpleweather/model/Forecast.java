package xyz.garywzh.simpleweather.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by garywzh on 2016/7/15.
 */
public class Forecast {

    public double latitude;
    public double longitude;
    public String timezone;
    public int offset;
    /**
     * time : 1468560115
     * summary : Mostly Cloudy
     * icon : partly-cloudy-day
     * precipIntensity : 7.0E-4
     * precipProbability : 0.01
     * precipType : rain
     * temperature : 83.4
     * apparentTemperature : 82.26
     * dewPoint : 54.12
     * humidity : 0.37
     * windSpeed : 4.03
     * windBearing : 169
     * cloudCover : 0.84
     * pressure : 1003.18
     * ozone : 298.99
     */

    public CurrentlyBean currently;

    public HourlyBean hourly;

    public DailyBean daily;
    /**
     * sources : ["gfs","cmc","fnmoc","isd"]
     * isd-stations : ["544290-99999","545230-99999","545290-99999","545340-99999","546230-99999"]
     * units : us
     */

    public FlagsBean flags;

    public static class CurrentlyBean {
        public int time;
        public String summary;
        public String icon;
        public double precipIntensity;
        public double precipProbability;
        public String precipType;
        public double temperature;
        public double apparentTemperature;
        public double dewPoint;
        public double humidity;
        public double windSpeed;
        public int windBearing;
        public double cloudCover;
        public double pressure;
        public double ozone;
    }

    public static class HourlyBean {
        public String summary;
        public String icon;
        /**
         * time : 1468558800
         * summary : Mostly Cloudy
         * icon : partly-cloudy-day
         * precipIntensity : 0
         * precipProbability : 0
         * temperature : 83.02
         * apparentTemperature : 82.01
         * dewPoint : 54.31
         * humidity : 0.37
         * windSpeed : 3.87
         * windBearing : 162
         * cloudCover : 0.86
         * pressure : 1003.29
         * ozone : 297.71
         */

        public java.util.List<DataBean> data;

        public static class DataBean {
            public int time;
            public String summary;
            public String icon;
            public double precipIntensity;
            public double precipProbability;
            public double temperature;
            public double apparentTemperature;
            public double dewPoint;
            public double humidity;
            public double windSpeed;
            public int windBearing;
            public double cloudCover;
            public double pressure;
            public double ozone;
        }
    }

    public static class DailyBean {
        public String summary;
        public String icon;
        /**
         * time : 1468512000
         * summary : Light rain in the morning.
         * icon : rain
         * sunriseTime : 1468529640
         * sunsetTime : 1468582564
         * moonPhase : 0.34
         * precipIntensity : 0.0034
         * precipIntensityMax : 0.0116
         * precipIntensityMaxTime : 1468530000
         * precipProbability : 0.45
         * precipType : rain
         * temperatureMin : 66.03
         * temperatureMinTime : 1468594800
         * temperatureMax : 84.22
         * temperatureMaxTime : 1468566000
         * apparentTemperatureMin : 66.03
         * apparentTemperatureMinTime : 1468594800
         * apparentTemperatureMax : 82.78
         * apparentTemperatureMaxTime : 1468566000
         * dewPoint : 59.44
         * humidity : 0.61
         * windSpeed : 0.84
         * windBearing : 247
         * cloudCover : 0.82
         * pressure : 1003.76
         * ozone : 296.74
         */

        public java.util.List<DataBean> data;

        public static class DataBean {
            public int time;
            public String summary;
            public String icon;
            public int sunriseTime;
            public int sunsetTime;
            public double moonPhase;
            public double precipIntensity;
            public double precipIntensityMax;
            public int precipIntensityMaxTime;
            public double precipProbability;
            public String precipType;
            public double temperatureMin;
            public int temperatureMinTime;
            public double temperatureMax;
            public int temperatureMaxTime;
            public double apparentTemperatureMin;
            public int apparentTemperatureMinTime;
            public double apparentTemperatureMax;
            public int apparentTemperatureMaxTime;
            public double dewPoint;
            public double humidity;
            public double windSpeed;
            public int windBearing;
            public double cloudCover;
            public double pressure;
            public double ozone;
        }
    }

    public static class FlagsBean {

        /**
         * sources : ["gfs","cmc","fnmoc","isd"]
         * isd-stations : ["544290-99999","545230-99999","545290-99999","545340-99999","546230-99999"]
         * units : us
         */

        public String units;
        public java.util.List<String> sources;
        @SerializedName("isd-stations")
        public java.util.List<String> isd_stations;
    }
}
