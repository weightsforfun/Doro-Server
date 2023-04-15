package com.example.DoroServer.domain.userLecture.service;

import com.example.DoroServer.domain.lecture.entity.Lecture;
import com.example.DoroServer.domain.lecture.repository.LectureRepository;
import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.domain.user.repository.UserRepository;
import com.example.DoroServer.domain.userLecture.dto.CreateTutorReq;
import com.example.DoroServer.domain.userLecture.dto.FindAllTutorsRes;
import com.example.DoroServer.domain.userLecture.dto.FindMyLecturesRes;
import com.example.DoroServer.domain.userLecture.dto.SelectTutorReq;
import com.example.DoroServer.domain.userLecture.entity.TutorStatus;
import com.example.DoroServer.domain.userLecture.entity.UserLecture;
import com.example.DoroServer.domain.userLecture.repository.UserLectureRepository;
import com.example.DoroServer.global.exception.BaseException;
import com.example.DoroServer.global.exception.Code;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserLectureService {

    private final UserLectureRepository userLectureRepository;
    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;

    public List<FindAllTutorsRes> findAllTutors(Long id) {
        List<UserLecture> allTutors = userLectureRepository.findAllTutors(id);
        List<FindAllTutorsRes> allTutorsResList = allTutors.stream()
                .map(FindAllTutorsRes::fromEntity)
                .collect(Collectors.toList());
        return allTutorsResList;
    }

    public List<FindMyLecturesRes> findMyLectures(Long id) {
        List<UserLecture> myLectures = userLectureRepository.findMyLectures(id);
        List<FindMyLecturesRes> findMyLecturesResList = myLectures.stream()
                .map(FindMyLecturesRes::fromEntity)
                .collect(Collectors.toList());
        return findMyLecturesResList;
    }

    public Long createTutor(Long id, CreateTutorReq createTutorReq) {
        Lecture lecture = lectureRepository.findById(id).
                orElseThrow(() -> new BaseException(Code.LECTURE_NOT_FOUND));
        User user = userRepository.findById(createTutorReq.getUserId())
                .orElseThrow(() -> new BaseException(Code.USER_NOT_FOUND));
        UserLecture userLecture = UserLecture.builder()
                .tutorRole(createTutorReq.getTutorRole())
                .user(user)
                .lecture(lecture)
                .tutorStatus(TutorStatus.WAITING)
                .build();
        UserLecture savedUserLecture = userLectureRepository.save(userLecture);
        return savedUserLecture.getId();


    }

    public String selectTutor(Long lectureId, SelectTutorReq selectTutorReq) {
        UserLecture userLecture = userLectureRepository.findUerLecture(lectureId,
                        selectTutorReq.getUserId(), selectTutorReq.getTutorRole())
                .orElseThrow(() -> new BaseException(Code.TUTOR_NOT_FOUND));

        userLecture.changeTutorStatus();
        return String.valueOf(userLecture.getTutorStatus());
    }
}
