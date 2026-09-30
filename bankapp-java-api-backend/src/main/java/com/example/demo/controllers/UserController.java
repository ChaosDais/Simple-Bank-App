package com.example.demo.controllers;

import com.example.demo.dto.UserRequest;
import com.example.demo.dto.AccountCreationRequest;
import com.example.demo.models.Account;
import com.example.demo.models.User;
import com.example.demo.services.AccountService;
import com.example.demo.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final AccountService accountService;

    public UserController(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        User user = userService.getUserById(id);
        return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
    }

    @GetMapping("/{id}/accounts")
    public ResponseEntity<List<Account>> getUserAccounts(@PathVariable int id) {
        User user = userService.getUserById(id);
        return user == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(user.getAccounts());
    }

    @PostMapping("/{id}/accounts")
    public ResponseEntity<Account> createUserAccount(
            @PathVariable int id,
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
            @PathVariable int id,
            @RequestBody UserRequest userRequest) {
        User user = userService.updateUser(id, userRequest.getName());
        return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        return userService.deleteUser(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
