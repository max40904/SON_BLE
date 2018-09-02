package Packet;

import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class PacketAckJoin extends Package {

    public PacketAckJoin(byte [] packetdata){
        super(packetdata);
    }
    public PacketAckJoin(String uniquename ,String serial){
        super( );

        String data ="22"+uniquename + serial ;
        byte [] fakeuuid = new byte[2];
        fakeuuid[0] = 0x11;
        fakeuuid[1] = 0x11;
        BLEdataFormat UUID  = new BLEdataFormat ( 3,3, fakeuuid );
        byte [] tempsave = data.getBytes();
        tempsave[0] = 0x78;
        tempsave[1] = 0x00;
        BLEdataFormat servicedata = new BLEdataFormat(data.length() + 1 , 16 ,tempsave);

        byte[] one = UUID.getByte();
        byte[] two = servicedata.getByte();
        byte[] combined = new byte[one.length + two.length];

        for (int i = 0; i < combined.length; ++i)
        {
            combined[i] = i < one.length ? one[i] : two[i - one.length];

        }
        Log.d("Packet" , new String(combined, StandardCharsets.UTF_8));
        setByte(combined);


    }


    public byte [] getBroadcastData(){
        return servicedata.getBroadcastData();
    }
    public byte [] getMac(){
        servicedata.getLength();
        servicedata.getData();
        return Arrays.copyOfRange(servicedata.getBroadcastData(),0,servicedata.getBroadcastData().length- 4 );

    }
    public byte [] getSerialnumber(){
        return Arrays.copyOfRange(servicedata.getBroadcastData(),servicedata.getBroadcastData().length- 4, getBroadcastData().length );
    }
}
