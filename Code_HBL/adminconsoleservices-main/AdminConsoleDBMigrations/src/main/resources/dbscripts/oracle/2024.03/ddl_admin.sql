CREATE TABLE  "placeholder"("placeholderId" VARCHAR2(255), "application" VARCHAR2(255),"channelSubType" VARCHAR2(255),"placeholderDescription" varchar2(255),"placeholderIdentifier" varchar2(255),"placeholderName" varchar2(255),"imageResolution" VARCHAR2(255),"imageScale" varchar2(255),"imageSize" varchar2(255), PRIMARY KEY ("placeholderId"));
CREATE TABLE  "onlinecontent"("onlineContentId" VARCHAR2(255), "targetURL" varchar2(255), "campaignId" varchar2(255), "placeholderId" varchar2(255), "imageURL" varchar2(255), "imageIndex" number(10), "callToActionButtonLabel" varchar2(255), "callToActionTargetURL" varchar2(255), "showReadLaterButton" varchar2(255), "showCloseIcon" varchar2(255), "bannerTitle" varchar2(255), "bannerDescription" varchar2(255), PRIMARY KEY ("onlineContentId"));
CREATE TABLE  "datacontext"("dataContextId" varchar2(255), "dataContextName" varchar2(255), "dataContextDescription" varchar2(255), "dataContextSource" varchar2(255), "dataContextServiceName" varchar2(255), "dataContextEndPoints" varchar2(255), PRIMARY KEY ("dataContextId"));
CREATE TABLE  "eventtriggers"("eventTriggerId" varchar2(255), "eventCode" varchar2(255), "eventDescription"  varchar2(255), "eventName" varchar2(255), "eventSource" varchar2(255), "eventTriggerType" varchar2(255), PRIMARY KEY ("eventTriggerId")); 

CREATE TABLE "campaigndefinition"
  (
			"campaignId"		VARCHAR2(255 CHAR) NOT NULL,
			"campaignName"		VARCHAR2(255 CHAR),
			"campaignDescription"		VARCHAR2(255 CHAR),
			"objectiveType"		VARCHAR2(255 CHAR),
			"productId"		VARCHAR2(255 CHAR),
			"productGroupId"		VARCHAR2(255 CHAR),
			"campaignPriority"		NUMBER(10),
			"campaignType"		VARCHAR2(255 CHAR),
			"campaignStatus"		VARCHAR2(255 CHAR),
			"startDate"		TIMESTAMP(6),
			"endDate"		TIMESTAMP(6),
			PRIMARY KEY ("campaignId")
 );

CREATE TABLE "campaignchanneltype"
(
		"campaignId"		VARCHAR2(255 CHAR) NOT NULL ,
		"channelType"		VARCHAR2(255 CHAR) 
);
CREATE TABLE "campaignchanneldetails"
(
		"campaignId"		VARCHAR2(255 CHAR) ,
		"channelPriority"		NUMBER(10) ,
		"channelSubType"		VARCHAR2(255 CHAR) 
);
CREATE TABLE "campaigneventtrigger"
(
		"campaignId"		VARCHAR2(255 CHAR) NOT NULL ,
		"eventTriggerId"		VARCHAR2(255 CHAR) NOT NULL 
);
CREATE TABLE "campaignprofile"
(
		"campaignId"	VARCHAR2(255 CHAR) NOT NULL ,
		"profileId"		VARCHAR2(255 CHAR) NOT NULL 
);

CREATE TABLE "profile"
  (
			"profileId"		VARCHAR2(255 CHAR) NOT NULL,
			"profileName"		VARCHAR2(255 CHAR),
			"profileDescription"		VARCHAR2(255 CHAR),
			"profileStatus"		VARCHAR2(255 CHAR),
			"numberOfUsers"		NUMBER(10),
			"profileCreationDate"		TIMESTAMP(6),
			"profileDeactivatedDate"		TIMESTAMP(6),
			PRIMARY KEY ("profileId")
 );


CREATE TABLE "profilecondition"
  (
			"profileId"		VARCHAR2(255 CHAR),
			"dataContextId"		VARCHAR2(255 CHAR),
			"profileConditionId"		VARCHAR2(255 CHAR) NOT NULL,
			"conditionExpression"		VARCHAR2(255 CHAR),
			PRIMARY KEY ("profileConditionId")
 );

CREATE TABLE "offlinetemplate"
  (
			"offlineTemplateId"		VARCHAR2(255 CHAR) NOT NULL,
			"campaignId"		VARCHAR2(255 CHAR),
			"channelSubType"		VARCHAR2(255 CHAR),
			"subject"		VARCHAR2(255 CHAR),
			"content"		CLOB,
			PRIMARY KEY ("offlineTemplateId")
 );
 
CREATE INDEX "campaign_campaignId_idx" ON "campaigndefinition" ("campaignId");
CREATE INDEX "profile_profileId_idx" ON "profile" ("profileId");
CREATE INDEX "profilecondition_profileConditionId_idx" ON "profilecondition" ("profileConditionId");
CREATE INDEX "placeholder_placeholderId_idx" ON "placeholder" ("placeholderId");
CREATE INDEX "datacontext_dataContextId_idx" ON "datacontext" ("dataContextId");
CREATE INDEX "offlinetemplate_offlineTemplateId_idx" ON "offlinetemplate" ("offlineTemplateId");
CREATE INDEX "eventtriggers_eventTriggerId_idx" ON "eventtriggers" ("eventTriggerId");
CREATE INDEX "onlinecontent_onlineContentId_idx" ON "onlinecontent" ("onlineContentId");

ALTER TABLE "campaigneventtrigger" ADD CONSTRAINT "campaigneventtrigger_campaignId_fk"  FOREIGN KEY ("campaignId") REFERENCES "campaigndefinition" ("campaignId");
ALTER TABLE "campaigneventtrigger" ADD CONSTRAINT "campaigneventtrigger_eventTriggerId_fk"  FOREIGN KEY ("eventTriggerId") REFERENCES "eventtriggers" ("eventTriggerId");
ALTER TABLE "campaignprofile" ADD CONSTRAINT "campaignprofile_campaignId_fk"  FOREIGN KEY ("campaignId") REFERENCES "campaigndefinition" ("campaignId");
ALTER TABLE "campaignprofile" ADD CONSTRAINT "campaignprofile_profileId_fk"  FOREIGN KEY ("profileId") REFERENCES "profile" ("profileId");
ALTER TABLE "profilecondition" ADD CONSTRAINT "profilecondition_profileId_fk"  FOREIGN KEY ("profileId") REFERENCES "profile" ("profileId");
ALTER TABLE "profilecondition" ADD CONSTRAINT "profilecondition_dataContextId_fk"  FOREIGN KEY ("dataContextId") REFERENCES "datacontext" ("dataContextId");
ALTER TABLE "offlinetemplate" ADD CONSTRAINT "offlinetemplate_campaignId_fk"  FOREIGN KEY ("campaignId") REFERENCES "campaigndefinition" ("campaignId");
ALTER TABLE "onlinecontent" ADD CONSTRAINT "onlinecontent_campaignId_fk"  FOREIGN KEY ("campaignId") REFERENCES "campaigndefinition" ("campaignId");
ALTER TABLE "onlinecontent" ADD CONSTRAINT "onlinecontent_placeholderId_fk"  FOREIGN KEY ("placeholderId") REFERENCES "placeholder" ("placeholderId");



CREATE TABLE financialinstitutiontype (
  finInstitutionTypeId varchar2(50 char) NOT NULL,
  description varchar2(50 char) NOT NULL,
  "level" varchar2(50 char) NOT NULL,
  prefix varchar2(50 char) NOT NULL,
  createdts timestamp(0) DEFAULT SYSTIMESTAMP NOT NULL,
  modifiedts timestamp(0) DEFAULT SYSTIMESTAMP NOT NULL,
   softdeleteflag raw(1) DEFAULT b NOT NULL '0',
  PRIMARY KEY (finInstitutionTypeId)
) ;

-- SQLINES LICENSE FOR EVALUATION USE ONLY
CREATE TABLE financialinstitution (
  finInstitutionId varchar2(50 char) NOT NULL,
  name varchar2(50 char) NOT NULL,
  shortName varchar2(50 char) NOT NULL,
  typeId varchar2(50 char) NOT NULL,
  parentId varchar2(50 char) NOT NULL,
  countryCode varchar2(50 char) NOT NULL,
  baseCurrency varchar2(50 char) NOT NULL,
  language varchar2(50 char) NOT NULL,
  effectiveDate timestamp(0) NOT NULL,
  closeDate timestamp(0) NOT NULL,
  comments varchar2(50 char) NOT NULL,
  createdts timestamp(0) DEFAULT SYSTIMESTAMP NOT NULL,
  modifiedts timestamp(0) DEFAULT SYSTIMESTAMP NOT NULL,
   softdeleteflag raw(1) DEFAULT b NOT NULL '0',
  PRIMARY KEY (finInstitutionId),
  CONSTRAINT FK_financialinstitution_TypeId FOREIGN KEY (typeId) REFERENCES financialinstitutiontype (finInstitutionTypeId),
  CONSTRAINT FK_financialinstitution_CountryCode FOREIGN KEY (countryCode) REFERENCES country (id)
  
) ;

-- SQLINES LICENSE FOR EVALUATION USE ONLY
CREATE TABLE organisationalunittype (
  organisationUnitTypeId varchar2(50 char) NOT NULL,
  description varchar2(50 char) NOT NULL,
  sharing varchar2(50 char) NOT NULL,
  createdts timestamp(0) DEFAULT SYSTIMESTAMP NOT NULL,
  modifiedts timestamp(0) DEFAULT SYSTIMESTAMP NOT NULL,
  softdeleteflag raw(1) DEFAULT b NOT NULL '0',
  PRIMARY KEY (organisationUnitTypeId)
) ;

-- SQLINES LICENSE FOR EVALUATION USE ONLY
CREATE TABLE organisationunit (
  organisationalUnitId varchar2(50 char) NOT NULL,
  name varchar2(50 char) NOT NULL,
  shortName varchar2(50 char) NOT NULL,
  usageType varchar2(50 char) NOT NULL,
  createdts timestamp(0) DEFAULT SYSTIMESTAMP NOT NULL,
  modifiedts timestamp(0) DEFAULT SYSTIMESTAMP NOT NULL,
  softdeleteflag raw(1) DEFAULT b NOT NULL '0',
  PRIMARY KEY (organisationalUnitId)
) ;

-- SQLINES LICENSE FOR EVALUATION USE ONLY
CREATE TABLE financialinstitutionrelationship (
  relationshipId varchar2(50 char) NOT NULL,
  legalEntityId varchar2(50 char) NOT NULL,
  relatedLegalEntityId varchar2(50 char) NOT NULL,
  relationshipType varchar2(50 char) NOT NULL,
  createdts timestamp(0) DEFAULT SYSTIMESTAMP NOT NULL,
  modifiedts timestamp(0) DEFAULT SYSTIMESTAMP NOT NULL,
  softdeleteflag raw(1) DEFAULT b NOT NULL '0',
  PRIMARY KEY (relationshipId),
  CONSTRAINT FK_financialinstitutionrelationship_LegalEntityId FOREIGN KEY (legalEntityId) REFERENCES financialinstitution (finInstitutionId),
  CONSTRAINT FK_financialinstitutionrelationship_RelatedLegalEntityId FOREIGN KEY (relatedLegalEntityId) REFERENCES financialinstitution (finInstitutionId)
) ;

-- SQLINES LICENSE FOR EVALUATION USE ONLY
CREATE TABLE financialinstitutionaltkey (
  alternateName varchar2(50 char) NOT NULL,
  alternateKey varchar2(50 char) NOT NULL,
  entityId varchar2(50 char) NOT NULL,
  createdts timestamp(0) DEFAULT SYSTIMESTAMP NOT NULL,
  modifiedts timestamp(0) DEFAULT SYSTIMESTAMP NOT NULL,
  softdeleteflag raw(1) DEFAULT b NOT NULL '0',
  PRIMARY KEY (alternateName, alternateKey),
  CONSTRAINT FK_financialinstitutionaltkey_EntityId FOREIGN KEY (entityId) REFERENCES financialinstitution (finInstitutionId)
) ;


-- SQLINES LICENSE FOR EVALUATION USE ONLY
CREATE TABLE financialinstitutionorganisationunits (
  finInstitutionId varchar2(50 char) NOT NULL,
  organisationalUnitId varchar2(50 char) NOT NULL,
  createdts timestamp(0) DEFAULT SYSTIMESTAMP NOT NULL,
  modifiedts timestamp(0) DEFAULT SYSTIMESTAMP NOT NULL,
  softdeleteflag raw(1) DEFAULT b NOT NULL '0',
  PRIMARY KEY (finInstitutionId, organisationalUnitId),
  CONSTRAINT FK_financialinstitutionorganisationunits_FinInstitutionId FOREIGN KEY (finInstitutionId) REFERENCES financialinstitution (finInstitutionId),
    CONSTRAINT FK_financialinstitutionorganisationunits_OrganisationalUnitId FOREIGN KEY (organisationalUnitId) REFERENCES organisationunit (organisationalUnitId)

) ;


-- SQLINES LICENSE FOR EVALUATION USE ONLY
CREATE TABLE organisationalunittypemapping (
  organisationalUnitId varchar2(50 char) NOT NULL,
  organisationUnitTypeId varchar2(50 char) NOT NULL,
  createdts timestamp(0) DEFAULT SYSTIMESTAMP NOT NULL,
  modifiedts timestamp(0) DEFAULT SYSTIMESTAMP NOT NULL,
  softdeleteflag raw(1) DEFAULT b NOT NULL '0',
  PRIMARY KEY (organisationalUnitId, organisationUnitTypeId),
  CONSTRAINT FK_organisationalunittypemapping_FinInstitutionId FOREIGN KEY (organisationalUnitId) REFERENCES organisationunit (organisationalUnitId),
    CONSTRAINT FK_organisationalunittypemapping_OrganisationalUnitId FOREIGN KEY (organisationUnitTypeId) REFERENCES organisationalunittype (organisationUnitTypeId)

) ;




CREATE OR REPLACE NONEDITIONABLE PROCEDURE "create_campaign_proc"(
v_eventTriggerIdList VARCHAR2(max);
v_profileIdList VARCHAR2(max);
v_channelType VARCHAR2(max);
v_offlineTemplate VARCHAR2(max);
v_onlineContent VARCHAR2(max);
v_channelDetails VARCHAR2(max);

v_campaignId  VARCHAR2(50);
v_campaignName  VARCHAR2(50);
v_campaignDescription VARCHAR2(100);
v_campaignPriority NUMBER(10; 0) ;
v_startDate   VARCHAR2(50);
v_endDate   VARCHAR2(50);
v_campaignType  VARCHAR2(50);
v_objectiveType  VARCHAR2(50);
v_productId  VARCHAR2(50);
v_productGroupId  VARCHAR2(50);
v_campaignStatus  VARCHAR2(50);
)
BEGIN
	
 FINISHED NUMBER(10) DEFAULT 0;
 campaignIdcur varchar2(255 char) DEFAULT "" ;
 campaignPriorityCur varchar2(255 char) DEFAULT "" ;
 finalCampaigns VARCHAR2(255 CHAR) DEFAULT "";

	CURSOR campaignscursor IS (select campaigndefinition.campaignId,campaigndefinition.campaignPriority from campaigndefinition where campaigndefinition.campaignId !=  campaignId and campaigndefinition.campaignPriority >= campaignPriority order by campaigndefinition.campaignPriority );
       
	  
  	INSERT INTO campaigndefinition(campaignId, campaignName,campaignDescription,objectiveType,productId,productGroupId,campaignPriority,campaignType,campaignStatus,startDate,endDate ) 
	  values (campaignId, campaignName,campaignDescription,objectiveType,productId,productGroupId,campaignPriority,campaignType,campaignStatus,startDate,endDate);
      @index := 0;
      @numOfRecords := LENGTH(eventTriggerIdList) - LENGTH(REPLACE(eventTriggerIdList, '|', '')) + 1;
      <<insertRecords>> LOOP
          @index := @index + 1;
          IF @index = @numOfRecords + 1 THEN
            EXIT insertRecords;
          else
            v_id := SUBSTRING_INDEX(SUBSTRING_INDEX(eventTriggerIdList, '|', @index), '|', -1 );
            
            INSERT INTO campaigneventtrigger(campaignId, eventTriggerId) values (campaignId, v_id);
           END IF;
      END LOOP insertRecords;
     
      @index := 0;
      @numOfRecords := LENGTH(profileIdList) - LENGTH(REPLACE(profileIdList, '|', '')) + 1;
      <<insertRecords>> LOOP
          @index := @index + 1;
          IF @index = @numOfRecords + 1 THEN
            EXIT insertRecords;
          else
            v_profileId := SUBSTRING_INDEX(SUBSTRING_INDEX(profileIdList, '|', @index), '|', -1 );
            
            INSERT INTO campaignprofile(campaignId, profileId) values (campaignId, v_profileId);
           END IF;
      END LOOP insertRecords;
     
      @index := 0;
      @numOfRecords := LENGTH(channelType) - LENGTH(REPLACE(channelType, '|', '')) + 1;
      <<insertRecords>> LOOP
          @index := @index + 1;
          IF @index = @numOfRecords + 1 THEN
            EXIT insertRecords;
          else
            v_chnlType := SUBSTRING_INDEX(SUBSTRING_INDEX(channelType, '|', @index), '|', -1 );
            
            INSERT INTO campaignchanneltype(campaignId, channelType) values (campaignId, v_chnlType);
           END IF;
      END LOOP insertRecords;
     
     @index := 0;
      @numOfRecords := LENGTH(offlineTemplate) - LENGTH(REPLACE(offlineTemplate, '|', '')) + 1;
      <<insertRecords>> LOOP
          @index := @index + 1;
          IF @index = @numOfRecords + 1 THEN
            EXIT insertRecords;
          else
            v_recordsData := SUBSTRING_INDEX(SUBSTRING_INDEX(offlineTemplate, '|', @index), '|', -1 );
            v_offlineTemplateId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, '$',1 ), '$', -1 );
            v_channelSubType := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, '$',2 ), '$', -1 );
            v_subject := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, '$',3 ), '$', -1 );
            v_messageContent := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, '$',4 ), '$', -1 );
            
            INSERT INTO offlinetemplate(offlineTemplateId, campaignId,channelSubType,subject,content) values (v_offlineTemplateId,campaignId, v_channelSubType,v_subject,v_messageContent);
           END IF;
      END LOOP insertRecords;
     
     @index := 0;
      @numOfRecords := LENGTH(onlineContent) - LENGTH(REPLACE(onlineContent, '|', '')) + 1;
      <<insertRecords>> LOOP
          @index := @index + 1;
          IF @index = @numOfRecords + 1 THEN
            EXIT insertRecords;
          else
            v_recordsData := SUBSTRING_INDEX(SUBSTRING_INDEX(onlineContent, '|', @index), '|', -1 );
            v_onlineContentId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, '$',1 ), '$', -1 );
            v_placeholderId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, '$',2 ), '$', -1 );
            v_targetURL := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, '$',3 ), '$', -1 );
            v_imageURL := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, '$',4 ), '$', -1 );
            v_callToActionButtonLabel := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, '$',5 ), '$', -1 );
            v_callToActionTargetURL := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, '$',6 ), '$', -1 );
            v_showReadLaterButton := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, '$',7 ), '$', -1 );
            v_showCloseIcon := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, '$',8 ), '$', -1 );
            v_bannerTitle := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, '$',9 ), '$', -1 );
            v_bannerDescription := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, '$',10 ), '$', -1 );
            
            INSERT INTO onlinecontent(onlineContentId, targetURL,campaignId,placeholderId,imageURL,imageIndex,callToActionButtonLabel,callToActionTargetURL,showReadLaterButton,showCloseIcon,bannerTitle,bannerDescription) 
           	values 
          	(v_onlineContentId, v_targetURL,campaignId,v_placeholderId,v_imageURL,@imageIndex,v_callToActionButtonLabel,v_callToActionTargetURL,v_showReadLaterButton,v_showCloseIcon,v_bannerTitle,v_bannerDescription);
           END IF;
      END LOOP insertRecords;
     
      @index := 0;
      @numOfRecords := LENGTH(channelDetails) - LENGTH(REPLACE(channelDetails, '|', '')) + 1;
      <<insertRecords>> LOOP
          @index := @index + 1;
          IF @index = @numOfRecords + 1 THEN
            EXIT insertRecords;
          else
            v_recordsData := SUBSTRING_INDEX(SUBSTRING_INDEX(channelDetails, '|', @index), '|', -1 );
            v_channelSubType := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, '$',1 ), '$', -1 );
            v_channelPriority := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, '$',2 ), '$', -1 );
            
            INSERT INTO campaignchanneldetails(campaignId,channelPriority,channelSubType) values (campaignId,v_channelPriority,v_channelSubType);
           END IF;
      END LOOP insertRecords;
     
     

 @index := campaignPriority;
OPEN campaignscursor;
<<getStatus>> LOOP
FETCH campaignscursor INTO campaignIdcur,campaignPriorityCur ;
IF campaignscursor%NOTFOUND THEN
        FINISHED := 1;
END IF;
IF FINISHED = 1 then
	EXIT getStatus;
else
    if campaignPriorityCur = @index then
    	update campaigndefinition set campaigndefinition.campaignPriority = campaignPriorityCur+1 where campaigndefinition.campaignId = campaignIdcur;
	    if sql%rowcount = 0 then 
        FINISHED := 1;
	    end if;
    	v_index  := @index  + 1;
    else
     	FINISHED := 0;
    end if;
	
END IF;
END LOOP getStatus;
CLOSE campaignscursor;
     
END



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "GetAllProductGroups_Campaign_proc"()
BEGIN
	
	select distinct "pg"."productGroupId", "pg"."productGroupName" from "productInformation" "pinf" 
	jov_"productGroup" "pg" on ("pinf"."productGroupRef" = "pg"."productGroupRef" )
	where "pinf"."purposeData" like '%Campaigns%';
END


CREATE OR REPLACE NONEDITIONABLE PROCEDURE "getProductsByProductGroup_proc"(
v_productGroups VARCHAR2(50);
)
BEGIN
	select "pinf"."productId","pinf"."productName", "pg"."productGroupId", "pg"."productGroupName" from "productInformation" "pinf" 
	jov_"productGroup" "pg" on ("pinf"."productGroupRef" = "pg"."productGroupRef" )
	where "pg"."productGroupId" COLLATE utf8_general_ci = productGroups and "pinf"."purposeData" like '%Campaigns%';

END



create or replace NONEDITIONABLE PROCEDURE "approvalmatrix_create_proc" (
  "_matrixValues" IN NVARCHAR2, "_approverIds" IN NVARCHAR2
) AS v_index1 NUMBER(10, 0) := 0;
v_length NUMBER(19, 0);
v_index2 NUMBER(10, 0) := 0;
v_id NUMBER(10, 0);
v_customerIds NVARCHAR2(2000);
v_customerIdsComma NVARCHAR2(2000);
v_length2 NUMBER(10, 0);
v_customerId NVARCHAR2(100);
v_matrixRecord NVARCHAR2(2000);
v_matrixComma VARCHAR2(4000);
v_query NVARCHAR2(2000);
identity_value NUMBER(10);
BEGIN v_length := LENGTH("_matrixValues") - LENGTH(
  REPLACE("_matrixValues", ',', '')
) + 1;
<< loop_2 >> WHILE (1 = 1) LOOP BEGIN v_index1 := v_index1 + 1;
IF v_index1 = v_length + 1 THEN EXIT;
ELSE BEGIN v_matrixRecord := SUBSTRING_INDEX(
  SUBSTRING_INDEX("_matrixValues", ',', v_index1), 
  ',', 
  -1
);
v_matrixComma := REPLACE(v_matrixRecord, ';', ',');
v_matrixComma := REPLACE(v_matrixComma, '"', '''');
v_query := (
  '
                                 INSERT into "approvalmatrix"(
                                    "approvalmatrix"."name",
                                    "approvalmatrix"."contractId",
                        "approvalmatrix"."coreCustomerId",
                                    "approvalmatrix"."actionId",
                                    "approvalmatrix"."accountId",
                                    "approvalmatrix"."approvalruleId",
                                    "approvalmatrix"."limitTypeId",
                                    "approvalmatrix"."lowerlimit",
                                    "approvalmatrix"."upperlimit",
                                    "approvalmatrix"."currency"
                                 ) VALUES ('
) || (v_matrixComma) || (')');
EXECUTE IMMEDIATE v_query;
select 
  "id" INTO identity_value 
from 
  "approvalmatrix" 
where 
  rowid =(
    select 
      max(rowid) 
    from 
      "approvalmatrix"
  );
v_id := identity_value;
v_customerIds := SUBSTRING_INDEX("_approverIds", ',', v_index1);
if(v_customerIds is not null) then
begin
v_customerIds := SUBSTRING_INDEX(
  v_customerIds,
  ',', 
  -1
);
v_customerIdsComma := REPLACE(v_customerIds, ';', ',');
v_length2 := LENGTH(v_customerIdsComma) - LENGTH(
  REPLACE(v_customerIdsComma, ',', '')
) + 1;
v_index2 := 0;
<< loop_1 >> WHILE (1 = 1) LOOP BEGIN v_index2 := v_index2 + 1;
IF v_index2 = v_length2 + 1 THEN EXIT;
ELSE BEGIN v_customerId := SUBSTRING_INDEX(
  SUBSTRING_INDEX(
    v_customerIdsComma, ',', v_index2
  ), 
  ',', 
  -1
);
INSERT INTO "customerapprovalmatrix" (
  "customerapprovalmatrix"."customerId", 
  "customerapprovalmatrix"."approvalMatrixId"
) 
VALUES 
  (v_customerId, v_id);
GOTO loop_1;
END;
END IF;
END;
END LOOP;
GOTO loop_2;
END;
END IF;
END;
END IF;
END LOOP;

END LOOP;
END;
/


create or replace NONEDITIONABLE PROCEDURE "approvalmatrix_default_create_proc" (
  "_actionIds" IN VARCHAR2, "_contractId" IN VARCHAR2, 
  "_accountIds" IN VARCHAR2, "_cif" IN VARCHAR2, 
  records OUT SYS_REFCURSOR
) AS v_accountList CLOB := '0';
v_limitTypeId_1 VARCHAR2(255) := 'DAILY_LIMIT';
v_limitTypeId_2 VARCHAR2(255) := 'MAX_TRANSACTION_LIMIT';
v_limitTypeId_3 VARCHAR2(255) := 'WEEKLY_LIMIT';
v_accountIndex NUMBER(10, 0) := 0;
v_actionIndex NUMBER(10, 0) := 0;
v_typeId CLOB := '';
v_numOfAccounts NUMBER(10, 0);
v_numOfActions NUMBER(10, 0);
v_accountId VARCHAR2(50);
v_actionId VARCHAR2(255);
BEGIN IF "_actionIds" IS NULL 
OR "_actionIds" = '' THEN RETURN;
END IF;
IF "_contractId" IS NULL 
OR "_contractId" = '' THEN RETURN;
END IF;
IF "_cif" IS NULL 
OR "_cif" = '' THEN RETURN;
END IF;
IF "_accountIds" IS NULL 
OR "_accountIds" = '' THEN RETURN;
END IF;
v_numOfAccounts := LENGTH("_accountIds") - LENGTH(
  REPLACE("_accountIds", ',', ' ')
) || 1;
v_numOfActions := LENGTH("_actionIds") - LENGTH(
  REPLACE("_actionIds", ',', ' ')
) || 1;
<< getAccount >> WHILE 1 = 1 LOOP BEGIN v_accountIndex := v_accountIndex + 1;
IF v_accountIndex = v_numOfAccounts + 1 THEN BEGIN EXIT;
END;
ELSE BEGIN v_accountId := UTILS.SUBSTRING_INDEX(
  UTILS.SUBSTRING_INDEX(
    "_accountIds", ',', v_accountIndex
  ), 
  ',', 
  -1
);
v_actionIndex := 0;
<< getAction >> WHILE 1 = 1 LOOP BEGIN v_actionIndex := v_actionIndex + 1;
IF v_actionIndex = v_numOfActions + 1 THEN BEGIN EXIT;
END;
ELSE BEGIN v_actionId := UTILS.SUBSTRING_INDEX(
  UTILS.SUBSTRING_INDEX("_actionIds", ',', v_actionIndex), 
  ',', 
  -1
);
SELECT 
  "Type_id" INTO v_typeId 
FROM 
  "featureaction" 
WHERE 
  "id" = v_actionId;
IF v_typeId = 'MONETARY' THEN BEGIN INSERT INTO "approvalmatrix" (
  "contractId", "name", "accountId", 
  "actionId", "limitTypeId", "coreCustomerId", 
  "approvalruleId"
) 
VALUES 
  (
    "_contractId", 
    (
      v_actionId || '_' || v_accountId || '_' || v_limitTypeId_1 || '_' || "_contractId"
    ), 
    v_accountId, 
    v_actionId, 
    v_limitTypeId_1, 
    "_cif", 
    'NO_APPROVAL'
  );
INSERT INTO "approvalmatrix" (
  "contractId", "name", "accountId", 
  "actionId", "limitTypeId", "coreCustomerId", 
  "approvalruleId"
) 
VALUES 
  (
    "_contractId", 
    (
      v_actionId || '_' || v_accountId || '_' || v_limitTypeId_2 || '_' || "_contractId"
    ), 
    v_accountId, 
    v_actionId, 
    v_limitTypeId_2, 
    "_cif", 
    'NO_APPROVAL'
  );
INSERT INTO "approvalmatrix" (
  "contractId", "name", "accountId", 
  "actionId", "limitTypeId", "coreCustomerId", 
  "approvalruleId"
) 
VALUES 
  (
    "_contractId", 
    (
      v_actionId || '_' || v_accountId || '_' || v_limitTypeId_3 || '_' || "_contractId"
    ), 
    v_accountId, 
    v_actionId, 
    v_limitTypeId_3, 
    "_cif", 
    'NO_APPROVAL'
  );
END;
ELSE IF v_typeId = 'NON_MONETARY' THEN BEGIN INSERT INTO "approvalmatrix" (
  "contractId", "name", "accountId", 
  "actionId", "limitTypeId", "coreCustomerId", 
  "approvalruleId"
) 
VALUES 
  (
    "_contractId", 
    (
      v_actionId || '_' || v_accountId || '_' || 'NON_MONETARY_LIMIT' || '_' || "_contractId"
    ), 
    v_accountId, 
    v_actionId, 
    'NON_MONETARY_LIMIT', 
    "_cif", 
    'NO_APPROVAL'
  );
END;
END IF;
END IF;
END;
END IF;
END;
END LOOP;
v_accountList := (
  v_accountId || ',' || v_accountList
);
END;
END IF;
END;
END LOOP;
SELECT 
  SUBSTR(
    v_accountList, 
    1, 
    (
      LENGTH(v_accountList) -1
    )
  ) INTO v_accountList 
FROM 
  DUAL;
OPEN records FOR 
SELECT 
  v_accountList accountList 
FROM 
  DUAL;
--DBMS_SQL.RETURN_RESULT(v_cursor);
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;

/

--  DDL for Procedure approvalmatrix_default_delete_proc
create or replace NONEDITIONABLE PROCEDURE "approvalmatrix_default_delete_proc" (
  "_contractId" IN VARCHAR2, "_cif" IN VARCHAR2, 
  "_filterColumnIds" IN VARCHAR2, "_filterColumnName" IN VARCHAR2
) AS BEGIN --   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
IF "_filterColumnName" = 'actionId' THEN BEGIN DELETE "approvalmatrix" 
WHERE 
  "approvalmatrix"."contractId" = "_contractId" 
  AND "approvalmatrix"."coreCustomerId" = "_cif" 
  AND "approvalmatrix"."actionId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT("_filterColumnIds")
      )
  );
END;
ELSE IF "_filterColumnName" = 'accountId' THEN BEGIN DELETE "approvalmatrix" 
WHERE 
  "approvalmatrix"."contractId" = "_contractId" 
  AND "approvalmatrix"."coreCustomerId" = "_cif" 
  AND "approvalmatrix"."accountId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT("_filterColumnIds")
      )
  );
END;
ELSE IF "_filterColumnName" = 'cif' THEN BEGIN DELETE "approvalmatrix" 
WHERE 
  "approvalmatrix"."contractId" = "_contractId" 
  AND "approvalmatrix"."coreCustomerId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT("_filterColumnIds")
      )
  );
END;
END IF;
END IF;
END IF;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;

/


--  DDL for Procedure approvalmatrix_signatorygroupmatrixcreate_proc


create or replace NONEDITIONABLE PROCEDURE "approvalmatrix_signatorygroupmatrixcreate_proc"
(
  "_matrixValues" IN NVARCHAR2,
  "_signatorymatrixValues" IN NVARCHAR2
)
AS
   v_index1 NUMBER(10,0) := 0;
   v_index2 NUMBER(10,0) := 0;
   v_length NUMBER(10,0) := 0;
   v_matrixRecord NVARCHAR2(2000);
   v_matrixComma VARCHAR2(4000);
   v_query NVARCHAR2(2000);
   v_sigValues NVARCHAR2(2000);
   v_id NVARCHAR2(2000);
   v_groupList NVARCHAR2(2000);
   v_groupRule NVARCHAR2(2000);
   identity_value NUMBER(10);

BEGIN

   v_length := LENGTH("_matrixValues") - LENGTH(REPLACE("_matrixValues", ',', '')) + 1 ;
   <<loop_1>>
   WHILE ( 1 = 1 ) 
   LOOP 
      
      BEGIN
         v_index1 := v_index1 + 1 ;
         IF v_index1 = v_length + 1 THEN
          EXIT;
         ELSE
         
         BEGIN
            v_matrixRecord := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX("_matrixValues", ',', v_index1), ',', -1) ;
            v_matrixComma := REPLACE(v_matrixRecord, ';', ',') ;
            v_matrixComma := REPLACE(v_matrixComma, '"', '''') ;
            v_query := ('INSERT INTO "approvalmatrix"("name","contractId","coreCustomerId","actionId","accountId","approvalruleId","isGroupMatrix","limitTypeId","lowerlimit","upperlimit","currency") VALUES ('|| v_matrixComma|| ')') ;
            EXECUTE IMMEDIATE v_query;
            select "id"  INTO identity_value from  "approvalmatrix" order by "id" desc fetch first row only;
      v_sigValues := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX("_signatorymatrixValues", '#', v_index1), '#', -1) ;
      v_id := identity_value ;
            v_groupList := UTILS.SUBSTRING_INDEX(v_sigValues, ';', 1) ;
            v_groupRule := UTILS.SUBSTRING_INDEX(v_sigValues, ';', -1) ;
            v_id := REPLACE(v_id, '"', '''') ;
            v_groupList := REPLACE(v_groupList, '"', '''') ;
            v_groupRule := REPLACE(v_groupRule, '"', '''') ;
            v_query := ('INSERT INTO "signatorygroupmatrix"("approvalMatrixId","groupList","groupRule") VALUES (') || v_id || ',' || '''' || v_groupList || '''' || ',' || '''' || v_groupRule || '''' || ')' ;
            EXECUTE IMMEDIATE v_query;
            GOTO loop_1;
         
         END;
         END IF;
      
      END;
   END LOOP;


END;
/

--  DDL for Procedure approvalmatrix_update_softdeleteflag_proc


create or replace NONEDITIONABLE PROCEDURE "approvalmatrix_update_softdeleteflag_proc" (
  "_contractId" IN VARCHAR2, "_cif" IN VARCHAR2, 
  "_accountIds" IN VARCHAR2, "_actionId" IN VARCHAR2, 
  "_limitTypeId" IN VARCHAR2
) AS BEGIN --   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
UPDATE 
  "approvalmatrix" 
SET 
  "softdeleteflag" = 1 
WHERE 
  "approvalmatrix"."contractId" = "_contractId" 
  AND "approvalmatrix"."coreCustomerId" = "_cif" 
  AND CAST(
    "approvalmatrix"."accountId" AS VARCHAR2(255)
  ) IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT("_accountIds")
      )
  ) 
  AND "approvalmatrix"."actionId" = "_actionId" 
  AND "approvalmatrix"."limitTypeId" = "_limitTypeId";
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;

/

--  DDL for Procedure approvalmatrixtemplate_create_proc


create or replace NONEDITIONABLE PROCEDURE "approvalmatrixtemplate_create_proc"(
  "_matrixValues" IN NVARCHAR2, 
  "_matrixApprover" IN NVARCHAR2,
  "_isGroupMatrix" IN NUMBER
) AS
    v_index1 NUMBER(10) := 0;
    v_index2 NUMBER(10) := 0;
    v_length NUMBER(19) := 0;
    v_length2 NUMBER(10) := 0;
    v_matrixRecord NVARCHAR2(2000);
    v_matrixComma NVARCHAR2(2000);
    v_query NVARCHAR2(2000);
    v_id NVARCHAR2(50);
    v_customerIds NVARCHAR2(255);
    v_customerIdsComma NVARCHAR2(255);
    v_customerId NVARCHAR2(50);
    v_sigValues NVARCHAR2(255);
    v_groupList NVARCHAR2(255);
    v_groupRule NVARCHAR2(255);
    identity_value NUMBER(10);
BEGIN
  
  v_length := LENGTH("_matrixValues") - LENGTH(REPLACE("_matrixValues", ',', '')) + 1;
  <<loop_2>>
    WHILE (1 = 1)
    LOOP
        BEGIN
      v_index1 := v_index1 + 1;
      IF v_index1 = v_length + 1 THEN 
        EXIT;
      ELSE
                BEGIN 
                    v_matrixRecord := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX("_matrixValues", ',', v_index1), ',', -1 );
                    v_matrixComma := REPLACE(v_matrixRecord, ';', ',');
                    v_matrixComma := REPLACE(v_matrixComma, '"', '''');
                    v_query := 'INSERT INTO "approvalmatrixtemplate"("contractId","coreCustomerId","actionId","approvalruleId","limitTypeId","lowerlimit","upperlimit","currency","isGroupMatrix") VALUES (' ||v_matrixComma ||')';
                    EXECUTE IMMEDIATE v_query;
                    SELECT "approvalmatrixtemplate"."id"  INTO v_id from "approvalmatrixtemplate" ORDER BY "id" desc FETCH FIRST ROW ONLY;
                    IF "_isGroupMatrix" = 0 THEN
                        v_customerIds := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX("_matrixApprover", ',', v_index1), ',', -1 );
                        IF (v_customerIds IS NOT NULL AND LENGTH(v_customerIds) > 0) THEN
                            v_customerIdsComma := REPLACE(v_customerIds, ';', ',');
                                v_length2 := LENGTH(v_customerIdsComma) - LENGTH(REPLACE(v_customerIdsComma, ',', '')) + 1;
                                v_index2 := 0;
                                <<loop_1>>
                                WHILE(1 = 1)
                                LOOP
                                    BEGIN
                                    v_index2 := v_index2 + 1;
                                    IF v_index2 = v_length2 + 1 THEN 
                                        EXIT;
                                    ELSE
                                        BEGIN
                                        v_customerId := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_customerIdsComma, ',', v_index2), ',', -1 );
                                        INSERT INTO "customerapprovalmatrixtemplate"("customerId","approvalMatrixId") values (v_customerId,v_id);             
                                        GOTO loop_1;
                                        END;
                                    END IF;
                                    END;
                                END LOOP;
                            END IF; 
                        GOTO loop_2;
                        ELSE IF "_isGroupMatrix" = 1 THEN
                            v_sigValues := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX("_matrixApprover", '#', v_index1), '#', -1 );
                            v_groupList := UTILS.SUBSTRING_INDEX(v_sigValues, ';', 1 );
                            v_groupRule := UTILS.SUBSTRING_INDEX(v_sigValues, ';', -1 );
                            INSERT INTO "signatorygroupmatrixtemplate"("approvalMatrixId", "groupList", "groupRule") values (v_id, v_groupList, v_groupRule);
                        END IF;  
                    END IF;
                END;
                END IF;
            END;
  END LOOP; 
