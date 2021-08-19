package io.nikolamicic21.redditclone.repository;

import io.nikolamicic21.redditclone.dto.CommentResponse;
import io.nikolamicic21.redditclone.model.Comment;
import io.nikolamicic21.redditclone.model.Post;
import io.nikolamicic21.redditclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Collection<Comment> findAllByPost(Post post);
    Collection<Comment> findAllByUser(User user);

}
