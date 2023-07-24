package com.example.DoroServer.domain.userLecture.service;

import com.example.DoroServer.domain.lecture.entity.Lecture;
import com.example.DoroServer.domain.lecture.entity.LectureDate;
import com.example.DoroServer.domain.lecture.entity.LectureStatus;
import com.example.DoroServer.domain.lecture.repository.LectureRepository;
import com.example.DoroServer.domain.notification.service.NotificationService;
import com.example.DoroServer.domain.user.entity.Degree;
import com.example.DoroServer.domain.user.entity.Gender;
import com.example.DoroServer.domain.user.entity.StudentStatus;
import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.domain.user.repository.UserRepository;
import com.example.DoroServer.domain.userLecture.dto.*;
import com.example.DoroServer.domain.userLecture.entity.TutorRole;
import com.example.DoroServer.domain.userLecture.entity.TutorStatus;
import com.example.DoroServer.domain.userLecture.entity.UserLecture;
import com.example.DoroServer.domain.userLecture.repository.UserLectureRepository;
import com.example.DoroServer.global.exception.BaseException;
import com.example.DoroServer.global.exception.Code;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;


import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserLectureServiceTest {
    @InjectMocks
    private UserLectureService userLectureService;

    @Mock
    private UserLectureRepository userLectureRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LectureRepository lectureRepository;

    @Spy
    private UserLectureMapper userLectureMapper = Mappers.getMapper(UserLectureMapper.class);

    @Mock
    private NotificationService notificationService;

    private User setUpUser(Long userId){
        return User.builder()
                .id(userId)
                .name("name")
                .account("account")
                .birth(LocalDate.now())
                .gender(Gender.FEMALE)
                .phone("01012341234")
                .degree(Degree.builder()
                        .major("major")
                        .school("school")
                        .studentStatus(StudentStatus.ABSENCE)
                        .studentId("123")
                        .build())
                .generation(1)
                .build();
    }

    private Lecture setUpLecture(Long lectureId){
        ArrayList<LocalDate> dates = new ArrayList<>();
        LocalDate now = LocalDate.now();
        dates.add(now);
        return Lecture.builder()
                .id(lectureId)
                .mainTitle("mainTitle")
                .subTitle("subTitle")
                .institution("institution")
                .city("city")
                .place("place")
                .studentGrade("studentGrade")
                .studentNumber("studentNumber")
                .mainTutor("mainTutor")
                .subTutor("subTutor")
                .staff("staff")
                .mainPayment("mainPayment")
                .subPayment("subPayment")
                .staffPayment("staffPayment")
                .transportCost("transPortCost")
                .time("time")
                .lectureDates(dates)
                .lectureDate(LectureDate.builder()
                        .enrollEndDate(now)
                        .enrollStartDate(now)
                        .build())
                .status(LectureStatus.RECRUITING)
                .build();
    }

    private UserLecture setUpUserLecture(Long userLectureId){
        Long userId=3L;
        User user = setUpUser(userId);

        Long lectureId=2L;
        Lecture lecture = setUpLecture(lectureId);

        return UserLecture.builder()
                .id(userLectureId)
                .user(user)
                .lecture(lecture)
                .tutorStatus(TutorStatus.WAITING)
                .tutorRole(TutorRole.SUB_TUTOR)
                .build();
    }

    private List<UserLecture> setUpUserLectureList(int length){
        ArrayList<UserLecture> userLectureArrayList = new ArrayList<>();
        for(int i=0;i<length;i++){
            UserLecture userLecture = setUpUserLecture((long) i);
            userLectureArrayList.add(userLecture);
        }
        return userLectureArrayList;
    }

    private FindAllTutorsRes setUpFindAllTutorsRes(){
        return FindAllTutorsRes.builder()
                .build();
    }

    private FindMyLecturesRes setUpFindMyLectures(){
        return FindMyLecturesRes.builder().build();
    }

    @DisplayName("강의 신청 예외 - 중복 방지")
    @Test
    void createTutorDuplicateTutorExceptionTest() {
        // given
        Long userLectureId=5L;
        UserLecture userLecture = setUpUserLecture(userLectureId);
        given(userLectureRepository.findUserLecture(any(Long.class),any(Long.class),any(TutorRole.class))).willReturn(Optional.of(userLecture));

        Long lectureId=1L;
        CreateTutorReq createTutorReq = CreateTutorReq.builder()
                .userId(1L)
                .tutorRole(TutorRole.SUB_TUTOR)
                .build();
        // when

        // then
        BaseException baseException = assertThrows(BaseException.class, () -> {
            userLectureService.createTutor(lectureId, createTutorReq);
        });
        assertThat(baseException.getCode()).isEqualTo(Code.ALREADY_EXIST);
    }

    @DisplayName("강의 신청 예외 - 강의가 없습니다.")
    @Test
    void createTutorNoLectureExceptionTest() {
        // given
        given(userLectureRepository.findUserLecture(any(Long.class),any(Long.class),any(TutorRole.class))).willReturn(Optional.empty());
        given(lectureRepository.findById(any(Long.class))).willReturn(Optional.empty());

        Long lectureId=1L;
        CreateTutorReq createTutorReq = CreateTutorReq.builder()
                .userId(1L)
                .tutorRole(TutorRole.SUB_TUTOR)
                .build();
        // when

        // then
        BaseException baseException = assertThrows(BaseException.class, () -> {
            userLectureService.createTutor(lectureId, createTutorReq);
        });
        assertThat(baseException.getCode()).isEqualTo(Code.LECTURE_NOT_FOUND);
    }

    @DisplayName("강의 신청 예외 - 유저가 없습니다.")
    @Test
    void createTutorNoUserExceptionTest() {
        // given
        given(userLectureRepository.findUserLecture(any(Long.class),any(Long.class),any(TutorRole.class))).willReturn(Optional.empty());
        given(userRepository.findById(any(Long.class))).willReturn(Optional.empty());
        given(lectureRepository.findById(any(Long.class))).willReturn(Optional.of(Lecture.builder().build()));

        Long lectureId=1L;
        CreateTutorReq createTutorReq = CreateTutorReq.builder()
                .userId(1L)
                .tutorRole(TutorRole.SUB_TUTOR)
                .build();
        // when

        // then
        BaseException baseException = assertThrows(BaseException.class, () -> {
            userLectureService.createTutor(lectureId, createTutorReq);
        });
        assertThat(baseException.getCode()).isEqualTo(Code.USER_NOT_FOUND);
    }

    @DisplayName("강의 신청 테스트")
    @Test
    void createTutorTest() {
        // given
        given(userLectureRepository.findUserLecture(any(Long.class),any(Long.class),any(TutorRole.class))).willReturn(Optional.empty());

        Long userLectureId=4L;
        UserLecture userLecture = setUpUserLecture(userLectureId);

        given(userRepository.findById(any(Long.class))).willReturn(Optional.of(userLecture.getUser()));

        given(lectureRepository.findById(any(Long.class))).willReturn(Optional.of(userLecture.getLecture()));

        given(userLectureRepository.save(any(UserLecture.class))).willReturn(userLecture);

        Long lectureId=1L;
        CreateTutorReq createTutorReq = CreateTutorReq.builder()
                .userId(1L)
                .tutorRole(TutorRole.SUB_TUTOR)
                .build();
        // when
        userLectureService.createTutor(lectureId,createTutorReq);
        // then
        verify(lectureRepository,times(1)).findById(any(Long.class));
        verify(userRepository,times(1)).findById(any(Long.class));
        verify(userLectureRepository,times(1)).save(any(UserLecture.class));

    }

    @DisplayName("모든 신청 강사 조회 Mapper 테스트")
    @Test
    void findAllTutorsMapperTest() throws IllegalAccessException {
        // given
        Long userLectureId = 5L;
        UserLecture userLecture = setUpUserLecture(userLectureId);
        // when
        FindAllTutorsRes findAllTutorsRes = userLectureMapper.toFindAllTutorsRes(userLecture, userLecture.getUser());
        // then
        for (Field field : findAllTutorsRes.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(findAllTutorsRes);
            assertThat(value).isNotNull();
        }
    }

    @DisplayName("모든 신청 강사 조회 테스트")
    @Test
    void findAllTutorsTest() {
        // given
        int userLectureCount=3;
        List<UserLecture> userLectures = setUpUserLectureList(userLectureCount);
        given(userLectureRepository.findAllTutors(any(Long.class))).willReturn(userLectures);

        FindAllTutorsRes findAllTutorsRes = setUpFindAllTutorsRes();
        given(userLectureMapper.toFindAllTutorsRes(any(UserLecture.class),any(User.class))).willReturn(findAllTutorsRes);
        // when
        Long lectureId=3L;
        List<FindAllTutorsRes> allTutors = userLectureService.findAllTutors(lectureId);
        // then
        verify(userLectureRepository,times(1)).findAllTutors(any(Long.class));
        assertThat(allTutors.size()).isEqualTo(userLectureCount);
    }

    @DisplayName("모든 내 강의 신청 내역 조회 Mapper 테스트")
    @Test
    void findMyLecturesMapperTest() throws IllegalAccessException {
        // given
        Long userLectureId=4L;
        UserLecture userLecture = setUpUserLecture(userLectureId);

        // when
        FindMyLecturesRes findMyLecturesRes = userLectureMapper.toFindMyLecturesRes(userLecture.getLecture(), userLecture);
        // then
        for (Field field : findMyLecturesRes.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(findMyLecturesRes);
            assertThat(value).isNotNull();
        }

    }

    @DisplayName("모든 내 강의 신청 내역 조회")
    @Test
    void findMyLectureTest() {
        // given
        int userLectureCount = 5;
        List<UserLecture> userLectures = setUpUserLectureList(userLectureCount);
        given(userLectureRepository.findMyLectures(any(Long.class))).willReturn(userLectures);

        FindMyLecturesRes findMyLecturesRes = setUpFindMyLectures();
        given(userLectureMapper.toFindMyLecturesRes(any(Lecture.class),any(UserLecture.class))).willReturn(findMyLecturesRes);

        // when
        Long userId=4L;
        List<FindMyLecturesRes> myLectures = userLectureService.findMyLectures(userId);

        // then
        verify(userLectureRepository,times(1)).findMyLectures(any(Long.class));
        assertThat(myLectures.size()).isEqualTo(userLectureCount);

    }

    @DisplayName("강사 선정 예외 - 신청내역이 없습니다.")
    @Test
    void selectTutorNoTutorExceptionTest() {
        // given
        given(userLectureRepository.findUserLecture(any(Long.class),any(Long.class),any(TutorRole.class))).willReturn(Optional.empty());


        Long lectureId = 1L;
        SelectTutorReq selectTutorReq = SelectTutorReq.builder()
                .userId(1L)
                .tutorRole(TutorRole.MAIN_TUTOR)
                .build();
        // when

        // then
        BaseException baseException = assertThrows(BaseException.class, () -> {
            userLectureService.selectTutor(lectureId, selectTutorReq);
        });
        assertThat(baseException.getCode()).isEqualTo(Code.TUTOR_NOT_FOUND);
    }

    @DisplayName("강사 선정 예외 - 유저가 존재하지 않습니다.")
    @Test
    void selectTutorNoUserExceptionTest() {
        // given
        Long userLectureId=4L;
        UserLecture userLecture = setUpUserLecture(userLectureId);
        given(userLectureRepository.findUserLecture(any(Long.class),any(Long.class),any(TutorRole.class))).willReturn(Optional.of(userLecture));

        given(userRepository.findById(any(Long.class))).willReturn(Optional.empty());
        Long lectureId = 1L;
        SelectTutorReq selectTutorReq = SelectTutorReq.builder()
                .userId(1L)
                .tutorRole(TutorRole.MAIN_TUTOR)
                .build();
        // when

        // then
        BaseException baseException = assertThrows(BaseException.class, () -> {
            userLectureService.selectTutor(lectureId, selectTutorReq);
        });
        assertThat(baseException.getCode()).isEqualTo(Code.USER_NOT_FOUND);
    }

    @DisplayName("강사 선정 예외 - 강의가 존재하지 않습니다.")
    @Test
    void selectTutorNoLectureExceptionTest() {
        // given
        Long userLectureId=4L;
        UserLecture userLecture = setUpUserLecture(userLectureId);
        given(userLectureRepository.findUserLecture(any(Long.class),any(Long.class),any(TutorRole.class))).willReturn(Optional.of(userLecture));

        given(userRepository.findById(any(Long.class))).willReturn(Optional.of(userLecture.getUser()));
        given(lectureRepository.findById(any(Long.class))).willReturn(Optional.empty());

        Long lectureId = 1L;
        SelectTutorReq selectTutorReq = SelectTutorReq.builder()
                .userId(1L)
                .tutorRole(TutorRole.MAIN_TUTOR)
                .build();
        // when

        // then
        BaseException baseException = assertThrows(BaseException.class, () -> {
            userLectureService.selectTutor(lectureId, selectTutorReq);
        });
        assertThat(baseException.getCode()).isEqualTo(Code.LECTURE_NOT_FOUND);

    }

    @DisplayName("강사 선정 테스트")
    @Test
    void selectTutorTest() {
        // given
        Long userLectureId=4L;
        UserLecture userLecture = setUpUserLecture(userLectureId);
        given(userLectureRepository.findUserLecture(any(Long.class),any(Long.class),any(TutorRole.class))).willReturn(Optional.of(userLecture));

        Long userId=3L;
        User user = setUpUser(userId);
        given(userRepository.findById(any(Long.class))).willReturn(Optional.of(user));

        Long lectureId=4L;
        Lecture lecture = setUpLecture(lectureId);
        given(lectureRepository.findById(any(Long.class))).willReturn(Optional.of(lecture));


        SelectTutorReq selectTutorReq = SelectTutorReq.builder()
                .userId(userId)
                .tutorRole(TutorRole.MAIN_TUTOR)
                .build();

        // when
        userLectureService.selectTutor(lectureId,selectTutorReq);
        // then
        assertThat(userLecture.getTutorStatus()).isEqualTo(TutorStatus.ASSIGNED);
    }

    @DisplayName("강의 신청 취소 테스트")
    @Test
    void deleteLectureTest() {
        // given
        doNothing().when(userLectureRepository).deleteById(any(Long.class));
        // when
        Long userLectureId=1L;
        userLectureService.deleteLecture(userLectureId);
        // then
        verify(userLectureRepository,times(1)).deleteById(any(Long.class));

    }
}