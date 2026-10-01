package com.example.demo.dto;

public class AdminSignUpRequest {
    private String name;
    private String email;
    private String password;
    private String secret;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getSecret() { return secret; }
    public void setSecret(String secret) { this.secret = secret; }
}
