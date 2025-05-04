package com.startupsphere.capstone.controller;

import com.startupsphere.capstone.entity.Startup;
import com.startupsphere.capstone.repository.StartupRepository;
import com.startupsphere.capstone.service.StartupRankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
    
    @GetMapping("/dashboard-analytics")
    public ResponseEntity<Map<String, Object>> getDashboardAnalytics() {
        List<Startup> allStartups = startupRepository.findAll();
        Map<String, Object> response = new HashMap<>();
        
        response.put("growthData", generateGrowthData(allStartups));
        
        response.put("fundingData", generateFundingData(allStartups));
        
        response.put("locationData", generateLocationData(allStartups));
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Generate growth data for top industries over the last 6 months
     * This is a simulated implementation that uses growth rates by industry
     */
    private List<Map<String, Object>> generateGrowthData(List<Startup> startups) {
        // Get top 3 industries by number of startups
        Map<String, Long> industriesCount = startups.stream()
                .filter(s -> s.getIndustry() != null && !s.getIndustry().isEmpty())
                .collect(Collectors.groupingBy(
                        Startup::getIndustry,
                        Collectors.counting()
                ));
        
        List<String> topIndustries = industriesCount.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        
        // Generate monthly data points for the last 6 months
        List<Map<String, Object>> growthData = new ArrayList<>();
        LocalDate today = LocalDate.now();
        
        for (int i = 5; i >= 0; i--) {
            YearMonth month = YearMonth.from(today.minusMonths(i));
            String monthName = month.format(DateTimeFormatter.ofPattern("MMM"));
            
            Map<String, Object> monthData = new HashMap<>();
            monthData.put("name", monthName);
            
            // Get average growth rate for each top industry for this month
            // (this is simulated based on the available data)
            for (String industry : topIndustries) {
                double avgGrowthRate = startups.stream()
                        .filter(s -> industry.equals(s.getIndustry()))
                        .mapToDouble(Startup::getAverageStartupGrowthRate)
                        .average()
                        .orElse(0.0);
                
                // Simulating some monthly variation
                double scaleFactor = 1.0 + (0.1 * i);  // Older months have slightly lower values
                monthData.put(industry, Math.round(avgGrowthRate * scaleFactor));
            }
            
            growthData.add(monthData);
        }
        
        return growthData;
    }
    
    /**
     * Generate funding stage distribution data
     */
    private List<Map<String, Object>> generateFundingData(List<Startup> startups) {
        Map<String, Long> fundingDistribution = startups.stream()
                .filter(s -> s.getFundingStage() != null && !s.getFundingStage().isEmpty())
                .collect(Collectors.groupingBy(
                        Startup::getFundingStage,
                        Collectors.counting()
                ));
        
        // Create a standardized set of funding stages for more consistent UI
        Map<String, Long> standardizedFunding = new LinkedHashMap<>();
        standardizedFunding.put("Seed", fundingDistribution.getOrDefault("Seed", 0L));
        standardizedFunding.put("Series A", fundingDistribution.getOrDefault("Series A", 0L));
        standardizedFunding.put("Series B", fundingDistribution.getOrDefault("Series B", 0L));
        standardizedFunding.put("Series C+", 
                fundingDistribution.getOrDefault("Series C", 0L) + 
                fundingDistribution.getOrDefault("Series D", 0L) + 
                fundingDistribution.getOrDefault("Series E", 0L) +
                fundingDistribution.getOrDefault("Series F+", 0L));
        
        // Count other funding stages not in our standard list
        long otherCount = fundingDistribution.entrySet().stream()
                .filter(e -> !standardizedFunding.containsKey(e.getKey()))
                .mapToLong(Map.Entry::getValue)
                .sum();
        
        if (otherCount > 0) {
            standardizedFunding.put("Other", otherCount);
        }
        
        // Convert to required format
        return standardizedFunding.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("name", entry.getKey());
                    item.put("value", entry.getValue());
                    return item;
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Generate location distribution data
     */
    private List<Map<String, Object>> generateLocationData(List<Startup> startups) {
        Map<String, Long> locationDistribution = startups.stream()
                .filter(s -> s.getCity() != null && !s.getCity().isEmpty())
                .collect(Collectors.groupingBy(
                        Startup::getCity,
                        Collectors.counting()
                ));
        
        // Sort by count and take top 4 locations
        List<Map.Entry<String, Long>> topLocations = locationDistribution.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(4)
                .collect(Collectors.toList());
        
        List<Map<String, Object>> result = new ArrayList<>();
        
        // Add top locations
        for (Map.Entry<String, Long> entry : topLocations) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", entry.getKey());
            item.put("value", entry.getValue());
            result.add(item);
        }
        
        // Calculate "Other" category
        long topLocationsCount = topLocations.stream()
                .mapToLong(Map.Entry::getValue)
                .sum();
        
        long totalCount = locationDistribution.values().stream()
                .mapToLong(Long::longValue)
                .sum();
        
        long otherCount = totalCount - topLocationsCount;
        if (otherCount > 0) {
            Map<String, Object> otherItem = new HashMap<>();
            otherItem.put("name", "Other");
            otherItem.put("value", otherCount);
            result.add(otherItem);
        }
        
        return result;
    }
}