package com.example.DoroServer.domain.lecture.api;

import com.example.DoroServer.global.common.SuccessResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lectures")
public class LectureApi {

    @GetMapping()
    public SuccessResponse getLectures() {
        return SuccessResponse.successResponse("all lectures");
    }

    @PostMapping()
    public SuccessResponse postLecture() {
        return SuccessResponse.successResponse("lecture created");
    }

    @GetMapping("/{id}")
    public SuccessResponse getLecture(@PathVariable("id") String id) {
        return SuccessResponse.successResponse(id + "th lecture");
    }

    @PatchMapping("/{id}")
    public SuccessResponse patchLecture(@PathVariable("id") String id) {
        return SuccessResponse.successResponse(id + "th lecture patched");
    }

    @DeleteMapping("/{id}")
    public SuccessResponse deleteLecture(@PathVariable("id") String id) {
        return SuccessResponse.successResponse(id + "th lecture deleted");
    }


}
