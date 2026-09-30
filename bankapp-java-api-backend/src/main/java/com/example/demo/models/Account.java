package com.example.demo.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Document(collection = "accounts")
public class Account {
    @Id
    private String id;
    private String userId;
    private String accountType;
    private double balance;
    @Transient
    private List<Transaction> transactionHistory = new ArrayList<>();

    public Account() {
    }

    public Account(String accountType, String userId) {
        this.userId = userId;
        this.accountType = accountType;
    }

    public String getId(){
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccountType(){
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public double getBalance(){
        return balance;
    }

    public void setBalance(double balance){
        this.balance = balance;
    }

    public void addTransaction(Transaction transaction){
        transactionHistory.add(transaction);
    }

    public List<Transaction> getTransactionHistory(){
        return transactionHistory;
    }

    public void setTransactionHistory(List<Transaction> transactionHistory) {
        this.transactionHistory = transactionHistory;
    }
}
