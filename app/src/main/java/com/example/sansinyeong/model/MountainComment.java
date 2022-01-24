package com.example.sansinyeong.model;

public class MountainComment {
    String username;
    String content;
    String uid;
    Long star;

    // firebase에서 리스트 형태로 받아오려면 무조건 넣어줘야함!!!
    public MountainComment(){}

    public MountainComment(String username, String content, Long star, String uid) {
        this.username = username;
        this.content = content;
        this.star = star;
        this.uid = uid;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public Long getStar() {
        return star;
    }

    public void setStar(Long star) {
        this.star = star;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
