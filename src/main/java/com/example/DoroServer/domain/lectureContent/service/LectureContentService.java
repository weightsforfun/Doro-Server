package com.example.DoroServer.domain.lectureContent.service;

import com.example.DoroServer.domain.lecture.repository.LectureRepository;
import com.example.DoroServer.domain.lectureContent.dto.LectureContentDto;
import com.example.DoroServer.domain.lectureContent.dto.LectureContentMapper;
import com.example.DoroServer.domain.lectureContent.dto.UpdateLectureContentReq;
import com.example.DoroServer.domain.lectureContent.entity.LectureContent;
import com.example.DoroServer.domain.lectureContent.repository.LectureContentRepository;
import com.example.DoroServer.global.exception.BaseException;
import com.example.DoroServer.global.exception.Code;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LectureContentService {
    private final LectureContentRepository lectureContentRepository;
    private final LectureContentMapper lectureContentMapper;
    private final ModelMapper modelMapper;
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

    public Long updateLectureContent(Long id, UpdateLectureContentReq updateLectureContentReq){
        LectureContent lectureContent = lectureContentRepository.findById(id)
                .orElseThrow(() -> new BaseException(Code.LECTURE_CONTENT_NOT_FOUND));
        modelMapper.map(updateLectureContentReq,lectureContent);
        return lectureContent.getId();
    }
}
