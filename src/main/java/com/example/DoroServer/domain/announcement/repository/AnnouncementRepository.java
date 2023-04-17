package com.example.DoroServer.domain.announcement.repository;

import com.example.DoroServer.domain.announcement.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

}
