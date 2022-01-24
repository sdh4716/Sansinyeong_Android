package com.example.sansinyeong.model;

import java.util.ArrayList;

public class WeatherXml { //data tag
    private String city;
    private ArrayList<WeatherData> dataBody;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public ArrayList<WeatherData> getDataBody() {
        return dataBody;
    }

    public void setDataBody(ArrayList<WeatherData> dataBody) {
        this.dataBody = dataBody;
    }
}
