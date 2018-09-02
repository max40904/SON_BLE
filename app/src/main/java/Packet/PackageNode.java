package Packet;

import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.math.BigInteger;

import Converter.Converter;

public class PackageNode extends Package {
    private byte [] seriaNumber ;
    private byte dataAmount ;
    private byte[] data ;    //ASCII
    public PackageNode(byte [] incomingPackage){
        super(incomingPackage);
        seriaNumber = Arrays.copyOfRange(incomingPackage, 8, 10); //java.util.Arrays
        byte[] info_content = Arrays.copyOfRange(incomingPackage, 10, incomingPackage[4] + 5);
        dataAmount = info_content[0];
        //dataAmount = info_content[1] - '0' & 0xFF; //to integer
        data = Arrays.copyOfRange(info_content, 1, 1 + dataAmount);
    }
    public PackageNode( String serialnumber, int dataamount, byte [] constructdata){
        super( );
        byte[] bledatatype = {(byte) 0x82, (byte) 0x00};
        byte dataam = (byte)(dataamount & 0x000000ff); dataAmount = dataam;
        byte [] dataa = new byte []{(dataam)};
        //Log.d("PackageNode", "dataamount : "+ Converter.bytesToHex(dataa));
        //Log.d("PackageNode", "dataamount : "+ dataam);
        //Log.d("PackageNode", "constructdata : "+Converter.bytesToHex(constructdata));
        String temp = serialnumber ;
        byte[] temp2 = new BigInteger(temp,16).toByteArray();
        seriaNumber = temp2;
        data = constructdata;
        byte [] fakeuuid = new byte[2];
        fakeuuid[0] = 0x11;
        fakeuuid[1] = 0x11;
        BLEdataFormat UUID  = new BLEdataFormat ( 3,3, fakeuuid );
        byte [] comb_temp5 = CombineByte(bledatatype,temp2);
        byte [] comb_temp2 = CombineByte(comb_temp5,dataa);
        byte [] comb_temp3 = CombineByte(comb_temp2,constructdata);
        BLEdataFormat servicedata = new BLEdataFormat(comb_temp3.length+1, BLEDataType.Node , comb_temp3);
        byte [] comb_temp1 = CombineByte(UUID.getByte(),servicedata.getByte());

        //Log.d("PackageNode", "comb_temp3 : "+Converter.bytesToHex(comb_temp1));
        setByte(comb_temp1);
    }

    public byte [] getSerialNumber(){
        return seriaNumber;
    }
    public byte getDataAmount(){
        return dataAmount;
    }
    public byte[] getData() {
        return data;
    }
    public byte [] getBroadcastData(){
        return servicedata.getBroadcastData();
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
