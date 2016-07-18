package xyz.garywzh.simpleweather.network;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import xyz.garywzh.simpleweather.model.GeoInfo;

/**
 * Created by garywzh on 2016/7/15.
 */
public interface GeodecodeService {

    @GET("")
    Observable<GeoInfo> getGeoInfo(@Query("key") String tencentMapKey, @Query("location") String location);
}
