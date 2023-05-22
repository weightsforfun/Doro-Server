package com.example.DoroServer.domain.user.dto;

import com.example.DoroServer.domain.lecture.dto.FindAllLecturesRes;
import com.example.DoroServer.domain.user.entity.Degree;
import com.example.DoroServer.domain.user.entity.User;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class FindAllUsersRes {
    private Long id;
    private String name; // 사용자 이름
    private LocalDate birth; // 생년월일
    private int generation; // 사용자 기수
    private Degree degree;




}
