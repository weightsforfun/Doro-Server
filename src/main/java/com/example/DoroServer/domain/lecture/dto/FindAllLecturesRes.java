package com.example.DoroServer.domain.lecture.dto;


import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@ToString
@Builder
@AllArgsConstructor
public class FindAllLecturesRes {
    private List<FindAllLecturesInfo> lecturesInfos;
    private Long totalCount;
}
