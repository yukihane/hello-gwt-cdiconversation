package com.example.shared;

public class FieldVerifier {
    public static boolean isValid(String text) {
        try {
            int input = Integer.parseInt(text);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
