package com.soict.CodeArena.request;

import com.soict.CodeArena.model.MANAGER_ACTION;
import com.soict.CodeArena.model.USER_ROLE;
import lombok.Data;

@Data
public class ManageAdminRequest {
    private String username;
    private USER_ROLE role;
    private MANAGER_ACTION action;
}