END;
/

--  DDL for Procedure approvalmatrixtemplate_default_create_proc


create or replace NONEDITIONABLE PROCEDURE "approvalmatrixtemplate_default_create_proc" (
  "_actionIds" IN NVARCHAR2, "_contractId" IN NVARCHAR2, 
  "_approvalmode" IN NVARCHAR2, "_cif" IN NVARCHAR2, 
  "_currency" IN NVARCHAR2
) AS v_accountList VARCHAR2(4000) := 0;
v_actionIds VARCHAR2(32000) := 0;
v_limitTypeId_1 VARCHAR2(255) := 'DAILY_LIMIT';
v_limitTypeId_2 VARCHAR2(255) := 'MAX_TRANSACTION_LIMIT';
v_limitTypeId_3 VARCHAR2(255) := 'WEEKLY_LIMIT';
v_actionIndex NUMBER(10, 0) := 0;
v_actionId VARCHAR2(2000);
v_typeId VARCHAR2(4000) := ' ';
v_isGroupMatrix NUMBER(10, 0) := 0;
v_cursor SYS_REFCURSOR;
v_numOfActions NUMBER(10,0);
v_legalEntityId varchar2(255) := 'ALL';
BEGIN 
if "_contractId" is not null 
and "_cif" is not null 
and "_actionIds" is not null THEN If "_approvalmode" = '0' THEN v_isGroupMatrix := 0;
else v_isGroupMatrix := 1;
end if;
SELECT 
  "companyLegalUnit" into v_legalEntityId 
from 
  "contractcorecustomers" 
where 
  "coreCustomerId" = "_cif" 
  and "contractId" = "_contractId";
v_numOfActions := CASE WHEN "_actionIds" IS NULL THEN 0 ELSE LENGTH("_actionIds") - LENGTH(REPLACE("_actionIds", ',', '')
) + 1 END;
v_actionIds := REPLACE("_actionIds",' ','');
DBMS_OUTPUT.PUT_LINE('v_actionIds'||v_actionIds);
WHILE (1 = 1) LOOP BEGIN v_actionIndex := v_actionIndex + 1;
IF (v_actionIndex = v_numOfActions + 1) THEN EXIT;
ELSE BEGIN v_actionId := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_actionIds, ',', v_actionIndex),',',-1);
SELECT 
  "Type_id" INTO v_typeId 
FROM 
  "featureaction" 
WHERE 
  "id" = v_actionId 
  and "companyLegalUnit" = v_legalEntityId;
IF v_typeId = 'MONETARY' THEN BEGIN INSERT INTO "approvalmatrixtemplate" (
  "contractId", "actionId", "limitTypeId", 
  "coreCustomerId", "approvalruleId", 
  "isGroupMatrix", "currency"
) 
VALUES 
  (
    "_contractId", v_actionId, V_limitTypeId_1, 
    "_cif", 'NO_APPROVAL', v_isGroupMatrix, 
    "_currency"
  );
INSERT INTO "approvalmatrixtemplate" (
  "contractId", "actionId", "limitTypeId", 
  "coreCustomerId", "approvalruleId", 
  "isGroupMatrix", "currency"
) 
VALUES 
  (
    "_contractId", v_actionId, V_limitTypeId_2, 
    "_cif", 'NO_APPROVAL', v_isGroupMatrix, 
    "_currency"
  );
INSERT INTO "approvalmatrixtemplate" (
  "contractId", "actionId", "limitTypeId", 
  "coreCustomerId", "approvalruleId", 
  "isGroupMatrix", "currency"
) 
VALUES 
  (
    "_contractId", v_actionId, V_limitTypeId_3, 
    "_cif", 'NO_APPROVAL', v_isGroupMatrix, 
    "_currency"
  );
END;
ELSE IF v_typeId = 'NON_MONETARY' THEN BEGIN INSERT INTO "approvalmatrixtemplate" (
  "contractId", "actionId", "limitTypeId", 
  "coreCustomerId", "approvalruleId", 
  "isGroupMatrix", "currency"
) 
VALUES 
  (
    "_contractId", v_actionId, 'NON_MONETARY_LIMIT', 
    "_cif", 'NO_APPROVAL', v_isGroupMatrix, 
    "_currency"
  );
END;
END IF;
END IF;
END;
END IF;
END;
END LOOP;
END IF;
END;

/

--  DDL for Procedure approvalrequest_counts_proc


create or replace NONEDITIONABLE PROCEDURE "approvalrequest_counts_proc" (
  "_customerId" IN NVARCHAR2, "_approveActionList" IN NVARCHAR2, 
  "_createActionList" IN NVARCHAR2, 
  "records" OUT SYS_REFCURSOR, "records1" out sys_refcursor
) AS iv_approveActionList NVARCHAR2(2000) := "_approveActionList";
iv_createActionList NVARCHAR2(2000) := "_createActionList";
BEGIN DECLARE v_approvalRequestIds NVARCHAR2(2000);
v_companyId NVARCHAR2(2000);
v_features NVARCHAR2(2000);
v_createApproveActions NVARCHAR2(2000);
v_customerMatrixIds NVARCHAR2(2000);
v_select_statement NVARCHAR2(4000);
v_alreadyApprovedIds NVARCHAR2(2000);
BEGIN OPEN "records" FOR 
SELECT 
  0 "count", 
  'ACHFilesForMyApproval' "TransactionType" 
FROM 
  DUAL 
UNION 
SELECT 
  0 "count", 
  'ACHTransactionsForMyApproval' "TransactionType" 
FROM 
  DUAL 
UNION 
SELECT 
  0 "count", 
  'GeneralTransactionsForMyApproval' "TransactionType" 
FROM 
  DUAL 
UNION 
SELECT 
  0 "count", 
  'GeneralTransactionsForMyApproval' "TransactionType" 
FROM 
  DUAL 
UNION 
SELECT 
  0 "count", 
  'myRequestsWaiting' "TransactionType" 
FROM 
  DUAL 
UNION 
SELECT 
  0 "count", 
  'myRequestsRejected' "TransactionType" 
FROM 
  DUAL 
UNION 
SELECT 
  0 "count", 
  'myRequestsApproved' "TransactionType" 
FROM 
  DUAL;
SELECT 
  "customer"."Organization_Id" INTO v_companyId 
FROM 
  "customer" 
WHERE 
  "customer"."id" = "_customerId";
IF v_companyId IS NULL THEN v_companyId := ' ';
END IF;
IF iv_approveActionList IS NULL THEN iv_approveActionList := ' ';
END IF;
IF iv_createActionList IS NULL THEN iv_createActionList := ' ';
END IF;
SELECT 
  LISTAGG(
    CAST(
      "featureaction"."Feature_id" AS NVARCHAR2(2000)
    ), 
    ','
  ) INTO v_features 
FROM 
  "featureaction" 
WHERE 
  "featureaction"."id" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(iv_approveActionList)
      )
  );
IF v_features IS NULL THEN v_features := ' ';
END IF;
SELECT 
  LISTAGG(
    CAST(
      "featureaction"."id" AS NVARCHAR2(2000)
    ), 
    ','
  ) INTO v_createApproveActions 
FROM 
  "featureaction" 
WHERE 
  "featureaction"."Feature_id" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(v_features)
      )
  ) 
  AND (
    "featureaction"."id" LIKE '%_CREATE' 
    OR "featureaction"."id" LIKE '%_UPLOAD'
  );
IF v_createApproveActions IS NULL THEN v_createApproveActions := ' ';
END IF;
SELECT 
  LISTAGG(
    CAST(
      "customerapprovalmatrix"."approvalMatrixId" AS NVARCHAR2(2000)
    ), 
    ','
  ) INTO v_customerMatrixIds 
FROM 
  "customerapprovalmatrix" 
WHERE 
  "customerapprovalmatrix"."customerId" = "_customerId";
IF v_customerMatrixIds IS NULL THEN v_customerMatrixIds := ' ';
END IF;
SELECT 
  LISTAGG(
    CAST(
      "bbactedrequest"."requestId" AS NVARCHAR2(2000)
    ), 
    ','
  ) INTO v_alreadyApprovedIds 
FROM 
  "bbactedrequest" 
WHERE 
  "bbactedrequest"."createdby" = "_customerId" 
  AND "bbactedrequest"."action" = 'Approved';
IF v_alreadyApprovedIds IS NULL THEN v_alreadyApprovedIds := ' ';
END IF;
SELECT 
  LISTAGG(
    CAST(
      "requestapprovalmatrix"."requestId" AS NVARCHAR2(2000)
    ), 
    ','
  ) INTO v_approvalRequestIds 
FROM 
  "requestapprovalmatrix" 
WHERE 
  (
    CAST(
      "requestapprovalmatrix"."approvalMatrixId" AS NVARCHAR2(2000)
    )
  ) IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(v_customerMatrixIds)
      )
  ) 
  AND FIND_IN_SET(
    CAST(
      "requestapprovalmatrix"."requestId" AS NVARCHAR2(2000)
    ), 
    v_alreadyApprovedIds
  ) = 0;
IF v_approvalRequestIds IS NULL THEN v_approvalRequestIds := ' ';
END IF;
v_select_statement := (
  'select count(tablea.requestCountA) as "count", tablea."TransactionType" 
               from (
                 select DISTINCT("bbrequest"."requestId") as requestCountA, 
                       case when "bbrequest"."featureActionId" LIKE ''ACH_FILE%''
                        then  ''ACHFilesForMyApproval'' else(
                           case when "bbrequest"."featureActionId" LIKE ''ACH%''
                              then ''ACHTransactionsForMyApproval'' else 
                              ''GeneralTransactionsForMyApproval'' end
                              ) end
                       as "TransactionType",
                       "bbrequest"."createdby",
                       "bbrequest"."companyId",
                       "bbrequest"."status"
                       FROM (
                           "bbrequest" LEFT JOIN "requestapprovalmatrix" ON 
                           "bbrequest"."requestId" = "requestapprovalmatrix"."requestId"
                       )
                       WHERE FIND_IN_SET(CAST("bbrequest"."requestId" as nvarchar2(2000)),''' || (v_approvalRequestIds) || ''')>0 AND "bbrequest"."companyId" = ''' || (v_companyId) || ''' AND FIND_IN_SET("bbrequest"."featureActionId",''' || (v_createApproveActions) || ''')>0' || ' AND "bbrequest"."status" = ''Pending'') tablea GROUP BY "TransactionType"'
);
v_select_statement := (v_select_statement) || ' UNION select count(tableb.requestCountB) as "count", 
                tableb."TransactionType" from (
                    select  
                        DISTINCT("bbrequest"."requestId") as requestCountB,
                        case when "bbrequest"."status" = ''Pending'' then ''myRequestsWaiting'' else
                            (case when "bbrequest"."status" = ''Rejected'' then ''myRequestsRejected'' else 
                                (case when "bbrequest"."status" = ''Approved'' then ''myRequestsApproved'' else ''myRequestsWithdrawn'' end)end)end as "TransactionType",
                        "bbrequest"."createdby",
                        "bbrequest"."companyId",
                        "bbrequest"."status"
                       FROM "bbrequest" WHERE "bbrequest"."companyId" = ''' || (v_companyId) || '''' || ' AND FIND_IN_SET("bbrequest"."featureActionId",''' || (iv_createActionList) || ''')>0 AND "bbrequest"."createdby" = ' || '''' || "_customerId" || '''' || ') tableb group BY "TransactionType"';
WHILE v_select_statement IS NULL LOOP EXIT;
END LOOP;
open "records1" for v_select_statement;
END;
--<<MAINLABEL$leave>>
END;

/

--  DDL for Procedure signatorygroup_create_proc


create or replace NONEDITIONABLE PROCEDURE "signatorygroup_create_proc" 
(
  "signatoryGroupValues" IN VARCHAR2,
  "customerSignatoryGroupValues" IN VARCHAR2
)
AS
   v_index1 NUMBER(10,0) := 0;
   v_length1 NUMBER(10,0) := 0;
   v_matrixRecord VARCHAR2(4000);
   v_matrixComma VARCHAR2(4000);
   v_matrixComma1 VARCHAR2(4000);
   v_query VARCHAR2(4000);
   v_signatory VARCHAR2(4000);
   v_signatoriesComma VARCHAR2(4000);
   v_signatoriesComma1 VARCHAR2(4000);

BEGIN

   v_matrixRecord := UTILS.SUBSTRING_INDEX("signatoryGroupValues", ',', 1) ;
   v_matrixComma1 := REPLACE(v_matrixRecord, ';', ',') ;
   v_matrixComma := REPLACE(v_matrixComma1, '"', '''') ;
   v_query := 'INSERT INTO "signatorygroup"("signatoryGroupId","signatoryGroupName","signatoryGroupDescription","coreCustomerId","contractId","createdby") values (' || v_matrixComma || ')' ;
   EXECUTE IMMEDIATE v_query;
   v_length1 := LENGTH("customerSignatoryGroupValues") - LENGTH(REPLACE("customerSignatoryGroupValues", ',', '')) ;
   WHILE v_index1 != v_length1 
   LOOP 

      BEGIN
         v_index1 := v_index1 + 1 ;
         v_signatory := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX("customerSignatoryGroupValues", ',', v_index1), ',', -1) ;
         v_signatoriesComma1 := REPLACE(v_signatory, ';', ',') ;
         v_signatoriesComma := REPLACE(v_signatoriesComma1, '"', '''') ;
         v_query := 'INSERT INTO "customersignatorygroup"("customerSignatoryGroupId", "signatoryGroupId", "customerId", "createdby") values (' || v_signatoriesComma || ')' ;
         EXECUTE IMMEDIATE v_query;

      END;
   END LOOP;

END;
/

--  DDL for Procedure signatorygroup_update_proc


create or replace NONEDITIONABLE PROCEDURE "signatorygroup_update_proc" 
(
  "_sigGroupValues" IN VARCHAR2,
  "_newSigValues" IN VARCHAR2,
  "_deleteSigValues" IN VARCHAR2
)
AS
   v_index1 NUMBER(10,0) := 0;
   v_length1 NUMBER(19,0);
   v_signatoriesComma VARCHAR2(4000);
   v_custId VARCHAR2(100);
   v_sigGroupId VARCHAR2(4000);
   v_SigGroupName VARCHAR2(4000);
   v_sigGroupDes VARCHAR2(4000);
   v_signatory VARCHAR2(4000);
   v_query VARCHAR2(4000);
   v_sigCreatedBy VARCHAR2(4000);

BEGIN

   v_sigGroupId := UTILS.SUBSTRING_INDEX("_sigGroupValues", ';', 1) ;
   v_SigGroupName := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX("_sigGroupValues", ';', 2), ';', -1) ;
   v_SigGroupDes := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX("_sigGroupValues", ';', 3), ';', -1) ;
   v_sigCreatedBy := UTILS.SUBSTRING_INDEX("_sigGroupValues", ';', -1) ;
   v_SigGroupId := REPLACE(v_SigGroupId, '"', '''') ;
   IF v_SigGroupName IS NOT NULL
     AND v_SigGroupName != ' ' THEN

   BEGIN
      v_SigGroupName := REPLACE(v_SigGroupName, '"', '''') ;
      v_sigCreatedBy := REPLACE(v_sigCreatedBy, '"', '''') ;
      v_query := ('UPDATE "signatorygroup" SET "signatoryGroupName" = ') || v_SigGroupName || (' ,"lastmodifiedts" = CURRENT_TIMESTAMP, "modifiedby" = ') || v_sigCreatedBy || (' WHERE "signatoryGroupId" = ') || v_sigGroupId || (' ') ;
      EXECUTE IMMEDIATE v_query;

   END;
   END IF;
   IF v_sigGroupDes IS NOT NULL
     AND v_sigGroupDes != ' ' THEN

   BEGIN
      v_sigGroupDes := REPLACE(v_sigGroupDes, '"', '''') ;
      v_sigCreatedBy := REPLACE(v_sigCreatedBy, '"', '''') ;
      v_query := ('UPDATE "signatorygroup" SET "signatoryGroupDescription" = ') || v_sigGroupDes || (' , "lastmodifiedts" = CURRENT_TIMESTAMP, "modifiedby" = ') || v_sigCreatedBy || (' WHERE "signatoryGroupId" = ') || v_sigGroupId || (' ') ;
      EXECUTE IMMEDIATE v_query;

   END;
   END IF;
   IF "_newSigValues" IS NOT NULL
     AND "_newSigValues" != ' ' THEN

   BEGIN
      v_length1 := LENGTH("_newSigValues") - LENGTH(REPLACE("_newSigValues", ',', '')) + 1 ;
      v_index1 := 0 ;
      <<loop_1>>
      WHILE ( 1 = 1 ) 
      LOOP 

         BEGIN
            v_index1 := v_index1 + 1 ;
            IF v_index1 = v_length1 + 1 THEN
             EXIT;
            ELSE

            BEGIN
               v_signatory := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX("_newSigValues", ',', v_index1), ',', -1) ;
               v_signatoriesComma := REPLACE(v_signatory, ';', ',') ;
               v_signatoriesComma := REPLACE(v_signatoriesComma, '"', '''') ;
               v_query := ('INSERT INTO "customersignatorygroup"("customerSignatoryGroupId", "signatoryGroupId", "customerId", "createdby") values (') || v_signatoriesComma || (')') ;
               EXECUTE IMMEDIATE v_query;
               GOTO loop_1;

            END;
            END IF;

         END;
      END LOOP;

   END;
   END IF;
   IF "_deleteSigValues" IS NOT NULL
     AND "_deleteSigValues" != ' ' THEN

   BEGIN
      v_length1 := LENGTH("_deleteSigValues") - LENGTH(REPLACE("_deleteSigValues", ',', ''))+ 1 ;
      v_index1 := 0 ;
      <<loop_1>>
      WHILE ( 1 = 1 ) 
      LOOP 

         BEGIN
            v_index1 := v_index1 + 1 ;
            IF v_index1 = v_length1 + 1 THEN
             EXIT;
            ELSE

            BEGIN
               v_signatory := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX("_deleteSigValues", ',', v_index1), ',', -1) ;
               v_sigGroupId := UTILS.SUBSTRING_INDEX(v_signatory, ';', 1) ;
               v_custId := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_signatory, ';', 2), ';', -1) ;
               v_sigGroupId := REPLACE(v_sigGroupId, '"', '''') ;
               v_custId := REPLACE(v_custId, '"', '''') ;
               v_query := ('DELETE FROM "customersignatorygroup" WHERE "customersignatorygroup"."signatoryGroupId" =') || v_sigGroupId || ('AND "customersignatorygroup"."customerId"=') || v_custId || (' ') ;
               EXECUTE IMMEDIATE v_query;
               GOTO loop_1;

            END;
            END IF;

         END;
      END LOOP;

   END;
   END IF;

END;
/

create or replace NONEDITIONABLE PROCEDURE "update_bbactedrequest_proc" ("_requestId" IN VARCHAR2) AS BEGIN 
UPDATE 
  "bbactedrequest" 
SET 
  "softdeleteflag" = '1' 
WHERE 
  ("requestId" = "_requestId");
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/

CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "approval_matrix_manual_cleanup_proc" (
  v__contractId IN VARCHAR2, v__coreCustomerId IN VARCHAR2, 
  "contractaccounts" OUT SYS_REFCURSOR
) AS v_customerAccounts CLOB;
v_customerActions CLOB;
BEGIN 
UPDATE 
  "approvalmatrix" 
SET 
  "softdeleteflag" = '1' 
WHERE 
  "approvalmatrix"."coreCustomerId" = v__coreCustomerId;
SELECT 
  DISTINCT LISTAGG("contractaccounts"."accountId") INTO v_customerAccounts 
FROM 
  "contractaccounts" 
WHERE 
  (
    "contractId" = v__contractId 
    AND "coreCustomerId" = v__coreCustomerId
  );
SELECT 
  DISTINCT LISTAGG("actionId") INTO v_customerActions 
FROM 
  "contractactionlimit" 
  LEFT JOIN "featureaction" ON (
    "featureaction"."id" = "contractactionlimit"."actionId"
  ) 
WHERE 
  (
    "contractId" = v__contractId 
    AND "coreCustomerId" = v__coreCustomerId
  ) 
  AND "featureaction"."approveFeatureAction" IS NOT NULL;
--APPROVALMATRIX_DEFAULT_CREATE_PROC(v_customerActions,
--                                        v__contractId,
--                                      v_customerAccounts,
--                                    v__coreCustomerId,
--                                  v_cursor) ;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/

  create or replace NONEDITIONABLE PROCEDURE "approvalmatrix_fetch_grouprecords_proc" 
(
  "_contractId" IN VARCHAR2,
  "_cif" IN VARCHAR2,
  "_accountId" IN VARCHAR2,
  "_limitTypeId" IN VARCHAR2,
  "_actions" IN VARCHAR2,
  "records" OUT SYS_REFCURSOR
)
AS
   v__cif VARCHAR2(50) := "_cif";
   v__accountId VARCHAR2(50) := "_accountId";
   v__limitTypeId VARCHAR2(50) := "_limitTypeId";


BEGIN

--   /*TODO:SQLDEV*/ SET XACT_ABORT ON /*END:SQLDEV*/
   IF v__cif = '' THEN
    v__cif := '%' ;
   END IF;
   IF v__accountId = '' THEN
    v__accountId := '%' ;
   END IF;
   IF v__limitTypeId = '' or v__limitTypeId is null  THEN
    v__limitTypeId := '%' ;
   END IF;
   dbms_output.put_line('v__cif'||v__cif);
   dbms_output.put_line('v__accountId'||v__accountId);
   dbms_output.put_line('v__limitTypeId'||v__limitTypeId);
   dbms_output.put_line('_contractId'||"_contractId");
   dbms_output.put_line('_actions'||"_actions");
   OPEN  "records" FOR
      SELECT approvalmatrix."id" ,
             approvalmatrix."contractId" ,
             approvalmatrix."accountId" ,
             approvalmatrix."limitTypeId" ,
             featureaction."id" "actionId"  ,
             featureaction."name" "actionName"  ,
             featureaction."description" "actionDescription"  ,
             featureaction."Feature_id" "featureId"  ,
             featureaction."Type_id" "actionType"  ,
             featureaction."isAccountLevel" "isAccountLevel"  ,
             feature."name" "featureName"  ,
             feature."Status_id" "fifeaturestatus"  ,
             approvalrule."id" "approvalruleId"  ,
             approvalrule."numberOfApprovals" ,
             approvalrule."name" "approvalRuleName"  ,
             approvalmatrix."lowerlimit" ,
             approvalmatrix."upperlimit" ,
--             approvalmatrix."currency",
             signatorygroupmatrix."groupList" "groupList"  ,
             signatorygroupmatrix."groupRule" "groupRule"  ,
             contractcorecustomers."coreCustomerId" "cifId"  ,
             contractcorecustomers."coreCustomerName" "cifName"  ,
             approvalmatrix."invalid" ,
             approvalmatrix."isGroupMatrix"  
        FROM ( ( ( ( ( ( "approvalmatrix" approvalmatrix
                         LEFT JOIN "signatorygroupmatrix" signatoryGroupMatrix   ON approvalmatrix."id" = signatorygroupmatrix."approvalMatrixId"
                          ) 
                       LEFT JOIN "featureaction" featureaction   ON approvalmatrix."actionId" = featureaction."id"
                        ) 
                     LEFT JOIN "approvalrule" approvalRule   ON approvalmatrix."approvalruleId" = approvalrule."id"
                      ) 
                   LEFT JOIN "feature" feature   ON featureaction."Feature_id" = feature."id"
                    ) 
                 LEFT JOIN "contractfeatures" contractfeatures   ON feature."id" = contractfeatures."featureId"
                 AND approvalmatrix."contractId" = contractfeatures."contractId"
                 AND approvalmatrix."coreCustomerId" = contractfeatures."coreCustomerId"
                  ) 
               LEFT JOIN "contractcorecustomers" contractcorecustomers   ON approvalmatrix."contractId" = contractcorecustomers."contractId"
               AND approvalmatrix."coreCustomerId" = contractcorecustomers."coreCustomerId"
                ) 
       WHERE  approvalmatrix."contractId" = "_contractId"
                AND approvalmatrix."coreCustomerId" LIKE v__cif
                AND approvalmatrix."isGroupMatrix" = 1
                AND approvalmatrix."accountId" LIKE v__accountId
                AND approvalmatrix."actionId" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT("_actions")))
                AND approvalmatrix."limitTypeId" LIKE v__limitTypeId
                AND approvalmatrix."softdeleteflag" = 0
                AND featureaction."approveFeatureAction" IS NOT NULL
                AND featureaction."status" = 'SID_ACTION_ACTIVE'
        ORDER BY approvalmatrix."contractId",
                 approvalmatrix."accountId",
                 approvalmatrix."limitTypeId",
                 approvalmatrix."actionId",
                 approvalmatrix."lowerlimit" ;
      --DBMS_SQL.RETURN_RESULT(v_cursor);

