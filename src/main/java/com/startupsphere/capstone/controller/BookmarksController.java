package com.startupsphere.capstone.controller;

import com.startupsphere.capstone.entity.Bookmarks;
import com.startupsphere.capstone.entity.Investor;
import com.startupsphere.capstone.entity.Startup;
import com.startupsphere.capstone.entity.User;
import com.startupsphere.capstone.repository.InvestorRepository;
import com.startupsphere.capstone.repository.StartupRepository;
import com.startupsphere.capstone.repository.UserRepository;
import com.startupsphere.capstone.service.BookmarksService;
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
    public Bookmarks createBookmark(@RequestParam Integer userId,
                                    @RequestParam Long startupId,
                                    @RequestParam Integer investor_Id) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Startup startup = startupRepository.findById(startupId)
                .orElseThrow(() -> new RuntimeException("Startup not found"));
        Investor investor = investorRepository.findById(investor_Id)
                .orElseThrow(() -> new RuntimeException("Investor not found"));

        Bookmarks bookmark = new Bookmarks();
        bookmark.setUser(user);
        bookmark.setStartup(startup);
        bookmark.setInvestor(investor);

        return bookmarksService.createBookmark(bookmark);
    }

    @GetMapping
    public List<Bookmarks> getAllBookmarks() {
        return bookmarksService.getAllBookmarks();
    }

    @DeleteMapping("/{id}")
    public void deleteBookmark(@PathVariable Long id) {
        bookmarksService.deleteBookmark(id);
    }
}
