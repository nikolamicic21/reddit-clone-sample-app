package io.nikolamicic21.redditclone.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "refresh_token")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class RefreshToken extends BaseEntity {

    private String token;

}
