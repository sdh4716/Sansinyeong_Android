package com.example.sansinyeong.model;

import java.util.Date;

public class ChatData {

    private String userName;
    private String message;
    private Date timestamp;

    public ChatData() { }

    public ChatData(String userName, String message,Date timestamp) {
        this.userName = userName;
        this.message = message;
        this.timestamp=timestamp;
    }


    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ChatData{" +
                "userName='" + userName + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
