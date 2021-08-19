package io.nikolamicic21.redditclone.controller;

import io.nikolamicic21.redditclone.dto.*;
import io.nikolamicic21.redditclone.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/sign-up")
    public ResponseEntity<SignUpResponse> signUp(@RequestBody RegisterRequest registerRequest) {
        this.authService.signUp(registerRequest);
        final var response = SignUpResponse.builder()
                .message("User Registration successful!")
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/account-verification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        this.authService.verifyAccount(token);

        return ResponseEntity.ok("Account Activated successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        final var response = this.authService.login(loginRequest);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<LoginResponse> exchangeRefreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok().body(this.authService.exchangeRefreshToken(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity
                .ok()
                .body(this.authService.logout(request));
    }

}
