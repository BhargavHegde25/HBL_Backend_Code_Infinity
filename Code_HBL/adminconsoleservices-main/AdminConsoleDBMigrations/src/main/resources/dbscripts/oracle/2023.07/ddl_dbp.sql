ALTER TABLE "excludedcontractaccounts" DROP CONSTRAINT EXCLUDEDCONTRACTACCOUNTS_ACCOUNTID_UNIQUE;



ALTER TABLE "dbxdb"."excludedcontractaccounts" ADD CONSTRAINT "EXCLUDEDCONTRACTACCOUNTS_ACCOUNTID_CUSTOMER_UNIQUE" UNIQUE("accountId","contractId","coreCustomerId");


create or replace NONEDITIONABLE PROCEDURE "contractaccounts_exists_proc"(

 "_accountsCSV" IN NVARCHAR2(50) 


begin
	
	SELECT "contractaccounts"."accountId" AS accountId, "contractaccounts"."coreCustomerId" as coreCustomerId
        FROM "contractaccounts"
        WHERE find_in_set("contractaccounts"."accountId","_accountsCSV"); 
END

END



DROP PROCEDURE IF EXISTS "excludedcontractaccounts_delete_proc";



CREATE PROCEDURE "excludedcontractaccounts_delete_proc"(
 "_contractId"  IN NVARCHAR2(50) ,
   "_coreCustomerId"  IN NVARCHAR2(50) ,
   "_accountId"  IN NVARCHAR2(50) 

)
begin
	
	DELETE 
      FROM "excludedcontractaccounts"
      WHERE 
        "excludedcontractaccounts"."contractId" = "_contractId" AND 
         "excludedcontractaccounts"."accountId" = "_accountId" AND 
         "excludedcontractaccounts"."coreCustomerId" = "_coreCustomerId";
         
END

END





create or replace NONEDITIONABLE PROCEDURE  "get_valid_customeraccounts_get_proc"(
	 "_customeraccountsCSV"  IN NVARCHAR2(2000) ,
	 "_customerId"  IN NVARCHAR2(50) ,
	 "_coreCustomerId"  IN NVARCHAR2(50) 
)
BEGIN

SET @list = (SELECT _customeraccountsCSV);
SET @validcustomeraccountsCSV = '';

iterator: LOOP
  IF LENGTH(TRIM(@list)) = 0 OR @list IS NULL THEN
    LEAVE iterator;
  END IF;
    SET @next = SUBSTRING_INDEX(@list,',',1);
    SET @nextlen = LENGTH(@next);
    SET @value = TRIM(@next);
    SET @accountIdList = (SELECT GROUP_CONCAT("customeraccounts"."Account_id" SEPARATOR ",") from "customeraccounts" WHERE ("Account_id"=@value) and "Customer_id" = "_customerId" and  "coreCustomerId" = "_coreCustomerId");
    IF ISNULL(@accountIdList)>0 OR @accountIdList="" THEN 
        IF @validcustomeraccountsCSV='' THEN
           SET @validcustomeraccountsCSV = @value;
		ELSE 
           SET @validcustomeraccountsCSV = CONCAT(@validcustomeraccountsCSV,",",@value);
	    END IF;
	END IF;
    SET @list = INSERT(@list,1,@nextlen + 1,'');
END LOOP;

select @validcustomeraccountsCSV As validAccounts;
END




create or replace NONEDITIONABLE PROCEDURE  "user_associated_corecustomeraccounts_info"(
     "customerId"  IN NVARCHAR2(50) 
)
BEGIN
    
    SET @implictCIF = (SELECT GROUP_CONCAT("contractcustomers"."coreCustomerId" SEPARATOR ",")
    FROM "contractcustomers"
    WHERE "contractcustomers"."customerId" = "customerId"
    AND "contractcustomers"."autoSyncAccounts" = '1' );
    
    SELECT "contractcorecustomers"."coreCustomerId" , "contractcorecustomers"."contractId"  from 
    "contractcorecustomers" where FIND_IN_SET("contractcorecustomers"."coreCustomerId" , @implictCIF) > 0;
    

    IF( @implictCIF) THEN
    
        SELECT "customeraccounts"."Account_id" AS nonCIFAccounts
        FROM "customeraccounts" 
        WHERE "customeraccounts"."Customer_id" = "customerId"
        AND FIND_IN_SET("customeraccounts"."coreCustomerId" , @implictCIF) = 0 ;
        
        SELECT "contractaccounts"."accountId" AS contractaccounts, "contractaccounts"."coreCustomerId" as contractcustomer
        FROM "contractaccounts"
        WHERE FIND_IN_SET("contractaccounts"."coreCustomerId" , @implictCIF) > 0 ; 
        
        SELECT "excludedcontractaccounts"."accountId" AS excludedcontractaccounts, "excludedcontractaccounts"."coreCustomerId" as contractcustomer
        FROM "excludedcontractaccounts"
        WHERE FIND_IN_SET("excludedcontractaccounts"."coreCustomerId" COLLATE utf8_general_ci , @implictCIF) > 0 ; 
        
        SELECT "customeraccounts"."Account_id" AS customeraccounts
        FROM "customeraccounts"
        WHERE "customeraccounts"."Customer_id" = "customerId"; 
        
        SELECT "excludedcustomeraccounts"."Account_id" AS excludedcustomeraccounts
        FROM "excludedcustomeraccounts"
        WHERE FIND_IN_SET("excludedcustomeraccounts"."coreCustomerId" , @implictCIF) > 0 
        AND "excludedcustomeraccounts"."Customer_id" = "customerId"; 
    ELSE
        SELECT "customeraccounts"."Account_id" AS nonCIFAccounts
        FROM "customeraccounts" 
        WHERE "customeraccounts"."Customer_id" = "customerId";
    END IF;    
END

create or replace NONEDITIONABLE PROCEDURE  "default_autosync_accounts_create_proc"
(
  "_customerId" IN NVARCHAR2,
  "_queryInput" IN NVARCHAR2
)
AS
   v_index NUMBER(10,0) := 0;
   v_recordsData NVARCHAR2(255) := N'';
   v_numOfRecords VARCHAR2(255) := N'';
   v_coreCustomerId VARCHAR2(255) := N'';
   v_accountId VARCHAR2(255) := N'';
   v_arrangementId VARCHAR2(255) := N'';
   v_accountName VARCHAR2(255) := N'';
   v_accountType VARCHAR2(255) := N'';
   v_contractId VARCHAR2(255) := N'';
   v_typeId VARCHAR2(255) := N'';
   v_ownertype VARCHAR2(255) := N'';
   v_legalEntityId VARCHAR2(255) := N'';
   v_contractaccounts VARCHAR2(255) := N'';
   v_id VARCHAR2(255) := N'';

BEGIN

   v_numOfRecords := LENGTHB("_queryInput") - LENGTHB(REPLACE("_queryInput", N'|', N'')) + 1 ;
   WHILE ( 1 = 1 ) 
   LOOP 

      BEGIN
         v_index := v_index + 1 ;
         IF v_index = v_numOfRecords + 1 THEN
          EXIT;
         ELSE

         BEGIN
            v_recordsData := (SUBSTRING_INDEX(SUBSTRING_INDEX("_queryInput", N'|', v_index), N'|', -1)) ;
            v_coreCustomerId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, ':', 1), ':', -1) ;
            v_accountId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, ':', 2), ':', -1) ;
            v_arrangementId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, ':', 3), ':', -1) ;
            v_accountName := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, ':', 4), ':', -1) ;
            v_accountType := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, ':', 5), ':', -1) ;
            v_ownertype := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, ':', 6), ':', -1) ;
            v_legalEntityId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_recordsData, ':', 7), ':', -1) ;
            
            SELECT "contractcorecustomers"."contractId" 

              INTO v_contractId
              FROM "contractcorecustomers" 
             WHERE  "contractcorecustomers"."coreCustomerId" = v_coreCustomerId;
            SELECT "accounttype"."TypeID" 

              INTO v_typeId
              FROM "accounttype" 
             WHERE  "accounttype"."TypeDescription" = v_accountType;
            SELECT "contractaccounts"."id" 

              INTO v_contractaccounts
              FROM "contractaccounts" 
             WHERE  "contractaccounts"."coreCustomerId" = v_coreCustomerId
                      AND "contractaccounts"."accountId" = v_accountId;
              exception
             when NO_DATA_FOUND then
          -- exception handling logic goes here
          v_contractaccounts := null; 
             IF (v_contractaccounts is null ) THEN
            BEGIN
               SELECT SUBSTR(SYS_GUID(), 0, 50) 

                 INTO v_id
                 FROM DUAL ;
               INSERT INTO "contractaccounts"
                 ( "contractaccounts"."id", "contractaccounts"."contractId", "contractaccounts"."accountId", "contractaccounts"."accountName", "contractaccounts"."typeId", "contractaccounts"."coreCustomerId", "contractaccounts"."ownerType", "contractaccounts"."statusDesc", "contractaccounts"."arrangementId","contractaccounts"."companyLegalUnit" )
                 VALUES ( v_id, v_contractId, v_accountId, v_accountName, v_typeId, v_coreCustomerId, 'Owner', 'Active', v_arrangementId,v_legalEntityId );

            END;
            END IF;
            SELECT SUBSTR(SYS_GUID(), 0, 50) 

              INTO v_id
              FROM DUAL ;
            INSERT INTO "customeraccounts"
              ( "customeraccounts"."id", "customeraccounts"."Customer_id", "customeraccounts"."Account_id", "customeraccounts"."AccountName", "customeraccounts"."contractId", "customeraccounts"."coreCustomerId", "customeraccounts"."accountType","customeraccounts"."companyLegalUnit" )
              VALUES ( v_id, "_customerId", v_accountId, v_accountName, v_contractId, v_coreCustomerId, v_accountType,v_legalEntityId );
             "user_account_default_actions_create_proc"(' ',"_customerId",
                                                                 v_accountId,
                                                                 v_coreCustomerId,
                                                                 v_contractId,
                                                                 ' ',v_legalEntityId) ;

         END;
         END IF;
      END;
   END LOOP;


