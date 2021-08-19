package io.nikolamicic21.redditclone.model;

import lombok.Getter;

@Getter
public enum VoteType {

    UP_VOTE(1), DOWN_VOTE(-1);

    private final int direction;

    VoteType(int direction) {
        this.direction = direction;
    }
}
