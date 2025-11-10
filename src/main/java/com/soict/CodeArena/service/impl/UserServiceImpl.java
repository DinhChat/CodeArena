package com.soict.CodeArena.service.impl;

import com.soict.CodeArena.model.MANAGER_ACTION;
import com.soict.CodeArena.model.USER_ROLE;
import com.soict.CodeArena.model.User;
import com.soict.CodeArena.repository.UserRepository;
import com.soict.CodeArena.request.ManageAdminRequest;
import com.soict.CodeArena.request.RegisterRequest;
import com.soict.CodeArena.response.UserManagerResponse;
import com.soict.CodeArena.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<UserManagerResponse> findAllUsers() throws Exception {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new Exception("Can't find all users");
        }
        List<UserManagerResponse> userManagerResponses = new ArrayList<>();
        users.forEach(user -> {
            UserManagerResponse userManagerResponse = new UserManagerResponse();
            userManagerResponse.setUsername(user.getUsername());
            userManagerResponse.setRole(USER_ROLE.USER);
            userManagerResponses.add(userManagerResponse);
        });
        return userManagerResponses;
    }

    @Override
    public List<UserManagerResponse> findAllUsersByRole(USER_ROLE role) throws Exception {
        List<User> users = userRepository.findAllByRole(role);
        if (users.isEmpty()) {
            throw new Exception("Can't find all users");
        }

        List<UserManagerResponse> userManagerResponses = new ArrayList<>();
        users.forEach(user -> {
            UserManagerResponse userManagerResponse = new UserManagerResponse();
            userManagerResponse.setUsername(user.getUsername());
            userManagerResponse.setRole(user.getRole());
            userManagerResponses.add(userManagerResponse);
        });
        return userManagerResponses;
    }

    @Override
    public UserManagerResponse manageAdminRole(ManageAdminRequest req) throws Exception {
        User user = userRepository.findByUsername(req.getUsername());
        if (user == null) {
            throw new Exception("User not found");
        }
        if (req.getAction() == MANAGER_ACTION.GRANT) {
            user.setRole(USER_ROLE.ADMIN);
        } else if (req.getAction() == MANAGER_ACTION.REVOKE) {
            user.setRole(USER_ROLE.USER);
        }
        userRepository.save(user);
        return new UserManagerResponse(user.getUsername(), user.getRole());
    }

    @Override
    public UserManagerResponse deleteUserById(Long uid) throws Exception {
        User user = userRepository.findUserByUid(uid);
        if (user == null) {
            throw new Exception("User not found");
        }
        userRepository.delete(user);
        return new UserManagerResponse(user.getUsername(), user.getRole());
    }
}
