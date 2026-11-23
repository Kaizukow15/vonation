-- voNation: ONE-TIME fix for event joins that were saved with the login email as the name.
-- Run in pgAdmin (Query Tool) on the "vonation" database. Run STEP 1 first and check the rows, then run STEP 2.
-- Only rows whose saved name is the email (or empty) AND whose user has a volunteer registration are changed.
-- The contact number the user typed is left as it is.

-- STEP 1 - PREVIEW (changes nothing). registered_name is the Full Name from the volunteer registration.
SELECT r.id,
       r.event_id,
       r.user_email,
       r.full_name AS saved_name,
       (SELECT v.full_name
          FROM volunteers v
         WHERE lower(v.user_email) = lower(r.user_email) AND v.status <> 'Rejected'
         ORDER BY v.id DESC LIMIT 1) AS registered_name
FROM event_registrations r
WHERE lower(btrim(r.full_name)) = lower(r.user_email) OR btrim(r.full_name) = ''
ORDER BY r.id;

-- STEP 2 - FIX
UPDATE event_registrations r
SET full_name = (SELECT v.full_name
                   FROM volunteers v
                  WHERE lower(v.user_email) = lower(r.user_email) AND v.status <> 'Rejected'
                  ORDER BY v.id DESC LIMIT 1)
WHERE (lower(btrim(r.full_name)) = lower(r.user_email) OR btrim(r.full_name) = '')
  AND EXISTS (SELECT 1 FROM volunteers v
               WHERE lower(v.user_email) = lower(r.user_email) AND v.status <> 'Rejected');

-- STEP 3 - CHECK (should list nothing, or only users who never registered as a volunteer)
SELECT id, event_id, user_email, full_name
FROM event_registrations
WHERE lower(btrim(full_name)) = lower(user_email) OR btrim(full_name) = '';
