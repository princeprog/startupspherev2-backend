package com.startupsphere.capstone.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.startupsphere.capstone.entity.Startup;
import org.springframework.data.repository.query.Param;

public interface StartupRepository extends JpaRepository<Startup, Long> {
    List<Startup> findByCompanyNameContainingIgnoreCase(String query);

    List<Startup> findByUser_Id(Integer userId);

    Optional<Startup> findByContactEmailAndVerificationCode(String contactEmail, String verificationCode);

    boolean existsByContactEmailAndEmailVerifiedTrue(String contactEmail);

    List<Startup> findByStatus(String status);

    @Query("SELECT s FROM Startup s WHERE s.emailVerified = true")
    List<Startup> findAllVerifiedEmailStartups();

    @Query("SELECT s FROM Startup s WHERE s.status = 'Approved'")
    List<Startup> findAllApprovedStartups();

    @Query("SELECT s FROM Startup s WHERE " +
            "(:industry IS NULL OR s.industry = :industry) AND " +
            "(:status IS NULL OR s.status = :status) AND " +
            "(:region IS NULL OR s.region = :region) AND " +
            "(:search IS NULL OR LOWER(s.companyName) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(s.companyDescription) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
            "(:startDate IS NULL OR s.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR s.createdAt <= :endDate)")
    List<Startup> findStartupsWithFilters(
            @Param("industry") String industry,
            @Param("status") String status,
            @Param("region") String region,
            @Param("search") String search,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT s FROM Startup s WHERE s.lastUpdated < :threshold AND s.emailVerified = true")
    List<Startup> findStartupsNotUpdatedSince(LocalDateTime threshold);
}