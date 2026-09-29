package com.example.demo.repos;

import java.util.*;

import com.example.demo.models.Transaction;
import org.springframework.stereotype.Repository;
import com.example.demo.models.Account;
import com.example.demo.utilities.AccountData;

@Repository
public class AccountRepository {

    public Account getAccountById(int id){
        List<Account> accounts = AccountData.accounts;
        for (Account account : accounts){
            if (account.getId() == id) return account;
        }

        return null;
    }

    // POST: Create Account
    public Account createAccount(int userId, String accountType){
        Account newAccount = new Account(userId, accountType);
        AccountData.accounts.add(newAccount);
        return newAccount;
    }

    // POST: Deposit
    public Transaction deposit(int accountId, double amount){
        Account account = getAccountById(accountId);
        account.setBalance(account.getBalance() + amount);

        Transaction transaction = new Transaction("DEPOSIT", amount);
        account.addTransaction(transaction);
        return transaction;
    }

    // POST: Withdraw
    public Transaction withdraw(int accountId, double amount){
        Account account = getAccountById(accountId);
        double currentBal = account.getBalance();

        if (currentBal - amount >= 0){
            account.setBalance(currentBal - amount);
            Transaction transaction = new Transaction("WITHDRAW", amount);
            account.addTransaction(transaction);
            return transaction;
        }
        // TODO: handle exception

        return null;


    }
}
