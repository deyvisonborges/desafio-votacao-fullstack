package br.com.deyvisonborges.dbservervotingapi.app.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
    ErrorResponse error = new ErrorResponse(
      ex.getMessage(),
      HttpStatus.NOT_FOUND.value(),
      System.currentTimeMillis()
    );
    log.error("ResourceNotFoundException: {}", ex.getMessage());
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }
  
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusinessDomain(BusinessException ex) {
    ErrorResponse error = new ErrorResponse(
      ex.getMessage(),
      HttpStatus.BAD_REQUEST.value(),
      System.currentTimeMillis()
    );
    log.error("BusinessException: {}", ex.getMessage());
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }
}

