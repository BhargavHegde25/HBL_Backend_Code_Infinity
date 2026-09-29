ALTER TABLE "alertsubtypecustomertype"
MODIFY "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL';

ALTER TABLE "customerpreference"
ADD "DefaultFromAccountQR" VARCHAR2(50 CHAR) DEFAULT NULL;

ALTER TABLE "dbxalertcategory"
MODIFY "createdby" VARCHAR2(255) DEFAULT NULL;
ALTER TABLE "dbxalertcategory"
MODIFY "modifiedby" NVARCHAR2(255) DEFAULT NULL;

ALTER TABLE "notification"
MODIFY "receivedDate" VARCHAR2(50 CHAR) DEFAULT NULL;

CREATE OR REPLACE FORCE NONEDITIONABLE VIEW "alertcustomerchannels_view_alertcategorylevel" ("AlertSubTypeId", "Customer_id", "AccountId", "AccountType", "Value1", "Value2", "ChannelId", "companyLegalUnit") AS 
  SELECT "alertsubtype"."id" "AlertSubTypeId"  , 
          "dbxcustomeralertentitlement"."Customer_id" "Customer_id"  , 
          "dbxcustomeralertentitlement"."AccountId" "AccountId"  , 
          "dbxcustomeralertentitlement"."AccountType" "AccountType"  , 
          "dbxcustomeralertentitlement"."Value1" "Value1"  , 
          "dbxcustomeralertentitlement"."Value2" "Value2"  , 
          "customeralertchannel"."channelId" "ChannelId"  ,
          "dbxcustomeralertentitlement"."companyLegalUnit" "companyLegalUnit"
     FROM ( ( ( "dbxcustomeralertentitlement"  
                JOIN "customeralertchannel"    ON ( ( ( "dbxcustomeralertentitlement"."Customer_id" = "customeralertchannel"."customerId" ) 
                AND ( "dbxcustomeralertentitlement"."AccountId" = "customeralertchannel"."accountId" ) 
                AND ( "dbxcustomeralertentitlement"."AccountType" = "customeralertchannel"."accountType" ) 
                AND ( "dbxcustomeralertentitlement"."alertCategoryId" = "customeralertchannel"."alertCategoryId" ) ) ) 
                 )  
              JOIN "dbxalerttype"    ON ( ( "dbxcustomeralertentitlement"."alertCategoryId" = "dbxalerttype"."AlertCategoryId" ) ) 
               )  
            JOIN "alertsubtype"    ON ( ( "dbxalerttype"."id" = "alertsubtype"."AlertTypeId" ) ) 
             );


  CREATE OR REPLACE FORCE NONEDITIONABLE VIEW "alertcustomerchannels_view_alertlevel" ("AlertSubTypeId", "Customer_id", "AccountId", "AccountType", "Value1", "Value2", "ChannelId", "companyLegalUnit") AS 
  SELECT "dbxcustomeralertentitlement"."alertSubTypeId" "AlertSubTypeId"  , 
          "dbxcustomeralertentitlement"."Customer_id" "Customer_id"  , 
          "dbxcustomeralertentitlement"."AccountId" "AccountId"  , 
          "dbxcustomeralertentitlement"."AccountType" "AccountType"  , 
          "dbxcustomeralertentitlement"."Value1" "Value1"  , 
          "dbxcustomeralertentitlement"."Value2" "Value2"  , 
          "customeralertchannel"."channelId" "ChannelId" ,
          "dbxcustomeralertentitlement"."companyLegalUnit" "companyLegalUnit"
     FROM ( "dbxcustomeralertentitlement"  
            JOIN "customeralertchannel"    ON ( ( ( "dbxcustomeralertentitlement"."Customer_id" = "customeralertchannel"."customerId" ) 
            AND ( "dbxcustomeralertentitlement"."AccountId" = "customeralertchannel"."accountId" ) 
            AND ( "dbxcustomeralertentitlement"."AccountType" = "customeralertchannel"."accountType" ) 
            AND ( "dbxcustomeralertentitlement"."alertSubTypeId" = "customeralertchannel"."alertSubTypeId" ) 
            AND ( "dbxcustomeralertentitlement"."alertCategoryId" = "customeralertchannel"."alertCategoryId" ) 
            AND ( "dbxcustomeralertentitlement"."AlertTypeId" = "customeralertchannel"."alertTypeId" ) ) ) 
             );

