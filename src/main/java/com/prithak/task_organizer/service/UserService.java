package com.prithak.task_organizer.service;

import com.prithak.task_organizer.dto.RegisterRequestDto;
import com.prithak.task_organizer.dto.UserDto;

public interface UserService {
  UserDto registerUser(RegisterRequestDto registerRequestDto);
}
