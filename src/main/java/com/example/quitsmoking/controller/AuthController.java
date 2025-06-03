// src/main/java/com/example/quitsmoking/controller/AuthController.java
package com.example.quitsmoking.controller;

import com.example.quitsmoking.model.User;
import com.example.quitsmoking.payload.request.LoginRequest;
import com.example.quitsmoking.payload.request.RegisterRequest;
import com.example.quitsmoking.payload.response.JwtResponse;
import com.example.quitsmoking.payload.response.MessageResponse;
import com.example.quitsmoking.repository.UserRepository;
import com.example.quitsmoking.security.jwt.JwtUtils;
import com.example.quitsmoking.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

// Không cần import java.util.UUID ở đây nếu chỉ dùng cho id của userDetails
// vì userDetails.getId() đã trả về UUID rồi.
// Tuy nhiên, giữ lại cũng không sao nếu bạn dùng UUID ở chỗ khác.

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // userDetails.getId() trả về UUID (theo UserDetailsImpl.java)
        // JwtResponse constructor mong muốn UUID (theo JwtResponse.java)
        // KHÔNG CẦN BẤT KỲ CHUYỂN ĐỔI NÀO Ở ĐÂY!
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(), // <-- DÒNG NÀY ĐÚNG RỒI! BỎ MỌI THAY ĐỔI TRƯỚC ĐÓ Ở ĐÂY
                userDetails.getUsername(),
                userDetails.getEmail()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        // Constructor của User KHÔNG NHẬN ID vì nó được @GeneratedValue
        User user = new User(registerRequest.getUsername(),
                registerRequest.getEmail(),
                encoder.encode(registerRequest.getPassword()),
                1); // Gán roleID mặc định là 1 (ví dụ: cho vai trò "USER")

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}