EXEC [${dbxschemaname}].sp_delete_default_constranit '[${dbxschemaname}].manageapprovalmatrix', 'isDisabled';
GO
ALTER TABLE [${dbxschemaname}].[manageapprovalmatrix] alter column  [isDisabled] [BIT] NOT NULL ;
GO