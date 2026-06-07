package com.example.demoStep3.dto;

public class WalkViewDto {
    private String id;
    private String trailName;
    private String courseName;
    private String districtName;
    private String difficulty;
    private double courseKm;
    private int minutes;

    public WalkViewDto(String id, String trailName, String courseName,
                       String districtName, String difficulty, double courseKm, int minutes) {
        this.id = id;
        this.trailName = trailName;
        this.courseName = courseName;
        this.districtName = districtName;
        this.difficulty = difficulty;
        this.courseKm = courseKm;
        this.minutes = minutes;
    }

    public String getId() { return id; }
    public String getTrailName() { return trailName; }
    public String getCourseName() { return courseName; }
    public String getDistrictName() { return districtName; }
    public String getDifficulty() { return difficulty; }
    public double getCourseKm() { return courseKm; }
    public int getMinutes() { return minutes; }
}