package com.juliana.demo_park_api.services;

import com.juliana.demo_park_api.entities.User;
import com.juliana.demo_park_api.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Transactional
    public User salvar(User user) {
        return userRepository.save(user);
    }
}
