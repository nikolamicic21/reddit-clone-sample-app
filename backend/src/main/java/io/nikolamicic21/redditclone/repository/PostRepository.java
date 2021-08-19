package io.nikolamicic21.redditclone.repository;

import io.nikolamicic21.redditclone.model.Post;
import io.nikolamicic21.redditclone.model.SubReddit;
import io.nikolamicic21.redditclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByName(String name);
    Collection<Post> findAllBySubReddit(SubReddit subReddit);
    Collection<Post> findAllByUser(User user);

}
