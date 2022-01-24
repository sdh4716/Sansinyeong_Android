package com.example.sansinyeong.model;

import java.util.ArrayList;

public class WeatherData { //data tag
    private String tmEf; //날짜
    private String wf; //날씨
    private String tmn; //최저기온
    private String tmx; //최고기온

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
