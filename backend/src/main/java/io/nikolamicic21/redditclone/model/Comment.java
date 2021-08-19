package io.nikolamicic21.redditclone.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Table(name = "comment")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Comment extends BaseEntity {

    @Column(name = "text", nullable = false)
    @NotEmpty(message = "Text cannot be empty")
    private String text;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false, referencedColumnName = "id")
    private Post post;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private User user;

}