CREATE OR REPLACE FORCE NONEDITIONABLE VIEW "alerts_fetch_globaldata_view_alertcategorylevel" ("AlertTypeId", "AlertCategoryId", "alertGroupName", "AttributeId", "recipienttype", "AlertConditionId", "Value1", "Value2", "alertName", "alerttype_status_id", "IsGlobal", "alertcategory_status_id", "ChannelId", "AlertSubTypeId", "alertsubtypetype_status_id", "accountLevel", "externalSystem") AS 
  SELECT "dbxalerttype"."id" "AlertTypeId"  , 
          "dbxalerttype"."AlertCategoryId" "AlertCategoryId"  , 
          "dbxalerttype"."Name" "alertGroupName"  , 
          "alertsubtype"."attributeId" "AttributeId"  , 
          "alertsubtype"."recipienttype" "recipienttype",
          "alertsubtype"."alertConditionId" "AlertConditionId"  , 
          "alertsubtype"."value1" "Value1"  , 
          "alertsubtype"."value2" "Value2"  , 
          "alertsubtype"."Name" "alertName"  , 
          "dbxalerttype"."Status_id" "alerttype_status_id"  , 
          "alertsubtype"."isGlobal" "IsGlobal"  , 
          "dbxalertcategory"."status_id" "alertcategory_status_id"  , 
          "alertcategorychannel"."ChannelID" "ChannelId"  , 
          "alertsubtype"."id" "AlertSubTypeId"  , 
          "alertsubtype"."Status_id" "alertsubtypetype_status_id"  , 
          "alertsubtype"."isAccountLevel" "accountLevel"  , 
          "alertsubtype"."externalSystem" "externalSystem"   
     FROM ( ( ( "dbxalertcategory"  
                JOIN "alertcategorychannel"    ON ( ( "alertcategorychannel"."AlertCategoryId" = "dbxalertcategory"."id" ) ) 
                 )  
              JOIN "dbxalerttype"    ON ( ( "dbxalerttype"."AlertCategoryId" = "dbxalertcategory"."id" ) ) 
               )  
            JOIN "alertsubtype"    ON ( ( "alertsubtype"."AlertTypeId" = "dbxalerttype"."id" ) ) 
             );




