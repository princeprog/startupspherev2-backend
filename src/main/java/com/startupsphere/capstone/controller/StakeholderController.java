package com.startupsphere.capstone.controller;

import com.startupsphere.capstone.entity.Stakeholder;
import com.startupsphere.capstone.service.StakeholderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stakeholders")
public class StakeholderController {

    private final StakeholderService service;

    @Autowired
    public StakeholderController(StakeholderService service) {
        this.service = service;
    }

    @GetMapping
    public List<Stakeholder> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stakeholder> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Stakeholder create(@RequestBody Stakeholder stakeholder) {
        return service.save(stakeholder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Stakeholder> updateStakeholder(@PathVariable Long id, @RequestBody Stakeholder stakeholder) {
        return service.updateStakeholder(id, stakeholder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.findById(id)
                .map(stakeholder -> {
                    service.deleteById(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}