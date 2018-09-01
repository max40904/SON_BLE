package SON.SONGateway;

import android.content.Context;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import SON.SONConstants;

import SON.TimeSchedule;

public class Gateway {
    String GWName;
    private Timer Jointimer = new Timer();
    private Timer ReceiveJointimer = new Timer();
    private Timer SendScheduletimer = new Timer();
    private Timer Sendtimer = new Timer();
    private Context context;
    public  Gateway(Context context){
        GWName = "unknown";
        Jointimer = new Timer();
        Sendtimer = new Timer();
        this.context = context;

    }
    public void setDID(String DID){
        GWName = DID;
    }
    //time schedule time

    //receive joining packet
    public void setCycleTime(){
        //time schedule time

        //receive joining packet

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

        }
    }
    public class SendScheduleSlot extends TimerTask {
        private Context context;
        public SendScheduleSlot(Context context){
            this.context = context;
        }
        @Override
        public void run() {

        }
    }




}
