package xyz.garywzh.simpleweather.ui.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import xyz.garywzh.simpleweather.R;
import xyz.garywzh.simpleweather.helper.DateHelper;
import xyz.garywzh.simpleweather.helper.IconDrawableHelper;
import xyz.garywzh.simpleweather.model.DataBundle;
import xyz.garywzh.simpleweather.model.Forecast;
import xyz.garywzh.simpleweather.utils.LogUtil;

/**
 * Created by garywzh on 2016/7/17.
 */
public class RootViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = RootViewAdapter.class.getSimpleName();

    public static final int TYPE_OVERVIEW = 1;
    public static final int TYPE_DAILY = 2;
    public static final int TYPE_FOOTER = 3;

    private DataBundle mDataBundle;
    private OnFooterClickListener mListener;

    public RootViewAdapter(OnFooterClickListener listener) {
        mListener = listener;
    }

    public void setData(DataBundle dataBundle) {
        mDataBundle = dataBundle;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataBundle == null ? 0 : 3;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_OVERVIEW;
            case 1:
                return TYPE_DAILY;
            case 2:
                return TYPE_FOOTER;
            default:
                LogUtil.e(TAG, "unhandled position");
                return 0;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_OVERVIEW:
                final View OverViewCard = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_overview, parent, false);
                return new OverViewVH(OverViewCard);
            case TYPE_DAILY:
                final View dailyCard = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_daily, parent, false);
                return new DailyVH(dailyCard);
            case TYPE_FOOTER:
                final View footer = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_footer, parent, false);
                return new FooterVH(footer, mListener);
            default:
                LogUtil.e(TAG, "unKnown viewType");
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OverViewVH) {
            ((OverViewVH) holder).fillData(mDataBundle);
        } else if (holder instanceof DailyVH) {
            ((DailyVH) holder).fillData(mDataBundle.getForecast().daily);
        } else if (holder instanceof FooterVH) {
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

        public void fillData(DataBundle dataBundle) {
            district.setText(dataBundle.getLocation().district);
            city.setText(dataBundle.getLocation().city);
            temperatureMaxMin.setText(String.format("%s %d° - %s %d°",
                temperatureMaxMin.getContext().getString(R.string.max),
                Math.round(dataBundle.getForecast().daily.data.get(0).temperatureMax),
                temperatureMaxMin.getContext().getString(R.string.min),
                Math.round(dataBundle.getForecast().daily.data.get(0).temperatureMin)));
            temperature.setText(String.format("%d",
                Math.round(dataBundle.getForecast().currently.temperature)));
            condition.setText(dataBundle.getForecast().currently.summary);
            Glide.with(conditionIcon.getContext())
                .load(IconDrawableHelper.getDrawable(dataBundle.getForecast().currently.icon))
                .crossFade()
                .into(conditionIcon);
        }
    }

    public static class DailyVH extends RecyclerView.ViewHolder implements
        DailyAdapter.OnDailyItemClickListener {

        private DailyAdapter dailyAdapter;
        private TextView weekSummary;
        private TextView time;
        private TextView daySummary;
        private TextView precipProbability;
        private TextView humidity;
        private TextView windSpeed;
        private TextView pressure;

        public DailyVH(View itemView) {
            super(itemView);
            RecyclerView dailyRecyclerView = (RecyclerView) itemView.findViewById(R.id.daily);
            dailyRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
            dailyAdapter = new DailyAdapter(this);
            dailyRecyclerView.setAdapter(dailyAdapter);

            weekSummary = (TextView) itemView.findViewById(R.id.week_summary);
            time = (TextView) itemView.findViewById(R.id.time);
            daySummary = (TextView) itemView.findViewById(R.id.day_summary);
            precipProbability = (TextView) itemView.findViewById(R.id.precip_probability_content);
            humidity = (TextView) itemView.findViewById(R.id.humidity_content);
            windSpeed = (TextView) itemView.findViewById(R.id.wind_speed_content);
            pressure = (TextView) itemView.findViewById(R.id.pressure_content);
        }

        public void fillData(Forecast.DailyBean dailyBean) {
            dailyAdapter.setData(dailyBean.data);
            weekSummary.setText(dailyBean.summary);

            setDailySheetData(dailyBean.data.get(0));
        }

        @Override
        public void onDailyItemClick(Forecast.DailyBean.DataBean dataBean) {
            setDailySheetData(dataBean);
        }

        private void setDailySheetData(Forecast.DailyBean.DataBean dataBean) {
            time.setText(DateHelper.getDailyDate(dataBean.time));
            daySummary.setText(dataBean.summary);
            precipProbability
                .setText(String.format("%d%%", Math.round(dataBean.precipProbability * 100)));
            humidity.setText(String.format("%d%%", Math.round(dataBean.humidity * 100)));
            windSpeed.setText(String.format("%s km/h", dataBean.windSpeed));
            pressure.setText(String.format("%s hPa", dataBean.pressure));
        }
    }

    public static class FooterVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private OnFooterClickListener mListener;

        public FooterVH(View itemView, OnFooterClickListener listener) {
            super(itemView);
            mListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onFooterClick();
        }
    }

    public interface OnFooterClickListener {

        void onFooterClick();
    }
}
