package SON.SONNode;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Timer;

import blework.BleInterface;
import GUI.SONNodeFragment;
import Packet.BLEDataType;
import Packet.PackageJoin;
import Packet.PackageNode;
import SON.SONConstants;
import java.util.TimerTask;
import Converter.Converter;
import SON.TimeSchedule;

public class SONNode {

    private Timer Jointimer = new Timer();
    private Timer Receivetimer = new Timer();
    private Timer Scheduletimer = new Timer();
    private Timer Sendtimer = new Timer();
    private Timer GUItimer = new Timer();
    private Timer GUI2timer = new Timer();
    private Context context;
    private  BleInterface ble;
    private String fakemac;
    private String serialname;
    public static final String RECEIVER_INTENT = "RECEIVER_INTENT";
    public static final String RECEIVER_MESSAGE = "RECEIVER_MESSAGE";
    private byte [] messagecontent;
    private byte [] reccontent;
    private String targetrecnode;
    private int locX;
    private int locY;
    public SONNode(Context context, BleInterface ble){
        serialname = "unknown";
        Jointimer = new Timer();
        Receivetimer = new Timer();
        Scheduletimer = new Timer();
        Sendtimer = new Timer();
        this.context = context;
        this.ble  = ble;


    }
    public void setSerialname(String DID){
        serialname = DID;
        messagecontent = serialname.substring(3,4).getBytes();
    }
    public void setMacAddress(String mac){
        fakemac = mac;
    }
    public void startJoinTime(){
        TimerTask rectask = new JoinSlot(context);

        Jointimer.schedule(rectask,0,60*1000 );
    }
    public void setReceicontent(byte [] content){

        reccontent = content;
    }

    public void setLoc(int x, int y){
        locX = x;
        locY = y;
    }
    public void stopJoinTime(){

        Jointimer.cancel();

    }
    public String getTargetNode(){
        return targetrecnode;
    }

