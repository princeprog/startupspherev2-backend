package com.startupsphere.capstone.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.startupsphere.capstone.entity.Investor;

@Repository
public interface InvestorRepository extends CrudRepository<Investor, Integer> {
    
}

