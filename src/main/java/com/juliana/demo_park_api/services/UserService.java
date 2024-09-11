package com.juliana.demo_park_api.services;

import com.juliana.demo_park_api.entities.User;
import com.juliana.demo_park_api.exception.EntityNotFoundException;
import com.juliana.demo_park_api.exception.PasswordInvalidException;
import com.juliana.demo_park_api.exception.UsernameUniqueViolationException;
import com.juliana.demo_park_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    @Autowired
    private final UserRepository userRepository;

    @Transactional
    public User save(User user) {
        try {
            return userRepository.save(user);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            throw new UsernameUniqueViolationException(String.format("Username {%s} already registered", user.getUsername()));
        }
    }

    @Transactional(readOnly = true)
    public User searchById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("User {id=%s} not found", id)));
    }
    @Transactional
    public User editPassword(Long id, String currentPassword, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new PasswordInvalidException("New password does not match");
        }
        User user = searchById(id);
        if (!user.getPassword().equals(currentPassword)) {
            throw new PasswordInvalidException("Current password does not match");
        }
        user.setPassword(newPassword);
        return user;
    }
    @Transactional(readOnly = true)
    public List<User> searchAll() {
        return userRepository.findAll();
    }
}
