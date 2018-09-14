package PathAlgorithm;


import android.util.Log;

import java.util.ArrayList;
import java.util.Map;

import SON.DeviceInformation;

public class PathAlgorithmInterface {

    private ArrayList<DeviceInformation> devs ;


    public static int [] getShortestPath(Map<String,DeviceInformation> devices){
        if (devices.size() ==1){
            int [] anw = new int [1];
            for (Map.Entry<String, DeviceInformation> entry : devices.entrySet()){
                entry.getValue();
                anw[0] =  Integer.parseInt(entry.getValue().getSerialnumber());
            }
            return anw;

        }
        ArrayList<DeviceInformation> devs = new  ArrayList<DeviceInformation>();
        for (Map.Entry<String, DeviceInformation> entry : devices.entrySet()){
            devs.add(entry.getValue());
        }

        int time = devs.size();
        int [] anwser = new int [time];
        int temproiginX = 0;
        int temproiginY = 0;
        int distanance = 9999999;
        DeviceInformation target   = null;
        for (int i = 0 ; i < time ;i++){
            distanance = 9999999;
            for (DeviceInformation temp : devs) {


                int tempdis = (temproiginX - temp.getX())*(temproiginX - temp.getX()) +(temproiginY - temp.getY()) * (temproiginY - temp.getY());

                if (tempdis < distanance){
                    System.out.println(i +" " +tempdis + " " +  distanance);
                    target = temp;
                    distanance = tempdis;
                }

            }
            temproiginX = target.getX();
            temproiginY = target.getY();
            anwser[i] = Integer.parseInt(target.getSerialnumber());
            devs.remove(target);

        }



        return anwser;
    }

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
