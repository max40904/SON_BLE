package Packet;

import java.sql.Array;
import java.util.Arrays;

public class PackageJoin extends Package{
    public static final String  requestcode = "77";
    public PackageJoin(byte [] packetdata){
        super(packetdata);
    }
    //uniqename 4byte 8 hex number
    public PackageJoin(String uniquename, int x , int y ){
        super( );

        String data ="22"+uniquename + requestcode;
        byte [] fakeuuid = new byte[2];
        fakeuuid[0] = 0x11;
        fakeuuid[1] = 0x11;
        BLEdataFormat UUID  = new BLEdataFormat ( 3,3, fakeuuid );
        byte [] tempsave = data.getBytes();
        tempsave[1] = 0x00;
        tempsave[0] = 0x6e;
        byte [] location = new byte[2];
        location[0] = (byte )(x & 0x000000ff );
        location[1] = (byte )(y & 0x000000ff );
        byte [] rawdata = CombineByte(tempsave,location);
        BLEdataFormat servicedata = new BLEdataFormat(rawdata.length + 1 , 16 , rawdata);

        byte[] one = UUID.getByte();
        byte[] two = servicedata.getByte();
        byte[] combined = new byte[one.length + two.length];

        for (int i = 0; i < combined.length; ++i)
        {
            combined[i] = i < one.length ? one[i] : two[i - one.length];
        }

        setByte(combined);

    }
    public byte [] getMac(){
        servicedata.getLength();
        servicedata.getData();
        return Arrays.copyOfRange(servicedata.getBroadcastData(),0,servicedata.getBroadcastData().length-4 );

    }

    public byte [] getBroadcastData(){
        return servicedata.getBroadcastData();
    }
    public boolean checkRequestCode(){

        byte [] request = Arrays.copyOfRange(servicedata.getBroadcastData(),servicedata.getBroadcastData().length-4,servicedata.getBroadcastData().length -2);

        if (request[0] == '7' && request[1] == '7'){
            return true;
        }
        return false;
    }
    public byte getX(){
        byte [] data = Arrays.copyOfRange(servicedata.getBroadcastData(),servicedata.getBroadcastData().length-2,servicedata.getBroadcastData().length -1);
        return data[0];

    }

    public byte getY(){
        byte [] data = Arrays.copyOfRange(servicedata.getBroadcastData(),servicedata.getBroadcastData().length-1,servicedata.getBroadcastData().length );

        return data[0];
    }
    private byte [] CombineByte(byte[] one, byte[] two){
        byte[] combined = new byte[one.length + two.length];
        for (int i = 0; i < combined.length; ++i)
        {
            combined[i] = i < one.length ? one[i] : two[i - one.length];
        }
        return combined;
    }
}
