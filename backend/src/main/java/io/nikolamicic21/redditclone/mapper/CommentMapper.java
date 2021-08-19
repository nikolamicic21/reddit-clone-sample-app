package io.nikolamicic21.redditclone.mapper;

import io.nikolamicic21.redditclone.dto.CommentRequest;
import io.nikolamicic21.redditclone.dto.CommentResponse;
import io.nikolamicic21.redditclone.model.Comment;
import io.nikolamicic21.redditclone.model.Post;
import io.nikolamicic21.redditclone.model.User;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "text", source = "request.text")
    @Mapping(target = "post", source = "post")
    @Mapping(target = "user", source = "user")
    Comment requestToComment(CommentRequest request, Post post, User user);

    CommentResponse commentToResponse(Comment comment);

}
