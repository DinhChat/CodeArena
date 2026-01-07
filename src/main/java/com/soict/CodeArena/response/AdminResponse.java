package com.soict.CodeArena.response;

import com.soict.CodeArena.model.USER_ROLE;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminResponse {
    private Long userId;
    private String username;
    private USER_ROLE role;
}
