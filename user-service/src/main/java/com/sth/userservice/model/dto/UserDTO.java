package com.sth.userservice.model.dto;

import com.sth.userservice.model.entity.User;
import jakarta.persistence.Column;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private String id;
    private String username;
    private String password;
    private String email;
    private String uid;
    private String encryptedPwd;

    public User toEntity() {
        return User.builder()
                .id(id)
                .username(username)
                .password(password)
                .email(email)
                .uid(uid)
                .encryptedPwd(encryptedPwd)
                .build();
    }
}
