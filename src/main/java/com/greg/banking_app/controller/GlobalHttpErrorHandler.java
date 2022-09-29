package com.greg.banking_app.controller;

import com.greg.banking_app.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalHttpErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException exception) {
        return new ResponseEntity<>("User with given Id doesn't exist", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Object> handleAccountNotFoundException(AccountNotFoundException exception) {
        return new ResponseEntity<>("Account with given Id doesn't exist", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LoanNotFoundException.class)
    public ResponseEntity<Object> handleLoanNotFoundException(LoanNotFoundException exception) {
        return new ResponseEntity<>("Loan with given Id doesn't exist", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OperationNotFoundException.class)
    public ResponseEntity<Object> handleOperationNotFoundException(OperationNotFoundException exception) {
        return new ResponseEntity<>("Operation with given Id doesn't exist", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAddressNotFoundException.class)
    public ResponseEntity<Object> handleUserAddressNotFoundException(UserAddressNotFoundException exception) {
        return new ResponseEntity<>("Address with given Id doesn't exist", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InstallmentNotFoundException.class)
    public ResponseEntity<Object> handleInstallmentNotFoundException(InstallmentNotFoundException exception) {
        return new ResponseEntity<>("Installment with given Id doesn't exist", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LoanNotCreatedException.class)
    public ResponseEntity<Object> handleLoanNotCreatedException(LoanNotCreatedException exception) {
        return new ResponseEntity<>("Something went wrong. I didn't create new Loan", HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(CurrencyNotFoundException.class)
    public ResponseEntity<Object> handleCurrencyNotFoundException(CurrencyNotFoundException exception) {
        return new ResponseEntity<>("You entered the wrong currency IsoCode. Please check it and send it again.", HttpStatus.BAD_REQUEST);
    }
}
