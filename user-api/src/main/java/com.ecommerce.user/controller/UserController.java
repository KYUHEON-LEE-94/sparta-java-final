package com.project.springmasters.domain.user.controller;

import com.project.springmasters.common.ApiResponse;
import com.project.springmasters.domain.user.dto.UserRequest;
import com.project.springmasters.domain.user.dto.UserResponse;
import com.project.springmasters.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping
  public ApiResponse<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
    UserResponse userResponse = userService.create(userRequest);
    return ApiResponse.Success(userResponse);
  }
}
