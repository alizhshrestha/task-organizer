package com.prithak.task_organizer.controller;

import com.prithak.task_organizer.constants.CommonMessageConstant;
import com.prithak.task_organizer.dto.TaskDto;
import com.prithak.task_organizer.enums.ResponseType;
import com.prithak.task_organizer.response.ApiResponse;
import com.prithak.task_organizer.response.ExceptionResponse;
import com.prithak.task_organizer.service.TaskService;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Slf4j
public class TaskController {
  private final TaskService taskService;

  @PostMapping
  public ResponseEntity<?> createTask(@RequestBody TaskDto taskDto, Authentication authentication) {
    try {
      return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, ResponseType.SUCCESS.getMessage(), taskService.createTask(taskDto, authentication)));
    } catch (BadCredentialsException | SignatureException e) {
      log.error("Exception Type : {}, Message : {}", e.getClass().getName(), e.getMessage());
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse> getTaskById(@PathVariable Long id) {
    return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, ResponseType.SUCCESS.getMessage(), taskService.getTaskById(id)));
  }

  @GetMapping
  public ResponseEntity<ApiResponse> getAllTasks(Authentication authentication) {
    return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, ResponseType.SUCCESS.getMessage(), taskService.getAllTask(authentication)));
  }

  //Not necessarily important as createTask method can handle both create and edit task.
  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse> updateTask(Authentication authentication, @PathVariable Long id, @RequestBody TaskDto taskDto) {
    return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, ResponseType.SUCCESS.getMessage(), taskService.updateTask(taskDto, authentication, id)));
  }

  @PutMapping("/{id}/transition")
  public ResponseEntity<ApiResponse> updateStatusOfTask(@PathVariable Long id) {
    return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, ResponseType.SUCCESS.getMessage(), taskService.updateStatusOfTask(id)));
  }

  @PutMapping("/{id}/comment")
  public ResponseEntity<ApiResponse> addCommentToTask(@PathVariable Long id, @RequestParam String comment) {
    return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, ResponseType.SUCCESS.getMessage(), taskService.addCommentToTask(id, comment)));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse> deleteTask(@PathVariable Long id) {
    return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, CommonMessageConstant.DELETED, taskService.deleteTask(id)));
  }
}
