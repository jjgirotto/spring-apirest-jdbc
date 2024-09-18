package com.juliana.demo_park_api.exception;

import lombok.Getter;

@Getter
public class ReciptCheckInNotFoundException extends RuntimeException{

    private String recipt;

    public ReciptCheckInNotFoundException(String recipt) {
        this.recipt = recipt;
    }
}
