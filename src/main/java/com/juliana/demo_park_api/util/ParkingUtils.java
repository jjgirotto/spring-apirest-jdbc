package com.juliana.demo_park_api.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParkingUtils {

    //format = 20240916-153030
    public static String generateRecipt() {
        LocalDateTime date = LocalDateTime.now();
        String recipt = date.toString().substring(0,19);
        return recipt.replace("-", "")
                .replace(":", "")
                .replace("T", "-");
    }
}
