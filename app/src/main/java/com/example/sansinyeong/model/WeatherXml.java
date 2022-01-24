package com.example.sansinyeong.model;

import java.util.ArrayList;

public class WeatherXml { //data tag
    private ArrayList<LocationBody> locationBody;

    public ArrayList<LocationBody> getLocationBody() {
        return locationBody;
    }

    public void setLocationBody(ArrayList<LocationBody> locationBody) {
        this.locationBody = locationBody;
    }

    class LocationBody{
        private String province;
        private String city;
        private ArrayList<DataBody> dataBody;

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
    }

    class DataBody{
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
}
