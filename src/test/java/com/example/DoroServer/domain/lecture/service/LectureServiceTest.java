package com.example.DoroServer.domain.lecture.service;

import com.example.DoroServer.domain.lecture.dto.*;
import com.example.DoroServer.domain.lecture.entity.Lecture;
import com.example.DoroServer.domain.lecture.entity.LectureDate;
import com.example.DoroServer.domain.lecture.entity.LectureStatus;
import com.example.DoroServer.domain.lecture.repository.LectureRepository;
import com.example.DoroServer.domain.lectureContent.dto.LectureContentDto;
import com.example.DoroServer.domain.lectureContent.dto.LectureContentMapper;
import com.example.DoroServer.domain.lectureContent.dto.UpdateLectureContentReq;
import com.example.DoroServer.domain.lectureContent.entity.LectureContent;
import com.example.DoroServer.domain.lectureContent.repository.LectureContentRepository;
import com.example.DoroServer.domain.lectureContent.service.LectureContentService;
import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.domain.userLecture.dto.FindAllAssignedTutorsRes;
import com.example.DoroServer.domain.userLecture.dto.UserLectureMapper;
import com.example.DoroServer.domain.userLecture.entity.UserLecture;
import com.example.DoroServer.domain.userLecture.repository.UserLectureRepository;
import com.example.DoroServer.global.exception.BaseException;
import com.example.DoroServer.global.exception.Code;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;


import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
class LectureServiceTest {

    @InjectMocks
    private LectureService lectureService;

    @Mock
    private LectureRepository lectureRepository;

    @Mock
    private LectureContentRepository lectureContentRepository;
    @Mock
    private UserLectureRepository userLectureRepository;

    @Mock
    private UserLecture userLecture;

    @Mock
    private ModelMapper modelMapper;

    @Spy
    private LectureMapper lectureMapper = Mappers.getMapper(LectureMapper.class);

    @Mock
    private LectureContentMapper lectureContentMapper;

    @Mock
    private UserLectureMapper userLectureMapper;

    private CreateLectureReq setUpCreateLectureReq(Long lectureContentId) {
        ArrayList<LocalDate> dates = new ArrayList<>();
        LocalDate now = LocalDate.now();
        dates.add(now);
        return CreateLectureReq.builder()
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
                .lectureContentId(lectureContentId)
                .build();
    }

    private LectureContent setUpLectureContent() {
        return LectureContent.builder()
                .content("content")
                .kit("kit")
                .id(1L)
                .remark("remark")
                .requirement("requirement")
                .detail("detail")
                .build();
    }

    private Lecture setUpLecture(Long lectureId) {
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
                .lectureContent(setUpLectureContent())
                .build();
    }

    private User setUpUser(Long userId){
        return User.builder()
                .id(userId)
                .build();
    }

    private List<Lecture> setUpLectures(int length){
        ArrayList<Lecture> lectureArrayList = new ArrayList<>();
        for(int i=0;i<length;i++){
            lectureArrayList.add(setUpLecture(Long.valueOf(i)));
        }
        return lectureArrayList;
    }

    private UpdateLectureReq setUpUpdateLectureReq(){
        ArrayList<LocalDate> localDates = new ArrayList<>();
        localDates.add(LocalDate.now());
        localDates.add(LocalDate.now().minusWeeks(1));
        return UpdateLectureReq.builder()
                .city("city")
                .institution("institution")
                .lectureDates(localDates)
                .build();
    }

    private LectureContentDto setUpLectureContentDTO(){
        return LectureContentDto.builder().build();
    }
    private LectureDto setUpLectureDTO(){
        return LectureDto.builder()
                .build();
    }

    private List<UserLecture> setUpUserLectures(int userLectureCount){
        ArrayList<UserLecture> userLectureArrayList = new ArrayList<>();
        for(int i=0;i<userLectureCount;i++){
            userLectureArrayList.add(UserLecture.builder()
                            .user(User.builder().build())
                            .build());
        }
        return userLectureArrayList;
    }

