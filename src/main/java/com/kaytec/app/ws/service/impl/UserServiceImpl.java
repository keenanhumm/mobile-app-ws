package com.kaytec.app.ws.service.impl;

import com.kaytec.app.ws.exceptions.UserServiceException;
import com.kaytec.app.ws.io.entity.UserEntity;
import com.kaytec.app.ws.io.repositories.UserRepository;
import com.kaytec.app.ws.model.dto.UserDTO;
import com.kaytec.app.ws.model.response.ErrorMessages;
import com.kaytec.app.ws.service.UserService;
import com.kaytec.app.ws.shared.Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    Utils utils;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        if(userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new RuntimeException("User already exists with this email");
        }

        // translate dto to entity
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDTO, userEntity);

        // create usesr id
        String publicUserId = utils.generateUserId(30);
        userEntity.setUserId(publicUserId);
        userEntity.setEncryptedPassword(passwordEncoder.encode(userDTO.getPassword()));

        // save entity
        UserEntity createdUserEntity = userRepository.save(userEntity);

        // translate entity to dto
        UserDTO createdUserDTO = new UserDTO();
        BeanUtils.copyProperties(createdUserEntity, createdUserDTO);

        // and return it
        return createdUserDTO;
    }

    @Override
    public UserDTO updateUser(String id, UserDTO userDTO) {
        UserDTO updatedUserDTO = new UserDTO();
        UserEntity userEntity = userRepository.findByUserId(id);

        if(userEntity == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        userEntity.setFirstName(userDTO.getFirstName());
        userEntity.setLastName(userDTO.getLastName());

        UserEntity updatedUserEntity = userRepository.save(userEntity);

        BeanUtils.copyProperties(updatedUserEntity, updatedUserDTO);

        return updatedUserDTO;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntityFound = userRepository.findByEmail(email);

        if (userEntityFound == null) throw new UsernameNotFoundException(email);
        return new User(userEntityFound.getEmail(), userEntityFound.getEncryptedPassword(), new ArrayList<>());
    }

    @Override
    public UserDTO getUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) throw new UsernameNotFoundException(email);

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userEntity, userDTO);
        return userDTO;
    }

    @Override
    public UserDTO getUserByUserId(String userId) {
        UserDTO userFoundDTO = new UserDTO();
        UserEntity userEntity = userRepository.findByUserId(userId);

        if(userEntity == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        BeanUtils.copyProperties(userEntity, userFoundDTO);

        return userFoundDTO;
    }

    @Override
    public void deleteUser(String userId) {
        UserEntity targetUser = userRepository.findByUserId(userId);

        if(targetUser == null) {
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }

        userRepository.delete(targetUser);
    }
}
