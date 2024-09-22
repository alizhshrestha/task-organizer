package com.prithak.task_organizer.service;

import com.prithak.task_organizer.dto.TaskDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface TaskService {
  TaskDto createTask(TaskDto taskDto, Authentication authentication);

  TaskDto getTaskById(Long id);

  List<TaskDto> getAllTask(Authentication authentication);

  TaskDto updateStatusOfTask(Long id);

  TaskDto addCommentToTask(Long id, String comment);

  TaskDto deleteTask(Long id);

  TaskDto updateTask(TaskDto taskDto, Authentication authentication, Long id);
}
