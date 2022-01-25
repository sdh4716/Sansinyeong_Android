package com.example.sansinyeong.model;

public class CommentInfo {

    public String uid;
    public String text;
    public String author;

    public CommentInfo() {
    }

    public CommentInfo(String uid, String text,String author) {
        this.uid = uid;
        this.text = text;
        this.author=author;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "CommentInfo{" +
                "uid='" + uid + '\'' +
                ", text='" + text + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
