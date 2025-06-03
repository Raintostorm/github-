package com.example.quitsmoking.controller;

import com.example.quitsmoking.model.User;
import com.example.quitsmoking.payload.request.RegisterRequest;
import com.example.quitsmoking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.UUID; // <--- Đảm bảo có import này

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    // Get all users
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) { // <--- Đảm bảo là UUID
        Optional<User> user = userRepository.findById(id); // <--- Đảm bảo findById dùng UUID
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update user
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @Valid @RequestBody RegisterRequest userDetails) { // <--- Đảm bảo là UUID
        Optional<User> userOptional = userRepository.findById(id); // <--- Đảm bảo findById dùng UUID

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Cần kiểm tra null cho các trường trước khi set nếu registerRequest có thể null
            if (userDetails.getUsername() != null) { // <--- Sử dụng getter từ RegisterRequest
                user.setUsername(userDetails.getUsername());
            }
            if (userDetails.getEmail() != null) { // <--- Sử dụng getter từ RegisterRequest
                user.setEmail(userDetails.getEmail());
            }
            if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) { // <--- Sử dụng getter từ RegisterRequest
                user.setPassword(encoder.encode(userDetails.getPassword()));
            }
            // Cập nhật các trường khác nếu có

            User updatedUser = userRepository.save(user);
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) { // <--- Đảm bảo là UUID
        if (userRepository.existsById(id)) { // <--- Đảm bảo existsById dùng UUID
            userRepository.deleteById(id); // <--- Đảm bảo deleteById dùng UUID
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}