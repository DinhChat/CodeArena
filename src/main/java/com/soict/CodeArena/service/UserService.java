package com.soict.CodeArena.service;

import com.soict.CodeArena.model.USER_ROLE;
import com.soict.CodeArena.model.User;
import com.soict.CodeArena.model.UserProfile;
import com.soict.CodeArena.request.LoginRequest;
import com.soict.CodeArena.request.ManageAdminRequest;
import com.soict.CodeArena.request.RegisterRequest;
import com.soict.CodeArena.request.UserProfileRequest;
import com.soict.CodeArena.response.PagedResponse;
import com.soict.CodeArena.response.UserManagerResponse;
import com.soict.CodeArena.response.UserProfileResponse;

import java.util.List;

public interface UserService {
    User registerUser(RegisterRequest registerRequest) throws Exception;

    User loginUser(LoginRequest loginRequest) throws Exception;

    PagedResponse<UserManagerResponse> findAllUsers(Integer page, Integer pageSize, Integer offset) throws Exception;

    PagedResponse<UserManagerResponse> findAllUsersByRole(USER_ROLE role, Integer page, Integer pageSize,
            Integer offset) throws Exception;

    UserManagerResponse manageAdminRole(ManageAdminRequest req) throws Exception;

    UserManagerResponse deleteUserById(Long uid) throws Exception;

    User findByUsername(String username) throws Exception;

    UserProfileResponse GetUserProfile(String username) throws Exception;

    UserProfileResponse updateProfile(UserProfileRequest req, String username) throws Exception;
}
