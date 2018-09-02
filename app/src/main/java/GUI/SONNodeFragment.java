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
import Converter.Converter;
import com.example.max40904.son.R;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import BLE.BleInterface;
import Packet.BLEDataType;
import Packet.PackageTimeSchedule;
import Packet.PacketAckJoin;
import SON.DeviceInformation;
import SON.SONNode.SONNode;
import SON.TimeSchedule;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SONNodeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SONNodeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SONNodeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    private BleInterface ble;
    private BluetoothAdapter mBluetoothAdapter;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String SETSCHDULE_INTENT = "SETSCHDULE_INTENT";
    public static final String SETSCHDULE_MESSAGE = "SETSCHDULE_MESSAGE";



    public static final String RECEIVER_INTENT = "RECEIVER_INTENT";
    public static final String RECEIVER_MESSAGE = "RECEIVER_MESSAGE";



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String UUID;
    private OnFragmentInteractionListener mListener;
    private SONNode sonnode ;
    public SONNodeFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SONNodeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SONNodeFragment newInstance(String param1, String param2) {
        SONNodeFragment fragment = new SONNodeFragment();
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





        /**
         * ble work
         * */
        ble = new BleInterface(getContext(),UUID ,false);

        Log.d("Tracer","MAC:"+UUID);
        ble.setBluetoothAdapter(mBluetoothAdapter);

        sonnode = new SONNode(getContext(),ble);
        sonnode.setMacAddress(UUID.substring(0,8));
        byte [] test = new byte[10];
        Arrays.fill( test, (byte) 1 );


        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((mBroadcastReceiver),
                new IntentFilter(SETSCHDULE_INTENT)
        );
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((mBroadcastReceiver),
                new IntentFilter(RECEIVER_INTENT)
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sonnode, container, false);
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
//        Intent intent = new Intent(SETSCHDULE_INTENT);
//        intent.putExtra(SETSCHDULE_MESSAGE, "i am come from onResume");
//        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
        sonnode.startJoinTime();
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


            if (intent.getAction().equals(RECEIVER_INTENT)){
                byte [] oribyteA = intent.getByteArrayExtra(RECEIVER_MESSAGE);
                PacketAckJoin ackpacket = new PacketAckJoin(oribyteA);
                Log.d("Trigger","MAC:"+ackpacket.getMac());

                String realmac = new String (ackpacket.getMac(), StandardCharsets.UTF_8 );
                if (realmac.equals(UUID.substring(0,8))){
                    ble.stopAdvertising();
                    ble.stopScan();
                    ble.startScan(BLEDataType.Timeschdule,10);
                }
                Log.d("Trigger",RECEIVER_MESSAGE);
            }
            else if (intent.getAction().equals(SETSCHDULE_INTENT)){
                Log.d("Trigger",SETSCHDULE_INTENT);
                ble.stopScan();
                byte [] oribyteA = intent.getByteArrayExtra(SETSCHDULE_MESSAGE);
                PackageTimeSchedule timepacket = new PackageTimeSchedule(oribyteA);
                Log.d("Trigger", "Split TimeSchedule Minutes : "+(timepacket.getMinute() & 0xff));
                Log.d("Trigger", "Split TimeSchedule Seconds : "+ ( timepacket.getSecond() & 0xff));
                Log.d("Trigger", "Split TimeSchedule Slot Amount : "+(timepacket.getSlotNumber() & 0xff));
                Log.d("Trigger", "Split TimeSchedule Slot State : "+Converter.bytesToHex(timepacket.getSlotState() ) );

                Calendar cri = Calendar.getInstance();
                TimeSchedule t  = new TimeSchedule();
                sonnode.setCycleTime(cri,t);

            }

            // now you can call all your fragments method here
        }
    };
}
