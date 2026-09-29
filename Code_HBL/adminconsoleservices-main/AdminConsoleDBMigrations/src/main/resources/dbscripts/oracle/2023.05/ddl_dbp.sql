create or replace  PROCEDURE  "get_associated_contractaccounts_proc"
(
  "_accountIdList" IN NVARCHAR2,
  "_coreCustomerId" IN NVARCHAR2,
  "records" out SYS_REFCURSOR,
  "records1" OUT SYS_REFCURSOR,
  "records2" OUT SYS_REFCURSOR,
  "records3" OUT SYS_REFCURSOR
  
)
AS
   v__accountIdList NVARCHAR2(2000);
   v_excludedaccountIdList NVARCHAR2(2000);
   v_coreCustomerAccounts NVARCHAR2(2000);
  v_otherCoreCustomerAccounts NVARCHAR2(2000);


BEGIN

   SELECT listagg("contractaccounts"."accountId", ',') 

     INTO v__accountIdList
     FROM "contractaccounts" 
    WHERE  FIND_IN_SET("contractaccounts"."accountId", "_accountIdList") > 0;
   SELECT listagg("excludedcontractaccounts"."accountId", ',') 

     INTO v_excludedaccountIdList
     FROM "excludedcontractaccounts" 
    WHERE  FIND_IN_SET("excludedcontractaccounts"."accountId", "_accountIdList") > 0;
   OPEN  "records" FOR
      SELECT v__accountIdList "accountIdList" 
        FROM DUAL  ;
      OPEN "records1" FOR
       SELECT v_excludedaccountIdList "excludedaccountIdList" FROM DUAL;
    SELECT LISTAGG("contractaccounts"."accountId", ',') 
    INTO v_coreCustomerAccounts FROM "contractaccounts" WHERE  
    FIND_IN_SET("contractaccounts"."accountId", "_accountIdList") > 0 AND "contractaccounts"."coreCustomerId" = "_coreCustomerId";
   OPEN "records2" FOR
       SELECT v_coreCustomerAccounts "coreCustomerAccounts" FROM DUAL;
   SELECT LISTAGG("contractaccounts"."accountId", ',') 
    INTO v_otherCoreCustomerAccounts FROM "contractaccounts" WHERE  
    FIND_IN_SET("contractaccounts"."accountId", "_accountIdList") > 0 AND "contractaccounts"."coreCustomerId" <> "_coreCustomerId";
   OPEN "records3" FOR
       SELECT v_otherCoreCustomerAccounts "otherCoreCustomerAccounts" FROM DUAL;

END;
/

create or replace PROCEDURE "user_customers_proc"
(
  "_customerId" IN NVARCHAR2,
  "_coreCustomerId" IN NVARCHAR2,
  "_legalEntityId" IN NVARCHAR2,
  "records" out sys_refcursor
)
AS
   v_select_statement NVARCHAR2(2000);
   v_isWhereAppened NVARCHAR2(100);
   v_shouldAndAppend NVARCHAR2(100);
   v_filtered_contracts NVARCHAR2(2000);

