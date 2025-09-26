package com.startupsphere.capstone.service;

import com.startupsphere.capstone.entity.Stakeholder;
import com.startupsphere.capstone.repository.StakeholderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public ResponseEntity<Stakeholder> updateStakeholder(Long id, Stakeholder updatedStakeholder) {
        return repository.findById(id)
                .map(existingStakeholder -> {
                    existingStakeholder.setName(updatedStakeholder.getName());
                    existingStakeholder.setEmail(updatedStakeholder.getEmail());
                    existingStakeholder.setPhoneNumber(updatedStakeholder.getPhoneNumber());
                    existingStakeholder.setRegion(updatedStakeholder.getRegion());
                    existingStakeholder.setCity(updatedStakeholder.getCity());
                    existingStakeholder.setBarangay(updatedStakeholder.getBarangay());
                    existingStakeholder.setStreet(updatedStakeholder.getStreet());
                    existingStakeholder.setPostalCode(updatedStakeholder.getPostalCode());
                    existingStakeholder.setFacebook(updatedStakeholder.getFacebook());
                    existingStakeholder.setLinkedIn(updatedStakeholder.getLinkedIn());
                    existingStakeholder.setLocationLat(updatedStakeholder.getLocationLat());
                    existingStakeholder.setLocationLng(updatedStakeholder.getLocationLng());
                    existingStakeholder.setLocationName(updatedStakeholder.getLocationName());

                    Stakeholder saved = repository.save(existingStakeholder);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}