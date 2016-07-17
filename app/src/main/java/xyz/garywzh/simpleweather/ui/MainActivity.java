package xyz.garywzh.simpleweather.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import xyz.garywzh.simpleweather.R;
import xyz.garywzh.simpleweather.model.DataRepo;
import xyz.garywzh.simpleweather.model.Forecast;
import xyz.garywzh.simpleweather.network.ForecastService;
import xyz.garywzh.simpleweather.network.NetworkHelper;
import xyz.garywzh.simpleweather.ui.adapter.RootViewAdapter;
import xyz.garywzh.simpleweather.utils.LogUtil;

public class MainActivity extends AppCompatActivity implements TencentLocationListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int COARSE_PERMISSION_REQUEST_CODE = 1;

    private RootViewAdapter rootAdapter;
    private TencentLocationManager mLocationManager;
    private DataRepo mDataRepo;
    private boolean fetching = false;
    private boolean alreadyGotData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rootRecyclerView = (RecyclerView) findViewById(R.id.root_view);
        rootRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rootAdapter = new RootViewAdapter();
        rootRecyclerView.setAdapter(rootAdapter);

        mDataRepo = new DataRepo();
        mLocationManager = TencentLocationManager.getInstance(this);
        mLocationManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_GCJ02);
        checkPermission();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            LogUtil.d(TAG, "checkPermission");
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                LogUtil.d(TAG, "requestPermissions");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        COARSE_PERMISSION_REQUEST_CODE);
            } else {
                // No explanation needed, we can request the permission.
                LogUtil.d(TAG, "requestPermissions");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        COARSE_PERMISSION_REQUEST_CODE);
            }
        } else {
            startLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case COARSE_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    checkPermission();
                } else {
                    LogUtil.d(TAG, "permission granted");
                    handleDeniedPermission();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void handleDeniedPermission() {
        LogUtil.w(TAG, "permission denied");
    }

    public void startLocation() {
        TencentLocationRequest request = TencentLocationRequest.create()
                // 设置定位周期
                .setInterval(1000)
                // 设置定位level
                .setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA);

        // 开始定位
        LogUtil.d(TAG, "start location");
        mLocationManager.requestLocationUpdates(request, this);
    }

    private void getData() {
        NetworkHelper.getForecastService()
                .getForecast(mDataRepo.getLocation().getLatitude() + "," + mDataRepo.getLocation().getLongitude(), ForecastService.LANG_SIMPLIFIED_CHINESE)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        fetching = true;
                        LogUtil.d(TAG, "fetching data");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Forecast>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        fetching = false;
                        alreadyGotData = false;
                    }

                    @Override
                    public void onNext(Forecast forecast) {
                        mLocationManager.removeUpdates(MainActivity.this);
                        fetching = false;
                        alreadyGotData = true;
                        mDataRepo.setForecast(forecast);

                        rootAdapter.setData(mDataRepo);
                    }
                });
    }

    // TencentLocationListener Callback
    @Override
    public void onLocationChanged(TencentLocation location, int error, String reason) {
        if (error == TencentLocation.ERROR_OK) {
            LogUtil.d(TAG, "location changed" +
                    "latitude" + location.getLatitude() +
                    "longitude" + location.getLongitude());
            mDataRepo.setLocation(location);
            if (!alreadyGotData && !fetching)
                getData();
        } else {
            LogUtil.d(TAG, "location failed");
        }
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationManager.removeUpdates(this);
    }
}
