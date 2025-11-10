package com.soict.CodeArena.controller;

import com.soict.CodeArena.model.USER_ROLE;
import com.soict.CodeArena.request.ManageAdminRequest;
import com.soict.CodeArena.response.UserManagerResponse;
import com.soict.CodeArena.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/manager")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserManagerResponse>> getAllUser() throws Exception {
        return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{role}")
    public ResponseEntity<List<UserManagerResponse>> getAllUsersByRole(@PathVariable("role") USER_ROLE role) throws Exception {
        return new ResponseEntity<>(userService.findAllUsersByRole(role), HttpStatus.OK);
    }

    @PutMapping("/admin")
    public ResponseEntity<UserManagerResponse> manageUser(
            @RequestBody ManageAdminRequest req
    ) throws Exception {
        return new ResponseEntity<>(userService.manageAdminRole(req), HttpStatus.OK);
    }

    @DeleteMapping("/user/{uid}")
    public ResponseEntity<UserManagerResponse> deleteUser(
            @PathVariable("uid") Long uid
    ) throws Exception {
        return new ResponseEntity<>(userService.deleteUserById(uid), HttpStatus.OK);
    }

}
