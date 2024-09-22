package com.prithak.task_organizer.dto;

import com.prithak.task_organizer.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
  private Long id;
  private String username;
  private String email;
  private String password;
  private Set<RoleDto> roles = new HashSet<>();
}
