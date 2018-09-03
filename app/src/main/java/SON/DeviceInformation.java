package SON;

public class DeviceInformation {
    public String serialnumber;
    public boolean state;
    public int x;
    public int y;
    public DeviceInformation(String serialnumber, boolean state, int x, int y ){
        this.serialnumber =serialnumber;
        this.state = state;
        this.x = x;
        this.y = y;
    }

    public String getSerialnumber(){
        return serialnumber;
    }

    public boolean getState(){
        return state;
    }

    public int getLOC_X(){
        return x;
    }

    public int getLOC_Y(){
        return y;
    }
}
