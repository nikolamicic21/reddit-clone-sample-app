package io.nikolamicic21.redditclone.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Table(name = "user")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class User extends BaseEntity {

    @Column(name = "username", nullable = false)
    @NotBlank(message = "Username is required")
    private String username;

    @Column(name = "password", nullable = false)
    @NotBlank(message = "Password is required")
    private String password;

    @Column(name = "email", nullable = false)
    @NotBlank(message = "Email is required")
    @Email
    private String email;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

}
