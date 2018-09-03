package SON;
import java.sql.Time;
import java.util.Calendar;
import java.util.Map;
import PathAlgorithm.PathAlgorithmInterface;

import Packet.PackageTimeSchedule;
public  class TimeSchedule {
    private int [] TimeSlot;
    private int slotnumber;
    Map<String,DeviceInformation> devicemap;
    public TimeSchedule(Map<String,DeviceInformation> devicemap){
        this.devicemap = devicemap;
        slotnumber = devicemap.size();
        TimeSlot = PathAlgorithmInterface.getPathResult(devicemap);
    }
    public TimeSchedule(int slotnumber, int [] TimeSlot){
        this.slotnumber = slotnumber;
        this.TimeSlot = TimeSlot;
    }
    public int getSendSlot(String nNode){
        if (slotnumber == 0 ){
            return 0;
        }
        int nodename = Integer.parseInt(nNode);
        for (int i = 0  ; i < slotnumber ; i++){
            if (TimeSlot[i*2] == nodename){
                return i  ;
            }

        }
        return -1;
    }

    public int getReceiveSlot(String nNode){
        if (slotnumber == 0 ){
            return 0;
        }
        int nodename = Integer.parseInt(nNode);
        for (int i = 0  ; i < slotnumber ; i++){
            if (TimeSlot[i*2+ 1] == nodename){
                return i ;
            }

        }
        return -1;
    }
    public int getRecJoinSlot(){
        return slotnumber  ;
    }
    public int getTimeSchduleSlot(){
        return slotnumber + 1 ;
    }
    public int getSumSlot(){
        return slotnumber  + 2 ;
    }

    public int [] getSlotSchduler(){
        return TimeSlot;
    }
    //get A {[A] [B]}
    public int getTargetNode(String nNode){
        int nodename = Integer.parseInt(nNode);
        for (int i = 0  ; i < slotnumber ; i++){
            if (TimeSlot[i*2+1] == nodename){
                return  TimeSlot[ i * 2 ] ;
            }

        }
        return 0;
    }
//    public TimeSchedule(PackageTimeSchedule packet, Calendar calendar){
//        this.packet = packet;
//        this.calendar = calendar;
//    }
//    public int getMySlot(String slot){
//        int n = Integer.parseInt(slot) - 1 ;
//
//        byte [] test = packet.getSlotState();
//        byte p = test[n];
//        int v2 = p & 0xFF;
//        return v2;
//
//    }
//    public int getTimeSchduleSlot(String slot){
//        return packet.getSlotNumber();
//    }
//
//    public Calendar getCalender(){
//        return calendar;
//
//    }

}
