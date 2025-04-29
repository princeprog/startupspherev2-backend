package com.startupsphere.capstone.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Startup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToMany(mappedBy = "startup", cascade = CascadeType.ALL)
    private List<Like> likes;

    @JsonIgnore
    @OneToMany(mappedBy = "startup", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Bookmarks> bookmarks = new ArrayList<>();

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

    // Default constructor
    public Startup() {
    }

    // All-args constructor
    public Startup(Long id, String companyName, String companyDescription, String foundedDate, String typeOfCompany,
                   String numberOfEmployees, String phoneNumber, String contactEmail, String streetAddress,
                   String country, String city, String province, String postalCode, String industry, String website,
                   String facebook, String twitter, String instagram, String linkedIn, Double locationLat,
                   Double locationLng, String locationName, String startupCode) {
        this.id = id;
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
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public List<Like> getLikes() { return likes; }
    public void setLikes(List<Like> likes) { this.likes = likes; }

    public List<Bookmarks> getBookmarks() { return bookmarks; }
    public void setBookmarks(List<Bookmarks> bookmarks) { this.bookmarks = bookmarks; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getCompanyDescription() { return companyDescription; }
    public void setCompanyDescription(String companyDescription) { this.companyDescription = companyDescription; }

    public String getFoundedDate() { return foundedDate; }
    public void setFoundedDate(String foundedDate) { this.foundedDate = foundedDate; }

    public String getTypeOfCompany() { return typeOfCompany; }
    public void setTypeOfCompany(String typeOfCompany) { this.typeOfCompany = typeOfCompany; }

    public String getNumberOfEmployees() { return numberOfEmployees; }
    public void setNumberOfEmployees(String numberOfEmployees) { this.numberOfEmployees = numberOfEmployees; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public String getStreetAddress() { return streetAddress; }
    public void setStreetAddress(String streetAddress) { this.streetAddress = streetAddress; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    public String getFacebook() { return facebook; }
    public void setFacebook(String facebook) { this.facebook = facebook; }

    public String getTwitter() { return twitter; }
    public void setTwitter(String twitter) { this.twitter = twitter; }

    public String getInstagram() { return instagram; }
    public void setInstagram(String instagram) { this.instagram = instagram; }

    public String getLinkedIn() { return linkedIn; }
    public void setLinkedIn(String linkedIn) { this.linkedIn = linkedIn; }

    public Double getLocationLat() { return locationLat; }
    public void setLocationLat(Double locationLat) { this.locationLat = locationLat; }

    public Double getLocationLng() { return locationLng; }
    public void setLocationLng(Double locationLng) { this.locationLng = locationLng; }

    public String getLocationName() { return locationName; }
    public void setLocationName(String locationName) { this.locationName = locationName; }

    public String getStartupCode() { return startupCode; }
    public void setStartupCode(String startupCode) { this.startupCode = startupCode; }
}
