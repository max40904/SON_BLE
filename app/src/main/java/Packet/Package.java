package Packet;

import java.util.Arrays;

public class Package {
    private byte [] byteData;
    private byte [] UUID;
    private byte [] SID;
    private byte [] rawData;
    public Package (byte [] packetbyte){
        byteData=  packetbyte ;
        byte[] incomingPackage = packetbyte;

//        SID = Arrays.copyOfRange(incomingPackage, 6, 8); //java.util.Arrays
//        rawData =  Arrays.copyOfRange(incomingPackage, 8, incomingPackage[4] + 5);
//        rawPackage = info_content;
//        //uuid = new String(info_uuid); //run ma
//        //uuid = Arrays.toString(info_uuid);
//        uuid = Integer.toString(info_uuid[0]/16) + Integer.toString(info_uuid[0]%16) + Integer.toString(info_uuid[1]/16) + Integer.toString(info_uuid[1]%16);
//        serialNumber = info_content[0];
//        dataAmount = info_content[1] - '0' & 0xFF;
//        data = Arrays.copyOfRange(info_content, 2, info_content.length);
    }
    public byte [] getUUID(){
        return null;

    }
    public byte [] getRawData(){
        return null;
    }
    public byte [] getSID(){
        return null;
    }
    public String toString(){
        String message = "" ;
        for (int i = 0 ; i <byteData.length ; i++ ){
            message +="["+i +"]:"+ Integer.toHexString(byteData[i])+", " ;
        }
        return message;
    }
}
