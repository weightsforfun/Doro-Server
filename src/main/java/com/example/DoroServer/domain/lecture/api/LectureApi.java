package com.example.DoroServer.domain.lecture.api;

import com.example.DoroServer.domain.lecture.dto.CreateLectureReq;
import com.example.DoroServer.domain.lecture.dto.FindAllLecturesCond;
import com.example.DoroServer.domain.lecture.dto.FindAllLecturesRes;
import com.example.DoroServer.domain.lecture.dto.FindLectureRes;
import com.example.DoroServer.domain.lecture.dto.UpdateLectureReq;
import com.example.DoroServer.domain.lecture.service.LectureService;
import com.example.DoroServer.global.common.SuccessResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lectures")
@RequiredArgsConstructor
public class LectureApi {

    private final LectureService lectureService;

    @GetMapping()
    public SuccessResponse findAllLectures(
            @RequestBody FindAllLecturesCond findAllLecturesCond,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page,size);
        List<FindAllLecturesRes> allLectures = lectureService.findAllLectures(findAllLecturesCond,
                pageRequest);
        return SuccessResponse.successResponse(allLectures);
    }

    @PostMapping()
    public SuccessResponse createLecture(@RequestBody CreateLectureReq createLectureReq) {
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
            @RequestBody UpdateLectureReq updateLectureReq
    ) {
        Long lectureId = lectureService.updateLecture(id, updateLectureReq);
        return SuccessResponse.successResponse(lectureId + "th lecture patched");
    }

    @DeleteMapping("/{id}")
    public SuccessResponse deleteLecture(@PathVariable("id") Long id) {
        String lectureId = lectureService.deleteLecture(id);
        return SuccessResponse.successResponse(lectureId + "th lecture deleted");
    }


}
