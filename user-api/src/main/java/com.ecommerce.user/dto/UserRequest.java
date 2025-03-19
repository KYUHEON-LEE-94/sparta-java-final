package com.ecommerce.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserRequest {

  @NotBlank(message = "이름이 없습니다.")
  String username;
  String email;
  String passwordHash;
  String phoneNumber;
  String address;
  String role;
}
