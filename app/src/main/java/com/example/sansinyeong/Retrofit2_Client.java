package com.example.sansinyeong;

import com.example.sansinyeong.service.Weather_Service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit2_Client {
    private static Retrofit2_Client instance;
    private Weather_Service weather_service;

    public Retrofit2_Client(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        weather_service = retrofit.create(Weather_Service.class);
    }

    public static Retrofit2_Client getInstance(){
        if(instance == null){
            instance = new Retrofit2_Client();
        }
        return instance;
    }
    public Weather_Service getItemService(){
        return weather_service;
    }
}
