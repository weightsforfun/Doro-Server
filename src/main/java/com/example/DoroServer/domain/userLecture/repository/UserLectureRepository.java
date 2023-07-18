package com.example.DoroServer.domain.userLecture.repository;

import com.example.DoroServer.domain.lecture.entity.Lecture;
import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.domain.userLecture.entity.TutorRole;
import com.example.DoroServer.domain.userLecture.entity.UserLecture;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;


@Repository
public interface UserLectureRepository extends JpaRepository<UserLecture, Long> {

    @Query(value =
            "select ul from UserLecture ul "
                    + "join ul.lecture l "
                    + "join fetch ul.user "
                    + "where l.id = :id"
    )
    List<UserLecture> findAllTutors(@Param("id") Long id);

    @Query(value =
            "select ul from UserLecture ul "
                    + "join ul.lecture l "
                    + "join fetch ul.user "
                    + "where l.id = :lectureId and (ul.tutorStatus = 'ASSIGNED' or ul.user.id=:userId)"
    )
    List<UserLecture> findAllAssignedTutors(
            @Param("lectureId") Long lectureId,
            @Param("userId") Long userId
            );

    @Query(value =
            "select ul from UserLecture ul "
                    + "join ul.user u "
                    + "join fetch ul.lecture "
                    + "where u.id = :id"
    )
    List<UserLecture> findMyLectures(@Param("id") Long id);

    @Query(value =
            "select ul from UserLecture ul "
                    + "join ul.user u "
                    + "join ul.lecture l "
                    + "where u.id = :userId and "
                    + "l.id = :lectureId and "
                    + "ul.tutorRole = :tutorRole "

    )
    Optional<UserLecture> findUserLecture(
            @Param("lectureId") Long lectureId,
            @Param("userId") Long userId,
            @Param("tutorRole") TutorRole tutorRole
    );
    void deleteAllByUser(User user);

    void deleteAllByLecture(Lecture lecture);
}
