package BLE;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class AdvertiserService extends Service {
    private static final String TAG = AdvertiserService.class.getSimpleName();

    private static final int FOREGROUND_NOTIFICATION_ID = 1;

    private boolean flag;

    public static boolean running = false;

    public static final String ADVERTISING_FAILED =
            "com.example.android.bluetoothadvertisements.advertising_failed";

    public static final String ADVERTISING_FAILED_EXTRA_CODE = "failureCode";


    private BluetoothLeAdvertiser mBluetoothLeAdvertiser;

    private AdvertiseCallback mAdvertiseCallback;

    private Handler mHandler;

    private Runnable timeoutRunnable;

    public static final int ADVERTISING_TIMED_OUT = 6;


    public static final String  intentArgument = "ID";

    public static final String  intentArgument2 = "RawData";

    public static final String  intentArgument3 = "Time";

    private String  UUID;

    private byte[]  rawData;

    private int timeout;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Tracer","AdvertiserService onCreate start");
        initialize();

        Log.d("Tracer","AdvertiserService onCreate end");
        // TODO Auto-generated method stub
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Tracer","AdvertiserService onStartCommand start");
        running = true;
        flag = intent.getExtras().getBoolean("flag");
        UUID = intent.getStringExtra(intentArgument);
        rawData = intent.getByteArrayExtra(intentArgument2);
        timeout = intent.getIntExtra(intentArgument3,0);
        String meesage = "this is "+UUID + "  "+rawData +"   " +timeout;

        Log.d("Tracer",meesage);

        startAdvertising();
        setTimeout();
        //timeout
        //UUID
        //rawData
        Log.d("Tracer","AdvertiserService onStartCommand end");
        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void onDestroy()
    {
        Log.d("Tracer","AdvertiserService onDestroy start");
        running = false;
        stopAdvertising();
        mHandler.removeCallbacks(timeoutRunnable);
        Log.d("Tracer","AdvertiserService onDestroy end");
        super.onDestroy();
        // TODO Auto-generated method stub
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }




    private void initialize() {
        Log.d("Tracer","AdvertiserService initialize start");
        if (mBluetoothLeAdvertiser == null) {
            BluetoothManager mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager != null) {
                BluetoothAdapter mBluetoothAdapter = mBluetoothManager.getAdapter();
                if (mBluetoothAdapter != null) {
                    mBluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
                } else {
                    //Toast.makeText(this, getString(R.string.bt_null), Toast.LENGTH_LONG).show();
                }
            } else {
                //Toast.makeText(this, getString(R.string.bt_null), Toast.LENGTH_LONG).show();
            }
        }
        Log.d("Tracer","AdvertiserService initialize end");

    }
    private void startAdvertising() {
        //goForeground();

        Log.d("Tracer","AdvertiserService startAdvertising start");

        if (mAdvertiseCallback == null) {
            AdvertiseSettings settings = buildAdvertiseSettings();
            AdvertiseData data = buildAdvertiseData();
            //AdvertiseData scanresult = buildScanResultData();
            mAdvertiseCallback = new AdvertiserService.SampleAdvertiseCallback();

            /*if (mBluetoothLeAdvertiser != null) {
                mBluetoothLeAdvertiser.startAdvertising(settings, data,
                    mAdvertiseCallback);
                Log.d(TAG, "Wareable: Starting Advertising!"+ data);
            }*/
            if (mBluetoothLeAdvertiser != null) {
                //mBluetoothLeAdvertiser.startAdvertising(settings, data,scanresult, mAdvertiseCallback);
                mBluetoothLeAdvertiser.startAdvertising(settings, data, mAdvertiseCallback);
                Log.d(TAG, "Wareable: Starting Advertising!" + data + "\n");
                //Log.d(TAG, "Wareable: scanresult"+ scanresult + "\n");
            }
        }
        Log.d("Tracer","AdvertiserService startAdvertising end");
    }


    private void setTimeout(){
        Log.d("Tracer","AdvertiserService setTimeout start");
        mHandler = new Handler();
        timeoutRunnable = new Runnable() {
            @Override
            public void run() {
                Log.d("Tracer", "AdvertiserService has reached timeout of "+timeout+"seconds, stopping advertising.");
                stopSelf();
            }
        };
        mHandler.postDelayed(timeoutRunnable, timeout * 1000);
        Log.d("Tracer","AdvertiserService setTimeout end");
    }

    private AdvertiseData buildAdvertiseData() {

        /**
         * Note: There is a strict limit of 31 Bytes on packets sent over BLE Advertisements.
         *  This includes everything put into AdvertiseData including UUIDs, device info, &
         *  arbitrary service or manufacturer data.
         *  Attempting to send packets over this limit will result in a failure with error code
         *  AdvertiseCallback.ADVERTISE_FAILED_DATA_TOO_LARGE. Catch this error in the
         *  onStartFailure() method of an AdvertiseCallback implementation.
         */

        AdvertiseData.Builder dataBuilder = new AdvertiseData.Builder();
        dataBuilder.addServiceUuid(Constants.Service_UUID);
        //dataBuilder.setIncludeDeviceName(true);

        /* For example - this will cause advertising to fail (exceeds size limit) */
        String failureData = "1234";
        dataBuilder.addServiceData(Constants.Service_UUID, failureData.getBytes());

        return dataBuilder.build();
    }

    /**
     * Returns an AdvertiseSettings object set to use low power (to help preserve battery life)
     * and disable the built-in timeout since this code uses its own timeout runnable.
     */
    private AdvertiseSettings buildAdvertiseSettings() {
        AdvertiseSettings.Builder settingsBuilder = new AdvertiseSettings.Builder();
        settingsBuilder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY);
        settingsBuilder.setTimeout(0);
        settingsBuilder.setConnectable(false);
        return settingsBuilder.build();
    }


    private class SampleAdvertiseCallback extends AdvertiseCallback {

        @Override
        public void onStartFailure(int errorCode) {
            super.onStartFailure(errorCode);

            Log.d(TAG, "Advertising failed");
            sendFailureIntent(errorCode);
            stopSelf();

        }

        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
            Log.d(TAG, "Advertising successfully started");
        }
    }
    private void stopAdvertising() {
        Log.d(TAG, "Service: Stopping Advertising");
        if (mBluetoothLeAdvertiser != null) {
            mBluetoothLeAdvertiser.stopAdvertising(mAdvertiseCallback);
            mAdvertiseCallback = null;
        }
    }
    private void sendFailureIntent(int errorCode) {
        Intent failureIntent = new Intent();
        failureIntent.setAction(ADVERTISING_FAILED);
        failureIntent.putExtra(ADVERTISING_FAILED_EXTRA_CODE, errorCode);
        sendBroadcast(failureIntent);
    }


}
