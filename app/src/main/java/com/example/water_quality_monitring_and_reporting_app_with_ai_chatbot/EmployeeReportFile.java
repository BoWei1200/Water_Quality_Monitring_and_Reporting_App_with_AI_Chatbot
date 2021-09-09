package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

public class EmployeeReportFile {
    String key;
    String name;
    String url;

    public EmployeeReportFile() {

    }

    public EmployeeReportFile(String key, String name, String url) {
        this.key = key;
        this.name = name;
        this.url = url;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