    private FindAllAssignedTutorsRes setUpFindAllAssignedTutorsRes(Long userId){
        return FindAllAssignedTutorsRes.builder()
                .userId(userId)
                .name("name")
                .build();
    }


    @DisplayName("강의 생성 mapper 테스트")
    @Test
    void createLectureReqMapperTest() throws IllegalAccessException {
        // given
        CreateLectureReq createLectureReq = setUpCreateLectureReq(1L);

        // when
        Lecture lecture = lectureMapper.toLecture(createLectureReq);
        // then
        for (Field field : lecture.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(lecture);
            if (field.getName().equals("id") || field.getName().equals("lectureContent") || field.getName().equals("chat")) {
                continue;
            }

            assertThat(value).isNotNull();
        }

    }

    @DisplayName("강의 생성 예외 테스트 - 강의 자료가 존재하지 않음")
    @Test
    void createLectureLectureContentNotFoundTest() {
        // given
        Long lectureContentId=3L;
        CreateLectureReq createLectureReq = setUpCreateLectureReq(lectureContentId);

        Long lectureId=4L;
        Lecture lecture = setUpLecture(lectureId);
        given(lectureMapper.toLecture(any(CreateLectureReq.class))).willReturn(lecture);

        given(lectureContentRepository.findById(any(Long.class))).willReturn(Optional.empty());
        // when

        // then
        BaseException baseException = assertThrows(BaseException.class, () -> {
            lectureService.createLecture(createLectureReq);
        });
        assertThat(baseException.getCode()).isEqualTo(Code.LECTURE_CONTENT_NOT_FOUND);

    }

    @DisplayName("강의 생성 테스트")
    @Test
    void createLectureTest() {
        // given
        Long lectureContentId=4L;
        CreateLectureReq createLectureReq = setUpCreateLectureReq(lectureContentId);

        Long lectureId=2L;
        Lecture lecture = setUpLecture(lectureId);
        LectureContent lectureContent = setUpLectureContent();
        given(lectureMapper.toLecture(any(CreateLectureReq.class))).willReturn(lecture);
        given(lectureContentRepository.findById(any(Long.class))).willReturn(Optional.of(lectureContent));
        given(lectureRepository.save(any(Lecture.class))).willReturn(lecture);

        // when
        lectureService.createLecture(createLectureReq);
        // then
        verify(lectureMapper, times(1)).toLecture(createLectureReq);
        verify(lectureContentRepository, times(1)).findById(createLectureReq.getLectureContentId());
        verify(lectureRepository, times(1)).save(lecture);
        assertThat(lecture.getLectureContent()).isNotNull();

    }

    @DisplayName("모든 강의 조회 mapper 테스트")
    @Test
    void findAllLecturesResMapperTest() throws IllegalAccessException {
        // given
        Long lectureId=3L;
        Lecture lecture = setUpLecture(lectureId);
        // when

        FindAllLecturesRes findAllLecturesRes = lectureMapper.toFindAllLecturesRes(lecture, lecture.getLectureDate());

        // then
        for (Field field : findAllLecturesRes.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(findAllLecturesRes);
            assertThat(value).isNotNull();
        }

    }

    @DisplayName("모든 강의 조회 테스트")
    @Test
    void findAllLectureTest() {
        // given
        int lectureLength=5;
        List<Lecture> lectureList = setUpLectures(lectureLength);
        Page<Lecture> lecturesPage = new PageImpl<>(lectureList);
        FindAllLecturesRes findAllLecturesRes = FindAllLecturesRes.builder()
                .place("place")
                .build();

        given(lectureRepository.findAllLecturesWithFilter(any(FindAllLecturesCond.class),any(org.springframework.data.domain.Pageable.class))).willReturn(lecturesPage);
        given(lectureMapper.toFindAllLecturesRes(any(Lecture.class),any(LectureDate.class))).willReturn(findAllLecturesRes);
        FindAllLecturesCond findAllLecturesCond = FindAllLecturesCond.builder()
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .city(new ArrayList<>())
                .build();

        int page=0;
        int size=10;
        PageRequest pageRequest = PageRequest.of(page, size);
        // when
        List<FindAllLecturesRes> allLectures = lectureService.findAllLectures(findAllLecturesCond, pageRequest);
        // then
        assertThat(allLectures.size()).isEqualTo(lectureLength);
        verify(lectureRepository,times(1)).findAllLecturesWithFilter(findAllLecturesCond,pageRequest);
        verify(lectureMapper,times(lectureLength)).toFindAllLecturesRes(any(Lecture.class),any(LectureDate.class));

    }

