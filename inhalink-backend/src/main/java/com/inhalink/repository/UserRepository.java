package com.inhalink.repository;

import com.inhalink.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // 나중에 이메일로 회원을 찾고 싶다면 아래 한 줄로 기능 추가 가능
    // Optional<User> findByEmail(String email);
}