package com.startupsphere.capstone.controller;

import com.startupsphere.capstone.entity.Investor;
import com.startupsphere.capstone.entity.Like;
import com.startupsphere.capstone.entity.Startup;
import com.startupsphere.capstone.entity.User;
import com.startupsphere.capstone.repository.InvestorRepository;
import com.startupsphere.capstone.repository.StartupRepository;
import com.startupsphere.capstone.repository.UserRepository;
import com.startupsphere.capstone.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public Like createLike(@RequestParam Integer userId,
                           @RequestParam Long startupId,
                           @RequestParam Integer investor_Id) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Startup startup = startupRepository.findById(startupId)
                .orElseThrow(() -> new RuntimeException("Startup not found"));
        Investor investor = investorRepository.findById(investor_Id)
                .orElseThrow(() -> new RuntimeException("Investor not found"));

        Like like = new Like();
        like.setUser(user);
        like.setStartup(startup);
        like.setInvestor(investor);

        return likeService.createLike(like);
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