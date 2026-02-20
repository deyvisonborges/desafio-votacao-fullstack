package br.com.deyvisonborges.dbservervotingapi.app.exceptions;

public class ResourceNotFoundException extends RuntimeException {
  public ResourceNotFoundException(final String message) {super(message);}
}
