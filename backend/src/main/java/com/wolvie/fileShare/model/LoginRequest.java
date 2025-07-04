package com.wolvie.fileShare.model;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
