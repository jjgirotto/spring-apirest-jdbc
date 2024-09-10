package com.juliana.demo_park_api.services;

import com.juliana.demo_park_api.entities.User;
import com.juliana.demo_park_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    @Autowired
    private final UserRepository userRepository;

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User searchById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }
    @Transactional
    public User editPassword(Long id, String password) {
        User user = searchById(id);
        user.setPassword(password);
        return user;
    }
    @Transactional(readOnly = true)
    public List<User> searchAll() {
        return userRepository.findAll();
    }
}
