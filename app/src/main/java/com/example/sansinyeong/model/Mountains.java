package com.example.sansinyeong.model;

public class Mountains {

    String name;
    String address;
    Long height;
    String feature;

    // firebase에서 리스트 형태로 받아오려면 무조건 넣어줘야함!!!
    public Mountains(){}

    public Mountains(String name, String address, Long height, String feature) {
        this.name = name;
        this.address = address;
        this.height = height;
        this.feature = feature;
    }

    public Mountains(String name, String address, Long height) {
        this.name = name;
        this.address = address;
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }


}
