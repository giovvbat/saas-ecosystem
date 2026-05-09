package com.giovanna.saas_users.controller;

import com.giovanna.saas_users.dto.UserDto;
import com.giovanna.saas_users.model.User;
import com.giovanna.saas_users.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping
    public ResponseEntity<User> save(@RequestBody @Valid UserDto userDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(userDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> retrieve(@PathVariable String id) {
        return ResponseEntity.ok(service.retrieve(id));
    }
}
