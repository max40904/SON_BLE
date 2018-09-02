package GUI;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.max40904.son.R;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import BLE.BleInterface;
import Packet.BLEDataType;
import Packet.PackageJoin;
import Packet.PacketAckJoin;
import SON.DeviceInformation;
import SON.SONGateway.Gateway;
import SON.TimeSchedule;
import Converter.Converter;
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SONGWFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SONGWFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SONGWFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static int serial = 1 ;
    private BleInterface ble;
    private BluetoothAdapter mBluetoothAdapter;
    private  Gateway songw;
    private Map<String,DeviceInformation> devicemap;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String UUID;
    private OnFragmentInteractionListener mListener;

    public static final String GW_SETSCHDULE_INTENT = "GW_SETSCHDULE_INTENT";
    public static final String GW_SETSCHDULE_MESSAGE = "SETSCHDULE_MESSAGE";



    public static final String GW_RECEIVER_INTENT = "GW_RECEIVER_INTENT";
    public static final String GW_RECEIVER_MESSAGE = "GW_RECEIVER_MESSAGE";



    public SONGWFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SONGWFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SONGWFragment newInstance(String param1, String param2) {
        SONGWFragment fragment = new SONGWFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            UUID = mParam1;
        }

        if (savedInstanceState == null) {

            mBluetoothAdapter = ((BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE))
                    .getAdapter();

            // Is Bluetooth supported on this device?
            if (mBluetoothAdapter != null) {

                // Is Bluetooth turned on?
                if (mBluetoothAdapter.isEnabled()) {

                    // Are Bluetooth Advertisements supported on this device?
                    if (mBluetoothAdapter.isMultipleAdvertisementSupported()) {



                    } else {
                        Toast.makeText(getActivity(), "Fail ,this device didn't support Peripheral  ", Toast.LENGTH_SHORT).show();
                        Log.d("Tracer","Fail ,this device didn't support Peripheral  ");

                    }
                } else {

                    Toast.makeText(getActivity(), "Fail ,this device didn't open ble  ", Toast.LENGTH_SHORT).show();
                    Log.d("Tracer","Fail ,this device didn't open ble  ");
                }
            } else {

                Toast.makeText( getActivity(), "Fail ,this device didn't get ble  ", Toast.LENGTH_SHORT).show();
                Log.d("Tracer","Fail ,this device didn't get ble  ");

            }


        }
        devicemap = new HashMap<String,DeviceInformation>();
        ble = new BleInterface(getContext(),UUID , true);
        ble.setBluetoothAdapter(mBluetoothAdapter);


        songw = new Gateway(getContext(), ble);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((mBroadcastReceiver),
                new IntentFilter(GW_SETSCHDULE_INTENT)
        );
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((mBroadcastReceiver),
                new IntentFilter(GW_RECEIVER_INTENT)
        );


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_songw, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        Intent intent = new Intent(GW_SETSCHDULE_INTENT);
        intent.putExtra(GW_SETSCHDULE_MESSAGE, "i am come from onResume");
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.getAction().equals(GW_RECEIVER_INTENT)){
                byte [] oribyteA = intent.getByteArrayExtra(GW_RECEIVER_MESSAGE);
                PackageJoin recpackage =new PackageJoin(oribyteA);
                if (recpackage.checkRequestCode()){

                    /**
                     * get device mac
                     * */
                    byte [] tempmac = recpackage.getMac();

                    String mac = new String(tempmac, StandardCharsets.UTF_8);

                    if (devicemap.get(mac)!=null){
                        return ;
                    }
                    // 1  >  0001
                    // 2  >  0002
                    // 20 >  0014
                    //int 20 > hex 14 > string 0014
                    /**
                     * int to hex string
                     * */
                    String hex = Integer.toHexString(serial);
                    for (int i = hex.length() ; i<4 ; i++){
                        hex = "0"+hex;
                    }
                    Log.d("Trigger", "devicename : " + hex);

                    /**
                     * input into map
                     *
                     */
                    DeviceInformation newdevice = new DeviceInformation(mac, true, 0 , 0 );
                    devicemap.put(mac, newdevice);
                    serial++;



                    /**
                     * Generate packageackjoin to device
                     *
                     * */
                    PacketAckJoin ack = new PacketAckJoin(mac , hex);
                    ble.startAdvertising(BLEDataType.AckJoin_string, ack.getBroadcastData() , 5);
                    String message = "";
                    byte [] temp = ack.getBroadcastData();

                    Log.d ("Trigger","send byte to hex :"+new String(temp,StandardCharsets.UTF_8));

                    Log.d("Trigger", mac);

                }
                else {
                    Log.d("Trigger", "false");
                }

                Log.d("Trigger",GW_RECEIVER_MESSAGE);
            }
            else if (intent.getAction().equals(GW_SETSCHDULE_INTENT)){
                Log.d("Trigger",GW_SETSCHDULE_INTENT);
                String message = intent.getStringExtra(GW_SETSCHDULE_MESSAGE);
                Calendar cri = Calendar.getInstance();
                TimeSchedule t  = new TimeSchedule();
                songw.setCycleTime(cri,t);
                Log.d("Trigger",message);
            }

            // now you can call all your fragments method here
        }
    };
}
