package com.example.DoroServer.domain.lectureContent.service;

import com.example.DoroServer.domain.lecture.repository.LectureRepository;
import com.example.DoroServer.domain.lectureContent.dto.LectureContentDto;
import com.example.DoroServer.domain.lectureContent.dto.LectureContentMapper;
import com.example.DoroServer.domain.lectureContent.entity.LectureContent;
import com.example.DoroServer.domain.lectureContent.repository.LectureContentRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LectureContentService {
    private final LectureContentRepository lectureContentRepository;
    private final LectureContentMapper lectureContentMapper;
    public Long createLecture(LectureContentDto lectureContentDto){
        LectureContent lectureContent = lectureContentMapper.toLectureContent(lectureContentDto);
        LectureContent savedLectureContent = lectureContentRepository.save(lectureContent);
        return savedLectureContent.getId();
    }
    public List<LectureContentDto> findAllLectureContents(){
        List<LectureContent> lectureContentList = lectureContentRepository.findAll();
        List<LectureContentDto> lectureContentResList = lectureContentList.stream()
                .map(lectureContent -> lectureContentMapper.toLectureContentDto(lectureContent))
                .collect(Collectors.toList());
        return lectureContentResList;
    }
}
