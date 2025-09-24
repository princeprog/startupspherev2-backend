package com.startupsphere.capstone.repository;

import com.startupsphere.capstone.entity.Stakeholder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StakeholderRepository extends JpaRepository<Stakeholder, Long> {
    Optional<Stakeholder> findByEmail(String email);
}