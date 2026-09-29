CREATE OR REPLACE VIEW "makercheckerconfig_view" AS
    SELECT 
        "mcconfig"."id" AS "id",
	    "mcmodule"."name" AS "moduleName",
        "mcaction"."name" AS "actionName",
	    "mcaction"."language_code" AS "languageCode",
	    "mcconfig"."isApprovalRequired" AS "isApprovalRequired",
	    "mcconfig"."companyLegalUnit" AS "companyLegalUnit"
    FROM
        (
          "makercheckerconfig" "mcconfig" JOIN "mcmoduletext" "mcmodule"
          ON ("mcconfig"."module" = "mcmodule"."id") 
          JOIN "mcactiontext" "mcaction"
          ON ("mcconfig"."action" = "mcaction"."id" AND "mcmodule"."language_code" = "mcaction"."language_code")
	    );
ALTER TABLE "makercheckerconfig" ADD ("auditModule" VARCHAR(50) DEFAULT NULL, "auditEvent" VARCHAR(50) DEFAULT NULL);

CREATE OR REPLACE NONEDITIONABLE PROCEDURE "update_approvalrequests_proc"(
_status VARCHAR2(50),
_reason CLOB,
_requestId VARCHAR2(50),
_currentStatus VARCHAR2(50),
_checkedBy VARCHAR2(50)
)
BEGIN
update "dbxdb"."approvalrequests" set "status"=_status, "reason"=_reason, "checkedBy"=_checkedBy where "requestId"=_requestId and "status" = _currentStatus;
select SQL%ROWCOUNT as result;
END;


ALTER TABLE "makercheckerconfig" ADD ("excludedParams" VARCHAR(200) DEFAULT NULL);
ALTER TABLE "externalaccount" ADD ("payeeVerification" VARCHAR(45) DEFAULT NULL);

CREATE OR REPLACE PROCEDURE fetch_maker_pending_requests_proc(
    _legalEntityId IN VARCHAR2,
    _module IN VARCHAR2,
    _action IN VARCHAR2,
    _userName IN VARCHAR2,
    _submittedDate IN DATE,
    _pageOffset IN INT,
    _pageSize IN INT
)
IS
    userId_Id VARCHAR2(50);
    LegalEntity_Id VARCHAR2(1024);
    Request_Module VARCHAR2(1024);
    Request_Action VARCHAR2(1024);
    submittedDate DATE;
    value VARCHAR2(50);
    sql_query VARCHAR2(4000);
    sql_query_count VARCHAR2(4000);
