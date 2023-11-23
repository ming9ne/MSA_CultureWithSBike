package com.sth.userservice.service;

import com.sth.userservice.model.dto.UserDTO;
import com.sth.userservice.model.entity.User;
import com.sth.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
