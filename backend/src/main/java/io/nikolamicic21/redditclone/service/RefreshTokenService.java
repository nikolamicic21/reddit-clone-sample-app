package io.nikolamicic21.redditclone.service;

import io.nikolamicic21.redditclone.dto.RefreshTokenResponse;
import io.nikolamicic21.redditclone.exception.RedditCloneException;
import io.nikolamicic21.redditclone.model.RefreshToken;
import io.nikolamicic21.redditclone.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenResponse generate() {
        final var refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .createdDate(Instant.now())
                .build();

        return refreshTokenToResponse(this.refreshTokenRepository.save(refreshToken));
    }

    public void validate(String refreshToken) {
        this.refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RedditCloneException("Invalid Refresh Token"));
    }

    public void delete(String refreshToken) {
        this.refreshTokenRepository.deleteByToken(refreshToken);
    }

    private RefreshTokenResponse refreshTokenToResponse(RefreshToken refreshToken) {
        return RefreshTokenResponse.builder()
                .token(refreshToken.getToken())
                .createdDate(refreshToken.getCreatedDate())
                .build();
    }

}
