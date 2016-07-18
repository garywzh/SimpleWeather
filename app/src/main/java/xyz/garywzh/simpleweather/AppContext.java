package xyz.garywzh.simpleweather;

import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Bundle;

/**
 * Created by garywzh on 2016/7/18.
 */
public class AppContext extends Application {
    public static final String TAG = AppContext.class.getSimpleName();

    private static AppContext mInstance;
    private String tencentMapKey;
    private String forecastKey;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        try {
            Bundle bundle = getPackageManager()
                    .getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA)
                    .metaData;
            tencentMapKey = bundle.getString("TencentMapSDK");
            forecastKey = bundle.getString("ForecastApiKey");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (tencentMapKey == null) {
            throw new RuntimeException("need tencentMapKey in manifest or local.properties");
        }

        if (forecastKey == null) {
            throw new RuntimeException("need forecastKey in manifest or local.properties");
        }
    }

    public static AppContext getInstance() {
        return mInstance;
    }

    public String getForecastKey() {
        return forecastKey;
    }
}
