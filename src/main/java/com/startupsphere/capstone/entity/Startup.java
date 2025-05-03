package com.startupsphere.capstone.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "startups")
public class Startup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "startup", cascade = CascadeType.ALL)
    private List<Like> likes;

    @JsonIgnore
    @OneToMany(mappedBy = "startup", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Bookmarks> bookmarks = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "startup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Views> views = new ArrayList<>();

    @Column(name = "views_count", nullable = false)
    private Integer viewsCount = 0;

    private String companyName;
    private String companyDescription;
    private String foundedDate;
    private String typeOfCompany;
    private String numberOfEmployees;
    private String phoneNumber;
    private String contactEmail;
    private String streetAddress;
    private String country;
    private String city;
    private String province;
    private String postalCode;
    private String industry;
    private String website;
    private String facebook;
    private String twitter;
    private String instagram;
    private String linkedIn;
    private Double locationLat;
    private Double locationLng;
    private String locationName;
    private String startupCode;

    // New attributes
    private double revenue;
    private Double annualRevenue;
    private Double paidUpCapital;
    private String fundingStage;
    private String businessActivity;
    private String operatingHours;
    private int numberOfActiveStartups;
    private int numberOfNewStartupsThisYear;
    private double averageStartupGrowthRate;
    private double startupSurvivalRate;
    private double totalStartupFundingReceived;
    private double averageFundingPerStartup;
    private int numberOfFundingRounds;
    private int numberOfStartupsWithForeignInvestment;
    private double amountOfGovernmentGrantsOrSubsidiesReceived;
    private int numberOfStartupIncubatorsOrAccelerators;
    private int numberOfStartupsInIncubationPrograms;
    private int numberOfMentorsOrAdvisorsInvolved;
    private int publicPrivatePartnershipsInvolvingStartups;

    // Email verification fields
    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified = false;

    // Default constructor
    public Startup() {
    }

    // All-args constructor
    public Startup(Long id, User user, List<Like> likes, List<Bookmarks> bookmarks, Integer viewsCount,
                   String companyName, String companyDescription, String foundedDate, String typeOfCompany,
                   String numberOfEmployees, String phoneNumber, String contactEmail, String streetAddress,
                   String country, String city, String province, String postalCode, String industry, String website,
                   String facebook, String twitter, String instagram, String linkedIn, Double locationLat,
                   Double locationLng, String locationName, String startupCode, double revenue, Double annualRevenue,
                   Double paidUpCapital, String fundingStage, String businessActivity, String operatingHours,
                   int numberOfActiveStartups, int numberOfNewStartupsThisYear, double averageStartupGrowthRate,
                   double startupSurvivalRate, double totalStartupFundingReceived, double averageFundingPerStartup,
                   int numberOfFundingRounds, int numberOfStartupsWithForeignInvestment,
                   double amountOfGovernmentGrantsOrSubsidiesReceived, int numberOfStartupIncubatorsOrAccelerators,
                   int numberOfStartupsInIncubationPrograms, int numberOfMentorsOrAdvisorsInvolved,
                   int publicPrivatePartnershipsInvolvingStartups, String verificationCode, Boolean emailVerified) {
        this.id = id;
        this.user = user;
        this.likes = likes;
        this.bookmarks = bookmarks;
        this.viewsCount = viewsCount;
        this.companyName = companyName;
        this.companyDescription = companyDescription;
        this.foundedDate = foundedDate;
        this.typeOfCompany = typeOfCompany;
        this.numberOfEmployees = numberOfEmployees;
        this.phoneNumber = phoneNumber;
        this.contactEmail = contactEmail;
        this.streetAddress = streetAddress;
        this.country = country;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        this.industry = industry;
        this.website = website;
        this.facebook = facebook;
        this.twitter = twitter;
        this.instagram = instagram;
        this.linkedIn = linkedIn;
        this.locationLat = locationLat;
        this.locationLng = locationLng;
        this.locationName = locationName;
        this.startupCode = startupCode;
        this.revenue = revenue;
        this.annualRevenue = annualRevenue;
        this.paidUpCapital = paidUpCapital;
        this.fundingStage = fundingStage;
        this.businessActivity = businessActivity;
        this.operatingHours = operatingHours;
        this.numberOfActiveStartups = numberOfActiveStartups;
        this.numberOfNewStartupsThisYear = numberOfNewStartupsThisYear;
        this.averageStartupGrowthRate = averageStartupGrowthRate;
        this.startupSurvivalRate = startupSurvivalRate;
        this.totalStartupFundingReceived = totalStartupFundingReceived;
        this.averageFundingPerStartup = averageFundingPerStartup;
        this.numberOfFundingRounds = numberOfFundingRounds;
        this.numberOfStartupsWithForeignInvestment = numberOfStartupsWithForeignInvestment;
        this.amountOfGovernmentGrantsOrSubsidiesReceived = amountOfGovernmentGrantsOrSubsidiesReceived;
        this.numberOfStartupIncubatorsOrAccelerators = numberOfStartupIncubatorsOrAccelerators;
        this.numberOfStartupsInIncubationPrograms = numberOfStartupsInIncubationPrograms;
        this.numberOfMentorsOrAdvisorsInvolved = numberOfMentorsOrAdvisorsInvolved;
        this.publicPrivatePartnershipsInvolvingStartups = publicPrivatePartnershipsInvolvingStartups;
        this.verificationCode = verificationCode;
        this.emailVerified = emailVerified;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public List<Bookmarks> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(List<Bookmarks> bookmarks) {
        this.bookmarks = bookmarks;
    }

    public Integer getViewsCount() {
        return (viewsCount == null) ? 0 : viewsCount;
    }

    public void setViewsCount(Integer viewsCount) {
        this.viewsCount = viewsCount;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    public String getFoundedDate() {
        return foundedDate;
    }

    public void setFoundedDate(String foundedDate) {
        this.foundedDate = foundedDate;
    }

    public String getTypeOfCompany() {
        return typeOfCompany;
    }

    public void setTypeOfCompany(String typeOfCompany) {
        this.typeOfCompany = typeOfCompany;
    }

    public String getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public void setNumberOfEmployees(String numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getLinkedIn() {
        return linkedIn;
    }

    public void setLinkedIn(String linkedIn) {
        this.linkedIn = linkedIn;
    }

    public Double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(Double locationLat) {
        this.locationLat = locationLat;
    }

    public Double getLocationLng() {
        return locationLng;
    }

    public void setLocationLng(Double locationLng) {
        this.locationLng = locationLng;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getStartupCode() {
        return startupCode;
    }

    public void setStartupCode(String startupCode) {
        this.startupCode = startupCode;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public Double getAnnualRevenue() {
        return annualRevenue;
    }

    public void setAnnualRevenue(Double annualRevenue) {
        this.annualRevenue = annualRevenue;
    }

    public Double getPaidUpCapital() {
        return paidUpCapital;
    }

    public void setPaidUpCapital(Double paidUpCapital) {
        this.paidUpCapital = paidUpCapital;
    }

    public String getFundingStage() {
        return fundingStage;
    }

    public void setFundingStage(String fundingStage) {
        this.fundingStage = fundingStage;
    }

    public String getBusinessActivity() {
        return businessActivity;
    }

    public void setBusinessActivity(String businessActivity) {
        this.businessActivity = businessActivity;
    }

    public String getOperatingHours() {
        return operatingHours;
    }

    public void setOperatingHours(String operatingHours) {
        this.operatingHours = operatingHours;
    }

    public int getNumberOfActiveStartups() {
        return numberOfActiveStartups;
    }

    public void setNumberOfActiveStartups(int numberOfActiveStartups) {
        this.numberOfActiveStartups = numberOfActiveStartups;
    }

    public int getNumberOfNewStartupsThisYear() {
        return numberOfNewStartupsThisYear;
    }

    public void setNumberOfNewStartupsThisYear(int numberOfNewStartupsThisYear) {
        this.numberOfNewStartupsThisYear = numberOfNewStartupsThisYear;
    }

    public double getAverageStartupGrowthRate() {
        return averageStartupGrowthRate;
    }

    public void setAverageStartupGrowthRate(double averageStartupGrowthRate) {
        this.averageStartupGrowthRate = averageStartupGrowthRate;
    }

    public double getStartupSurvivalRate() {
        return startupSurvivalRate;
    }

    public void setStartupSurvivalRate(double startupSurvivalRate) {
        this.startupSurvivalRate = startupSurvivalRate;
    }

    public double getTotalStartupFundingReceived() {
        return totalStartupFundingReceived;
    }

    public void setTotalStartupFundingReceived(double totalStartupFundingReceived) {
        this.totalStartupFundingReceived = totalStartupFundingReceived;
    }

    public double getAverageFundingPerStartup() {
        return averageFundingPerStartup;
    }

    public void setAverageFundingPerStartup(double averageFundingPerStartup) {
        this.averageFundingPerStartup = averageFundingPerStartup;
    }

    public int getNumberOfFundingRounds() {
        return numberOfFundingRounds;
    }

    public void setNumberOfFundingRounds(int numberOfFundingRounds) {
        this.numberOfFundingRounds = numberOfFundingRounds;
    }

    public int getNumberOfStartupsWithForeignInvestment() {
        return numberOfStartupsWithForeignInvestment;
    }

    public void setNumberOfStartupsWithForeignInvestment(int numberOfStartupsWithForeignInvestment) {
        this.numberOfStartupsWithForeignInvestment = numberOfStartupsWithForeignInvestment;
    }

    public double getAmountOfGovernmentGrantsOrSubsidiesReceived() {
        return amountOfGovernmentGrantsOrSubsidiesReceived;
    }

    public void setAmountOfGovernmentGrantsOrSubsidiesReceived(double amountOfGovernmentGrantsOrSubsidiesReceived) {
        this.amountOfGovernmentGrantsOrSubsidiesReceived = amountOfGovernmentGrantsOrSubsidiesReceived;
    }

    public int getNumberOfStartupIncubatorsOrAccelerators() {
        return numberOfStartupIncubatorsOrAccelerators;
    }

    public void setNumberOfStartupIncubatorsOrAccelerators(int numberOfStartupIncubatorsOrAccelerators) {
        this.numberOfStartupIncubatorsOrAccelerators = numberOfStartupIncubatorsOrAccelerators;
    }

    public int getNumberOfStartupsInIncubationPrograms() {
        return numberOfStartupsInIncubationPrograms;
    }

    public void setNumberOfStartupsInIncubationPrograms(int numberOfStartupsInIncubationPrograms) {
        this.numberOfStartupsInIncubationPrograms = numberOfStartupsInIncubationPrograms;
    }

    public int getNumberOfMentorsOrAdvisorsInvolved() {
        return numberOfMentorsOrAdvisorsInvolved;
    }

    public void setNumberOfMentorsOrAdvisorsInvolved(int numberOfMentorsOrAdvisorsInvolved) {
        this.numberOfMentorsOrAdvisorsInvolved = numberOfMentorsOrAdvisorsInvolved;
    }

    public int getPublicPrivatePartnershipsInvolvingStartups() {
        return publicPrivatePartnershipsInvolvingStartups;
    }

    public void setPublicPrivatePartnershipsInvolvingStartups(int publicPrivatePartnershipsInvolvingStartups) {
        this.publicPrivatePartnershipsInvolvingStartups = publicPrivatePartnershipsInvolvingStartups;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }
}