--  DDL for View alerts_fetch_globaldata_view_alertgrouplevel


  
  CREATE OR REPLACE FORCE NONEDITIONABLE VIEW "alerts_fetch_globaldata_view_alertgrouplevel" ("AlertTypeId", "AlertCategoryId", "alertGroupName", "companyLegalUnit", "AttributeId","recipienttype" ,"AlertConditionId", "Value1", "Value2", "alertName", "alerttype_status_id", "IsGlobal", "alertcategory_status_id", "ChannelId", "AlertSubTypeId", "alertsubtypetype_status_id", "accountLevel", "externalSystem") AS 
  SELECT "dbxalerttype"."id" "AlertTypeId"  , 
          "dbxalerttype"."AlertCategoryId" "AlertCategoryId"  , 
          "dbxalerttype"."Name" "alertGroupName"  , 
          "alerttypechannel"."companyLegalUnit"  "companyLegalUnit",
          "alertsubtype"."attributeId" "AttributeId"  , 
          "alertsubtype"."recipienttype" "recipienttype"  , 
          "alertsubtype"."alertConditionId" "AlertConditionId"  , 
          "alertsubtype"."value1" "Value1"  , 
          "alertsubtype"."value2" "Value2"  , 
          "alertsubtype"."Name" "alertName"  , 
          "dbxalerttype"."Status_id" "alerttype_status_id"  , 
          "alertsubtype"."isGlobal" "IsGlobal"  , 
          "dbxalertcategory"."status_id" "alertcategory_status_id"  , 
          "alerttypechannel"."channelId" "ChannelId"  , 
          "alertsubtype"."id" "AlertSubTypeId"  , 
          "alertsubtype"."Status_id" "alertsubtypetype_status_id"  , 
          "alertsubtype"."isAccountLevel" "accountLevel"  , 
          "alertsubtype"."externalSystem" "externalSystem"   
     FROM ( ( ( "dbxalerttype"  
                JOIN "alerttypechannel"    ON ( ( "dbxalerttype"."id" = "alerttypechannel"."alertTypeId" ) ) 
                 )  
              JOIN "alertsubtype"    ON ( ( "dbxalerttype"."id" = "alertsubtype"."AlertTypeId" ) ) 
               )  
            JOIN "dbxalertcategory"    ON ( ( "dbxalerttype"."AlertCategoryId" = "dbxalertcategory"."id" ) ) 
             );


--  DDL for View alerts_fetch_globaldata_view_alertlevel


  CREATE OR REPLACE FORCE NONEDITIONABLE VIEW "alerts_fetch_globaldata_view_alertlevel" ("AlertTypeId", "AlertCategoryId", "alertGroupName", "AttributeId", "recipienttype", "AlertConditionId", "Value1", "Value2", "alertName", "alerttype_status_id", "IsGlobal", "alertcategory_status_id", "ChannelId", "AlertSubTypeId", "alertsubtypetype_status_id", "accountLevel", "externalSystem") AS 
  SELECT "dbxalerttype"."id" "AlertTypeId"  , 
          "dbxalerttype"."AlertCategoryId" "AlertCategoryId"  , 
          "dbxalerttype"."Name" "alertGroupName"  , 
          "alertsubtype"."attributeId" "AttributeId"  , 
          "alertsubtype"."recipienttype" "recipienttype",
          "alertsubtype"."alertConditionId" "AlertConditionId"  , 
          "alertsubtype"."value1" "Value1"  , 
          "alertsubtype"."value2" "Value2"  , 
          "alertsubtype"."Name" "alertName"  , 
          "dbxalerttype"."Status_id" "alerttype_status_id"  , 
          "alertsubtype"."isGlobal" "IsGlobal"  , 
          "dbxalertcategory"."status_id" "alertcategory_status_id"  , 
          "alertsubtypechannel"."channelId" "ChannelId"  , 
          "alertsubtype"."id" "AlertSubTypeId"  , 
          "alertsubtype"."Status_id" "alertsubtypetype_status_id"  , 
          "alertsubtype"."isAccountLevel" "accountLevel"  , 
          "alertsubtype"."externalSystem" "externalSystem"   
     FROM ( ( ( "alertsubtype"  
                JOIN "alertsubtypechannel"    ON ( ( "alertsubtype"."id" = "alertsubtypechannel"."alertSubTypeId" ) ) 
                 )  
              JOIN "dbxalerttype"    ON ( ( "alertsubtype"."AlertTypeId" = "dbxalerttype"."id" ) ) 
               )  
            JOIN "dbxalertcategory"    ON ( ( "dbxalerttype"."AlertCategoryId" = "dbxalertcategory"."id" ) ) 
             );




     CREATE 
