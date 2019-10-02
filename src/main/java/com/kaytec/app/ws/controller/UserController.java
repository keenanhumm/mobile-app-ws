package com.kaytec.app.ws.controller;

import com.kaytec.app.ws.model.dto.UserDTO;
import com.kaytec.app.ws.model.request.UserDetailsRequest;
import com.kaytec.app.ws.model.response.UserResponse;
import com.kaytec.app.ws.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users") // http:/localhost:8080/users
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public String getUser() {
        return "Get user was called";
    }
    @PostMapping
    public UserResponse createUser(@RequestBody UserDetailsRequest addUserRequest) {
        UserDTO userRequestDTO = new UserDTO();

        BeanUtils.copyProperties(addUserRequest, userRequestDTO);

        UserDTO createdUserDTO = userService.createUser(userRequestDTO);

        UserResponse createdUserResponse = new UserResponse();
        BeanUtils.copyProperties(createdUserDTO, createdUserResponse);

        return createdUserResponse;
    }
    @PutMapping
    public String updateUser() {
        return "Update user was called";
    }
    @DeleteMapping
    public String deleteUser() {
        return "Delete user was called";
    }

}
