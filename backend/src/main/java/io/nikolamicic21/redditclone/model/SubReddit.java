package io.nikolamicic21.redditclone.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sub_reddit")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter @Setter
public class SubReddit extends BaseEntity {

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Community name is required")
    private String name;

    @Column(name = "description", nullable = false)
    @NotBlank(message = "Description is required")
    private String description;

    @OneToMany(mappedBy = "subReddit", orphanRemoval = true)
    private List<Post> posts;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
