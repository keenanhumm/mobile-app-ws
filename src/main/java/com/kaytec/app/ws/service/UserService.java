package com.kaytec.app.ws.service;

import com.kaytec.app.ws.model.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDTO createUser(UserDTO userDTO);
    UserDTO updateUser(String id, UserDTO userDTO);
    UserDTO getUser(String email);
    UserDTO getUserByUserId(String id);
    void deleteUser(String userId);
}
