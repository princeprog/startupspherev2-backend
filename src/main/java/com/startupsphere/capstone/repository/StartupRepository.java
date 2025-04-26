package com.startupsphere.capstone.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.startupsphere.capstone.entity.Startup;

public interface StartupRepository extends JpaRepository<Startup,Long>  {
    
}
