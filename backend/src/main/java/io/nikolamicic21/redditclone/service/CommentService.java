package io.nikolamicic21.redditclone.service;

import io.nikolamicic21.redditclone.dto.CommentRequest;
import io.nikolamicic21.redditclone.dto.CommentResponse;
import io.nikolamicic21.redditclone.exception.PostNotFoundException;
import io.nikolamicic21.redditclone.mapper.CommentMapper;
import io.nikolamicic21.redditclone.model.Comment;
import io.nikolamicic21.redditclone.model.NotificationMail;
import io.nikolamicic21.redditclone.model.Post;
import io.nikolamicic21.redditclone.model.User;
import io.nikolamicic21.redditclone.repository.CommentRepository;
import io.nikolamicic21.redditclone.repository.PostRepository;
import io.nikolamicic21.redditclone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentMapper mapper;
    private final MailContentService mailContentService;
    private final MailOutboundService mailOutboundService;

    public CommentResponse save(@NonNull CommentRequest request) {
        final var post = this.postRepository.findByName(request.getPostName())
                .orElseThrow(() -> new PostNotFoundException(request.getPostName()));
        final var comment = this.mapper.requestToComment(request, post, this.authService.getCurrentUser());
        final var savedComment = this.commentRepository.save(comment);

        sendNewCommentNotificationEmail(post, comment);

        return this.mapper.commentToResponse(savedComment);
    }

    @Transactional(readOnly = true)
    public Collection<CommentResponse> getAllByPostName(String postName) {
        final var post = this.postRepository.findByName(postName)
                .orElseThrow(() -> new PostNotFoundException(postName));

        return this.commentRepository.findAllByPost(post)
                .stream()
                .map(this.mapper::commentToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Collection<CommentResponse> getAllByUserName(String userName) {
        final var user = this.userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));

        return this.commentRepository.findAllByUser(user)
                .stream()
                .map(this.mapper::commentToResponse)
                .collect(Collectors.toList());
    }

    private void sendNewCommentNotificationEmail(Post post, Comment comment) {
        final var message = comment.getUser().getUsername() + " posted a comment on your post: " + post.getName();
        final var notificationMail = NotificationMail.builder()
        .subject("New comment on your post " + post.getName())
                .recipient(post.getUser().getEmail())
                .body(message)
                .build();

        this.mailOutboundService.send(notificationMail);
    }
}
