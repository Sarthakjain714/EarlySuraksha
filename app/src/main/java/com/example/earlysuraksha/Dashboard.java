package com.example.earlysuraksha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class Dashboard extends AppCompatActivity  {
    public String longitude,latitude;
    LocationRequest locationRequest;
    LatLng latLng2;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallBack;
    SupportMapFragment supportMapFragment;
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        supportMapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map2);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest=new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(30000);
        locationRequest.setFastestInterval(5000);

        locationCallBack= new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                latitude=String.valueOf(location.getLatitude());
                longitude=String.valueOf(location.getLongitude());
            }
        };
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallBack,null);
        updategps();
    }

    private void updategps() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Dashboard.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                Log.d("getlocation", "onMapReady: "+location.getLatitude()+"  " +location.getLongitude());
                                latLng2=latLng;
                                setlongitudeandlatitude(latLng2);
                                MarkerOptions options = new MarkerOptions().position(latLng).title("You are here");
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                                googleMap.addMarker(options);
                            }
                        });
                    }
                }
            });
        }
    }
    private void setlongitudeandlatitude(LatLng latLng2) {
        longitude= String.valueOf(latLng2.longitude);
        latitude= String.valueOf(latLng2.latitude);
        Log.d("stringcheck", "onCreate: "+longitude+"    "+latitude);
    }
}