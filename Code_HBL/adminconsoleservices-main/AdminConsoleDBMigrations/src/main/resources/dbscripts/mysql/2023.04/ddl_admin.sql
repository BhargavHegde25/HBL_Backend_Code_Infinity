ALTER TABLE `bbrequest` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `bbactedrequest` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';

ALTER TABLE `archivedcustomerrequest`
ADD COLUMN `isTypeBusiness` varchar(45) DEFAULT NULL AFTER softdeleteflag,
ADD COLUMN `companyLegalUnit` varchar(50) NOT NULL DEFAULT 'ALL' AFTER isTypeBusiness;

ALTER TABLE `archivedrequestmessage`
ADD COLUMN `frominternaluser` tinyint(1) NOT NULL DEFAULT '0' AFTER softdeleteflag,
ADD COLUMN `isPriorityMessage` varchar(20) DEFAULT '0' AFTER frominternaluser,
ADD COLUMN `companyLegalUnit` varchar(50) NOT NULL DEFAULT 'ALL' AFTER isPriorityMessage;
 
ALTER TABLE `archivedmessageattachment`
ADD COLUMN `companyLegalUnit` varchar(50) NOT NULL DEFAULT 'ALL' AFTER softdeleteflag;
 
ALTER TABLE `archivedmedia`
ADD COLUMN `companyLegalUnit` varchar(50) NOT NULL DEFAULT 'ALL' AFTER softdeleteflag;

DROP PROCEDURE IF EXISTS `getRequestApprovers_proc`;

DELIMITER $$
CREATE PROCEDURE `getRequestApprovers_proc`(
	IN _requestId TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _status TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

	IF _status IS NULL OR _status = '' THEN
		SET @isGroupMatrix = (select isGroupMatrix from bbrequest where requestId = _requestId);
		IF @isGroupMatrix = '1' THEN
			select _requestId AS requestId, (select companyLegalUnit from bbrequest where requestId = _requestId) as `companyLegalUnit`, bb.companyLegalUnit, csg.customerId AS approvers, c.FirstName, c.LastName FROM customersignatorygroup as csg 
			LEFT JOIN customer AS c ON (c.id = csg.customerId)
			where FIND_IN_SET(csg.signatoryGroupId,
			(SELECT group_concat(REPLACE(REPLACE(REPLACE(pendingGroupList,']',''),'[',''),'"',''))
				FROM signatorygrouprequestmatrix WHERE 
				signatorygrouprequestmatrix.requestId = _requestId AND signatorygrouprequestmatrix.isApproved = false)); 
        ELSE
            SELECT bb.requestId, bb.companyLegalUnit AS companyLegalUnit, cam.customerId AS approvers, c.FirstName, c.LastName FROM bbrequest AS bb JOIN requestapprovalmatrix AS ram JOIN customerapprovalmatrix AS cam JOIN customer AS c WHERE bb.requestId = ram.requestId AND ram.approvalMatrixId = cam.approvalMatrixId AND cam.customerId = c.id AND bb.requestId = _requestId GROUP BY approvers;
		END IF;
	ELSE
		SELECT bb.createdby AS approvers, bb.companyLegalUnit AS companyLegalUnit, c.FirstName, c.LastName FROM bbactedrequest AS bb JOIN customer AS c WHERE bb.createdby = c.id AND requestId = _requestId AND status = _status GROUP BY approvers;
	END IF;

END $$
DELIMITER ;

drop procedure if exists `log_bbactedrequest_proc`;

DELIMITER $$
create procedure `log_bbactedrequest_proc`(IN _input VARCHAR(1000))
BEGIN
	set @requestId = JSON_UNQUOTE(JSON_EXTRACT(_input, '$.requestId'));
    set @companyId = JSON_UNQUOTE(JSON_EXTRACT(_input, '$.companyId'));
    set @statusId = JSON_UNQUOTE(JSON_EXTRACT(_input, '$.status'));
    set @comments = JSON_UNQUOTE(JSON_EXTRACT(_input, '$.comments'));
    set @createdby = JSON_UNQUOTE(JSON_EXTRACT(_input, '$.createdby'));
    set @actionId = JSON_UNQUOTE(JSON_EXTRACT(_input, '$.action'));
    set @groupName = JSON_UNQUOTE(JSON_EXTRACT(_input, '$.groupName'));
    set @legalEntityId = '';
    select companyLegalUnit into @legalEntityId from bbrequest where requestId = @requestId;
    INSERT INTO `bbactedrequest`(`requestId`, `companyId`, `status`, `comments`, `createdby`, `action`, `groupName`, `companyLegalUnit`) VALUES (@requestId, @companyId, @statusId, @comments, @createdby, @actionId, @groupName, @legalEntityId);
END $$
DELIMITER ;

/* SQL Scripts for InfinityWealth - Strategy - Model Constraint */
DROP TABLE IF EXISTS `inf_wlth_model_constraint`;
CREATE TABLE `inf_wlth_model_constraint` (
  `portfolioId` VARCHAR(50) NOT NULL,
  `portfolioCode` VARCHAR(50) NOT NULL,
  `customerId` VARCHAR(50) NOT NULL,  
  `constraintId` VARCHAR(50),  
  `createdby` VARCHAR(50) NULL DEFAULT NULL,
  `modifiedby` VARCHAR(50) NULL DEFAULT NULL,
  `createdts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`portfolioId`),
  CONSTRAINT `FK_model_constraint_customer_id` FOREIGN KEY (`customerId`) REFERENCES `customer` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `channeltext` (`channelID`, `LanguageCode`, `Description`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('CH_NOTIFICATION_CENTER', 'en-GB', 'Notification Center', 'Kony User', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `channeltext` (`channelID`, `LanguageCode`, `Description`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('CH_PUSH_NOTIFICATION', 'en-GB', 'Push Notification', 'Kony User', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `channeltext` (`channelID`, `LanguageCode`, `Description`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('CH_SMS', 'en-GB', 'SMS/Text', 'Kony User', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `channeltext` (`channelID`, `LanguageCode`, `Description`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('CH_EMAIL', 'en-GB', 'Email', 'Kony User', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

delete from `groupactionlimit` where `id`  = '428f5248-7cf8-11ea-bc55-0242ac130003';
delete from `groupactionlimit` where `id`  = '1b548792-3d55-11ea-acf5-00090faa0001';
delete from `groupactionlimit` where `id`  = '1b550ab0-3d55-11ea-acf5-00090faa0001';
delete from `groupactionlimit` where `id`  = '1b564773-3d55-11ea-acf5-00090faa0001';
delete from `groupactionlimit` where `id`  = '1b56c8fd-3d55-11ea-acf5-00090faa0001';
delete from `groupactionlimit` where `Group_id`  = 'GROUP_CREATOR' and `Action_id` = 'ADD_USER_ANOTHER_ENTITY';
