package com.capstone.safeGuard.repository;

import com.capstone.safeGuard.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {


    Member findByEmail(String loginEmail);

    boolean existsByEmail(String email);
    boolean existsByMemberId(String memberId);

    List<Member> findAllByFcmToken(String fcmToken);
}
