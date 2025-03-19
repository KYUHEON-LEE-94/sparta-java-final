package com.ecommerce.user.controller;

import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("users")
@Tag(name = "User API", description = "사용자 API")
public class UserController {

  private final UserService userService;

  @GetMapping("/{id}")
  @Operation(summary = "사용자 조회", description = "ID를 통해 사용자의 정보를 조회합니다.")
  public ResponseEntity<UserResponse> createUser(@PathVariable Long id) {
    UserResponse userResponse = userService.findUserById(id);
    return ResponseEntity.ok(userResponse);
  }

  @PostMapping
  public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) {
    UserResponse userResponse = userService.create(userRequest);

    return ResponseEntity.status(201).body(userResponse);
  }
}
