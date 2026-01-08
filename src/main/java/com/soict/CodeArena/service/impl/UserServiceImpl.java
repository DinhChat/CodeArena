package com.soict.CodeArena.service.impl;

import com.soict.CodeArena.model.MANAGER_ACTION;
import com.soict.CodeArena.model.USER_ROLE;
import com.soict.CodeArena.model.User;
import com.soict.CodeArena.model.UserProfile;
import com.soict.CodeArena.repository.ProblemRepository;
import com.soict.CodeArena.repository.UserProblemStatRepository;
import com.soict.CodeArena.repository.UserProfileRepository;
import com.soict.CodeArena.repository.UserRepository;
import com.soict.CodeArena.request.LoginRequest;
import com.soict.CodeArena.request.ManageAdminRequest;
import com.soict.CodeArena.request.RegisterRequest;
import com.soict.CodeArena.request.UserProfileRequest;
import com.soict.CodeArena.response.AdminResponse;
import com.soict.CodeArena.response.PagedResponse;
import com.soict.CodeArena.response.UserManagerResponse;
import com.soict.CodeArena.response.UserProfileResponse;
import com.soict.CodeArena.service.UserService;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProblemRepository problemRepository;
    private final UserProblemStatRepository userProblemStatRepository;

    public UserServiceImpl(
            UserRepository userRepository,
            UserProfileRepository userProfileRepository,
            PasswordEncoder passwordEncoder,
            ProblemRepository problemRepository,
            UserProblemStatRepository userProblemStatRepository
            ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userProfileRepository = userProfileRepository;
        this.problemRepository = problemRepository;
        this.userProblemStatRepository = userProblemStatRepository;
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
    public User loginUser(LoginRequest loginRequest) throws ResponseStatusException {
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
    public UserManagerResponse manageAdminRole(ManageAdminRequest req) throws ResponseStatusException {
        User user = userRepository.findByUsername(req.getUsername());
        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "user not found");
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
    public UserManagerResponse deleteUserById(Long uid) throws ResponseStatusException {
        User user = userRepository.findUserByUserId(uid);
        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "user not found");
        }
        userProblemStatRepository.deleteByUser_UserId(uid);
        problemRepository.deleteByCreatedBy_UserId(uid);
        userRepository.delete(user);
        return new UserManagerResponse(user.getUsername(), user.getRole());
    }

    @Override
    public User findByUsername(String username) throws ResponseStatusException {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserProfileResponse GetUserProfile(String username) throws ResponseStatusException {
        User user = userRepository.findByUsername(username);
        UserProfile userProfile = userProfileRepository
                .findByUser_UserId(user.getUserId())
                .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "user not found"));

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
    public UserProfileResponse updateProfile(UserProfileRequest req, String username) throws ResponseStatusException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "user not found");
        }

        UserProfile profile = userProfileRepository
                .findByUser_UserId(user.getUserId())
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

    @Override
    public PagedResponse<AdminResponse> getAllClass(Integer page, Integer pageSize, Integer offset) throws ResponseStatusException {
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
        Page<User> userPage = userRepository.findAllByRole(USER_ROLE.ADMIN, pageable);

        List<AdminResponse> responses = userPage.getContent().stream()
                .map(user -> {
                    AdminResponse response = new AdminResponse();
                    response.setUserId(user.getUserId());
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
}
