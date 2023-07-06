package com.example.DoroServer.domain.lecture.api;

import com.example.DoroServer.domain.lecture.dto.CreateLectureReq;
import com.example.DoroServer.domain.lecture.dto.FindAllLecturesCond;
import com.example.DoroServer.domain.lecture.dto.FindAllLecturesRes;
import com.example.DoroServer.domain.lecture.dto.FindLectureRes;
import com.example.DoroServer.domain.lecture.dto.UpdateLectureReq;
import com.example.DoroServer.domain.lecture.service.LectureService;
import com.example.DoroServer.global.common.SuccessResponse;
import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "강의")
@RestController
@RequestMapping("/lectures")
@RequiredArgsConstructor
@Slf4j
public class LectureApi {

    private final LectureService lectureService;
    @ApiOperation(value = "강의 조회",notes = "모든 강의 조회  lecture content 먼저 만들고 여기에 넣어줘야 정삭적으로 만들어집니다.")
    @GetMapping()
    public SuccessResponse findAllLectures(
            @ModelAttribute("findAllLecturesCond") FindAllLecturesCond findAllLecturesCond,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page,size);
        List<FindAllLecturesRes> allLectures = lectureService.findAllLectures(findAllLecturesCond,
                pageRequest);
        return SuccessResponse.successResponse(allLectures);
    }

    @PostMapping()
    public SuccessResponse createLecture(@RequestBody @Valid CreateLectureReq createLectureReq) {
        Long lectureId = lectureService.createLecture(createLectureReq);
        return SuccessResponse.successResponse(
                "lecture created"
                        + lectureId
        );
    }

    @GetMapping("/{id}")
    public SuccessResponse findLecture(@PathVariable("id") Long id) {
        FindLectureRes lecture = lectureService.findLecture(id);
        return SuccessResponse.successResponse(lecture);
    }

    @PatchMapping("/{id}")
    public SuccessResponse updateLecture(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateLectureReq updateLectureReq
    ) {
        Long lectureId = lectureService.updateLecture(id, updateLectureReq);
        return SuccessResponse.successResponse(lectureId + "th lecture patched");
    }

    @DeleteMapping("/{id}")
    public SuccessResponse deleteLecture(@PathVariable("id") Long id) {
        String lectureId = lectureService.deleteLecture(id);
        return SuccessResponse.successResponse(lectureId + "th lecture deleted");
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void checkLectureFinishedDate(){
        lectureService.checkLectureFinishedDate();
    }




}
