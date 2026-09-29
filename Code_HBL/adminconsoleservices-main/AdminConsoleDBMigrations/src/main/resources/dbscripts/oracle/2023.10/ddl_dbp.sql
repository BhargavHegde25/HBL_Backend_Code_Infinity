ALTER TABLE "accountsstatementfiles" ADD "inputPayload" NVARCHAR2(50);
INSERT INTO "eventtopicconfiguration" ("eventCode", "topic") values ('ADHOC_STATEMENT','/events/adhocstatement'); 
ALTER TABLE "accountsstatementfiles"  ADD "statementType" NVARCHAR2(10) DEFAULT 'COMBINED'; 


CREATE OR REPLACE NONEDITIONABLE PROCEDURE "customer_search_proc"
  (
    "_searchType" IN VARCHAR2,
    "_id" IN VARCHAR2,
    "_name" IN VARCHAR2,
    "_SSN" IN VARCHAR2,
    "_username" IN VARCHAR2,
    "_phone" IN VARCHAR2,
    "_email" IN VARCHAR2,
    "_dateOfBirth" IN VARCHAR2,
    "_IsStaffMember" IN VARCHAR2,
    "_cardorAccountnumber" IN VARCHAR2,
    "_TIN" IN VARCHAR2,
    "_group" IN VARCHAR2,
    "_IDType" IN VARCHAR2,
    "_IDValue" IN VARCHAR2,
    "_companyId" IN VARCHAR2,
    "_requestID" IN VARCHAR2,
    "_branchIDS" IN VARCHAR2,
    "_productIDS" IN VARCHAR2,
    "_cityIDS" IN VARCHAR2,
    "_entitlementIDS" IN VARCHAR2,
    "_groupIDS" IN VARCHAR2,
    "_customerStatus" IN VARCHAR2,
    "_before" IN VARCHAR2,
    "_after" IN VARCHAR2,
    "_sortVariable" IN VARCHAR2,
    "_sortDirection" IN VARCHAR2,
    "_pageOffset" IN NUMBER,
    "_pageSize" IN NUMBER,
      "_legalEntityId" IN VARCHAR2,
    "records" OUT SYS_REFCURSOR,
      "records1" out sys_refcursor
  
  )
  AS

  BEGIN

     DECLARE
      v__maxLockCount VARCHAR2(50);
      v_search_select_statement NVARCHAR2(2000);
      v_search_count_statement NVARCHAR2(2000);
      v_queryStatement long;
      v_queryStatement2 long;

     BEGIN

      IF "_searchType" LIKE 'GROUP_SEARCH%' THEN
       DECLARE
       v_whereclause NVARCHAR2(2000);

      BEGIN

       v_search_select_statement := 'SELECT "customer"."id", "customer"."FirstName", "customer"."MiddleName", 
                                    "customer"."LastName",(NVL("customer"."FirstName",'''') || '' '' ||
                    NVL("customer"."MiddleName",'''') || '' '' || NVL("customer"."LastName",'''')) as 
                    "name","customer"."UserName" as "Username", "customer"."isCombinedUser" as "isCombinedUser",
                     NVL("customer"."combinedUserId", '''') as "combinedUserId"
                     , "customer"."Salutation", "customer"."Gender", "customer"."IsStaffMember", 
                     DECODE("customer"."isCombinedUser" , ''1'',''TYPE_id_RETAIL,TYPE_id_BUSINESS'', "customer"."CustomerType_id") AS "CustomerTypeId", 
                     "customer"."modifiedby", "customer"."lastmodifiedts", "customer"."Status_id",
                     "PrimaryEmail"."Value" AS "PrimaryEmail",LISTAGG(CAST("customergroup"."Group_id" as nvarchar2(2000)),'','') 
                                         as "assigned_group_ids","address"."City_id", "city"."Name" as "City_name", "address"."addressLine1" As "addressLine1", "address"."addressLine2" As "addressLine2", "city"."Name" As "city", 
                                         "address"."zipCode" As "zipCode", "country"."Name" As "county", "customer"."isEnrolled" as "isEnrolled","customer"."Location_id" AS 
                                         "branch_id","location"."Name" AS "branch_name", ''true'' as "isProfileExist"' ;

      v_search_count_statement := 'SELECT count(distinct "customer"."id") as "SearchMatchs" ' ;
      v_queryStatement := 'FROM "customer" JOIN (SELECT "customer"."id" FROM "customer" ' || (CASE 
                                                              WHEN ( "_groupIDS" <> ' '
                                                              OR "_entitlementIDS" <> ' ' ) THEN ' LEFT JOIN "customergroup" ON ("customergroup"."Customer_id"="customer"."id")'
       ELSE ''
        END) || (CASE 
                WHEN ( "_cityIDS" <> ' ' ) THEN ' LEFT JOIN "customeraddress" ON ("customer"."id"="customeraddress"."Customer_id" AND "customeraddress"."isPrimary"=1 and "customeraddress"."Type_id"=''ADR_TYPE_HOME'') LEFT JOIN "address" ON ("customeraddress"."Address_id" = "address"."id") 
                        LEFT JOIN "city" ON ("address"."City_id" = "city"."id") '
       ELSE ''
        END) || (CASE 
                WHEN ( "_entitlementIDS" <> ' ' ) THEN ' LEFT JOIN "customerentitlement" ON ("customerentitlement"."Customer_id"="customer"."id") '
       ELSE ''
        END) || (CASE 
                WHEN ( "_productIDS" <> ' ' ) THEN ' LEFT JOIN "customerproduct" ON ("customerproduct"."Customer_id"="customer"."id") '
       ELSE ''
        END) ;
       v_whereclause := ' WHERE 1=1 ' ;
       IF "_username" <> ' ' THEN

       BEGIN
        v_whereclause := (v_whereclause) || (' AND ("customer"."FirstName" like (''') || ("_username") || '%'')' ;
        v_whereclause := (v_whereclause) || (' OR "customer"."UserName" like (''') || ("_username") || '%'')' ;
        v_whereclause := (v_whereclause) || (' OR "customer"."id" like (''') || ("_username") || '%''))' ;

       END;
       END IF;
       IF "_IsStaffMember" <> ' ' THEN
        IF "_IsStaffMember" = 'true' THEN
        v_whereclause := (v_whereclause) || (' AND "customer"."IsStaffMember" = ''1''') ;
       ELSE
        v_whereclause := (v_whereclause) || (' AND "customer"."IsStaffMember" = ''0''') ;
       END IF;
       END IF;
       IF "_entitlementIDS" <> ' ' THEN
        v_whereclause := (v_whereclause) || (' AND ("customerentitlement"."Service_id" in (') || (func_escape_input_for_in_operator("_entitlementIDS")) || (') OR "customergroup"."Group_id" in ( select "Group_id" from "groupentitlement" where "Service_id" in (') || (func_escape_input_for_in_operator("_entitlementIDS")) || (' )))') ;
       END IF;
       IF "_groupIDS" <> ' ' THEN
        v_whereclause := (v_whereclause) || (' AND "customergroup"."Group_id" in (') || (func_escape_input_for_in_operator("_groupIDS")) || (') ') ;
       END IF;
       IF "_productIDS" <> ' ' THEN
        v_whereclause := (v_whereclause) || (' AND "customerproduct"."Product_id" in (') || (func_escape_input_for_in_operator("_productIDS")) || (')') ;
       END IF;
       IF "_branchIDS" <> ' ' THEN
        v_whereclause := (v_whereclause) || (' AND "customer"."Location_id" in (') || (func_escape_input_for_in_operator("_branchIDS")) || (')') ;
       END IF;
       IF "_customerStatus" <> ' ' THEN
        v_whereclause := (v_whereclause) || (' AND "customer"."Status_id" = ') || '''' || "_customerStatus" || '''' ;
       END IF;
       IF "_cityIDS" <> ' ' THEN
        v_whereclause := (v_whereclause) || (' AND "address"."City_id" in (') || (func_escape_input_for_in_operator("_cityIDS")) || (')') ;
       END IF;
       IF "_before" <> ' '
         AND "_after" <> ' ' THEN
                v_whereclause := (v_whereclause) || ' AND  "customer"."createdts" >= ' ||'''' ||  to_date("_before",'yyyy-mm-dd') || ''''
                || ' and "customer"."createdts" <= ' || '''' || to_date("_after",'yyyy-mm-dd') || '''' ;

            ELSE
        IF "_before" <> ' ' THEN
         --v_whereclause := (v_whereclause) || (N' AND CONVERT(DATE,"customer"."createdts",105) >= CONVERT(DATE, ') || '''' || "_before" || '''' || ',105)' ;
        v_whereclause := (v_whereclause) || ' AND "customer"."createdts" >= ' ||'''' ||  to_date("_before",'yyyy-mm-dd') || ''''; 
                ELSE

        BEGIN
           IF "_after" <> ' ' THEN
          --v_whereclause := (v_whereclause) || (N' AND CONVERT(DATE,"customer"."createdts",105) >= CONVERT(DATE, ') || '''' || "_after" || '''' || ',105)' ;
          v_whereclause := (v_whereclause) || ' AND "customer"."createdts" >= ' ||'''' ||  to_date("_after",'yyyy-mm-dd') || ''''; 
                   END IF;

        END;
        END IF;
       END IF;



       v_queryStatement := (v_queryStatement) || (v_whereclause) || (') "paginatedCustomers" ON ("paginatedCustomers"."id"="customer"."id") LEFT JOIN "customercommunication" "PrimaryEmail" ON ("PrimaryEmail"."Customer_id"="paginatedCustomers"."id" AND "PrimaryEmail"."isPrimary"=1 AND "PrimaryEmail"."Type_id"=''COMM_TYPE_email'') LEFT JOIN "customergroup" ON ("customergroup"."Customer_id"="paginatedCustomers"."id") LEFT JOIN "customeraddress" ON ("paginatedCustomers"."id"="customeraddress"."Customer_id" AND "customeraddress"."isPrimary"=1 and "customeraddress"."Type_id"=''ADR_TYPE_HOME'') LEFT JOIN "address" ON ("customeraddress"."Address_id" = "address"."id") LEFT JOIN "city" ON ("city"."id" = "address"."City_id") LEFT JOIN "country" ON ("city"."Country_id" = "country"."id") LEFT JOIN "location" ON ("location"."id"="customer"."Location_id")') ;
       IF "_searchType" = 'GROUP_SEARCH' THEN

       BEGIN
        v_queryStatement2 := (v_search_count_statement) || (v_queryStatement) ;


        v_queryStatement := (v_search_select_statement) || (v_queryStatement) || (' GROUP BY "customer"."id", "customer"."FirstName", "customer"."MiddleName", "customer"."LastName", "customer"."UserName", "customer"."isCombinedUser", "customer"."combinedUserId", "customer"."Salutation", "customer"."Gender", "customer"."IsStaffMember", "customer"."CustomerType_id", "customer"."modifiedby", "customer"."lastmodifiedts", "customer"."Status_id", "PrimaryEmail"."Value","address"."City_id", "city"."Name" ,"customer"."Location_id","location"."Name", "paginatedCustomers"."id", "address"."addressLine1","address"."addressLine2" ,"address"."zipCode","country"."Name","customer"."isEnrolled" ') ;
        IF "_sortVariable" = 'DEFAULT'
          OR "_sortVariable" = ' '
          OR "_sortVariable" IS NULL THEN
         v_queryStatement := (v_queryStatement) || (' ORDER BY "FirstName"') ;
        ELSE

        BEGIN
           IF "_sortVariable" <> ' ' THEN
          v_queryStatement := (v_queryStatement) || (' ORDER BY ') || ("_sortVariable") ;
           END IF;

        END;
        END IF;
        IF "_sortDirection" <> ' ' THEN
         v_queryStatement := (v_queryStatement) || (' ') || ("_sortDirection") ;
        END IF;
        v_queryStatement := (v_queryStatement) || (' OFFSET ') || CAST("_pageOffset" AS NVARCHAR2) || ' rows fetch next ' || CAST("_pageSize" AS NVARCHAR2) ||(' rows only') ;

       END;
       ELSE
        IF "_searchType" = 'GROUP_SEARCH_TOTAL_COUNT' THEN
         v_queryStatement := (v_search_count_statement) || (v_queryStatement) ;
        END IF;
       END IF;

      END;
      ELSE
       IF "_searchType" LIKE 'CUSTOMER_SEARCH%' THEN

       BEGIN
        SELECT CAST("passwordlockoutsettings"."accountLockoutThreshold" AS VARCHAR2(50)) 

          INTO v__maxLockCount
          FROM "passwordlockoutsettings" 
         WHERE  "passwordlockoutsettings"."id" = 'PLOCKID1';
        v_search_select_statement := ('SELECT "customer"."id", "customer"."FirstName", "customer"."MiddleName", "customer"."companyLegalUnit", "customer"."LastName","customer"."DateOfBirth",(NVL("customer"."FirstName",'''')|| '' ''|| NVL("customer"."MiddleName",'''')|| '' ''|| NVL("customer"."LastName",'''')) as "name","customer"."UserName" as "Username", "customer"."isEnrolledFromSpotlight" as "isEnrolledFromSpotlight", "customer"."isCombinedUser" as "isCombinedUser", "customer"."Salutation", "customer"."Gender",(''****''|| SUBSTR("customer"."Ssn", -4)) as "Ssn",decode("customer"."isCombinedUser" , ''1'',''TYPE_id_RETAIL,TYPE_id_BUSINESS'',"customer"."CustomerType_id") AS "CustomerTypeId", NVL("customer"."combinedUserId", '''') as "combinedUserId", "company"."id" as "CompanyId", "company"."Name" as "CompanyName","organisationemployees"."isAuthSignatory" as "isAuthSignatory", case when NVL("customer"."lockCount",0) >= ') || (v__maxLockCount) || (' then N''SID_CUS_LOCKED'' else "customer"."Status_id" end as "Status_id","PrimaryPhone"."Value" AS "PrimaryPhoneNumber","PrimaryEmail"."Value" AS "PrimaryEmailAddress",LISTAGG(CAST("membergroup"."Name" as nvarchar2(2000)),'','') as "groups", "address"."addressLine1" As "addressLine1", "address"."addressLine2" As "addressLine2", "city"."Name" As "city", "address"."zipCode" As "zipCode", "country"."Name" As "county", "customer"."isEnrolled" as "isEnrolled","customer"."ApplicantChannel", "customer"."createdts", ''true'' as "isProfileExist"') ;
        v_search_count_statement := 'SELECT count(distinct "customer"."id") as "SearchMatchs" ' ;
        v_queryStatement := ' FROM "customer"  JOIN "customerlegalentity" on ("customer"."id" = "customerlegalentity"."customer_id" and "customerlegalentity"."legalEntityId"= '||'''' || "_legalEntityId" || ''''||' )
         JOIN (SELECT "customer"."id" FROM "customer" where "customer"."id" = "customer"."id" ' || (CASE 
                                                            WHEN ( "_phone" <> ' ' ) THEN ' JOIN "customercommunication PrimaryPhone" ON ("PrimaryPhone"."Customer_id"="customer"."id" AND "PrimaryPhone"."isPrimary"=1 AND "PrimaryPhone"."Type_id"=''COMM_TYPE_phone'') '
        ELSE ''
           END) || (CASE 
                 WHEN ( "_email" <> ' ' ) THEN '  JOIN "customercommunication" "PrimaryEmail" ON ("PrimaryEmail"."Customer_id"="customer"."id" AND "PrimaryEmail"."isPrimary"=1 AND "PrimaryEmail"."Type_id"=''COMM_TYPE_email'') '
        ELSE ''
           END) || (CASE 
                 WHEN ( "_TIN" <> ' ' ) THEN ' LEFT JOIN "organisationmembership" ON ("customer"."Organization_Id" = "organisationmembership"."Organization_id")'
        ELSE ''
           END) || (CASE 
                 WHEN ( "_cardorAccountnumber" <> ' ' ) THEN ' LEFT JOIN "card" ON ("customer"."id" = "card"."User_id") LEFT JOIN "accounts" ON ("customer"."id" = "accounts"."User_id") LEFT JOIN "customeraccounts" ON ("customer"."id" = "customeraccounts"."Customer_id")'
        ELSE ''
           END) ;
        IF "_id" <> ' ' THEN
         v_queryStatement := (v_queryStatement) || (' and "customer"."id" = ') || '''' || "_id" || '''' ;
        END IF;
        IF "_name" <> ' ' THEN
         v_queryStatement := (v_queryStatement) || (' and "customer"."LastName" like (''') || ("_name") || '%'')' ;
        END IF;
        IF "_SSN" <> ' ' THEN
         v_queryStatement := (v_queryStatement) || (' and "customer"."Ssn" = ') || '''' || "_SSN" || '''' ;
        END IF;
        IF "_username" <> ' ' THEN
         v_queryStatement := (v_queryStatement) || (' and "customer"."UserName" = ') || '''' || "_username" || '''' ;
        END IF;
        IF "_dateOfBirth" <> ' ' THEN
         v_queryStatement := (v_queryStatement) || (' and "customer"."DateOfBirth" = ') || '''' || "_dateOfBirth" || '''' ;
        END IF;
        IF "_phone" <> ' ' THEN
         IF LENGTHB("_phone") > 9 THEN
         v_queryStatement := (v_queryStatement) || (' and "PrimaryPhone"."Value" like (''%') || ("_phone") || '%'')' ;
        ELSE
           v_queryStatement := (v_queryStatement) || (' and "PrimaryPhone"."Value" = ') || '''' || "_phone" || '''' ;
        END IF;
        END IF;
        IF "_email" <> ' ' THEN
         v_queryStatement := (v_queryStatement) || (' and "PrimaryEmail"."Value" = ') || '''' || "_email" || '''' ;
        END IF;
        IF "_companyId" <> ' ' THEN
         v_queryStatement := (v_queryStatement) || (' and "customer"."Organization_Id" = ') || '''' || "_companyId" || '''' ;
        END IF;
        IF "_IDValue" <> ' ' THEN
         IF "_IDType" = 'ID_DRIVING_LICENSE' THEN
         v_queryStatement := (v_queryStatement) || (' and ("customer"."DrivingLicenseNumber" = ') ||  '''' || "_IDValue" || '''' || (' or ("customer"."IDType_id" = ') || '''' || "_IDType" || '''' || (' and "customer"."IDValue" = ') || '''' || "_IDValue" || '''' || ('))') ;
        ELSE
           v_queryStatement := (v_queryStatement) || (' and ("customer"."IDType_id" = ') || '''' || "_IDType" || '''' || (' and "customer"."IDValue" = ') || '''' || "_IDValue" || '''' || (')') ;
        END IF;
        END IF;
        IF "_TIN" <> ' ' THEN
         v_queryStatement := (v_queryStatement) || (' and "organisationmembership"."Taxid" = ') || '''' || "_TIN" || '''' ;
        END IF;
        IF "_cardorAccountnumber" <> ' ' THEN
         v_queryStatement := (v_queryStatement) || (' and ("card"."cardNumber" = ') || '''' || "_cardorAccountnumber" || ''''  || (' or "accounts"."Account_id" = ') || '''' || "_cardorAccountnumber" || '''' || (' or "customeraccounts"."Account_id" = ') || '''' || "_cardorAccountnumber" || '''' || (')') ;
        END IF;
        v_queryStatement := (v_queryStatement) || (')         "paginatedCustomers" ON         ("paginatedCustomers"."id"="customer"."id") 
                  LEFT JOIN "customercommunication" "PrimaryPhone" 
                  ON ("PrimaryPhone"."Customer_id"="paginatedCustomers"."id" AND "PrimaryPhone"."isPrimary"=1 AND "PrimaryPhone"."Type_id"=''COMM_TYPE_phone'')
                  LEFT JOIN "customercommunication" "PrimaryEmail"          
                  ON ("PrimaryEmail"."Customer_id"="paginatedCustomers"."id" 
                  AND "PrimaryEmail"."isPrimary"=1 AND "PrimaryEmail"."Type_id"=''COMM_TYPE_email'')
                  LEFT JOIN "customeraddress" ON ("paginatedCustomers"."id"="customeraddress"."Customer_id" AND "customeraddress"."isPrimary"=1 and "customeraddress"."Type_id"= ''ADR_TYPE_HOME'')
                  LEFT JOIN "address" ON ("customeraddress"."Address_id" = "address"."id")
                  LEFT JOIN "city" ON ("city"."id" = "address"."City_id")
                  LEFT JOIN "country" ON ("city"."Country_id" = "country"."id")
                  LEFT JOIN "customergroup" ON 
                  ("customergroup"."Customer_id"="paginatedCustomers"."id") LEFT JOIN "membergroup" ON ("membergroup"."id"="customergroup"."Group_id") 
                  LEFT JOIN "organisation" "company" ON ("customer"."Organization_Id" = "company"."id") LEFT JOIN 
                  "organisationemployees" ON ("organisationemployees"."Organization_id" = "company"."id")') ;
        IF "_searchType" = 'CUSTOMER_SEARCH' THEN

        BEGIN
           v_queryStatement2 := (v_search_count_statement) || (v_queryStatement) ;
           v_queryStatement := (v_search_select_statement) || (v_queryStatement) || (' GROUP BY "customer"."id", "customer"."FirstName", "customer"."MiddleName", "customer"."LastName", "customer"."UserName", "customer"."Salutation", "customer"."Gender", "customer"."IsStaffMember","customer"."modifiedby", "customer"."lastmodifiedts", "customer"."Status_id", "PrimaryPhone"."Value","PrimaryEmail"."Value","customer"."Location_id","paginatedCustomers"."id","customer"."DateOfBirth","customer"."Ssn","CustomerType_id","company"."id","company"."Name","customer"."lockCount","customer"."ApplicantChannel","customer"."createdts","customer"."isEnrolledFromSpotlight","customer"."isCombinedUser","customer"."combinedUserId","organisationemployees"."isAuthSignatory","address"."addressLine1", "address"."addressLine2", "city"."Name", "address"."zipCode", "country"."Name", "customer"."isEnrolled", "customer"."companyLegalUnit" ') ;
           IF "_sortVariable" = 'DEFAULT'
           OR "_sortVariable" = ' '
           OR "_sortVariable" IS NULL THEN
          v_queryStatement := (v_queryStatement) || (' ORDER BY "FirstName"') ;
           ELSE

           BEGIN
            IF "_sortVariable" <> ' ' THEN
             v_queryStatement := (v_queryStatement) || (' ORDER BY "') || ("_sortVariable") || '"' ;
            END IF;

           END;
           END IF;
           IF "_sortDirection" <> ' ' THEN
          v_queryStatement := (v_queryStatement) || (' ') || ("_sortDirection") ;
           END IF;
           v_queryStatement := (v_queryStatement) || (' OFFSET ') || (CAST("_pageOffset" AS VARCHAR2)) || ' rows fetch next ' || (CAST("_pageSize" AS VARCHAR2)) || (' rows only') ;

        END;
        ELSE

        BEGIN
           IF "_searchType" = 'CUSTOMER_SEARCH_TOTAL_COUNT' THEN
          v_queryStatement := (v_search_count_statement) || (v_queryStatement) ;
           END IF;

        END;
        END IF;

       END;
       END IF;
      END IF;
         open "records" for v_queryStatement;
      DBMS_OUTPUT.PUT_LINE(v_queryStatement);
     IF "_searchType" = 'CUSTOMER_SEARCH'
     OR "_searchType" = 'GROUP_SEARCH' THEN

     BEGIN
      open "records1" for v_queryStatement2;

     END;
     END IF;


     END;



  END;
/

create or replace PROCEDURE "fetch_pending_approvalsqueue_proc"
(
  "_CUSTOMERID" IN NVARCHAR2,
  "_TRANSACTIONIDS" IN NVARCHAR2,
  "_REQUESTIDS" IN NVARCHAR2,
  "_FEATUREACTIONLIST" IN NVARCHAR2,
  "records" out sys_refcursor
)
AS
   iv_transactionIds NVARCHAR2(50) := "_TRANSACTIONIDS";
   iv_requestIds NVARCHAR2(2000) := "_REQUESTIDS";

BEGIN

   DECLARE
      v_combinedIds NVARCHAR2(2000);
      v_alreadyApprovedIds NVARCHAR2(2000);
      v_companyId NVARCHAR2(2000);
      v_customerMatrixIds NVARCHAR2(2000);
      v_approvalRequestIds NVARCHAR2(2000);
      v_features NVARCHAR2(2000);
      v_monetaryActions NVARCHAR2(2000);
      v_companyRequestIds NVARCHAR2(50);
      v__requestIds NVARCHAR2(50);
      v_query long;
      v_select_statement long;

   BEGIN
     
      SELECT LISTAGG("customer"."id", ',') 

        INTO v_combinedIds
        FROM "customer" 
       WHERE  "customer"."combinedUserId" = "_CUSTOMERID";
      IF v_combinedIds IS NULL THEN
       v_combinedIds := "_CUSTOMERID" ;
      ELSE
         v_combinedIds := ("_CUSTOMERID" || ',' || v_combinedIds) ;
      END IF;
      IF ( v_combinedIds IS NULL
        OR v_combinedIds = ' ' ) THEN
       v_combinedIds := '''' ;
      ELSE
         v_combinedIds := v_combinedIds ;
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
       WHERE  "contractcustomers"."customerId" = "_CUSTOMERID";
      IF v_companyId IS NULL THEN
       v_companyId := ' ' ;
      END IF;
      WHILE "_FEATUREACTIONLIST" IS NULL 
	  LOOP
	  EXIT;
       --GOTO MAINLABEL$leave;
      END LOOP;
      SELECT LISTAGG(CAST("customerapprovalmatrix"."approvalMatrixId" AS NVARCHAR2(2000)), ',') 

        INTO v_customerMatrixIds
        FROM "customerapprovalmatrix" 
       WHERE  FIND_IN_SET("customerapprovalmatrix"."customerId", v_combinedIds) <> 0;
      IF v_customerMatrixIds IS NULL THEN
       v_customerMatrixIds := ' ' ;
      END IF;
      SELECT LISTAGG(CAST("bbactedrequest"."requestId" AS NVARCHAR2(2000)), ',') 

        INTO v_alreadyApprovedIds
        FROM "bbactedrequest" 
       WHERE  FIND_IN_SET("bbactedrequest"."createdby", v_combinedIds) <> 0
                AND FIND_IN_SET("bbactedrequest"."action", 'Pending') <> 1
                AND "bbactedrequest"."softdeleteflag" = 0;
      IF v_alreadyApprovedIds IS NULL THEN

      BEGIN
         v_alreadyApprovedIds := '''' ;

      END;
      END IF;
      SELECT LISTAGG(CAST("requestapprovalmatrix"."requestId" AS NVARCHAR2(2000)), ',') 

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
         v_approvalRequestIds := ' ' ;

      END;
      END IF;
      SELECT LISTAGG(CAST("featureaction"."Feature_id" AS NVARCHAR2(2000)), ',') 

        INTO v_features
        FROM "featureaction" 
       WHERE  FIND_IN_SET("featureaction"."id", "_FEATUREACTIONLIST") <> 0;
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
      IF iv_transactionIds = '''' THEN
       v_query := 'WHERE FIND_IN_SET("bbrequest"."requestId",' || iv_requestIds || ')>0' ;
      ELSE
         v_query := 'WHERE FIND_IN_SET("bbrequest"."transactionId",'|| iv_transactionIds ||  ')>0' || 'AND FIND_IN_SET("bbrequest"."featureActionId",'|| v_monetaryActions || ')>0' ;
      END IF;
    v_select_statement := ('SELECT 
                                                "bbrequest"."requestId",
                                                "bbrequest"."transactionId",
                                                "bbrequest"."status",
                                   			 "bbrequest"."featureActionId",
												"bbrequest"."additionalMeta",
                                            (CASE
                                               WHEN FIND_IN_SET(CAST("bbrequest"."createdby" AS nvarchar2(2000)),''' || v_combinedIds || ''') > 0 THEN ''true''
                                               ELSE ''false''
                                               END) as "amICreator",

                                            (CASE 
                                               WHEN FIND_IN_SET(CAST("bbrequest"."requestId" AS nvarchar2(2000)),''' || v_approvalRequestIds || ''') >0 THEN ''true''
                                               ELSE ''false''
                                            END)
                                             as "amIApprover",
                                            (CASE
                                               WHEN FIND_IN_SET(CAST("bbrequest"."requestId" AS nvarchar2(2000)),''' || v_alreadyApprovedIds || ''') > 0 THEN ''true''
                                               ELSE ''false''
                                             END) as "actedByMeAlready",
                                             (select count(DISTINCT("createdby")) from "bbactedrequest" where "bbactedrequest"."action" = ''Approved'' AND  "bbactedrequest"."requestId" = "bbrequest"."requestId" AND "bbactedrequest"."softdeleteflag" = 0) 
                                   							as "receivedApprovals",
                                                       LEAST(
                                   						(SELECT COUNT(DISTINCT("customerId")) FROM "customerapprovalmatrix" WHERE "customerapprovalmatrix"."approvalMatrixId" in (SELECT "approvalMatrixId" FROM "requestapprovalmatrix" where "requestapprovalmatrix"."requestId" = "bbrequest"."requestId")) 
                                   						, 

                                   							CASE "approvalrule"."numberOfApprovals"
                                   								WHEN -1 THEN (SELECT COUNT(*) FROM "customerapprovalmatrix" WHERE "customerapprovalmatrix"."approvalMatrixId" = "requestapprovalmatrix"."approvalMatrixId")
                                   								WHEN NULL THEN 0
                                   								ELSE "approvalrule"."numberOfApprovals"
                                   							END

                                   					) as "requiredApprovals"
                                   				FROM
                                   				 "bbrequest"
                                   				LEFT JOIN "requestapprovalmatrix" ON ("bbrequest"."requestId" = "requestapprovalmatrix"."requestId")
                                   				LEFT JOIN "approvalmatrix" ON ("requestapprovalmatrix"."approvalMatrixId" = "approvalmatrix"."id")
                                   				LEFT JOIN "approvalrule" ON ("approvalmatrix"."approvalruleId" = "approvalrule"."id") ');

      WHILE v_select_statement IS NULL 
	  LOOP
	   EXIT;
       --GOTO MAINLABEL$leave;
      END LOOP;
      open "records" for v_select_statement;



   END;

END;
/
