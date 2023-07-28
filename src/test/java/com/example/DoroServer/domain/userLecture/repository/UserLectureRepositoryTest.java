package com.example.DoroServer.domain.userLecture.repository;

import com.example.DoroServer.domain.lecture.entity.Lecture;
import com.example.DoroServer.domain.lecture.repository.LectureRepository;
import com.example.DoroServer.domain.user.entity.*;
import com.example.DoroServer.domain.user.repository.UserRepository;
import com.example.DoroServer.domain.userLecture.entity.TutorRole;
import com.example.DoroServer.domain.userLecture.entity.TutorStatus;
import com.example.DoroServer.domain.userLecture.entity.UserLecture;
import com.example.DoroServer.global.config.QueryDslConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
@Import({QueryDslConfig.class})
class UserLectureRepositoryTest {
    @Autowired
    UserLectureRepository userLectureRepository;

    @Autowired
    LectureRepository lectureRepository;

    @Autowired
    UserRepository userRepository;

    @PersistenceContext
    EntityManager em;

    private List<User> setUpUserList(int userCount){
        LinkedList<User> userList= new LinkedList<>();
        for(int i=0;i<5;i++){
            User user = User.builder()
                    .account("123"+i)
                    .password("123")
                    .name("123")
                    .birth(LocalDate.now())
                    .gender(Gender.FEMALE)
                    .phone("123")
                    .degree(
                            Degree.builder()
                                    .school("123")
                                    .studentId("123")
                                    .major("123")
                                    .studentStatus(StudentStatus.ABSENCE)
                                    .build()
                    )
                    .generation(1)
                    .role(UserRole.ROLE_USER)
                    .profileImg("123")
                    .notificationAgreement(true)
                    .isActive(true)
                    .build();
            User savedUser = userRepository.save(user);
            userList.add(savedUser);
        }
        return userList;
    }

    @DisplayName("findAllTutors_success_test")
    @Test
    void findAllTutorsSuccessTest() {
        // given
        User user = User.builder()
                .account("123")
                .password("123")
                .name("123")
                .birth(LocalDate.now())
                .gender(Gender.FEMALE)
                .phone("123")
                .degree(
                        Degree.builder()
                                .school("123")
                                .studentId("123")
                                .major("123")
                                .studentStatus(StudentStatus.ABSENCE)
                                .build()
                )
                .generation(1)
                .role(UserRole.ROLE_USER)
                .profileImg("123")
                .notificationAgreement(true)
                .isActive(true)
                .build();
        userRepository.save(user);


        Lecture lecture = Lecture.builder()
                .build();
        Lecture savedLecture = lectureRepository.save(lecture);

        UserLecture userLecture = UserLecture.builder()
                .lecture(savedLecture)
                .user(user)
                .tutorRole(TutorRole.SUB_TUTOR)
                .tutorStatus(TutorStatus.WAITING)
                .build();

        userLectureRepository.save(userLecture);
        // when
        List<UserLecture> allTutors = userLectureRepository.findAllTutors(savedLecture.getId());

        // then
        assertThat(em.getEntityManagerFactory().getPersistenceUnitUtil()
                .isLoaded(allTutors.get(0).getUser())).isTrue();
    }

    @DisplayName("findMyLectures_success_test")
    @Test
    void findMyLecturesTest() {
        // given
        User user = User.builder()
                .account("123")
                .password("123")
                .name("123")
                .birth(LocalDate.now())
                .gender(Gender.FEMALE)
                .phone("123")
                .degree(
                        Degree.builder()
                                .school("123")
                                .studentId("123")
                                .major("123")
                                .studentStatus(StudentStatus.ABSENCE)
                                .build()
                )
                .generation(1)
                .role(UserRole.ROLE_USER)
                .profileImg("123")
                .notificationAgreement(true)
                .isActive(true)
                .build();
        User savedUser = userRepository.save(user);


        Lecture lecture = Lecture.builder()
                .build();
        Lecture savedLecture = lectureRepository.save(lecture);

        UserLecture userLecture = UserLecture.builder()
                .lecture(savedLecture)
                .user(user)
                .tutorRole(TutorRole.SUB_TUTOR)
                .tutorStatus(TutorStatus.WAITING)
                .build();

        userLectureRepository.save(userLecture);
        // when
        List<UserLecture> myLectures = userLectureRepository.findMyLectures(savedUser.getId());
        // then
        assertThat(em.getEntityManagerFactory().getPersistenceUnitUtil()
                .isLoaded(myLectures.get(0).getUser())).isTrue();
    }

