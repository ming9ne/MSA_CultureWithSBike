package com.sth.userservice.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class RequestUser {
    private String id;
    private String password;
    private String username;
    private String email;
}
