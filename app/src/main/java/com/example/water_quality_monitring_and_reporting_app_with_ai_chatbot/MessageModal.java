package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

public class MessageModal {
    private String message;
    private String sender;

    public MessageModal(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
