package com.technoabinash.mig33.model;

public class MessageModel {
    String messagePublisher,message,messageId;
    Long timeStamp;

    public MessageModel() {
    }

    public MessageModel(String messagePublisher, String message) {
        this.messagePublisher = messagePublisher;
        this.message = message;
    }

    public String getMessagePublisher() {
        return messagePublisher;
    }

    public void setMessagePublisher(String messagePublisher) {
        this.messagePublisher = messagePublisher;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
