package SecondCounter;

import java.util.Calendar;

public class SecondCounter {
    public Calendar time ;
    public int second;
    public SecondCounter(int second){
        time = Calendar.getInstance();
        this.second = second;
    }
    public boolean checkTimeOut(){
        Calendar currenttime = Calendar.getInstance();

        int currenttimesum = sumTime(currenttime);
        int timesum = sumTime(time);
        if (currenttimesum  - second <= timesum){
            return false;
        }

        return true;

    }
    private int sumTime(Calendar time){
        int sum = time.get(Calendar.SECOND) + time.get(Calendar.MINUTE) * 60 +  time.get(Calendar.HOUR)* 60 * 60 ;
        return sum;
    }
}