END;
/

create or replace NONEDITIONABLE PROCEDURE "approvalmatrix_fetch_records_proc"
(
  "_contractId" IN VARCHAR2,
  "_cif" IN VARCHAR2,
  "_accountId" IN VARCHAR2,
  "_limitTypeId" IN VARCHAR2,
  "_actions" IN NVARCHAR2,
  "records" OUT SYS_REFCURSOR
)
AS
   iv_cif VARCHAR2(50) := "_cif";
   iv_accountId VARCHAR2(50) := "_accountId";
   iv_limitTypeId VARCHAR2(50) := "_limitTypeId" ;


BEGIN
   IF iv_cif is null THEN
    iv_cif := '%' ;
   END IF;
   IF iv_accountId is null THEN
    iv_accountId := '%' ;
   END IF;
   IF iv_limitTypeId is null THEN
    iv_limitTypeId := '%' ;
   END IF;
   OPEN  "records" FOR
      SELECT "approvalmatrix"."id" ,
             "approvalmatrix"."contractId" ,
             "approvalmatrix"."accountId" ,
             "approvalmatrix"."limitTypeId" ,
             "featureAction"."id" "actionId"  ,
             "featureAction"."name" "actionName"  ,
             "featureAction"."description" "actionDescription"  ,
             "featureAction"."Feature_id" "featureId"  ,
             "featureAction"."Type_id" "actionType"  ,
             "featureAction"."isAccountLevel" "isAccountLevel",
             "feature"."name" "featureName"  ,
             "feature"."Status_id" "fifeaturestatus"  ,
             "approvalRule"."id" "approvalruleId"  ,
             "approvalRule"."numberOfApprovals" ,
             "approvalRule"."name" "approvalRuleName"  ,
             "approvalmatrix"."lowerlimit" ,
             "approvalmatrix"."upperlimit" ,
--             "approvalmatrix"."currency",
             "customer"."id" "customerId"  ,
             "customer"."FirstName" "firstName"  ,
             "customer"."LastName" "lastName"  ,
             "contractcorecustomers"."coreCustomerId" "cifId"  ,
             "contractcorecustomers"."coreCustomerName" "cifName"  ,
             "approvalmatrix"."invalid" ,
             "approvalmatrix"."isGroupMatrix" 
        FROM ( ( ( ( ( ( ( "approvalmatrix" "approvalmatrix"
                           LEFT JOIN "customerapprovalmatrix" "customerapprovalMatrix"   ON "approvalmatrix"."id" = "customerapprovalMatrix"."approvalMatrixId"
                            ) 
                         LEFT JOIN "customer" "customer"   ON "customerapprovalMatrix"."customerId" = "customer"."id"
                          ) 
                       LEFT JOIN "featureaction" "featureAction"   ON "approvalmatrix"."actionId" = "featureAction"."id"
                        ) 
                     LEFT JOIN "approvalrule" "approvalRule"   ON "approvalmatrix"."approvalruleId" = "approvalRule"."id"
                      ) 
                   LEFT JOIN "feature" "feature"   ON "featureAction"."Feature_id" = "feature"."id"
                    ) 
                 LEFT JOIN "contractfeatures" "contractfeatures"   ON "feature"."id" = "contractfeatures"."featureId"
                 AND "approvalmatrix"."contractId" = "contractfeatures"."contractId"
                 AND "approvalmatrix"."coreCustomerId" = "contractfeatures"."coreCustomerId"
                  ) 
               LEFT JOIN "contractcorecustomers" "contractcorecustomers"   ON "approvalmatrix"."contractId" = "contractcorecustomers"."contractId"
               AND "approvalmatrix"."coreCustomerId" = "contractcorecustomers"."coreCustomerId"
                ) 
       WHERE  
       "approvalmatrix"."contractId" = "_contractId"
                AND
                "approvalmatrix"."coreCustomerId" LIKE iv_cif
                AND "approvalmatrix"."accountId" LIKE iv_accountId
--                AND FIND_IN_SET("approvalmatrix"."actionId", "_actions") > 0
                AND "approvalmatrix"."actionId" IN (SELECT DISTINCT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT("_actions")))
                AND "approvalmatrix"."limitTypeId" LIKE iv_limitTypeId
                AND "approvalmatrix"."softdeleteflag" = 0
                AND "featureAction"."approveFeatureAction" IS NOT NULL
                AND "featureAction"."approveFeatureAction" IS NOT NULL
                AND "featureAction"."status" = 'SID_ACTION_ACTIVE'
        ORDER BY "approvalmatrix"."contractId",
                 "approvalmatrix"."accountId",
                 "approvalmatrix"."limitTypeId",
                 "approvalmatrix"."actionId",
                 "approvalmatrix"."lowerlimit" ;


END;
/

--  DDL for Procedure approvalmatrixtemplate_cleanup_proc


create or replace NONEDITIONABLE PROCEDURE "approvalmatrixtemplate_cleanup_proc" (
  "_actionIds" IN VARCHAR2, "_contractId" IN VARCHAR2, 
  "_cif" IN VARCHAR2, "_limitTypeId" IN VARCHAR2
) AS BEGIN << MAINLABEL >> BEGIN 
UPDATE 
  "approvalmatrix" 
SET 
  "approvalmatrix"."softdeleteflag" = 1 
WHERE 
  "approvalmatrix"."contractId" = "_contractId" 
  AND "approvalmatrix"."coreCustomerId" = "_cif" 
  AND "approvalmatrix"."actionId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT("_actionIds")
      )
  ) 
  AND "approvalmatrix"."limitTypeId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT("_limitTypeId")
      )
  ) 
  AND "approvalmatrix"."softdeleteflag" = 0;
UPDATE 
  "approvalmatrixtemplate" 
SET 
  "approvalmatrixtemplate"."softdeleteflag" = 1 
WHERE 
  "approvalmatrixtemplate"."contractId" = "_contractId" 
  AND "approvalmatrixtemplate"."coreCustomerId" = "_cif" 
  AND "approvalmatrixtemplate"."actionId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT("_actionIds")
      )
  ) 
  AND "approvalmatrixtemplate"."limitTypeId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT("_limitTypeId")
      )
  ) 
  AND "approvalmatrixtemplate"."softdeleteflag" = 0;
END;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;

/

--  DDL for Procedure update_requestapprovalmatrix_proc


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "update_requestapprovalmatrix_proc" (
  v__requestApprovalMatrixId IN VARCHAR2
) AS BEGIN 
UPDATE 
  "requestapprovalmatrix" 
SET 
  "receivedApprovals" = "receivedApprovals" + 1 
WHERE 
  "requestapprovalmatrix"."id" IN (
    SELECT 
      column_value 
    from 
      TABLE(
        UTILS.STRING_SPLIT(v__requestApprovalMatrixId)
      )
  );
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/

--  DDL for Procedure update_signatorygroup_for_user_proc


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "update_signatorygroup_for_user_proc" (
  v__coreCustomerId IN VARCHAR2, v__contractId IN VARCHAR2, 
  v__customerId IN VARCHAR2, v__signatorygroupId IN VARCHAR2
) AS v_index1 NUMBER(10, 0) := 0;
v_length NUMBER(10, 0) := 0;
v_cusrecord VARCHAR2(4000);
v_cus VARCHAR2(4000);
BEGIN v_length := LENGTH(v__customerId) - LENGTH(
  REPLACE(v__customerId, ',', ' ')
) || 1;
<< loop_1 >> WHILE (1 = 1) LOOP BEGIN v_index1 := v_index1 + 1;
IF v_index1 = v_length + 1 THEN EXIT;
ELSE BEGIN v_cusrecord := UTILS.SUBSTRING_INDEX(
  UTILS.SUBSTRING_INDEX(v__customerId, ',', v_index1), 
  ',', 
  -1
);
v_cus := REPLACE(v_cusrecord, ';', ',');
v_cus := REPLACE(v_cus, '"', '''');
DELETE "customersignatorygroup" 
WHERE 
  "customerId" = v_cus 
  AND "signatoryGroupId" IN (
    SELECT 
      "signatoryGroupId" 
    FROM 
      "signatorygroup" 
    WHERE 
      "coreCustomerId" = v__coreCustomerId 
      AND "contractId" = v__contractId
  );
IF v__signatorygroupId != ' ' THEN BEGIN INSERT INTO "customersignatorygroup" (
  "customerSignatoryGroupId", "signatoryGroupId", 
  "customerId", "createdby"
) 
VALUES 
  (
    SYS_GUID(), 
    v__signatorygroupId, 
    v_cus, 
    v_cus
  );
END;
END IF;
GOTO loop_1;
END;
END IF;
END;
END LOOP;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/

--  DDL for Procedure fetch_approval_history_proc


create or replace NONEDITIONABLE PROCEDURE "fetch_approval_history_proc" (
  "_userId" IN VARCHAR2, "_moduleListArr" IN VARCHAR2, 
  "_actionListArr" IN VARCHAR2, "_featureListArr" IN VARCHAR2, 
  "_searchStartDate" IN VARCHAR2, "_searchEndDate" IN VARCHAR2, 
  "_sortParam" IN VARCHAR2, "_sortOrder" IN VARCHAR2, 
  "records" OUT SYS_REFCURSOR
) AS dateFilterStmt VARCHAR2(256);
dateField VARCHAR2(128);
req_pending_status VARCHAR2(64);
sqlStmt VARCHAR2(4000);
sortParam VARCHAR2(64);
BEGIN dateField := 'ar."checkedts"';
IF "_sortParam" IS NOT NULL THEN sortParam := 'ar.' || "_sortParam" || '';
END IF;
IF "_searchStartDate" IS NULL THEN dateFilterStmt := '';
ELSE dateFilterStmt := ' AND TO_CHAR(' || dateField || ', ''YYYY-MM-DD'') BETWEEN ''' || "_searchStartDate" || ''' AND ';
IF "_searchEndDate" IS NOT NULL THEN dateFilterStmt := dateFilterStmt || '''' || "_searchEndDate" || '''';
ELSE dateFilterStmt := dateFilterStmt || '(SELECT TO_CHAR(SYSDATE, ''YYYY-MM-DD'') FROM DUAL)';
END IF;
END IF;
sqlStmt := 'SELECT ar."requestId", ar."recordId", ar."module", ar."feature", ar."expAPIOperationName", ar."expAPINickName", ar."permissionId", ar."permissionName", cru."Username" as createdby, ar."createdts", ar."status", cku."Username" as checkedBy, ar."checkedts", ar."reason"
                    FROM "approvalrequests" ar
                    LEFT JOIN "systemuser"  cru ON cru."id" = ar."createdby"
                    LEFT JOIN "systemuser" cku ON cku."id" = ar."checkedBy"
                  WHERE  ar."checkedBy" = ''' || "_userId" || '''';
IF "_moduleListArr" IS NOT NULL THEN sqlStmt := sqlStmt || ' AND ar."module" IN (' || "_moduleListArr" || ')';
END IF;
IF "_featureListArr" IS NOT NULL THEN sqlStmt := sqlStmt || ' AND ar."feature" IN (' || "_featureListArr" || ')';
END IF;
IF "_actionListArr" IS NOT NULL THEN sqlStmt := sqlStmt || ' AND ar."expAPINickName" IN (' || "_actionListArr" || ')';
END IF;
sqlStmt := sqlStmt || dateFilterStmt;
sqlStmt := sqlStmt || ' ORDER BY ';
IF(
  sortParam IS NOT NULL 
  AND sortParam IN ('ar."status"', dateField)
) THEN sqlStmt := sqlStmt || sortParam;
ELSE sqlStmt := sqlStmt || dateField;
END IF;
IF(
  "_sortOrder" IS NOT NULL 
  AND (
    UPPER("_sortOrder")
  ) IN ('DESC', 'ASC')
) THEN sqlStmt := sqlStmt || ' ' || "_sortOrder";
ELSE sqlStmt := sqlStmt || ' ' || 'DESC';
END IF;
OPEN "records" FOR sqlStmt;
END;
/

--  DDL for Procedure fetch_approvalgroups_for_pendingtxn_proc


create or replace NONEDITIONABLE PROCEDURE "fetch_approvalgroups_for_pendingtxn_proc" (
  "signatorygroup" OUT SYS_REFCURSOR
) AS BEGIN OPEN "signatorygroup" FOR 
SELECT 
  "signatorygroup"."signatoryGroupId" 
FROM 
  "signatorygroup" 
WHERE 
  "signatorygroup"."signatoryGroupId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(
          (
            SELECT 
              LISTAGG(
                REPLACE(
                  REPLACE(
                    REPLACE(s."pendingGroupList", ']', ' '), 
                    '[', 
                    ' '
                  ), 
                  '"', 
                  ' '
                ), 
                ','
              ) 
            FROM 
              "signatorygrouprequestmatrix" S 
              JOIN "bbrequest" b ON (S."requestId" = b."requestId") 
            WHERE 
              s."isApproved" = 'false' 
              AND b."status" = 'Pending'
          )
        )
      )
  );
--DBMS_SQL.RETURN_RESULT(v_cursor);
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;

/

create or replace NONEDITIONABLE PROCEDURE "fetch_approvalmatrixtemplate_proc" (
  "_contractId" IN VARCHAR2, "_cif" IN VARCHAR2, 
  "_limitTypeId" IN VARCHAR2, "_actions" IN VARCHAR2, 
  "records" OUT SYS_REFCURSOR
) AS v__cif VARCHAR2(50) := "_cif";
v__limitTypeId VARCHAR2(50) := "_limitTypeId";
v_isGroupMatrix NUMBER(10, 0);
v_legalEntityId VARCHAR2(255);
BEGIN IF "_cif" IS NULL THEN BEGIN v__cif := '%';
END;
END IF;
BEGIN 
SELECT 
  "companyLegalUnit" INTO v_legalEntityId 
FROM 
  "contractcorecustomers" 
WHERE 
  "coreCustomerId" = "_cif" 
  AND "contractId" = "_contractId";
EXCEPTION WHEN NO_DATA_FOUND THEN v_legalEntityId := '';
END;
IF v__limitTypeId IS NULL THEN BEGIN v__limitTypeId := '%';
END;
END IF;
BEGIN 
SELECT 
  "isGroupLevel" INTO v_isGroupMatrix 
FROM 
  "approvalmode" 
WHERE 
  "contractId" = "_contractId" 
  AND "coreCustomerId" = "_cif";
EXCEPTION WHEN NO_DATA_FOUND THEN v_isGroupMatrix := '';
END;
IF v_isGroupMatrix IS NULL THEN BEGIN v_isGroupMatrix := 0;
END;
END IF;
dbms_output.put_line('v_isGroupMatrix'||v_isGroupMatrix);
dbms_output.put_line('v_legalEntityId'||v_legalEntityId);
dbms_output.put_line('_contractId'||"_contractId");
dbms_output.put_line('_cif'||"_cif");
dbms_output.put_line('v__limitTypeId'||v__limitTypeId);
dbms_output.put_line('_actions'||"_actions");
IF v_isGroupMatrix = 0 THEN BEGIN OPEN "records" FOR 
SELECT 
  approvalmatrixtemplate."id", 
  approvalmatrixtemplate."contractId", 
  approvalmatrixtemplate."limitTypeId", 
  featureaction."id" "actionId", 
  featureaction."name" "actionName", 
  featureaction."description" "actionDescription", 
  featureaction."Feature_id" "featureId", 
  featureaction."Type_id" "actionType", 
  featureaction."isAccountLevel" "isAccountLevel", 
  feature."name" "featureName", 
  feature."Status_id" "fifeaturestatus", 
  approvalrule."id" "approvalruleId", 
  approvalrule."numberOfApprovals", 
  approvalrule."name" "approvalRuleName", 
  approvalmatrixtemplate."lowerlimit", 
  approvalmatrixtemplate."upperlimit", 
  approvalmatrixtemplate."currency", 
  customer."id" "customerId", 
  customer."FirstName" "firstName", 
  customer."LastName" "lastName", 
  contractcorecustomers."coreCustomerId" "cifId", 
  contractcorecustomers."coreCustomerName" "cifName", 
  approvalmatrixtemplate."invalid", 
  approvalmatrixtemplate."isGroupMatrix" 
FROM 
  (
    (
      (
        (
          (
            (
              (
                "approvalmatrixtemplate" approvalmatrixtemplate 
                LEFT JOIN "customerapprovalmatrixtemplate" customerapprovalmatrixtemplate ON approvalmatrixtemplate."id" = customerapprovalmatrixtemplate."approvalMatrixId"
              ) 
              LEFT JOIN "customer" customer ON customerapprovalmatrixtemplate."customerId" = customer."id"
            ) 
            LEFT JOIN "featureaction" featureAction ON approvalmatrixtemplate."actionId" = featureAction."id"
          ) 
          LEFT JOIN "approvalrule" approvalRule ON approvalmatrixtemplate."approvalruleId" = approvalRule."id"
        ) 
        LEFT JOIN "feature" feature ON featureaction."Feature_id" = feature."id"
      ) 
      LEFT JOIN "contractfeatures" contractfeatures ON feature."id" = contractfeatures."featureId" 
      AND approvalmatrixtemplate."contractId" = contractfeatures."contractId" 
      AND approvalmatrixtemplate."coreCustomerId" = contractfeatures."coreCustomerId"
    ) 
    LEFT JOIN "contractcorecustomers" contractcorecustomers ON approvalmatrixtemplate."contractId" = contractcorecustomers."contractId" 
    AND approvalmatrixtemplate."coreCustomerId" = contractcorecustomers."coreCustomerId"
  ) 
WHERE 
  approvalmatrixtemplate."contractId" = "_contractId" 
  AND approvalmatrixtemplate."coreCustomerId" LIKE "_cif" 
  AND approvalmatrixtemplate."actionId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT("_actions")
      )
  ) 
  AND approvalmatrixtemplate."limitTypeId" LIKE v__limitTypeId 
  AND approvalmatrixtemplate."softdeleteflag" = 0 
  AND featureaction."approveFeatureAction" IS NOT NULL 
  AND featureaction."status" = 'SID_ACTION_ACTIVE' 
  AND featureaction."companyLegalUnit" = v_legalEntityId 
  AND feature."companyLegalUnit" = v_legalEntityId 
ORDER BY 
  approvalmatrixtemplate."contractId", 
  approvalmatrixtemplate."coreCustomerId", 
  approvalmatrixtemplate."limitTypeId", 
  approvalmatrixtemplate."actionId", 
  approvalmatrixtemplate."lowerlimit";
--DBMS_SQL.RETURN_RESULT(v_cursor);
END;
ELSE IF v_isGroupMatrix = 1 THEN BEGIN -- SQLINES LICENSE FOR EVALUATION USE ONLY
OPEN "records" FOR 
SELECT 
  approvalmatrixtemplate."id", 
  approvalmatrixtemplate."contractId", 
  approvalmatrixtemplate."limitTypeId", 
  featureaction."id" "actionId", 
  featureaction."name" "actionName", 
  featureaction."description" "actionDescription", 
  featureaction."Feature_id" "featureId", 
  featureaction."Type_id" "actionType", 
  featureaction."isAccountLevel" "isAccountLevel", 
  feature."name" "featureName", 
  feature."Status_id" "fifeaturestatus", 
  approvalrule."id" "approvalruleId", 
  approvalrule."numberOfApprovals", 
  approvalrule."name" "approvalRuleName", 
  approvalmatrixtemplate."lowerlimit", 
  approvalmatrixtemplate."upperlimit", 
  approvalmatrixtemplate."currency", 
  signatorygroupmatrixtemplate."groupList" "groupList", 
  signatorygroupmatrixtemplate."groupRule" "groupRule", 
  contractcorecustomers."coreCustomerId" "cifId", 
  contractcorecustomers."coreCustomerName" "cifName", 
  approvalmatrixtemplate."invalid", 
  approvalmatrixtemplate."isGroupMatrix" 
FROM 
  (
    (
      (
        (
          (
            (
              "approvalmatrixtemplate" approvalmatrixtemplate 
              LEFT JOIN "signatorygroupmatrixtemplate" signatorygroupmatrixtemplate ON approvalmatrixtemplate."id" = signatorygroupmatrixtemplate."approvalMatrixId"
            ) 
            LEFT JOIN "featureaction" featureAction ON approvalmatrixtemplate."actionId" = featureAction."id"
          ) 
          LEFT JOIN "approvalrule" approvalRule ON approvalmatrixtemplate."approvalruleId" = approvalRule."id"
        ) 
        LEFT JOIN "feature" feature ON featureAction."Feature_id" = feature."id"
      ) 
      LEFT JOIN "contractfeatures" contractfeatures ON feature."id" = contractfeatures."featureId" 
      AND approvalmatrixtemplate."contractId" = contractfeatures."contractId" 
      AND approvalmatrixtemplate."coreCustomerId" = contractfeatures."coreCustomerId"
    ) 
    LEFT JOIN "contractcorecustomers" contractcorecustomers ON approvalmatrixtemplate."contractId" = contractcorecustomers."contractId" 
    AND approvalmatrixtemplate."coreCustomerId" = contractcorecustomers."coreCustomerId"
  ) 
WHERE 
  approvalmatrixtemplate."contractId" = "_contractId" 
  AND approvalmatrixtemplate."coreCustomerId" LIKE "_cif" 
  AND approvalmatrixtemplate."isGroupMatrix" = 1 
  AND approvalmatrixtemplate."actionId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT("_actions")
      )
  ) 
  AND approvalmatrixtemplate."limitTypeId" LIKE v__limitTypeId 
  AND approvalmatrixtemplate."softdeleteflag" = 0 
  AND featureaction."approveFeatureAction" IS NOT NULL 
  AND featureaction."status" = 'SID_ACTION_ACTIVE' 
  AND featureaction."companyLegalUnit" = v_legalEntityId 
  AND feature."companyLegalUnit" = v_legalEntityId 
ORDER BY 
  approvalmatrixtemplate."contractId", 
  approvalmatrixtemplate."coreCustomerId", 
  approvalmatrixtemplate."limitTypeId", 
  approvalmatrixtemplate."actionId", 
  approvalmatrixtemplate."lowerlimit";
--DBMS_SQL.RETURN_RESULT(v_cursor);
END;
END IF;
END IF;
--EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/

create or replace NONEDITIONABLE PROCEDURE "fetch_approvalqueue_proc"
(
  "_customerId" IN VARCHAR2,
  "_transactionIds" IN VARCHAR2,
  "_requestIds" IN VARCHAR2,
  "_featureactionlist" IN VARCHAR2,
  "records" out sys_refcursor
)
AS
   iv_transactionIds NVARCHAR2(50) := "_transactionIds";
   iv_requestIds NVARCHAR2(2000) := "_requestIds";

BEGIN

   DECLARE
      v_combinedIds NVARCHAR2(2000);
      v_alreadyApprovedIds NVARCHAR2(2000);
      v_companyId NVARCHAR2(2000);
      v_customerMatrixIds NVARCHAR2(2000);
      v_approvalRequestIds VARCHAR2(32000);
      v_customerGroupIds NVARCHAR2(32000);
      v_groupIds NVARCHAR2(2000);
      v_features VARCHAR2(32000);
      v_monetaryActions VARCHAR2(32000);
      v_companyRequestIds NVARCHAR2(50);
      v__requestIds NVARCHAR2(50);
      v_requestIds VARCHAR2(32000);
      v_query long;
      v_select_statement long;
      v_strLen NUMBER(10,0);
      v_SubStrLen NUMBER(10,0);

   BEGIN
     
      SELECT LISTAGG("customer"."id", ',') 

        INTO v_combinedIds
        FROM "customer" 
       WHERE  "customer"."combinedUserId" = "_customerId";

      IF v_combinedIds IS NULL or v_combinedIds= '' THEN
       v_combinedIds := "_customerId" ;
      ELSE
         v_combinedIds := ("_customerId" || ',' || v_combinedIds) ;
      END IF;
      IF ( iv_transactionIds IS NULL
        OR iv_transactionIds = ' ' ) THEN
       iv_transactionIds := '''' ;
      ELSE
         iv_transactionIds := iv_transactionIds ;
      END IF;
      IF ( iv_requestIds IS NULL
        OR iv_requestIds = ' ' ) THEN
       iv_requestIds := '''' ;
      ELSE
         iv_requestIds := iv_requestIds ;
      END IF;
      SELECT LISTAGG(CAST("contractcustomers"."contractId"|| '_'|| "contractcustomers"."coreCustomerId" AS NVARCHAR2(2000)), ',') 

        INTO v_companyId
        FROM "contractcustomers" 
       WHERE  "contractcustomers"."customerId" = "_customerId";
      IF v_companyId IS NULL THEN
       v_companyId := ' ' ;
      END IF;
      WHILE "_featureactionlist" IS NULL 
    LOOP
    EXIT;
       --GOTO MAINLABEL$leave;
      END LOOP;
      SELECT LISTAGG(CAST("customerapprovalmatrix"."approvalMatrixId" AS NVARCHAR2(2000)), ',') 

        INTO v_customerMatrixIds
        FROM "customerapprovalmatrix" 
       WHERE  FIND_IN_SET("customerapprovalmatrix"."customerId", v_combinedIds) <> 0;
      IF v_customerMatrixIds IS NULL THEN
       v_customerMatrixIds := '' ;
      END IF;
      
      SELECT LISTAGG(CAST("customersignatorygroup"."signatoryGroupId" AS NVARCHAR2(2000)), ',') 

        INTO v_customerGroupIds
        FROM "customersignatorygroup" 
       WHERE  FIND_IN_SET("customersignatorygroup"."customerId", v_combinedIds) <> 0;
       
      IF v_customerGroupIds IS NULL THEN
       v_customerGroupIds := '' ;
      END IF;
      
      SELECT listagg(distinct CAST("bbactedrequest"."requestId" AS VARCHAR2(2000)),',') within group (order by "requestId")

        INTO v_alreadyApprovedIds
        FROM "bbactedrequest" 
       WHERE  FIND_IN_SET("bbactedrequest"."createdby", v_combinedIds) <> 0
                AND FIND_IN_SET("bbactedrequest"."action", 'Pending') <> 1
                AND "bbactedrequest"."softdeleteflag" = 0;
      IF v_alreadyApprovedIds IS NULL THEN

      BEGIN
         v_alreadyApprovedIds := '' ;

      END;
      END IF;
      dbms_output.put_line('v_alreadyApprovedIds'||v_alreadyApprovedIds);
      SELECT LISTAGG(DISTINCT CAST("requestapprovalmatrix"."requestId" AS VARCHAR2(2000)), ',') within group (order by "requestId")

        INTO v_approvalRequestIds
        FROM "requestapprovalmatrix" 
               JOIN "approvalmatrix"    ON "requestapprovalmatrix"."approvalMatrixId" = "approvalmatrix"."id"
               JOIN "approvalrule"    ON "approvalmatrix"."approvalruleId" = "approvalrule"."id"
       WHERE  FIND_IN_SET(CAST("requestapprovalmatrix"."approvalMatrixId" AS NVARCHAR2(2000)), v_customerMatrixIds) <> 0
                AND NOT FIND_IN_SET(CAST("requestapprovalmatrix"."requestId" AS NVARCHAR2(2000)), v_alreadyApprovedIds) <> 0
                AND ( ( "approvalrule"."numberOfApprovals" = -1
                AND "requestapprovalmatrix"."receivedApprovals" < ( SELECT COUNT(DISTINCT ("customerapprovalmatrix"."customerId"))  
                                                                            FROM "customerapprovalmatrix" 
                                                                             WHERE  "customerapprovalmatrix"."approvalMatrixId" = "requestapprovalmatrix"."approvalMatrixId" ) ) );
      IF v_approvalRequestIds IS NULL THEN

      BEGIN
         v_approvalRequestIds := '' ;
      END;
      END IF;
      
      v_groupIds := v_customerGroupIds;
      
      <<do_this>> LOOP
        dbms_output.put_line('v_groupIds'||v_groupIds);

 v_strLen := LENGTH(v_groupIds);
 SELECT listagg(DISTINCT("requestId"),',') into v_requestIds FROM "signatorygrouprequestmatrix" 
 WHERE NOT FIND_IN_SET("requestId", v_approvalRequestIds) > 0 AND "isApproved" = '0' AND FIND_IN_SET(UTILS.SUBSTRING_INDEX(v_groupIds, ',', 1) ,REPLACE(REPLACE(REPLACE("pendingGroupList",'[',''),']',''),' ','')) > 0;
 IF v_requestIds is NULL THEN
 v_requestIds := '';
 END IF;
  dbms_output.put_line('test'||v_requestIds);

 if(v_approvalRequestIds = '' OR v_approvalRequestIds IS NULL) then 
          v_approvalRequestIds := v_requestIds;
        else 
          v_approvalRequestIds := v_approvalRequestIds || ',' || v_requestIds;
        end if;
 v_SubStrLen := LENGTH(SUBSTRING_INDEX(v_groupIds, ',', 1));
 v_groupIds := SUBSTR(SUBSTR(v_groupIds,v_SubStrLen + 2),0,v_strLen);
 IF LENGTH(v_groupIds) <= 0 or v_groupIds is null THEN
 EXIT do_this;
 END IF;
 END LOOP;


      
      SELECT STRING_AGG(CAST("featureaction"."Feature_id" AS VARCHAR2(2000)))
        INTO v_features
        FROM "featureaction" 
       WHERE  FIND_IN_SET("featureaction"."id", "_featureactionlist") <> 0;
      IF v_features IS NULL THEN

      BEGIN
         v_features := ' ' ;

      END;
      END IF;
      SELECT LISTAGG(CAST("featureaction"."id" AS NVARCHAR2(2000)), ',') 

        INTO v_monetaryActions
        FROM "featureaction" 
       WHERE  FIND_IN_SET("featureaction"."Feature_id", v_features) <> 0;
      IF v_monetaryActions IS NULL THEN

      BEGIN
         v_monetaryActions := ' ' ;

      END;
      END IF;
      dbms_output.put_line('v_companyId'||v_companyId);
      dbms_output.put_line('v_monetaryActions'||v_monetaryActions);
      SELECT LISTAGG("bbrequest"."requestId", ',') 

        INTO v_companyRequestIds
        FROM "bbrequest"
       WHERE  FIND_IN_SET("bbrequest"."companyId", v_companyId) <> 0
                AND FIND_IN_SET("bbrequest"."featureActionId", v_monetaryActions) <> 0;
      IF v_companyRequestIds IS NULL THEN

      BEGIN
         v_companyRequestIds := ' ' ;

      END;
      END IF;
        IF iv_requestIds = '''' THEN
       v__requestIds := v_companyRequestIds ;
      ELSE
         v__requestIds := iv_requestIds ;
      END IF;
      dbms_output.put_line('v__requestIds'||v__requestIds);
      IF iv_transactionIds = '''' THEN
       v_query := 'WHERE FIND_IN_SET("bbrequest"."requestId",''' || v__requestIds || ''')>0' ;
      ELSE
         v_query := 'WHERE FIND_IN_SET("bbrequest"."transactionId",'|| iv_transactionIds ||  ')>0' || 'AND FIND_IN_SET("bbrequest"."featureActionId",'|| v_monetaryActions || ')>0' ;
      END IF;
    v_select_statement := ('SELECT 
                                                distinct("bbrequest"."requestId"),
                                                "bbrequest"."transactionId",
                                                "bbrequest"."status",
                                         "bbrequest"."featureActionId",
                                                "bbrequest"."isGroupMatrix",
                                                "bbrequest"."companyId",
                                                "bbrequest"."accountId",
                        "bbrequest"."additionalMeta",
                                            (CASE
                                               WHEN FIND_IN_SET(CAST("bbrequest"."createdby" AS nvarchar2(2000)),''' || v_combinedIds || ''') > 0 THEN ''true''
                                               ELSE ''false''
                                               END) as "amICreator",

                                            (CASE 
                                               WHEN FIND_IN_SET(CAST("bbrequest"."requestId" AS nvarchar2(2000)),''' || v_approvalRequestIds || ''') >0 THEN ''true''
                                               ELSE ''false''
                                            END) as "amIApprover",
                                            (CASE
                                               WHEN FIND_IN_SET(CAST("bbrequest"."requestId" AS nvarchar2(2000)),''' || v_alreadyApprovedIds || ''') > 0 THEN ''true''
                                               ELSE ''false''
                                             END) as "actedByMeAlready",
                                             (select count(DISTINCT("createdby")) from "bbactedrequest" where "bbactedrequest"."action" = ''Approved'' AND  "bbactedrequest"."requestId" = "bbrequest"."requestId" AND "bbactedrequest"."softdeleteflag" = 0) 
                                                as "receivedApprovals",
                                                       
                                            CASE 
    WHEN "bbrequest"."isGroupMatrix" = 0 THEN
        LEAST(
            (SELECT COUNT(DISTINCT "customerId") FROM "customerapprovalmatrix" WHERE "customerapprovalmatrix"."approvalMatrixId" IN (SELECT "approvalMatrixId" FROM "requestapprovalmatrix" WHERE "requestapprovalmatrix"."requestId" = "bbrequest"."requestId")),
            SUM(
                CASE 
                    WHEN "approvalrule"."numberOfApprovals" = -1 THEN (SELECT COUNT(*) FROM "customerapprovalmatrix" WHERE "customerapprovalmatrix"."approvalMatrixId" = "requestapprovalmatrix"."approvalMatrixId")
                    WHEN "approvalrule"."numberOfApprovals" IS NULL OR "approvalrule"."numberOfApprovals" = '''' THEN 0
                    ELSE "approvalrule"."numberOfApprovals"
                END
            )
        )
    ELSE NULL
