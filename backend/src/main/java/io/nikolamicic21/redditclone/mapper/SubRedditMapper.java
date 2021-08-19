package io.nikolamicic21.redditclone.mapper;

import io.nikolamicic21.redditclone.dto.SubRedditRequest;
import io.nikolamicic21.redditclone.dto.SubRedditResponse;
import io.nikolamicic21.redditclone.model.Post;
import io.nikolamicic21.redditclone.model.SubReddit;
import io.nikolamicic21.redditclone.model.User;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface SubRedditMapper {

    @Mapping(target = "postsCount", expression = "java(mapPosts(subReddit.getPosts()))")
    SubRedditResponse subRedditToResponse(SubReddit subReddit);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "name", source = "subRedditRequest.name")
    @Mapping(target = "description", source = "subRedditRequest.description")
    SubReddit requestToSubReddit(SubRedditRequest subRedditRequest, List<Post> posts, User user);

    default Integer mapPosts(Collection<Post> posts) {
        return posts.size();
    }
}
