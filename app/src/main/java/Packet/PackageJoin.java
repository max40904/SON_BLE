package Packet;

public class PackageJoin extends Package{
    public static final String  requestcode = "77";
    public PackageJoin(byte [] packetdata){
        super(packetdata);
    }
    public PackageJoin(String uniquename ){
        super( );

        String data =uniquename + requestcode;
        byte [] fakeuuid = new byte[2];
        fakeuuid[0] = 0x11;
        fakeuuid[1] = 0x11;
        BLEdataFormat UUID  = new BLEdataFormat ( 3,3, fakeuuid );
        BLEdataFormat servicedata = new BLEdataFormat(data.length() + 1 , 16 , data.getBytes());

        byte[] one = UUID.getByte();
        byte[] two = servicedata.getByte();
        byte[] combined = new byte[one.length + two.length];

        for (int i = 0; i < combined.length; ++i)
        {
            combined[i] = i < one.length ? one[i] : two[i - one.length];
        }

        setByte(combined);

    }
    public byte [] getUniqueName(){
        return null;
    }
}
