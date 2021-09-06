package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

public class UserReportImage {
    public String name;
    public String url;

    public UserReportImage() {
    }

    public UserReportImage(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
