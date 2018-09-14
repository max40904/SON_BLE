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
        TimeSlot = null;
        if (devicemap.size()!=0) {
            int[] path = PathAlgorithmInterface.getShortestPath(devicemap);
            int[] newpath = new int[devicemap.size() * 2];
            int x = 0;
            for (int i = path.length - 1; i >= 0; i--) {
                newpath[x * 2] = path[i];
                x++;
            }


            x = 0;
            for (int i = path.length - 2; i >= 0; i--) {
                newpath[x * 2 + 1] = path[i];
                x++;
            }
            newpath[devicemap.size() * 2 - 1] = 90;
            TimeSlot = newpath;
        }


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
    //get B {[A][B]}
    public int getSendToNode(String nNode){
        int nodename = Integer.parseInt(nNode);
        for (int i = 0  ; i < slotnumber ; i++){
            if (TimeSlot[i*2] == nodename){
                return  TimeSlot[ i * 2  + 1] ;
            }

        }
        return 0;
    }

    public int getTimeSlotWhoSend(int n){

        return TimeSlot[n*2];
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
