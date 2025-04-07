package com.tienda_back.Controller;

import com.tienda_back.model.exception.DuplicateDataException;
import com.tienda_back.model.exception.ErrorResponse;
import com.tienda_back.model.exception.JsonNullException;
import com.tienda_back.model.exception.ResourceNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> defaultErrorHandler(Exception ex) {
        System.err.println(ex.getMessage());
        log.error(ex.getMessage());
        ex.printStackTrace();
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JsonNullException.class)
    public ResponseEntity<ErrorResponse> handleJsonNullException(JsonNullException ex) {
        String message = (ex != null && ex.getMessage() != null) ? ex.getMessage() : "The request body is empty or invalid";
        ErrorResponse error = new ErrorResponse(message, HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handelBadCredentialsException(BadCredentialsException ex) {
        String message = (ex != null && ex.getMessage() != null) ? ex.getMessage() : "Authentication is required or has failed";
        ErrorResponse error = new ErrorResponse(message, HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        String message = (ex != null && ex.getMessage() != null) ? ex.getMessage() : "Access is denied";
        ErrorResponse error = new ErrorResponse(message, HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DuplicateDataException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateDataException(DuplicateDataException ex) {
        String message = (ex != null && ex.getMessage() != null) ? ex.getMessage() : "Data Duplicated";
        ErrorResponse error = new ErrorResponse(message, HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        String message = (ex != null && ex.getMessage() != null) ? ex.getMessage() : "User Not Found";
        ErrorResponse error = new ErrorResponse(message, HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwtException(ExpiredJwtException ex) {
        String message = (ex != null && ex.getMessage() != null) ? ex.getMessage() : "Token Expired";
        ErrorResponse error = new ErrorResponse(message, HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEntryException(DataIntegrityViolationException ex) {
        String message = "This data is already associated with another user.";

        if (ex.getCause() instanceof ConstraintViolationException) {
            message = "The value you are trying to save is already in use by another user.";
        }

        ErrorResponse error = new ErrorResponse(message, HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}
