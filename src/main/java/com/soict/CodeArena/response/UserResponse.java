package com.soict.CodeArena.response;

import com.soict.CodeArena.model.USER_ROLE;
import lombok.Data;

@Data
public class UserResponse {
    private String username;
    private String jwtToken;
    private USER_ROLE role;
    private String message;
}
