package com.inhalink.repository;

import com.inhalink.domain.EmailVerification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVerificationRepository extends CrudRepository<EmailVerification, String> {
}