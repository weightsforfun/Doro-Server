package com.example.DoroServer.domain.lecture.service;

import com.example.DoroServer.domain.lecture.dto.CreateLectureReq;
import com.example.DoroServer.domain.lecture.dto.FindAllLecturesCond;
import com.example.DoroServer.domain.lecture.dto.FindAllLecturesRes;
import com.example.DoroServer.domain.lecture.dto.FindLectureRes;
import com.example.DoroServer.domain.lecture.dto.LectureDto;
import com.example.DoroServer.domain.lecture.dto.LectureMapper;
import com.example.DoroServer.domain.lecture.dto.UpdateLectureReq;
import com.example.DoroServer.domain.lecture.entity.Lecture;
import com.example.DoroServer.domain.lecture.entity.LectureStatus;
import com.example.DoroServer.domain.lecture.repository.LectureRepository;
import com.example.DoroServer.domain.lectureContent.dto.LectureContentDto;
import com.example.DoroServer.domain.lectureContent.dto.LectureContentMapper;
import com.example.DoroServer.domain.lectureContent.entity.LectureContent;
import com.example.DoroServer.domain.lectureContent.repository.LectureContentRepository;
import com.example.DoroServer.global.exception.BaseException;
import com.example.DoroServer.global.exception.Code;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
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
    private final LectureMapper lectureMapper;
    private final LectureContentMapper lectureContentMapper;

    public List<FindAllLecturesRes> findAllLectures(FindAllLecturesCond findAllLecturesCond,
            Pageable pageable) {
        Page<Lecture> allLecturesWithFilter = lectureRepository.findAllLecturesWithFilter(
                findAllLecturesCond, pageable);

        List<Lecture> content = allLecturesWithFilter.getContent();

        List<FindAllLecturesRes> lectureResList = content.stream()
                .map(res -> lectureMapper.toFindAllLecturesRes(res,res.getLectureDate()))
                .collect(Collectors.toList());

        return lectureResList;
    }

    public Long createLecture(CreateLectureReq createLectureReq) {

        Lecture lecture = lectureMapper.toLecture(createLectureReq);
        Optional<LectureContent> optionalLectureContent = lectureContentRepository.findById(
                createLectureReq.getLectureContentId());

        if (optionalLectureContent.isPresent()) {
            lecture.setLectureContent(optionalLectureContent.get());
            lectureRepository.save(lecture);
            return lecture.getId();
        }

        throw new BaseException(Code.LECTURE_NOT_FOUND);
    }

    public FindLectureRes findLecture(Long id) {
        Optional<Lecture> optionalLecture = lectureRepository.findLectureById(id);
        if (optionalLecture.isPresent()) {
            Lecture lecture = optionalLecture.get();
            LectureDto lectureDto = lectureMapper.toLectureDto(lecture);
            LectureContentDto lectureContentDto = lectureContentMapper.toLectureContentDto(lecture.getLectureContent());
            FindLectureRes findLectureRes = lectureMapper.toFindLectureRes(lectureDto,lectureContentDto);
            return findLectureRes;
        } else {
            throw new BaseException(Code.LECTURE_NOT_FOUND);
        }
    }

    public Long updateLecture(Long id, UpdateLectureReq updateLectureReq) {
        Lecture lecture = lectureRepository.findById(id).orElseThrow(() -> new BaseException(Code.LECTURE_NOT_FOUND));
        modelMapper.map(updateLectureReq,lecture);
        lecture.getLectureDates().clear();
        List<LocalDate> newLectureDates = updateLectureReq.getLectureDates();
        for (LocalDate newLectureDate : newLectureDates) {
            lecture.getLectureDates().add(newLectureDate);
        }

        return id;
    }

    public String deleteLecture(Long id) {
        lectureRepository.deleteById(id);
        return "deleted";
    }

    public void checkLectureFinishedDate(){
        List<Long> finishedLecturesId = lectureRepository.findLecturesByFinishedDate(LocalDate.now().minusDays(1));

        log.info(finishedLecturesId.toString());
        for (Long id : finishedLecturesId) {
            Lecture lecture = lectureRepository.findLectureById(id).orElseThrow(() -> new BaseException(Code.LECTURE_NOT_FOUND));
            lecture.changeLectureStatus(LectureStatus.FINISH);
        }
    }
}
