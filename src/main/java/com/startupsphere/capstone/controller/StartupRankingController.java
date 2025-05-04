package com.startupsphere.capstone.controller;

import com.startupsphere.capstone.entity.Startup;
import com.startupsphere.capstone.repository.StartupRepository;
import com.startupsphere.capstone.service.StartupRankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rankings")
public class StartupRankingController {

    @Autowired
    private StartupRepository startupRepository;

    @Autowired
    private StartupRankingService rankingService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getRankedStartups(
            @RequestParam(required = false) String industry,
            @RequestParam(required = false, defaultValue = "overall") String metric) {
        
        List<Startup> allStartups = startupRepository.findAll();
        List<Startup> rankedStartups;
        
        if (industry != null && !industry.trim().isEmpty() && !industry.equalsIgnoreCase("All")) {
            rankedStartups = allStartups.stream()
                    .filter(startup -> industry.equalsIgnoreCase(startup.getIndustry()))
                    .collect(Collectors.toList());
        } else {
            rankedStartups = allStartups;
        }
        
        rankedStartups = rankingService.rankStartupsByMetric(rankedStartups, metric);
        
        Map<String, Object> response = new HashMap<>();
        response.put("totalCount", rankedStartups.size());
        
        List<Map<String, Object>> startupData = rankedStartups.stream().map(startup -> {
            Map<String, Object> data = new HashMap<>();
            data.put("id", startup.getId());
            data.put("companyName", startup.getCompanyName());
            data.put("industry", startup.getIndustry());
            data.put("overallScore", Math.round(rankingService.calculateOverallScore(startup)));
            data.put("growthScore", Math.round(rankingService.calculateGrowthScore(startup) * 100));
            data.put("investmentScore", Math.round(rankingService.calculateInvestmentScore(startup) * 100));
            data.put("ecosystemScore", Math.round(rankingService.calculateEcosystemScore(startup) * 100));
            data.put("engagementScore", Math.round(rankingService.calculateEngagementScore(startup) * 100));
            return data;
        }).collect(Collectors.toList());
        
        response.put("rankings", startupData);
        return ResponseEntity.ok(response);
    }

   
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getStartupScoreDetails(@PathVariable Long id) {
        return startupRepository.findById(id)
                .map(startup -> {
                    Map<String, Object> details = new HashMap<>();
                    details.put("id", startup.getId());
                    details.put("companyName", startup.getCompanyName());
                    details.put("industry", startup.getIndustry());
                    
                    details.put("overallScore", Math.round(rankingService.calculateOverallScore(startup)));
                    details.put("growthScore", Math.round(rankingService.calculateGrowthScore(startup) * 100));
                    details.put("investmentScore", Math.round(rankingService.calculateInvestmentScore(startup) * 100));
                    details.put("ecosystemScore", Math.round(rankingService.calculateEcosystemScore(startup) * 100));
                    details.put("engagementScore", Math.round(rankingService.calculateEngagementScore(startup) * 100));
                    
                    Map<String, Object> metrics = new HashMap<>();
                    metrics.put("annualRevenue", startup.getAnnualRevenue());
                    metrics.put("growthRate", startup.getAverageStartupGrowthRate());
                    metrics.put("survivalRate", startup.getStartupSurvivalRate());
                    metrics.put("fundingReceived", startup.getTotalStartupFundingReceived());
                    metrics.put("fundingRounds", startup.getNumberOfFundingRounds());
                    metrics.put("governmentSupport", startup.getAmountOfGovernmentGrantsOrSubsidiesReceived());
                    metrics.put("mentors", startup.getNumberOfMentorsOrAdvisorsInvolved());
                    metrics.put("partnerships", startup.getPublicPrivatePartnershipsInvolvingStartups());
                    metrics.put("views", startup.getViewsCount());
                    metrics.put("likes", startup.getLikes() != null ? startup.getLikes().size() : 0);
                    metrics.put("bookmarks", startup.getBookmarks() != null ? startup.getBookmarks().size() : 0);
                    
                    details.put("metrics", metrics);
                    return ResponseEntity.ok(details);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/top")
    public ResponseEntity<List<Map<String, Object>>> getTopStartups(
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false) String industry) {
        
        List<Startup> allStartups = startupRepository.findAll();
        List<Startup> rankedStartups;
        
        if (industry != null && !industry.trim().isEmpty() && !industry.equalsIgnoreCase("All")) {
            rankedStartups = rankingService.rankStartupsByIndustry(allStartups, industry);
        } else {
            rankedStartups = rankingService.rankStartups(allStartups);
        }
        
        rankedStartups = rankedStartups.stream().limit(limit).collect(Collectors.toList());
        
        List<Map<String, Object>> response = rankedStartups.stream().map(startup -> {
            Map<String, Object> data = new HashMap<>();
            data.put("id", startup.getId());
            data.put("companyName", startup.getCompanyName());
            data.put("industry", startup.getIndustry());
            data.put("score", Math.round(rankingService.calculateOverallScore(startup)));
            data.put("growthRate", startup.getAverageStartupGrowthRate());
            return data;
        }).collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
}