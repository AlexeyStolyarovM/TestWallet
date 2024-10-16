package org.example.testwallet.conroller;

import org.example.testwallet.exception.InsufficientFundsException;
import org.example.testwallet.exception.WalletNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<String> handleWalletNotFoundException(WalletNotFoundException e) {
        logger.error("Wallet not found: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<String> handleInsufficientFundsException(InsufficientFundsException e) {
        logger.error("Insufficient funds: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        logger.error("An unexpected error occurred: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
    }
}
