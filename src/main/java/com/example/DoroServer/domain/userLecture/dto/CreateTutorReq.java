package com.example.DoroServer.domain.userLecture.dto;

import com.example.DoroServer.domain.userLecture.entity.TutorRole;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.*;

@Getter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CreateTutorReq {
    @NotNull
    @Enumerated(EnumType.STRING)
    private TutorRole tutorRole;
    @NotNull
    private Long userId;

}
