package com.kaytec.app.ws.service.impl;

import com.kaytec.app.ws.UserRepository;
import com.kaytec.app.ws.io.entity.UserEntity;
import com.kaytec.app.ws.model.dto.UserDTO;
import com.kaytec.app.ws.service.UserService;
import com.kaytec.app.ws.shared.Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }
}
