package io.nikolamicic21.redditclone.controller;

import io.nikolamicic21.redditclone.dto.VoteRequest;
import io.nikolamicic21.redditclone.dto.VoteResponse;
import io.nikolamicic21.redditclone.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/votes")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<VoteResponse> create(@RequestBody VoteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.voteService.save(request));
    }

}
