package com.startupsphere.capstone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.startupsphere.capstone.entity.Startup;

public interface StartupRepository extends JpaRepository<Startup, Long> {
    List<Startup> findByCompanyNameContainingIgnoreCase(String query);
}
