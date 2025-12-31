package com.soict.CodeArena.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class UserProfileRequest {
    private String fullName;
    private String avatarUrl;
    private String bio;
    private String email;
    private String phone;
    private String github;
    private String facebook;
    private LocalDateTime birthday;
}
