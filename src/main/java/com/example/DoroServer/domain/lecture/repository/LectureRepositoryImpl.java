package com.example.DoroServer.domain.lecture.repository;

import static com.example.DoroServer.domain.lecture.entity.QLecture.*;

import com.example.DoroServer.domain.lecture.dto.FindAllLecturesCond;
import com.example.DoroServer.domain.lecture.entity.Lecture;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class LectureRepositoryImpl implements LectureRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    public Page<Lecture> findAllLecturesWithFilter(FindAllLecturesCond condition, Pageable pageable){
        List<Lecture> results= queryFactory
                .select(lecture)
                .from(lecture)
                .where(
                        containCity(condition.getCity()),
                        betweenDate(condition.getStartDate(),condition.getEndDate())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(results,pageable,results.size());

    }
   private BooleanBuilder containCity(List<String> cities){
        if(cities==null){
            return null;
        }
       BooleanBuilder booleanBuilder = new BooleanBuilder();
       for (String city: cities){
            booleanBuilder.or(lecture.city.contains(city));
        }
        return booleanBuilder;
   }
   private BooleanExpression betweenDate(LocalDateTime startDate,LocalDateTime endDate){
        if(startDate !=null && endDate != null){
            return lecture.lectureDates.any().between(startDate,endDate);
        }
        return null;

   }



}
