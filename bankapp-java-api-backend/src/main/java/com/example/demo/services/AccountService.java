package com.example.demo.services;

import com.example.demo.models.Account;
import com.example.demo.models.Transaction;
import com.example.demo.repos.AccountRepository;
import com.example.demo.repos.TransactionRepository;
import com.example.demo.repos.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public AccountService(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository,
            UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public Account getAccountById(String id) {
        Account account = accountRepository.findById(id).orElse(null);
        return account == null ? null : attachTransactions(account);
    }

    public List<Account> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        accounts.forEach(this::attachTransactions);
        return accounts;
    }

    public List<Account> getAccountsByUserId(String userId) {
        List<Account> accounts = accountRepository.findByUserId(userId);
        accounts.forEach(this::attachTransactions);
        return accounts;
    }

    public Account createAccount(String userId, String accountType) {
        if (userId == null || !userRepository.existsById(userId)) {
            return null;
        }

        return attachTransactions(accountRepository.save(new Account(accountType, userId)));
    }

    public Account updateAccount(String id, String accountType) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null) {
            return null;
        }

        account.setAccountType(accountType);
        return attachTransactions(accountRepository.save(account));
    }

    public boolean deleteAccount(String id) {
        Optional<Account> account = accountRepository.findById(id);
        if (account.isEmpty()) {
            return false;
        }

        transactionRepository.deleteAll(transactionRepository.findByAccountId(id));
        accountRepository.delete(account.get());
        return true;
    }

    public Transaction deposit(String accountId, double amount) {
        Account account = accountRepository.findById(accountId).orElse(null);
        if (account == null) {
            return null;
        }

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
        return transactionRepository.save(new Transaction("DEPOSIT", amount, accountId));
    }

    public Transaction withdraw(String accountId, double amount) {
        Account account = accountRepository.findById(accountId).orElse(null);
        if (account == null || account.getBalance() - amount < 0) {
            return null;
        }

        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);
        return transactionRepository.save(new Transaction("WITHDRAW", amount, accountId));
    }

    public List<Transaction> getTransactions(String accountId) {
        return accountRepository.existsById(accountId)
                ? transactionRepository.findByAccountId(accountId)
                : null;
    }

    private Account attachTransactions(Account account) {
        account.setTransactionHistory(new ArrayList<>(transactionRepository.findByAccountId(account.getId())));
        return account;
    }
}
