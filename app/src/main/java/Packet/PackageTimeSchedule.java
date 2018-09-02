package Packet;

import android.bluetooth.le.ScanResult;
import android.util.Log;
import java.nio.*;
import java.util.Arrays;

public class PackageTimeSchedule extends Package {
    private byte Minute ;
    private byte Second ;
    private byte SlotNumber ;
    private byte[] SlotState ;
    //public int[] slotState ;    //ASCII

// System.out.println("Data - byte[4] = " + incomingPackage[4]);

    public  PackageTimeSchedule(byte [] incomingPackage){
        super(incomingPackage);
        byte[] info_content = Arrays.copyOfRange(incomingPackage, 8, incomingPackage[4] + 5);
        Minute = info_content[0];
        Second = info_content[1];
        SlotNumber = info_content[2];

        //SlotState = Arrays.copyOfRange(info_content, 3, info_content.length);
        SlotState = Arrays.copyOfRange(info_content, 3, 3 + 2 * SlotNumber);
    }

    public PackageTimeSchedule(int minute ,int second ,int slotnumber ,int[] slotstate ) {
        super();
        byte[] fakeGatewayUniqueName = {(byte) 0x64, (byte) 0x00};

        //----int to byte array----
        byte m = (byte) (minute & 0x000000ff); Minute = m;
        byte[] m_b = new byte[]{(m)};
        byte s = (byte) (second & 0x000000ff); Second = s;
        byte[] s_b = new byte[]{(s)};
        byte sn = (byte) (slotnumber & 0x000000ff); SlotNumber= sn;
        byte[] sn_b = new byte[]{(sn)};

        //----int array to byte array----
        /*ByteBuffer byteBuffer = ByteBuffer.allocate(slotstate.length * 4);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(slotstate);
        byte[] ss = byteBuffer.array();*/
        byte[] ss = new byte[sn * 2];
        for (int i = 0; i < slotstate.length; i++) {
            ss[i] = (byte) (slotstate[i] & 0x000000ff);
        }
        SlotState = ss;

        byte[] fakeuuid = new byte[2];
        fakeuuid[0] = 0x11;
        fakeuuid[1] = 0x11;
        BLEdataFormat UUID = new BLEdataFormat(3, 3, fakeuuid);
        byte[] comb_temp2 = CombineByte(fakeGatewayUniqueName, m_b);
        byte[] comb_temp3 = CombineByte(comb_temp2, s_b);
        byte[] comb_temp4 = CombineByte(comb_temp3, sn_b);
        byte[] comb_temp5 = CombineByte(comb_temp4, ss);
        BLEdataFormat servicedata = new BLEdataFormat(comb_temp5.length + 1, BLEDataType.Timeschdule, comb_temp5);
        byte[] comb_temp1 = CombineByte(UUID.getByte(), servicedata.getByte());

        setByte(comb_temp1);
    }
    public byte getMinute() {
        return Minute;
    }
    public byte getSecond(){
        return Second;
    }
    public byte getSlotNumber(){
        return SlotNumber;
    }
    public byte[] getSlotState(){
        return SlotState;
    }/*
    public int[] getSlotState(){
        return slotState;
    }*/

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
