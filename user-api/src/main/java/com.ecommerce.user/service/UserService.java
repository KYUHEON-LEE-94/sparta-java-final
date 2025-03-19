package com.ecommerce.user.service;

import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.user.model.User;
import com.ecommerce.user.repository.UserRepository;
import com.project.springmasters.common.exception.ServiceException;
import com.project.springmasters.common.exception.ServiceExceptionCode;
import com.project.springmasters.domain.user.dto.UserRequest;
import com.project.springmasters.domain.user.dto.UserResponse;
import com.project.springmasters.domain.user.entity.User;
import com.project.springmasters.domain.user.mapper.UserMapper;
import com.project.springmasters.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public UserResponse create(UserRequest request) {
    User userRequest = UserMapper.INSTANCE.toEntity(request);
    User user = userRepository.save(userRequest);
    return UserMapper.INSTANCE.toResponse(user);
  }

  public User getUser(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_USER));
  }

  public Optional<User> findUserById(Long id) {
    return userRepository.findById(id);
  }



}
