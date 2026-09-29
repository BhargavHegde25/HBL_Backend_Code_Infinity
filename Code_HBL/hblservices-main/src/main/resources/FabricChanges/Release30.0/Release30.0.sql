Campaign Issues- create and update
-------------------------------------
1. Backup Create Script
=========================================
USE `dbxdb`;
DROP procedure IF EXISTS `create_campaign_proc_bkp`;

USE `dbxdb`;
DROP procedure IF EXISTS `dbxdb`.`create_campaign_proc_bkp`;
;

DELIMITER $$
USE `dbxdb`$$
CREATE DEFINER=`infinitydev`@`%` PROCEDURE `create_campaign_proc_bkp`(
IN eventTriggerIdList MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN profileIdList MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN channelType MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN offlineTemplate MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN onlineContent MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN channelDetails MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN campaignId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN campaignName VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN campaignDescription VARCHAR(100) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN campaignPriority int ,
IN startDate  VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN endDate  VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN campaignType VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN objectiveType VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN productId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN productGroupId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN campaignStatus VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
	
	DECLARE FINISHED INTEGER DEFAULT 0;
	DECLARE campaignIdcur varchar(255) DEFAULT "" ;
	DECLARE campaignPriorityCur varchar(255) DEFAULT "" ;
	DECLARE finalCampaigns VARCHAR(255) DEFAULT "";

	DECLARE campaignscursor CURSOR FOR (select `campaigndefinition`.`campaignId`,`campaigndefinition`.`campaignPriority` from `campaigndefinition` where `campaigndefinition`.`campaignId` COLLATE utf8_general_ci !=  campaignId and `campaigndefinition`.`campaignPriority` COLLATE utf8_general_ci >= campaignPriority order by `campaigndefinition`.`campaignPriority` );
    DECLARE CONTINUE HANDLER
        FOR NOT FOUND SET FINISHED = 1;
    
	  INSERT INTO `campaigndefinition`(`campaignId`, `campaignName`,`campaignDescription`,`objectiveType`,`productId`,`productGroupId`,`campaignPriority`,`campaignType`,`campaignStatus`,`startDate`,`endDate` ) 
	  values (campaignId, campaignName,campaignDescription,objectiveType,productId,productGroupId,campaignPriority,campaignType,campaignStatus,startDate,endDate);
      set @index = 0;
      set @numOfRecords = LENGTH(eventTriggerIdList) - LENGTH(REPLACE(eventTriggerIdList, '|', '')) + 1;
      insertRecords : LOOP
          set @index = @index + 1;
          IF @index = @numOfRecords + 1 or eventTriggerIdList = '' THEN
            LEAVE insertRecords;
          else
            set @id = SUBSTRING_INDEX(SUBSTRING_INDEX(eventTriggerIdList, '|', @index), '|', -1 );
            INSERT INTO `campaigneventtrigger`(`campaignId`, `eventTriggerId`) values (campaignId, @id);
           END IF;
      END LOOP insertRecords;
     
      set @index = 0;
      set @numOfRecords = LENGTH(profileIdList) - LENGTH(REPLACE(profileIdList, '|', '')) + 1;
      insertRecords : LOOP
          set @index = @index + 1;
          IF @index = @numOfRecords + 1 or profileIdList = '' THEN
            LEAVE insertRecords;
          else
            set @profileId = SUBSTRING_INDEX(SUBSTRING_INDEX(profileIdList, '|', @index), '|', -1 );
            INSERT INTO `campaignprofile`(`campaignId`, `profileId`) values (campaignId, @profileId);
           END IF;
      END LOOP insertRecords;
     
      set @index = 0;
      set @numOfRecords = LENGTH(channelType) - LENGTH(REPLACE(channelType, '|', '')) + 1;
      insertRecords : LOOP
          set @index = @index + 1;
          IF @index = @numOfRecords + 1 or channelType = '' THEN
            LEAVE insertRecords;
          else
            set @chnlType = SUBSTRING_INDEX(SUBSTRING_INDEX(channelType, '|', @index), '|', -1 );
            INSERT INTO `campaignchanneltype`(`campaignId`, `channelType`) values (campaignId, @chnlType);
           END IF;
      END LOOP insertRecords;
     
     set @index = 0;
      set @numOfRecords = LENGTH(offlineTemplate) - LENGTH(REPLACE(offlineTemplate, '|', '')) + 1;
      insertRecords : LOOP
          set @index = @index + 1;
          IF @index = @numOfRecords + 1 or offlineTemplate = '' THEN
            LEAVE insertRecords;
          else
            set @recordsData = SUBSTRING_INDEX(SUBSTRING_INDEX(offlineTemplate, '|', @index), '|', -1 );
            set @offlineTemplateId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '$',1 ), '$', -1 );
            set @channelSubType = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '$',2 ), '$', -1 );
            set @subject = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '$',3 ), '$', -1 );
            set @messageContent = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '$',4 ), '$', -1 );
            INSERT INTO `offlinetemplate`(`offlineTemplateId`, `campaignId`,`channelSubType`,`subject`,`content`) values (@offlineTemplateId,campaignId, @channelSubType,@subject,@messageContent);
           END IF;
      END LOOP insertRecords;
     
     set @index = 0;
      set @numOfRecords = LENGTH(onlineContent) - LENGTH(REPLACE(onlineContent, '|', '')) + 1;
      insertRecords : LOOP
          set @index = @index + 1;
          IF @index = @numOfRecords + 1 or onlineContent = '' THEN
            LEAVE insertRecords;
          else
            set @recordsData = SUBSTRING_INDEX(SUBSTRING_INDEX(onlineContent, '|', @index), '|', -1 );
            set @onlineContentId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '$',1 ), '$', -1 );
            set @placeholderId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '$',2 ), '$', -1 );
            set @targetURL = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '$',3 ), '$', -1 );
            set @imageURL = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '$',4 ), '$', -1 );
            set @callToActionButtonLabel = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '$',5 ), '$', -1 );
            set @callToActionTargetURL = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '$',6 ), '$', -1 );
            set @showReadLaterButton = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '$',7 ), '$', -1 );
            set @showCloseIcon = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '$',8 ), '$', -1 );
            set @bannerTitle = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '$',9 ), '$', -1 );
            set @bannerDescription = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '$',10 ), '$', -1 );
            INSERT INTO `onlinecontent`(`onlineContentId`, `targetURL`,`campaignId`,`placeholderId`,`imageURL`,`imageIndex`,`callToActionButtonLabel`,`callToActionTargetURL`,`showReadLaterButton`,`showCloseIcon`,`bannerTitle`,`bannerDescription`) 
           	values 
          	(@onlineContentId, @targetURL,campaignId,@placeholderId,@imageURL,@imageIndex,@callToActionButtonLabel,@callToActionTargetURL,@showReadLaterButton,@showCloseIcon,@bannerTitle,@bannerDescription);
           END IF;
      END LOOP insertRecords;
     
      set @index = 0;
      set @numOfRecords = LENGTH(channelDetails) - LENGTH(REPLACE(channelDetails, '|', '')) + 1;
      insertRecords : LOOP
          set @index = @index + 1;
          IF @index = @numOfRecords + 1 or channelDetails = '' THEN
            LEAVE insertRecords;
          else
            set @recordsData = SUBSTRING_INDEX(SUBSTRING_INDEX(channelDetails, '|', @index), '|', -1 );
            set @channelSubType = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '$',1 ), '$', -1 );
            set @channelPriority = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '$',2 ), '$', -1 );
            INSERT INTO `campaignchanneldetails`(`campaignId`,`channelPriority`,`channelSubType`) values (campaignId,@channelPriority,@channelSubType);
           END IF;
      END LOOP insertRecords;
     
     

