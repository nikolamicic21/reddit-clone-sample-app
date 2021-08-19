package io.nikolamicic21.redditclone.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import io.nikolamicic21.redditclone.dto.PostRequest;
import io.nikolamicic21.redditclone.dto.PostResponse;
import io.nikolamicic21.redditclone.model.Post;
import io.nikolamicic21.redditclone.model.SubReddit;
import io.nikolamicic21.redditclone.model.User;
import io.nikolamicic21.redditclone.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Builder;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface PostMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "name", source = "postRequest.name")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "voteCount", constant = "0L")
    @Mapping(target = "subReddit", source = "subReddit")
    @Mapping(target = "user", source = "user")
    Post requestToPost(PostRequest postRequest, SubReddit subReddit, User user);

    @Mapping(target = "subRedditName", source = "post.subReddit.name")
    @Mapping(target = "userName", source = "post.user.username")
    @Mapping(target = "commentCount", source = "post")
    @Mapping(target = "duration", source = "post")
    PostResponse postToResponse(Post post, @Context CommentRepository commentRepository);

    default Integer getCommentCount(Post post, @Context CommentRepository commentRepository) {
        return commentRepository.findAllByPost(post).size();
    }

    default String getDuration(Post post) {
        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }
}
