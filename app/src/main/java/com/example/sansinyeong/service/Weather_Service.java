package com.example.sansinyeong.service;

import com.example.sansinyeong.WeatherResponse;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Weather_Service {
    @GET("weather")
    Call<JsonObject> getCurrentWeather(@Query("q") String q, @Query("appid") String appid);
    //q=도시이름, appId=apiKey

    @GET("weather")
    Call<WeatherResponse> CurrentWeather(@Query("lat") String lat,
                                         @Query("lon") String lon,
                                         @Query("APPID") String APPID,
                                         @Query("units") String units,
                                         @Query("lang") String lang
                                         );
    //위치값, Key값, 온도 단위 설정, @Query("lang") String lang-> 언어 설정
}