package com.inhalink.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 자식 엔티티에게 이 클래스의 필드(createdAt, updatedAt)를 컬럼으로 물려줌
@EntityListeners(AuditingEntityListener.class) // 스프링이 이 엔티티의 변화를 감지해서 시간을 자동으로 update
public abstract class BaseTimeEntity {

    @CreatedDate
    @Column(updatable = false) // 생성 시간은 한 번 세팅되면 수정되지 않도록 막기
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt; // 마지막 수정 시간
}