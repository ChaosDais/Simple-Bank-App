package com.example.demo.dto;

public class TransactionRequest {
    private int userId;
    private double amount;

    public int getId(){
        return userId;
    }
    public double getAmount(){
        return amount;
    }
}
