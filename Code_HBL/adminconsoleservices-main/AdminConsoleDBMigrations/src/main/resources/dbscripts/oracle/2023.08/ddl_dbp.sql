create or replace PROCEDURE "user_contracts_proc"
(
  "_customerId" IN NVARCHAR2,
  "_contractId" IN NVARCHAR2,
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
    WHERE "contractaccounts"."statusDesc" != 'CLOSED' AND "contractaccounts"."coreCustomerId" IN 
	(SELECT "contractcustomers"."coreCustomerId" FROM "contractcustomers" WHERE "contractcustomers"."customerId" = "_customerId"));
	
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

   IF ( "_contractId"  is not null ) THEN
    
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
      v_select_statement := v_select_statement || ' "contractcustomers"."contractId" = ' || '''' || "_contractId" || '''' ;
   
   END;
   END IF;
   v_select_statement := CONCAT(v_select_statement, ' and `contractcustomers`.`contractId` in (' || '''' || v_filtered_contracts || '''' ||' ))') ;
   open "records" for v_select_statement;


END;
/



CREATE OR REPLACE PROCEDURE "user_contracts_withaccounts_proc" (
    "_customerId"     IN NVARCHAR2,
    "_contractId" IN NVARCHAR2,
    "_legalEntityId" IN NVARCHAR2,
    "records"         OUT SYS_REFCURSOR
) AS
    v_select_statement LONG;
    v_iswhereappened   NVARCHAR2(100);
    v_shouldandappend  NVARCHAR2(100);
BEGIN
    v_select_statement := '(SELECT 
        "contractcustomers"."customerId" AS "customerId",
        "contractcustomers"."companyLegalUnit" AS "companyLegalUnit",
        "contractcustomers"."coreCustomerId" AS "coreCustomerId",
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
        "membergroup"."Name" AS "userRole",
        "contractaccounts"."accountId",
        "contractaccounts"."accountName",
        "contractaccounts"."statusDesc",
        "contractaccounts"."typeId"
    FROM
        (("contractcustomers"
        LEFT JOIN "contract" ON ("contract"."id" = "contractcustomers"."contractId")
        LEFT JOIN "contractcorecustomers" ON (("contractcorecustomers"."contractId" = "contractcustomers"."contractId")
        AND ("contractcorecustomers"."coreCustomerId" = "contractcustomers"."coreCustomerId"))
        LEFT JOIN "contractaccounts" ON (("contractaccounts"."contractId" = "contractcustomers"."contractId")
        AND ("contractaccounts"."coreCustomerId" = "contractcustomers"."coreCustomerId"))
        LEFT JOIN "servicedefinition" ON ("servicedefinition"."id" = "contract"."servicedefinitionId")
        LEFT JOIN "membergrouptype" ON ("membergrouptype"."id" = "servicedefinition"."serviceType")
        LEFT JOIN "customergroup" ON ((("customergroup"."Customer_id" = "contractcustomers"."customerId")
        AND ("customergroup"."contractId" = "contractcustomers"."contractId")
        AND ("customergroup"."coreCustomerId" = "contractcustomers"."coreCustomerId"))))
        LEFT JOIN "membergroup" ON (("membergroup"."id" = "customergroup"."Group_id")))';
    v_iswhereappened := 'false';
    v_shouldandappend := 'false';
    IF ( "_customerId" != '' ) THEN
        BEGIN
            IF ( v_iswhereappened = 'false' ) THEN
                BEGIN
                    v_select_statement := concat(v_select_statement, ' where');
                    v_iswhereappened := 'true';
                    v_shouldandappend := 'true';
                END;
            END IF;

            v_select_statement := v_select_statement
                                  || ' "contractcustomers"."customerId" = '
                                  || ''''
                                  || "_customerId"
                                  || '''';
        END;
    END IF;

    IF ( "_legalEntityId" IS NOT NULL ) THEN
        BEGIN
            IF ( v_iswhereappened = 'false' ) THEN
                BEGIN
                    v_select_statement := concat(v_select_statement, ' where');
                    v_shouldandappend := 'true';
                END;
            END IF;

            IF (
                v_iswhereappened = 'true'
                AND v_shouldandappend = 'true'
            ) THEN
                BEGIN
                    v_select_statement := concat(v_select_statement, ' and');
                    v_shouldandappend := 'true';
                END;
            END IF;

            v_select_statement := v_select_statement
                                  || ' "contractcustomers"."companyLegalUnit" = '
                                  || ''''
                                  || "_legalEntityId"
                                  || '''';
        END;
    END IF;


    IF ( "_contractId" IS NOT NULL ) THEN
        BEGIN
            IF ( v_iswhereappened = 'false' ) THEN
                BEGIN
                    v_select_statement := concat(v_select_statement, ' where');
                    v_shouldandappend := 'true';
                END;
            END IF;

            IF (
                v_iswhereappened = 'true'
                AND v_shouldandappend = 'true'
            ) THEN
                BEGIN
                    v_select_statement := concat(v_select_statement, ' and');
                    v_shouldandappend := 'true';
                END;
            END IF;

            v_select_statement := v_select_statement
                                  || ' "contractcustomers"."contractId" = '
                                  || ''''
                                  || "_contractId"
                                  || '''';
        END;
    END IF;

    v_select_statement := concat(v_select_statement, ')');
    OPEN "records" FOR v_select_statement;

END;
/

