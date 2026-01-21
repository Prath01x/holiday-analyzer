package com.holidayanalyzer.controller;

import com.holidayanalyzer.model.Region;
import com.holidayanalyzer.repository.RegionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/regions")
public class RegionController {

    private final RegionRepository regionRepository;

    public RegionController(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    @GetMapping
    public List<Region> getRegions(@RequestParam(required = false) String countryCode) {
        if (countryCode != null && !countryCode.isEmpty()) {
            return regionRepository.findByCountryCode(countryCode);
        }
        return regionRepository.findAll();
    }

    @GetMapping("/{code}")
    public Region getRegionByCode(@PathVariable String code) {
        return regionRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Region not found: " + code));
    }
}
