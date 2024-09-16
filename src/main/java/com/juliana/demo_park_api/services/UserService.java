package com.juliana.demo_park_api.services;

import com.juliana.demo_park_api.entities.Client;
import com.juliana.demo_park_api.entities.User;
import com.juliana.demo_park_api.exception.EntityNotFoundException;
import com.juliana.demo_park_api.exception.PasswordInvalidException;
import com.juliana.demo_park_api.exception.UsernameUniqueViolationException;
import com.juliana.demo_park_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User save(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
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
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new PasswordInvalidException("Current password does not match");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        return user;
    }
    @Transactional(readOnly = true)
    public List<User> searchAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User searchByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(String.format("User com %s not found", username)));
    }

    @Transactional(readOnly = true)
    public User.Role searchRoleByUsername(String username) {
        return userRepository.findRoleByUsername(username);
    }

}
