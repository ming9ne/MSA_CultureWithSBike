package com.sth.userservice.service;

import com.sth.userservice.model.dto.UserDTO;
import com.sth.userservice.model.entity.User;
import com.sth.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDTO> listUser() {
        List<User> list = new ArrayList<>();
        List<UserDTO> resultList = new ArrayList<>();

        list = userRepository.findAll();
        for(User user : list) {
            resultList.add(user.toDto());
        }

        return resultList;
    }

    // 유저 회원가입
    public UserDTO createUser(UserDTO userDto) {
        userDto.setId(UUID.randomUUID().toString());

        User user = userDto.toEntity();
        user.setEncryptedPwd(passwordEncoder.encode(userDto.getPassword()));

        userRepository.save(user);

        return user.toDto();
    }
}
