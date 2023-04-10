package com.example.DoroServer.domain.lectureContent.dto;

import com.example.DoroServer.domain.lecture.entity.Lecture;
import com.example.DoroServer.domain.lectureContent.entity.LectureContent;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LectureContentDto {

    @NotEmpty
    private String kit; // 강의 사용 키트
    @NotEmpty
    private String detail; // 강의 세부 구성
    @NotEmpty
    private String remark; // 강의 기타 사항
    @NotEmpty
    private String requirement; // 강의 자격 요건

    public LectureContent toEntity() {
        return LectureContent.builder()
                .kit(kit)
                .detail(detail)
                .remark(remark)
                .requirement(requirement)
                .build();
    }

    public static LectureContentDto fromEntity(LectureContent lectureContent) {
        return LectureContentDto.builder()
                .kit(lectureContent.getKit())
                .detail(lectureContent.getDetail())
                .remark(lectureContent.getRemark())
                .requirement(lectureContent.getRequirement())
                .build();
    }
}
