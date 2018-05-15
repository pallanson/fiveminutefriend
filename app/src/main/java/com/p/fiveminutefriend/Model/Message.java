package com.p.fiveminutefriend.Model;

import java.util.Date;

public class Message {

    public static final int DELIVERY_CREATED = 0;
    public static final int DELIVERY_SENT = 1;
    public static final int DELIVERY_RECEIVED = 2;
    public static final int DELIVERY_READ = 3;
    public static final int MSGTYPE_TEXT = 0;
    public static final int MSGTYPE_IMG = 1;

    public String text = "";
    public int msgType = 0;
    public int deliveryStatus = 0;
    public String sender;
    public long timeSent;
    public long timeReceived;

    public Message(){
        timeReceived = new Date().getTime();
    }

    public Message(String text, int msgType, String sender, int deliveryStatus, long timeSent) {
        this.text = text;
        this.msgType = msgType;
        this.sender = sender;
        this.timeSent = timeSent;
        this.deliveryStatus = deliveryStatus;

        timeReceived = new Date().getTime();
    }

    public long getTimeReceived() {
        return timeReceived;
    }

    public long getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(long timeSent) {
        this.timeSent = timeSent;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public int getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(int deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

}
