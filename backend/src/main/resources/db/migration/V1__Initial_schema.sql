-- Initial database schema for Holiday Analyzer
-- This migration creates all tables and relationships

-- Create countries table
CREATE TABLE IF NOT EXISTS countries (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    code VARCHAR(2) NOT NULL UNIQUE,
    population BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create regions table (states, regions, cantons, etc.)
CREATE TABLE IF NOT EXISTS regions (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(10) NOT NULL,
    population BIGINT NOT NULL,
    country_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_region_country FOREIGN KEY (country_id) REFERENCES countries(id) ON DELETE CASCADE,
    CONSTRAINT uk_region_code_country UNIQUE (code, country_id)
);

-- Create holidays table (public holidays)
CREATE TABLE IF NOT EXISTS holidays (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    date DATE NOT NULL,
    is_national BOOLEAN DEFAULT FALSE,
    country_id BIGINT NOT NULL,
    subdivision_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_holiday_country FOREIGN KEY (country_id) REFERENCES countries(id) ON DELETE CASCADE,
    CONSTRAINT fk_holiday_region FOREIGN KEY (subdivision_id) REFERENCES regions(id) ON DELETE CASCADE
);

-- Create school_holidays table (vacation periods)
CREATE TABLE IF NOT EXISTS school_holidays (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    subdivision_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_school_holiday_region FOREIGN KEY (subdivision_id) REFERENCES regions(id) ON DELETE CASCADE,
    CONSTRAINT chk_school_holiday_dates CHECK (end_date >= start_date)
);

-- Create users table (for admin authentication)
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_holidays_date ON holidays(date);
CREATE INDEX IF NOT EXISTS idx_holidays_country ON holidays(country_id);
CREATE INDEX IF NOT EXISTS idx_holidays_region ON holidays(subdivision_id);
CREATE INDEX IF NOT EXISTS idx_school_holidays_dates ON school_holidays(start_date, end_date);
CREATE INDEX IF NOT EXISTS idx_school_holidays_region ON school_holidays(subdivision_id);
CREATE INDEX IF NOT EXISTS idx_regions_country ON regions(country_id);
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);

-- Add comments for documentation
COMMENT ON TABLE countries IS 'European countries with population data';
COMMENT ON TABLE regions IS 'Administrative regions (states, länder, cantons) within countries';
COMMENT ON TABLE holidays IS 'Public holidays (national and regional)';
COMMENT ON TABLE school_holidays IS 'School vacation periods by region';
COMMENT ON TABLE users IS 'Application users for admin authentication';
