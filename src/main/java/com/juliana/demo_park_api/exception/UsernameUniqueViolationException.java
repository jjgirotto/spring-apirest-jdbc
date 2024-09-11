package com.juliana.demo_park_api.exception;

public class UsernameUniqueViolationException extends RuntimeException {
    public UsernameUniqueViolationException(String s) {
        super(s);
    }
}
