package io.nikolamicic21.redditclone.dto;

import io.nikolamicic21.redditclone.model.VoteType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteRequest {

    private VoteType type;
    private String postName;

}
