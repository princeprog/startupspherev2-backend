package com.startupsphere.capstone.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.startupsphere.capstone.entity.Startup;

public interface StartupRepository extends JpaRepository<Startup, Long> {
    List<Startup> findByCompanyNameContainingIgnoreCase(String query);

    List<Startup> findByUser_Id(Integer userId);

    Optional<Startup> findByContactEmailAndVerificationCode(String contactEmail, String verificationCode);

    boolean existsByContactEmailAndEmailVerifiedTrue(String contactEmail);
}