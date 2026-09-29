ALTER TABLE `adminactivity` ADD COLUMN `eventData` text DEFAULT NULL after `modulename`;
ALTER TABLE `adminactivity` MODIFY `userRole` VARCHAR(500);