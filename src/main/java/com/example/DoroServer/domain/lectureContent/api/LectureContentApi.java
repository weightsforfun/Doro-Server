package com.example.DoroServer.domain.lectureContent.api;


import com.example.DoroServer.domain.lectureContent.dto.LectureContentDto;
import com.example.DoroServer.domain.lectureContent.service.LectureContentService;
import com.example.DoroServer.global.common.SuccessResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lecture-contents")
@RequiredArgsConstructor
public class LectureContentApi {
    private final LectureContentService lectureContentService;
    @PostMapping()
    public SuccessResponse createLectureContent(@RequestBody LectureContentDto lectureContentDto){
        Long lectureId = lectureContentService.createLecture(lectureContentDto);
        return SuccessResponse.successResponse(lectureId);
    }
    @GetMapping()
    public SuccessResponse findAllLectureContents(){
        List<LectureContentDto> allLectureContents = lectureContentService.findAllLectureContents();
        return SuccessResponse.successResponse(allLectureContents);
    }

}
