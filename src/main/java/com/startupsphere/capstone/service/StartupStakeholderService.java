package com.startupsphere.capstone.service;

import com.startupsphere.capstone.dtos.StartupStakeholderRequest;
import com.startupsphere.capstone.entity.Stakeholder;
import com.startupsphere.capstone.entity.Startup;
import com.startupsphere.capstone.entity.StartupStakeholder;
import com.startupsphere.capstone.repository.StakeholderRepository;
import com.startupsphere.capstone.repository.StartupRepository;
import com.startupsphere.capstone.repository.StartupStakeholderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StartupStakeholderService {

    private final StartupStakeholderRepository repository;

    @Autowired
    StartupRepository srepo;

    @Autowired
    StakeholderRepository strepo;

    @Autowired
    public StartupStakeholderService(StartupStakeholderRepository repository) {
        this.repository = repository;
    }

    public List<StartupStakeholder> findAll() {
        return repository.findAll();
    }

    public Optional<StartupStakeholder> findById(Long id) {
        return repository.findById(id);
    }

    public StartupStakeholder save(StartupStakeholderRequest request) {
        StartupStakeholder startupStakeholder = new StartupStakeholder();
        Startup startup = srepo.findById(request.getStartupId())
                .orElseThrow(() -> new IllegalArgumentException("Startup not found"));
        startupStakeholder.setStartup(startup);
        Stakeholder stakeholder = strepo.findById(request.getStakeholderId())
                .orElseThrow(() -> new IllegalArgumentException("Stakeholder not found"));
        startupStakeholder.setStakeholder(stakeholder);
        startupStakeholder.setRole(request.getRole());
        startupStakeholder.setStatus(request.getStatus());
        return repository.save(startupStakeholder);
    }

    @Transactional
    public ResponseEntity<StartupStakeholder> updateStartupStakeholder(Long id, StartupStakeholderRequest request) {
        return repository.findById(id)
                .map(existing -> {
                    // Update stakeholder if changed
                    if (request.getStakeholderId() != null && !request.getStakeholderId().equals(existing.getStakeholder().getId())) {
                        Stakeholder stakeholder = strepo.findById(request.getStakeholderId())
                                .orElseThrow(() -> new IllegalArgumentException("Stakeholder not found"));
                        existing.setStakeholder(stakeholder);
                    }

                    // Update startup if changed
                    if (request.getStartupId() != null && !request.getStartupId().equals(existing.getStartup().getId())) {
                        Startup startup = srepo.findById(request.getStartupId())
                                .orElseThrow(() -> new IllegalArgumentException("Startup not found"));
                        existing.setStartup(startup);
                    }

                    // Update other fields
                    existing.setRole(request.getRole());
                    existing.setStatus(request.getStatus());

                    return ResponseEntity.ok(repository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public List<StartupStakeholder> findByStartupId(Long startupId) {
        return repository.findByStartupId(startupId);
    }

    public List<com.startupsphere.capstone.dtos.StakeholderInfoDTO> findStakeholderByStartupId(Long startupId){
        return repository.findStakeholderInfoByStartupId(startupId);
    }
}