package com.sth.userservice.controller;

import com.sth.userservice.model.dto.UserDTO;
import com.sth.userservice.service.UserService;
import com.sth.userservice.vo.RequestUser;
import com.sth.userservice.vo.ResponseUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // 유저 조회
    @GetMapping("/users")
    public List<UserDTO> getUsers() {
        return userService.listUser();
    }

    // 유저 등록
    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {
        UserDTO userDto = UserDTO.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .build();
        userService.createUser(userDto);

        ResponseUser responseUser = ResponseUser.builder()
                .id(userDto.getId())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
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
