package com.holidayanalyzer.controller;

import com.holidayanalyzer.dto.VacationLoadResponse;
import com.holidayanalyzer.service.VacationLoadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vacation-load")
public class VacationLoadController {

    private final VacationLoadService vacationLoadService;

    public VacationLoadController(VacationLoadService vacationLoadService) {
        this.vacationLoadService = vacationLoadService;
    }

    @GetMapping
    public ResponseEntity<VacationLoadResponse> getVacationLoad(
            @RequestParam(defaultValue = "DE") String countryCode,
            @RequestParam int year) {
        VacationLoadResponse response = vacationLoadService.calculateVacationLoad(countryCode, year);
        return ResponseEntity.ok(response);
    }
}
