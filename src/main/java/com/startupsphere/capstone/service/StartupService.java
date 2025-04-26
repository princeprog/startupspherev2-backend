package com.startupsphere.capstone.service;

import com.startupsphere.capstone.entity.Startup;
import com.startupsphere.capstone.repository.StartupRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StartupService {

    private final StartupRepository startupRepository;

    public StartupService(StartupRepository startupRepository) {
        this.startupRepository = startupRepository;
    }

    public Startup createStartup(Startup startup) {
        return startupRepository.save(startup);
    }

    public List<Startup> getAllStartups() {
        return startupRepository.findAll();
    }

    public Optional<Startup> getStartupById(Long id) {
        return startupRepository.findById(id);
    }

    public Startup updateStartup(Long id, Startup updatedStartup) {
        return startupRepository.findById(id)
                .map(existingStartup -> {
                    updatedStartup.setId(existingStartup.getId()); // Ensure the ID remains the same
                    return startupRepository.save(updatedStartup);
                })
                .orElseThrow(() -> new RuntimeException("Startup with ID " + id + " not found"));
    }

    public void deleteStartup(Long id) {
        if (startupRepository.existsById(id)) {
            startupRepository.deleteById(id);
        } else {
            throw new RuntimeException("Startup with ID " + id + " not found");
        }
    }

    public List<Startup> searchStartups(String query) {
        return startupRepository.findByCompanyNameContainingIgnoreCase(query);
    }
}