package com.example.earlysuraksha.retrofit;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface myservice {

    @FormUrlEncoded
    @POST("user/createuser")
    Call<JsonObject> registerUser(@Field("email") String email,
                                  @Field("name") String name,
                                  @Field("password") String password,
                                  @Field("phoneNumber") String phoneNumber);

    @POST("user/login")
    @FormUrlEncoded
    Call<JsonObject> loginUser(@Field("email") String email,
                               @Field("password") String password);

    @FormUrlEncoded
    @POST("user/setLocation")
    Call<JsonObject> setlocation(@Field("lat") String lat,
                                 @Field("long") String longitude,
                                 @Header("auth-token") String authtoken);

    @POST("custom/sendAlert")
    Call<JsonObject> sendalert(@Field("number") String number);

    @POST("user/getuser")
    Call<JsonObject> getuser(@Header("auth-token") String auth);


}

