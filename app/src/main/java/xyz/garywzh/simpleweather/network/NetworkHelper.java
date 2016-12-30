package xyz.garywzh.simpleweather.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import xyz.garywzh.simpleweather.AppContext;
import xyz.garywzh.simpleweather.exception.LocationApiException;
import xyz.garywzh.simpleweather.model.DataBundle;
import xyz.garywzh.simpleweather.model.Forecast;
import xyz.garywzh.simpleweather.model.LocationInfo;
import xyz.garywzh.simpleweather.model.Repository;
import xyz.garywzh.simpleweather.model.SimpleLocation;
import xyz.garywzh.simpleweather.utils.LogUtil;

/**
 * Created by garywzh on 2016/7/12.
 */
public class NetworkHelper {

    public static String TAG = NetworkHelper.class.getSimpleName();
    public static String FORECAST_BASE_URL = "https://api.darksky.net/forecast/";
    public static String GEO_BASE_URL = "http://apis.map.qq.com/ws/geocoder/v1/";

    private static ForecastService forecastService;
    private static GeocodeService geocodeService;

    static {
        Retrofit.Builder builder = new Retrofit.Builder()
            .client(new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor(new SimpleLogger())
                    .setLevel(HttpLoggingInterceptor.Level.BASIC))
                .build())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create());

        forecastService = builder.baseUrl(FORECAST_BASE_URL).build().create(ForecastService.class);
        geocodeService = builder.baseUrl(GEO_BASE_URL).build().create(GeocodeService.class);
    }

    public static Observable<DataBundle> fetchDataWithLocation(
        final SimpleLocation simpleLocation) {
        return Observable
            .just(simpleLocation)
            .compose(locationToBundle(true));
    }

    public static Observable<DataBundle> fetchDataWithAddress(String address) {
        return geocodeService
            .getLocation(AppContext.getInstance().getmTencentMapKey(), address)
            .flatMap(new Func1<LocationInfo, Observable<SimpleLocation>>() {
                @Override
                public Observable<SimpleLocation> call(LocationInfo locationInfo) {
                    if (locationInfo.status != 0) {
                        return Observable.error(new LocationApiException(locationInfo.message));
                    } else {
                        return Observable.just(new SimpleLocation(
                            locationInfo.result.address_components.city,
                            locationInfo.result.address_components.district,
                            String.valueOf(locationInfo.result.location.lat),
                            String.valueOf(locationInfo.result.location.lng)));
                    }
                }
            })
            .compose(locationToBundle(false));
    }

    private static Observable.Transformer<SimpleLocation, DataBundle> locationToBundle(
        final boolean isLocal) {
        return new Observable.Transformer<SimpleLocation, DataBundle>() {
            @Override
            public Observable<DataBundle> call(
                Observable<SimpleLocation> simpleLocationObservable) {
                return simpleLocationObservable
                    .flatMap(new Func1<SimpleLocation, Observable<DataBundle>>() {
                        @Override
                        public Observable<DataBundle> call(final SimpleLocation simpleLocation) {
                            return forecastService
                                .getForecast(
                                    AppContext.getInstance().getmForecastKey(),
                                    simpleLocation.latitude + "," + simpleLocation.longitude,
                                    ForecastService.LANG_SIMPLIFIED_CHINESE)
                                .map(new Func1<Forecast, DataBundle>() {
                                    @Override
                                    public DataBundle call(Forecast forecast) {
                                        return new DataBundle(simpleLocation, forecast);
                                    }
                                });
                        }
                    })
                    .doOnNext(new Action1<DataBundle>() {
                        @Override
                        public void call(DataBundle bundle) {
                            Repository.getInstance().save(bundle, isLocal);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    private static class SimpleLogger implements HttpLoggingInterceptor.Logger {

        @Override
        public void log(String message) {
            LogUtil.d(TAG, message);
        }
    }
}
