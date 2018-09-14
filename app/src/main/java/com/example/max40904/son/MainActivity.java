package com.example.max40904.son;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


import java.nio.charset.StandardCharsets;
import java.util.TimerTask;

import GUI.BLEFragement;
import GUI.SONGWFragment;
import GUI.SONNodeFragment;
import blework.Constants;
import java.util.UUID;
import Converter.Converter;


import Packet.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener ,  SONNodeFragment.OnFragmentInteractionListener, BLEFragement.OnFragmentInteractionListener,SONGWFragment.OnFragmentInteractionListener{

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 456;
    public static final int REQUEST_ENABLE_BT = 1;
    private static int counter = 0 ;
    private String uniqueName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Tracer", "MainActivity onCreate start");

        uniqueName = UUID.randomUUID().toString();
        Log.d("Tracer", "uniqueid length:"+uniqueName.length());
        EditText mName = (EditText) findViewById(R.id.editText3);
        EditText y = (EditText) findViewById(R.id.editText2);
        setContentView(R.layout.logout);

        Button bt1=(Button)findViewById(R.id.button);
        bt1.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
        }
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, Constants.REQUEST_ENABLE_BT);

        Log.d("Tracer",uniqueName);
        Log.d("Tracer", "MainActivity onCreate end");


    }
    @Override
    protected void onResume() {
        super.onResume();
//        uniqueName.substring(0,8);
//        Log.d("Tracer",uniqueName.substring(0,8) );
//
//        PackageJoin join = new PackageJoin(uniqueName.substring(0,8));
//
//
//        Log.d("Tracer", "DATA : "+new String(join.getRawData(), StandardCharsets.UTF_8));
//        Log.d("Tracer", "UUID : "+Converter.bytesToHex(join.getUUID ( ) ) );
//        PackageJoin ackJoin = new PackageJoin(join.getOriData());
//
//        Log.d("Tracer", "newDATA : "+new String(ackJoin.getRawData(), StandardCharsets.UTF_8));
//        Log.d("Tracer", "newUUID : "+Converter.bytesToHex(ackJoin.getUUID ( ) ) );



    }
    @Override
    public void onClick(View v) {
        String message =""+v.getId();
        Log.d("Tracer",message);
        CheckBox NODE = (CheckBox)findViewById(R.id.checkBox);
        CheckBox GW = (CheckBox)findViewById(R.id.checkBox2);
        switch(v.getId()){


            case R.id.button:
                EditText x = (EditText) findViewById(R.id.editText3);
                EditText y = (EditText) findViewById(R.id.editText2);
                setContentView(R.layout.activity_main);
                Toast.makeText(this, "runOnUiThread 執行了！", Toast.LENGTH_SHORT).show();
                Log.d("Tracer","1");
                if (NODE.isChecked()) {

                    int loc_x =Integer.parseInt( x.getText().toString());
                    int loc_y =Integer.parseInt( y.getText().toString());
                    Toast.makeText(this, "runOnUiThread 執行了！", Toast.LENGTH_SHORT).show();
                    Log.d("Tracer","2");
                    setSONNodeFragment(loc_x,loc_y);


                }
                else if (GW.isChecked()){
                    Toast.makeText(this, "runOnUiThread 執行了！", Toast.LENGTH_SHORT).show();
                    Log.d("Tracer","3");
                    setSONGGatewayFragment();
                }
                Log.d("Tracer","4");
               // setBLEFragment();
                x.clearFocus();
                y.clearFocus();
        }
    }

    public class timecounter extends TimerTask{
        @Override
        public void run() {
            // TODO Auto-generated method stub
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    counter++;
                    String message =  "" + counter;
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    Log.d("Tracer", ""+counter);

                }
            });
        }
    }
    public void setSONGGatewayFragment(){
        getSupportFragmentManager().beginTransaction().add(R.id.framelayout1, SONGWFragment.newInstance(uniqueName,"world"),"f1").commit();
    }
    public void setSONNodeFragment(int x , int y ){
        getSupportFragmentManager().beginTransaction().add(R.id.framelayout1, SONNodeFragment.newInstance(uniqueName,x+"", y+""),"f1").commit();
    }
    public void setBLEFragment(){
       // getSupportFragmentManager().beginTransaction().add(R.id.framelayout2, BLEFragement.newInstance(uniqueName,"world"),"f1").commit();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, yay! Start the Bluetooth device scan.
                } else {
                    // Alert the user that this application requires the location permission to perform the scan.
                }
            }
        }
    }
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        CheckBox NODE = (CheckBox)findViewById(R.id.checkBox);
        CheckBox GW = (CheckBox)findViewById(R.id.checkBox2);
        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkBox:
                GW.setChecked(false);
                break;
            case R.id.checkBox2:
                NODE.setChecked(false);
                break;
            // TODO: Veggie sandwich
        }
    }
//   TimerTask task = new TimerTask() {
//
//        @Override
//        public void run() {
//            // TODO Auto-generated method stub
//            runOnUiThread(new Runnable() {
//
//                @Override
//                public void run() {
//                    // TODO Auto-generated method stub
//                    counter++;
//                    String message =  "" + counter;
//                   Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//                   Log.d("Tracer", ""+counter);
//
//                }
//            });
//        }
//    };


}
