CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "servicedefinition_delete_proc" (
  "_servicedefinitionid" IN VARCHAR2
) AS BEGIN DELETE "groupservicedefinition" 
WHERE 
  "serviceDefinitionId" = "_servicedefinitionid";
DELETE "servicedefinitionactionlimit" 
WHERE 
  "serviceDefinitionId" = "_servicedefinitionid";
DELETE "userroleservicedefinition" 
WHERE 
  "servicedefinitionId" = "_servicedefinitionid";
DELETE "servicedefinition" 
WHERE 
  "id" = v__servicedefinitionid;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/


create or replace NONEDITIONABLE PROCEDURE          "fetch_request_history_proc" (
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