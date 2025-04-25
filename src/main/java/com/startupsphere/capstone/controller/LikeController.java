package com.startupsphere.capstone.controller;

import com.startupsphere.capstone.entity.LikeEntity;
import com.startupsphere.capstone.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    // Create
    @PostMapping
    public ResponseEntity<LikeEntity> createLike(@RequestBody LikeEntity like) {
        LikeEntity createdLike = likeService.createLike(like);
        return ResponseEntity.status(201).body(createdLike);
    }

    // Read all
    @GetMapping
    public ResponseEntity<List<LikeEntity>> getAllLikes() {
        List<LikeEntity> likes = likeService.getAllLikes();
        return ResponseEntity.ok(likes);
    }

    // Read by ID
    @GetMapping("/{id}")
    public ResponseEntity<LikeEntity> getLikeById(@PathVariable Integer id) {
        Optional<LikeEntity> like = likeService.getLikeById(id);
        return like.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.status(404).build());
    }

    // Read by userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LikeEntity>> getLikesByUserId(@PathVariable Integer userId) {
        List<LikeEntity> likes = likeService.getLikesByUserId(userId);
        return ResponseEntity.ok(likes);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<LikeEntity> updateLike(@PathVariable Integer id, @RequestBody LikeEntity like) {
        LikeEntity updatedLike = likeService.updateLike(id, like);
        return ResponseEntity.ok(updatedLike);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLike(@PathVariable Integer id) {
        likeService.deleteLike(id);
        return ResponseEntity.status(204).build();
    }
}