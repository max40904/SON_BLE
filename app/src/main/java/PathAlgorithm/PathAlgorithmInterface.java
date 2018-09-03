package PathAlgorithm;


import android.util.Log;

import java.util.Map;

import SON.DeviceInformation;

public class PathAlgorithmInterface {
    public static int [] getPathResult(Map<String,DeviceInformation> devicemap){
        int  devicenumber = devicemap.size() ;
        if (devicenumber  == 0){
            return null;
        }
        int [] slotmember = new int[devicenumber * 2 ];
        int i = 0;
        for (Map.Entry<String, DeviceInformation> entry : devicemap.entrySet()){
            DeviceInformation value = entry.getValue();
            Log.d("PathAlgorithmInterface", "getPathResult");
            Log.d("PathAlgorithmInterface", value.getSerialnumber());
            slotmember [i] = Integer.valueOf( value.getSerialnumber());

            i = i + 2;
        }

        for (int j = 1 ; j < devicenumber * 2 - 1 ; j+=2){
            slotmember [j] = slotmember [j+1];

        }
        slotmember [devicenumber * 2 - 1 ] = 90;
        return slotmember;

    }

}
