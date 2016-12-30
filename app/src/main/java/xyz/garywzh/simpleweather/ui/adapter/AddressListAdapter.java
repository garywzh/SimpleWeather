package xyz.garywzh.simpleweather.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import xyz.garywzh.simpleweather.R;

/**
 * Created by garywzh on 2016/7/28.
 */
public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.AddressItemVH> {

    public static final String TAG = AddressListAdapter.class.getSimpleName();

    private List<String> mData;

    public void setData(List<String> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public AddressItemVH onCreateViewHolder(ViewGroup parent, int viewType) {
        final View addressView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.view_address_item, parent, false);
        return new AddressItemVH(addressView);
    }

    @Override
    public void onBindViewHolder(AddressItemVH holder, int position) {
        holder.fillData(mData.get(position));
    }

    public void remove(int swipedPosition) {
        mData.remove(swipedPosition);
        notifyItemRemoved(swipedPosition);
    }

    public static class AddressItemVH extends RecyclerView.ViewHolder {

        private TextView mAddress;

        public AddressItemVH(View itemView) {
            super(itemView);
            mAddress = (TextView) itemView.findViewById(R.id.address);
        }

        public void fillData(String s) {
            mAddress.setText(s);
        }

        public String getContent() {
            return mAddress.getText().toString();
        }
    }
}
