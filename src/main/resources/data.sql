
-- Create first administrative user, if not already exists.
INSERT INTO `User` (`admin`, `name`, `password`, `salt`)
SELECT * FROM (SELECT 1, 'admin', 'maconha', 'random') AS tmp
WHERE NOT EXISTS (
    SELECT name FROM `User` WHERE name = 'admin'
) LIMIT 1;
