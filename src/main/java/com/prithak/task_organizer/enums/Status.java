package com.prithak.task_organizer.enums;

import com.prithak.task_organizer.constants.CommonMessageConstant;

public enum Status {
  PENDING(CommonMessageConstant.PENDING), COMPLETED(CommonMessageConstant.COMPLETED);

  private final String message;

  Status(String message) {
    this.message = message;
  }

  public String getMessage() {
    return this.message;
  }
}
