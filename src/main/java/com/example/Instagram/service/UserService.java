package com.example.Instagram.service;

import com.example.Instagram.model.*;
import com.example.Instagram.repository.FollowRepository;
import com.example.Instagram.repository.PostRepository;
import com.example.Instagram.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FollowRepository followRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PostRepository postRepository,
            FollowRepository followRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.followRepository = followRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public User updateUser(String id, User user) {
        user.setId(id);
        return userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Get complete user profile
    public ProfileResponse getProfile(String userId) {

        User user = getUserById(userId);

        ProfileResponse profile = new ProfileResponse();

        profile.setId(user.getId());
        profile.setUsername(user.getUsername());
        profile.setProfilePic(user.getProfilePic());
        profile.setBio(user.getBio());

        // Followers count
        profile.setFollowersCount(
                followRepository.countByFollowingId(userId)
        );

        // Following count
        profile.setFollowingCount(
                followRepository.countByFollowerId(userId)
        );

        // Posts count
        profile.setPostsCount(
                postRepository.countByUserId(userId)
        );

        return profile;
    }
    public List<User> getSuggestions(String userId) {

        List<User> users = userRepository.findAll();

        return users.stream()
                .filter(user -> !user.getId().equals(userId))
                .toList();
    }
    public ProfileResponse updateProfile(String userId, ProfileUpdateRequest request) {

        User user = getUserById(userId);

        user.setUsername(request.getUsername());
        user.setProfilePic(request.getProfilePic());
        user.setBio(request.getBio());

        userRepository.save(user);

        return getProfile(userId);
    }
    public List<User> searchUsers(String username) {
        return userRepository.findByUsernameContainingIgnoreCase(username);
    }
    public User setOnline(String userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setOnline(true);
        user.setLastSeen(LocalDateTime.now());

        return userRepository.save(user);
    }
    public User setOffline(String userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setOnline(false);
        user.setLastSeen(LocalDateTime.now());

        return userRepository.save(user);
    }
    public User updateOnlineStatus(String userId, boolean online) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setOnline(online);
        user.setLastSeen(java.time.LocalDateTime.now());

        return userRepository.save(user);
    }
    public User registerUser(RegisterRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword( passwordEncoder.encode(request.getPassword())
        );

        user.setProfilePic(
                "https://i.pravatar.cc/150?img=12"
        );

        user.setBio("");

        user.setOnline(false);

        return userRepository.save(user);
    }


    public LoginResponse loginUser(LoginRequest request) {

        User user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid username or password"
                        )
                );

        boolean passwordValid;

        try {
            passwordValid = passwordEncoder.matches(
                    request.getPassword(),
                    user.getPassword()
            );
        } catch (Exception e) {
            passwordValid = false;
        }

        // Existing users may still have plain-text passwords.
        // If the plain-text password matches, convert it to BCrypt.
        if (!passwordValid &&
                user.getPassword().equals(request.getPassword())) {

            user.setPassword(
                    passwordEncoder.encode(request.getPassword())
            );

            userRepository.save(user);

            passwordValid = true;
        }

        if (!passwordValid) {
            throw new RuntimeException(
                    "Invalid username or password"
            );
        }

        String token = jwtService.generateToken(
                user.getId(),
                user.getUsername()
        );

        return new LoginResponse(
                token,
                user.getId(),
                user.getUsername()
        );
    }


}