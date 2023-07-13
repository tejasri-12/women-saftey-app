package com.example.t;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.SEND_SMS;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import android.os.Bundle;
import android.content.Intent;
import android.location.LocationManager;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationSettingsRequest;

import java.util.ArrayList;
public class MainActivity2 extends AppCompatActivity {
    static String x=null;
    static String y=null;
    Button b1,b2;
    private FusedLocationProviderClient client;
    DatabaseHandler myDB;
    private final int REQUEST_CHECK_CODE=8989;
    private LocationSettingsRequest.Builder builder;

    LocationManager locationManager;
    Intent mIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        b1=findViewById(R.id.button);
        b2=findViewById(R.id.button2);
        myDB = new DatabaseHandler(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            onGPS();
            startTrack();
        }
        else{
            startTrack();
        }
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Registor.class);
                startActivity(i);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
    }

    private void loadData() {
        ArrayList<String> theList=new ArrayList<>();
        Cursor data= myDB.getListContents();
        if(data.getCount()==0){
            Toast.makeText(this,"no content to show",Toast.LENGTH_SHORT).show();
        }
        else {
            String msg ="I NEED HELP LATITUDE :" +x+"LONGITUDE: "+y;
            String number="";

            while (data.moveToNext()){
                number="";
                theList.add(data.getString(1));
                number=number+data.getString(1)+(data.isLast()?"":";");
                sendSms(number,msg,true);
            }call();
        }
    }


    private void sendSms(String number, String msg, boolean b) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "sms sent successfully!", Toast.LENGTH_LONG).show();
            return;
        }

    private void call() {
        Intent i= new Intent(Intent.ACTION_CALL);
        i.setData(Uri.parse("tel:9490213278"));
        if(ContextCompat.checkSelfPermission(getApplicationContext(),CALL_PHONE)== PackageManager.PERMISSION_GRANTED){
            startActivity(i);
        }
        else{
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                requestPermissions(new String[]{CALL_PHONE},1);
            }
        }
    }

    private void startTrack() {
        if(ActivityCompat.checkSelfPermission(MainActivity2.this, Manifest.permission.ACCESS_FINE_LOCATION)
        !=PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(MainActivity2.this,Manifest.permission.ACCESS_COARSE_LOCATION)
        !=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);}
        else{
            @SuppressLint("MissingPermission")
            Location locationGPS=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(locationGPS!=null){
                double lat=locationGPS.getLatitude();
                double lon=locationGPS.getLongitude();
                x=String.valueOf(lat);
                y= String.valueOf(lon);
            }
            else{
                Toast.makeText(this,"Unable to find location",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void onGPS() {
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Enadle GPS").setCancelable(false).setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setPositiveButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

}