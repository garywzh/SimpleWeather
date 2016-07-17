package xyz.garywzh.simpleweather.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import xyz.garywzh.simpleweather.utils.LogUtil;

/**
 * Created by garywzh on 2016/7/12.
 */
public class NetworkHelper {
    public static String TAG = NetworkHelper.class.getSimpleName();
    public static String FORECAST_BASE_URL = "https://api.forecast.io/forecast/";
    public static String GEO_BASE_URL = "http://apis.map.qq.com/ws/geocoder/v1/";

    private static ForecastService forecastService;
    private static GeodecodeService geodecodeService;

    static {
        Retrofit.Builder builder = new Retrofit.Builder()
                .client(new OkHttpClient.Builder()
                        .addInterceptor(new HttpLoggingInterceptor(new SimpleLogger())
                                .setLevel(HttpLoggingInterceptor.Level.BASIC))
                        .build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());

        forecastService = builder.baseUrl(FORECAST_BASE_URL).build().create(ForecastService.class);
        geodecodeService = builder.baseUrl(GEO_BASE_URL).build().create(GeodecodeService.class);
    }

    public static ForecastService getForecastService() {
        return forecastService;
    }

    public static GeodecodeService getGeodecodeService() {
        return geodecodeService;
    }

    private static class SimpleLogger implements HttpLoggingInterceptor.Logger {
        @Override
        public void log(String message) {
            LogUtil.d(TAG, message);
        }
    }
}