set  @index = campaignPriority;
OPEN campaignscursor;
getStatus: LOOP
FETCH campaignscursor INTO campaignIdcur,campaignPriorityCur ;
IF FINISHED = 1 then
	LEAVE getStatus;
else
    if campaignPriorityCur = @index then
    	update `campaigndefinition` set `campaigndefinition`.`campaignPriority` = campaignPriorityCur+1 where `campaigndefinition`.`campaignId` = campaignIdcur;
    	set @index  = @index  + 1;
    else
     	set FINISHED = 0;
    end if;
	
END IF;
END LOOP getStatus;
CLOSE campaignscursor;
     
END$$

DELIMITER ;
;




=============================================
2. New Create Script
===========================================


USE `dbxdb`;
DROP procedure IF EXISTS `create_campaign_proc`;

USE `dbxdb`;
DROP procedure IF EXISTS `dbxdb`.`create_campaign_proc`;
;

DELIMITER $$
USE `dbxdb`$$
CREATE DEFINER=`infinitydev`@`%` PROCEDURE `create_campaign_proc`(
IN eventTriggerIdList MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN profileIdList MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN channelType MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN offlineTemplate MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN onlineContent MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN channelDetails MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,

IN campaignId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN campaignName VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN campaignDescription VARCHAR(100) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN campaignPriority int ,
IN startDate  VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN endDate  VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN campaignType VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN objectiveType VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN productId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN productGroupId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN campaignStatus VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
	
	DECLARE FINISHED INTEGER DEFAULT 0;
	DECLARE campaignIdcur varchar(255) DEFAULT "" ;
	DECLARE campaignPriorityCur varchar(255) DEFAULT "" ;
	DECLARE finalCampaigns VARCHAR(255) DEFAULT "";

	DECLARE campaignscursor CURSOR FOR (select `campaigndefinition`.`campaignId`,`campaigndefinition`.`campaignPriority` from `campaigndefinition` where `campaigndefinition`.`campaignId` !=  campaignId and `campaigndefinition`.`campaignPriority` >= campaignPriority order by `campaigndefinition`.`campaignPriority` );
    DECLARE CONTINUE HANDLER
        FOR NOT FOUND SET FINISHED = 1;
    
	  INSERT INTO `campaigndefinition`(`campaignId`, `campaignName`,`campaignDescription`,`objectiveType`,`productId`,`productGroupId`,`campaignPriority`,`campaignType`,`campaignStatus`,`startDate`,`endDate` ) 
	  values (campaignId, campaignName,campaignDescription,objectiveType,productId,productGroupId,campaignPriority,campaignType,campaignStatus,startDate,endDate);
      set @index = 0;
      set @numOfRecords = LENGTH(eventTriggerIdList) - LENGTH(REPLACE(eventTriggerIdList, '|', '')) + 1;
      insertRecords : LOOP
          set @index = @index + 1;
          IF @index = @numOfRecords + 1 or eventTriggerIdList = '' THEN
            LEAVE insertRecords;
          else
            set @id = SUBSTRING_INDEX(SUBSTRING_INDEX(eventTriggerIdList, '|', @index), '|', -1 );
            INSERT INTO `campaigneventtrigger`(`campaignId`, `eventTriggerId`) values (campaignId, @id);
           END IF;
      END LOOP insertRecords;
     
      set @index = 0;
      set @numOfRecords = LENGTH(profileIdList) - LENGTH(REPLACE(profileIdList, '|', '')) + 1;
      insertRecords : LOOP
          set @index = @index + 1;
          IF @index = @numOfRecords + 1 or profileIdList = '' THEN
            LEAVE insertRecords;
          else
            set @profileId = SUBSTRING_INDEX(SUBSTRING_INDEX(profileIdList, '|', @index), '|', -1 );
            INSERT INTO `campaignprofile`(`campaignId`, `profileId`) values (campaignId, @profileId);
           END IF;
      END LOOP insertRecords;
     
      set @index = 0;
      set @numOfRecords = LENGTH(channelType) - LENGTH(REPLACE(channelType, '|', '')) + 1;
      insertRecords : LOOP
          set @index = @index + 1;
          IF @index = @numOfRecords + 1 or channelType = '' THEN
            LEAVE insertRecords;
          else
            set @chnlType = SUBSTRING_INDEX(SUBSTRING_INDEX(channelType, '|', @index), '|', -1 );
            INSERT INTO `campaignchanneltype`(`campaignId`, `channelType`) values (campaignId, @chnlType);
           END IF;
      END LOOP insertRecords;
     
     set @index = 0;
      set @numOfRecords = LENGTH(offlineTemplate) - LENGTH(REPLACE(offlineTemplate, '|', '')) + 1;
      insertRecords : LOOP
          set @index = @index + 1;
          IF @index = @numOfRecords + 1 or offlineTemplate = '' THEN
            LEAVE insertRecords;
          else
            set @recordsData = SUBSTRING_INDEX(SUBSTRING_INDEX(offlineTemplate, '|', @index), '|', -1 );
            set @offlineTemplateId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '$',1 ), '$', -1 );
            set @channelSubType = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '$',2 ), '$', -1 );
            set @subject = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '$',3 ), '$', -1 );
            set @messageContent = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '$',4 ), '$', -1 );
            INSERT INTO `offlinetemplate`(`offlineTemplateId`, `campaignId`,`channelSubType`,`subject`,`content`) values (@offlineTemplateId,campaignId, @channelSubType,@subject,@messageContent);
           END IF;
      END LOOP insertRecords;
     
     set @index = 0;
      set @numOfRecords = LENGTH(onlineContent) - LENGTH(REPLACE(onlineContent, '|', '')) + 1;
      insertRecords : LOOP
          set @index = @index + 1;
          IF @index = @numOfRecords + 1 or onlineContent = '' THEN
            LEAVE insertRecords;
          else
            set @recordsData = SUBSTRING_INDEX(SUBSTRING_INDEX(onlineContent, '|', @index), '|', -1 );
            set @onlineContentId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '$',1 ), '$', -1 );
            set @placeholderId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '$',2 ), '$', -1 );
            set @targetURL = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '$',3 ), '$', -1 );
            set @imageURL = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '$',4 ), '$', -1 );
            set @callToActionButtonLabel = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '$',5 ), '$', -1 );
            set @callToActionTargetURL = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '$',6 ), '$', -1 );
            set @showReadLaterButton = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '$',7 ), '$', -1 );
            set @showCloseIcon = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '$',8 ), '$', -1 );
            set @bannerTitle = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '$',9 ), '$', -1 );
            set @bannerDescription = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '$',10 ), '$', -1 );
            INSERT INTO `onlinecontent`(`onlineContentId`, `targetURL`,`campaignId`,`placeholderId`,`imageURL`,`imageIndex`,`callToActionButtonLabel`,`callToActionTargetURL`,`showReadLaterButton`,`showCloseIcon`,`bannerTitle`,`bannerDescription`) 
           	values 
          	(@onlineContentId, @targetURL,campaignId,@placeholderId,@imageURL,@imageIndex,@callToActionButtonLabel,@callToActionTargetURL,@showReadLaterButton,@showCloseIcon,@bannerTitle,@bannerDescription);
           END IF;
      END LOOP insertRecords;
     
      set @index = 0;
      set @numOfRecords = LENGTH(channelDetails) - LENGTH(REPLACE(channelDetails, '|', '')) + 1;
      insertRecords : LOOP
          set @index = @index + 1;
          IF @index = @numOfRecords + 1 or channelDetails = '' THEN
            LEAVE insertRecords;
          else
            set @recordsData = SUBSTRING_INDEX(SUBSTRING_INDEX(channelDetails, '|', @index), '|', -1 );
            set @channelSubType = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '$',1 ), '$', -1 );
            set @channelPriority = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '$',2 ), '$', -1 );
            INSERT INTO `campaignchanneldetails`(`campaignId`,`channelPriority`,`channelSubType`) values (campaignId,@channelPriority,@channelSubType);
           END IF;
      END LOOP insertRecords;
     
     

