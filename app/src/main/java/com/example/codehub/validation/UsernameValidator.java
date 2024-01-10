package com.example.codehub.validation;

public class UsernameValidator {
    public static boolean isValidUsername(String username) {
        return username != null && username.length() >= 5;
    }
}
