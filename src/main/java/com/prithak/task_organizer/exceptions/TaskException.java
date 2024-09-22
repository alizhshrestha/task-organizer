package com.prithak.task_organizer.exceptions;

public class TaskException extends RuntimeException {
  private String message;

  public TaskException(String message) {
    super(message);
  }
}
