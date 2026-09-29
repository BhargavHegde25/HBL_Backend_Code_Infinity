alter table "pfmtransactions"
modify "transactionDate" varchar2(50);

create or replace NONEDITIONABLE TRIGGER "convert_limit_to_double_approvalmatrix"
BEFORE INSERT OR UPDATE ON "approvalmatrix" 
FOR EACH ROW
DECLARE
BEGIN
  if :NEW."lowerlimit" is not null then
  if :NEW."lowerlimit"  = 0 or  :NEW."lowerlimit" = '0' then
    :NEW."lowerlimit" := '0.00';
  else
  :NEW."lowerlimit" := TRIM(TO_CHAR(:NEW."lowerlimit",'999999999999D99'));
  end if;
  end if;
  if :NEW."upperlimit" is not null then
  if :NEW."upperlimit"  = 0 or  :NEW."upperlimit" = '0' then
    :NEW."upperlimit" := '0.00';
  else
    :NEW."upperlimit" := TRIM(TO_CHAR(:NEW."upperlimit",'999999999999D99'));
  end if;
  end if;
END;
/


create or replace NONEDITIONABLE TRIGGER "convert_limit_to_double_approvalmatrixtemplate"
BEFORE INSERT OR UPDATE ON "approvalmatrixtemplate" 
FOR EACH ROW
DECLARE
BEGIN
  if :NEW."lowerlimit" is not null then
  if :NEW."lowerlimit"  = 0 or  :NEW."lowerlimit" = '0' then
    :NEW."lowerlimit" := '0.00';
  else
  :NEW."lowerlimit" := TRIM(TO_CHAR(:NEW."lowerlimit",'999999999999D99'));
  end if;
  end if;
  if :NEW."upperlimit" is not null then
  if :NEW."upperlimit"  = 0 or  :NEW."upperlimit" = '0' then
    :NEW."upperlimit" := '0.00';
  else
    :NEW."upperlimit" := TRIM(TO_CHAR(:NEW."upperlimit",'999999999999D99'));
  end if;
  end if;
END;
/


  CREATE OR REPLACE FORCE NONEDITIONABLE VIEW "locationdetails_view" ("locationId", "workingHours", "informationTitle", "Name", "Description", "Code", "phoneNumber", "email", "status", "isVisible", "type", "isMobile", "IsMainBranch", "services", "currencies", "segments", "facilities_names", "facilities_codes", "city", "region", "country", "addressLine1", "addressLine2", "addressLine3", "zipCode", "latitude", "longitude") AS 
  SELECT 
  DISTINCT "location"."id" "locationId", 
  (
    SELECT 
      DISTINCT LISTAGG(
        UPPER(
          SUBSTR(
            "dayschedule"."WeekDayName", 0, 1
          )
        ) || LOWER(
          SUBSTR(
            "dayschedule"."WeekDayName", 0, 2
          )
        ) || ':' || CAST(
          "dayschedule"."StartTime" as varchar2(4000)
        ) || '-' || CAST(
          "dayschedule"."EndTime" as varchar2(4000)
        ), 
        '||'
      ) 
    FROM 
      (
        "dayschedule" 
        JOIN "location" l ON "dayschedule"."WorkSchedule_id" = l."WorkSchedule_id"
      )
  ) "workingHours", 
  "location"."Name" "informationTitle", 
  "location"."Name" "Name", 
  "location"."Description" "Description", 
  "location"."Code" "Code", 
  "location"."PhoneNumber" "phoneNumber", 
  "location"."EmailId" "email", 
  (
    CASE CAST(
      "location"."Status_id" as varchar2(50 char)
    ) WHEN 'SID_ACTIVE' THEN 'OPEN' ELSE 'CLOSED' END
  ) status, 
  "location"."Status_id" "isVisible", 
  "location"."Type_id" type, 
  "location"."isMobile" "isMobile", 
  "location"."IsMainBranch" "IsMainBranch", 
  (
    SELECT 
      LISTAGG("service"."Name", '||') 
    FROM 
      "service" 
      JOIN "locationservice" ON (
        (
          "service"."id" = "locationservice"."Service_id"
        ) 
        AND (
          "locationservice"."Location_id" = "location"."id"
        )
      )
  ) "services", 
  (
    SELECT 
      LISTAGG("currency"."code", ',') 
    FROM 
      "currency" 
      JOIN "locationcurrency" ON (
        (
          "currency"."code" = CAST(
            "locationcurrency"."currency_code" as varchar2(30)
          )
        ) 
        AND (
          CAST(
            "locationcurrency"."Location_id" as varchar2(30)
          ) = "location"."id"
        )
      )
  ) "currencies", 
  (
    SELECT 
      LISTAGG(
        CAST(
          "customersegment"."type" as varchar2(2000)
        ), 
        ','
      ) 
    FROM 
      "customersegment" 
      JOIN "locationcustomersegment" ON (
        (
          "customersegment"."id" = CAST(
            "locationcustomersegment"."segment_id" as varchar2(30)
          )
        ) 
        AND (
          CAST(
            "locationcustomersegment"."Location_id" as varchar(30)
          ) = "location"."id"
        )
      )
  ) "segments", 
  (
    SELECT 
      LISTAGG(
        CAST(
          "facility"."id" as varchar(2000)
        ), 
        ','
      ) 
    FROM 
      "facility" 
      JOIN "locationfacility" ON (
        (
          "facility"."id" = CAST(
            "locationfacility"."facility_id" as varchar2(30)
          )
        ) 
        AND (
          CAST(
            "locationfacility"."Location_id" as varchar2(30)
          ) = "location"."id"
        )
      )
  ) "facilities_names", 
  (
    SELECT 
      LISTAGG("facility"."id", ',') 
    FROM 
      "facility" 
      JOIN "locationfacility" ON (
        (
          "facility"."id" = CAST(
            "locationfacility"."facility_id" as varchar2(30)
          )
        ) 
        AND (
          CAST(
            "locationfacility"."Location_id" as varchar2(30)
          ) = "location"."id"
        )
      )
  ) "facilities_codes", 
  (
    SELECT 
      "city"."Name" 
    FROM 
      "city" 
    WHERE 
      (
        "city"."id" = "address"."City_id"
      )
  ) "city", 
  (
    SELECT 
      "region"."Name" 
    FROM 
      "region" 
    WHERE 
      (
        "region"."id" = "address"."Region_id"
      )
  ) "region", 
  (
    SELECT 
      "country"."Name" 
    FROM 
      "country" 
    WHERE 
      (
        "country"."id" = (
          SELECT 
            "region"."Country_id" 
          FROM 
            DUAL 
          WHERE 
            (
              "region"."id" = "address"."Region_id"
            )
        )
      )
  ) "country", 
  "address"."addressLine1" "addressLine1", 
  "address"."addressLine2" "addressLine2", 
  "address"."addressLine3" "addressLine3", 
  "address"."zipCode" "zipCode", 
  "address"."latitude" "latitude", 
  "address"."logitude" "longitude" 