BEGIN

	SELECT listagg("contractaccounts"."contractId", ',') 
    INTO v_filtered_contracts
    FROM "contractaccounts" 
    WHERE "contractaccounts"."statusDesc" != 'CLOSED' AND "contractaccounts"."contractId" IN 
	(SELECT "contractcustomers"."contractId" FROM "contractcustomers" WHERE "contractcustomers"."customerId" = "_customerId"));
	
   v_select_statement := ('(SELECT 
               "contractcustomers"."customerId" AS "customerId",
               "contractcustomers"."coreCustomerId" AS "coreCustomerId",
			   "contractcustomers"."companyLegalUnit" AS "companyLegalUnit",
               "contractcustomers"."contractId" AS "contractId",
               "contractcustomers"."autoSyncAccounts" AS "autoSyncAccounts",
               "contract"."name" AS "contractName",
               "contractcustomers"."isPrimary" AS "isPrimary",
               "contractcorecustomers"."coreCustomerName" AS "coreCustomerName",
               "contractcorecustomers"."isBusiness" AS "isBusiness",
               "contract"."servicedefinitionId" AS "serviceDefinitionId",
               "servicedefinition"."name" AS "serviceDefinitionName",
               "membergrouptype"."description" AS "serviceDefinitionType",
               "membergroup"."id" AS "roleId",
               "membergroup"."Name" AS "userRole"
           FROM
               (((((("contractcustomers"
               LEFT JOIN "contractcorecustomers" ON ((("contractcorecustomers"."contractId" = "contractcustomers"."contractId")
                   AND ("contractcorecustomers"."coreCustomerId" = "contractcustomers"."coreCustomerId"))))
               LEFT JOIN "contract" ON (("contract"."id" = "contractcorecustomers"."contractId")))
               LEFT JOIN "servicedefinition" ON (("servicedefinition"."id" = "contract"."servicedefinitionId")))
               LEFT JOIN "membergrouptype" ON (("membergrouptype"."id" = "servicedefinition"."serviceType")))
               LEFT JOIN "customergroup" ON ((("customergroup"."Customer_id" = "contractcustomers"."customerId")
                   AND ("customergroup"."contractId" = "contractcustomers"."contractId")
                   AND ("customergroup"."coreCustomerId" = "contractcustomers"."coreCustomerId"))))
               LEFT JOIN "membergroup" ON (("membergroup"."id" = "customergroup"."Group_id")))') ;
   v_isWhereAppened := 'false' ;
   v_shouldAndAppend := 'false' ;
   IF ("_customerId" is not null) THEN
    --and ("_customerId" !='') and ("_customerId" !=' ' )
   BEGIN
      IF ( v_isWhereAppened = 'false' ) THEN
       
      BEGIN
         v_select_statement := CONCAT(v_select_statement, ' where') ;
         v_isWhereAppened := 'true' ;
         v_shouldAndAppend := 'true' ;
      
      END;
      END IF;
      v_select_statement :=  v_select_statement || ' "contractcustomers"."customerId" = '|| '''' || "_customerId" || '''' ;
   
   END;
   END IF;
   IF ( "_legalEntityId"  is not null ) THEN
    
   BEGIN
      IF ( v_isWhereAppened = 'false' ) THEN
       
      BEGIN
         v_select_statement := CONCAT(v_select_statement, ' where') ;
         v_shouldAndAppend := 'true' ;
      
      END;
      END IF;
      IF ( v_isWhereAppened = 'true'
        AND v_shouldAndAppend = 'true' ) THEN
       
      BEGIN
         v_select_statement := CONCAT(v_select_statement, ' and') ;
         v_shouldAndAppend := 'true' ;
      
      END;
      END IF;
      v_select_statement := v_select_statement || ' "contractcustomers"."companyLegalUnit" = ' || '''' || "_legalEntityId" || '''' ;
   
   END;
   END IF;

   IF ( "_coreCustomerId"  is not null ) THEN
    
   BEGIN
      IF ( v_isWhereAppened = 'false' ) THEN
       
      BEGIN
         v_select_statement := CONCAT(v_select_statement, ' where') ;
         v_shouldAndAppend := 'true' ;
      
      END;
      END IF;
      IF ( v_isWhereAppened = 'true'
        AND v_shouldAndAppend = 'true' ) THEN
       
      BEGIN
         v_select_statement := CONCAT(v_select_statement, ' and') ;
         v_shouldAndAppend := 'true' ;
      
      END;
      END IF;
      v_select_statement := v_select_statement || ' "contractcustomers"."coreCustomerId" = ' || '''' || "_coreCustomerId" || '''' ;
   
   END;
   END IF;
   v_select_statement := CONCAT(v_select_statement, ' and `contractcustomers`.`contractId` in (' || '''' || v_filtered_contracts || '''' ||' ))') ;
   open "records" for v_select_statement;


END;
/

ALTER TABLE "customer" ADD "isHeavyUser" NUMBER(1,0);

ALTER TABLE "customview" ADD "coreCustomerId" NVARCHAR2(45);

ALTER TABLE "contractcustomers" ADD "FavouriteStatus" NUMBER(1,0) DEFAULT '0';
