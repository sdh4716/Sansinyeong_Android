package com.example.sansinyeong.model;

public class Mountains {

    String name;
    String address;
    Long height;
    String feature;
    String img1;
    String img2;
    String img3;

    // firebase에서 리스트 형태로 받아오려면 무조건 넣어줘야함!!!
    public Mountains(){}

    public Mountains(String name, String address, Long height, String feature) {
        this.name = name;
        this.address = address;
        this.height = height;
        this.feature = feature;
    }

    public Mountains(String name, String address, Long height, String feature, String img1, String img2, String img3) {
        this.name = name;
        this.address = address;
        this.height = height;
        this.feature = feature;
        this.img1 = img1;
        this.img2 = img2;
        this.img3 = img3;
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

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }
}
