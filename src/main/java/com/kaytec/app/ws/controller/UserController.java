package com.kaytec.app.ws.controller;

import com.kaytec.app.ws.exceptions.UserServiceException;
import com.kaytec.app.ws.model.dto.UserDTO;
import com.kaytec.app.ws.model.request.UserDetailsRequest;
import com.kaytec.app.ws.model.response.ErrorMessages;
import com.kaytec.app.ws.model.response.UserResponse;
import com.kaytec.app.ws.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users") // http:/localhost:8080/users
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(path="/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserResponse getUser(@PathVariable String id) {
        UserResponse userFoundResponse = new UserResponse();

        UserDTO userFoundDTO = userService.getUserByUserId(id);

        BeanUtils.copyProperties(userFoundDTO, userFoundResponse);

        return userFoundResponse;
    }

    @PostMapping(
            consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
            produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
            )
    public UserResponse createUser(@RequestBody UserDetailsRequest addUserRequest) throws Exception{
        UserDTO userRequestDTO = new UserDTO();

        if (addUserRequest.getFirstName().isEmpty()) {
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

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