set  @index = campaignPriority;
OPEN campaignscursor;
getStatus: LOOP
FETCH campaignscursor INTO campaignIdcur,campaignPriorityCur ;
IF FINISHED = 1 then
	LEAVE getStatus;
else
    if campaignPriorityCur = @index then
    	update `campaigndefinition` set `campaigndefinition`.`campaignPriority` = campaignPriorityCur+1 where `campaigndefinition`.`campaignId` = campaignIdcur;
    	set @index  = @index  + 1;
    else
     	set FINISHED = 0;
    end if;
	
END IF;
END LOOP getStatus;
CLOSE campaignscursor;
     
END$$

DELIMITER ;
;


=====================================
1.Bkp Update Scripts
=====================================

USE `dbxdb`;
DROP procedure IF EXISTS `dbxdb`.`update_campaign_proc_bkp`;
;

DELIMITER $$
USE `dbxdb`$$
CREATE DEFINER=`infinitydev`@`%` PROCEDURE `update_campaign_proc_bkp`(
IN eventTriggerIdList MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN profileIdList MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN channelType MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN offlineTemplate MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN onlineContent MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN channelDetails MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,

IN campaignId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN campaignName VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN campaignDescription VARCHAR(100) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN campaignPriority int ,
IN startDate  VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN endDate  VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN campaignType VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN objectiveType VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN productId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN productGroupId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN campaignStatus VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
	
	delete from `campaigneventtrigger` where `campaigneventtrigger`.`campaignId`  COLLATE utf8_general_ci = campaignId;
	
	delete from `campaignprofile` where `campaignprofile`.`campaignId`  COLLATE utf8_general_ci   = campaignId;
	delete from `campaignchanneltype` where `campaignchanneltype`.`campaignId`  COLLATE utf8_general_ci  =  campaignId;
	delete from `offlinetemplate` where `offlinetemplate`.`campaignId`   COLLATE utf8_general_ci   =  campaignId;
	delete from `onlinecontent` where `onlinecontent`.`campaignId`  COLLATE utf8_general_ci   =  campaignId;
	delete from `campaignchanneldetails` where `campaignchanneldetails`.`campaignId`  COLLATE utf8_general_ci   =  campaignId;
	delete from `campaigndefinition` where `campaigndefinition`.`campaignId`   COLLATE utf8_general_ci  =  campaignId;
	 call create_campaign_proc(eventTriggerIdList,profileIdList,channelType,offlineTemplate,onlineContent,channelDetails,
 campaignId,campaignName,campaignDescription,campaignPriority,startDate,endDate,campaignType,objectiveType,productId,productGroupId,campaignStatus);
     

END$$

DELIMITER ;
;



===============================================
2. New Update Script
==========================================

USE `dbxdb`;
DROP procedure IF EXISTS `dbxdb`.`update_campaign_proc`;
;

DELIMITER $$
USE `dbxdb`$$
CREATE DEFINER=`infinitydev`@`%` PROCEDURE `update_campaign_proc`(
IN eventTriggerIdList MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN profileIdList MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN channelType MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN offlineTemplate MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN onlineContent MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN channelDetails MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,

IN campaignId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN campaignName VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN campaignDescription VARCHAR(100) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN campaignPriority int ,
IN startDate  VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN endDate  VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN campaignType VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN objectiveType VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN productId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN productGroupId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN campaignStatus VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
	
	delete from `campaigneventtrigger` where `campaigneventtrigger`.`campaignId` = campaignId;
	
	delete from `campaignprofile` where `campaignprofile`.`campaignId` = campaignId;
	delete from `campaignchanneltype` where `campaignchanneltype`.`campaignId`  =  campaignId;
	delete from `offlinetemplate` where `offlinetemplate`.`campaignId` =  campaignId;
	delete from `onlinecontent` where `onlinecontent`.`campaignId`  =  campaignId;
	delete from `campaignchanneldetails` where `campaignchanneldetails`.`campaignId` =  campaignId;
	delete from `campaigndefinition` where `campaigndefinition`.`campaignId` =  campaignId;
	 call create_campaign_proc(eventTriggerIdList,profileIdList,channelType,offlineTemplate,onlineContent,channelDetails,
 campaignId,campaignName,campaignDescription,campaignPriority,startDate,endDate,campaignType,objectiveType,productId,productGroupId,campaignStatus);
     

END$$

DELIMITER ;
;


