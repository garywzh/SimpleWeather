package xyz.garywzh.simpleweather.model;

import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import xyz.garywzh.simpleweather.AppContext;

/**
 * Created by garywzh on 2016/7/22.
 */
public class Repository {
    private static String PREF_FILE_NAME = "DistrictMap";
    public static String PREF_KEY_LOCAL = "local";
    private static Repository sRepository;

    private Gson mGson = new Gson();

    public static Repository getInstance() {
        if (sRepository == null)
            sRepository = new Repository();
        return sRepository;
    }

    public void save(DataBundle data, boolean isLocal) {
        SharedPreferences.Editor editor = AppContext.getInstance()
                .getSharedPreferences(PREF_FILE_NAME, 0)
                .edit();

        editor.putString(isLocal ? PREF_KEY_LOCAL : data.getLocation().city + data.getLocation().district, mGson.toJson(data)).apply();
    }

    public void delete(String address) {
        SharedPreferences.Editor editor = AppContext.getInstance()
                .getSharedPreferences(PREF_FILE_NAME, 0)
                .edit();

        editor.remove(address).apply();
    }

    public DataBundle get(String address) {
        String json = AppContext.getInstance()
                .getSharedPreferences(PREF_FILE_NAME, 0).getString(address, null);
        return mGson.fromJson(json, new TypeToken<DataBundle>() {
        }.getType());
    }

    public List<String> getAddressList() {
        Set<String> set = new TreeSet<>();
        Map<String, ?> map = AppContext.getInstance()
                .getSharedPreferences(PREF_FILE_NAME, 0)
                .getAll();

        for (Map.Entry<String, ?> entry : map.entrySet()) {
            String address = entry.getKey();
            if (!address.equals(PREF_KEY_LOCAL))
                set.add(address);
        }
        return new ArrayList<>(set);
    }
}
