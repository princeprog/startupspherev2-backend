package com.startupsphere.capstone.controller;

import com.startupsphere.capstone.entity.Startup;
import com.startupsphere.capstone.entity.User;
import com.startupsphere.capstone.repository.StartupRepository;
import com.startupsphere.capstone.responses.ErrorResponse;
import com.startupsphere.capstone.responses.SuccessResponse;
import com.startupsphere.capstone.service.StartupService;

import io.jsonwebtoken.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/startups")
public class StartupController {

    private static final Logger logger = LoggerFactory.getLogger(StartupController.class);

    private final StartupService startupService;
    private final StartupRepository startupRepository;

    public StartupController(StartupService startupService, StartupRepository startupRepository) {
        this.startupService = startupService;
        this.startupRepository = startupRepository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Startup> createStartup(@RequestBody Startup startup) {
        logger.info("Attempting to create startup: {}", startup.getCompanyName());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {
            logger.warn("Unauthorized attempt to create startup");
            return ResponseEntity.status(401).body(null);
        }

        User loggedInUser = (User) authentication.getPrincipal();
        startup.setUser(loggedInUser);

        try {
            Startup createdStartup = startupService.createStartup(startup);
            logger.info("Startup created successfully with ID: {}", createdStartup.getId());
            return ResponseEntity.ok(createdStartup);
        } catch (Exception e) {
            logger.error("Error creating startup: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<Startup>> getAllStartups() {
        List<Startup> startups = startupService.getAllStartups();
        return ResponseEntity.ok(startups);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Startup>> searchStartups(@RequestParam String query) {
        List<Startup> startups = startupService.searchStartups(query);
        return ResponseEntity.ok(startups);
    }

    @GetMapping("/{id:[0-9]+}")
    public ResponseEntity<Startup> getStartupById(@PathVariable Long id) {
        return startupService.getStartupById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id:[0-9]+}")
    public ResponseEntity<Startup> updateStartup(@PathVariable Long id, @RequestBody Startup updatedStartup) {
        try {
            Startup updated = startupService.updateStartup(id, updatedStartup);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id:[0-9]+}")
    public ResponseEntity<Void> deleteStartup(@PathVariable Long id) {
        logger.info("Received request to delete startup with id: {}", id);

        try {
            startupService.deleteStartup(id);
            logger.info("Successfully deleted startup with id: {}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            logger.error("Error deleting startup with id: {}. Error: {}", id, e.getMessage(), e);
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(404).build();
            }
            return ResponseEntity.status(500).build();
        } catch (Exception e) {
            logger.error("Unexpected error deleting startup with id: {}. Error: {}", id, e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id:[0-9]+}/views")
    public ResponseEntity<Integer> getViewsByStartupId(@PathVariable Long id) {
        Optional<Startup> optionalStartup = startupRepository.findById(id);
        if (optionalStartup.isPresent()) {
            Startup startup = optionalStartup.get();
            return ResponseEntity.ok(startup.getViewsCount());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id:[0-9]+}/increment-views")
    public ResponseEntity<Integer> incrementViews(@PathVariable Long id) {
        Optional<Startup> optionalStartup = startupRepository.findById(id);
        if (optionalStartup.isPresent()) {
            Startup startup = optionalStartup.get();
            startup.setViewsCount(startup.getViewsCount() + 1);
            startupRepository.save(startup);
            return ResponseEntity.ok(startup.getViewsCount());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{startupId:[0-9]+}/upload-csv")
    public ResponseEntity<String> uploadStartupCsv(
            @PathVariable Long startupId,
            @RequestParam("file") MultipartFile file) {
        if (file.isEmpty() || !file.getOriginalFilename().endsWith(".csv")) {
            return ResponseEntity.badRequest().body("Please upload a valid CSV file.");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            Optional<Startup> optionalStartup = startupService.getStartupById(startupId);
            if (optionalStartup.isEmpty()) {
                return ResponseEntity.badRequest().body("Startup with ID " + startupId + " not found.");
            }

            Startup startup = optionalStartup.get();
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] fields = line.split(",");
                startup.setRevenue(Double.parseDouble(fields[0]));
                startup.setAnnualRevenue(Double.parseDouble(fields[1]));
                startup.setPaidUpCapital(Double.parseDouble(fields[2]));
                startup.setNumberOfActiveStartups(Integer.parseInt(fields[3]));
                startup.setNumberOfNewStartupsThisYear(Integer.parseInt(fields[4]));
                startup.setAverageStartupGrowthRate(Double.parseDouble(fields[5]));
                startup.setStartupSurvivalRate(Double.parseDouble(fields[6]));
                startup.setTotalStartupFundingReceived(Double.parseDouble(fields[7]));
                startup.setAverageFundingPerStartup(Double.parseDouble(fields[8]));
                startup.setNumberOfFundingRounds(Integer.parseInt(fields[9]));
                startup.setNumberOfStartupsWithForeignInvestment(Integer.parseInt(fields[10]));
                startup.setAmountOfGovernmentGrantsOrSubsidiesReceived(Double.parseDouble(fields[11]));
                startup.setNumberOfStartupIncubatorsOrAccelerators(Integer.parseInt(fields[12]));
                startup.setNumberOfStartupsInIncubationPrograms(Integer.parseInt(fields[13]));
                startup.setNumberOfMentorsOrAdvisorsInvolved(Integer.parseInt(fields[14]));
                startup.setPublicPrivatePartnershipsInvolvingStartups(Integer.parseInt(fields[15]));
            }

            startupService.updateStartup(startupId, startup);
            return ResponseEntity.ok("CSV file processed and data updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing the file: " + e.getMessage());
        }
    }

    @GetMapping("/my-startups")
    public ResponseEntity<List<Long>> getStartupIdsByLoggedInUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            List<Long> startupIds = startupService.getStartupIdsByLoggedInUser(authentication);
            return ResponseEntity.ok(startupIds);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(null);
        }
    }

    @GetMapping("/{id:[0-9]+}/view-count")
    public ResponseEntity<Integer> getStartupViews(@PathVariable Long id) {
        Optional<Startup> optionalStartup = startupService.getStartupById(id);
        if (optionalStartup.isPresent()) {
            Startup startup = optionalStartup.get();
            return ResponseEntity.ok(startup.getViewsCount());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/my-startups/details")
    public ResponseEntity<List<Startup>> getStartupsByLoggedInUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            List<Startup> startups = startupService.getStartupsByLoggedInUser(authentication);
            return ResponseEntity.ok(startups);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/send-verification-email")
    public ResponseEntity<Map<String, String>> sendVerificationEmail(@RequestBody VerificationRequest request) {
        try {
            startupService.sendVerificationEmail(request.getStartupId(), request.getEmail());
            return ResponseEntity.ok(Map.of("message", "Verification email sent successfully."));
        } catch (RuntimeException e) {
            logger.error("Error sending verification email: {}", e.getMessage(), e);
            if (e.getMessage().contains("Email is already verified")) {
                logger.info("Email already verified for startup ID: {}. Resending verification email.",
                        request.getStartupId());
                return ResponseEntity.ok(Map.of("message", "Verification email resent successfully."));
            }
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Error sending verification email: " + e.getMessage()));
        }
    }

    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestBody VerificationRequest request) {
        try {
            startupService.verifyEmail(request.getStartupId(), request.getEmail(), request.getCode());
            return ResponseEntity.ok("Email verified successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error verifying email: " + e.getMessage());
        }
    }

    @PutMapping("/{id:[0-9]+}/upload-photo")
    public ResponseEntity<?> uploadStartupPhoto(
            @PathVariable Long id,
            @RequestParam(value = "photo", required = false) MultipartFile photo) {
        logger.info("Received photo upload request for startup ID: {}", id);

        if (photo == null || photo.isEmpty()) {
            logger.warn("Upload attempt with empty or null photo for startup ID: {}", id);
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Please upload a valid image file."));
        }

        logger.info("Photo details - Name: {}, Size: {}, Content-Type: {}",
                photo.getOriginalFilename(),
                photo.getSize(),
                photo.getContentType());

        if (!photo.getContentType().startsWith("image/")) {
            logger.warn("Invalid file type uploaded for startup ID: {}. Content-Type: {}", id, photo.getContentType());
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Please upload a valid image file (e.g., JPEG, PNG)."));
        }

        try {
            Optional<Startup> optionalStartup = startupService.getStartupById(id);
            if (optionalStartup.isEmpty()) {
                logger.warn("Startup not found for ID: {}", id);
                return ResponseEntity.badRequest().body(
                        new ErrorResponse("Startup with ID " + id + " not found."));
            }

            Startup startup = optionalStartup.get();
            logger.info("Found startup: {}", startup.getCompanyName());

            byte[] photoBytes = photo.getBytes();
            logger.info("Successfully read photo bytes, size: {}", photoBytes.length);

            startup.setPhoto(photoBytes);
            logger.info("Set photo bytes to startup entity");

            Startup updatedStartup = startupService.updateStartup(id, startup);
            logger.info("Successfully updated startup with photo");

            return ResponseEntity.ok(
                    new SuccessResponse("Photo uploaded successfully."));
        } catch (IOException e) {
            logger.error("Failed to read photo bytes for startup ID: {}", id, e);
            return ResponseEntity.status(500).body(
                    new ErrorResponse("Error processing image file: " + e.getMessage()));
        } catch (Exception e) {
            logger.error("Error uploading photo for startup ID: {}", id, e);
            return ResponseEntity.status(500).body(
                    new ErrorResponse("Error uploading photo: " + e.getMessage()));
        }
    }

    @GetMapping("/{id:[0-9]+}/photo")
    public ResponseEntity<byte[]> getStartupPhoto(@PathVariable Long id) {
        Optional<Startup> optionalStartup = startupService.getStartupById(id);
        if (optionalStartup.isEmpty() || optionalStartup.get().getPhoto() == null) {
            logger.warn("Photo not found for startup ID: {}", id);
            return ResponseEntity.notFound().build();
        }

        byte[] photo = optionalStartup.get().getPhoto();
        return ResponseEntity.ok()
                .header("Content-Type", "image/jpeg")
                .body(photo);
    }

    @GetMapping("/email-verified")
    public ResponseEntity<List<Startup>> getAllEmailVerifiedStartups() {
        List<Startup> startups = startupService.getAllEmailVerifiedStartups();
        return ResponseEntity.ok(startups);
    }

    @PutMapping("/{id:[0-9]+}/approve")
    public ResponseEntity<Startup> approveStartup(@PathVariable Long id) {
        try {
            Startup approvedStartup = startupService.approveStartup(id);
            return ResponseEntity.ok(approvedStartup);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @PutMapping("/{id:[0-9]+}/reject")
    public ResponseEntity<Startup> rejectStartup(@PathVariable Long id) {
        try {
            Startup rejectedStartup = startupService.rejectStartup(id);
            return ResponseEntity.ok(rejectedStartup);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/approved")
    public ResponseEntity<List<Startup>> getAllApprovedStartups() {
        List<Startup> startups = startupService.getAllApprovedStartups();
        return ResponseEntity.ok(startups);
    }

    @PostMapping("/test-reminder-emails")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> testReminderEmails() {
        try {
            logger.info("Manually triggering reminder emails");
            startupService.sendUpdateReminderEmails();
            return ResponseEntity.ok(Map.of("message", "Reminder emails triggered successfully"));
        } catch (Exception e) {
            logger.error("Error triggering reminder emails: {}", e.getMessage(), e);
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Failed to trigger reminder emails: " + e.getMessage()));
        }
    }

    public static class VerificationRequest {
        private Long startupId;
        private String email;
        private String code;

        public Long getStartupId() {
            return startupId;
        }

        public void setStartupId(Long startupId) {
            this.startupId = startupId;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}