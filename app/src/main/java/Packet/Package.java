package Packet;

import java.util.Arrays;

public class Package {
    private byte [] oriData; //original data
    private BLEdataFormat UUID;
    private BLEdataFormat servicedata;

    public Package (byte [] packetbyte){

        oriData =  Arrays.copyOfRange(packetbyte, 0, 32);
        int uuid_length = oriData[0];
        UUID = new BLEdataFormat(Arrays.copyOfRange(oriData ,0 , uuid_length + 1));
        int sDataLength = oriData[uuid_length + 1 ];
        servicedata = new BLEdataFormat (Arrays.copyOfRange(oriData , uuid_length + 1 ,  uuid_length + 1 + sDataLength + 1));


    }
    protected Package (){

    }
    public void setByte(byte [] packetbyte){
        oriData =  Arrays.copyOfRange(packetbyte, 0, 32);
        int uuid_length = oriData[0];
        UUID = new BLEdataFormat(Arrays.copyOfRange(oriData ,0 , uuid_length + 1));
        int sDataLength = oriData[uuid_length + 1 ];
        servicedata = new BLEdataFormat (Arrays.copyOfRange(oriData , uuid_length + 1 ,  uuid_length + 1 + sDataLength + 1));


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
            message +="["+i +"]:"+ Integer.toHexString(oriData[i])+", " ;
        }
        return message;
    }
}
