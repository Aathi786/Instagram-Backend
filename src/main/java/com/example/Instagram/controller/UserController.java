package com.example.Instagram.controller;

import com.example.Instagram.model.*;
import com.example.Instagram.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Create User
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // Get All Users
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Get User By ID
    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    // Get User By Username
    @GetMapping("/username/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    // Get Complete Profile
    @GetMapping("/profile/{userId}")
    public ProfileResponse getProfile(@PathVariable String userId) {
        return userService.getProfile(userId);
    }

    // Update User
    @PutMapping("/{id}")
    public User updateUser(
            @PathVariable String id,
            @RequestBody User user) {

        return userService.updateUser(id, user);
    }

    // Delete User
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable String id) {

        userService.deleteUser(id);

        return "User deleted successfully";
    }
    @GetMapping("/suggestions/{userId}")
    public List<User> getSuggestions(@PathVariable String userId) {
        return userService.getSuggestions(userId);
    }
    @PutMapping("/profile/{userId}")
    public ProfileResponse updateProfile(
            @PathVariable String userId,
            @RequestBody ProfileUpdateRequest request) {

        return userService.updateProfile(userId, request);
    }
    @GetMapping("/search")
    public List<User> searchUsers(@RequestParam String username) {
        return userService.searchUsers(username);
    }
    @PutMapping("/online/{userId}")
    public User setOnline(
            @PathVariable String userId) {

        return userService.setOnline(userId);
    }
    @PutMapping("/offline/{userId}")
    public User setOffline(
            @PathVariable String userId) {

        return userService.setOffline(userId);
    }
    @PutMapping("/status/{userId}")
    public User updateOnlineStatus(
            @PathVariable String userId,
            @RequestParam boolean online) {

        return userService.updateOnlineStatus(
                userId,
                online
        );
    }
    @PostMapping("/register")
    public User register(
            @RequestBody RegisterRequest request) {

        return userService.registerUser(request);
    }


    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request) {

        return userService.loginUser(request);
    }

}