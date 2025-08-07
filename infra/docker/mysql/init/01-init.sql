-- Initialize LifePuzzle Database
CREATE DATABASE IF NOT EXISTS lifepuzzle;

-- Create user if not exists
CREATE USER IF NOT EXISTS 'lifepuzzle'@'%' IDENTIFIED BY 'lifepuzzlepass';

-- Grant privileges
GRANT ALL PRIVILEGES ON lifepuzzle.* TO 'lifepuzzle'@'%';
FLUSH PRIVILEGES;

-- Use the database
USE lifepuzzle;

-- Set timezone
SET time_zone = '+09:00';