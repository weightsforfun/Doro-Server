package com.example.DoroServer.domain.userLecture.dto;

import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.domain.userLecture.entity.TutorRole;
import com.example.DoroServer.domain.userLecture.entity.UserLecture;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindAllTutorsRes {
    private String name;
    private String major;
    private TutorRole tutorRole;
    public static FindAllTutorsRes fromEntity(UserLecture userLecture){
        User user = userLecture.getUser();
        return FindAllTutorsRes.builder()
                .name(user.getName())
                .major(user.getDegree().getMajor())
                .tutorRole(userLecture.getTutorRole())
                .build();
    }
}