FROM 
  "address" 
  JOIN "location" ON "address"."id" = "location"."Address_id" 
  JOIN "region" ON "region"."id" = "address"."Region_id";



  create or replace NONEDITIONABLE PROCEDURE         "approvalmatrix_fetch_grouprecords_proc" 
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



create or replace NONEDITIONABLE PROCEDURE                                 "fetch_approvalqueue_proc"
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


create or replace NONEDITIONABLE PROCEDURE "fetch_generaltranscation_proc" (
  "_customerId" IN VARCHAR2, "_transactionId" IN VARCHAR2, 
  "_featureActionId" IN VARCHAR2, 
  "_featureactionlist" IN VARCHAR2, 
  "_queryType" IN VARCHAR2, "_filterByParam" IN VARCHAR2, 
  "_filterByValue" IN VARCHAR2, "_searchString" IN VARCHAR2, 
  "_sortByParam" IN VARCHAR2, "_sortOrder" IN VARCHAR2, 
  "_pageSize" IN VARCHAR2, "_pageOffset" IN VARCHAR2, 
  "records" OUT SYS_REFCURSOR
) AS v__filterByParam VARCHAR2(4000) := "_filterByParam";
v__filterByValue VARCHAR2(4000) := "_filterByValue";
v__transactionId VARCHAR2(50) := "_transactionId";
v__featureActionId VARCHAR2(50) := "_featureActionId";
v__sortByParam VARCHAR2(50) := "_sortByParam";
v__sortOrder VARCHAR2(50) := "_sortOrder";
BEGIN DECLARE v_customerMatrixIds CLOB;
v_alreadyApprovedIds CLOB;
v_companyId CLOB;
v_approvalRequestIds CLOB;
v_queryTypecondition VARCHAR2(4000);
v_combinedIds CLOB;
v_numOfParams NUMBER(10, 0) := 0;
v_searchQuery VARCHAR2(4000) := ' ';
v_idx NUMBER(10, 0) := 1;
v_filterParam VARCHAR2(4000) := ' ';
v_filterValue VARCHAR2(4000) := ' ';
v_paginationQuery VARCHAR2(4000);
v_features CLOB;
v_companyRequestIds CLOB;
v_customerAcounts CLOB;
v_isSelfApprovalenabled NUMBER(1, 0);
v_notCreatedBySelf VARCHAR2(4000);
v_createActions CLOB;
v_accountsQuery VARCHAR2(4000);
v_companyQuery VARCHAR2(4000);
v_select_statement VARCHAR2(4000);
v_numberOfApprovals VARCHAR2(4000) := ' ';
v_sql VARCHAR2(4000);
v_approvalCount1_4 CLOB;
v_approvalCount2_4 CLOB;
BEGIN << MainLabel >> v_sql := 'CREATE TEMPORARY TABLE tt_approvalCount1_4(
      customerCnt VARCHAR(50),
      requestId NUMBER
      )
      ON COMMIT DROP DEFINITION';
EXECUTE IMMEDIATE v_sql;
--      /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
SELECT 
  LISTAGG(
    CAST(
      "id" AS VARCHAR2(255)
    ), 
    ','
  ) INTO v_combinedIds 
FROM 
  "customer" 
WHERE 
  "combinedUserId" = '''' ||("_customerId" || '''');
IF v_combinedIds IS NULL 
OR v_combinedIds = ' ' THEN v_combinedIds := "_customerId";
ELSE v_combinedIds := "_customerId" || ',' || v_combinedIds;
END IF;
SELECT 
  "isSelfApprovalEnabled" INTO v_isSelfApprovalenabled 
FROM 
  "application";
