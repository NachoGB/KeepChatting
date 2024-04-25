package com.example.user.keepchatting;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    private String userID;
    private String messageText;
    private String name;
    private String now;

    public Message(){}

    public Message(String userID, String messageText, String name) {
        this.userID = userID;
        this.messageText = messageText;
        this.name = name;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        now  = dateFormat.format(new Date());
    }

    public String getUserID() {
        return userID;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getName(){
        return name;
    }

    public String getNow(){
        return now;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public void setName(String name){
        this.name=name;
    }
}
