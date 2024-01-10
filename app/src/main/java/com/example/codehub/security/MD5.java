package com.example.codehub.security;


import java.security.MessageDigest;

public class MD5 {
    public static String hash(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(input.getBytes());
        byte[] digest = md.digest();
        return String.format("%032x", new java.math.BigInteger(1, digest));
    }
}