END AS "requiredApprovals"

                                          FROM
                                           "bbrequest"
                                          LEFT JOIN "requestapprovalmatrix" ON ("bbrequest"."requestId" = "requestapprovalmatrix"."requestId")
                                          LEFT JOIN "approvalmatrix" ON ("requestapprovalmatrix"."approvalMatrixId" = "approvalmatrix"."id")
                                          LEFT JOIN "approvalrule" ON ("approvalmatrix"."approvalruleId" = "approvalrule"."id")'||v_query|| ' GROUP BY
    "bbrequest"."requestId",
    "bbrequest"."transactionId",
    "bbrequest"."status",
    "bbrequest"."featureActionId",
    "bbrequest"."isGroupMatrix",
    "bbrequest"."companyId",
    "bbrequest"."accountId",
    "bbrequest"."additionalMeta",
    "bbrequest"."createdby",
    "approvalrule"."numberOfApprovals",
    "requestapprovalmatrix"."approvalMatrixId"
');
                                            
      WHILE v_select_statement IS NULL 
    LOOP
     EXIT;
       --GOTO MAINLABEL$leave;
      END LOOP;
      dbms_output.put_line('v_select_statement'||v_select_statement);

      open "records" for v_select_statement;


   END;

END;
/

--  DDL for Procedure fetch_approvalrequests_counts_proc


create or replace NONEDITIONABLE PROCEDURE "fetch_approvalrequests_counts_proc" (
  "_permissionListArr" IN VARCHAR2, 
  "_userId" IN VARCHAR2, "records" OUT SYS_REFCURSOR
) AS v_req_pending_status VARCHAR2(4000);
v_execStmt VARCHAR2(4000);
BEGIN v_req_pending_status := 'Pending For Approval';
-- set it to the current string used to denote the Pending status
v_execStmt := (
  '
       select ''pendingRequests'' as category, count(*) as counts from "approvalrequests" where "createdby" = ''' || "_userId" || ''' and "status" = ''' || v_req_pending_status || '''
       union
       select ''requestHistory'' as category, count(*) as counts from "approvalrequests" where "createdby" = ''' || "_userId" || '''
       union
       select ''approvalHistory'' as category, count(*) as counts from "approvalrequests" where "checkedBy" = ''' || "_userId" || '''
       union
       select ''pendingApprovals'' as category, count(*) as counts from "approvalrequests" where "status" = ''' || v_req_pending_status || ''' and "createdby" != ''' || "_userId" || ''' and "expAPIOperationName" in 
       (select "expAPIOperationName" from "permissionapprovals" where "approvalPermissionName" in (' || "_permissionListArr" || '))'
);
EXECUTE IMMEDIATE v_execStmt;
OPEN "records" FOR v_execStmt;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/

--  DDL for Procedure fetch_approvers_proc


create or replace NONEDITIONABLE PROCEDURE "fetch_approvers_proc" (
  "_requestId" IN VARCHAR2, "records" OUT SYS_REFCURSOR
) AS BEGIN OPEN "records" FOR 
SELECT 
  "customerapprovalmatrix"."customerId" customerId 
FROM 
  "customerapprovalmatrix" 
WHERE 
  "customerapprovalmatrix"."approvalMatrixId" IN (
    SELECT 
      "requestapprovalmatrix"."approvalMatrixId" 
    FROM 
      "requestapprovalmatrix" 
    WHERE 
      "requestapprovalmatrix"."requestId" = "_requestId"
  );
--DBMS_SQL.RETURN_RESULT(v_cursor);
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;

/


--  DDL for Procedure fetch_pending_approvals_proc


CREATE OR REPLACE NONEDITIONABLE PROCEDURE "fetch_pending_approvals_proc" (
  v_permissionListArr IN VARCHAR2, v_userId IN VARCHAR2, 
  v_moduleListArr IN VARCHAR2, v_actionListArr IN VARCHAR2, 
  v_featureListArr IN VARCHAR2, v_searchStartDate IN VARCHAR2, 
  v_searchEndDate IN VARCHAR2, v_sortParam IN VARCHAR2, 
  v_sortOrder IN VARCHAR2, "approvalrequests" OUT SYS_REFCURSOR
) AS dateFilterStmt VARCHAR2(256);
dateField VARCHAR2(128);
req_pending_status VARCHAR2(64);
sqlStmt VARCHAR2(4000);
sortParam VARCHAR2(64);
BEGIN dateField := 'ar.createdts';
IF v_sortParam IS NOT NULL THEN sortParam := 'ar.' || v_sortParam || ' ';
END IF;
IF v_searchStartDate IS NULL THEN dateFilterStmt := '';
ELSE dateFilterStmt := ' AND TO_CHAR(' || dateField || ', ''YYYY-MM-DD'') BETWEEN ''' || v_searchStartDate || ''' AND ';
IF v_searchEndDate IS NOT NULL THEN dateFilterStmt := dateFilterStmt || '''' || v_searchEndDate || '''';
ELSE dateFilterStmt := dateFilterStmt || '(SELECT TO_CHAR(SYSDATE, ''YYYY-MM-DD'') FROM DUAL)';
END IF;
END IF;
req_pending_status := 'Pending For Approval';
sqlStmt := 'SELECT ar. "requestId" , ar. "recordId" , ar. "module" , ar. "feature" , ar. "expAPIOperationName" , ar. "expAPINickName" , ar. "permissionId" , ar. "permissionName" , cru. "Username"  asCREATEdby , ar. "createdts" , ar. "status" , cku. "Username"  as  checkedBy , ar. "checkedts" , ar. "reason" 
               FROM  "approvalrequests"  ar
                  LEFT JOIN  "systemuser"   cru ON cru. "id"  = ar. "createdby" 
                  LEFT JOIN  "systemuser"  cku ON cku. "id"  = ar. "checkedBy" 
               WHERE ar. "status"  = ''' || req_pending_status || ''' AND ar. "createdby"  != ''' || v_userId || '''';
IF v_moduleListArr IS NOT NULL THEN sqlStmt := sqlStmt || ' AND ar. "module"  IN (' || v_moduleListArr || ')';
END IF;
IF v_featureListArr IS NOT NULL THEN sqlStmt := sqlStmt || ' AND ar. "feature"  IN (' || v_featureListArr || ')';
END IF;
IF v_actionListArr IS NOT NULL THEN sqlStmt := sqlStmt || ' AND ar. "expAPINickName"  IN (' || v_actionListArr || ')';
END IF;
sqlStmt := sqlStmt || dateFilterStmt;
sqlStmt := sqlStmt || ' AND ar. "expAPIOperationName"  IN( 
                           SELECT  "expAPIOperationName"  FROM  "permissionapprovals"  
                           WHERE  "approvalPermissionName"  IN (' || v_permissionListArr || ') )';
sqlStmt := sqlStmt || ' ORDER BY ';
IF(
  sortParam IS NOT NULL 
  AND sortParam IN (dateField)
) THEN sqlStmt := sqlStmt || sortParam;
ELSE sqlStmt := sqlStmt || dateField;
END IF;
IF(
  v_sortOrder IS NOT NULL 
  AND (
    UPPER(v_sortOrder)
  ) IN ('DESC', 'ASC')
) THEN sqlStmt := sqlStmt || ' ' || v_sortOrder;
ELSE sqlStmt := sqlStmt || ' ' || 'DESC';
END IF;
OPEN "approvalrequests" FOR sqlStmt;
END;
/

--  DDL for Procedure fetch_requestapprovalmatrix_details_proc


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "fetch_requestapprovalmatrix_details_proc" (
  v_requestId IN nvarchar2, v_customerId IN nvarchar2, 
  "bbrequest" out SYS_REFCURSOR
) AS v_stmt nvarchar2(2000);
BEGIN OPEN "bbrequest" FOR 
SELECT 
  bb."requestId" requestId, 
  am."id" approvalMatrixId, 
  ram."id" requestApprovalMatrixId, 
  ram."receivedApprovals" receivedApprovals, 
  decode(
    ar."numberOfApprovals", -1, na.numberOfApprovals, 
    ar."numberOfApprovals"
  ) numberOfApprovals, 
  cam."customerId" customerId 
FROM 
  "bbrequest" bb 
  JOIN "requestapprovalmatrix" ram on bb."requestId" = ram."requestId" 
  and CAST(
    bb."requestId" as nvarchar2(2000)
  ) = v_requestId 
  JOIN "approvalmatrix" am on ram."approvalMatrixId" = am."id" 
  JOIN "customerapprovalmatrix" cam on cam."customerId" = v_customerId 
  JOIN "approvalrule" ar on am."approvalruleId" = ar."id" 
  JOIN (
    SELECT 
      "approvalMatrixId", 
      count(
        DISTINCT ("customerId")
      ) numberOfApprovals 
    FROM 
      "customerapprovalmatrix" 
    GROUP BY 
      "approvalMatrixId"
  ) na on na."approvalMatrixId" = am."id";
END;
/

--  DDL for Procedure fetch_signatorygroups_in_approvalrule_proc


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "fetch_signatorygroups_in_approvalrule_proc" (
  "signatorygroup" OUT SYS_REFCURSOR
) AS BEGIN OPEN "signatorygroup" FOR 
SELECT 
  "signatorygroup"."signatoryGroupId" 
FROM 
  "signatorygroup" 
WHERE 
  "signatorygroup"."signatoryGroupId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(
          (
            SELECT 
              LISTAGG(
                REPLACE(
                  REPLACE(
                    REPLACE(
                      "signatorygroupmatrix"."groupList", 
                      ']', ' '
                    ), 
                    '[', 
                    ' '
                  ), 
                  '"', 
                  ' '
                ), 
                ','
              ) 
            FROM 
              "signatorygroupmatrix" 
            WHERE 
              "signatorygroupmatrix"."approvalMatrixId" IN (
                SELECT 
                  "approvalmatrix"."id" 
                FROM 
                  "approvalmatrix" 
                WHERE 
                  "approvalmatrix"."softdeleteflag" = 'false'
              ) 
              AND "signatorygroupmatrix"."softdeleteflag" = 'false'
          )
        )
      )
  );
--DBMS_SQL.RETURN_RESULT(v_cursor);
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/

CREATE OR REPLACE NONEDITIONABLE PROCEDURE "get_actions_with_approvefeatureaction_proc" 
(
  "_featureActions" IN NVARCHAR2,
  "records" OUT SYS_REFCURSOR
)
AS
   v_actionsList NVARCHAR2(2000);


BEGIN

   SELECT listagg(CAST("featureaction"."id" AS NVARCHAR2(2000)), ',') 

     INTO v_actionsList
     FROM "featureaction" 
    WHERE  FIND_IN_SET("id", "_featureActions") <> 0
             AND ("featureaction"."approveFeatureAction" is not null or "featureaction"."approveFeatureAction" != '' );
   OPEN  "records" FOR
      SELECT v_actionsList "actions"  
        FROM DUAL  ;

END;
/

CREATE OR REPLACE NONEDITIONABLE PROCEDURE "get_signatorygroup_approvers_proc" (
  v__groupList IN VARCHAR2, "customersignatorygroup" OUT SYS_REFCURSOR
) AS v_execStmt VARCHAR2(4000);
BEGIN v_execStmt := 'select "customerId" from "customersignatorygroup" where "customersignatorygroup"."signatoryGroupId" IN ( SELECT column_value from TABLE(UTILS.STRING_SPLIT(''' || REPLACE(
  REPLACE(
    REPLACE(v__groupList, '[', ' '), 
    ']', 
    ' '
  ), 
  ' ', 
  ' '
) || ''' )))';
OPEN "customersignatorygroup" FOR v_execStmt;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/

--  DDL for Procedure getRequestApprovers_proc



create or replace NONEDITIONABLE PROCEDURE         "getRequestApprovers_proc" 
(
  "_requestId" IN NVARCHAR2,
  "_status" IN NVARCHAR2,
  "records" OUT SYS_REFCURSOR
)
AS
  v_isGroupMatrix NVARCHAR2(50);
BEGIN
  dbms_output.put_line('hi');
   IF "_status" IS NULL
     OR "_status" = ' ' THEN
   BEGIN
   SELECT "isGroupMatrix"
  INTO v_isGroupMatrix
     FROM "bbrequest" 
    WHERE  "bbrequest"."requestId" = "_requestId";
   IF v_isGroupMatrix = ' 1 ' THEN
    OPEN  "records" FOR
    
SELECT "_requestId"  "requestId",
"csg"."customerId" "approvers"  ,
                   "c"."FirstName" "FirstName",
                   "c"."LastName" "LastName"       
        FROM "customersignatorygroup" "csg" 
    LEFT JOIN "customer" "c"   ON "c"."id" = "csg"."customerId"
       WHERE  FIND_IN_SET("csg"."signatoryGroupId", ( SELECT LISTAGG(REPLACE(REPLACE(REPLACE("signatorygrouprequestmatrix"."pendingGroupList", ' ] ', ' '), ' [ ', ' '), ' "', ' '), ',') 
                                                             FROM "signatorygrouprequestmatrix" 
                                                              WHERE  "signatorygrouprequestmatrix"."requestId"="_requestId"
                                                            AND "signatorygrouprequestmatrix"."isApproved"='0' )) > 0 ;
   ELSE
    OPEN  "records" FOR
      SELECT MIN("bb"."requestId")  "requestId"  ,
             "cam"."customerId" "approvers"  ,
             MIN("c"."FirstName")  "FirstName"  ,
             MIN("c"."LastName")  "LastName"  
        FROM "bbrequest" "bb"
               CROSS JOIN "requestapprovalmatrix" "ram"
               CROSS JOIN "customerapprovalmatrix" "cam"
               CROSS JOIN "customer" "c"
       WHERE  "bb"."requestId" = "ram"."requestId"
                AND "ram"."approvalMatrixId" = "cam"."approvalMatrixId"
                AND "cam"."customerId" = "c"."id"
                AND CAST("bb"."requestId" AS NVARCHAR2(2000)) = "_requestId"
        GROUP BY "cam"."customerId"
        ORDER BY "cam"."customerId" ;
    
    END IF;
    END;
      
   ELSE

      OPEN  "records" FOR
         SELECT "bb"."createdby" "approvers"  ,
                MIN("c"."FirstName")  "FirstName"  ,
                MIN("c"."LastName")  "LastName"  
           FROM "bbactedrequest" "bb"
                  CROSS JOIN "customer" "c"
          WHERE  "bb"."createdby" = "c"."id"
                   AND CAST("bb"."requestId" AS NVARCHAR2(2000)) = "_requestId"
                   AND "bb"."status" = "_status"
           GROUP BY "bb"."createdby"
           ORDER BY "bb"."createdby" ;

   END IF;
END;
/

--  DDL for Procedure increment_receivedapprovals_proc


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "increment_receivedapprovals_proc" (
  v__requestId IN VARCHAR2, v__approvalMatrixId IN VARCHAR2
) AS BEGIN 
UPDATE 
  "requestapprovalmatrix" 
SET 
  "receivedApprovals" = "receivedApprovals" + 1 
WHERE 
  (
    "requestId" = v__requestId 
    AND "approvalMatrixId" = v__approvalMatrixId
  );
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/

--  DDL for Procedure manageapprovalmatrix_update_proc


create or replace NONEDITIONABLE PROCEDURE "manageapprovalmatrix_update_proc" 
(
  "_contractId" IN NVARCHAR2,
  "_cifList" IN NVARCHAR2,
  "_isDisabledflag" IN NVARCHAR2
)
AS
   v_index1 NUMBER(10,0) := 0;
   v_numOfRecords NUMBER(10,0);
   v_recordsData NVARCHAR2(2000);
   v_count NUMBER(10,0) := 0;
   v_query NVARCHAR2(2000);
   v_query2 NVARCHAR2(2000);
   v_cquery NVARCHAR2(2000);
   v_var NVARCHAR2(2000);
   stmt varchar2(2000);
   stmt1 varchar2(2000);
   stmt2 varchar2(2000);
   v_temp varchar2(2000);
   cnt NUMBER;

BEGIN   

   v_numOfRecords := LENGTH("_cifList") - LENGTH(REPLACE("_cifList", ',', '')) + 1 ;
   WHILE ( 1 = 1 ) 
   LOOP 

      BEGIN
         v_index1 := v_index1 + 1 ;
         IF v_index1 = v_numOfRecords + 1 THEN
          EXIT;
         ELSE

         BEGIN
            v_recordsData := (SUBSTRING_INDEX(SUBSTRING_INDEX("_cifList", ',', v_index1), ',', -1)) ;
                select count(*) into v_count from "manageapprovalmatrix" where "contractId"= "_contractId"  and "coreCustomerId"= v_recordsData ;
            IF v_count = 0 THEN

            BEGIN
               v_query := CONCAT('INSERT INTO "manageapprovalmatrix"("contractId","coreCustomerId","isDisabled") VALUES (''' || "_contractId" || ''',' || v_recordsData || ',' || "_isDisabledflag", ')') ;
               EXECUTE IMMEDIATE v_query;

            END;
            ELSE
            BEGIN
               v_query2 := 'UPDATE "manageapprovalmatrix" set "isDisabled"=' || "_isDisabledflag" || ' where "contractId"=' || "_contractId" || ' and "coreCustomerId"=' || v_recordsData ;
                EXECUTE IMMEDIATE v_query2;
            END;
            END IF;        

         END;
         END IF;

      END;
   END LOOP;

END;
/

--  DDL for Procedure role_data_movement_to_approval_proc

CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "role_data_movement_to_approval_proc" (
  v_requestId IN VARCHAR2, v_roleId IN VARCHAR2
) AS v_columnnames CLOB;
v_insertquery CLOB;
BEGIN 
Select 
  listagg(column_name, ',') WITHIN GROUP (
    ORDER BY 
      COLUMN_ID
  ) INTO v_columnnames 
from 
  user_tab_cols 
where 
  table_name = 'role';
v_insertquery := 'insert into "role_approval" select ' || v_columnnames || ',''' || v_requestId || ''',' || '''' || 'NONE' || '''' || ' from "role" where "id"=' || '''' || v_roleId || ''';';
execute immediate v_insertquery;
Select 
  listagg(column_name, ',') WITHIN GROUP (
    ORDER BY 
      COLUMN_ID
  ) INTO v_columnnames 
from 
  user_tab_cols 
where 
  table_name = 'rolepermission';
v_insertquery := 'insert into "rolepermission_approval" select ' || v_columnnames || ',''' || v_requestId || ''',' || '''' || 'NONE' || '''' || ' from "rolepermission" where "Role_id"=' || '''' || v_roleId || ''';';
execute immediate v_insertquery;
Select 
  listagg(column_name, ',') WITHIN GROUP (
    ORDER BY 
      COLUMN_ID
  ) INTO v_columnnames 
from 
  user_tab_cols 
where 
  table_name = 'userrole';
v_insertquery := 'insert into "userrole_approval" select ' || v_columnnames || ',''' || v_requestId || ''',' || '''' || 'NONE' || '''' || ' from "userrole" where "Role_id"=' || '''' || v_roleId || ''';';
execute immediate v_insertquery;
Select 
  listagg(column_name, ',') WITHIN GROUP (
    ORDER BY 
      COLUMN_ID
  ) INTO v_columnnames 
from 
  user_tab_cols 
where 
  table_name = 'userroleservicedefinition';
v_insertquery := 'insert into "userroleservicedefinition_approval" select ' || v_columnnames || ',''' || v_requestId || ''',' || '''' || 'NONE' || '''' || ' from "userroleservicedefinition" where "UserRole_id"=' || '''' || v_roleId || ''';';
execute immediate v_insertquery;
Select 
  listagg(column_name, ',') WITHIN GROUP (
    ORDER BY 
      COLUMN_ID
  ) INTO v_columnnames 
from 
  user_tab_cols 
where 
  table_name = 'rolecompositeaction';
v_insertquery := 'insert into "rolecompositeaction_approval" select ' || v_columnnames || ',''' || v_requestId || ''',' || '''' || 'NONE' || '''' || ' from "rolecompositeaction" where "Role_id"=' || '''' || v_roleId || ''';';
execute immediate v_insertquery;
END;
/

--  DDL for Procedure rolepermission_data_movement_approval_proc

CREATE OR REPLACE NONEDITIONABLE PROCEDURE "rolepermission_data_movement_approval_proc" (
  v__requestId IN VARCHAR2, v__context IN VARCHAR2
) AS BEGIN BEGIN IF v__context = 'Approved' THEN BEGIN INSERT INTO "rolepermission" (
  SELECT 
    "Role_id", 
    "Permission_id", 
    "createdby", 
    "modifiedby", 
    "createdts", 
    "lastmodifiedts", 
    "synctimestamp", 
    "softdeleteflag", 
    "companyLegalUnit" 
  FROM 
    "rolepermission_approval" 
  WHERE 
    "aprRequestId" = v__requestId
);
END;
END IF;
DELETE "rolepermission_approval" 
WHERE 
  "aprRequestId" = v__requestId 
  AND "Role_id" != ' ' 
  AND "Permission_id" != ' ';
--SQLDEV: NOT RECOGNIZED
END;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/

--  DDL for Procedure rolepermission_delete_approval_proc

CREATE OR REPLACE NONEDITIONABLE PROCEDURE "rolepermission_delete_approval_proc" (
  v__roleId IN VARCHAR2, v__PermissionIds IN CLOB, 
  v__requestId IN VARCHAR2, v__context IN VARCHAR2
) AS v_caid VARCHAR2(50);
v_isEnabled VARCHAR2(10);
v_finished NUMBER(10, 0) := 0;
v_caid_count NUMBER(10, 0) := 0;
CURSOR caids IS 
SELECT 
  c."id", 
  c."isEnabled" 
FROM 
  "compositeaction" c 
WHERE 
  (
    "Permission_id" IN (
      SELECT 
        COLUMN_VALUE 
      FROM 
        TABLE(
          UTILS.STRING_SPLIT(v__PermissionIds)
        )
    )
  );
BEGIN --   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
OPEN caids;
<< MANAGECAIDS >> WHILE 1 = 1 LOOP BEGIN FETCH caids INTO v_caid, 
v_isEnabled;
IF caids % FOUND <> FALSE THEN BEGIN GOTO MANAGECAIDS$LEAVE;
END;
END IF;
SELECT 
  COUNT(*) INTO v_caid_count 
FROM 
  "compositeaction" c, 
  "rolepermission" rp 
WHERE 
  rp."Role_id" = v__roleId 
  AND rp."Permission_id" = c."Permission_id" 
  AND rp."Permission_id" NOT IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(v__PermissionIds)
      )
  ) 
  AND c."id" = v_caid;
IF v_caid_count = 0 THEN BEGIN IF v__context = 'Approved' THEN BEGIN DELETE "rolecompositeaction" 
WHERE 
  "Role_id" = v__roleId 
  AND "CompositeAction_id" = v_caid;
END;
END IF;
DELETE "rolecompositeaction_approval" 
WHERE 
  "Role_id" = v__roleId 
  AND "CompositeAction_id" = v_caid 
  AND "aprRequestId" = v__requestId;
END;
END IF;
END;
END LOOP;
<< MANAGECAIDS$LEAVE >> CLOSE caids;
IF v__context = 'Approved' THEN BEGIN DELETE "rolepermission" 
WHERE 
  "Role_id" = v__roleId 
  AND "Permission_id" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(v__PermissionIds)
      )
  );
END;
END IF;
DELETE "rolepermission_approval" 
WHERE 
  "Role_id" = v__roleId 
  AND "Permission_id" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(v__PermissionIds)
      )
  ) 
  AND "aprRequestId" = v__requestId;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/

--  DDL for Procedure rolepermission_update_approval_proc


CREATE OR REPLACE NONEDITIONABLE PROCEDURE "rolepermission_update_approval_proc" (
  v__roleId IN VARCHAR2, v__PermissionIds IN CLOB, 
  v__requestId IN VARCHAR2
) AS v_caid VARCHAR2(50);
v_isEnabled VARCHAR2(10);
v_finished NUMBER(10, 0) := 0;
v_caid_count NUMBER(10, 0) := 0;
CURSOR caids IS 
SELECT 
  c."id", 
  c."isEnabled" 
FROM 
  "compositeaction" c 
WHERE 
  (
    "Permission_id" IN (
      SELECT 
        COLUMN_VALUE 
      FROM 
        TABLE(
          UTILS.STRING_SPLIT(v__PermissionIds)
        )
    )
  );
BEGIN --   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
OPEN caids;
<< MANAGECAIDS >> WHILE 1 = 1 LOOP BEGIN FETCH caids INTO v_caid, 
v_isEnabled;
IF caids % FOUND <> FALSE THEN BEGIN GOTO MANAGECAIDS$LEAVE;
END;
END IF;
SELECT 
  COUNT(*) INTO v_caid_count 
FROM 
  "compositeaction" c, 
  "rolepermission" rp 
WHERE 
  rp."Role_id" = v__roleId 
  AND rp."Permission_id" = c."Permission_id" 
  AND (
    rp."Permission_id" NOT IN (
      SELECT 
        COLUMN_VALUE 
      FROM 
        TABLE(
          UTILS.STRING_SPLIT(v__PermissionIds)
        )
    )
  ) 
  AND c."id" = v_caid;
IF v_caid_count = 0 THEN BEGIN 
UPDATE 
  "rolecompositeaction_approval" 
SET 
  "crudAction" = 'DEL' 
WHERE 
  "aprRequestId" = v__requestId 
  AND "CompositeAction_id" = v_caid;
END;
END IF;
END;
END LOOP;
<< MANAGECAIDS$LEAVE >> CLOSE caids;
UPDATE 
  "rolepermission_approval" 
SET 
  "crudAction" = 'DEL' 
WHERE 
  "aprRequestId" = v__requestId 
  AND "Role_id" = v__roleId 
  AND (
    "Permission_id" IN (
      SELECT 
        COLUMN_VALUE 
      FROM 
        TABLE(
          UTILS.STRING_SPLIT(v__PermissionIds)
        )
    )
  );
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/




