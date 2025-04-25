package com.startupsphere.capstone.repository;

import com.startupsphere.capstone.entity.ViewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ViewRepository extends JpaRepository<ViewEntity, Integer> {
    List<ViewEntity> findByUserId(Integer userId);
}