package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.models.Account;
import com.example.demo.models.User;
import com.example.demo.repos.AccountRepository;
import com.example.demo.repos.TransactionRepository;
import com.example.demo.repos.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(
            UserRepository userRepository,
            AccountRepository accountRepository,
            TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        users.forEach(this::attachAccounts);
        return users;
    }

    public User getUserById(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        return user == null ? null : attachAccounts(user);
    }

    public User createUser(String name) {
        return attachAccounts(userRepository.save(new User(name)));
    }

    public User signUp(String name, String email, String password) {
        return signUp(name, email, password, false);
    }

    public boolean adminAccountExists() {
        return userRepository.existsByAdminTrue();
    }

    public User signUpAdmin(String name, String email, String password) {
        if (adminAccountExists()) {
            throw new IllegalStateException("An admin account already exists");
        }

        return signUp(name, email, password, true);
    }

    private User signUp(String name, String email, String password, boolean admin) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use");
        }

        String passwordHash = passwordEncoder.encode(password);
        User user = new User(name, email, passwordHash);
        user.setAdmin(admin);
        return attachAccounts(userRepository.save(user));
    }

    public User signIn(String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null || user.getPasswordHash() == null
                || !passwordEncoder.matches(password, user.getPasswordHash())) {
            return null;
        }

        return attachAccounts(user);
    }

    public User updateUser(String userId, String name) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }

        user.setName(name);
        return attachAccounts(userRepository.save(user));
    }

    public boolean deleteUser(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return false;
        }

        List<Account> accounts = accountRepository.findByUserId(userId);
        accounts.forEach(account ->
                transactionRepository.deleteAll(transactionRepository.findByAccountId(account.getId())));
        accountRepository.deleteAll(accounts);
        userRepository.delete(user);
        return true;
    }

    private User attachAccounts(User user) {
        List<Account> accounts = new ArrayList<>(accountRepository.findByUserId(user.getId()));
        user.setAccounts(accounts);
        return user;
    }
}
