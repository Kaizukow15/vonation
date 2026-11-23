-- voNation PostgreSQL schema. The app does NOT create tables by itself: run this file once in the database
-- (pgAdmin > Query Tool > open schema.sql > Execute). It is safe to run again: nothing is dropped or deleted.
-- Create the database once:  CREATE DATABASE vonation;

CREATE TABLE IF NOT EXISTS users (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'User',
    phone VARCHAR(20) NOT NULL DEFAULT '',
    birth_date DATE,
    address TEXT NOT NULL DEFAULT '',
    created_at TIMESTAMPTZ NOT NULL DEFAULT now());

-- For a database that already has the users table (saves the address from Edit Profile):
ALTER TABLE users ADD COLUMN IF NOT EXISTS address TEXT NOT NULL DEFAULT '';

CREATE TABLE IF NOT EXISTS donations (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    donor VARCHAR(100) NOT NULL,
    donation_type VARCHAR(20) NOT NULL,
    item TEXT NOT NULL DEFAULT '',
    amount NUMERIC(12,2) NOT NULL DEFAULT 0 CHECK (amount >= 0),
    donated_on DATE NOT NULL DEFAULT CURRENT_DATE,
    note TEXT NOT NULL DEFAULT '',
    status VARCHAR(20) NOT NULL DEFAULT 'Pending',
    created_at TIMESTAMPTZ NOT NULL DEFAULT now());

CREATE INDEX IF NOT EXISTS idx_donations_donor ON donations (lower(donor));

CREATE TABLE IF NOT EXISTS events (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    event_date DATE NOT NULL,
    time_slot VARCHAR(60) NOT NULL DEFAULT '',
    location VARCHAR(200) NOT NULL DEFAULT '',
    category VARCHAR(40) NOT NULL DEFAULT '',
    status VARCHAR(30) NOT NULL DEFAULT 'Upcoming',
    photo_path TEXT NOT NULL DEFAULT '',
    initial_joined INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now());

-- For a database that already has the events table (adds the column the admin's "Initial Joined Volunteers" is saved in):
ALTER TABLE events ADD COLUMN IF NOT EXISTS initial_joined INTEGER NOT NULL DEFAULT 0;

CREATE TABLE IF NOT EXISTS event_registrations (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_id INTEGER NOT NULL REFERENCES events (id) ON DELETE CASCADE,
    user_email VARCHAR(150) NOT NULL,
    full_name VARCHAR(150) NOT NULL DEFAULT '',
    contact VARCHAR(30) NOT NULL DEFAULT '',
    status VARCHAR(20) NOT NULL DEFAULT 'Pending',
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(), 
    UNIQUE (event_id, user_email));

CREATE TABLE IF NOT EXISTS volunteers (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_email VARCHAR(150) NOT NULL,
    full_name VARCHAR(150) NOT NULL,
    contact VARCHAR(30) NOT NULL DEFAULT '',
    address TEXT NOT NULL DEFAULT '',
    role VARCHAR(40) NOT NULL DEFAULT '',
    availability VARCHAR(30) NOT NULL DEFAULT '',
    skills TEXT NOT NULL DEFAULT '',
    notes TEXT NOT NULL DEFAULT '',
    status VARCHAR(20) NOT NULL DEFAULT 'Pending',
    created_at TIMESTAMPTZ NOT NULL DEFAULT now());

CREATE INDEX IF NOT EXISTS idx_volunteers_email ON volunteers (lower(user_email));


-- Cash donations (campaigns) that the admin sets up and users donate to.
CREATE TABLE IF NOT EXISTS campaigns (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT NOT NULL DEFAULT '',
    goal_amount NUMERIC(12,2) NOT NULL DEFAULT 0 CHECK (goal_amount >= 0),
    photo_path TEXT NOT NULL DEFAULT '',
    gcash_name VARCHAR(150) NOT NULL DEFAULT '',
    gcash_number VARCHAR(30) NOT NULL DEFAULT '',
    walkin_location VARCHAR(250) NOT NULL DEFAULT '',
    walkin_days VARCHAR(100) NOT NULL DEFAULT '',
    walkin_from VARCHAR(20) NOT NULL DEFAULT '',
    walkin_to VARCHAR(20) NOT NULL DEFAULT '',
    status VARCHAR(20) NOT NULL DEFAULT 'Active',
    campaign_type VARCHAR(20) NOT NULL DEFAULT 'Cash',
    items_needed TEXT NOT NULL DEFAULT '',
    category VARCHAR(40) NOT NULL DEFAULT 'Cash',
    created_at TIMESTAMPTZ NOT NULL DEFAULT now());

-- For a database that already has the campaigns table (adds Cash / In-Kind and the items the admin asks for):
ALTER TABLE campaigns ADD COLUMN IF NOT EXISTS campaign_type VARCHAR(20) NOT NULL DEFAULT 'Cash';
ALTER TABLE campaigns ADD COLUMN IF NOT EXISTS items_needed TEXT NOT NULL DEFAULT '';
-- Category chips: Cash, Education, Relief Funds, Clothes, General Goods, Others
ALTER TABLE campaigns ADD COLUMN IF NOT EXISTS category VARCHAR(40) NOT NULL DEFAULT 'Cash';

-- Links a donation to the campaign it was given to (run after campaigns exists).
ALTER TABLE donations ADD COLUMN IF NOT EXISTS campaign_id INTEGER REFERENCES campaigns (id) ON DELETE SET NULL;
