package com.example.demoStep3.dto;

public class FavoriteDto {
    private String userId;
    private String id;

    public FavoriteDto() {}
    public FavoriteDto(String userId, String id) {
        this.userId = userId; this.id = id;
    }

    public String getUserId() { return userId; }
    public String getId() { return id; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setId(String id) { this.id = id; }
}