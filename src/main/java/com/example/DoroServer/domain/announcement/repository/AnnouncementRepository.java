package com.example.DoroServer.domain.announcement.repository;

import com.example.DoroServer.domain.announcement.dto.AnnouncementRes;
import com.example.DoroServer.domain.announcement.entity.Announcement;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    //AnnouncementRes 객체 전부를 조회하는 메서드
    @Query("select new com.example.DoroServer.domain.announcement.dto.AnnouncementRes(a.id, a.title, a.body, a.picture,a.createdAt,a.lastModifiedAt) from Announcement a")
    List<AnnouncementRes> findAllRes();

}
