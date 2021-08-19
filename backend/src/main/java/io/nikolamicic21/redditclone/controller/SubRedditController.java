package io.nikolamicic21.redditclone.controller;

import io.nikolamicic21.redditclone.dto.SubRedditRequest;
import io.nikolamicic21.redditclone.dto.SubRedditResponse;
import io.nikolamicic21.redditclone.service.SubRedditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/api/subreddits")
@RequiredArgsConstructor
public class SubRedditController {

    private final SubRedditService subRedditService;

    @PostMapping
    public ResponseEntity<SubRedditResponse> create(@RequestBody SubRedditRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.subRedditService.save(request));
    }

    @GetMapping
    public ResponseEntity<Collection<SubRedditResponse>> getAll() {
        return ResponseEntity.ok().body(this.subRedditService.getAll());
    }

    @GetMapping("/{name}")
    public ResponseEntity<SubRedditResponse> getByName(@PathVariable String name) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.subRedditService.getByName(name));
    }

}