IF (v_isSelfApprovalenabled = 0) THEN v_notCreatedBySelf := (
  ' AND "generaltransaction"."createdby" NOT IN ( SELECT column_value from TABLE(UTILS.STRING_SPLIT( ''' || v_combinedIds || ''')))'
);
ELSE v_notCreatedBySelf := ' ';
END IF;
v__filterByParam := CASE WHEN (
  v__filterByParam IS NULL 
  OR v__filterByParam = ''
) THEN ' ' ELSE v__filterByParam END;
v__filterByValue := CASE WHEN (
  v__filterByValue IS NULL 
  OR v__filterByValue = ''
) THEN ' ' ELSE v__filterByValue END;
v__transactionId := CASE WHEN (
  v__transactionId = '' 
  OR v__transactionId IS NULL
) THEN '%' ELSE v__transactionId END;
v__featureActionId := CASE WHEN (v__transactionId = '%') THEN '%' ELSE v__featureActionId END;
v_numOfParams := 0;
IF LENGTHB(v__filterByParam) > 0 THEN v_numOfParams := LENGTHB(v__filterByParam) - LENGTHB(
  REPLACE(v__filterByParam, ',', ' ')
) + 1;
END IF;
WHILE v_idx <= v_numOfParams LOOP BEGIN v_filterParam := UTILS.SUBSTRING_INDEX(
  UTILS.SUBSTRING_INDEX(v__filterByParam, ',', v_idx), 
  ',', 
  -1
);
v_filterValue := UTILS.SUBSTRING_INDEX(
  UTILS.SUBSTRING_INDEX(v__filterByValue, ',', v_idx), 
  ',', 
  -1
);
v_searchQuery := v_searchQuery || ' AND (' || v_filterParam || ' LIKE ''' || v_filterValue || ''' )';
v_idx := v_idx + 1;
END;
END LOOP;
SELECT 
  LISTAGG(
    CAST(
      (
        "contractcustomers"."contractId" || '_' || "contractcustomers"."coreCustomerId"
      ) AS VARCHAR(255)
    ), 
    ','
  ) INTO v_companyId 
FROM 
  "contractcustomers" 
WHERE 
  "contractcustomers"."customerId" = "_customerId";
IF v_companyId IS NULL THEN v_companyId := ' ';
END IF;
IF "_featureactionlist" IS NULL THEN RETURN;
END IF;
v__sortByParam := CASE WHEN v__sortByParam = '' 
OR v__sortByParam IS NULL THEN 'createdts' ELSE v__sortByParam END;
v__sortOrder := CASE WHEN v__sortOrder = '' 
OR v__sortOrder IS NULL THEN 'DESC' ELSE v__sortOrder END;
SELECT 
  LISTAGG(
    CAST(
      "approvalMatrixId" AS VARCHAR2(255)
    ), 
    ','
  ) INTO v_customerMatrixIds 
FROM 
  "customerapprovalmatrix" 
WHERE 
  "customerapprovalmatrix"."customerId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(v_combinedIds)
      )
  );
IF v_customerMatrixIds IS NULL THEN v_customerMatrixIds := ' ';
END IF;
SELECT 
  LISTAGG(
    CAST(
      "requestId" AS VARCHAR2(255)
    ), 
    ','
  ) INTO v_alreadyApprovedIds 
FROM 
  "bbactedrequest" 
WHERE 
  "bbactedrequest"."createdby" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(v_combinedIds)
      )
  ) 
  AND "bbactedrequest"."action" = 'Approved';
IF v_alreadyApprovedIds IS NULL THEN v_alreadyApprovedIds := ' ';
END IF;
SELECT 
  LISTAGG(
    CAST(
      "requestId" AS VARCHAR2(255)
    ), 
    ','
  ) INTO v_approvalRequestIds 
FROM 
  "requestapprovalmatrix" 
  JOIN "approvalmatrix" ON "requestapprovalmatrix"."approvalMatrixId" = "approvalmatrix"."id" 
  JOIN "approvalrule" ON "approvalmatrix"."approvalruleId" = "approvalrule"."id" 
WHERE 
  CAST(
    "requestapprovalmatrix"."approvalMatrixId" AS VARCHAR2(255)
  ) IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(v_customerMatrixIds)
      )
  ) 
  AND CAST(
    "requestapprovalmatrix"."requestId" AS VARCHAR2(255)
  ) NOT IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(v_alreadyApprovedIds)
      )
  ) 
  AND (
    (
      "approvalrule"."numberOfApprovals" = -1 
      AND "requestapprovalmatrix"."receivedApprovals" < (
        SELECT 
          COUNT(
            DISTINCT ("customerId")
          ) 
        FROM 
          "customerapprovalmatrix" 
        WHERE 
          "customerapprovalmatrix"."approvalMatrixId" = "requestapprovalmatrix"."approvalMatrixId"
      )
    ) 
    OR (
      "approvalrule"."numberOfApprovals" != -1 
      AND "requestapprovalmatrix"."receivedApprovals" < "approvalrule"."numberOfApprovals"
    )
  );
IF v_approvalRequestIds IS NULL THEN v_approvalRequestIds := ' ';
END IF;
v_queryTypecondition := CASE WHEN ("_queryType" = 'myRequests') THEN (
  ' AND  "generaltransaction"."createdby" IN ( SELECT column_value from TABLE(UTILS.STRING_SPLIT(''' || v_combinedIds || '''))) AND ("bbrequest"."status" = ''Pending'' OR "bbrequest"."status" = ''Approved'' OR "bbrequest"."status" = ''Rejected'' ) '
) ELSE CASE WHEN (
  "_queryType" = 'pendingForMyApprovals'
) THEN (
  ' AND CAST("generaltransaction"."requestId" as VARCHAR2(255))  IN ( SELECT column_value from TABLE(UTILS.STRING_SPLIT(''' || v_approvalRequestIds || '''))) AND "generaltransaction"."status" = ''Pending'' '
) || v_notCreatedBySelf ELSE CASE WHEN ("_queryType" = 'rejected') THEN (
  ' AND "generaltransaction"."status" = ''Rejected'' '
) ELSE CASE WHEN (v__transactionId = '%') THEN (
  ' AND NOT "generaltransaction"."status" = ''Withdrawn'''
) ELSE ' ' END END END END;
v_searchQuery := CASE WHEN v_searchQuery = '' 
OR v_searchQuery IS NULL THEN ' ' ELSE 'AND ("generaltransaction"."payeeId" LIKE ''%' || "_searchString" || '%'' OR "customeraccounts"."accountName" LIKE ''%' || "_searchString" || '%'' OR "feature"."name" LIKE ''%' || "_searchString" || '%'' OR "customer"."userName" LIKE ''%' || "_searchString" || '%'' )' END;
v_paginationQuery := CASE WHEN (
  "_pageOffset" = '' 
  OR "_pageOffset" IS NULL
) 
AND (
  "_pageSize" = '' 
  OR "_pageSize" IS NULL
) THEN ' ' ELSE ' OFFSET ' || "_pageOffset" || '  ROWS FETCH NEXT ' || "_pageSize" || ' ROWS ONLY ' END;
SELECT 
  LISTAGG(
    CAST(
      "Feature_id" AS VARCHAR2(255)
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
        UTILS.STRING_SPLIT("_featureactionlist")
      )
  );
IF v_features IS NULL THEN v_features := ' ';
END IF;
SELECT 
  LISTAGG(
    CAST(
      "id" AS VARCHAR2(255)
    ), 
    ','
  ) INTO v_createActions 
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
  AND "featureaction"."id" LIKE '%_CREATE';
IF v_createActions IS NULL THEN v_createActions := ' ';
END IF;
SELECT 
  LISTAGG(
    CAST(
      "requestId" AS VARCHAR2(255)
    ), 
    ','
  ) INTO v_companyRequestIds 
FROM 
  "bbrequest" 
WHERE 
  "bbrequest"."companyId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(v_companyId)
      )
  ) 
  AND "bbrequest"."featureActionId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(v_createActions)
      )
  );
IF v_companyRequestIds IS NULL THEN v_companyRequestIds := ' ';
END IF;
SELECT 
  LISTAGG(
    CAST(
      "Account_id" AS VARCHAR2(255)
    ), 
    ','
  ) INTO v_customerAcounts 
FROM 
  "customeraccounts" 
WHERE 
  "customeraccounts"."Customer_id" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(v_combinedIds)
      )
  );
