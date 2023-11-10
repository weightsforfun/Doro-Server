package com.example.DoroServer.domain.post.dto;


import com.example.DoroServer.domain.post.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel="spring")
public interface PostMapper {

    @Mapping(target="isAnswered", constant = "false")
    Post toPost(CreatePostReq createPostReq);

    FindAllPostRes toFindAllPostRes(Post post);

    FindPostRes toFindPostRes(Post post);


}
