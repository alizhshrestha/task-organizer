package com.prithak.task_organizer.controller;

import com.prithak.task_organizer.constants.ExceptionMessageConstant;
import com.prithak.task_organizer.constants.RoleConstant;
import com.prithak.task_organizer.dto.LoginRequestDto;
import com.prithak.task_organizer.dto.RegisterRequestDto;
import com.prithak.task_organizer.dto.UserDto;
import com.prithak.task_organizer.response.ApiResponse;
import com.prithak.task_organizer.response.ExceptionResponse;
import com.prithak.task_organizer.service.UserService;
import com.prithak.task_organizer.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtService;
  private final UserService userService;

  @PostMapping("/login")
  public ResponseEntity<?> authentication(@RequestBody LoginRequestDto loginRequestDto) {
    try {
      Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword())
      );
      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      SecurityContextHolder.getContext().getAuthentication().getDetails();
      SecurityContextHolder.getContext().setAuthentication(authentication);
      Map<String, Object> roleMap = new HashMap<>();
      roleMap.put("role", RoleConstant.ROLE_USER);
      String jwt = jwtService.generateToken(roleMap, userDetails);
      return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, "Successfully logged in", jwt));
    } catch (BadCredentialsException e) {
      log.error("Exception Type : {}, Message : {}", e.getClass().getName(), e.getMessage());
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
    }
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterRequestDto registerRequestDto) {
    try {
      UserDto registeredUser = userService.registerUser(registerRequestDto);
      return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED, "Successfully registered", registeredUser), HttpStatus.CREATED);
    } catch (DataIntegrityViolationException e) {
      log.error("Exception Type : {}, Message : {}", e.getClass().getName(), e.getMessage());
      return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse(HttpStatus.CONFLICT.value(), ExceptionMessageConstant.DUPLICATE_DATA));
    }
  }
}
