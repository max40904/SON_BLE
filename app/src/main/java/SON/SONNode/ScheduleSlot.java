package SON.SONNode;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.TimerTask;

import GUI.SONNodeFragment;

public class ScheduleSlot  extends TimerTask {
    private Context context;
    public ScheduleSlot(Context context){
        this.context = context;
    }
    @Override
    public void run() {
        Intent intent = new Intent(SONNodeFragment.RECEIVER_INTENT);
        intent.putExtra(SONNodeFragment.RECEIVER_MESSAGE, "i am come from onResume");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        Log.d("Tracer","ScheduleSlot");
    }
}
