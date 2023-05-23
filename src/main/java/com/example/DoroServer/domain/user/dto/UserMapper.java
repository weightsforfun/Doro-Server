package com.example.DoroServer.domain.user.dto;


import com.example.DoroServer.domain.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    FindUserRes toFindUserRes(User user);

    FindAllUsersRes toFindAllUsersRes(User user);
}
