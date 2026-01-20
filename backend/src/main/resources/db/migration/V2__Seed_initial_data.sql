-- Seed initial data for Holiday Analyzer
-- This migration adds default admin user and sample data

-- Insert default admin user (password: admin123 - BCrypt hashed)
-- Note: In production, this should be created via environment variables
INSERT INTO users (username, password, email, role) 
VALUES ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'admin@example.com', 'ADMIN')
ON CONFLICT (username) DO NOTHING;

-- Note: Additional seed data (countries, subdivisions, holidays) will be loaded
-- by the DataLoader.java component at application startup
-- This keeps the migration files focused on schema and critical data only
