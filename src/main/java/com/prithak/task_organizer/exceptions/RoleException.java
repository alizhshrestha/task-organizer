package com.prithak.task_organizer.exceptions;

public class RoleException extends RuntimeException {
  private String message;

  public RoleException(String message) {
    super(message);
  }
}
