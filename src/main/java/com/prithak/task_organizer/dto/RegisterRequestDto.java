package com.prithak.task_organizer.dto;

import com.prithak.task_organizer.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDto {
  private Long id;
  private String username;
  private String password;
  private String email;
  private Set<RoleDto> roles;
}
