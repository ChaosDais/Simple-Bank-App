package com.example.demo.controllers;

import java.util.*;

import com.example.demo.dto.*;
import com.example.demo.models.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.models.Account;
import com.example.demo.services.AccountService;

@RestController
@RequestMapping("/api")
public class AccountController {
    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable int id){
        Account account = accountService.getAccountById(id);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/accounts")
    public ResponseEntity<Account> createAccount(@RequestBody AccountCreationRequest creationRequest){
        Account savedAccount = accountService.createAccount(creationRequest.getUserId(), creationRequest.getType());
        return ResponseEntity.ok(savedAccount);
    }

    @PostMapping("/accounts/{id}/deposit")
    public ResponseEntity<Transaction> deposit(@PathVariable int id, @RequestParam double amount){
        Transaction transaction = accountService.deposit(id, amount);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/accounts/{id}/withdraw")
    public ResponseEntity<Transaction> withdraw(@PathVariable int id, @RequestParam double amount){
        Transaction transaction = accountService.withdraw(id,amount);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/accounts/{id}/transactions")
    public List<Transaction> getTransactions(@PathVariable int id){
        return accountService.getTransactions(id);
    }
}
