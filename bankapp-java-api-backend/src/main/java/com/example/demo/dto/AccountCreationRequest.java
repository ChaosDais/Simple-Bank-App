package com.example.demo.dto;

public class AccountCreationRequest {
    private String userId;
    private String type;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
