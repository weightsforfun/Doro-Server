package com.example.DoroServer.domain.lecture.repository;

import com.example.DoroServer.domain.lecture.dto.FindAllLecturesCond;
import com.example.DoroServer.domain.lecture.entity.Lecture;
import com.example.DoroServer.domain.lectureContent.entity.LectureContent;
import com.example.DoroServer.domain.lectureContent.repository.LectureContentRepository;
import com.example.DoroServer.global.config.QueryDslConfig;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Import({QueryDslConfig.class})
@ActiveProfiles("test")
class LectureRepositoryTest {

    @Autowired
    LectureRepository lectureRepository;

    @Autowired
    LectureContentRepository lectureContentRepository;
    @PersistenceContext
    EntityManager em;

    private LectureContent setUpLectureContent(){
        LectureContent lectureContent = LectureContent.builder().build();
        LectureContent saved = lectureContentRepository.save(lectureContent);
        return saved;
    }
    @DisplayName("findLectureById_Success_Test")
    @Test
    void findLectureByIdTest() {
        // given
        LectureContent lectureContent = setUpLectureContent();

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
        LectureContent lectureContent = setUpLectureContent();

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

    @DisplayName("findAllLectureWithFilter_success_test")
    @Test
    void findAllLecturesWithFilterTest() {
        // given
        //다른 도시 3개에 강의 2개씩
        //다른 범위 날짜 3개 그 중 2개는 겹치게
        LectureContent lectureContent = setUpLectureContent();

        ArrayList<ArrayList<LocalDate>> localDatesList = new ArrayList<>();

        ArrayList<LocalDate> localDates = new ArrayList<>();
        localDates.add(LocalDate.of(2023,10,10));
        localDates.add(LocalDate.of(2023,10,11));


        ArrayList<LocalDate> localDates2 = new ArrayList<>();
        localDates2.add(LocalDate.of(2023,10,10));
        localDates2.add(LocalDate.of(2023,10,9));

        ArrayList<LocalDate> localDates3 = new ArrayList<>();
        localDates3.add(LocalDate.of(2023,10,9));


        localDatesList.add(localDates);
        localDatesList.add(localDates2);
        localDatesList.add(localDates3);


        Lecture cityLecture = Lecture.builder()
                .city("Seoul")
                .build();
        lectureRepository.save(cityLecture);

        for(ArrayList<LocalDate> inputList  :  localDatesList){
            Lecture lecture1 = Lecture.builder()
                    .lectureDates(inputList)
                    .city("Seoul")
                    .lectureContent(lectureContent)
                    .build();
            lectureRepository.save(lecture1);
        }

        for(ArrayList<LocalDate> inputList  :  localDatesList){
            Lecture lecture1 = Lecture.builder()
                    .lectureDates(inputList)
                    .city("NoTarget")
                    .lectureContent(lectureContent)
                    .build();
            lectureRepository.save(lecture1);
        }


        Pageable pageable = Pageable.ofSize(10);
        FindAllLecturesCond nothing = FindAllLecturesCond.builder().build();

        FindAllLecturesCond city = FindAllLecturesCond.builder()
                .city(new ArrayList<String>(Arrays.asList("Seoul")))
                .build();

        FindAllLecturesCond dates = FindAllLecturesCond.builder()
                .startDate(LocalDate.of(2023,10,10))
                .endDate(LocalDate.of(2023,10,11))
                .build();

        FindAllLecturesCond all = FindAllLecturesCond.builder()
                .city(new ArrayList<String>(Arrays.asList("Seoul")))
                .startDate(LocalDate.of(2023,10,10))
                .endDate(LocalDate.of(2023,10,11))
                .build();

        // when
        //아무것도 없이, 도시만,날짜만, 도시 날짜 둘다


        Page<Lecture> nothingList = lectureRepository.findAllLecturesWithFilter(nothing, pageable);
        Page<Lecture> cityList = lectureRepository.findAllLecturesWithFilter(city, pageable);
        Page<Lecture> datesList = lectureRepository.findAllLecturesWithFilter(dates, pageable);
        Page<Lecture> allList = lectureRepository.findAllLecturesWithFilter(all, pageable);

        // then

        assertThat(nothingList.getNumberOfElements()).isEqualTo(7);
        assertThat(cityList.getContent().size()).isEqualTo(4);
        assertThat(datesList.getContent().size()).isEqualTo(4);
        assertThat(allList.getContent().size()).isEqualTo(2);

    }
}