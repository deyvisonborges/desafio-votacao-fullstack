package br.com.deyvisonborges.dbservervotingapi.app.exceptions;

public class BusinessException extends RuntimeException {
  public BusinessException(final String message) {
    super(message);
  }
}