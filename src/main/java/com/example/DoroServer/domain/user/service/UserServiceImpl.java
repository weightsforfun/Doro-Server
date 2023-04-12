package com.example.DoroServer.domain.user.service;

import com.example.DoroServer.domain.user.dto.UpdateUserReq;
import com.example.DoroServer.domain.user.entity.Degree;
import com.example.DoroServer.domain.user.entity.Degree.DegreeBuilder;
import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.domain.user.repository.UserRepository;
import com.example.DoroServer.global.exception.BaseException;
import com.example.DoroServer.global.exception.Code;
import com.example.DoroServer.global.jwt.RedisService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final RedisService redisService;

    @Override
    public void updateUser(String id, UpdateUserReq updateUserReq) {
        User user = userRepository.findById(Long.valueOf(id)).orElseThrow(()
        -> new BaseException(Code.BAD_REQUEST));

        Degree updateDegree = Degree.builder()
            .school(updateUserReq.getSchool())
            .studentId(updateUserReq.getStudentId())
            .studentStatus(updateUserReq.getStudentStatus())
            .major(updateUserReq.getMajor())
            .build();

        user.updateDegree(updateDegree);
        user.updateGeneration(updateUserReq.getGeneration());

        if(!user.getPhone().equals(updateUserReq.getPhone())){
            if(!"Verified".equals(redisService.getValues("UPDATE" + updateUserReq.getPhone()))) {
                throw new BaseException(Code.UNAUTHORIZED_PHONE_NUMBER);
            }
            user.updatePhone(updateUserReq.getPhone());
        }
    }
}
