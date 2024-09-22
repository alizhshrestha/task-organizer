package com.prithak.task_organizer.enums;

import com.prithak.task_organizer.constants.ResponseMessageConstant;

public enum ResponseType {
  SUCCESS(ResponseMessageConstant.SUCCESS), FAILURE(ResponseMessageConstant.ERROR);

  private final String message;

  ResponseType(String message) {
    this.message = message;
  }

  public String getMessage() {
    return this.message;
  }
}
