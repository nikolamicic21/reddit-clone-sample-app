package io.nikolamicic21.redditclone.service;

import io.nikolamicic21.redditclone.dto.VoteRequest;
import io.nikolamicic21.redditclone.dto.VoteResponse;
import io.nikolamicic21.redditclone.exception.PostNotFoundException;
import io.nikolamicic21.redditclone.exception.RedditCloneException;
import io.nikolamicic21.redditclone.model.Post;
import io.nikolamicic21.redditclone.model.User;
import io.nikolamicic21.redditclone.model.Vote;
import io.nikolamicic21.redditclone.repository.PostRepository;
import io.nikolamicic21.redditclone.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

import static io.nikolamicic21.redditclone.model.VoteType.DOWN_VOTE;
import static io.nikolamicic21.redditclone.model.VoteType.UP_VOTE;

@Service
@RequiredArgsConstructor
@Transactional
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    public VoteResponse save(VoteRequest request) {
        final var post = this.postRepository.findByName(request.getPostName())
                .orElseThrow(() -> new PostNotFoundException(request.getPostName()));
        final var user = this.authService.getCurrentUser();
        final Optional<Vote> voteByPostAndUser = this.voteRepository.findTopByPostAndUserOrderByIdDesc(
                post,
                user
        );
        if (voteByPostAndUser.isPresent() && voteByPostAndUser.get().getType().equals(request.getType())) {
            throw new RedditCloneException("You have already " + request.getType() + "'d this post");
        }
        if (UP_VOTE.equals(request.getType())) {
            post.setVoteCount(post.getVoteCount() + UP_VOTE.getDirection());
        } else {
            post.setVoteCount(post.getVoteCount() + DOWN_VOTE.getDirection());
        }

        final var savedPost = this.postRepository.save(post);
        final var savedVote = this.voteRepository.save(requestToVote(request, savedPost, user));

        return voteToResponse(savedVote);
    }

    private VoteResponse voteToResponse(Vote savedVote) {
        return VoteResponse.builder()
                .postName(savedVote.getPost().getName())
                .type(savedVote.getType())
                .build();
    }

    private Vote requestToVote(VoteRequest request, Post post, User user) {
        return Vote.builder()
                .post(post)
                .user(user)
                .type(request.getType())
                .createdDate(Instant.now())
                .build();
    }

}
