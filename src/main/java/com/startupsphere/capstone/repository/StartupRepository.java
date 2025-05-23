package com.startupsphere.capstone.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.startupsphere.capstone.entity.Startup;

public interface StartupRepository extends JpaRepository<Startup, Long> {
    List<Startup> findByCompanyNameContainingIgnoreCase(String query);

    List<Startup> findByUser_Id(Integer userId);

    Optional<Startup> findByContactEmailAndVerificationCode(String contactEmail, String verificationCode);

    boolean existsByContactEmailAndEmailVerifiedTrue(String contactEmail);

    @Query("SELECT s FROM Startup s WHERE s.emailVerified = true")
    List<Startup> findAllVerifiedEmailStartups();

    @Query("SELECT s FROM Startup s WHERE s.status = 'Approved'")
    List<Startup> findAllApprovedStartups();

    @Query("SELECT s FROM Startup s WHERE s.lastUpdated < :threshold AND s.emailVerified = true")
    List<Startup> findStartupsNotUpdatedSince(LocalDateTime threshold);
}