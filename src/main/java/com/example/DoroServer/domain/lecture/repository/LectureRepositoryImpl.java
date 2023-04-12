package com.example.DoroServer.domain.lecture.repository;

import static com.example.DoroServer.domain.lecture.entity.QLecture.*;

import com.example.DoroServer.domain.lecture.dto.FindAllLecturesCond;
import com.example.DoroServer.domain.lecture.entity.Lecture;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class LectureRepositoryImpl implements LectureRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    public Page<Lecture> findAllLecturesWithFilter(FindAllLecturesCond condition, Pageable pageable){
        QueryResults<Lecture> results= queryFactory
                .select(lecture)
                .from(lecture)
                .where(
                        containCity(condition.getCity())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        List<Lecture> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content,pageable,total);

    }
   private BooleanExpression containCity(String city){
        return StringUtils.hasText(city) ? lecture.city.contains(city) : null;
   }



}