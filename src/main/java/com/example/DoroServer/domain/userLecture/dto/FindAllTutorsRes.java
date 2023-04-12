package com.example.DoroServer.domain.userLecture.dto;

import com.example.DoroServer.domain.user.entity.User;
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
    private static FindAllTutorsRes fromEntity(User user){
        return FindAllTutorsRes.builder()
                .name(user.getName())
                .major(user.getDegree().getMajor())
                .build();
    }
}
