package com.startupsphere.capstone.service;

import com.startupsphere.capstone.entity.Stakeholder;
import com.startupsphere.capstone.repository.StakeholderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StakeholderService {

    private final StakeholderRepository repository;

    @Autowired
    public StakeholderService(StakeholderRepository repository) {
        this.repository = repository;
    }

    public List<Stakeholder> findAll() {
        return repository.findAll();
    }

    public Optional<Stakeholder> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<Stakeholder> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public Stakeholder save(Stakeholder stakeholder) {
        return repository.save(stakeholder);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}