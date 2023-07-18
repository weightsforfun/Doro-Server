package com.example.DoroServer.domain.lecture.repository;

import com.example.DoroServer.domain.lecture.entity.Lecture;
import com.example.DoroServer.domain.lectureContent.entity.LectureContent;
import com.example.DoroServer.global.config.QueryDslConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryDslConfig.class})
class LectureRepositoryTest {

    @Autowired
    LectureRepository lectureRepository;

    @PersistenceContext
    EntityManager em;


    @DisplayName("findLectureById_Success_Test")
    @Test
    void findLectureByIdTest() {
        // given
        LectureContent lectureContent = LectureContent.builder().id(1L).build();
        Lecture lecture = Lecture.builder()
                .lectureContent(lectureContent)
                .build();
        Lecture saved = lectureRepository.save(lecture);
        // when
        Optional<Lecture> optionalLecture = lectureRepository.findLectureById(saved.getId());
        // then
        assertThat(em.getEntityManagerFactory().getPersistenceUnitUtil()
                .isLoaded(optionalLecture.get().getLectureContent())).isTrue();
    }

    @DisplayName("findLecturesByFinishedDate_Success_Test")
    @Test
    void findLecturesByFinishedDateTest() {
        // given
        LectureContent lectureContent = LectureContent.builder().id(1L).build();

        ArrayList<ArrayList<LocalDate>> localDatesList = new ArrayList<>();

        ArrayList<LocalDate> localDates = new ArrayList<>();
        localDates.add(LocalDate.of(2023,10,10));
        localDates.add(LocalDate.of(2023,10,11));


        ArrayList<LocalDate> localDates2 = new ArrayList<>();
        localDates2.add(LocalDate.of(2023,10,10));
        localDates2.add(LocalDate.of(2023,10,9));

        ArrayList<LocalDate> localDates3 = new ArrayList<>();
        localDates3.add(LocalDate.of(2023,10,10));


        localDatesList.add(localDates);
        localDatesList.add(localDates2);
        localDatesList.add(localDates3);

        for(ArrayList<LocalDate> inputList  :  localDatesList){
            Lecture lecture1 = Lecture.builder()
                    .lectureDates(inputList)
                    .lectureContent(lectureContent)
                    .build();
            Lecture saved = lectureRepository.save(lecture1);
        }
        Integer finishedLecturesCount=2;

        LocalDate finishedDate=LocalDate.of(2023, 10, 10);
        // when
        List<Lecture> lecturesByFinishedDate = lectureRepository.findLecturesByFinishedDate(finishedDate);
        // then
        assertThat(lecturesByFinishedDate.size()).isEqualTo(finishedLecturesCount);
    }

}