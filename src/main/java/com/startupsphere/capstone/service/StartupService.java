package com.startupsphere.capstone.service;

import com.startupsphere.capstone.entity.Startup;
import com.startupsphere.capstone.entity.User;
import com.startupsphere.capstone.repository.StartupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class StartupService {

    private static final Logger logger = LoggerFactory.getLogger(StartupService.class);

    private final StartupRepository startupRepository;
    private final JavaMailSender mailSender;

    public StartupService(StartupRepository startupRepository, JavaMailSender mailSender) {
        this.startupRepository = startupRepository;
        this.mailSender = mailSender;
    }

    @Transactional
    public Startup createStartup(Startup startup) {
        logger.info("Saving startup: {}", startup.getCompanyName());

        startup.setStatus("In Review");
        return startupRepository.save(startup);
    }

    public List<Startup> getAllStartups() {
        return startupRepository.findAll();
    }

    public Optional<Startup> getStartupById(Long id) {
        return startupRepository.findById(id);
    }

    @Transactional
    public Startup updateStartup(Long id, Startup updatedStartup) {
        return startupRepository.findById(id)
                .map(startup -> {
                    updatedStartup.setId(startup.getId());
                    updatedStartup.setUser(startup.getUser());
                    updatedStartup.setLikes(startup.getLikes());
                    updatedStartup.setBookmarks(startup.getBookmarks());
                    updatedStartup.setViews(startup.getViews());

                    startup.setCompanyName(updatedStartup.getCompanyName());
                    startup.setCompanyDescription(updatedStartup.getCompanyDescription());
                    startup.setFoundedDate(updatedStartup.getFoundedDate());
                    startup.setTypeOfCompany(updatedStartup.getTypeOfCompany());
                    startup.setNumberOfEmployees(updatedStartup.getNumberOfEmployees());
                    startup.setPhoneNumber(updatedStartup.getPhoneNumber());
                    startup.setContactEmail(updatedStartup.getContactEmail());
                    startup.setStreetAddress(updatedStartup.getStreetAddress());
                    startup.setCity(updatedStartup.getCity());
                    startup.setStatus(updatedStartup.getStatus());
                    startup.setProvince(updatedStartup.getProvince());
                    startup.setPostalCode(updatedStartup.getPostalCode());
                    startup.setIndustry(updatedStartup.getIndustry());
                    startup.setWebsite(updatedStartup.getWebsite());
                    startup.setFacebook(updatedStartup.getFacebook());
                    startup.setTwitter(updatedStartup.getTwitter());
                    startup.setInstagram(updatedStartup.getInstagram());
                    startup.setLinkedIn(updatedStartup.getLinkedIn());
                    startup.setLocationLat(updatedStartup.getLocationLat());
                    startup.setLocationLng(updatedStartup.getLocationLng());
                    startup.setLocationName(updatedStartup.getLocationName());
                    startup.setStartupCode(updatedStartup.getStartupCode());
                    startup.setRevenue(updatedStartup.getRevenue());
                    startup.setAnnualRevenue(updatedStartup.getAnnualRevenue());
                    startup.setPaidUpCapital(updatedStartup.getPaidUpCapital());
                    startup.setFundingStage(updatedStartup.getFundingStage());
                    startup.setBusinessActivity(updatedStartup.getBusinessActivity());
                    startup.setOperatingHours(updatedStartup.getOperatingHours());
                    startup.setNumberOfActiveStartups(updatedStartup.getNumberOfActiveStartups());
                    startup.setNumberOfNewStartupsThisYear(updatedStartup.getNumberOfNewStartupsThisYear());
                    startup.setAverageStartupGrowthRate(updatedStartup.getAverageStartupGrowthRate());
                    startup.setStartupSurvivalRate(updatedStartup.getStartupSurvivalRate());
                    startup.setTotalStartupFundingReceived(updatedStartup.getTotalStartupFundingReceived());
                    startup.setAverageFundingPerStartup(updatedStartup.getAverageFundingPerStartup());
                    startup.setNumberOfFundingRounds(updatedStartup.getNumberOfFundingRounds());
                    startup.setRegion(updatedStartup.getRegion());
                    startup.setBarangay(updatedStartup.getBarangay());
                    startup.setNumberOfStartupsWithForeignInvestment(
                            updatedStartup.getNumberOfStartupsWithForeignInvestment());
                    startup.setAmountOfGovernmentGrantsOrSubsidiesReceived(
                            updatedStartup.getAmountOfGovernmentGrantsOrSubsidiesReceived());
                    startup.setNumberOfStartupIncubatorsOrAccelerators(
                            updatedStartup.getNumberOfStartupIncubatorsOrAccelerators());
                    startup.setNumberOfStartupsInIncubationPrograms(
                            updatedStartup.getNumberOfStartupsInIncubationPrograms());
                    startup.setNumberOfMentorsOrAdvisorsInvolved(updatedStartup.getNumberOfMentorsOrAdvisorsInvolved());
                    startup.setPublicPrivatePartnershipsInvolvingStartups(
                            updatedStartup.getPublicPrivatePartnershipsInvolvingStartups());
                    startup.setVerificationCode(updatedStartup.getVerificationCode());
                    startup.setEmailVerified(updatedStartup.getEmailVerified());

                    if (updatedStartup.getPhoto() != null) {
                        startup.setPhoto(updatedStartup.getPhoto());
                    }

                    return startupRepository.save(startup);
                })
                .orElseThrow(() -> new RuntimeException("Startup not found with id: " + id));
    }

    public void deleteStartup(Long id) {
        if (!startupRepository.existsById(id)) {
            throw new RuntimeException("Startup not found with id: " + id);
        }
        startupRepository.deleteById(id);
    }

    public List<Startup> searchStartups(String query) {
        return startupRepository.findByCompanyNameContainingIgnoreCase(query);
    }

    public List<Long> getStartupIdsByLoggedInUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {
            throw new RuntimeException("User not authenticated");
        }
        User loggedInUser = (User) authentication.getPrincipal();
        return startupRepository.findByUser_Id(loggedInUser.getId())
                .stream()
                .map(Startup::getId)
                .toList();
    }

    public List<Startup> getStartupsByLoggedInUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {
            throw new RuntimeException("User not authenticated");
        }
        User loggedInUser = (User) authentication.getPrincipal();
        return startupRepository.findByUser_Id(loggedInUser.getId());
    }

    public void sendVerificationEmail(Long startupId, String email) {
        logger.info("Attempting to send verification email for startup ID: {} to email: {}", startupId, email);

        Startup startup = startupRepository.findById(startupId)
                .orElseThrow(() -> {
                    logger.error("Startup not found with id: {}", startupId);
                    return new RuntimeException("Startup not found with id: " + startupId);
                });

        String verificationCode = String.format("%06d", new Random().nextInt(999999));
        startup.setVerificationCode(verificationCode);
        startup.setEmailVerified(false);
        startupRepository.save(startup);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Verify Your Email");
        message.setText("Your verification code is: " + verificationCode);
        try {
            mailSender.send(message);
            logger.info("Verification email sent to {}", email);
        } catch (Exception e) {
            logger.error("Failed to send verification email to {}: {}", email, e.getMessage(), e);
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }

    @Transactional
    public void verifyEmail(Long startupId, String email, String code) {
        Optional<Startup> startupOpt = startupRepository.findByContactEmailAndVerificationCode(email, code);
        if (startupOpt.isEmpty()) {
            throw new RuntimeException("Invalid verification code");
        }

        Startup startup = startupOpt.get();
        if (!startup.getId().equals(startupId)) {
            throw new RuntimeException("Startup ID does not match the provided email and code");
        }

        startup.setEmailVerified(true);
        startup.setVerificationCode(null);
        startupRepository.save(startup);
        logger.info("Email verified for startup ID: {}", startupId);
    }

    public List<Startup> getAllEmailVerifiedStartups() {
        return startupRepository.findAllVerifiedEmailStartups();
    }

    @Transactional
    public Startup approveStartup(Long id) {
        Startup startup = startupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Startup not found with id: " + id));

        startup.setStatus("Approved");
        return startupRepository.save(startup);
    }

    @Transactional
    public Startup rejectStartup(Long id) {
        Startup startup = startupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Startup not found with id: " + id));

        startup.setStatus("Rejected");
        return startupRepository.save(startup);
    }

    public List<Startup> getAllApprovedStartups() {
        return startupRepository.findAllApprovedStartups();
    }

    @Scheduled(cron = "0 0 9 * * ?")
    @Transactional
    public void sendUpdateReminderEmails() {
        logger.info("Checking for startups needing update reminders");
        LocalDateTime sixMonthsAgo = LocalDateTime.now().minusMonths(6); // Change to .minusMinutes(5) for testing
        List<Startup> startups = startupRepository.findStartupsNotUpdatedSince(sixMonthsAgo);

        for (Startup startup : startups) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(startup.getContactEmail());
                message.setSubject("Reminder: Update Your Startup Information");
                message.setText(
                        "Dear " + startup.getCompanyName() + ",\n\n" +
                        "It has been over 6 months since your startup information was last updated. " +
                        "To ensure your data remains relevant and verified, please update your details.\n\n" +
                        "You can update your information by logging into your account and visiting the startup dashboard.\n" +
                        "Link: [Add Frontend URL Link]" +
                        "Thank you,\nStartupSphere Team"
                );
                mailSender.send(message);
                logger.info("Reminder email sent to {} for startup ID: {}", startup.getContactEmail(), startup.getId());
            } catch (Exception e) {
                logger.error("Failed to send reminder email to {}: {}", startup.getContactEmail(), e.getMessage(), e);
            }
        }
    }
}