package com.example.DoroServer.domain.user.service;

import com.example.DoroServer.domain.user.dto.UpdateUserReq;
import com.example.DoroServer.domain.user.entity.User;

public interface UserService {

    void updateUser(String id, UpdateUserReq updateUserReq);
}
