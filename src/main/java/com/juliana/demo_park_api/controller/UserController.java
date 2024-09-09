package com.juliana.demo_park_api.controller;

import com.juliana.demo_park_api.entities.User;
import com.juliana.demo_park_api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        User obj = userService.salvar(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(obj);
    }
}