    @DisplayName("findAllAssignedTutors_success_test")
    @Test
    void findAllAssignedTutorsTest() {
        // given
        int userCount=5;
        List<User> userList = setUpUserList(userCount);

        Lecture lecture = Lecture.builder()
                .build();
        Lecture savedLecture = lectureRepository.save(lecture);

        int assignedUserCount=3;

        for(int i=0;i<assignedUserCount;i++){
            UserLecture userLecture = UserLecture.builder()
                    .lecture(savedLecture)
                    .user(userList.get(i))
                    .tutorRole(TutorRole.SUB_TUTOR)
                    .tutorStatus(TutorStatus.ASSIGNED)
                    .build();
            userLectureRepository.save(userLecture);
        }

        for(int i=assignedUserCount;i<userCount;i++){
            UserLecture userLecture = UserLecture.builder()
                    .lecture(savedLecture)
                    .user(userList.get(i))
                    .tutorRole(TutorRole.SUB_TUTOR)
                    .tutorStatus(TutorStatus.WAITING)
                    .build();
            userLectureRepository.save(userLecture);
        }
        //1~3 번 유저 강사 선정
        //4~5 번 유저 강사 미선정
        //5번 유저로 조회시 선정강사 + 5번유저 정보 => 4개 나와야 함
        User unassignedUser=userList.get(userCount-1);
        // when
        List<UserLecture> allAssignedTutors = userLectureRepository.findAllAssignedTutors(savedLecture.getId(), unassignedUser.getId());

        // then
        assertThat(allAssignedTutors.size()).isEqualTo(assignedUserCount+1);
        assertThat(em.getEntityManagerFactory().getPersistenceUnitUtil()
                .isLoaded(allAssignedTutors.get(0).getUser())).isTrue();
    }

    @DisplayName("findUserLecture_success_test")
    @Test
    void findUserLectureTest() {
        // given
        //1 강의 2유저 3튜터롤 선정
        Lecture lecture = Lecture.builder()
                .build();
        Lecture savedLecture = lectureRepository.save(lecture);

        int userCount=2;
        List<User> userList = setUpUserList(userCount);
        for (User user : userList) {
            for (TutorRole tutorRole : TutorRole.values()){
                UserLecture userLecture = UserLecture.builder()
                        .tutorRole(tutorRole)
                        .user(user)
                        .lecture(savedLecture)
                        .build();
                userLectureRepository.save(userLecture);
            }
        }
        // when
        // 강의 유저 튜터롤로 가져오기
        int userIndex=0;
        TutorRole tutorRole=TutorRole.SUB_TUTOR;
        Optional<UserLecture> userLecture = userLectureRepository.findUserLecture(savedLecture.getId(), userList.get(userIndex).getId(), tutorRole);

        // then
        // 1개 나와야하고 값 전부 일치
        assertThat(userLecture.get().getUser()).isEqualTo(userList.get(userIndex));
        assertThat(userLecture.get().getLecture()).isEqualTo(savedLecture);
        assertThat(userLecture.get().getTutorRole()).isEqualTo(tutorRole);
    }
    @DisplayName("findUserLecture_noResult_test")
    @Test
    void testMethodNameHere() {
        Lecture lecture = Lecture.builder()
                .build();
        Lecture savedLecture = lectureRepository.save(lecture);

        int userCount=2;
        List<User> userList = setUpUserList(userCount);
        for (User user : userList) {
            for (TutorRole tutorRole : TutorRole.values()){
                if(tutorRole == TutorRole.MAIN_TUTOR){
                    continue;
                }
                UserLecture userLecture = UserLecture.builder()
                        .tutorRole(tutorRole)
                        .user(user)
                        .lecture(savedLecture)
                        .build();
                userLectureRepository.save(userLecture);
            }
        }
        // when
        // 강의 유저 튜터롤로 가져오기
        //MAIN_TUTOR는 아무도 신청 안함 empty 반환해야함
        int userIndex=0;
        TutorRole tutorRole=TutorRole.MAIN_TUTOR;
        Optional<UserLecture> userLecture = userLectureRepository.findUserLecture(savedLecture.getId(), userList.get(userIndex).getId(), tutorRole);

        // then
        assertThat(userLecture.isEmpty()).isTrue();
    }
}