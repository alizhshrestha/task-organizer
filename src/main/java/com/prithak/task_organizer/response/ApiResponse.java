package com.prithak.task_organizer.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {
  private HttpStatus status;
  private String message;
  private Object data;
  public ApiResponse(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }
}
