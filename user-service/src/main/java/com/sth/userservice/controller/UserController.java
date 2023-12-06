package com.sth.userservice.controller;

import com.sth.userservice.jwt.JwtFilter;
import com.sth.userservice.jwt.TokenProvider;
import com.sth.userservice.model.dto.UserDTO;
import com.sth.userservice.service.UserService;
import com.sth.userservice.vo.RequestLogin;
import com.sth.userservice.vo.RequestUser;
import com.sth.userservice.vo.ResponseUser;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user-service")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "http://localhost:3000")
public class UserController {
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

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
                .id(user.getId())
                .password(user.getPassword())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
        userService.createUser(userDto);

        ResponseUser responseUser = ResponseUser.builder()
                .id(userDto.getId())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .userId(userDto.getUserId())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody RequestLogin requestLogin, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(requestLogin.getId(), requestLogin.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        response.addHeader("token", "Bearer " + jwt);

        return new ResponseEntity<>(jwt, httpHeaders, HttpStatus.OK);
    }
}
