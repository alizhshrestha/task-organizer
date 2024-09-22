package com.prithak.task_organizer.service.impl;

import com.prithak.task_organizer.constants.ExceptionMessageConstant;
import com.prithak.task_organizer.dto.TaskDto;
import com.prithak.task_organizer.enums.Status;
import com.prithak.task_organizer.exceptions.TaskException;
import com.prithak.task_organizer.model.Comment;
import com.prithak.task_organizer.model.Task;
import com.prithak.task_organizer.model.User;
import com.prithak.task_organizer.repository.TaskRepository;
import com.prithak.task_organizer.repository.UserRepository;
import com.prithak.task_organizer.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
  private final TaskRepository taskRepository;
  private final ModelMapper modelMapper;
  private final UserRepository userRepository;

  @Override
  public TaskDto createTask(TaskDto taskDto, Authentication authentication) {
    User loggedInUser = (User) authentication.getPrincipal();
    Task task;
    if (Objects.nonNull(taskDto.getId())) {
      task = taskRepository.findById(taskDto.getId()).orElseThrow(() -> new TaskException(ExceptionMessageConstant.TASK_NOT_FOUND));
      User user = userRepository.findByUsername(taskDto.getAssignee().getUsername()).orElseThrow(() -> new UsernameNotFoundException(ExceptionMessageConstant.USER_NOT_FOUND));
      task.setTitle(taskDto.getTitle());
      task.setDescription(taskDto.getDescription());
      task.setDueDate(taskDto.getDueDate());
      task.setAttachment(taskDto.getAttachment());
      task.setAssignee(user);
      task.setCreatedBy(loggedInUser);
      task.setUpdatedBy(loggedInUser);
      task.setStatus(null != taskDto.getStatus() ? taskDto.getStatus() : Status.PENDING);
    } else {
      task = modelMapper.map(taskDto, Task.class);
      User user = userRepository.findByUsername(taskDto.getAssignee().getUsername()).orElseThrow(() -> new UsernameNotFoundException(ExceptionMessageConstant.USER_NOT_FOUND));
      task.setAssignee(user);
      task.setCreatedBy(loggedInUser);
      task.setStatus(Status.PENDING);
    }
    Task savedUser = taskRepository.save(task);
    return modelMapper.map(savedUser, TaskDto.class);
  }

  @Override
  public TaskDto getTaskById(Long id) {
    return modelMapper.map(taskRepository.findById(id).orElseThrow(() -> new TaskException(ExceptionMessageConstant.TASK_NOT_FOUND)), TaskDto.class);
  }

  @Override
  public List<TaskDto> getAllTask(Authentication authentication) {
    User loggedInUser = (User) authentication.getPrincipal();
    List<Task> tasks = taskRepository.findByAssigneeOrCreatedBy(loggedInUser, loggedInUser).orElseThrow(() -> new TaskException(ExceptionMessageConstant.TASK_NOT_FOUND));
    return tasks.stream().map(task -> modelMapper.map(task, TaskDto.class)).toList();
  }

  @Override
  public TaskDto updateTask(TaskDto taskDto, Authentication authentication, Long id) {
    User loggedInUser = (User) authentication.getPrincipal();
    Task task = taskRepository.findById(id).orElseThrow(() -> new TaskException(ExceptionMessageConstant.TASK_NOT_FOUND));
    User user = userRepository.findByUsername(taskDto.getAssignee().getUsername()).orElseThrow(() -> new UsernameNotFoundException(ExceptionMessageConstant.USER_NOT_FOUND));
    task.setTitle(taskDto.getTitle());
    task.setDescription(taskDto.getDescription());
    task.setDueDate(taskDto.getDueDate());
    task.setAttachment(taskDto.getAttachment());
    task.setAssignee(user);
    task.setCreatedBy(loggedInUser);
    task.setUpdatedBy(loggedInUser);
    task.setStatus(null != taskDto.getStatus() ? taskDto.getStatus() : Status.PENDING);
    return modelMapper.map(taskRepository.save(task), TaskDto.class);
  }

  @Override
  public TaskDto updateStatusOfTask(Long id) {
    Task task = taskRepository.findById(id).orElseThrow(() -> new TaskException(ExceptionMessageConstant.TASK_NOT_FOUND));
    if (task.getStatus().equals(Status.PENDING)) {
      task.setStatus(Status.COMPLETED);
    } else {
      task.setStatus(Status.PENDING);
    }
    return modelMapper.map(taskRepository.save(task), TaskDto.class);
  }

  @Transactional
  @Override
  public TaskDto addCommentToTask(Long id, String commentText) {
    Task task = taskRepository.findById(id).orElseThrow(() -> new TaskException(ExceptionMessageConstant.TASK_NOT_FOUND));
    Comment comment = new Comment();
    comment.setName(commentText);
    task.addComment(comment);
    Task savedTask = taskRepository.save(task);
    return modelMapper.map(savedTask, TaskDto.class);
  }

  @Override
  public TaskDto deleteTask(Long id) {
    Task task = taskRepository.findById(id).orElseThrow(() -> new TaskException(ExceptionMessageConstant.TASK_NOT_FOUND));
    task.setDeleted(Boolean.TRUE);
    return modelMapper.map(taskRepository.save(task), TaskDto.class);
  }
}
