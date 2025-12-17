package com.soict.CodeArena.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {

    @Id
    private Long userId; // shared PK

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private String fullName;
    private String avatarUrl;
    private String bio;
    private String email;
    private String phone;
    private String github;
    private String facebook;
    private LocalDateTime birthday;
}
