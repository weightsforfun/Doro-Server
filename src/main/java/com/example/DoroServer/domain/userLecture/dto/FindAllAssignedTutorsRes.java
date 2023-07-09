package com.example.DoroServer.domain.userLecture.dto;


import com.example.DoroServer.domain.user.entity.Degree;
import com.example.DoroServer.domain.userLecture.entity.TutorRole;
import com.example.DoroServer.domain.userLecture.entity.TutorStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindAllAssignedTutorsRes {
    private Long id;
    private String name;
    private Degree degree;
    private TutorRole tutorRole;
    private Long userId;
    private TutorStatus tutorStatus;
}
