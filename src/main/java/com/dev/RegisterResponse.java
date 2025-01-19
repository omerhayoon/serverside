package com.dev;

import com.dev.models.User;
import com.dev.BasicResponse;

public class RegisterResponse extends BasicResponse{
    String message;
    public RegisterResponse(boolean success, Integer errorCode,String message) {
        super(success, errorCode);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}