    @DisplayName("강의 DTO mapper 테스트")
    @Test
    void lectureDTOMapperTest() throws IllegalAccessException {
        // given
        Long lectureId=5L;
        Lecture lecture = setUpLecture(lectureId);
        // when
        LectureDto lectureDto = lectureMapper.toLectureDto(lecture);
        // then
        for (Field field : lectureDto.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(lectureDto);
            assertThat(value).isNotNull();
        }

    }

    @DisplayName("강의 조회 예외 테스트 - 강의가 존재하지 않음")
    @Test
    void findLectureLectureNotFoundTest() {
        // given
        given(lectureRepository.findLectureById(any(Long.class))).willReturn(Optional.empty());

        Long lectureId=3L;
        Long userId=6L;
        User user = setUpUser(userId);

        // when

        // then
        BaseException baseException = assertThrows(BaseException.class, () -> {
            lectureService.findLecture(lectureId, user);
        });
        assertThat(baseException.getCode()).isEqualTo(Code.LECTURE_NOT_FOUND);
    }

    @DisplayName("선정된 강의 조회 테스트 ")
    @Test
    void assignedFindLectureTest() throws IllegalAccessException {
        // given
        Long lectureId=3L;
        Lecture lecture = setUpLecture(lectureId);

        LectureContentDto lectureContentDto = setUpLectureContentDTO();
        LectureDto lectureDto = setUpLectureDTO();

        given(lectureRepository.findLectureById(any(Long.class))).willReturn(Optional.ofNullable(lecture));
        given(lectureMapper.toLectureDto(any(Lecture.class))).willReturn(lectureDto);
        given(lectureContentMapper.toLectureContentDto(any(LectureContent.class))).willReturn(lectureContentDto);

        int userLectureCount=5; //강사로 선정된 유저 ID
        List<UserLecture> userLectures = setUpUserLectures(userLectureCount);
        given(userLectureRepository.findAllAssignedTutors(any(Long.class),any(Long.class))).willReturn(userLectures);

        Long assignedUserId= 5L;//강사로 선정된 유저 ID
        FindAllAssignedTutorsRes findAllAssignedTutorsRes = setUpFindAllAssignedTutorsRes(assignedUserId);
        given(userLectureMapper.toFindAllAssignedTutorsRes(any(UserLecture.class),any(User.class))).willReturn(findAllAssignedTutorsRes);

        Long currentUserId=5L;// 현재 강의를 조회하는 유저 ID (선정된 유저라 강사 정보 보여야함)
        User user = setUpUser(currentUserId);

        // when
        FindLectureRes findLectureRes = lectureService.findLecture(lecture.getId(), user);
        // then
        for (Field field : findLectureRes.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(findLectureRes);
            if(field.getName().equals("assignedTutors")){
                assertThat(value).isNotNull();
            }
            else{
                assertThat(value).isNotNull();
            }
        }

    }

