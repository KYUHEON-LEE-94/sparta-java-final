package com.ecommerce.user.service;

import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.user.metric.annotation.TimedMetric;
import com.ecommerce.user.model.User;
import com.ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  @TimedMetric("user_create")
  public UserResponse create(UserRequest request) {
    User user = buildUser(request);

    User userEntity = userRepository.save(user);

    return buildUserResponse(userEntity);
  }

  @TimedMetric("user_get_by_id")
  public UserResponse findUserById(Long id) {
    User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    return buildUserResponse(user);
  }

  private UserResponse buildUserResponse(User userEntity) {
    return UserResponse.builder()
            .id(userEntity.getId())
            .username(userEntity.getUsername())
            .phoneNumber(userEntity.getPhoneNumber())
            .email(userEntity.getEmail())
            .addres(userEntity.getAddress())
            .role(userEntity.getRole())
            .createdAt(userEntity.getCreatedAt())
            .updatedAt(userEntity.getUpdatedAt())
            .build();
  }

  private User buildUser(UserRequest request) {
    User user = new User();
    user.setUsername(request.getUsername());
    user.setEmail(request.getEmail());
    user.setPasswordHash(request.getPasswordHash());
    user.setPhoneNumber(request.getPhoneNumber());
    user.setAddress(request.getAddress());
    user.setDeletedYn(false);
    return user;
  }
}
