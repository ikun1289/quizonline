package com.tlcn.quizonline.payload;

import lombok.Data;

@Data
public class GeneralResponse {
    private String Message;

    public GeneralResponse(String message) {
        this.Message = message;
    }
}
