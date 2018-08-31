package SON.SONNode;

import android.content.Context;
import android.util.Log;

import java.util.TimerTask;

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
