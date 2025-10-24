package com.soict.CodeArena.service;

import com.soict.CodeArena.model.User;
import com.soict.CodeArena.request.RegisterRequest;

public interface UserService {
    User findByUsername(String username) throws Exception;
    User registerUser(RegisterRequest registerRequest) throws Exception;
}
