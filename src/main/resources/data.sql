-- Make the size for serialized contexts larger because we need to store lots of date (all file names to scan).
-- ALTER TABLE `maconha-dev`.BATCH_JOB_EXECUTION_CONTEXT MODIFY SERIALIZED_CONTEXT MEDIUMTEXT;
-- ALTER TABLE `maconha-dev`.BATCH_STEP_EXECUTION_CONTEXT MODIFY SERIALIZED_CONTEXT MEDIUMTEXT;

-- Create first administrative user.
INSERT INTO `User` (`admin`, `name`, `password`, `salt`) VALUES (1, 'admin', 'maconha', 'random');
