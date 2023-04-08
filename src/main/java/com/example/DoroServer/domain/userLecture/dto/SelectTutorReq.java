package com.example.DoroServer.domain.userLecture.dto;

import com.example.DoroServer.domain.userLecture.entity.TutorRole;
import java.util.List;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class SelectTutorReq {

    private List<Long> assignedUser;
    @NotNull()
    @Enumerated(EnumType.STRING)
    private TutorRole tutorRole;


}
