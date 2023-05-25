package com.example.DoroServer.domain.userLecture.api;

import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.domain.userLecture.dto.FindAllTutorsRes;
import com.example.DoroServer.domain.userLecture.dto.FindMyLecturesRes;
import com.example.DoroServer.domain.userLecture.dto.SelectTutorReq;
import com.example.DoroServer.domain.userLecture.dto.CreateTutorReq;
import com.example.DoroServer.domain.userLecture.service.UserLectureService;
import com.example.DoroServer.global.common.SuccessResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/users-lectures")
@RequiredArgsConstructor
public class UserLectureApi {

    private final UserLectureService userLectureService;

    @GetMapping("/lectures/{id}")
    public SuccessResponse findAllTutors(@PathVariable("id") Long id) {
        List<FindAllTutorsRes> allTutorsResList = userLectureService.findAllTutors(id);
        return SuccessResponse.successResponse(allTutorsResList);
    }

    @GetMapping("/users/{id}")
    public SuccessResponse findMyLectures(@PathVariable("id") Long id) {
        List<FindMyLecturesRes> findMyLecturesResList = userLectureService.findMyLectures(id);
        return SuccessResponse.successResponse(findMyLecturesResList);
    }

    @PostMapping("/lectures/{id}")
    //토큰에서 user id 갖고오고 신청대기, role선택해서 저장
    //이미 신청한 user  일시 예외 출력
    public SuccessResponse createTutor(
            @PathVariable("id") Long id,
            @RequestBody CreateTutorReq createTutorReq) {
        Long userLectureId = userLectureService.createTutor(id, createTutorReq );

        return SuccessResponse.successResponse(userLectureId + "is created");
    }

    @PatchMapping("/lectures/{id}")
    public SuccessResponse selectTutor(
            @PathVariable("id") Long id,
            @RequestBody SelectTutorReq selectTutorReq) {
        String result = userLectureService.selectTutor(id, selectTutorReq);
        return SuccessResponse.successResponse("change to"+result);
    }

    @DeleteMapping("/lectures/{id}")
    public SuccessResponse deleteLecture(@PathVariable("id") Long id){
        Long deleteLectureID = userLectureService.deleteLecture(id);
        return SuccessResponse.successResponse("deleted"+deleteLectureID);
    }

}