CREATE OR REPLACE NONEDITIONABLE PROCEDURE "create_campaign_proc"(
v_eventTriggerIdList VARCHAR2(max);
v_profileIdList VARCHAR2(max);
v_channelType VARCHAR2(max);
v_offlineTemplate VARCHAR2(max);
v_onlineContent VARCHAR2(max);
v_channelDetails VARCHAR2(max);

v_campaignId  VARCHAR2(50);
v_campaignName  VARCHAR2(50);
v_campaignDescription VARCHAR2(100);
v_campaignPriority NUMBER(10; 0) ;
v_startDate   VARCHAR2(50);
v_endDate   VARCHAR2(50);
v_campaignType  VARCHAR2(50);
v_objectiveType  VARCHAR2(50);
v_productId  VARCHAR2(50);
v_productGroupId  VARCHAR2(50);
v_campaignStatus  VARCHAR2(50);
)
BEGIN
	
 FINISHED NUMBER(10) DEFAULT 0;
 campaignIdcur varchar2(255 char) DEFAULT "" ;
 campaignPriorityCur varchar2(255 char) DEFAULT "" ;
 finalCampaigns VARCHAR2(255 CHAR) DEFAULT "";

	CURSOR campaignscursor IS (select campaigndefinition.campaignId,campaigndefinition.campaignPriority from campaigndefinition where campaigndefinition.campaignId !=  campaignId and campaigndefinition.campaignPriority >= campaignPriority order by campaigndefinition.campaignPriority );
       
	  
  	INSERT INTO campaigndefinition(campaignId, campaignName,campaignDescription,objectiveType,productId,productGroupId,campaignPriority,campaignType,campaignStatus,startDate,endDate ) 
	  values (campaignId, campaignName,campaignDescription,objectiveType,productId,productGroupId,campaignPriority,campaignType,campaignStatus,startDate,endDate);
      @index := 0;
      @numOfRecords := LENGTH(eventTriggerIdList) - LENGTH(REPLACE(eventTriggerIdList, '|', '')) + 1;
      <<insertRecords>> LOOP
          @index := @index + 1;
          IF @index = @numOfRecords + 1 THEN
            EXIT insertRecords;
          else
            v_id := SUBSTRING_INDEX(SUBSTRING_INDEX(eventTriggerIdList, '|', @index), '|', -1 );
            
            INSERT INTO campaigneventtrigger(campaignId, eventTriggerId) values (campaignId, v_id);
           END IF;
      END LOOP insertRecords;
     
      @index := 0;
      @numOfRecords := LENGTH(profileIdList) - LENGTH(REPLACE(profileIdList, '|', '')) + 1;
      <<insertRecords>> LOOP
          @index := @index + 1;
          IF @index = @numOfRecords + 1 THEN
            EXIT insertRecords;
          else
            v_profileId := SUBSTRING_INDEX(SUBSTRING_INDEX(profileIdList, '|', @index), '|', -1 );
            
            INSERT INTO campaignprofile(campaignId, profileId) values (campaignId, v_profileId);
           END IF;
      END LOOP insertRecords;
     
      @index := 0;
      @numOfRecords := LENGTH(channelType) - LENGTH(REPLACE(channelType, '|', '')) + 1;
      <<insertRecords>> LOOP
          @index := @index + 1;
          IF @index = @numOfRecords + 1 THEN
            EXIT insertRecords;
          else
            v_chnlType := SUBSTRING_INDEX(SUBSTRING_INDEX(channelType, '|', @index), '|', -1 );
            
            INSERT INTO campaignchanneltype(campaignId, channelType) values (campaignId, v_chnlType);
           END IF;
      END LOOP insertRecords;
     
     @index := 0;
      @numOfRecords := LENGTH(offlineTemplate) - LENGTH(REPLACE(offlineTemplate, '|', '')) + 1;
      <<insertRecords>> LOOP
          @index := @index + 1;
          IF @index = @numOfRecords + 1 THEN
            EXIT insertRecords;
          else
            v_recordsData := SUBSTRING_INDEX(SUBSTRING_INDEX(offlineTemplate, '|', @index), '|', -1 );
            v_offlineTemplateId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, '$',1 ), '$', -1 );
            v_channelSubType := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, '$',2 ), '$', -1 );
            v_subject := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, '$',3 ), '$', -1 );
            v_messageContent := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, '$',4 ), '$', -1 );
            
            INSERT INTO offlinetemplate(offlineTemplateId, campaignId,channelSubType,subject,content) values (v_offlineTemplateId,campaignId, v_channelSubType,v_subject,v_messageContent);
           END IF;
      END LOOP insertRecords;
     
     @index := 0;
      @numOfRecords := LENGTH(onlineContent) - LENGTH(REPLACE(onlineContent, '|', '')) + 1;
      <<insertRecords>> LOOP
          @index := @index + 1;
          IF @index = @numOfRecords + 1 THEN
            EXIT insertRecords;
          else
            v_recordsData := SUBSTRING_INDEX(SUBSTRING_INDEX(onlineContent, '|', @index), '|', -1 );
            v_onlineContentId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, '$',1 ), '$', -1 );
            v_placeholderId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, '$',2 ), '$', -1 );
            v_targetURL := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, '$',3 ), '$', -1 );
            v_imageURL := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, '$',4 ), '$', -1 );
            v_callToActionButtonLabel := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, '$',5 ), '$', -1 );
            v_callToActionTargetURL := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, '$',6 ), '$', -1 );
            v_showReadLaterButton := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, '$',7 ), '$', -1 );
            v_showCloseIcon := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, '$',8 ), '$', -1 );
            v_bannerTitle := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, '$',9 ), '$', -1 );
            v_bannerDescription := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, '$',10 ), '$', -1 );
            
            INSERT INTO onlinecontent(onlineContentId, targetURL,campaignId,placeholderId,imageURL,imageIndex,callToActionButtonLabel,callToActionTargetURL,showReadLaterButton,showCloseIcon,bannerTitle,bannerDescription) 
           	values 
          	(v_onlineContentId, v_targetURL,campaignId,v_placeholderId,v_imageURL,@imageIndex,v_callToActionButtonLabel,v_callToActionTargetURL,v_showReadLaterButton,v_showCloseIcon,v_bannerTitle,v_bannerDescription);
           END IF;
      END LOOP insertRecords;
     
      @index := 0;
      @numOfRecords := LENGTH(channelDetails) - LENGTH(REPLACE(channelDetails, '|', '')) + 1;
      <<insertRecords>> LOOP
          @index := @index + 1;
          IF @index = @numOfRecords + 1 THEN
            EXIT insertRecords;
          else
            v_recordsData := SUBSTRING_INDEX(SUBSTRING_INDEX(channelDetails, '|', @index), '|', -1 );
            v_channelSubType := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, '$',1 ), '$', -1 );
            v_channelPriority := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, '$',2 ), '$', -1 );
            
            INSERT INTO campaignchanneldetails(campaignId,channelPriority,channelSubType) values (campaignId,v_channelPriority,v_channelSubType);
           END IF;
      END LOOP insertRecords;
     
     

 @index := campaignPriority;
OPEN campaignscursor;
<<getStatus>> LOOP
FETCH campaignscursor INTO campaignIdcur,campaignPriorityCur ;
IF campaignscursor%NOTFOUND THEN
        FINISHED := 1;
END IF;
IF FINISHED = 1 then
	EXIT getStatus;
else
    if campaignPriorityCur = @index then
    	update campaigndefinition set campaigndefinition.campaignPriority = campaignPriorityCur+1 where campaigndefinition.campaignId = campaignIdcur;
	    if sql%rowcount = 0 then 
        FINISHED := 1;
	    end if;
    	v_index  := @index  + 1;
    else
     	FINISHED := 0;
    end if;
	
END IF;
END LOOP getStatus;
CLOSE campaignscursor;
     
END



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "GetAllProductGroups_Campaign_proc"()
BEGIN
	
	select distinct "pg"."productGroupId", "pg"."productGroupName" from "productInformation" "pinf" 
	jov_"productGroup" "pg" on ("pinf"."productGroupRef" = "pg"."productGroupRef" )
	where "pinf"."purposeData" like '%Campaigns%';
END


CREATE OR REPLACE NONEDITIONABLE PROCEDURE "getProductsByProductGroup_proc"(
v_productGroups VARCHAR2(50);
)
BEGIN
	select "pinf"."productId","pinf"."productName", "pg"."productGroupId", "pg"."productGroupName" from "productInformation" "pinf" 
	jov_"productGroup" "pg" on ("pinf"."productGroupRef" = "pg"."productGroupRef" )
	where "pg"."productGroupId" COLLATE utf8_general_ci = productGroups and "pinf"."purposeData" like '%Campaigns%';

END



create or replace NONEDITIONABLE PROCEDURE "approvalmatrix_create_proc" (
  "_matrixValues" IN NVARCHAR2, "_approverIds" IN NVARCHAR2
) AS v_index1 NUMBER(10, 0) := 0;
v_length NUMBER(19, 0);
v_index2 NUMBER(10, 0) := 0;
v_id NUMBER(10, 0);
v_customerIds NVARCHAR2(2000);
v_customerIdsComma NVARCHAR2(2000);
v_length2 NUMBER(10, 0);
v_customerId NVARCHAR2(100);
v_matrixRecord NVARCHAR2(2000);
v_matrixComma VARCHAR2(4000);
v_query NVARCHAR2(2000);
identity_value NUMBER(10);
BEGIN v_length := LENGTH("_matrixValues") - LENGTH(
  REPLACE("_matrixValues", ',', '')
) + 1;
<< loop_2 >> WHILE (1 = 1) LOOP BEGIN v_index1 := v_index1 + 1;
IF v_index1 = v_length + 1 THEN EXIT;
ELSE BEGIN v_matrixRecord := SUBSTRING_INDEX(
  SUBSTRING_INDEX("_matrixValues", ',', v_index1), 
  ',', 
  -1
);
v_matrixComma := REPLACE(v_matrixRecord, ';', ',');
v_matrixComma := REPLACE(v_matrixComma, '"', '''');
v_query := (
  '
                                 INSERT into "approvalmatrix"(
                                    "approvalmatrix"."name",
                                    "approvalmatrix"."contractId",
                        "approvalmatrix"."coreCustomerId",
                                    "approvalmatrix"."actionId",
                                    "approvalmatrix"."accountId",
                                    "approvalmatrix"."approvalruleId",
                                    "approvalmatrix"."limitTypeId",
                                    "approvalmatrix"."lowerlimit",
                                    "approvalmatrix"."upperlimit",
                                    "approvalmatrix"."currency"
                                 ) VALUES ('
) || (v_matrixComma) || (')');
EXECUTE IMMEDIATE v_query;
select 
  "id" INTO identity_value 
from 
  "approvalmatrix" 
where 
  rowid =(
    select 
      max(rowid) 
    from 
      "approvalmatrix"
  );
v_id := identity_value;
v_customerIds := SUBSTRING_INDEX("_approverIds", ',', v_index1);
if(v_customerIds is not null) then
begin
v_customerIds := SUBSTRING_INDEX(
  v_customerIds,
  ',', 
  -1
);
v_customerIdsComma := REPLACE(v_customerIds, ';', ',');
v_length2 := LENGTH(v_customerIdsComma) - LENGTH(
  REPLACE(v_customerIdsComma, ',', '')
) + 1;
v_index2 := 0;
<< loop_1 >> WHILE (1 = 1) LOOP BEGIN v_index2 := v_index2 + 1;
IF v_index2 = v_length2 + 1 THEN EXIT;
ELSE BEGIN v_customerId := SUBSTRING_INDEX(
  SUBSTRING_INDEX(
    v_customerIdsComma, ',', v_index2
  ), 
  ',', 
  -1
);
INSERT INTO "customerapprovalmatrix" (
  "customerapprovalmatrix"."customerId", 
  "customerapprovalmatrix"."approvalMatrixId"
) 
VALUES 
  (v_customerId, v_id);
GOTO loop_1;
END;
END IF;
END;
END LOOP;
GOTO loop_2;
END;
END IF;
END;
END IF;
END LOOP;

END LOOP;
END;
/


create or replace NONEDITIONABLE PROCEDURE "approvalmatrix_default_create_proc" (
  "_actionIds" IN VARCHAR2, "_contractId" IN VARCHAR2, 
  "_accountIds" IN VARCHAR2, "_cif" IN VARCHAR2, 
  records OUT SYS_REFCURSOR
) AS v_accountList CLOB := '0';
v_limitTypeId_1 VARCHAR2(255) := 'DAILY_LIMIT';
v_limitTypeId_2 VARCHAR2(255) := 'MAX_TRANSACTION_LIMIT';
v_limitTypeId_3 VARCHAR2(255) := 'WEEKLY_LIMIT';
v_accountIndex NUMBER(10, 0) := 0;
v_actionIndex NUMBER(10, 0) := 0;
v_typeId CLOB := '';
v_numOfAccounts NUMBER(10, 0);
v_numOfActions NUMBER(10, 0);
v_accountId VARCHAR2(50);
v_actionId VARCHAR2(255);
BEGIN IF "_actionIds" IS NULL 
OR "_actionIds" = '' THEN RETURN;
END IF;
IF "_contractId" IS NULL 
OR "_contractId" = '' THEN RETURN;
END IF;
IF "_cif" IS NULL 
OR "_cif" = '' THEN RETURN;
END IF;
IF "_accountIds" IS NULL 
OR "_accountIds" = '' THEN RETURN;
END IF;
v_numOfAccounts := LENGTH("_accountIds") - LENGTH(
  REPLACE("_accountIds", ',', ' ')
) || 1;
v_numOfActions := LENGTH("_actionIds") - LENGTH(
  REPLACE("_actionIds", ',', ' ')
) || 1;
<< getAccount >> WHILE 1 = 1 LOOP BEGIN v_accountIndex := v_accountIndex + 1;
IF v_accountIndex = v_numOfAccounts + 1 THEN BEGIN EXIT;
END;
ELSE BEGIN v_accountId := UTILS.SUBSTRING_INDEX(
  UTILS.SUBSTRING_INDEX(
    "_accountIds", ',', v_accountIndex
  ), 
  ',', 
  -1
);
v_actionIndex := 0;
<< getAction >> WHILE 1 = 1 LOOP BEGIN v_actionIndex := v_actionIndex + 1;
IF v_actionIndex = v_numOfActions + 1 THEN BEGIN EXIT;
END;
ELSE BEGIN v_actionId := UTILS.SUBSTRING_INDEX(
  UTILS.SUBSTRING_INDEX("_actionIds", ',', v_actionIndex), 
  ',', 
  -1
);
SELECT 
  "Type_id" INTO v_typeId 
FROM 
  "featureaction" 
WHERE 
  "id" = v_actionId;
IF v_typeId = 'MONETARY' THEN BEGIN INSERT INTO "approvalmatrix" (
  "contractId", "name", "accountId", 
  "actionId", "limitTypeId", "coreCustomerId", 
  "approvalruleId"
) 
VALUES 
  (
    "_contractId", 
    (
      v_actionId || '_' || v_accountId || '_' || v_limitTypeId_1 || '_' || "_contractId"
    ), 
    v_accountId, 
    v_actionId, 
    v_limitTypeId_1, 
    "_cif", 
    'NO_APPROVAL'
  );
INSERT INTO "approvalmatrix" (
  "contractId", "name", "accountId", 
  "actionId", "limitTypeId", "coreCustomerId", 
  "approvalruleId"
) 
VALUES 
  (
    "_contractId", 
    (
      v_actionId || '_' || v_accountId || '_' || v_limitTypeId_2 || '_' || "_contractId"
    ), 
    v_accountId, 
    v_actionId, 
    v_limitTypeId_2, 
    "_cif", 
    'NO_APPROVAL'
  );
INSERT INTO "approvalmatrix" (
  "contractId", "name", "accountId", 
  "actionId", "limitTypeId", "coreCustomerId", 
  "approvalruleId"
) 
VALUES 
  (
    "_contractId", 
    (
      v_actionId || '_' || v_accountId || '_' || v_limitTypeId_3 || '_' || "_contractId"
    ), 
    v_accountId, 
    v_actionId, 
    v_limitTypeId_3, 
    "_cif", 
    'NO_APPROVAL'
  );
END;
ELSE IF v_typeId = 'NON_MONETARY' THEN BEGIN INSERT INTO "approvalmatrix" (
  "contractId", "name", "accountId", 
  "actionId", "limitTypeId", "coreCustomerId", 
  "approvalruleId"
) 
VALUES 
  (
    "_contractId", 
    (
      v_actionId || '_' || v_accountId || '_' || 'NON_MONETARY_LIMIT' || '_' || "_contractId"
    ), 
    v_accountId, 
    v_actionId, 
    'NON_MONETARY_LIMIT', 
    "_cif", 
    'NO_APPROVAL'
  );
END;
END IF;
END IF;
END;
END IF;
END;
END LOOP;
v_accountList := (
  v_accountId || ',' || v_accountList
);
END;
END IF;
END;
END LOOP;
SELECT 
  SUBSTR(
    v_accountList, 
    1, 
    (
      LENGTH(v_accountList) -1
    )
  ) INTO v_accountList 
FROM 
  DUAL;
OPEN records FOR 
SELECT 
  v_accountList accountList 
FROM 
  DUAL;
--DBMS_SQL.RETURN_RESULT(v_cursor);
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;

/

--  DDL for Procedure approvalmatrix_default_delete_proc
create or replace NONEDITIONABLE PROCEDURE "approvalmatrix_default_delete_proc" (
  "_contractId" IN VARCHAR2, "_cif" IN VARCHAR2, 
  "_filterColumnIds" IN VARCHAR2, "_filterColumnName" IN VARCHAR2
) AS BEGIN --   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
IF "_filterColumnName" = 'actionId' THEN BEGIN DELETE "approvalmatrix" 
WHERE 
  "approvalmatrix"."contractId" = "_contractId" 
  AND "approvalmatrix"."coreCustomerId" = "_cif" 
  AND "approvalmatrix"."actionId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT("_filterColumnIds")
      )
  );
END;
ELSE IF "_filterColumnName" = 'accountId' THEN BEGIN DELETE "approvalmatrix" 
WHERE 
  "approvalmatrix"."contractId" = "_contractId" 
  AND "approvalmatrix"."coreCustomerId" = "_cif" 
  AND "approvalmatrix"."accountId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT("_filterColumnIds")
      )
  );
END;
ELSE IF "_filterColumnName" = 'cif' THEN BEGIN DELETE "approvalmatrix" 
WHERE 
  "approvalmatrix"."contractId" = "_contractId" 
  AND "approvalmatrix"."coreCustomerId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT("_filterColumnIds")
      )
  );
END;
END IF;
END IF;
END IF;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;

/


--  DDL for Procedure approvalmatrix_signatorygroupmatrixcreate_proc


create or replace NONEDITIONABLE PROCEDURE "approvalmatrix_signatorygroupmatrixcreate_proc"
(
  "_matrixValues" IN NVARCHAR2,
  "_signatorymatrixValues" IN NVARCHAR2
)
AS
   v_index1 NUMBER(10,0) := 0;
   v_index2 NUMBER(10,0) := 0;
   v_length NUMBER(10,0) := 0;
   v_matrixRecord NVARCHAR2(2000);
   v_matrixComma VARCHAR2(4000);
   v_query NVARCHAR2(2000);
   v_sigValues NVARCHAR2(2000);
   v_id NVARCHAR2(2000);
   v_groupList NVARCHAR2(2000);
   v_groupRule NVARCHAR2(2000);
   identity_value NUMBER(10);

BEGIN

   v_length := LENGTH("_matrixValues") - LENGTH(REPLACE("_matrixValues", ',', '')) + 1 ;
   <<loop_1>>
   WHILE ( 1 = 1 ) 
   LOOP 
      
      BEGIN
         v_index1 := v_index1 + 1 ;
         IF v_index1 = v_length + 1 THEN
          EXIT;
         ELSE
         
         BEGIN
            v_matrixRecord := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX("_matrixValues", ',', v_index1), ',', -1) ;
            v_matrixComma := REPLACE(v_matrixRecord, ';', ',') ;
            v_matrixComma := REPLACE(v_matrixComma, '"', '''') ;
            v_query := ('INSERT INTO "approvalmatrix"("name","contractId","coreCustomerId","actionId","accountId","approvalruleId","isGroupMatrix","limitTypeId","lowerlimit","upperlimit","currency") VALUES ('|| v_matrixComma|| ')') ;
            EXECUTE IMMEDIATE v_query;
            select "id"  INTO identity_value from  "approvalmatrix" order by "id" desc fetch first row only;
      v_sigValues := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX("_signatorymatrixValues", '#', v_index1), '#', -1) ;
      v_id := identity_value ;
            v_groupList := UTILS.SUBSTRING_INDEX(v_sigValues, ';', 1) ;
            v_groupRule := UTILS.SUBSTRING_INDEX(v_sigValues, ';', -1) ;
            v_id := REPLACE(v_id, '"', '''') ;
            v_groupList := REPLACE(v_groupList, '"', '''') ;
            v_groupRule := REPLACE(v_groupRule, '"', '''') ;
            v_query := ('INSERT INTO "signatorygroupmatrix"("approvalMatrixId","groupList","groupRule") VALUES (') || v_id || ',' || '''' || v_groupList || '''' || ',' || '''' || v_groupRule || '''' || ')' ;
            EXECUTE IMMEDIATE v_query;
            GOTO loop_1;
         
         END;
         END IF;
      
      END;
   END LOOP;


END;
/

--  DDL for Procedure approvalmatrix_update_softdeleteflag_proc


create or replace NONEDITIONABLE PROCEDURE "approvalmatrix_update_softdeleteflag_proc" (
  "_contractId" IN VARCHAR2, "_cif" IN VARCHAR2, 
  "_accountIds" IN VARCHAR2, "_actionId" IN VARCHAR2, 
  "_limitTypeId" IN VARCHAR2
) AS BEGIN --   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
UPDATE 
  "approvalmatrix" 
SET 
  "softdeleteflag" = 1 
WHERE 
  "approvalmatrix"."contractId" = "_contractId" 
  AND "approvalmatrix"."coreCustomerId" = "_cif" 
  AND CAST(
    "approvalmatrix"."accountId" AS VARCHAR2(255)
  ) IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT("_accountIds")
      )
  ) 
  AND "approvalmatrix"."actionId" = "_actionId" 
  AND "approvalmatrix"."limitTypeId" = "_limitTypeId";
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;

/

--  DDL for Procedure approvalmatrixtemplate_create_proc


create or replace NONEDITIONABLE PROCEDURE "approvalmatrixtemplate_create_proc"(
  "_matrixValues" IN NVARCHAR2, 
  "_matrixApprover" IN NVARCHAR2,
  "_isGroupMatrix" IN NUMBER
) AS
    v_index1 NUMBER(10) := 0;
    v_index2 NUMBER(10) := 0;
    v_length NUMBER(19) := 0;
    v_length2 NUMBER(10) := 0;
    v_matrixRecord NVARCHAR2(2000);
    v_matrixComma NVARCHAR2(2000);
    v_query NVARCHAR2(2000);
    v_id NVARCHAR2(50);
    v_customerIds NVARCHAR2(255);
    v_customerIdsComma NVARCHAR2(255);
    v_customerId NVARCHAR2(50);
    v_sigValues NVARCHAR2(255);
    v_groupList NVARCHAR2(255);
    v_groupRule NVARCHAR2(255);
    identity_value NUMBER(10);
BEGIN
  
  v_length := LENGTH("_matrixValues") - LENGTH(REPLACE("_matrixValues", ',', '')) + 1;
  <<loop_2>>
    WHILE (1 = 1)
    LOOP
        BEGIN
      v_index1 := v_index1 + 1;
      IF v_index1 = v_length + 1 THEN 
        EXIT;
      ELSE
                BEGIN 
                    v_matrixRecord := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX("_matrixValues", ',', v_index1), ',', -1 );
                    v_matrixComma := REPLACE(v_matrixRecord, ';', ',');
                    v_matrixComma := REPLACE(v_matrixComma, '"', '''');
                    v_query := 'INSERT INTO "approvalmatrixtemplate"("contractId","coreCustomerId","actionId","approvalruleId","limitTypeId","lowerlimit","upperlimit","currency","isGroupMatrix") VALUES (' ||v_matrixComma ||')';
                    EXECUTE IMMEDIATE v_query;
                    SELECT "approvalmatrixtemplate"."id"  INTO v_id from "approvalmatrixtemplate" ORDER BY "id" desc FETCH FIRST ROW ONLY;
                    IF "_isGroupMatrix" = 0 THEN
                        v_customerIds := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX("_matrixApprover", ',', v_index1), ',', -1 );
                        IF (v_customerIds IS NOT NULL AND LENGTH(v_customerIds) > 0) THEN
                            v_customerIdsComma := REPLACE(v_customerIds, ';', ',');
                                v_length2 := LENGTH(v_customerIdsComma) - LENGTH(REPLACE(v_customerIdsComma, ',', '')) + 1;
                                v_index2 := 0;
                                <<loop_1>>
                                WHILE(1 = 1)
                                LOOP
                                    BEGIN
                                    v_index2 := v_index2 + 1;
                                    IF v_index2 = v_length2 + 1 THEN 
                                        EXIT;
                                    ELSE
                                        BEGIN
                                        v_customerId := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_customerIdsComma, ',', v_index2), ',', -1 );
                                        INSERT INTO "customerapprovalmatrixtemplate"("customerId","approvalMatrixId") values (v_customerId,v_id);             
                                        GOTO loop_1;
                                        END;
                                    END IF;
                                    END;
                                END LOOP;
                            END IF; 
                        GOTO loop_2;
                        ELSE IF "_isGroupMatrix" = 1 THEN
                            v_sigValues := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX("_matrixApprover", '#', v_index1), '#', -1 );
                            v_groupList := UTILS.SUBSTRING_INDEX(v_sigValues, ';', 1 );
                            v_groupRule := UTILS.SUBSTRING_INDEX(v_sigValues, ';', -1 );
                            INSERT INTO "signatorygroupmatrixtemplate"("approvalMatrixId", "groupList", "groupRule") values (v_id, v_groupList, v_groupRule);
                        END IF;  
                    END IF;
                END;
                END IF;
            END;
  END LOOP; 
END;
/

--  DDL for Procedure approvalmatrixtemplate_default_create_proc


create or replace NONEDITIONABLE PROCEDURE "approvalmatrixtemplate_default_create_proc" (
  "_actionIds" IN NVARCHAR2, "_contractId" IN NVARCHAR2, 
  "_approvalmode" IN NVARCHAR2, "_cif" IN NVARCHAR2, 
  "_currency" IN NVARCHAR2
) AS v_accountList VARCHAR2(4000) := 0;
v_actionIds VARCHAR2(32000) := 0;
v_limitTypeId_1 VARCHAR2(255) := 'DAILY_LIMIT';
v_limitTypeId_2 VARCHAR2(255) := 'MAX_TRANSACTION_LIMIT';
v_limitTypeId_3 VARCHAR2(255) := 'WEEKLY_LIMIT';
v_actionIndex NUMBER(10, 0) := 0;
v_actionId VARCHAR2(2000);
v_typeId VARCHAR2(4000) := ' ';
v_isGroupMatrix NUMBER(10, 0) := 0;
v_cursor SYS_REFCURSOR;
v_numOfActions NUMBER(10,0);
v_legalEntityId varchar2(255) := 'ALL';
BEGIN 
if "_contractId" is not null 
and "_cif" is not null 
and "_actionIds" is not null THEN If "_approvalmode" = '0' THEN v_isGroupMatrix := 0;
else v_isGroupMatrix := 1;
end if;
SELECT 
  "companyLegalUnit" into v_legalEntityId 
from 
  "contractcorecustomers" 
where 
  "coreCustomerId" = "_cif" 
  and "contractId" = "_contractId";
v_numOfActions := CASE WHEN "_actionIds" IS NULL THEN 0 ELSE LENGTH("_actionIds") - LENGTH(REPLACE("_actionIds", ',', '')
) + 1 END;
v_actionIds := REPLACE("_actionIds",' ','');
DBMS_OUTPUT.PUT_LINE('v_actionIds'||v_actionIds);
WHILE (1 = 1) LOOP BEGIN v_actionIndex := v_actionIndex + 1;
IF (v_actionIndex = v_numOfActions + 1) THEN EXIT;
ELSE BEGIN v_actionId := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_actionIds, ',', v_actionIndex),',',-1);
SELECT 
  "Type_id" INTO v_typeId 
FROM 
  "featureaction" 
WHERE 
  "id" = v_actionId 
  and "companyLegalUnit" = v_legalEntityId;
IF v_typeId = 'MONETARY' THEN BEGIN INSERT INTO "approvalmatrixtemplate" (
  "contractId", "actionId", "limitTypeId", 
  "coreCustomerId", "approvalruleId", 
  "isGroupMatrix", "currency"
) 
VALUES 
  (
    "_contractId", v_actionId, V_limitTypeId_1, 
    "_cif", 'NO_APPROVAL', v_isGroupMatrix, 
    "_currency"
  );
INSERT INTO "approvalmatrixtemplate" (
  "contractId", "actionId", "limitTypeId", 
  "coreCustomerId", "approvalruleId", 
  "isGroupMatrix", "currency"
) 
VALUES 
  (
    "_contractId", v_actionId, V_limitTypeId_2, 
    "_cif", 'NO_APPROVAL', v_isGroupMatrix, 
    "_currency"
  );
INSERT INTO "approvalmatrixtemplate" (
  "contractId", "actionId", "limitTypeId", 
  "coreCustomerId", "approvalruleId", 
  "isGroupMatrix", "currency"
) 
VALUES 
  (
    "_contractId", v_actionId, V_limitTypeId_3, 
    "_cif", 'NO_APPROVAL', v_isGroupMatrix, 
    "_currency"
  );
END;
ELSE IF v_typeId = 'NON_MONETARY' THEN BEGIN INSERT INTO "approvalmatrixtemplate" (
  "contractId", "actionId", "limitTypeId", 
  "coreCustomerId", "approvalruleId", 
  "isGroupMatrix", "currency"
) 
VALUES 
  (
    "_contractId", v_actionId, 'NON_MONETARY_LIMIT', 
    "_cif", 'NO_APPROVAL', v_isGroupMatrix, 
    "_currency"
  );
END;
END IF;
END IF;
END;
END IF;
END;
END LOOP;
END IF;
END;

/

--  DDL for Procedure approvalrequest_counts_proc


create or replace NONEDITIONABLE PROCEDURE "approvalrequest_counts_proc" (
  "_customerId" IN NVARCHAR2, "_approveActionList" IN NVARCHAR2, 
  "_createActionList" IN NVARCHAR2, 
  "records" OUT SYS_REFCURSOR, "records1" out sys_refcursor
) AS iv_approveActionList NVARCHAR2(2000) := "_approveActionList";
iv_createActionList NVARCHAR2(2000) := "_createActionList";
BEGIN DECLARE v_approvalRequestIds NVARCHAR2(2000);
v_companyId NVARCHAR2(2000);
v_features NVARCHAR2(2000);
v_createApproveActions NVARCHAR2(2000);
v_customerMatrixIds NVARCHAR2(2000);
v_select_statement NVARCHAR2(4000);
v_alreadyApprovedIds NVARCHAR2(2000);
BEGIN OPEN "records" FOR 
SELECT 
  0 "count", 
  'ACHFilesForMyApproval' "TransactionType" 
FROM 
  DUAL 
UNION 
SELECT 
  0 "count", 
  'ACHTransactionsForMyApproval' "TransactionType" 
FROM 
  DUAL 
UNION 
SELECT 
  0 "count", 
  'GeneralTransactionsForMyApproval' "TransactionType" 
FROM 
  DUAL 
UNION 
SELECT 
  0 "count", 
  'GeneralTransactionsForMyApproval' "TransactionType" 
FROM 
  DUAL 
UNION 
SELECT 
  0 "count", 
  'myRequestsWaiting' "TransactionType" 
FROM 
  DUAL 
UNION 
SELECT 
  0 "count", 
  'myRequestsRejected' "TransactionType" 
FROM 
  DUAL 
UNION 
SELECT 
  0 "count", 
  'myRequestsApproved' "TransactionType" 
FROM 
  DUAL;
SELECT 
  "customer"."Organization_Id" INTO v_companyId 
FROM 
  "customer" 
WHERE 
  "customer"."id" = "_customerId";
