package com.startupsphere.capstone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.startupsphere.capstone.entity.Notifications;

@Repository
public interface NotificationsRepository extends JpaRepository<Notifications, Integer> {
    long countByIsViewedFalse();
    List<Notifications> findByIsViewedFalse();
    List<Notifications> findByUserIdOrderByIdDesc(Integer userId);
    List<Notifications> findByUserIdAndIsViewedFalse(Integer userId);
    long countByUserIdAndIsViewedFalse(Integer userId);
}
