package blework;

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
    private String UUID;
    //context = acitivy context , UUID = MAC ADDRESS, flag: (true = gw ) (false = node)
    public BleInterface(Context context, String UUID, boolean flag){
        Log.d("Tracer","BleInterface start");
        this.context = context;
        scanpart = new ScanPart(context, flag);
        this.UUID = UUID;
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
    public void startAdvertising(String nodename, byte [] data, int timelimit) {
        Log.d("Tracer","BleInterface startAdvertising start");
        context.startService(getAdvertisingIntent(nodename,data,timelimit));
        Log.d("Tracer","BleInterface startAdvertising end");

    }

    /**
     * Stops BLE Advertising by stopping {@code AdvertiserService}.
     */
    public void stopAdvertising() {
        Log.d("Tracer","BleInterface stopAdvertising start");

        context.stopService(getIntent());
        Log.d("Tracer","BleInterface stopAdvertising end");
    }

    public Intent getAdvertisingIntent(String nName,byte [] rawData, int time){
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putString(AdvertiserService.intentArgument,nName);
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
    public void startScan(int filter){
        scanpart.startScanning(filter, SONConstants.timeslot);
    }
    //listen all
    public void startScan(){

        scanpart.startScanning(0, SONConstants.timeslot);
    }

    public void startScan(int filter, int time){

        scanpart.startScanning(filter, time*1000 );
    }
    public void stopScan(){
        scanpart.stopScanning();
    }



}
