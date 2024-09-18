package com.juliana.demo_park_api.exception;

import lombok.Getter;

@Getter
public class CodeUniqueViolationException extends RuntimeException {

    private String resource;
    private String code;
    public CodeUniqueViolationException(String message) {
        super(message);
    }

    public CodeUniqueViolationException(String resource, String code) {
        this.resource = resource;
        this.code = code;
    }
}
