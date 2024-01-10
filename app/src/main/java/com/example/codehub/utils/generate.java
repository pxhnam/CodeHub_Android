package com.example.codehub.utils;

import java.util.Random;

public class generate {
    public static String OTP() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }
}
