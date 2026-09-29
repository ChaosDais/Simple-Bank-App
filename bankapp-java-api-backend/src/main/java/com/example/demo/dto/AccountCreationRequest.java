package com.example.demo.dto;

public class AccountCreationRequest {
    private int userId;
    private String type;

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
