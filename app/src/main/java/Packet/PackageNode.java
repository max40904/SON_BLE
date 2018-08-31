package Packet;

import android.bluetooth.le.ScanResult;
import android.util.Log;

import java.util.Arrays;

public class PackageNode {
    public String uuid ;
    public byte serialNumber ; //directly change from ScanResult
    public int dataAmount ;
    public byte[] data ;    //ASCII
    public byte[] rawPackage;   //AdvertiseData without UUID

    public PackageNode( ScanResult scanResult){
        byte[] incomingPackage = (scanResult.getScanRecord().getBytes());
        String message = "" ;
        for (int i = 0 ; i <incomingPackage.length ; i++ ){
            message += Integer.toHexString(incomingPackage[i])+ " ";
        }
        //Log.d("In PackageNode", "Wareable: Receive Packet ("+ incomingPackage.length + "):" +  message + "\n");
        System.out.println("Data - byte[4] = " + incomingPackage[4]);
        System.out.println("Data - Hex = " + message);
        byte[] info_uuid = Arrays.copyOfRange(incomingPackage, 6, 8); //java.util.Arrays
        byte[] info_content = Arrays.copyOfRange(incomingPackage, 8, incomingPackage[4] + 5);
        rawPackage = info_content;
        //uuid = new String(info_uuid); //run ma
        //uuid = Arrays.toString(info_uuid);
        uuid = Integer.toString(info_uuid[0]/16) + Integer.toString(info_uuid[0]%16) + Integer.toString(info_uuid[1]/16) + Integer.toString(info_uuid[1]%16);
        serialNumber = info_content[0];
        dataAmount = info_content[1] - '0' & 0xFF;
        data = Arrays.copyOfRange(info_content, 2, info_content.length);
    }
    public PackageNode(byte []incomingPackage){
        byte[] info_uuid = Arrays.copyOfRange(incomingPackage, 6, 8); //java.util.Arrays
        byte[] info_content = Arrays.copyOfRange(incomingPackage, 8, incomingPackage[4] + 5);
        rawPackage = info_content;
        uuid = Integer.toString(info_uuid[0]/16) + Integer.toString(info_uuid[0]%16) + Integer.toString(info_uuid[1]/16) + Integer.toString(info_uuid[1]%16);
        serialNumber = info_content[0];
        dataAmount = info_content[1] - '0' & 0xFF;
        data = Arrays.copyOfRange(info_content, 2, info_content.length);
    }
    public String getUuid() {
        return uuid;
    }
    public byte getSerialNumber() {
        return serialNumber;
    }
    public int getDataAmount(){
        return dataAmount;
    }
    public byte[] getData() {
        return data;
    }
    public byte[] getRawPackage() {
        return rawPackage;
    }
}
