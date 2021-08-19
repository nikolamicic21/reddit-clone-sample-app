package io.nikolamicic21.redditclone.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "post")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter @Setter
public class Post extends BaseEntity {

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Post name cannot be empty or null")
    private String name;

    @Column(name = "url")
    @Nullable
    private String url;

    @Lob
    @Column(name = "description")
    @Nullable
    private String description;

    @Column(name = "vote_count")
    private Long voteCount;

    @ManyToOne
    @JoinColumn(name = "sub_reddit_id", nullable = false, referencedColumnName = "id")
    private SubReddit subReddit;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private User user;

}
