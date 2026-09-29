
create or replace procedure "group_features_actions_view_proc"
(
"_groupId" IN NVARCHAR2,
"_companyLegalUnit" IN NVARCHAR2,
"records" out SYS_REFCURSOR
)
AS v_select_statement NCLOB;
BEGIN
v_select_statement:= ('SELECT "groupactionlimit"."Group_id", 
"groupactionlimit"."Action_id", 
"groupactionlimit"."LimitType_id", 
"groupactionlimit"."value", 
"groupactionlimit"."id" "groupactionlimit_id",
"groupactionlimit"."softdeleteflag" "softdelete", 
"membergroup"."Type_id", 
"membergroup"."Name" "Group_name",
"membergroup"."Description" "Group_description",
"featureaction"."name" "Action_name",
"featureaction"."description" "Action_description", 
"featureaction"."Type_id" "Action_Type_id", 
"featureaction"."Feature_id", 
"featureaction"."isMFAApplicable", 
"featureaction"."isAccountLevel",
"featureaction"."isPrimary", 
"featureaction"."DisplaySequence" "Action_displaysequence", 
"featureaction"."dependency" "Action_dependency", 
"featureaction"."status" "actionStatus",
"accesspolicy"."name" "accessPolicy", 
"featureaction"."accesspolicyId", 
"featureaction"."limitgroupId", 
"featureaction"."companyLegalUnit",
"limitgroup"."name" "limitGroup", 
"actionlevel"."name" "actionlevel", 
"featureaction"."actionlevelId",
"feature"."name" "featureName", 
"feature"."name" "Feature_name", 
"feature"."description" "Feature_description", 
"feature"."Type_id" "Feature_Type_id", 
"feature"."Status_id" "Feature_Status_id",
"feature"."DisplaySequence" "Feature_displaysequence", 
"feature"."isPrimary" "Feature_isPrimary"
FROM ("groupactionlimit" 
LEFT OUTER JOIN
"membergroup" ON ("membergroup"."id" = "groupactionlimit"."Group_id" and "membergroup"."companyLegalUnit" = "groupactionlimit"."companyLegalUnit") 
LEFT OUTER JOIN
"featureaction" ON ("featureaction"."id" = "groupactionlimit"."Action_id" and "featureaction"."companyLegalUnit" = "groupactionlimit"."companyLegalUnit") 
LEFT OUTER JOIN
"feature" ON ("feature"."id" = "featureaction"."Feature_id" and "feature"."companyLegalUnit" = "featureaction"."companyLegalUnit") 
LEFT OUTER JOIN
"accesspolicy" ON ("featureaction"."accesspolicyId" = "accesspolicy"."id") 
LEFT OUTER JOIN
"limitgroup" ON ("featureaction"."limitgroupId" = "limitgroup"."id") 
LEFT OUTER JOIN
"actionlevel" ON ("featureaction"."actionlevelId" = "actionlevel"."id" and "featureaction"."companyLegalUnit" = "actionlevel"."companyLegalUnit" ))');
IF ("_groupId" is not null AND LENGTH(RTRIM("_groupId"))>0) THEN 
	 v_select_statement := (v_select_statement) || (u' where "groupactionlimit"."Group_id" ='''|| "_groupId" || '''' ||' and "featureaction"."companyLegalUnit" =''' || "_companyLegalUnit" ||'''');
END IF;
open "records" for v_select_statement;
END;
/