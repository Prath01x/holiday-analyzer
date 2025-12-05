package com.holidayanalyzer.repository;

import com.holidayanalyzer.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Long> {

    Optional<Country> findByCode(String code);
}