IF v_customerAcounts IS NULL THEN v_customerAcounts := ' ';
END IF;
v_accountsQuery := CASE WHEN (
  v_companyId = '' 
  OR v_companyId IS NULL
) THEN ' ' ELSE ' AND "generaltransaction"."fromAccountNumber" IN ( SELECT column_value from TABLE(UTILS.STRING_SPLIT(''' || v_customerAcounts || ''')))' END;
v_companyQuery := CASE WHEN (
  v_companyId = '' 
  OR v_companyId IS NULL
) THEN ' ' ELSE ' AND ("generaltransaction"."companyId" IN ( SELECT column_value from TABLE(UTILS.STRING_SPLIT(''' || v_companyId || '''))) OR "generaltransaction"."createdby"IN ( SELECT column_value from TABLE(UTILS.STRING_SPLIT( ''' || v_combinedIds || ''')))) ' END;
SELECT 
  COUNT(*) INTO v_numberOfApprovals 
FROM 
  "bbrequest" 
  JOIN "requestapprovalmatrix" ON (
    "bbrequest"."requestId" = "requestapprovalmatrix"."requestId"
  ) 
  JOIN "customerapprovalmatrix" ON (
    "customerapprovalmatrix"."approvalMatrixId" = "requestapprovalmatrix"."approvalMatrixId"
  ) 
WHERE 
  CAST(
    "bbrequest"."requestId" AS VARCHAR2(255)
  ) IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(v_companyRequestIds)
      )
  );
--      DELETE FROM   tt_approvalCount1_4;
--      UTILS.IDENTITY_RESET('tt_approvalCount1_4');
v_approvalCount1_4 := 'SELECT customerCnt ,
              requestId 
          FROM ( SELECT COUNT(DISTINCT ("customerId"))  customerCnt  ,
                       "requestapprovalmatrix"."requestId" requestId  
                FROM "customerapprovalmatrix" 
                       JOIN "requestapprovalmatrix"    ON ( "customerapprovalmatrix"."approvalMatrixId" = "requestapprovalmatrix"."approvalMatrixId" )
                 WHERE  CAST("requestapprovalmatrix"."requestId" AS VARCHAR2(255)) IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT(v_companyRequestIds)))
                  GROUP BY "requestapprovalmatrix"."requestId" )';
--      DELETE FROM tt_approvalCount2_4;
--      UTILS.IDENTITY_RESET('tt_approvalCount2_4');
v_approvalCount2_4 := 'SELECT requestId ,
              totalCnt 
          FROM ( SELECT requestId ,
                       SUM(t)  totalCnt  
                FROM ( SELECT "bbrequest"."requestId" requestId  ,
                              CASE 
                                   WHEN "approvalrule"."numberOfApprovals" = -1 THEN ( SELECT COUNT(*)  
          FROM "customerapprovalmatrix" 
         WHERE  "customerapprovalmatrix"."approvalMatrixId" = "requestapprovalmatrix"."approvalMatrixId" )
                                   WHEN "approvalrule"."numberOfApprovals" IS NULL
                                     OR "approvalrule"."numberOfApprovals" = '''' THEN 0
                              ELSE "approvalrule"."numberOfApprovals"
                                 END t  
                       FROM "customerapprovalmatrix" 
                              LEFT JOIN "requestapprovalmatrix"    ON "customerapprovalmatrix"."approvalMatrixId" = "requestapprovalmatrix"."approvalMatrixId"
                              LEFT JOIN "bbrequest"    ON ( "bbrequest"."requestId" = "requestapprovalmatrix"."requestId" )
                              LEFT JOIN "approvalmatrix"    ON ( "requestapprovalmatrix"."approvalMatrixId" = "approvalmatrix"."id" )
                              LEFT JOIN "approvalrule"    ON ( "approvalmatrix".approvalruleId = "approvalrule"."id" )
                        WHERE  CAST("bbrequest"."requestId" AS VARCHAR2(255)) IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT(v_companyRequestIds))) ) approvals
                  GROUP BY requestId )';
