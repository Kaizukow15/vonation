-- Run in psql:  \c vonation   then   \i check_donations.sql
-- Shows every donation exactly as the Admin / User dashboards read it.
SELECT 'D-' || lpad(id::text, 4, '0') AS donation_id,
       donor, donation_type, item, amount, donated_on, note, status, created_at
FROM donations
ORDER BY id DESC;

-- Total of approved (Processed) cash donations = the "Total Approved Cash" card on the admin page
SELECT COALESCE(SUM(amount), 0) AS approved_cash_total
FROM donations
WHERE donation_type = 'Cash' AND status NOT IN ('Pending', 'Cancelled', 'Canceled');

-- OPTIONAL: wipe old test rows so you start clean (uncomment to use)
-- TRUNCATE donations RESTART IDENTITY;x
