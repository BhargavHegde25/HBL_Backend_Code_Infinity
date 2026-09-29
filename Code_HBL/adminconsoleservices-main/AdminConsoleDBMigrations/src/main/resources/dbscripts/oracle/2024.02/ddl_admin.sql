DROP VIEW IF EXISTS "get_mc_approvalrequests_view";
CREATE VIEW "get_mc_approvalrequests_view" AS
    SELECT 
        "mcconfig"."action" AS "action",
        COUNT("ar"."requestId") AS "requestCount",
        "ar"."createdby" AS "createdby",
        "ar"."status" AS "status",
        "ar"."companyLegalUnit" AS "companyLegalUnit",
        "mcconfig"."approvalPermissionName" AS "approvalPermissionName"
    FROM
        (
          "approvalrequests" "ar" JOIN "makercheckerconfig" "mcconfig"
          ON ("ar"."expAPIOperationName" = "mcconfig"."expAPIOperationName" AND "ar"."companyLegalUnit" = "mcconfig"."companyLegalUnit")
        )
    GROUP BY "mcconfig"."action" , "ar"."createdby" , "ar"."status" , "ar"."companyLegalUnit" , "mcconfig"."approvalPermissionName";
	
ALTER TABLE "approvalrequests" ADD "action" varchar(50);

DROP TABLE IF EXISTS "mcactiontext";
CREATE TABLE "mcactiontext" (
  "id" VARCHAR(50) NOT NULL,
  "name" VARCHAR(50) NOT NULL,
  "language_code" VARCHAR(10) NOT NULL,
  "createdts" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "lastmodifiedts" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "softdeleteflag" NUMBER(1,0) NOT NULL DEFAULT '0',
  PRIMARY KEY ("id", "language_code"));

DROP TABLE IF EXISTS "mcmoduletext";
CREATE TABLE "mcmoduletext" (
  "id" VARCHAR(50) NOT NULL,
  "name" VARCHAR(50) NOT NULL,
  "language_code" VARCHAR(10) NOT NULL,
  "createdts" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "lastmodifiedts" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "softdeleteflag" NUMBER(1,0) NOT NULL DEFAULT '0',
  PRIMARY KEY ("id", "language_code"));
  
DROP PROCEDURE IF EXISTS "user_sba_securityattributes_get_proc";

CREATE PROCEDURE "user_sba_securityattributes_get_proc"(
 "@_userId"  IN NVARCHAR2(50) ,
   "@_legalEntityId"  IN NVARCHAR2(50) ,
)
DECLARE
    userAssociatedCoreCustomers VARCHAR2(255);
BEGIN
    SELECT LISTAGG(coreCustomerId, ',') WITHIN GROUP (ORDER BY coreCustomerId)
    INTO userAssociatedCoreCustomers
    FROM contractcustomers
    WHERE customerId = @_userId AND companyLegalUnit = @_legalEntityId;
 
    DBMS_OUTPUT.PUT_LINE('User Associated Core Customers: ' || userAssociatedCoreCustomers);
 
    FOR rec IN (
        SELECT contractId, coreCustomerId, Action_id, featureId
        FROM customeraction
        WHERE Customer_id = @_userId
            AND (isAllowed = '1' OR isAllowed = 'true')
            AND companyLegalUnit = @_legalEntityId
            AND INSTR(',' || userAssociatedCoreCustomers || ',', ',' || coreCustomerId || ',') > 0
            AND (Action_id LIKE 'SBA_%' OR Action_id LIKE 'CASHFLOW_%')
    )
    LOOP
        DBMS_OUTPUT.PUT_LINE('contractId=' || rec.contractId || ', coreCustomerId=' || rec.coreCustomerId || ', Action_id=' || rec.Action_id || ', featureId=' || rec.featureId);
    END LOOP;
 
    FOR rec IN (
        SELECT bi.BackendId, bi.Customer_id, c.sbaEnrolmentStatus
        FROM backendidentifier bi
        JOIN customer c ON c.id = bi.Customer_id
        WHERE bi.BackendType = 'CORE'
          AND INSTR(userAssociatedCoreCustomers, bi.BackendId) > 0
    )
    LOOP
        DBMS_OUTPUT.PUT_LINE('BackendId=' || rec.BackendId || ', Customer_id=' || rec.Customer_id || ', sbaEnrolmentStatus=' || rec.sbaEnrolmentStatus);
    END LOOP;
END;