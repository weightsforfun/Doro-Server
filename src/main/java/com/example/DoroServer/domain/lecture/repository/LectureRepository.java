package com.example.DoroServer.domain.lecture.repository;

import com.example.DoroServer.domain.lecture.entity.Lecture;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.example.DoroServer.domain.lecture.entity.LectureStatus;
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


    @Query(value = "select l from Lecture l join l.lectureDates ld " +
                    "group by l.id " +
                    "having max(ld) = :finishedDate")
    List<Lecture> findLecturesByFinishedDate(@Param("finishedDate") LocalDate finishedDate);

    @Query(value = "select DISTINCT (l.city) from Lecture l where l.status= :lectureStatus")
    List<String> findDistinctCity(LectureStatus lectureStatus);
}
