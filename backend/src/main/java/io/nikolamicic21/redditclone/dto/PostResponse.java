package io.nikolamicic21.redditclone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {

    private String name;
    private String description;
    private String url;
    private String subRedditName;
    private String userName;
    private Integer voteCount;
    private Integer commentCount;
    private String duration;

}
