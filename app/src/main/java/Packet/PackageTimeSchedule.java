package Packet;

import android.bluetooth.le.ScanResult;
import android.util.Log;

import java.util.Arrays;

public class PackageTimeSchedule {
    public String uuid ;
    public int minute ;
    public int second ;
    public int slotNumber ;
    public byte[] slotState ;
    //public int[] slotState ;    //ASCII
    public byte[] rawPackage;   //AdvertiseData without UUID

    PackageTimeSchedule( ScanResult scanResult){
        byte[] incomingPackage = (scanResult.getScanRecord().getBytes());
        String message = "" ;
        for (int i = 0 ; i <incomingPackage.length ; i++ ){
            message += Integer.toHexString(incomingPackage[i])+ " ";
        }
        //Log.d("In PacketTimeSchedule", "Wareable: Receive Packet ("+ incomingPackage.length + "):" +  message + "\n");
        System.out.println("Data - byte[4] = " + incomingPackage[4]);
        System.out.println("Data - Hex = " + message);
        byte[] info_uuid = Arrays.copyOfRange(incomingPackage, 6, 8); //java.util.Arrays

        byte[] info_content = Arrays.copyOfRange(incomingPackage, 8, incomingPackage[4] + 5);

        rawPackage = info_content;

        //uuid = new String(info_uuid); //run ma
        //uuid = Arrays.toString(info_uuid);
        uuid = Integer.toString(info_uuid[0]/16) + Integer.toString(info_uuid[0]%16) + Integer.toString(info_uuid[1]/16) + Integer.toString(info_uuid[1]%16);
        minute = info_content[0] - '0' & 0xFF;
        second = info_content[1] - '0' & 0xFF;
        slotNumber = info_content[2] - '0' & 0xFF;

        slotState = Arrays.copyOfRange(info_content, 3, 3+slotNumber+1);
        /* //Blow is for slotState for "int" formate
        slotState = new int[info_content.length - 3];
        for (int i = 3; i < info_content.length; slotState[i - 3] = info_content[i++]);*/
    }
    public  PackageTimeSchedule(byte [] incomingPackage){
        String message = "" ;
        for (int i = 0 ; i <incomingPackage.length ; i++ ){
            message += Integer.toHexString(incomingPackage[i])+ " ";
        }
        byte[] info_uuid = Arrays.copyOfRange(incomingPackage, 6, 8); //java.util.Arrays
        byte[] info_content = Arrays.copyOfRange(incomingPackage, 8, incomingPackage[4] + 5);
        rawPackage = info_content;

        //uuid = new String(info_uuid); //run ma
        //uuid = Arrays.toString(info_uuid);
        uuid = Integer.toString(info_uuid[0]/16) + Integer.toString(info_uuid[0]%16) + Integer.toString(info_uuid[1]/16) + Integer.toString(info_uuid[1]%16);
        minute = info_content[0] - '0' & 0xFF;
        second = info_content[1] - '0' & 0xFF;
        slotNumber = info_content[2] - '0' & 0xFF;

        slotState = Arrays.copyOfRange(info_content, 3, 3+slotNumber+1);

    }

    public PackageTimeSchedule(String uuid, int minute ,int second, byte[] slot ){

    }
    public String getUuid() {
        return uuid;
    }
    public int getMinute() {
        return minute;
    }
    public int getSecond(){
        return second;
    }
    public int getSlotNumber(){
        return slotNumber;
    }
    public byte[] getSlotState(){
        return slotState;
    }/*
    public int[] getSlotState(){
        return slotState;
    }*/
    public byte[] getRawPackage() {
        return rawPackage;
    }
}
