package com.example.DoroServer.domain.base;

import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseEntity{

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt; // 생성 날짜

    @LastModifiedDate
    private LocalDateTime lastModifiedAt; // 수정 날짜
}
