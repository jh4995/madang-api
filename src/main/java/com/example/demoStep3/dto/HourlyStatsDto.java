package com.example.demoStep3.dto;

public class HourlyStatsDto {
    private String id;
    private int hour;
    private double avgEntries;
    private int sampleDays;

    public HourlyStatsDto(String id, int hour, double avgEntries, int sampleDays) {
        this.id = id; this.hour = hour;
        this.avgEntries = avgEntries; this.sampleDays = sampleDays;
    }

    public String getId() { return id; }
    public int getHour() { return hour; }
    public double getAvgEntries() { return avgEntries; }
    public int getSampleDays() { return sampleDays; }
}