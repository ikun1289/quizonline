package com.tlcn.quizonline.payload;

import lombok.Data;
import lombok.NonNull;

@Data
public class RegisterRequest {

    private String username;

    private String email;

    private String password;
}
