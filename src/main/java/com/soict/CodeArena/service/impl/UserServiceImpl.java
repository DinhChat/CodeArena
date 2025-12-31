package com.soict.CodeArena.service.impl;

import com.soict.CodeArena.model.MANAGER_ACTION;
import com.soict.CodeArena.model.USER_ROLE;
import com.soict.CodeArena.model.User;
import com.soict.CodeArena.model.UserProfile;
import com.soict.CodeArena.repository.UserProfileRepository;
import com.soict.CodeArena.repository.UserRepository;
import com.soict.CodeArena.request.LoginRequest;
import com.soict.CodeArena.request.ManageAdminRequest;
import com.soict.CodeArena.request.RegisterRequest;
import com.soict.CodeArena.request.UserProfileRequest;
import com.soict.CodeArena.response.PagedResponse;
import com.soict.CodeArena.response.UserManagerResponse;
import com.soict.CodeArena.response.UserProfileResponse;
import com.soict.CodeArena.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private UserProfileRepository userProfileRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserRepository(UserRepository userRepository, UserProfileRepository userProfileRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public User registerUser(RegisterRequest registerRequest) {
        if (userRepository.findByUsername(registerRequest.getUsername()) != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Username already exists");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setHashedPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(USER_ROLE.USER);
        user.setCreatedDate(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        userProfileRepository.save(userProfile);

        return savedUser;
    }

    @Override
    public User loginUser(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername());
        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid username or password");
        }

        if (!passwordEncoder.matches(
                loginRequest.getPassword(),
                user.getHashedPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid username or password");
        }

        return user;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('MANAGER')")
    public PagedResponse<UserManagerResponse> findAllUsers(Integer page, Integer pageSize, Integer offset) {
        int actualPageSize = (pageSize != null && pageSize > 0) ? pageSize : 10;
        int actualPage;

        if (offset != null && offset >= 0) {
            actualPage = offset / actualPageSize;
        } else if (page != null && page >= 0) {
            actualPage = page;
        } else {
            actualPage = 0;
        }

        Pageable pageable = PageRequest.of(actualPage, actualPageSize, Sort.by("createdDate").descending());
        Page<User> userPage = userRepository.findAll(pageable);

        List<UserManagerResponse> responses = userPage.getContent().stream()
                .map(user -> {
                    UserManagerResponse response = new UserManagerResponse();
                    response.setUsername(user.getUsername());
                    response.setRole(user.getRole());
                    return response;
                })
                .collect(Collectors.toList());

        return new PagedResponse<>(
                responses,
                userPage.getNumber(),
                userPage.getSize(),
                userPage.getTotalElements(),
                userPage.getTotalPages(),
                userPage.isLast(),
                userPage.isFirst());
    }

    @Override
    public PagedResponse<UserManagerResponse> findAllUsersByRole(USER_ROLE role, Integer page, Integer pageSize,
            Integer offset) {
        int actualPageSize = (pageSize != null && pageSize > 0) ? pageSize : 10;
        int actualPage;

        if (offset != null && offset >= 0) {
            actualPage = offset / actualPageSize;
        } else if (page != null && page >= 0) {
            actualPage = page;
        } else {
            actualPage = 0;
        }

        Pageable pageable = PageRequest.of(actualPage, actualPageSize, Sort.by("createdDate").descending());
        Page<User> userPage = userRepository.findAllByRole(role, pageable);

        List<UserManagerResponse> responses = userPage.getContent().stream()
                .map(user -> {
                    UserManagerResponse response = new UserManagerResponse();
                    response.setUsername(user.getUsername());
                    response.setRole(user.getRole());
                    return response;
                })
                .collect(Collectors.toList());

        return new PagedResponse<>(
                responses,
                userPage.getNumber(),
                userPage.getSize(),
                userPage.getTotalElements(),
                userPage.getTotalPages(),
                userPage.isLast(),
                userPage.isFirst());
    }

    @Override
    @PreAuthorize("hasAnyAuthority('MANAGER')")
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
    @PreAuthorize("hasAnyAuthority('MANAGER')")
    public UserManagerResponse deleteUserById(Long uid) throws Exception {
        User user = userRepository.findUserByUserId(uid);
        if (user == null) {
            throw new Exception("User not found");
        }
        userRepository.delete(user);
        return new UserManagerResponse(user.getUsername(), user.getRole());
    }

    @Override
    public User findByUsername(String username) throws Exception {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserProfileResponse GetUserProfile(String username) throws Exception {
        User user = userRepository.findByUsername(username);
        UserProfile userProfile = userProfileRepository.findUserProfileByUser(user)
                .orElseThrow(() -> new Exception("User not found"));

        return new UserProfileResponse(
                userProfile.getFullName(),
                userProfile.getAvatarUrl(),
                userProfile.getBio(),
                userProfile.getEmail(),
                userProfile.getPhone(),
                userProfile.getGithub(),
                userProfile.getFacebook(),
                userProfile.getBirthday());
    }

    @Override
    public UserProfileResponse updateProfile(UserProfileRequest req, String username) throws Exception {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new Exception("User not found");
        }

        UserProfile profile = userProfileRepository
                .findUserProfileByUser(user)
                .orElseGet(() -> {
                    UserProfile p = new UserProfile();
                    p.setUser(user);
                    return p;
                });

        profile.setFullName(req.getFullName());
        profile.setAvatarUrl(req.getAvatarUrl());
        profile.setBio(req.getBio());
        profile.setEmail(req.getEmail());
        profile.setPhone(req.getPhone());
        profile.setGithub(req.getGithub());
        profile.setFacebook(req.getFacebook());
        profile.setBirthday(req.getBirthday());

        UserProfile saved = userProfileRepository.save(profile);
        return new UserProfileResponse(
                saved.getFullName(),
                saved.getAvatarUrl(),
                saved.getBio(),
                saved.getEmail(),
                saved.getPhone(),
                saved.getGithub(),
                saved.getFacebook(),
                saved.getBirthday());
    }
}
