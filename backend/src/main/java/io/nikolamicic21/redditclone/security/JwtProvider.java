package io.nikolamicic21.redditclone.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.nikolamicic21.redditclone.exception.RedditCloneException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtProvider {

    private KeyStore keyStore;

    @Value("${jwt.expiration.time-in-millis}")
    @Getter
    private Long jwtExpirationTimeInMillis;

    @PostConstruct
    public void init() {
        try {
            this.keyStore = KeyStore.getInstance("JKS");
            final var jksKeyStoreStream = new ClassPathResource("keystore").getInputStream();
            this.keyStore.load(jksKeyStoreStream, "secret".toCharArray());
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
            throw new RedditCloneException("Exception occurred while loading the KeyStore");
        }
    }

    public String generateToken(Authentication authentication) {
        final var user = (User)authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(Date.from(Instant.now().plusMillis(this.jwtExpirationTimeInMillis)))
                .signWith(getPrivateKey())
                .compact();
    }

    public String generateTokenWithUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(Date.from(Instant.now().plusMillis(this.jwtExpirationTimeInMillis)))
                .signWith(getPrivateKey())
                .compact();
    }

    public boolean validateToken(String jwtToken) {
        var jwtParser = Jwts.parserBuilder()
                .setSigningKey(getPublicKey())
                .build();
        jwtParser.parseClaimsJws(jwtToken);

        return true;
    }

    public String getUsernameFromJwt(String jwtToken) {
        var jwtParser = Jwts.parserBuilder()
                .setSigningKey(getPublicKey())
                .build();
        var claims = jwtParser.parseClaimsJws(jwtToken);

        return claims.getBody().getSubject();
    }

    private Key getPrivateKey() {
        try {
            return this.keyStore.getKey("redditclone", "secret".toCharArray());
        } catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {
            throw new RedditCloneException("Exception occurred while retrieving private key from KeyStore");
        }
    }

    private Key getPublicKey() {
        try {
            return this.keyStore.getCertificate("redditclone").getPublicKey();
        } catch (KeyStoreException e) {
            throw new RedditCloneException("Exception occurred while retrieving public key from KeyStore");
        }
    }
}
