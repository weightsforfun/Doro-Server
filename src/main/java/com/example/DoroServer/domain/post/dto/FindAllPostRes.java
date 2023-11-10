package com.example.DoroServer.domain.post.dto;


import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class FindAllPostRes {

    private Long id;

    private String title;

    private boolean isLocked;

    private boolean isAnswered;

    private String ownerName;

    private LocalDateTime createdAt;

}
