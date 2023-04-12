package com.example.DoroServer.domain.lecture.repository;

import com.example.DoroServer.domain.lecture.dto.FindAllLecturesCond;
import com.example.DoroServer.domain.lecture.entity.Lecture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LectureRepositoryCustom {
    Page<Lecture> findAllLecturesWithFilter(FindAllLecturesCond findAllLecturesCond, Pageable pageable);

}
