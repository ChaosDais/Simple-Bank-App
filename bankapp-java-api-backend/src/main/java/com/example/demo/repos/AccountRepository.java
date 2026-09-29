package com.example.demo.repos;

import java.util.*;

import com.example.demo.models.Transaction;
import org.springframework.stereotype.Repository;
import com.example.demo.models.Account;
import com.example.demo.models.User;
import com.example.demo.utilities.AccountData;

@Repository
public class AccountRepository {
    private final UserRepository userRepository;
    private int nextAccountId = AccountData.accounts.stream()
            .mapToInt(Account::getId)
            .max()
            .orElse(0) + 1;

    public AccountRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<Account> getAllAccounts() {
        return new ArrayList<>(AccountData.accounts);
    }

    public Account getAccountById(int id){
        List<Account> accounts = AccountData.accounts;
        for (Account account : accounts){
            if (account.getId() == id) return account;
        }

        return null;
    }

    // POST: Create Account
    public Account createAccount(int userId, String accountType){
        User user = userRepository.getUserById(userId);
        if (user == null) {
            return null;
        }

        Account newAccount = new Account(nextAccountId++, accountType);
        AccountData.accounts.add(newAccount);
        user.addAccount(newAccount);
        return newAccount;
    }

    public Account updateAccount(int id, String accountType) {
        Account account = getAccountById(id);
        if (account != null) {
            account.setAccountType(accountType);
        }
        return account;
    }

    public boolean deleteAccount(int id) {
        Account account = getAccountById(id);
        if (account == null) {
            return false;
        }

        AccountData.accounts.remove(account);
        userRepository.getAllUsers().forEach(user -> user.removeAccount(account));
        return true;
    }

    // POST: Deposit
    public Transaction deposit(int accountId, double amount){
        Account account = getAccountById(accountId);
        if (account == null) {
            return null;
        }

        account.setBalance(account.getBalance() + amount);

        Transaction transaction = new Transaction("DEPOSIT", amount);
        account.addTransaction(transaction);
        return transaction;
    }

    // POST: Withdraw
    public Transaction withdraw(int accountId, double amount){
        Account account = getAccountById(accountId);
        if (account == null) {
            return null;
        }

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
