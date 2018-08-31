package SON;
import java.util.Calendar;

import Packet.PackageTimeSchedule;
public  class TimeSchedule {
    private int [] TimeSlot;
    private PackageTimeSchedule packet;
    private Calendar calendar;
    public TimeSchedule(){

    }
    public TimeSchedule(PackageTimeSchedule packet, Calendar calendar){
        this.packet = packet;
        this.calendar = calendar;
    }
    public int getMySlot(String slot){
        int n = Integer.parseInt(slot) - 1 ;

        byte [] test = packet.getSlotState();
        byte p = test[n];
        int v2 = p & 0xFF;
        return v2;

    }
    public int getTimeSchduleSlot(String slot){
        return packet.getSlotNumber();
    }
                                
    public Calendar getCalender(){
        return calendar;

    }

}
