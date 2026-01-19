package com.holidayanalyzer.controller;

import com.holidayanalyzer.model.Country;
import com.holidayanalyzer.model.Region;
import com.holidayanalyzer.repository.CountryRepository;
import com.holidayanalyzer.repository.RegionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminCountryController {

    private final CountryRepository countryRepository;
    private final RegionRepository regionRepository;

    public AdminCountryController(CountryRepository countryRepository,
                                  RegionRepository regionRepository) {
        this.countryRepository = countryRepository;
        this.regionRepository = regionRepository;
    }

    // ==================== COUNTRIES ====================

    @PostMapping("/countries")
    public ResponseEntity<Country> addCountry(
            @RequestParam String code,
            @RequestParam String name,
            @RequestParam(required = false) String nameEn,
            @RequestParam(required = false) Long population) {

        // Prüfe ob Land bereits existiert
        if (countryRepository.findByCode(code).isPresent()) {
            throw new IllegalArgumentException("Country with code " + code + " already exists");
        }

        Country country = new Country();
        country.setCode(code.toUpperCase());
        country.setName(name);
        if (population != null) {
            country.setPopulation(population);
        }

        Country saved = countryRepository.save(country);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/countries/{id}")
    public ResponseEntity<Country> updateCountry(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam(required = false) String nameEn,
            @RequestParam(required = false) Long population) {

        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Country not found: " + id));

        country.setName(name);
        if (population != null) {
            country.setPopulation(population);
        }

        Country saved = countryRepository.save(country);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/countries/{id}")
    public ResponseEntity<String> deleteCountry(@PathVariable Long id) {
        // Prüfe ob Regionen existieren
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Country not found: " + id));

        List<Region> regions = regionRepository.findByCountryCode(country.getCode());
        if (!regions.isEmpty()) {
            // Lösche zuerst alle Regionen
            regionRepository.deleteAll(regions);
        }

        countryRepository.deleteById(id);
        return ResponseEntity.ok("Country and " + regions.size() + " regions deleted");
    }

    // ==================== REGIONS ====================

    @PostMapping("/regions")
    public ResponseEntity<Region> addRegion(
            @RequestParam String code,
            @RequestParam String name,
            @RequestParam String countryCode,
            @RequestParam(required = false) Long population) {

        Country country = countryRepository.findByCode(countryCode)
                .orElseThrow(() -> new IllegalArgumentException("Country not found: " + countryCode));

        // Prüfe ob Region bereits existiert
        if (regionRepository.findByCode(code).isPresent()) {
            throw new IllegalArgumentException("Region with code " + code + " already exists");
        }

        Region region = new Region();
        region.setCode(code.toUpperCase());
        region.setName(name);
        region.setCountry(country);
        if (population != null) {
            region.setPopulation(population);
        }

        Region saved = regionRepository.save(region);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/regions/{id}")
    public ResponseEntity<Region> updateRegion(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam(required = false) Long population) {

        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Region not found: " + id));

        region.setName(name);
        if (population != null) {
            region.setPopulation(population);
        }

        Region saved = regionRepository.save(region);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/regions/{id}")
    public ResponseEntity<String> deleteRegion(@PathVariable Long id) {
        regionRepository.deleteById(id);
        return ResponseEntity.ok("Region deleted");
    }
}