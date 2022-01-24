package com.example.sansinyeong;

import com.example.sansinyeong.model.Main;
import com.example.sansinyeong.model.Sys;
import com.example.sansinyeong.model.Weather;
import com.example.sansinyeong.model.Wind;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WeatherResponse {
    @SerializedName("weather")
    ArrayList<Weather> weather;
    @SerializedName("main")
    Main main;
    @SerializedName("wind")
    Wind wind;
    @SerializedName("clouds")
    Clouds clouds;
    @SerializedName("dt")
    Long dt;
    @SerializedName("sys")
    Sys sys;
    @SerializedName("timezone")
    Long timezone;
    @SerializedName("name")
    String name;



    class Clouds{
        @SerializedName("all")
        private Integer all;

        public Integer getAll() {
            return all;
        }

        public void setAll(Integer all) {
            this.all = all;
        }
    }
}
