package SON.SONNode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.max40904.son.MainActivity;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Timer;

import BLE.BleInterface;
import GUI.SONNodeFragment;
import Packet.BLEDataType;
import Packet.PackageJoin;
import SON.SONConstants;
import java.util.TimerTask;

import SON.TimeSchedule;

public class SONNode {
    String NodeName;
    private Timer Jointimer = new Timer();
    private Timer Receivetimer = new Timer();
    private Timer Scheduletimer = new Timer();
    private Timer Sendtimer = new Timer();
    private Context context;
    private  BleInterface ble;
    private  String fakemac;
    private String nodename;
    public static final String RECEIVER_INTENT = "RECEIVER_INTENT";
    public static final String RECEIVER_MESSAGE = "RECEIVER_MESSAGE";

    public SONNode(Context context, BleInterface ble){
        NodeName = "unknown";
        Jointimer = new Timer();
        Receivetimer = new Timer();
        Scheduletimer = new Timer();
        Sendtimer = new Timer();
        this.context = context;
        this.ble  = ble;


    }
    public void setDID(String DID){
        NodeName = DID;
    }
    public void setMacAddress(String mac){
        fakemac = mac;
    }
    public void startJoinTime(){
        TimerTask rectask = new JoinSlot(context);

        Jointimer.schedule(rectask,0,60*1000 );
    }

    public void stopJoinTime(){

        Jointimer.cancel();

    }

    public void setCycleTime(Calendar packetttime, TimeSchedule slotschedule){
        //receive
        TimerTask rectask = new ReceiveSlot(context);
        Calendar rec = (Calendar) packetttime.clone();
        rec.add(Calendar.SECOND, SONConstants.timeslot * 1 );
        Receivetimer.schedule(rectask,rec.getTime() );

        //send
        TimerTask sendtask = new SendSlot(context);
        Calendar sen = (Calendar) packetttime.clone();
        sen.add(Calendar.SECOND, SONConstants.timeslot * 2 );
        Sendtimer.schedule(sendtask,sen.getTime() );

        //timeschedule
        TimerTask schedtask = new ScheduleSlot(context);
        Calendar sched = (Calendar) packetttime.clone();
        sched.add(Calendar.SECOND, SONConstants.timeslot * 3 );
        Sendtimer.schedule(schedtask,sched.getTime() );

    }

    public class JoinSlot extends TimerTask {
        private Context context;
        public JoinSlot(Context context){
            this.context = context;
        }
        @Override
        public void run() {
            PackageJoin packet = new PackageJoin(fakemac);

            ble.startAdvertising(BLEDataType.Join_string, packet.getBroadcastData(), 60);
            ble.startScan(BLEDataType.AckJoin,60);
            Log.d("Tracer","JoinSlot");
        }
    }
    public class SendSlot extends TimerTask {
        private Context context;
        public SendSlot(Context context){
            this.context = context;
        }
        @Override
        public void run() {
            byte [] test = new byte[10];
            Arrays.fill( test, (byte) 1 );

            ble.startAdvertising("0001", test, 5);


            Log.d("Tracer","SendSlot");
        }
    }
    public class ScheduleSlot  extends TimerTask {
        private Context context;
        public ScheduleSlot(Context context){
            this.context = context;
        }
        @Override
        public void run() {
            Bundle bundle = new Bundle();
            bundle.putString(SONNodeFragment.SETSCHDULE_MESSAGE,"i am come from onResume");


            Intent intent = new Intent(SONNodeFragment.SETSCHDULE_INTENT);
            intent.putExtras(bundle);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            Log.d("Tracer","ScheduleSlot");
        }
    }

    public class ReceiveSlot extends TimerTask {
        private Context context;
        public ReceiveSlot(Context context){
            this.context = context;
        }
        @Override
        public void run() {
            Log.d("Tracer","ReceiveSlot");
        }
    }



}
