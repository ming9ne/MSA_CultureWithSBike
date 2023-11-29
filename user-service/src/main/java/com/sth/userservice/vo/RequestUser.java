package com.sth.userservice.vo;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class RequestUser {
    private String username;
    private String password;
    private String email;
}
