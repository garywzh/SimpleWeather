package xyz.garywzh.simpleweather.ui.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import xyz.garywzh.simpleweather.R;
import xyz.garywzh.simpleweather.model.DataRepo;
import xyz.garywzh.simpleweather.model.Forecast;
import xyz.garywzh.simpleweather.ui.IconDrawableHelper;
import xyz.garywzh.simpleweather.utils.LogUtil;

/**
 * Created by garywzh on 2016/7/17.
 */
public class RootViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = RootViewAdapter.class.getSimpleName();

    public static final int TYPE_OVERVIEW = 1;
    public static final int TYPE_DAILY = 2;

    private DataRepo mDataRepo;

    public void setData(DataRepo dataRepo) {
        mDataRepo = dataRepo;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataRepo == null ? 0 : 2;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_OVERVIEW;
            case 1:
                return TYPE_DAILY;
            default:
                LogUtil.e(TAG, "unhandled position");
                return 0;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_OVERVIEW:
                final View OverViewCard = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_overview, parent, false);
                return new OverViewVH(OverViewCard);
            case TYPE_DAILY:
                final View dailyCard = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_daily, parent, false);
                return new DailyVH(dailyCard);
            default:
                LogUtil.e(TAG, "unKnown viewType");
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OverViewVH) {
            ((OverViewVH) holder).fillData(mDataRepo);
        } else if (holder instanceof DailyVH) {
            ((DailyVH) holder).fillData(mDataRepo.getForecast().daily.data);
        } else {
            LogUtil.e(TAG, "unknown viewHolder");
        }
    }

    public static class OverViewVH extends RecyclerView.ViewHolder {
        private TextView district;
        private TextView city;
        private TextView temperatureMaxMin;
        private TextView temperature;
        private ImageView conditionIcon;
        private TextView condition;

        public OverViewVH(View itemView) {
            super(itemView);
            district = (TextView) itemView.findViewById(R.id.district);
            city = (TextView) itemView.findViewById(R.id.city);
            temperatureMaxMin = (TextView) itemView.findViewById(R.id.temperatureMaxMin);
            temperature = (TextView) itemView.findViewById(R.id.temperature);
            conditionIcon = (ImageView) itemView.findViewById(R.id.condition_icon);
            condition = (TextView) itemView.findViewById(R.id.condition);
        }

        public void fillData(DataRepo dataRepo) {
            district.setText(dataRepo.getLocation().getDistrict());
            city.setText(dataRepo.getLocation().getCity());
            temperatureMaxMin.setText(String.format("%s %d° - %s %d°",
                    temperatureMaxMin.getContext().getString(R.string.max),
                    (int) dataRepo.getForecast().daily.data.get(0).temperatureMax,
                    temperatureMaxMin.getContext().getString(R.string.min),
                    (int) dataRepo.getForecast().daily.data.get(0).temperatureMin));
            temperature.setText(String.format("%d",
                    (int) dataRepo.getForecast().currently.temperature));
            condition.setText(dataRepo.getForecast().currently.summary);
            Glide.with(conditionIcon.getContext())
                    .load(IconDrawableHelper.getDrawable(dataRepo.getForecast().currently.icon))
                    .crossFade()
                    .into(conditionIcon);
        }
    }

    public static class DailyVH extends RecyclerView.ViewHolder {
        private DailyAdapter dailyAdapter;

        public DailyVH(View itemView) {
            super(itemView);
            RecyclerView dailyRecyclerView = (RecyclerView) itemView.findViewById(R.id.daily);
            dailyRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(),
                    LinearLayoutManager.HORIZONTAL, false));
            dailyAdapter = new DailyAdapter();
            dailyRecyclerView.setAdapter(dailyAdapter);
        }

        public void fillData(List<Forecast.DailyBean.DataBean> data) {
            dailyAdapter.setData(data);
        }
    }
}
