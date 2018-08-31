package BLE;

import android.os.ParcelUuid;
import android.util.Log;

public class Constants {
    /*public static final ParcelUuid Service_UUID = ParcelUuid
            .fromString("0000b81d-0000-1000-8000-00805f9b34fb");*/
    public static final ParcelUuid Service_UUID = ParcelUuid
            .fromString("00007300-0000-1000-8000-00805f9b34fb");

    public static final int REQUEST_ENABLE_BT = 1;

    public static ParcelUuid getService_UUID(String id){
        Log.d ("Tracer", "Constants getService_UUID start");
        String uuid ;
        String afterpart = "-0000-1000-8000-00805f9b34fb";
        if (id.length()==8){
            uuid = id + afterpart;
        }
        else if (id.length() == 4){
            uuid = "0000" + id + afterpart;
        }
        else{
            // wrong length
            Log.d ("Tracer","fail length");
            return null;
        }
        Log.d ("Tracer", uuid);
        Log.d ("Tracer", "Constants getService_UUID end");
        return ParcelUuid.fromString(uuid);
    }


}
