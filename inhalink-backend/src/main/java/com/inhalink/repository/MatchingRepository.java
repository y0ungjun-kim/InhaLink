package com.inhalink.repository;

import com.inhalink.domain.MatchingRequest;
import com.inhalink.domain.enums.MatchingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchingRepository extends JpaRepository<MatchingRequest, String> {

    Optional<MatchingRequest> findFirstByStatusAndStudentIdNot(MatchingStatus status, String excludeStudentId);
}
