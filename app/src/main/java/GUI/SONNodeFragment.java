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
import Converter.Converter;
import com.example.max40904.son.R;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Calendar;

import blework.BleInterface;
import Packet.BLEDataType;
import Packet.PackageNode;
import Packet.PackageTimeSchedule;
import Packet.PacketAckJoin;
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

    private View view;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";



    public static final String SETSCHDULE_INTENT = "SETSCHDULE_INTENT";
    public static final String SETSCHDULE_MESSAGE = "SETSCHDULE_MESSAGE";

    public static final String RECEIVER_INTENT = "RECEIVER_INTENT";
    public static final String RECEIVER_MESSAGE = "RECEIVER_MESSAGE";

    public static final String NODE_INTENT = "NODE_INTENT";
    public static final String NODE_MESSAGE = "NODE_MESSAGE";

    public static final String GUI_INTENT = "GUI_INTENT";
    public static final String GUI_TARGET = "GUI_TARGET";
    public static final String GUI_FLAG = "GUI_FLAG";
    public static final String GUI_MESSAGE = "GUI_MESSAGE";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;
    private String UUID;
    private String serialname;
    private OnFragmentInteractionListener mListener;
    private SONNode sonnode ;
    private int x;
    private int y;
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
    public static SONNodeFragment newInstance(String param1, String param2,String param3) {
        SONNodeFragment fragment = new SONNodeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
            x = Integer.parseInt(mParam2);
            y = Integer.parseInt(mParam3);
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
         *
         * GUI
         * */



        /**
         * ble work
         * */
        ble = new BleInterface(getContext(),UUID ,false);

        Log.d("Tracer","MAC:"+UUID);
        ble.setBluetoothAdapter(mBluetoothAdapter);

        sonnode = new SONNode(getContext(),ble);
        sonnode.setMacAddress(UUID.substring(0,8));
        sonnode.setLoc(x,y);
        byte [] test = new byte[10];
        Arrays.fill( test, (byte) 1 );


        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((mBroadcastReceiver),
                new IntentFilter(SETSCHDULE_INTENT)
        );
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((mBroadcastReceiver),
                new IntentFilter(RECEIVER_INTENT)
        );
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((mBroadcastReceiver),
                new IntentFilter(NODE_INTENT)
        );
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((mBroadcastReceiver),
                new IntentFilter(GUI_INTENT)
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View newview = inflater.inflate(R.layout.fragment_sonnode,
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

    public void setText(String target, int flag, String message ){
        if (target.equals("askJoinTextView")){
            TextView textView = (TextView) getView().findViewById(R.id.askJoinTextView);
            if (flag ==0){
                textView.setVisibility(View.INVISIBLE);
            }
            else{
                textView.setVisibility(View.VISIBLE);
            }


        }
        else if (target.equals("recNodeTextView")){
            TextView textView = (TextView) getView().findViewById(R.id.recNodeTextView);
            if (flag ==0){
                textView.setVisibility(View.INVISIBLE);
            }
            else{
                textView.setVisibility(View.VISIBLE);
            }

        }
        else if (target.equals("adNodeTextView")){
            TextView textView = (TextView) getView().findViewById(R.id.adNodeTextView);
            if (flag ==0){
                textView.setVisibility(View.INVISIBLE);
            }
            else{
                textView.setVisibility(View.VISIBLE);
            }
        }
        else if (target.equals("tschduleTextView")){
            TextView textView = (TextView) getView().findViewById(R.id.tschduleTextView);
            if (flag ==0){
                textView.setVisibility(View.INVISIBLE);
            }
            else{
                textView.setVisibility(View.VISIBLE);
            }
        }
        else if (target.equals("receiNodeTextView")){

            TextView textView = (TextView) getView().findViewById(R.id.receiNodeTextView);
            if (flag ==0){
                textView.setVisibility(View.INVISIBLE);
            }
            else if (flag == 1){
                textView.setVisibility(View.VISIBLE);
            }
            else if (flag == 2){
                textView.setText(message);
            }
            else if (flag == 3){
                textView.setVisibility(View.VISIBLE);
            }
        }
        else if (target.equals("sendNodeTextView")){
            TextView textView = (TextView) getView().findViewById(R.id.sendNodeTextView);
            if (flag ==0){
                textView.setVisibility(View.INVISIBLE);
            }
            else if (flag == 1){
                textView.setVisibility(View.VISIBLE);
            }
            else if (flag == 2){
                if (message.equals("90") ){
                    message = "Gateway";
                }

                textView.setText(message);
            }
            else if (flag == 3){
                textView.setVisibility(View.VISIBLE);
            }
        }
        else if (target.equals("SerialNameTextView")){
            TextView textView = (TextView) getView().findViewById(R.id.SerialNameTextView);
            if (flag ==0){
                textView.setVisibility(View.INVISIBLE);
            }
            else if (flag == 1){
                textView.setVisibility(View.VISIBLE);
            }
            else if (flag == 2){
                textView.setText("Serial Name :" + message);
            }
            else if (flag == 3){
                textView.setVisibility(View.VISIBLE);
            }
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
            if (intent.getAction().equals(GUI_INTENT)){
                String target = intent.getStringExtra(GUI_TARGET);
                int flag = intent.getIntExtra(GUI_FLAG , 0);
                String mes = intent.getStringExtra(GUI_MESSAGE);
                setText(target, flag, mes );

            }
            else if (intent.getAction().equals(NODE_INTENT)){

                byte [] oribyteA = intent.getByteArrayExtra(NODE_MESSAGE);
                PackageNode recpackage =new PackageNode(oribyteA);
                byte [] recSerialnumber = recpackage.getSerialNumber();
                Log.d ("SerialCheck",Converter.byteToInt(recSerialnumber[0]) + " " + Integer.parseInt( sonnode.getTargetNode()) );
                if (Converter.byteToInt(recSerialnumber[0])!= Integer.parseInt( sonnode.getTargetNode())){
                    return ;
                }
                ble.stopScan();


                Log.d("Trigger", "Split Node SerialNumber : "+Converter.bytesToHex(recpackage.getSerialNumber()));
                Log.d("Trigger", "Split Node DataAmount : "+ ( recpackage.getDataAmount() & 0xff ));
                Log.d("Trigger", "Split Node Data : "+Converter.bytesToHex(recpackage.getData()));

                sonnode.setReceicontent(recpackage.getData());

            }
            else if (intent.getAction().equals(RECEIVER_INTENT)){
                byte [] oribyteA = intent.getByteArrayExtra(RECEIVER_MESSAGE);
                PacketAckJoin ackpacket = new PacketAckJoin(oribyteA);
                Log.d("Trigger","MAC:"+ackpacket.getMac());

                String realmac = new String (ackpacket.getMac(), StandardCharsets.UTF_8 );
                if (realmac.equals(UUID.substring(0,8))){
                    setText("askJoinTextView" , 0 , "");
                    sonnode.stopJoinTime();
                    ble.stopAdvertising();
                    ble.stopScan();
                    ble.startScan(BLEDataType.Timeschdule,10);

                    String tempserial = new String (ackpacket.getSerialnumber(), StandardCharsets.UTF_8 );
                    serialname = tempserial;
                    setText("SerialNameTextView",2, serialname);
                    sonnode.setSerialname(serialname);
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
                int minute = timepacket.getMinute() & 0xff;
                int second = timepacket.getSecond() & 0xff;
                int slotnumber = timepacket.getSlotNumber() & 0xff;
                int [] slotstate = new int [slotnumber * 2 ];
                byte []  slotbyte = timepacket.getSlotState();
                for (int i = 0 ; i < slotnumber * 2 ; i++){
                    slotstate[i] = slotbyte[i] & 0xff;
                }

                Calendar cri = Calendar.getInstance();

                cri.set(Calendar.MINUTE, minute);
                cri.set(Calendar.SECOND, second);
                if (minute == 0){
                    cri.add(Calendar.HOUR,1);
                }
                TimeSchedule t  = new TimeSchedule(slotnumber,slotstate);
                sonnode.setCycleTime(cri,t);

            }

            // now you can call all your fragments method here
        }
    };
}
