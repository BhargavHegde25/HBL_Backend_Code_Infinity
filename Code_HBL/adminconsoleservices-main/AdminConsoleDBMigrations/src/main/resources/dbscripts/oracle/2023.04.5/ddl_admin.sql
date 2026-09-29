delete from "groupactionlimit" where "Group_id"  = 'GROUP_CREATOR' and "Action_id" = 'ADD_USER_ANOTHER_ENTITY';

/* SQL Scripts for InfinityWealth - Strategy - Model Constraint */
CREATE TABLE "inf_wlth_model_constraint" ( 
  "portfolioId" NVARCHAR2(50) NOT NULL, 
  "portfolioCode" NVARCHAR2(50) NOT NULL, 
  "customerId" NVARCHAR2(50) NOT NULL, 
  "constraintId" NVARCHAR2(50), 
  "createdby" NVARCHAR2(50), 
  "modifiedby" NVARCHAR2(50), 
  "createdts" timestamp, 
  "lastmodifiedts" timestamp, 
  "synctimestamp" timestamp, 
  "softdeleteflag" NUMBER(1,0) 
); 

ALTER TABLE "inf_wlth_model_constraint" MODIFY ("portfolioId" DEFAULT NULL); 
ALTER TABLE "inf_wlth_model_constraint" MODIFY ("portfolioCode" DEFAULT NULL); 
ALTER TABLE "inf_wlth_model_constraint" MODIFY ("customerId" DEFAULT NULL); 
ALTER TABLE "inf_wlth_model_constraint" MODIFY ("constraintId" DEFAULT NULL); 
ALTER TABLE "inf_wlth_model_constraint" MODIFY ("createdby" DEFAULT NULL); 
ALTER TABLE "inf_wlth_model_constraint" MODIFY ("modifiedby" DEFAULT NULL); 
ALTER TABLE "inf_wlth_model_constraint" MODIFY ("createdts" DEFAULT CURRENT_TIMESTAMP); 
ALTER TABLE "inf_wlth_model_constraint" MODIFY ("lastmodifiedts" DEFAULT CURRENT_TIMESTAMP); 
ALTER TABLE "inf_wlth_model_constraint" MODIFY ("synctimestamp" DEFAULT CURRENT_TIMESTAMP); 
ALTER TABLE "inf_wlth_model_constraint" MODIFY ("softdeleteflag" DEFAULT (0)); 
 
ALTER TABLE "inf_wlth_model_constraint" 
ADD CONSTRAINT PK__infwlth__3213E83FBCB3542B PRIMARY KEY 
( 
  "portfolioId" 
) 
ENABLE 
; 

ALTER TABLE "inf_wlth_model_constraint" 
ADD CONSTRAINT FK_model_constraint_customer_id FOREIGN KEY 
( 
  "customerId" 
) 
REFERENCES "customer" 
( 
  "id" 
) 
ENABLE 
; 