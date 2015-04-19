package com.trangpig.myapp;

import java.util.Date;

/**
 * Created by TrangPig on 04/15/2015.
 */
public class Message {
    private long id, idSender;
    private String fromName, message;
    private boolean isSelf;
    private Date date;

    public Message() {
    }

    public Message(String fromName, String message, boolean isSelf) {
        this.fromName = fromName;
        this.message = message;
        this.isSelf = isSelf;
    }

    public Message(long id, long idSender, String fromName, String message, Date date){
        this.fromName = fromName;
        this.message = message;
        this.id = id;
        this.idSender = idSender;
        this.date = date;
    }

    public long getId() {return id; }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setSelf(boolean isSelf) {
        this.isSelf = isSelf;
    }
}
