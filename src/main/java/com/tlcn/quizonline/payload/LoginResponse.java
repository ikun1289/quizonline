package com.tlcn.quizonline.payload;

import lombok.Data;

@Data
public class LoginResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private String role;

    public LoginResponse(String accessToken, String role) {
        this.accessToken = accessToken;
        this.role = role;
    }
}
