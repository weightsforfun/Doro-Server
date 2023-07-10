package com.example.DoroServer.domain.lecture.repository;

import com.example.DoroServer.domain.lecture.entity.Lecture;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureRepository extends JpaRepository<Lecture,Long>, LectureRepositoryCustom {
    @Query(value="select l from Lecture l "
            +"join fetch l.lectureContent "
            +"where l.id = :id")
    Optional<Lecture> findLectureById(@Param("id") Long id);

    @Query(value="select * "
            + "from lecture l "
            + "where l.lecture_id in "
            + "(select ld.lecture_id "
            + "from lecture_date ld "
            + "group by ld.lecture_id having date_format(max(lecture_dates),\"%Y-%m-%d\") = date_format(:finishedDate,\"%Y-%m-%d\")) "
            ,nativeQuery = true)
    List<Lecture> findLecturesByFinishedDate(@Param("finishedDate") LocalDate finishedDate);
}
