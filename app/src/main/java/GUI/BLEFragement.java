package GUI;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.max40904.son.R;

import java.util.Arrays;

import BLE.BleInterface;

/**
*****
 * give up
 *
 *
 *
 */
public class BLEFragement extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String UUID;
    private View view;
    private OnFragmentInteractionListener mListener;

    private BleInterface ble;
    private BluetoothAdapter mBluetoothAdapter;



    public BLEFragement() {
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BLEFragement.
     */
    // TODO: Rename and change types and number of parameters
    public static BLEFragement newInstance(String param1, String param2) {
        BLEFragement fragment = new BLEFragement();
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

        /**
         * ble function
         * */
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
        ble = new BleInterface(getContext(),UUID);


        ble.setBluetoothAdapter(mBluetoothAdapter);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_blefragement,
                container, false);
        Button Sbutton = (Button) view.findViewById(R.id.button2);
        Button Rbutton = (Button) view.findViewById(R.id.button3);
        Sbutton.setOnClickListener(this);
        Rbutton.setOnClickListener(this);
        return view;
    }
    @Override
    public void onResume(){
        super.onResume();


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    /**
     * Clink implement
     *
     *
     * */
    @Override
    public void onClick(View v) {
        String message =""+v.getId();
        Log.d("Tracer",message);
        switch(v.getId()){


            case R.id.button2:
                ble.startAdvertising("1111",null,10);
                break;
            case R.id.button3:
                ble.startScan();
                break;

        }
    }
}
