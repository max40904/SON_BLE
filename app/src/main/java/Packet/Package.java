package Packet;

import android.util.Log;

import java.util.Arrays;

import Converter.Converter;

public class Package {
    private byte [] oriData; //original data
    private BLEdataFormat UUID;
    protected ServiceDataFormat servicedata;

    public Package (byte [] packetbyte){

        oriData =  Arrays.copyOfRange(packetbyte, 0, 32);
        int uuid_length = oriData[0];
        UUID = new BLEdataFormat(Arrays.copyOfRange(oriData ,0 , uuid_length + 1));
        int sDataLength = oriData[uuid_length + 1 ];
        servicedata = new ServiceDataFormat (Arrays.copyOfRange(oriData , uuid_length + 1 ,  uuid_length + 1 + sDataLength + 1));


    }
    protected Package (){

    }
    public void setByte(byte [] packetbyte){
        oriData =  Arrays.copyOfRange(packetbyte, 0, 32);
        int uuid_length = oriData[0];
        UUID = new BLEdataFormat(Arrays.copyOfRange(oriData ,0 , uuid_length + 1));
        int sDataLength = oriData[uuid_length + 1 ];
        servicedata = new ServiceDataFormat (Arrays.copyOfRange(oriData , uuid_length + 1 ,  uuid_length + 1 + sDataLength + 1));


    }
    public byte [] getUUID(){
        return UUID.getData();

    }
    public byte [] getRawData(){
        return servicedata.getData();
    }

    public byte [] getOriData(){
        return oriData;
    }
    public String toString(){
        String message = "" ;
        for (int i = 0 ; i <oriData.length ; i++ ){
            message +=Integer.toHexString(oriData[i])+" " ;
        }
        return message;
    }
    public int getDataType(){
        return servicedata.getType();
    }
    public int getServicetpye(){
        byte [] data = servicedata.getServicetpye();
        byte swap = data[1];
        data[1] = data[0];
        data[0] = swap;
        int type = Converter.byteToInt(data[0]) * 256 + Converter.byteToInt(data[1]);
        Log.d("convert", ""+type);
        return type;
    }
}
