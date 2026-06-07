package com.example.demoStep3.dto;

public class WalkDetailDto {
    private String id;
    private String trailName;
    private String courseName;
    private String courseDesc;
    private String districtName;
    private String difficulty;
    private double courseKm;
    private int hour;
    private int minute;
    private int minutes;
    private String water;
    private String toilet;
    private String address;
    private double lat;
    private double lng;

    public WalkDetailDto(String id, String trailName, String courseName, String courseDesc,
                         String districtName, String difficulty, double courseKm,
                         int hour, int minute, int minutes, String water, String toilet,
                         String address, double lat, double lng) {
        this.id = id;
        this.trailName = trailName;
        this.courseName = courseName;
        this.courseDesc = courseDesc;
        this.districtName = districtName;
        this.difficulty = difficulty;
        this.courseKm = courseKm;
        this.hour = hour;
        this.minute = minute;
        this.minutes = minutes;
        this.water = water;
        this.toilet = toilet;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
    }

    public String getId() { return id; }
    public String getTrailName() { return trailName; }
    public String getCourseName() { return courseName; }
    public String getCourseDesc() { return courseDesc; }
    public String getDistrictName() { return districtName; }
    public String getDifficulty() { return difficulty; }
    public double getCourseKm() { return courseKm; }
    public int getHour() { return hour; }
    public int getMinute() { return minute; }
    public int getMinutes() { return minutes; }
    public String getWater() { return water; }
    public String getToilet() { return toilet; }
    public String getAddress() { return address; }
    public double getLat() { return lat; }
    public double getLng() { return lng; }
}