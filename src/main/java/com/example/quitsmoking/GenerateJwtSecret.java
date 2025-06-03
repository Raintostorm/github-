package com.example.quitsmoking;

import java.util.Base64;
import java.security.SecureRandom;

public class GenerateJwtSecret {
    public static void main(String[] args) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32]; // 256 bits
        random.nextBytes(bytes);
        String base64Encoded = Base64.getEncoder().encodeToString(bytes);
        System.out.println("Your JWT Secret Key: " + base64Encoded);
    }
}