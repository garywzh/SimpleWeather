package xyz.garywzh.simpleweather.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import xyz.garywzh.simpleweather.R;
import xyz.garywzh.simpleweather.model.Forecast;
import xyz.garywzh.simpleweather.ui.IconDrawableHelper;

/**
 * Created by garywzh on 2016/7/17.
 */
public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.DailyItemVH> {
    public static final String TAG = DailyAdapter.class.getSimpleName();

    private List<Forecast.DailyBean.DataBean> mData;

    public void setData(List<Forecast.DailyBean.DataBean> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public DailyItemVH onCreateViewHolder(ViewGroup parent, int viewType) {
        final View dailyView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_daily_item, parent, false);
        return new DailyItemVH(dailyView);
    }

    @Override
    public void onBindViewHolder(DailyItemVH holder, int position) {
        holder.fillData(mData.get(position));
    }

    public static class DailyItemVH extends RecyclerView.ViewHolder {
        private TextView weekName;
        private ImageView icon;
        private TextView max;
        private TextView min;

        public DailyItemVH(View itemView) {
            super(itemView);
            weekName = (TextView) itemView.findViewById(R.id.week_name);
            icon = (ImageView) itemView.findViewById(R.id.condition_icon);
            max = (TextView) itemView.findViewById(R.id.max_temperature);
            min = (TextView) itemView.findViewById(R.id.min_temperature);
        }

        public void fillData(Forecast.DailyBean.DataBean dataBean) {
            weekName.setText(getDayOfWeek(dataBean.time));
            max.setText(String.format(" %d°", (int) dataBean.temperatureMax));
            min.setText(String.format(" %d°", (int) dataBean.temperatureMin));
            Glide.with(weekName.getContext())
                    .load(IconDrawableHelper.getDrawable(dataBean.icon))
                    .crossFade()
                    .into(icon);
        }

        private String getDayOfWeek(int timeStamp) {
            SimpleDateFormat format = new SimpleDateFormat("E");
            format.setTimeZone(TimeZone.getDefault());
            return format.format(new Date(timeStamp * 1000L));
        }
    }
}
