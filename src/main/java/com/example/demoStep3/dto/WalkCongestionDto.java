package com.example.demoStep3.dto;

public class WalkCongestionDto {
    private String id;
    private String trailName;
    private String districtName;
    private int nowWalking;
    private String congestionGrade;

    public WalkCongestionDto(String id, String trailName, String districtName,
                             int nowWalking, String congestionGrade) {
        this.id = id; this.trailName = trailName; this.districtName = districtName;
        this.nowWalking = nowWalking; this.congestionGrade = congestionGrade;
    }

    public String getId() { return id; }
    public String getTrailName() { return trailName; }
    public String getDistrictName() { return districtName; }
    public int getNowWalking() { return nowWalking; }
    public String getCongestionGrade() { return congestionGrade; }
}