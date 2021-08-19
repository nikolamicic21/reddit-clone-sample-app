package io.nikolamicic21.redditclone.service;

import io.nikolamicic21.redditclone.dto.*;
import io.nikolamicic21.redditclone.exception.RedditCloneException;
import io.nikolamicic21.redditclone.model.NotificationMail;
import io.nikolamicic21.redditclone.model.User;
import io.nikolamicic21.redditclone.model.VerificationToken;
import io.nikolamicic21.redditclone.repository.UserRepository;
import io.nikolamicic21.redditclone.repository.VerificationTokenRepository;
import io.nikolamicic21.redditclone.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailOutboundService mailOutboundService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    public void signUp(RegisterRequest registerRequest) {
        var user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(this.passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreatedDate(Instant.now());
        user.setEnabled(Boolean.FALSE);

        this.userRepository.save(user);

        var token = generateVerificationToken(user);
        final var notificationMail = NotificationMail.builder()
                .subject("Please Activate Your Account!")
                .recipient(user.getEmail())
                .body(
                        "Thank you for signing up to Reddit Clone, " +
                        "please click on the link below to activate your account: " +
                        "http://localhost:8080/api/auth/account-verification/" + token
                )
                .build();
        this.mailOutboundService.send(notificationMail);
    }

    public void verifyAccount(@NonNull String token) {
        var verificationToken = this.verificationTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new RedditCloneException("Invalid Verification Token"));
        getUserAndEnable(verificationToken);
    }

    public LoginResponse login(LoginRequest loginRequest) {
        var usernameToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );
        final var authentication = this.authenticationManager.authenticate(usernameToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final var token = this.jwtProvider.generateToken(authentication);
        final var refreshToken = this.refreshTokenService.generate();

        return LoginResponse.builder()
                .username(loginRequest.getUsername())
                .token(token)
                .refreshToken(refreshToken.getToken())
                .expiresAt(Instant.now().plusMillis(this.jwtProvider.getJwtExpirationTimeInMillis()))
                .build();
    }

    public LoginResponse exchangeRefreshToken(RefreshTokenRequest request) {
        this.refreshTokenService.validate(request.getRefreshToken());
        final var token = this.jwtProvider.generateTokenWithUsername(request.getUsername());

        return LoginResponse.builder()
                .username(request.getUsername())
                .token(token)
                .refreshToken(request.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(this.jwtProvider.getJwtExpirationTimeInMillis()))
                .build();
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        var authenticationToken = (UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext()
                .getAuthentication();
        var user = (org.springframework.security.core.userdetails.User)authenticationToken.getPrincipal();
        var username = user.getUsername();
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));
    }

    public LogoutResponse logout(RefreshTokenRequest request) {
        this.refreshTokenService.delete(request.getRefreshToken());
        return LogoutResponse.builder()
                .message("Logout for the user " + request.getUsername() + " was successful")
                .build();
    }

    private String generateVerificationToken(User user) {
        var token = UUID.randomUUID().toString();
        var verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setCreatedDate(Instant.now());
        verificationToken.setExpiryDate(Instant.now().plus(Duration.ofHours(1)));

        this.verificationTokenRepository.save(verificationToken);

        return token;
    }

    private void getUserAndEnable(VerificationToken verificationToken) {
        final var user = verificationToken.getUser();
        user.setEnabled(Boolean.TRUE);
        this.userRepository.save(user);
    }
}
