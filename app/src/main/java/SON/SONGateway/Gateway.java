package SON.SONGateway;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import BLE.BleInterface;
import GUI.SONGWFragment;
import Packet.BLEDataType;
import Packet.PackageTimeSchedule;
import SON.DeviceInformation;
import SON.SONConstants;

import SON.TimeSchedule;
import java.util.Map;

public class Gateway {
    String GWName;
    private Timer Jointimer = new Timer();
    private Timer ReceiveJointimer = new Timer();
    private Timer SendScheduletimer = new Timer();
    private Timer Sendtimer = new Timer();
    private Context context;
    private BleInterface ble;



    public  Gateway(Context context, BleInterface ble){
        GWName = "unknown";
        Jointimer = new Timer();
        Sendtimer = new Timer();
        this.context = context;
        this.ble = ble;
    }
    public void setDID(String DID){
        GWName = DID;
    }


    public void setCycleTime(Calendar packetttime, TimeSchedule slotschedule){
        //time schedule time
        TimerTask ReceiveJoin = new ReceiveJoinSlot(context);
        Calendar rec = (Calendar) packetttime.clone();
        rec.add(Calendar.SECOND, SONConstants.timeslot * 1 );
        Jointimer.schedule(ReceiveJoin,rec.getTime() );
        //receive joining packet
        TimerTask SendSlot = new SendScheduleSlot(context);
        Calendar sen = (Calendar) packetttime.clone();
        sen.add(Calendar.SECOND, SONConstants.timeslot * 2 );
        Sendtimer.schedule(SendSlot,sen.getTime() );


    }
    //lost judge
    public void judgement(){


    }
    public class ReceiveJoinSlot extends TimerTask {
        private Context context;
        public ReceiveJoinSlot(Context context){
            this.context = context;
        }
        @Override
        public void run() {
            ble.startScan(BLEDataType.Join, SONConstants.timeslot);
            Log.d("Tracer","ReceiveJoinSlot");
        }
    }
    public class SendScheduleSlot extends TimerTask {
        private Context context;
        public SendScheduleSlot(Context context){
            this.context = context;
        }
        @Override
        public void run() {

            Log.d("Tracer","SendScheduleSlot");
            //public PackageTimeSchedule(int minute ,int second ,int slotnumber ,int[] slotstate )
            int [] newarray = {1,2,2,3};
            PackageTimeSchedule timepacket = new PackageTimeSchedule(5,25,2,newarray);
            ble.startAdvertising(BLEDataType.Timeschdule_string,timepacket.getBroadcastData(), 5);


            Bundle bundle = new Bundle();
            bundle.putString(SONGWFragment.GW_SETSCHDULE_MESSAGE,"i am come from onResume");
            Intent intent = new Intent(SONGWFragment.GW_SETSCHDULE_INTENT);

            intent.putExtras(bundle);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

        }
    }




}
