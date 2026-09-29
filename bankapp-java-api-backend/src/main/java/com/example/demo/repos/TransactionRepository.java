package com.example.demo.repos;

import com.example.demo.models.Account;
import com.example.demo.models.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransactionRepository {
    // GET: Transaction History
    public List<Transaction> getTransactionHistory(Account account){
        return account.getTransactionHistory();
    }
}
