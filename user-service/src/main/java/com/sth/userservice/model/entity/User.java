package com.sth.userservice.model.entity;

import com.sth.userservice.model.dto.UserDTO;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User {
    @Id
    private String id;
    @Column
    private String username;
    @Column
    private String password;
    @Column
    private String email;
    @Column
    private String encryptedPwd;

    public UserDTO toDto() {
        return UserDTO.builder()
                .id(id)
                .username(username)
                .password(password)
                .email(email)
                .encryptedPwd(encryptedPwd)
                .build();
    }

}
