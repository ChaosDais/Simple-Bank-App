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

    public List<Account> getAllAccounts() {
        return accountRepository.getAllAccounts();
    }

    public Account createAccount(int userId, String accountType){
        return accountRepository.createAccount(userId, accountType);
    }

    public Account updateAccount(int id, String accountType) {
        return accountRepository.updateAccount(id, accountType);
    }

    public boolean deleteAccount(int id) {
        return accountRepository.deleteAccount(id);
    }

    public Transaction deposit(int accountId, double amount){
        return accountRepository.deposit(accountId, amount);
    }

    public Transaction withdraw(int accountId, double amount){
        return accountRepository.withdraw(accountId, amount);
    }

    public List<Transaction> getTransactions(int accountId){
        Account account = getAccountById(accountId);
        return account == null ? null : transactionRepository.getTransactionHistory(account);
    }
}
