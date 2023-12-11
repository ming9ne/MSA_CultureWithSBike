package com.sth.userservice.service;

import com.sth.userservice.model.dto.UserDTO;
import com.sth.userservice.model.entity.User;
import com.sth.userservice.repository.UserRepository;
import com.sth.userservice.vo.RequestUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    // 유저 전체 조회
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
    public UserDTO createUser(RequestUser requestUser) {
        User user = User.builder()
                .id(requestUser.getId())
                .username(requestUser.getUsername())
                .email(requestUser.getEmail())
                .uid(UUID.randomUUID().toString())
                .encryptedPwd(passwordEncoder.encode(requestUser.getPassword()))
                .build();

        userRepository.save(user);

        return user.toDto();
    }

    // 유저 디테일 by 아이디
    public UserDTO getUserDetailsById(String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            UserDTO userDto = user.get().toDto();
            return userDto;
        }else {
            throw new UsernameNotFoundException(id);
        }
    }
    
    // 유저 디테일 by 이메일
    public UserDTO getUserDetailsByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null)
            throw new UsernameNotFoundException(email);

        UserDTO userDto = user.toDto();
        return userDto;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findById(username);
        User user;
        if(optionalUser.isPresent()) {
            user = optionalUser.get();

            return new org.springframework.security.core.userdetails.User(user.getId(), user.getEncryptedPwd(),
                    true, true, true, true,
                    new ArrayList<>());
        }else {

            throw new UsernameNotFoundException(username + ": not found");
        }


    }
}
