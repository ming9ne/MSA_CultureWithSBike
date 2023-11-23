package com.sth.userservice.model.entity;

import com.sth.userservice.model.dto.UserDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String username;
    @Column
    private String password;
    @Column
    private String email;

    public UserDTO toDto() {
        return UserDTO.builder()
                .id(id)
                .username(username)
                .password(password)
                .email(email)
                .build();
    }

}
