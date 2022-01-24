package com.example.sansinyeong.model;

import java.util.ArrayList;

public class WeatherXml { //data tag
    String province;
    String city;
    ArrayList<WeatherData> dataBody;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

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
