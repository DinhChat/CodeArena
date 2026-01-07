package com.soict.CodeArena.service;

import com.soict.CodeArena.model.USER_ROLE;
import com.soict.CodeArena.model.User;
import com.soict.CodeArena.model.UserProfile;
import com.soict.CodeArena.request.LoginRequest;
import com.soict.CodeArena.request.ManageAdminRequest;
import com.soict.CodeArena.request.RegisterRequest;
import com.soict.CodeArena.request.UserProfileRequest;
import com.soict.CodeArena.response.AdminResponse;
import com.soict.CodeArena.response.PagedResponse;
import com.soict.CodeArena.response.UserManagerResponse;
import com.soict.CodeArena.response.UserProfileResponse;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface UserService {
    User registerUser(RegisterRequest registerRequest) throws Exception;

    User loginUser(LoginRequest loginRequest) throws ResponseStatusException;

    PagedResponse<UserManagerResponse> findAllUsers(Integer page, Integer pageSize, Integer offset) throws ResponseStatusException;

    PagedResponse<UserManagerResponse> findAllUsersByRole(USER_ROLE role, Integer page, Integer pageSize,
            Integer offset) throws ResponseStatusException;

    UserManagerResponse manageAdminRole(ManageAdminRequest req) throws ResponseStatusException;

    UserManagerResponse deleteUserById(Long uid) throws ResponseStatusException;

    User findByUsername(String username) throws ResponseStatusException;

    UserProfileResponse GetUserProfile(String username) throws ResponseStatusException;

    UserProfileResponse updateProfile(UserProfileRequest req, String username) throws ResponseStatusException;

    PagedResponse<AdminResponse> getAllClass(Integer page, Integer pageSize, Integer offset) throws ResponseStatusException;
}
