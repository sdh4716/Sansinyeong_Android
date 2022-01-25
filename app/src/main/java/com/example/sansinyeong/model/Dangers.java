package com.example.sansinyeong;

//위험지역 마커설정 데이터(위치,이름,기타등)
public class Dangers {
    String explanation;
    String locationName;
    Double latitude;
    Double longitude;

    public Dangers(){}

    public Dangers(String locationName, Double latitude,
                   Double longitude,String explanation) {
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.explanation= explanation;
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

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
