package com.startupsphere.capstone.controller;

import com.startupsphere.capstone.dtos.BookmarksRequest;
import com.startupsphere.capstone.entity.Bookmarks;
import com.startupsphere.capstone.entity.Investor;
import com.startupsphere.capstone.entity.Startup;
import com.startupsphere.capstone.entity.User;
import com.startupsphere.capstone.repository.InvestorRepository;
import com.startupsphere.capstone.repository.StartupRepository;
import com.startupsphere.capstone.repository.UserRepository;
import com.startupsphere.capstone.service.BookmarksService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarksController {

    private final BookmarksService bookmarksService;
    private final UserRepository userRepository;
    private final StartupRepository startupRepository;
    private final InvestorRepository investorRepository;

    @Autowired
    public BookmarksController(BookmarksService bookmarksService,
            UserRepository userRepository,
            StartupRepository startupRepository,
            InvestorRepository investorRepository) {
        this.bookmarksService = bookmarksService;
        this.userRepository = userRepository;
        this.startupRepository = startupRepository;
        this.investorRepository = investorRepository;
    }

    @PostMapping
    public ResponseEntity<Bookmarks> createBookmark(@RequestBody BookmarksRequest request) {
        // Get the currently authenticated user
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    
        Startup startup = null;
        Investor investor = null;
    
        // Only look up startup if ID is provided
        if (request.getStartupId() != null) {
            startup = startupRepository.findById(request.getStartupId())
                    .orElseThrow(() -> new RuntimeException("Startup not found"));
        }
    
        // Only look up investor if ID is provided
        if (request.getInvestorId() != null) {
            investor = investorRepository.findById(request.getInvestorId())
                    .orElseThrow(() -> new RuntimeException("Investor not found"));
        }
    
        // Ensure at least one of startup or investor is present
        if (startup == null && investor == null) {
            return ResponseEntity.badRequest().build(); // or return a custom error message
        }
    
        // Create and save the bookmark
        Bookmarks bookmark = new Bookmarks();
        bookmark.setUser(user);
        bookmark.setStartup(startup);
        bookmark.setInvestor(investor);
    
        return ResponseEntity.ok(bookmarksService.createBookmark(bookmark));
    }
    
    @GetMapping
    public ResponseEntity<List<Bookmarks>> getAllBookmarks() {
        return ResponseEntity.ok(bookmarksService.getAllBookmarks());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookmark(@PathVariable Long id) {
        bookmarksService.deleteBookmark(id);
        return ResponseEntity.noContent().build();
    }
}