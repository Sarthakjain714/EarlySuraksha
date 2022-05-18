package com.example.earlysuraksha.retrofit;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface myservice {

    @FormUrlEncoded
    @POST("user/createuser")
    Call<JsonObject> registerUser(@Field("email") String email,
                                                @Field("name") String name,
                                                @Field("password") String password);

    @POST("user/login")
    @FormUrlEncoded
    Call<JsonObject> loginUser(@Field("email") String email,
                               @Field("password")String password);


    @Headers({"Accept: application/json",
            "User-Agent: councellor section"
             })
    @POST("user/getuser")
    Call<JsonObject> getuser(@Header("auth-token") String auth);

    @FormUrlEncoded
    @POST("register")
    Call<JsonObject> setdomain(@Field("domain") String domain,
                               @Field("year") String year,
                               @Header("auth-token") String authtoken);
    @FormUrlEncoded
    @POST("markAttendance")
    Call<JsonObject> getstudents(@Field("code") String code,
                                 @Field("domain") String domain,
                               @Field("year") String year,
                               @Header("auth-token") String authtoken);
    @FormUrlEncoded
    @POST("markPresent")
    Call<JsonObject> executeattendance(@Field("list") List<String> list,
                                   @Header("auth-token") String authtoken);
}

