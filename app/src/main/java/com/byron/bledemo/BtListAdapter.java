package com.byron.bledemo;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BtListAdapter extends RecyclerView.Adapter<BtListAdapter.ViewHolder> {
    List<BluetoothDevice> mBTDeviceList;

    public BtListAdapter(List<BluetoothDevice> mBTDeviceList) {
        this.mBTDeviceList = mBTDeviceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bt_recyclerview_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        BluetoothDevice device = mBTDeviceList.get(holder.getAdapterPosition());
        holder.mBleName.setText(device.getName());
        holder.mBleAddress.setText(device.getAddress());
    }

    @Override
    public int getItemCount() {
        return mBTDeviceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ble_device_name)
        TextView mBleName;

        @BindView(R.id.ble_device_address) TextView mBleAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
