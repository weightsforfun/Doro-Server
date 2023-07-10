package com.example.DoroServer.domain.announcement.service;

import com.example.DoroServer.domain.announcement.dto.AnnouncementMultipartReq;
import com.example.DoroServer.domain.announcement.dto.AnnouncementReq;
import com.example.DoroServer.domain.announcement.dto.AnnouncementRes;
import com.example.DoroServer.domain.announcement.entity.Announcement;
import com.example.DoroServer.domain.announcement.repository.AnnouncementRepository;
import com.example.DoroServer.global.exception.BaseException;
import com.example.DoroServer.global.exception.Code;
import com.example.DoroServer.global.s3.AwsS3Service;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final AwsS3Service awsS3Service;

    // AnnouncementRes Dto객체 단일 조회
    public AnnouncementRes findById(Long id) {
        Optional<Announcement> announcement = announcementRepository.findById(id);
        return announcement.orElseThrow(() -> {
            log.info("Announcement를 찾을 수 없습니다. id = {}", id);
            throw new BaseException(Code.ANNOUNCEMENT_NOT_FOUND);
        }).toRes();
    }

    // AnnouncementRes Dto객체 전부 조회
    public List<AnnouncementRes> findAllAnnouncements(Pageable pageable) {
        return announcementRepository.findAll(pageable).stream()
                .map(Announcement::toRes)
                .collect(Collectors.toList());
    }

    // Announcement 생성 메소드
    @Transactional
    public Long createAnnouncement(AnnouncementMultipartReq announcementMultipartReq) {
        String imgUrl = null;
        MultipartFile picture = announcementMultipartReq.getPicture();
        try {
            if (picture != null) {
                imgUrl = awsS3Service.upload(picture, "announcement");
            }
        } catch (IOException e) {
            throw new BaseException(Code.UPLOAD_FAILED);
        }
        Announcement announcement = Announcement.builder()
            .title(announcementMultipartReq.getTitle())
            .body(announcementMultipartReq.getBody())
            .writer(announcementMultipartReq.getWriter())
            .picture(imgUrl)
            .build();
        announcementRepository.save(announcement);
        return announcement.getId();
    }

    // Announcement 수정 메소드
    @Transactional
    public Long updateAnnouncement(Long id, AnnouncementMultipartReq announcementMultipartReq) {
        Announcement announcement = announcementRepository.findById(id).orElseThrow(() -> {
            log.info("업데이트 하려는 공지를 찾을 수 없습니다. id = {}", id);
            return new BaseException(Code.ANNOUNCEMENT_NOT_FOUND);
        });

        if(announcement.getPicture() != null){
            awsS3Service.deleteImage(announcement.getPicture());
        }

        String imgUrl = null;
        MultipartFile picture = announcementMultipartReq.getPicture();
        try {
            if (picture != null) {
                imgUrl = awsS3Service.upload(picture, "announcement");
            }
        } catch (IOException e) {
            throw new BaseException(Code.UPLOAD_FAILED);
        }
        announcement.update(announcementMultipartReq, imgUrl);
        return announcement.getId();
    }

    // Announcement 수정 메서드
    @Transactional
    public void deleteAnnouncement(Long id) {
        Optional<Announcement> announcement = announcementRepository.findById(id);
        announcement.orElseThrow(() -> {
            log.info("delete 하려는 Announcement를 찾을 수 없습니다. id = {}", id);
            return new BaseException(Code.ANNOUNCEMENT_NOT_FOUND);
        });
        announcementRepository.deleteById(id);
    }



}
