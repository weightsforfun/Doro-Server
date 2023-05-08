package com.example.DoroServer.domain.user.service;

import static com.example.DoroServer.global.common.Constants.REDIS_MESSAGE_PREFIX.UPDATE;
import static com.example.DoroServer.global.common.Constants.VERIFIED_CODE;

import com.example.DoroServer.domain.user.dto.FindAllUsersRes;
import com.example.DoroServer.domain.user.dto.FindUserRes;
import com.example.DoroServer.domain.user.dto.UpdateUserReq;
import com.example.DoroServer.domain.user.entity.Degree;
import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.domain.user.repository.UserRepository;
import com.example.DoroServer.global.common.Constants.REDIS_MESSAGE_PREFIX;
import com.example.DoroServer.global.exception.BaseException;
import com.example.DoroServer.global.exception.Code;
import com.example.DoroServer.global.jwt.RedisService;
import java.util.List;
import java.util.stream.Collectors;
import com.example.DoroServer.global.s3.AwsS3Service;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final RedisService redisService;
    private final AwsS3Service awsS3Service;

    @Override
    public void updateUser(String id, UpdateUserReq updateUserReq) {
        User user = userRepository.findById(Long.valueOf(id)).orElseThrow(()
        -> new BaseException(Code.USER_NOT_FOUND));

        Degree updateDegree = Degree.builder()
            .school(updateUserReq.getSchool())
            .studentId(updateUserReq.getStudentId())
            .studentStatus(updateUserReq.getStudentStatus())
            .major(updateUserReq.getMajor())
            .build();

        user.updateDegree(updateDegree);
        user.updateGeneration(updateUserReq.getGeneration());

        if(!user.getPhone().equals(updateUserReq.getPhone())){
            if(!VERIFIED_CODE.equals(redisService.getValues(UPDATE + updateUserReq.getPhone()))) {
                throw new BaseException(Code.UNAUTHORIZED_PHONE_NUMBER);
            }
            user.updatePhone(updateUserReq.getPhone());
        }
    }

    @Override
    public List<FindAllUsersRes> findAllUsers() {
        List<User> userList = userRepository.findAll();
        List<FindAllUsersRes> findAllUsersResList = userList.stream().map(FindAllUsersRes::fromEntity)
                .collect(Collectors.toList());
        return findAllUsersResList;
    }

    @Override
    public FindUserRes findUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new BaseException(Code.USER_NOT_FOUND));
        return FindUserRes.fromEntity(user);
    }

    @Override
    public void updateUserProfile(User user, MultipartFile multipartFile) throws IOException {
            if(user.getProfileImg() != null){
                awsS3Service.deleteImage(user.getProfileImg());
            }
            String imgUrl = awsS3Service.upload(multipartFile,"profile");
            userRepository.updateProfileImgById(user.getId(), imgUrl);
    }
}
