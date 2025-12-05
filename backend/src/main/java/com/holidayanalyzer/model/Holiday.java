package com.holidayanalyzer.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "holidays")
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String localName;

    @Column(nullable = false)
    private String englishName;

    @Column(nullable = false, length = 5)
    private String countryCode;

    @Column(nullable = false)
    private boolean globalHoliday;

    @Column
    private String types;

    @Column
    private String subdivisionCodes; // comma-separated ISO 3166-2 codes, e.g. "DE-BW,DE-BY"

    @Column(nullable = false)
    private int year;

    public Holiday() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public boolean isGlobalHoliday() {
        return globalHoliday;
    }

    public void setGlobalHoliday(boolean globalHoliday) {
        this.globalHoliday = globalHoliday;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getSubdivisionCodes() {
        return subdivisionCodes;
    }

    public void setSubdivisionCodes(String subdivisionCodes) {
        this.subdivisionCodes = subdivisionCodes;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
