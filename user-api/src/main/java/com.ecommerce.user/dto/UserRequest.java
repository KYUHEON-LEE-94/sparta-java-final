package com.ecommerce.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserRequest {

  @javax.validation.constraints.NotBlank(message = "이름이 없습니다.")
  String username;
  String email;
  String phoneNumber;
  String passwordHash;
  String role;
}
