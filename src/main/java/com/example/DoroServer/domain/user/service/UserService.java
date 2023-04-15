package com.example.DoroServer.domain.user.service;

import com.example.DoroServer.domain.user.dto.UpdateUserReq;
import com.example.DoroServer.domain.user.entity.User;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    void updateUser(String id, UpdateUserReq updateUserReq);

    void updateUserProfile(User user, MultipartFile multipartFile) throws IOException;
}
