package com.byron.bledemo;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.ble_device_recyclerview)
    RecyclerView mBleListRecyclerView;

    private final int REQUEST_BLE_PERMISSION_CODE = 1;
    private BluetoothAdapter mBtAdapter;

    List<BluetoothDevice> mBleDeviceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mBleListRecyclerView.setAdapter(new BtListAdapter(mBleDeviceList));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBleListRecyclerView.setLayoutManager(layoutManager);
    }

    private void checkPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_BLE_PERMISSION_CODE);
        } else {
            openBt();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_BLE_PERMISSION_CODE:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openBt();
                } else {
                    Toast.makeText(this, "打开蓝牙失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void openBt() {
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBtAdapter = bluetoothManager.getAdapter();

        if(!mBtAdapter.isEnabled()) {
            mBtAdapter.enable();
        }
    }

    @OnClick(R.id.open_bt_btn)
    void onOpenBtClick(){
        checkPermission();
    }

    @OnClick(R.id.search_ble_btn)
    void onSearchDeviceClick() {
        BluetoothLeScanner scanner = mBtAdapter.getBluetoothLeScanner();
        scanner.startScan(new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
                BluetoothDevice device = result.getDevice();

                if(!mBleDeviceList.contains(device) && device.getName() != null) {
                    mBleDeviceList.add(device);
                    mBleListRecyclerView.getAdapter().notifyItemInserted(mBleDeviceList.size() - 1);
                    Timber.d("find device :" + device.getName() + ", address:" + device.getAddress());
                }

            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
            }
        });
    }
}