    @DisplayName("선정안된 강의 조회 테스트 ")
    @Test
    void unassignedFindLectureTest() throws IllegalAccessException {
        // given
        Long lectureId=3L;
        Lecture lecture = setUpLecture(lectureId);

        LectureContentDto lectureContentDto = setUpLectureContentDTO();
        LectureDto lectureDto = setUpLectureDTO();

        given(lectureRepository.findLectureById(any(Long.class))).willReturn(Optional.ofNullable(lecture));
        given(lectureMapper.toLectureDto(any(Lecture.class))).willReturn(lectureDto);
        given(lectureContentMapper.toLectureContentDto(any(LectureContent.class))).willReturn(lectureContentDto);

        int userLectureCount=5;
        List<UserLecture> userLectures = setUpUserLectures(userLectureCount);
        given(userLectureRepository.findAllAssignedTutors(any(Long.class),any(Long.class))).willReturn(userLectures);

        Long assignedUserId= 5L; // 강의에 선정된 유저 ID
        FindAllAssignedTutorsRes findAllAssignedTutorsRes = setUpFindAllAssignedTutorsRes(assignedUserId);
        given(userLectureMapper.toFindAllAssignedTutorsRes(any(UserLecture.class),any(User.class))).willReturn(findAllAssignedTutorsRes);

        Long currentUserId=4L; // 현재 강의를 조회하는 유저 ID (선정되지 않아 강사 정보가 안보여야 한다.)
        User user = setUpUser(currentUserId);

        // when
        FindLectureRes findLectureRes = lectureService.findLecture(lecture.getId(), user);
        // then
        for (Field field : findLectureRes.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(findLectureRes);
            assertThat(value).isNotNull();

        }

    }

    @DisplayName("강의 업데이트 예외 테스트")
    @Test
    void updateLectureExceptionTest() {
        // given
        Long lectureId=2L;
        Lecture lecture = setUpLecture(lectureId);

        UpdateLectureReq updateLectureReq = setUpUpdateLectureReq();
        given(lectureRepository.findById(any(Long.class))).willReturn(Optional.empty());
        // when

        // then
        BaseException baseException = assertThrows(BaseException.class, () -> {
            lectureService.updateLecture(lecture.getId(), updateLectureReq);
        });
        assertThat(baseException.getCode()).isEqualTo(Code.LECTURE_NOT_FOUND);
    }

    @DisplayName("강의 업데이트 테스트")
    @Test
    void updateLectureTest() {
        // given
        Long lectureId=2L;
        Lecture lecture = setUpLecture(lectureId);

        UpdateLectureReq updateLectureReq = setUpUpdateLectureReq();
        given(lectureRepository.findById(any(Long.class))).willReturn(Optional.of(lecture));

        doNothing().when(modelMapper).map(any(UpdateLectureReq.class),any(Lecture.class));

        // when
        lectureService.updateLecture(lecture.getId(),updateLectureReq);
        // then
        verify(lectureRepository,times(1)).findById(lecture.getId());
        verify(modelMapper,times(1)).map(updateLectureReq,lecture);
        assertThat(lecture.getLectureDates().size()).isEqualTo(updateLectureReq.getLectureDates().size());

    }

    @DisplayName("강의 삭제 테스트")
    @Test
    void deleteLectureTest() {
        // given
        Long lectureId=1L;

        Lecture lecture = setUpLecture(lectureId);
        doNothing().when(lectureRepository).deleteById(any(Long.class));
        given(lectureRepository.findLectureById(any(Long.class))).willReturn(Optional.of(lecture));
        doNothing().when(userLectureRepository).deleteAllByLecture(any(Lecture.class));
        // when
        lectureService.deleteLecture(lectureId);
        // then
        verify(lectureRepository,times(1)).deleteById(lectureId);
        verify(lectureRepository,times(1)).findLectureById(lectureId);
        verify(userLectureRepository,times(1)).deleteAllByLecture(lecture);
    }

    @DisplayName("끝난 강의 상태 변경 스케줄링 테스트")
    @Test
    void checkLectureFinishedDateTest() {
        // given
        int lectureCount=5;
        List<Lecture> lectureList = setUpLectures(lectureCount);
        given(lectureRepository.findLecturesByFinishedDate(any(LocalDate.class))).willReturn(lectureList);
        // when
        lectureService.checkLectureFinishedDate();
        // then
        verify(lectureRepository,times(1)).findLecturesByFinishedDate(any(LocalDate.class));
        lectureList.stream()
                .forEach(res-> assertThat(res.getStatus()).isEqualTo(LectureStatus.FINISH));


    }
}