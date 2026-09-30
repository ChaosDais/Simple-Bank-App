package com.example.demo.repos;

import com.example.demo.models.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface AccountRepository extends MongoRepository<Account, String> {
    List<Account> findByUserId(String userId);
}
