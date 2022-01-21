package com.example.sansinyeong;

//위험지역 마커설정 데이터(위치,이름,기타등)
public class Dangers {
    Integer number;
    String locationName;
    Double latitude;
    Double longitude;

    public Dangers(){}

    public Dangers(Integer number, String locationName, Double latitude, Double longitude) {
        this.number = number;
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
