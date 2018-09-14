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
import android.widget.TextView;
import android.widget.Toast;

import com.example.max40904.son.R;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import blework.BleInterface;
import Packet.BLEDataType;
import Packet.PackageJoin;
import Packet.PackageNode;
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
    //TODO: Settime schdule packet intent
    public static final String GW_SETSCHDULE_INTENT = "GW_SETSCHDULE_INTENT";
    public static final String GW_SETSCHDULE_MESSAGE = "GW_SETSCHDULE_MESSAGE";


    //TODO: receive join packet intent
    public static final String GW_RECEIVER_INTENT = "GW_RECEIVER_INTENT";
    public static final String GW_RECEIVER_MESSAGE = "GW_RECEIVER_MESSAGE";

    //TODO: receive package from Node
    public static final String GW_NODE_INTENT = "GW_NODE_INTENT";
    public static final String GW_NODE_MESSAGE = "GW_NODE_MESSAGE";

    //TODO: receive package from Node
    public static final String GW_GUI_INTENT = "GW_GUI_INTENT";
    public static final String GW_GUI_TARGET = "GW_GUI_TARGET";
    public static final String GW_GUI_FLAG = "GW_GUI_FLAG";
    public static final String GW_GUI_MESSAGE = "GW_GUI_MESSAGE";

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
        songw.setDeviceInformation(devicemap);


        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((mBroadcastReceiver),
                new IntentFilter(GW_SETSCHDULE_INTENT)
        );
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((mBroadcastReceiver),
                new IntentFilter(GW_RECEIVER_INTENT)
        );
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((mBroadcastReceiver),
                new IntentFilter(GW_NODE_INTENT)
        );
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((mBroadcastReceiver),
                new IntentFilter(GW_GUI_INTENT)
        );


        Log.d("DEBUG MODE", "DEBUG");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View newview = inflater.inflate(R.layout.fragment_songw,
                container, false);
        // Inflate the layout for this fragment
        return newview;
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
        Calendar nextschduletime = Calendar.getInstance();
        nextschduletime.add(Calendar.SECOND,10);
        songw.setCurrentSchduleTime(nextschduletime);
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
    public void setText(String target, int flag, String message ){
        if (target.equals("recTextView")){
            TextView textView = (TextView) getView().findViewById(R.id.recTextView);
            if (flag ==0){
                textView.setVisibility(View.INVISIBLE);
            }
            else{
                textView.setVisibility(View.VISIBLE);
            }


        }
        else if (target.equals("adTextView")){
            TextView textView = (TextView) getView().findViewById(R.id.adTextView);
            if (flag ==0){
                textView.setVisibility(View.INVISIBLE);
            }
            else{
                textView.setVisibility(View.VISIBLE);
            }

        }
        else if (target.equals("nodeInfoTextView")){


                TextView textView = (TextView) getView().findViewById(R.id.nodeInfoTextView);

                textView.setText(message);


        }



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
            if (intent.getAction().equals(GW_GUI_INTENT)){
                String target = intent.getStringExtra(GW_GUI_TARGET);
                int flag = intent.getIntExtra(GW_GUI_FLAG , 0);
                String mes = intent.getStringExtra(GW_GUI_MESSAGE);
                setText(target, flag, mes );

            }
            else if (intent.getAction().equals(GW_NODE_INTENT)){
                //TODO: show data
                byte [] oribyteA = intent.getByteArrayExtra(GW_NODE_MESSAGE);
                PackageNode recpackage =new PackageNode(oribyteA);
                byte [] recSerialnumber = recpackage.getSerialNumber();
                Log.d ("SerialCheck",Converter.byteToInt(recSerialnumber[0]) + " " + Integer.parseInt( songw.getTargetNode()) );
                if (Converter.byteToInt(recSerialnumber[0])!= Integer.parseInt( songw.getTargetNode())){
                    return ;
                }
                ble.stopScan();

                Log.d("Trigger", "Node SerialNumber : "+Converter.bytesToHex(recpackage.getSerialNumber()));

                Log.d("Trigger", "Node DataAmount : "+ ( recpackage.getDataAmount() & 0xff ));

                Log.d("Trigger", "Node Data : "+Converter.bytesToHex(recpackage.getData()));
                songw.setReceicontent(recpackage.getData());

                //TODO: if some body miss refresh map

            }
            else if (intent.getAction().equals(GW_RECEIVER_INTENT)){
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
                    DeviceInformation newdevice = new DeviceInformation(hex, true, recpackage.getX()&0Xff , recpackage.getY() & 0xff );
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


                    /**
                     * refres device map
                     *
                     * */
                    songw.setDeviceInformation(devicemap);
                    byte [] fakemessage  = new byte [devicemap.size()];


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

                Calendar cri = songw.getNowSchduletime();
                TimeSchedule t  = new TimeSchedule(devicemap);
                songw.setCycleTime(cri,t);
                Log.d("Trigger",message);
            }

            // now you can call all your fragments method here
        }
    };
}
