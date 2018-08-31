package SON.SONNode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.max40904.son.MainActivity;

import java.util.Calendar;
import java.util.Timer;
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


    public static final String RECEIVER_INTENT = "RECEIVER_INTENT";
    public static final String RECEIVER_MESSAGE = "RECEIVER_MESSAGE";

    public SONNode(Context context){
        NodeName = "unknown";
        Jointimer = new Timer();
        Receivetimer = new Timer();
        Scheduletimer = new Timer();
        Sendtimer = new Timer();
        this.context = context;


    }
    public void setDID(String DID){
        NodeName = DID;
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




}
