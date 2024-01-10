package com.example.codehub.validation;

import static android.util.Patterns.EMAIL_ADDRESS;

public class EmailValidator {
    public static boolean isValidEmail(String email) {
        return EMAIL_ADDRESS.matcher(email).matches();
    }
}