OR REPLACE FORCE NONEDITIONABLE VIEW "notificationview" (
  "notificationId", "imageURL", "isRead", 
  "notificationActionLink", "notificationModule", 
  "notificationSubject", "notificationSubModule", 
  "notificationText", "receivedDate", 
  "userNotificationId", "user_id", 
  "notificationCategory", "actionButtonLabelName","companyLegalUnit"
) AS 
SELECT 
  "notification"."notificationId" "notificationId", 
  "notification"."imageURL" "imageURL", 
  "usernotification"."isRead" "isRead", 
  "notification"."notificationActionLink" "notificationActionLink", 
  "notification"."notificationModule" "notificationModule", 
  "notification"."notificationSubject" "notificationSubject", 
  "notification"."notificationSubModule" "notificationSubModule", 
  "notification"."notificationText" "notificationText", 
  "usernotification"."receivedDate" "receivedDate", 
  "usernotification"."id" "userNotificationId", 
  "usernotification"."user_id" "user_id", 
  "notification"."notificationCategory" "notificationCategory", 
  "notification"."actionButtonLabelName" "actionButtonLabelName" ,
  "notification"."companyLegalUnit" "companyLegalUnit"
FROM 
  (
    "usernotification" 
    JOIN "notification" ON (
      (
        "notification"."notificationId" = "usernotification"."notification_id"
      )
    )
  );


  create or replace NONEDITIONABLE PROCEDURE "dbpalerts_customercommunication" 
(
  "_customers" IN VARCHAR2, "records" OUT SYS_REFCURSOR
)
AS
   

BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   OPEN  "records" FOR
      SELECT "customercommunication"."Customer_id" ,
             "customercommunication"."Type_id" ,
             "customercommunication"."Value" 
        FROM "customercommunication" 
       WHERE "customercommunication"."Customer_id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT("_customers")))
                AND "customercommunication"."isPrimary" = 1
        ORDER BY "customercommunication"."Type_id" ;
      --DBMS_SQL.RETURN_RESULT(v_cursor);

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/


create or replace NONEDITIONABLE PROCEDURE "dbpalerts_getnotificationid" 
(
    "records" OUT SYS_REFCURSOR
)
AS
   /*
         *   SSMA warning messages:
         *   M2SS0240: The behaviour of Standard Function SCOPE_IDENTITY may not be same as in MySQL
         */


BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   OPEN  "records" FOR
      SELECT NVL(utils.getidentity, 0) lastid  
        FROM DUAL  ;
      --DBMS_SQL.RETURN_RESULT("records");

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/

create or replace NONEDITIONABLE PROCEDURE "dbpevents_getCustidFromAccount" (
  "_accounts" IN NVARCHAR2, "records" OUT SYS_REFCURSOR
) AS BEGIN OPEN "records" FOR 
SELECT 
  "Account_id", 
  "User_id", 
  "Type_id" "accounttype_id" 
FROM 
  "accounts" 
WHERE 
  FIND_IN_SET(
    "accounts"."Account_id", "_accounts"
  ) <> 0;
END;

/
--  DDL for Procedure dbpevents_getCustIdFromCore


create or replace NONEDITIONABLE PROCEDURE "dbpevents_getCustIdFromCore" (
  "_backendids" IN NVARCHAR2, "records" OUT SYS_REFCURSOR
) AS BEGIN OPEN "records" FOR 
SELECT 
  "BackendId", 
  "Customer_id" 
FROM 
  "backendidentifier" 
WHERE 
  FIND_IN_SET(
    "backendidentifier"."BackendId", 
    "_backendids"
  ) <> 0;
END;

/
--  DDL for Procedure dbpevents_getCustomerData


create or replace NONEDITIONABLE PROCEDURE "dbpevents_getCustomerData" (
  "_customerids" IN NVARCHAR2, "_usernames" IN NVARCHAR2, 
  "records" OUT SYS_REFCURSOR
) AS BEGIN OPEN "records" FOR 
SELECT 
  "id" "CustomerId", 
  "UserName" ,"companyLegalUnit"
FROM 
  "customer" 
WHERE 
  FIND_IN_SET("customer"."id", "_customerids") <> 0 
  OR FIND_IN_SET(
    "customer"."UserName", "_usernames"
  ) <> 0;
END;
/