package com.example.DoroServer.domain.lectureContent.api;


import com.example.DoroServer.domain.lectureContent.dto.LectureContentDto;
import com.example.DoroServer.domain.lectureContent.dto.UpdateLectureContentReq;
import com.example.DoroServer.domain.lectureContent.service.LectureContentService;
import com.example.DoroServer.global.common.SuccessResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/lecture-contents")
@RequiredArgsConstructor
@Validated
public class LectureContentApi {
    private final LectureContentService lectureContentService;
    @PostMapping()
    public SuccessResponse createLectureContent(@RequestBody @Valid LectureContentDto lectureContentDto){
        Long lectureId = lectureContentService.createLecture(lectureContentDto);
        return SuccessResponse.successResponse(lectureId);
    }
    @GetMapping()
    public SuccessResponse findAllLectureContents(){
        List<LectureContentDto> allLectureContents = lectureContentService.findAllLectureContents();
        return SuccessResponse.successResponse(allLectureContents);
    }
    @PatchMapping("/{id}")
    public SuccessResponse updateLectureContent(
            @PathVariable("id") Long id,
            @RequestBody UpdateLectureContentReq updateLectureContentReq){
        Long updateLectureContentId = lectureContentService.updateLectureContent(id, updateLectureContentReq);
        return SuccessResponse.successResponse(updateLectureContentId + "is updated");
    }

}
