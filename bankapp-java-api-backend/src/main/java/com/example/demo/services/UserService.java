package com.example.demo.services;

import com.example.demo.models.User;
import com.example.demo.repos.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public User getUserById(int userId) {
        return userRepository.getUserById(userId);
    }

    public User createUser(String name) {
        return userRepository.createUser(name);
    }

    public User updateUser(int userId, String name) {
        return userRepository.updateUser(userId, name);
    }

    public boolean deleteUser(int userId) {
        return userRepository.deleteUser(userId);
    }
}
