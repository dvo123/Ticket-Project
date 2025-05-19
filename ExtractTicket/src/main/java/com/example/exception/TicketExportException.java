package com.example.exception;

@SuppressWarnings("serial")
public class TicketExportException extends RuntimeException {
    public TicketExportException(String message) {
        super(message);
    }

    public TicketExportException(String message, Throwable cause) {
        super(message, cause);
    }
}