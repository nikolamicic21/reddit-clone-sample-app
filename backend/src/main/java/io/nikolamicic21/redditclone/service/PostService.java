package io.nikolamicic21.redditclone.service;

import io.nikolamicic21.redditclone.dto.PostRequest;
import io.nikolamicic21.redditclone.dto.PostResponse;
import io.nikolamicic21.redditclone.exception.PostNotFoundException;
import io.nikolamicic21.redditclone.exception.SubRedditNotFoundException;
import io.nikolamicic21.redditclone.mapper.PostMapper;
import io.nikolamicic21.redditclone.repository.CommentRepository;
import io.nikolamicic21.redditclone.repository.PostRepository;
import io.nikolamicic21.redditclone.repository.SubRedditRepository;
import io.nikolamicic21.redditclone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final SubRedditRepository subRedditRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentRepository commentRepository;
    private final PostMapper mapper;

    public PostResponse save(@NonNull PostRequest request) {
        final var subReddit = this.subRedditRepository.findByName(request.getSubRedditName()).orElseThrow(() ->
                new SubRedditNotFoundException(request.getSubRedditName()));
        final var user = this.authService.getCurrentUser();
        final var post = this.mapper.requestToPost(request, subReddit, user);
        final var savedPost = this.postRepository.save(post);

        return this.mapper.postToResponse(savedPost, this.commentRepository);
    }

    @Transactional(readOnly = true)
    public PostResponse getByName(String name) {
        final var post = this.postRepository.findByName(name)
                .orElseThrow(() -> new PostNotFoundException(name));

        return this.mapper.postToResponse(post, this.commentRepository);
    }

    @Transactional(readOnly = true)
    public Collection<PostResponse> getAll() {
        return this.postRepository.findAll()
                .stream()
                .map((post) -> this.mapper.postToResponse(post, this.commentRepository))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Transactional(readOnly = true)
    public Collection<PostResponse> getAllBySubRedditName(String subRedditName) {
        final var subReddit = this.subRedditRepository.findByName(subRedditName)
                .orElseThrow(() -> new SubRedditNotFoundException(subRedditName));

        return this.postRepository.findAllBySubReddit(subReddit)
                .stream()
                .map((post) -> this.mapper.postToResponse(post, this.commentRepository))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Collection<PostResponse> getAllByUserName(String userName) {
        final var user = this.userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));

        return this.postRepository.findAllByUser(user)
                .stream()
                .map((post) -> this.mapper.postToResponse(post, this.commentRepository))
                .collect(Collectors.toList());
    }
}
