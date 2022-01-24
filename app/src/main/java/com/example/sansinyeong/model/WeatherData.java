package com.example.sansinyeong.model;

import java.util.ArrayList;

public class WeatherData { //data tag
    String city; //도시
    String tmEf; //날짜
    String wf; //날씨
    String tmn; //최저기온
    String tmx; //최고기온

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTmEf() {
        return tmEf;
    }

    public void setTmEf(String tmEf) {
        this.tmEf = tmEf;
    }

    public String getWf() {
        return wf;
    }

    public void setWf(String wf) {
        this.wf = wf;
    }

    public String getTmn() {
        return tmn;
    }

    public void setTmn(String tmn) {
        this.tmn = tmn;
    }

    public String getTmx() {
        return tmx;
    }

    public void setTmx(String tmx) {
        this.tmx = tmx;
    }
}
