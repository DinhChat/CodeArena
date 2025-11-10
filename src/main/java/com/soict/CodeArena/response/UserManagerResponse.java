package com.soict.CodeArena.response;

import com.soict.CodeArena.model.USER_ROLE;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserManagerResponse {
    private String username;
    private USER_ROLE role;
}
