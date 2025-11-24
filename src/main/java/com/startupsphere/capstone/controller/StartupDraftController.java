package com.startupsphere.capstone.controller;

import com.startupsphere.capstone.dtos.DraftRequest;
import com.startupsphere.capstone.dtos.DraftResponse;
import com.startupsphere.capstone.entity.User;
import com.startupsphere.capstone.security.CurrentUser;
import com.startupsphere.capstone.service.StartupDraftService;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/startups/draft")
public class StartupDraftController {

    private final StartupDraftService service;

    public StartupDraftController(StartupDraftService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> save(@CurrentUser User user, @Valid @RequestBody DraftRequest req) {
        if (user == null) return ResponseEntity.status(401).build();
        service.saveDraft(user.getId(), req);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<DraftResponse> get(@CurrentUser User user) {
        if (user == null) return ResponseEntity.status(401).build();

        DraftResponse draft = service.getDraft(user.getId());
        return draft != null 
            ? ResponseEntity.ok(draft) 
            : ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@CurrentUser User user) {
        if (user == null) return ResponseEntity.status(401).build();
        service.deleteDraft(user.getId());
        return ResponseEntity.ok().build();
    }
}