package com.example.gibeom.testchat;

public class MessageModel {

    private String chatMessage;
    private String chatTime;
    private String deviceName;
    private String deviceId;
    private String chatIdx;

    public MessageModel() {
    }

    public MessageModel(String chatMessage, String chatTime, String deviceName, String deviceId, String chatIdx) {
        this.chatMessage = chatMessage;
        this.chatTime = chatTime;
        this.deviceName = deviceName;
        this.deviceId = deviceId;
        this.chatIdx = chatIdx;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public String getChatTime() {
        return chatTime;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getChatIdx() {
        return chatIdx;
    }
}
