package com.example.earlysuraksha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class emergencyphone extends AppCompatActivity {
    Button call;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergencyphone);
        call=findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "011-24363260"));
                startActivity(intent);
            }
        });
    }
}