    public void setCycleTime(Calendar packetttime, TimeSchedule slotschedule){
        setReceicontent(null);
        //receive
        if (slotschedule.getReceiveSlot(serialname) !=-1) {
            TimerTask rectask = new ReceiveSlot(context);
            Calendar rec = (Calendar) packetttime.clone();
            rec.add(Calendar.SECOND, SONConstants.timeslot * slotschedule.getReceiveSlot(serialname));
            Receivetimer.schedule(rectask, rec.getTime());
            targetrecnode = "" + slotschedule.getTargetNode(serialname);
            sendIntentToGUI("receiNodeTextView", 2 , targetrecnode);

        }
        //send
        TimerTask sendtask = new SendSlot(context);
        Calendar sen = (Calendar) packetttime.clone();
        sen.add(Calendar.SECOND, SONConstants.timeslot * slotschedule.getSendSlot(serialname) );
        Sendtimer.schedule(sendtask,sen.getTime() );
        String SendTOStr = ""+slotschedule.getSendToNode(serialname);
        sendIntentToGUI("sendNodeTextView", 2 , SendTOStr);

            TimerTask Gui2lot = new GUISlot(context);
            Calendar gcal2 = (Calendar) packetttime.clone();
            gcal2.add(Calendar.SECOND, SONConstants.timeslot * ( 1 +slotschedule.getSendSlot(serialname) ) );
            GUI2timer.schedule(Gui2lot,gcal2.getTime() );

        //timeschedule
        TimerTask schedtask = new ScheduleSlot(context);
        Calendar sched = (Calendar) packetttime.clone();
        sched.add(Calendar.SECOND, SONConstants.timeslot * slotschedule.getTimeSchduleSlot() );
        Sendtimer.schedule(schedtask,sched.getTime() );



        //GUI
        TimerTask GCISlot = new GUISlot(context);
        Calendar gcal = (Calendar) packetttime.clone();
        gcal.add(Calendar.SECOND, SONConstants.timeslot * ( 1 +slotschedule.getTimeSchduleSlot()) );
        GUItimer.schedule(GCISlot,gcal.getTime() );


    }
    public void sendIntentToGUI(String target ,int flag, String meessage ){
        Bundle bundle = new Bundle();
        bundle.putString(SONNodeFragment.GUI_TARGET, target);
        bundle.putInt(SONNodeFragment.GUI_FLAG, flag);
        bundle.putString(SONNodeFragment.GUI_MESSAGE, meessage);
        Intent intent = new Intent(SONNodeFragment.GUI_INTENT);

        intent.putExtras(bundle);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
    public class JoinSlot extends TimerTask {
        private Context context;
        public JoinSlot(Context context){
            this.context = context;
        }
        @Override
        public void run() {
            PackageJoin packet = new PackageJoin(fakemac,locX,locY);

            ble.startAdvertising(BLEDataType.Join_string, packet.getBroadcastData(), 60);
            ble.startScan(BLEDataType.AckJoin,60);
            Log.d("TimerTask","JoinSlot");
            sendIntentToGUI("askJoinTextView", 1 , "");
            sendIntentToGUI("recNodeTextView", 0 , "");
            sendIntentToGUI("adNodeTextView", 0 , "");
            sendIntentToGUI("tschduleTextView", 0, "");
        }
    }
    public class SendSlot extends TimerTask {
        private Context context;
        public SendSlot(Context context){
            this.context = context;
        }
        @Override
        public void run() {
            ble.stopScan();
            byte [] test = new byte[10];
            Arrays.fill( test, (byte) 1 );
            //public PackageNode( String serialnumber, int dataamount, byte [] constructdata)
            byte [] temp = new byte [2];
            temp[0] = 0x01;
            temp[1] = 0x03;

            byte [] sendbyte = messagecontent;
            if (reccontent!=null){
                sendbyte = Converter.byteArrayCombinebyteArray(messagecontent,reccontent);
            }

            Log.d("Node_meesage",  new String (sendbyte,StandardCharsets.UTF_8));

            PackageNode packetnode = new PackageNode(serialname, sendbyte.length , sendbyte);

            ble.startAdvertising(BLEDataType.Node_string, packetnode.getBroadcastData(), 5);
            sendIntentToGUI("askJoinTextView", 0 , "");
            sendIntentToGUI("recNodeTextView", 0 , "");
            sendIntentToGUI("adNodeTextView", 1 , "");
            sendIntentToGUI("tschduleTextView", 0, "");

            Log.d("TimerTask","SendSlot");
        }
    }

    public class ReceiveSlot extends TimerTask {
        private Context context;
        public ReceiveSlot(Context context){
            this.context = context;
        }
        @Override
        public void run() {
            ble.stopScan();
            ble.startScan(BLEDataType.Node,SONConstants.timeslot);
            sendIntentToGUI("askJoinTextView", 0 , "");
            sendIntentToGUI("recNodeTextView", 1 , "");
            sendIntentToGUI("adNodeTextView", 0, "");
            sendIntentToGUI("tschduleTextView", 0, "");
            Log.d("TimerTask","ReceiveSlot");
        }
    }

    public class ScheduleSlot  extends TimerTask {
        private Context context;
        public ScheduleSlot(Context context){
            this.context = context;
        }
        @Override
        public void run() {
            ble.stopScan();
            ble.startScan(BLEDataType.Timeschdule,SONConstants.timeslot);
            sendIntentToGUI("askJoinTextView", 0 , "");
            sendIntentToGUI("recNodeTextView", 0 , "");
            sendIntentToGUI("adNodeTextView", 0, "");
            sendIntentToGUI("tschduleTextView", 1, "");

            Log.d("TimerTask","ScheduleSlot");
        }
    }


    public class GUISlot  extends TimerTask {
        private Context context;
        public GUISlot(Context context){
            this.context = context;
        }
        @Override
        public void run() {

            sendIntentToGUI("askJoinTextView", 0 , "");
            sendIntentToGUI("recNodeTextView", 0 , "");
            sendIntentToGUI("adNodeTextView", 0, "");
            sendIntentToGUI("tschduleTextView", 0, "");


        }
    }





}
