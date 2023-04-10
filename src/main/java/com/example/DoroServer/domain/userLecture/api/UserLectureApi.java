package com.example.DoroServer.domain.userLecture.api;

import com.example.DoroServer.domain.userLecture.dto.SelectTutorReq;
import com.example.DoroServer.domain.userLecture.dto.CreateTutorReq;
import com.example.DoroServer.global.common.SuccessResponse;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/users-lectures")
public class UserLectureApi {

    @GetMapping("/lectures/{id}")
    public SuccessResponse findAllTutors(@PathVariable("id") Long id) {
        //fetchjoin 필요
        return SuccessResponse.successResponse(id + "th tutors");
    }

    @GetMapping("/users/{id}")
    public SuccessResponse findMyLectures(@PathVariable("id") Long id) {
        //fetchjoin필요
        return SuccessResponse.successResponse(id + "user's lectures");
    }

    @PostMapping("/lectures/{id}")
    //토큰에서 user id 갖고오고 신청대기, role선택해서 저장
    //이미 신청한 user  일시 예외 출력
    public SuccessResponse createTutor(
            @PathVariable("id") String id,
            @RequestBody CreateTutorReq createTutorReq) {
        return SuccessResponse.successResponse(id + "added as"+createTutorReq.getTutorRole() );
    }

    @PatchMapping("/lectures/{id}")
    public SuccessResponse selectTutor(
            @PathVariable("id") String id,
            @RequestBody SelectTutorReq selectTutor) {
        //선정된 유저, 역할 갖고와서 서비스계층에서 변경
        //sql로 선정됐거나 id 해당 유저들 다 갖고와서 일단 초기화 시키고
        //id 선택된 애들만 바꾸는 식으로 하는게 좋을듯
        return SuccessResponse.successResponse(
                id + "th main patched"+
                "assignedUser:"+selectTutor.getAssignedUser()+
                "as" + selectTutor.getTutorRole());
    }

}
