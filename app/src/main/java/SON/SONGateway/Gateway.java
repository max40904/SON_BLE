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
import PathAlgorithm.PathAlgorithmInterface;

public class Gateway {
    String GWName;
    String serialname  = "0090";
    private Timer ReceiveNodetimer ;
    private Timer ReceiveJointimer ;
    private Timer SendScheduletimer ;
    private Calendar schduletime;
    private Context context;
    private BleInterface ble;
    private String targetrecnode;
    private Map<String,DeviceInformation> devicemap;


    public  Gateway(Context context, BleInterface ble){
        GWName = "unknown";
        ReceiveNodetimer = new Timer();
        ReceiveJointimer = new Timer();
        SendScheduletimer = new Timer();
        this.context = context;
        this.ble = ble;
    }

    public String getTargetNode(){
        return targetrecnode;
    }
    public void setDeviceInformation(Map <String,DeviceInformation> map){
        devicemap = map;
    }



    public void setCycleTime(Calendar packetttime, TimeSchedule slotschedule){
        if (slotschedule.getSumSlot() > 2 ) {
            //ReceiveNode
            TimerTask ReceiveNode = new ReceiveNodeSlot(context);
            Calendar rn = (Calendar) packetttime.clone();
            rn.add(Calendar.SECOND, SONConstants.timeslot *  ( slotschedule.getReceiveSlot(serialname)  )  );
            ReceiveNodetimer.schedule(ReceiveNode, rn.getTime());
            targetrecnode = "" + slotschedule.getTargetNode(serialname);
        }
        //ReceiveJoin
        TimerTask ReceiveJoin = new ReceiveJoinSlot(context);
        Calendar rec = (Calendar) packetttime.clone();
        rec.add(Calendar.SECOND, SONConstants.timeslot *  ( slotschedule.getRecJoinSlot()) );
        ReceiveJointimer.schedule(ReceiveJoin,rec.getTime() );
        //SendTimeSchdule
        TimerTask SendSlot = new SendScheduleSlot(context);
        Calendar sen = (Calendar) packetttime.clone();
        sen.add(Calendar.SECOND, SONConstants.timeslot * ( slotschedule.getTimeSchduleSlot()) );
        SendScheduletimer.schedule(SendSlot,sen.getTime() );


    }
    //lost judge
    public void judgement(){


    }
    //get "now" scheduletime
    public Calendar getNowSchduletime(){
        return schduletime;
    }
    //get "next" scheduletime
    public Calendar getNextCurrentSchduleTime(){
        Calendar currenttime = Calendar.getInstance();
        Calendar newtime = Calendar.getInstance();
        int hour = currenttime.get((Calendar.HOUR));
        int minute = currenttime.get((Calendar.MINUTE));
        int second = currenttime.get((Calendar.SECOND));

        if (second != 0){
            newtime.set(Calendar.SECOND,0);
        }
        if (minute!=59){
            newtime.set(Calendar.MINUTE,minute + 1 );
        }
        else{
            newtime.set(Calendar.MINUTE,0 );
            newtime.set(Calendar.HOUR,hour + 1 );
        }

        return newtime;
    }
    //set next
    public void setCurrentSchduleTime(Calendar currenttime){
        schduletime = currenttime;
    }
    /**
     * Receive packet from node
     *
     *
     * */
    public class ReceiveNodeSlot extends TimerTask {
        private Context context;
        public ReceiveNodeSlot(Context context){
            this.context = context;
        }
        @Override
        public void run() {
            ble.stopScan();
            ble.startScan(BLEDataType.Node, SONConstants.timeslot);
            Log.d("TimerTask","ReceiveNodeSlot");
        }
    }

    /**
     * Receive Packet from node which want to join internet
     * Can receive A lot of same time
     *
     *
     * */

    public class ReceiveJoinSlot extends TimerTask {
        private Context context;
        public ReceiveJoinSlot(Context context){
            this.context = context;
        }
        @Override
        public void run() {
            ble.stopScan();
            ble.startScan(BLEDataType.Join, SONConstants.timeslot);
            Log.d("TimerTask","ReceiveJoinSlot");
        }
    }
    /**
     * SendBroadcasttime to All Node
     *
     *
     *
     * */
    public class SendScheduleSlot extends TimerTask {
        private Context context;
        public SendScheduleSlot(Context context){
            this.context = context;
        }
        @Override
        public void run() {
            ble.stopScan();
            Log.d("TimerTask","SendScheduleSlot");
            //public PackageTimeSchedule(int minute ,int second ,int slotnumber ,int[] slotstate )
            int [] newarray ;

            schduletime= getNextCurrentSchduleTime( );

            TimeSchedule sch  = new TimeSchedule(devicemap);
            if (sch.getSumSlot() > 2) {
                PackageTimeSchedule timepacket = new PackageTimeSchedule(schduletime.get(Calendar.MINUTE), schduletime.get(Calendar.SECOND), sch.getSumSlot() - 2, sch.getSlotSchduler());
                ble.startAdvertising(BLEDataType.Timeschdule_string, timepacket.getBroadcastData(), 5);
            }

            Bundle bundle = new Bundle();
            bundle.putString(SONGWFragment.GW_SETSCHDULE_MESSAGE,"i am come from SendSchduleSlot");
            Intent intent = new Intent(SONGWFragment.GW_SETSCHDULE_INTENT);

            intent.putExtras(bundle);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

        }
    }






}
