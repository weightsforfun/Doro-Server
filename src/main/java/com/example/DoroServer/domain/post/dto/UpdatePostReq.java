package com.example.DoroServer.domain.post.dto;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UpdatePostReq {

    private String currentPassword; // 현재 비밀번호를 통한 게시물 작성자 인증

    private String title;

    private String content;

    private String password;

    private boolean isLocked;

    private String ownerName;

    private String institution;

    private String phoneNumber;

    private String email;

}
