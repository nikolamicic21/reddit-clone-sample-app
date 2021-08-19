package io.nikolamicic21.redditclone.repository;

import io.nikolamicic21.redditclone.model.Post;
import io.nikolamicic21.redditclone.model.User;
import io.nikolamicic21.redditclone.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findTopByPostAndUserOrderByIdDesc(Post post, User user);

}