v_select_statement := '   
         SELECT * FROM

              (SELECT 
                  "generaltransaction"."transactionId",
                  "generaltransaction"."featureActionId",
                  "feature"."name" as featureName,
                  "generaltransaction"."fromAccountNumber",
                  "generaltransaction"."amount",
                  "generaltransaction"."requestId",
                  "generaltransaction"."createdby",
                  "generaltransaction"."createdts",
                  "generaltransaction"."frequencyTypeId",
                  "generaltransaction"."status",
                  "generaltransaction"."numberOfRecurrences",
                  "generaltransaction"."payeeId",
                  "generaltransaction"."companyId",
                  "generaltransaction"."scheduledDate",
                  ( CASE 
                    WHEN "customeraccounts"."AccountName" is NULL THEN ''AccountName'' 
                            ELSE "customeraccounts"."AccountName" 
                  END ) AS accountName,
                  "customer"."UserName" AS userName,
                  "bbrequest"."createdby" AS requestCreatedby,
                  (CASE
                    WHEN "generaltransaction"."createdby"  IN ( SELECT column_value from TABLE(UTILS.STRING_SPLIT(CAST(''' || v_combinedIds || ''' as VARCHAR2(4000))))) THEN ''true''
                    ELSE ''false''
                   END) as amICreator,
                  (CASE 
                    WHEN CAST("bbrequest"."requestId" AS varchar2(255)) IN ( SELECT column_value from TABLE(UTILS.STRING_SPLIT(CAST(''' || v_approvalRequestIds || ''' as VARCHAR2(4000))))) THEN ''true''
                        ELSE ''false''
                   END)
                    as amIApprover

              FROM

              ( SELECT 
                  "billpaytransfers"."transactionId" AS transactionId,
                  "billpaytransfers"."featureActionId" AS featureActionId,
                  "billpaytransfers"."fromAccountNumber" AS fromAccountNumber,
                  "billpaytransfers"."amount" AS amount,
                  "billpaytransfers"."requestId" AS requestId,
                  "billpaytransfers"."createdby" AS createdby,
                  "billpaytransfers"."createdts" AS createdts,
                  "billpaytransfers"."frequencyTypeId" AS frequencyTypeId,
                  "billpaytransfers"."status" AS status,
                  "billpaytransfers"."numberOfRecurrences" AS numberOfRecurrences,
                              (CASE 
                    WHEN "billpaytransfers"."payeeName" IS NULL OR "billpaytransfers"."payeeName" = '''' THEN 
                    CASE
                      WHEN "billpaytransfers"."billerId" IS NULL OR "billpaytransfers"."billerId" = '''' THEN 
                        CASE 
                          WHEN "billpaytransfers"."payeeId" IS NULL OR "billpaytransfers"."payeeId" = '''' THEN "billpaytransfers"."toAccountNumber"
                          ELSE "billpaytransfers"."payeeId"
                        END
                      ELSE "billpaytransfers"."billerId"
                    END
                    ELSE "billpaytransfers"."payeeName"
                  END) 
                              AS payeeId,
                  "billpaytransfers"."companyId" AS companyId,
                          "billpaytransfers"."softdeleteflag" AS softdeleteflag,
                  "billpaytransfers"."scheduledDate" AS scheduledDate
                FROM
                  "billpaytransfers"
              UNION
                SELECT 
                  "p2ptransfers"."transactionId" AS transactionId,
                  "p2ptransfers"."featureActionId" AS featureActionId,
                  "p2ptransfers"."fromAccountNumber" AS fromAccountNumber,
                  "p2ptransfers"."amount" AS amount,
                  "p2ptransfers"."requestId" AS requestId,
                  "p2ptransfers"."createdby" AS createdby,
                  "p2ptransfers"."createdts" AS createdts,
                  "p2ptransfers"."frequencyTypeId" AS frequencyTypeId,
                  "p2ptransfers"."status" AS status,
                  "p2ptransfers"."numberOfRecurrences" AS numberOfRecurrences,
                  CASE 
                    WHEN "p2ptransfers"."payeeName" IS NULL OR "p2ptransfers"."payeeName" = '''' THEN 
                    CASE
                      WHEN "p2ptransfers"."personId" IS NULL OR "p2ptransfers"."personId" = '''' THEN 
                        CASE 
                          WHEN "p2ptransfers"."p2pContact" IS NULL OR "p2ptransfers"."p2pContact" = '''' THEN "p2ptransfers"."toAccountNumber"
                          ELSE "p2ptransfers"."p2pContact"
                        END
                      ELSE "p2ptransfers"."personId"
                    END
                    ELSE "p2ptransfers"."payeeName"
                  END 
                              AS payeeId,
                  "p2ptransfers"."companyId" AS companyId,
                          "p2ptransfers"."softdeleteflag" AS softdeleteflag,
                  "p2ptransfers"."scheduledDate" AS scheduledDate
                FROM
                  "p2ptransfers"
              UNION
                SELECT 
                  "wiretransfers"."transactionId" AS transactionId,
                  "wiretransfers"."featureActionId" AS featureActionId,
                  "wiretransfers"."fromAccountNumber" AS fromAccountNumber,
                  "wiretransfers"."amount" AS amount,
                  "wiretransfers"."requestId" AS requestId,
                  "wiretransfers"."createdby" AS createdby,
                  "wiretransfers"."createdts" AS createdts,
                  null AS frequencyTypeId,
                  "wiretransfers"."status" AS status,
                  null AS numberOfRecurrences,
                              CASE 
                    WHEN "wiretransfers"."payeeName" IS NULL OR "wiretransfers"."payeeName" = '''' THEN 
                    CASE
                      WHEN "wiretransfers"."payPersonName" IS NULL OR "wiretransfers"."payPersonName" = '''' THEN 
                        CASE 
                          WHEN "wiretransfers"."payeeId" IS NULL OR "wiretransfers"."payeeId" = '''' THEN "wiretransfers".payeeAccountNumber
                          ELSE "wiretransfers"."payeeId"
                        END
                      ELSE "wiretransfers"."payPersonName"
                    END
                    ELSE "wiretransfers"."payeeName"
                  END 
                              AS payeeId,
                  "wiretransfers"."companyId" AS companyId,
                          "wiretransfers"."softdeleteflag" AS softdeleteflag,
                  "wiretransfers"."createdts" AS scheduledDate
                FROM
                  wiretransfers
              UNION
                SELECT 
                  "intrabanktransfers"."transactionId" AS transactionId,
                  "intrabanktransfers"."featureActionId" AS featureActionId,
                  "intrabanktransfers"."fromAccountNumber" AS fromAccountNumber,
                  "intrabanktransfers"."amount" AS amount,
                  "intrabanktransfers"."requestId" AS requestId,
                  "intrabanktransfers"."createdby" AS createdby,
                  "intrabanktransfers"."createdts" AS createdts,
                  "intrabanktransfers"."frequencyTypeId" AS frequencyTypeId,
                  "intrabanktransfers"."status" AS status,
                  "intrabanktransfers"."numberOfRecurrences" AS numberOfRecurrences,
                              CASE 
                    WHEN "intrabanktransfers"."payeeName" IS NULL OR "intrabanktransfers"."payeeName" = '''' THEN 
                    CASE
                      WHEN "intrabanktransfers"."payPersonName" IS NULL OR "intrabanktransfers"."payPersonName" = '''' THEN 
                        CASE 
                          WHEN "intrabanktransfers"."personId" IS NULL OR "intrabanktransfers"."personId" = '''' THEN "intrabanktransfers".toAccountNumber
                          ELSE "intrabanktransfers"."personId"
                        END
                      ELSE "intrabanktransfers"."payPersonName"
                    END
                    ELSE "intrabanktransfers"."payeeName"
                  END 
                              AS payeeId,
                  "intrabanktransfers"."companyId" AS companyId,
                          "intrabanktransfers"."softdeleteflag" AS softdeleteflag,
                  "intrabanktransfers"."scheduledDate" AS scheduledDate
                FROM
                  intrabanktransfers
              UNION
                SELECT 
                  "interbankfundtransfers"."transactionId" AS transactionId,
                  "interbankfundtransfers"."featureActionId" AS featureActionId,
                  "interbankfundtransfers"."fromAccountNumber" AS fromAccountNumber,
                  "interbankfundtransfers"."amount" AS amount,
                  "interbankfundtransfers"."requestId" AS requestId,
                  "interbankfundtransfers"."createdby" AS createdby,
                  "interbankfundtransfers"."createdts" AS createdts,
                  "interbankfundtransfers"."frequencyTypeId" AS frequencyTypeId,
                  "interbankfundtransfers"."status" AS status,
                  "interbankfundtransfers"."numberOfRecurrences" AS numberOfRecurrences,
                  CASE 
                    WHEN "interbankfundtransfers"."payeeName" IS NULL OR "interbankfundtransfers"."payeeName" = '''' THEN 
                    CASE
                      WHEN "interbankfundtransfers"."payPersonName" IS NULL OR "interbankfundtransfers"."payPersonName" = '''' THEN 
                        CASE 
                          WHEN "interbankfundtransfers"."personId" IS NULL OR "interbankfundtransfers"."personId" = '''' THEN "interbankfundtransfers"."toAccountNumber"
                          ELSE "interbankfundtransfers"."personId"
                        END
                      ELSE "interbankfundtransfers"."payPersonName"
                    END
                    ELSE "interbankfundtransfers"."payeeName"
                  END 
                              AS payeeId,
                  "interbankfundtransfers"."companyId" AS companyId,
                          "interbankfundtransfers"."softdeleteflag" AS softdeleteflag,
                  "interbankfundtransfers"."scheduledDate" AS scheduledDate
                FROM
                  "interbankfundtransfers"
              UNION
                SELECT 
                  "internationalfundtransfers"."transactionId" AS transactionId,
                  "internationalfundtransfers"."featureActionId" AS featureActionId,
                  "internationalfundtransfers"."fromAccountNumber" AS fromAccountNumber,
                  "internationalfundtransfers"."amount" AS amount,
                  "internationalfundtransfers"."requestId" AS requestId,
                  "internationalfundtransfers"."createdby" AS createdby,
                  "internationalfundtransfers"."createdts" AS createdts,
                  "internationalfundtransfers"."frequencyTypeId" AS frequencyTypeId,
                  "internationalfundtransfers"."status" AS status,
                  "internationalfundtransfers"."numberOfRecurrences" AS numberOfRecurrences,
                  CASE 
                    WHEN "internationalfundtransfers"."payeeName" IS NULL OR "internationalfundtransfers"."payeeName" = '''' THEN 
                    CASE
                      WHEN "internationalfundtransfers"."payPersonName" IS NULL OR "internationalfundtransfers"."payPersonName" = '''' THEN 
                        CASE 
                          WHEN "internationalfundtransfers"."personId" IS NULL OR "internationalfundtransfers"."personId" = '''' THEN "internationalfundtransfers"."toAccountNumber"
                          ELSE "internationalfundtransfers"."personId"
                        END
                      ELSE "internationalfundtransfers"."payPersonName" 
                    END
                    ELSE "internationalfundtransfers"."payeeName"
                  END 
                              AS payeeId,
                  "internationalfundtransfers"."companyId" AS companyId,
                          "internationalfundtransfers"."softdeleteflag" AS softdeleteflag,
                  "internationalfundtransfers"."scheduledDate" AS scheduledDate
                FROM
                  "internationalfundtransfers"
              UNION
                SELECT 
                  "ownaccounttransfers"."transactionId" AS transactionId,
                  "ownaccounttransfers"."featureActionId" AS featureActionId,
                  "ownaccounttransfers"."fromAccountNumber" AS fromAccountNumber,
                  "ownaccounttransfers"."amount" AS amount,
                  "ownaccounttransfers"."requestId" AS requestId,
                  "ownaccounttransfers"."createdby" AS createdby,
                  "ownaccounttransfers"."createdts" AS createdts,
                  "ownaccounttransfers"."frequencyTypeId" AS frequencyTypeId,
                  "ownaccounttransfers"."status" AS status,
                  "ownaccounttransfers"."numberOfRecurrences" AS numberOfRecurrences,
                  CASE 
                    WHEN "ownaccounttransfers"."payeeName" IS NULL OR "ownaccounttransfers"."payeeName" = '''' THEN 
                    CASE
                      WHEN "ownaccounttransfers"."payPersonName" IS NULL OR "ownaccounttransfers"."payPersonName" = '''' THEN 
                        CASE 
                          WHEN "ownaccounttransfers"."personId" IS NULL OR "ownaccounttransfers"."personId" = '''' THEN "ownaccounttransfers".toAccountNumber
                          ELSE "ownaccounttransfers"."personId"
                        END
                      ELSE "ownaccounttransfers"."payPersonName"
                    END
                    ELSE "ownaccounttransfers"."payeeName"
                  END 
                              AS payeeId,
                  "ownaccounttransfers"."companyId" AS companyId,
                          "ownaccounttransfers"."softdeleteflag" AS softdeleteflag,
                  "ownaccounttransfers"."scheduledDate" AS scheduledDate
                FROM
                  "ownaccounttransfers"
              ) AS generaltransaction
                  LEFT JOIN "customer" ON ("generaltransaction"."createdby" = "customer".id)
                  LEFT JOIN "customeraccounts" ON (("generaltransaction"."createdby" = "customeraccounts"."Customer_id")
                  AND ("generaltransaction"."fromAccountNumber" = "customeraccounts"."Account_id"))
                  LEFT JOIN "bbrequest"ON ("generaltransaction"."requestId" = "bbrequest"."requestId")
                  LEFT JOIN "featureaction" ON ("generaltransaction"."featureActionId" = "featureaction"."id")
                      LEFT JOIN "feature" ON ("featureaction"."Feature_id" = "feature"."id")

                WHERE "generaltransaction"."softdeleteflag = 0
              ' || v_companyQuery || '
               AND "generaltransaction"."transactionId LIKE ''' || v__transactionId || ''' AND "generaltransaction"."featureActionId LIKE ''' || v__featureActionId || ''' ' || v_accountsQuery || ' AND "generaltransaction"."featureActionId" IN ( SELECT column_value from TABLE(UTILS.STRING_SPLIT(CAST(''' || v_createActions || ''' as VARCHAR2(4000))))) ' || v_queryTypecondition || ' ' || v_searchQuery || ' ) AS t1
                  INNER JOIN
                  ( 
              SELECT 
                "bbrequest"."requestId",
                     (select count(DISTINCT("bbactedrequest"."createdby")) from "bbactedrequest" where "bbactedrequest"."action" = ''Approved'' AND  "bbactedrequest"."requestId" = "bbrequest"."requestId") 
                           as receivedApprovals,
                          (LEASTINT((select customerCnt from (v_approvalCount1_4) where "requestid"="bbrequest"."requestId" ),(select totalCnt from (v_approvalCount2_4) where requestid="bbrequest"."requestId" ))) as requiredApprovals
              FROM
               "bbrequest"
              LEFT JOIN "requestapprovalmatrix" ON ("bbrequest"."requestId" = "requestapprovalmatrix"."requestId")
              LEFT JOIN "approvalmatrix" ON ("requestapprovalmatrix"."approvalMatrixId" = "approvalmatrix"."id")
              LEFT JOIN "approvalrule" ON ("approvalmatrix"."approvalruleId" = "approvalrule"."id")
                      WHERE (CAST("bbrequest"."requestId" as VARCHAR2(255)) IN ( SELECT column_value from TABLE(UTILS.STRING_SPLIT(CAST(''' || v_companyRequestIds || ''' as VARCHAR2(4000)))))
              GROUP BY "bbrequest"."requestId"
                  ) AS t2
                  ON 
                  t1.requestId = t2.requestId
                  ORDER BY ' || v__sortByParam || ' ' || v__sortOrder || ' ' || v_paginationQuery;
OPEN "records" FOR v_select_statement;
--      EXECUTE IMMEDIATE ' TRUNCATE TABLE tt_approvalCount1_4 ';
--      EXECUTE IMMEDIATE ' TRUNCATE TABLE tt_approvalCount2_4 ';
END;
--   <<MAINLABEL$leave>>
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/




create or replace NONEDITIONABLE PROCEDURE "fetch_signatorygroup_for_customer_and_request_proc" (
  "_requestId" IN VARCHAR2, "_customerId" IN VARCHAR2, 
  "records" OUT SYS_REFCURSOR
) AS v_signatoryGroupsList CLOB;
v_signatoryGroupsQuery VARCHAR2(2048);
BEGIN --   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
SELECT 
  LISTAGG(
    REPLACE(
      (
        REPLACE(
          REPLACE("groupList", ']', ''''), 
          '[', 
          ''''
        )
      ), 
      ',', 
      ''','''
    ), 
    ','
  ) INTO v_signatoryGroupsList 
FROM 
  "signatorygroupmatrix" 
WHERE 
  "approvalMatrixId" IN (
    SELECT 
      "approvalMatrixId" 
    FROM 
      "requestapprovalmatrix" 
    WHERE 
      "requestId" = "_requestId"
  );
v_signatoryGroupsQuery := (
  'SELECT 
                                                        csg."customerSignatoryGroupId",
                                                        csg."signatoryGroupId",
                                                        sg."signatoryGroupName",
                                                        csg."customerId",
                                                        csg."createdby",
                                                        csg."createdts",
                                                        csg."modifiedby",
                                                        csg."lastmodifiedts",
                                                        csg."synctimestamp",
                                                        csg."softdeleteflag" FROM 
                                                        "customersignatorygroup" csg 
                                                        INNER JOIN "signatorygroup" sg 
                                                        ON csg."signatoryGroupId" = sg."signatoryGroupId" 
                                                            WHERE csg."signatoryGroupId" IN (' || v_signatoryGroupsList || ') 
                                                            AND csg."softdeleteflag" = 0 
                                                            AND sg."softdeleteflag" = 0 
                                                            AND csg."customerId" = ''' || "_customerId" || ''''
);
EXECUTE IMMEDIATE v_signatoryGroupsQuery;
OPEN "records" FOR v_signatoryGroupsQuery;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/



create or replace NONEDITIONABLE PROCEDURE                         "location_search_proc"
(
  "_searchKeyword" IN VARCHAR2, "records" OUT SYS_REFCURSOR
) AS v__searchKeyword VARCHAR2(1000) := "_searchKeyword";
v__next VARCHAR2(4000) := NULL;
v__nextlen NUMBER(10, 0) := NULL;
v__value VARCHAR2(4000) := NULL;
v_sql1 VARCHAR2(4000);
v_sqlFrontPart VARCHAR2(4000);
v_sqlresult VARCHAR2(4000) := NULL;
v_counter NUMBER(10, 0);
BEGIN --   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
v_sqlFrontPart := 'SELECT * FROM "locationdetails_view" where ';
v_counter := 1;
WHILE (1 = 1) LOOP DECLARE v_var VARCHAR2(4000) := NULL;
BEGIN IF LENGTHB(
  LTRIM(
    RTRIM(v__searchKeyword)
  )
) = 0 
OR v__searchKeyword IS NULL THEN EXIT;
END IF;
v__next := UTILS.SUBSTRING_INDEX(v__searchKeyword, ',', 1);
v__nextlen := LENGTHB(v__next);
v__value := LOWER(
  LTRIM(
    RTRIM(v__next)
  )
);
v_sql1 := NULL;
v_var := NULL;
v_var := REPLACE(
  LTRIM(
    RTRIM(v__value)
  ), 
  ' ', 
  '%'
);
v_sql1 := ('( LOWER("city") like (''') || ('%') || (v_var) || ('%') || 
(''')  or LOWER("informationTitle") like (''') || ('%') || (v_var) || ('%') || 
(''')  or LOWER("Name") like (''') || ('%') || (v_var) || ('%') || 
(''')  or LOWER("Description") like (''') || ('%') || (v_var) || ('%') || 
(''')  or LOWER("Code") like (''') || ('%') || (v_var) || ('%') || 
(''')  or LOWER("phoneNumber") like (''') || ('%') || (v_var) || ('%') || 
(''')  or LOWER("email") like (''') || ('%') || (v_var) || ('%') || 
(''')  or LOWER("status") like (''') || ('%') || (v_var) || ('%') || 
(''')  or LOWER("type") like (''') || ('%') || (v_var) || ('%') || 
(''')  or LOWER("services") like (''') || ('%') || (v_var) || ('%') || 
(''')  or LOWER("region") like (''') || ('%') || (v_var) || ('%') || 
(''')  or LOWER("addressLine1") like (''') || ('%') || (v_var) || ('%') || 
(''') or LOWER("addressLine2") like (''') || ('%') || (v_var) || ('%') || 
(''')  or LOWER("addressLine3") like (''') || ('%') || (v_var) || ('%') || 
(''')  or LOWER("country") like (''') || ('%') || (v_var) || ('%') || 
(''')  or LOWER("zipCode") like (''') || ('%') || (v_var) || ('%') || ('''))');
IF v_counter = 1 THEN v_sqlresult := v_sql1;
ELSE v_sqlresult := (v_sql1) || (' AND ') || (v_sqlresult);
END IF;
v_counter := v_counter + 1;
v__searchKeyword := REPLACE(
  v__searchKeyword, 
  SUBSTR(
    v__searchKeyword, 1, v__nextlen + 1
  ), 
  ''
);
END;
END LOOP;
v_sqlresult := ('(') || (v_sqlFrontPart) || (v_sqlresult) || (')');
EXECUTE IMMEDIATE v_sqlresult;
OPEN "records" FOR v_sqlresult;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
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



create or replace NONEDITIONABLE PROCEDURE "update_transaction_status_proc" (
  "_featureId" IN VARCHAR2, "_status" IN VARCHAR2, 
  "_confirmationNumber" IN VARCHAR2, 
  "records" OUT SYS_REFCURSOR
) AS v_isSuccess VARCHAR2(50);
BEGIN v_isSuccess := 'true';
IF ("_featureId" = 'ACH_COLLECTION') THEN 
UPDATE 
  "achtransaction" 
SET 
  "status" = "_status" 
WHERE 
  "confirmationNumber" = "_confirmationNumber";
ELSE IF ("_featureId" = 'ACH_PAYMENT') THEN 
UPDATE 
  "achtransaction" 
SET 
  "status" = "_status" 
WHERE 
  "confirmationNumber" = "_confirmationNumber";
ELSE IF ("_featureId" = 'ACH_FILES') THEN 
UPDATE 
  "achfile" 
SET 
  "status" = "_status" 
WHERE 
  "confirmationNumber" = "_confirmationNumber";
ELSE IF ("_featureId" = 'BILL_PAY') THEN 
UPDATE 
  "billpaytransfers" 
SET 
  "status" = "_status" 
WHERE 
  "confirmationNumber" = "_confirmationNumber";
ELSE IF (
  "_featureId" = 'DOMESTIC_WIRE_TRANSFER'
) THEN 
UPDATE 
  "wiretransfers" 
SET 
  "status" = "_status" 
WHERE 
  "confirmationNumber" = "_confirmationNumber";
ELSE IF (
  "_featureId" = 'INTERNATIONAL_WIRE_TRANSFER'
) THEN 
UPDATE 
  "wiretransfers" 
SET 
  "status" = "_status" 
WHERE 
  "confirmationNumber" = "_confirmationNumber";
ELSE IF (
  "_featureId" = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER'
) THEN 
UPDATE 
  "internationalfundtransfers" 
SET 
  "status" = "_status" 
WHERE 
  "confirmationNumber" = "_confirmationNumber";
ELSE IF (
  "_featureId" = 'INTER_BANK_ACCOUNT_FUND_TRANSFER'
) THEN 
UPDATE 
  "interbankfundtransfers" 
SET 
  "status" = "_status" 
WHERE 
  "confirmationNumber" = "_confirmationNumber";
ELSE IF (
  "_featureId" = 'INTRA_BANK_FUND_TRANSFER'
) THEN 
UPDATE 
  "intrabanktransfers" 
SET 
  "status" = "_status" 
WHERE 
  "confirmationNumber" = "_confirmationNumber";
ELSE IF ("_featureId" = 'P2P') THEN 
UPDATE 
  "p2ptransfers" 
SET 
  "status" = "_status" 
WHERE 
  "confirmationNumber" = "_confirmationNumber";
ELSE IF (
  "_featureId" = 'TRANSFER_BETWEEN_OWN_ACCOUNT'
) THEN 
UPDATE 
  "ownaccounttransfers" 
SET 
  "status" = "_status" 
WHERE 
  "confirmationNumber" = "_confirmationNumber";
ELSE v_isSuccess := 'false';
END IF;
END IF;
END IF;
END IF;
END IF;
END IF;
END IF;
END IF;
END IF;
END IF;
END IF;
OPEN "records" FOR 
SELECT 
  v_isSuccess "isSuccess"
FROM 
  DUAL;
--DBMS_SQL.RETURN_RESULT(v_cursor);
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/



create or replace NONEDITIONABLE PROCEDURE "getCustomerCommunicationData" 
(
  "_customers" IN NVARCHAR2,
  "records" out SYS_REFCURSOR
)
AS
   

BEGIN

   
   OPEN  "records" FOR
      SELECT "customercommunication"."Customer_id" "custid"  ,
             "customercommunication"."Value" "value"  ,
             "customercommunication"."Type_id" "type" 
        FROM "customercommunication" 
       WHERE  "customercommunication"."isPrimary" = 1
                AND FIND_IN_SET("customercommunication"."Customer_id", "_customers") <> 0 ;
      


END;
/



create or replace NONEDITIONABLE PROCEDURE "getCustomerInfo" 
(
  "_customers" IN NVARCHAR2,"records" out SYS_REFCURSOR
)
AS
   

BEGIN

   OPEN  "records" FOR
      SELECT "customer"."id" "custid"  ,
             "customer"."FirstName" "fname"  ,
             "customer"."LastName" "lname"  ,
             "customer"."CountryCode" "country"  
        FROM "customer"
       WHERE  FIND_IN_SET("customer"."id", "_customers") <> 0 ;
     


END;
/



create or replace NONEDITIONABLE PROCEDURE "getCustomersIdFromCoreId" 
(
  "_corecustomers" IN NVARCHAR2,
  "records" out SYS_REFCURSOR
)
AS
   

BEGIN

   OPEN  "records" FOR
      SELECT "backendidentifier"."Customer_id" "custid"  ,
             "backendidentifier"."BackendId" "corecustid"  
        FROM "backendidentifier"
       WHERE  FIND_IN_SET("backendidentifier"."BackendId", "_corecustomers") <> 0 ;
      


END;
/


create or replace NONEDITIONABLE PROCEDURE "customeraction_save_proc"
(
  "_queryInput" IN long
)
AS
   iv_queryInput long := "_queryInput";
   v_recordRow long;
   v_recordsData long;
   v_query long;
   CURSOR actions

      IS SELECT(regexp_substr(iv_queryInput, '[^|]+',1,level)) from dual
    connect by regexp_substr(iv_queryInput, '[^|]+', 1, level) is not null;


BEGIN


   iv_queryInput := REPLACE(iv_queryInput, '\', ' ') ;
   iv_queryInput := REPLACE(iv_queryInput, '"', '''') ;
   OPEN actions;
   FETCH actions INTO v_recordRow;
   <<loop_1>>

   WHILE (actions%FOUND)
   LOOP 

      BEGIN
         v_recordsData := '''' || CAST(SYS_GUID() AS NVARCHAR2) || ''',' || v_recordRow ;
         v_query := ('INSERT INTO "customeraction"("id","RoleType_id","Customer_id","coreCustomerId","contractId","featureId","Action_id","Account_id","isAllowed","limitGroupId","LimitType_id","value","companyLegalUnit") VALUES (') || (v_recordsData) || (')') ;
         EXECUTE IMMEDIATE v_query;
         FETCH actions INTO v_recordRow;
         GOTO loop_1;

      END;
   END LOOP;

END;
/



create or replace NONEDITIONABLE PROCEDURE         "fetch_request_history_proc" (
  "_customerId" IN VARCHAR2, "_requestId" IN VARCHAR2, 
  "records" OUT SYS_REFCURSOR
) AS v_companyId CLOB;
BEGIN --   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
SELECT 
  LISTAGG(
    CAST(
      (
        "contractcustomers"."contractId" || '_' || "contractcustomers"."coreCustomerId"
      ) AS VARCHAR2(255)
    ), 
    ','
  ) INTO v_companyId 
FROM 
  "contractcustomers" 
WHERE 
  "contractcustomers"."customerId" = "_customerId";
IF v_companyId IS NULL THEN v_companyId := ' ';
END IF;
OPEN "records" FOR 
SELECT 
  "bbactedrequest"."approvalId" approvalId, 
  "bbactedrequest"."requestId" requestId, 
  "bbactedrequest"."companyId" companyId, 
  "bbactedrequest"."createdby" requestActedby, 
  "bbactedrequest"."status" STATUS, 
  "bbactedrequest"."comments" comments, 
  "bbactedrequest"."action" ACTION, 
  "bbactedrequest"."groupName" groupName, 
  "bbactedrequest"."createdts" actionts, 
  "bbactedrequest"."softdeleteflag" softdeleteflag, 
  "customer"."UserName" userName, 
  CASE WHEN cast(
    "bbactedrequest"."createdby" as varchar2(255)
  ) IS NULL THEN cast(
    'System' as nvarchar2(255)
  ) ELSE (
    CASE WHEN (
      LENGTHB("customer"."FirstName") <> 0
    ) THEN "customer"."FirstName" ELSE cast(
      '' as nvarchar2(255)
    ) END
  ) || ' ' || (
    CASE WHEN (
      LENGTHB("customer"."MiddleName") <> 0
    ) THEN "customer"."MiddleName" ELSE cast(
      '' as nvarchar2(255)
    ) END
  ) || ' ' || (
    CASE WHEN (
      LENGTHB("customer"."LastName") <> 0
    ) THEN "customer"."LastName" ELSE cast(
      '' as nvarchar2(255)
    ) END
  ) END customerName, 
  "customer"."FullName" customerFullName 
FROM 
  (
    "bbactedrequest" 
    LEFT JOIN "customer" ON (
      "bbactedrequest"."createdby" = "customer"."id"
    )
  ) 
WHERE 
  CAST(
    "bbactedrequest"."requestId" AS VARCHAR2(255)
  ) = "_requestId" 
  AND "bbactedrequest"."companyId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(v_companyId)
      )
  );
--DBMS_SQL.RETURN_RESULT(v_cursor);
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/