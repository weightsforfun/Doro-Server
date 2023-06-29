package com.example.DoroServer.domain.user.service;

import static com.example.DoroServer.global.common.Constants.REDIS_MESSAGE_PREFIX.UPDATE;
import static com.example.DoroServer.global.common.Constants.VERIFIED_CODE;

import com.example.DoroServer.domain.token.service.TokenService;
import com.example.DoroServer.domain.user.dto.FindAllUsersRes;
import com.example.DoroServer.domain.user.dto.FindUserRes;
import com.example.DoroServer.domain.user.dto.UpdateUserReq;
import com.example.DoroServer.domain.user.dto.UserMapper;
import com.example.DoroServer.domain.user.entity.Degree;
import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.domain.user.entity.UserRole;
import com.example.DoroServer.domain.user.repository.UserRepository;
import com.example.DoroServer.global.exception.BaseException;
import com.example.DoroServer.global.exception.Code;
import com.example.DoroServer.global.jwt.RedisService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.example.DoroServer.global.s3.AwsS3Service;
import java.io.IOException;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final RedisService redisService;
    private final AwsS3Service awsS3Service;
    private final UserMapper userMapper;

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
        List<FindAllUsersRes> findAllUsersResList = userList.stream()
                .map(user -> userMapper.toFindAllUsersRes(user))
                .collect(Collectors.toList());
        return findAllUsersResList;
    }

    @Override
    public FindUserRes findUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new BaseException(Code.USER_NOT_FOUND));
        return userMapper.toFindUserRes(user);
    }

    @Override
    public String updateGeneration(Long id, int generation) {
        User user = userRepository.findById(id)
            .orElseThrow(()-> new BaseException(Code.USER_NOT_FOUND));
        user.updateGeneration(generation);
        return user.getName();
    }

    @Override
    public void updateInactiveUser() {
        List<User> targetUsers =
            userRepository.findBylastModifiedAtBeforeAndStatusEquals(
                LocalDateTime.now().minusYears(1),
                true);
        if (targetUsers.size() > 0){
           targetUsers.forEach(User::toInactive);
            log.info(LocalDate.now() + " - "+ targetUsers.size() + "명이 휴먼계정으로 전환되었습니다.");
        }else {
            log.info(LocalDate.now() + " - 휴먼 계정 처리 스케줄러");
        }
    }

    @Override
    public void updateUserProfile(User user, MultipartFile multipartFile) throws IOException {
            if(user.getProfileImg() != null){
                awsS3Service.deleteImage(user.getProfileImg());
            }
            String imgUrl = awsS3Service.upload(multipartFile,"profile");
            userRepository.updateProfileImgById(user.getId(), imgUrl);
    }

    public Long updateNotificationAgreement(Long id, Map<String, Boolean> notificationAgreement) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new BaseException(Code.USER_NOT_FOUND));
        Boolean agreement = notificationAgreement.getOrDefault("agreement",false);
        user.updateNotificationAgreement(agreement);
        return id;
    }

}
