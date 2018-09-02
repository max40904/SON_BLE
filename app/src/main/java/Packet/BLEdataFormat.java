package Packet;

import java.util.Arrays;

public class BLEdataFormat {
    private int length;
    private int type ;
    private byte [] data;
    public BLEdataFormat(int length, int type, byte [] data){
        this.length = length;
        this.type = type;
        this.data = data;

    }
    public BLEdataFormat(byte [] databyte){
        int size_length = databyte[0];
        length = size_length ;
        type = databyte[1];
        data = Arrays.copyOfRange(databyte, 2, 2 + size_length - 1 );


    }
    public int getLength(){
        return length;
    }
    public int getType(){
        return type;
    }
    //
    public byte []  getData(){
        return data;
    }
    //all btte (include length type)
    public byte [] getByte(){
        byte temp1 = (byte)( (length & 0x000000ff) );
        byte temp2 = (byte)( (type & 0x000000ff) );
        byte [] re = new byte [length + 1 ];
        re [0]= temp1;
        re [1]= temp2;
        for (int i = 2 ; i < 2 + length - 1  ; i ++){
            re[i] = data [i -2];
        }
        return re;
    }
}
