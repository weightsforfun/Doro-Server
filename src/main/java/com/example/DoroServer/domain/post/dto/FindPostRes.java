package com.example.DoroServer.domain.post.dto;


import lombok.*;

@ToString
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class FindPostRes {

    private Long id; // PK

    private String title;

    private String content;

    private String ownerName;

    private String institution;

    private String email;

    private String answer;

    private boolean isAnswered;

}
