package com.sth.userservice.controller;

import com.sth.userservice.jwt.JwtFilter;
import com.sth.userservice.jwt.TokenProvider;
import com.sth.userservice.model.dto.UserDTO;
import com.sth.userservice.service.UserService;
import com.sth.userservice.vo.RequestLogin;
import com.sth.userservice.vo.RequestUser;
import com.sth.userservice.vo.ResponseUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
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

    // 유저 전체 조회
    @GetMapping("/users")
    public List<UserDTO> listUsers() {
        return userService.listUser();
    }

    // 유저 조회
    @GetMapping("/user")
    public ResponseEntity<Object> findUser(@RequestParam("id") String id) {
        UserDTO userDTO;

        try {
            userDTO =  userService.getUserDetailsById(id);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

        ResponseUser responseUser = ResponseUser.builder()
                .id(userDTO.getId())
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .uid(userDTO.getUid())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseUser);
    }

    // 유저 등록
    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {
        UserDTO userDTO = userService.createUser(user);

        ResponseUser responseUser = ResponseUser.builder()
                .id(userDTO.getId())
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .uid(userDTO.getUid())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseUser> login(@RequestBody RequestLogin requestLogin) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(requestLogin.getId(), requestLogin.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication);

        try{
            tokenProvider.validateToken(jwt);
        } catch (Exception e) {
            System.out.println("123");
        }

        UserDTO userDto = userService.getUserDetailsById(authenticationToken.getName());
        ResponseUser responseUser = ResponseUser.builder()
                .id(userDto.getId())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .uid(userDto.getUid())
                .build();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(responseUser, httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/valid")
    public HttpStatus valid(HttpServletRequest httpServletRequest) {
        String jwt = httpServletRequest.getHeader("Authorization");
        if(tokenProvider.validateToken(jwt)) {
            return HttpStatus.OK;
        } else {
            return HttpStatus.FORBIDDEN;
        }
    }
}
