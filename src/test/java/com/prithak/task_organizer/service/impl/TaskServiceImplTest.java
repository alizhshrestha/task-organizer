package com.prithak.task_organizer.service.impl;

import com.prithak.task_organizer.dto.CommentDto;
import com.prithak.task_organizer.dto.TaskDto;
import com.prithak.task_organizer.dto.UserDto;
import com.prithak.task_organizer.enums.Status;
import com.prithak.task_organizer.exceptions.TaskException;
import com.prithak.task_organizer.model.Comment;
import com.prithak.task_organizer.model.Task;
import com.prithak.task_organizer.model.User;
import com.prithak.task_organizer.repository.TaskRepository;
import com.prithak.task_organizer.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class TaskServiceImplTest {
  @Mock
  private TaskRepository taskRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private ModelMapper modelMapper;
  @Mock
  private Authentication authentication;
  @InjectMocks
  private TaskServiceImpl taskService;

  private UserDto userDto;
  private TaskDto taskDto;
  private Task task;
  private User user;

  @Before
  public void setUp() {
    userDto = new UserDto();
    userDto.setUsername("testUser");

    taskDto = new TaskDto();
    taskDto.setId(null);
    taskDto.setTitle("Title");
    taskDto.setDescription("Description");
    taskDto.setAssignee(userDto);
    taskDto.setComments(new ArrayList<>());

    user = new User();
    user.setUsername("testUser");

    task = new Task();
    task.setTitle("Title");
    task.setDescription("Description");
    task.setAssignee(user);
    task.setCreatedBy(user);
    task.setComments(new ArrayList<>());

    Mockito.when(authentication.getPrincipal()).thenReturn(user);
  }


  @Test
  public void testCreateTask_whenTaskDtoHasNoId_shouldCreateTask() {
    Mockito.when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
    Mockito.when(modelMapper.map(any(TaskDto.class), eq(Task.class))).thenReturn(task);
    Mockito.when(modelMapper.map(any(Task.class), eq(TaskDto.class))).thenReturn(taskDto);
    Mockito.when(taskRepository.save(any(Task.class))).thenReturn(task);
    TaskDto result = taskService.createTask(taskDto, authentication);
    assertNotNull(result);
    assertEquals(taskDto.getTitle(), result.getTitle());
    Mockito.verify(userRepository, times(1)).findByUsername(anyString());
    Mockito.verify(taskRepository, times(1)).save(any(Task.class));
  }

  @Test(expected = UsernameNotFoundException.class)
  public void testCreateTask_whenUserNotFound_shouldThrowUserNotFoundException() {
    Mockito.when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
    taskService.createTask(taskDto, authentication);
  }

  @Test(expected = TaskException.class)
  public void testCreateTask_whenTaskNotFound_shouldThrowTaskException() {
    taskDto.setId(1L);
    Mockito.when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());
    taskService.createTask(taskDto, authentication);
  }

  @Test
  public void testGetTaskById_whenTaskExists_shouldReturnTaskDto() {
    task.setId(1L);
    taskDto.setId(1L);
    Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
    Mockito.when(modelMapper.map(task, TaskDto.class)).thenReturn(taskDto);
    TaskDto result = taskService.getTaskById(1L);
    assertNotNull(result);
    assertEquals("Title", result.getTitle());
    Mockito.verify(taskRepository, times(1)).findById(1L);
    Mockito.verify(modelMapper, times(1)).map(task, TaskDto.class);
  }

  @Test(expected = TaskException.class)
  public void testGetTaskById_whenTaskNotFound_shouldThrowTaskException() {
    task.setId(1L);
    taskDto.setId(1L);
    Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.empty());
    taskService.getTaskById(1L);
    Mockito.verify(taskRepository, times(1)).findById(1L);
    Mockito.verify(modelMapper, times(0)).map(any(Task.class), eq(TaskDto.class));
  }

  @Test
  public void taskGetAllTask_whenTasksExist_shouldReturnTaskDtoList() {
    user.setId(1L);
    Mockito.when(authentication.getPrincipal()).thenReturn(user);
    List<Task> tasks = Collections.singletonList(task);
    Mockito.when(taskRepository.findByAssigneeOrCreatedBy(user, user)).thenReturn(Optional.of(tasks));
    Mockito.when(modelMapper.map(task, TaskDto.class)).thenReturn(taskDto);
    List<TaskDto> result = taskService.getAllTask(authentication);
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Title", result.get(0).getTitle());
    Mockito.verify(authentication, times(1)).getPrincipal();
    Mockito.verify(taskRepository, times(1)).findByAssigneeOrCreatedBy(user, user);
    Mockito.verify(modelMapper, times(1)).map(task, TaskDto.class);
  }

  @Test(expected = TaskException.class)
  public void taskGetAllTask_whenTaskNotFound_shouldThrowTaskException() {
    Mockito.when(authentication.getPrincipal()).thenReturn(user);
    Mockito.when(taskRepository.findByAssigneeOrCreatedBy(user, user)).thenReturn(Optional.empty());
    taskService.getAllTask(authentication);
    Mockito.verify(authentication, times(1)).getPrincipal();
    Mockito.verify(taskRepository, times(1)).findByAssigneeOrCreatedBy(user, user);
    Mockito.verify(modelMapper, times(0)).map(any(Task.class), eq(TaskDto.class));
  }

  @Test
  public void taskUpdateTaskStatus_whenStatusIsPending_shouldSetStatusToCompleted() {
    taskDto.setStatus(Status.COMPLETED);
    task.setStatus(Status.PENDING);
    Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
    task.setStatus(Status.COMPLETED);
    Mockito.when(taskRepository.save(task)).thenReturn(task);
    Mockito.when(modelMapper.map(task, TaskDto.class)).thenReturn(taskDto);
    TaskDto result = taskService.updateStatusOfTask(1L);
    assertNotNull(result);
    assertEquals(Status.COMPLETED, result.getStatus());
    Mockito.verify(taskRepository, times(1)).findById(1L);
    Mockito.verify(taskRepository, times(1)).save(task);
    Mockito.verify(modelMapper, times(1)).map(task, TaskDto.class);
  }

  @Test
  public void testUpdateStatusOfTask_whenStatusIsCompleted_shouldSetStatusToPending() {
    task.setStatus(Status.COMPLETED);
    Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
    taskDto.setStatus(Status.PENDING);
    Mockito.when(taskRepository.save(task)).thenReturn(task);
    taskDto.setStatus(Status.PENDING);
    Mockito.when(modelMapper.map(task, TaskDto.class)).thenReturn(taskDto);
    TaskDto result = taskService.updateStatusOfTask(1L);
    assertNotNull(result);
    assertEquals(Status.PENDING, result.getStatus());
    Mockito.verify(taskRepository, times(1)).findById(1L);
    Mockito.verify(taskRepository, times(1)).save(task);
    Mockito.verify(modelMapper, times(1)).map(task, TaskDto.class);
  }

  @Test(expected = TaskException.class)
  public void testUpdateStatusOfTask_whenTaskNotFound_shouldThrowTaskException() {
    Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.empty());
    taskService.updateStatusOfTask(1L);
    Mockito.verify(taskRepository, times(1)).findById(1L);
    Mockito.verify(taskRepository, times(0)).save(any(Task.class));
    Mockito.verify(modelMapper, times(0)).map(any(Task.class), eq(TaskDto.class));
  }

  @Test
  public void testTaskToAddComment_whenTaskExists_shouldAddCommentAndReturnTaskDto() {
    Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
    Comment comment = new Comment();
    comment.setName("Test comment");
    task.addComment(comment);
    Mockito.when(taskRepository.save(task)).thenReturn(task);
    CommentDto commentDto = new CommentDto();
    commentDto.setName("Test Comment");
    taskDto.getComments().add(commentDto);
    Mockito.when(modelMapper.map(task, TaskDto.class)).thenReturn(taskDto);
    TaskDto result = taskService.addCommentToTask(1L, "Test Comment");
    assertNotNull(result);
    assertEquals(1, result.getComments().size());
    assertEquals("Test Comment", result.getComments().get(0).getName());
    Mockito.verify(taskRepository, times(1)).findById(1L);
    Mockito.verify(taskRepository, times(1)).save(task);
    Mockito.verify(modelMapper, times(1)).map(task, TaskDto.class);
  }

  @Test(expected = TaskException.class)
  public void testTaskToAddComment_whenTaskNotFound_shouldThrowTaskException() {
    Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.empty());
    taskService.addCommentToTask(1L, "Test Comment");
    Mockito.verify(taskRepository, times(1)).findById(1L);
    Mockito.verify(taskRepository, times(0)).save(any(Task.class));
    Mockito.verify(modelMapper, times(0)).map(any(Task.class), eq(TaskDto.class));
  }

  @Test
  public void testTaskIsDeleted_whenTaskExists_shouldMarkDeletedAsTrueAndReturnTaskDto() {
    task.setDeleted(false);
    taskDto.setDeleted(true);
    Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
    task.setDeleted(true);
    Mockito.when(taskRepository.save(task)).thenReturn(task);
    Mockito.when(modelMapper.map(task, TaskDto.class)).thenReturn(taskDto);
    TaskDto result = taskService.deleteTask(1L);
    assertNotNull(result);
    assertTrue(result.isDeleted());
    Mockito.verify(taskRepository, times(1)).findById(1L);
    Mockito.verify(taskRepository, times(1)).save(task);
    Mockito.verify(modelMapper, times(1)).map(task, TaskDto.class);
  }

  @Test(expected = TaskException.class)
  public void testDeleteTask_whenTaskNotFound_shouldThrowTaskException() {
    Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.empty());
    taskService.deleteTask(1L);
    Mockito.verify(taskRepository, times(1)).findById(1L);
    Mockito.verify(taskRepository, times(0)).save(any(Task.class));
    Mockito.verify(modelMapper, times(0)).map(any(Task.class), eq(TaskDto.class));
  }
}