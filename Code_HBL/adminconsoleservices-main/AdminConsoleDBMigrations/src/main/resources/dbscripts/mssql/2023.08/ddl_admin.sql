SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE OR ALTER PROCEDURE [${dbxschemaname}].[group_features_actions_view_proc]
@_groupId nvarchar(50),
@_companyLegalUnit  nvarchar(50)
AS
BEGIN

SET XACT_ABORT ON

SET NOCOUNT ON
DECLARE @select_statement nvarchar(max)


SET @select_statement = 'SELECT ${dbxschemaname}.groupactionlimit.Group_id, ${dbxschemaname}.groupactionlimit.Action_id, ${dbxschemaname}.groupactionlimit.LimitType_id, ${dbxschemaname}.groupactionlimit.value, ${dbxschemaname}.groupactionlimit.id AS groupactionlimit_id,
${dbxschemaname}.groupactionlimit.softdeleteflag AS softdelete, ${dbxschemaname}.membergroup.Type_id, ${dbxschemaname}.membergroup.Name AS Group_name, ${dbxschemaname}.membergroup.Description AS Group_description, ${dbxschemaname}.featureaction.name AS Action_name,
${dbxschemaname}.featureaction.description AS Action_description, ${dbxschemaname}.featureaction.Type_id AS Action_Type_id, ${dbxschemaname}.featureaction.Feature_id, ${dbxschemaname}.featureaction.isMFAApplicable, ${dbxschemaname}.featureaction.isAccountLevel,
${dbxschemaname}.featureaction.isPrimary, ${dbxschemaname}.featureaction.DisplaySequence AS Action_displaysequence, ${dbxschemaname}.featureaction.dependency AS Action_dependency, ${dbxschemaname}.featureaction.status AS actionStatus,
${dbxschemaname}.accesspolicy.name AS accessPolicy, ${dbxschemaname}.featureaction.accesspolicyId, ${dbxschemaname}.featureaction.limitgroupId, ${dbxschemaname}.featureaction.companyLegalUnit AS companyLegalUnit, ${dbxschemaname}.limitgroup.name AS limitGroup, ${dbxschemaname}.actionlevel.name AS actionlevel, ${dbxschemaname}.featureaction.actionlevelId,
${dbxschemaname}.feature.name AS featureName, ${dbxschemaname}.feature.name AS Feature_name, ${dbxschemaname}.feature.description AS Feature_description, ${dbxschemaname}.feature.Type_id AS Feature_Type_id, ${dbxschemaname}.feature.Status_id AS Feature_Status_id,
${dbxschemaname}.feature.DisplaySequence AS Feature_displaysequence, ${dbxschemaname}.feature.isPrimary AS Feature_isPrimary
FROM ${dbxschemaname}.groupactionlimit 
LEFT OUTER JOIN
${dbxschemaname}.membergroup ON (${dbxschemaname}.membergroup.id = ${dbxschemaname}.groupactionlimit.Group_id 
								 and ${dbxschemaname}.membergroup.companyLegalUnit = ${dbxschemaname}.groupactionlimit.companyLegalUnit)
LEFT OUTER JOIN
${dbxschemaname}.featureaction ON (${dbxschemaname}.featureaction.id = ${dbxschemaname}.groupactionlimit.Action_id 
								 and ${dbxschemaname}.featureaction.companyLegalUnit = ${dbxschemaname}.groupactionlimit.companyLegalUnit)
LEFT OUTER JOIN
${dbxschemaname}.feature ON (${dbxschemaname}.feature.id = ${dbxschemaname}.featureaction.Feature_id 
								 and ${dbxschemaname}.feature.companyLegalUnit = ${dbxschemaname}.featureaction.companyLegalUnit )
LEFT OUTER JOIN
${dbxschemaname}.accesspolicy ON ${dbxschemaname}.featureaction.accesspolicyId = ${dbxschemaname}.accesspolicy.id 
LEFT OUTER JOIN
${dbxschemaname}.limitgroup ON (${dbxschemaname}.featureaction.limitgroupId = ${dbxschemaname}.limitgroup.id )
LEFT OUTER JOIN
${dbxschemaname}.actionlevel ON (${dbxschemaname}.featureaction.actionlevelId = ${dbxschemaname}.actionlevel.id 
								 and ${dbxschemaname}.featureaction.companyLegalUnit = ${dbxschemaname}.actionlevel.companyLegalUnit )';

if @_groupId is not null and len(@_groupId)>0
SET @select_statement = CONCAT (@select_statement ,' where ${dbxschemaname}.groupactionlimit.Group_id = ''', @_groupId,'''', 'and ${dbxschemaname}.groupactionlimit.companyLegalUnit =''',@_companyLegalUnit ,''';');

exec(@select_statement);

END;
GO
