package com.juliana.demo_park_api.repositories.projection;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface ClientSpaceProjection {

    String getPlate();
    String getBrand();
    String getModel();
    String getColor();
    String getClientCpf();
    String getRecipt();
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime getDateEntry();
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime getDateExit();
    String getSpaceCode();
    BigDecimal getValue();
    BigDecimal getDiscount();
}
