package xyz.garywzh.simpleweather.network;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import xyz.garywzh.simpleweather.model.GeoInfo;

/**
 * Created by garywzh on 2016/7/15.
 */
public interface GeodecodeService {
    String GEO_API_KEY = "KC5BZ-EW2RP-7KWDT-VKYOI-K2H5E-HBBG3";

    @GET("?key=" + GEO_API_KEY)
    Observable<GeoInfo> getGeoInfo(@Query("location") String location);
}
