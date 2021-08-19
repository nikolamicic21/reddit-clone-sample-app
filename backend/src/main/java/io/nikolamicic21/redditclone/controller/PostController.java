package io.nikolamicic21.redditclone.controller;

import io.nikolamicic21.redditclone.dto.PostRequest;
import io.nikolamicic21.redditclone.dto.PostResponse;
import io.nikolamicic21.redditclone.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<Collection<PostResponse>> getAll() {
        return ResponseEntity.ok(this.postService.getAll());
    }

    @GetMapping("/{name}")
    public ResponseEntity<PostResponse> getByName(@PathVariable String name) {
        return ResponseEntity.ok(this.postService.getByName(name));
    }

    @GetMapping("/subreddits/{subRedditName}")
    public ResponseEntity<Collection<PostResponse>> getAllBySubRedditName(@PathVariable String subRedditName) {
        return ResponseEntity.ok(this.postService.getAllBySubRedditName(subRedditName));
    }

    @GetMapping("/users/{userName}")
    public ResponseEntity<Collection<PostResponse>> getAllByUserName(@PathVariable String userName) {
        return ResponseEntity.ok(this.postService.getAllByUserName(userName));
    }

    @PostMapping
    public ResponseEntity<PostResponse> create(@RequestBody PostRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.postService.save(request));
    }
}