END;
/


create or replace NONEDITIONABLE PROCEDURE  "user_account_default_actions_create_proc"
(
  "_customerId" IN NVARCHAR2,
  "_userId" IN NVARCHAR2,
  "_accountsCSV" IN NVARCHAR2,
  "_coreCustomerId" IN NVARCHAR2,
  "_contractId" IN NVARCHAR2,
  "_groupId" IN NVARCHAR2,
  "_legalEntityId" IN NVARCHAR2
)
AS
   iv_accountsCSV NVARCHAR2(2000) := "_accountsCSV";
   iv_groupId NVARCHAR2(50) := "_groupId";

BEGIN

   DECLARE
      v_finished NUMBER(10,0) := 0;
      v_featureActionId VARCHAR2(255) := N'';
      v_actionslist VARCHAR2(4000) := N'';
      v_limitId VARCHAR2(255) := N'';
      v_entryStatus NUMBER(10,0) := 0;
      v_accountId VARCHAR2(255) := N'';
      v_actualLimitId VARCHAR2(255) := N'';
      v_serviceDefinitionId VARCHAR2(255) := N'';
      v_serviceType VARCHAR2(255) := N'';
      v_validFIActions long := N'';
      v_validServiceDefinitionActions VARCHAR2(4000) := N'';
      v_validGroupActions VARCHAR2(4000) := N'';
      v_validActionsList VARCHAR2(4000) := N'';
      v_groupId VARCHAR2(255) := N'';
      v_featureId VARCHAR2(255) := N'';
      v_limitvalue VARCHAR2(255) := N'';
      v_id VARCHAR2(255) := N'';
      CURSOR accounts
        IS SELECT "customeraccounts"."Account_id" 
        FROM "customeraccounts" 
       WHERE  "customeraccounts"."contractId" = "_contractId"
        AND "customeraccounts"."coreCustomerId" = "_coreCustomerId"
        AND "customeraccounts"."Customer_id" = "_userId"
        AND INSTR(iv_accountsCSV, "Account_id") <> 0;
      CURSOR limits
        IS SELECT "LimitType_id" 
        FROM "actionlimit" 
       WHERE  "Action_id" = v_featureActionId;
      --schema 
      CURSOR actions
        IS SELECT "id" 
        FROM "featureaction" 
       WHERE  INSTR(v_validActionsList, "id") <> 0
        AND ( "featureaction"."isAccountLevel" = '1' );
   
   BEGIN
     
      IF ( CASE 
                WHEN iv_accountsCSV IS NULL THEN 1
      ELSE 0
         END <> 0
        OR iv_accountsCSV = ' ' ) THEN
       SELECT listagg(CAST("customeraccounts"."Account_id" AS NVARCHAR2(2000)), ',') 

        INTO iv_accountsCSV
        FROM "customeraccounts" 
       WHERE  "customeraccounts"."Customer_id" = "_userId"
                AND "customeraccounts"."contractId" = "_contractId"
                AND "customeraccounts"."coreCustomerId" = "_coreCustomerId";
      END IF;
      SELECT "contract"."servicedefinitionId" 

        INTO v_serviceDefinitionId
        FROM "contract" 
       WHERE  "contract"."id" = "_contractId";
      SELECT "servicedefinition"."serviceType" 

        INTO v_serviceType
        FROM "servicedefinition" 
       WHERE  "servicedefinition"."id" = v_serviceDefinitionId;
      IF ( CASE 
                WHEN iv_groupId IS NULL THEN 1
      ELSE 0
         END <> 0
        OR iv_groupId = ' ' ) THEN
       SELECT "groupservicedefinition"."Group_id" 

        INTO v_groupId
        FROM "groupservicedefinition"
       WHERE  "groupservicedefinition"."serviceDefinitionId" = v_serviceDefinitionId
                AND "groupservicedefinition"."isDefaultGroup" = '1';
      END IF;
         SELECT rtrim(xmlagg(XMLELEMENT(e,"featureaction"."id",',').EXTRACT('//text()')).GetClobVal(),',') 

        INTO v_validFIActions
        FROM "featureaction" ;
      SELECT listagg(CAST("servicedefinitionactionlimit"."actionId" AS NVARCHAR2(2000)), ',') 

        INTO v_validServiceDefinitionActions
        FROM "servicedefinitionactionlimit" 
       WHERE  "servicedefinitionactionlimit"."serviceDefinitionId" = v_serviceDefinitionId
                AND FIND_IN_SET("servicedefinitionactionlimit"."actionId", v_validFIActions) = '1';
      SELECT "groupservicedefinition"."Group_id" 

        INTO iv_groupId
        FROM "groupservicedefinition"
       WHERE  "groupservicedefinition"."serviceDefinitionId" = v_serviceDefinitionId
                AND "groupservicedefinition"."Group_id" = iv_groupId;
      exception
      when NO_DATA_FOUND then
          -- exception handling logic goes here
          iv_groupId := null; 
      SELECT listagg(CAST("groupactionlimit"."Action_id" AS NVARCHAR2(2000)), ',') 

        INTO v_validGroupActions
        FROM "groupactionlimit" 
       WHERE  "groupactionlimit"."Group_id" = iv_groupId
                AND FIND_IN_SET("groupactionlimit"."Action_id", v_validServiceDefinitionActions) = '1';
 
      SELECT listagg(CAST("contractactionlimit"."actionId" AS NVARCHAR2(2000)), ',') 

        INTO v_validActionsList
        FROM "contractactionlimit" 
       WHERE  "contractactionlimit"."contractId" = "_contractId"
                AND FIND_IN_SET("contractactionlimit"."actionId", v_validGroupActions) = '1';
      OPEN accounts;
      FETCH accounts INTO v_accountId;
      <<loop_3>>
     
	    WHILE (accounts%FOUND) 
      LOOP 
         
         BEGIN
            OPEN actions;
            FETCH actions INTO v_featureActionId;
            <<loop_2>>
           
			 WHILE (actions%FOUND) 
            LOOP 
               
               BEGIN
                  SELECT "featureaction"."Feature_id" 

                    INTO v_featureId
                    FROM "featureaction" 
                   WHERE  "featureaction"."id" = v_featureActionId;
                  v_entryStatus := 0 ;
                  OPEN limits;
                  FETCH limits INTO v_limitId;
                  <<loop_1>>
              
				   WHILE (limits%FOUND)  
                  LOOP 
                     
                     BEGIN
                        SELECT "contractactionlimit"."value" 

                          INTO v_limitvalue
                          FROM "contractactionlimit" 
                         WHERE  "contractactionlimit"."actionId" = v_featureActionId
                                  AND "contractactionlimit"."limitTypeId" = v_limitId
                                  AND "contractactionlimit"."contractId" = "_contractId"
                                  AND "contractactionlimit"."coreCustomerId" = "_coreCustomerId";
                        IF ( v_limitId = 'MAX_TRANSACTION_LIMIT' ) THEN
                         v_actualLimitId := N'AUTO_DENIED_TRANSACTION_LIMIT' ;
                        ELSE
                           IF ( v_limitId = 'MIN_TRANSACTION_LIMIT' ) THEN
                            v_actualLimitId := N'PRE_APPROVED_TRANSACTION_LIMIT' ;
                           ELSE
                              IF ( v_limitId = 'DAILY_LIMIT' ) THEN
                               
                              BEGIN
                                 v_actualLimitId := N'PRE_APPROVED_DAILY_LIMIT' ;
                                 SELECT SUBSTR(SYS_GUID(), 0, 50) 

                                   INTO v_id
                                   FROM DUAL ;
                                 INSERT INTO "customeraction"
                                   ( "customeraction"."id", "customeraction"."RoleType_id", "customeraction"."Customer_id", "customeraction"."contractId", "customeraction"."coreCustomerId", "customeraction"."featureId", "customeraction"."Action_id", "customeraction"."Account_id", "customeraction"."isAllowed", "customeraction"."LimitType_id", "customeraction"."value","customeraction"."legalEntityId" )
                                   VALUES ( v_id, v_serviceType, "_userId", "_contractId", "_coreCustomerId", v_featureId, v_featureActionId, v_accountId, 1, v_actualLimitId, 0.00,v_legalEntityId );
                                 v_actualLimitId := N'AUTO_DENIED_DAILY_LIMIT' ;
                              
                              END;
                              ELSE
                              
                              BEGIN
                                 IF ( v_limitId = 'WEEKLY_LIMIT' ) THEN
                                  
                                 BEGIN
                                    v_actualLimitId := N'PRE_APPROVED_WEEKLY_LIMIT' ;
                                    SELECT SUBSTR(SYS_GUID(), 0, 50) 

                                      INTO v_id
                                      FROM DUAL ;
                                    INSERT INTO "customeraction"
                                      ( "customeraction"."id", "customeraction"."RoleType_id", "customeraction"."Customer_id", "customeraction"."contractId", "customeraction"."coreCustomerId", "customeraction"."featureId", "customeraction"."Action_id", "customeraction"."Account_id", "customeraction"."isAllowed", "customeraction"."LimitType_id", "customeraction"."value","customeraction"."legalEntityId" )
                                      VALUES ( v_id, v_serviceType, "_userId", "_contractId", "_coreCustomerId", v_featureId, v_featureActionId, v_accountId, 1, v_actualLimitId, 0.00,v_legalEntityId );
                                    v_actualLimitId := N'AUTO_DENIED_WEEKLY_LIMIT' ;
                                 
                                 END;
                                 END IF;
                              
                              END;
                              END IF;
                           END IF;
                        END IF;
                        SELECT SUBSTR(SYS_GUID(), 0, 50) 

                          INTO v_id
                          FROM DUAL ;
                        INSERT INTO "customeraction"
                          ( "customeraction"."id", "customeraction"."RoleType_id", "customeraction"."Customer_id", "customeraction"."contractId", "customeraction"."coreCustomerId", "customeraction"."featureId", "customeraction"."Action_id", "customeraction"."Account_id", "customeraction"."isAllowed", "customeraction"."LimitType_id", "customeraction"."value","customeraction"."legalEntityId" )
                          VALUES ( v_id, v_serviceType, "_userId", "_contractId", "_coreCustomerId", v_featureId, v_featureActionId, v_accountId, 1, v_actualLimitId, v_limitvalue,v_legalEntityId );
                        SELECT SUBSTR(SYS_GUID(), 0, 50) 

                          INTO v_id
                          FROM DUAL ;
                        INSERT INTO "customeraction"
                          ( "customeraction"."id", "customeraction"."RoleType_id", "customeraction"."Customer_id", "customeraction"."contractId", "customeraction"."coreCustomerId", "customeraction"."featureId", "customeraction"."Action_id", "customeraction"."Account_id", "customeraction"."isAllowed", "customeraction"."LimitType_id", "customeraction"."value","customeraction"."legalEntityId" )
                          VALUES ( v_id, v_serviceType, "_userId", "_contractId", "_coreCustomerId", v_featureId, v_featureActionId, v_accountId, 1, v_limitId, v_limitvalue,v_legalEntityId );
                        v_entryStatus := 1 ;
                        FETCH limits INTO v_limitId;
                        GOTO loop_1;
                     
                     END;
                  END LOOP;
                  CLOSE limits;
                  IF v_entryStatus = 0 THEN
                   
                  BEGIN
                     SELECT SUBSTR(SYS_GUID(), 0, 50) 

                       INTO v_id
                       FROM DUAL ;
                     INSERT INTO "customeraction"
                       ( "customeraction"."id", "customeraction"."RoleType_id", "customeraction"."Customer_id", "customeraction"."contractId", "customeraction"."coreCustomerId", "customeraction"."featureId", "customeraction"."Action_id", "customeraction"."Account_id", "customeraction"."isAllowed" ,"customeraction"."legalEntityId")
                       VALUES ( v_id, v_serviceType, "_userId", "_contractId", "_coreCustomerId", v_featureId, v_featureActionId, v_accountId, 1,v_legalEntityId );
                  
                  END;
                  END IF;
                  v_actionslist := v_featureActionId || N',' || v_actionslist ;
                  FETCH actions INTO v_featureActionId;
                  GOTO loop_2;
               
               END;
            END LOOP;
            CLOSE actions;
            FETCH accounts INTO v_accountId;
            GOTO loop_3;
         
         END;
      END LOOP;
      CLOSE accounts;END ; 
