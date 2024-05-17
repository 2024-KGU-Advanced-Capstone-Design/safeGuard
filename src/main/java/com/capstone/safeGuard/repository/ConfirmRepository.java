package com.capstone.safeGuard.repository;

import com.capstone.safeGuard.domain.Confirm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmRepository extends JpaRepository<Confirm, Long> {
}