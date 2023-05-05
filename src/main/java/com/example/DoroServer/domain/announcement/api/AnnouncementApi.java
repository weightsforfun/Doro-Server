package com.example.DoroServer.domain.announcement.api;

import com.example.DoroServer.domain.announcement.dto.AnnouncementReq;
import com.example.DoroServer.domain.announcement.dto.AnnouncementRes;
import com.example.DoroServer.domain.announcement.service.AnnouncementService;
import com.example.DoroServer.domain.notification.dto.NotificationContentReq;
import com.example.DoroServer.domain.notification.service.NotificationService;
import com.example.DoroServer.global.common.SuccessResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "공지📋")
@RestController
@RequestMapping("/announcements")
@RequiredArgsConstructor
public class AnnouncementApi {

    private final AnnouncementService announcementService;
    private final NotificationService notificationService;

    // 모든 공지를 찾아 반환
    @ApiOperation(value = "공지 글 전체 조회", notes = "생성되어있는 모든 공지를 조회한다.")
    @GetMapping
    public SuccessResponse findAllAnnouncement() {
        List<AnnouncementRes> announcements = announcementService.findAllAnnouncements();
        return SuccessResponse.successResponse(announcements);
    }

    // 공지 생성 후 생성 확인 알림 전송
    @ApiOperation(value = "공지 글 생성", notes = "공지 제목(title), 내용(body), 이미지(image)를 입력받아 공지를 생성한다.")
    @PostMapping
    public SuccessResponse createAnnouncement(@RequestBody @Valid AnnouncementReq announcementReq) {
        Long announcementId = announcementService.createAnnouncement(announcementReq);
        notificationService.sendNotificationToAll(NotificationContentReq.builder()
                .title("새로운 공지가 있습니다.")
                .body(announcementReq.getTitle())
                .build());

        return SuccessResponse.successResponse("announcement created " + announcementId);
    }

    // id에 해당하는 공지 하나 조회
    @ApiOperation(value = "공지 글 단일 조회", notes = "id에 해당하는 공지 글을 조회한다.")
    @GetMapping("/{id}")
    public SuccessResponse findAnnouncement(@PathVariable("id") Long id) {
        AnnouncementRes announcementRes = announcementService.findById(id);
        return SuccessResponse.successResponse(announcementRes);
    }

    // id에 해당하는 공지 수정
    @ApiOperation(value = "공지 글 수정", notes = "id에 해당하는 공지 글을 수정한다.")
    @PatchMapping("/{id}")
    public SuccessResponse editAnnouncement(@PathVariable("id") Long id,
            @RequestBody @Valid AnnouncementReq announcementReq) {
        announcementService.updateAnnouncement(id, announcementReq);
        return SuccessResponse.successResponse("update complete");
    }

    // id에 해당하는 공지 삭제
    @ApiOperation(value = "공지 글 삭제", notes = "id에 해당하는 공지글을 삭제한다.")
    @DeleteMapping("/{id}")
    public SuccessResponse deleteAnnouncement(@PathVariable("id") Long id) {
        announcementService.deleteAnnouncement(id);
        return SuccessResponse.successResponse("delete complete");
    }
}
