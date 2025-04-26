package com.startupsphere.capstone.controller;

import com.startupsphere.capstone.entity.Investor;
import com.startupsphere.capstone.entity.User;
import com.startupsphere.capstone.service.InvestorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/investors")
public class InvestorController {

    private final InvestorService investorService;

    public InvestorController(InvestorService investorService) {
        this.investorService = investorService;
    }

    @GetMapping
    public ResponseEntity<List<Investor>> getAllInvestors() {
        return ResponseEntity.ok(investorService.getAllInvestors());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Investor>> searchInvestors(@RequestParam String query) {
        List<Investor> investors = investorService.searchInvestors(query);
        return ResponseEntity.ok(investors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Investor> getInvestorById(@PathVariable Integer id) {
        return ResponseEntity.ok(investorService.getInvestorById(id));
    }

    @PostMapping
    public ResponseEntity<Investor> createInvestor(@RequestBody Investor investor) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal(); // Authenticated user from Spring Security

        investor.setUserId(currentUser); // Link investor to the current user

        return ResponseEntity.ok(investorService.createInvestor(investor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Investor> updateInvestor(@PathVariable Integer id, @RequestBody Investor investor) {
        return ResponseEntity.ok(investorService.updateInvestor(id, investor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInvestor(@PathVariable Integer id) {
        investorService.deleteInvestor(id);
        return ResponseEntity.ok("Investor deleted successfully.");
    }
}