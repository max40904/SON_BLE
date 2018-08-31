package BLE;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import SON.SONConstants;

public class BleInterface {
    private Context context;
    private BluetoothAdapter btAdapter;
    private ScanPart scanpart;
    public BleInterface(Context context){
        Log.d("Tracer","BleInterface start");
        this.context = context;
        scanpart = new ScanPart(context);
        Log.d("Tracer","BleInterface end");
    }
    public void CreateAdervtiserService(){
        Log.d("Tracer","BleInterface start");

        Log.d("Tracer","BleInterface end");
    }



    /**
    * Advertising:
    * startAdvertising
    * stopAdvertising
    * getAdvertisingIntent
    * getIntent
    *
    *
    *
    * */
    public void startAdvertising(String uuid, byte [] data, int timelimit) {
        Log.d("Tracer","BleInterface startAdvertising start");
        context.startService(getAdvertisingIntent(uuid,data,timelimit));
        Log.d("Tracer","BleInterface startAdvertising end");

    }

    /**
     * Stops BLE Advertising by stopping {@code AdvertiserService}.
     */
    public void stopAdvertising() {


        context.stopService(getIntent());

    }

    public Intent getAdvertisingIntent(String UUID,byte [] rawData, int time){
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putString(AdvertiserService.intentArgument,UUID);
        bundle.putByteArray(AdvertiserService.intentArgument2,rawData);
        bundle.putInt(AdvertiserService.intentArgument3, time);

        intent.putExtras(bundle);

        return intent;
    }
    public Intent getIntent(){
        Intent intent = new Intent(context, AdvertiserService.class);
        return intent;
    }

    /**
     * SCAN
     *setBluetoothAdapter
     *startScan(String target, int time)
     *startScan(String target)//default slot time
     *startScan() //listen all(no target), default slot time
     * */

    public void setBluetoothAdapter(BluetoothAdapter btAdapter){
        this.btAdapter = btAdapter;
        scanpart.setBleAdapter(btAdapter);
    }
    //listen someone
    public void startScan(int target){
        scanpart.startScanning(target, SONConstants.timeslot);
    }
    //listen all
    public void startScan(){

        scanpart.startScanning(-1, SONConstants.timeslot);
    }

    public void startScan(int target, int time){

        scanpart.startScanning(target, time );
    }



}
