package com.example.earlysuraksha;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.earlysuraksha.retrofit.myservice;
import com.example.earlysuraksha.retrofit.retrofitclient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText loginemail,loginpassword;
    TextView signuptext;
    Button login;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    myservice client;
    String answer;


    public static String input;
    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginemail=findViewById(R.id.emailidlogin);
        loginpassword=findViewById(R.id.passwordlogin);
        signuptext=findViewById(R.id.signuptext);
        login=findViewById(R.id.loginbutton);

        signuptext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),register.class);
                startActivity(intent);
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginuser(loginemail.getText().toString(),loginpassword.getText().toString());

            }
        });
    }

    public void loginuser(String email, String password) {
        client= retrofitclient.getInstance().create(myservice.class);
        client.loginUser(email,password).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("responsebody", "onResponse: "+response.body());
                if(response.body()==null){
                    Toast.makeText(MainActivity.this, "Check your credentials or internet connection", Toast.LENGTH_SHORT).show();
                    return;
                }
                answer=new Gson().toJson(response.body());
                JsonParser jsonParser = new JsonParser();
                JsonObject authtoken =(JsonObject) jsonParser.parse(answer);
                Log.d("accessobject", "onResponse: "+authtoken.get("authToken"));
                input=authtoken.get("authToken").toString();
                Log.d("qwerty1", "newuser: "+input);

                Log.d("responselogin", "onResponse: "+answer);

                if(answer.length()>100){

//                    newuser();
                    Toast.makeText(MainActivity.this, "login success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),Dashboard.class);
                    startActivity(intent);
                    finish();

                }
                else{
                    Toast.makeText(MainActivity.this, "Some error occured", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Wrong credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }

}