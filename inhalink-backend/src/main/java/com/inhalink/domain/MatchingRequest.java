package com.inhalink.domain;

import com.inhalink.domain.enums.MatchingStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "matching_requests")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingRequest extends BaseTimeEntity {

    @Id
    @Column(name = "student_id", length = 20)
    private String studentId; // 요청자 학번 (PK, 1인 1개만 존재)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", insertable = false, updatable = false)
    private User requester;

    @Column(length = 20)
    private String matchedWithId; // 매칭된 상대방 학번 (MATCHED 상태일 때만 세팅)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MatchingStatus status;

    public static MatchingRequest waiting(String studentId) {
        MatchingRequest req = new MatchingRequest();
        req.studentId = studentId;
        req.status = MatchingStatus.WAITING;
        return req;
    }

    public void matchWith(String partnerId) {
        this.matchedWithId = partnerId;
        this.status = MatchingStatus.MATCHED;
    }

    public void cancel() {
        this.status = MatchingStatus.CANCELLED;
    }
}
