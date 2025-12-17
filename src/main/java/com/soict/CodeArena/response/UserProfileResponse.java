package com.soict.CodeArena.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
    private String fullName;
    private String avatarUrl;
    private String bio;
    private String email;
    private String phone;
    private String github;
    private String facebook;
    private LocalDateTime birthday;
}