IF v_companyId IS NULL THEN v_companyId := ' ';
END IF;
IF iv_approveActionList IS NULL THEN iv_approveActionList := ' ';
END IF;
IF iv_createActionList IS NULL THEN iv_createActionList := ' ';
END IF;
SELECT 
  LISTAGG(
    CAST(
      "featureaction"."Feature_id" AS NVARCHAR2(2000)
    ), 
    ','
  ) INTO v_features 
FROM 
  "featureaction" 
WHERE 
  "featureaction"."id" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(iv_approveActionList)
      )
  );
IF v_features IS NULL THEN v_features := ' ';
END IF;
SELECT 
  LISTAGG(
    CAST(
      "featureaction"."id" AS NVARCHAR2(2000)
    ), 
    ','
  ) INTO v_createApproveActions 
FROM 
  "featureaction" 
WHERE 
  "featureaction"."Feature_id" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(v_features)
      )
  ) 
  AND (
    "featureaction"."id" LIKE '%_CREATE' 
    OR "featureaction"."id" LIKE '%_UPLOAD'
  );
IF v_createApproveActions IS NULL THEN v_createApproveActions := ' ';
END IF;
SELECT 
  LISTAGG(
    CAST(
      "customerapprovalmatrix"."approvalMatrixId" AS NVARCHAR2(2000)
    ), 
    ','
  ) INTO v_customerMatrixIds 
FROM 
  "customerapprovalmatrix" 
WHERE 
  "customerapprovalmatrix"."customerId" = "_customerId";
IF v_customerMatrixIds IS NULL THEN v_customerMatrixIds := ' ';
END IF;
SELECT 
  LISTAGG(
    CAST(
      "bbactedrequest"."requestId" AS NVARCHAR2(2000)
    ), 
    ','
  ) INTO v_alreadyApprovedIds 
FROM 
  "bbactedrequest" 
WHERE 
  "bbactedrequest"."createdby" = "_customerId" 
  AND "bbactedrequest"."action" = 'Approved';
IF v_alreadyApprovedIds IS NULL THEN v_alreadyApprovedIds := ' ';
END IF;
SELECT 
  LISTAGG(
    CAST(
      "requestapprovalmatrix"."requestId" AS NVARCHAR2(2000)
    ), 
    ','
  ) INTO v_approvalRequestIds 
FROM 
  "requestapprovalmatrix" 
WHERE 
  (
    CAST(
      "requestapprovalmatrix"."approvalMatrixId" AS NVARCHAR2(2000)
    )
  ) IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(v_customerMatrixIds)
      )
  ) 
  AND FIND_IN_SET(
    CAST(
      "requestapprovalmatrix"."requestId" AS NVARCHAR2(2000)
    ), 
    v_alreadyApprovedIds
  ) = 0;
IF v_approvalRequestIds IS NULL THEN v_approvalRequestIds := ' ';
END IF;
v_select_statement := (
  'select count(tablea.requestCountA) as "count", tablea."TransactionType" 
               from (
                 select DISTINCT("bbrequest"."requestId") as requestCountA, 
                       case when "bbrequest"."featureActionId" LIKE ''ACH_FILE%''
                        then  ''ACHFilesForMyApproval'' else(
                           case when "bbrequest"."featureActionId" LIKE ''ACH%''
                              then ''ACHTransactionsForMyApproval'' else 
                              ''GeneralTransactionsForMyApproval'' end
                              ) end
                       as "TransactionType",
                       "bbrequest"."createdby",
                       "bbrequest"."companyId",
                       "bbrequest"."status"
                       FROM (
                           "bbrequest" LEFT JOIN "requestapprovalmatrix" ON 
                           "bbrequest"."requestId" = "requestapprovalmatrix"."requestId"
                       )
                       WHERE FIND_IN_SET(CAST("bbrequest"."requestId" as nvarchar2(2000)),''' || (v_approvalRequestIds) || ''')>0 AND "bbrequest"."companyId" = ''' || (v_companyId) || ''' AND FIND_IN_SET("bbrequest"."featureActionId",''' || (v_createApproveActions) || ''')>0' || ' AND "bbrequest"."status" = ''Pending'') tablea GROUP BY "TransactionType"'
);
v_select_statement := (v_select_statement) || ' UNION select count(tableb.requestCountB) as "count", 
                tableb."TransactionType" from (
                    select  
                        DISTINCT("bbrequest"."requestId") as requestCountB,
                        case when "bbrequest"."status" = ''Pending'' then ''myRequestsWaiting'' else
                            (case when "bbrequest"."status" = ''Rejected'' then ''myRequestsRejected'' else 
                                (case when "bbrequest"."status" = ''Approved'' then ''myRequestsApproved'' else ''myRequestsWithdrawn'' end)end)end as "TransactionType",
                        "bbrequest"."createdby",
                        "bbrequest"."companyId",
                        "bbrequest"."status"
                       FROM "bbrequest" WHERE "bbrequest"."companyId" = ''' || (v_companyId) || '''' || ' AND FIND_IN_SET("bbrequest"."featureActionId",''' || (iv_createActionList) || ''')>0 AND "bbrequest"."createdby" = ' || '''' || "_customerId" || '''' || ') tableb group BY "TransactionType"';
WHILE v_select_statement IS NULL LOOP EXIT;
END LOOP;
open "records1" for v_select_statement;
END;
--<<MAINLABEL$leave>>
END;

/

--  DDL for Procedure signatorygroup_create_proc


create or replace NONEDITIONABLE PROCEDURE "signatorygroup_create_proc" 
(
  "signatoryGroupValues" IN VARCHAR2,
  "customerSignatoryGroupValues" IN VARCHAR2
)
AS
   v_index1 NUMBER(10,0) := 0;
   v_length1 NUMBER(10,0) := 0;
   v_matrixRecord VARCHAR2(4000);
   v_matrixComma VARCHAR2(4000);
   v_matrixComma1 VARCHAR2(4000);
   v_query VARCHAR2(4000);
   v_signatory VARCHAR2(4000);
   v_signatoriesComma VARCHAR2(4000);
   v_signatoriesComma1 VARCHAR2(4000);

BEGIN

   v_matrixRecord := UTILS.SUBSTRING_INDEX("signatoryGroupValues", ',', 1) ;
   v_matrixComma1 := REPLACE(v_matrixRecord, ';', ',') ;
   v_matrixComma := REPLACE(v_matrixComma1, '"', '''') ;
   v_query := 'INSERT INTO "signatorygroup"("signatoryGroupId","signatoryGroupName","signatoryGroupDescription","coreCustomerId","contractId","createdby") values (' || v_matrixComma || ')' ;
   EXECUTE IMMEDIATE v_query;
   v_length1 := LENGTH("customerSignatoryGroupValues") - LENGTH(REPLACE("customerSignatoryGroupValues", ',', '')) ;
   WHILE v_index1 != v_length1 
   LOOP 

      BEGIN
         v_index1 := v_index1 + 1 ;
         v_signatory := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX("customerSignatoryGroupValues", ',', v_index1), ',', -1) ;
         v_signatoriesComma1 := REPLACE(v_signatory, ';', ',') ;
         v_signatoriesComma := REPLACE(v_signatoriesComma1, '"', '''') ;
         v_query := 'INSERT INTO "customersignatorygroup"("customerSignatoryGroupId", "signatoryGroupId", "customerId", "createdby") values (' || v_signatoriesComma || ')' ;
         EXECUTE IMMEDIATE v_query;

      END;
   END LOOP;

END;
/

--  DDL for Procedure signatorygroup_update_proc


create or replace NONEDITIONABLE PROCEDURE "signatorygroup_update_proc" 
(
  "_sigGroupValues" IN VARCHAR2,
  "_newSigValues" IN VARCHAR2,
  "_deleteSigValues" IN VARCHAR2
)
AS
   v_index1 NUMBER(10,0) := 0;
   v_length1 NUMBER(19,0);
   v_signatoriesComma VARCHAR2(4000);
   v_custId VARCHAR2(100);
   v_sigGroupId VARCHAR2(4000);
   v_SigGroupName VARCHAR2(4000);
   v_sigGroupDes VARCHAR2(4000);
   v_signatory VARCHAR2(4000);
   v_query VARCHAR2(4000);
   v_sigCreatedBy VARCHAR2(4000);

BEGIN

   v_sigGroupId := UTILS.SUBSTRING_INDEX("_sigGroupValues", ';', 1) ;
   v_SigGroupName := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX("_sigGroupValues", ';', 2), ';', -1) ;
   v_SigGroupDes := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX("_sigGroupValues", ';', 3), ';', -1) ;
   v_sigCreatedBy := UTILS.SUBSTRING_INDEX("_sigGroupValues", ';', -1) ;
   v_SigGroupId := REPLACE(v_SigGroupId, '"', '''') ;
   IF v_SigGroupName IS NOT NULL
     AND v_SigGroupName != ' ' THEN

   BEGIN
      v_SigGroupName := REPLACE(v_SigGroupName, '"', '''') ;
      v_sigCreatedBy := REPLACE(v_sigCreatedBy, '"', '''') ;
      v_query := ('UPDATE "signatorygroup" SET "signatoryGroupName" = ') || v_SigGroupName || (' ,"lastmodifiedts" = CURRENT_TIMESTAMP, "modifiedby" = ') || v_sigCreatedBy || (' WHERE "signatoryGroupId" = ') || v_sigGroupId || (' ') ;
      EXECUTE IMMEDIATE v_query;

   END;
   END IF;
   IF v_sigGroupDes IS NOT NULL
     AND v_sigGroupDes != ' ' THEN

   BEGIN
      v_sigGroupDes := REPLACE(v_sigGroupDes, '"', '''') ;
      v_sigCreatedBy := REPLACE(v_sigCreatedBy, '"', '''') ;
      v_query := ('UPDATE "signatorygroup" SET "signatoryGroupDescription" = ') || v_sigGroupDes || (' , "lastmodifiedts" = CURRENT_TIMESTAMP, "modifiedby" = ') || v_sigCreatedBy || (' WHERE "signatoryGroupId" = ') || v_sigGroupId || (' ') ;
      EXECUTE IMMEDIATE v_query;

   END;
   END IF;
   IF "_newSigValues" IS NOT NULL
     AND "_newSigValues" != ' ' THEN

   BEGIN
      v_length1 := LENGTH("_newSigValues") - LENGTH(REPLACE("_newSigValues", ',', '')) + 1 ;
      v_index1 := 0 ;
      <<loop_1>>
      WHILE ( 1 = 1 ) 
      LOOP 

         BEGIN
            v_index1 := v_index1 + 1 ;
            IF v_index1 = v_length1 + 1 THEN
             EXIT;
            ELSE

            BEGIN
               v_signatory := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX("_newSigValues", ',', v_index1), ',', -1) ;
               v_signatoriesComma := REPLACE(v_signatory, ';', ',') ;
               v_signatoriesComma := REPLACE(v_signatoriesComma, '"', '''') ;
               v_query := ('INSERT INTO "customersignatorygroup"("customerSignatoryGroupId", "signatoryGroupId", "customerId", "createdby") values (') || v_signatoriesComma || (')') ;
               EXECUTE IMMEDIATE v_query;
               GOTO loop_1;

            END;
            END IF;

         END;
      END LOOP;

   END;
   END IF;
   IF "_deleteSigValues" IS NOT NULL
     AND "_deleteSigValues" != ' ' THEN

   BEGIN
      v_length1 := LENGTH("_deleteSigValues") - LENGTH(REPLACE("_deleteSigValues", ',', ''))+ 1 ;
      v_index1 := 0 ;
      <<loop_1>>
      WHILE ( 1 = 1 ) 
      LOOP 

         BEGIN
            v_index1 := v_index1 + 1 ;
            IF v_index1 = v_length1 + 1 THEN
             EXIT;
            ELSE

            BEGIN
               v_signatory := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX("_deleteSigValues", ',', v_index1), ',', -1) ;
               v_sigGroupId := UTILS.SUBSTRING_INDEX(v_signatory, ';', 1) ;
               v_custId := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_signatory, ';', 2), ';', -1) ;
               v_sigGroupId := REPLACE(v_sigGroupId, '"', '''') ;
               v_custId := REPLACE(v_custId, '"', '''') ;
               v_query := ('DELETE FROM "customersignatorygroup" WHERE "customersignatorygroup"."signatoryGroupId" =') || v_sigGroupId || ('AND "customersignatorygroup"."customerId"=') || v_custId || (' ') ;
               EXECUTE IMMEDIATE v_query;
               GOTO loop_1;

            END;
            END IF;

         END;
      END LOOP;

   END;
   END IF;

END;
/

create or replace NONEDITIONABLE PROCEDURE "update_bbactedrequest_proc" ("_requestId" IN VARCHAR2) AS BEGIN 
UPDATE 
  "bbactedrequest" 
SET 
  "softdeleteflag" = '1' 
WHERE 
  ("requestId" = "_requestId");
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/

CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "approval_matrix_manual_cleanup_proc" (
  v__contractId IN VARCHAR2, v__coreCustomerId IN VARCHAR2, 
  "contractaccounts" OUT SYS_REFCURSOR
) AS v_customerAccounts CLOB;
v_customerActions CLOB;
BEGIN 
UPDATE 
  "approvalmatrix" 
SET 
  "softdeleteflag" = '1' 
WHERE 
  "approvalmatrix"."coreCustomerId" = v__coreCustomerId;
SELECT 
  DISTINCT LISTAGG("contractaccounts"."accountId") INTO v_customerAccounts 
FROM 
  "contractaccounts" 
WHERE 
  (
    "contractId" = v__contractId 
    AND "coreCustomerId" = v__coreCustomerId
  );
SELECT 
  DISTINCT LISTAGG("actionId") INTO v_customerActions 
FROM 
  "contractactionlimit" 
  LEFT JOIN "featureaction" ON (
    "featureaction"."id" = "contractactionlimit"."actionId"
  ) 
WHERE 
  (
    "contractId" = v__contractId 
    AND "coreCustomerId" = v__coreCustomerId
  ) 
  AND "featureaction"."approveFeatureAction" IS NOT NULL;
--APPROVALMATRIX_DEFAULT_CREATE_PROC(v_customerActions,
--                                        v__contractId,
--                                      v_customerAccounts,
--                                    v__coreCustomerId,
--                                  v_cursor) ;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/

  create or replace NONEDITIONABLE PROCEDURE "approvalmatrix_fetch_grouprecords_proc" 
(
  "_contractId" IN VARCHAR2,
  "_cif" IN VARCHAR2,
  "_accountId" IN VARCHAR2,
  "_limitTypeId" IN VARCHAR2,
  "_actions" IN VARCHAR2,
  "records" OUT SYS_REFCURSOR
)
AS
   v__cif VARCHAR2(50) := "_cif";
   v__accountId VARCHAR2(50) := "_accountId";
   v__limitTypeId VARCHAR2(50) := "_limitTypeId";


BEGIN

--   /*TODO:SQLDEV*/ SET XACT_ABORT ON /*END:SQLDEV*/
   IF v__cif = '' THEN
    v__cif := '%' ;
   END IF;
   IF v__accountId = '' THEN
    v__accountId := '%' ;
   END IF;
   IF v__limitTypeId = '' or v__limitTypeId is null  THEN
    v__limitTypeId := '%' ;
   END IF;
   dbms_output.put_line('v__cif'||v__cif);
   dbms_output.put_line('v__accountId'||v__accountId);
   dbms_output.put_line('v__limitTypeId'||v__limitTypeId);
   dbms_output.put_line('_contractId'||"_contractId");
   dbms_output.put_line('_actions'||"_actions");
   OPEN  "records" FOR
      SELECT approvalmatrix."id" ,
             approvalmatrix."contractId" ,
             approvalmatrix."accountId" ,
             approvalmatrix."limitTypeId" ,
             featureaction."id" "actionId"  ,
             featureaction."name" "actionName"  ,
             featureaction."description" "actionDescription"  ,
             featureaction."Feature_id" "featureId"  ,
             featureaction."Type_id" "actionType"  ,
             featureaction."isAccountLevel" "isAccountLevel"  ,
             feature."name" "featureName"  ,
             feature."Status_id" "fifeaturestatus"  ,
             approvalrule."id" "approvalruleId"  ,
             approvalrule."numberOfApprovals" ,
             approvalrule."name" "approvalRuleName"  ,
             approvalmatrix."lowerlimit" ,
             approvalmatrix."upperlimit" ,
--             approvalmatrix."currency",
             signatorygroupmatrix."groupList" "groupList"  ,
             signatorygroupmatrix."groupRule" "groupRule"  ,
             contractcorecustomers."coreCustomerId" "cifId"  ,
             contractcorecustomers."coreCustomerName" "cifName"  ,
             approvalmatrix."invalid" ,
             approvalmatrix."isGroupMatrix"  
        FROM ( ( ( ( ( ( "approvalmatrix" approvalmatrix
                         LEFT JOIN "signatorygroupmatrix" signatoryGroupMatrix   ON approvalmatrix."id" = signatorygroupmatrix."approvalMatrixId"
                          ) 
                       LEFT JOIN "featureaction" featureaction   ON approvalmatrix."actionId" = featureaction."id"
                        ) 
                     LEFT JOIN "approvalrule" approvalRule   ON approvalmatrix."approvalruleId" = approvalrule."id"
                      ) 
                   LEFT JOIN "feature" feature   ON featureaction."Feature_id" = feature."id"
                    ) 
                 LEFT JOIN "contractfeatures" contractfeatures   ON feature."id" = contractfeatures."featureId"
                 AND approvalmatrix."contractId" = contractfeatures."contractId"
                 AND approvalmatrix."coreCustomerId" = contractfeatures."coreCustomerId"
                  ) 
               LEFT JOIN "contractcorecustomers" contractcorecustomers   ON approvalmatrix."contractId" = contractcorecustomers."contractId"
               AND approvalmatrix."coreCustomerId" = contractcorecustomers."coreCustomerId"
                ) 
       WHERE  approvalmatrix."contractId" = "_contractId"
                AND approvalmatrix."coreCustomerId" LIKE v__cif
                AND approvalmatrix."isGroupMatrix" = 1
                AND approvalmatrix."accountId" LIKE v__accountId
                AND approvalmatrix."actionId" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT("_actions")))
                AND approvalmatrix."limitTypeId" LIKE v__limitTypeId
                AND approvalmatrix."softdeleteflag" = 0
                AND featureaction."approveFeatureAction" IS NOT NULL
                AND featureaction."status" = 'SID_ACTION_ACTIVE'
        ORDER BY approvalmatrix."contractId",
                 approvalmatrix."accountId",
                 approvalmatrix."limitTypeId",
                 approvalmatrix."actionId",
                 approvalmatrix."lowerlimit" ;
      --DBMS_SQL.RETURN_RESULT(v_cursor);

END;
/

create or replace NONEDITIONABLE PROCEDURE "approvalmatrix_fetch_records_proc"
(
  "_contractId" IN VARCHAR2,
  "_cif" IN VARCHAR2,
  "_accountId" IN VARCHAR2,
  "_limitTypeId" IN VARCHAR2,
  "_actions" IN NVARCHAR2,
  "records" OUT SYS_REFCURSOR
)
AS
   iv_cif VARCHAR2(50) := "_cif";
   iv_accountId VARCHAR2(50) := "_accountId";
   iv_limitTypeId VARCHAR2(50) := "_limitTypeId" ;


BEGIN
   IF iv_cif is null THEN
    iv_cif := '%' ;
   END IF;
   IF iv_accountId is null THEN
    iv_accountId := '%' ;
   END IF;
   IF iv_limitTypeId is null THEN
    iv_limitTypeId := '%' ;
   END IF;
   OPEN  "records" FOR
      SELECT "approvalmatrix"."id" ,
             "approvalmatrix"."contractId" ,
             "approvalmatrix"."accountId" ,
             "approvalmatrix"."limitTypeId" ,
             "featureAction"."id" "actionId"  ,
             "featureAction"."name" "actionName"  ,
             "featureAction"."description" "actionDescription"  ,
             "featureAction"."Feature_id" "featureId"  ,
             "featureAction"."Type_id" "actionType"  ,
             "featureAction"."isAccountLevel" "isAccountLevel",
             "feature"."name" "featureName"  ,
             "feature"."Status_id" "fifeaturestatus"  ,
             "approvalRule"."id" "approvalruleId"  ,
             "approvalRule"."numberOfApprovals" ,
             "approvalRule"."name" "approvalRuleName"  ,
             "approvalmatrix"."lowerlimit" ,
             "approvalmatrix"."upperlimit" ,
--             "approvalmatrix"."currency",
             "customer"."id" "customerId"  ,
             "customer"."FirstName" "firstName"  ,
             "customer"."LastName" "lastName"  ,
             "contractcorecustomers"."coreCustomerId" "cifId"  ,
             "contractcorecustomers"."coreCustomerName" "cifName"  ,
             "approvalmatrix"."invalid" ,
             "approvalmatrix"."isGroupMatrix" 
        FROM ( ( ( ( ( ( ( "approvalmatrix" "approvalmatrix"
                           LEFT JOIN "customerapprovalmatrix" "customerapprovalMatrix"   ON "approvalmatrix"."id" = "customerapprovalMatrix"."approvalMatrixId"
                            ) 
                         LEFT JOIN "customer" "customer"   ON "customerapprovalMatrix"."customerId" = "customer"."id"
                          ) 
                       LEFT JOIN "featureaction" "featureAction"   ON "approvalmatrix"."actionId" = "featureAction"."id"
                        ) 
                     LEFT JOIN "approvalrule" "approvalRule"   ON "approvalmatrix"."approvalruleId" = "approvalRule"."id"
                      ) 
                   LEFT JOIN "feature" "feature"   ON "featureAction"."Feature_id" = "feature"."id"
                    ) 
                 LEFT JOIN "contractfeatures" "contractfeatures"   ON "feature"."id" = "contractfeatures"."featureId"
                 AND "approvalmatrix"."contractId" = "contractfeatures"."contractId"
                 AND "approvalmatrix"."coreCustomerId" = "contractfeatures"."coreCustomerId"
                  ) 
               LEFT JOIN "contractcorecustomers" "contractcorecustomers"   ON "approvalmatrix"."contractId" = "contractcorecustomers"."contractId"
               AND "approvalmatrix"."coreCustomerId" = "contractcorecustomers"."coreCustomerId"
                ) 
       WHERE  
       "approvalmatrix"."contractId" = "_contractId"
                AND
                "approvalmatrix"."coreCustomerId" LIKE iv_cif
                AND "approvalmatrix"."accountId" LIKE iv_accountId
--                AND FIND_IN_SET("approvalmatrix"."actionId", "_actions") > 0
                AND "approvalmatrix"."actionId" IN (SELECT DISTINCT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT("_actions")))
                AND "approvalmatrix"."limitTypeId" LIKE iv_limitTypeId
                AND "approvalmatrix"."softdeleteflag" = 0
                AND "featureAction"."approveFeatureAction" IS NOT NULL
                AND "featureAction"."approveFeatureAction" IS NOT NULL
                AND "featureAction"."status" = 'SID_ACTION_ACTIVE'
        ORDER BY "approvalmatrix"."contractId",
                 "approvalmatrix"."accountId",
                 "approvalmatrix"."limitTypeId",
                 "approvalmatrix"."actionId",
                 "approvalmatrix"."lowerlimit" ;


END;
/

--  DDL for Procedure approvalmatrixtemplate_cleanup_proc


create or replace NONEDITIONABLE PROCEDURE "approvalmatrixtemplate_cleanup_proc" (
  "_actionIds" IN VARCHAR2, "_contractId" IN VARCHAR2, 
  "_cif" IN VARCHAR2, "_limitTypeId" IN VARCHAR2
) AS BEGIN << MAINLABEL >> BEGIN 
UPDATE 
  "approvalmatrix" 
SET 
  "approvalmatrix"."softdeleteflag" = 1 
WHERE 
  "approvalmatrix"."contractId" = "_contractId" 
  AND "approvalmatrix"."coreCustomerId" = "_cif" 
  AND "approvalmatrix"."actionId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT("_actionIds")
      )
  ) 
  AND "approvalmatrix"."limitTypeId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT("_limitTypeId")
      )
  ) 
  AND "approvalmatrix"."softdeleteflag" = 0;
UPDATE 
  "approvalmatrixtemplate" 
SET 
  "approvalmatrixtemplate"."softdeleteflag" = 1 
WHERE 
  "approvalmatrixtemplate"."contractId" = "_contractId" 
  AND "approvalmatrixtemplate"."coreCustomerId" = "_cif" 
  AND "approvalmatrixtemplate"."actionId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT("_actionIds")
      )
  ) 
  AND "approvalmatrixtemplate"."limitTypeId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT("_limitTypeId")
      )
  ) 
  AND "approvalmatrixtemplate"."softdeleteflag" = 0;
END;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;

/

--  DDL for Procedure update_requestapprovalmatrix_proc


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "update_requestapprovalmatrix_proc" (
  v__requestApprovalMatrixId IN VARCHAR2
) AS BEGIN 
UPDATE 
  "requestapprovalmatrix" 
SET 
  "receivedApprovals" = "receivedApprovals" + 1 
WHERE 
  "requestapprovalmatrix"."id" IN (
    SELECT 
      column_value 
    from 
      TABLE(
        UTILS.STRING_SPLIT(v__requestApprovalMatrixId)
      )
  );
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/

