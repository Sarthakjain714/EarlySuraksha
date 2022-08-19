package com.example.earlysuraksha;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.earlysuraksha.retrofit.myservice;
import com.example.earlysuraksha.retrofit.retrofitclient;
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
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@RequiresApi(api = Build.VERSION_CODES.O)
public class Dashboard extends AppCompatActivity  {
    public String longitude,latitude;
    LocationRequest locationRequest;
    myservice client;
    LatLng latLng2;
    String input;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallBack;
    SupportMapFragment supportMapFragment;
    TextView launchgames,emergencyphone,addlocation,appdrawer;
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    String username,email;
    ImageView shownavigationdrawer;
    TextView statusmessage,tvusername,tvemail,runtimechanges,severe;
    ImageView statusimage;
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
        input=MainActivity.getauthtoken();
        launchgames=findViewById(R.id.launchgamescreen);
        appdrawer=findViewById(R.id.hamburgericon);
        emergencyphone=findViewById(R.id.chats);
        runtimechanges=findViewById(R.id.runtimechanges);
        nav=findViewById(R.id.navmenu);
        nav.bringToFront();
        drawerLayout=findViewById(R.id.drawer);
        toolbar=findViewById(R.id.toolbar);
        shownavigationdrawer=findViewById(R.id.shownavdrawer);
        statusimage=findViewById(R.id.statusimage);
        statusmessage=findViewById(R.id.statustext);
        severe=findViewById(R.id.severe);

        View hView = nav.getHeaderView(0);
        tvusername=(TextView) hView.findViewById(R.id.username);
        tvemail=(TextView) hView.findViewById(R.id.emailid);

        setSupportActionBar(toolbar);
        shownavigationdrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        appdrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("status", "onClick: "+"idhar aya hu bro");
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case (R.id.profile):
                        Toast.makeText(Dashboard.this, "Abc", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case (R.id.home):
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case (R.id.disastermanagementkit):
                        Intent inten=new Intent(getApplicationContext(),disastermanagementkit.class);
                        startActivity(inten);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case (R.id.precautionarygames):
                        Log.d("status", "onNavigationItemSelected: "+"idhar hu bhai");
                        Intent intent = new Intent(getApplicationContext(),gamescreen.class);
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case (R.id.helplinenumber):
                        Intent intent1 = new Intent(getApplicationContext(), emergencyphone.class);
                        startActivity(intent1);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case (R.id.logout):
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
                return false;
            }
        });
        runtimechanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusimage.setImageResource(R.drawable.alert);
                statusimage.getLayoutParams().height=100;
                statusimage.getLayoutParams().width=100;
                callnotification();
                statusmessage.setText("Alert! Lightning");
                severe.setText("Severe");
            }

        });

//        if (Build.VERSION.SDK_INT >=(Build.VERSION_CODES.O)){
//            CharSequence name = "Suraj";
//
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel("com.example.notification", name, importance);
//
//            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//            assert notificationManager != null;
//            notificationManager.createNotificationChannel(channel);
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "com.example.notification");
//            Notification notification=builder
//                    .setSmallIcon(R.drawable.alert)
//                    .setContentTitle("Alert !")
//                    .setContentText("Busy please avoid this zone")
//                    .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
//                    .setAutoCancel(true)
//                    .setCategory(Notification.CATEGORY_SERVICE)
//                    .build();
//            notificationManager.notify(0,notification);
//
//        }

        StringBuilder smsBuilder = new StringBuilder();
        final String SMS_URI_INBOX = "content://sms/inbox";
        final String SMS_URI_ALL = "content://sms/";
        try {
            Uri uri = Uri.parse(SMS_URI_INBOX);
            String[] projection = new String[] { "_id", "address", "person", "body", "date", "type" };
            Cursor cur = getContentResolver().query(uri, projection, "address='57575701'", null, "date desc");
            if (cur.moveToFirst()) {
                int index_Address = cur.getColumnIndex("address");
                int index_Person = cur.getColumnIndex("person");
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                int index_Type = cur.getColumnIndex("type");
                do {
                    String strAddress = cur.getString(index_Address);
                    int intPerson = cur.getInt(index_Person);
                    String strbody = cur.getString(index_Body);
                    long longDate = cur.getLong(index_Date);
                    int int_Type = cur.getInt(index_Type);

                    smsBuilder.append("[ ");
                    smsBuilder.append(strAddress + ", ");
                    smsBuilder.append(intPerson + ", ");
                    smsBuilder.append(strbody + ", ");
                    smsBuilder.append(longDate + ", ");
                    smsBuilder.append(int_Type);
                    smsBuilder.append(" ]\n\n");
                } while (cur.moveToNext());

                if (!cur.isClosed()) {
                    cur.close();
                    cur = null;
                }
            } else {
                smsBuilder.append("no result!");
            } // end if
        }
        catch (SQLiteException ex) {
        Log.d("SQLiteException", ex.getMessage());
    }
    newuser();
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
        emergencyphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, com.example.earlysuraksha.emergencyphone.class);
                startActivity(intent);
            }
        });
        launchgames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this,gamescreen.class);
                startActivity(intent);

            }
        });
    }

    private void callnotification() {
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
            CharSequence name = "Suraj";

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("com.example.notification", name, importance);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "com.example.notification");
            Notification notification=builder
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle("Alert !")
                    .setContentText("Our system detects severe lightning in your postal region")
                    .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
                    .setAutoCancel(true)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .build();
            notificationManager.notify(0,notification);

        }
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

                                longitude=String.valueOf(location.getLatitude());
                                latitude=String.valueOf(location.getLatitude());
                                setlongitudeandlatitude(latLng2);
                                setlatlan(latitude,longitude,input);
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
    public void setlatlan(String latitude,String longitude,String input) {
        client= retrofitclient.getInstance().create(myservice.class);
        client.setlocation(latitude,longitude,""+input.substring(1,input.length()-1)).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String answer = new Gson().toJson(response.body());
                Log.d("resbody2", "onResponse: "+input.substring(1,input.length()-1));
                Log.d("resbody", "onResponse: "+answer+"\n" +answer.length());

                if(answer.length()>100){
                    Toast.makeText(Dashboard.this, "New user created", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(Dashboard.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void newuser() {
        Log.d("qwerty", "newuser: "+input.substring(1,input.length()-1));
        client = retrofitclient.getInstance().create(myservice.class);
        client.getuser(""+input.substring(1,input.length()-1)).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String output = new Gson().toJson(response.body());
                JsonParser jsonParser1 =new JsonParser();
                JsonObject user =(JsonObject) jsonParser1.parse(output);
                Log.d("firstuser", "onResponse: "+user.get("domain")+"  "+user.get("year"));
                username=user.get("name").toString().substring(1,user.get("name").toString().length()-1);
                email=user.get("email").toString().substring(1,user.get("email").toString().length()-1);;
                tvemail.setText(email);
                tvusername.setText(username);
//                numberofclassesheld=Integer.parseInt(user.get("classesHeld").toString());
//                numberofpresent=Integer.parseInt(user.get("attendance").toString());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(Dashboard.this, "error occured", Toast.LENGTH_SHORT).show();
                System.out.println("Mai idhar aya hu");
            }
        });
    }

}