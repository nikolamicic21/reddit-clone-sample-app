package io.nikolamicic21.redditclone.controller;

import io.nikolamicic21.redditclone.dto.CommentRequest;
import io.nikolamicic21.redditclone.dto.CommentResponse;
import io.nikolamicic21.redditclone.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/posts/{postName}")
    public ResponseEntity<Collection<CommentResponse>> getAllByPostName(@PathVariable String postName) {
        return ResponseEntity.ok(this.commentService.getAllByPostName(postName));
    }

    @GetMapping("/users/{userName}")
    public ResponseEntity<Collection<CommentResponse>> getAllByUserName(@PathVariable String userName) {
        return ResponseEntity.ok(this.commentService.getAllByUserName(userName));
    }

    @PostMapping
    public ResponseEntity<CommentResponse> create(@RequestBody CommentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.commentService.save(request));
    }

}
