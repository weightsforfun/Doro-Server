package com.example.DoroServer.domain.userLecture.api;

import com.example.DoroServer.global.common.SuccessResponse;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/users-lectures")
public class UserLectureApi {

    @GetMapping("/lectures/{id}")
    public SuccessResponse getTutor(@PathVariable("id") String id) {
        return SuccessResponse.successResponse(id + "th tutors");
    }

    @GetMapping("/users/{id}")
    public SuccessResponse getLecture(@PathVariable("id") String id) {
        return SuccessResponse.successResponse(id + "user's lectures");
    }

    @PostMapping("/lectures/{id}")
    public SuccessResponse postTutor(@PathVariable("id") String id) {
        return SuccessResponse.successResponse(id + "added");
    }

    @PatchMapping("/lectures/{id}")
    public SuccessResponse patchTutor(@PathVariable("id") String id) {
        return SuccessResponse.successResponse(id + "th main patched");
    }

}
