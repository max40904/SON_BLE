package BLE;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import GUI.SONNodeFragment;
import Packet.Package;

public class ScanPart {
    private Context context;
    private static final long SCAN_PERIOD = 5000;

    private BluetoothAdapter mBluetoothAdapter;

    private BluetoothLeScanner mBluetoothLeScanner;

    private ScanCallback mScanCallback;

    private int target;

    private Handler mHandler;

    public ScanPart(Context context) {
        this.context = context;
        mHandler = new Handler();
    }

    public void setBleAdapter(BluetoothAdapter btAdapter) {
        this.mBluetoothAdapter = btAdapter;
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
    }
    public void startScanning(int target , int time) {
        Log.d("Tracer","startScanning start");
        if (mScanCallback == null) {
            this.target = target;
            Log.d("Tracer", "Starting Scanning");

            // Will stop the scanning after a set time.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopScanning();
                }
            }, time * 1000);

            // Kick off a new scan.
            mScanCallback = new SampleScanCallback();

            mBluetoothLeScanner.startScan(buildScanFilters(), buildScanSettings(), mScanCallback);

        } else {
        }
        Log.d("Tracer","startScanning end");
    }
    public void stopScanning() {
        Log.d("ScannerFragment","stopScanning start");


        // Stop the scan, wipe the callback.
        mBluetoothLeScanner.stopScan(mScanCallback);
        mScanCallback = null;


        Log.d("ScannerFragment","stopScanning end");
    }

    private ScanSettings buildScanSettings() {
        Log.d("ScannerFragment","buildScanSettings start");
        ScanSettings.Builder builder = new ScanSettings.Builder();
        builder.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY);
        Log.d("ScannerFragment","buildScanSettings end");
        return builder.build();
    }

    private List<ScanFilter> buildScanFilters() {
        Log.d("ScannerFragment","buildScanFilters start");
        List<ScanFilter> scanFilters = new ArrayList<>();

        ScanFilter.Builder builder = new ScanFilter.Builder();
        // Comment out the below line to see all BLE devices around you

        builder.setServiceUuid(Constants.Service_UUID);
        scanFilters.add(builder.build());
        Log.d("ScannerFragment","buildScanFilters end");
        return scanFilters;
    }
    /**
     * Custom ScanCallback object - adds to adapter on success, displays error on failure.
     */
    private class SampleScanCallback extends ScanCallback {

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            Log.d("Tracer","onBatchScanResults start");

            for (ScanResult result : results) {
                Package test  = new Package(result.getScanRecord().getBytes());
                Log.d("Tracer", "SampleScanCallback onBatchScanResults " + test);
            }
            Bundle bundle = new Bundle();
            bundle.putString(SONNodeFragment.RECEIVER_MESSAGE,"i am come from onBatchScanResults");
            Intent intent = new Intent(SONNodeFragment.RECEIVER_INTENT);
            intent.putExtras(bundle);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

            Log.d("Tracer","onBatchScanResults end");
        }

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            Log.d("Tracer","onBatchScanResults start");

            Package test  = new Package(result.getScanRecord().getBytes());
            Bundle bundle = new Bundle();
            bundle.putString(SONNodeFragment.RECEIVER_MESSAGE,"i am come from onBatchScanResults");
            Intent intent = new Intent(SONNodeFragment.RECEIVER_INTENT);
            intent.putExtras(bundle);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            Log.d("Tracer", "SampleScanCallback onScanResult " + test);

            Log.d("Tracer","onBatchScanResults end");
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.d("SampleScanCallback","onScanFailed start");

            Toast.makeText(context, "Scan failed with error: " + errorCode, Toast.LENGTH_LONG)
                    .show();
            Log.d("SampleScanCallback","onScanFailed end");
        }
    }

}
