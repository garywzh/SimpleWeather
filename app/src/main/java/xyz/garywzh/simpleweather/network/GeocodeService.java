package xyz.garywzh.simpleweather.network;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import xyz.garywzh.simpleweather.model.AddressInfo;
import xyz.garywzh.simpleweather.model.LocationInfo;

/**
 * Created by garywzh on 2016/7/15.
 */
public interface GeocodeService {

    //逆地址解析
    @GET("./")
    Observable<AddressInfo> getAddress(@Query("key") String tencentMapKey,
        @Query("location") String location);

    //地址解析
    @GET("./")
    Observable<LocationInfo> getLocation(@Query("key") String tencentMapKey,
        @Query("address") String address);
}
