package com.example.DoroServer.domain.lecture.service;

import com.example.DoroServer.domain.lecture.dto.CreateLectureReq;
import com.example.DoroServer.domain.lecture.dto.FindAllLecturesCond;
import com.example.DoroServer.domain.lecture.dto.FindAllLecturesRes;
import com.example.DoroServer.domain.lecture.dto.FindLectureRes;
import com.example.DoroServer.domain.lecture.dto.LectureDto;
import com.example.DoroServer.domain.lecture.dto.UpdateLectureReq;
import com.example.DoroServer.domain.lecture.entity.Lecture;
import com.example.DoroServer.domain.lecture.repository.LectureRepository;
import com.example.DoroServer.domain.lectureContent.dto.LectureContentDto;
import com.example.DoroServer.domain.lectureContent.entity.LectureContent;
import com.example.DoroServer.domain.lectureContent.repository.LectureContentRepository;
import com.example.DoroServer.global.common.ErrorResponse;
import com.example.DoroServer.global.exception.BaseException;
import com.example.DoroServer.global.exception.Code;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LectureService {

    private final LectureRepository lectureRepository;
    private final LectureContentRepository lectureContentRepository;
    private final ModelMapper modelMapper;

    public List<FindAllLecturesRes> findAllLectures(FindAllLecturesCond findAllLecturesCond,
            Pageable pageable) {
        Page<Lecture> allLecturesWithFilter = lectureRepository.findAllLecturesWithFilter(
                findAllLecturesCond, pageable);

        List<Lecture> content = allLecturesWithFilter.getContent();

        List<FindAllLecturesRes> lectureResList = content.stream()
                .map(FindAllLecturesRes::fromEntity)
                .collect(Collectors.toList());

        return lectureResList;
    }

    public Long createLecture(CreateLectureReq createLectureReq) {

        Lecture lecture = createLectureReq.toEntity();
        Optional<LectureContent> optionalLectureContent = lectureContentRepository.findById(
                createLectureReq.getLectureContentId());

        if (optionalLectureContent.isPresent()) {
            lecture.setLectureContent(optionalLectureContent.get());
            lectureRepository.save(lecture);
            return lecture.getId();
        }

        throw new BaseException(Code.BAD_REQUEST);
    }

    public FindLectureRes findLecture(Long id) {
        Optional<Lecture> optionalLecture = lectureRepository.findLectureById(id);
        if (optionalLecture.isPresent()) {
            Lecture lecture = optionalLecture.get();
            LectureDto lectureDto = LectureDto.fromEntity(lecture);
            LectureContentDto lectureContentDto = LectureContentDto.fromEntity(
                    lecture.getLectureContent());
            FindLectureRes findLectureRes = FindLectureRes.builder()
                    .lectureDto(lectureDto)
                    .lectureContentDto(lectureContentDto)
                    .build();
            return findLectureRes;
        } else {
            throw new BaseException(Code.BAD_REQUEST);
        }
    }

    public Long updateLecture(Long id, UpdateLectureReq updateLectureReq) {
        Optional<Lecture> optionalLecture = lectureRepository.findById(id);
        if (optionalLecture.isPresent()) {
            Lecture lecture = optionalLecture.get();
            log.info(updateLectureReq.toString());
            modelMapper.map(updateLectureReq, lecture);
            return id;
        } else {
            throw new BaseException(Code.BAD_REQUEST);
        }
    }

    public String deleteLecture(Long id) {
        lectureRepository.deleteById(id);
        return "deleted";
    }
}
