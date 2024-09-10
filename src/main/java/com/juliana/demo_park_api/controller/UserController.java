package com.juliana.demo_park_api.controller;

import com.juliana.demo_park_api.entities.User;
import com.juliana.demo_park_api.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/users")
public class UserController {
    @Autowired
    private final UserService userService;
    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        User obj = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(obj);
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        User user = userService.searchById(id);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> updatePassword(@PathVariable Long id, @RequestBody User user) {
        User obj = userService.editPassword(id, user.getPassword());
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        List<User> users = userService.searchAll();
        return ResponseEntity.ok(users);
    }
}
