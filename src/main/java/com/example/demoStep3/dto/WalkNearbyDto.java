package com.example.demoStep3.dto;

public class WalkNearbyDto {
    private String id;
    private String trailName;
    private String courseName;
    private String districtName;
    private String difficulty;
    private double courseKm;
    private int minutes;
    private String water;
    private String toilet;
    private double lat;
    private double lng;
    private long distanceM;

    public WalkNearbyDto(String id, String trailName, String courseName, String districtName,
                         String difficulty, double courseKm, int minutes,
                         String water, String toilet, double lat, double lng, long distanceM) {
        this.id = id; this.trailName = trailName; this.courseName = courseName;
        this.districtName = districtName; this.difficulty = difficulty;
        this.courseKm = courseKm; this.minutes = minutes; this.water = water;
        this.toilet = toilet; this.lat = lat; this.lng = lng; this.distanceM = distanceM;
    }

    public String getId() { return id; }
    public String getTrailName() { return trailName; }
    public String getCourseName() { return courseName; }
    public String getDistrictName() { return districtName; }
    public String getDifficulty() { return difficulty; }
    public double getCourseKm() { return courseKm; }
    public int getMinutes() { return minutes; }
    public String getWater() { return water; }
    public String getToilet() { return toilet; }
    public double getLat() { return lat; }
    public double getLng() { return lng; }
    public long getDistanceM() { return distanceM; }
}