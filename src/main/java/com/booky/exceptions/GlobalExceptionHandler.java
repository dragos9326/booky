package com.booky.exceptions;

import com.booky.model.dto.GeneralResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

/**
 * @author dragos
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<GeneralResponse> exception(NullPointerException ex) {
        log.error(ex.getClass().getName(), ex);
        GeneralResponse response = new GeneralResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "NPE", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<GeneralResponse> generalException(GeneralException ex) {
        log.error(ex.getClass().getName(), ex);
        GeneralResponse response = new GeneralResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BookingOverlappingException.class)
    public ResponseEntity<GeneralResponse> bookingOverlappingException(BookingOverlappingException ex) {
        log.error(ex.getClass().getName(), ex);
        GeneralResponse response = new GeneralResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<GeneralResponse> entityNotFound(EntityNotFoundException ex) {
        log.error(ex.getClass().getName(), ex);
        GeneralResponse response = new GeneralResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
