package com.example.earlysuraksha.Location;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.earlysuraksha.R;
import com.example.earlysuraksha.retrofit.myservice;
import com.example.earlysuraksha.retrofit.retrofitclient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PeriodicLocation extends Worker {
    private static final String DEFAULT_START_TIME = "08:00";
    private static final String DEFAULT_END_TIME = "24:00";
    private static final String TAG = "MyWorker";
    public static final String input = "X";
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private Location mLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private Context mContext;
    private LocationCallback mLocationCallback;
    myservice client;

    public PeriodicLocation(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        String input = getInputData().getString("Input");
        Log.d(TAG, "doWork: Done");
        Log.d(TAG, "work" + input);

        Log.d(TAG, "onStartJob: STARTING JOB..");

        DateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        String formattedDate = dateFormat.format(date);

        try {
            Date currentDate = dateFormat.parse(formattedDate);
            Date startDate = dateFormat.parse(DEFAULT_START_TIME);
            Date endDate = dateFormat.parse(DEFAULT_END_TIME);

            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                }
            };

            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
            mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            try {
                mFusedLocationClient
                        .getLastLocation()
                        .addOnCompleteListener(new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                if (task.isSuccessful() && task.getResult() != null) {
                                    mLocation = task.getResult();
                                    Log.d(TAG, "Location : " + mLocation);

                                    // Create the NotificationChannel, but only on API 26+ because
                                    // the NotificationChannel class is new and not in the support library
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        CharSequence name = mContext.getString(R.string.app_name);
                                        String description = mContext.getString(R.string.app_name);
                                        int importance = NotificationManager.IMPORTANCE_DEFAULT;
                                        NotificationChannel channel = new NotificationChannel(mContext.getString(R.string.app_name), name, importance);
                                        channel.setDescription(description);
                                        // Register the channel with the system; you can't change the importance
                                        // or other notification behaviors after this
                                        NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
                                        notificationManager.createNotificationChannel(channel);
                                    }

                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, mContext.getString(R.string.app_name))
                                            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
                                            .setContentTitle("New Location Update")
                                            .setContentText("You are at " + getCompleteAddressString(mLocation.getLatitude(), mLocation.getLongitude()))
                                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                            .setStyle(new NotificationCompat.BigTextStyle().bigText("You are at " + getCompleteAddressString(mLocation.getLatitude(), mLocation.getLongitude())));

                                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);

                                    // notificationId is a unique int for each notification that you must define
                                    notificationManager.notify(1001, builder.build());
                                    assert input != null;
                                    setLatLan(String.valueOf(mLocation.getLatitude()),String.valueOf(mLocation.getLongitude()),input);
                                    mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                                } else {
                                    Log.w(TAG, "Failed to get location.");
                                }
                            }
                        });
            } catch (SecurityException unlikely) {
                Log.e(TAG, "Lost location permission." + unlikely);
            }

            try {
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, null);
            } catch (SecurityException unlikely) {
                //Utils.setRequestingLocationUpdates(this, false);
                Log.e(TAG, "Lost location permission. Could not request updates. " + unlikely);
            }

        } catch (ParseException ignored) {

        }

        return Result.success();
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }

    // Setting Longitude and Latitude to server//
    public void setLatLan(String latitude, String longitude, String input) {
        client = retrofitclient.getInstance().create(myservice.class);
        client.setlocation(latitude, longitude, "" + input.substring(1, input.length() - 1)).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String answer = new Gson().toJson(response.body());
                Log.d(TAG, "onResponse: " + input.substring(1, input.length() - 1));
                Log.d(TAG, "onResponse: " + answer + "\n" + answer.length());

                if (answer.length() > 100) {
                    Log.d(TAG, "New user created ");

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG,t.getMessage());
            }
        });
    }
}

