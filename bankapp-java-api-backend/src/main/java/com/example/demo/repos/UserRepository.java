package com.example.demo.repos;

import com.example.demo.models.User;
import com.example.demo.utilities.AccountData;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {
    private final List<User> users = new ArrayList<>();
    private int nextUserId = 1;

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public User getUserById(int userId) {
        return users.stream()
                .filter(user -> user.getUserId() == userId)
                .findFirst()
                .orElse(null);
    }

    public User createUser(String name) {
        User user = new User(name, nextUserId++);
        users.add(user);
        return user;
    }

    public User updateUser(int userId, String name) {
        User user = getUserById(userId);
        if (user != null) {
            user.setName(name);
        }
        return user;
    }

    public boolean deleteUser(int userId) {
        User user = getUserById(userId);
        if (user == null) {
            return false;
        }

        AccountData.accounts.removeAll(user.getAccounts());
        users.remove(user);
        return true;
    }
}
