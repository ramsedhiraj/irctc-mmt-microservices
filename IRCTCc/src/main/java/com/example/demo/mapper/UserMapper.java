package com.example.demo.mapper;

import org.springframework.stereotype.Component;
import com.example.demo.dto.request.UserRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.User;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setAddress(user.getAddress());
        response.setAadhar(user.getAadhar());
        response.setEmail(user.getEmail());
        return response;
    }

    public User toEntity(UserRequest request) {
        if (request == null) {
            return null;
        }
        User user = new User();
        user.setName(request.getName());
        user.setAddress(request.getAddress());
        user.setAadhar(request.getAadhar());
        user.setEmail(request.getEmail());
        return user;
    }

    public void updateEntity(UserRequest request, User user) {
        if (request == null || user == null) {
            return;
        }
        user.setName(request.getName());
        user.setAddress(request.getAddress());
        user.setAadhar(request.getAadhar());
        user.setEmail(request.getEmail());
    }
}
