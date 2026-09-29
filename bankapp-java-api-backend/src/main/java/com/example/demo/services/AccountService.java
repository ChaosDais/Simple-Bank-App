package com.example.demo.services;

import java.util.*;

import com.example.demo.models.Transaction;
import com.example.demo.repos.TransactionRepository;
import org.springframework.stereotype.Service;
import com.example.demo.models.Account;
import com.example.demo.repos.AccountRepository;

@Service
public class AccountService {
    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;

    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository){
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public Account getAccountById(int id){
        return accountRepository.getAccountById(id);
    }

    public Account createAccount(int userId, String accountType){
        return accountRepository.createAccount(userId, accountType);
    }

    public Transaction deposit(int accountId, double amount){
        return accountRepository.deposit(accountId, amount);
    }

    public Transaction withdraw(int accountId, double amount){
        return accountRepository.withdraw(accountId, amount);
    }

    public List<Transaction> getTransactions(int accountId){
        return transactionRepository.getTransactionHistory(getAccountById(accountId));
    }
}
