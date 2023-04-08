package com.example.DoroServer.domain.lecture.api;

import com.example.DoroServer.domain.lecture.dto.CreateLectureReq;
import com.example.DoroServer.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lectures")
@RequiredArgsConstructor
public class LectureApi {


    @GetMapping()
    public SuccessResponse findAllLectures() {
        return SuccessResponse.successResponse("all lectures");
    }

    @PostMapping()
    public SuccessResponse createLecture(@RequestBody CreateLectureReq createLectureReq) {
        return SuccessResponse.successResponse(
                "lecture created"
                + createLectureReq
        );
    }

    @GetMapping("/{id}")
    public SuccessResponse findLecture(@PathVariable("id") Long id) {

        return SuccessResponse.successResponse("hi");
    }

    @PatchMapping("/{id}")
    public SuccessResponse updateLecture(@PathVariable("id") String id) {
        return SuccessResponse.successResponse(id + "th lecture patched");
    }

    @DeleteMapping("/{id}")
    public SuccessResponse deleteLecture(@PathVariable("id") String id) {
        return SuccessResponse.successResponse(id + "th lecture deleted");
    }


}
