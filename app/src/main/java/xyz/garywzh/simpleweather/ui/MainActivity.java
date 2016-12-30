package xyz.garywzh.simpleweather.ui;

import android.Manifest.permission;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.CompositePermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.karumi.dexter.listener.single.SnackbarOnDeniedPermissionListener;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import java.util.List;
import java.util.concurrent.Callable;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import xyz.garywzh.simpleweather.R;
import xyz.garywzh.simpleweather.model.DataBundle;
import xyz.garywzh.simpleweather.model.Repository;
import xyz.garywzh.simpleweather.model.SimpleLocation;
import xyz.garywzh.simpleweather.network.NetworkHelper;
import xyz.garywzh.simpleweather.ui.adapter.RootViewAdapter;
import xyz.garywzh.simpleweather.ui.fragment.AddItemAlertDialogFragment;
import xyz.garywzh.simpleweather.utils.LogUtil;

public class MainActivity extends AppCompatActivity implements TencentLocationListener,
    RootViewAdapter.OnFooterClickListener, SwipeRefreshLayout.OnRefreshListener,
    NavigationView.OnNavigationItemSelectedListener,
    AddItemAlertDialogFragment.OnAddAddressListener, PermissionListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private SwipeRefreshLayout mRefreshLayout;
    private RootViewAdapter mAdapter;
    private TencentLocationManager mLocationManager;
    private PermissionListener mPermissionListener;
    private boolean mLoading = false;
    private Subscription mSubscription;
    private String mAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAddress = Repository.PREF_KEY_LOCAL;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.inflateHeaderView(R.layout.view_drawer_header);
        mNavigationView.setNavigationItemSelectedListener(this);

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        mRefreshLayout.setOnRefreshListener(this);

        RecyclerView rootRecyclerView = (RecyclerView) findViewById(R.id.root_view);
        rootRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RootViewAdapter(this);
        rootRecyclerView.setAdapter(mAdapter);

        loadLocalCache();
        refreshAddressList();

        createPermissionListeners();
        checkPermission();
    }

    private void loadLocalCache() {
        Observable
            .fromCallable(new Callable<DataBundle>() {
                @Override
                public DataBundle call() throws Exception {
                    return Repository.getInstance().get(mAddress);
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<DataBundle>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onNext(DataBundle bundle) {
                    mAdapter.setData(bundle);
                }
            });
    }

    private void refreshAddressList() {
        Observable
            .fromCallable(new Callable<List<String>>() {
                @Override
                public List<String> call() throws Exception {
                    return Repository.getInstance().getAddressList();
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<List<String>>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onNext(List<String> strings) {
                    mNavigationView.getMenu().clear();
                    mNavigationView.inflateMenu(R.menu.menu_drawer);
                    Menu menu = mNavigationView.getMenu();
                    for (String address : strings) {
                        menu.add(R.id.group_address, Menu.NONE, Menu.NONE, address);
                    }
                }
            });
    }

    private void startLocation() {
        mLocationManager = TencentLocationManager.getInstance(this);
        mLocationManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_GCJ02);

        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
            }
        });

        TencentLocationRequest request = TencentLocationRequest.create()
            // 设置定位周期
            .setInterval(1000)
            // 设置定位level
            .setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA);

        int result = mLocationManager.requestLocationUpdates(request, this);
        if (result == 0) {
            LogUtil.d(TAG, "start location");
        } else {
            LogUtil.d(TAG, "cannot start location! error: " + result);
        }
    }

    private Subscription loadDataWithLocation(SimpleLocation location) {
        return NetworkHelper.fetchDataWithLocation(location)
            .doOnSubscribe(new Action0() {
                @Override
                public void call() {
                    mRefreshLayout.setRefreshing(true);
                    mLoading = true;
                    LogUtil.d(TAG, "fetching data");
                }
            })
            .subscribe(new Subscriber<DataBundle>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                    LogUtil.d(TAG, "error");
                    mLoading = false;
                }

                @Override
                public void onNext(DataBundle bundle) {
                    mLocationManager.removeUpdates(MainActivity.this);
                    mLoading = false;
                    LogUtil.d(TAG, "got data");

                    mAdapter.setData(bundle);
                    mRefreshLayout.setRefreshing(false);
                }
            });
    }

    private Subscription loadDataWithAddress(String address, final boolean refreshAddressList) {
        return NetworkHelper.fetchDataWithAddress(address)
            .doOnSubscribe(new Action0() {
                @Override
                public void call() {
                    mDrawerLayout.closeDrawer(mNavigationView);
                    mRefreshLayout.setRefreshing(true);
                    mLoading = true;
                    LogUtil.d(TAG, "fetching data");
                }
            })
            .subscribe(new Subscriber<DataBundle>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                    mRefreshLayout.setRefreshing(false);
                    mLoading = false;
                }

                @Override
                public void onNext(DataBundle bundle) {
                    mLoading = false;
                    LogUtil.d(TAG, "got data");

                    mAdapter.setData(bundle);
                    mRefreshLayout.setRefreshing(false);

                    if (refreshAddressList) {
                        refreshAddressList();
                    }
                }
            });
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshAddressList();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(this);
        }
        mRefreshLayout.setRefreshing(false);
        mLoading = false;

        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
            mSubscription = null;
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.local) {
            mDrawerLayout.closeDrawer(mNavigationView);
            mAddress = Repository.PREF_KEY_LOCAL;
            loadLocalCache();
            return false;
        }
        if (item.getGroupId() == R.id.group_address) {
            if (item.getItemId() == R.id.manage) {
                startActivity(new Intent(this, ManageAddressActivity.class));
                return false;
            } else {
                mAddress = item.getTitle().toString();
                loadLocalCache();
                loadDataWithAddress(mAddress, false);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(mNavigationView);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mNavigationView)) {
            mDrawerLayout.closeDrawer(mNavigationView);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onRefresh() {
        if (mAddress.equals(Repository.PREF_KEY_LOCAL)) {
            checkPermission();
        } else {
            loadDataWithAddress(mAddress, false);
        }
    }

    // TencentLocationListener Callback
    @Override
    public void onLocationChanged(TencentLocation location, int error, String reason) {
        if (error == TencentLocation.ERROR_OK) {
            LogUtil.d(TAG, "location changed" +
                "latitude" + location.getLatitude() +
                "longitude" + location.getLongitude());

            if (!mLoading) {
                mSubscription = loadDataWithLocation(new SimpleLocation(
                    location.getCity(),
                    location.getDistrict(),
                    String.valueOf(location.getLatitude()),
                    String.valueOf(location.getLongitude())));
            }
        } else {
            LogUtil.d(TAG, "location failed: " + reason + " code: " + error);
        }
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {
        LogUtil.d(TAG, "status updated: " + s + " " + s1);
    }

    //OnFooterClickListener
    @Override
    public void onFooterClick() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://darksky.net/poweredby/"));
        startActivity(intent);
    }

    //OnAddAddressListener
    @Override
    public void OnAddAddress(String address) {
        mAddress = address;
        loadDataWithAddress(mAddress, true);
    }

    private void checkPermission() {
        Dexter.withActivity(this)
            .withPermission(permission.ACCESS_COARSE_LOCATION)
            .withListener(mPermissionListener)
            .check();
    }

    private void createPermissionListeners() {
        mPermissionListener = new CompositePermissionListener(this,
            SnackbarOnDeniedPermissionListener.Builder
                .with(mRefreshLayout, R.string.location_permission_denied_feedback)
                .withOpenSettingsButton(R.string.permission_rationale_settings_button_text)
                .withCallback(new Snackbar.Callback() {
                    @Override
                    public void onShown(Snackbar snackbar) {
                        super.onShown(snackbar);
                    }

                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        super.onDismissed(snackbar, event);
                    }
                })
                .build());
    }

    //PermissionListener
    @Override
    public void onPermissionGranted(PermissionGrantedResponse response) {
        startLocation();
    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse response) {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permission,
        PermissionToken token) {
        showPermissionRationale(token);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void showPermissionRationale(final PermissionToken token) {
        new AlertDialog.Builder(this).setTitle(R.string.permission_rationale_title)
            .setMessage(R.string.permission_rationale_message)
            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    token.cancelPermissionRequest();
                }
            })
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    token.continuePermissionRequest();
                }
            })
            .setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    token.cancelPermissionRequest();
                }
            })
            .show();
    }
}