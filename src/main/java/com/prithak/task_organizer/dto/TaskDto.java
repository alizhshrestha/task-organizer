package com.prithak.task_organizer.dto;

import com.prithak.task_organizer.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
  private Long id;
  private String title;
  private String description;
  private LocalDate dueDate;
  private String attachment;
  private UserDto assignee;
  private UserDto createdBy;
  private Status status;
  private List<CommentDto> comments = new ArrayList<>();
  private boolean deleted;
}
