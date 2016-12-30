package xyz.garywzh.simpleweather.network;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import xyz.garywzh.simpleweather.model.Forecast;

/**
 * Created by garywzh on 2016/7/12.
 */
public interface ForecastService {

    String LANG_SIMPLIFIED_CHINESE = "zh";
    String LANG_ENGLISH = "en";

    @GET("{forecastKey}/{location}?units=auto")
    Observable<Forecast> getForecast(@Path("forecastKey") String forecastKey,
        @Path("location") String location, @Query("lang") String lang);
}
