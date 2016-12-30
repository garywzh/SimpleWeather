package xyz.garywzh.simpleweather.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.List;
import xyz.garywzh.simpleweather.R;
import xyz.garywzh.simpleweather.helper.DateHelper;
import xyz.garywzh.simpleweather.helper.IconDrawableHelper;
import xyz.garywzh.simpleweather.model.Forecast;

/**
 * Created by garywzh on 2016/7/17.
 */
public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.DailyItemVH> {
    public static final String TAG = DailyAdapter.class.getSimpleName();

    private View.OnClickListener mListener;
    private List<Forecast.DailyBean.DataBean> mData;

    public DailyAdapter(OnDailyItemClickListener listener) {
        mListener = new OnDailyClickListener(listener);
    }

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
        dailyView.setOnClickListener(mListener);
        return new DailyItemVH(dailyView);
    }

    @Override
    public void onBindViewHolder(DailyItemVH holder, int position) {
        holder.fillData(mData.get(position));
    }

    public static class DailyItemVH extends RecyclerView.ViewHolder {
        private View root;
        private TextView weekName;
        private ImageView icon;
        private TextView max;
        private TextView min;

        public DailyItemVH(View itemView) {
            super(itemView);
            root = itemView;
            weekName = (TextView) itemView.findViewById(R.id.week_name);
            icon = (ImageView) itemView.findViewById(R.id.condition_icon);
            max = (TextView) itemView.findViewById(R.id.max_temperature);
            min = (TextView) itemView.findViewById(R.id.min_temperature);
        }

        public void fillData(Forecast.DailyBean.DataBean dataBean) {
            root.setTag(dataBean);
            weekName.setText(DateHelper.getDayOfWeek(dataBean.time));
            max.setText(String.format(" %d°", Math.round(dataBean.temperatureMax)));
            min.setText(String.format(" %d°", Math.round(dataBean.temperatureMin)));
            Glide.with(weekName.getContext())
                    .load(IconDrawableHelper.getDrawable(dataBean.icon))
                    .crossFade()
                    .into(icon);
        }
    }

    private static class OnDailyClickListener implements View.OnClickListener {
        OnDailyItemClickListener mListener;

        public OnDailyClickListener(OnDailyItemClickListener listener) {
            mListener = listener;
        }

        @Override
        public void onClick(View view) {
            mListener.onDailyItemClick((Forecast.DailyBean.DataBean) view.getTag());
        }
    }

    public interface OnDailyItemClickListener {
        void onDailyItemClick(Forecast.DailyBean.DataBean dataBean);
    }
}
