package com.example.sansinyeong.model;

public class Mountains {

    String name;
    String address;
    String height;
    String feature;

    public Mountains(String name, String address, String height, String feature) {
        this.name = name;
        this.address = address;
        this.height = height;
        this.feature = feature;
    }

    public Mountains(String name, String address, String height) {
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

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }


}
