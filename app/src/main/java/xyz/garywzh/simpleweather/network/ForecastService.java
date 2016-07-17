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
    String FORECAST_API_KEY = "fd918ea47a7f6906c8d3a7cd5d02df86";
    String LANG_SIMPLIFIED_CHINESE = "zh";
    String LANG_ENGLISH = "en";

    @GET(FORECAST_API_KEY + "/{location}?units=auto")
    Observable<Forecast> getForecast(@Path("location") String location, @Query("lang") String lang);
}
