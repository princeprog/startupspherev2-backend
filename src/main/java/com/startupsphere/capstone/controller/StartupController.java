package com.startupsphere.capstone.controller;

import com.startupsphere.capstone.entity.Startup;
import com.startupsphere.capstone.repository.StartupRepository;
import com.startupsphere.capstone.service.StartupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/startups")
public class StartupController {

    private final StartupService startupService;
    private final StartupRepository startupRepository;

    public StartupController(StartupService startupService, StartupRepository startupRepository) {
        this.startupService = startupService;
        this.startupRepository = startupRepository;
    }

    @PostMapping
    public ResponseEntity<Startup> createStartup(@RequestBody Startup startup) {
        Startup createdStartup = startupService.createStartup(startup);
        return ResponseEntity.ok(createdStartup);
    }

    @GetMapping
    public ResponseEntity<List<Startup>> getAllStartups() {
        List<Startup> startups = startupService.getAllStartups();
        return ResponseEntity.ok(startups);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Startup>> searchStartups(@RequestParam String query) {
        List<Startup> startups = startupService.searchStartups(query);
        return ResponseEntity.ok(startups);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Startup> getStartupById(@PathVariable Long id) {
        return startupService.getStartupById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Startup> updateStartup(@PathVariable Long id, @RequestBody Startup updatedStartup) {
        try {
            Startup updated = startupService.updateStartup(id, updatedStartup);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStartup(@PathVariable Long id) {
        try {
            startupService.deleteStartup(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/views")
    public ResponseEntity<Integer> getViewsByStartupId(@PathVariable Long id) {
        Optional<Startup> optionalStartup = startupRepository.findById(id);
        if (optionalStartup.isPresent()) {
            Startup startup = optionalStartup.get();
            return ResponseEntity.ok(startup.getViewsCount());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Increment the views count for a specific startup
    @PutMapping("/{id}/increment-views")
    public ResponseEntity<Integer> incrementViews(@PathVariable Long id) {
        Optional<Startup> optionalStartup = startupRepository.findById(id);
        if (optionalStartup.isPresent()) {
            Startup startup = optionalStartup.get();
            startup.setViewsCount(startup.getViewsCount() + 1);
            startupRepository.save(startup); // Save updated startup
            return ResponseEntity.ok(startup.getViewsCount());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}