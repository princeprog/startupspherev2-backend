package com.startupsphere.capstone.controller;

import com.startupsphere.capstone.dtos.LikeRequest;
import com.startupsphere.capstone.entity.Investor;
import com.startupsphere.capstone.entity.Like;
import com.startupsphere.capstone.entity.Startup;
import com.startupsphere.capstone.entity.User;
import com.startupsphere.capstone.repository.InvestorRepository;
import com.startupsphere.capstone.repository.StartupRepository;
import com.startupsphere.capstone.repository.UserRepository;
import com.startupsphere.capstone.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StartupRepository startupRepository;

    @Autowired
    private InvestorRepository investorRepository;

    @PostMapping
    public ResponseEntity<Object> toggleLike(@RequestBody LikeRequest likeRequest) {
        likeRequest.validate();

        Integer userId = likeRequest.getUserId();
        Long startupId = likeRequest.getStartupId();
        Integer investorId = likeRequest.getInvestorId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Startup startup = null;
        if (startupId != null) {
            startup = startupRepository.findById(startupId)
                    .orElseThrow(() -> new RuntimeException("Startup not found"));
        }

        Investor investor = null;
        if (investorId != null) {
            investor = investorRepository.findById(investorId)
                    .orElseThrow(() -> new RuntimeException("Investor not found"));
        }

        Like like = new Like();
        like.setUser(user);
        like.setStartup(startup);
        like.setInvestor(investor);

        // Toggle the like (like or unlike)
        String result = likeService.toggleLike(like);

        // Return the result as a JSON response
        return ResponseEntity.ok(Map.of("message", result));
    }

    @GetMapping
    public List<Like> getAllLikes() {
        return likeService.getAllLikes();
    }

    @GetMapping("/{id}")
    public Optional<Like> getLikeById(@PathVariable Long id) {
        return likeService.getLikeById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteLike(@PathVariable Long id) {
        likeService.deleteLike(id);
    }
}