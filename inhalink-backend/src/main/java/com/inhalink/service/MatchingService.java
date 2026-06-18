package com.inhalink.service;

import com.inhalink.domain.MatchingRequest;
import com.inhalink.domain.User;
import com.inhalink.domain.enums.MatchingStatus;
import com.inhalink.dto.response.MatchingResponse;
import com.inhalink.exception.UserNotFoundException;
import com.inhalink.repository.MatchingRepository;
import com.inhalink.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchingService {

    private final MatchingRepository matchingRepository;
    private final UserRepository userRepository;

    @Transactional
    public MatchingResponse joinOrCheck(String studentId) {
        userRepository.findById(studentId).orElseThrow(UserNotFoundException::new);

        // 이미 매칭 요청이 있다면 현재 상태 그대로 반환
        Optional<MatchingRequest> existing = matchingRepository.findById(studentId);
        if (existing.isPresent()) {
            MatchingRequest req = existing.get();
            if (req.getStatus() == MatchingStatus.MATCHED) {
                User partner = userRepository.findById(req.getMatchedWithId())
                        .orElseThrow(UserNotFoundException::new);
                return MatchingResponse.matched(partner);
            }
            if (req.getStatus() == MatchingStatus.WAITING) {
                return MatchingResponse.waiting();
            }
        }

        // 대기 중인 다른 유저 탐색
        Optional<MatchingRequest> waitingPartner = matchingRepository
                .findFirstByStatusAndStudentIdNot(MatchingStatus.WAITING, studentId);

        if (waitingPartner.isPresent()) {
            MatchingRequest partnerReq = waitingPartner.get();
            partnerReq.matchWith(studentId);

            MatchingRequest myReq = MatchingRequest.waiting(studentId);
            myReq.matchWith(partnerReq.getStudentId());
            matchingRepository.save(myReq);

            User partner = userRepository.findById(partnerReq.getStudentId())
                    .orElseThrow(UserNotFoundException::new);
            return MatchingResponse.matched(partner);
        }

        // 대기열에 추가
        MatchingRequest newReq = MatchingRequest.waiting(studentId);
        matchingRepository.save(newReq);
        return MatchingResponse.waiting();
    }

    @Transactional(readOnly = true)
    public MatchingResponse getStatus(String studentId) {
        MatchingRequest req = matchingRepository.findById(studentId)
                .orElse(null);

        if (req == null || req.getStatus() == MatchingStatus.CANCELLED) {
            return MatchingResponse.cancelled();
        }
        if (req.getStatus() == MatchingStatus.MATCHED) {
            User partner = userRepository.findById(req.getMatchedWithId())
                    .orElseThrow(UserNotFoundException::new);
            return MatchingResponse.matched(partner);
        }
        return MatchingResponse.waiting();
    }

    @Transactional
    public void cancel(String studentId) {
        matchingRepository.findById(studentId).ifPresent(req -> {
            req.cancel();
        });
    }
}
