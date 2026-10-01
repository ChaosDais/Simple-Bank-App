package com.example.demo.controllers;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AccountCreationRequest;
import com.example.demo.dto.AdminSignUpRequest;
import com.example.demo.dto.SignInRequest;
import com.example.demo.dto.SignUpRequest;
import com.example.demo.dto.UserRequest;
import com.example.demo.models.Account;
import com.example.demo.models.User;
import com.example.demo.services.AccountService;
import com.example.demo.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final AccountService accountService;

    @Value("${app.admin-signup-secret:}")
    private String adminSignupSecret;

    public UserController(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@RequestBody SignUpRequest signUpRequest) {
        try {
            User user = userService.signUp(
                    signUpRequest.getName(), signUpRequest.getEmail(), signUpRequest.getPassword());
            return ResponseEntity.status(201).body(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).build();
        }
    }

    @GetMapping("/admin-exists")
    public boolean adminExists() {
        return userService.adminAccountExists();
    }

    @PostMapping("/signup-admin")
    public ResponseEntity<User> signUpAdmin(@RequestBody AdminSignUpRequest signUpRequest) {
        if (!isValidAdminSecret(signUpRequest.getSecret())) {
            return ResponseEntity.status(403).build();
        }

        try {
            User user = userService.signUpAdmin(
                    signUpRequest.getName(), signUpRequest.getEmail(), signUpRequest.getPassword());
            return ResponseEntity.status(201).body(user);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.status(409).build();
        }
    }

    private boolean isValidAdminSecret(String providedSecret) {
        if (adminSignupSecret == null || adminSignupSecret.isEmpty() || providedSecret == null) {
            return false;
        }

        byte[] expected = adminSignupSecret.getBytes(StandardCharsets.UTF_8);
        byte[] actual = providedSecret.getBytes(StandardCharsets.UTF_8);
        return MessageDigest.isEqual(expected, actual);
    }

    @PostMapping("/signin")
    public ResponseEntity<User> signIn(@RequestBody SignInRequest signInRequest) {
        User user = userService.signIn(signInRequest.getEmail(), signInRequest.getPassword());
        return user == null ? ResponseEntity.status(401).build() : ResponseEntity.ok(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        User user = userService.getUserById(id);
        return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
    }

    @GetMapping("/{id}/accounts")
    public ResponseEntity<List<Account>> getUserAccounts(@PathVariable String id) {
        User user = userService.getUserById(id);
        return user == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(user.getAccounts());
    }

    @PostMapping("/{id}/accounts")
    public ResponseEntity<Account> createUserAccount(
            @PathVariable String id,
            @RequestBody AccountCreationRequest accountRequest) {
        Account account = accountService.createAccount(id, accountRequest.getType());
        return account == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.status(201).body(account);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserRequest userRequest) {
        User user = userService.createUser(userRequest.getName());
        return ResponseEntity.status(201).body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable String id,
            @RequestBody UserRequest userRequest) {
        User user = userService.updateUser(id, userRequest.getName());
        return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        return userService.deleteUser(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