BEGIN
    DROP TABLE temp_maker_requests_filters;
    CREATE GLOBAL TEMPORARY TABLE temp_maker_requests_filters (
        LegalEntityId VARCHAR2(50),
        ModuleName VARCHAR2(255),
        ActionName VARCHAR2(255)
    ) ON COMMIT PRESERVE ROWS;

    userId_Id := _userName;
    LegalEntity_Id := _legalEntityId;
    Request_Module := _module;
    Request_Action := _action;
    submittedDate := _submittedDate;

    WHILE LENGTH(LegalEntity_Id) > 0 LOOP
        value := TRIM(SUBSTR(LegalEntity_Id, 1, INSTR(LegalEntity_Id, ',') - 1));
        LegalEntity_Id := TRIM(SUBSTR(LegalEntity_Id, INSTR(LegalEntity_Id, ',') + 1));
        INSERT INTO temp_maker_requests_filters (LegalEntityId) VALUES (value);
    END LOOP;

    IF Request_Module IS NOT NULL THEN
        WHILE LENGTH(Request_Module) > 0 LOOP
            value := TRIM(SUBSTR(Request_Module, 1, INSTR(Request_Module, ',') - 1));
            Request_Module := TRIM(SUBSTR(Request_Module, INSTR(Request_Module, ',') + 1));
            INSERT INTO temp_maker_requests_filters (ModuleName) VALUES (value);
        END LOOP;
    END IF;

    IF Request_Action IS NOT NULL THEN
        WHILE LENGTH(Request_Action) > 0 LOOP
            value := TRIM(SUBSTR(Request_Action, 1, INSTR(Request_Action, ',') - 1));
            Request_Action := TRIM(SUBSTR(Request_Action, INSTR(Request_Action, ',') + 1));
            INSERT INTO temp_maker_requests_filters (ActionName) VALUES (value);
        END LOOP;
    END IF;

    sql_query_count := '
        SELECT count(*) AS totalRecords
        FROM approvalrequests 
        WHERE status = ''SID_PENDING'' AND createdby = ''' || userId_Id || ''' 
        AND (';

    SELECT LISTAGG('INSTR(companyLegalUnit, ''' || LegalEntityId || ''') > 0', ' OR ') WITHIN GROUP (ORDER BY LegalEntityId)
    INTO sql_query_count
    FROM temp_maker_requests_filters;

    sql_query_count := sql_query_count || ')';

    sql_query := '
        SELECT requestId, module, action, createdts, createdby, companyLegalUnit
        FROM approvalrequests 
        WHERE status = ''SID_PENDING'' AND createdby = ''' || userId_Id || ''' 
        AND (';

    SELECT LISTAGG('INSTR(companyLegalUnit, ''' || LegalEntityId || ''') > 0', ' OR ') WITHIN GROUP (ORDER BY LegalEntityId)
    INTO sql_query
    FROM temp_maker_requests_filters;

    sql_query := sql_query || ')';

    IF Request_Module IS NOT NULL THEN
        sql_query := sql_query || '
            AND (';
        SELECT LISTAGG('INSTR(module, ''' || ModuleName || ''') > 0', ' OR ') WITHIN GROUP (ORDER BY ModuleName)
        INTO sql_query
        FROM temp_maker_requests_filters;
        sql_query := sql_query || ')';
		
		sql_query_count := sql_query_count || '
            AND (';
        SELECT LISTAGG('INSTR(module, ''' || ModuleName || ''') > 0', ' OR ') WITHIN GROUP (ORDER BY ModuleName)
        INTO sql_query_count
        FROM temp_maker_requests_filters;
        sql_query_count := sql_query_count || ')';
    END IF;

    IF Request_Action IS NOT NULL THEN
        sql_query := sql_query || '
            AND (';
        SELECT LISTAGG('INSTR(action, ''' || ActionName || ''') > 0', ' OR ') WITHIN GROUP (ORDER BY ActionName)
        INTO sql_query
        FROM temp_maker_requests_filters;
        sql_query := sql_query || ')';
		
		sql_query_count := sql_query_count || '
            AND (';
        SELECT LISTAGG('INSTR(action, ''' || ActionName || ''') > 0', ' OR ') WITHIN GROUP (ORDER BY ActionName)
        INTO sql_query_count
        FROM temp_maker_requests_filters;
        sql_query_count := sql_query_count || ')';
    END IF;

    IF submittedDate IS NOT NULL THEN
        sql_query := sql_query || '
            AND TRUNC(createdts) = TO_DATE(''' || TO_CHAR(submittedDate, 'YYYY-MM-DD') || ''', ''YYYY-MM-DD'')';
        sql_query_count := sql_query_count || '
            AND TRUNC(createdts) = TO_DATE(''' || TO_CHAR(submittedDate, 'YYYY-MM-DD') || ''', ''YYYY-MM-DD'')';
    END IF;

    sql_query := sql_query || ' ORDER BY createdts ASC OFFSET ' || _pageOffset || ' ROWS FETCH NEXT ' || _pageSize || ' ROWS ONLY';

    EXECUTE IMMEDIATE sql_query_count INTO totalRecords;
    DBMS_OUTPUT.PUT_LINE('Total Records: ' || totalRecords);
    
    EXECUTE IMMEDIATE sql_query;
    
    DROP TABLE temp_maker_requests_filters;
END;



CREATE OR REPLACE PROCEDURE get_checkerpending_requests_proc (
    _legalEntityId VARCHAR2,
    _module VARCHAR2,
    _action VARCHAR2,
    _userName VARCHAR2,
    _submittedDate DATE,
    _pageOffset NUMBER,
    _pageSize NUMBER
) AS
    userId_Id VARCHAR2(50);
    LegalEntity_Id VARCHAR2(1024);
    Request_Module VARCHAR2(1024);
    Request_Action VARCHAR2(1024);
    submittedDate DATE;
    value VARCHAR2(50);
    sql_query CLOB;
BEGIN
    userId_Id := _userName;
    LegalEntity_Id := _legalEntityId;
    Request_Module := _module;
    Request_Action := _action;
    submittedDate := _submittedDate;

    FOR i IN 1 .. LENGTH(LegalEntity_Id) LOOP
        value := TRIM(SUBSTR(LegalEntity_Id, 1, INSTR(LegalEntity_Id, ',') - 1));
        LegalEntity_Id := TRIM(SUBSTR(LegalEntity_Id, INSTR(LegalEntity_Id, ',') + 1));
        INSERT INTO temp_checker_requests_filters (LegalEntityId) VALUES (value);
    END LOOP;
    
    IF Request_Module IS NOT NULL THEN
        FOR i IN 1 .. LENGTH(Request_Module) LOOP
            value := TRIM(SUBSTR(Request_Module, 1, INSTR(Request_Module, ',') - 1));
            Request_Module := TRIM(SUBSTR(Request_Module, INSTR(Request_Module, ',') + 1));
            INSERT INTO temp_checker_requests_filters (ModuleName) VALUES (value);
        END LOOP;
    END IF;

    IF Request_Action IS NOT NULL THEN
        FOR i IN 1 .. LENGTH(Request_Action) LOOP
            value := TRIM(SUBSTR(Request_Action, 1, INSTR(Request_Action, ',') - 1));
            Request_Action := TRIM(SUBSTR(Request_Action, INSTR(Request_Action, ',') + 1));
            INSERT INTO temp_checker_requests_filters (ActionName) VALUES (value);
        END LOOP;
    END IF;

    sql_query := '
        SELECT requestId, module, action, permissionName, createdts, createdby, companyLegalUnit
        FROM approvalrequests
        WHERE status = ''SID_PENDING'' AND createdby != ''' || userId_Id || ''' AND (';
    
    SELECT LISTAGG('INSTR(companyLegalUnit, ''' || LegalEntityId || ''') > 0', ' OR ') WITHIN GROUP (ORDER BY LegalEntityId)
    INTO sql_query
    FROM temp_checker_requests_filters;
	
	sql_query_count := '
        SELECT count(*) As totalRecords
        FROM approvalrequests
        WHERE status = ''SID_PENDING'' AND createdby != ''' || userId_Id || ''' AND (';
    
    SELECT LISTAGG('INSTR(companyLegalUnit, ''' || LegalEntityId || ''') > 0', ' OR ') WITHIN GROUP (ORDER BY LegalEntityId)
    INTO sql_query_count
    FROM temp_checker_requests_filters;

    sql_query := sql_query || ')';

    IF Request_Module IS NOT NULL THEN
        sql_query := sql_query || '
            AND (';
        SELECT LISTAGG('INSTR(module, ''' || ModuleName || ''') > 0', ' OR ') WITHIN GROUP (ORDER BY ModuleName)
        INTO sql_query
        FROM temp_checker_requests_filters;
        sql_query := sql_query || ')';
		
		sql_query_count := sql_query_count || '
            AND (';
        SELECT LISTAGG('INSTR(module, ''' || ModuleName || ''') > 0', ' OR ') WITHIN GROUP (ORDER BY ModuleName)
        INTO sql_query_count
        FROM temp_checker_requests_filters;
        sql_query_count := sql_query_count || ')';
    END IF;

    IF Request_Action IS NOT NULL THEN
        sql_query := sql_query || '
            AND (';
        SELECT LISTAGG('INSTR(action, ''' || ActionName || ''') > 0', ' OR ') WITHIN GROUP (ORDER BY ActionName)
        INTO sql_query
        FROM temp_checker_requests_filters;
        sql_query := sql_query || ')';
		
		sql_query_count := sql_query_count || '
            AND (';
        SELECT LISTAGG('INSTR(action, ''' || ActionName || ''') > 0', ' OR ') WITHIN GROUP (ORDER BY ActionName)
        INTO sql_query_count
        FROM temp_checker_requests_filters;
        sql_query_count := sql_query_count || ')';
    END IF;

    IF submittedDate IS NOT NULL THEN
        sql_query := sql_query || '
            AND TRUNC(createdts) = TO_DATE(''' || TO_CHAR(submittedDate, 'YYYY-MM-DD') || ''', ''YYYY-MM-DD'')';
			
		sql_query_count := sql_query_count || '
            AND TRUNC(createdts) = TO_DATE(''' || TO_CHAR(submittedDate, 'YYYY-MM-DD') || ''', ''YYYY-MM-DD'')';
    END IF;
	
	EXECUTE IMMEDIATE sql_query_count;
	
    sql_query := sql_query || ' ORDER BY createdts ASC OFFSET ' || _pageOffset || ' ROWS FETCH NEXT ' || _pageSize || ' ROWS ONLY';

    EXECUTE IMMEDIATE sql_query;

    DROP TABLE temp_checker_requests_filters;

END;


CREATE VIEW "get_mc_requestshistory_view" AS
    SELECT 
        "mcconfig"."action" AS "action",
        "ar"."createdby" AS "createdby",
        UPPER("ar"."status") AS "status",
        "ar"."companyLegalUnit" AS "companyLegalUnit",
        "mcconfig"."approvalPermissionName" AS "approvalPermissionName",
		"ar"."requestId" AS "requestId",
		"ar"."module" AS "module",
		CAST("ar"."createdts" AS DATE) AS "createdDate",
		"ar"."createdts" AS "createdTs",
		"ar"."reason" AS "reason",
		"ar"."checkedBy" AS "checkedBy",
		"ar"."checkedts" AS "actionedDate"
    FROM
        (
          "approvalrequests" "ar" JOIN "makercheckerconfig" "mcconfig"
          ON ("ar"."expAPIOperationName" = "mcconfig"."expAPIOperationName" AND "ar"."companyLegalUnit" = "mcconfig"."companyLegalUnit")
        );

	
CREATE OR REPLACE VIEW get_mc_moduleactionname_view AS
    SELECT DISTINCT
        mcconfig.module AS moduleid,
        mcconfig.action AS actionid,
        mcmodule.name AS modulename,
        mcaction.name AS actionname,
        mcmodule.language_code AS languagecode
    FROM
        makercheckerconfig mcconfig
        JOIN mcmoduletext mcmodule ON mcmodule.id = mcconfig.module
        JOIN mcactiontext mcaction ON mcaction.id = mcconfig.action;
END;
/
create or replace NONEDITIONABLE PROCEDURE "update_makercheckerconfig_proc" (
    v__input IN VARCHAR2
)
AS
    v_idx INT := 0;
    v_maxIdx INT;
    v_companyLegalUnit VARCHAR(50);
    v_isApprovalRequired NUMBER(1);
    v_id INT;
    v_mc_data VARCHAR2(30000);
    v_rowdata VARCHAR2(30000);
BEGIN
    v_mc_data := JSON_QUERY(v__input, '$.*');
   	SELECT COUNT(*) INTO v_maxIdx FROM JSON_TABLE( v_mc_data, '$[*]' COLUMNS ( dummy VARCHAR2(1) PATH '$' ) );
    WHILE v_idx < v_maxIdx LOOP
	    v_rowdata := JSON_QUERY(v_mc_data, '$[' || v_idx || ']');
        SELECT JSON_VALUE(v_rowdata,'$.companyLegalUnit') INTO v_companyLegalUnit FROM DUAL;
        SELECT JSON_VALUE(v_rowdata,'$.isApprovalRequired') INTO v_isApprovalRequired FROM DUAL;
        SELECT JSON_VALUE(v_rowdata,'$.id') INTO v_id FROM DUAL;
        UPDATE "makercheckerconfig" SET "isApprovalRequired" = v_isApprovalRequired WHERE "id" = v_id AND "companyLegalUnit" = v_companyLegalUnit;
        v_idx := v_idx + 1;
    END LOOP;
END;
/

CREATE OR REPLACE PROCEDURE "checkforpendingrequests_proc"(
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
                status = 'SID_PENDING' AND
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

CREATE TABLE "tf_records_module_configurations" (
    "module_id" VARCHAR2(255) PRIMARY KEY,
    "allowed_fields" CLOB
);

CREATE TABLE "tf_records" (
    "record_id" NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "module_id" VARCHAR2(255),
    "record_data" CLOB,
    "created_by" VARCHAR2(255),
    "updated_by" VARCHAR2(255),
    "created_date" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    "updated_date" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT "tf_fk_module_id" FOREIGN KEY ("module_id") REFERENCES "tf_records_module_configurations"("module_id"),
    CONSTRAINT "tf_record_data_validate" CHECK ("record_data" IS JSON)
);

CREATE SEQUENCE tf_record_id_seq
  START WITH 1000000
  INCREMENT BY 1;

create or replace TRIGGER trg_before_insert_tf_records
BEFORE INSERT ON "tf_records"
FOR EACH ROW
BEGIN
    :NEW."record_id" := tf_record_id_seq.NEXTVAL;
END;

CREATE TABLE "ld_records_module_configurations" (
    "module_id" VARCHAR2(255) PRIMARY KEY,
    "allowed_fields" CLOB
);

CREATE TABLE "ld_records" (
    "record_id" NUMBER PRIMARY KEY,
    "module_id" VARCHAR2(255),
    "record_data" CLOB,
    "created_by" VARCHAR2(255),
    "updated_by" VARCHAR2(255),
    "created_date" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    "updated_date" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT "ld_fk_module_id" FOREIGN KEY ("module_id") REFERENCES "ld_records_module_configurations"("module_id"),
    CONSTRAINT "ld_record_data_validate" CHECK ("record_data" IS JSON)
);

CREATE SEQUENCE ld_record_id_seq
  START WITH 10000000
  INCREMENT BY 1;

create or replace TRIGGER trg_before_insert_ld_records
BEFORE INSERT ON "ld_records"
FOR EACH ROW
BEGIN
    :NEW."record_id" := ld_record_id_seq.NEXTVAL;
END;

ALTER TABLE "approvalrequests" MODIFY ("reason" CLOB);

-- SQL Scripts for DigitalWealth - Favorite Customer 
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE inf_wlth_favorite_customer';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;

CREATE TABLE inf_wlth_favorite_customer (
  id number(10),
  customerId nvarchar2(50) NOT NULL,
  favoriteCustomerId varchar2(2000),
  createdby varchar2(50) DEFAULT NULL,
  createdts timestamp(3) DEFAULT SYSTIMESTAMP NOT NULL,
  modifiedby varchar2(50) DEFAULT NULL,
  lastmodifiedts timestamp(3) DEFAULT SYSTIMESTAMP NOT NULL,
  synctimestamp timestamp(3) DEFAULT SYSTIMESTAMP NOT NULL,
  softdeleteflag number(5) DEFAULT '0' NOT NULL,
  PRIMARY KEY (id)
  CONSTRAINT FK_favorite_customer_id FOREIGN KEY (customerId) REFERENCES customer (id) ON DELETE CASCADE
);

-- Generate ID using sequence and trigger
CREATE SEQUENCE inf_wlth_favorite_customer_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER inf_wlth_favorite_customer_seq_tr
 BEFORE INSERT ON inf_wlth_favorite_customer FOR EACH ROW
 WHEN (NEW.id IS NULL)
BEGIN
 SELECT inf_wlth_favorite_customer_seq.NEXTVAL INTO :NEW.id FROM DUAL;
END;

ALTER TABLE "approvalrequests" ADD "viewDetailsResponse" CLOB;

create or replace PROCEDURE FETCH_NEW_SIGNATORY_GROUP_USER_DETAILS(
    customerIdList IN VARCHAR2,
    coreCustomerIdInput IN VARCHAR2
) AS
    TYPE customer_id_list IS TABLE OF NUMBER;
    v_ids customer_id_list;
BEGIN
    SELECT CAST(COLLECT(TRIM(REGEXP_SUBSTR(customerIdList, '[^,]+', 1, LEVEL))) AS customer_id_list)
    INTO v_ids
    FROM DUAL
    CONNECT BY REGEXP_SUBSTR(customerIdList, '[^,]+', 1, LEVEL) IS NOT NULL;

    SELECT
        customer.UserName as username,
        membergroup.Name AS role,
        customer.id as customerId,
        CURRENT_TIMESTAMP as addedts,
        customer.FirstName || ' ' || customer.LastName as customerName
    FROM customer
    JOIN customergroup ON customer.id = customergroup.Customer_id
    JOIN membergroup ON customergroup.Group_id = membergroup.id
    WHERE customergroup.coreCustomerId = coreCustomerIdInput
    AND customer.id IN (SELECT column_value FROM TABLE(v_ids)); 
END;

ALTER TABLE "makercheckerconfig" modify ("viewDetailsAPI" varchar(200));