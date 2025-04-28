package com.startupsphere.capstone.repository;


import com.startupsphere.capstone.entity.Like;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserIdAndStartupId(Integer userId, Long startupId);

    // Corrected method to reference the 'investorId' field in the 'Investor' entity
    Optional<Like> findByUserIdAndInvestor_InvestorId(Integer userId, Integer investorId);

}