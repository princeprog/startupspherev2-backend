package com.startupsphere.capstone.repository;

import com.startupsphere.capstone.entity.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LikeRepository extends JpaRepository<LikeEntity, Integer> {
    List<LikeEntity> findByUserId(Integer userId);
}