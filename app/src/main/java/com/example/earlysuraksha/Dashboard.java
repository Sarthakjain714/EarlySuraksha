package com.example.earlysuraksha;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.earlysuraksha.Location.PeriodicLocation;
import com.example.earlysuraksha.retrofit.myservice;
import com.example.earlysuraksha.retrofit.retrofitclient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@RequiresApi(api = Build.VERSION_CODES.O)
public class Dashboard extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 20;
    private static final int MY_BACKGROUND_LOCATION_REQUEST = 25;
    private static final String TAG = "LocationUpdate";
    public String longitude, latitude;
    LocationRequest locationRequest;
    myservice client;
    LatLng latLng2;
    String input;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallBack;
    SupportMapFragment supportMapFragment;
    TextView launchgames, emergencyphone, addlocation, appdrawer;
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    String username, email;
    ImageView shownavigationdrawer;
    TextView statusmessage, tvusername, tvemail, runtimechanges, severe;
    ImageView statusimage;
    WorkManager mWorkManager;

    @RequiresApi(api = Build.VERSION_CODES.Q)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map2);
        if (!checkLocationPermission()) {
            ActivityCompat.requestPermissions(Dashboard.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CALL_PHONE,
                    "android.permission.READ_SMS",
            }, PERMISSION_REQUEST_CODE);
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = new LocationRequest();
        mWorkManager = WorkManager.getInstance(getApplication());
        locationRequest.setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(30000);
        locationRequest.setFastestInterval(5000);
        input = MainActivity.getauthtoken();
        launchgames = findViewById(R.id.launchgamescreen);
        appdrawer = findViewById(R.id.hamburgericon);
        emergencyphone = findViewById(R.id.chats);
        runtimechanges = findViewById(R.id.runtimechanges);
        nav = findViewById(R.id.navmenu);
        nav.bringToFront();
        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        shownavigationdrawer = findViewById(R.id.shownavdrawer);
        statusimage = findViewById(R.id.statusimage);
        statusmessage = findViewById(R.id.statustext);
        severe = findViewById(R.id.severe);
        View hView = nav.getHeaderView(0);
        tvusername = (TextView) hView.findViewById(R.id.username);
        tvemail = (TextView) hView.findViewById(R.id.emailid);
        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case (R.id.profile):
                        Toast.makeText(Dashboard.this, "Abc", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case (R.id.home):
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case (R.id.disastermanagementkit):
                        Intent inten = new Intent(getApplicationContext(), disastermanagementkit.class);
                        startActivity(inten);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case (R.id.precautionarygames):
                        Log.d("status", "onNavigationItemSelected: " + "idhar hu bhai");
                        Intent intent = new Intent(getApplicationContext(), gamescreen.class);
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

        shownavigationdrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        appdrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("status", "onClick: " + "idhar aya hu bro");
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        runtimechanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusimage.setImageResource(R.drawable.alert);
                statusimage.getLayoutParams().height = 100;
                statusimage.getLayoutParams().width = 100;
                callNotification();
                statusmessage.setText("Alert! Lightning");
                severe.setText("Severe");
            }

        });

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
                Intent intent = new Intent(Dashboard.this, gamescreen.class);
                startActivity(intent);

            }
        });

        if (checkLocationPermission()) {

        }

        newUser(input);
        if (checkLocationPermission()) {
            smsRelated();
            LocationRealted();
            requestPermission();


        }

    }

    public void smsRelated() {
        StringBuilder smsBuilder = new StringBuilder();
        final String SMS_URI_INBOX = "content://sms/inbox";
        final String SMS_URI_ALL = "content://sms/";
        try {
            Uri uri = Uri.parse(SMS_URI_INBOX);
            String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
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
        } catch (SQLiteException ex) {
            Log.d("SQLiteException", ex.getMessage());
        }
    }

    public void LocationRealted() {

        locationCallBack = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                latitude = String.valueOf(location.getLatitude());
                longitude = String.valueOf(location.getLongitude());
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, null);
        }
        updateGps();
    }

    public void setWorkManager() {

        // Create Network constraint
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        Data myData = new Data.Builder()
                .putString("Input",input).build();
        PeriodicWorkRequest periodicSyncDataWork =
                new PeriodicWorkRequest.Builder(PeriodicLocation.class, 15, TimeUnit.MINUTES)
                        .addTag(TAG)
                        .setInputData(myData)
                        .setConstraints(constraints)
                        // setting a backoff on case the work needs to retry
                        .setBackoffCriteria(BackoffPolicy.LINEAR, PeriodicWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
                        .build();
        mWorkManager.enqueueUniquePeriodicWork(
                TAG,
                ExistingPeriodicWorkPolicy.KEEP, //Existing Periodic Work policy
                periodicSyncDataWork //work request
        );
        Toast.makeText(getApplication(), "Location Worker Started : " + periodicSyncDataWork.getId(), Toast.LENGTH_SHORT).show();
        try {
            isWorkScheduled(WorkManager.getInstance(getApplication()).getWorkInfosByTag(TAG).get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    //Function to Create the Notification
    private void callNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Suraj";

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("com.example.notification", name, importance);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "com.example.notification");
            Notification notification = builder
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle("Alert !")
                    .setContentText("Our system detects severe lightning in your postal region")
                    .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
                    .setAutoCancel(true)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .build();
            notificationManager.notify(0, notification);

        }
    }


    // Function to update the Gps//
    private void updateGps() {
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
                                Log.d("getlocation", "onMapReady: " + location.getLatitude() + "  " + location.getLongitude());
                                latLng2 = latLng;
                                longitude = String.valueOf(location.getLatitude());
                                latitude = String.valueOf(location.getLatitude());
                                setLongitudeAndLatitude(latLng2);
                                setLatLan(latitude, longitude, input);
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


    // setting Longitude and Latitude
    private void setLongitudeAndLatitude(LatLng latLng2) {
        longitude = String.valueOf(latLng2.longitude);
        latitude = String.valueOf(latLng2.latitude);
        Log.d("stringCheck", "onCreate: " + longitude + "    " + latitude);
    }

    // Setting Longitude and Latitude to server//
    public void setLatLan(String latitude, String longitude, String input) {
        client = retrofitclient.getInstance().create(myservice.class);
        client.setlocation(latitude, longitude, "" + input.substring(1, input.length() - 1)).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String answer = new Gson().toJson(response.body());
                Log.d("resbody2", "onResponse: " + input.substring(1, input.length() - 1));
                Log.d("resbody", "onResponse: " + answer + "\n" + answer.length());

                if (answer.length() > 100) {
                    Toast.makeText(Dashboard.this, "New user created", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(Dashboard.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    //  For New User//
    private void newUser(String input) {
        Log.d("qwerty", "newuser: " + input.substring(1, input.length() - 1));
        client = retrofitclient.getInstance().create(myservice.class);
        client.getuser("" + input.substring(1, input.length() - 1)).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String output = new Gson().toJson(response.body());
                JsonParser jsonParser1 = new JsonParser();
                JsonObject user = (JsonObject) jsonParser1.parse(output);
                Log.d("firstuser", "onResponse: " + user.get("domain") + "  " + user.get("year"));
                username = user.get("name").toString().substring(1, user.get("name").toString().length() - 1);
                email = user.get("email").toString().substring(1, user.get("email").toString().length() - 1);
                ;
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


    //  Check For Worker //
    private boolean isWorkScheduled(List<WorkInfo> workInfos) {
        boolean running = false;
        if (workInfos == null || workInfos.size() == 0) return false;
        for (WorkInfo workStatus : workInfos) {
            running = workStatus.getState() == WorkInfo.State.RUNNING | workStatus.getState() == WorkInfo.State.ENQUEUED;
        }
        return running;
    }

    // Check for Permission//
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private boolean checkLocationPermission() {
        int result0 = ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION");
        int result1 = ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION");
        int result2 = ContextCompat.checkSelfPermission(this, "android.permission.CALL_PHONE");
        int result3 = ContextCompat.checkSelfPermission(this, "android.permission.READ_SMS");
        int result4 = ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_BACKGROUND_LOCATION");
        return result0 == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED && result4 == PackageManager.PERMISSION_GRANTED;

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void requestBackgroundLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                MY_BACKGROUND_LOCATION_REQUEST);
    }

    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(Dashboard.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                if (ActivityCompat.checkSelfPermission(Dashboard.this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {


                    AlertDialog alertDialog = new AlertDialog.Builder(Dashboard.this).create();
                    alertDialog.setTitle("Background permission");
                    alertDialog.setMessage("This Permission is  required for background location");
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Grant background Permission",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    requestBackgroundLocationPermission();
                                    dialog.dismiss();
                                }
                            });

                    alertDialog.show();


                } else if (ActivityCompat.checkSelfPermission(Dashboard.this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    setWorkManager();
                }
            } else {
                setWorkManager();
            }

        } else if (ActivityCompat.checkSelfPermission(Dashboard.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Dashboard.this, Manifest.permission.ACCESS_FINE_LOCATION)) {


                AlertDialog alertDialog = new AlertDialog.Builder(Dashboard.this).create();
                alertDialog.setTitle("ACCESS_FINE_LOCATION");
                alertDialog.setMessage("Location permission required");

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(Dashboard.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.CALL_PHONE,
                                        "android.permission.READ_SMS",
                                }, PERMISSION_REQUEST_CODE);
                                dialog.dismiss();
                            }
                        });


                alertDialog.show();

            } else {
                ActivityCompat.requestPermissions(Dashboard.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CALL_PHONE,
                        "android.permission.READ_SMS",
                }, PERMISSION_REQUEST_CODE);
            }
        }

    }


    // Requesting for Permission//
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                boolean coarseLocation = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean fineLocation = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                boolean callPhone = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                boolean sms = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                if (coarseLocation)
                    Toast.makeText(this, "Permission Granted1", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(this, "Permission Denied1", Toast.LENGTH_SHORT).show();
                }
                if (fineLocation)
                    Toast.makeText(this, "Permission Granted2", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(this, "Permission Denied2", Toast.LENGTH_SHORT).show();
                }
                if (callPhone)
                    Toast.makeText(this, "Permission Granted3", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(this, "Permission Denied3", Toast.LENGTH_SHORT).show();
                }
                if (sms)
                    Toast.makeText(this, "Permission Granted4", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(this, "Permission Denied4", Toast.LENGTH_SHORT).show();
                }
                if (coarseLocation && fineLocation && callPhone && sms) {
                    smsRelated();
                    LocationRealted();
                    requestBackgroundLocationPermission();

                } else {
                    Toast.makeText(this, "ACCESS_FINE_LOCATION permission denied", Toast.LENGTH_LONG).show();
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.parse("package:com.example.earlysuraksha")
                        ));

                    }
                }
            }
        } else if (requestCode == MY_BACKGROUND_LOCATION_REQUEST) {

            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    setWorkManager();
                    Toast.makeText(this, "Background location Permission Granted", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Background location permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
}
