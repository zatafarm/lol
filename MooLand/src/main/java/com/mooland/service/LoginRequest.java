package com.mooland.service;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
public class LoginRequest {

    private String loginId;
    private String password;
}