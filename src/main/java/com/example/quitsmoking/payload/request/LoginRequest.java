package com.example.quitsmoking.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data; // <--- Đảm bảo có dòng này

@Data // <--- Đảm bảo có annotation này
public class LoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}