--  DDL for Procedure update_signatorygroup_for_user_proc


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "update_signatorygroup_for_user_proc" (
  v__coreCustomerId IN VARCHAR2, v__contractId IN VARCHAR2, 
  v__customerId IN VARCHAR2, v__signatorygroupId IN VARCHAR2
) AS v_index1 NUMBER(10, 0) := 0;
v_length NUMBER(10, 0) := 0;
v_cusrecord VARCHAR2(4000);
v_cus VARCHAR2(4000);
BEGIN v_length := LENGTH(v__customerId) - LENGTH(
  REPLACE(v__customerId, ',', ' ')
) || 1;
<< loop_1 >> WHILE (1 = 1) LOOP BEGIN v_index1 := v_index1 + 1;
IF v_index1 = v_length + 1 THEN EXIT;
ELSE BEGIN v_cusrecord := UTILS.SUBSTRING_INDEX(
  UTILS.SUBSTRING_INDEX(v__customerId, ',', v_index1), 
  ',', 
  -1
);
v_cus := REPLACE(v_cusrecord, ';', ',');
v_cus := REPLACE(v_cus, '"', '''');
DELETE "customersignatorygroup" 
WHERE 
  "customerId" = v_cus 
  AND "signatoryGroupId" IN (
    SELECT 
      "signatoryGroupId" 
    FROM 
      "signatorygroup" 
    WHERE 
      "coreCustomerId" = v__coreCustomerId 
      AND "contractId" = v__contractId
  );
IF v__signatorygroupId != ' ' THEN BEGIN INSERT INTO "customersignatorygroup" (
  "customerSignatoryGroupId", "signatoryGroupId", 
  "customerId", "createdby"
) 
VALUES 
  (
    SYS_GUID(), 
    v__signatorygroupId, 
    v_cus, 
    v_cus
  );
END;
END IF;
GOTO loop_1;
END;
END IF;
END;
END LOOP;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/

--  DDL for Procedure fetch_approval_history_proc


create or replace NONEDITIONABLE PROCEDURE "fetch_approval_history_proc" (
  "_userId" IN VARCHAR2, "_moduleListArr" IN VARCHAR2, 
  "_actionListArr" IN VARCHAR2, "_featureListArr" IN VARCHAR2, 
  "_searchStartDate" IN VARCHAR2, "_searchEndDate" IN VARCHAR2, 
  "_sortParam" IN VARCHAR2, "_sortOrder" IN VARCHAR2, 
  "records" OUT SYS_REFCURSOR
) AS dateFilterStmt VARCHAR2(256);
dateField VARCHAR2(128);
req_pending_status VARCHAR2(64);
sqlStmt VARCHAR2(4000);
sortParam VARCHAR2(64);
BEGIN dateField := 'ar."checkedts"';
IF "_sortParam" IS NOT NULL THEN sortParam := 'ar.' || "_sortParam" || '';
END IF;
IF "_searchStartDate" IS NULL THEN dateFilterStmt := '';
ELSE dateFilterStmt := ' AND TO_CHAR(' || dateField || ', ''YYYY-MM-DD'') BETWEEN ''' || "_searchStartDate" || ''' AND ';
IF "_searchEndDate" IS NOT NULL THEN dateFilterStmt := dateFilterStmt || '''' || "_searchEndDate" || '''';
ELSE dateFilterStmt := dateFilterStmt || '(SELECT TO_CHAR(SYSDATE, ''YYYY-MM-DD'') FROM DUAL)';
END IF;
END IF;
sqlStmt := 'SELECT ar."requestId", ar."recordId", ar."module", ar."feature", ar."expAPIOperationName", ar."expAPINickName", ar."permissionId", ar."permissionName", cru."Username" as createdby, ar."createdts", ar."status", cku."Username" as checkedBy, ar."checkedts", ar."reason"
                    FROM "approvalrequests" ar
                    LEFT JOIN "systemuser"  cru ON cru."id" = ar."createdby"
                    LEFT JOIN "systemuser" cku ON cku."id" = ar."checkedBy"
                  WHERE  ar."checkedBy" = ''' || "_userId" || '''';
IF "_moduleListArr" IS NOT NULL THEN sqlStmt := sqlStmt || ' AND ar."module" IN (' || "_moduleListArr" || ')';
END IF;
IF "_featureListArr" IS NOT NULL THEN sqlStmt := sqlStmt || ' AND ar."feature" IN (' || "_featureListArr" || ')';
END IF;
IF "_actionListArr" IS NOT NULL THEN sqlStmt := sqlStmt || ' AND ar."expAPINickName" IN (' || "_actionListArr" || ')';
END IF;
sqlStmt := sqlStmt || dateFilterStmt;
sqlStmt := sqlStmt || ' ORDER BY ';
IF(
  sortParam IS NOT NULL 
  AND sortParam IN ('ar."status"', dateField)
) THEN sqlStmt := sqlStmt || sortParam;
ELSE sqlStmt := sqlStmt || dateField;
END IF;
IF(
  "_sortOrder" IS NOT NULL 
  AND (
    UPPER("_sortOrder")
  ) IN ('DESC', 'ASC')
) THEN sqlStmt := sqlStmt || ' ' || "_sortOrder";
ELSE sqlStmt := sqlStmt || ' ' || 'DESC';
END IF;
OPEN "records" FOR sqlStmt;
END;
/

--  DDL for Procedure fetch_approvalgroups_for_pendingtxn_proc


create or replace NONEDITIONABLE PROCEDURE "fetch_approvalgroups_for_pendingtxn_proc" (
  "signatorygroup" OUT SYS_REFCURSOR
) AS BEGIN OPEN "signatorygroup" FOR 
SELECT 
  "signatorygroup"."signatoryGroupId" 
FROM 
  "signatorygroup" 
WHERE 
  "signatorygroup"."signatoryGroupId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(
          (
            SELECT 
              LISTAGG(
                REPLACE(
                  REPLACE(
                    REPLACE(s."pendingGroupList", ']', ' '), 
                    '[', 
                    ' '
                  ), 
                  '"', 
                  ' '
                ), 
                ','
              ) 
            FROM 
              "signatorygrouprequestmatrix" S 
              JOIN "bbrequest" b ON (S."requestId" = b."requestId") 
            WHERE 
              s."isApproved" = 'false' 
              AND b."status" = 'Pending'
          )
        )
      )
  );
--DBMS_SQL.RETURN_RESULT(v_cursor);
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;

/

create or replace NONEDITIONABLE PROCEDURE "fetch_approvalmatrixtemplate_proc" (
  "_contractId" IN VARCHAR2, "_cif" IN VARCHAR2, 
  "_limitTypeId" IN VARCHAR2, "_actions" IN VARCHAR2, 
  "records" OUT SYS_REFCURSOR
) AS v__cif VARCHAR2(50) := "_cif";
v__limitTypeId VARCHAR2(50) := "_limitTypeId";
v_isGroupMatrix NUMBER(10, 0);
v_legalEntityId VARCHAR2(255);
BEGIN IF "_cif" IS NULL THEN BEGIN v__cif := '%';
END;
END IF;
BEGIN 
SELECT 
  "companyLegalUnit" INTO v_legalEntityId 
FROM 
  "contractcorecustomers" 
WHERE 
  "coreCustomerId" = "_cif" 
  AND "contractId" = "_contractId";
EXCEPTION WHEN NO_DATA_FOUND THEN v_legalEntityId := '';
END;
IF v__limitTypeId IS NULL THEN BEGIN v__limitTypeId := '%';
END;
END IF;
BEGIN 
SELECT 
  "isGroupLevel" INTO v_isGroupMatrix 
FROM 
  "approvalmode" 
WHERE 
  "contractId" = "_contractId" 
  AND "coreCustomerId" = "_cif";
EXCEPTION WHEN NO_DATA_FOUND THEN v_isGroupMatrix := '';
END;
IF v_isGroupMatrix IS NULL THEN BEGIN v_isGroupMatrix := 0;
END;
END IF;
dbms_output.put_line('v_isGroupMatrix'||v_isGroupMatrix);
dbms_output.put_line('v_legalEntityId'||v_legalEntityId);
dbms_output.put_line('_contractId'||"_contractId");
dbms_output.put_line('_cif'||"_cif");
dbms_output.put_line('v__limitTypeId'||v__limitTypeId);
dbms_output.put_line('_actions'||"_actions");
IF v_isGroupMatrix = 0 THEN BEGIN OPEN "records" FOR 
SELECT 
  approvalmatrixtemplate."id", 
  approvalmatrixtemplate."contractId", 
  approvalmatrixtemplate."limitTypeId", 
  featureaction."id" "actionId", 
  featureaction."name" "actionName", 
  featureaction."description" "actionDescription", 
  featureaction."Feature_id" "featureId", 
  featureaction."Type_id" "actionType", 
  featureaction."isAccountLevel" "isAccountLevel", 
  feature."name" "featureName", 
  feature."Status_id" "fifeaturestatus", 
  approvalrule."id" "approvalruleId", 
  approvalrule."numberOfApprovals", 
  approvalrule."name" "approvalRuleName", 
  approvalmatrixtemplate."lowerlimit", 
  approvalmatrixtemplate."upperlimit", 
  approvalmatrixtemplate."currency", 
  customer."id" "customerId", 
  customer."FirstName" "firstName", 
  customer."LastName" "lastName", 
  contractcorecustomers."coreCustomerId" "cifId", 
  contractcorecustomers."coreCustomerName" "cifName", 
  approvalmatrixtemplate."invalid", 
  approvalmatrixtemplate."isGroupMatrix" 
FROM 
  (
    (
      (
        (
          (
            (
              (
                "approvalmatrixtemplate" approvalmatrixtemplate 
                LEFT JOIN "customerapprovalmatrixtemplate" customerapprovalmatrixtemplate ON approvalmatrixtemplate."id" = customerapprovalmatrixtemplate."approvalMatrixId"
              ) 
              LEFT JOIN "customer" customer ON customerapprovalmatrixtemplate."customerId" = customer."id"
            ) 
            LEFT JOIN "featureaction" featureAction ON approvalmatrixtemplate."actionId" = featureAction."id"
          ) 
          LEFT JOIN "approvalrule" approvalRule ON approvalmatrixtemplate."approvalruleId" = approvalRule."id"
        ) 
        LEFT JOIN "feature" feature ON featureaction."Feature_id" = feature."id"
      ) 
      LEFT JOIN "contractfeatures" contractfeatures ON feature."id" = contractfeatures."featureId" 
      AND approvalmatrixtemplate."contractId" = contractfeatures."contractId" 
      AND approvalmatrixtemplate."coreCustomerId" = contractfeatures."coreCustomerId"
    ) 
    LEFT JOIN "contractcorecustomers" contractcorecustomers ON approvalmatrixtemplate."contractId" = contractcorecustomers."contractId" 
    AND approvalmatrixtemplate."coreCustomerId" = contractcorecustomers."coreCustomerId"
  ) 
WHERE 
  approvalmatrixtemplate."contractId" = "_contractId" 
  AND approvalmatrixtemplate."coreCustomerId" LIKE "_cif" 
  AND approvalmatrixtemplate."actionId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT("_actions")
      )
  ) 
  AND approvalmatrixtemplate."limitTypeId" LIKE v__limitTypeId 
  AND approvalmatrixtemplate."softdeleteflag" = 0 
  AND featureaction."approveFeatureAction" IS NOT NULL 
  AND featureaction."status" = 'SID_ACTION_ACTIVE' 
  AND featureaction."companyLegalUnit" = v_legalEntityId 
  AND feature."companyLegalUnit" = v_legalEntityId 
ORDER BY 
  approvalmatrixtemplate."contractId", 
  approvalmatrixtemplate."coreCustomerId", 
  approvalmatrixtemplate."limitTypeId", 
  approvalmatrixtemplate."actionId", 
  approvalmatrixtemplate."lowerlimit";
--DBMS_SQL.RETURN_RESULT(v_cursor);
END;
ELSE IF v_isGroupMatrix = 1 THEN BEGIN -- SQLINES LICENSE FOR EVALUATION USE ONLY
OPEN "records" FOR 
SELECT 
  approvalmatrixtemplate."id", 
  approvalmatrixtemplate."contractId", 
  approvalmatrixtemplate."limitTypeId", 
  featureaction."id" "actionId", 
  featureaction."name" "actionName", 
  featureaction."description" "actionDescription", 
  featureaction."Feature_id" "featureId", 
  featureaction."Type_id" "actionType", 
  featureaction."isAccountLevel" "isAccountLevel", 
  feature."name" "featureName", 
  feature."Status_id" "fifeaturestatus", 
  approvalrule."id" "approvalruleId", 
  approvalrule."numberOfApprovals", 
  approvalrule."name" "approvalRuleName", 
  approvalmatrixtemplate."lowerlimit", 
  approvalmatrixtemplate."upperlimit", 
  approvalmatrixtemplate."currency", 
  signatorygroupmatrixtemplate."groupList" "groupList", 
  signatorygroupmatrixtemplate."groupRule" "groupRule", 
  contractcorecustomers."coreCustomerId" "cifId", 
  contractcorecustomers."coreCustomerName" "cifName", 
  approvalmatrixtemplate."invalid", 
  approvalmatrixtemplate."isGroupMatrix" 
FROM 
  (
    (
      (
        (
          (
            (
              "approvalmatrixtemplate" approvalmatrixtemplate 
              LEFT JOIN "signatorygroupmatrixtemplate" signatorygroupmatrixtemplate ON approvalmatrixtemplate."id" = signatorygroupmatrixtemplate."approvalMatrixId"
            ) 
            LEFT JOIN "featureaction" featureAction ON approvalmatrixtemplate."actionId" = featureAction."id"
          ) 
          LEFT JOIN "approvalrule" approvalRule ON approvalmatrixtemplate."approvalruleId" = approvalRule."id"
        ) 
        LEFT JOIN "feature" feature ON featureAction."Feature_id" = feature."id"
      ) 
      LEFT JOIN "contractfeatures" contractfeatures ON feature."id" = contractfeatures."featureId" 
      AND approvalmatrixtemplate."contractId" = contractfeatures."contractId" 
      AND approvalmatrixtemplate."coreCustomerId" = contractfeatures."coreCustomerId"
    ) 
    LEFT JOIN "contractcorecustomers" contractcorecustomers ON approvalmatrixtemplate."contractId" = contractcorecustomers."contractId" 
    AND approvalmatrixtemplate."coreCustomerId" = contractcorecustomers."coreCustomerId"
  ) 
WHERE 
  approvalmatrixtemplate."contractId" = "_contractId" 
  AND approvalmatrixtemplate."coreCustomerId" LIKE "_cif" 
  AND approvalmatrixtemplate."isGroupMatrix" = 1 
  AND approvalmatrixtemplate."actionId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT("_actions")
      )
  ) 
  AND approvalmatrixtemplate."limitTypeId" LIKE v__limitTypeId 
  AND approvalmatrixtemplate."softdeleteflag" = 0 
  AND featureaction."approveFeatureAction" IS NOT NULL 
  AND featureaction."status" = 'SID_ACTION_ACTIVE' 
  AND featureaction."companyLegalUnit" = v_legalEntityId 
  AND feature."companyLegalUnit" = v_legalEntityId 
ORDER BY 
  approvalmatrixtemplate."contractId", 
  approvalmatrixtemplate."coreCustomerId", 
  approvalmatrixtemplate."limitTypeId", 
  approvalmatrixtemplate."actionId", 
  approvalmatrixtemplate."lowerlimit";
--DBMS_SQL.RETURN_RESULT(v_cursor);
END;
END IF;
END IF;
--EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/

create or replace NONEDITIONABLE PROCEDURE "fetch_approvalqueue_proc"
(
  "_customerId" IN VARCHAR2,
  "_transactionIds" IN VARCHAR2,
  "_requestIds" IN VARCHAR2,
  "_featureactionlist" IN VARCHAR2,
  "records" out sys_refcursor
)
AS
   iv_transactionIds NVARCHAR2(50) := "_transactionIds";
   iv_requestIds NVARCHAR2(2000) := "_requestIds";

BEGIN

   DECLARE
      v_combinedIds NVARCHAR2(2000);
      v_alreadyApprovedIds NVARCHAR2(2000);
      v_companyId NVARCHAR2(2000);
      v_customerMatrixIds NVARCHAR2(2000);
      v_approvalRequestIds VARCHAR2(32000);
      v_customerGroupIds NVARCHAR2(32000);
      v_groupIds NVARCHAR2(2000);
      v_features VARCHAR2(32000);
      v_monetaryActions VARCHAR2(32000);
      v_companyRequestIds NVARCHAR2(50);
      v__requestIds NVARCHAR2(50);
      v_requestIds VARCHAR2(32000);
      v_query long;
      v_select_statement long;
      v_strLen NUMBER(10,0);
      v_SubStrLen NUMBER(10,0);

   BEGIN
     
      SELECT LISTAGG("customer"."id", ',') 

        INTO v_combinedIds
        FROM "customer" 
       WHERE  "customer"."combinedUserId" = "_customerId";

      IF v_combinedIds IS NULL or v_combinedIds= '' THEN
       v_combinedIds := "_customerId" ;
      ELSE
         v_combinedIds := ("_customerId" || ',' || v_combinedIds) ;
      END IF;
      IF ( iv_transactionIds IS NULL
        OR iv_transactionIds = ' ' ) THEN
       iv_transactionIds := '''' ;
      ELSE
         iv_transactionIds := iv_transactionIds ;
      END IF;
      IF ( iv_requestIds IS NULL
        OR iv_requestIds = ' ' ) THEN
       iv_requestIds := '''' ;
      ELSE
         iv_requestIds := iv_requestIds ;
      END IF;
      SELECT LISTAGG(CAST("contractcustomers"."contractId"|| '_'|| "contractcustomers"."coreCustomerId" AS NVARCHAR2(2000)), ',') 

        INTO v_companyId
        FROM "contractcustomers" 
       WHERE  "contractcustomers"."customerId" = "_customerId";
      IF v_companyId IS NULL THEN
       v_companyId := ' ' ;
      END IF;
      WHILE "_featureactionlist" IS NULL 
    LOOP
    EXIT;
       --GOTO MAINLABEL$leave;
      END LOOP;
      SELECT LISTAGG(CAST("customerapprovalmatrix"."approvalMatrixId" AS NVARCHAR2(2000)), ',') 

        INTO v_customerMatrixIds
        FROM "customerapprovalmatrix" 
       WHERE  FIND_IN_SET("customerapprovalmatrix"."customerId", v_combinedIds) <> 0;
      IF v_customerMatrixIds IS NULL THEN
       v_customerMatrixIds := '' ;
      END IF;
      
      SELECT LISTAGG(CAST("customersignatorygroup"."signatoryGroupId" AS NVARCHAR2(2000)), ',') 

        INTO v_customerGroupIds
        FROM "customersignatorygroup" 
       WHERE  FIND_IN_SET("customersignatorygroup"."customerId", v_combinedIds) <> 0;
       
      IF v_customerGroupIds IS NULL THEN
       v_customerGroupIds := '' ;
      END IF;
      
      SELECT listagg(distinct CAST("bbactedrequest"."requestId" AS VARCHAR2(2000)),',') within group (order by "requestId")

        INTO v_alreadyApprovedIds
        FROM "bbactedrequest" 
       WHERE  FIND_IN_SET("bbactedrequest"."createdby", v_combinedIds) <> 0
                AND FIND_IN_SET("bbactedrequest"."action", 'Pending') <> 1
                AND "bbactedrequest"."softdeleteflag" = 0;
      IF v_alreadyApprovedIds IS NULL THEN

      BEGIN
         v_alreadyApprovedIds := '' ;

      END;
      END IF;
      dbms_output.put_line('v_alreadyApprovedIds'||v_alreadyApprovedIds);
      SELECT LISTAGG(DISTINCT CAST("requestapprovalmatrix"."requestId" AS VARCHAR2(2000)), ',') within group (order by "requestId")

        INTO v_approvalRequestIds
        FROM "requestapprovalmatrix" 
               JOIN "approvalmatrix"    ON "requestapprovalmatrix"."approvalMatrixId" = "approvalmatrix"."id"
               JOIN "approvalrule"    ON "approvalmatrix"."approvalruleId" = "approvalrule"."id"
       WHERE  FIND_IN_SET(CAST("requestapprovalmatrix"."approvalMatrixId" AS NVARCHAR2(2000)), v_customerMatrixIds) <> 0
                AND NOT FIND_IN_SET(CAST("requestapprovalmatrix"."requestId" AS NVARCHAR2(2000)), v_alreadyApprovedIds) <> 0
                AND ( ( "approvalrule"."numberOfApprovals" = -1
                AND "requestapprovalmatrix"."receivedApprovals" < ( SELECT COUNT(DISTINCT ("customerapprovalmatrix"."customerId"))  
                                                                            FROM "customerapprovalmatrix" 
                                                                             WHERE  "customerapprovalmatrix"."approvalMatrixId" = "requestapprovalmatrix"."approvalMatrixId" ) ) );
      IF v_approvalRequestIds IS NULL THEN

      BEGIN
         v_approvalRequestIds := '' ;
      END;
      END IF;
      
      v_groupIds := v_customerGroupIds;
      
      <<do_this>> LOOP
        dbms_output.put_line('v_groupIds'||v_groupIds);

 v_strLen := LENGTH(v_groupIds);
 SELECT listagg(DISTINCT("requestId"),',') into v_requestIds FROM "signatorygrouprequestmatrix" 
 WHERE NOT FIND_IN_SET("requestId", v_approvalRequestIds) > 0 AND "isApproved" = '0' AND FIND_IN_SET(UTILS.SUBSTRING_INDEX(v_groupIds, ',', 1) ,REPLACE(REPLACE(REPLACE("pendingGroupList",'[',''),']',''),' ','')) > 0;
 IF v_requestIds is NULL THEN
 v_requestIds := '';
 END IF;
  dbms_output.put_line('test'||v_requestIds);

 if(v_approvalRequestIds = '' OR v_approvalRequestIds IS NULL) then 
          v_approvalRequestIds := v_requestIds;
        else 
          v_approvalRequestIds := v_approvalRequestIds || ',' || v_requestIds;
        end if;
 v_SubStrLen := LENGTH(SUBSTRING_INDEX(v_groupIds, ',', 1));
 v_groupIds := SUBSTR(SUBSTR(v_groupIds,v_SubStrLen + 2),0,v_strLen);
 IF LENGTH(v_groupIds) <= 0 or v_groupIds is null THEN
 EXIT do_this;
 END IF;
 END LOOP;


      
      SELECT STRING_AGG(CAST("featureaction"."Feature_id" AS VARCHAR2(2000)))
        INTO v_features
        FROM "featureaction" 
       WHERE  FIND_IN_SET("featureaction"."id", "_featureactionlist") <> 0;
      IF v_features IS NULL THEN

      BEGIN
         v_features := ' ' ;

      END;
      END IF;
      SELECT LISTAGG(CAST("featureaction"."id" AS NVARCHAR2(2000)), ',') 

        INTO v_monetaryActions
        FROM "featureaction" 
       WHERE  FIND_IN_SET("featureaction"."Feature_id", v_features) <> 0;
      IF v_monetaryActions IS NULL THEN

      BEGIN
         v_monetaryActions := ' ' ;

      END;
      END IF;
      dbms_output.put_line('v_companyId'||v_companyId);
      dbms_output.put_line('v_monetaryActions'||v_monetaryActions);
      SELECT LISTAGG("bbrequest"."requestId", ',') 

        INTO v_companyRequestIds
        FROM "bbrequest"
       WHERE  FIND_IN_SET("bbrequest"."companyId", v_companyId) <> 0
                AND FIND_IN_SET("bbrequest"."featureActionId", v_monetaryActions) <> 0;
      IF v_companyRequestIds IS NULL THEN

      BEGIN
         v_companyRequestIds := ' ' ;

      END;
      END IF;
        IF iv_requestIds = '''' THEN
       v__requestIds := v_companyRequestIds ;
      ELSE
         v__requestIds := iv_requestIds ;
      END IF;
      dbms_output.put_line('v__requestIds'||v__requestIds);
      IF iv_transactionIds = '''' THEN
       v_query := 'WHERE FIND_IN_SET("bbrequest"."requestId",''' || v__requestIds || ''')>0' ;
      ELSE
         v_query := 'WHERE FIND_IN_SET("bbrequest"."transactionId",'|| iv_transactionIds ||  ')>0' || 'AND FIND_IN_SET("bbrequest"."featureActionId",'|| v_monetaryActions || ')>0' ;
      END IF;
    v_select_statement := ('SELECT 
                                                distinct("bbrequest"."requestId"),
                                                "bbrequest"."transactionId",
                                                "bbrequest"."status",
                                         "bbrequest"."featureActionId",
                                                "bbrequest"."isGroupMatrix",
                                                "bbrequest"."companyId",
                                                "bbrequest"."accountId",
                        "bbrequest"."additionalMeta",
                                            (CASE
                                               WHEN FIND_IN_SET(CAST("bbrequest"."createdby" AS nvarchar2(2000)),''' || v_combinedIds || ''') > 0 THEN ''true''
                                               ELSE ''false''
                                               END) as "amICreator",

                                            (CASE 
                                               WHEN FIND_IN_SET(CAST("bbrequest"."requestId" AS nvarchar2(2000)),''' || v_approvalRequestIds || ''') >0 THEN ''true''
                                               ELSE ''false''
                                            END) as "amIApprover",
                                            (CASE
                                               WHEN FIND_IN_SET(CAST("bbrequest"."requestId" AS nvarchar2(2000)),''' || v_alreadyApprovedIds || ''') > 0 THEN ''true''
                                               ELSE ''false''
                                             END) as "actedByMeAlready",
                                             (select count(DISTINCT("createdby")) from "bbactedrequest" where "bbactedrequest"."action" = ''Approved'' AND  "bbactedrequest"."requestId" = "bbrequest"."requestId" AND "bbactedrequest"."softdeleteflag" = 0) 
                                                as "receivedApprovals",
                                                       
                                            CASE 
    WHEN "bbrequest"."isGroupMatrix" = 0 THEN
        LEAST(
            (SELECT COUNT(DISTINCT "customerId") FROM "customerapprovalmatrix" WHERE "customerapprovalmatrix"."approvalMatrixId" IN (SELECT "approvalMatrixId" FROM "requestapprovalmatrix" WHERE "requestapprovalmatrix"."requestId" = "bbrequest"."requestId")),
            SUM(
                CASE 
                    WHEN "approvalrule"."numberOfApprovals" = -1 THEN (SELECT COUNT(*) FROM "customerapprovalmatrix" WHERE "customerapprovalmatrix"."approvalMatrixId" = "requestapprovalmatrix"."approvalMatrixId")
                    WHEN "approvalrule"."numberOfApprovals" IS NULL OR "approvalrule"."numberOfApprovals" = '''' THEN 0
                    ELSE "approvalrule"."numberOfApprovals"
                END
            )
        )
    ELSE NULL
END AS "requiredApprovals"

                                          FROM
                                           "bbrequest"
                                          LEFT JOIN "requestapprovalmatrix" ON ("bbrequest"."requestId" = "requestapprovalmatrix"."requestId")
                                          LEFT JOIN "approvalmatrix" ON ("requestapprovalmatrix"."approvalMatrixId" = "approvalmatrix"."id")
                                          LEFT JOIN "approvalrule" ON ("approvalmatrix"."approvalruleId" = "approvalrule"."id")'||v_query|| ' GROUP BY
    "bbrequest"."requestId",
    "bbrequest"."transactionId",
    "bbrequest"."status",
    "bbrequest"."featureActionId",
    "bbrequest"."isGroupMatrix",
    "bbrequest"."companyId",
    "bbrequest"."accountId",
    "bbrequest"."additionalMeta",
    "bbrequest"."createdby",
    "approvalrule"."numberOfApprovals",
    "requestapprovalmatrix"."approvalMatrixId"
');
                                            
      WHILE v_select_statement IS NULL 
    LOOP
     EXIT;
       --GOTO MAINLABEL$leave;
      END LOOP;
      dbms_output.put_line('v_select_statement'||v_select_statement);

      open "records" for v_select_statement;


   END;

END;
/

--  DDL for Procedure fetch_approvalrequests_counts_proc


create or replace NONEDITIONABLE PROCEDURE "fetch_approvalrequests_counts_proc" (
  "_permissionListArr" IN VARCHAR2, 
  "_userId" IN VARCHAR2, "records" OUT SYS_REFCURSOR
) AS v_req_pending_status VARCHAR2(4000);
v_execStmt VARCHAR2(4000);
BEGIN v_req_pending_status := 'Pending For Approval';
-- set it to the current string used to denote the Pending status
v_execStmt := (
  '
       select ''pendingRequests'' as category, count(*) as counts from "approvalrequests" where "createdby" = ''' || "_userId" || ''' and "status" = ''' || v_req_pending_status || '''
       union
       select ''requestHistory'' as category, count(*) as counts from "approvalrequests" where "createdby" = ''' || "_userId" || '''
       union
       select ''approvalHistory'' as category, count(*) as counts from "approvalrequests" where "checkedBy" = ''' || "_userId" || '''
       union
       select ''pendingApprovals'' as category, count(*) as counts from "approvalrequests" where "status" = ''' || v_req_pending_status || ''' and "createdby" != ''' || "_userId" || ''' and "expAPIOperationName" in 
       (select "expAPIOperationName" from "permissionapprovals" where "approvalPermissionName" in (' || "_permissionListArr" || '))'
);
EXECUTE IMMEDIATE v_execStmt;
OPEN "records" FOR v_execStmt;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/

--  DDL for Procedure fetch_approvers_proc


create or replace NONEDITIONABLE PROCEDURE "fetch_approvers_proc" (
  "_requestId" IN VARCHAR2, "records" OUT SYS_REFCURSOR
) AS BEGIN OPEN "records" FOR 
SELECT 
  "customerapprovalmatrix"."customerId" customerId 
FROM 
  "customerapprovalmatrix" 
WHERE 
  "customerapprovalmatrix"."approvalMatrixId" IN (
    SELECT 
      "requestapprovalmatrix"."approvalMatrixId" 
    FROM 
      "requestapprovalmatrix" 
    WHERE 
      "requestapprovalmatrix"."requestId" = "_requestId"
  );
--DBMS_SQL.RETURN_RESULT(v_cursor);
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;

/


--  DDL for Procedure fetch_pending_approvals_proc


CREATE OR REPLACE NONEDITIONABLE PROCEDURE "fetch_pending_approvals_proc" (
  v_permissionListArr IN VARCHAR2, v_userId IN VARCHAR2, 
  v_moduleListArr IN VARCHAR2, v_actionListArr IN VARCHAR2, 
  v_featureListArr IN VARCHAR2, v_searchStartDate IN VARCHAR2, 
  v_searchEndDate IN VARCHAR2, v_sortParam IN VARCHAR2, 
  v_sortOrder IN VARCHAR2, "approvalrequests" OUT SYS_REFCURSOR
) AS dateFilterStmt VARCHAR2(256);
dateField VARCHAR2(128);
req_pending_status VARCHAR2(64);
sqlStmt VARCHAR2(4000);
sortParam VARCHAR2(64);
BEGIN dateField := 'ar.createdts';
IF v_sortParam IS NOT NULL THEN sortParam := 'ar.' || v_sortParam || ' ';
END IF;
IF v_searchStartDate IS NULL THEN dateFilterStmt := '';
ELSE dateFilterStmt := ' AND TO_CHAR(' || dateField || ', ''YYYY-MM-DD'') BETWEEN ''' || v_searchStartDate || ''' AND ';
IF v_searchEndDate IS NOT NULL THEN dateFilterStmt := dateFilterStmt || '''' || v_searchEndDate || '''';
ELSE dateFilterStmt := dateFilterStmt || '(SELECT TO_CHAR(SYSDATE, ''YYYY-MM-DD'') FROM DUAL)';
END IF;
END IF;
req_pending_status := 'Pending For Approval';
sqlStmt := 'SELECT ar. "requestId" , ar. "recordId" , ar. "module" , ar. "feature" , ar. "expAPIOperationName" , ar. "expAPINickName" , ar. "permissionId" , ar. "permissionName" , cru. "Username"  asCREATEdby , ar. "createdts" , ar. "status" , cku. "Username"  as  checkedBy , ar. "checkedts" , ar. "reason" 
               FROM  "approvalrequests"  ar
                  LEFT JOIN  "systemuser"   cru ON cru. "id"  = ar. "createdby" 
                  LEFT JOIN  "systemuser"  cku ON cku. "id"  = ar. "checkedBy" 
               WHERE ar. "status"  = ''' || req_pending_status || ''' AND ar. "createdby"  != ''' || v_userId || '''';
IF v_moduleListArr IS NOT NULL THEN sqlStmt := sqlStmt || ' AND ar. "module"  IN (' || v_moduleListArr || ')';
END IF;
IF v_featureListArr IS NOT NULL THEN sqlStmt := sqlStmt || ' AND ar. "feature"  IN (' || v_featureListArr || ')';
END IF;
IF v_actionListArr IS NOT NULL THEN sqlStmt := sqlStmt || ' AND ar. "expAPINickName"  IN (' || v_actionListArr || ')';
END IF;
sqlStmt := sqlStmt || dateFilterStmt;
sqlStmt := sqlStmt || ' AND ar. "expAPIOperationName"  IN( 
                           SELECT  "expAPIOperationName"  FROM  "permissionapprovals"  
                           WHERE  "approvalPermissionName"  IN (' || v_permissionListArr || ') )';
sqlStmt := sqlStmt || ' ORDER BY ';
IF(
  sortParam IS NOT NULL 
  AND sortParam IN (dateField)
) THEN sqlStmt := sqlStmt || sortParam;
ELSE sqlStmt := sqlStmt || dateField;
END IF;
IF(
  v_sortOrder IS NOT NULL 
  AND (
    UPPER(v_sortOrder)
  ) IN ('DESC', 'ASC')
) THEN sqlStmt := sqlStmt || ' ' || v_sortOrder;
ELSE sqlStmt := sqlStmt || ' ' || 'DESC';
END IF;
OPEN "approvalrequests" FOR sqlStmt;
END;
/

--  DDL for Procedure fetch_requestapprovalmatrix_details_proc


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "fetch_requestapprovalmatrix_details_proc" (
  v_requestId IN nvarchar2, v_customerId IN nvarchar2, 
  "bbrequest" out SYS_REFCURSOR
) AS v_stmt nvarchar2(2000);
BEGIN OPEN "bbrequest" FOR 
SELECT 
  bb."requestId" requestId, 
  am."id" approvalMatrixId, 
  ram."id" requestApprovalMatrixId, 
  ram."receivedApprovals" receivedApprovals, 
  decode(
    ar."numberOfApprovals", -1, na.numberOfApprovals, 
    ar."numberOfApprovals"
  ) numberOfApprovals, 
  cam."customerId" customerId 
FROM 
  "bbrequest" bb 
  JOIN "requestapprovalmatrix" ram on bb."requestId" = ram."requestId" 
  and CAST(
    bb."requestId" as nvarchar2(2000)
  ) = v_requestId 
  JOIN "approvalmatrix" am on ram."approvalMatrixId" = am."id" 
  JOIN "customerapprovalmatrix" cam on cam."customerId" = v_customerId 
  JOIN "approvalrule" ar on am."approvalruleId" = ar."id" 
  JOIN (
    SELECT 
      "approvalMatrixId", 
      count(
        DISTINCT ("customerId")
      ) numberOfApprovals 
    FROM 
      "customerapprovalmatrix" 
    GROUP BY 
      "approvalMatrixId"
  ) na on na."approvalMatrixId" = am."id";
END;
/

--  DDL for Procedure fetch_signatorygroups_in_approvalrule_proc


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "fetch_signatorygroups_in_approvalrule_proc" (
  "signatorygroup" OUT SYS_REFCURSOR
) AS BEGIN OPEN "signatorygroup" FOR 
SELECT 
  "signatorygroup"."signatoryGroupId" 
FROM 
  "signatorygroup" 
WHERE 
  "signatorygroup"."signatoryGroupId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(
          (
            SELECT 
              LISTAGG(
                REPLACE(
                  REPLACE(
                    REPLACE(
                      "signatorygroupmatrix"."groupList", 
                      ']', ' '
                    ), 
                    '[', 
                    ' '
                  ), 
                  '"', 
                  ' '
                ), 
                ','
              ) 
            FROM 
              "signatorygroupmatrix" 
            WHERE 
              "signatorygroupmatrix"."approvalMatrixId" IN (
                SELECT 
                  "approvalmatrix"."id" 
                FROM 
                  "approvalmatrix" 
                WHERE 
                  "approvalmatrix"."softdeleteflag" = 'false'
              ) 
              AND "signatorygroupmatrix"."softdeleteflag" = 'false'
          )
        )
      )
  );
--DBMS_SQL.RETURN_RESULT(v_cursor);
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/

CREATE OR REPLACE NONEDITIONABLE PROCEDURE "get_actions_with_approvefeatureaction_proc" 
(
  "_featureActions" IN NVARCHAR2,
  "records" OUT SYS_REFCURSOR
)
AS
   v_actionsList NVARCHAR2(2000);


BEGIN

   SELECT listagg(CAST("featureaction"."id" AS NVARCHAR2(2000)), ',') 

     INTO v_actionsList
     FROM "featureaction" 
    WHERE  FIND_IN_SET("id", "_featureActions") <> 0
             AND ("featureaction"."approveFeatureAction" is not null or "featureaction"."approveFeatureAction" != '' );
   OPEN  "records" FOR
      SELECT v_actionsList "actions"  
        FROM DUAL  ;

END;
/

CREATE OR REPLACE NONEDITIONABLE PROCEDURE "get_signatorygroup_approvers_proc" (
  v__groupList IN VARCHAR2, "customersignatorygroup" OUT SYS_REFCURSOR
) AS v_execStmt VARCHAR2(4000);
BEGIN v_execStmt := 'select "customerId" from "customersignatorygroup" where "customersignatorygroup"."signatoryGroupId" IN ( SELECT column_value from TABLE(UTILS.STRING_SPLIT(''' || REPLACE(
  REPLACE(
    REPLACE(v__groupList, '[', ' '), 
    ']', 
    ' '
  ), 
  ' ', 
  ' '
) || ''' )))';
OPEN "customersignatorygroup" FOR v_execStmt;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/

--  DDL for Procedure getRequestApprovers_proc



create or replace NONEDITIONABLE PROCEDURE         "getRequestApprovers_proc" 
(
  "_requestId" IN NVARCHAR2,
  "_status" IN NVARCHAR2,
  "records" OUT SYS_REFCURSOR
)
AS
  v_isGroupMatrix NVARCHAR2(50);
BEGIN
  dbms_output.put_line('hi');
   IF "_status" IS NULL
     OR "_status" = ' ' THEN
   BEGIN
   SELECT "isGroupMatrix"
  INTO v_isGroupMatrix
     FROM "bbrequest" 
    WHERE  "bbrequest"."requestId" = "_requestId";
   IF v_isGroupMatrix = ' 1 ' THEN
    OPEN  "records" FOR
    
SELECT "_requestId"  "requestId",
"csg"."customerId" "approvers"  ,
                   "c"."FirstName" "FirstName",
                   "c"."LastName" "LastName"       
        FROM "customersignatorygroup" "csg" 
    LEFT JOIN "customer" "c"   ON "c"."id" = "csg"."customerId"
       WHERE  FIND_IN_SET("csg"."signatoryGroupId", ( SELECT LISTAGG(REPLACE(REPLACE(REPLACE("signatorygrouprequestmatrix"."pendingGroupList", ' ] ', ' '), ' [ ', ' '), ' "', ' '), ',') 
                                                             FROM "signatorygrouprequestmatrix" 
                                                              WHERE  "signatorygrouprequestmatrix"."requestId"="_requestId"
                                                            AND "signatorygrouprequestmatrix"."isApproved"='0' )) > 0 ;
   ELSE
    OPEN  "records" FOR
      SELECT MIN("bb"."requestId")  "requestId"  ,
             "cam"."customerId" "approvers"  ,
             MIN("c"."FirstName")  "FirstName"  ,
             MIN("c"."LastName")  "LastName"  
        FROM "bbrequest" "bb"
               CROSS JOIN "requestapprovalmatrix" "ram"
               CROSS JOIN "customerapprovalmatrix" "cam"
               CROSS JOIN "customer" "c"
       WHERE  "bb"."requestId" = "ram"."requestId"
                AND "ram"."approvalMatrixId" = "cam"."approvalMatrixId"
                AND "cam"."customerId" = "c"."id"
                AND CAST("bb"."requestId" AS NVARCHAR2(2000)) = "_requestId"
        GROUP BY "cam"."customerId"
        ORDER BY "cam"."customerId" ;
    
    END IF;
    END;
      
   ELSE

      OPEN  "records" FOR
         SELECT "bb"."createdby" "approvers"  ,
                MIN("c"."FirstName")  "FirstName"  ,
                MIN("c"."LastName")  "LastName"  
           FROM "bbactedrequest" "bb"
                  CROSS JOIN "customer" "c"
          WHERE  "bb"."createdby" = "c"."id"
                   AND CAST("bb"."requestId" AS NVARCHAR2(2000)) = "_requestId"
                   AND "bb"."status" = "_status"
           GROUP BY "bb"."createdby"
           ORDER BY "bb"."createdby" ;

   END IF;
END;
/

--  DDL for Procedure increment_receivedapprovals_proc


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "increment_receivedapprovals_proc" (
  v__requestId IN VARCHAR2, v__approvalMatrixId IN VARCHAR2
) AS BEGIN 
UPDATE 
  "requestapprovalmatrix" 
SET 
  "receivedApprovals" = "receivedApprovals" + 1 
WHERE 
  (
    "requestId" = v__requestId 
    AND "approvalMatrixId" = v__approvalMatrixId
  );
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/

--  DDL for Procedure manageapprovalmatrix_update_proc


create or replace NONEDITIONABLE PROCEDURE "manageapprovalmatrix_update_proc" 
(
  "_contractId" IN NVARCHAR2,
  "_cifList" IN NVARCHAR2,
  "_isDisabledflag" IN NVARCHAR2
)
AS
   v_index1 NUMBER(10,0) := 0;
   v_numOfRecords NUMBER(10,0);
   v_recordsData NVARCHAR2(2000);
   v_count NUMBER(10,0) := 0;
   v_query NVARCHAR2(2000);
   v_query2 NVARCHAR2(2000);
   v_cquery NVARCHAR2(2000);
   v_var NVARCHAR2(2000);
   stmt varchar2(2000);
   stmt1 varchar2(2000);
   stmt2 varchar2(2000);
   v_temp varchar2(2000);
   cnt NUMBER;

BEGIN   

   v_numOfRecords := LENGTH("_cifList") - LENGTH(REPLACE("_cifList", ',', '')) + 1 ;
   WHILE ( 1 = 1 ) 
   LOOP 

      BEGIN
         v_index1 := v_index1 + 1 ;
         IF v_index1 = v_numOfRecords + 1 THEN
          EXIT;
         ELSE

         BEGIN
            v_recordsData := (SUBSTRING_INDEX(SUBSTRING_INDEX("_cifList", ',', v_index1), ',', -1)) ;
                select count(*) into v_count from "manageapprovalmatrix" where "contractId"= "_contractId"  and "coreCustomerId"= v_recordsData ;
            IF v_count = 0 THEN

            BEGIN
               v_query := CONCAT('INSERT INTO "manageapprovalmatrix"("contractId","coreCustomerId","isDisabled") VALUES (''' || "_contractId" || ''',' || v_recordsData || ',' || "_isDisabledflag", ')') ;
               EXECUTE IMMEDIATE v_query;

            END;
            ELSE
            BEGIN
               v_query2 := 'UPDATE "manageapprovalmatrix" set "isDisabled"=' || "_isDisabledflag" || ' where "contractId"=' || "_contractId" || ' and "coreCustomerId"=' || v_recordsData ;
                EXECUTE IMMEDIATE v_query2;
            END;
            END IF;        

         END;
         END IF;

      END;
   END LOOP;

END;
/

--  DDL for Procedure role_data_movement_to_approval_proc

CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "role_data_movement_to_approval_proc" (
  v_requestId IN VARCHAR2, v_roleId IN VARCHAR2
) AS v_columnnames CLOB;
v_insertquery CLOB;
BEGIN 
Select 
  listagg(column_name, ',') WITHIN GROUP (
    ORDER BY 
      COLUMN_ID
  ) INTO v_columnnames 
from 
  user_tab_cols 
where 
  table_name = 'role';
v_insertquery := 'insert into "role_approval" select ' || v_columnnames || ',''' || v_requestId || ''',' || '''' || 'NONE' || '''' || ' from "role" where "id"=' || '''' || v_roleId || ''';';
execute immediate v_insertquery;
Select 
  listagg(column_name, ',') WITHIN GROUP (
    ORDER BY 
      COLUMN_ID
  ) INTO v_columnnames 
