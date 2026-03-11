package com.example.drivertracker;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.ToggleButton;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ToggleButton toggleButton;
    FusedLocationProviderClient locationClient;
    LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toggleButton = findViewById(R.id.toggleLocation);

        locationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },1);
        }

        locationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {

                Location location = locationResult.getLastLocation();

                if(location != null){

                    double lat = location.getLatitude();
                    double lon = location.getLongitude();

                    sendLocation(lat, lon);

                }
            }
        };

        toggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked){

                startLocationUpdates();

            }else{

                locationClient.removeLocationUpdates(locationCallback);

            }

        });
    }

    private void startLocationUpdates(){

        LocationRequest request = LocationRequest.create();
        request.setInterval(5000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationClient.requestLocationUpdates(request, locationCallback, null);
    }

    private void sendLocation(double lat, double lon){

        new Thread(() -> {

            try{

                URL url = new URL("https://drivertracker-a4290-default-rtdb.firebaseio.com/location.json");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type","application/json");
                conn.setDoOutput(true);

                JSONObject json = new JSONObject();

                json.put("latitude",lat);
                json.put("longitude",lon);

                OutputStream os = conn.getOutputStream();
                os.write(json.toString().getBytes());
                os.close();

                conn.getResponseCode();

            }catch(Exception e){
                e.printStackTrace();
            }

        }).start();
    }
}
