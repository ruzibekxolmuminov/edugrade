package com.example.exp;

public class UserExist extends RuntimeException {
    public UserExist(String message) {
        super(message);
    }
}
