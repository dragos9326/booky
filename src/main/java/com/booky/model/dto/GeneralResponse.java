package com.booky.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class GeneralResponse {
    private int errorCode;
    private String message;
    private LocalDateTime timestamp;

}
