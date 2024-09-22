package com.prithak.task_organizer.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {
  @JsonIgnore
  private Long id;
  private String name;
  private Set<PermissionDto> permissions = new HashSet<>();
}
