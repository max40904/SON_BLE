package Packet;

import java.lang.reflect.Array;
import java.util.Arrays;

public class ServiceDataFormat extends  BLEdataFormat{
    public ServiceDataFormat(int length, int type, byte [] data){
        super(length,type,data);
    }
    public ServiceDataFormat(byte [] databyte){
        super(databyte);


    }
    //become type
    public byte[] getServicetpye(){
        return Arrays.copyOfRange(getData(), 0, 2  );
    }
    public byte[] getBroadcastData(){
        return Arrays.copyOfRange(getData(), 2, 2 +getLength() -3  );
    }

}
