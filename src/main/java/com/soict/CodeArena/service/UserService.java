package com.soict.CodeArena.service;

import com.soict.CodeArena.model.USER_ROLE;
import com.soict.CodeArena.model.User;
import com.soict.CodeArena.request.ManageAdminRequest;
import com.soict.CodeArena.request.RegisterRequest;
import com.soict.CodeArena.response.UserManagerResponse;

import java.util.List;

public interface UserService {
    User registerUser(RegisterRequest registerRequest) throws Exception;
    List<UserManagerResponse> findAllUsers() throws Exception;
    List<UserManagerResponse> findAllUsersByRole(USER_ROLE role) throws Exception;
    UserManagerResponse manageAdminRole(ManageAdminRequest req) throws Exception;
    UserManagerResponse deleteUserById(Long uid) throws Exception;
}
