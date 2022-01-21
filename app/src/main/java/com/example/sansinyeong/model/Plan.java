package com.example.sansinyeong.model;

public class Plan {
    String startdate;
    String enddate;
    String mountain;
    String bak;
    String members;

    // firebase에서 리스트 형태로 받아오려면 무조건 넣어줘야함!!!
    public Plan(){}

    public Plan(String startdate, String enddate, String mountain, String bak, String members) {
        this.startdate = startdate;
        this.enddate = enddate;
        this.mountain = mountain;
        this.bak = bak;
        this.members = members;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getMountain() {
        return mountain;
    }

    public void setMountain(String mountain) {
        this.mountain = mountain;
    }

    public String getBak() {
        return bak;
    }

    public void setBak(String bak) {
        this.bak = bak;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }
}
