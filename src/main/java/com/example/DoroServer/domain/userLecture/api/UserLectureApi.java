package com.example.DoroServer.domain.userLecture.api;

import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.domain.userLecture.dto.FindAllTutorsRes;
import com.example.DoroServer.domain.userLecture.dto.FindMyLecturesRes;
import com.example.DoroServer.domain.userLecture.dto.SelectTutorReq;
import com.example.DoroServer.domain.userLecture.dto.CreateTutorReq;
import com.example.DoroServer.domain.userLecture.service.UserLectureService;
import com.example.DoroServer.global.common.SuccessResponse;
import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "강의 신청 및 배정")
@RestController()
@RequestMapping("/users-lectures")
@RequiredArgsConstructor
public class UserLectureApi {

    private final UserLectureService userLectureService;
    @ApiOperation(value = "강사목록 조회",notes = "id에 lecture id 를 넣으면 해당 강의에 신청한 강사 목록을 불러옵니다.")
    @GetMapping("/lectures/{id}")
    public SuccessResponse findAllTutors(@PathVariable("id") Long id) {
        List<FindAllTutorsRes> allTutorsResList = userLectureService.findAllTutors(id);
        return SuccessResponse.successResponse(allTutorsResList);
    }
    @ApiOperation(value = "내 강의 조회" ,notes = "id 에 user id 를 넣으면 해당 user 가 신청한 강의를 불러옵니다.")
    @GetMapping("/users/{id}")
    public SuccessResponse findMyLectures(@PathVariable("id") Long id) {
        List<FindMyLecturesRes> findMyLecturesResList = userLectureService.findMyLectures(id);
        return SuccessResponse.successResponse(findMyLecturesResList);
    }
    @ApiOperation(value = "강사 신청",notes = "id에 강의 id를 createTutorReq 에 강사 id와 직업을 넣으면 해당 강의에 강사 신청이 됩니다.")
    @PostMapping("/lectures/{id}")
    //토큰에서 user id 갖고오고 신청대기, role선택해서 저장
    //이미 신청한 user  일시 예외 출력
    public SuccessResponse createTutor(
            @PathVariable("id") Long id,
            @RequestBody @Valid CreateTutorReq createTutorReq) {
        Long userLectureId = userLectureService.createTutor(id, createTutorReq );

        return SuccessResponse.successResponse(userLectureId + "is created");
    }
    @ApiOperation(value = "강사 선정",notes = "id에 강의 id를 selectTutorReq 에는 강사 id와 직업을 넣으면 해당 강사가 강의에 배정됩니다. ")
    @PatchMapping("/lectures/{id}")
    public SuccessResponse selectTutor(
            @PathVariable("id") Long id,
            @RequestBody @Valid SelectTutorReq selectTutorReq) {
        String result = userLectureService.selectTutor(id, selectTutorReq);
        return SuccessResponse.successResponse("change to"+result);
    }
    @ApiOperation(value="강사 신청 취소",notes = "id에 해당하는 신청내역을 삭제합니다.")
    @DeleteMapping("/lectures/{id}")
    public SuccessResponse deleteUserLecture(@PathVariable("id") Long id){
        Long deleteLectureID = userLectureService.deleteLecture(id);
        return SuccessResponse.successResponse("deleted"+deleteLectureID);
    }

}
