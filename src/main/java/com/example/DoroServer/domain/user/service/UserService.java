package com.example.DoroServer.domain.user.service;

import com.example.DoroServer.domain.user.dto.FindAllUsersRes;
import com.example.DoroServer.domain.user.dto.FindUserRes;
import com.example.DoroServer.domain.user.dto.UpdateUserReq;
import com.example.DoroServer.domain.user.entity.User;
import java.util.List;

public interface UserService {

    void updateUser(String id, UpdateUserReq updateUserReq);

    List<FindAllUsersRes> findAllUsers();

    FindUserRes findUser(Long id);
}