--	 BEGIN
--   execute immediate 'drop procedure "default_autosync_accounts_create_proc"'; 
--   END;
   


END;
/


create or replace NONEDITIONABLE PROCEDURE "get_validcorecustomerslist_proc"
(
  "_coreCustomersCSV" IN NVARCHAR2,
  "companyLegalUnit" IN NVARCHAR2,
  "records" out SYS_REFCURSOR
)
AS
   v_list NVARCHAR2(2000) := N'';
   v_validcorecustomersCSV NVARCHAR2(2000) := N'';
   v_next NVARCHAR2(2000) := N'';
   v_nextlen NUMBER(10,0);
   v_initiallength NUMBER(10,0);
   v_value NVARCHAR2(2000) := N'';
   v_customers NVARCHAR2(2000) := N'';
   v_stringLength NVARCHAR2(2000) := N'';


BEGIN
 SELECT "_coreCustomersCSV" 

     INTO v_list
     FROM DUAL ;
   v_validcorecustomersCSV := N'' ;
   WHILE ( 1 = 1 ) 
   LOOP 

      BEGIN
         IF LENGTH(LTRIM(RTRIM(v_list))) = 0
           OR v_list IS NULL OR v_list = '' THEN
          EXIT;
         END IF;
         v_initiallength := LENGTH(v_list) ;
         v_next := SUBSTRING_INDEX(v_list, N',', 1) ;
         v_nextlen := LENGTH(v_next) ;
         v_value := LTRIM(RTRIM(v_next)) ;
         SELECT listagg(CAST("contractcorecustomers"."id" AS NVARCHAR2(2000)), ',') 

           INTO v_customers
           FROM "contractcorecustomers" 
          WHERE  "contractcorecustomers"."coreCustomerId" = v_value and "contractcorecustomers"."companyLegalUnit" = companyLegalUnit;
           IF v_customers is null THEN

       BEGIN
            IF v_validcorecustomersCSV is null THEN
             v_validcorecustomersCSV := v_value ;
            ELSE
               v_validcorecustomersCSV := (v_validcorecustomersCSV) || (N',') || (v_value) ;
               EXIT;
            END IF;

         END;
         END IF;
         v_stringLength := LENGTH(v_list) ;
         IF v_nextlen + 2 < v_initiallength THEN
          v_list := SUBSTR(v_list, v_nextlen + 2, v_stringLength - v_nextlen - 1) ;
         ELSE
            v_list := '' ;
         END IF;
      END;
   END LOOP;
   OPEN  "records" FOR
      SELECT v_validcorecustomersCSV "validCustomers"  
        FROM DUAL  ;


END;
/



