package com.booky.exceptions;

/**
 * @author dragos
 */
public class BookingOverlappingException extends RuntimeException {
    public BookingOverlappingException(String message) {
        super(message);
    }
}
