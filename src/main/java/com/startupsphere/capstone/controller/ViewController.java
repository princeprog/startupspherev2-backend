package com.startupsphere.capstone.controller;

import com.startupsphere.capstone.entity.ViewEntity;
import com.startupsphere.capstone.service.ViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/views")
public class ViewController {

    @Autowired
    private ViewService viewService;

    // Create
    @PostMapping
    public ResponseEntity<ViewEntity> createView(@RequestBody ViewEntity view) {
        ViewEntity createdView = viewService.createView(view);
        return ResponseEntity.status(201).body(createdView);
    }

    // Read all
    @GetMapping
    public ResponseEntity<List<ViewEntity>> getAllViews() {
        List<ViewEntity> views = viewService.getAllViews();
        return ResponseEntity.ok(views);
    }

    // Read by ID
    @GetMapping("/{id}")
    public ResponseEntity<ViewEntity> getViewById(@PathVariable Integer id) {
        Optional<ViewEntity> view = viewService.getViewById(id);
        return view.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.status(404).build());
    }

    // Read by userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ViewEntity>> getViewsByUserId(@PathVariable Integer userId) {
        List<ViewEntity> views = viewService.getViewsByUserId(userId);
        return ResponseEntity.ok(views);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<ViewEntity> updateView(@PathVariable Integer id, @RequestBody ViewEntity view) {
        ViewEntity updatedView = viewService.updateView(id, view);
        return ResponseEntity.ok(updatedView);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteView(@PathVariable Integer id) {
        viewService.deleteView(id);
        return ResponseEntity.status(204).build();
    }
}