package com.example.DoroServer.domain.user.dto;

import com.example.DoroServer.domain.user.entity.Degree;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;


@Getter
@EqualsAndHashCode
public class UpdateUserReq {
    private Degree degree;

    private String phone; // 사용자 전화번호

    private int generation; // 사용자 기수

    //사용자 프로필 수정 기능 추가필요
}
