package com.example.earlysuraksha;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class gamescreen extends AppCompatActivity {
    ImageView heatwave1,lightning1,openquiz;
    TextView openhomescreen,opencallscreen;
    CardView playgame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamescreen);
        opencallscreen=findViewById(R.id.chats1);
        heatwave1=findViewById(R.id.heatwave1);
        lightning1=findViewById(R.id.lightning1);
        openhomescreen=findViewById(R.id.homescreen1);
        opencallscreen=findViewById(R.id.chats1);
        playgame=findViewById(R.id.playgame);
        openquiz=findViewById(R.id.openquiz);
        heatwave1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),heatwave1.class);
                startActivity(intent);
            }
        });
        lightning1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), lightning1.class);
                startActivity(intent);
            }
        });
        openhomescreen.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Dashboard.class);
                startActivity(intent);
                finish();
            }
        });
        opencallscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),emergencyphone.class);
                startActivity(intent);
            }
        });
        openquiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.DefaultCompany.sihquiz");
                if (launchIntent != null) {
                    startActivity(launchIntent);
                }
            }
        });
        playgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.DefaultCompany.terrain");
                if (launchIntent != null) {
                    startActivity(launchIntent);
                }
            }
        });
    }
}