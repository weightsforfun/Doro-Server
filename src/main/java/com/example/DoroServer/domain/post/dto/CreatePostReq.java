package com.example.DoroServer.domain.post.dto;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CreatePostReq {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    private String password;

    @NotNull
    private boolean isLocked;

    @NotBlank
    private String ownerName;

    @NotBlank
    private String institution;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String email;

}
