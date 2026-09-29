DROP PROCEDURE IF EXISTS [${dbxschemaname}].[customrole_contract_delete_proc];
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[customrole_contract_delete_proc]  
   @customRoleId nvarchar(max),
   @contractId nvarchar(max),
   @coreCustomerId nvarchar(max)
AS 
   BEGIN
		DECLARE @contract_statement nvarchar(max);
		DECLARE @accounts_statement nvarchar(max);
		DECLARE @action_statement nvarchar(max);
		DECLARE @limitgroup_statement nvarchar(max);
		DECLARE @where_clause nvarchar(max);
		DECLARE @where_clause1 nvarchar(max);
		DECLARE @where_clause2 nvarchar(max);
		
		SET @contract_statement = N'DELETE FROM [${dbxschemaname}].[contractcustomrole] where';
		SET @accounts_statement = N'DELETE FROM [${dbxschemaname}].[customroleaccounts] where ';
		SET @action_statement = N'DELETE FROM [${dbxschemaname}].[customroleactionlimits] where ';
		SET @limitgroup_statement = N'DELETE FROM [${dbxschemaname}].[customerlimitgrouplimits] where ';

		SET @where_clause = '';
		SET @where_clause1 = '';
		if(@customRoleId != '') 
		BEGIN
			SET @where_clause = @where_clause + (N' and customRoleId = ') + ((QUOTENAME((@customRoleId), '''')))
			SET @where_clause1 = @where_clause1 + (N' and customRole_id = ') + ((QUOTENAME((@customRoleId), '''')))
			SET @where_clause2 = @where_clause2 + (N' and Customer_id = ') + ((QUOTENAME((@customRoleId), '''')))
			
		END
		
		IF(@contractId != '') 
		BEGIN
			IF(@where_clause != '')
			BEGIN
				SET @where_clause = @where_clause + (N' AND ')
				SET @where_clause1 = @where_clause1 + (N' AND ')
				SET @where_clause2 = @where_clause2 + (N' AND ')
			END
			SET @where_clause = @where_clause + (N' and contractId = ') + ((QUOTENAME((@contractId), '''')))
			SET @where_clause1 = @where_clause1 + (N' and contractId = ') + ((QUOTENAME((@contractId), '''')))
			SET @where_clause2 = @where_clause2 + (N' and contractId = ') + ((QUOTENAME((@contractId), '''')))
		END
		
		IF(@coreCustomerId != '') 
		BEGIN
			IF(@where_clause != '')
			BEGIN
				SET @where_clause = @where_clause + (N' AND ')
				SET @where_clause1 = @where_clause1 + (N' AND ')
				SET @where_clause2 = @where_clause2 + (N' AND ')
			END
			SET @where_clause = @where_clause + (N' and coreCustomerId = ') + ((QUOTENAME((@coreCustomerId), '''')))
			SET @where_clause1 = @where_clause1 + (N' and coreCustomerId = ') + ((QUOTENAME((@coreCustomerId), '''')))
			SET @where_clause2 = @where_clause2 + (N' and coreCustomerId = ') + ((QUOTENAME((@coreCustomerId), '''')))
		END
		
		IF(@where_clause != '')
		BEGIN	
			SET @contract_statement = @contract_statement + @where_clause;	
			SET @accounts_statement = @accounts_statement + @where_clause;	
			SET @action_statement = @action_statement + @where_clause1;	
			SET @limitgroup_statement = @limitgroup_statement + @where_clause2;	
		
			exec(@contract_statement);
			exec(@accounts_statement);
			exec(@action_statement);
			exec(@limitgroup_statement)
		END
   END
GO