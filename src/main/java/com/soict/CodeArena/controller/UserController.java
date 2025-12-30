package com.soict.CodeArena.controller;

import com.soict.CodeArena.model.USER_ROLE;
import com.soict.CodeArena.request.ManageAdminRequest;
import com.soict.CodeArena.request.UserProfileRequest;
import com.soict.CodeArena.response.UserManagerResponse;
import com.soict.CodeArena.response.UserProfileResponse;
import com.soict.CodeArena.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<UserManagerResponse>> getAllUser() throws Exception {
        return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserProfileResponse> getUser(@PathVariable String username) throws Exception {
        return new ResponseEntity<>(userService.GetUserProfile(username), HttpStatus.OK);
    }

    @PostMapping("/profile")
    public ResponseEntity<UserProfileResponse> updateUserProfile(UserProfileRequest req, Authentication authentication) throws Exception{
        String username = authentication.getName();
        return new ResponseEntity<>(userService.updateProfile(req, username), HttpStatus.OK);
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserManagerResponse>> getAllUsersByRole(@PathVariable USER_ROLE role) throws Exception {
        return new ResponseEntity<>(userService.findAllUsersByRole(role), HttpStatus.OK);
    }

    @PutMapping("/manage")
    public ResponseEntity<UserManagerResponse> manageUser(
            @RequestBody ManageAdminRequest req
    ) throws Exception {
        return new ResponseEntity<>(userService.manageAdminRole(req), HttpStatus.OK);
    }

    @DeleteMapping("/{uid}")
    public ResponseEntity<UserManagerResponse> deleteUser(
            @PathVariable Long uid
    ) throws Exception {
        return new ResponseEntity<>(userService.deleteUserById(uid), HttpStatus.OK);
    }

}
