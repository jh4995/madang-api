package com.example.demoStep3.dto;

public class FavoriteWithCongestionDto {
    private String id;
    private String trailName;
    private String courseName;
    private String districtName;
    private String difficulty;
    private double courseKm;
    private int nowWalking;
    private String congestionGrade;
    private String createdAt;

    public FavoriteWithCongestionDto(String id, String trailName, String courseName,
                                     String districtName, String difficulty, double courseKm,
                                     int nowWalking, String congestionGrade, String createdAt) {
        this.id = id; this.trailName = trailName; this.courseName = courseName;
        this.districtName = districtName; this.difficulty = difficulty;
        this.courseKm = courseKm; this.nowWalking = nowWalking;
        this.congestionGrade = congestionGrade; this.createdAt = createdAt;
    }

    public String getId() { return id; }
    public String getTrailName() { return trailName; }
    public String getCourseName() { return courseName; }
    public String getDistrictName() { return districtName; }
    public String getDifficulty() { return difficulty; }
    public double getCourseKm() { return courseKm; }
    public int getNowWalking() { return nowWalking; }
    public String getCongestionGrade() { return congestionGrade; }
    public String getCreatedAt() { return createdAt; }
}