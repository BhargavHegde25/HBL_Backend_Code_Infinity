CREATE TABLE "productLine" (
  "productLineId" varchar(100)  NOT NULL,
  "productLineRef" varchar(100)  NOT NULL,
  "productLineName" varchar(100)  DEFAULT NULL,
  "externalIndicator" NUMBER(1) DEFAULT NULL,
  PRIMARY KEY ("productLineRef")
);

CREATE TABLE "productGroup" (
  "productGroupId" varchar(100)  NOT NULL,
  "productGroupRef" varchar(100)  NOT NULL,
  "productGroupName" varchar(100)  DEFAULT NULL,
  "description" varchar(200)  DEFAULT NULL,
  "branchRef" varchar(100)  DEFAULT NULL,
  "detailedDesc" varchar(300)  DEFAULT NULL,
  "productLineRef" varchar(100)  DEFAULT NULL,
  PRIMARY KEY ("productGroupRef"),
  CONSTRAINT "productGroup_FK" FOREIGN KEY ("productLineRef") REFERENCES "productLine" ("productLineRef")
);

CREATE TABLE "productInformation" (
  "productId" varchar(100)  NOT NULL,
  "productRef" varchar(100)  NOT NULL,
  "branchRef" varchar(100)  NOT NULL,
  "productName" varchar(100)  NOT NULL,
  "productLineRef" varchar(100)  NOT NULL,
  "productGroupRef" varchar(100)  NOT NULL,
  "status" varchar(50)  DEFAULT NULL,
  PRIMARY KEY ("productRef"),
  CONSTRAINT "productInformation_FK" FOREIGN KEY ("productLineRef") REFERENCES "productLine" ("productLineRef"),
  CONSTRAINT "productInformation_FK_1" FOREIGN KEY ("productGroupRef") REFERENCES "productGroup" ("productGroupRef")
);

CREATE OR REPLACE VIEW "dbxdb"."productinformationview" AS
select
    "productInformation"."productId" AS "productId",
    "productInformation"."productName" AS "productName",
    "productInformation"."productLineRef" AS "productLineRef",
    "productInformation"."productGroupRef" AS "productGroupRef",
    "productInformation"."branchRef" AS "branchRef",
    "productInformation"."status" AS "status",
    "productInformation"."productRef" AS "productRef",
    "productGroup"."productGroupName" AS "productGroupName",
    "productGroup"."productGroupId" AS "productGroupId",
    "productLine"."productLineName" AS "productLineName",
    "productLine"."productLineId" AS "productLineId"
from
    (("productInformation"
join "productGroup" on
    (("productInformation"."productGroupRef" = "productGroup"."productGroupRef")))
join "productLine" on
    (("productInformation"."productLineRef" = "productLine"."productLineRef")))
order by
    "productInformation"."productLineRef",
    "productInformation"."productGroupRef";
