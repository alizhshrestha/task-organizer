package com.prithak.task_organizer.service.impl;

import com.prithak.task_organizer.dto.RegisterRequestDto;
import com.prithak.task_organizer.dto.UserDto;
import com.prithak.task_organizer.model.User;
import com.prithak.task_organizer.repository.RoleRepository;
import com.prithak.task_organizer.repository.UserRepository;
import com.prithak.task_organizer.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final ModelMapper modelMapper;

  @Transactional
  @Override
  public UserDto registerUser(RegisterRequestDto registerRequestDto) {
    User user;
    if (Objects.nonNull(registerRequestDto.getId())) {
      user = userRepository.findById(registerRequestDto.getId()).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
      user.setId(registerRequestDto.getId());
      user.setUsername(registerRequestDto.getUsername());
      user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
      user.setEmail(registerRequestDto.getEmail());
      user.setRoles(registerRequestDto.getRoles()
              .stream()
              .map(roleDto -> roleRepository.findByName(roleDto.getName()).orElseThrow(() -> new EntityNotFoundException("Role not found")))
              .collect(Collectors.toSet()));
    } else {
      user = new User();
      user.setUsername(registerRequestDto.getUsername());
      user.setEmail(registerRequestDto.getEmail());
      user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
      user.setRoles(registerRequestDto.getRoles()
              .stream()
              .map(roleDto -> roleRepository.findByName(roleDto.getName()).orElseThrow(() -> new EntityNotFoundException("Role not found")))
              .collect(Collectors.toSet()));
    }
    return modelMapper.map(userRepository.save(user), UserDto.class);
  }
}
