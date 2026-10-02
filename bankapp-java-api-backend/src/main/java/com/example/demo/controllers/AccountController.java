package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AccountCreationRequest;
import com.example.demo.models.Account;
import com.example.demo.models.Transaction;
import com.example.demo.services.AccountService;

@RestController
@RequestMapping("/api")
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    @GetMapping("/accounts")
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable String id){
        Account account = accountService.getAccountById(id);
        return account == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(account);
    }

    @PostMapping("/accounts")
    public ResponseEntity<Account> createAccount(@RequestBody AccountCreationRequest creationRequest){
        Account savedAccount = accountService.createAccount(creationRequest.getUserId(), creationRequest.getType());
        return savedAccount == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.status(201).body(savedAccount);
    }

    @PutMapping("/accounts/{id}")
    public ResponseEntity<Account> updateAccount(
            @PathVariable String id,
            @RequestBody AccountCreationRequest updateRequest) {
        Account updatedAccount = accountService.updateAccount(id, updateRequest.getType());
        return updatedAccount == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(updatedAccount);
    }

    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String id) {
        return accountService.deleteAccount(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/accounts/{id}/deposit")
    public ResponseEntity<Transaction> deposit(@PathVariable String id, @RequestParam double amount){
        if (accountService.getAccountById(id) == null) {
            return ResponseEntity.notFound().build();
        }

        Transaction transaction = accountService.deposit(id, amount);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/accounts/{id}/withdraw")
    public ResponseEntity<Transaction> withdraw(@PathVariable String id, @RequestParam double amount){
        if (accountService.getAccountById(id) == null) {
            return ResponseEntity.notFound().build();
        }

        Transaction transaction = accountService.withdraw(id,amount);
        return transaction == null
                ? ResponseEntity.badRequest().build()
                : ResponseEntity.ok(transaction);
    }

    @GetMapping("/accounts/{id}/transactions")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable String id){
        List<Transaction> transactions = accountService.getTransactions(id);
        return transactions == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(transactions);
    }
}
