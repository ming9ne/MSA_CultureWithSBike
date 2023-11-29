package com.sth.userservice.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseUser {
    private String id;
    private String username;
    private String email;
}