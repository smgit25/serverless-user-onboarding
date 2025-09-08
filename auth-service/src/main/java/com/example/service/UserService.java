package com.example.authservice.service;

import com.example.authservice.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private final Map<String, User> usersById = new HashMap<>();
    private final Map<String, User> usersByUsername = new HashMap<>();
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService() {
        // example seeded user
        var u = new User(UUID.randomUUID().toString(), "admin", encoder.encode("admin123"), "ROLE_ADMIN", "admin@example.com");
        usersById.put(u.getId(), u);
        usersByUsername.put(u.getUsername(), u);
    }

    public User register(String username, String password, String email) {
        if (usersByUsername.containsKey(username)) throw new RuntimeException("User exists");
        String id = UUID.randomUUID().toString();
        String hash = encoder.encode(password);
        User u = new User(id, username, hash, "ROLE_USER", email);
        usersById.put(id, u);
        usersByUsername.put(username, u);
        return u;
    }

    public Optional<User> authenticate(String username, String rawPassword) {
        User u = usersByUsername.get(username);
        if (u == null) return Optional.empty();
        if (encoder.matches(rawPassword, u.getPasswordHash())) return Optional.of(u);
        return Optional.empty();
    }

    public Optional<User> findById(String id) { return Optional.ofNullable(usersById.get(id)); }
}
