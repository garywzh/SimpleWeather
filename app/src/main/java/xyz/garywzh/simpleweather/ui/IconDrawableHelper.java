package xyz.garywzh.simpleweather.ui;

import xyz.garywzh.simpleweather.R;

/**
 * Created by garywzh on 2016/7/15.
 */
public class IconDrawableHelper {
    public static int getDrawable(String icon) {
        switch (icon) {
            case "clear-day":
                return R.drawable.clear;
            case "clear-night":
                return R.drawable.clear_night;
            case "cloudy":
                return R.drawable.cloudy;
            case "fog":
                return R.drawable.fog;
            case "partly-cloudy-day":
                return R.drawable.partly_cloudy;
            case "partly-cloudy-night":
                return R.drawable.partly_cloudy_night;
            case "rain":
                return R.drawable.rain;
            case "sleet":
                return R.drawable.sleet;
            case "snow":
                return R.drawable.snow;
            case "thunderstorm":
                return R.drawable.thunderstorm;
            default:
                return R.drawable.clear;
        }
    }
}
