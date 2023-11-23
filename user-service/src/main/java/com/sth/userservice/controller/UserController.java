package com.sth.userservice.controller;

import com.sth.userservice.model.dto.UserDTO;
import com.sth.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user-service")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/")
    public String hello() {
        return "This is user service!";
    }

    @GetMapping("/users")
    public List<UserDTO> getUsers() {
        return null;
    }

    @GetMapping("/login")
    public void login() {

    }

    @GetMapping("/logout")
    public void logout() {

    }

    @GetMapping("signUp")
    public void singUp() {

    }
}
