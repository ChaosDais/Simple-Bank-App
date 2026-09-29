package com.example.demo.models;

import java.util.*;

public class Account {
    private int id;
    private String accountType;
    private double balance;
    private List<Transaction> transactionHistory;

    public Account(int id, String accountType) {
        this.id = id;
        this.accountType = accountType;
        balance = 0;
        transactionHistory = new ArrayList<Transaction>();
    }

    public int getId(){
        return id;
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
}
