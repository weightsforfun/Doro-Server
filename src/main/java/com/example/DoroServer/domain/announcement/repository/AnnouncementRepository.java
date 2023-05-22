package com.example.DoroServer.domain.announcement.repository;

import com.example.DoroServer.domain.announcement.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    @Transactional
    @Modifying
    @Query(value = "update Announcement a set a.picture = :picture where a.id = :announcementId")
    void updateAnnouncementImgById(@Param("announcementId") Long announcementId, @Param("picture") String picture);
}
