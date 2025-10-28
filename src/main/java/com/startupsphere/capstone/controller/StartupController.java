package com.startupsphere.capstone.controller;

import com.startupsphere.capstone.entity.Startup;
import com.startupsphere.capstone.entity.User;
import com.startupsphere.capstone.repository.StartupRepository;
import com.startupsphere.capstone.responses.ErrorResponse;
import com.startupsphere.capstone.responses.SuccessResponse;
import com.startupsphere.capstone.service.NotificationService;
import com.startupsphere.capstone.service.StartupService;

import io.jsonwebtoken.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

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

    @Autowired
    private NotificationService notificationService;

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
            notificationService.createStartupApprovalNotification(
                    createdStartup,
                    "in review",
                    "New startup application submitted for review."
            );
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

    @GetMapping("/submitted")
    public ResponseEntity<List<Startup>> getAllSubmittedStartups() {
        try {
            logger.info("Fetching all submitted startups");
            List<Startup> startups = startupService.getAllSubmittedStartups();
            return ResponseEntity.ok(startups);
        } catch (Exception e) {
            logger.error("Error fetching submitted startups: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
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
        String fileName = file.getOriginalFilename();
        if (file.isEmpty() || fileName == null || !fileName.endsWith(".csv")) {
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

        String contentType = photo.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            logger.warn("Invalid file type uploaded for startup ID: {}. Content-Type: {}", id, contentType);
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

            startupService.updateStartup(id, startup);
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

    @PutMapping("/{id:[0-9]+}/upload-registration-certificate")
    public ResponseEntity<?> uploadRegistrationCertificate(
            @PathVariable Long id,
            @RequestParam("registrationCertificate") MultipartFile registrationCertificate) {
        logger.info("Received registration certificate upload request for startup ID: {}", id);

        if (registrationCertificate == null || registrationCertificate.isEmpty()) {
            logger.warn("Upload attempt with empty or null registration certificate for startup ID: {}", id);
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Please upload a valid file for registration certificate."));
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

            byte[] fileBytes = registrationCertificate.getBytes();
            logger.info("Successfully read registration certificate bytes, size: {}", fileBytes.length);

            startup.setRegistrationCertificate(fileBytes);
            startupService.updateStartup(id, startup);
            logger.info("Successfully updated startup with registration certificate");

            return ResponseEntity.ok(
                    new SuccessResponse("Registration certificate uploaded successfully."));
        } catch (java.io.IOException e) {
            logger.error("Failed to read registration certificate bytes for startup ID: {}", id, e);
            return ResponseEntity.status(500).body(
                    new ErrorResponse("Error processing registration certificate file: " + e.getMessage()));
        } catch (Exception e) {
            logger.error("Error uploading registration certificate for startup ID: {}", id, e);
            return ResponseEntity.status(500).body(
                    new ErrorResponse("Error uploading registration certificate: " + e.getMessage()));
        }
    }

    @GetMapping("/{id:[0-9]+}/registration-certificate")
    public ResponseEntity<byte[]> getRegistrationCertificate(@PathVariable Long id) {
        Optional<Startup> optionalStartup = startupService.getStartupById(id);
        if (optionalStartup.isEmpty() || optionalStartup.get().getRegistrationCertificate() == null) {
            logger.warn("Registration certificate not found for startup ID: {}", id);
            return ResponseEntity.notFound().build();
        }

        byte[] file = optionalStartup.get().getRegistrationCertificate();
        return ResponseEntity.ok()
                .header("Content-Type", "application/octet-stream")
                .header("Content-Disposition", "attachment; filename=registration_certificate")
                .body(file);
    }

    @GetMapping("/review")
    public ResponseEntity<List<Startup>> getStartupsWithFilters(
            @RequestParam(required = false) String industry,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        try {
            List<Startup> startups = startupService.getStartupsWithFilters(
                    industry,
                    status,
                    region,
                    search,
                    startDate,
                    endDate
            );
            return ResponseEntity.ok(startups);
        } catch (Exception e) {
            logger.error("Error fetching filtered startups: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/industries")
    public ResponseEntity<List<String>> getDistinctIndustries() {
        try {
            List<String> industries = startupService.getDistinctIndustries();
            return ResponseEntity.ok(industries);
        } catch (Exception e) {
            logger.error("Error fetching distinct industries: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/upload-startups")
    public ResponseEntity<?> uploadStartupsCsv(@RequestParam("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (file.isEmpty() || fileName == null || !fileName.toLowerCase().endsWith(".csv")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Please upload a valid CSV file."));
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String headerLine = reader.readLine();
            if (headerLine == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "CSV file is empty."));
            }

            logger.info("Processing CSV upload with header: {}", headerLine);

            // Parse headers and create a mapping from column name to index
            String[] headers = headerLine.split(",", -1);
            Map<String, Integer> headerMap = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                String headerName = headers[i].trim().toLowerCase();
                headerMap.put(headerName, i);
                logger.debug("Header[{}]: '{}'", i, headerName);
            }

            List<Startup> startups = new ArrayList<>();
            List<String> errors = new ArrayList<>();
            String line;
            int lineNumber = 1;
            int successCount = 0;
            int skipCount = 0;

            // Retrieve the currently authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
                return ResponseEntity.status(401).body(Map.of("error", "Unauthorized: No authenticated user found."));
            }
            User loggedInUser = (User) authentication.getPrincipal();

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] fields = line.split(",", -1); // -1 to preserve trailing empty strings

                // Log raw line for first few records to debug
                if (lineNumber <= 3) {
                    logger.info("Line {}: Total fields={}, Raw line preview: {}", 
                        lineNumber, fields.length, line.length() > 200 ? line.substring(0, 200) + "..." : line);
                }

                try {
                    // Validate minimum required fields
                    String companyName = getValueByHeader(fields, headerMap, "companyname");
                    if (companyName == null || companyName.isEmpty()) {
                        throw new IllegalArgumentException("Company name is required");
                    }

                    Startup startup = new Startup();
                    startup.setUser(loggedInUser);
                    
                    // Basic Information
                    startup.setCompanyName(companyName);
                    startup.setCompanyDescription(getValueByHeader(fields, headerMap, "companydescription"));
                    startup.setFoundedDate(getValueByHeader(fields, headerMap, "foundeddate"));
                    startup.setTypeOfCompany(getValueByHeader(fields, headerMap, "typeofcompany"));
                    startup.setNumberOfEmployees(getValueByHeader(fields, headerMap, "numberofemployees"));
                    startup.setPhoneNumber(getValueByHeader(fields, headerMap, "phonenumber"));
                    startup.setContactEmail(getValueByHeader(fields, headerMap, "contactemail"));
                    
                    // Address Information
                    startup.setStreetAddress(getValueByHeader(fields, headerMap, "streetaddress"));
                    startup.setCity(getValueByHeader(fields, headerMap, "city"));
                    startup.setProvince(getValueByHeader(fields, headerMap, "province"));
                    startup.setPostalCode(getValueByHeader(fields, headerMap, "postalcode"));
                    
                    // Category and Social Media
                    startup.setIndustry(getValueByHeader(fields, headerMap, "industry"));
                    startup.setWebsite(getValueByHeader(fields, headerMap, "website"));
                    startup.setFacebook(getValueByHeader(fields, headerMap, "facebook"));
                    startup.setTwitter(getValueByHeader(fields, headerMap, "twitter"));
                    startup.setInstagram(getValueByHeader(fields, headerMap, "instagram"));
                    startup.setLinkedIn(getValueByHeader(fields, headerMap, "linkedin"));
                    
                    // Location Coordinates
                    startup.setLocationLat(parseDoubleSafe(getValueByHeader(fields, headerMap, "locationlat")));
                    startup.setLocationLng(parseDoubleSafe(getValueByHeader(fields, headerMap, "locationlng")));
                    startup.setLocationName(getValueByHeader(fields, headerMap, "locationname"));
                    
                    // Status and Code
                    startup.setStartupCode(getValueByHeader(fields, headerMap, "startupcode"));
                    startup.setStatus(getValueByHeader(fields, headerMap, "status"));
                    
                    // Financial Information
                    // revenue is primitive double, others are nullable Double
                    String revenueStr = getValueByHeader(fields, headerMap, "revenue");
                    startup.setRevenue(parseDoubleSafeWithDefault(revenueStr, 0.0));
                    
                    String annualRevenueStr = getValueByHeader(fields, headerMap, "annualrevenue");
                    startup.setAnnualRevenue(parseDoubleSafe(annualRevenueStr));
                    
                    startup.setPaidUpCapital(parseDoubleSafe(getValueByHeader(fields, headerMap, "paidupcapital")));
                    
                    // Business Details
                    startup.setFundingStage(getValueByHeader(fields, headerMap, "fundingstage"));
                    startup.setBusinessActivity(getValueByHeader(fields, headerMap, "businessactivity"));
                    startup.setOperatingHours(getValueByHeader(fields, headerMap, "operatinghours"));
                    
                    // Metrics - All are primitive int, so use default of 0
                    String numActiveStartupsStr = getValueByHeader(fields, headerMap, "numberofactivestartups");
                    startup.setNumberOfActiveStartups(parseIntSafeWithDefault(numActiveStartupsStr, 0));
                    
                    String numNewStartupsStr = getValueByHeader(fields, headerMap, "numberOfNewStartupsThisYear");
                    startup.setNumberOfNewStartupsThisYear(parseIntSafeWithDefault(numNewStartupsStr, 0));
                    
                    startup.setAverageStartupGrowthRate(parseDoubleSafeWithDefault(getValueByHeader(fields, headerMap, "averageStartupGrowthRate"), 0.0));
                    startup.setStartupSurvivalRate(parseDoubleSafeWithDefault(getValueByHeader(fields, headerMap, "startupSurvivalRate"), 0.0));
                    startup.setTotalStartupFundingReceived(parseDoubleSafeWithDefault(getValueByHeader(fields, headerMap, "totalStartupFundingReceived"), 0.0));
                    startup.setAverageFundingPerStartup(parseDoubleSafeWithDefault(getValueByHeader(fields, headerMap, "averageFundingPerStartup"), 0.0));
                    
                    String numFundingRoundsStr = getValueByHeader(fields, headerMap, "numberOfFundingRounds");
                    startup.setNumberOfFundingRounds(parseIntSafeWithDefault(numFundingRoundsStr, 0));
                    
                    String numForeignInvestmentStr = getValueByHeader(fields, headerMap, "numberOfStartupsWithForeignInvestment");
                    startup.setNumberOfStartupsWithForeignInvestment(parseIntSafeWithDefault(numForeignInvestmentStr, 0));
                    
                    startup.setAmountOfGovernmentGrantsOrSubsidiesReceived(parseDoubleSafeWithDefault(getValueByHeader(fields, headerMap, "amountOfGovernmentGrantsOrSubsidiesReceived"), 0.0));
                    startup.setNumberOfStartupIncubatorsOrAccelerators(parseIntSafeWithDefault(getValueByHeader(fields, headerMap, "numberOfStartupIncubatorsOrAccelerators"), 0));
                    startup.setNumberOfStartupsInIncubationPrograms(parseIntSafeWithDefault(getValueByHeader(fields, headerMap, "numberOfStartupsInIncubationPrograms"), 0));
                    startup.setNumberOfMentorsOrAdvisorsInvolved(parseIntSafeWithDefault(getValueByHeader(fields, headerMap, "numberOfMentorsOrAdvisorsInvolved"), 0));
                    startup.setPublicPrivatePartnershipsInvolvingStartups(parseIntSafeWithDefault(getValueByHeader(fields, headerMap, "publicPrivatePartnershipsInvolvingStartups"), 0));
                    
                    // Regional Information
                    startup.setRegion(getValueByHeader(fields, headerMap, "region"));
                    startup.setBarangay(getValueByHeader(fields, headerMap, "barangay"));
                    
                    // Registration Information
                    startup.setIsGovernmentRegistered(parseBooleanSafe(getValueByHeader(fields, headerMap, "isGovernmentRegistered")));
                    startup.setRegistrationAgency(getValueByHeader(fields, headerMap, "registrationAgency"));
                    
                    String registrationNumberStr = getValueByHeader(fields, headerMap, "registrationNumber");
                    startup.setRegistrationNumber(registrationNumberStr);
                    
                    startup.setRegistrationDate(getValueByHeader(fields, headerMap, "registrationDate"));
                    startup.setOtherRegistrationAgency(getValueByHeader(fields, headerMap, "otherRegistrationagency"));
                    startup.setBusinessLicenseNumber(getValueByHeader(fields, headerMap, "businessLicensenumber"));
                    startup.setTin(getValueByHeader(fields, headerMap, "tin"));

                    // Log for debugging specific problematic fields
                    if (lineNumber <= 3) {
                        logger.info("Line {} parsed values - revenue: '{}'->{}, annualRevenue: '{}'->{}, numActiveStartups: '{}'->{}, numNewStartups: '{}'->{}, numFundingRounds: '{}'->{}, numForeignInvestment: '{}'->{}, registrationNumber: '{}'=>'{}'",
                            lineNumber, 
                            revenueStr, startup.getRevenue(),
                            annualRevenueStr, startup.getAnnualRevenue(),
                            numActiveStartupsStr, startup.getNumberOfActiveStartups(),
                            numNewStartupsStr, startup.getNumberOfNewStartupsThisYear(),
                            numFundingRoundsStr, startup.getNumberOfFundingRounds(),
                            numForeignInvestmentStr, startup.getNumberOfStartupsWithForeignInvestment(),
                            registrationNumberStr, startup.getRegistrationNumber());
                    }

                    startups.add(startup);
                    successCount++;
                } catch (Exception e) {
                    skipCount++;
                    String errorMsg = String.format("Line %d: %s", lineNumber, e.getMessage());
                    errors.add(errorMsg);
                    logger.warn("Skipping line {}: {}", lineNumber, e.getMessage());
                }
            }

            // Save all valid startups in batch
            if (!startups.isEmpty()) {
                try {
                    List<Startup> savedStartups = startupRepository.saveAll(startups);
                    logger.info("Successfully saved {} startups from CSV upload", savedStartups.size());
                    
                    // Log sample of saved data for verification (first startup only)
                    if (!savedStartups.isEmpty() && logger.isInfoEnabled()) {
                        Startup sample = savedStartups.get(0);
                        logger.info("Sample saved startup - ID: {}, Name: {}, Revenue: {}, AnnualRevenue: {}, NumActiveStartups: {}, NumNewStartups: {}, NumFundingRounds: {}, NumForeignInvestment: {}, RegistrationNumber: {}",
                            sample.getId(), sample.getCompanyName(), sample.getRevenue(), 
                            sample.getAnnualRevenue(), sample.getNumberOfActiveStartups(),
                            sample.getNumberOfNewStartupsThisYear(), sample.getNumberOfFundingRounds(),
                            sample.getNumberOfStartupsWithForeignInvestment(), sample.getRegistrationNumber());
                    }
                } catch (Exception e) {
                    logger.error("Error saving startups to database: {}", e.getMessage(), e);
                    return ResponseEntity.status(500).body(Map.of("error", "Error saving to database: " + e.getMessage()));
                }
            }

            // Prepare response
            Map<String, Object> response = new HashMap<>();
            response.put("message", "CSV file processed successfully");
            response.put("successCount", successCount);
            response.put("skippedCount", skipCount);
            response.put("totalProcessed", lineNumber - 1);
            
            if (!errors.isEmpty() && errors.size() <= 10) {
                // Only include errors if there aren't too many
                response.put("errors", errors);
            } else if (!errors.isEmpty()) {
                response.put("errorSummary", String.format("%d rows had errors. Check logs for details.", skipCount));
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error processing CSV file: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of("error", "Error processing the file: " + e.getMessage()));
        }
    }

    /**
     * Safely get a string value from array at index.
     * Returns null only if index is out of bounds or value is null.
     * Returns the trimmed string even if it's empty (to preserve empty strings vs null).
     */
    private String getSafe(String[] arr, int index) {
        if (index < arr.length && arr[index] != null) {
            String trimmed = arr[index].trim();
            // Return null for truly empty strings, but preserve "0" and other values
            return trimmed.isEmpty() ? null : trimmed;
        }
        return null;
    }

    /**
     * Get value from CSV row by header name (case-insensitive).
     * Uses the header map to find the correct column index.
     */
    private String getValueByHeader(String[] fields, Map<String, Integer> headerMap, String headerName) {
        Integer index = headerMap.get(headerName.toLowerCase());
        if (index == null) {
            logger.debug("Header '{}' not found in CSV", headerName);
            return null;
        }
        return getSafe(fields, index);
    }

    /**
     * Parse Double with null return for invalid/empty values.
     * Preserves zero values.
     */
    private Double parseDoubleSafe(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            logger.debug("Failed to parse double value: '{}'", value);
            return null;
        }
    }

    /**
     * Parse double with default value for primitive fields.
     * Preserves zero values from CSV.
     */
    private double parseDoubleSafeWithDefault(String value, double defaultValue) {
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            logger.debug("Failed to parse double value: '{}', using default: {}", value, defaultValue);
            return defaultValue;
        }
    }

    /**
     * Parse Integer with null return for invalid/empty values.
     * Preserves zero values.
     */
    private Integer parseIntSafe(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            logger.debug("Failed to parse integer value: '{}'", value);
            return null;
        }
    }

    /**
     * Parse int with default value for primitive fields.
     * Preserves zero values from CSV.
     */
    private int parseIntSafeWithDefault(String value, int defaultValue) {
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            logger.debug("Failed to parse integer value: '{}', using default: {}", value, defaultValue);
            return defaultValue;
        }
    }

    /**
     * Parse Boolean safely handling various representations.
     */
    private Boolean parseBooleanSafe(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        // Handle common boolean representations
        String lowerValue = value.toLowerCase().trim();
        return lowerValue.equals("true") || lowerValue.equals("1") || lowerValue.equals("yes");
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