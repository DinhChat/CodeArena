package com.soict.CodeArena.service.impl;

import com.soict.CodeArena.model.USER_ROLE;
import com.soict.CodeArena.model.User;
import com.soict.CodeArena.repository.UserRepository;
import com.soict.CodeArena.request.RegisterRequest;
import com.soict.CodeArena.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserRepository(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findByUsername(String username) throws Exception {
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new Exception("Can't find User " + username);
        }
        return user;
    }

    @Override
    public User registerUser(RegisterRequest registerRequest) {
        if (userRepository.findByUsername(registerRequest.getUsername()) != null) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setHashedPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(USER_ROLE.USER);
        user.setCreatedDate(LocalDateTime.now());

        return userRepository.save(user);
    }
}
