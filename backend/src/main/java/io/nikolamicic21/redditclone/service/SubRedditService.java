package io.nikolamicic21.redditclone.service;

import io.nikolamicic21.redditclone.dto.SubRedditRequest;
import io.nikolamicic21.redditclone.dto.SubRedditResponse;
import io.nikolamicic21.redditclone.exception.RedditCloneException;
import io.nikolamicic21.redditclone.mapper.SubRedditMapper;
import io.nikolamicic21.redditclone.model.SubReddit;
import io.nikolamicic21.redditclone.repository.SubRedditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubRedditService {

    private final SubRedditRepository subRedditRepository;
    private final AuthService authService;
    private final SubRedditMapper mapper;

    @Transactional
    public SubRedditResponse save(SubRedditRequest request) {
        final var subReddit = mapToSubReddit(request);
        final var savedSubReddit = this.subRedditRepository.save(subReddit);
        final var posts = savedSubReddit.getPosts();

        return SubRedditResponse.builder()
                .name(savedSubReddit.getName())
                .postsCount(posts != null ? posts.size() : 0)
                .build();
    }

    @Transactional(readOnly = true)
    public Collection<SubRedditResponse> getAll() {
        return this.subRedditRepository.findAll()
                .stream()
                .map(this.mapper::subRedditToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SubRedditResponse getByName(@NonNull String name) {
        final var subReddit =  this.subRedditRepository.findByName(name)
                .orElseThrow(() -> new RedditCloneException("Subreddit with name " + name + " not found"));

        return this.mapper.subRedditToResponse(subReddit);
    }

    private SubReddit mapToSubReddit(SubRedditRequest request) {
        var user = this.authService.getCurrentUser();

        return this.mapper.requestToSubReddit(request, List.of(), user);
    }
}