from 
  user_tab_cols 
where 
  table_name = 'rolepermission';
v_insertquery := 'insert into "rolepermission_approval" select ' || v_columnnames || ',''' || v_requestId || ''',' || '''' || 'NONE' || '''' || ' from "rolepermission" where "Role_id"=' || '''' || v_roleId || ''';';
execute immediate v_insertquery;
Select 
  listagg(column_name, ',') WITHIN GROUP (
    ORDER BY 
      COLUMN_ID
  ) INTO v_columnnames 
from 
  user_tab_cols 
where 
  table_name = 'userrole';
v_insertquery := 'insert into "userrole_approval" select ' || v_columnnames || ',''' || v_requestId || ''',' || '''' || 'NONE' || '''' || ' from "userrole" where "Role_id"=' || '''' || v_roleId || ''';';
execute immediate v_insertquery;
Select 
  listagg(column_name, ',') WITHIN GROUP (
    ORDER BY 
      COLUMN_ID
  ) INTO v_columnnames 
from 
  user_tab_cols 
where 
  table_name = 'userroleservicedefinition';
v_insertquery := 'insert into "userroleservicedefinition_approval" select ' || v_columnnames || ',''' || v_requestId || ''',' || '''' || 'NONE' || '''' || ' from "userroleservicedefinition" where "UserRole_id"=' || '''' || v_roleId || ''';';
execute immediate v_insertquery;
Select 
  listagg(column_name, ',') WITHIN GROUP (
    ORDER BY 
      COLUMN_ID
  ) INTO v_columnnames 
from 
  user_tab_cols 
where 
  table_name = 'rolecompositeaction';
v_insertquery := 'insert into "rolecompositeaction_approval" select ' || v_columnnames || ',''' || v_requestId || ''',' || '''' || 'NONE' || '''' || ' from "rolecompositeaction" where "Role_id"=' || '''' || v_roleId || ''';';
execute immediate v_insertquery;
END;
/

--  DDL for Procedure rolepermission_data_movement_approval_proc

CREATE OR REPLACE NONEDITIONABLE PROCEDURE "rolepermission_data_movement_approval_proc" (
  v__requestId IN VARCHAR2, v__context IN VARCHAR2
) AS BEGIN BEGIN IF v__context = 'Approved' THEN BEGIN INSERT INTO "rolepermission" (
  SELECT 
    "Role_id", 
    "Permission_id", 
    "createdby", 
    "modifiedby", 
    "createdts", 
    "lastmodifiedts", 
    "synctimestamp", 
    "softdeleteflag", 
    "companyLegalUnit" 
  FROM 
    "rolepermission_approval" 
  WHERE 
    "aprRequestId" = v__requestId
);
END;
END IF;
DELETE "rolepermission_approval" 
WHERE 
  "aprRequestId" = v__requestId 
  AND "Role_id" != ' ' 
  AND "Permission_id" != ' ';
--SQLDEV: NOT RECOGNIZED
END;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/

--  DDL for Procedure rolepermission_delete_approval_proc

CREATE OR REPLACE NONEDITIONABLE PROCEDURE "rolepermission_delete_approval_proc" (
  v__roleId IN VARCHAR2, v__PermissionIds IN CLOB, 
  v__requestId IN VARCHAR2, v__context IN VARCHAR2
) AS v_caid VARCHAR2(50);
v_isEnabled VARCHAR2(10);
v_finished NUMBER(10, 0) := 0;
v_caid_count NUMBER(10, 0) := 0;
CURSOR caids IS 
SELECT 
  c."id", 
  c."isEnabled" 
FROM 
  "compositeaction" c 
WHERE 
  (
    "Permission_id" IN (
      SELECT 
        COLUMN_VALUE 
      FROM 
        TABLE(
          UTILS.STRING_SPLIT(v__PermissionIds)
        )
    )
  );
BEGIN --   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
OPEN caids;
<< MANAGECAIDS >> WHILE 1 = 1 LOOP BEGIN FETCH caids INTO v_caid, 
v_isEnabled;
IF caids % FOUND <> FALSE THEN BEGIN GOTO MANAGECAIDS$LEAVE;
END;
END IF;
SELECT 
  COUNT(*) INTO v_caid_count 
FROM 
  "compositeaction" c, 
  "rolepermission" rp 
WHERE 
  rp."Role_id" = v__roleId 
  AND rp."Permission_id" = c."Permission_id" 
  AND rp."Permission_id" NOT IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(v__PermissionIds)
      )
  ) 
  AND c."id" = v_caid;
IF v_caid_count = 0 THEN BEGIN IF v__context = 'Approved' THEN BEGIN DELETE "rolecompositeaction" 
WHERE 
  "Role_id" = v__roleId 
  AND "CompositeAction_id" = v_caid;
END;
END IF;
DELETE "rolecompositeaction_approval" 
WHERE 
  "Role_id" = v__roleId 
  AND "CompositeAction_id" = v_caid 
  AND "aprRequestId" = v__requestId;
END;
END IF;
END;
END LOOP;
<< MANAGECAIDS$LEAVE >> CLOSE caids;
IF v__context = 'Approved' THEN BEGIN DELETE "rolepermission" 
WHERE 
  "Role_id" = v__roleId 
  AND "Permission_id" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(v__PermissionIds)
      )
  );
END;
END IF;
DELETE "rolepermission_approval" 
WHERE 
  "Role_id" = v__roleId 
  AND "Permission_id" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(v__PermissionIds)
      )
  ) 
  AND "aprRequestId" = v__requestId;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/

--  DDL for Procedure rolepermission_update_approval_proc


CREATE OR REPLACE NONEDITIONABLE PROCEDURE "rolepermission_update_approval_proc" (
  v__roleId IN VARCHAR2, v__PermissionIds IN CLOB, 
  v__requestId IN VARCHAR2
) AS v_caid VARCHAR2(50);
v_isEnabled VARCHAR2(10);
v_finished NUMBER(10, 0) := 0;
v_caid_count NUMBER(10, 0) := 0;
CURSOR caids IS 
SELECT 
  c."id", 
  c."isEnabled" 
FROM 
  "compositeaction" c 
WHERE 
  (
    "Permission_id" IN (
      SELECT 
        COLUMN_VALUE 
      FROM 
        TABLE(
          UTILS.STRING_SPLIT(v__PermissionIds)
        )
    )
  );
BEGIN --   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
OPEN caids;
<< MANAGECAIDS >> WHILE 1 = 1 LOOP BEGIN FETCH caids INTO v_caid, 
v_isEnabled;
IF caids % FOUND <> FALSE THEN BEGIN GOTO MANAGECAIDS$LEAVE;
END;
END IF;
SELECT 
  COUNT(*) INTO v_caid_count 
FROM 
  "compositeaction" c, 
  "rolepermission" rp 
WHERE 
  rp."Role_id" = v__roleId 
  AND rp."Permission_id" = c."Permission_id" 
  AND (
    rp."Permission_id" NOT IN (
      SELECT 
        COLUMN_VALUE 
      FROM 
        TABLE(
          UTILS.STRING_SPLIT(v__PermissionIds)
        )
    )
  ) 
  AND c."id" = v_caid;
IF v_caid_count = 0 THEN BEGIN 
UPDATE 
  "rolecompositeaction_approval" 
SET 
  "crudAction" = 'DEL' 
WHERE 
  "aprRequestId" = v__requestId 
  AND "CompositeAction_id" = v_caid;
END;
END IF;
END;
END LOOP;
<< MANAGECAIDS$LEAVE >> CLOSE caids;
UPDATE 
  "rolepermission_approval" 
SET 
  "crudAction" = 'DEL' 
WHERE 
  "aprRequestId" = v__requestId 
  AND "Role_id" = v__roleId 
  AND (
    "Permission_id" IN (
      SELECT 
        COLUMN_VALUE 
      FROM 
        TABLE(
          UTILS.STRING_SPLIT(v__PermissionIds)
        )
    )
  );
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/

CREATE OR REPLACE NONEDITIONABLE PROCEDURE financialinstitution_get_proc(
	"records" OUT SYS_REFCURSOR
)
AS
BEGIN
	OPEN "records" FOR SELECT 
	"financialinstitution"."name" as "companyName",
	"financialinstitution"."shortName" as "shortName",
	"financialinstitution"."typeId" as "typeId",
	"financialinstitution"."parentId" as "parentId",
	"financialinstitution"."countryCode" as "countryCode",
	"financialinstitution"."baseCurrency" as "baseCurrency",
	"financialinstitution"."language" as "language",
	"financialinstitution"."effectiveDate" as "effectiveDate",
	"financialinstitution"."closeDate" as "closeDate",
	"financialinstitutionaltkey"."alternateKey" as "id"
	FROM 
	"financialinstitution" JOIN "financialinstitutionaltkey" 
	on ("financialinstitution"."finInstitutionId" = "financialinstitutionaltkey"."entityId")
	where "financialinstitution"."softdeleteflag" = 0 and "financialinstitutionaltkey"."softdeleteflag" = 0;
END;
/

CREATE OR REPLACE NONEDITIONABLE PROCEDURE "fetch_all_profiles_proc" (
"records" OUT SYS_REFCURSOR,
"records1" out SYS_REFCURSOR
)
AS
BEGIN

open "records" for 
select "profile"."profileId", "profile"."profileName" , "profile"."profileDescription" , "profile"."profileStatus", "profile"."numberOfUsers", "profile"."profileCreationDate" , "profile"."profileDeactivatedDate",
"profilecondition"."profileConditionId" , "profilecondition"."conditionExpression" , "datacontext"."dataContextId" ,"datacontext"."dataContextName" , "datacontext"."dataContextDescription" , "datacontext"."dataContextEndPoints"
from
"profile",
"profilecondition",
"datacontext"
where
"profile"."profileId" = "profilecondition"."profileId" and
"profilecondition"."dataContextId" = "datacontext"."dataContextId" and 
"profile"."profileStatus" <> 'DELETED';
 
open "records1" for 
select "profile"."profileId",  "campaigndefinition"."campaignId" , "campaigndefinition"."campaignName" , "campaigndefinition"."campaignDescription" , "campaigndefinition"."campaignStatus" , "campaigndefinition"."startDate" ,"campaigndefinition"."endDate"  from
"campaigndefinition"  
inner join "campaignprofile" on "campaigndefinition"."campaignId" = "campaignprofile"."campaignId"  
right outer join "profile" on "profile"."profileId" = "campaignprofile"."profileId" 
where "profile"."profileStatus" <> 'DELETED';
END;
/

CREATE OR REPLACE PROCEDURE get_campaign_proc(
	"_eventCode" varchar2, 
    "_status" varchar2,
	"cur" OUT SYS_REFCURSOR, 
	"cur2" OUT SYS_REFCURSOR, 
	"cur3" OUT SYS_REFCURSOR, 
	"cur4" OUT SYS_REFCURSOR, 
	"cur5" OUT SYS_REFCURSOR, 
	"cur6" OUT SYS_REFCURSOR, 
	"cur7" OUT SYS_REFCURSOR, 
	"cur8" OUT SYS_REFCURSOR, 
	"cur9" OUT SYS_REFCURSOR, 
	"cur10" OUT SYS_REFCURSOR, 
	"cur11" OUT SYS_REFCURSOR, 
	"cur12" OUT SYS_REFCURSOR, 
	"cur13" OUT SYS_REFCURSOR, 
	"cur14" OUT SYS_REFCURSOR ,
    "cur15" OUT SYS_REFCURSOR,
    "cur16" OUT SYS_REFCURSOR
)
IS
BEGIN
	
if(length(_eventCode)>0)
then
  open "cur2" for select cd.* 
	from 
	"campaigndefinition" cd, 
	"campaigneventtrigger" cet, 
	"eventtriggers" et 
	where 
	cd."campaignId" = cet."campaignId" and 
	cet."eventTriggerId" = et."eventTriggerId" and 
	et."eventCode" = _eventCode;
elsif(length(_status)>0)
then
  open "cur15" for select * from "campaigndefinition" where "campaignStatus" = _status;
else
  open "cur" for select * from "campaigndefinition";
end if;

open "cur3" for select * from "campaigneventtrigger" ;
open "cur4" for select * from "campaignprofile"; 
open "cur5" for select * from "campaignchanneltype"; 
open "cur6" for select * from "campaignchanneldetails";  
open "cur7" for select * from "offlinetemplate"; 
open "cur8" for select * from "onlinecontent" where "campaignId"  is not null;

open "cur9" for select * from "profile" where "profileId"  in (select "profileId"  from "campaignprofile");
open "cur10" for select * from "profilecondition" where "profileId"  in (select "profileId"  from "campaignprofile") ;
open "cur11" for select * from "placeholder" where "placeholderId"  in (select "placeholderId"  from "onlinecontent" where "campaignId"  is not null);

if(length(_eventCode)>0)
then
    open "cur13" for select * from "eventtriggers" where "eventTriggerId"  in (select cet."eventTriggerId"
	from 
	"campaigneventtrigger" cet left outer join
	"eventtriggers" et 
	on 
	cet."eventTriggerId" = et."eventTriggerId" and 
	et."eventCode" = _eventCode);
elsif (length(_status)>0)
then
  open "cur16" for select * from "eventtriggers" where "eventTriggerId" in (select "eventTriggerId" from "campaigneventtrigger" c where "campaignId" in (select "campaignId"  from "campaigndefinition" c where "campaignStatus" =_status));
else
  open "cur12" for select * from "eventtriggers" where "eventTriggerId"  in (select "eventTriggerId" from "campaigneventtrigger");
end if;

open "cur14" for select * from "datacontext" where "dataContextId"  in (select "dataContextId"  from "profilecondition" where "profileId"  in (select "profileId"  from "campaignprofile") );

END;
/

DROP PROCEDURE IF EXISTS default_campaign_get_proc;

CREATE OR REPLACE NONEDITIONABLE PROCEDURE default_campaign_get_proc(
	"records" OUT SYS_REFCURSOR
)
IS
BEGIN
	OPEN "records" FOR SELECT 
	"onlinecontent"."placeholderId" ,
	"onlinecontent"."targetURL" ,
	"onlinecontent"."onlineContentId" ,
	"onlinecontent"."imageIndex" ,
	"onlinecontent"."imageURL" ,
	"placeholder"."placeholderDescription" ,
	"placeholder"."placeholderName" ,
	"placeholder"."channelSubType" ,
	"placeholder"."imageResolution",
	"placeholder"."imageScale",
  "placeholder"."placeholderIdentifier",
	FROM 
	"onlinecontent" JOIN "placeholder" 
	on ("onlinecontent"."placeholderId" = "placeholder"."placeholderId")
  WHERE "onlinecontent"."campaignId" is NULL ;
END;
/

CREATE OR REPLACE NONEDITIONABLE PROCEDURE "update_campaign_proc"(
v_eventTriggerIdList VARCHAR2(max);
v_profileIdList VARCHAR2(max);
v_channelType VARCHAR2(max);
v_offlineTemplate VARCHAR2(max);
v_onlineContent VARCHAR2(max);
v_channelDetails VARCHAR2(max);

v_campaignId  VARCHAR2(50);
v_campaignName  VARCHAR2(50);
v_campaignDescription VARCHAR2(100);
v_campaignPriority NUMBER(10; 0) ;
v_startDate   VARCHAR2(50);
v_endDate   VARCHAR2(50);
v_campaignType  VARCHAR2(50);
v_objectiveType  VARCHAR2(50);
v_productId  VARCHAR2(50);
v_productGroupId  VARCHAR2(50);
v_campaignStatus  VARCHAR2(50);
)
AS
BEGIN
	
	delete from "campaigneventtrigger" where "campaigneventtrigger"."campaignId"  = campaignId;
	
	delete from "campaignprofile" where "campaignprofile"."campaignId"    = campaignId;
	delete from "campaignchanneltype" where "campaignchanneltype"."campaignId"  =  campaignId;
	delete from "offlinetemplate" where "offlinetemplate"."campaignId"     =  campaignId;
	delete from "onlinecontent" where "onlinecontent"."campaignId"    =  campaignId;
	delete from "campaignchanneldetails" where "campaignchanneldetails"."campaignId"    =  campaignId;
	delete from "campaigndefinition" where "campaigndefinition"."campaignId"    =  campaignId;
	create_campaign_proc(eventTriggerIdList,profileIdList,channelType,offlineTemplate,onlineContent,channelDetails,
 campaignId,campaignName,campaignDescription,campaignPriority,startDate,endDate,campaignType,objectiveType,productId,productGroupId,campaignStatus);
     

open cur for select * from campaigndefinition;

open cur2 for select * from campaigneventtrigger ;
open cur3 for select * from campaignprofile; 
open cur4 for select * from campaignchanneltype; 
open cur5 for select * from campaignchanneldetails;  
open cur6 for select * from offlinetemplate; 
open cur7 for select * from onlinecontent where campaignId  is not null;

open cur8 for select * from profile where profileId  in (select profileId  from campaignprofile);
open cur9 for select * from profilecondition where profileId  in (select profileId  from campaignprofile) ;
open cur10 for select * from placeholder where placeholderId  in (select placeholderId  from onlinecontent where campaignId  is not null);
open cur11 for select * from eventtriggers where eventTriggerId  in (select eventTriggerId from campaigneventtrigger);
open cur12 for select * from datacontext where dataContextId  in (select dataContextId  from profilecondition where profileId  in (select profileId  from campaignprofile) );
END;
/

CREATE OR REPLACE NONEDITIONABLE PROCEDURE "update_profile_users_proc"(
	v__profileDataCSV IN VARCHAR2
)
AS 
v_index NUMBER(10, 0) := 0;
v_recordsData VARCHAR2(255) := '';
v_numOfRecords VARCHAR2(255) := '';
v_profileId VARCHAR2(255) := '';
v_userCount VARCHAR2(255) := '';
v_sql_query VARCHAR2(4000);

BEGIN
	v_numOfRecords := LENGTHB(v__profileDataCSV) - LENGTHB(REPLACE(v__profileDataCSV, '|', '')) + 1;
	WHILE (1 = 1) LOOP BEGIN v_index := v_index + 1;
		IF v_index = v_numOfRecords + 1 THEN EXIT;
		ELSE BEGIN 
			v_recordsData := (
			  UTILS.SUBSTRING_INDEX(
				UTILS.SUBSTRING_INDEX(v__profileDataCSV, ',', v_index), 
				',', 
				-1
			  )
			);
			v_profileId := UTILS.SUBSTRING_INDEX(
			  UTILS.SUBSTRING_INDEX(v_recordsData, ':', 1), 
			  ':', 
			  -1
			);
			v_userCount := UTILS.SUBSTRING_INDEX(
			  UTILS.SUBSTRING_INDEX(v_recordsData, ':', 2), 
			  ':', 
			  -1
			);
			
			v_sql_query := 'update "profile" set numberOfUsers = '''||v_userCount||''' where profileId = '||v_profileId ;
			EXECUTE IMMEDIATE v_select_statement;

		END;
		END IF;
	END;
	END LOOP;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/
     
END
/

DROP PROCEDURE IF EXISTS checkforpendingrequests_proc;
CREATE OR REPLACE NONEDITIONABLE PROCEDURE "checkforpendingrequests_proc"(
	_module VARCHAR2,
    _action VARCHAR2,
    _record VARCHAR2,
    _companyLegalUnitId VARCHAR2)
AS
    i INT := 1;
    num_records INT;
    current_module VARCHAR2(50);
    current_action VARCHAR2(50);
    current_legalUnit VARCHAR2(50);
    current_record VARCHAR2(255);
    requestDetails VARCHAR2(1024);
    temp_requestId VARCHAR2(50);
BEGIN
	 SELECT MAX(LENGTH(_module) - LENGTH(REPLACE(_module, '|', ''))) + 1
    INTO num_records
    FROM dual;
 
    EXECUTE IMMEDIATE 'DROP TABLE temp_checkforpendingrequests_results';
 
    EXECUTE IMMEDIATE 'CREATE GLOBAL TEMPORARY TABLE temp_checkforpendingrequests_results (
                            requestId VARCHAR2(50),
                            requestDetails VARCHAR2(1024)
                        ) ON COMMIT PRESERVE ROWS';
 
    WHILE i <= num_records LOOP
		temp_requestId = null;
        current_module := REGEXP_SUBSTR(_module, '[^|]+', 1, i);
        current_action := REGEXP_SUBSTR(_action, '[^|]+', 1, i);
        current_legalUnit := REGEXP_SUBSTR(_companyLegalUnitId, '[^|]+', 1, i);
        current_record := REGEXP_SUBSTR(_record, '[^|]+', 1, i);
 
        requestDetails := current_module || '_' || current_action || '_' || current_legalUnit || '_' || current_record;
 
        BEGIN
            SELECT requestId
            INTO temp_requestId
            FROM approvalrequests
            WHERE
                recordId = current_record AND
                module = current_module AND
                companyLegalUnit = current_legalUnit AND
                action = current_action AND
                status = 'pending' AND
                ROWNUM = 1;
 
            INSERT INTO temp_checkforpendingrequests_results (requestId, requestDetails) VALUES (temp_requestId, requestDetails);
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                NULL; 
        END;
 
        i := i + 1;
    END LOOP;
 
    FOR temp_result IN (SELECT * FROM temp_checkforpendingrequests_results) LOOP
        DBMS_OUTPUT.PUT_LINE(temp_result.requestId || ' - ' || temp_result.requestDetails);
    END LOOP;
 
    EXECUTE IMMEDIATE 'TRUNCATE TABLE temp_checkforpendingrequests_results';
END;
/
ALTER TABLE "makercheckerconfig" ADD "viewDetailsAPI" varchar(50) DEFAULT NULL;

ALTER TABLE "externalaccount" ADD "clearingIdentifierCode" varchar(100) DEFAULT NULL;
ALTER TABLE "externalaccount" ADD "clearingCode" varchar(50) DEFAULT NULL;
ALTER TABLE "externalaccount" ADD "streetName" varchar(140) DEFAULT NULL;
ALTER TABLE "externalaccount" ADD "townName" varchar(50) DEFAULT NULL;
ALTER TABLE "externalaccount" ADD "bankCountryName" varchar(50) DEFAULT NULL;
ALTER TABLE "externalaccount" ADD "intermediaryBIC" varchar(45) DEFAULT NULL;



create or replace NONEDITIONABLE PROCEDURE "default_campaign_update_proc"(
  "_campaignData" IN NVARCHAR2
) AS
    v_index NUMBER(10) := 0;
    v_numOfRecords NUMBER(10) := 0;
    v_responseIds NVARCHAR2(max);
    v_recordsData NVARCHAR2(max);
    v_onlineContentId NVARCHAR2(max);
    v_placeholderId NVARCHAR2(max);
    v_imageURL NVARCHAR2(max);
    v_targetURL NVARCHAR2(max);
    v_imageIndex NVARCHAR2(max);
 
BEGIN

  truncate table "onlinecontent";
  delete from "onlinecontent" where "onlinecontent"."campaignId" IS NULL;
  v_index = 1;
  v_numOfRecords := LENGTH("_campaignData") - LENGTH(REPLACE("_campaignData", '|', '')) + 1;
  <<loop_1>>
    WHILE (v_index <= v_numOfRecords)
    LOOP
      BEGIN 
          v_recordsData := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX("_campaignData", '|', v_index), '|', -1 );
          v_onlineContentId:= UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_recordsData, '<>', 1), '<>', -1 );
          v_placeholderId := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_recordsData, '<>', 1), '<>', -1  );
          v_imageURL  := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_recordsData, '<>', 1), '<>', -1 );
          v_targetURL  := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_recordsData, '<>', 1), '<>', -1 );
          v_imageIndex  := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_recordsData, '<>', 1), '<>', -1  );
          INSERT INTO "onlinecontent"("onlinecontent"."onlineContentId","onlinecontent"."placeholderId","onlinecontent"."imageURL","onlinecontent"."targetURL","onlinecontent"."imageIndex")
		      	values (v_onlineContentId,v_placeholderId,v_imageURL,v_targetURL,v_imageIndex);
          v_responseIds := concat(v_responseIds,' ',v_onlineContentId);
          v_index := v_index + 1;
     END
  <<loop_1>>
   SELECT v_responseIds as onlineContentId;
  END
  /
                            
                            
