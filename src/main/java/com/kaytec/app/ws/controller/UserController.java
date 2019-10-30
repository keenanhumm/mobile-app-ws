package com.kaytec.app.ws.controller;

import com.kaytec.app.ws.model.dto.UserDTO;
import com.kaytec.app.ws.model.enums.OperationName;
import com.kaytec.app.ws.model.enums.OperationStatus;
import com.kaytec.app.ws.model.request.UserDetailsRequest;
import com.kaytec.app.ws.model.response.OperationStatusResult;
import com.kaytec.app.ws.model.response.UserResponse;
import com.kaytec.app.ws.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

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
    public UserResponse createUser(@Valid @RequestBody UserDetailsRequest addUserRequest){
        UserDTO userRequestDTO = new UserDTO();

        BeanUtils.copyProperties(addUserRequest, userRequestDTO);

        UserDTO createdUserDTO = userService.createUser(userRequestDTO);

        UserResponse createdUserResponse = new UserResponse();
        BeanUtils.copyProperties(createdUserDTO, createdUserResponse);

        return createdUserResponse;
    }

    @PutMapping(
            path="/{id}",
            consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
            produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
            )
    public UserResponse updateUser(@PathVariable String id, @RequestBody UserDetailsRequest updateUserRequest) {
        UserDTO userRequestDTO = new UserDTO();

        BeanUtils.copyProperties(updateUserRequest, userRequestDTO);

        UserDTO updateUserDTO = userService.updateUser(id, userRequestDTO);

        UserResponse updatedUserResponse = new UserResponse();
        BeanUtils.copyProperties(updateUserDTO, updatedUserResponse);

        return updatedUserResponse;
    }

    @DeleteMapping(
            path="/{id}",
            consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
            produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
    )
    public OperationStatusResult deleteUser(@PathVariable String id) {
        OperationStatusResult operationStatusResult = new OperationStatusResult();

        operationStatusResult.setOperationName(OperationName.DELETE.name());

        userService.deleteUser(id);

        operationStatusResult.setOperationResult(OperationStatus.SUCCESS.name());

        return operationStatusResult;
    }

    @GetMapping(
        produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }
    )
    public List<UserResponse> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                       @RequestParam(value = "limit", defaultValue = "25") int limit)
    {
        if (page > 0) page = page - 1;

        List<UserResponse> users = new ArrayList<>();

        List<UserDTO> userDTOs = userService.getUsers(page, limit);

        userDTOs.parallelStream().forEach(userDTO -> {
            UserResponse userResponse = new UserResponse();
            BeanUtils.copyProperties(userDTO, userResponse);
            users.add(userResponse);
        });

        return users;
    }

}
