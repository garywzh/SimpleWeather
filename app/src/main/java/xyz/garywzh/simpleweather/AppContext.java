package xyz.garywzh.simpleweather;

import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Bundle;

/**
 * Created by garywzh on 2016/7/18.
 */
public class AppContext extends Application {
    public static final String TAG = AppContext.class.getSimpleName();

    private static AppContext sInstance;
    private String mTencentMapKey;
    private String mForecastKey;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        try {
            Bundle bundle = getPackageManager()
                    .getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA)
                    .metaData;
            mTencentMapKey = bundle.getString("TencentMapSDK");
            mForecastKey = bundle.getString("ForecastApiKey");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (mTencentMapKey == null) {
            throw new RuntimeException("need tencent Map Key in manifest or local.properties");
        }

        if (mForecastKey == null) {
            throw new RuntimeException("need forecast Key in manifest or local.properties");
        }
    }

    public static AppContext getInstance() {
        return sInstance;
    }

    public String getmForecastKey() {
        return mForecastKey;
    }

    public String getmTencentMapKey() {
        return mTencentMapKey;
    }
}
