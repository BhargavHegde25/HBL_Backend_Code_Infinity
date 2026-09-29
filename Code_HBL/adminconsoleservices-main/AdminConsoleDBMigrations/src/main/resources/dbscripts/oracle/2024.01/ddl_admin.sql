CREATE TABLE "makercheckerconfig" (
  "id" INT NOT NULL PRIMARY KEY,
  "expAPIOperationName" VARCHAR(250) DEFAULT NULL,
  "module" VARCHAR(250) DEFAULT NULL,
  "action" VARCHAR(250) DEFAULT NULL,
  "approvalPermissionId" VARCHAR(10) DEFAULT NULL,
  "approvalPermissionName" VARCHAR(50) DEFAULT NULL,
  "keyColumnNames" VARCHAR(200) NOT NULL,
  "isApprovalRequired" NUMBER(1) NOT NULL,
  "createdby" VARCHAR(50) DEFAULT NULL,
  "createdts" timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
  "companyLegalUnit" VARCHAR(50) DEFAULT 'ALL' NOT NULL
);

ALTER TABLE "approvalrequests" ADD "reqPayload" CLOB NOT NULL, "companyLegalUnit" varchar(50) NOT NULL DEFAULT 'ALL';

ALTER TABLE "customer" ADD "sbaEnrolmentStatus" VARCHAR2(70) DEFAULT NULL;

ALTER TABLE "customertermsandconditions" ADD "companyLegalUnit" VARCHAR(50) DEFAULT 'ALL' NOT NULL ;

CREATE TABLE "scf_records_module_configurations" (
    "module_id" VARCHAR2(255) PRIMARY KEY,
    "allowed_fields" CLOB
);

CREATE TABLE "scf_records" (
    "record_id" NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "module_id" VARCHAR2(255),
    "record_data" CLOB,
    "created_by" VARCHAR2(255),
    "updated_by" VARCHAR2(255),
    "created_date" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    "updated_date" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT "fk_module_id" FOREIGN KEY ("module_id") REFERENCES "scf_records_module_configurations"("module_id"),
    CONSTRAINT "record_data_validate" CHECK ("record_data" IS JSON)
);

CREATE SEQUENCE scf_record_id_seq
  START WITH 1000000
  INCREMENT BY 1;

create or replace TRIGGER trg_before_insert_scf_records
BEFORE INSERT ON "scf_records"
FOR EACH ROW
BEGIN
    :NEW."record_id" := scf_record_id_seq.NEXTVAL;
END;

ALTER TABLE "rrole" MODIFY "id" VARCHAR2(60 CHAR);