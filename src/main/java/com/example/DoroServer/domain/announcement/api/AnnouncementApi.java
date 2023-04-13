package com.example.DoroServer.domain.announcement.api;

import com.example.DoroServer.domain.announcement.dto.AnnouncementReq;
import com.example.DoroServer.domain.announcement.dto.AnnouncementRes;
import com.example.DoroServer.domain.announcement.service.AnnouncementService;
import com.example.DoroServer.domain.notification.dto.NotificationReq;
import com.example.DoroServer.domain.notification.service.NotificationService;
import com.example.DoroServer.global.common.SuccessResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/announcements")
@RequiredArgsConstructor
public class AnnouncementApi {

    private final AnnouncementService announcementService;
    private final NotificationService notificationService;

    @GetMapping
    public SuccessResponse findAllAnnouncement() {
        List<AnnouncementRes> announcements = announcementService.findAllAnnouncements();
        return SuccessResponse.successResponse(announcements);
    }

    @PostMapping
    public SuccessResponse createAnnouncement(@RequestBody AnnouncementReq announcementReq) {
        Long announcementId = announcementService.createAnnouncement(announcementReq);
        //todo: 사용자 토큰 멤버변수 생기면 아래거 이걸로 교체
//        notificationService.sendMessageToAll(NotificationContentReq.builder()
//                .title(announcementReq.getTitle())
//                .body(announcementReq.getBody())
//                .build());
        notificationService.sendMessageTo(NotificationReq.builder()
                .targetToken("") // 임시 토큰 나중에 사용자들에게서 직접 받을 것
                .title(announcementReq.getTitle())
                .body(announcementReq.getBody())
                .build());

        return SuccessResponse.successResponse("announcement created " + announcementId);
    }

    @GetMapping("/{id}")
    public SuccessResponse findAnnouncement(@PathVariable("id") Long id) {
        AnnouncementRes announcementRes = announcementService.findById(id);
        return SuccessResponse.successResponse(announcementRes);
    }

    @PatchMapping("/{id}")
    public SuccessResponse editAnnouncement(@PathVariable("id") Long id,
            @RequestBody AnnouncementReq announcementReq) {
        announcementService.updateAnnouncement(id, announcementReq);
        return SuccessResponse.successResponse("update complete");
    }

    @DeleteMapping("/{id}")
    public SuccessResponse deleteAnnouncement(@PathVariable("id") Long id) {
        announcementService.deleteAnnouncement(id);
        return SuccessResponse.successResponse("delete complete");
    }
}
