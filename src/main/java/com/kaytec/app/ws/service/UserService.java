package com.kaytec.app.ws.service;

import com.kaytec.app.ws.model.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserDTO createUser(UserDTO userDTO);
    UserDTO updateUser(String id, UserDTO userDTO);
    UserDTO getUser(String email);
    List<UserDTO> getUsers(int page, int limit);
    UserDTO getUserByUserId(String id);
    void deleteUser(String userId);
}
