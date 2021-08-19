package io.nikolamicic21.redditclone.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Table(name = "verification_token")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class VerificationToken extends BaseEntity {

    @Column(name = "token", nullable = false, unique = true)
    @NotBlank(message = "Token is required")
    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "expiry_date", nullable = false)
    private Instant expiryDate;

}
