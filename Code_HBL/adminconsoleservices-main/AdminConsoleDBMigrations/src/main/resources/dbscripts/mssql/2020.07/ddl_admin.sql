USE [${dbxdbname}]
GO

CREATE PROCEDURE [${dbxschemaname}].[account_action_approvers_proc]  
   @_organizationId nvarchar(50),
   @_accountId nvarchar(50),
   @_approvalActionList nvarchar(max),
   @_featureId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      declare @customerIdList nvarchar(max)
      SET  NOCOUNT  ON

      SET @customerIdList = 
         (
            SELECT String_agg(CAST(customeraction.Customer_id as nvarchar(max)),',')
            FROM [${dbxschemaname}].customeraction
            WHERE 
               [${dbxschemaname}].customeraction.isAllowed = 0 AND 
               [${dbxschemaname}].customeraction.Action_id = @_approvalActionList AND 
               [${dbxschemaname}].customeraction.Account_id = @_accountId
         )
		 
      SET @customerIdList = 
         CASE 
            WHEN (@customerIdList IS NULL) THEN ''
            ELSE @customerIdList
         END

      SELECT DISTINCT 
         (customer.id) AS id, 
         (customer.UserName) AS userName, 
         (membergroup.Name) AS groupId, 
         (customer.FirstName) AS firstName, 
         (customer.LastName) AS lastName
      FROM ([${dbxschemaname}].customer 
         LEFT JOIN [${dbxschemaname}].organisation 
         ON ([${dbxschemaname}].organisation.id = [${dbxschemaname}].customer.Organization_Id) 
         LEFT JOIN [${dbxschemaname}].customergroup 
         ON ([${dbxschemaname}].customergroup.Customer_id = [${dbxschemaname}].customer.id) 
         LEFT JOIN [${dbxschemaname}].membergroup 
         ON ([${dbxschemaname}].membergroup.id = [${dbxschemaname}].customergroup.Group_id) 
         LEFT JOIN [${dbxschemaname}].groupactionlimit 
         ON ([${dbxschemaname}].groupactionlimit.Group_id = [${dbxschemaname}].customergroup.Group_id) 
         LEFT JOIN [${dbxschemaname}].customeraccounts 
         ON ([${dbxschemaname}].customeraccounts.Customer_id = [${dbxschemaname}].customer.id) 
         LEFT JOIN [${dbxschemaname}].organisationfeatures 
         ON ([${dbxschemaname}].organisationfeatures.organisationId = [${dbxschemaname}].customer.Organization_Id))
      WHERE 
         [${dbxschemaname}].organisation.id = @_organizationId AND 
         [${dbxschemaname}].organisationfeatures.featureId = @_featureId AND 
         ([${dbxschemaname}].organisationfeatures.featureStatus IS NULL OR len([${dbxschemaname}].organisationfeatures.featureStatus) = 0) AND 
         [${dbxschemaname}].customeraccounts.Account_id = @_accountId AND 
         [${dbxschemaname}].customer.Status_id = 'SID_CUS_ACTIVE' AND 
         [${dbxschemaname}].customer.id NOT IN ( (@customerIdList) ) AND 
         [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].groupactionlimit.Action_id,@_approvalActionList) > 0

   END
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[ach_file_record_subrecord_create_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[ach_file_record_subrecord_create_proc]  
   @_recordvalues nvarchar(max),
   @_subRecordvalues nvarchar(max)
AS 
   BEGIN
      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON
      DECLARE @index1 int = 0
      DECLARE @numOfRecords int
      DECLARE @recordsData nvarchar(max)
      DECLARE @query nvarchar(max)
      DECLARE @subRecordsData nvarchar(max)
      DECLARE @subRecordData nvarchar(max)
      DECLARE @subRecordIndex int
      DECLARE @numberOfSubRecords int
      DECLARE @id nvarchar(max)

      SET @numOfRecords = len(@_recordvalues) - len(replace(@_recordvalues, '|', '')) + 1
	  SET @index1 = @index1 + 1
	  WHILE (@index1 != @numOfRecords + 1)
	  BEGIN
					
                  SET @recordsData = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_recordvalues,'|', @index1),'|', -1)
                  SET @query = (
                     'INSERT INTO [${dbxschemaname}].achfilerecord(
                        [${dbxschemaname}].achfilerecord.achFileId,
                        [${dbxschemaname}].achfilerecord.offsetAccountNumber,
                        [${dbxschemaname}].achfilerecord.offsetAmount,
                        [${dbxschemaname}].achfilerecord.offsetTransactionType,
                        [${dbxschemaname}].achfilerecord.effectiveDate,
                        [${dbxschemaname}].achfilerecord.requestType,
                        [${dbxschemaname}].achfilerecord.totalCreditAmount,
                        [${dbxschemaname}].achfilerecord.totalDebitAmount,
                        [${dbxschemaname}].achfilerecord.transactionType
                     ) VALUES (') + (replace(@recordsData, '"', '''')) + (');')
                  EXEC(@query)
                  SET @id=@@IDENTITY
                  IF @_subRecordvalues IS NOT NULL
                     BEGIN
					 SET @_subRecordvalues=(replace(@_subRecordvalues, '"', ''''))
                        SET @subRecordsData = ([${dbxschemaname}].SUBSTRING_INDEX(([${dbxschemaname}].SUBSTRING_INDEX((@_subRecordvalues),'|',@index1)),'|', -1))
                        IF @subRecordsData IS NOT NULL
                           BEGIN
                              SET @numberOfSubRecords = len(@subRecordsData) - len(replace(@subRecordsData, ';', '')) + 1
                              SET @subRecordIndex = 1
                              WHILE (1 = 1)
                              
                                 BEGIN
                                    IF @subRecordIndex = @numberOfSubRecords + 1
                                       BREAK
                                    ELSE 
                                       BEGIN
                                          SET @subRecordData = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX( @subRecordsData, ';',@subRecordIndex),';', -1)
                                          SET @query = ('
                                             INSERT INTO [${dbxschemaname}].achfilesubrecord(
                                                [${dbxschemaname}].achfilesubrecord.amount,
                                                [${dbxschemaname}].achfilesubrecord.receiverAccountNumber,
                                                [${dbxschemaname}].achfilesubrecord.receiverAccountType,
                                                [${dbxschemaname}].achfilesubrecord.receiverName,
                                                [${dbxschemaname}].achfilesubrecord.receiverTransactionType,
                                                [${dbxschemaname}].achfilesubrecord.achFileRecordId
                                             ) VALUES (') + @subRecordData + (',') + (@id) + (');')
                                          EXEC(@query)
                                          SET @subRecordIndex = @subRecordIndex + 1
                                       END
                                    END
                           END
                     END
					 SET @index1 = @index1 + 1
               END
         END

GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[ach_template_record_subrecord_create_proc]  
   @_recordvalues nvarchar(max),
   @_subRecordvalues nvarchar(max)
AS 
   BEGIN
      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON
      DECLARE @index1 int = 0
      DECLARE @numOfRecords int
      DECLARE @recordsData nvarchar(max)
      DECLARE @query nvarchar(max)
      DECLARE @subRecordsData nvarchar(max)
      DECLARE @subRecordData nvarchar(max)
      DECLARE @subRecordIndex int
      DECLARE @numberOfSubRecords int
      DECLARE @id int

	  SET @_recordvalues = ( select replace(@_recordvalues, '"', ''''))
	  SET @_subRecordvalues = ( select replace(@_subRecordvalues, '"', ''''))

      SET @numOfRecords = LEN(@_recordvalues) - LEN(replace(@_recordvalues, '|', '')) + 1
      WHILE (1 = 1)
      
         BEGIN
            SET @index1 = @index1 + 1
            IF @index1 = @numOfRecords + 1
               BREAK
            ELSE 
               BEGIN
                  SET @recordsData = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX(@_recordvalues, '|', @index1), '|', -1)
                  SET @query = (
                     'INSERT INTO [${dbxschemaname}].bbtemplaterecord(
                        bbtemplaterecord.record_Name,
                        bbtemplaterecord.toAccountNumber,
                        bbtemplaterecord.abatrcNumber,
                        bbtemplaterecord.detail_id,
                        bbtemplaterecord.amount,
                        bbtemplaterecord.additionalInfo,
                        bbtemplaterecord.ein,
                        bbtemplaterecord.isZeroTaxDue,
                        bbtemplaterecord.template_id,
                        bbtemplaterecord.taxType_id,
                        bbtemplaterecord.templateRequestType_id,
                        bbtemplaterecord.toAccountType
                      ) 
                      VALUES (') + (@recordsData) + (')')
                  EXEC(@query)
                  SET @id = @@IDENTITY
                  IF @_subRecordvalues <> ''
                     BEGIN
                        SET @subRecordsData = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX( @_subRecordvalues,'|', @index1), '|', -1)
                        IF @subRecordsData <> '' and @subRecordsData != ';'
                           BEGIN
                              SET @numberOfSubRecords = LEN(@subRecordsData) - LEN(replace(@subRecordsData, ';', '')) + 1
                              SET @subRecordIndex = 1
                              WHILE (1 = 1)
                                 BEGIN
                                    IF @subRecordIndex = @numberOfSubRecords + 1
                                       BREAK
                                    ELSE 
                                       BEGIN
                                          SET @subRecordData = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX( @subRecordsData, ';', @subRecordIndex), ';', -1)
                                          SET @query = (
                                             'INSERT INTO [${dbxschemaname}].bbtemplatesubrecord(
                                                [${dbxschemaname}].bbtemplatesubrecord.amount,
                                                [${dbxschemaname}].bbtemplatesubrecord.taxSubCategory_id,
                                                [${dbxschemaname}].bbtemplatesubrecord.templateRecord_id
                                             ) VALUES (') + (@subRecordData) + (',') + (CAST(@id AS nvarchar(max))) + (');')
                                          EXEC(@query)
                                          SET @subRecordIndex = @subRecordIndex + 1
                                       END
                                    END
                           END
                     END
               END
         END
   END
GO


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [${dbxschemaname}].[ach_transaction_record_subrecord_create_proc]  
   @_recordvalues nvarchar(max),
   @_subRecordvalues nvarchar(max)
AS 
   BEGIN
      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON

      DECLARE @index1 int = 0
      DECLARE @numOfRecords int
      DECLARE @recordsData nvarchar(max)
      DECLARE @query nvarchar(max)
      DECLARE @subRecordsData nvarchar(max)
      DECLARE @subRecordData nvarchar(max)
      DECLARE @subRecordIndex int
      DECLARE @numberOfSubRecords int
      DECLARE @id int

	  SET @_recordvalues = ( select replace(@_recordvalues, '"', ''''))
	  SET @_subRecordvalues = ( select replace(@_subRecordvalues, '"', ''''))

      SET @numOfRecords = LEN(@_recordvalues) - LEN(replace(@_recordvalues, '|', '')) + 1
      WHILE (1 = 1)

         BEGIN

            SET @index1 = @index1 + 1
            IF @index1 = @numOfRecords + 1
               BREAK
            ELSE 
               BEGIN
                  SET @recordsData = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX( @_recordvalues, '|', @index1), '|', -1)
                  SET @query = (
                     'INSERT INTO [${dbxschemaname}].achtransactionrecord(
                        achTransactionrecord.record_Name,
                        achTransactionrecord.toAccountNumber,
                        achTransactionrecord.abatrcNumber,
                        achTransactionrecord.detail_id,
                        achTransactionrecord.amount,
                        achTransactionrecord.additionalInfo,
                        achTransactionrecord.eIN,
                        achTransactionrecord.isZeroTaxDue,
                        achTransactionrecord.transaction_id,
                        achTransactionrecord.taxType_id,
                        achTransactionrecord.templateRequestType_id,
                        achTransactionrecord.toAccountType
                     ) VALUES (') + (@recordsData) + (');')
                  EXEC(@query)
                  SET @id = @@IDENTITY
                  IF @_subRecordvalues <> ''
                     BEGIN
                        SET @subRecordsData = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX( @_subRecordvalues, '|', @index1),'|', -1)
                        IF @subRecordsData <> ''  and @subRecordsData != ';'
                           BEGIN
                              SET @numberOfSubRecords = LEN(@subRecordsData) - LEN(replace(@subRecordsData, ';', '')) + 1
                              SET @subRecordIndex = 1
                              WHILE (1 = 1)
                                 BEGIN
                                    IF @subRecordIndex = @numberOfSubRecords + 1
                                       BREAK
                                    ELSE 
                                       BEGIN
                                          SET @subRecordData = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX( @subRecordsData, ';', @subRecordIndex),';', -1)
                                          SET @query = (
                                             'INSERT INTO [${dbxschemaname}].achtransactionsubrecord( 
                                                achTransactionsubrecord.amount,
                                                achTransactionsubrecord.taxSubCategory_id,
                                                achTransactionsubrecord.transactionRecord_id
                                                ) VALUES (') + (@subRecordData) + (',') + (CAST(@id AS nvarchar(max))) + (');')
                                          EXEC(@query)
                                          SET @subRecordIndex = @subRecordIndex + 1
                                       END
                                 END
                           END
                     END
               END
         END
   END
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[achtransactions_fetch_records_subrecords_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[achtransactions_fetch_records_subrecords_proc]  
   @_transactionId int
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT 
         achTransaction.transaction_id, 
         achTransaction.fromAccount, 
         achTransaction.effectiveDate, 
         achTransaction.requestId, 
         achTransaction.createdby, 
         achTransaction.createdts AS createdOn, 
         achTransaction.maxAmount, 
         achTransaction.status, 
         achTransaction.transactionType_id, 
         achTransaction.templateType_id, 
         achTransaction.companyId, 
         achTransaction.templateRequestType_id, 
         achTransaction.templateName, 
         achTransaction.confirmationNumber, 
         achTransaction.actedBy, 
         achTransaction.template_id, 
         achTransaction.totalAmount, 
         achTransaction.featureActionId, 
         bbTemplateRequestType.templateRequestTypeName AS templateRequestType, 
         achTransactionRecord.transactionRecord_id, 
         achTransactionRecord.toAccountNumber, 
         achTransactionRecord.toAccountType, 
         achTransactionRecord.abatrcNumber, 
         achTransactionRecord.detail_id, 
         achTransactionRecord.amount, 
         achTransactionRecord.additionalInfo, 
         achTransactionRecord.eIN, 
         achTransactionRecord.isZeroTaxDue, 
         achTransactionRecord.taxType_id, 
         achTransactionRecord.templateRequestType_id, 
         achTransactionRecord.transaction_id AS transaction_id_reclevel, 
         achTransactionRecord.record_Name, 
         achTransactionSubRecord.transcationSubRecord_id, 
         achTransactionSubRecord.amount AS subrecordamount, 
         achTransactionSubRecord.taxSubCategory_id, 
         bbTaxType.taxType, 
         bbTaxSubtype.taxSubType, 
         achTransactionSubrecord.transactionRecord_id AS transactionRecord_id_subreclevel
      FROM ((((([${dbxschemaname}].achtransaction  AS achTransaction 
         LEFT JOIN [${dbxschemaname}].achtransactionrecord  AS achTransactionRecord 
         ON achtransaction.transaction_id = achtransactionrecord.transaction_id) 
         LEFT JOIN [${dbxschemaname}].achtransactionsubrecord AS achTransactionSubRecord 
         ON achtransactionrecord.transactionRecord_id = achtransactionsubrecord.transactionRecord_id) 
         LEFT JOIN [${dbxschemaname}].bbtaxtype  AS bbTaxType 
         ON achtransactionrecord.taxType_id = bbtaxtype.id) 
         LEFT JOIN [${dbxschemaname}].bbtaxsubtype  AS bbTaxSubtype 
         ON achtransactionsubrecord.taxSubCategory_id = bbtaxsubtype.id) 
         LEFT JOIN [${dbxschemaname}].bbtemplaterequesttype  AS bbTemplateRequestType 
         ON achtransaction.templateRequestType_id = bbtemplaterequesttype.templateRequestType_id)
      WHERE achtransaction.transaction_id = @_transactionId
         ORDER BY achtransaction.transaction_id, achtransactionrecord.transactionRecord_id, achtransactionsubrecord.transactionRecord_id

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[action_limits_update_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[action_limits_update_proc]  
   @_action nvarchar(50),
   @_minTxLimit decimal(20, 2),
   @_maxTxLimit decimal(20, 2),
   @_dailyLimit decimal(20, 2),
   @_weeklyLimit decimal(20, 2)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      UPDATE [${dbxschemaname}].actionlimit
         SET 
            value = @_minTxLimit
      WHERE actionlimit.Action_id = @_action AND actionlimit.LimitType_id = 'MIN_TRANSACTION_LIMIT'

      UPDATE [${dbxschemaname}].actionlimit
         SET 
            value = @_maxTxLimit
      WHERE actionlimit.Action_id = @_action AND actionlimit.LimitType_id = 'MAX_TRANSACTION_LIMIT'

      UPDATE [${dbxschemaname}].actionlimit
         SET 
            value = @_dailyLimit
      WHERE actionlimit.Action_id = @_action AND actionlimit.LimitType_id = 'DAILY_LIMIT'

      UPDATE [${dbxschemaname}].actionlimit
         SET 
            value = @_weeklyLimit
      WHERE actionlimit.Action_id = @_action AND actionlimit.LimitType_id = 'WEEKLY_LIMIT'

      UPDATE [${dbxschemaname}].groupactionlimit
         SET 
            value = @_maxTxLimit
      WHERE 
         groupactionlimit.Action_id = @_action AND 
         groupactionlimit.LimitType_id = 'MAX_TRANSACTION_LIMIT' AND 
         groupactionlimit.value > @_maxTxLimit

      UPDATE [${dbxschemaname}].groupactionlimit
         SET 
            value = @_dailyLimit
      WHERE 
         groupactionlimit.Action_id = @_action AND 
         groupactionlimit.LimitType_id = 'DAILY_LIMIT' AND 
         groupactionlimit.value > @_dailyLimit

      UPDATE [${dbxschemaname}].groupactionlimit
         SET 
            value = @_weeklyLimit
      WHERE 
         groupactionlimit.Action_id = @_action AND 
         groupactionlimit.LimitType_id = 'WEEKLY_LIMIT' AND 
         groupactionlimit.value > @_weeklyLimit

      UPDATE [${dbxschemaname}].organisationactionlimit
         SET 
            value = @_maxTxLimit
      WHERE 
         organisationactionlimit.Action_id = @_action AND 
         organisationactionlimit.LimitType_id = 'MAX_TRANSACTION_LIMIT' AND 
         organisationactionlimit.value > @_maxTxLimit

      UPDATE [${dbxschemaname}].organisationactionlimit
         SET 
            value = @_dailyLimit
      WHERE 
         organisationactionlimit.Action_id = @_action AND 
         organisationactionlimit.LimitType_id = 'DAILY_LIMIT' AND 
         organisationactionlimit.value > @_dailyLimit

      UPDATE [${dbxschemaname}].organisationactionlimit
         SET 
            value = @_weeklyLimit
      WHERE 
         organisationactionlimit.Action_id = @_action AND 
         organisationactionlimit.LimitType_id = 'WEEKLY_LIMIT' AND 
         organisationactionlimit.value > @_weeklyLimit

      UPDATE [${dbxschemaname}].customeraction
         SET 
            value = @_maxTxLimit
      WHERE 
         customeraction.Action_id = @_action AND 
         customeraction.LimitType_id = 'PRE_APPROVED_TRANSACTION_LIMIT' AND 
         customeraction.value > @_maxTxLimit

      UPDATE [${dbxschemaname}].customeraction
         SET 
            value = @_dailyLimit
      WHERE 
         customeraction.Action_id = @_action AND 
         customeraction.LimitType_id = 'PRE_APPROVED_DAILY_LIMIT' AND 
         customeraction.value > @_dailyLimit

      UPDATE [${dbxschemaname}].customeraction
         SET 
            value = @_weeklyLimit
      WHERE 
         customeraction.Action_id = @_action AND 
         customeraction.LimitType_id = 'PRE_APPROVED_WEEKLY_LIMIT' AND 
         customeraction.value > @_weeklyLimit

      UPDATE [${dbxschemaname}].customeraction
         SET 
            value = @_maxTxLimit
      WHERE 
         customeraction.Action_id = @_action AND 
         customeraction.LimitType_id = 'AUTO_DENIED_TRANSACTION_LIMIT' AND 
         customeraction.value > @_maxTxLimit

      UPDATE [${dbxschemaname}].customeraction
         SET 
            value = @_dailyLimit
      WHERE 
         customeraction.Action_id = @_action AND 
         customeraction.LimitType_id = 'AUTO_DENIED_DAILY_LIMIT' AND 
         customeraction.value > @_dailyLimit

      UPDATE [${dbxschemaname}].customeraction
         SET 
            value = @_weeklyLimit
      WHERE 
         customeraction.Action_id = @_action AND 
         customeraction.LimitType_id = 'AUTO_DENIED_WEEKLY_LIMIT' AND 
         customeraction.value > @_weeklyLimit

      UPDATE [${dbxschemaname}].customeraction
         SET 
            value = @_maxTxLimit
      WHERE 
         customeraction.Action_id = @_action AND 
         customeraction.LimitType_id = 'MAX_TRANSACTION_LIMIT' AND 
         customeraction.value > @_maxTxLimit

      UPDATE [${dbxschemaname}].customeraction
         SET 
            value = @_dailyLimit
      WHERE 
         customeraction.Action_id = @_action AND 
         customeraction.LimitType_id = 'DAILY_LIMIT' AND 
         customeraction.value > @_dailyLimit

      UPDATE [${dbxschemaname}].customeraction
         SET 
            value = @_weeklyLimit
      WHERE 
         customeraction.Action_id = @_action AND 
         customeraction.LimitType_id = 'WEEKLY_LIMIT' AND 
         customeraction.value > @_weeklyLimit

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[alert_history_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[alert_history_proc]  
   @_customerId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT TOP (800) 
         alerthistory.id AS alerthistory_id, 
         alerthistory.EventId AS alerthistory_EventId, 
         alerthistory.AlertSubTypeId AS alerthistory_AlertSubTypeId, 
         alerthistory.AlertTypeId AS alerthistory_AlertTypeId, 
         alerthistory.AlertCategoryId AS alerthistory_AlertCategoryId, 
         alerthistory.Customer_Id AS alerthistory_Customer_Id, 
         alerthistory.LanguageCode AS alerthistory_LanguageCode, 
         alerthistory.ChannelId AS alerthistory_ChannelId, 
         alerthistory.Status AS alerthistory_Status, 
         alerthistory.Subject AS alerthistory_Subject, 
         alerthistory.Message AS alerthistory_Message, 
         alerthistory.SenderName AS alerthistory_SenderName, 
         alerthistory.SenderEmail AS alerthistory_SenderEmail, 
         alerthistory.ReferenceNumber AS alerthistory_ReferenceNumber, 
         alerthistory.createdts AS alerthistory_sentDate, 
         alerthistory.softdeleteflag AS alerthistory_softdeleteflag, 
         alertsubtype.Name AS alertsubtype_Name, 
         alertsubtype.Description AS alertsubtype_Description, 
         channeltext.Description AS channeltext_Description
      FROM 
         [${dbxschemaname}].alerthistory 
            INNER JOIN [${dbxschemaname}].alertsubtype 
            ON (alertsubtype.id = alerthistory.AlertSubTypeId) 
            INNER JOIN [${dbxschemaname}].channeltext 
            ON (channeltext.channelID = alerthistory.ChannelId)
      WHERE (alerthistory.Customer_Id = @_customerId AND channeltext.LanguageCode = 'en-US')
         ORDER BY alerthistory.DispatchDate DESC

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[approvalmatrix_create_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[approvalmatrix_create_proc]  
   @_matrixValues nvarchar(max),
   @_approverIds nvarchar(max)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DECLARE @index1 int = 0
		DECLARE @length bigint
      DECLARE @index2 int = 0
      DECLARE @id int
      DECLARE @customerIds nvarchar(max)
      DECLARE @customerIdsComma nvarchar(max)
      DECLARE @length2 int
      DECLARE @customerId nvarchar(100)
      DECLARE @matrixRecord nvarchar(max)
      DECLARE @matrixComma varchar(max)
      DECLARE @query nvarchar(max)

      SET @length = LEN(@_matrixValues) - LEN(replace(@_matrixValues, ',', '')) + 1

      WHILE (1 = 1)
         BEGIN
            SET @index1 = @index1 + 1
            IF @index1 = @length + 1
               BREAK
            ELSE 
               BEGIN
				  set @matrixRecord = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX(@_matrixValues, ',', @index1), ',', -1 );
                  SET @matrixComma = replace(@matrixRecord, ';', ',')
				  SET @matrixComma = replace(@matrixComma, '"', '''')
                  SET @query = ('
                     INSERT [${dbxschemaname}].approvalmatrix(
                        approvalmatrix.name,
                        approvalmatrix.companyId,
                        approvalmatrix.actionId,
                        approvalmatrix.accountId,
                        approvalmatrix.approvalruleId,
                        approvalmatrix.limitTypeId,
                        approvalmatrix.lowerlimit,
                        approvalmatrix.upperlimit
                     ) VALUES (') + (@matrixComma) + (')')
					
                  execute (@query)
                  SET @id = @@IDENTITY
				  
				  set @customerIds = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX(@_approverIds, ',', @index1), ',', -1 );
                  SET @customerIdsComma = replace(@customerIds, ';', ',')
                  SET @length2 = LEN(@customerIdsComma) - LEN(replace(@customerIdsComma, ',', '')) + 1
                  SET @index2 = 0
                  WHILE (1 = 1)
                     BEGIN
                        SET @index2 = @index2 + 1
                        IF @index2 = @length2 + 1
                           BREAK
                        ELSE 
                           BEGIN
							  set @customerId = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX(@customerIdsComma, ',', @index2), ',', -1 );
                              INSERT INTO [${dbxschemaname}].customerapprovalmatrix([${dbxschemaname}].customerapprovalmatrix.customerId, [${dbxschemaname}].customerapprovalmatrix.approvalMatrixId)
                                 VALUES (@customerId, @id)
                              CONTINUE
                           END
                     END
                  CONTINUE
               END
         END
   END
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[approvalmatrix_fetch_records_proc]    Script Date: 6/9/2020 2:00:56 PM ******/

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[approvalmatrix_fetch_records_proc]  
   @_companyId nvarchar(50),
   @_accountId nvarchar(50),
   @_limitTypeId nvarchar(50),
   @_actions nvarchar(max)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON
      IF @_accountId = ''
         SET @_accountId = '%'
      IF @_limitTypeId = ''
         SET @_limitTypeId = '%'

      SELECT 
         approvalMatrix.id, 
         approvalMatrix.companyId, 
         approvalMatrix.accountId, 
         approvalMatrix.limitTypeId, 
         featureAction.id AS actionId, 
         featureAction.name AS actionName, 
         featureAction.description AS actionDescription, 
         featureAction.Feature_id AS featureId, 
         feature.name AS featureName, 
         feature.Status_id AS fifeaturestatus, 
         organisationfeatures.featureStatus AS orgfeaturestatus, 
         approvalRule.id AS approvalruleId, 
         approvalRule.numberOfApprovals, 
         approvalRule.name AS approvalRuleName, 
         approvalMatrix.lowerlimit, 
         approvalMatrix.upperlimit, 
         customer.id AS customerId, 
         customer.FirstName AS firstName, 
         customer.LastName AS lastName
      FROM (((((([${dbxschemaname}].approvalmatrix  AS approvalMatrix 
         LEFT JOIN [${dbxschemaname}].customerapprovalmatrix  AS customerApprovalMatrix 
         ON approvalMatrix.id = customerApprovalMatrix.approvalMatrixId) 
         LEFT JOIN [${dbxschemaname}].customer  AS customer 
         ON customerApprovalMatrix.customerId = customer.id) 
         LEFT JOIN [${dbxschemaname}].featureaction  AS featureAction 
         ON approvalMatrix.actionId = featureAction.id) 
         LEFT JOIN [${dbxschemaname}].approvalrule  AS approvalRule 
         ON approvalMatrix.approvalruleId = approvalRule.id) 
         LEFT JOIN [${dbxschemaname}].feature  AS feature 
         ON featureAction.Feature_id = feature.id) 
         LEFT JOIN [${dbxschemaname}].organisationfeatures  AS organisationfeatures 
         ON feature.id = organisationfeatures.featureId AND approvalMatrix.companyId = organisationfeatures.organisationId)
      WHERE 
         approvalMatrix.companyId = @_companyId AND 
         approvalMatrix.accountId LIKE @_accountId AND 
         [${dbxschemaname}].FIND_IN_SET(approvalMatrix.actionId, @_actions) > 0 AND 
         approvalMatrix.limitTypeId LIKE @_limitTypeId AND 
         approvalMatrix.softdeleteflag = 0
         ORDER BY 
            approvalMatrix.companyId, 
            approvalMatrix.accountId, 
            approvalMatrix.limitTypeId, 
            approvalMatrix.actionId, 
            approvalMatrix.lowerlimit
   END
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[approvalmatrix_default_delete_proc]    Script Date: 6/9/2020 2:00:56 PM ******/

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[approvalmatrix_default_delete_proc]  
   @_companyId nvarchar(50),
   @_filterColumnIds nvarchar(max),
   @_filterColumnName nvarchar(50)
AS 
   BEGIN
	  
      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON
      IF  @_filterColumnName = 'actionId'
		 begin
         DELETE FROM [${dbxschemaname}].approvalmatrix
         WHERE [${dbxschemaname}].approvalmatrix.companyId = @_companyId AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].approvalmatrix.actionId, @_filterColumnIds) <> 0
		end
      ELSE 
         BEGIN
            IF @_filterColumnName = 'accountId'
               DELETE FROM [${dbxschemaname}].approvalmatrix
               WHERE [${dbxschemaname}].approvalmatrix.companyId = @_companyId AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].approvalmatrix.accountId, @_filterColumnIds) <> 0
         END
   END
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[approvalmatrix_default_create_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[approvalmatrix_default_create_proc]  
   @_actionIds nvarchar(max),
   @_companyId nvarchar(50),
   @_accountId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON

      DECLARE @finished int = 0
      DECLARE @actionId varchar(255) = ''
      DECLARE @actionList varchar(max) = ''
      DECLARE @limitTypeId_1 varchar(255) = 'DAILY_LIMIT'
      DECLARE @limitTypeId_2 varchar(255) = 'MAX_TRANSACTION_LIMIT'
      DECLARE @limitTypeId_3 varchar(255) = 'WEEKLY_LIMIT'

      DECLARE
          actions CURSOR LOCAL FORWARD_ONLY FOR 
             (
               SELECT featureaction.id
               FROM [${dbxschemaname}].featureaction
               WHERE [${dbxschemaname}].FIND_IN_SET(featureaction.id, @_actionIds) <> 0
             )

      OPEN actions

      WHILE (1 = 1)
      
         BEGIN

            FETCH actions
                INTO @actionId

            IF @@FETCH_STATUS <> 0
               SET @finished = 1

            IF @finished = 1
               BREAK
            ELSE 
               BEGIN

                  INSERT [${dbxschemaname}].approvalmatrix(
                     [${dbxschemaname}].approvalmatrix.companyId, 
                     [${dbxschemaname}].approvalmatrix.name, 
                     [${dbxschemaname}].approvalmatrix.accountId, 
                     [${dbxschemaname}].approvalmatrix.actionId, 
                     [${dbxschemaname}].approvalmatrix.limitTypeId)
                     VALUES (
                        @_companyId, 
                        @actionId + '_'+ @_accountId+ '_'+ @limitTypeId_1+ '_'+ @_companyId, 
                        @_accountId, 
                        @actionId, 
                        @limitTypeId_1
                     )

                  INSERT [${dbxschemaname}].approvalmatrix(
                     [${dbxschemaname}].approvalmatrix.companyId, 
                     [${dbxschemaname}].approvalmatrix.name, 
                     [${dbxschemaname}].approvalmatrix.accountId, 
                     [${dbxschemaname}].approvalmatrix.actionId, 
                     [${dbxschemaname}].approvalmatrix.limitTypeId)
                     VALUES (
                        @_companyId, 
                        @actionId+ '_'+ @_accountId+ '_' + @limitTypeId_2 + '_'+ @_companyId, 
                        @_accountId, 
                        @actionId, 
                        @limitTypeId_2
                     )

                  INSERT [${dbxschemaname}].approvalmatrix(
                     [${dbxschemaname}].approvalmatrix.companyId, 
                     [${dbxschemaname}].approvalmatrix.name, 
                     [${dbxschemaname}].approvalmatrix.accountId, 
                     [${dbxschemaname}].approvalmatrix.actionId, 
                     [${dbxschemaname}].approvalmatrix.limitTypeId)
                     VALUES (
                        @_companyId, 
                        @actionId + '_' + @_accountId + '_' + @limitTypeId_3 + '_' + @_companyId, 
                        @_accountId, 
                        @actionId, 
                        @limitTypeId_3
                     )

                  SET @actionList = @actionId + ',' + @actionList
                  CONTINUE
               END

         END
      CLOSE actions
      DEALLOCATE actions
      SET @actionList = 
         (
            SELECT substring(@actionList, 1, LEN(@actionList)-1)
         )
      SELECT @actionList AS actionList

   END
GO


/****** Object:  StoredProcedure [${dbxschemaname}].[approvalmatrix_update_softdeleteflag_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[approvalmatrix_update_softdeleteflag_proc]  
   @_companyId nvarchar(50),
   @_accountId nvarchar(50),
   @_actionId nvarchar(50),
   @_limitTypeId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      /*
      *   SSMA warning messages:
      *   M2SS0183: The following SQL clause was ignored during conversion: COLLATE utf8_general_ci.
      */

      UPDATE [${dbxschemaname}].approvalmatrix
         SET 
            softdeleteflag = 1
      WHERE 
         [${dbxschemaname}].approvalmatrix.companyId = @_companyId AND 
         [${dbxschemaname}].approvalmatrix.accountId = @_accountId AND 
         [${dbxschemaname}].approvalmatrix.actionId = @_actionId AND 
         [${dbxschemaname}].approvalmatrix.limitTypeId = @_limitTypeId

   END
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[approvalrequest_counts_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[approvalrequest_counts_proc]  
   @_customerId nvarchar(50),
   @_approveActionList nvarchar(max),
   @_createActionList nvarchar(max)
AS 

   BEGIN

    MAINLABEL: 
      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON

	   DECLARE @approvalRequestIds nvarchar(max)
      DECLARE @companyId nvarchar(max)
      DECLARE @features nvarchar(max)
      DECLARE @createApproveActions nvarchar(max)
      DECLARE @customerMatrixIds nvarchar(max)
      DECLARE @select_statement nvarchar(max)
      

      SELECT 0 AS count, 'ACHFilesForMyApproval' AS TransactionType
       UNION
      SELECT 0 AS count, 'ACHTransactionsForMyApproval' AS TransactionType
       UNION
      SELECT 0 AS count, 'GeneralTransactionsForMyApproval' AS TransactionType
       UNION
      SELECT 0 AS count, 'GeneralTransactionsForMyApproval' AS TransactionType
       UNION
      SELECT 0 AS count, 'myRequestsWaiting' AS TransactionType
       UNION
      SELECT 0 AS count, 'myRequestsRejected' AS TransactionType
       UNION
      SELECT 0 AS count, 'myRequestsApproved' AS TransactionType

      SET @companyId = 
         (
            SELECT [${dbxschemaname}].customer.Organization_Id
            FROM [${dbxschemaname}].customer
            WHERE [${dbxschemaname}].customer.id = @_customerId
         )
      IF @companyId IS NULL
         SET @companyId = ''
      IF @_approveActionList IS NULL
         SET @_approveActionList = ''
      IF @_createActionList IS NULL
         SET @_createActionList = ''
      SET @features = 
         (
            SELECT STRING_AGG(CAST(featureaction.Feature_id as nvarchar(max)), ',')
            FROM [${dbxschemaname}].featureaction
            WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id, @_approveActionList) > 0
         )
      IF @features IS NULL
         SET @features = ''
      SET @createApproveActions = 
         (
            SELECT STRING_AGG(CAST(featureaction.id as nvarchar(max)), ',')
            FROM [${dbxschemaname}].featureaction
            WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.Feature_id, @features) > 0 AND ([${dbxschemaname}].featureaction.id LIKE '%_CREATE' OR [${dbxschemaname}].featureaction.id LIKE '%_UPLOAD')
         )
      IF @createApproveActions IS NULL
         SET @createApproveActions = ''
		 
      SET @customerMatrixIds = 
         (
            SELECT STRING_AGG(CAST(customerapprovalmatrix.approvalMatrixId as nvarchar(max)), ',')
            FROM [${dbxschemaname}].customerapprovalmatrix
            WHERE [${dbxschemaname}].customerapprovalmatrix.customerId = @_customerId
         )

      IF @customerMatrixIds IS NULL
         GOTO MAINLABEL$leave
		 declare @alreadyApprovedIds nvarchar(max)
      SET @alreadyApprovedIds = 
         (
            SELECT STRING_AGG(CAST(bbactedrequest.requestId as nvarchar(max)), ',')
            FROM [${dbxschemaname}].bbactedrequest
            WHERE [${dbxschemaname}].bbactedrequest.createdby = @_customerId AND [${dbxschemaname}].bbactedrequest.action = 'Approved'
         )
		 
      IF @alreadyApprovedIds IS NULL
         SET @alreadyApprovedIds = ''		
      SET @approvalRequestIds = 
         (
            SELECT STRING_AGG(CAST(requestapprovalmatrix.requestId as nvarchar(max)), ',')
            FROM [${dbxschemaname}].requestapprovalmatrix
            WHERE [${dbxschemaname}].FIND_IN_SET(CAST(requestapprovalmatrix.approvalMatrixId as nvarchar(max)), @customerMatrixIds) > 0 AND [${dbxschemaname}].FIND_IN_SET(CAST(requestapprovalmatrix.requestId as nvarchar(max)), @alreadyApprovedIds) = 0
         )
      SET @select_statement = 
         ('select count(tablea.requestCountA) as count, tablea.TransactionType 
		 from (
         	select DISTINCT([${dbxschemaname}].bbrequest.requestId) as requestCountA, 
         		iif([${dbxschemaname}].bbrequest.featureActionId LIKE ''ACH_FILE%'',
					''ACHFilesForMyApproval'',
         			iif([${dbxschemaname}].bbrequest.featureActionId LIKE ''ACH%'',
						''ACHTransactionsForMyApproval'',
						''GeneralTransactionsForMyApproval''
						)
				) as TransactionType,
         	    [${dbxschemaname}].bbrequest.createdby,
         	    [${dbxschemaname}].bbrequest.companyId,
         	    [${dbxschemaname}].bbrequest.status
         	    FROM (
         	    	[${dbxschemaname}].bbrequest LEFT JOIN [${dbxschemaname}].requestapprovalmatrix ON 
         	    	[${dbxschemaname}].bbrequest.requestId = [${dbxschemaname}].requestapprovalmatrix.requestId
         	    )
         	    WHERE [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].bbrequest.requestId as nvarchar(max)),''' + (@approvalRequestIds) + ''')>0 AND [${dbxschemaname}].bbrequest.companyId = ' + (@companyId) + 
         	    ' AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbrequest.featureActionId,''' + (@createApproveActions) + ''')>0' + 
         	    ' AND [${dbxschemaname}].bbrequest.status = ''Pending'') as tablea GROUP BY TransactionType')
      SET @select_statement = (@select_statement) + ' UNION select count(tableb.requestCountB) as count, 
      	tableb.TransactionType from (
      		select  
      			DISTINCT([${dbxschemaname}].bbrequest.requestId) as requestCountB,
      			iif([${dbxschemaname}].bbrequest.status = ''Pending'', ''myRequestsWaiting'', 
      				iif([${dbxschemaname}].bbrequest.status = ''Rejected'', ''myRequestsRejected'', 
      					iif([${dbxschemaname}].bbrequest.status = ''Approved'', ''myRequestsApproved'', ''myRequestsWithdrawn''))) as TransactionType,
      			[${dbxschemaname}].bbrequest.createdby,
      			[${dbxschemaname}].bbrequest.companyId,
      			[${dbxschemaname}].bbrequest.status
      			FROM [${dbxschemaname}].bbrequest WHERE [${dbxschemaname}].bbrequest.companyId = ''' + (@companyId) + '''' + 
         		' AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbrequest.featureActionId,''' + (@_createActionList) + 
         		''')>0 AND [${dbxschemaname}].bbrequest.createdby = ' + (QUOTENAME(@_customerId, '''')) + ') as tableb group BY TransactionType'
      IF @select_statement IS NULL
         GOTO MAINLABEL$leave
		exec(@select_statement)
	  End
   MAINLABEL$leave:
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[archived_request_search_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[archived_request_search_proc]  
   @_dateInitialPoint char(50),
   @_dateFinalPoint char(50),
   @_requestStatusID char(50),
   @_requestCategory char(50),
   @_offset char(50),
   @_sortCriteria char(50),
   @_sortOrder char(50),
   @_requestAssignedTo char(50),
   @_searchKey char(50),
   @_messageRepliedBy char(50),
   @_recordsPerPage varchar(50),
   @_queryType char(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
	  SET  NOCOUNT  ON

	  DECLARE @selectClause nvarchar(max)
	  DECLARE @stmt nvarchar(max)
	  DECLARE @whereclause nvarchar(max)
	  DECLARE @joinCustomer nvarchar(max)

      SET @selectClause = 
         CASE 
            WHEN (isnull(@_queryType, '') = 'count') THEN 'count(acr.id) AS cnt '
            ELSE 'acr.id AS customerrequest_id'
         END

      SET @stmt = ('SELECT ') + (@selectClause) + (' FROM [${dbxschemaname}].archivedcustomerrequest acr ')

      SET @whereclause = ' WHERE 1=1 '
  
      SET @joinCustomer = 0

      IF @_dateInitialPoint <> '' AND @_dateFinalPoint <> ''

         SET @whereclause = 
            (@whereclause)
             + 
            (' '+'AND acr.createdts >= ')
             + 
            QUOTENAME(trim(@_dateInitialPoint),'''')
             + 
            (N'   AND ')
             + 
            (N' acr.createdts <= ')
             + 
            QUOTENAME(trim(@_dateFinalPoint), '''')

      ELSE 
         IF @_dateInitialPoint <> ''

            SET @whereclause = (@whereclause) + ('    AND acr.createdts = ') + QUOTENAME(trim(@_dateInitialPoint), '''')

         ELSE 
            BEGIN
               IF @_dateFinalPoint <> ''

                  SET @whereclause = (@whereclause) + ('   AND acr.createdts = ') + QUOTENAME(trim(@_dateFinalPoint), '''')

            END

      IF @_requestStatusID <> ''

         SET @whereclause = (@whereclause) + ('  AND acr.Status_id IN (') + ([${dbxschemaname}].func_escape_input_for_in_operator(trim(@_requestStatusID))) + (+') ')
		 


      IF @_requestCategory <> ''

         SET @whereclause = (@whereclause) +'  AND acr.RequestCategory_id = '+''''+(trim(@_requestCategory))+''''


      IF @_requestAssignedTo <> ''

         SET @whereclause = (@whereclause) + (' AND acr.AssignedTo = ') +''''+(trim(@_requestAssignedTo))+''''

      IF @_messageRepliedBy <> ''
         BEGIN

            SET @stmt = (@stmt) + (N'LEFT JOIN [${dbxschemaname}].archivedrequestmessage ON (acr.id = archivedrequestmessage.CustomerRequest_id) ')

            SET @whereclause = (@whereclause) + (' '+'  AND archivedrequestmessage.RepliedBy_id = ') + ''''+(trim(@_messageRepliedBy))+''''

         END

      IF @_searchKey <> ''
         BEGIN

            SET @joinCustomer = 1
            SET @_searchKey = N'%' + @_searchKey + N'%'
            SET @whereclause = 
               (@whereclause)
                + 
               (N' AND '+' ('+'  acr.Customer_id LIKE ')
                + 
               ''''+(@_searchKey)+''''
                + 
               (N' OR acr.id LIKE ')
                + 
               ''''+(@_searchKey)+''''
                + 
               (N' OR customer.UserName LIKE ')
                + 
               ''''+(@_searchKey)+''''
                + 
               (N')')
         END

      IF @_queryType <> 'count'
         BEGIN

            IF @_sortCriteria = 'customer_Fullname'

               SET @joinCustomer = 1
				declare @sortColumn nvarchar(max)
            SET @sortColumn = N'acr.lastmodifiedts'

            IF (@_sortCriteria = 'customerrequest_Customer_id')

               SET @sortColumn = N'acr.Customer_id'
            ELSE 
               IF (@_sortCriteria = 'customerrequest_AssignedTo')

                  SET @sortColumn = N'acr.AssignedTo'
               ELSE 
                  IF (@_sortCriteria = 'customerrequest_createdts')

                     SET @sortColumn = N'acr.createdts'

                  ELSE 
                     IF (@_sortCriteria = 'customerrequest_RequestCategory_id')

                        SET @sortColumn = N' acr.RequestCategory_id '

                     ELSE 
                        IF (@_sortCriteria = 'customer_Fullname')

                           SET @sortColumn = N'(customer.FirstName, customer.LastName)'

                        ELSE 
                           IF (@_sortCriteria = 'customerrequest_Status_id')

                              SET @sortColumn = N'acr.Status_id'

                           ELSE 
                              BEGIN
                                 IF (@_sortCriteria = 'customerrequest_AssignedTo_Name')

                                    SET @sortColumn = N'(systemuser.FirstName, systemuser.LastName)'

                              END

            SET @whereclause = (@whereclause) + (N'  ORDER BY ') + (
               CASE 
                  WHEN (@sortColumn = '') THEN N' acr.lastmodifiedts '
                  ELSE @sortColumn
               END) + (N' ') + (
               CASE 
                  WHEN (@sortColumn = '' OR @_sortOrder = '') THEN N' DESC'
                  ELSE @_sortOrder
               END)

            SET @whereclause = (@whereclause) + (N' OFFSET ') + (
               CASE 
                  WHEN (@_offset = '') THEN N'0'
                  ELSE @_offset
               END) + (N' ROWS FETCH NEXT ') + (
               CASE 
                  WHEN (@_recordsPerPage = '') THEN N'10'+ (N' ROWS ONLY ')
                  ELSE @_recordsPerPage +  (N' ROWS ONLY ')
               END)
  
         END
      IF @joinCustomer = 1

         SET @stmt = (@stmt) + (N'LEFT JOIN [${dbxschemaname}].customer ON (acr.Customer_id = customer.id) ')
		 
      IF @_sortCriteria = 'customerrequest_AssignedTo_Name'

         SET @stmt = (@stmt) + (N'LEFT JOIN [${dbxschemaname}].systemuser ON (acr.AssignedTo = systemuser.id) ')

		 SET @stmt = (@stmt) + (@whereclause)
      EXECUTE (@stmt)
   END
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[businesstype_defaultgroup_update_proc]    Script Date: 6/9/2020 2:00:56 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[businesstype_defaultgroup_update_proc]  
   @_businessTypeId nvarchar(50),
   @_groupId nvarchar(50),
   @_isDefault varchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

	  if @_isDefault = '0'
	  UPDATE [${dbxschemaname}].groupbusinesstype SET isDefaultGroup = 0 where groupbusinesstype.BusinessType_id = @_businessTypeId AND groupbusinesstype.Group_id = @_groupId
	  else
	   begin

      UPDATE [${dbxschemaname}].groupbusinesstype
         SET 
            isDefaultGroup = 0
      WHERE groupbusinesstype.BusinessType_id = @_businessTypeId


      UPDATE [${dbxschemaname}].groupbusinesstype
         SET 
            isDefaultGroup = 1
      WHERE groupbusinesstype.BusinessType_id = @_businessTypeId AND groupbusinesstype.Group_id = @_groupId
	  end
   END
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[bbrequest_updatestatus_proc]    Script Date: 6/9/2020 2:00:56 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[bbrequest_updatestatus_proc]  
   @_requestId bigint,
   @_status varchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      UPDATE [${dbxschemaname}].bbrequest
         SET 
            [${dbxschemaname}].bbrequest.[status] = @_status
      WHERE [${dbxschemaname}].bbrequest.requestId = @_requestId

      SELECT 
         [${dbxschemaname}].bbrequest.requestId, 
         [${dbxschemaname}].bbrequest.transactionId, 
         [${dbxschemaname}].bbrequest.featureActionId, 
         [${dbxschemaname}].bbrequest.createdby, 
         [${dbxschemaname}].bbrequest.companyId, 
         [${dbxschemaname}].bbrequest.requiredSets, 
         [${dbxschemaname}].bbrequest.receivedSets, 
         [${dbxschemaname}].bbrequest.createdts, 
         [${dbxschemaname}].bbrequest.status, 
         [${dbxschemaname}].bbrequest.softDelete, 
         [${dbxschemaname}].bbrequest.accountId
      FROM [${dbxschemaname}].bbrequest
      WHERE CAST([${dbxschemaname}].bbrequest.requestId as nvarchar(max)) = @_requestId

   END


GO

/****** Object:  StoredProcedure [${dbxschemaname}].[bbrequest_updatecounter_proc]    Script Date: 6/9/2020 2:00:56 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[bbrequest_updatecounter_proc]  
   @_requestId bigint,
   @_counter int
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      UPDATE [${dbxschemaname}].bbrequest
         SET 
            [${dbxschemaname}].bbrequest.receivedSets = [${dbxschemaname}].bbrequest.receivedSets + @_counter
      WHERE [${dbxschemaname}].bbrequest.requestId = @_requestId

      SELECT *
      FROM [${dbxschemaname}].bbrequest
      WHERE [${dbxschemaname}].bbrequest.requestId = @_requestId

   END


GO

/****** Object:  StoredProcedure [${dbxschemaname}].[authorizationCheckForRejectAndWithdrawl_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [${dbxschemaname}].[authorizationCheckForRejectAndWithdrawl_proc]  
   @_requestId nvarchar(50),
   @_companyId nvarchar(50),
   @_featureactionlist nvarchar(max)
AS 
   BEGIN
      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON

      DECLARE @group_concat_max_len bigint
      SET @group_concat_max_len = 100000000
      DECLARE @features nvarchar(max)
      DECLARE @createActions nvarchar(max)

      SET @features = 
         (
            SELECT string_agg(CAST(featureaction.Feature_id as nvarchar(max)),',')
            FROM [${dbxschemaname}].featureaction
            WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id, @_featureactionlist) > 0
         )

      IF @features IS NULL
         SET @features = ''
		 SET @createActions = 
         (
            SELECT string_agg(CAST(featureaction.id as nvarchar(max)),',')
            FROM [${dbxschemaname}].featureaction
            WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.Feature_id, @features) > 0 AND [${dbxschemaname}].featureaction.id LIKE '%_CREATE'
         )

      IF @createActions IS NULL
         SET @createActions = ''
      SELECT 
         [${dbxschemaname}].bbrequest.requestId, 
         [${dbxschemaname}].bbrequest.transactionId, 
         [${dbxschemaname}].bbrequest.featureActionId, 
         [${dbxschemaname}].bbrequest.createdby, 
         [${dbxschemaname}].bbrequest.companyId, 
         [${dbxschemaname}].bbrequest.requiredSets, 
         [${dbxschemaname}].bbrequest.receivedSets, 
         [${dbxschemaname}].bbrequest.createdts, 
         [${dbxschemaname}].bbrequest.status, 
         [${dbxschemaname}].bbrequest.softDelete, 
         [${dbxschemaname}].bbrequest.accountId
      FROM [${dbxschemaname}].bbrequest
      WHERE 
         [${dbxschemaname}].bbrequest.requestId = CAST(@_requestId AS float(53)) AND 
         [${dbxschemaname}].bbrequest.companyId = @_companyId AND 
         [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbrequest.featureActionId, @createActions) <> 0
   END
GO


/****** Object:  StoredProcedure [${dbxschemaname}].[auto_reject_invalid_pending_requests_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[auto_reject_invalid_pending_requests_proc]  
   @_customerId nvarchar(50)
AS 
   /*
   *   SSMA informational messages:
   *   M2SS0003: The following SQL clause was ignored during conversion:
   *   MAINLABEL : .
   */

   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      declare @oldMatrixIds nvarchar(max)
	  declare @newMatrixIds nvarchar(max)
	  declare @invalidMatrixIds nvarchar(max)
	  declare @invalidRequestIds nvarchar(max)
	  declare @companyId nvarchar(max)
	  declare @numOfParams int
	  declare @idx int
	  declare @select_statement nvarchar(max)
	  declare @SQL_SAFE_UPDATES nvarchar(max)
	  declare @requestId nvarchar(max)

      SET @oldMatrixIds = (SELECT String_agg(CAST(customerapprovalmatrix.approvalMatrixId as nvarchar(max)),',')
            FROM [${dbxschemaname}].customerapprovalmatrix
            WHERE customerapprovalmatrix.customerId = @_customerId)

      IF (@oldMatrixIds IS NULL)
         GOTO MAINLABEL$leave

      SET @newMatrixIds = 
         (
            SELECT STRING_AGG(CAST(customerapprovalmatrix.approvalMatrixId as nvarchar(max)),',')
            FROM 
               [${dbxschemaname}].customerapprovalmatrix 
                  LEFT JOIN [${dbxschemaname}].approvalmatrix 
                  ON ([${dbxschemaname}].customerapprovalmatrix.approvalMatrixId = [${dbxschemaname}].approvalmatrix.id) 
                  INNER JOIN 
                  (SELECT DISTINCT [${dbxschemaname}].customeraction.Account_id, replace(replace([${dbxschemaname}].customeraction.Action_id, 'FILE_APPROVE', 'FILE_UPLOAD'), '_APPROVE', '_CREATE') AS Action_id
                     FROM [${dbxschemaname}].customeraction
                     WHERE 
                        [${dbxschemaname}].customeraction.Customer_id = @_customerId AND 
                        [${dbxschemaname}].customeraction.isAllowed = 1 AND 
                        [${dbxschemaname}].customeraction.Account_id IS NOT NULL AND 
                        [${dbxschemaname}].customeraction.Action_id LIKE '%_APPROVE'
                  )  AS can 
                  ON (([${dbxschemaname}].approvalmatrix.actionId = can.Action_id) AND ([${dbxschemaname}].approvalmatrix.accountId = can.Account_id))
            WHERE [${dbxschemaname}].customerapprovalmatrix.customerId = @_customerId
         )

      IF (@newMatrixIds IS NULL)

         SET @newMatrixIds = ''

	  SET @invalidMatrixIds = 
         (
            SELECT STRING_AGG(CAST(id as nvarchar(max)),',')
            FROM [${dbxschemaname}].approvalmatrix
            WHERE [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].approvalmatrix.id AS nvarchar(max)), @oldMatrixIds) <> 0 AND [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].approvalmatrix.id AS nvarchar(max)), @oldMatrixIds) = 0
         )

      IF (@invalidMatrixIds IS NULL)
         GOTO MAINLABEL$leave

      SET @invalidRequestIds = (
			SELECT STRING_AGG(CAST(bbrequest.requestId as nvarchar(max)),',')
            FROM
               [${dbxschemaname}].bbrequest
                  LEFT JOIN [${dbxschemaname}].requestapprovalmatrix
                  ON ([${dbxschemaname}].bbrequest.requestId = [${dbxschemaname}].requestapprovalmatrix.requestId)
                  INNER JOIN
                  (
                    ( SELECT [${dbxschemaname}].approvalmatrix.id AS approvalMatrixId, [${dbxschemaname}].approvalrule.numberOfApprovals AS numberOfApprovalsRequired,
                        numberOfApprovers = (
                           SELECT count_big(DISTINCT ([${dbxschemaname}].customerapprovalmatrix.customerId))
                           FROM [${dbxschemaname}].customerapprovalmatrix
                           WHERE [${dbxschemaname}].customerapprovalmatrix.approvalMatrixId = [${dbxschemaname}].approvalmatrix.id
                        )
                     FROM
                        [${dbxschemaname}].approvalmatrix
                           LEFT JOIN [${dbxschemaname}].approvalrule
                           ON ([${dbxschemaname}].approvalmatrix.approvalruleId = [${dbxschemaname}].approvalrule.id))
                  ) AS temp_request_table
                  ON ([${dbxschemaname}].requestapprovalmatrix.approvalMatrixId = temp_request_table.approvalMatrixId)
            WHERE [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].requestapprovalmatrix.approvalMatrixId AS nvarchar(max)), @invalidMatrixIds) <> 0 AND (temp_request_table.numberOfApprovalsRequired = -1 OR temp_request_table.numberOfApprovalsRequired >= temp_request_table.numberOfApprovers)
			)

      SET @SQL_SAFE_UPDATES = 0

      SET @select_statement = ('DELETE FROM [${dbxschemaname}].customerapprovalmatrix WHERE [${dbxschemaname}].customerapprovalmatrix.approvalMatrixId in (' + (@invalidMatrixIds) + ') AND [${dbxschemaname}].customerapprovalmatrix.customerId = ''') + (@_customerId) + ('''')

      SET @select_statement = ('UPDATE [${dbxschemaname}].approvalmatrix SET invalid = ''1'' WHERE [${dbxschemaname}].approvalmatrix.id in  (') + (@invalidMatrixIds) + (')')

      IF (@invalidRequestIds IS NULL)
         BEGIN

            SET @SQL_SAFE_UPDATES = 1

            GOTO MAINLABEL$leave

         END

      SET @select_statement = ('UPDATE [${dbxschemaname}].bbrequest SET status = ''Rejected'' WHERE [${dbxschemaname}].bbrequest.requestId in  (') + (@invalidRequestIds) + (')')

	  SET @select_statement = ('UPDATE [${dbxschemaname}].billpaytransfers INNER JOIN [${dbxschemaname}].bbrequest ON [${dbxschemaname}].billpaytransfers.requestId = [${dbxschemaname}].bbrequest.requestId SET [${dbxschemaname}].billpaytransfers.status =  ''Rejected'' [${dbxschemaname}].WHERE billpaytransfers.requestId in (') + (@invalidRequestIds) + (')')

      SET @select_statement = ('UPDATE [${dbxschemaname}].ownaccounttransfers INNER JOIN [${dbxschemaname}].bbrequest ON [${dbxschemaname}].ownaccounttransfers.requestId = [${dbxschemaname}].bbrequest.requestId SET [${dbxschemaname}].ownaccounttransfers.status =  ''Rejected'' WHERE [${dbxschemaname}].ownaccounttransfers.requestId in (') + (@invalidRequestIds) + (')')

	  SET @select_statement = ('UPDATE [${dbxschemaname}].interbankfundtransfers INNER JOIN [${dbxschemaname}].bbrequest ON [${dbxschemaname}].interbankfundtransfers.requestId = [${dbxschemaname}].bbrequest.requestId SET [${dbxschemaname}].interbankfundtransfers.status =  ''Rejected'' WHERE [${dbxschemaname}].interbankfundtransfers.requestId in (') + (@invalidRequestIds) + (')')

      SET @select_statement = ('UPDATE [${dbxschemaname}].intrabanktransfers INNER JOIN [${dbxschemaname}].bbrequest ON [${dbxschemaname}].intrabanktransfers.requestId = [${dbxschemaname}].bbrequest.requestId SET [${dbxschemaname}].intrabanktransfers.status =  ''Rejected'' WHERE [${dbxschemaname}].intrabanktransfers.requestId in (') + (@invalidRequestIds) + (')')

      SET @select_statement = ('UPDATE [${dbxschemaname}].p2ptransfers INNER JOIN [${dbxschemaname}].bbrequest ON [${dbxschemaname}].p2ptransfers.requestId = [${dbxschemaname}].bbrequest.requestId SET [${dbxschemaname}].p2ptransfers.status =  ''Rejected'' WHERE [${dbxschemaname}].p2ptransfers.requestId in (') + (@invalidRequestIds) + (')')

      SET @select_statement = ('UPDATE [${dbxschemaname}].wiretransfers INNER JOIN [${dbxschemaname}].bbrequest ON [${dbxschemaname}].wiretransfers.requestId = [${dbxschemaname}].bbrequest.requestId SET [${dbxschemaname}].wiretransfers.status =  ''Rejected'' WHERE [${dbxschemaname}].wiretransfers.requestId in (') + (@invalidRequestIds) + (')')

      SET @select_statement = ('UPDATE [${dbxschemaname}].internationalfundtransfers INNER JOIN [${dbxschemaname}].bbrequest ON [${dbxschemaname}].internationalfundtransfers.requestId = [${dbxschemaname}].bbrequest.requestId SET [${dbxschemaname}].internationalfundtransfers.status = ''Rejected'' WHERE [${dbxschemaname}].internationalfundtransfers.requestId in (') + (@invalidRequestIds) + (')')

      SET @select_statement = ('UPDATE [${dbxschemaname}].achtransaction INNER JOIN [${dbxschemaname}].bbrequest ON [${dbxschemaname}].achtransaction.requestId = [${dbxschemaname}].bbrequest.requestId SET [${dbxschemaname}].achtransaction.status =  ''Rejected'' WHERE [${dbxschemaname}].achtransaction.requestId in (') + (@invalidRequestIds) + (')')

      SET @select_statement = ('UPDATE [${dbxschemaname}].achfile INNER JOIN [${dbxschemaname}].bbrequest ON [${dbxschemaname}].achfile.requestId = [${dbxschemaname}].bbrequest.requestId SET [${dbxschemaname}].achfile.status = ''Rejected'' WHERE [${dbxschemaname}].achfile.requestId in (') + (@invalidRequestIds) + (')')

      SET @companyId = 
         (
            SELECT customer.Organization_Id
            FROM [${dbxschemaname}].customer
            WHERE [${dbxschemaname}].customer.id = @_customerId
         )
     
	 IF (@companyId IS NULL)
         GOTO MAINLABEL$leave
    
	 SET @numOfParams = 0
     
	 IF (datalength(@invalidRequestIds) > 0)
         SET @numOfParams = datalength(@invalidRequestIds) - datalength(replace(@invalidRequestIds, ',', '')) + 1
         
		 SET @select_statement = ''

		 SET @idx = 1
		 WHILE (1 = 1)
      
         BEGIN

            IF (@idx > @numOfParams)
               BREAK
			 
			/* SET @requestId = substring(@invalidRequestIds, , @idx) */
			SET @requestId = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX( @invalidRequestIds, ',',@idx), ',', -1 );

			/* SET @requestId = m2ss.substring_index(m2ss.substring_index([@invalidRequestIds], N',', [@idx]), N',', -1) */
			
            SET @select_statement = ('
               INSERT INTO [${dbxschemaname}].bbactedrequest(
                  [${dbxschemaname}].bbactedrequest.requestId, 
                  [${dbxschemaname}].bbactedrequest.companyId, 
                  [${dbxschemaname}].bbactedrequest.comments, 
                  [${dbxschemaname}].bbactedrequest.status, 
                  [${dbxschemaname}].bbactedrequest.action
               ) VALUES (') + (@requestId) + (',') + (@companyId) + (''' , ''Rejected by system as one of the approver lost his permission'', ''Rejected'', ''Rejected'');')
           
			SET @idx = @idx + 1

         END

      SET @SQL_SAFE_UPDATES = 1

   END
   MAINLABEL$leave:
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[bulkwiretemplate_delete_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[bulkwiretemplate_delete_proc]  
   @_bulkwiretemplateID nvarchar(50),
   @_bulkwiretemplatelineitemIDs nvarchar(max)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      declare @filter nvarchar(max)
	declare @totalCount nvarchar(max)
	declare @DomCount nvarchar(max)
	declare @InternationalCount nvarchar(max)

      IF @_bulkwiretemplateID IS NOT NULL AND @_bulkwiretemplateID <> ''
         IF @_bulkwiretemplatelineitemIDs IS NOT NULL AND @_bulkwiretemplatelineitemIDs <> ''
            BEGIN

               UPDATE [${dbxschemaname}].bulkwiretemplatelineitems SET softdeleteflag = 1 
               WHERE [${dbxschemaname}].FIND_IN_SET(CAST(bulkwiretemplatelineitems.bulkWireTemplateLineItemID AS nvarchar(max)), @_bulkwiretemplatelineitemIDs) <> 0
               
               SET @DomCount = 'SELECT count_big(*) FROM [${dbxschemaname}].bulkwiretemplatelineitems
               WHERE [${dbxschemaname}].bulkwiretemplatelineitems.bulkWireTemplateID = @_bulkwiretemplateID AND 
			   bulkwiretemplatelineitems.bulkWireTransferType = ''Domestic'' AND bulkwiretemplatelineitems.softdeleteflag = 0'
               
               SET @InternationalCount = 'SELECT count_big(*) FROM [${dbxschemaname}].bulkwiretemplatelineitems
               WHERE bulkwiretemplatelineitems.bulkWireTemplateID = @_bulkwiretemplateID AND 
               bulkwiretemplatelineitems.bulkWireTransferType = ''International'' AND bulkwiretemplatelineitems.softdeleteflag = 0'
              
               SET @totalCount = @DomCount + @InternationalCount
              
               UPDATE [${dbxschemaname}].bulkwiretemplate
                  SET 
                     noOfTransactions = @totalCount, 
                     noOfDomesticTransactions = @DomCount, 
                     noOfInternationalTransactions = @InternationalCount
               WHERE bulkwiretemplate.bulkWireTemplateID = @_bulkwiretemplateID
               
               SELECT bulkwiretemplate.bulkWireTemplateID, bulkwiretemplate.bulkWireTemplateName, bulkwiretemplate.noOfTransactions, bulkwiretemplate.noOfDomesticTransactions, bulkwiretemplate.noOfInternationalTransactions, bulkwiretemplate.createdBy, bulkwiretemplate.modifiedBy, bulkwiretemplate.createdts, bulkwiretemplate.lastmodifiedts, bulkwiretemplate.synctimestamp, bulkwiretemplate.company_id, bulkwiretemplate.softdeleteflag, bulkwiretemplate.lastExecutedOn, bulkwiretemplate.defaultFromAccount, bulkwiretemplate.defaultCurrency, bulkwiretemplate.deleteUniqueValue FROM [${dbxschemaname}].bulkwiretemplate WHERE bulkwiretemplate.bulkWireTemplateID = @_bulkwiretemplateID

            END
         ELSE 
            BEGIN

               /*
               *   SSMA warning messages:
               *   M2SS0183: The following SQL clause was ignored during conversion: COLLATE utf8_general_ci.
               */

               UPDATE [${dbxschemaname}].bulkwiretemplate
                  SET 
                     deleteUniqueValue = @_bulkwiretemplateID
               WHERE bulkwiretemplate.bulkWireTemplateID = @_bulkwiretemplateID

               /*
               *   SSMA warning messages:
               *   M2SS0183: The following SQL clause was ignored during conversion: COLLATE utf8_general_ci.
               */

               UPDATE [${dbxschemaname}].bulkwiretemplate
                  SET 
                     softdeleteflag = 1
               WHERE bulkwiretemplate.bulkWireTemplateID = @_bulkwiretemplateID

               /*
               *   SSMA warning messages:
               *   M2SS0183: The following SQL clause was ignored during conversion: COLLATE utf8_general_ci.
               */

               UPDATE [${dbxschemaname}].bulkwiretemplatelineitems
                  SET 
                     softdeleteflag = 1
               WHERE bulkwiretemplatelineitems.bulkWireTemplateID = @_bulkwiretemplateID

               SELECT N'SUCCESS' AS N'SUCCESS'

            END
      ELSE 
         SELECT N'FAILED' AS N'FAILED'

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[businesstyperole_get_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[businesstyperole_get_proc]  
   @_businessTypeId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT 
         groupbusinesstype.BusinessType_id AS businessTypeId, 
         membergroup.id AS groupId, 
         membergroup.Name AS groupName, 
         membergroup.Description AS groupDescription, 
         groupbusinesstype.isDefaultGroup AS isDefaultGroup
      FROM 
         [${dbxschemaname}].groupbusinesstype 
            LEFT JOIN [${dbxschemaname}].membergroup 
            ON (groupbusinesstype.Group_id = membergroup.id)
      WHERE groupbusinesstype.BusinessType_id = @_businessTypeId

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[campaign_c360_count_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[campaign_c360_count_proc]
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      /*
      *   SSMA warning messages:
      *   M2SS0104: Non aggregated column CHANNEL is aggregated with Min(..) in Select, Orderby and Having clauses.
      *   M2SS0104: Non aggregated column SCREEN is aggregated with Min(..) in Select, Orderby and Having clauses.
      *   M2SS0104: Non aggregated column IMAGE_RESOLUTION is aggregated with Min(..) in Select, Orderby and Having clauses.
      */

      SELECT min(campaignplaceholder.channel) AS channel, min(campaignplaceholder.screen) AS screen, min(campaignplaceholder.image_resolution) AS image_resolution, count_big(*) AS campaign_count
      FROM 
         [${dbxschemaname}].defaultcampaignspecification 
            LEFT JOIN [${dbxschemaname}].campaignplaceholder 
            ON (defaultcampaignspecification.campaignplaceholder_id = campaignplaceholder.id)
      GROUP BY campaignplaceholder.id
         ORDER BY campaignplaceholder.id

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[campaign_c360_customergroup_delete_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[campaign_c360_customergroup_delete_proc]  
   @_groupId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DECLARE
         @cursorFinished int = 0

      DECLARE
         @customerId nvarchar(50)

      DECLARE
          customerGroupCursor CURSOR LOCAL FORWARD_ONLY FOR 
            SELECT customergroup.Customer_id
            FROM [${dbxschemaname}].customergroup
            WHERE customergroup.Group_id = @_groupId

      OPEN customerGroupCursor

      WHILE (1 = 1)
      
         BEGIN

            FETCH customerGroupCursor
                INTO @customerId

            IF @@FETCH_STATUS <> 0
               SET @cursorFinished = 1

            IF @cursorFinished = 1
               BREAK

            DELETE 
            FROM [${dbxschemaname}].customergroup
            WHERE customergroup.Customer_id = @customerId AND customergroup.Group_id = @_groupId

         END

      CLOSE customerGroupCursor

      DEALLOCATE customerGroupCursor

   END
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[campaign_c360_datacontextsandattributes_get_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[campaign_c360_datacontextsandattributes_get_proc]
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT 
         model.id AS modelId, 
         model.name AS modelName, 
         model.endpoint_url AS endpoint, 
         attr.id AS attributeid, 
         attr.endpoint_attribute_id AS attributendpoint, 
         attr.name AS attributename, 
         attr.attributetype AS attributetype, 
         attr.range AS range, 
         attr.helptext AS helptext, 
         attr.criterias AS criterias, 
         attr.options AS options
      FROM 
         [${dbxschemaname}].attribute  AS attr 
            INNER JOIN [${dbxschemaname}].modelattribute  AS ma 
            ON attr.id = ma.attribute_id 
            INNER JOIN [${dbxschemaname}].model  AS model 
            ON ma.model_id = model.id
         ORDER BY model.name, attr.name DESC

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[campaign_c360_default_specification_get_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[campaign_c360_default_specification_get_proc]
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT 
         campaignplaceholder.id, 
         campaignplaceholder.channel, 
         campaignplaceholder.screen, 
         campaignplaceholder.image_resolution, 
         campaignplaceholder.image_scale, 
         defaultcampaignspecification.image_index, 
         defaultcampaignspecification.image_url, 
         defaultcampaignspecification.destination_url
      FROM 
         [${dbxschemaname}].defaultcampaignspecification 
            LEFT JOIN [${dbxschemaname}].campaignplaceholder 
            ON (defaultcampaignspecification.campaignplaceholder_id = campaignplaceholder.id)

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[campaign_c360_default_specification_manage_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[campaign_c360_default_specification_manage_proc]  
   @_channel nvarchar(50),
   @_screen nvarchar(50),
   @_imageIndex int,
   @_imageResolution nvarchar(10),
   @_imageURL nvarchar(200),
   @_destinationURL nvarchar(200),
   @_createdOrModifiedBy nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DECLARE
         @placeholderId nvarchar(50)

      DECLARE
         @currentImageURL nvarchar(200)

      SET @placeholderId = 
         (
            SELECT campaignplaceholder.id
            FROM [${dbxschemaname}].campaignplaceholder
            WHERE 
               campaignplaceholder.channel = @_channel AND 
               campaignplaceholder.screen = @_screen AND 
               campaignplaceholder.image_resolution = @_imageResolution
         )

      SET @currentImageURL = 
         (
            SELECT defaultcampaignspecification.image_url
            FROM [${dbxschemaname}].defaultcampaignspecification
            WHERE defaultcampaignspecification.campaignplaceholder_id = @placeholderId AND defaultcampaignspecification.image_index = @_imageIndex
         )

      IF @currentImageURL IS NULL
         INSERT [${dbxschemaname}].defaultcampaignspecification(
            [${dbxschemaname}].defaultcampaignspecification.campaignplaceholder_id, 
            [${dbxschemaname}].defaultcampaignspecification.image_index, 
            [${dbxschemaname}].defaultcampaignspecification.image_url, 
            [${dbxschemaname}].defaultcampaignspecification.destination_url, 
            [${dbxschemaname}].defaultcampaignspecification.createdby)
            VALUES (
               @placeholderId, 
               @_imageIndex, 
               @_imageURL, 
               @_destinationURL, 
               @_createdOrModifiedBy)

      IF @currentImageURL IS NOT NULL
         UPDATE [${dbxschemaname}].defaultcampaignspecification
            SET 
               image_url = defaultcampaignspecification.image_url, 
               destination_url = @_destinationURL, 
               modifiedby = @_createdOrModifiedBy
         WHERE defaultcampaignspecification.campaignplaceholder_id = @placeholderId AND defaultcampaignspecification.image_index = @_imageIndex

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[campaign_c360_group_get_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[campaign_c360_group_get_proc]  
   @_campaignId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT 
         membergroup.id AS group_id, 
         membergroup.Name AS group_name, 
         membergroup.Description AS group_desc, 
         groupattribute.admin_attributes AS attributes, 
         groupattribute.customer_count AS customer_count
      FROM 
         [${dbxschemaname}].membergroup 
            LEFT JOIN [${dbxschemaname}].campaigngroup 
            ON (membergroup.id = campaigngroup.group_id) 
            LEFT JOIN [${dbxschemaname}].groupattribute 
            ON (membergroup.id = groupattribute.group_id)
      WHERE campaigngroup.campaign_id = @_campaignId

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[campaign_c360_model_get_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[campaign_c360_model_get_proc]  
   @_attributeId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT model.id, model.name, model.endpoint_url
      FROM 
         [${dbxschemaname}].model 
            LEFT JOIN [${dbxschemaname}].modelattribute 
            ON (model.id = modelattribute.model_id)
      WHERE modelattribute.attribute_id = @_attributeId

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[campaign_c360_priority_update_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[campaign_c360_priority_update_proc]  
   @_priorityStart int,
   @_priorityEnd int
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON



      UPDATE [${dbxschemaname}].campaign
         SET 
            priority = campaign.priority + 1
      WHERE campaign.priority >= @_priorityStart AND campaign.priority < @_priorityEnd



   END
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[campaign_c360_specification_delete_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[campaign_c360_specification_delete_proc]  
   @_campaignId nvarchar(50),
   @_channel nvarchar(50),
   @_screen nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DECLARE
         @cursorFinished int = 0

      DECLARE
         @campaignPlaceholderId nvarchar(50)

      DECLARE
          campaignPlaceholderCursor CURSOR LOCAL FORWARD_ONLY FOR 
            SELECT campaignplaceholder.id
            FROM [${dbxschemaname}].campaignplaceholder
            WHERE campaignplaceholder.channel = @_channel AND campaignplaceholder.screen = @_screen

      OPEN campaignPlaceholderCursor

      /*
      *   SSMA informational messages:
      *   M2SS0003: The following SQL clause was ignored during conversion:
      *   getPlaceholder : .
      *   M2SS0003: The following SQL clause was ignored during conversion:
      *   getPlaceholder.
      */

      WHILE (1 = 1)
      
         BEGIN

            FETCH campaignPlaceholderCursor
                INTO @campaignPlaceholderId

            IF @@FETCH_STATUS <> 0
               SET @cursorFinished = 1

            IF @cursorFinished = 1
               BREAK

            DELETE 
            FROM [${dbxschemaname}].campaignspecification
            WHERE campaignspecification.campaign_id = @_campaignId AND campaignspecification.campaignplaceholder_id = @campaignPlaceholderId

         END

      CLOSE campaignPlaceholderCursor

      DEALLOCATE campaignPlaceholderCursor

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[campaign_c360_specification_get_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[campaign_c360_specification_get_proc]  
   @_campaignId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT 
         campaignspecification.campaign_id, 
         campaignplaceholder.channel, 
         campaignplaceholder.screen, 
         campaignplaceholder.image_resolution, 
         campaignplaceholder.image_scale, 
         campaignspecification.image_url, 
         campaignspecification.destination_url
      FROM 
         [${dbxschemaname}].campaignspecification 
            LEFT JOIN [${dbxschemaname}].campaignplaceholder 
            ON (campaignspecification.campaignplaceholder_id = campaignplaceholder.id)
      WHERE campaignspecification.campaign_id = @_campaignId

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[campaign_c360_specification_manage_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[campaign_c360_specification_manage_proc]  
   @_campaignId nvarchar(50),
   @_channel nvarchar(50),
   @_screen nvarchar(50),
   @_imageResolution nvarchar(10),
   @_imageURL nvarchar(200),
   @_destinationURL nvarchar(200),
   @_userId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DECLARE
         @placeholderId nvarchar(50)

      DECLARE
         @existingImageURL nvarchar(200)

      SET @placeholderId = 
         (
            SELECT campaignplaceholder.id
            FROM [${dbxschemaname}].campaignplaceholder
            WHERE 
               campaignplaceholder.channel = @_channel AND 
               campaignplaceholder.screen = @_screen AND 
               campaignplaceholder.image_resolution = @_imageResolution
         )

      SET @existingImageURL = 
         (
            SELECT campaignspecification.image_url
            FROM [${dbxschemaname}].campaignspecification
            WHERE campaignspecification.campaign_id = @_campaignId AND campaignspecification.campaignplaceholder_id = @placeholderId
         )

		 declare @mainQuery nvarchar(max)
      IF @existingImageURL IS NULL
         BEGIN
            SET @mainQuery = (N'insert into campaignspecification ') + (N'(`campaign_id`, `campaignplaceholder_id`, `image_url`, ')

            IF @_destinationURL <> ''

               SET @mainQuery = (@mainQuery) + (N'`destination_url`, ')

            SET @mainQuery = 
               (@mainQuery)
                + 
               (N'`createdby`) values (')
                + 
               ((QUOTENAME((@_campaignId), '''')))
                + 
               (N', ')
                + 
               ((QUOTENAME((@placeholderId), '''')))
                + 
               (N', ')
                + 
               ''''+(@_imageURL)+''''
                + 
               (N', ')

            IF @_destinationURL <> ''

               SET @mainQuery = (@mainQuery) +  ''''+(@_destinationURL)+''''  + (N', ')


            SET @mainQuery = (@mainQuery) + ((QUOTENAME((@_userId), ''''))) + (N')')


           EXEC(@mainQuery)

         END

      IF @existingImageURL IS NOT NULL
         UPDATE [${dbxschemaname}].campaignspecification
            SET 
               image_url = @_imageURL, 
               modifiedby = @_userId, 
               destination_url = @_destinationURL
         WHERE campaignspecification.campaign_id = @_campaignId AND campaignspecification.campaignplaceholder_id = @placeholderId

   END
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[campaign_dbp_count_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[campaign_dbp_count_proc]
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      /*
      *   SSMA warning messages:
      *   M2SS0088: Unable to determine if GROUP BY clause contains constant expression or alias, because it contains unresolved identifiers.
      */

      SELECT campaignplaceholder_groupby_id.channel, campaignplaceholder_groupby_id.screen, campaignplaceholder_groupby_id.campaign_count
      FROM 
         (
            /*
            *   SSMA warning messages:
            *   M2SS0104: Non aggregated column CHANNEL is aggregated with Min(..) in Select, Orderby and Having clauses.
            *   M2SS0104: Non aggregated column SCREEN is aggregated with Min(..) in Select, Orderby and Having clauses.
            */

            SELECT TOP (9223372036854775807) min(campaignplaceholder.channel) AS channel, min(campaignplaceholder.screen) AS screen, count_big(*) AS campaign_count
            FROM 
               [${dbxschemaname}].defaultcampaignspecification 
                  LEFT JOIN [${dbxschemaname}].campaignplaceholder 
                  ON (defaultcampaignspecification.campaignplaceholder_id = campaignplaceholder.id)
            GROUP BY campaignplaceholder.id
               ORDER BY campaignplaceholder.id
         )  AS campaignplaceholder_groupby_id
      GROUP BY campaignplaceholder_groupby_id.channel, campaignplaceholder_groupby_id.screen, campaignplaceholder_groupby_id.campaign_count
         ORDER BY campaignplaceholder_groupby_id.channel, campaignplaceholder_groupby_id.screen, campaignplaceholder_groupby_id.campaign_count

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[campaign_dbp_display_count_update_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[campaign_dbp_display_count_update_proc]  
   @_campaignId nvarchar(50),
   @_campaignPlaceholderId nvarchar(50),
   @_imageIndex int
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DECLARE
         @displayCount int

      IF @_campaignId = 'DEFAULT_CAMPAIGN'
         BEGIN

            SET @displayCount = 
               (
                  SELECT (defaultcampaignspecification.display_count + 1)
                  FROM [${dbxschemaname}].defaultcampaignspecification
                  WHERE defaultcampaignspecification.campaignplaceholder_id = @_campaignPlaceholderId AND defaultcampaignspecification.image_index = @_imageIndex
               )

            UPDATE [${dbxschemaname}].defaultcampaignspecification
               SET 
                  display_count = @displayCount
            WHERE defaultcampaignspecification.campaignplaceholder_id = @_campaignPlaceholderId AND defaultcampaignspecification.image_index = @_imageIndex

         END

      IF @_campaignId <> 'DEFAULT_CAMPAIGN'
         BEGIN

            SET @displayCount = 
               (
                  SELECT (campaignspecification.display_count + 1)
                  FROM [${dbxschemaname}].campaignspecification
                  WHERE campaignspecification.campaign_id = @_campaignId AND campaignspecification.campaignplaceholder_id = @_campaignPlaceholderId
               )

            UPDATE [${dbxschemaname}].campaignspecification
               SET 
                  display_count = @displayCount
            WHERE campaignspecification.campaign_id = @_campaignId AND campaignspecification.campaignplaceholder_id = @_campaignPlaceholderId

         END

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[campaign_dbp_specification_get_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[campaign_dbp_specification_get_proc]  
   @_currentTimestamp nvarchar(20),
   @_scale nvarchar(10),
   @_username nvarchar(50),
   @_channel nvarchar(50),
   @_screen nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DECLARE
         @placeholderId nvarchar(50)

      DECLARE
         @customerId nvarchar(50)

      SET @placeholderId = 
         (
            SELECT campaignplaceholder.id
            FROM [${dbxschemaname}].campaignplaceholder
            WHERE 
               campaignplaceholder.channel = @_channel AND 
               campaignplaceholder.screen = @_screen AND 
               campaignplaceholder.image_scale = @_scale
         ) 

      SET @customerId = 
         (
            SELECT customer.id
            FROM [${dbxschemaname}].customer
            WHERE customer.UserName = @_username
         )


      SELECT 
         campaignspecification.campaign_id, 
         campaignspecification.campaignplaceholder_id, 
         campaignspecification.image_url, 
         campaignspecification.destination_url, 
         1 AS image_index, 
         campaign.priority
      FROM 
         [${dbxschemaname}].campaignspecification 
            LEFT JOIN [${dbxschemaname}].campaign 
            ON (campaignspecification.campaign_id = campaign.id)
      WHERE 
         campaignspecification.campaignplaceholder_id = @placeholderId AND 
         campaign.status_id = 'SID_SCHEDULED_ACTIVE_COMPLETED' AND 
         campaignspecification.campaign_id IN 
         (
            SELECT campaigngroup.campaign_id
            FROM [${dbxschemaname}].campaigngroup
            WHERE campaigngroup.group_id IN 
               (
                  SELECT customergroup.Group_id
                  FROM [${dbxschemaname}].customergroup
                  WHERE customergroup.Customer_id = @customerId
               )
         ) AND 
         campaign.start_datetime <= convert(datetime,@_currentTimestamp,120) AND
         campaign.end_datetime >= convert(datetime,@_currentTimestamp,120) AND 
         campaign.id NOT IN 
         (
            SELECT custcompletedcampaign.campaign_id
            FROM [${dbxschemaname}].custcompletedcampaign
            WHERE custcompletedcampaign.customer_id = @customerId
         )
       UNION ALL
      SELECT 
         N'DEFAULT_CAMPAIGN' AS campaign_id, 
         defaultcampaignspecification.campaignplaceholder_id, 
         defaultcampaignspecification.image_url, 
         defaultcampaignspecification.destination_url, 
         defaultcampaignspecification.image_index, 
         1000 AS priority
      FROM [${dbxschemaname}].defaultcampaignspecification
      WHERE defaultcampaignspecification.campaignplaceholder_id = @placeholderId
         ORDER BY campaign.priority, image_index
   END
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[campaign_dbp_specification_prelogin_get_proc]    Script Date: 6/9/2020 2:00:56 PM ******/

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[campaign_dbp_specification_prelogin_get_proc]  
   @_currentTimestamp nvarchar(16),
   @_scale nvarchar(10),
   @_deviceId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DECLARE
         @placeholderId nvarchar(50)

      DECLARE
         @customerId nvarchar(50)

      SET @placeholderId = 
         (
            SELECT campaignplaceholder.id
            FROM [${dbxschemaname}].campaignplaceholder
            WHERE 
               campaignplaceholder.channel = 'MOBILE' AND 
               campaignplaceholder.screen = 'PRE_LOGIN' AND 
               campaignplaceholder.image_scale = @_scale
         )

      SET @customerId = 
         (


            SELECT TOP (1) customerdevice.Customer_id
            FROM [${dbxschemaname}].customerdevice
            WHERE customerdevice.id = @_deviceId AND customerdevice.Channel_id = 'CH_ID_MOB'
               ORDER BY customerdevice.LastLoginTime DESC
         )


      SELECT 
         campaignspecification.campaign_id, 
         campaignspecification.campaignplaceholder_id, 
         campaignspecification.image_url, 
         campaignspecification.destination_url, 
         1 AS image_index, 
         campaign.priority
      FROM 
         [${dbxschemaname}].campaignspecification 
            LEFT JOIN [${dbxschemaname}].campaign 
            ON (campaignspecification.campaign_id = campaign.id)
      WHERE 
         campaignspecification.campaignplaceholder_id = @placeholderId AND 
         campaign.status_id = 'SID_SCHEDULED_ACTIVE_COMPLETED' AND 
         campaign.id IN 
         (
            SELECT campaigngroup.campaign_id
            FROM [${dbxschemaname}].campaigngroup
            WHERE campaigngroup.group_id IN 
               (
                  SELECT customergroup.Group_id
                  FROM [${dbxschemaname}].customergroup
                  WHERE customergroup.Customer_id = @customerId
               )
         ) AND 
         campaign.start_datetime <= convert(datetime,@_currentTimestamp,121) AND 
         campaign.end_datetime >= convert(datetime,@_currentTimestamp, 121) AND 
         campaign.id NOT IN 
         (
            SELECT custcompletedcampaign.campaign_id
            FROM [${dbxschemaname}].custcompletedcampaign
            WHERE custcompletedcampaign.customer_id = @customerId
         )
       UNION ALL
      SELECT 
         N'DEFAULT_CAMPAIGN' AS campaign_id, 
         defaultcampaignspecification.campaignplaceholder_id, 
         defaultcampaignspecification.image_url, 
         defaultcampaignspecification.destination_url, 
         defaultcampaignspecification.image_index, 
         1000 AS priority
      FROM [${dbxschemaname}].defaultcampaignspecification
      WHERE defaultcampaignspecification.campaignplaceholder_id = @placeholderId
         ORDER BY campaign.priority, image_index


   END
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[custom_role_details_fetch_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[custom_role_details_fetch_proc]  
   @customRoleID nvarchar(max)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

	  DECLARE @selectDetails nvarchar(max)

      SET @selectDetails = 'select customroleactionlimits.customRole_id,    customroleactionlimits.action_id,   customroleactionlimits.account_id, customroleactionlimits.isAllowed,    customroleactionlimits.limitType_id, customroleactionlimits.value, accounts.AccountName as accountName, featureaction.isAccountLevel, featureaction.Type_id as actionType, featureaction.name as actionName, featureaction.description as actionDescription, feature.name as featureName, feature.description as featureDescription, feature.id as featureId FROM ([${dbxschemaname}].customroleactionlimits LEFT JOIN [${dbxschemaname}].accounts ON (customroleactionlimits.account_id = accounts.Account_id) LEFT JOIN [${dbxschemaname}].featureaction ON (customroleactionlimits.action_id = featureaction.id) LEFT JOIN [${dbxschemaname}].feature ON ( featureaction.Feature_id = feature.id )) WHERE customroleactionlimits.customRole_id = ' + (@customRoleID) + (N' ;')

	  EXEC (@selectDetails) 

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[customer_action_group_action_limits_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[customer_action_group_action_limits_proc]  
   @_customerID nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT 
         customeractionlimit.Customer_id AS Customer_id, 
         customeractionlimit.LimitType_id AS Customer_LimitType_id, 
         (N'') + (customeractionlimit.Value) AS Customer_Limit_Value, 
         NULL AS Group_LimitType_id, 
         NULL AS Group_Limit_Value, 
         service.id AS Action_id, 
         service.Name AS Action_Name, 
         service.code AS Action_Code, 
         service.Description AS Action_Description, 
         service.Status_id AS Action_Status_id, 
         app.id AS App_id, 
         app.Name AS App_Name, 
         appactionlimit.LimitType_id AS LimitType_id, 
         (N'') + (appactionlimit.Value) AS Limit_Value, 
         feature.id AS Feature_id, 
         feature.name AS Feature_Name, 
         feature.code AS Feature_Code, 
         feature.description AS Feature_Description, 
         feature.Status_id AS Feature_Status_id
      FROM ((((([${dbxschemaname}].customeractionlimit 
         INNER JOIN [${dbxschemaname}].app 
         ON ((appaction.App_id = app.id))) 
         INNER JOIN [${dbxschemaname}].service 
         ON ((service.id = customeractionlimit.Action_id))) 
         LEFT JOIN [${dbxschemaname}].feature 
         ON ((feature.id = service.Feature_id))) 
         LEFT JOIN [${dbxschemaname}].appaction 
         ON ((service.id = appaction.Action_id))) 
         LEFT JOIN appactionlimit 
         ON ((appaction.id = appactionlimit.AppAction_id)))
      WHERE customeractionlimit.Customer_id = @_customerID
       UNION

      SELECT 
         customergroup.Customer_id AS Customer_id, 
         NULL AS Customer_LimitType_id, 
         NULL AS Customer_Limit_Value, 
         groupactionlimit.LimitType_id AS Group_LimitType_id, 
         (N'') + (CAST(groupactionlimit.value AS varchar(50))) AS Group_Limit_Value, 
         service.id AS Action_id, 
         service.Name AS Action_Name, 
         service.code AS Action_Code, 
         service.Description AS Action_Description, 
         service.Status_id AS Action_Status_id, 
         app.id AS App_id, 
         app.Name AS App_Name, 
         appactionlimit.LimitType_id AS LimitType_id, 
         (N'') + (appactionlimit.Value) AS Limit_Value, 
         feature.id AS Feature_id, 
         feature.name AS Feature_Name, 
         feature.code AS Feature_Code, 
         feature.description AS Feature_Description, 
         feature.Status_id AS Feature_Status_id
      FROM (((((([${dbxschemaname}].customergroup 
         INNER JOIN [${dbxschemaname}].service 
         ON ((service.id = groupactionlimit.Action_id))) 
         INNER JOIN [${dbxschemaname}].app 
         ON ((appaction.App_id = app.id))) 
         LEFT JOIN [${dbxschemaname}].groupactionlimit 
         ON ((groupactionlimit.Group_id = customergroup.Group_id))) 
         LEFT JOIN [${dbxschemaname}].feature 
         ON ((feature.id = service.Feature_id))) 
         LEFT JOIN [${dbxschemaname}].appaction 
         ON ((service.id = appaction.Action_id))) 
         LEFT JOIN appactionlimit 
         ON ((appaction.id = appactionlimit.AppAction_id)))
      WHERE 
         customergroup.Customer_id = @_customerID AND 
         service.Status_id <> 'SID_INACTIVE' AND 
         feature.Status_id <> 'SID_INACTIVE'

   END
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[customer_actions_delete]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[customer_actions_delete]  
   @_customerId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DELETE 
      FROM [${dbxschemaname}].customeraction
      WHERE customeraction.Customer_id = @_customerId

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[customer_actions_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[customer_actions_proc]  
   @_customerId nvarchar(50),
   @_actionId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
	  declare @orgId nvarchar(max)

      SET @orgId = 
         (
            SELECT customer.Organization_Id
            FROM [${dbxschemaname}].customer
            WHERE customer.id = @_customerId
         )

      IF @orgId IS NULL
         SET @orgId = N''

		declare @active_features nvarchar(max)
      SET @active_features = 
         (

            SELECT String_agg(cast(feature.id  as NVARCHAR(MAX)),',')
            FROM [${dbxschemaname}].feature
            WHERE feature.Status_id = 'SID_FEATURE_ACTIVE'

         )

      IF @active_features IS NULL
         SET @active_features = N''

		 declare @active_actions nvarchar(max)
      SET @active_actions = 
         (

            SELECT String_agg(cast(featureaction.id as NVARCHAR(MAX)),',')
            FROM [${dbxschemaname}].featureaction
            WHERE [${dbxschemaname}].FIND_IN_SET(featureaction.Feature_id, @active_features) <> 0

         )
 
      IF @active_actions IS NULL
         SET @active_actions = N''

		 declare @organization_active_features nvarchar(max)
      SET @organization_active_features = 
         (

            SELECT String_agg(cast(organisationfeatures.featureId as NVARCHAR(MAX)),',')
            FROM [${dbxschemaname}].organisationfeatures
            WHERE 
               (organisationfeatures.featureStatus IS NULL OR organisationfeatures.featureStatus = 'SID_FEATURE_ACTIVE') AND 
               [${dbxschemaname}].FIND_IN_SET(organisationfeatures.featureId, @active_features) <> 0 AND 
               organisationfeatures.organisationId = @orgId

         )

      IF @organization_active_features IS NULL
         SET @organization_active_features = N''
       
	   declare @organization_active_actions nvarchar(max)
      SET @organization_active_actions = 
         (

            SELECT String_agg(cast(featureaction.id as NVARCHAR(MAX)),',')
            FROM [${dbxschemaname}].featureaction
            WHERE [${dbxschemaname}].FIND_IN_SET(featureaction.Feature_id, @organization_active_features) <> 0

         )

      IF @organization_active_actions IS NULL
         SET @organization_active_actions = N''

		 declare @business_customer_enabled_actions nvarchar(max)
      SET @business_customer_enabled_actions = 
         (

            SELECT String_agg(cast(customeraction.Action_id as NVARCHAR(MAX)),',')
            FROM [${dbxschemaname}].customeraction
            WHERE 
               customeraction.isAllowed = 1 AND 
               customeraction.Customer_id = @_customerId AND 
               [${dbxschemaname}].FIND_IN_SET(customeraction.Action_id, @organization_active_actions) <> 0
  
         )

      IF @business_customer_enabled_actions IS NULL
         SET @business_customer_enabled_actions = N''

		 declare @customer_disabled_actions nvarchar(max)
      SET @customer_disabled_actions = 
         (

            SELECT String_agg(cast(customeraction.Action_id as NVARCHAR(MAX)),',')
            FROM [${dbxschemaname}].customeraction
            WHERE 
               customeraction.isAllowed = 0 AND 
               customeraction.Account_id IS NULL AND 
               customeraction.Customer_id = @_customerId
  
         )

      IF @customer_disabled_actions IS NULL
         SET @customer_disabled_actions = N''

		 declare @customer_groups nvarchar(max)
      SET @customer_groups = 
         (

            SELECT String_agg(cast(customergroup.Group_id  as NVARCHAR(MAX)),',')
            FROM [${dbxschemaname}].customergroup
            WHERE customergroup.Customer_id = @_customerId
 
         )

      IF @customer_groups IS NULL
         SET @customer_groups = N''

		 declare @group_actions nvarchar(max)
      SET @group_actions = 
         (

            SELECT String_agg(cast(groupactionlimit.Action_id  as NVARCHAR(MAX)),',')
            FROM [${dbxschemaname}].groupactionlimit
            WHERE [${dbxschemaname}].FIND_IN_SET(groupactionlimit.Group_id, @customer_groups) <> 0

         )

      IF @group_actions IS NULL
         SET @group_actions = N''

 declare @active_feature_condition nvarchar(max)
 declare @select_statement nvarchar(max)

      IF @orgId = ''
         SET @active_feature_condition = 'feature.Status_id =''SID_FEATURE_ACTIVE'' AND [${dbxschemaname}].FIND_IN_SET(featureaction.id,CAST(''' + (@group_actions) + (''' as nvarchar(max))) <> 0')
	  ELSE 
      SET @active_feature_condition = '[${dbxschemaname}].FIND_IN_SET(featureaction.id, CAST(''' + (@organization_active_actions) + (''' as nvarchar(max))) <> 0 AND [${dbxschemaname}].FIND_IN_SET(featureaction.id,CAST( ''' + (@group_actions) + (''' as nvarchar(max))) <> 0'))
      SET @select_statement = 'SELECT customeraction.Customer_id AS Customer_id,customeraction.Account_id AS Account_id, case when customeraction.isAllowed = ''1'' then ''true'' else ''false'' end AS isAllowed,featureaction.id AS Action_id, case when featureaction.isAccountLevel = ''1'' then ''true'' else ''false'' end AS isAccountLevel,feature.Status_id AS Feature_Status_id,feature.id AS Feature_id,customeraction.RoleType_id AS RoleType_id,customeraction.LimitType_id AS LimitType_id,customeraction.value AS value FROM [${dbxschemaname}].customeraction LEFT JOIN [${dbxschemaname}].featureaction ON (featureaction.id = customeraction.Action_id) LEFT JOIN [${dbxschemaname}].feature ON (feature.id = featureaction.Feature_id) where customeraction.Customer_id = ''' + (@_customerId) + (''' and ') + (@active_feature_condition)

      IF (@_actionId <> '')
 
         SET @select_statement = (@select_statement) + (N' and customeraction.Action_id = ') + ''''+@_actionId+''''

      SET @select_statement = 
         (@select_statement)
          + 
         ' UNION SELECT customergroup.Customer_id AS Customer_id,NULL AS Account_id, case when (membergroup.Type_id = ''TYPE_ID_BUSINESS'') then (case when [${dbxschemaname}].FIND_IN_SET(featureaction.id, CAST('''
          + 
         (@business_customer_enabled_actions)
          + 
         (''' as nvarchar(max))) <> 0 then ''true'' else ''false'' end) else ''true'' end AS isAllowed,featureaction.id AS Action_id, case when featureaction.isAccountLevel = ''1'' then ''true'' else ''false'' end AS isAccountLevel,feature.Status_id AS Feature_Status_id,feature.id AS Feature_id,membergroup.Type_id AS RoleType_id,groupactionlimit.LimitType_id AS LimitType_id,groupactionlimit.value AS value FROM ([${dbxschemaname}].customergroup LEFT JOIN [${dbxschemaname}].membergroup ON (membergroup.id = customergroup.Group_id) LEFT JOIN [${dbxschemaname}].groupactionlimit ON (groupactionlimit.Group_id = customergroup.Group_id) LEFT JOIN [${dbxschemaname}].featureaction ON (featureaction.id = groupactionlimit.Action_id) LEFT JOIN [${dbxschemaname}].feature ON (feature.id = featureaction.Feature_id)) where customergroup.Customer_id ='''
          + 
         (@_customerId)
          + 
         ''' and feature.Status_id = ''SID_FEATURE_ACTIVE'' and [${dbxschemaname}].FIND_IN_SET(groupactionlimit.Action_id, CAST(''')
          + 
         (@customer_disabled_actions)
          + 
         (''' as nvarchar(max))) = 0 and ')
          + 
         (@active_feature_condition)
  

      IF (@_actionId <> '')

         SET @select_statement = (@select_statement) + (N' and groupactionlimit.Action_id = ') + ''''+(@_actionId)+''''

      exec(@select_statement)

   END
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[customer_basic_info_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[customer_basic_info_proc]  
   @_customerId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
	  declare @accountLockoutThreshold nvarchar(max)
	  declare @accountLockoutTime nvarchar(max)

	  SET @accountLockoutThreshold = (SELECT accountLockoutThreshold from [${dbxschemaname}].passwordlockoutsettings)
	  SET @accountLockoutTime =(SELECT accountLockoutTime from [${dbxschemaname}].passwordlockoutsettings)  

      SELECT TOP (1) 
         customer.UserName AS Username, 
         customer.FirstName AS FirstName, 
         customer.MiddleName AS MiddleName, 
         customer.LastName AS LastName, 
         (ISNULL(customer.FirstName, N'')) + (N' ') + (ISNULL(customer.MiddleName, N'')) + (N' ') + (ISNULL(customer.LastName, N'')) AS Name, 
         customer.Salutation AS Salutation, 
         customer.id AS Customer_id, 
         (N'****') + (right(customer.Ssn, 4)) AS SSN, 
         customer.createdts AS CustomerSince, 
         customer.Gender AS Gender, 
         customer.DateOfBirth AS DateOfBirth, 
         CASE 
            WHEN (customer.Status_id = 'SID_CUS_SUSPENDED') THEN customer.Status_id
            ELSE CASE 
               WHEN (customer.lockCount + 1 >= @accountLockoutThreshold) THEN N'SID_CUS_LOCKED'
               ELSE customer.Status_id
            END
         END AS CustomerStatus_id, 
         customerstatus.Description AS CustomerStatus_name, 
         customer.MaritalStatus_id AS MaritalStatus_id, 
         maritalstatus.Description AS MaritalStatus_name, 
         customer.SpouseName AS SpouseName, 
         customer.DrivingLicenseNumber AS DrivingLicenseNumber, 
         customer.lockedOn AS lockedOn, 
         customer.lockCount AS lockCount, 
         customer.EmployementStatus_id AS EmployementStatus_id, 
         employementstatus.Description AS EmployementStatus_name, 
         
            (

               SELECT String_agg(Status_id,',')
               FROM [${dbxschemaname}].customerflagstatus
               WHERE (customerflagstatus.Customer_id = customer.id)

            ) AS CustomerFlag_ids, 
         
            (

               SELECT String_agg(CAST(Description as nvarchar(max)),',')
               FROM [${dbxschemaname}].status
               WHERE status.id IN 
                  (
                     SELECT customerflagstatus.Status_id
                     FROM [${dbxschemaname}].customerflagstatus
                     WHERE (customerflagstatus.Customer_id = customer.id)
                  )
 
            ) AS CustomerFlag, 
         customer.IsEnrolledForOlb AS IsEnrolledForOlb, 
         customer.IsStaffMember AS IsStaffMember, 
         customer.Location_id AS Branch_id, 
         location.Name AS Branch_name, 
         location.Code AS Branch_code, 
         customer.IsOlbAllowed AS IsOlbAllowed, 
         customer.IsAssistConsented AS IsAssistConsented, 
         customer.isEagreementSigned AS isEagreementSigned, 
         customer.CustomerType_id AS CustomerType_id, 
         customertype.Name AS CustomerType_Name, 
         customertype.Description AS CustomerType_Description, 
         membergroup.Name AS Customer_Role, 
         membergroup.isEAgreementActive AS isEAgreementRequired, 
         customer.Organization_Id AS organisation_id, 
         organisation.Name AS organisation_name, 
         primaryphone.Value AS PrimaryPhoneNumber, 
         primaryemail.Value AS PrimaryEmailAddress, 
         customer.DocumentsSubmitted AS DocumentsSubmitted, 
         customer.ApplicantChannel AS ApplicantChannel, 
         customer.Product AS Product, 
         customer.Reason AS Reason, 
         @accountLockoutTime AS accountLockoutTime
      FROM (((((((([${dbxschemaname}].customer 
         LEFT JOIN [${dbxschemaname}].location 
         ON ((customer.Location_id = location.id))) 
         LEFT JOIN [${dbxschemaname}].organisation 
         ON ((customer.Organization_Id = organisation.id))) 
         INNER JOIN [${dbxschemaname}].customertype 
         ON ((customer.CustomerType_id = customertype.id))) 
         LEFT JOIN [${dbxschemaname}].status  AS customerstatus 
         ON ((customer.Status_id = customerstatus.id))) 
         LEFT JOIN [${dbxschemaname}].status  AS maritalstatus 
         ON ((customer.MaritalStatus_id = maritalstatus.id))) 
         LEFT JOIN [${dbxschemaname}].status  AS employementstatus 
         ON ((customer.EmployementStatus_id = employementstatus.id))) 
         LEFT JOIN [${dbxschemaname}].customercommunication  AS primaryphone 
         ON ((
            (primaryphone.Customer_id = customer.id) AND 
            (primaryphone.isPrimary = 1) AND 
            (primaryphone.Type_id = 'COMM_TYPE_PHONE')))) 
         LEFT JOIN [${dbxschemaname}].customercommunication  AS primaryemail 
         ON ((
            (primaryemail.Customer_id = customer.id) AND 
            (primaryemail.isPrimary = 1) AND 
            (primaryemail.Type_id = 'COMM_TYPE_EMAIL'))) 
         LEFT JOIN [${dbxschemaname}].customergroup 
         ON ((customer.id = customergroup.Customer_id)) 
         LEFT JOIN [${dbxschemaname}].membergroup 
         ON ((membergroup.id = customergroup.Group_id)))
      WHERE customer.id = @_customerId

   END
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[customer_eagreement_get_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[customer_eagreement_get_proc]  
   @_customerId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT membergroup.isEAgreementActive AS isEAgreementActive
      FROM 
         [${dbxschemaname}].membergroup 
            LEFT JOIN [${dbxschemaname}].customergroup 
            ON (customergroup.Group_id = membergroup.id)
      WHERE customergroup.Customer_id = @_customerId AND membergroup.Type_id = 'TYPE_ID_BUSINESS'

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[customer_entitlements_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[customer_entitlements_proc]  
   @_customerID nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      /*
      *   SSMA informational messages:
      *   M2SS0052: string literal was converted to NUMERIC literal
      */

      SELECT 
         service.id AS serviceId, 
         service.Name AS serviceName, 
         service.Description AS serviceDesc, 
         service.Notes AS serviceNotes, 
         CASE 
            WHEN (customerentitlement.MaxTransactionLimit <> 0 AND CASE 
               WHEN NOT CASE 
                  WHEN CAST(customerentitlement.MaxTransactionLimit AS varchar(50)) IS NULL THEN 1
                  ELSE 0
               END <> 0 THEN 1
               ELSE 
                  0
            END <> 0) THEN customerentitlement.MaxTransactionLimit
            ELSE service.MaxTransferLimit
         END AS maxTransferLimit, 
         service.MinTransferLimit AS minTransferLimit, 
         service.DisplayName AS displayName, 
         service.DisplayDescription AS displayDesc
      FROM 
         [${dbxschemaname}].customerentitlement 
            INNER JOIN [${dbxschemaname}].service 
            ON (service.id = customerentitlement.Service_id)
      WHERE customerentitlement.Customer_id = @_customerID
       UNION
      SELECT 
         service.id AS serviceId, 
         service.Name AS serviceName, 
         service.Description AS serviceDesc, 
         service.Notes AS serviceNotes, 
         service.MaxTransferLimit AS maxTransferLimit, 
         service.MinTransferLimit AS minTransferLimit, 
         service.DisplayName AS displayName, 
         service.DisplayDescription AS displayDesc
      FROM 
         [${dbxschemaname}].customergroup 
            INNER JOIN [${dbxschemaname}].groupentitlement 
            ON (groupentitlement.Group_id = customergroup.Group_id) 
            INNER JOIN [${dbxschemaname}].service 
            ON (service.id = groupentitlement.Service_id)
      WHERE customergroup.Customer_id = @_customerID

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[customer_group_actions_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[customer_group_actions_proc]  
   @_customerId nvarchar(50),
   @_actionId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

	  declare @isCombinedUser int

      SET @isCombinedUser = 
         (
            SELECT customer.isCombinedUser
            FROM [${dbxschemaname}].customer
            WHERE customer.id = @_customerId
         )

	  declare @customer_disabled_actions nvarchar(max)
	  declare @customer_groups nvarchar(max)

      IF @isCombinedUser = 1
         BEGIN

            SET @customer_disabled_actions = 
               (

                  SELECT string_agg(CAST(customeraction.Action_id  as nvarchar(max)), ',')
                  FROM [${dbxschemaname}].customeraction
                  WHERE 
                     customeraction.isAllowed = 0 AND 
                     customeraction.Account_id IS NULL AND 
                     customeraction.Customer_id = @_customerId
               )


            IF @customer_disabled_actions IS NULL
               SET @customer_disabled_actions = N''

            SET @customer_groups = 
               (
                  SELECT string_agg(CAST(Group_id  as nvarchar(max)), ',')
                  FROM [${dbxschemaname}].customergroup
                  WHERE customergroup.Customer_id = @_customerId AND customergroup.Group_id IN 
                     (
                        SELECT membergroup.id
                        FROM [${dbxschemaname}].membergroup
                        WHERE membergroup.Type_id = 'TYPE_ID_RETAIL'
                     )

               )


            IF @customer_groups IS NULL
               SET @customer_groups = N''

			declare @group_actions nvarchar(max)
			declare @active_feature_condition nvarchar(max)
			declare @select_statement nvarchar(max)

            SET @group_actions = 
               (

                  SELECT string_agg(CAST(Action_id  as nvarchar(max)), ',')
                  FROM [${dbxschemaname}].groupactionlimit
                  WHERE [${dbxschemaname}].FIND_IN_SET(groupactionlimit.Group_id, @customer_groups) <> 0

               )

            IF @group_actions IS NULL

               SET @group_actions = N''

            SET @active_feature_condition = ('feature.Status_id = ''SID_FEATURE_ACTIVE'' AND [${dbxschemaname}].FIND_IN_SET(featureaction.id, CAST(''') + (@group_actions) + (''' as nvarchar(max))) <> 0')


            SET @select_statement = 
               ('SELECT '+' customergroup.Customer_id AS Customer_id,'+' NULL AS Account_id,'+ ' ''true'' AS isAllowed,'+'  featureaction.id AS Action_id,'+' feature.Status_id AS Feature_Status_id,'+'  feature.id AS Feature_id,'+' case when featureaction.isAccountLevel = ''1'' then ''true'' else ''false'' end AS isAccountLevel,'+'        membergroup.Type_id AS RoleType_id,'+'        groupactionlimit.LimitType_id AS LimitType_id,'+'        groupactionlimit.value AS value'+'    FROM '+'        ([${dbxschemaname}].customergroup'+'  LEFT JOIN [${dbxschemaname}].membergroup ON (membergroup.id = customergroup.Group_id and membergroup.Type_id = ''TYPE_ID_RETAIL'')'+'   LEFT JOIN [${dbxschemaname}].groupactionlimit ON (groupactionlimit.Group_id = customergroup.Group_id)'+'   LEFT JOIN [${dbxschemaname}].featureaction ON (featureaction.id = groupactionlimit.Action_id)'+'        LEFT JOIN [${dbxschemaname}].featureactionroletype ON (featureactionroletype.Action_id = featureaction.id and featureactionroletype.RoleType_id= ''TYPE_ID_RETAIL'')'+'        LEFT JOIN [${dbxschemaname}].feature ON (feature.id = featureaction.Feature_id))'+'      where customergroup.Customer_id =')
                + 
               ((QUOTENAME((@_customerId), '''')))
                + 
               (' and feature.Status_id = ''SID_FEATURE_ACTIVE'''+'  and [${dbxschemaname}].FIND_IN_SET(groupactionlimit.Action_id, CAST(''')
                + 
               (@customer_disabled_actions)
                + 
               (''' as nvarchar(max))) = 0'+'   and ')
                + 
               (@active_feature_condition)



            IF (@_actionId <> '')

               SET @select_statement = (@select_statement) + (' and groupactionlimit.Action_id = ') + ''''+(@_actionId)+''''


			exec(@select_statement)
			END



   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[customer_group_org_actionlimits_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[customer_group_org_actionlimits_proc]  
   @_customerId nvarchar(50),
   @_organisationId nvarchar(50),
   @_isOnlyPremissions nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

	  DECLARE @organization_actions nvarchar(max)
	  DECLARE @business_customer_enabled_actions nvarchar(max)
	  DECLARE @groups nvarchar(max)
	  DECLARE @list nvarchar(max)
	  DECLARE @select_statement nvarchar(max)
	  DECLARE @next nvarchar(max)
	  DECLARE @nextlen bigint
	  DECLARE @value nvarchar(max)
	  DECLARE @roleType nvarchar(max)

      SET @organization_actions = 
         (

            SELECT String_agg(CAST(organisationactionlimit.Action_id AS nvarchar(max)),',')
            FROM [${dbxschemaname}].organisationactionlimit
            WHERE organisationactionlimit.Organisation_id = @_organisationId AND organisationactionlimit.Action_id NOT IN 
               (
                  SELECT featureaction.id
                  FROM 
                     [${dbxschemaname}].featureaction 
                        LEFT JOIN [${dbxschemaname}].feature 
                        ON (featureaction.Feature_id = feature.id) 
                        LEFT JOIN [${dbxschemaname}].organisationfeatures 
                        ON (organisationfeatures.featureId = feature.id)
                  WHERE organisationfeatures.organisationId = @_organisationId AND organisationfeatures.featureStatus = 'SID_FEATURE_SUSPENDED' OR feature.Status_id <> 'SID_FEATURE_ACTIVE'
               )

         )

      SET @organization_actions = ('''') + (
         CASE 
            WHEN (@organization_actions IS NULL) THEN N''
            ELSE @organization_actions
         END) + (N'''')

	  SET @business_customer_enabled_actions = (SELECT String_Agg(CAST(Action_id AS nvarchar(max)),',') FROM [${dbxschemaname}].customeraction WHERE isAllowed = '1' AND 
	  Customer_id = @_customerId AND [${dbxschemaname}].FIND_IN_SET(Action_id, @organization_actions)<>0)

	  IF (@business_customer_enabled_actions IS NOT NULL) 
  	  SET @business_customer_enabled_actions = '''' + (CASE WHEN (@business_customer_enabled_actions is null) THEN  '' ELSE @business_customer_enabled_actions END) + ''''

	  SET @groups = (SELECT String_agg(CAST(customergroup.Group_id AS nvarchar(max)),',') FROM [${dbxschemaname}].customergroup WHERE Customer_id=@_customerId)

	  IF(@groups != '' )
      SET @groups =  (@groups+',')

	  SET @list = (SELECT @groups)

	  SET @select_statement = ''

	  WHILE((LEN(TRIM(@list)) != 0 OR @list IS NOT NULL))
	  BEGIN
	  SET @next = [${dbxschemaname}].SUBSTRING_INDEX(@list,',',1)
	  SET @nextlen = LEN(@next)
	  SET @value = TRIM(@next)
	  SET @roleType = (SELECT membergroup.Type_id FROM [${dbxschemaname}].membergroup WHERE membergroup.id= @value )
	  IF (@roleType = ('TYPE_ID_BUSINESS'))
	  BEGIN
	  IF (@_isOnlyPremissions = 'true')
	  SELECT DISTINCT customeraction.Action_id AS actionId FROM [${dbxschemaname}].customeraction WHERE isAllowed = '1' AND Customer_id =@_customerId AND 
	  [${dbxschemaname}].FIND_IN_SET(Action_id, @organization_actions)<>0 AND (customeraction.Action_id IN (SELECT DISTINCT groupactionlimit.Action_id FROM [${dbxschemaname}].groupactionlimit
	  WHERE groupactionlimit.Group_id = @value))
	  ELSE
	  BEGIN
	  SET @select_statement =  ('SELECT DISTINCT
								feature.id as featureId,
								feature.name AS featureName,
								feature.description AS featureDescription,
								featureaction.id as actionId,
								featureaction.Type_id as actionType,
								featureaction.description AS actionDescription,
								featureaction.name AS actionName,
								CASE WHEN ([${dbxschemaname}].FIND_IN_SET (featureaction.id  ,CAST('+ @business_customer_enabled_actions+' as nvarchar(max))) > 0 ) THEN ''true'' ELSE ''false'' END AS isActionAllowed,
								CASE WHEN (featureaction.isAccountLevel= 1) THEN ''true'' ELSE ''false'' END AS isAccountLevel,
								CASE WHEN (customeraction.isAllowed = 1) THEN ''true'' ELSE ''false'' END as isAllowedForCustomer,
								customeraction.Account_id as accountId,
								customeraction.LimitType_id as limitTypeId,
								customeraction.value as value
						   FROM
							([${dbxschemaname}].customeraction 
							LEFT JOIN [${dbxschemaname}].customergroup ON (customergroup.Customer_id = customeraction.Customer_id)
							LEFT JOIN [${dbxschemaname}].membergroup ON (membergroup.id = customergroup.Group_id)
							LEFT JOIN [${dbxschemaname}].featureaction ON (featureaction.id = customeraction.Action_id)
							LEFT JOIN [${dbxschemaname}].feature ON (feature.id = featureaction.Feature_id))
						where 
							customeraction.Customer_id = '+quotename(@_customerId,'''')+'
							and (customeraction.Action_id in (
							select DISTINCT groupactionlimit.Action_id from 
							[${dbxschemaname}].groupactionlimit where 
							groupactionlimit.Group_id = '+''''+(@value)+''''+'))
							and ([${dbxschemaname}].FIND_IN_SET(featureaction.id, CAST('+ @business_customer_enabled_actions+' as nvarchar(max))) > 0 )')

		 EXEC(@select_statement)
		 END
		 END
		 SET @list = STUFF(@list,1,@nextlen + 1,'')
		 END
END
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[customer_group_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[customer_group_proc]  
   @_customerIds nvarchar(max),
   @_groupId nvarchar(50),
   @_numberOfRows bigint,
   @_userId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DECLARE
         @INSERTED_RECORDS_COUNT bigint = 0

      DECLARE
         @INDEXVALUE bigint = 1

      DECLARE
         @CustomerId nvarchar(max) = NULL

      DECLARE
         @buffer nvarchar(max) = NULL

      SET @buffer = N''

      /*
      *   SSMA informational messages:
      *   M2SS0003: The following SQL clause was ignored during conversion:
      *   iterator : .
      */

      WHILE (1 = 1)
      
         BEGIN

            IF datalength(LTRIM(RTRIM(@_customerIds))) = 0 OR @_customerIds IS NULL OR @INDEXVALUE > @_numberOfRows
               BREAK

            SET @CustomerId = [${dbxschemaname}].func_split_str(@_customerIds, N',', @INDEXVALUE)

            /*
            *   SSMA warning messages:
            *   M2SS0097: IGNORE was not converted
            */

            IF 
               CASE 
                  WHEN EXISTS 
                     (
                        SELECT 
                           customergroup.Customer_id, 
                           customergroup.Group_id, 
                           customergroup.createdby, 
                           customergroup.modifiedby, 
                           customergroup.createdts, 
                           customergroup.lastmodifiedts, 
                           customergroup.synctimestamp, 
                           customergroup.softdeleteflag
                        FROM [${dbxschemaname}].customergroup
                        WHERE customergroup.Customer_id = @CustomerId AND customergroup.Group_id = @_groupId
                     ) THEN 1
                  ELSE 0
               END <> 0
               SET @INDEXVALUE = @INDEXVALUE + 1
            ELSE 
               BEGIN

                  INSERT [${dbxschemaname}].customergroup([${dbxschemaname}].customergroup.Customer_id, [${dbxschemaname}].customergroup.Group_id, [${dbxschemaname}].customergroup.createdby)
                     VALUES (@CustomerId, @_groupId, @_userId)

                  IF (@@rowcount = 0)
                     SET @buffer = @buffer + N',' + @CustomerId

                  SET @INSERTED_RECORDS_COUNT = @INSERTED_RECORDS_COUNT + @@rowcount

                  SET @INDEXVALUE = @INDEXVALUE + 1

               END

         END

      SET @buffer = right(@buffer, datalength(@buffer) - 1)

      SELECT @buffer AS N'FAILED_RECORDS'

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[customer_group_unlink_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[customer_group_unlink_proc]  
   @roleId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DELETE 
      FROM [${dbxschemaname}].customergroup
      WHERE customergroup.Group_id = @roleId

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[customer_request_archived_message_search_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[customer_request_archived_message_search_proc]  
   @_customerID nvarchar(50),
   @_customerName nvarchar(50),
   @_customerFirstName nvarchar(50),
   @_customerMiddleName nvarchar(50),
   @_customerLastName nvarchar(50),
   @_customerUsername nvarchar(50),
   @_messageRepliedBy nvarchar(50),
   @_requestSubject nvarchar(50),
   @_requestAssignedTo nvarchar(50),
   @_requestCategory nvarchar(50),
   @_requestID nvarchar(50),
   @_requestStatusID nvarchar(50),
   @_dateInitialPoint nvarchar(50),
   @_dateFinalPoint nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
	  declare @queryStatement nvarchar(max)

      SET @queryStatement = 'SELECT archivedcustomerrequest.id AS customerrequest_id,archivedcustomerrequest.RequestCategory_id AS customerrequest_RequestCategory_id,archivedcustomerrequest.lastupdatedbycustomer AS customerrequest_lastupdatedbycustomer,requestcategory.Name AS requestcategory_Name, archivedcustomerrequest.Customer_id AS customerrequest_Customer_id,customer.FirstName AS customer_FirstName,customer.MiddleName AS customer_MiddleName,(customer.FirstName+customer.LastName) AS customer_Fullname,(systemuser.FirstName+systemuser.LastName) AS customerrequest_AssignedTo_Name,customer.LastName AS customer_LastName,customer.UserName AS customer_Username,customer.Salutation AS customer_Salutation,customer.Gender AS customer_Gender,customer.DateOfBirth AS customer_DateOfBirth,customer.Status_id AS customer_Status_id, customer.Ssn AS customer_Ssn,customer.MaritalStatus_id AS customer_MaritalStatus_id,customer.SpouseName AS customer_SpouseName,customer.EmployementStatus_id AS customer_EmployementStatus_id,customer.IsEnrolledForOlb AS customer_IsEnrolledForOlb,customer.IsStaffMember AS customer_IsStaffMember,customer.Location_id AS customer_Location_id,customer.PreferredContactMethod AS customer_PreferredContactMethod,  customer.PreferredContactTime AS customer_PreferredContactTime,archivedcustomerrequest.Priority AS customerrequest_Priority,archivedcustomerrequest.Status_id AS customerrequest_Status_id,archivedcustomerrequest.AssignedTo AS customerrequest_AssignedTo,archivedcustomerrequest.RequestSubject AS customerrequest_RequestSubject,archivedcustomerrequest.Accountid AS customerrequest_Accountid, archivedcustomerrequest.createdby AS customerrequest_createdby,archivedcustomerrequest.modifiedby AS customerrequest_modifiedby,   archivedcustomerrequest.createdts AS customerrequest_createdts,archivedcustomerrequest.lastmodifiedts AS customerrequest_lastmodifiedts,    archivedcustomerrequest.synctimestamp AS customerrequest_synctimestamp,archivedcustomerrequest.softdeleteflag AS customerrequest_softdeleteflag,archivedrequestmessage.id AS requestmessage_id,archivedrequestmessage.RepliedBy AS requestmessage_RepliedBy,archivedrequestmessage.RepliedBy_Name AS requestmessage_RepliedBy_Name,  archivedrequestmessage.MessageDescription AS requestmessage_MessageDescription, archivedrequestmessage.ReplySequence AS requestmessage_ReplySequence,archivedrequestmessage.IsRead AS requestmessage_IsRead,    archivedrequestmessage.createdby AS requestmessage_createdby,archivedrequestmessage.modifiedby AS requestmessage_modifiedby,     archivedrequestmessage.createdts AS requestmessage_createdts, archivedrequestmessage.lastmodifiedts AS requestmessage_lastmodifiedts,   archivedrequestmessage.synctimestamp AS requestmessage_synctimestamp,archivedrequestmessage.softdeleteflag AS requestmessage_softdeleteflag,    archivedmessageattachment.id AS messageattachment_id,archivedmessageattachment.AttachmentType_id AS messageattachment_AttachmentType_id,        archivedmessageattachment.Media_id AS messageattachment_Media_id,archivedmessageattachment.createdby AS messageattachment_createdby, archivedmessageattachment.modifiedby AS messageattachment_modifiedby,archivedmessageattachment.createdts AS messageattachment_createdts,archivedmessageattachment.lastmodifiedts AS messageattachment_lastmodifiedts,     archivedmessageattachment.softdeleteflag AS messageattachment_softdeleteflag,archivedmedia.id AS media_id,     archivedmedia.Name AS media_Name,archivedmedia.Size AS media_Size,archivedmedia.Type AS media_Type,archivedmedia.Description AS media_Description,archivedmedia.Url AS media_Url,archivedmedia.createdby AS media_createdby,   archivedmedia.modifiedby AS media_modifiedby,archivedmedia.lastmodifiedts AS media_lastmodifiedts,archivedmedia.synctimestamp AS media_synctimestamp,archivedmedia.softdeleteflag AS media_softdeleteflag FROM ([${dbxschemaname}].archivedcustomerrequest JOIN [${dbxschemaname}].archivedrequestmessage ON ([${dbxschemaname}].archivedcustomerrequest.id = [${dbxschemaname}].archivedrequestmessage.CustomerRequest_id) JOIN [${dbxschemaname}].customer ON ([${dbxschemaname}].archivedcustomerrequest.Customer_id = [${dbxschemaname}].customer.id) JOIN [${dbxschemaname}].requestcategory ON ([${dbxschemaname}].archivedcustomerrequest.RequestCategory_id = [${dbxschemaname}].requestcategory.id) LEFT JOIN [${dbxschemaname}].archivedmessageattachment ON ([${dbxschemaname}].archivedrequestmessage.id = [${dbxschemaname}].archivedmessageattachment.RequestMessage_id) LEFT JOIN [${dbxschemaname}].archivedmedia ON ([${dbxschemaname}].archivedmessageattachment.Media_id = [${dbxschemaname}].archivedmedia.id) LEFT JOIN [${dbxschemaname}].systemuser ON ([${dbxschemaname}].archivedcustomerrequest.AssignedTo = [${dbxschemaname}].systemuser.id)) WHERE 1=1 '
    
      IF @_customerID <> ''

         SET @queryStatement = (@queryStatement) + (N' and customer.id = ') + ((QUOTENAME((@_customerID), '''')))
  
      IF @_customerFirstName <> ''

         SET @queryStatement = (@queryStatement) + (N' and customer.FirstName = ') + ((QUOTENAME((@_customerFirstName), '''')))


      IF @_customerMiddleName <> ''

         SET @queryStatement = (@queryStatement) + (N' and customer.MiddleName = ') + ((QUOTENAME((@_customerMiddleName), '''')))
 

      IF @_customerLastName <> ''
  
         SET @queryStatement = (@queryStatement) + (N' and customer.LastName = ') + ((QUOTENAME((@_customerLastName), '''')))
    

      IF @_customerUsername <> ''
 
         SET @queryStatement = (@queryStatement) + (N' and customer.LastName = ') + ''''+(@_customerUsername)+''''
  

      IF @_messageRepliedBy <> ''
  
         SET @queryStatement = (@queryStatement) + (N' and archivedrequestmessage.RepliedBy = ') + ''''+(@_messageRepliedBy)+''''
      

      IF @_requestSubject <> ''
     
         SET @queryStatement = (@queryStatement) + (N' and archivedcustomerrequest.RequestSubject = ') + ''''+(@_requestSubject)+''''
         

      IF @_requestAssignedTo <> ''
     
         SET @queryStatement = (@queryStatement) + (N' and archivedcustomerrequest.AssignedTo = ') + ''''+(@_requestAssignedTo)+''''

      IF @_requestCategory <> ''
         
         SET @queryStatement = (@queryStatement) + (N' and archivedcustomerrequest.RequestCategory_id = ') + ''''+(@_requestCategory)+''''
         

      IF @_requestID <> ''
        
         SET @queryStatement = (@queryStatement) + (N' and archivedcustomerrequest.id = ') + ((QUOTENAME((@_requestID), '''')))
         

      IF @_requestStatusID <> ''
         
         SET @queryStatement = (@queryStatement) + (N' and archivedcustomerrequest.Status_id = ') + ((QUOTENAME((@_requestStatusID), '''')))
         

      IF @_customerName <> ''
         
         SET @queryStatement = (@queryStatement) + (N' and  (customer.FirstName+customer.LastName).Status_id = ') + ''''+(@_customerName)+''''
         

      IF @_dateInitialPoint <> ''
         IF [${dbxschemaname}].FIND_IN_SET(N'=', @_dateInitialPoint) <> 0
           
            SET @queryStatement = (@queryStatement) + (N' and archivedrequestmessage.createdts = ') + ((QUOTENAME((@_dateInitialPoint), '''')))
            
         ELSE 
            IF [${dbxschemaname}].FIND_IN_SET(N'>', @_dateInitialPoint) <> 0
     
               SET @queryStatement = (@queryStatement) + (N' and archivedrequestmessage.createdts > ') + ((QUOTENAME((@_dateInitialPoint), '''')))
           

            ELSE 
               IF [${dbxschemaname}].FIND_IN_SET(N'<', @_dateInitialPoint) <> 0
                
                  SET @queryStatement = (@queryStatement) + (N' and archivedrequestmessage.createdts < ') + ((QUOTENAME((@_dateInitialPoint), '''')))
                  
   
               ELSE 
                  BEGIN
                     IF @_dateFinalPoint <> ''
                        
                        SET @queryStatement = (@queryStatement) + (N' and archivedrequestmessage.createdts > ') + ((QUOTENAME((@_dateInitialPoint), ''''))) + (N' and requestmessage.createdts < ') + ((QUOTENAME((@_dateFinalPoint), '''')))
                       
                  END

      SET @queryStatement = (@queryStatement) + (N' ORDER BY archivedrequestmessage.ReplySequence DESC')
 
     
     exec(@queryStatement)
      

   END
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[customer_request_message_search_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[customer_request_message_search_proc]  
   @_customerID nvarchar(50),
   @_customerName nvarchar(50),
   @_customerFirstName nvarchar(50),
   @_customerMiddleName nvarchar(50),
   @_customerLastName nvarchar(50),
   @_customerUsername nvarchar(50),
   @_messageRepliedBy nvarchar(50),
   @_requestSubject nvarchar(50),
   @_requestAssignedTo nvarchar(50),
   @_requestCategory nvarchar(50),
   @_requestID nvarchar(50),
   @_requestStatusID nvarchar(50),
   @_dateInitialPoint nvarchar(50),
   @_dateFinalPoint nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

	  declare @queryStatement nvarchar(max)

      SET @queryStatement = 'SELECT customerrequest.id AS customerrequest_id,customerrequest.RequestCategory_id AS customerrequest_RequestCategory_id,customerrequest.lastupdatedbycustomer AS customerrequest_lastupdatedbycustomer,requestcategory.Name AS requestcategory_Name,customerrequest.Customer_id AS customerrequest_Customer_id,customer.FirstName AS customer_FirstName,customer.MiddleName AS customer_MiddleName,(customer.FirstName+customer.LastName) AS customer_Fullname,(systemuser.FirstName+systemuser.LastName) AS customerrequest_AssignedTo_Name,customer.LastName AS customer_LastName,customer.UserName AS customer_Username,customer.Salutation AS customer_Salutation,customer.Gender AS customer_Gender,customer.DateOfBirth AS customer_DateOfBirth,customer.Status_id AS customer_Status_id,customer.Ssn AS customer_Ssn,customer.MaritalStatus_id AS customer_MaritalStatus_id, customer.SpouseName AS customer_SpouseName,customer.EmployementStatus_id AS customer_EmployementStatus_id,customer.IsEnrolledForOlb AS customer_IsEnrolledForOlb,customer.IsStaffMember AS customer_IsStaffMember,customer.Location_id AS customer_Location_id,customer.PreferredContactMethod AS customer_PreferredContactMethod,customer.PreferredContactTime AS customer_PreferredContactTime,customerrequest.Priority AS customerrequest_Priority,customerrequest.Status_id AS customerrequest_Status_id,customerrequest.AssignedTo AS customerrequest_AssignedTo,customerrequest.RequestSubject AS customerrequest_RequestSubject,customerrequest.Accountid AS customerrequest_Accountid,customerrequest.createdby AS customerrequest_createdby,customerrequest.modifiedby AS customerrequest_modifiedby,customerrequest.createdts AS customerrequest_createdts,customerrequest.lastmodifiedts AS customerrequest_lastmodifiedts,customerrequest.synctimestamp AS customerrequest_synctimestamp,customerrequest.softdeleteflag AS customerrequest_softdeleteflag,requestmessage.id AS requestmessage_id,requestmessage.RepliedBy AS requestmessage_RepliedBy,requestmessage.RepliedBy_Name AS requestmessage_RepliedBy_Name,requestmessage.MessageDescription AS requestmessage_MessageDescription,      requestmessage.ReplySequence AS requestmessage_ReplySequence,requestmessage.IsRead AS requestmessage_IsRead,requestmessage.createdby AS requestmessage_createdby,requestmessage.modifiedby AS requestmessage_modifiedby, requestmessage.createdts AS requestmessage_createdts,requestmessage.lastmodifiedts AS requestmessage_lastmodifiedts,    requestmessage.synctimestamp AS requestmessage_synctimestamp,requestmessage.softdeleteflag AS requestmessage_softdeleteflag,     messageattachment.id AS messageattachment_id,messageattachment.AttachmentType_id AS messageattachment_AttachmentType_id,     messageattachment.Media_id AS messageattachment_Media_id,messageattachment.createdby AS messageattachment_createdby,     messageattachment.modifiedby AS messageattachment_modifiedby,messageattachment.createdts AS messageattachment_createdts,     messageattachment.lastmodifiedts AS messageattachment_lastmodifiedts,messageattachment.softdeleteflag AS messageattachment_softdeleteflag, media.id AS media_id,media.Name AS media_Name,media.Size AS media_Size,media.Type AS media_Type,  media.Description AS media_Description,media.Url AS media_Url,media.createdby AS media_createdby,media.modifiedby AS media_modifiedby,media.lastmodifiedts AS media_lastmodifiedts,media.synctimestamp AS media_synctimestamp,media.softdeleteflag AS media_softdeleteflag FROM ([${dbxschemaname}].customerrequest JOIN [${dbxschemaname}].requestmessage ON (customerrequest.id = requestmessage.CustomerRequest_id) JOIN [${dbxschemaname}].customer ON (customerrequest.Customer_id = customer.id) JOIN [${dbxschemaname}].requestcategory ON (customerrequest.RequestCategory_id = requestcategory.id) LEFT JOIN [${dbxschemaname}].systemuser ON (customerrequest.AssignedTo = systemuser.id)  LEFT JOIN [${dbxschemaname}].messageattachment ON (requestmessage.id = messageattachment.RequestMessage_id)  LEFT JOIN [${dbxschemaname}].media ON (messageattachment.Media_id = media.id)) WHERE 1=1 '


      IF @_customerID <> ''

         SET @queryStatement = (@queryStatement) + (N' and customer.id = ') + ((QUOTENAME((@_customerID), '''')))

      IF @_customerFirstName <> ''

         SET @queryStatement = (@queryStatement) + (N' and customer.FirstName = ') + ((QUOTENAME((@_customerFirstName), '''')))

      IF @_customerMiddleName <> ''

         SET @queryStatement = (@queryStatement) + (N' and customer.MiddleName = ') + ((QUOTENAME((@_customerMiddleName), '''')))


      IF @_customerLastName <> ''

         SET @queryStatement = (@queryStatement) + (N' and customer.LastName = ') + ((QUOTENAME((@_customerLastName), '''')))
        

      IF @_customerUsername <> ''

         SET @queryStatement = (@queryStatement) + (N' and customer.LastName = ') + ''''+(@_customerUsername)+''''
   

      IF @_messageRepliedBy <> ''
        
         SET @queryStatement = (@queryStatement) + (N' and requestmessage.RepliedBy = ') + ''''+(@_messageRepliedBy)+''''
         

      IF @_requestSubject <> ''

         SET @queryStatement = (@queryStatement) + (N' and customerrequest.RequestSubject = ') + ''''+(@_requestSubject)+''''


      IF @_requestAssignedTo <> ''

         SET @queryStatement = (@queryStatement) + (N' and customerrequest.AssignedTo = ') + ''''+(@_requestAssignedTo)+''''


      IF @_requestCategory <> ''

         SET @queryStatement = (@queryStatement) + (N' and customerrequest.RequestCategory_id = ') + ''''+(@_requestCategory)+''''

      IF @_requestID <> ''

         SET @queryStatement = (@queryStatement) + (N' and customerrequest.id = ') + ((QUOTENAME((@_requestID), '''')))

      IF @_requestStatusID <> ''

         SET @queryStatement = (@queryStatement) + (N' and customerrequest.Status_id = ') + ((QUOTENAME((@_requestStatusID), '''')))

      IF @_customerName <> ''

         SET @queryStatement = (@queryStatement) + (N' and  (customer.FirstName+customer.LastName).Status_id = ') + ''''+(@_customerName)+''''

      IF @_dateInitialPoint <> ''
         IF [${dbxschemaname}].FIND_IN_SET(N'=', @_dateInitialPoint) <> 0
 
            SET @queryStatement = (@queryStatement) + (N' and requestmessage.createdts = ') + ((QUOTENAME((@_dateInitialPoint), '''')))
   
         ELSE 
            IF [${dbxschemaname}].FIND_IN_SET(N'>', @_dateInitialPoint) <> 0
 
               SET @queryStatement = (@queryStatement) + (N' and requestmessage.createdts > ') + ((QUOTENAME((@_dateInitialPoint), '''')))

            ELSE 
               IF [${dbxschemaname}].FIND_IN_SET(N'<', @_dateInitialPoint) <> 0
 
                  SET @queryStatement = (@queryStatement) + (N' and requestmessage.createdts < ') + ((QUOTENAME((@_dateInitialPoint), '''')))

               ELSE 
                  BEGIN
                     IF @_dateFinalPoint <> ''

                        SET @queryStatement = (@queryStatement) + (N' and requestmessage.createdts > ') + ((QUOTENAME((@_dateInitialPoint), ''''))) + (N' and requestmessage.createdts < ') + ((QUOTENAME((@_dateFinalPoint), '''')))

                  END


      SET @queryStatement = (@queryStatement) + (N' ORDER BY requestmessage.ReplySequence DESC')
       EXEC(@queryStatement)

   END

GO

/****** Object:  StoredProcedure [${dbxschemaname}].[customer_request_search_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[customer_request_search_proc]  
   
   @_dateInitialPoint varchar(50),
   @_dateFinalPoint varchar(50),
   @_requestStatusID varchar(50),
   @_requestCategory varchar(50),
   @_offset varchar(50),
   @_sortCriteria varchar(50),
   @_sortOrder varchar(50),
   @_requestAssignedTo varchar(50),
   @_searchKey varchar(50),
   @_messageRepliedBy varchar(50),
   @_recordsPerPage varchar(50),
   @_queryType varchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON

	  DECLARE @selectClause nvarchar(max)
	  DECLARE @stmt nvarchar(max)
	  DECLARE @whereclause nvarchar(max)
	  DECLARE @joinCustomer int

      SET @selectClause = 
         CASE 
            WHEN (ISNULL(@_queryType, N'') = 'count') THEN N'count(cr.id) AS cnt'
            ELSE N'cr.id AS customerrequest_id'
         END
	
      SET @stmt = (N'SELECT ') + (@selectClause) + (N' FROM [${dbxschemaname}].customerrequest cr ')
 
      SET @whereclause = N' WHERE 1=1 '
 
      SET @joinCustomer = 0
	  

      IF @_dateInitialPoint <> '' AND @_dateFinalPoint <> ''

         SET @whereclause = 
            (@whereclause)
             + 
            (N'  AND cr.createdts >= ')
             + 
            ((QUOTENAME((@_dateInitialPoint), '''')))
             + 
            (N'  AND ')
             + 
            (N' cr.createdts <= ')
             + 
            ((QUOTENAME((@_dateFinalPoint), '''')))

      ELSE 
         IF @_dateInitialPoint <> ''

            SET @whereclause = (@whereclause) + (+N'  AND cr.createdts = ''') + CONVERT(datetime,@_dateInitialPoint,120)+''''
         ELSE 
            BEGIN
               IF @_dateFinalPoint <> ''

                  SET @whereclause = (@whereclause) + (+N'  AND cr.createdts = ''') + CONVERT(datetime,@_dateFinalPoint,120)+'''' 
            END


      IF @_requestStatusID <> ''
 
         SET @whereclause = (@whereclause) + (N'  AND cr.Status_id IN  ( ') + ([${dbxschemaname}].func_escape_input_for_in_operator(@_requestStatusID)) + (+N'  ) ')


      IF @_requestCategory <> ''
   
         SET @whereclause = (@whereclause) + (+N'  AND cr.RequestCategory_id = ') + ''''+(@_requestCategory)+''''

      IF @_requestAssignedTo <> ''
 
         SET @whereclause = (@whereclause) + (+N'  AND cr.AssignedTo = ') + ''''+(@_requestAssignedTo)+''''


      IF @_messageRepliedBy <> ''
         BEGIN


            SET @stmt = (@stmt) + (N'JOIN [${dbxschemaname}].requestmessage ON (cr.id = requestmessage.CustomerRequest_id) ')
 
            SET @whereclause = (@whereclause) + (+N'  AND requestmessage.RepliedBy_id = ') + ''''+(@_messageRepliedBy)+''''


         END

      IF @_searchKey <> ''
         BEGIN

            SET @joinCustomer = 1


            SET @_searchKey = N'%' + @_searchKey + N'%'

            SET @whereclause = 
               (@whereclause)
                + 
               (+N'  AND '+N'  ('+N'    cr.Customer_id LIKE ')
                + 
               ''''+(@_searchKey)+''''
                + 
               (N' OR cr.id LIKE ')
                + 
               ''''+(@_searchKey)+''''
                + 
               (N' OR customer.UserName LIKE ')
                + 
               ''''+(@_searchKey)+''''
                + 
               (N')')

         END
	DECLARE @sortColumn nvarchar(max)
      IF @_queryType <> 'count'
         BEGIN

            IF @_sortCriteria = 'customer_Fullname'

               SET @joinCustomer = 1

            SET @sortColumn = N'cr.lastmodifiedts'

            IF (@_sortCriteria = 'customerrequest_Customer_id')

               SET @sortColumn = N'cr.Customer_id'

            ELSE 
               IF (@_sortCriteria = 'customerrequest_AssignedTo')
        
                  SET @sortColumn = N'cr.AssignedTo'

               ELSE 
                  IF (@_sortCriteria = 'customerrequest_createdts')

                     SET @sortColumn = N'cr.createdts'
     
                  ELSE 
                     IF (@_sortCriteria = 'customerrequest_RequestCategory_id')
            
                        SET @sortColumn = N'cr.RequestCategory_id'
                     
                     ELSE 
                        IF (@_sortCriteria = 'customer_Fullname')
                      
                           SET @sortColumn = '(customer.FirstName'+'customer.LastName)'
                       
                        ELSE 
                           IF (@_sortCriteria = 'customerrequest_Status_id')
                             
                              SET @sortColumn = N'cr.Status_id'
                              
                           ELSE 
                              BEGIN
                                 IF (@_sortCriteria = 'customerrequest_AssignedTo_Name')
                                   
                                    SET @sortColumn = N'(systemuser.FirstName'+'systemuser.LastName)'
                                    
                              END

            SET @whereclause = (@whereclause) + (+N' ORDER BY '+N'  ') + (
               CASE 
                  WHEN (ISNULL(@sortColumn,'') = '') THEN N'cr.lastmodifiedts'
                  ELSE @sortColumn
               END) + (N' ') + (
               CASE 
                  WHEN ((ISNULL(@sortColumn,'') = '') OR @_sortOrder = '') THEN N'DESC'
                  ELSE @_sortOrder
               END)
           
            SET @whereclause = (@whereclause) + (N' offset ') + (
               CASE 
                  WHEN (@_offset = '') THEN N'0'
                  ELSE @_offset
               END) + (N' rows fetch next ') + (
               CASE 
                  WHEN (@_recordsPerPage = '') THEN N'10'
                  ELSE @_recordsPerPage
               END) + (N' rows only')
          
         END

      IF @joinCustomer = 1
        
         SET @stmt = (@stmt) + (N'JOIN [${dbxschemaname}].customer ON (cr.Customer_id = customer.id) ')
      

      IF @_sortCriteria = 'customerrequest_AssignedTo_Name'
     
         SET @stmt = (@stmt) + (N'LEFT JOIN [${dbxschemaname}].systemuser ON (cr.AssignedTo = systemuser.id) ')
         
      SET @stmt = (@stmt) + (@whereclause)

		EXEC(@stmt)
  
   END

GO


/****** Object:  StoredProcedure [${dbxschemaname}].[customer_search_proc]    Script Date: 6/9/2020 2:00:56 PM ******/

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [${dbxschemaname}].[customer_search_proc]  
   @_searchType varchar(60),
   @_id varchar(50),
   @_name varchar(50),
   @_SSN varchar(50),
   @_username varchar(50),
   @_phone varchar(100),
   @_email varchar(100),
   @_IsStaffMember varchar(10),
   @_cardorAccountnumber varchar(50),
   @_TIN varchar(50),
   @_group varchar(40),
   @_IDType varchar(50),
   @_IDValue varchar(50),
   @_companyId varchar(50),
   @_requestID varchar(50),
   @_branchIDS varchar(2000),
   @_productIDS varchar(2000),
   @_cityIDS varchar(2000),
   @_entitlementIDS varchar(2000),
   @_groupIDS varchar(2000),
   @_customerStatus varchar(50),
   @_before varchar(20),
   @_after varchar(20),
   @_sortVariable varchar(100),
   @_sortDirection varchar(4),
   @_pageOffset bigint,
   @_pageSize bigint
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DECLARE
         @_maxLockCount varchar(50)
		 declare @search_select_statement nvarchar(max)
		 declare @search_count_statement nvarchar(max)
		 declare @queryStatement nvarchar(max)
		 
      IF @_searchType LIKE N'GROUP_SEARCH%'
		BEGIN
            SET @search_select_statement = N'SELECT customer.id, customer.FirstName, customer.MiddleName, customer.LastName,(ISNULL(customer.FirstName,'''')+ '' ''+ ISNULL(customer.MiddleName,'''')+ '' ''+ ISNULL(customer.LastName,'''')) as name,customer.UserName as Username, customer.Salutation, customer.Gender, customer.IsStaffMember,customer.modifiedby, customer.lastmodifiedts, customer.Status_id, PrimaryEmail.value AS PrimaryEmail,STRING_AGG(CAST(customergroup.Group_id  as nvarchar(max)),'','') as assigned_group_ids,address.City_id, city.Name as City_name,customer.Location_id AS branch_id,location.Name AS branch_name '


            SET @search_count_statement = N'SELECT count(distinct customer.id) as SearchMatchs '
 
            SET @queryStatement = 'FROM [${dbxschemaname}].customer JOIN (SELECT [${dbxschemaname}].customer.id FROM [${dbxschemaname}].customer ' + (
               CASE 
                  WHEN (@_groupIDS <> '' OR @_entitlementIDS <> '') THEN N' LEFT JOIN [${dbxschemaname}].customergroup ON (customergroup.Customer_id=customer.id)'
                  ELSE N''
               END) + (
               CASE 
                  WHEN (@_cityIDS <> '') THEN N' LEFT JOIN [${dbxschemaname}].customeraddress ON (customer.id=customeraddress.Customer_id AND customeraddress.isPrimary=1 and customeraddress.Type_id=''ADR_TYPE_HOME'') LEFT JOIN [${dbxschemaname}].address ON (customeraddress.Address_id = address.id) '
                  ELSE N''
               END) + (
               CASE 
                  WHEN (@_entitlementIDS <> '') THEN N' LEFT JOIN [${dbxschemaname}].customerentitlement ON (customerentitlement.Customer_id=customer.id) '
                  ELSE N''
               END) + (
               CASE 
                  WHEN (@_productIDS <> '') THEN N' LEFT JOIN [${dbxschemaname}].customerproduct ON (customerproduct.Customer_id=customer.id) '
                  ELSE N''
               END)

			   declare @whereclause nvarchar(max)
            SET @whereclause = N' WHERE 1=1 '


            IF @_username <> ''
               BEGIN


                  SET @whereclause = (@whereclause) + (N' AND (customer.firstname like (''') + (@_username)  +'%'')'

                  SET @whereclause = (@whereclause) + (N' OR customer.username like (''') + (@_username)  +'%'')'

                  SET @whereclause = (@whereclause) + (N' OR customer.id like (''') + (@_username) + '%''))'


               END


            IF @_IsStaffMember <> ''
               IF @_IsStaffMember = 'true'
 
                  SET @whereclause = (@whereclause) + (N' AND customer.IsStaffMember = ''1''')

               ELSE 

                  SET @whereclause = (@whereclause) + (N' AND customer.IsStaffMember = ''0''')



            IF @_entitlementIDS <> ''

               SET @whereclause = 
                  (@whereclause)
                   + 
                  (N' AND (customerentitlement.Service_id in (')
                   + 
                  ([${dbxschemaname}].func_escape_input_for_in_operator(@_entitlementIDS))
                   + 
                  (N') OR customergroup.Group_id in ( select Group_id from [${dbxschemaname}].groupentitlement where Service_id in (')
                   + 
                  ([${dbxschemaname}].func_escape_input_for_in_operator(@_entitlementIDS))
                   + 
                  (N' )))')
       
            IF @_groupIDS <> ''

               SET @whereclause = (@whereclause) + (N' AND customergroup.Group_id in (') + ([${dbxschemaname}].func_escape_input_for_in_operator(@_groupIDS)) + (N') ')

            IF @_productIDS <> ''

               SET @whereclause = (@whereclause) + (N' AND customerproduct.Product_id in (') + ([${dbxschemaname}].func_escape_input_for_in_operator(@_productIDS)) + (N')')


            IF @_branchIDS <> ''

               SET @whereclause = (@whereclause) + (N' AND customer.Location_id in (') + ([${dbxschemaname}].func_escape_input_for_in_operator(@_branchIDS)) + (N')')
   

            IF @_customerStatus <> ''
           
               SET @whereclause = (@whereclause) + (N' AND customer.Status_id = ') + ((QUOTENAME((@_customerStatus), '''')))
           
            IF @_cityIDS <> ''
            
               SET @whereclause = (@whereclause) + (N' AND address.City_id in (') + ([${dbxschemaname}].func_escape_input_for_in_operator(@_cityIDS)) + (N')')
               

            IF @_before <> '' AND @_after <> ''
             
               SET @whereclause = (@whereclause) + (N' AND CONVERT(DATE,customer.createdts,105) >= CONVERT(DATE, ') + ((QUOTENAME((@_before), ''''))) + (N',105) and CONVERT(DATE,customer.createdts,105) <= CONVERT(DATE, ') + ((QUOTENAME((@_after), '''')))+',105)'
            
            ELSE 
               IF @_before <> ''
             
                  SET @whereclause = (@whereclause) + (N' AND CONVERT(DATE,customer.createdts,105) >= CONVERT(DATE, ') + ((QUOTENAME((@_before), '''')))+',105)'
                  

                  
               ELSE 
                  BEGIN
                     IF @_after <> ''
                    
                        SET @whereclause = (@whereclause) + (N' AND CONVERT(DATE,customer.createdts,105) >= CONVERT(DATE, ') + ((QUOTENAME((@_after), '''')))+',105)'
                        
                  END

        
            SET @queryStatement = (@queryStatement) + (@whereclause) + (N') paginatedCustomers ON (paginatedCustomers.id=customer.id) LEFT JOIN [${dbxschemaname}].customercommunication PrimaryEmail ON (PrimaryEmail.Customer_id=paginatedCustomers.id AND PrimaryEmail.isPrimary=1 AND PrimaryEmail.Type_id=''COMM_TYPE_EMAIL'') LEFT JOIN [${dbxschemaname}].customergroup ON (customergroup.Customer_id=paginatedCustomers.id) LEFT JOIN [${dbxschemaname}].customeraddress ON (paginatedCustomers.id=customeraddress.Customer_id AND customeraddress.isPrimary=1 and customeraddress.Type_id=''ADR_TYPE_HOME'') LEFT JOIN [${dbxschemaname}].address ON (customeraddress.Address_id = address.id) LEFT JOIN [${dbxschemaname}].city ON (city.id = address.City_id) LEFT JOIN [${dbxschemaname}].location ON (location.id=customer.Location_id)')
        
	
			IF @_searchType = 'GROUP_SEARCH'
				BEGIN

              
                  SET @queryStatement = (@search_select_statement) + (@queryStatement) + (N' GROUP BY customer.id, customer.FirstName, customer.MiddleName, customer.LastName, customer.UserName, customer.Salutation, customer.Gender, customer.IsStaffMember,customer.modifiedby, customer.lastmodifiedts, customer.Status_id, PrimaryEmail.value,address.City_id, city.Name ,customer.Location_id,location.Name, paginatedCustomers.id ')
                  

                  IF @_sortVariable = 'DEFAULT' OR @_sortVariable = '' OR @_sortVariable IS NULL
                  
                     SET @queryStatement = (@queryStatement) + (N' ORDER BY FirstName')
                     
                  ELSE 
                     BEGIN
                        IF @_sortVariable <> ''
                        
                           SET @queryStatement = (@queryStatement) + (N' ORDER BY ') + (@_sortVariable)
                    
                     END

                  IF @_sortDirection <> ''
                   
                     SET @queryStatement = (@queryStatement) + (N' ') + (@_sortDirection)
                

                  SET @queryStatement = (@queryStatement) + (' OFFSET ') + CAST(@_pageOffset AS NVARCHAR(MAX)) + ' rows fetch next ' +CAST(@_pageSize AS NVARCHAR(MAX)) + +(' rows only')
                
                END
			    ELSE IF @_searchType = 'GROUP_SEARCH_TOTAL_COUNT'
                 
                     SET @queryStatement = (@search_count_statement) + (@queryStatement)

		END
      ELSE IF @_searchType LIKE N'CUSTOMER_SEARCH%'
        BEGIN

                  SELECT @_maxLockCount = CAST(passwordlockoutsettings.accountLockoutThreshold AS varchar(50))
                  FROM [${dbxschemaname}].passwordlockoutsettings
                  WHERE passwordlockoutsettings.id = 'PLOCKID1'

                  SET @search_select_statement = (N'SELECT customer.id, customer.FirstName, customer.MiddleName, customer.LastName,customer.DateOfBirth,(ISNULL(customer.FirstName,'''')+ '' ''+ ISNULL(customer.MiddleName,'''')+ '' ''+ ISNULL(customer.LastName,'''')) as name,customer.UserName as Username, customer.Salutation, customer.Gender,(''****''+ RIGHT(customer.Ssn, 4)) as Ssn,customer.CustomerType_id as CustomerTypeId, organisation.id as CompanyId, organisation.Name as CompanyName,case when ISNULL(customer.lockCount,0) >= ') + (@_maxLockCount) + (N' then ''SID_CUS_LOCKED'' else customer.Status_id end as Status_id,customercommunication.value AS PrimaryPhoneNumber,customercommunication.value AS PrimaryEmailAddress,String_agg(CAST(membergroup.Name as nvarchar(max)),'','') as groups,customer.ApplicantChannel, customer.createdts')
                 

                  SET @search_count_statement = N'SELECT count(distinct customer.id) as SearchMatchs '
                
                  SET @queryStatement = ' FROM [${dbxschemaname}].customer JOIN (SELECT customer.id FROM [${dbxschemaname}].customer ' + (
                     CASE 
                        WHEN (@_phone <> '') THEN N' JOIN [${dbxschemaname}].customercommunication PrimaryPhone ON (PrimaryPhone.Customer_id=customer.id AND PrimaryPhone.isPrimary=1 AND PrimaryPhone.Type_id=''COMM_TYPE_PHONE'') '
                        ELSE N''
                     END) + (
                     CASE 
                        WHEN (@_email <> '') THEN N'  JOIN [${dbxschemaname}].customercommunication PrimaryEmail ON (PrimaryEmail.Customer_id=customer.id AND PrimaryEmail.isPrimary=1 AND PrimaryEmail.Type_id=''COMM_TYPE_EMAIL'') '
                        ELSE N''
                     END) + (
                     CASE 
                        WHEN (@_TIN <> '') THEN N' LEFT JOIN [${dbxschemaname}].organisationmembership ON (customer.Organization_id = organisationmembership.Organization_id)'
                        ELSE N''
                     END) + (
                     CASE 
                        WHEN (@_cardorAccountnumber <> '') THEN N' LEFT JOIN [${dbxschemaname}].card ON (customer.id = card.User_id) LEFT JOIN [${dbxschemaname}].accounts ON (customer.id = accounts.User_id) LEFT JOIN [${dbxschemaname}].customeraccounts ON (customer.id = customeraccounts.Customer_id)'
                        ELSE N''
                     END)
            
                  SET @queryStatement = (@queryStatement) + (N' WHERE 1=1 ')
                 
                  IF @_id <> ''
                   
                     SET @queryStatement = (@queryStatement) + (N' and customer.id = ') + ((QUOTENAME((@_id), '''')))
                     

                  IF @_name <> ''
                    
                     SET @queryStatement = (@queryStatement) + (N' and customer.FirstName like (''') + (@_name) +'%'')'
                     

                  IF @_SSN <> ''
                 
                     SET @queryStatement = (@queryStatement) + (N' and customer.Ssn = ') + ((QUOTENAME((@_SSN), '''')))
            

                  IF @_username <> ''
                 
                     SET @queryStatement = (@queryStatement) + (N' and customer.username = ') + ''''+(@_username)+''''
                   

                  IF @_phone <> ''
                     IF datalength(@_phone) > 9
                     
                        SET @queryStatement = (@queryStatement) + (N' and PrimaryPhone.value like (%''') + (@_phone) +
						'%'')'
                       
                     ELSE 
                       
                        SET @queryStatement = (@queryStatement) + (N' and PrimaryPhone.value = ') + ((QUOTENAME((@_phone), '''')))
                       

                  IF @_email <> ''
                   
                     SET @queryStatement = (@queryStatement) + (N' and PrimaryEmail.value = ') + ((QUOTENAME((@_email), '''')))
                   

                  IF @_companyId <> ''
                  
                     SET @queryStatement = (@queryStatement) + (N' and customer.Organization_id = ') + ((QUOTENAME((@_companyId), '''')))
                    

                  IF @_IDValue <> ''
                     IF @_IDType = 'ID_DRIVING_LICENSE'
                     
                        SET @queryStatement = 
                           (@queryStatement)
                            + 
                           (N' and (customer.DrivingLicenseNumber = ')
                            + 
                           ((QUOTENAME((@_IDValue), '''')))
                            + 
                           (N' or (customer.IDType_id = ')
                            + 
                           ((QUOTENAME((@_IDType), '''')))
                            + 
                           (N' and customer.IDValue = ')
                            + 
                           ((QUOTENAME((@_IDValue), '''')))
                            + 
                           (N'))')
                        
                        
                     ELSE 
                        
                        SET @queryStatement = 
                           (@queryStatement)
                            + 
                           (N' and (customer.IDType_id = ')
                            + 
                           ((QUOTENAME((@_IDType), '''')))
                            + 
                           (N' and customer.IDValue = ')
                            + 
                           ((QUOTENAME((@_IDValue), '''')))
                            + 
                           (N')')
                       

                  IF @_TIN <> ''
                   
                     SET @queryStatement = (@queryStatement) + (N' and organisationmembership.Taxid = ') + ((QUOTENAME((@_TIN), '''')))
                     

                  IF @_cardorAccountnumber <> ''
                  
                     SET @queryStatement = 
                        (@queryStatement)
                         + 
                        (N' and (card.cardNumber = ')
                         + 
                        ((QUOTENAME((@_cardorAccountnumber), '''')))
                         + 
                        (N' or accounts.Account_id = ')
                         + 
                        ((QUOTENAME((@_cardorAccountnumber), '''')))
                         + 
                        (N' or customeraccounts.Account_id = ')
                         + 
                        ((QUOTENAME((@_cardorAccountnumber), '''')))
                         + 
                        (N')')

                 
                  SET @queryStatement = (@queryStatement) + (N') paginatedCustomers ON (paginatedCustomers.id=customer.id) LEFT JOIN 
[${dbxschemaname}].customercommunication ON (customercommunication.Customer_id=paginatedCustomers.id AND customercommunication.isPrimary=1 AND 
customercommunication.Type_id=''COMM_TYPE_PHONE'' AND customercommunication.Customer_id=paginatedCustomers.id 
AND customercommunication.isPrimary=1 AND customercommunication.Type_id=''COMM_TYPE_EMAIL'') LEFT JOIN [${dbxschemaname}].customergroup ON 
(customergroup.Customer_id=paginatedCustomers.id) LEFT JOIN [${dbxschemaname}].membergroup ON (membergroup.id=customergroup.group_id) LEFT JOIN 
[${dbxschemaname}].organisation ON (customer.Organization_id = organisation.id)')
				   
                 

                  IF @_searchType = 'CUSTOMER_SEARCH'
                     BEGIN

                   
                        SET @queryStatement = (@search_select_statement) + (@queryStatement) + (N' GROUP BY customer.id, customer.FirstName, customer.MiddleName, customer.LastName, customer.UserName, customer.Salutation, customer.Gender, customer.IsStaffMember,customer.modifiedby, customer.lastmodifiedts, customer.Status_id, customercommunication.value,customer.Location_id,paginatedCustomers.id,customer.DateOfBirth,customer.Ssn,CustomerType_id,organisation.id,organisation.Name,customer.lockCount,customer.ApplicantChannel,customer.createdts ')
                       

                        IF @_sortVariable = 'DEFAULT' OR @_sortVariable = '' OR @_sortVariable IS NULL
                       
                           SET @queryStatement = (@queryStatement) + (N' ORDER BY FirstName')
                        
                          
                        ELSE 
                           BEGIN
                              IF @_sortVariable <> ''
                              
                                 SET @queryStatement = (@queryStatement) + (N' ORDER BY ') + (@_sortVariable)
                                 
                           END

                        IF @_sortDirection <> ''
                         
                           SET @queryStatement = (@queryStatement) + (N' ') + (@_sortDirection)


                        SET @queryStatement = (@queryStatement) + (N' OFFSET ') + (CAST(@_pageOffset AS varchar(50))) +' rows fetch next ' + (CAST(@_pageSize AS varchar(50))) + +(N' rows only')
                      

                    END
                  ELSE 
                     BEGIN
                        IF @_searchType = 'CUSTOMER_SEARCH_TOTAL_COUNT'
                        
                           SET @queryStatement = (@search_count_statement) + (@queryStatement)
                    END

        END
     END
    exec(@queryStatement)

	GO

/****** Object:  StoredProcedure [${dbxschemaname}].[customer_requests_assign_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[customer_requests_assign_proc]  
   @_requestIds nvarchar(max),
   @_csrID nvarchar(200)
AS 
   BEGIN

      SET  XACT_ABORT  ON
	  declare @updateclause nvarchar(max)
      SET  NOCOUNT  ON
      SET @updateclause = (N'UPDATE customerrequest SET customerrequest.AssignedTo = ') + ((QUOTENAME((@_csrID), ''''))) + (N' where customerrequest.id in (') + ([${dbxschemaname}].func_escape_input_for_in_operator(@_requestIds)) + (N')')
      
      EXECUTE @updateclause


   END
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[customer_unread_message_count_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[customer_unread_message_count_proc]  
   @_customerId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT count_big(requestmessage.id) AS messageCount
      FROM ([${dbxschemaname}].customerrequest 
         INNER JOIN [${dbxschemaname}].requestmessage 
         ON ((requestmessage.CustomerRequest_id = customerrequest.id)))
      WHERE (
         requestmessage.IsRead = 'FALSE' AND 
         customerrequest.softdeleteflag = 0 AND 
         customerrequest.Customer_id = @_customerId AND 
         customerrequest.Status_id <> 'SID_DELETED')

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[customeractions_create_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[customeractions_create_proc]  
   @_customerActionsCSV nvarchar(max),
   @_accountsCSV nvarchar(max),
   @_customerId nvarchar(50),
   @_businessTypeId nvarchar(50),
   @_groupId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON

      DECLARE @finished int = 0 
	  DECLARE @id nvarchar(50)
      DECLARE @featureActionId varchar(max)
      DECLARE @actionslist varchar(max)
      DECLARE @limitId varchar(max)
      DECLARE @entryStatus int = 0
      DECLARE @accountId varchar(max)
      DECLARE @actualLimitId varchar(max)
	  DECLARE @groupId varchar(max)
	  DECLARE @validActionsList varchar(max)
	  DECLARE @limitvalue varchar(max)

	  DECLARE accounts CURSOR LOCAL
      FOR (SELECT Account_id FROM [${dbxschemaname}].accounts WHERE [${dbxschemaname}].FIND_IN_SET(Account_id,@_accountsCSV)<>0);
	  

      IF(@_groupId IS NULL OR @_groupId = '')
         SET @groupId =(SELECT groupbusinesstype.Group_id FROM [${dbxschemaname}].groupbusinesstype
						WHERE groupbusinesstype.BusinessType_id = @_businessTypeId AND groupbusinesstype.isDefaultGroup = 1)
      ELSE
         SET @groupId = @_groupId

		 SET @groupId =(SELECT membergroup.id FROM [${dbxschemaname}].membergroup WHERE 
               membergroup.id = @groupId AND 
               membergroup.Type_id = 'TYPE_ID_BUSINESS' AND 
               membergroup.Status_id = 'SID_ACTIVE')

		 SET @validActionsList =(SELECT String_agg(CAST(Action_id as nvarchar(max)),',') FROM [${dbxschemaname}].groupactionlimit
            WHERE groupactionlimit.Group_id = @groupId AND ([${dbxschemaname}].FIND_IN_SET(groupactionlimit.Action_id, @_customerActionsCSV) <> 0))

		DECLARE actions CURSOR LOCAL
      FOR (select id from [${dbxschemaname}].featureaction where [${dbxschemaname}].FIND_IN_SET(id,@validActionsList)<>0);

	OPEN accounts     
	FETCH NEXT FROM accounts into @accountId
      WHILE (@@FETCH_STATUS=0)
         BEGIN
	OPEN actions
	FETCH NEXT FROM actions into @featureActionId
	  WHILE (@@FETCH_STATUS=0)
          BEGIN
	      SET @entryStatus = 0
	DECLARE limits CURSOR LOCAL
	  FOR (select LimitType_id from [${dbxschemaname}].actionlimit where Action_id = @featureActionId);
	OPEN limits
	FETCH NEXT FROM limits into @limitId
      WHILE (@@FETCH_STATUS=0)
           BEGIN
	        SET @limitvalue = (SELECT actionlimit.value FROM [${dbxschemaname}].actionlimit
                               WHERE actionlimit.Action_id = @featureActionId AND actionlimit.LimitType_id = @limitId)

            IF (@limitId = 'MAX_TRANSACTION_LIMIT')
            SET @actualLimitId = N'AUTO_DENIED_TRANSACTION_LIMIT'
            ELSE IF (@limitId = 'MIN_TRANSACTION_LIMIT')
            SET @actualLimitId = N'PRE_APPROVED_TRANSACTION_LIMIT'
            ELSE IF (@limitId = 'DAILY_LIMIT')
            BEGIN
	           SET @actualLimitId = N'PRE_APPROVED_DAILY_LIMIT'
			   SET @id = (SELECT left(newid(), 50))
			   INSERT [${dbxschemaname}].customeraction(customeraction.id, 
                                             customeraction.RoleType_id, 
                                             customeraction.Customer_id, 
                                             customeraction.Action_id, 
                                             customeraction.Account_id, 
                                             customeraction.isAllowed, 
                                             customeraction.LimitType_id, 
                                             customeraction.[value])
                                             VALUES (@id, 
                                                     'TYPE_ID_BUSINESS', 
                                                      @_customerId, 
                                                      @featureActionId, 
                                                      @accountId, 
                                                      1, 
                                                      @actualLimitId, 
                                                      0.00)
			   SET @actualLimitId = N'AUTO_DENIED_DAILY_LIMIT'

             END
             ELSE 
             BEGIN
			 IF (@limitId = 'WEEKLY_LIMIT')
             BEGIN
			 SET @actualLimitId = N'PRE_APPROVED_WEEKLY_LIMIT'
			 SET @id = (SELECT left(newid(), 50))
             INSERT [${dbxschemaname}].customeraction(customeraction.id, 
                                           customeraction.RoleType_id, 
                                           customeraction.Customer_id, 
                                           customeraction.Action_id, 
                                           customeraction.Account_id, 
                                           customeraction.isAllowed, 
                                           customeraction.LimitType_id, 
                                           customeraction.[value])
                                           VALUES ( @id, 
                                                    'TYPE_ID_BUSINESS', 
                                                    @_customerId, 
                                                    @featureActionId, 
                                                    @accountId, 
                                                    1, 
                                                    @actualLimitId, 
                                                    0.00)
			 SET @actualLimitId = N'AUTO_DENIED_WEEKLY_LIMIT'
			 END
             END
		SET @id =(SELECT left(newid(), 50))
        INSERT [${dbxschemaname}].customeraction(customeraction.id, 
                                      customeraction.RoleType_id, 
                                      customeraction.Customer_id, 
                                      customeraction.Action_id, 
                                      customeraction.Account_id, 
                                      customeraction.isAllowed, 
                                      customeraction.LimitType_id, 
                                      customeraction.[value])
                                      VALUES (@id, 
                                              'TYPE_ID_BUSINESS', 
                                               @_customerId, 
                                               @featureActionId, 
                                               @accountId, 
                                               1, 
                                               @actualLimitId, 
                                               @limitvalue)
		  SET @entryStatus = 1
		  FETCH NEXT FROM limits into @limitId
		  CONTINUE
		  END
	      CLOSE limits
		  DEALLOCATE limits
          IF @entryStatus = 0
          BEGIN
		  SET @id =(SELECT left(newid(), 50))
		  INSERT [${dbxschemaname}].customeraction(customeraction.id, 
                                        customeraction.RoleType_id, 
                                        customeraction.Customer_id, 
                                        customeraction.Action_id, 
                                        customeraction.Account_id, 
                                        customeraction.isAllowed)
                                        VALUES (
                                          @id, 
                                          N'TYPE_ID_BUSINESS', 
                                          @_customerId, 
                                          @featureActionId, 
                                          @accountId, 
                                          1)
		   END
		   SET @actionslist = @featureActionId + N',' + @actionslist
		   FETCH NEXT FROM actions into @featureActionId
		   CONTINUE
           END
	       CLOSE actions
		   DEALLOCATE actions
		   FETCH NEXT FROM accounts into @accountId
		   CONTINUE
           END
		   CLOSE accounts
		   DEALLOCATE accounts

      SET @actionslist =(SELECT substring(@actionslist, 1, (len(@actionslist) - 1)))
      SELECT @actionslist as actionslist
   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[customrole_actionlimits_create_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[customrole_actionlimits_create_proc]  
   @_queryInput nvarchar(max),
   @_customRoleId bigint
AS 
   BEGIN
   
      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
   DECLARE @index int
   DECLARE @numOfRecords int
   DECLARE @recordsData nvarchar(max)
   DECLARE @query nvarchar(max)

      DELETE 
      FROM [${dbxschemaname}].customroleactionlimits
      WHERE customroleactionlimits.customRole_id = @_customRoleId

      SET @index = 0
	  SET @_queryInput = (SELECT REPLACE(@_queryInput,'"',''''))
      SET @numOfRecords = case when @_queryInput is null then 0 else LEN(@_queryInput) - LEN(replace(@_queryInput, '|', '')) + 1 end

      WHILE (1 = 1)
      
         BEGIN

           SET  @index = @index + 1
            IF (@index = @numOfRecords + 1)
               BREAK
            ELSE 
               BEGIN
                  SET @recordsData = [${dbxschemaname}].substring_index([${dbxschemaname}].substring_index(@_queryInput, N'|', @index), N'|', -1)
                  SET @query = ('INSERT INTO [${dbxschemaname}].customroleactionlimits(customRole_id,action_id,account_id,isAllowed,limitType_id,value,createdby,modifiedby) VALUES (') + (@recordsData) + (N')')
				  EXEC(@query)
               END
         END
   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[dbpalerts_customercommunication]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[dbpalerts_customercommunication]  
   @_customers nvarchar(max)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT customercommunication.Customer_id, customercommunication.Type_id, customercommunication.Value FROM [${dbxschemaname}].customercommunication
      WHERE [${dbxschemaname}].FIND_IN_SET(customercommunication.Customer_id, @_customers) <> 0 AND customercommunication.isPrimary = 1
         ORDER BY customercommunication.Type_id


   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[dbpalerts_getCustidFromAccount]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[dbpalerts_getCustidFromAccount]  
   @_accounts nvarchar(max)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

	  SELECT Account_id, User_id, Type_id AS accounttype_id
      FROM [${dbxschemaname}].accounts
      WHERE [${dbxschemaname}].FIND_IN_SET(accounts.Account_id, @_accounts) <> 0

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[dbpalerts_getCustIdFromCore]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[dbpalerts_getCustIdFromCore]  
   @_backendids nvarchar(max)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT backendidentifier.BackendId, backendidentifier.Customer_id
      FROM [${dbxschemaname}].backendidentifier
      WHERE [${dbxschemaname}].FIND_IN_SET(backendidentifier.BackendId, @_backendids) <> 0
	  
   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[dbpalerts_getCustomerData]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[dbpalerts_getCustomerData]  
   @_customerids nvarchar(max),
   @_usernames nvarchar(max)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT customer.id AS CustomerId, customer.UserName
      FROM [${dbxschemaname}].customer
      WHERE [${dbxschemaname}].FIND_IN_SET(customer.id, @_customerids) <> 0 OR [${dbxschemaname}].FIND_IN_SET(customer.UserName, @_usernames) <> 0
	  
   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[dbpalerts_getNotificationId]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[dbpalerts_getNotificationId]
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      /*
      *   SSMA warning messages:
      *   M2SS0240: The behaviour of Standard Function SCOPE_IDENTITY may not be same as in MySQL
      */

      SELECT ISNULL(@@IDENTITY,0) AS lastid

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[DMS_create_user_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[DMS_create_user_proc]  
   @_username nvarchar(50),
   @_group varchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
	  
      DECLARE
              @customerId varchar(2000)

      DECLARE
                 @Id varchar(2000)

      DECLARE
         

         @dmsaddressId varchar(50)

      DECLARE
         

         @tempDayName varchar(2000)

      DECLARE
         

         @tempStartTime varchar(2000)

      DECLARE
         

         @tempEndTime varchar(2000)

      DECLARE
         

         @tempServiceName varchar(2000)

      DECLARE
         

         @servicesList varchar(max)

      DECLARE
         

         @serviceName varchar(2000)

      DECLARE
         @b int

      DECLARE
         @pipeFlag int

      DECLARE
         

         @addressId varchar(2000)

      DECLARE
         

         @customerCommId varchar(2000)

      DECLARE
         

         @customerReqId varchar(2000)

      DECLARE
         

         @reqMsgId varchar(2000)

      DECLARE
         

         @LogId varchar(2000)

      SELECT @customerId = customer.id
      FROM [${dbxschemaname}].customer
      WHERE customer.UserName = @_username

      IF @customerId IS NULL
         BEGIN
		 declare @ctimestamp datetime
            SELECT @customerId = CAST(round((CAST(REPLACE((REPLACE((REPLACE((CONVERT(varchar(20),DATEDIFF(SECOND,{d '1970-01-01'}, @ctimestamp), 120)), '-', '')), ' ', '')), ':', '') AS bigint)) * 10, 0) AS varchar(50))
        

            INSERT [${dbxschemaname}].customer(
               [${dbxschemaname}].customer.id, 
               [${dbxschemaname}].customer.FirstName, 
               [${dbxschemaname}].customer.MiddleName, 
               [${dbxschemaname}].customer.LastName, 
               [${dbxschemaname}].customer.UserName, 
               [${dbxschemaname}].customer.Salutation, 
               [${dbxschemaname}].customer.Gender, 
               [${dbxschemaname}].customer.DateOfBirth, 
               [${dbxschemaname}].customer.Status_id, 
               [${dbxschemaname}].customer.Ssn, 
               [${dbxschemaname}].customer.MaritalStatus_id, 
               [${dbxschemaname}].customer.SpouseName, 
               [${dbxschemaname}].customer.EmployementStatus_id, 
               [${dbxschemaname}].customer.IsEnrolledForOlb, 
               [${dbxschemaname}].customer.IsStaffMember, 
               [${dbxschemaname}].customer.Location_id, 
               [${dbxschemaname}].customer.PreferredContactMethod, 
               [${dbxschemaname}].customer.PreferredContactTime, 
               [${dbxschemaname}].customer.createdby, 
               [${dbxschemaname}].customer.modifiedby, 
               [${dbxschemaname}].customer.UserImage, 
               [${dbxschemaname}].customer.EligbilityCriteria, 
               [${dbxschemaname}].customer.Reason, 
               [${dbxschemaname}].customer.DocumentsSubmitted)
               VALUES (
                  @customerId, 
                  N'John', 
                  N'', 
                  N'Bailey', 
                  @_username, 
                  N'Mr.', 
                  N'Male', 
                  '1999-11-11', 
                  N'SID_CUS_ACTIVE', 
                  N'123456789', 
                  N'SID_SINGLE', 
                  N'', 
                  N'SID_EMPLOYED', 
                  1, 
                  0, 
                  N'LID1', 
                  N'Call', 
                  N'Morning, Afternoon', 
                  N'Kony User', 
                  N'Kony Dev', 
                  '', 
                  '', 
                  '', 
                  '')

            SET @customerCommId = N'CID' + @customerId

         

            INSERT [${dbxschemaname}].customercommunication(
               [${dbxschemaname}].customercommunication.id, 
               [${dbxschemaname}].customercommunication.Type_id, 
               [${dbxschemaname}].customercommunication.Customer_id, 
               [${dbxschemaname}].customercommunication.isPrimary, 
               [${dbxschemaname}].customercommunication.Value, 
               [${dbxschemaname}].customercommunication.Extension, 
               [${dbxschemaname}].customercommunication.Description, 
               [${dbxschemaname}].customercommunication.createdby, 
               [${dbxschemaname}].customercommunication.modifiedby)
               VALUES (
                  @customerCommId, 
                  N'COMM_TYPE_EMAIL', 
                  @customerId, 
                  1, 
                  N'john.bailey@yahoo.com', 
                  N'Personal', 
                  N'NULL', 
                  N'Kony User', 
                  N'Kony Dev')

            SELECT @customerCommId = CAST((CAST(REPLACE(REPLACE(REPLACE(CONVERT(varchar(20), getdate(), 120), '-', ''), ' ', ''), ':', '') AS bigint) + 2) AS varchar(50))

            SET @customerCommId = N'CID' + @customerCommId

          
            INSERT [${dbxschemaname}].customercommunication(
               [${dbxschemaname}].customercommunication.id, 
               [${dbxschemaname}].customercommunication.Type_id, 
               [${dbxschemaname}].customercommunication.Customer_id, 
               [${dbxschemaname}].customercommunication.isPrimary, 
               [${dbxschemaname}].customercommunication.Value, 
               [${dbxschemaname}].customercommunication.Extension, 
               [${dbxschemaname}].customercommunication.Description, 
               [${dbxschemaname}].customercommunication.createdby, 
               [${dbxschemaname}].customercommunication.modifiedby)
               VALUES (
                  @customerCommId, 
                  N'COMM_TYPE_PHONE', 
                  @customerId, 
                  1, 
                  N'8729899218', 
                  N'Personal', 
                  N'NULL', 
                  N'Kony User', 
                  N'Kony Dev')

         END

 
      SELECT @dmsaddressId = (N'Addr1') + (CAST(round((CAST(REPLACE((REPLACE((REPLACE((CONVERT(varchar(20), DATEDIFF(SECOND,{d '1970-01-01'}, @ctimestamp), 120)), '-', '')), ' ', '')), ':', '') AS bigint)) * 10000, 0) AS varchar(50)))
     
      INSERT [${dbxschemaname}].address(
         [${dbxschemaname}].address.id, 
         [${dbxschemaname}].address.Region_id, 
         [${dbxschemaname}].address.City_id, 
         [${dbxschemaname}].address.addressLine1, 
         [${dbxschemaname}].address.addressLine2, 
         [${dbxschemaname}].address.zipCode, 
         [${dbxschemaname}].address.latitude, 
         [${dbxschemaname}].address.logitude, 
         [${dbxschemaname}].address.createdby, 
         [${dbxschemaname}].address.modifiedby)
         VALUES (
            @dmsaddressId, 
            N'R57', 
            N'CITY1318', 
            N'9225 Bee Caves Rd #300', 
            N'Fusce Rd.', 
            N'20620', 
            N'40.7128', 
            N'74.0060', 
            N'Kony User', 
            N'Kony Dev')

     
      INSERT [${dbxschemaname}].customeraddress(
         [${dbxschemaname}].customeraddress.Customer_id, 
         [${dbxschemaname}].customeraddress.Address_id, 
         [${dbxschemaname}].customeraddress.Type_id, 
         [${dbxschemaname}].customeraddress.isPrimary, 
         [${dbxschemaname}].customeraddress.createdby, 
         [${dbxschemaname}].customeraddress.modifiedby)
         VALUES (
            @customerId, 
            @dmsaddressId, 
            N'ADR_TYPE_WORK', 
            0, 
            N'Kony User', 
            N'Kony Dev')

      SELECT @dmsaddressId = (N'Addr2') + (CAST(round((CAST(REPLACE((REPLACE((REPLACE((CONVERT(varchar(20), DATEDIFF(SECOND,{d '1970-01-01'}, @ctimestamp), 120)), '-', '')), ' ', '')), ':', '') AS bigint)) * 10000, 0) AS varchar(50)))
     
      INSERT [${dbxschemaname}].address(
         [${dbxschemaname}].address.id, 
         [${dbxschemaname}].address.Region_id, 
         [${dbxschemaname}].address.City_id, 
         [${dbxschemaname}].address.addressLine1, 
         [${dbxschemaname}].address.addressLine2, 
         [${dbxschemaname}].address.zipCode, 
         [${dbxschemaname}].address.latitude, 
         [${dbxschemaname}].address.logitude, 
         [${dbxschemaname}].address.createdby, 
         [${dbxschemaname}].address.modifiedby)
         VALUES (
            @dmsaddressId, 
            N'R52', 
            N'CITY1289', 
            N'P.O. Box 283 8562', 
            N'Sit Rd.', 
            N'20645', 
            N'40.7128', 
            N'74.0060', 
            N'Kony User', 
            N'Kony Dev')


      INSERT [${dbxschemaname}].customeraddress(
         [${dbxschemaname}].customeraddress.Customer_id, 
         [${dbxschemaname}].customeraddress.Address_id, 
         [${dbxschemaname}].customeraddress.Type_id, 
         [${dbxschemaname}].customeraddress.isPrimary, 
         [${dbxschemaname}].customeraddress.createdby, 
         [${dbxschemaname}].customeraddress.modifiedby)
         VALUES (
            @customerId, 
            @dmsaddressId, 
            N'ADR_TYPE_HOME', 
            1, 
            N'Kony User', 
            N'Kony Dev')


      INSERT [${dbxschemaname}].customergroup(
         [${dbxschemaname}].customergroup.Customer_id, 
         [${dbxschemaname}].customergroup.Group_id, 
         [${dbxschemaname}].customergroup.createdby, 
         [${dbxschemaname}].customergroup.modifiedby, 
         [${dbxschemaname}].customergroup.softdeleteflag)
         VALUES (
            @customerId, 
            @_group, 
            N'Kony User', 
            N'Kony Dev', 
            0)

      INSERT [${dbxschemaname}].customerproduct(
         [${dbxschemaname}].customerproduct.Customer_id, 
         [${dbxschemaname}].customerproduct.Product_id, 
         [${dbxschemaname}].customerproduct.createdby, 
         [${dbxschemaname}].customerproduct.modifiedby, 
         [${dbxschemaname}].customerproduct.softdeleteflag)
         VALUES (
            @customerId, 
            N'PRODUCT2', 
            N'Kony User', 
            N'Kony Dev', 
            0)

      INSERT [${dbxschemaname}].customerproduct(
         [${dbxschemaname}].customerproduct.Customer_id, 
         [${dbxschemaname}].customerproduct.Product_id, 
         [${dbxschemaname}].customerproduct.createdby, 
         [${dbxschemaname}].customerproduct.modifiedby, 
         [${dbxschemaname}].customerproduct.softdeleteflag)
         VALUES (
            @customerId, 
            N'PRODUCT4', 
            N'Kony User', 
            N'Kony Dev', 
            0)


      INSERT [${dbxschemaname}].customerproduct(
         [${dbxschemaname}].customerproduct.Customer_id, 
         [${dbxschemaname}].customerproduct.Product_id, 
         [${dbxschemaname}].customerproduct.createdby, 
         [${dbxschemaname}].customerproduct.modifiedby, 
         [${dbxschemaname}].customerproduct.softdeleteflag)
         VALUES (
            @customerId, 
            N'PRODUCT7', 
            N'Kony User', 
            N'Kony Dev', 
            0)


      INSERT [${dbxschemaname}].customerproduct(
         [${dbxschemaname}].customerproduct.Customer_id, 
         [${dbxschemaname}].customerproduct.Product_id, 
         [${dbxschemaname}].customerproduct.createdby, 
         [${dbxschemaname}].customerproduct.modifiedby, 
         [${dbxschemaname}].customerproduct.softdeleteflag)
         VALUES (
            @customerId, 
            N'PRODUCT14', 
            N'Kony User', 
            N'Kony Dev', 
            0)

 
      INSERT [${dbxschemaname}].customerproduct(
         [${dbxschemaname}].customerproduct.Customer_id, 
         [${dbxschemaname}].customerproduct.Product_id, 
         [${dbxschemaname}].customerproduct.createdby, 
         [${dbxschemaname}].customerproduct.modifiedby, 
         [${dbxschemaname}].customerproduct.softdeleteflag)
         VALUES (
            @customerId, 
            N'PRODUCT10', 
            N'Kony User', 
            N'Kony Dev', 
            0)

      SET @customerReqId = N'REC' + @customerId


      INSERT [${dbxschemaname}].customerrequest(
         [${dbxschemaname}].customerrequest.id, 
         [${dbxschemaname}].customerrequest.RequestCategory_id, 
         [${dbxschemaname}].customerrequest.Customer_id, 
         [${dbxschemaname}].customerrequest.Priority, 
         [${dbxschemaname}].customerrequest.Status_id, 
         [${dbxschemaname}].customerrequest.RequestSubject, 
         [${dbxschemaname}].customerrequest.AssignedTo, 
         [${dbxschemaname}].customerrequest.Accountid, 
         [${dbxschemaname}].customerrequest.createdby, 
         [${dbxschemaname}].customerrequest.modifiedby, 
         [${dbxschemaname}].customerrequest.softdeleteflag)
         VALUES (
            @customerReqId, 
            N'RCID_DEPOSITS', 
            @customerId, 
            N'HIGH', 
            N'SID_OPEN', 
            N'Communication information change', 
            NULL, 
            N'090871', 
            N'KonyUser', 
            N'KonyDev', 
            0)

      SET @reqMsgId = N'MSG' + @customerId


      INSERT [${dbxschemaname}].requestmessage(
         [${dbxschemaname}].requestmessage.id, 
         [${dbxschemaname}].requestmessage.CustomerRequest_id, 
         [${dbxschemaname}].requestmessage.MessageDescription, 
         [${dbxschemaname}].requestmessage.RepliedBy, 
         [${dbxschemaname}].requestmessage.RepliedBy_id, 
         [${dbxschemaname}].requestmessage.RepliedBy_Name, 
         [${dbxschemaname}].requestmessage.ReplySequence, 
         [${dbxschemaname}].requestmessage.IsRead, 
         [${dbxschemaname}].requestmessage.createdby, 
         [${dbxschemaname}].requestmessage.modifiedby, 
         [${dbxschemaname}].requestmessage.softdeleteflag)
         VALUES (
            @reqMsgId, 
            @customerReqId, 
            N'What is the expected resolution date?', 
            N'CUSTOMER', 
            @customerId, 
            N'John bailey', 
            1, 
            N'TRUE', 
            N'KonyUser', 
            N'konyolbuser', 
            0)

      SET @reqMsgId = N'MSG' + CAST((CAST(@customerId AS float(53)) + 1) AS varchar(50))


      INSERT [${dbxschemaname}].requestmessage(
         [${dbxschemaname}].requestmessage.id, 
         [${dbxschemaname}].requestmessage.CustomerRequest_id, 
         [${dbxschemaname}].requestmessage.MessageDescription, 
         [${dbxschemaname}].requestmessage.RepliedBy, 
         [${dbxschemaname}].requestmessage.RepliedBy_id, 
         [${dbxschemaname}].requestmessage.RepliedBy_Name, 
         [${dbxschemaname}].requestmessage.ReplySequence, 
         [${dbxschemaname}].requestmessage.IsRead, 
         [${dbxschemaname}].requestmessage.createdby, 
         [${dbxschemaname}].requestmessage.modifiedby, 
         [${dbxschemaname}].requestmessage.softdeleteflag)
         VALUES (
            @reqMsgId, 
            @customerReqId, 
            N'Can you escalate the request?', 
            N'CUSTOMER', 
            @customerId, 
            N'John bailey', 
            2, 
            N'TRUE', 
            N'KonyUser', 
            N'konyolbuser', 
            0)

      SET @reqMsgId = N'MSG' + CAST((CAST(@customerId AS float(53)) + 2) AS varchar(50))

      INSERT [${dbxschemaname}].requestmessage(
         [${dbxschemaname}].requestmessage.id, 
         [${dbxschemaname}].requestmessage.CustomerRequest_id, 
         [${dbxschemaname}].requestmessage.MessageDescription, 
         [${dbxschemaname}].requestmessage.RepliedBy, 
         [${dbxschemaname}].requestmessage.RepliedBy_id, 
         [${dbxschemaname}].requestmessage.RepliedBy_Name, 
         [${dbxschemaname}].requestmessage.ReplySequence, 
         [${dbxschemaname}].requestmessage.IsRead, 
         [${dbxschemaname}].requestmessage.createdby, 
         [${dbxschemaname}].requestmessage.modifiedby, 
         [${dbxschemaname}].requestmessage.softdeleteflag)
         VALUES (
            @reqMsgId, 
            @customerReqId, 
            N'Share the current status of the request', 
            N'CUSTOMER', 
            @customerId, 
            N'John bailey', 
            3, 
            N'TRUE', 
            N'KonyUser', 
            N'konyolbuser', 
            0)

      SET @customerReqId = N'REC' + CAST((CAST(@customerId AS float(53)) + 1) AS varchar(50))

      INSERT [${dbxschemaname}].customerrequest(
         [${dbxschemaname}].customerrequest.id, 
         [${dbxschemaname}].customerrequest.RequestCategory_id, 
         [${dbxschemaname}].customerrequest.Customer_id, 
         [${dbxschemaname}].customerrequest.Priority, 
         [${dbxschemaname}].customerrequest.Status_id, 
         [${dbxschemaname}].customerrequest.RequestSubject, 
         [${dbxschemaname}].customerrequest.AssignedTo, 
         [${dbxschemaname}].customerrequest.Accountid, 
         [${dbxschemaname}].customerrequest.lastupdatedbycustomer, 
         [${dbxschemaname}].customerrequest.createdby, 
         [${dbxschemaname}].customerrequest.createdts, 
         [${dbxschemaname}].customerrequest.lastmodifiedts, 
         [${dbxschemaname}].customerrequest.synctimestamp, 
         [${dbxschemaname}].customerrequest.softdeleteflag)
         VALUES (
            @customerReqId, 
            N'RCID_CREDITCARD', 
            @customerId, 
            N'High', 
            N'SID_INPROGRESS', 
            N'Bill Payment Failed', 
            N'UID10', 
            N'1234', 
            1, 
            N'admin2', 
            isnull(getdate(), getdate()), 
            isnull(getdate(), getdate()), 
            isnull(getdate(), getdate()), 
            0)

      SET @reqMsgId = N'MSG' + CAST((CAST(@customerId AS float(53)) + 3) AS varchar(50))


      INSERT [${dbxschemaname}].requestmessage(
         [${dbxschemaname}].requestmessage.id, 
         [${dbxschemaname}].requestmessage.CustomerRequest_id, 
         [${dbxschemaname}].requestmessage.MessageDescription, 
         [${dbxschemaname}].requestmessage.RepliedBy, 
         [${dbxschemaname}].requestmessage.RepliedBy_id, 
         [${dbxschemaname}].requestmessage.RepliedBy_Name, 
         [${dbxschemaname}].requestmessage.ReplySequence, 
         [${dbxschemaname}].requestmessage.IsRead, 
         [${dbxschemaname}].requestmessage.createdby, 
         [${dbxschemaname}].requestmessage.modifiedby, 
         [${dbxschemaname}].requestmessage.createdts, 
         [${dbxschemaname}].requestmessage.lastmodifiedts, 
         [${dbxschemaname}].requestmessage.synctimestamp, 
         [${dbxschemaname}].requestmessage.softdeleteflag)
         VALUES (
            @reqMsgId, 
            @customerReqId, 
            N'<p><span style="color: #000000;">Hi,</span></p>'+NCHAR(10)+N'<p><span style="color: #000000;">&nbsp;</span></p>'+NCHAR(10)+N'<p><span style="color: #000000;">I had scheduled a bill payment to my internet provider- AT&amp;T for $25, this Monday but the payment failed. Can you help me out?</span></p>'+NCHAR(10)+N'<p><span style="color: #000000;">&nbsp;</span></p>'+NCHAR(10)+N'<p><span style="font-size: 11.0pt; font-family: ''Calibri'',sans-serif; color: #000000;">Thanks </span></p>', 
            N'CUSTOMER', 
            @customerId, 
            N'John bailey', 
            1, 
            N'TRUE', 
            N'jane.doe', 
            N'jane.doe', 
            isnull(getdate(), getdate()), 
            isnull(getdate(), getdate()), 
            isnull(getdate(), getdate()), 
            0)

      SET @reqMsgId = N'MSG' + CAST((CAST(@customerId AS float(53)) + 4) AS varchar(50))

      INSERT [${dbxschemaname}].requestmessage(
         [${dbxschemaname}].requestmessage.id, 
         [${dbxschemaname}].requestmessage.CustomerRequest_id, 
         [${dbxschemaname}].requestmessage.MessageDescription, 
         [${dbxschemaname}].requestmessage.RepliedBy, 
         [${dbxschemaname}].requestmessage.RepliedBy_id, 
         [${dbxschemaname}].requestmessage.RepliedBy_Name, 
         [${dbxschemaname}].requestmessage.ReplySequence, 
         [${dbxschemaname}].requestmessage.IsRead, 
         [${dbxschemaname}].requestmessage.createdby, 
         [${dbxschemaname}].requestmessage.modifiedby, 
         [${dbxschemaname}].requestmessage.createdts, 
         [${dbxschemaname}].requestmessage.lastmodifiedts, 
         [${dbxschemaname}].requestmessage.synctimestamp, 
         [${dbxschemaname}].requestmessage.softdeleteflag)
         VALUES (
            @reqMsgId, 
            @customerReqId, 
            N'<p><span style="color: #000000;">Dear Customer,</span></p>'+NCHAR(10)+N'<p><span style="color: #000000;">&nbsp;</span></p>'+NCHAR(10)+N'<p><span style="color: #000000;">Thanks for writing to us. We can see that the payment failed because you had insufficient funds in your account on Monday, 19<sup>th</sup> Feb 2018. You seem to have the required balance at the moment. You can try making this payment now. </span></p>'+NCHAR(10)+N'<p><span style="color: #000000;">&nbsp;</span></p>'+NCHAR(10)+N'<p><span style="color: #000000;">Do let us know in case you face any other issues. </span></p>'+NCHAR(10)+N'<p><span style="color: #000000;">&nbsp;</span></p>'+NCHAR(10)+N'<p><span style="color: #000000;">Thanks,</span></p>'+NCHAR(10)+N'<p><span style="color: #000000;">Richard</span></p>'+NCHAR(10)+N'<p><span style="font-size: 11.0pt; font-family: ''Calibri'',sans-serif; color: #000000;">Kony Bank, Customer Service Team</span></p>', 
            N'ADMIN|CSR', 
            N'UID10', 
            N'admin', 
            2, 
            N'TRUE', 
            N'admin1', 
            N'admin1', 
            isnull(getdate(), getdate()), 
            isnull(getdate(), getdate()), 
            isnull(getdate(), getdate()), 
            0)

      SET @reqMsgId = N'MSG' + CAST((CAST(@customerId AS float(53)) + 5) AS varchar(50))


      INSERT [${dbxschemaname}].requestmessage(
         [${dbxschemaname}].requestmessage.id, 
         [${dbxschemaname}].requestmessage.CustomerRequest_id, 
         [${dbxschemaname}].requestmessage.MessageDescription, 
         [${dbxschemaname}].requestmessage.RepliedBy, 
         [${dbxschemaname}].requestmessage.RepliedBy_id, 
         [${dbxschemaname}].requestmessage.RepliedBy_Name, 
         [${dbxschemaname}].requestmessage.ReplySequence, 
         [${dbxschemaname}].requestmessage.IsRead, 
         [${dbxschemaname}].requestmessage.createdby, 
         [${dbxschemaname}].requestmessage.modifiedby, 
         [${dbxschemaname}].requestmessage.createdts, 
         [${dbxschemaname}].requestmessage.lastmodifiedts, 
         [${dbxschemaname}].requestmessage.synctimestamp, 
         [${dbxschemaname}].requestmessage.softdeleteflag)
         VALUES (
            @reqMsgId, 
            @customerReqId, 
            N'<p><span style="color: #000000;">Hi Richard,</span></p><p><span style="color: #000000;">&nbsp;</span></p><p><span style="color: #000000;">Thanks for letting me know. I will go ahead with the payment now. </span></p><p><span style="color: #000000;">&nbsp;</span></p><p><span style="font-size: 11.0pt; font-family: ''Calibri'',sans-serif; color: #000000;">Thanks</span></p>', 
            N'CUSTOMER', 
            @customerId, 
            N'John bailey', 
            3, 
            N'TRUE', 
            N'jane.doe', 
            N'jane.doe', 
            isnull(getdate(), getdate()), 
            isnull(getdate(), getdate()), 
            isnull(getdate(), getdate()), 
            0)

      SET @customerReqId = N'REC' + CAST((CAST(@customerId AS float(53)) + 2) AS varchar(50))


      INSERT [${dbxschemaname}].customerrequest(
         [${dbxschemaname}].customerrequest.id, 
         [${dbxschemaname}].customerrequest.RequestCategory_id, 
         [${dbxschemaname}].customerrequest.Customer_id, 
         [${dbxschemaname}].customerrequest.Priority, 
         [${dbxschemaname}].customerrequest.Status_id, 
         [${dbxschemaname}].customerrequest.RequestSubject, 
         [${dbxschemaname}].customerrequest.AssignedTo, 
         [${dbxschemaname}].customerrequest.Accountid, 
         [${dbxschemaname}].customerrequest.lastupdatedbycustomer, 
         [${dbxschemaname}].customerrequest.createdby, 
         [${dbxschemaname}].customerrequest.createdts, 
         [${dbxschemaname}].customerrequest.lastmodifiedts, 
         [${dbxschemaname}].customerrequest.synctimestamp, 
         [${dbxschemaname}].customerrequest.softdeleteflag)
         VALUES (
            @customerReqId, 
            N'RCID_CREDITCARD', 
            @customerId, 
            N'High', 
            N'SID_INPROGRESS', 
            N'International Transactions on Credit Card', 
            N'UID10', 
            N'1234', 
            0, 
            N'admin2', 
            isnull(getdate(), getdate()), 
            isnull(getdate(), getdate()), 
            isnull(getdate(), getdate()), 
            0)

      SET @reqMsgId = N'MSG' + CAST((CAST(@customerId AS float(53)) + 6) AS varchar(50))


      INSERT [${dbxschemaname}].requestmessage(
         [${dbxschemaname}].requestmessage.id, 
         [${dbxschemaname}].requestmessage.CustomerRequest_id, 
         [${dbxschemaname}].requestmessage.MessageDescription, 
         [${dbxschemaname}].requestmessage.RepliedBy, 
         [${dbxschemaname}].requestmessage.RepliedBy_id, 
         [${dbxschemaname}].requestmessage.RepliedBy_Name, 
         [${dbxschemaname}].requestmessage.ReplySequence, 
         [${dbxschemaname}].requestmessage.IsRead, 
         [${dbxschemaname}].requestmessage.createdby, 
         [${dbxschemaname}].requestmessage.modifiedby, 
         [${dbxschemaname}].requestmessage.createdts, 
         [${dbxschemaname}].requestmessage.lastmodifiedts, 
         [${dbxschemaname}].requestmessage.synctimestamp, 
         [${dbxschemaname}].requestmessage.softdeleteflag)
         VALUES (
            @reqMsgId, 
            @customerReqId, 
            N' <p><span style="color: #000000;">Dear Customer,</span></p> <p><span style="color: #000000;">&nbsp;</span></p> <p><span style="color: #000000;">Thank you for informing us about your travel plans. Your card is already enabled for international transactions in Australia. You can use the card at all POS transaction points hassle free. Have a great trip!</span></p> <p><span style="color: #000000;">&nbsp;</span></p> <p><span style="font-size: 11.0pt; font-family: ''Calibri'',sans-serif; color: #000000;">Thanks</span></p> <p><span style="font-size: 11.0pt; font-family: ''Calibri'',sans-serif; color: #000000;">Celeste</span></p> <p><span style="font-size: 11.0pt; font-family: ''Calibri'',sans-serif; color: #000000;">Kony Bank, Customer Service</span></p>', 
            N'ADMIN|CSR', 
            N'UID10', 
            N'admin', 
            2, 
            N'TRUE', 
            N'admin1', 
            N'admin1', 
            '2018-02-26 19:11:15', 
            '2018-02-26 19:11:15', 
            '2018-02-26 19:11:15', 
            0)

      SET @reqMsgId = N'MSG' + CAST((CAST(@customerId AS float(53)) + 7) AS varchar(50))


      INSERT [${dbxschemaname}].requestmessage(
         [${dbxschemaname}].requestmessage.id, 
         [${dbxschemaname}].requestmessage.CustomerRequest_id, 
         [${dbxschemaname}].requestmessage.MessageDescription, 
         [${dbxschemaname}].requestmessage.RepliedBy, 
         [${dbxschemaname}].requestmessage.RepliedBy_id, 
         [${dbxschemaname}].requestmessage.RepliedBy_Name, 
         [${dbxschemaname}].requestmessage.ReplySequence, 
         [${dbxschemaname}].requestmessage.IsRead, 
         [${dbxschemaname}].requestmessage.createdby, 
         [${dbxschemaname}].requestmessage.modifiedby, 
         [${dbxschemaname}].requestmessage.createdts, 
         [${dbxschemaname}].requestmessage.lastmodifiedts, 
         [${dbxschemaname}].requestmessage.synctimestamp, 
         [${dbxschemaname}].requestmessage.softdeleteflag)
         VALUES (
            @reqMsgId, 
            @customerReqId, 
            N' <p><span style="color: #000000;">Hi,</span></p> <p><span style="color: #000000;">&nbsp;</span></p> <p><span style="color: #000000;">I will be travelling to Australia from 1<sup>st</sup>-10<sup>th</sup> May 2018 and will be using my Kony Bank Credit Card for the same. Please enable international transactions on this card for this period.</span></p> <p><span style="color: #000000;">&nbsp;</span></p> <p><span style="font-size: 11.0pt; font-family: ''Calibri'',sans-serif; color: #000000;">Thanks</span></p>', 
            N'CUSTOMER', 
            @customerId, 
            N'John bailey', 
            2, 
            N'TRUE', 
            N'admin1', 
            N'admin1', 
            '2018-02-26 19:11:15', 
            '2018-02-26 19:11:15', 
            '2018-02-26 19:11:15', 
            0)

      SELECT @LogId = CAST((CAST(REPLACE(REPLACE(REPLACE(CONVERT(varchar(20), getdate(), 120), '-', ''), ' ', ''), ':', '') AS bigint) + 1) AS varchar(50))

 
         INSERT  INTO konydbplog.customeractivity(
            id, 
            sessionId, 
            username, 
            moduleName, 
            activityType, 
            description, 
            eventts, 
            status, 
            channel, 
            ipAddress, 
            device, 
            operatingSystem, 
            referenceId, 
            errorCode, 
            createdBy, 
            createdOn)
             VALUES (
               @LogID + 1, 
               @LogID + 1, 
               @_username, 
               'Login', 
               'Login', 
               'Login with Username/Password', 
               CURRENT_TIMESTAMP, 
               'Open', 
               'Web', 
               '10.10.1.1', 
               'Chrome', 
               'Windows', 
               '1234', 
               '0', 
               'olbuser', 
               CURRENT_TIMESTAMP)
      

      
         INSERT  INTO konydbplog.customeractivity(
            id, 
            sessionId, 
            username, 
            moduleName, 
            activityType, 
            description, 
            eventts, 
            status, 
            channel, 
            ipAddress, 
            device, 
            operatingSystem, 
            referenceId, 
            errorCode, 
            createdBy, 
            createdOn)
             VALUES (
               @LogID + 2, 
               @LogID + 2, 
               @_username, 
               'Profile', 
               'Update contact number', 
               'Changed Primary Contact Number', 
               CURRENT_TIMESTAMP, 
               'Open', 
               'Web', 
               '10.10.1.2', 
               'Chrome', 
               'Windows', 
               '1234', 
               '0', 
               'olbuser', 
               CURRENT_TIMESTAMP)
   
         INSERT  INTO konydbplog.customeractivity(
            id, 
            sessionId, 
            username, 
            moduleName, 
            activityType, 
            description, 
            eventts, 
            status, 
            channel, 
            ipAddress, 
            device, 
            operatingSystem, 
            referenceId, 
            errorCode, 
            createdBy, 
            createdOn)
             VALUES (
               @LogID + 3, 
               @LogID + 3, 
               @_username, 
               'Bill Pay', 
               'Activate Bill Payment Service', 
               'Activate Bill Payment Service', 
               CURRENT_TIMESTAMP, 
               'Open', 
               'Web', 
               '10.10.1.3', 
               'Chrome', 
               'Windows', 
               '1234', 
               '0', 
               'olbuser', 
               CURRENT_TIMESTAMP)
     


         INSERT  INTO konydbplog.customeractivity(
            id, 
            sessionId, 
            username, 
            moduleName, 
            activityType, 
            description, 
            eventts, 
            status, 
            channel, 
            ipAddress, 
            device, 
            operatingSystem, 
            referenceId, 
            errorCode, 
            createdBy, 
            createdOn)
             VALUES (
               @LogID + 4, 
               @LogID + 4, 
               @_username, 
               'Bill Pay', 
               'Add Payee', 
               'Added Payee CitiBank Credit Card', 
               CURRENT_TIMESTAMP, 
               'Open', 
               'Web', 
               '10.10.1.4', 
               'Chrome', 
               'Windows', 
               '', 
               '', 
               '', 
               CURRENT_TIMESTAMP)
    
         INSERT  INTO konydbplog.customeractivity(
            id, 
            sessionId, 
            username, 
            moduleName, 
            activityType, 
            description, 
            eventts, 
            status, 
            channel, 
            ipAddress, 
            device, 
            operatingSystem, 
            referenceId, 
            errorCode, 
            createdBy, 
            createdOn)
             VALUES (
               @LogID + 5, 
               @LogID + 5, 
               @_username, 
               'Bill Pay', 
               'Pay Bill', 
               'BillPayment to CitiBank Credit Card $450', 
               CURRENT_TIMESTAMP, 
               'Open', 
               'Web', 
               '10.10.1.5', 
               'Chrome', 
               'Windows', 
               '', 
               '', 
               '', 
               CURRENT_TIMESTAMP)
     
         INSERT  INTO konydbplog.customeractivity(
            id, 
            sessionId, 
            username, 
            moduleName, 
            activityType, 
            description, 
            eventts, 
            status, 
            channel, 
            ipAddress, 
            device, 
            operatingSystem, 
            referenceId, 
            errorCode, 
            createdBy, 
            createdOn)
             VALUES (
               @LogID + 6, 
               @LogID + 6, 
               @_username, 
               'Logout', 
               'Logout', 
               'Logout', 
               CURRENT_TIMESTAMP, 
               'Open', 
               'Web', 
               '10.10.1.6', 
               'Chrome', 
               'Windows', 
               '1234', 
               '0', 
               'olbuser', 
               CURRENT_TIMESTAMP)
      

         INSERT  INTO konydbplog.customeractivity(
            id, 
            sessionId, 
            username, 
            moduleName, 
            activityType, 
            description, 
            eventts, 
            status, 
            channel, 
            ipAddress, 
            device, 
            operatingSystem, 
            referenceId, 
            errorCode, 
            createdBy, 
            createdOn)
             VALUES (
               @LogID + 7, 
               @LogID + 7, 
               @_username, 
               'Login', 
               'Login', 
               'Login with FaceID', 
               CURRENT_TIMESTAMP, 
               'Open', 
               'Mobile', 
               '10.10.1.7', 
               'Moto', 
               'Android', 
               '1234', 
               '0', 
               'olbuser', 
               CURRENT_TIMESTAMP)

         INSERT  INTO konydbplog.customeractivity(
            id, 
            sessionId, 
            username, 
            moduleName, 
            activityType, 
            description, 
            eventts, 
            status, 
            channel, 
            ipAddress, 
            device, 
            operatingSystem, 
            referenceId, 
            errorCode, 
            createdBy, 
            createdOn)
             VALUES (
               @LogID + 8, 
               @LogID + 8, 
               @_username, 
               'Accounts', 
               'View Accounts', 
               'View Checking Account XX2455', 
               CURRENT_TIMESTAMP, 
               'Open', 
               'Mobile', 
               '10.10.1.8', 
               'Moto', 
               'Android', 
               '1234', 
               '0', 
               'olbuser', 
               CURRENT_TIMESTAMP)
     
         INSERT  INTO konydbplog.customeractivity(
            id, 
            sessionId, 
            username, 
            moduleName, 
            activityType, 
            description, 
            eventts, 
            status, 
            channel, 
            ipAddress, 
            device, 
            operatingSystem, 
            referenceId, 
            errorCode, 
            createdBy, 
            createdOn)
             VALUES (
               @LogID + 9, 
               @LogID + 9, 
               @_username, 
               'Transfers', 
               'Add Recipient', 
               'Add IntraBank Recipient Tom Brummet', 
               CURRENT_TIMESTAMP, 
               'Open', 
               'Mobile', 
               '10.10.1.9', 
               'Moto', 
               'Android', 
               '1234', 
               '0', 
               'olbuser', 
               CURRENT_TIMESTAMP)
     



      
         INSERT  INTO konydbplog.customeractivity(
            id, 
            sessionId, 
            username, 
            moduleName, 
            activityType, 
            description, 
            eventts, 
            status, 
            channel, 
            ipAddress, 
            device, 
            operatingSystem, 
            referenceId, 
            errorCode, 
            createdBy, 
            createdOn)
             VALUES (
               @LogID + 10, 
               @LogID + 10, 
               @_username, 
               'Transfers', 
               'IntraBank Fund Transfer', 
               'IntraBank FT to Tom Brumet $150', 
               CURRENT_TIMESTAMP, 
               'Open', 
               'Mobile', 
               '10.10.1.10', 
               'Moto', 
               'Android', 
               '1234', 
               '0', 
               'olbuser', 
               CURRENT_TIMESTAMP)
      
         INSERT  INTO konydbplog.customeractivity(
            id, 
            sessionId, 
            username, 
            moduleName, 
            activityType, 
            description, 
            eventts, 
            status, 
            channel, 
            ipAddress, 
            device, 
            operatingSystem, 
            referenceId, 
            errorCode, 
            createdBy, 
            createdOn)
             VALUES (
               @LogID + 11, 
               @LogID + 11, 
               @_username, 
               'Accounts', 
               'View Accounts', 
               'View Checking Account XX2455', 
               CURRENT_TIMESTAMP, 
               'Open', 
               'Mobile', 
               '10.10.1.11', 
               'Moto', 
               'Android', 
               '1234', 
               '0', 
               'olbuser', 
               CURRENT_TIMESTAMP)
     
         INSERT  INTO konydbplog.customeractivity(
            id, 
            sessionId, 
            username, 
            moduleName, 
            activityType, 
            description, 
            eventts, 
            status, 
            channel, 
            ipAddress, 
            device, 
            operatingSystem, 
            referenceId, 
            errorCode, 
            createdBy, 
            createdOn)
             VALUES (
               @LogID + 12, 
               @LogID + 12, 
               @_username, 
               'Messages', 
               'Send Message', 
               'Send Message subject Bill Payment Failed', 
               CURRENT_TIMESTAMP, 
               'Open', 
               'Mobile', 
               '10.10.1.12', 
               'Moto', 
               'Android', 
               '1234', 
               '0', 
               'olbuser', 
               CURRENT_TIMESTAMP)
      
         INSERT  INTO konydbplog.customeractivity(
            id, 
            sessionId, 
            username, 
            moduleName, 
            activityType, 
            description, 
            eventts, 
            status, 
            channel, 
            ipAddress, 
            device, 
            operatingSystem, 
            referenceId, 
            errorCode, 
            createdBy, 
            createdOn)
             VALUES (
               @LogID + 13, 
               @LogID + 13, 
               @_username, 
               'P2P', 
               'Send Money', 
               'Send Money to Judy Blume $25', 
               CURRENT_TIMESTAMP, 
               'Open', 
               'Mobile', 
               '10.10.1.13', 
               'Moto', 
               'Android', 
               '1234', 
               '0', 
               'olbuser', 
               CURRENT_TIMESTAMP)
     
         INSERT  INTO konydbplog.customeractivity(
            id, 
            sessionId, 
            username, 
            moduleName, 
            activityType, 
            description, 
            eventts, 
            status, 
            channel, 
            ipAddress, 
            device, 
            operatingSystem, 
            referenceId, 
            errorCode, 
            createdBy, 
            createdOn)
             VALUES (
               @LogID + 14, 
               @LogID + 14, 
               @_username, 
               'Logout', 
               'Logout', 
               'Logout', 
               CURRENT_TIMESTAMP, 
               'Open', 
               'Mobile', 
               '10.10.1.14', 
               'Moto', 
               'Android', 
               '1234', 
               '0', 
               'olbuser', 
               CURRENT_TIMESTAMP)
     
         INSERT  INTO konydbplog.customeractivity(
            id, 
            sessionId, 
            username, 
            moduleName, 
            activityType, 
            description, 
            eventts, 
            status, 
            channel, 
            ipAddress, 
            device, 
            operatingSystem, 
            referenceId, 
            errorCode, 
            createdBy, 
            createdOn)
             VALUES (
               @LogID + 15, 
               @LogID + 15, 
               @_username, 
               'Login', 
               'Login', 
               'Login with Username/Password', 
               CURRENT_TIMESTAMP, 
               'Open', 
               'Web', 
               '10.10.1.15', 
               'Chrome', 
               'Windows', 
               '', 
               '', 
               '', 
               CURRENT_TIMESTAMP)

         INSERT  INTO konydbplog.customeractivity(
            id, 
            sessionId, 
            username, 
            moduleName, 
            activityType, 
            description, 
            eventts, 
            status, 
            channel, 
            ipAddress, 
            device, 
            operatingSystem, 
            referenceId, 
            errorCode, 
            createdBy, 
            createdOn)
             VALUES (
               @LogID + 16, 
               @LogID + 16, 
               @_username, 
               'Wire Transfer', 
               'Manage Payee', 
               'Edit Details for Payee Jane', 
               CURRENT_TIMESTAMP, 
               'Open', 
               'Web', 
               '10.10.1.16', 
               'Chrome', 
               'Windows', 
               '', 
               '', 
               '', 
               CURRENT_TIMESTAMP)

         INSERT  INTO konydbplog.customeractivity(
            id, 
            sessionId, 
            username, 
            moduleName, 
            activityType, 
            description, 
            eventts, 
            status, 
            channel, 
            ipAddress, 
            device, 
            operatingSystem, 
            referenceId, 
            errorCode, 
            createdBy, 
            createdOn)
             VALUES (
               @LogId + 17, 
               @LogID + 17, 
               @_username, 
               'Wire Transfer', 
               'Wire Transfer', 
               'Wire Transfer to Jane $200', 
               CURRENT_TIMESTAMP, 
               'Open', 
               'Web', 
               '10.10.1.17', 
               'Chrome', 
               'Windows', 
               '', 
               '', 
               '', 
               CURRENT_TIMESTAMP)

         INSERT  INTO konydbplog.customeractivity(
            id, 
            sessionId, 
            username, 
            moduleName, 
            activityType, 
            description, 
            eventts, 
            status, 
            channel, 
            ipAddress, 
            device, 
            operatingSystem, 
            referenceId, 
            errorCode, 
            createdBy, 
            createdOn)
             VALUES (
               @LogID + 18, 
               @LogID + 18, 
               @_username, 
               'Accounts', 
               'View Accounts', 
               'View Checking Account XX2455', 
               CURRENT_TIMESTAMP, 
               'Open', 
               'Web', 
               '10.10.1.18', 
               'Chrome', 
               'Windows', 
               '', 
               '', 
               '', 
               CURRENT_TIMESTAMP)

         INSERT  INTO konydbplog.customeractivity(
            id, 
            sessionId, 
            username, 
            moduleName, 
            activityType, 
            description, 
            eventts, 
            status, 
            channel, 
            ipAddress, 
            device, 
            operatingSystem, 
            referenceId, 
            errorCode, 
            createdBy, 
            createdOn)
             VALUES (
               @LogID + 19, 
               @LogID + 19, 
               @_username, 
               'Accounts', 
               'Add External Account', 
               'Add External Account Chase Bank xx7583', 
               CURRENT_TIMESTAMP, 
               'Open', 
               'Web', 
               '10.10.1.19', 
               'Chrome', 
               'Windows', 
               '', 
               '', 
               '', 
               CURRENT_TIMESTAMP)
  
         INSERT  INTO konydbplog.customeractivity(
            id, 
            sessionId, 
            username, 
            moduleName, 
            activityType, 
            description, 
            eventts, 
            status, 
            channel, 
            ipAddress, 
            device, 
            operatingSystem, 
            referenceId, 
            errorCode, 
            createdBy, 
            createdOn)
             VALUES (
               @LogID + 20, 
               @LogID + 20, 
               @_username, 
               'Logout', 
               'Logout', 
               'Logout', 
               CURRENT_TIMESTAMP, 
               'Open', 
               'Web', 
               '10.10.1.20', 
               'Chrome', 
               'Windows', 
               '', 
               '', 
               '', 
               CURRENT_TIMESTAMP)
    

      INSERT [${dbxschemaname}].customernotification(
         Customer_id, 
         Notification_id, 
         IsRead, 
         createdby, 
         modifiedby, 
         createdts, 
         lastmodifiedts, 
         synctimestamp, 
         softdeleteflag)
         VALUES (
            @customerId, 
            N'NID1', 
            0, 
            N'Kony User', 
            N'Kony Dev', 
            isnull(getdate(), getdate()), 
            isnull(getdate(), getdate()), 
            isnull(getdate(), getdate()), 
            0)

      INSERT [${dbxschemaname}].customernotification(
         Customer_id, 
         Notification_id, 
         IsRead, 
         createdby, 
         modifiedby, 
         createdts, 
         lastmodifiedts, 
         synctimestamp, 
         softdeleteflag)
         VALUES (
            @customerId, 
            N'NID10', 
            0, 
            N'Kony User', 
            N'Kony Dev', 
            isnull(getdate(), getdate()), 
            isnull(getdate(), getdate()), 
            isnull(getdate(), getdate()), 
            0)

    
      INSERT [${dbxschemaname}].customernotification(
         Customer_id, 
         Notification_id, 
         IsRead, 
         createdby, 
         modifiedby, 
         createdts, 
         lastmodifiedts, 
         synctimestamp, 
         softdeleteflag)
         VALUES (
            @customerId, 
            N'NID12', 
            0, 
            N'Kony User', 
            N'Kony Dev', 
            isnull(getdate(), getdate()), 
            isnull(getdate(), getdate()), 
            isnull(getdate(), getdate()), 
            0)

   

      INSERT [${dbxschemaname}].customernotification(
         Customer_id, 
         Notification_id, 
         IsRead, 
         createdby, 
         modifiedby, 
         createdts, 
         lastmodifiedts, 
         synctimestamp, 
         softdeleteflag)
         VALUES (
            @customerId, 
            N'NID13', 
            0, 
            N'Kony User', 
            N'Kony Dev', 
            isnull(getdate(), getdate()), 
            isnull(getdate(), getdate()), 
            isnull(getdate(), getdate()), 
            0)

  

      INSERT [${dbxschemaname}].customernotification(
         Customer_id, 
         Notification_id, 
         IsRead, 
         createdby, 
         modifiedby, 
         createdts, 
         lastmodifiedts, 
         synctimestamp, 
         softdeleteflag)
         VALUES (
            @customerId, 
            N'NID14', 
            0, 
            N'Kony User', 
            N'Kony Dev', 
            isnull(getdate(), getdate()), 
            isnull(getdate(), getdate()), 
            isnull(getdate(), getdate()), 
            0)


      INSERT [${dbxschemaname}].customernotification(
         Customer_id, 
         Notification_id, 
         IsRead, 
         createdby, 
         modifiedby, 
         createdts, 
         lastmodifiedts, 
         synctimestamp, 
         softdeleteflag)
         VALUES (
            @customerId, 
            N'NID2', 
            0, 
            N'Kony User', 
            N'Kony Dev', 
            isnull(getdate(), getdate()), 
            isnull(getdate(), getdate()), 
            isnull(getdate(), getdate()), 
            0)


      INSERT [${dbxschemaname}].customernotification(
         Customer_id, 
         Notification_id, 
         IsRead, 
         createdby, 
         modifiedby, 
         createdts, 
         lastmodifiedts, 
         synctimestamp, 
         softdeleteflag)
         VALUES (
            @customerId, 
            N'NID3', 
            0, 
            N'Kony User', 
            N'Kony Dev', 
            isnull(getdate(), getdate()), 
            isnull(getdate(), getdate()), 
            isnull(getdate(), getdate()), 
            0)


      INSERT [${dbxschemaname}].customernotification(
         Customer_id, 
         Notification_id, 
         IsRead, 
         createdby, 
         modifiedby, 
         createdts, 
         lastmodifiedts, 
         synctimestamp, 
         softdeleteflag)
         VALUES (
            @customerId, 
            N'NID4', 
            0, 
            N'Kony User', 
            N'Kony Dev', 
            isnull(getdate(), getdate()), 
            isnull(getdate(), getdate()), 
            isnull(getdate(), getdate()), 
            0)



      INSERT [${dbxschemaname}].customernotification(
         Customer_id, 
         Notification_id, 
         IsRead, 
         createdby, 
         modifiedby, 
         createdts, 
         lastmodifiedts, 
         synctimestamp, 
         softdeleteflag)
         VALUES (
            @customerId, 
            N'NID5', 
            0, 
            N'Kony User', 
            N'Kony Dev', 
            isnull(getdate(), getdate()), 
            isnull(getdate(), getdate()), 
            isnull(getdate(), getdate()), 
            0)

      INSERT [${dbxschemaname}].customernotification(
         Customer_id, 
         Notification_id, 
         IsRead, 
         createdby, 
         modifiedby, 
         createdts, 
         lastmodifiedts, 
         synctimestamp, 
         softdeleteflag)
         VALUES (
            @customerId, 
            N'NID6', 
            0, 
            N'Kony User', 
            N'Kony Dev', 
            isnull(getdate(), getdate()), 
            isnull(getdate(), getdate()), 
            isnull(getdate(), getdate()), 
            0)

      INSERT [${dbxschemaname}].customernotification(
         Customer_id, 
         Notification_id, 
         IsRead, 
         createdby, 
         modifiedby, 
         createdts, 
         lastmodifiedts, 
         synctimestamp, 
         softdeleteflag)
         VALUES (
            @customerId, 
            N'NID7', 
            0, 
            N'Kony User', 
            N'Kony Dev', 
            isnull(getdate(), getdate()), 
            isnull(getdate(), getdate()), 
            isnull(getdate(), getdate()), 
            0)


      INSERT [${dbxschemaname}].customernotification(
         Customer_id, 
         Notification_id, 
         IsRead, 
         createdby, 
         modifiedby, 
         createdts, 
         lastmodifiedts, 
         synctimestamp, 
         softdeleteflag)
         VALUES (
            @customerId, 
            N'NID8', 
            0, 
            N'Kony User', 
            N'Kony Dev', 
            isnull(getdate(), getdate()), 
            isnull(getdate(), getdate()), 
            isnull(getdate(), getdate()), 
            0)

      INSERT [${dbxschemaname}].customernotification(
         Customer_id, 
         Notification_id, 
         IsRead, 
         createdby, 
         modifiedby, 
         createdts, 
         lastmodifiedts, 
         synctimestamp, 
         softdeleteflag)
         VALUES (
            @customerId, 
            N'NID9', 
            0, 
            N'Kony User', 
            N'Kony Dev', 
            isnull(getdate(), getdate()), 
            isnull(getdate(), getdate()), 
            isnull(getdate(), getdate()), 
            0)

   END
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[fetch_achfiles_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[fetch_achfiles_proc]  
   @_customerId nvarchar(50),
   @_achFile_id nvarchar(50),
   @_featureactionlist nvarchar(max),
   @_queryType nvarchar(50),
   @_filterByParam nvarchar(max),
   @_filterByValue nvarchar(max),
   @_searchString nvarchar(50),
   @_sortByParam nvarchar(50),
   @_sortOrder nvarchar(50),
   @_pageSize nvarchar(50),
   @_pageOffset nvarchar(50)
AS 

   BEGIN

     SET  XACT_ABORT  ON
     SET  NOCOUNT  ON
     DECLARE @combinedIds nvarchar(max)
     DECLARE @numOfParams int
     DECLARE @searchQuery nvarchar(max)
     DECLARE @idx int
     DECLARE @filterParam nvarchar(max)
     DECLARE @filterValue nvarchar(max)
     DECLARE @companyId nvarchar(max)
     DECLARE @customerMatrixIds nvarchar(max)
     DECLARE @alreadyApprovedIds nvarchar(max)
     DECLARE @approvalRequestIds nvarchar(max)
     DECLARE @queryTypecondition nvarchar(max)
     DECLARE @paginationQuery nvarchar(max)
     DECLARE @features nvarchar(max)
     DECLARE @createActions nvarchar(max)
     DECLARE @companyRequestIds nvarchar(max)
     DECLARE @customerAcounts nvarchar(max)
     DECLARE @select_statement nvarchar(max)
     
      SET @numOfParams = 0
      SET @searchQuery = ''
      SET @idx = 1
      SET @paginationQuery = 0

     SET @combinedIds = (select String_agg(CAST(customer.id as nvarchar(max)), ',') from [${dbxschemaname}].customer where customer.combinedUserId = @_customerId)
     IF @combinedIds is NULL      
        SET @combinedIds = @_customerId;
     ELSE 
        SET @combinedIds =(@_customerId + ',' +@combinedIds)


		SET @_filterByParam = CASE WHEN (@_filterByParam IS NULL OR @_filterByParam='' ) THEN  '' ELSE @_filterByParam END
        SET @_filterByValue = CASE WHEN (@_filterByValue IS NULL OR @_filterByValue='') THEN '' ELSE  @_filterByValue END
        SET @_achFile_id = CASE WHEN (@_achFile_id = '' OR @_achFile_id IS NULL) THEN '%' ELSE @_achFile_id END


      IF LEN(@_filterByParam) > 0
         SET @numOfParams = LEN(@_filterByParam) - LEN(replace(@_filterByParam, ',', '')) + 1
      
      WHILE (@idx <= @numOfParams)
         BEGIN
            SET @filterParam = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX( @_filterByParam, ',', @idx), ',', -1 );
            SET @filterValue = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX( @_filterByValue, ',', @idx), ',', -1 );
            SET @searchQuery = @searchQuery+ ' AND ('+@filterParam+' LIKE '''+@filterValue+''' )'
            SET @idx = @idx + 1   
         END

      SET @companyId = ( SELECT customer.Organization_Id FROM [${dbxschemaname}].customer WHERE customer.id = @_customerId)
      IF @companyId IS NULL
         SET @companyId = ''
      IF @_featureactionlist IS NULL
         GOTO MAINLABEL$leave

        SET @_sortByParam = CASE WHEN (@_sortByParam = '' OR @_sortByParam IS NULL) THEN 'createdts' ELSE @_sortByParam END
		SET @_sortOrder = CASE WHEN (@_sortOrder = '' OR @_sortOrder IS NULL) THEN 'DESC' ELSE @_sortOrder END



      SET @customerMatrixIds = ( SELECT String_agg(CAST(customerapprovalmatrix.approvalMatrixId as nvarchar(max)),',') FROM [${dbxschemaname}].customerapprovalmatrix WHERE [${dbxschemaname}].FIND_IN_SET(customerapprovalmatrix.customerId,@combinedIds) <> 0)
      IF @customerMatrixIds IS NULL
         SET @customerMatrixIds = ''
      
      SET @alreadyApprovedIds = (SELECT String_agg(CAST(bbactedrequest.requestId as nvarchar(max)) ,',') FROM [${dbxschemaname}].bbactedrequest WHERE [${dbxschemaname}].FIND_IN_SET(bbactedrequest.createdby,@combinedIds) <> 0 AND bbactedrequest.action = 'Approved')
      IF @alreadyApprovedIds IS NULL
      BEGIN
         SET @alreadyApprovedIds = ''
      END

      SET @approvalRequestIds = (SELECT STRING_AGG(CAST(requestId as nvarchar(max)), ',') 
        							FROM [${dbxschemaname}].requestapprovalmatrix
        							INNER JOIN [${dbxschemaname}].approvalmatrix ON requestapprovalmatrix.approvalMatrixId = approvalmatrix.id
									INNER JOIN [${dbxschemaname}].approvalrule ON approvalmatrix.approvalruleId = approvalrule.id
        								WHERE [${dbxschemaname}].FIND_IN_SET(CAST(requestapprovalmatrix.approvalMatrixId as nvarchar(max)),  @customerMatrixIds)  <> 0
        								AND [${dbxschemaname}].FIND_IN_SET(CAST(requestapprovalmatrix.requestId as nvarchar(max)), @alreadyApprovedIds) = 0
        								AND ((approvalrule.numberOfApprovals = -1 AND requestapprovalmatrix.receivedApprovals < (SELECT COUNT(DISTINCT(customerId)) FROM [${dbxschemaname}].customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)) 
												OR
											(approvalrule.numberOfApprovals != -1 AND requestapprovalmatrix.receivedApprovals < approvalrule.numberOfApprovals))
        							)

     IF @approvalRequestIds IS NULL
      BEGIN
        SET @approvalRequestIds = ''
      END

      SET @queryTypecondition = 
         CASE 
            WHEN (@_queryType = 'myRequests') THEN (' AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].achfile.createdby, CAST(''') + (@combinedIds) + (''' as nvarchar(max)))<>0 AND ([${dbxschemaname}].bbrequest.status = ''Pending'' OR [${dbxschemaname}].bbrequest.status = ''Approved'' OR [${dbxschemaname}].bbrequest.status = ''Rejected'' ) ')
            ELSE 
               CASE 
                  WHEN (@_queryType = 'pendingForMyApprovals') THEN (' AND [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].achfile.requestId AS nvarchar(max)),  CAST(''') + (@approvalRequestIds) + (''' as nvarchar(max)))<>0  AND [${dbxschemaname}].achfile.status = ''Pending'' ')
                  ELSE 
                     CASE 
                        WHEN (@_queryType = 'rejected') THEN ('AND [${dbxschemaname}].achfile.status = ''Rejected'' ')
                        ELSE 
                           CASE 
                              WHEN (@_achFile_id = '%') THEN (' AND NOT [${dbxschemaname}].achfile.status = ''Withdrawn''')
                              ELSE ''
                           END
                     END
               END
         END


		SET @searchQuery = CASE WHEN (@_searchString IS NULL OR @_searchString = '') THEN @searchQuery ELSE 
                (@searchQuery+ ' AND (achfile.achFileName LIKE ''%'+@_searchString+'%'' OR achfile.requestType LIKE ''%'+@_searchString+'%'')') END
        
        SET @paginationQuery = CASE WHEN (@_pageOffset IS NULL OR @_pageOffset = '' OR @_pageSize IS NULL OR @_pageSize = '') THEN
		'' ELSE (' OFFSET '+@_pageOffset+'  ROWS FETCH NEXT  ' +@_pageSize+'  ROWS ONLY') END


      SET @features = ( SELECT String_agg(CAST(featureaction.Feature_id as nvarchar(max)) ,',') FROM [${dbxschemaname}].featureaction WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id, @_featureactionlist) <> 0 )
      IF @features IS NULL
        BEGIN
          SET @features = ''
        END
 
      SET @createActions = ( SELECT String_agg(CAST(featureaction.id as nvarchar(max)),',') FROM [${dbxschemaname}].featureaction WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.Feature_id, @features) <> 0 AND [${dbxschemaname}].featureaction.id LIKE '%_UPLOAD')
      IF @createActions IS NULL
        BEGIN
          SET @createActions = ''
        END

      SET @companyRequestIds = ( SELECT String_agg(CAST(bbrequest.requestId as nvarchar(max)) ,',') FROM [${dbxschemaname}].bbrequest WHERE [${dbxschemaname}].bbrequest.companyId = @companyId AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbrequest.featureActionId, @createActions) <> 0)
      IF @companyRequestIds IS NULL
        BEGIN
          SET @companyRequestIds = ''
        END


	 SELECT  customerCnt, requestId into #approvalCount1 from
	 (SELECT COUNT(DISTINCT(customerId)) as customerCnt ,requestapprovalmatrix.requestId as requestId FROM [${dbxschemaname}].customerapprovalmatrix INNER JOIN [${dbxschemaname}].requestapprovalmatrix ON (customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId ) WHERE [${dbxschemaname}].FIND_IN_SET(cast(requestapprovalmatrix.requestId as nvarchar(max)), @companyRequestIds) <> 0 group by requestapprovalmatrix.requestId) as approvalCount1

	 SELECT  requestId, totalCnt into #approvalCount2 
	from (select requestId, sum(t) as totalCnt from (
	select bbrequest.requestId as requestId,
         CASE WHEN
			[${dbxschemaname}].approvalrule.numberOfApprovals = -1
			THEN (SELECT count(*)  FROM [${dbxschemaname}].customerapprovalmatrix where customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)
          WHEN [${dbxschemaname}].approvalrule.numberOfApprovals IS NULL OR [${dbxschemaname}].approvalrule.numberOfApprovals='' THEN 0
              ELSE [${dbxschemaname}].approvalrule.numberOfApprovals
          END as t
     from [${dbxschemaname}].customerapprovalmatrix
	 LEFT JOIN [${dbxschemaname}].requestapprovalmatrix ON [${dbxschemaname}].customerapprovalmatrix.approvalMatrixId = [${dbxschemaname}].requestapprovalmatrix.approvalMatrixId
	 LEFT JOIN [${dbxschemaname}].bbrequest ON ([${dbxschemaname}].bbrequest.requestId = [${dbxschemaname}].requestapprovalmatrix.requestId)
	 LEFT JOIN [${dbxschemaname}].approvalmatrix ON ([${dbxschemaname}].requestapprovalmatrix.approvalMatrixId = [${dbxschemaname}].approvalmatrix.id)
	 LEFT JOIN [${dbxschemaname}].approvalrule ON ([${dbxschemaname}].approvalmatrix.approvalruleId = [${dbxschemaname}].approvalrule.id)
     WHERE [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].bbrequest.requestId AS nvarchar(max)),@companyRequestIds)<>0 ) approvals group by requestId) as approvalCount2

	  
	  
	 SET @select_statement = ' 
	  SELECT * FROM
        (SELECT 
         achfile.achFile_id AS achFile_id,
         achfile.achFileName AS achFileName,
         achfile.featureActionId AS featureActionId,
         achfile.debitAmount AS debitAmount,
         achfile.approvalAccounts AS approvalAccounts,
         achfile.debitAccounts AS debitAccounts,
         achfile.createdby AS createdby,
         achfile.createdts AS createdts,
         achfile.requestType AS requestType,
         achfile.numberOfCredits AS numberOfCredits,
         achfile.numberOfDebits AS numberOfDebits,
         achfile.numberOfPrenotes AS numberOfPrenotes,
         achfile.requestId AS requestId,
         achfile.contents AS contents,
         achfile.fileSize AS fileSize,
         achfile.softDelete AS softDelete,
         achfile.creditAmount AS creditAmount,
         achfile.numberOfRecords AS numberOfRecords,
         achfile.achFileFormatType_id AS achFileFormatType_id,
         achfile.status AS status,
         achfile.companyId AS companyId,
         achfile.confirmationNumber AS confirmationNumber,
         customer.UserName AS userName,
         achfileformattype.fileType AS achFileFormatType,
         organisation.Name AS companyName,
         bbrequest.createdby AS requestCreatedby,
         (CASE
            WHEN [${dbxschemaname}].FIND_IN_SET(CAST(achfile.createdby AS nvarchar(max)),CAST(''' + @combinedIds +''' as nvarchar(max))) > 0 THEN ''true''
            ELSE ''false''
          END) as amICreator,
         (CASE 
            WHEN [${dbxschemaname}].FIND_IN_SET(CAST(bbrequest.requestId AS nvarchar(max)),CAST('''+ @approvalRequestIds +''' as nvarchar(max))) >0 THEN ''true''
            ELSE ''false''
         END)
          as amIApprover
      FROM
         (((([${dbxschemaname}].achfile
         LEFT JOIN [${dbxschemaname}].customer ON ([${dbxschemaname}].achfile.createdby = [${dbxschemaname}].customer.id))
         LEFT JOIN [${dbxschemaname}].achfileformattype ON ([${dbxschemaname}].achfile.achFileFormatType_id = [${dbxschemaname}].achfileformattype.id))
         LEFT JOIN [${dbxschemaname}].organisation ON ([${dbxschemaname}].achfile.companyId = [${dbxschemaname}].organisation.id))
         LEFT JOIN [${dbxschemaname}].bbrequest ON ([${dbxschemaname}].achfile.requestId = [${dbxschemaname}].bbrequest.requestId))
         WHERE [${dbxschemaname}].achfile.softDelete = ''0''
            AND ([${dbxschemaname}].achfile.companyId = '''+ @companyId + ''' OR [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].achfile.createdby, CAST('''+ @combinedIds + ''' as nvarchar(max)))<>0)
            AND [${dbxschemaname}].achfile.achFile_id LIKE '''+@_achFile_id + '''
                AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].achfile.featureActionId, CAST('''+ @createActions + ''' as nvarchar(max)))<>0 '+
                @queryTypecondition + ' '+
                @searchQuery + ') AS t1
            LEFT JOIN
            ( 
            SELECT 
               bbrequest.requestId,
               (select count(DISTINCT([${dbxschemaname}].bbactedrequest.createdby)) from [${dbxschemaname}].bbactedrequest where [${dbxschemaname}].bbactedrequest.action = ''Approved'' AND  [${dbxschemaname}].bbactedrequest.requestId = [${dbxschemaname}].bbrequest.requestId) 
                     as receivedApprovals,
                    ([${dbxschemaname}].LEASTINT((select customerCnt from #approvalCount1 where requestid=bbrequest.requestId ),(select totalCnt from #approvalCount2 where requestid=bbrequest.requestId ))) as requiredApprovals
            FROM
             [${dbxschemaname}].bbrequest
            LEFT JOIN [${dbxschemaname}].requestapprovalmatrix ON ([${dbxschemaname}].bbrequest.requestId = [${dbxschemaname}].requestapprovalmatrix.requestId)
            LEFT JOIN [${dbxschemaname}].approvalmatrix ON ([${dbxschemaname}].requestapprovalmatrix.approvalMatrixId = [${dbxschemaname}].approvalmatrix.id)
            LEFT JOIN [${dbxschemaname}].approvalrule ON ([${dbxschemaname}].approvalmatrix.approvalruleId = [${dbxschemaname}].approvalrule.id)
                WHERE [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].bbrequest.requestId AS nvarchar(max)),CAST('''+ @companyRequestIds +''' as nvarchar(max)))<>0 
            GROUP BY [${dbxschemaname}].bbrequest.requestId
            ) AS t2
            ON 
            t1.requestId = t2.requestId
            ORDER BY '+ @_sortByParam + ' '+ @_sortOrder + ' ' +
            @paginationQuery
            EXEC(@select_statement)
	DROP TABLE #approvalCount1;
	DROP TABLE #approvalCount2;
   END
   MAINLABEL$leave:
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[fetch_achtemplaterecords_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[fetch_achtemplaterecords_proc]  
   @_templateId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      /*
      *   SSMA informational messages:
      *   M2SS0052: string literal was converted to NUMERIC literal
      */

      SELECT 
         bbtemplaterecord.templateRecord_id AS templateRecord_id, 
         bbtemplaterecord.record_Name AS record_Name, 
         bbtemplaterecord.toAccountNumber AS toAccountNumber, 
         bbtemplaterecord.abatrcNumber AS abatrcNumber, 
         bbtemplaterecord.detail_id AS detail_id, 
         bbtemplaterecord.amount AS amount, 
         bbtemplaterecord.additionalInfo AS additionalInfo, 
         bbtemplaterecord.ein AS ein, 
         bbtemplaterecord.isZeroTaxDue AS isZeroTaxDue, 
         bbtemplaterecord.template_id AS template_id, 
         bbtemplaterecord.taxType_id AS taxType_id, 
         bbtemplaterecord.templateRequestType_id AS templateRequestType_id, 
         bbtemplaterecord.softDelete AS softDelete, 
         bbtemplaterecord.toAccountType AS toAccountType, 
         bbtaxtype.taxType AS taxType, 
         achaccountstype.accountType AS accountType, 
         bbtemplaterequesttype.templateRequestTypeName AS templateRequestTypeName
      FROM ((([${dbxschemaname}].bbtemplaterecord 
         LEFT JOIN [${dbxschemaname}].bbtaxtype 
         ON ([${dbxschemaname}].bbtemplaterecord.taxType_id = [${dbxschemaname}].bbtaxtype.id)) 
         LEFT JOIN [${dbxschemaname}].achaccountstype 
         ON ([${dbxschemaname}].bbtemplaterecord.toAccountType = [${dbxschemaname}].achaccountstype.id)) 
         LEFT JOIN [${dbxschemaname}].bbtemplaterequesttype 
         ON ([${dbxschemaname}].bbtemplaterecord.templateRequestType_id = [${dbxschemaname}].bbtemplaterequesttype.templateRequestType_id))
      WHERE [${dbxschemaname}].bbtemplaterecord.softDelete = 0 AND [${dbxschemaname}].bbtemplaterecord.template_id = @_templateId

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[fetch_achtemplatesubrecords_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[fetch_achtemplatesubrecords_proc]  
   @_templateRecordId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      /*
      *   SSMA informational messages:
      *   M2SS0052: string literal was converted to NUMERIC literal
      */

      SELECT 
         bbtemplatesubrecord.templateSubRecord_id AS templateSubRecord_id, 
         bbtemplatesubrecord.templateRecord_id AS templateRecord_id, 
         bbtemplatesubrecord.taxSubCategory_id AS taxSubCategory_id, 
         bbtemplatesubrecord.amount AS amount, 
         bbtemplatesubrecord.softDelete AS softDelete, 
         bbtaxsubtype.taxSubType AS taxSubType
      FROM ([${dbxschemaname}].bbtemplatesubrecord 
         LEFT JOIN [${dbxschemaname}].bbtaxsubtype 
         ON ([${dbxschemaname}].bbtemplatesubrecord.taxSubCategory_id = [${dbxschemaname}].bbtaxsubtype.id))
      WHERE [${dbxschemaname}].bbtemplatesubrecord.softDelete = 0 AND [${dbxschemaname}].bbtemplatesubrecord.templateRecord_id = @_templateRecordId

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[fetch_achtransaction_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[fetch_achtransaction_proc]  
   @_customerId nvarchar(50),
   @_transactionId nvarchar(max),
   @_featureactionlist nvarchar(max),
   @_queryType nvarchar(max),
   @_filterByParam nvarchar(max),
   @_filterByValue nvarchar(max),
   @_searchString nvarchar(max),
   @_sortByParam nvarchar(max),
   @_sortOrder nvarchar(max),
   @_pageSize nvarchar(50),
   @_pageOffset nvarchar(50)
AS 
   
   BEGIN
	SET  XACT_ABORT  ON
     SET  NOCOUNT  ON
     DECLARE @group_concat_max_len bigint
     DECLARE @combinedIds nvarchar(max)
     DECLARE @numOfParams int
     DECLARE @searchQuery nvarchar(max)
     DECLARE @idx int
     DECLARE @filterParam nvarchar(max)
     DECLARE @filterValue nvarchar(max)
     DECLARE @companyId nvarchar(max)
     DECLARE @customerMatrixIds nvarchar(max)
     DECLARE @alreadyApprovedIds nvarchar(max)
     DECLARE @approvalRequestIds nvarchar(max)
     DECLARE @queryTypecondition nvarchar(max)
     DECLARE @paginationQuery nvarchar(max)
     DECLARE @features nvarchar(max)
     DECLARE @createActions nvarchar(max)
     DECLARE @companyRequestIds nvarchar(max)
     DECLARE @customerAcounts nvarchar(max)
     DECLARE @select_statement nvarchar(max)
     DECLARE @validAccountsJoin nvarchar(max)
	 

      SET @numOfParams = 0
      SET @searchQuery = ''
      SET @idx = 1
      SET @paginationQuery = 0

      DECLARE
            @db_null_statement int

            DECLARE
               @db_null_statement$2 int

      SET @combinedIds = ( SELECT String_agg(customer.id, ',') FROM [${dbxschemaname}].customer WHERE customer.combinedUserId = @_customerId)
      IF @combinedIds IS NULL
         BEGIN
            SET @combinedIds = @_customerId
         END
      ELSE 
         SET @combinedIds = (@_customerId + ',' + @combinedIds)


		SET @_filterByParam = CASE WHEN (@_filterByParam IS NULL OR @_filterByParam='') THEN '' ELSE @_filterByParam END
        SET @_filterByValue = CASE WHEN (@_filterByValue IS NULL OR @_filterByValue='') THEN '' ELSE @_filterByValue END
        SET @_transactionId = CASE WHEN (@_transactionId = '' OR @_transactionId IS NULL) THEN '%' ELSE @_transactionId END
		SET @numOfParams = 0;

      IF LEN(@_filterByParam) > 0
         BEGIN
            SET @numOfParams = LEN(@_filterByParam) - LEN(replace(@_filterByParam, ',', '')) + 1
         END

	SET @searchQuery = ''

      WHILE (@idx <= @numOfParams)
         BEGIN
            SET @filterParam = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX( @_filterByParam, ',', @idx), ',', -1 );
            SET @filterValue = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX( @_filterByValue, ',', @idx), ',', -1 );
            SET @searchQuery = (@searchQuery + ' AND (' + @filterParam + ' LIKE ''' + @filterValue + ''' )')
            SET @idx = @idx + 1

         END

      SET @companyId = ( SELECT customer.Organization_Id FROM [${dbxschemaname}].customer WHERE customer.id = @_customerId)
      IF @companyId IS NULL
         BEGIN         
            SET @companyId = ''
         END

      IF @_featureactionlist IS NULL
         BEGIN
            GOTO MAINLABEL$leave
         END

		SET @_sortByParam = CASE WHEN (@_sortByParam = '' OR @_sortByParam is NULL) THEN 'createdts' ELSE @_sortByParam END
		SET @_sortOrder = CASE WHEN (@_sortOrder = '' OR @_sortOrder is NULL) THEN 'DESC' ELSE @_sortOrder END



      SET @customerMatrixIds = ( SELECT String_agg(CAST(customerapprovalmatrix.approvalMatrixId as nvarchar(max)),',') FROM [${dbxschemaname}].customerapprovalmatrix WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customerapprovalmatrix.customerId, @combinedIds) <> 0)
      IF @customerMatrixIds IS NULL
         BEGIN
            SET @customerMatrixIds = ''
         END

      SET @alreadyApprovedIds = ( SELECT String_agg(CAST(bbactedrequest.requestId as nvarchar(max)) ,',') FROM [${dbxschemaname}].bbactedrequest WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbactedrequest.createdby, @combinedIds) <> 0 AND [${dbxschemaname}].bbactedrequest.action = 'Approved')

      IF @alreadyApprovedIds IS NULL
         BEGIN
            SET @alreadyApprovedIds = ''
         END
      SET @approvalRequestIds = 
         (
            SELECT String_agg(CAST(requestId as nvarchar(max)) ,',')
            FROM 
               [${dbxschemaname}].requestapprovalmatrix 
                  INNER JOIN [${dbxschemaname}].approvalmatrix 
                  ON [${dbxschemaname}].requestapprovalmatrix.approvalMatrixId = [${dbxschemaname}].approvalmatrix.id 
                  INNER JOIN [${dbxschemaname}].approvalrule 
                  ON [${dbxschemaname}].approvalmatrix.approvalruleId = [${dbxschemaname}].approvalrule.id
            WHERE 
               [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].requestapprovalmatrix.approvalMatrixId AS nvarchar(max)), @customerMatrixIds) <> 0 AND 
               NOT [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].requestapprovalmatrix.requestId AS nvarchar(max)), @alreadyApprovedIds) <> 0 AND 
               (([${dbxschemaname}].approvalrule.numberOfApprovals = -1 AND [${dbxschemaname}].requestapprovalmatrix.receivedApprovals < 
               (
                  SELECT count_big(DISTINCT ([${dbxschemaname}].customerapprovalmatrix.customerId))
                  FROM [${dbxschemaname}].customerapprovalmatrix
                  WHERE [${dbxschemaname}].customerapprovalmatrix.approvalMatrixId = [${dbxschemaname}].requestapprovalmatrix.approvalMatrixId
               )) OR ([${dbxschemaname}].approvalrule.numberOfApprovals <> -1 AND [${dbxschemaname}].requestapprovalmatrix.receivedApprovals < [${dbxschemaname}].approvalrule.numberOfApprovals))            
         )
      IF @approvalRequestIds IS NULL
         BEGIN
            SET @approvalRequestIds = ''
         END

      SET @queryTypecondition = 
         CASE 
            WHEN (@_queryType = 'myRequests') THEN (' AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].achtransaction.createdby, ''') + (@combinedIds) + (''')<>0 AND ([${dbxschemaname}].bbrequest.status = ''Pending'' OR [${dbxschemaname}].bbrequest.status = ''Approved'' OR [${dbxschemaname}].bbrequest.status = ''Rejected'' ) ')
            ELSE 
               CASE 
                  WHEN (@_queryType = 'pendingForMyApprovals') THEN (' AND [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].achtransaction.requestId AS nvarchar(max)),  ''') + (@approvalRequestIds) + (''')<>0  AND [${dbxschemaname}].achtransaction.status = ''Pending'' ')
                  ELSE 
                     CASE 
                        WHEN (@_queryType = 'rejected') THEN (' AND [${dbxschemaname}].achtransaction.status = ''Rejected'' ')
                        ELSE 
                           CASE 
                              WHEN (@_transactionId = '%') THEN (' AND NOT [${dbxschemaname}].achtransaction.status = ''Withdrawn''')
                              ELSE ''
                           END
                     END
               END
         END
      SET @validAccountsJoin = 
         CASE 
            WHEN (@_queryType = '') THEN 
            (
               ' INNER JOIN (SELECT DISTINCT Account_id, REPLACE(Action_id, ''_VIEW'', ''_CREATE'') as Action_id
                 FROM [${dbxschemaname}].customeraction WHERE [${dbxschemaname}].customeraction.Customer_id = '''+@_customerId+''' AND [${dbxschemaname}].customeraction.isAllowed = ''1''
                 AND [${dbxschemaname}].customeraction.Account_id is NOT null AND [${dbxschemaname}].customeraction.Action_id like ''%_VIEW'') as can ON ([${dbxschemaname}].achtransaction.featureActionId = can.Action_id
                 AND [${dbxschemaname}].achtransaction.fromAccount = can.Account_id) '
            )
            ELSE ''
         END

		 SET @searchQuery = CASE WHEN (@_searchString is NULL OR @_searchString = '') THEN @searchQuery ELSE 
							(@searchQuery+ ' AND (achtransaction.templateName LIKE ''%'+@_searchString+'%'' OR customeraccounts.AccountName LIKE ''%'+
							@_searchString+'%'' OR bbtemplaterequesttype.templateRequestTypeName LIKE ''%'+@_searchString+'%'' OR 
							achtransaction.transaction_id LIKE ''%'+@_searchString+'%'' OR achtransaction.fromAccount LIKE ''%'+@_searchString+'%'' )') END
        
        SET @paginationQuery = CASE WHEN (@_pageOffset is NULL OR @_pageOffset = '' OR @_pageSize is NULL OR @_pageSize = '') THEN '' ELSE
								(' OFFSET '+@_pageOffset+ '   ROWS FETCH NEXT ' +@_pageSize+' ROWS ONLY ') END 
      SET @features = 
         (
            SELECT String_agg(CAST(featureaction.Feature_id as nvarchar(max)), ',')
            FROM [${dbxschemaname}].featureaction
            WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id, @_featureactionlist) <> 0
         )
      IF @features IS NULL
         BEGIN
            SET @features = ''
         END
      SET @createActions = 
         (
            SELECT String_agg(CAST(featureaction.id as nvarchar(max)), ',')
            FROM [${dbxschemaname}].featureaction
            WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.Feature_id, @features) <> 0 AND [${dbxschemaname}].featureaction.id LIKE '%_CREATE'
         )
      IF @createActions IS NULL
         BEGIN
            SET @createActions = ''
         END
      SET @companyRequestIds = 
         (
            SELECT String_agg(CAST(bbrequest.requestId as nvarchar(max)), ',')
            FROM [${dbxschemaname}].bbrequest
            WHERE CAST([${dbxschemaname}].bbrequest.companyId AS float(53)) = @companyId AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbrequest.featureActionId, @createActions) <> 0
         )
      IF @companyRequestIds IS NULL
         BEGIN
            SET @companyRequestIds = ''
         END         

      SET @customerAcounts = 
         (
            SELECT String_agg(CAST(customeraccounts.Account_id as nvarchar(max)), ',')
            FROM [${dbxschemaname}].customeraccounts
            WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraccounts.Customer_id, @combinedIds) <> 0
         )
      IF @customerAcounts IS NULL
         BEGIN
            SET @customerAcounts = ''
         END

	
	SELECT  customerCnt, requestId into #approvalCount1 from
	 (SELECT COUNT(DISTINCT(customerId)) as customerCnt ,requestapprovalmatrix.requestId as requestId FROM [${dbxschemaname}].customerapprovalmatrix INNER JOIN [${dbxschemaname}].requestapprovalmatrix ON (customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId ) WHERE [${dbxschemaname}].FIND_IN_SET(cast(requestapprovalmatrix.requestId as nvarchar(max)), @companyRequestIds) <> 0 group by requestapprovalmatrix.requestId) as approvalCount1

	 SELECT  requestId, totalCnt into #approvalCount2 
	from (select requestId, sum(t) as totalCnt from (
	select bbrequest.requestId as requestId,
         CASE WHEN
			[${dbxschemaname}].approvalrule.numberOfApprovals = -1
			THEN (SELECT count(*)  FROM [${dbxschemaname}].customerapprovalmatrix where customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)
          WHEN [${dbxschemaname}].approvalrule.numberOfApprovals IS NULL OR [${dbxschemaname}].approvalrule.numberOfApprovals='' THEN 0
              ELSE [${dbxschemaname}].approvalrule.numberOfApprovals
          END as t
     from [${dbxschemaname}].customerapprovalmatrix
	 LEFT JOIN [${dbxschemaname}].requestapprovalmatrix ON [${dbxschemaname}].customerapprovalmatrix.approvalMatrixId = [${dbxschemaname}].requestapprovalmatrix.approvalMatrixId
	 LEFT JOIN [${dbxschemaname}].bbrequest ON ([${dbxschemaname}].bbrequest.requestId = [${dbxschemaname}].requestapprovalmatrix.requestId)
	 LEFT JOIN [${dbxschemaname}].approvalmatrix ON ([${dbxschemaname}].requestapprovalmatrix.approvalMatrixId = [${dbxschemaname}].approvalmatrix.id)
	 LEFT JOIN [${dbxschemaname}].approvalrule ON ([${dbxschemaname}].approvalmatrix.approvalruleId = [${dbxschemaname}].approvalrule.id)
     WHERE [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].bbrequest.requestId AS nvarchar(max)),@companyRequestIds)<>0 ) approvals group by requestId) as approvalCount2

      SET @select_statement = '

         SELECT * FROM
            (SELECT 
         [${dbxschemaname}].achtransaction.transaction_id AS transaction_id,
         [${dbxschemaname}].achtransaction.fromAccount AS fromAccount,
         [${dbxschemaname}].achtransaction.effectiveDate AS effectiveDate,
         [${dbxschemaname}].achtransaction.requestId AS requestId,
         [${dbxschemaname}].achtransaction.createdby AS createdby,
         [${dbxschemaname}].achtransaction.createdts AS createdts,
         [${dbxschemaname}].achtransaction.maxAmount AS maxAmount,
         [${dbxschemaname}].achtransaction.status AS status,
         [${dbxschemaname}].achtransaction.transactionType_id AS transactionType_id,
         [${dbxschemaname}].achtransaction.templateType_id AS templateType_id,
         [${dbxschemaname}].achtransaction.companyId AS companyId,
         [${dbxschemaname}].achtransaction.templateRequestType_id AS templateRequestType_id,
         [${dbxschemaname}].achtransaction.softDelete AS softDelete,
         [${dbxschemaname}].achtransaction.templateName AS templateName,
         [${dbxschemaname}].achtransaction.template_id AS template_id,
         [${dbxschemaname}].achtransaction.totalAmount AS totalAmount,
         [${dbxschemaname}].achtransaction.featureActionId AS featureActionId,
         [${dbxschemaname}].achtransaction.confirmationNumber AS confirmationNumber,
         ( CASE 
            WHEN [${dbxschemaname}].customeraccounts.AccountName is NULL THEN ''AccountName'' 
                ELSE [${dbxschemaname}].customeraccounts.AccountName 
            END ) AS accountName,
         [${dbxschemaname}].customer.UserName AS userName,
         [${dbxschemaname}].bbtransactiontype.transactionTypeName AS transactionTypeName,
         [${dbxschemaname}].bbtemplatetype.templateTypeName AS templateTypeName,
         [${dbxschemaname}].bbtemplaterequesttype.templateRequestTypeName AS templateRequestTypeName,
         [${dbxschemaname}].organisation.Name AS companyName,
         [${dbxschemaname}].bbrequest.createdby AS requestCreatedby,
         (CASE
            WHEN [${dbxschemaname}].FIND_IN_SET(CAST(achtransaction.createdby AS nvarchar(max)),CAST(''' + @combinedIds +''' as nvarchar(max))) > 0 THEN ''true''
            ELSE ''false''
          END) as amICreator,
         (CASE 
            WHEN [${dbxschemaname}].FIND_IN_SET(CAST(bbrequest.requestId AS nvarchar(max)), CAST('''+ @approvalRequestIds +''' as nvarchar(max))) >0 THEN ''true''
            ELSE ''false''
         END)
          as amIApprover
      FROM
         ((((((([${dbxschemaname}].achtransaction
         LEFT JOIN [${dbxschemaname}].customer ON ([${dbxschemaname}].achtransaction.createdby = [${dbxschemaname}].customer.id))
         LEFT JOIN [${dbxschemaname}].customeraccounts ON (([${dbxschemaname}].achtransaction.createdby = [${dbxschemaname}].customeraccounts.Customer_id)
            AND ([${dbxschemaname}].achtransaction.fromAccount = [${dbxschemaname}].customeraccounts.Account_id)))
         LEFT JOIN [${dbxschemaname}].bbtransactiontype ON ([${dbxschemaname}].achtransaction.transactionType_id = [${dbxschemaname}].bbtransactiontype.transactionType_id))
         LEFT JOIN [${dbxschemaname}].bbtemplatetype ON ([${dbxschemaname}].achtransaction.templateType_id = [${dbxschemaname}].bbtemplatetype.templateType_id))
         LEFT JOIN [${dbxschemaname}].bbtemplaterequesttype ON ([${dbxschemaname}].achtransaction.templateRequestType_id = [${dbxschemaname}].bbtemplaterequesttype.templateRequestType_id))
         LEFT JOIN [${dbxschemaname}].organisation ON ([${dbxschemaname}].achtransaction.companyId = [${dbxschemaname}].organisation.id)) ' + @validAccountsJoin +
         ' LEFT JOIN [${dbxschemaname}].bbrequest ON ([${dbxschemaname}].achtransaction.requestId = [${dbxschemaname}].bbrequest.requestId))
         WHERE [${dbxschemaname}].achtransaction.softDelete = ''0''
            AND ( [${dbxschemaname}].achtransaction.companyId = '''+ @companyId + ''' OR [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].achtransaction.createdby, CAST('''+ @combinedIds + ''' as nvarchar(max)))<>0)
            AND [${dbxschemaname}].achtransaction.transaction_id LIKE '''+ @_transactionId + '''
            AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].achtransaction.fromAccount, CAST('''+ @customerAcounts + ''' as nvarchar(max)))<>0 
                AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].achtransaction.featureActionId, CAST(''' + @createActions + ''' as nvarchar(max)))<>0 ' + 
                @queryTypecondition + ' ' +
                @searchQuery + ' ) AS t1
            LEFT JOIN
            ( 
            SELECT 
               bbrequest.requestId,
               (select count(DISTINCT([${dbxschemaname}].bbactedrequest.createdby)) from [${dbxschemaname}].bbactedrequest where [${dbxschemaname}].bbactedrequest.action = ''Approved'' AND  [${dbxschemaname}].bbactedrequest.requestId = [${dbxschemaname}].bbrequest.requestId) 
                     as receivedApprovals,
                    ([${dbxschemaname}].LEASTINT((select customerCnt from #approvalCount1 where requestid=bbrequest.requestId ),(select totalCnt from #approvalCount2 where requestid=bbrequest.requestId ))) as requiredApprovals
            FROM
             [${dbxschemaname}].bbrequest
            LEFT JOIN [${dbxschemaname}].requestapprovalmatrix ON ([${dbxschemaname}].bbrequest.requestId = [${dbxschemaname}].requestapprovalmatrix.requestId)
            LEFT JOIN [${dbxschemaname}].approvalmatrix ON ([${dbxschemaname}].requestapprovalmatrix.approvalMatrixId = [${dbxschemaname}].approvalmatrix.id)
            LEFT JOIN [${dbxschemaname}].approvalrule ON ([${dbxschemaname}].approvalmatrix.approvalruleId = [${dbxschemaname}].approvalrule.id)
                WHERE [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].bbrequest.requestId AS nvarchar(max)), CAST(''' + @companyRequestIds + ''' as nvarchar(max)))<>0
            GROUP BY [${dbxschemaname}].bbrequest.requestId
            ) AS t2
            ON 
            t1.requestId = t2.requestId
            ORDER BY '+ @_sortByParam + ' ' + @_sortOrder + ' ' +
            @paginationQuery
      EXEC(@select_statement)
	  DROP TABLE #approvalCount1;
	 DROP TABLE #approvalCount2;
   END
   MAINLABEL$leave:



GO

/****** Object:  StoredProcedure [${dbxschemaname}].[fetch_achtransactionrecords_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[fetch_achtransactionrecords_proc]  
   @_transactionId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      /*
      *   SSMA informational messages:
      *   M2SS0052: string literal was converted to NUMERIC literal
      */

      SELECT 
         [${dbxschemaname}].achtransactionrecord.transactionRecord_id AS transactionRecord_id, 
         [${dbxschemaname}].achtransactionrecord.toAccountNumber AS toAccountNumber, 
         [${dbxschemaname}].achtransactionrecord.toAccountType AS toAccountType, 
         [${dbxschemaname}].achtransactionrecord.abatrcNumber AS abatrcNumber, 
         [${dbxschemaname}].achtransactionrecord.detail_id AS detail_id, 
         [${dbxschemaname}].achtransactionrecord.amount AS amount, 
         [${dbxschemaname}].achtransactionrecord.additionalInfo AS additionalInfo, 
         [${dbxschemaname}].achtransactionrecord.eIN AS eIN, 
         [${dbxschemaname}].achtransactionrecord.isZeroTaxDue AS isZeroTaxDue, 
         [${dbxschemaname}].achtransactionrecord.taxType_id AS taxType_id, 
         [${dbxschemaname}].achtransactionrecord.transaction_id AS transaction_id, 
         [${dbxschemaname}].achtransactionrecord.softDelete AS softDelete, 
         [${dbxschemaname}].achtransactionrecord.templateRequestType_id AS templateRequestType_id, 
         [${dbxschemaname}].achtransactionrecord.record_Name AS record_Name, 
         [${dbxschemaname}].bbtaxtype.taxType AS taxType, 
         [${dbxschemaname}].achaccountstype.accountType AS accountType, 
         [${dbxschemaname}].bbtemplaterequesttype.templateRequestTypeName AS templateRequestTypeName
      FROM ((([${dbxschemaname}].achtransactionrecord 
         LEFT JOIN [${dbxschemaname}].bbtaxtype 
         ON ([${dbxschemaname}].achtransactionrecord.taxType_id = [${dbxschemaname}].bbtaxtype.id)) 
         LEFT JOIN [${dbxschemaname}].achaccountstype 
         ON ([${dbxschemaname}].achtransactionrecord.toAccountType = [${dbxschemaname}].achaccountstype.id)) 
         LEFT JOIN [${dbxschemaname}].bbtemplaterequesttype 
         ON ([${dbxschemaname}].achtransactionrecord.templateRequestType_id = [${dbxschemaname}].bbtemplaterequesttype.templateRequestType_id))
      WHERE [${dbxschemaname}].achtransactionrecord.softDelete = 0 AND [${dbxschemaname}].achtransactionrecord.transaction_id = @_transactionId

   END
GO


/****** Object:  StoredProcedure [${dbxschemaname}].[customer_request_archive_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [${dbxschemaname}].[customer_request_archive_proc]
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DECLARE
         @customer_request_cursor_done bit = 0x0

      DECLARE
         @customer_requestmessage_cursor_done bit = 0x0

      DECLARE
         @customer_requestmessageattachment_cursor_done bit = 0x0

      DECLARE
         @curr_request_id nvarchar(50) = N''

      DECLARE
         @curr_requestmessage_id nvarchar(50) = N''

      DECLARE
         @curr_media_id nvarchar(50) = N''

      DECLARE
         @curr_messageattachment_id nvarchar(50) = N'0'

      DECLARE
         @media_count int = 0

      DECLARE
          customer_request_cursor CURSOR LOCAL FORWARD_ONLY FOR 


            SELECT id
            FROM [${dbxschemaname}].customerrequest
            WHERE Status_id = 0 AND datediff(second, lastmodifiedts, getdate()) >= 15552000

      OPEN customer_request_cursor


      WHILE (1 = 1)
      
         BEGIN


            FETCH customer_request_cursor
                INTO @curr_request_id


            IF @@FETCH_STATUS <> 0

               SET @customer_request_cursor_done = 0x1


               SET @customer_requestmessage_cursor_done = 0x1

               SET @customer_requestmessageattachment_cursor_done = 0x1

            IF NOT @customer_request_cursor_done <> 0
               BEGIN

                     INSERT  INTO [${dbxschemaname}].archivedcustomerrequest
                        SELECT 
                           *
                        FROM [${dbxschemaname}].customerrequest
                        WHERE id = @curr_request_id

                  UPDATE [${dbxschemaname}].archivedcustomerrequest
                     SET 
                        Status_id = 'SID_ARCHIVED'
                  WHERE id = @curr_request_id
                  



                  SELECT @curr_request_id


REQUESTMESSAGEBLOCK:

                  BEGIN

                     DECLARE
                         customer_requestmessage_cursor CURSOR LOCAL FORWARD_ONLY FOR 
                           SELECT id
                           FROM [${dbxschemaname}].requestmessage
                           WHERE CustomerRequest_id = @curr_request_id

                     OPEN customer_requestmessage_cursor

                     WHILE (1 = 1)
                     
                        BEGIN

                           FETCH customer_requestmessage_cursor
                               INTO @curr_requestmessage_id

                           IF @@FETCH_STATUS <> 0
 
                              SET @customer_request_cursor_done = 0x1


                              SET @customer_requestmessage_cursor_done = 0x1

                              SET @customer_requestmessageattachment_cursor_done = 0x1

                           IF NOT @customer_requestmessage_cursor_done <> 0
                              BEGIN
								    INSERT  INTO [${dbxschemaname}].archivedrequestmessage
                                       SELECT 
                                          *
                                       FROM [${dbxschemaname}].requestmessage
                                       WHERE id = @curr_requestmessage_id
                                 

MESSAGEATTACHMENTBLOCK:
                                 

                                 BEGIN

                                    DECLARE
                                        customer_messageattachment_cursor CURSOR LOCAL FORWARD_ONLY FOR 
                                          SELECT id
                                          FROM [${dbxschemaname}].messageattachment
                                          WHERE RequestMessage_id = @curr_requestmessage_id

                                    OPEN customer_messageattachment_cursor

customer_messageattachment_loop:
                                    

                                    WHILE (1 = 1)
                                    
                                       BEGIN

                                          FETCH customer_messageattachment_cursor
                                              INTO @curr_messageattachment_id


                                          IF @@FETCH_STATUS <> 0

                                             SET @customer_request_cursor_done = 0x1
                                   

                                             SET @customer_requestmessage_cursor_done = 0x1
                                           
                                             SET @customer_requestmessageattachment_cursor_done = 0x1

                                          IF NOT @customer_requestmessageattachment_cursor_done <> 0
                                             BEGIN

                                                SELECT @curr_media_id = Media_id
                                                FROM [${dbxschemaname}].messageattachment
                                                WHERE id = @curr_messageattachment_id

                                                SELECT @media_count = count_big(id)
                                                FROM [${dbxschemaname}].archivedmedia
                                                WHERE id = @curr_media_id
       

                                                IF @media_count = 0
 
                                                      INSERT  INTO archivedmedia
                                                         SELECT 
                                                            *
                                                         FROM [${dbxschemaname}].media
                                                         WHERE id = @curr_media_id
														 
													   INSERT  INTO archivedmessageattachment
                                                      SELECT 
                                                         *
                                                      FROM [${dbxschemaname}].messageattachment
                                                      WHERE id = @curr_messageattachment_id
                                                



     
                                                DELETE 
                                                FROM [${dbxschemaname}].messageattachment
                                                WHERE id = @curr_messageattachment_id
                                          
                                                SELECT @media_count = count_big(id)
                                                FROM [${dbxschemaname}].messageattachment
                                                WHERE Media_id = @curr_media_id
                                               

                                                IF @media_count = 0
                                        
                                                   DELETE 
                                                   FROM [${dbxschemaname}].media
                                                   WHERE id = @curr_media_id

                                             END

                                          IF @customer_requestmessageattachment_cursor_done <> 0
                                             BREAK

                                       END

                                    SET @customer_request_cursor_done = 0x0

                                    SET @customer_requestmessage_cursor_done = 0x0

                                    SET @customer_requestmessageattachment_cursor_done = 0x0

                                    CLOSE customer_messageattachment_cursor
                        
                                    DEALLOCATE customer_messageattachment_cursor
                                
                                 END

                                 DELETE 
                                 FROM [${dbxschemaname}].requestmessage
                                 WHERE id = @curr_requestmessage_id
                      

                              END

                           IF @customer_requestmessage_cursor_done <> 0
                              BREAK

                        END

                     SET @customer_request_cursor_done = 0x0


                     SET @customer_requestmessage_cursor_done = 0x0


                     SET @customer_requestmessageattachment_cursor_done = 0x0

                     CLOSE customer_requestmessage_cursor

                     DEALLOCATE customer_requestmessage_cursor
       
                  END

                  DELETE 
                  FROM [${dbxschemaname}].customerrequest
                  WHERE id = @curr_request_id
       
               END

            IF @customer_request_cursor_done <> 0
               BREAK

         END

      SET @customer_request_cursor_done = 0x0


      SET @customer_requestmessage_cursor_done = 0x0

      SET @customer_requestmessageattachment_cursor_done = 0x0

      CLOSE customer_request_cursor

      DEALLOCATE customer_request_cursor
 
   END
GO


/****** Object:  StoredProcedure [${dbxschemaname}].[fetch_achtransactionsubrecords_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[fetch_achtransactionsubrecords_proc]  
   @_transactionRecordId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      /*
      *   SSMA informational messages:
      *   M2SS0052: string literal was converted to NUMERIC literal
      */

      SELECT 
         [${dbxschemaname}].achtransactionsubrecord.transcationSubRecord_id AS transcationSubRecord_id, 
         [${dbxschemaname}].achtransactionsubrecord.amount AS amount, 
         [${dbxschemaname}].achtransactionsubrecord.transactionRecord_id AS transactionRecord_id, 
         [${dbxschemaname}].achtransactionsubrecord.taxSubCategory_id AS taxSubCategory_id, 
         [${dbxschemaname}].achtransactionsubrecord.softDelete AS softDelete, 
         [${dbxschemaname}].bbtaxsubtype.taxSubType AS taxSubType
      FROM ([${dbxschemaname}].achtransactionsubrecord 
         LEFT JOIN [${dbxschemaname}].bbtaxsubtype 
         ON ([${dbxschemaname}].achtransactionsubrecord.taxSubCategory_id = [${dbxschemaname}].bbtaxsubtype.id))
      WHERE [${dbxschemaname}].achtransactionsubrecord.softDelete = 0 AND [${dbxschemaname}].achtransactionsubrecord.transactionRecord_id = @_transactionRecordId

   END
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[fetch_bulkwire_filelineitems_proc]    Script Date: 6/9/2020 2:00:56 PM ******/

CREATE PROCEDURE [${dbxschemaname}].[fetch_bulkwire_filelineitems_proc]  
   @bulkWireFileID nvarchar(50),
   @searchString nvarchar(50),
   @sortByParam nvarchar(50),
   @sortOrder nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
	  declare @queryFilter nvarchar(max)

      SET @queryFilter = (N'bulkwirefilelineitems.bulkWireFileID = ''') + (@bulkWireFileID) + (N''' AND bulkwirefilelineitems.softdeleteflag = 0 ')
  
      SET @sortByParam=Case when @sortByParam = '' or  @sortByParam is null then 'recipientName' else @sortByParam end

      SET @sortOrder = case when @sortOrder = '' or @sortOrder is null then 'ASC' else @sortOrder end

      SET @searchString = case when @searchString = '' or @searchString is null then '' else '''%'+@searchString+'%''' end

	  declare @orderBy nvarchar(max)
	  declare @searchQuery nvarchar(max)

      SET @orderBy = (N' ORDER BY bulkWireTransferType,') + (@sortByParam) + (N' ') + (@sortOrder)
  
      SET @searchQuery = 
         (N'(bulkwirefilelineitems.recipientName LIKE ')
          + 
         (@searchString)
          + 
         (N' OR  bulkwirefilelineitems.fromAccountNumber LIKE ')
          + 
         (@searchString)
          + 
         (N' OR bulkwirefilelineitems.swiftCode LIKE ')
          + 
         (@searchString)
          + 
         (N' OR bulkwirefilelineitems.recipientAccountNumber LIKE ')
          + 
         (@searchString)
          + 
         (N' OR bulkwirefilelineitems.routingNumber LIKE ')
          + 
         (@searchString)
          + 
         (N' OR bulkwirefilelineitems.internationalRoutingNumber LIKE ')
          + 
         (@searchString)
          + 
         (N' OR bulkwirefilelineitems.note LIKE ')
          + 
         (@searchString)
          + 
         (N'  OR '+N'bulkwirefilelineitems.transactionType LIKE ')
          + 
         (@searchString)
          + 
         (N'  OR bulkwirefilelineitems.recipientAddressLine1 LIKE ')
          + 
         (@searchString)
          + 
         (N'  OR bulkwirefilelineitems.recipientAddressLine2 LIKE ')
          + 
         (@searchString)
          + 
         (N'  OR bulkwirefilelineitems.recipientCity LIKE ')
          + 
         (@searchString)
          + 
         (N'  OR bulkwirefilelineitems.recipientState LIKE ')
          + 
         (@searchString)
          + 
         (N'  OR bulkwirefilelineitems.recipientZipCode LIKE ')
          + 
         (@searchString)
          + 
         (N'  OR bulkwirefilelineitems.recipientBankName LIKE ')
          + 
         (@searchString)
          + 
         (N'  OR bulkwirefilelineitems.recipientBankAddress1 LIKE ')
          + 
         (@searchString)
          + 
         (N'  OR bulkwirefilelineitems.recipientBankAddress2 LIKE ')
          + 
         (@searchString)
          + 
         (N'  OR bulkwirefilelineitems.recipientBankcity LIKE ')
          + 
         (@searchString)
          + 
         (N'  OR bulkwirefilelineitems.recipientBankZipCode LIKE ')
          + 
         (@searchString)
          + 
         (N'  OR bulkwirefilelineitems.recipientBankstate LIKE ')
          + 
         (@searchString)
          + 
         (N')')
		 declare @defaultFilter nvarchar(max)
		 declare @searchFilter nvarchar(max)
		 declare @filter nvarchar(max)
		 declare @select_statement nvarchar(max)
      SET @defaultFilter = (@queryFilter) + (@orderBy)
 
      SET @searchFilter = (@queryFilter) + (N' AND ') + (@searchQuery) + (@orderBy)
 
      SET @filter = 
         CASE 
            WHEN (@searchString = '') THEN @defaultFilter
            ELSE @searchFilter
         END
   
      SET @select_statement = (N'SELECT * FROM [${dbxschemaname}].bulkwirefilelineitems WHERE ') + (@filter)

 
       EXEC(@select_statement)
   END
GO



/****** Object:  StoredProcedure [${dbxschemaname}].[fetch_bbtemplate_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/
CREATE PROCEDURE [${dbxschemaname}].[fetch_bbtemplate_proc]  
   @_customerId nvarchar(50),
   @_templateId nvarchar(50),
   @_featureactionlist nvarchar(max),
   @_queryType nvarchar(50),
   @_filterByParam nvarchar(max),
   @_filterByValue nvarchar(max),
   @_searchString nvarchar(50),
   @_sortByParam nvarchar(50),
   @_sortOrder nvarchar(50),
   @_pageSize nvarchar(50),
   @_pageOffset nvarchar(50)
AS 
   BEGIN

     MAINLABEL: 
      
      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

     DECLARE @group_concat_max_len bigint
     DECLARE @combinedIds nvarchar(max)
     DECLARE @numOfParams int
     DECLARE @searchQuery nvarchar(max)
     DECLARE @idx int
     DECLARE @filterParam nvarchar(max)
     DECLARE @filterValue nvarchar(max)
     DECLARE @companyId nvarchar(max)
     DECLARE @customerMatrixIds nvarchar(max)
     DECLARE @alreadyApprovedIds nvarchar(max)
     DECLARE @approvalRequestIds nvarchar(max)
     DECLARE @queryTypecondition nvarchar(max)
     DECLARE @paginationQuery nvarchar(max)
     DECLARE @features nvarchar(max)
     DECLARE @createActions nvarchar(max)
     DECLARE @companyRequestIds nvarchar(max)
     DECLARE @customerAcounts nvarchar(max)
     DECLARE @select_statement nvarchar(max)
     

     SET @group_concat_max_len = 100000000
     SET @combinedIds = (select String_agg(id, ',') from [${dbxschemaname}].customer where combinedUserId = @_customerId)

     IF @combinedIds is NULL      
        SET @combinedIds = @_customerId;
     ELSE 
        SET @combinedIds =(@_customerId + ',' +@combinedIds)
  
    SET @_filterByParam = CASE WHEN @_filterByParam IS NULL OR @_filterByParam='' THEN  '' ELSE @_filterByParam END
    SET @_filterByValue = CASE WHEN @_filterByValue IS NULL OR @_filterByValue='' THEN '' ELSE @_filterByValue END
    SET @_templateId = CASE WHEN @_templateId = '' OR @_templateId IS NULL THEN '%' ELSE @_templateId END
    SET @numOfParams = 0

    IF datalength(@_filterByParam) > 0
      SET @numOfParams = datalength(@_filterByParam) - datalength(REPLACE(@_filterByParam, ',', '')) + 1

    SET @searchQuery = ''
    SET @idx = 1
    WHILE(@idx <= @numOfParams) 
    BEGIN       
    SET @filterParam = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX( @_filterByParam, ',', @idx),',', -1 );
      SET @filterValue = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX( @_filterByValue, ',',   @idx),',', -1 );
      SET @searchQuery = @searchQuery+ ' AND ('+@filterParam+' LIKE '''+@filterValue+''' )'
      SET @idx = @idx + 1   
    END

    SET @companyId = ( SELECT Organization_Id FROM [${dbxschemaname}].customer WHERE [${dbxschemaname}].customer.id = @_customerId)

    IF @companyId IS NULL
      BEGIN
        SET @companyId = ''
      END

    IF @_featureactionlist IS NULL
      BEGIN
         GOTO MAINLABEL$leave
      END

    SET @_sortByParam = CASE WHEN @_sortByParam = '' OR @_sortByParam is NULL THEN 'createdts' ELSE @_sortByParam END
    SET @_sortOrder = CASE WHEN @_sortOrder = '' OR @_sortOrder is NULL THEN 'DESC' ELSE @_sortOrder END  
    SET @customerMatrixIds = ( SELECT String_agg(CAST(approvalMatrixId  as nvarchar(max)),',') FROM [${dbxschemaname}].customerapprovalmatrix WHERE [${dbxschemaname}].FIND_IN_SET(customerId, @combinedIds) <> 0)

    IF @customerMatrixIds IS NULL
      BEGIN
        SET @customerMatrixIds = ''
      END
  
    SET @alreadyApprovedIds = (SELECT String_agg(CAST(requestId as nvarchar(max)) ,',') FROM [${dbxschemaname}].bbactedrequest WHERE [${dbxschemaname}].FIND_IN_SET(createdby, @combinedIds) <> 0 AND bbactedrequest.action = 'Approved')

    IF @alreadyApprovedIds IS NULL
      BEGIN
         SET @alreadyApprovedIds = ''
      END

    SET @approvalRequestIds = (  
         SELECT String_agg(CAST(requestId as nvarchar(max)) ,',') FROM [${dbxschemaname}].requestapprovalmatrix
         INNER JOIN [${dbxschemaname}].approvalmatrix ON [${dbxschemaname}].requestapprovalmatrix.approvalMatrixId = approvalmatrix.id
         INNER JOIN [${dbxschemaname}].approvalrule ON [${dbxschemaname}].approvalmatrix.approvalruleId = [${dbxschemaname}].approvalrule.id
         WHERE [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].requestapprovalmatrix.approvalMatrixId AS nvarchar(max)),  @customerMatrixIds) <>0
         AND [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].requestapprovalmatrix.requestId AS nvarchar(max)), @alreadyApprovedIds) = 0
         AND (([${dbxschemaname}].approvalrule.numberOfApprovals = -1 AND [${dbxschemaname}].requestapprovalmatrix.receivedApprovals < (SELECT COUNT(DISTINCT(customerId)) FROM [${dbxschemaname}].customerapprovalmatrix 
         WHERE [${dbxschemaname}].customerapprovalmatrix.approvalMatrixId = [${dbxschemaname}].requestapprovalmatrix.approvalMatrixId)))
         )

    IF @approvalRequestIds IS NULL
      BEGIN
        SET @approvalRequestIds = ''
      END

    SET @queryTypecondition = 
      CASE WHEN (@_queryType = 'myRequests') THEN 
            (' AND  [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbtemplate.createdby,') + (@combinedIds) + (') <> 0 AND ([${dbxschemaname}].bbrequest.status = ''Pending'' OR [${dbxschemaname}].bbrequest.status = ''Approved'' OR [${dbxschemaname}].bbrequest.status = ''Rejected'') ')
        ELSE 
            CASE WHEN (@_queryType = 'pendingForMyApprovals') THEN (' AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbtemplate.requestId,  ''') + (@approvalRequestIds) + (''')<>0  AND [${dbxschemaname}].bbtemplate.status = ''Pending'' ')
              ELSE 
               CASE WHEN @_templateId = '%' THEN ' AND NOT [${dbxschemaname}].bbtemplate.status = ''Withdrawn''' 
                ELSE '' 
               END
            END
        END

      SET @searchQuery = CASE WHEN (@_searchString is NULL OR @_searchString = '') THEN @searchQuery 
                  ELSE 
                   @searchQuery+ ' AND ([${dbxschemaname}].bbtemplate.templateName LIKE ''%'+@_searchString+'%'' OR [${dbxschemaname}].customeraccounts.AccountName LIKE ''%'+@_searchString+'%'' OR [${dbxschemaname}].bbtemplaterequesttype.templateRequestTypeName LIKE ''%'+@_searchString+'%'' OR [${dbxschemaname}].bbtemplate.fromAccount LIKE ''%'+@_searchString+'%'')' 
                  END

      SET @paginationQuery = CASE WHEN (@_pageOffset is NULL OR @_pageOffset = '' OR @_pageSize is NULL OR @_pageSize = '') THEN '' 
                  ELSE
                    ' OFFSET '+ @_pageOffset +'  ROWS FETCH NEXT ' +@_pageSize+' ROWS ONLY' 
                  END

      SET @features = ( SELECT String_agg(CAST(Feature_id as nvarchar(max)) ,',') FROM [${dbxschemaname}].featureaction WHERE [${dbxschemaname}].FIND_IN_SET(featureaction.id, @_featureactionlist) <> 0 )
      
      IF @features IS NULL
        BEGIN
          SET @features = ''
        END
 
      SET @createActions = ( SELECT String_agg(CAST(featureaction.id as nvarchar(max)),',') FROM [${dbxschemaname}].featureaction WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.Feature_id, @features) <> 0 AND [${dbxschemaname}].featureaction.id LIKE '%_CREATE_TEMPLATE')

      IF @createActions IS NULL
        BEGIN
          SET @createActions = ''
        END

      SET @companyRequestIds = ( SELECT String_agg(CAST(bbrequest.requestId as nvarchar(max)) ,',') FROM [${dbxschemaname}].bbrequest WHERE [${dbxschemaname}].bbrequest.companyId = @companyId AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbrequest.featureActionId, @createActions) <> 0)
      IF @companyRequestIds IS NULL
        BEGIN
          SET @companyRequestIds = ''
        END
      SET @customerAcounts = ( SELECT String_agg(CAST(customeraccounts.Account_id as nvarchar(max)) ,',') FROM [${dbxschemaname}].customeraccounts WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraccounts.Customer_id , @_customerId) <> 0)

      IF @customerAcounts IS NULL
        BEGIN
          SET @customerAcounts = ''
        END

		SELECT  customerCnt, requestId into #approvalCount1 from
	 (SELECT COUNT(DISTINCT(customerId)) as customerCnt ,requestapprovalmatrix.requestId as requestId FROM [${dbxschemaname}].customerapprovalmatrix INNER JOIN [${dbxschemaname}].requestapprovalmatrix ON (customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId ) WHERE [${dbxschemaname}].FIND_IN_SET(cast(requestapprovalmatrix.requestId as nvarchar(max)), @companyRequestIds) <> 0 group by requestapprovalmatrix.requestId) as approvalCount1

	 SELECT  requestId, totalCnt into #approvalCount2 
	from (select requestId, sum(t) as totalCnt from (
	select bbrequest.requestId as requestId,
         CASE WHEN
			[${dbxschemaname}].approvalrule.numberOfApprovals = -1
			THEN (SELECT count(*)  FROM [${dbxschemaname}].customerapprovalmatrix where customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)
          WHEN [${dbxschemaname}].approvalrule.numberOfApprovals IS NULL OR [${dbxschemaname}].approvalrule.numberOfApprovals='' THEN 0
              ELSE [${dbxschemaname}].approvalrule.numberOfApprovals
          END as t
     from [${dbxschemaname}].customerapprovalmatrix
	 LEFT JOIN [${dbxschemaname}].requestapprovalmatrix ON [${dbxschemaname}].customerapprovalmatrix.approvalMatrixId = [${dbxschemaname}].requestapprovalmatrix.approvalMatrixId
	 LEFT JOIN [${dbxschemaname}].bbrequest ON ([${dbxschemaname}].bbrequest.requestId = [${dbxschemaname}].requestapprovalmatrix.requestId)
	 LEFT JOIN [${dbxschemaname}].approvalmatrix ON ([${dbxschemaname}].requestapprovalmatrix.approvalMatrixId = [${dbxschemaname}].approvalmatrix.id)
	 LEFT JOIN [${dbxschemaname}].approvalrule ON ([${dbxschemaname}].approvalmatrix.approvalruleId = [${dbxschemaname}].approvalrule.id)
     WHERE [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].bbrequest.requestId AS nvarchar(max)),@companyRequestIds)<>0 ) approvals group by requestId) as approvalCount2


      SET @select_statement = 
        '
		SELECT * FROM
          (SELECT 
            bbtemplate.templateId AS templateId,
            bbtemplate.templateName AS templateName,
            bbtemplate.templateDescription AS templateDescription,
            bbtemplate.featureActionId AS featureActionId,
            bbtemplate.fromAccount AS fromAccount,
            bbtemplate.effectiveDate AS effectiveDate,
            bbtemplate.requestId AS requestId,
            bbtemplate.createdby AS createdby,
            bbtemplate.updatedBy AS updatedBy,
            bbtemplate.createdts AS createdts,
            bbtemplate.maxAmount AS maxAmount,
            bbtemplate.status AS status,
            bbtemplate.transactionType_id AS transactionType_id,
            bbtemplate.templateType_id AS templateType_id,
            bbtemplate.companyId AS companyId,
            bbtemplate.templateRequestType_id AS templateRequestType_id,
            bbtemplate.softDelete AS softDelete,
            bbtemplate.totalAmount AS totalAmount,
            ( 
            CASE 
               WHEN customeraccounts.AccountName is NULL THEN ''AccountName'' 
               ELSE customeraccounts.AccountName 
            END 
            ) AS accountName,
            customer.UserName AS userName,
            bbtransactiontype.transactionTypeName AS transactionTypeName,
            bbtemplatetype.templateTypeName AS templateTypeName,
            bbtemplaterequesttype.templateRequestTypeName AS templateRequestTypeName,
            organisation.Name AS companyName,
            bbrequest.createdby AS requestCreatedby,
            (
            CASE
              WHEN [${dbxschemaname}].FIND_IN_SET(CAST(bbtemplate.createdby as nvarchar(max)),CAST('''+ @combinedIds +''' as nvarchar(max))) > 0 THEN ''true''
              ELSE ''false''
            END
            ) as amICreator,
            (
            CASE 
              WHEN [${dbxschemaname}].FIND_IN_SET(CAST(bbrequest.requestId AS nvarchar(max)),CAST('''+@approvalRequestIds+''' as nvarchar(max))) > 0 THEN ''true''
              ELSE ''false''
            END
            ) as amIApprover
            FROM
            ((((((([${dbxschemaname}].bbtemplate
            LEFT JOIN [${dbxschemaname}].customer ON ([${dbxschemaname}].bbtemplate.createdby = [${dbxschemaname}].customer.id))
            LEFT JOIN [${dbxschemaname}].customeraccounts ON (([${dbxschemaname}].bbtemplate.createdby = [${dbxschemaname}].customeraccounts.Customer_id) AND ([${dbxschemaname}].bbtemplate.fromAccount = [${dbxschemaname}].customeraccounts.Account_id)))
            LEFT JOIN [${dbxschemaname}].bbtransactiontype ON ([${dbxschemaname}].bbtemplate.transactionType_id = [${dbxschemaname}].bbtransactiontype.transactionType_id))
            LEFT JOIN [${dbxschemaname}].bbtemplatetype ON ([${dbxschemaname}].bbtemplate.templateType_id = [${dbxschemaname}].bbtemplatetype.templateType_id))
            LEFT JOIN [${dbxschemaname}].bbtemplaterequesttype ON ([${dbxschemaname}].bbtemplate.templateRequestType_id = [${dbxschemaname}].bbtemplaterequesttype.templateRequestType_id))
            LEFT JOIN [${dbxschemaname}].organisation ON ([${dbxschemaname}].bbtemplate.companyId = [${dbxschemaname}].organisation.id))
            LEFT JOIN [${dbxschemaname}].bbrequest ON ([${dbxschemaname}].bbtemplate.requestId = [${dbxschemaname}].bbrequest.requestId))
            WHERE [${dbxschemaname}].bbtemplate.softDelete = ''0''
            AND ([${dbxschemaname}].bbtemplate.companyId = '''+ @companyId +''' OR [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbtemplate.createdby, '''+@combinedIds+''')>0)
            AND [${dbxschemaname}].bbtemplate.templateId LIKE '''+@_templateId +'''
            AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbtemplate.fromAccount,'''+ @customerAcounts+ ''')<>0
                AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbtemplate.featureActionId,'''+ @createActions+''')<>0'+
                @queryTypecondition+ ' '+
                @searchQuery+ ' ) AS t1
            LEFT JOIN
            ( 
            SELECT 
               [${dbxschemaname}].bbrequest.requestId,
               (select count(DISTINCT([${dbxschemaname}].bbactedrequest.createdby)) from [${dbxschemaname}].bbactedrequest where [${dbxschemaname}].bbactedrequest.action = ''Approved'' AND  [${dbxschemaname}].bbactedrequest.requestId = [${dbxschemaname}].bbrequest.requestId) 
                     as receivedApprovals,
                    ([${dbxschemaname}].LEASTINT((select customerCnt from #approvalCount1 where requestid=bbrequest.requestId ),(select totalCnt from #approvalCount2 where requestid=bbrequest.requestId ))) as requiredApprovals
            FROM
            [${dbxschemaname}].bbrequest
            LEFT JOIN [${dbxschemaname}].requestapprovalmatrix ON ([${dbxschemaname}].bbrequest.requestId = [${dbxschemaname}].requestapprovalmatrix.requestId)
            LEFT JOIN [${dbxschemaname}].approvalmatrix ON ([${dbxschemaname}].requestapprovalmatrix.approvalMatrixId = [${dbxschemaname}].approvalmatrix.id)
            LEFT JOIN [${dbxschemaname}].approvalrule ON (approvalmatrix.approvalruleId = approvalrule.id)
                WHERE [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].bbrequest.requestId AS nvarchar(max)),'''+@companyRequestIds+''')<>0
            GROUP BY [${dbxschemaname}].bbrequest.requestId,[${dbxschemaname}].requestapprovalmatrix.approvalMatrixId,[${dbxschemaname}].approvalrule.numberOfApprovals
            ) AS t2
            ON 
            t1.requestId = t2.requestId
            ORDER BY '+ @_sortByParam+' '+ @_sortOrder + ' '+
            @paginationQuery
      EXEC(@select_statement)
	  DROP TABLE #approvalCount1;
	  DROP TABLE #approvalCount2;
   END
   MAINLABEL$leave:

GO

/****** Object:  StoredProcedure [${dbxschemaname}].[fetch_bulkwire_files_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0134: Conversion of following Comment(s) is not supported :  select @select_statement;
*
*/
CREATE PROCEDURE [${dbxschemaname}].[fetch_bulkwire_files_proc]  
   @createdBy nvarchar(50),
   @searchString nvarchar(50),
   @sortByParam nvarchar(50),
   @sortOrder nvarchar(50),
   @pageOffset int,
   @pageSize int
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
	  declare @companyId nvarchar(max)
	  declare @isSMEUser bit
	  declare @filterRetail nvarchar(max)
	  declare @filterSME nvarchar(max)
	  declare @getByIdFilter nvarchar(max)

      SET @companyId = 
         (
            SELECT customer.Organization_Id
            FROM [${dbxschemaname}].customer
            WHERE customer.id = @createdby
         )

      SET @isSMEUser = case when @companyId='' or @companyId is null then 0 else 1 end

      SET @filterRetail = (N'bulkwirefiles.createdBy = ') + (@createdby) + (N' AND bulkwirefiles.softdeleteflag = 0 ')

      SET @filterSME = (N'bulkwirefiles.company_id = ') + (@companyId) + (N' AND bulkwirefiles.softdeleteflag = 0')

      SET @getByIdFilter = 
         CASE 
            WHEN (@isSMEUser <> 0) THEN @filterSME
            ELSE @filterRetail
         END

      SET @sortByParam = 0

      SET @sortByParam = 
         CASE 
            WHEN (@sortByParam = 'username') THEN N'firstName,lastname'
            ELSE @sortByParam
         END

      SET @sortOrder = case when @sortOrder='' or @sortOrder is null then 'DESC' else @sortOrder end

      SET @searchString = case when @searchString ='' or @searchString is null then '' else '%'+@searchString+'%' end
	  declare @orderBy nvarchar(max)
	  declare @paginationQuery nvarchar(max)
	  declare @searchQuery nvarchar(max)

      SET @orderBy = (N' ORDER BY ') + (@sortByParam) + (N' ') + (@sortOrder)

      SET @paginationQuery = case when (@pageOffset ='' or @pageOffset  is null) and (@pageSize ='' or @pageSize is null)
	  then '' else 'OFFSET '+ @pageOffset +' ROWS FETCH NEXT '+@pageSize + ' ROWS ONLY' end

      SET @searchQuery = 
         (N'(bulkwirefiles.bulkWireFileName LIKE ')
          + 
         (@searchString)
          + 
         (N' OR bulkwirefiles.noOfTransactions LIKE ')
          + 
         (@searchString)
          + 
         (N' OR bulkwirefiles.noOfDomesticTransactions LIKE ')
          + 
         (@searchString)
          + 
         (N' OR bulkwirefiles.noOfInternationalTransactions LIKE ')
          + 
         (@searchString)
          + 
         (N' OR  customer.firstname LIKE ')
          + 
         (@searchString)
          + 
         (N' OR customer.lastname LIKE ')
          + 
         (@searchString)
          + 
         (N')')
		 declare @defaultFilter nvarchar(max)
		 declare @searchFilter nvarchar(max)
		 declare @filter nvarchar(max)
		 declare @select_statement nvarchar(max)
      SET @defaultFilter = (@getByIdFilter) + (@orderBy) + (@paginationQuery)
 
      SET @searchFilter = (@getByIdFilter) + (N' AND ') + (@searchQuery) + (@orderBy)

      SET @filter = 
         CASE 
            WHEN (@searchString = '') THEN @defaultFilter
            ELSE @searchFilter
         END

      SET @select_statement = (N'SELECT bulkwirefiles.bulkWireFileID,bulkwirefiles.bulkWireFileName, bulkwirefiles.noOfTransactions, bulkwirefiles.noOfDomesticTransactions,bulkwirefiles.noOfInternationalTransactions,bulkwirefiles.createdts,bulkwirefiles.lastmodifiedts,bulkwirefiles.lastExecutedOn,customer.id, customer.FirstName as firstname,customer.LastName as lastname FROM ([${dbxschemaname}].bulkwirefiles LEFT JOIN [${dbxschemaname}].customer ON (bulkwirefiles.createdBy = customer.id)) WHERE ') + (@filter)

	  exec(@select_statement)

   END
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[fetch_bulkwire_files_transct_detail_proc]    Script Date: 6/9/2020 2:00:56 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[fetch_bulkwire_files_transct_detail_proc]  
   @bulkWireFileID nvarchar(50),
   @searchString nvarchar(50),
   @sortByParam nvarchar(50),
   @sortOrder nvarchar(50),
   @pageOffset int,
   @pageSize int
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
	  declare @qfilter nvarchar(max)
      SET @qfilter = (N'bulkwirefiletransactdetails.bulkWireFileID = ''') + (@bulkWireFileID) + (N''' AND bulkwirefiletransactdetails.softdeleteflag = 0 ')


      SET @sortByParam = case when @sortByParam = '' or @sortByParam is null then 'transactionDate' else @sortByParam end

      SET @sortByParam = 
         CASE 
            WHEN (@sortByParam = 'username') THEN N'firstname,lastname'
            ELSE @sortByParam
         END

      SET @sortOrder = case when @sortOrder='' or @sortOrder is null then 'DESC' else @sortOrder end 

      SET @searchString = case when @searchString='' or @searchString is null then '' else '%'+@searchString+'%' end

	  declare @orderBy nvarchar(max)
	  declare @paginationQuery nvarchar(max)
	  declare @searchQuery nvarchar(max)

      SET @orderBy = (N' ORDER BY ') + (@sortByParam) + (N' ') + (@sortOrder)

      SET @paginationQuery = case when (@pageOffset='' or @pageOffset is null) and (@pageSize ='' or @pageSize is null)
	  then '' else ' OFFSET '+CAST(@pageOffset as nvarchar(max))+' ROWS FETCH NEXT '+CAST(@pageSize as nvarchar(max))+ ' ROWS ONLY '  end 
	  
      SET @searchQuery = 
         (N'(bulkwirefiletransactdetails.transactionDate LIKE ')
          + 
         (@searchString)
          + 
         (N' OR bulkwirefiletransactdetails.totalCountOfTransactions LIKE ')
          + 
         (@searchString)
          + 
         (N' OR  customer.firstname LIKE ')
          + 
         (@searchString)
          + 
         (N' OR customer.lastname LIKE ')
          + 
         (@searchString)
          + 
         (N')')
		 declare @defaultFilter nvarchar(max)
		 declare @searchFilter nvarchar(max)
		 declare @filter nvarchar(max)
      SET @defaultFilter = (@qfilter) + (@orderBy) + (@paginationQuery)

      SET @searchFilter = (@qfilter) + (N' AND ') + (@searchQuery) + (@orderBy)

      SET @filter = 
         CASE 
            WHEN (@searchString = '') THEN @defaultFilter
            ELSE @searchFilter
         END
	   declare @select_statement nvarchar(max)
      SET @select_statement = (N'SELECT bulkwirefiletransactdetails.bulkWireTransactionID,bulkwirefiletransactdetails.bulkWireFileID, bulkwirefiletransactdetails.initiatedBy, bulkwirefiletransactdetails.createdts,bulkwirefiletransactdetails.lastmodifiedts,bulkwirefiletransactdetails.transactionDate, bulkwirefiletransactdetails.totalCountOfTransactions,bulkwirefiletransactdetails.totalCountOfDomesticTransactions,bulkwirefiletransactdetails.totalCountOfInternationalTransactions,customer.FirstName as firstname,customer.LastName as lastname  FROM  ([${dbxschemaname}].bulkwirefiletransactdetails LEFT JOIN [${dbxschemaname}].customer ON ([${dbxschemaname}].bulkwirefiletransactdetails.initiatedBy = [${dbxschemaname}].customer.id))WHERE ') + (@filter)
	  exec(@select_statement)

   END
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[fetch_bulkwires_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[fetch_bulkwires_proc]  
   @createdBy nvarchar(50),
   @searchString nvarchar(50),
   @sortByParam nvarchar(50),
   @sortOrder nvarchar(50),
   @pageOffset int,
   @pageSize int,
   /*
   *   SSMA informational messages:
   *   M2SS0055: Data type was converted to VARCHAR according to character set mapping for latin1 character set
   */

   @bulkWireCategoryFilter varchar(50),
   @fileDomesticView smallint,
   @fileInternationalView smallint,
   @templateDomesticView smallint,
   @templateInternationalView smallint
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DECLARE @companyId nvarchar(max)
	  DECLARE @isSMEUser int
	  DECLARE @remove0DomesticFile nvarchar(max)
	  DECLARE @remove0InternationalFile nvarchar(max)
	  DECLARE @remove0DomesticTemplate nvarchar(max)
	  DECLARE @remove0InternationalTemplate nvarchar(max)
	  DECLARE @fileAddQuery nvarchar(max)
	  DECLARE @fileAdd nvarchar(max)
	  DECLARE @templateAddQuery nvarchar(max)
	  DECLARE @templateAdd nvarchar(max)
	  DECLARE @filterRetailFile nvarchar(max)
	  DECLARE @filterSMEFile nvarchar(max)
	  DECLARE @filterRetailTemplate nvarchar(max)
	  DECLARE @filterSMETemplate nvarchar(max)
	  DECLARE @getByIdFilterFile nvarchar(max)
	  DECLARE @getByIdFilterTemplate nvarchar(max)
	  DECLARE @searchQueryTemplate nvarchar(max)
	  DECLARE @searchQueryFile nvarchar(max)
	  DECLARE @orderBy nvarchar(max)
	  DECLARE @onlyFileWithoutSearch nvarchar(max)
	  DECLARE @searchFilterFile nvarchar(max)
	  DECLARE @paginationQuery nvarchar(max)
	  DECLARE @filterFile nvarchar(max)
	  DECLARE @OnlyFile nvarchar(max)
	  DECLARE @finalFile nvarchar(max)
	  DECLARE @defaultFilterTemplate nvarchar(max)
	  DECLARE @searchFilterTemplate nvarchar(max)
	  DECLARE @filterTemplate nvarchar(max)
	  DECLARE @select_files nvarchar(max)
	  DECLARE @select_templates nvarchar(max)
	  DECLARE @select_statement nvarchar(max)
	  
      SET @companyId = 
         (
            SELECT customer.Organization_Id
            FROM [${dbxschemaname}].customer
            WHERE customer.id = @createdby
         )

		 SET @isSMEUser = 0
	if(@companyId is not null or @companyId != '')
      SET @isSMEUser = 1


      SET @remove0DomesticFile = (N' AND bulkwirefiles.noOfDomesticTransactions <> 0')

      SET @remove0InternationalFile = (N' AND bulkwirefiles.noOfInternationalTransactions <> 0')

      SET @remove0DomesticTemplate = (N' AND bulkwiretemplate.noOfDomesticTransactions <> 0')

      SET @remove0InternationalTemplate = (N' AND bulkwiretemplate.noOfInternationalTransactions <> 0')

      SET @fileAddQuery = 
         CASE 
            WHEN (@fileDomesticView <> 0) THEN @remove0DomesticFile
            ELSE @remove0InternationalFile
         END

      SET @fileAdd = 
         CASE 
            WHEN (@fileDomesticView <> 0 AND @fileInternationalView <> 0) THEN N''
            ELSE @fileAddQuery
         END

      SET @templateAddQuery = 
         CASE 
            WHEN (@templateDomesticView <> 0) THEN @remove0DomesticTemplate
            ELSE @remove0InternationalTemplate
         END

      SET @templateAdd = 
         CASE 
            WHEN (@templateDomesticView <> 0 AND @templateInternationalView <> 0) THEN N''
            ELSE @templateAddQuery
         END
     
	  SET @filterRetailFile = (N'bulkwirefiles.createdBy = ''') + (@createdby) + (N''' AND bulkwirefiles.softdeleteflag = 0') + (@fileAdd)

      SET @filterSMEFile = (N'bulkwirefiles.company_id = ''') + (@companyId) + (N''' AND bulkwirefiles.softdeleteflag = 0') + (@fileAdd)
     
      SET @filterRetailTemplate = (N'bulkwiretemplate.createdBy = ''') + (@createdby) + (N''' AND bulkwiretemplate.softdeleteflag = 0') + (@templateAdd)

      SET @filterSMETemplate = (N'bulkwiretemplate.company_id = ''') + (@companyId) + (N''' AND bulkwiretemplate.softdeleteflag = 0') + (@templateAdd)

      SET @getByIdFilterFile = 
         CASE 
            WHEN (@isSMEUser <> 0) THEN @filterSMEFile
            ELSE @filterRetailFile
         END

      SET @getByIdFilterTemplate = 
         CASE 
            WHEN (@isSMEUser <> 0) THEN @filterSMETemplate
            ELSE @filterRetailTemplate
         END
 
      SET @sortByParam = 
		CASE 
            WHEN (@sortByParam = '' or @sortByParam is null) THEN N'createdts'
            ELSE @sortByParam
         END

      SET @sortOrder = 
		CASE 
            WHEN (@sortOrder = '' or @sortOrder is null) THEN N'DESC '
            ELSE @sortOrder
         END

      SET @sortByParam = 
         CASE 
            WHEN (@sortByParam = 'username') THEN N'firstName ' + @sortOrder + N',lastname'
            ELSE @sortByParam
         END

      SET @searchString = 
		CASE 
            WHEN (@searchString = '' or @searchString is null) THEN N''
            ELSE '''%'+@searchString+'%'''
         END

      SET @orderBy = (N' ORDER BY ') + (@sortByParam) + (N' ') + (@sortOrder)

      SET @paginationQuery = 
		CASE
			WHEN((@pageOffset is not NULL OR @pageOffset != '') AND( @pageSize is not NULL OR @pageSize != ''))
			THEN N'OFFSET '+CAST(@pageOffset as nvarchar(10))+N' ROWS FETCH NEXT '+ CAST(@pageSize as nvarchar(10)) + N' ROWS ONLY'
            ELSE N''
		END

      SET @searchQueryFile = 
         (N'(bulkwirefiles.bulkWireFileName LIKE ')
          + 
         (@searchString)
          + 
         (N' OR bulkwirefiles.noOfTransactions LIKE ')
          + 
         (@searchString)
          + 
         (N' OR bulkwirefiles.noOfDomesticTransactions LIKE ')
          + 
         (@searchString)
          + 
         (N' OR bulkwirefiles.noOfInternationalTransactions LIKE ')
          + 
         (@searchString)
          + 
         (N' OR  customer.firstname LIKE ')
          + 
         (@searchString)
          + 
         (N' OR customer.lastname LIKE ')
          + 
         (@searchString)
          + 
         (N')')
      
      SET @searchQueryTemplate = 
         (N'(bulkwiretemplate.bulkWireTemplateName LIKE ')
          + 
         (@searchString)
          + 
         (N' OR bulkwiretemplate.noOfTransactions LIKE ')
          + 
         (@searchString)
          + 
         (N' OR bulkwiretemplate.noOfDomesticTransactions LIKE ')
          + 
         (@searchString)
          + 
         (N' OR bulkwiretemplate.noOfInternationalTransactions LIKE ')
          + 
         (@searchString)
          + 
         (N' OR  customer.firstname LIKE ')
          + 
         (@searchString)
          + 
         (N' OR customer.lastname LIKE ')
          + 
         (@searchString)
          + 
         (N')')
      
      SET @onlyFileWithoutSearch = (@getByIdFilterFile) + (@orderBy) + (@paginationQuery)
      
      SET @searchFilterFile = (@getByIdFilterFile) + (N' AND ') + (@searchQueryFile)
     
      SET @filterFile = 
         CASE 
            WHEN (@searchString = '') THEN @getByIdFilterFile
            ELSE @searchFilterFile
         END
      
      SET @OnlyFile = 
         CASE 
            WHEN (@searchString = '') THEN @onlyFileWithoutSearch
            ELSE @searchFilterFile
         END
     
      SET @finalFile = 
         CASE 
            WHEN (@bulkWireCategoryFilter = 'Files') THEN @OnlyFile
            ELSE @filterFile
         END
     
      SET @defaultFilterTemplate = (@getByIdFilterTemplate) + (@orderBy) + (@paginationQuery)
      
      SET @searchFilterTemplate = (@getByIdFilterTemplate) + (N' AND ') + (@searchQueryTemplate) + (@orderBy)
      
      SET @filterTemplate = 
         CASE 
            WHEN (@searchString = '') THEN @defaultFilterTemplate
            ELSE @searchFilterTemplate
         END
     
      SET @select_files = ('SELECT bulkwirefiles.bulkWireFileID as bulkWireID, bulkwirefiles.bulkWireFileName as bulkWireName,  bulkwirefiles.noOfTransactions, bulkwirefiles.noOfDomesticTransactions, bulkwirefiles.noOfInternationalTransactions,  bulkwirefiles.createdts, bulkwirefiles.lastmodifiedts, bulkwirefiles.lastExecutedOn, NULL as defaultFromAccount, NULL as defaultCurrency, ''Files'' as bulkWireCategory, customer.FirstName as firstname, customer.LastName as lastname FROM ([${dbxschemaname}].bulkwirefiles LEFT JOIN [${dbxschemaname}].customer ON (bulkwirefiles.createdBy = customer.id)) WHERE ') + (@finalFile)

      SET @select_templates = ('SELECT bulkwiretemplate.bulkWireTemplateID as bulkWireID, bulkwiretemplate.bulkWireTemplateName as bulkWireName,  bulkwiretemplate.noOfTransactions,        bulkwiretemplate.noOfDomesticTransactions,       bulkwiretemplate.noOfInternationalTransactions,        bulkwiretemplate.createdts,       bulkwiretemplate.lastmodifiedts,        bulkwiretemplate.lastExecutedOn,          bulkwiretemplate.defaultFromAccount,           bulkwiretemplate.defaultCurrency, ''Templates'' as bulkWireCategory, customer.FirstName as firstname, customer.LastName as lastname FROM  ([${dbxschemaname}].bulkwiretemplate LEFT JOIN [${dbxschemaname}].customer ON (bulkwiretemplate.createdBy = customer.id)) WHERE ') + (@filterTemplate)

      IF @bulkWireCategoryFilter = 'Files'

         SET @select_statement = @select_files

      ELSE 
         IF @bulkWireCategoryFilter = 'Templates'

            SET @select_statement = @select_templates

         ELSE 

            SET @select_statement = (@select_files) + (N' UNION ALL ') + (@select_templates)
 
	EXECUTE (@select_statement)
   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[fetch_bulkWireTemplateTransactionsExecution_details_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[fetch_bulkWireTemplateTransactionsExecution_details_proc]  
   @BulkWireTemplateExecution_id nvarchar(50),
   @searchString nvarchar(50),
   @statusFilter nvarchar(50),
   @sortByParam nvarchar(50),
   @sortOrder nvarchar(50),
   @isDomesticPermitted int,
   @isInternationalPermitted int
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

	  DECLARE @query1 nvarchar(max)
	  DECLARE @query2 nvarchar(max)
	  DECLARE @query3 nvarchar(max)
	  DECLARE @orderBy nvarchar(max)
	  DECLARE @searchQuery nvarchar(max)
	  DECLARE @statusQuery nvarchar(max)
	  DECLARE @finalQueryFilter nvarchar(max)
	  DECLARE @defaultFilter nvarchar(max)
	  DECLARE @searchFilter nvarchar(max)
	  DECLARE @filter nvarchar(max)
	  DECLARE @DomQuery nvarchar(max)
	  DECLARE @DomCount int 
	  DECLARE @InternationalQuery nvarchar(max)
	  DECLARE @InternationalCount int 
	  DECLARE @select_statement nvarchar(max)
	  DECLARE @groupByCnt nvarchar(max)

      SET @query1 = ('wiretransfers.wireTemplateExecution_id = ''') + (@BulkWireTemplateExecution_id) + (''' AND wiretransfers.softdeleteflag = 0 ')
      SET @query2 = (N'wiretransfers.wireTemplateExecution_id = ''') + (@BulkWireTemplateExecution_id) + (''' AND wiretransfers.status =''') + (@statusFilter) + (N''' AND wiretransfers.softdeleteflag = 0 ')
      SET @query3 = (N'wiretransfers.wireTemplateExecution_id = ''') + (@BulkWireTemplateExecution_id) + (''' AND wiretransfers.status in (''Failed'',''Denied'')') + (N' AND wiretransfers.softdeleteflag = 0 ')

	  SET @sortByParam = CASE WHEN @sortByParam = '' OR @sortByParam is NULL THEN 'transactionId' ELSE @sortByParam END
	  SET @sortByParam = CASE WHEN @sortByParam = 'payeeName' THEN 'wiretransfers.payeeName' ELSE @sortByParam END
	  SET @sortOrder = CASE WHEN (@sortOrder = '' OR @sortOrder is NULL) THEN 'ASC' ELSE @sortOrder END

	  SET @searchString = CASE WHEN (@searchString = '' OR @searchString is NULL) THEN '' ELSE ('''%'+@searchString+'%''') END
	
	  SET @groupByCnt=' GROUP BY [${dbxschemaname}].wiretransfers.transactionId '
      SET @orderBy = (' ORDER BY ') + (@sortByParam) + (' ') + (@sortOrder)
      SET @searchQuery =('(wiretransfers.amount LIKE ')+(@searchString)+(' OR wiretransfers.notes LIKE ')+(@searchString)+ 
         (' OR wiretransfers.fromAccountNumber LIKE ')+(@searchString)+(' OR wiretransfers.payeeAccountNumber LIKE ')+(@searchString)+ 
         (' OR wiretransfers.transactionType LIKE ')+(@searchString)+(' OR onetimepayee.payeeName LIKE ')+(@searchString)+ 
         (' OR onetimepayee.payeeType LIKE ')+(@searchString)+(' OR onetimepayee.payeeAddressLine1 LIKE ')+(@searchString)+ 
         (' OR onetimepayee.payeeAddressLine2 LIKE ')+(@searchString)+(' OR onetimepayee.cityName LIKE ')+(@searchString)+ 
         (' OR onetimepayee.state LIKE ')+(@searchString)+(' OR onetimepayee.zipCode LIKE ')+(@searchString)+ 
         (' OR onetimepayee.bankName LIKE ')+(@searchString)+(' OR onetimepayee.bankAddressLine1 LIKE ')+(@searchString)+ 
         (' OR onetimepayee.bankAddressLine2 LIKE ')+(@searchString)+(' OR onetimepayee.bankZip LIKE ')+(@searchString)+ 
         (' OR onetimepayee.bankState LIKE ')+(@searchString)+(' OR onetimepayee.bankCity LIKE ')+(@searchString)+ 
         (' OR  onetimepayee.routingNumber LIKE ')+(@searchString)+(' OR onetimepayee.internationalRoutingCode LIKE ')+(@searchString)+ 
         (N' OR onetimepayee.swiftCode LIKE ')+(@searchString)+(N')')

      SET @statusQuery = CASE WHEN (@statusFilter = 'Failed') THEN @query3 ELSE @query2 END
      SET @finalQueryFilter = CASE WHEN (@statusFilter = '') THEN @query1 ELSE @statusQuery END
      SET @defaultFilter = (@finalQueryFilter)
      SET @searchFilter = (@finalQueryFilter) + (N' AND ') + (@searchQuery)
      SET @filter = CASE WHEN (@searchString = '') THEN @defaultFilter ELSE @searchFilter END

      SET @DomQuery = ('DECLARE @DomCount int
	  SET @DomCount=( SELECT COUNT(*) FROM ([${dbxschemaname}].wiretransfers LEFT JOIN [${dbxschemaname}].onetimepayee 
	  ON(wiretransfers.onetime_id = onetimepayee.onetime_id)) WHERE onetimepayee.wireAccountType = ''Domestic'' AND ') + (@filter) +
	  (' ) ')
      EXEC(@DomQuery)

      SET @InternationalQuery = ('DECLARE @InternationalCount int
	  SET @InternationalCount =( SELECT COUNT(*) FROM ([${dbxschemaname}].wiretransfers LEFT JOIN [${dbxschemaname}].onetimepayee 
	  ON(wiretransfers.onetime_id = onetimepayee.onetime_id)) WHERE onetimepayee.wireAccountType = ''International'' AND ') + (@filter) +
	  (' ) ')
	  EXEC(@InternationalQuery)

      IF (@isDomesticPermitted = 0 AND @isInternationalPermitted = 0)
         SELECT N'UNAUTHORIZED' AS ErrorMessage
      ELSE IF (@isDomesticPermitted = 0 AND @InternationalCount = 0)
         SELECT N'UNAUTHORIZED' AS ErrorMessage
      ELSE IF (@isInternationalPermitted = 0 AND @DomCount = 0)
         SELECT N'UNAUTHORIZED' AS ErrorMessage
      ELSE 
         BEGIN
           SET @select_statement = ('SELECT wiretransfers.transactionId,wiretransfers.confirmationNumber,wiretransfers.requestId,
		   wiretransfers.status,wiretransfers.onetime_id,wiretransfers.wireTemplateExecution_id,wiretransfers.notes,
		   wiretransfers.amount,wiretransfers.fromAccountNumber,wiretransfers.payeeAccountNumber,wiretransfers.transactionType,
		   wiretransfers.payeeId,wiretransfers.payeeCurrency,onetimepayee.payeeName,onetimepayee.payeeNickName,onetimepayee.payeeType,
		   onetimepayee.wireAccountType,onetimepayee.swiftCode,onetimepayee.routingNumber,onetimepayee.zipCode,onetimepayee.cityName,
		   onetimepayee.state,onetimepayee.country,onetimepayee.payeeAddressLine1,onetimepayee.payeeAddressLine2,onetimepayee.bankName,
		   onetimepayee.internationalRoutingCode,onetimepayee.bankAddressLine1,onetimepayee.bankAddressLine2,onetimepayee.bankCity,
		   onetimepayee.bankState,onetimepayee.bankZip FROM ([${dbxschemaname}].wiretransfers LEFT JOIN [${dbxschemaname}].onetimepayee 
		   ON(wiretransfers.onetime_id = onetimepayee.onetime_id)) WHERE ') + (@filter) + @orderBy
		   EXEC(@select_statement)
		   
         END
   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[fetch_bulkWireTransactionsExecution_details_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[fetch_bulkWireTransactionsExecution_details_proc]  
   @BulkWireFileExecution_id nvarchar(50),
   @searchString nvarchar(50),
   @statusFilter nvarchar(50),
   @sortByParam nvarchar(50),
   @sortOrder nvarchar(50),
   @isDomesticPermitted int,
   @isInternationalPermitted int
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

	  declare @query1 nvarchar(max)
	  declare @query2 nvarchar(max)
	  declare @query3 nvarchar(max)
	  declare @orderBy nvarchar(max)
	  declare @searchQuery nvarchar(max)
	  declare @statusQuery nvarchar(max)
	  declare @finalQueryFilter nvarchar(max)
	  declare @defaultFilter nvarchar(max)
	  declare @searchFilter nvarchar(max)
	  declare @filter nvarchar(max)
	  declare @select_statement nvarchar(max)
	  declare @DomQuery nvarchar(max)
	  declare @InternationalQuery nvarchar(max)
	  declare @DomCount nvarchar(max)
	  declare @InternationalCount nvarchar(max)

      SET @query1 = (N'wiretransfers.wireFileExecution_id = ') + (@BulkWireFileExecution_id) + (N' AND wiretransfers.softdeleteflag = 0 ')

      SET @query2 = (N'wiretransfers.wireFileExecution_id = ') + (@BulkWireFileExecution_id) + (N' AND wiretransfers.status =''') + (@statusFilter) + (N''' AND wiretransfers.softdeleteflag = 0 ')

      SET @query3 = (N'wiretransfers.wireFileExecution_id = ') + (@BulkWireFileExecution_id) + (N' AND wiretransfers.status in (''Failed'',''Denied'')') + (N' AND wiretransfers.softdeleteflag = 0 ')

      SET @sortByParam = case when @sortByParam='' or @sortByParam is null then 'transactionId' else @sortByParam end

      SET @sortOrder = case when @sortOrder='' or @sortOrder is null then 'ASC' else @sortOrder end

      SET @searchString = case when @searchString='' or @searchString is null then '' else '''%'+@searchString+'%''' end

	  
      SET @orderBy = (N' ORDER BY ') + (@sortByParam) + (N' ') + (@sortOrder)
	  
      SET @searchQuery = 
         (N'(wiretransfers.amount LIKE ')
          + 
         (@searchString)
          + 
         (N' OR wiretransfers.notes LIKE ')
          + 
         (@searchString)
          + 
         (N' OR wiretransfers.fromAccountNumber LIKE ')
          + 
         (@searchString)
          + 
         (N' OR wiretransfers.payeeAccountNumber LIKE ')
          + 
         (@searchString)
          + 
         (N' OR wiretransfers.transactionType LIKE ')
          + 
         (@searchString)
          + 
         (N' OR onetimepayee.payeeName LIKE ')
          + 
         (@searchString)
          + 
         (N' OR onetimepayee.payeeType LIKE ')
          + 
         (@searchString)
          + 
         (N' OR onetimepayee.payeeAddressLine1 LIKE ')
          + 
         (@searchString)
          + 
         (N' OR onetimepayee.payeeAddressLine2 LIKE ')
          + 
         (@searchString)
          + 
         (N' OR onetimepayee.cityName LIKE ')
          + 
         (@searchString)
          + 
         (N' OR onetimepayee.state LIKE ')
          + 
         (@searchString)
          + 
         (N' OR onetimepayee.zipCode LIKE ')
          + 
         (@searchString)
          + 
         (N' OR onetimepayee.bankName LIKE ')
          + 
         (@searchString)
          + 
         (N' OR onetimepayee.bankAddressLine1 LIKE ')
          + 
         (@searchString)
          + 
         (N' OR onetimepayee.bankAddressLine2 LIKE ')
          + 
         (@searchString)
          + 
         (N' OR onetimepayee.bankZip LIKE ')
          + 
         (@searchString)
          + 
         (N' OR onetimepayee.bankState LIKE ')
          + 
         (@searchString)
          + 
         (N' OR onetimepayee.bankCity LIKE ')
          + 
         (@searchString)
          + 
         (N' OR  onetimepayee.routingNumber LIKE ')
          + 
         (@searchString)
          + 
         (N' OR onetimepayee.internationalRoutingCode LIKE ')
          + 
         (@searchString)
          + 
         (N' OR onetimepayee.swiftCode LIKE ')
          + 
         (@searchString)
          + 
         (N')')
		 
      SET @statusQuery = 
         CASE 
            WHEN (@statusFilter = 'Failed') THEN @query3
            ELSE @query2
         END
		
      SET @finalQueryFilter = 
         CASE 
            WHEN (@statusFilter = '') THEN @query1
            ELSE @statusQuery
         END
		 
      SET @defaultFilter = (@finalQueryFilter)

      SET @searchFilter = (@finalQueryFilter) + (N' AND ') + (@searchQuery)

      SET @filter = 
         CASE 
            WHEN (@searchString = '') THEN @defaultFilter
            ELSE @searchFilter
         END

		 DECLARE @ParmDefinition nvarchar(500);
		 SET @ParmDefinition = N'@retvalOUT int OUTPUT';
		SET @DomQuery = 'SELECT @retvalOUT= COUNT(*) FROM [${dbxschemaname}].wiretransfers LEFT JOIN [${dbxschemaname}].onetimepayee ON(wiretransfers.onetime_id = onetimepayee.onetime_id) WHERE onetimepayee.wireAccountType = ''Domestic'' AND '+ @filter
		EXEC sp_executesql @DomQuery, @ParmDefinition, @retvalOUT=@DomCount OUTPUT;

		SET @InternationalQuery = 'SELECT @InternationalCount = COUNT(*) FROM [${dbxschemaname}].wiretransfers LEFT JOIN [${dbxschemaname}].onetimepayee ON(wiretransfers.onetime_id = onetimepayee.onetime_id) WHERE onetimepayee.wireAccountType = ''International'' AND '+ @filter
		EXEC sp_executesql @DomQuery, @ParmDefinition, @retvalOUT=@InternationalCount OUTPUT;

		 SET @filter = (@filter) + (@orderBy)

IF (@isDomesticPermitted = 0 AND @isInternationalPermitted = 0) 
	SELECT 'UNAUTHORIZED' AS ErrorMessage;
ELSE IF (@isDomesticPermitted = 0 AND @InternationalCount = 0)
	SELECT 'UNAUTHORIZED' AS ErrorMessage;
ELSE IF (@isInternationalPermitted = 0 AND @DomCount = 0) 
	SELECT 'UNAUTHORIZED' AS ErrorMessage;
ELSE
BEGIN
    SET @select_statement = ('SELECT wiretransfers.transactionId,wiretransfers.confirmationNumber,wiretransfers.requestId,      wiretransfers.status,wiretransfers.onetime_id,wiretransfers.wireFileExecution_id,wiretransfers.notes,    wiretransfers.amount,wiretransfers.fromAccountNumber,wiretransfers.payeeAccountNumber,      wiretransfers.transactionType,wiretransfers.payeeId,wiretransfers.payeeCurrency,onetimepayee.payeeName,   onetimepayee.payeeNickName,onetimepayee.payeeType,onetimepayee.wireAccountType,onetimepayee.swiftCode,     onetimepayee.routingNumber,onetimepayee.zipCode,onetimepayee.cityName,onetimepayee.state,      onetimepayee.country,onetimepayee.payeeAddressLine1,onetimepayee.payeeAddressLine2,onetimepayee.bankName,   onetimepayee.internationalRoutingCode,onetimepayee.bankAddressLine1,onetimepayee.bankAddressLine2,     onetimepayee.bankCity,onetimepayee.bankState,onetimepayee.bankZip FROM ([${dbxschemaname}].wiretransfers LEFT JOIN [${dbxschemaname}].onetimepayee ON([${dbxschemaname}].wiretransfers.onetime_id = [${dbxschemaname}].onetimepayee.onetime_id)) WHERE ') + (@filter)
	EXEC(@select_statement)
END
END

GO

/****** Object:  StoredProcedure [${dbxschemaname}].[fetch_cardtransaction_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0134: Conversion of following Comment(s) is not supported :  select @select_statement;
*
*/

CREATE PROCEDURE [${dbxschemaname}].[fetch_cardtransaction_proc]  
   /*
   *   SSMA informational messages:
   *   M2SS0055: Data type was converted to VARCHAR according to character set mapping for latin1 character set
   */

   @_cardNumber varchar(50),
   @_pageOffset int,
   @_pageSize int,
   /*
   *   SSMA informational messages:
   *   M2SS0055: Data type was converted to VARCHAR according to character set mapping for latin1 character set
   */

   @_sortOrder varchar(50),
   /*
   *   SSMA informational messages:
   *   M2SS0055: Data type was converted to VARCHAR according to character set mapping for latin1 character set
   */

   @_sortByParam varchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

	  declare @filter nvarchar(max)
	  declare @orderBy nvarchar(max)
	  declare @paginationQuery nvarchar(max)
	  declare @select_statement nvarchar(max)

      SET @filter = (N'cardtransaction.cardNumber = ') + (QUOTENAME(@_cardNumber,''''))

	  SET @_sortByParam = ( CASE when @_sortByParam is null or @_sortByParam = '' 
						then 'transactionDate' else 
						@_sortByParam
						end )
	SET @_sortOrder = ( CASE when @_sortOrder is null or @_sortOrder = '' 
						then 'DESC' else 
						@_sortOrder
						end )

      SET @orderBy = (N' ORDER BY ') + (@_sortByParam) + (N' ') + (@_sortOrder)

      SET @paginationQuery = (CASE when (@_pageOffset is not null and @_pageOffset != '' and @_pageSize is not null and @_pageSize != '') then 
								' OFFSET '+CAST(@_pageOffset as nvarchar(max))+' ROWS FETCH NEXT '+CAST(@_pageSize as nvarchar(max))+' ROWS ONLY'
								else '' end)

      SET @select_statement = 'SELECT cardtransaction.transactionDescription, cardtransaction.transactionBalance,   cardtransaction.transactionMerchantAddressName, cardtransaction.transactionMerchantCity, cardtransaction.merchantCategory, cardtransaction.transactionStatus, cardtransaction.transactionType, cardtransaction.transactionCategory,   cardtransaction.transactionDetailDescription, cardtransaction.transactionIndicator, cardtransaction.transactionDate, cardtransaction.transactionTime, cardtransaction.transactionAmount, cardtransaction.transactionReferenceNumber,  cardtransaction.transactionCurrencyCode, cardtransaction.transactionExchangeRate,   cardtransaction.exchangeCurrency, cardtransaction.exchangeAmount, cardtransaction.transactionTaxIndicator,   cardtransaction.taxPercentage, cardtransaction.transactionTaxAmount, cardtransaction.transactionTerminalID, cardtransaction.cardType  FROM [${dbxschemaname}].cardtransaction cardtransaction WHERE ' + (@filter) + (@orderBy) + (@paginationQuery) + (N' ')
	  
	  EXEC (@select_statement)

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[fetch_fi_org_group_level_limits]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[fetch_fi_org_group_level_limits]  
   @_groupId nvarchar(50),
   @_companyId nvarchar(50)
AS 
   BEGIN
      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON
	  
	  DECLARE @select nvarchar(max)

      SET @select =  'select actionlimit.Action_id,actionlimit.LimitType_id,actionlimit.value as fiValue,
	  organisationactionlimit.value as orgValue,groupactionlimit.value as groupValue from 
	  [${dbxschemaname}].actionlimit LEFT JOIN [${dbxschemaname}].groupactionlimit ON (actionlimit.Action_id = groupactionlimit.Action_id AND 
	  actionlimit.LimitType_id = groupactionlimit.LimitType_id) LEFT JOIN [${dbxschemaname}].organisationactionlimit ON 
	  ( actionlimit.Action_id =  organisationactionlimit.Action_id AND actionlimit.LimitType_id = organisationactionlimit.LimitType_id) 
	  WHERE groupactionlimit.Group_id = '''+(@_groupId)+(''' and ')+('organisationactionlimit.Organisation_id = ''')+(@_companyId)+''''
	  EXEC(@select)
   END

GO
/****** Object:  StoredProcedure [${dbxschemaname}].[fetch_generaltranscation_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[fetch_generaltranscation_proc]  
   @_customerId nvarchar(50),
   @_transactionId nvarchar(50),
   @_featureActionId nvarchar(50),
   @_featureactionlist nvarchar(max),
   @_queryType nvarchar(50),
   @_filterByParam nvarchar(max),
   @_filterByValue nvarchar(max),
   @_searchString nvarchar(50),
   @_sortByParam nvarchar(50),
   @_sortOrder nvarchar(50),
   @_pageSize nvarchar(50),
   @_pageOffset nvarchar(50)
AS 
  
   BEGIN
   
	 MainLabel:
   
      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON

	  declare @customerMatrixIds nvarchar(max)
	  declare @alreadyApprovedIds nvarchar(max)
	  declare @companyId nvarchar(max)
	  declare @approvalRequestIds nvarchar(max)
	  declare @queryTypecondition nvarchar(max)
	  declare @combinedIds nvarchar(max)
	  declare @numOfParams int = 0;
	  declare @searchQuery nvarchar(max) = ''
	  declare @idx int = 1
	  declare @filterParam nvarchar(max) = ''
	  declare @filterValue nvarchar(max) = ''
	  declare @paginationQuery nvarchar(max)
	  declare @features nvarchar(max)
	  declare @companyRequestIds nvarchar(max)
	  declare @customerAcounts nvarchar(max)

	  SET @combinedIds = (select STRING_AGG(CAST(id as nvarchar(max)), ',') from [${dbxschemaname}].customer where combinedUserId = Quotename(@_customerId,''''))

	  IF @combinedIds is NULL or @combinedIds = ''
		SET @combinedIds = @_customerId
	ELSE
		SET @combinedIds = @_customerId + ',' + @combinedIds

	SET @_filterByParam = CASE WHEN (@_filterByParam IS NULL OR @_filterByParam='') THEN '' ELSE @_filterByParam END
    SET @_filterByValue = CASE WHEN (@_filterByValue IS NULL OR @_filterByValue='') THEN  '' ELSE @_filterByValue END
	SET @_transactionId= CASE WHEN ( @_transactionId='' or @_transactionId is null) THEN '%' ELSE @_transactionId END
    SET @_featureActionId = CASE WHEN (@_transactionId = '%') THEN '%' ELSE @_featureActionId  END
	SET @numOfParams = 0;
	IF datalength(@_filterByParam) > 0 
	SET @numOfParams = datalength(@_filterByParam) - datalength(REPLACE(@_filterByParam, ',', '')) + 1;


		WHILE @idx <= @numOfParams
		BEGIN
			SET @filterParam = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_filterByParam, ',', @idx), ',', -1 )
			SET @filterValue = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_filterByValue, ',', @idx), ',', -1 )
			SET @searchQuery = @searchQuery + ' AND (' + @filterParam + ' LIKE '''+@filterValue +''' )'
			SET @idx = @idx + 1;
		END

      SET @companyId = 
         (
            SELECT customer.Organization_Id
            FROM [${dbxschemaname}].customer
            WHERE customer.id = @_customerId
         )

      IF @companyId IS NULL

         SET @companyId = ''
 
      IF @_featureactionlist IS NULL
         GOTO MAINLABEL$leave

      SET @_sortByParam = case when @_sortByParam='' or @_sortByParam is null then 'createdts' else @_sortByParam end

      SET @_sortOrder = case when @_sortOrder = '' or @_sortOrder is null then 'DESC' else @_sortOrder end

      SET @customerMatrixIds = 
         (

            SELECT String_agg(CAST(approvalMatrixId as nvarchar(max)) ,',')
            FROM [${dbxschemaname}].customerapprovalmatrix
            WHERE [${dbxschemaname}].FIND_IN_SET(customerapprovalmatrix.customerId , @combinedIds) <> 0

         )

      IF @customerMatrixIds IS NULL

         SET @customerMatrixIds = ''

      SET @alreadyApprovedIds = 
         (

            SELECT String_agg(CAST(requestId as nvarchar(max)),',')
            FROM [${dbxschemaname}].bbactedrequest
            WHERE [${dbxschemaname}].FIND_IN_SET(bbactedrequest.createdby , @combinedIds) <> 0 AND bbactedrequest.action = 'Approved'

         )
		 
      IF @alreadyApprovedIds IS NULL

         SET @alreadyApprovedIds = ''

	SET @approvalRequestIds = (SELECT STRING_AGG(CAST(requestId as nvarchar(max)), ',') 
        							FROM [${dbxschemaname}].requestapprovalmatrix
        							INNER JOIN [${dbxschemaname}].approvalmatrix ON requestapprovalmatrix.approvalMatrixId = approvalmatrix.id
									INNER JOIN [${dbxschemaname}].approvalrule ON approvalmatrix.approvalruleId = approvalrule.id
        								WHERE [${dbxschemaname}].FIND_IN_SET(CAST(requestapprovalmatrix.approvalMatrixId as nvarchar(max)),  @customerMatrixIds)  <> 0
        								AND [${dbxschemaname}].FIND_IN_SET(CAST(requestapprovalmatrix.requestId as nvarchar(max)), @alreadyApprovedIds) = 0
        								AND ((approvalrule.numberOfApprovals = -1 AND requestapprovalmatrix.receivedApprovals < (SELECT COUNT(DISTINCT(customerId)) FROM [${dbxschemaname}].customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)) 
												OR
											(approvalrule.numberOfApprovals != -1 AND requestapprovalmatrix.receivedApprovals < approvalrule.numberOfApprovals))
        							) 
        IF @approvalRequestIds is NULL      
			SET @approvalRequestIds = ''



      SET @queryTypecondition = 
         CASE 
            WHEN (@_queryType = 'myRequests') THEN (' AND  [${dbxschemaname}].FIND_IN_SET(generaltransaction.createdby, ''') + (@combinedIds) + (''') <> 0 AND (bbrequest.status = ''Pending'' OR bbrequest.status = ''Approved'' OR bbrequest.[status] = ''Rejected'' ) ')
            ELSE 
               CASE 
                  WHEN (@_queryType = 'pendingForMyApprovals') THEN (' AND [${dbxschemaname}].FIND_IN_SET(CAST(generaltransaction.requestId as NVARCHAR(MAX)),  ''') + (@approvalRequestIds) + (''') <> 0  AND generaltransaction.[status] = ''Pending''')
                  ELSE 
                     CASE 
                        WHEN (@_queryType = 'rejected') THEN (' AND generaltransaction.status = ''Rejected'' ')
                        ELSE
							CASE
								WHEN (@_transactionId = '%') THEN (' AND NOT generaltransaction.status = ''Withdrawn''')
								ELSE ''
							END
                     END
               END
         END
		
      set @searchQuery = case when @searchQuery='' or @searchQuery is null then '' else 
	  'AND (generaltransaction.payeeId LIKE ''%'+@_searchString+'%'' OR customeraccounts.accountName LIKE ''%'+@_searchString+'%'' OR feature.name LIKE ''%'+@_searchString+'%'' OR customer.userName LIKE ''%'+@_searchString+'%'' )' end
	  
      SET @paginationQuery = case when (@_pageOffset ='' or @_pageOffset is null) and (@_pageSize ='' or @_pageSize is null) then 
	  '' else ' OFFSET '+@_pageOffset+'  ROWS FETCH NEXT '+@_pageSize + ' ROWS ONLY ' end
	  

      SET @features = 
         (

            SELECT String_agg(CAST(Feature_id as nvarchar(max)),',')
            FROM [${dbxschemaname}].featureaction
            WHERE [${dbxschemaname}].FIND_IN_SET(featureaction.id, @_featureactionlist) <> 0

         )

      IF @features IS NULL
 
         SET @features = ''

	declare @createActions nvarchar(max)
      SET @createActions = 
         (

            SELECT String_agg(CAST(id  as nvarchar(max)),',')
            FROM [${dbxschemaname}].featureaction
            WHERE [${dbxschemaname}].FIND_IN_SET(featureaction.Feature_id, @features) <> 0 AND featureaction.id LIKE '%_CREATE'

         )

      IF @createActions IS NULL

         SET @createActions = ''

		 
      SET @companyRequestIds = 
         (

            SELECT String_Agg(CAST(requestId  as nvarchar(max)),',')
            FROM [${dbxschemaname}].bbrequest
            WHERE bbrequest.companyId = @companyId AND [${dbxschemaname}].FIND_IN_SET(bbrequest.featureActionId, @createActions) <> 0

         )

      IF @companyRequestIds IS NULL

         SET @companyRequestIds = ''
		 
      SET @customerAcounts = 
         (

            SELECT String_agg(CAST(Account_id  as nvarchar(max)),',')
            FROM [${dbxschemaname}].customeraccounts
            WHERE [${dbxschemaname}].FIND_IN_SET(customeraccounts.Customer_id, @combinedIds) <> 0

         )

      IF @customerAcounts IS NULL

         SET @customerAcounts = ''

		 declare @accountsQuery nvarchar(max)
		 declare @companyQuery nvarchar(max)
		 declare @select_statement nvarchar(max)

      SET @accountsQuery = case when (@companyId='' or @companyId is null) then '' else ' AND [${dbxschemaname}].FIND_IN_SET(generaltransaction.fromAccountNumber, '''+@customerAcounts+''') <> 0' end

      SET @companyQuery = case when (@companyId='' or @companyId is null) then '' else ' AND (generaltransaction.companyId = '''+@companyId +''' OR [${dbxschemaname}].FIND_IN_SET(generaltransaction.createdby, '''+ @combinedIds +''') <> 0) ' end

	
	 
	 declare @numberOfApprovals nvarchar(max) = ''
	 SET @numberOfApprovals = ( SELECT COUNT(*) FROM [${dbxschemaname}].bbrequest inner join [${dbxschemaname}].requestapprovalmatrix ON (bbrequest.requestId = requestapprovalmatrix.requestId) inner join [${dbxschemaname}].customerapprovalmatrix on (customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId ) WHERE [${dbxschemaname}].FIND_IN_SET(CAST(bbrequest.requestId as nvarchar(max)), @companyRequestIds) <> 0)



	  SELECT  customerCnt, requestId into #approvalCount1 from
	 (SELECT COUNT(DISTINCT(customerId)) as customerCnt ,requestapprovalmatrix.requestId as requestId FROM [${dbxschemaname}].customerapprovalmatrix INNER JOIN [${dbxschemaname}].requestapprovalmatrix ON (customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId ) WHERE [${dbxschemaname}].FIND_IN_SET(cast(requestapprovalmatrix.requestId as nvarchar(max)), @companyRequestIds) <> 0 group by requestapprovalmatrix.requestId) as approvalCount1


	 SELECT  requestId, totalCnt into #approvalCount2 
	from (select requestId, sum(t) as totalCnt from (
	select bbrequest.requestId as requestId,
         CASE WHEN
			[${dbxschemaname}].approvalrule.numberOfApprovals = -1
			THEN (SELECT count(*)  FROM [${dbxschemaname}].customerapprovalmatrix where customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)
          WHEN [${dbxschemaname}].approvalrule.numberOfApprovals IS NULL OR [${dbxschemaname}].approvalrule.numberOfApprovals='' THEN 0
              ELSE [${dbxschemaname}].approvalrule.numberOfApprovals
          END as t
     from [${dbxschemaname}].customerapprovalmatrix
	 LEFT JOIN [${dbxschemaname}].requestapprovalmatrix ON [${dbxschemaname}].customerapprovalmatrix.approvalMatrixId = [${dbxschemaname}].requestapprovalmatrix.approvalMatrixId
	 LEFT JOIN [${dbxschemaname}].bbrequest ON ([${dbxschemaname}].bbrequest.requestId = [${dbxschemaname}].requestapprovalmatrix.requestId)
	 LEFT JOIN [${dbxschemaname}].approvalmatrix ON ([${dbxschemaname}].requestapprovalmatrix.approvalMatrixId = [${dbxschemaname}].approvalmatrix.id)
	 LEFT JOIN [${dbxschemaname}].approvalrule ON ([${dbxschemaname}].approvalmatrix.approvalruleId = [${dbxschemaname}].approvalrule.id)
     WHERE [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].bbrequest.requestId AS nvarchar(max)),@companyRequestIds)<>0 ) approvals group by requestId) as approvalCount2


     SET @select_statement = '	 
	 SELECT * FROM
        
        (SELECT 
						generaltransaction.transactionId,
						generaltransaction.featureActionId,
						feature.name as featureName,
						generaltransaction.fromAccountNumber,
						generaltransaction.amount,
						generaltransaction.requestId,
						generaltransaction.createdby,
						generaltransaction.createdts,
						generaltransaction.frequencyTypeId,
						generaltransaction.status,
						generaltransaction.numberOfRecurrences,
						generaltransaction.payeeId,
						generaltransaction.companyId,
						generaltransaction.scheduledDate,
						( CASE 
							WHEN customeraccounts.AccountName is NULL THEN ''AccountName'' 
			                ELSE customeraccounts.AccountName 
						END ) AS accountName,
						customer.UserName AS userName,
						organisation.Name AS companyName,
						bbrequest.createdby AS requestCreatedby,
						(CASE
							WHEN [${dbxschemaname}].FIND_IN_SET(generaltransaction.createdby,  CAST('''+@combinedIds+''' as nvarchar(max))) > 0 THEN ''true''
							ELSE ''false''
						 END) as amICreator,
						(CASE 
							WHEN [${dbxschemaname}].FIND_IN_SET(CAST(bbrequest.requestId AS nvarchar(max)), CAST('''+@approvalRequestIds+''' as nvarchar(max))) > 0 THEN ''true''
			        		ELSE ''false''
				 		END)
				 		 as amIApprover

				FROM

				( SELECT 
						billpaytransfers.transactionId AS transactionId,
						billpaytransfers.featureActionId AS featureActionId,
						billpaytransfers.fromAccountNumber AS fromAccountNumber,
						billpaytransfers.amount AS amount,
						billpaytransfers.requestId AS requestId,
						billpaytransfers.createdby AS createdby,
						billpaytransfers.createdts AS createdts,
						billpaytransfers.frequencyTypeId AS frequencyTypeId,
						billpaytransfers.status AS status,
						billpaytransfers.numberOfRecurrences AS numberOfRecurrences,
                        (CASE 
							WHEN billpaytransfers.payeeName IS NULL OR billpaytransfers.payeeName = '''' THEN 
							CASE
								WHEN billpaytransfers.billerId IS NULL OR billpaytransfers.billerId = '''' THEN 
									CASE 
										WHEN billpaytransfers.payeeId IS NULL OR billpaytransfers.payeeId = '''' THEN billpaytransfers.toAccountNumber
										ELSE billpaytransfers.payeeId
									END
								ELSE billpaytransfers.billerId
							END
							ELSE billpaytransfers.payeeName
						END) 
                        AS payeeId,
						billpaytransfers.companyId AS companyId,
		                billpaytransfers.softdeleteflag AS softdeleteflag,
						billpaytransfers.scheduledDate AS scheduledDate
					FROM
						[${dbxschemaname}].billpaytransfers
				UNION
					SELECT 
						p2ptransfers.transactionId AS transactionId,
						p2ptransfers.featureActionId AS featureActionId,
						p2ptransfers.fromAccountNumber AS fromAccountNumber,
						p2ptransfers.amount AS amount,
						p2ptransfers.requestId AS requestId,
						p2ptransfers.createdby AS createdby,
						p2ptransfers.createdts AS createdts,
						p2ptransfers.frequencyTypeId AS frequencyTypeId,
						p2ptransfers.status AS status,
						p2ptransfers.numberOfRecurrences AS numberOfRecurrences,
						CASE 
							WHEN p2ptransfers.payeeName IS NULL OR p2ptransfers.payeeName = '''' THEN 
							CASE
								WHEN p2ptransfers.personId IS NULL OR p2ptransfers.personId = '''' THEN 
									CASE 
										WHEN p2ptransfers.p2pContact IS NULL OR p2ptransfers.p2pContact = '''' THEN p2ptransfers.toAccountNumber
										ELSE p2ptransfers.p2pContact
									END
								ELSE p2ptransfers.personId
							END
							ELSE p2ptransfers.payeeName
						END 
                        AS payeeId,
						p2ptransfers.companyId AS companyId,
		                p2ptransfers.softdeleteflag AS softdeleteflag,
						p2ptransfers.scheduledDate AS scheduledDate
					FROM
						[${dbxschemaname}].p2ptransfers
				UNION
					SELECT 
						wiretransfers.transactionId AS transactionId,
						wiretransfers.featureActionId AS featureActionId,
						wiretransfers.fromAccountNumber AS fromAccountNumber,
						wiretransfers.amount AS amount,
						wiretransfers.requestId AS requestId,
						wiretransfers.createdby AS createdby,
						wiretransfers.createdts AS createdts,
						null AS frequencyTypeId,
						wiretransfers.status AS status,
						null AS numberOfRecurrences,
                        CASE 
							WHEN wiretransfers.payeeName IS NULL OR wiretransfers.payeeName = '''' THEN 
							CASE
								WHEN wiretransfers.payPersonName IS NULL OR wiretransfers.payPersonName = '''' THEN 
									CASE 
										WHEN wiretransfers.payeeId IS NULL OR wiretransfers.payeeId = '''' THEN wiretransfers.payeeAccountNumber
										ELSE wiretransfers.payeeId
									END
								ELSE wiretransfers.payPersonName
							END
							ELSE wiretransfers.payeeName
						END 
                        AS payeeId,
						wiretransfers.companyId AS companyId,
		                wiretransfers.softdeleteflag AS softdeleteflag,
						wiretransfers.createdts AS scheduledDate
					FROM
						[${dbxschemaname}].wiretransfers
				UNION
					SELECT 
						intrabanktransfers.transactionId AS transactionId,
						intrabanktransfers.featureActionId AS featureActionId,
						intrabanktransfers.fromAccountNumber AS fromAccountNumber,
						intrabanktransfers.amount AS amount,
						intrabanktransfers.requestId AS requestId,
						intrabanktransfers.createdby AS createdby,
						intrabanktransfers.createdts AS createdts,
						intrabanktransfers.frequencyTypeId AS frequencyTypeId,
						intrabanktransfers.status AS status,
						intrabanktransfers.numberOfRecurrences AS numberOfRecurrences,
                        CASE 
							WHEN intrabanktransfers.payeeName IS NULL OR intrabanktransfers.payeeName = '''' THEN 
							CASE
								WHEN intrabanktransfers.payPersonName IS NULL OR intrabanktransfers.payPersonName = '''' THEN 
									CASE 
										WHEN intrabanktransfers.personId IS NULL OR intrabanktransfers.personId = '''' THEN intrabanktransfers.toAccountNumber
										ELSE intrabanktransfers.personId
									END
								ELSE intrabanktransfers.payPersonName
							END
							ELSE intrabanktransfers.payeeName
						END 
                        AS payeeId,
						intrabanktransfers.companyId AS companyId,
		                intrabanktransfers.softdeleteflag AS softdeleteflag,
						intrabanktransfers.scheduledDate AS scheduledDate
					FROM
						[${dbxschemaname}].intrabanktransfers
				UNION
					SELECT 
						interbankfundtransfers.transactionId AS transactionId,
						interbankfundtransfers.featureActionId AS featureActionId,
						interbankfundtransfers.fromAccountNumber AS fromAccountNumber,
						interbankfundtransfers.amount AS amount,
						interbankfundtransfers.requestId AS requestId,
						interbankfundtransfers.createdby AS createdby,
						interbankfundtransfers.createdts AS createdts,
						interbankfundtransfers.frequencyTypeId AS frequencyTypeId,
						interbankfundtransfers.status AS status,
						interbankfundtransfers.numberOfRecurrences AS numberOfRecurrences,
						CASE 
							WHEN interbankfundtransfers.payeeName IS NULL OR interbankfundtransfers.payeeName = '''' THEN 
							CASE
								WHEN interbankfundtransfers.payPersonName IS NULL OR interbankfundtransfers.payPersonName = '''' THEN 
									CASE 
										WHEN interbankfundtransfers.personId IS NULL OR interbankfundtransfers.personId = '''' THEN interbankfundtransfers.toAccountNumber
										ELSE interbankfundtransfers.personId
									END
								ELSE interbankfundtransfers.payPersonName
							END
							ELSE interbankfundtransfers.payeeName
						END 
                        AS payeeId,
						interbankfundtransfers.companyId AS companyId,
		                interbankfundtransfers.softdeleteflag AS softdeleteflag,
						interbankfundtransfers.scheduledDate AS scheduledDate
					FROM
						[${dbxschemaname}].interbankfundtransfers
				UNION
					SELECT 
						internationalfundtransfers.transactionId AS transactionId,
						internationalfundtransfers.featureActionId AS featureActionId,
						internationalfundtransfers.fromAccountNumber AS fromAccountNumber,
						internationalfundtransfers.amount AS amount,
						internationalfundtransfers.requestId AS requestId,
						internationalfundtransfers.createdby AS createdby,
						internationalfundtransfers.createdts AS createdts,
						internationalfundtransfers.frequencyTypeId AS frequencyTypeId,
						internationalfundtransfers.status AS status,
						internationalfundtransfers.numberOfRecurrences AS numberOfRecurrences,
						CASE 
							WHEN internationalfundtransfers.payeeName IS NULL OR internationalfundtransfers.payeeName = '''' THEN 
							CASE
								WHEN internationalfundtransfers.payPersonName IS NULL OR internationalfundtransfers.payPersonName = '''' THEN 
									CASE 
										WHEN internationalfundtransfers.personId IS NULL OR internationalfundtransfers.personId = '''' THEN internationalfundtransfers.toAccountNumber
										ELSE internationalfundtransfers.personId
									END
								ELSE internationalfundtransfers.payPersonName 
							END
							ELSE internationalfundtransfers.payeeName
						END 
                        AS payeeId,
						internationalfundtransfers.companyId AS companyId,
		                internationalfundtransfers.softdeleteflag AS softdeleteflag,
						internationalfundtransfers.scheduledDate AS scheduledDate
					FROM
						[${dbxschemaname}].internationalfundtransfers
				UNION
					SELECT 
						ownaccounttransfers.transactionId AS transactionId,
						ownaccounttransfers.featureActionId AS featureActionId,
						ownaccounttransfers.fromAccountNumber AS fromAccountNumber,
						ownaccounttransfers.amount AS amount,
						ownaccounttransfers.requestId AS requestId,
						ownaccounttransfers.createdby AS createdby,
						ownaccounttransfers.createdts AS createdts,
						ownaccounttransfers.frequencyTypeId AS frequencyTypeId,
						ownaccounttransfers.status AS status,
						ownaccounttransfers.numberOfRecurrences AS numberOfRecurrences,
						CASE 
							WHEN ownaccounttransfers.payeeName IS NULL OR ownaccounttransfers.payeeName = '''' THEN 
							CASE
								WHEN ownaccounttransfers.payPersonName IS NULL OR ownaccounttransfers.payPersonName = '''' THEN 
									CASE 
										WHEN ownaccounttransfers.personId IS NULL OR ownaccounttransfers.personId = '''' THEN ownaccounttransfers.toAccountNumber
										ELSE ownaccounttransfers.personId
									END
								ELSE ownaccounttransfers.payPersonName
							END
							ELSE ownaccounttransfers.payeeName
						END 
                        AS payeeId,
						ownaccounttransfers.companyId AS companyId,
		                ownaccounttransfers.softdeleteflag AS softdeleteflag,
						ownaccounttransfers.scheduledDate AS scheduledDate
					FROM
						[${dbxschemaname}].ownaccounttransfers
				) AS generaltransaction
		        LEFT JOIN [${dbxschemaname}].customer ON (generaltransaction.createdby = customer.id)
		        LEFT JOIN [${dbxschemaname}].customeraccounts ON ((generaltransaction.createdby = customeraccounts.Customer_id)
						AND (generaltransaction.fromAccountNumber = customeraccounts.Account_id))
				LEFT JOIN [${dbxschemaname}].organisation ON (generaltransaction.companyId = organisation.id)
		        LEFT JOIN [${dbxschemaname}].bbrequest ON (generaltransaction.requestId = bbrequest.requestId)
		        LEFT JOIN [${dbxschemaname}].featureaction ON (generaltransaction.featureActionId = featureaction.id)
                LEFT JOIN [${dbxschemaname}].feature ON (featureaction.Feature_id = feature.id)
        
        	WHERE generaltransaction.softdeleteflag = 0
				'+@companyQuery+'
				 AND generaltransaction.transactionId LIKE '''+@_transactionId +''' AND generaltransaction.featureActionId LIKE '''+@_featureActionId
				+''' '+@accountsQuery
                +' AND [${dbxschemaname}].FIND_IN_SET(generaltransaction.featureActionId, CAST('''+ @createActions+''' as nvarchar(max))) <> 0 '+
                @queryTypecondition +' '+
                @searchQuery+' ) AS t1
            INNER JOIN
            ( 
				SELECT 
					bbrequest.requestId,
               (select count(DISTINCT([${dbxschemaname}].bbactedrequest.createdby)) from [${dbxschemaname}].bbactedrequest where [${dbxschemaname}].bbactedrequest.action = ''Approved'' AND  [${dbxschemaname}].bbactedrequest.requestId = [${dbxschemaname}].bbrequest.requestId) 
                     as receivedApprovals,
                    ([${dbxschemaname}].LEASTINT((select customerCnt from #approvalCount1 where requestid=bbrequest.requestId ),(select totalCnt from #approvalCount2 where requestid=bbrequest.requestId ))) as requiredApprovals
				FROM
				 [${dbxschemaname}].bbrequest
				LEFT JOIN [${dbxschemaname}].requestapprovalmatrix ON (bbrequest.requestId = requestapprovalmatrix.requestId)
				LEFT JOIN [${dbxschemaname}].approvalmatrix ON (requestapprovalmatrix.approvalMatrixId = approvalmatrix.id)
				LEFT JOIN [${dbxschemaname}].approvalrule ON (approvalmatrix.approvalruleId = approvalrule.id)
                WHERE [${dbxschemaname}].FIND_IN_SET(CAST(bbrequest.requestId as nvarchar(max)), CAST('''+@companyRequestIds+''' as nvarchar(max))) <> 0
				GROUP BY bbrequest.requestId
            ) AS t2
            ON 
            t1.requestId = t2.requestId
            ORDER BY '+ @_sortByParam + ' ' + @_sortOrder + ' ' +
            @paginationQuery

         exec(@select_statement)
		 DROP TABLE #approvalCount1;
		DROP TABLE #approvalCount2;
   END
   MAINLABEL$leave:
   

GO

/****** Object:  StoredProcedure [${dbxschemaname}].[fetch_organisation_customroles_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[fetch_organisation_customroles_proc]  
   @organisationId nvarchar(max),
   @customRoleId nvarchar(max)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

	  DECLARE @select_roles nvarchar(max)

      SET @select_roles = 'SELECT customrole.synctimestamp,customrole.status_id,customrole.softdeleteflag,
	  customrole.parent_id,customrole.organization_id,customrole.name,customrole.modifiedby,customrole.lastmodifiedts,
	  customrole.id,customrole.description,customrole.createdts,customrole.createdby,customer.UserName as userName,
	  membergroup.Name as parentRoleName,status.Description as statusValue FROM 
	  [${dbxschemaname}].customrole LEFT JOIN [${dbxschemaname}].customer ON (customrole.createdby = customer.id) LEFT JOIN [${dbxschemaname}].status 
	  ON (customrole.status_id = status.id) LEFT JOIN [${dbxschemaname}].membergroup ON (customrole.parent_id = membergroup.id) 
	  WHERE customrole.organization_id = ''' + (@organisationId) + (''' and ') + ('customrole.softdeleteflag = 0')

      IF (@customRoleId = '')
         SET @select_roles = (@select_roles)
      ELSE 
         SET @select_roles = (@select_roles) + (' and customrole.id =') + (@customRoleId)

	  EXEC(@select_roles)

   END

GO
/****** Object:  StoredProcedure [${dbxschemaname}].[fetch_request_history_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[fetch_request_history_proc]  
   @_customerId nvarchar(50),
   @_requestId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON
      DECLARE @companyId nvarchar(max)

      SET @companyId = ( SELECT customer.Organization_Id FROM [${dbxschemaname}].customer WHERE customer.id = @_customerId)
      SELECT 
         bbactedrequest.approvalId AS approvalId, 
         bbactedrequest.requestId AS requestId, 
         bbactedrequest.companyId AS companyId, 
         bbactedrequest.createdby AS requestActedby, 
         bbactedrequest.status AS status, 
         bbactedrequest.comments AS comments, 
         bbactedrequest.action AS action, 
         bbactedrequest.createdts AS actionts, 
         bbactedrequest.softdeleteflag AS softdeleteflag, 
         customer.UserName AS userName, 
         CASE 
            WHEN [${dbxschemaname}].bbactedrequest.createdby IS NULL THEN 'System'
            ELSE (CASE 
               WHEN (datalength([${dbxschemaname}].customer.FirstName) <> 0) THEN [${dbxschemaname}].customer.FirstName
               ELSE ''
            END) + ' ' + (CASE 
               WHEN (datalength([${dbxschemaname}].customer.MiddleName) <> 0) THEN [${dbxschemaname}].customer.MiddleName
               ELSE ''
            END) + ' ' + (CASE 
               WHEN (datalength([${dbxschemaname}].customer.LastName) <> 0) THEN [${dbxschemaname}].customer.LastName
               ELSE ''
            END)
         END AS customerName, 
         customer.FullName AS customerFullName
      FROM ([${dbxschemaname}].bbactedrequest 
         LEFT JOIN [${dbxschemaname}].customer 
         ON (bbactedrequest.createdby = customer.id))
      WHERE 
         [${dbxschemaname}].bbactedrequest.softdeleteflag = 0 AND 
         CAST(bbactedrequest.requestId as nvarchar(max))= @_requestId AND 
         bbactedrequest.companyId = @companyId
   END
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[fetch_wiretransfer_details_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[fetch_wiretransfer_details_proc]  
   @_transactionId nvarchar(50),
   @_wireFileExecution_id nvarchar(50),
   @_customerId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
	  declare @companyId nvarchar(max)
      SET @companyId = 
         (
            SELECT customer.Organization_Id
            FROM [${dbxschemaname}].customer
            WHERE customer.id = @_customerId
         )

      IF @_transactionId IS NULL
         SET @_transactionId = N''

      IF @_wireFileExecution_id IS NULL
         SET @_wireFileExecution_id = N''

      IF @companyId IS NULL

         SET @companyId = N''
	  declare @isSMEUser int
	  declare @filterRetail nvarchar(max)
      SET @isSMEUser = iif( @companyId = '' OR @companyId IS NULL, 0, 1);

      SET @filterRetail = 
         CASE 
            WHEN (@_transactionId <> '') THEN (N'transactionId = ') + (@_transactionId) + (N' AND createdby = ') + (@_customerId)
            ELSE 
               CASE 
                  WHEN (@_wireFileExecution_id <> '') THEN (N'wireFileExecution_id = ') + (@_wireFileExecution_id) + (N' AND createdby = ') + (@_customerId)
                  ELSE N''
               END
         END
	  declare @filterSME nvarchar(max)
      SET @filterSME = 
         CASE 
            WHEN (@_transactionId <> '') THEN (N'transactionId = ') + (@_transactionId) + (N' AND companyId = ') + (@companyId)
            ELSE 
               CASE 
                  WHEN (@_wireFileExecution_id <> '') THEN (N'wireFileExecution_id = ') + (@_wireFileExecution_id) + (N' AND companyId = ') + (@companyId)
                  ELSE N''
               END
         END
		declare @filter nvarchar(max)
      SET @filter = 
         CASE 
            WHEN (@isSMEUser = 0) THEN @filterRetail
            ELSE @filterSME
         END
	  declare @select_statement nvarchar(max)
      SET @select_statement = (N'SELECT '+N'wiretransfers.transactionId,'+N'wiretransfers.featureActionId,'+N'        wiretransfers.confirmationNumber,'+N'        wiretransfers.companyId,'+N'        wiretransfers.createdby,'+N'        wiretransfers.requestId,'+N'        wiretransfers.status,'+N'        wiretransfers.onetime_id,'+N'        wiretransfers.wireFileExecution_id,'+N'        wiretransfers.notes,'+N'        wiretransfers.amount,'+N'        wiretransfers.fromAccountNumber,'+N'        wiretransfers.payeeAccountNumber,'+N'        wiretransfers.transactionType,'+N'        wiretransfers.payeeId,'+N'        wiretransfers.payeeCurrency,'+N'        onetimepayee.payeeName,'
	  +N'onetimepayee.payeeNickName,'+N'onetimepayee.payeeType,'+N'onetimepayee.wireAccountType,'+N'onetimepayee.swiftCode,'+N'onetimepayee.routingNumber,'+N'onetimepayee.zipCode,'+N'onetimepayee.cityName,'+N'onetimepayee.state,'+N'onetimepayee.country,'+N'onetimepayee.payeeAddressLine1,'+N'onetimepayee.payeeAddressLine2,'+N'onetimepayee.bankName,'+N'onetimepayee.internationalRoutingCode,'+N'onetimepayee.bankAddressLine1,'+N'onetimepayee.bankAddressLine2,'+N'onetimepayee.bankCity,'+N'onetimepayee.bankState,'+N'onetimepayee.bankZip'+N'        '+N'    FROM'+N'        ([${dbxschemaname}].wiretransfers'+N' LEFT JOIN [${dbxschemaname}].onetimepayee ON (wiretransfers.onetime_id = onetimepayee.onetime_id))'+N' WHERE ') + (@filter)

	  EXEC(@select_statement)


   END
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[get_alert_sub_types]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[get_alert_sub_types]  
   @_alertTypes nvarchar(max)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT alertsubtype.id AS alertSubType
      FROM [${dbxschemaname}].alertsubtype
      WHERE [${dbxschemaname}].FIND_IN_SET(alertsubtype.AlertTypeId, @_alertTypes) <> 0

   END
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[get_AlertTypes]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[get_AlertTypes]  
   @_alertTypes nvarchar(max)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT 
         dbxalerttype.id AS id, 
         dbxalerttype.AttributeId AS attributeId, 
         dbxalerttype.AlertConditionId AS alertConditionId, 
         dbxalerttype.Value1 AS value1, 
         dbxalerttype.Value2 AS value2, 
         dbxalerttype.IsGlobal AS isGlobal
      FROM [${dbxschemaname}].dbxalerttype
      WHERE [${dbxschemaname}].FIND_IN_SET(dbxalerttype.id, @_alertTypes) <> 0


   END
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[get_backendidentifiers_for_customerids]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[get_backendidentifiers_for_customerids]  
   @_customerIds nvarchar(max)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON


      SELECT backendidentifier.Customer_id AS customerId, backendidentifier.BackendId AS backendId
      FROM [${dbxschemaname}].backendidentifier
      WHERE [${dbxschemaname}].FIND_IN_SET(backendidentifier.Customer_id, @_customerIds) <> 0

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[get_customer_applicantinfo_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[get_customer_applicantinfo_proc]  
   /*
   *   SSMA informational messages:
   *   M2SS0055: Data type was converted to VARCHAR(MAX) according to character set mapping for latin1 character set
   */

   @Customer_id varchar(max)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT 
         customer.id AS Customer_id, 
         customer.FirstName AS FirstName, 
         customer.MiddleName AS MiddleName, 
         customer.LastName AS LastName, 
         customer.DateOfBirth AS DateOfBirth, 
         customer.Ssn AS Ssn, 
         customer.PreferredContactMethod AS PreferredContactMethod, 
         
            (
               SELECT TOP (1) customercommunication.Value
               FROM [${dbxschemaname}].customercommunication
               WHERE 
                  customercommunication.Type_id = 'COMM_TYPE_PHONE' AND 
                  customercommunication.isPrimary = 1 AND 
                  CAST(CAST(customercommunication.Customer_id AS varchar(max)) AS varbinary(max)) = CAST(@Customer_id AS varbinary(max))
            ) AS Phone, 
         
            (
               SELECT TOP (1) customercommunication.Value
               FROM [${dbxschemaname}].customercommunication
               WHERE 
                  customercommunication.Type_id = 'COMM_TYPE_EMAIL' AND 
                  customercommunication.isPrimary = 1 AND 
                  CAST(CAST(customercommunication.Customer_id AS varchar(max)) AS varbinary(max)) = CAST(@Customer_id AS varbinary(max))
            ) AS Email
      FROM [${dbxschemaname}].customer  AS customer
      WHERE CAST(CAST(customer.id AS varchar(max)) AS varbinary(max)) = CAST(@Customer_id AS varbinary(max))

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[get_customerAlertEntitlementForAlertTypes]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [${dbxschemaname}].[get_customer_employement_details_proc]  

   @Customer_id varchar(max)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT TOP (1) 
         employementdetails.Customer_id AS Customer_id, 
         employementdetails.EmploymentType AS EmploymentType, 
         employementdetails.CurrentEmployer AS CurrentEmployer, 
         employementdetails.Designation AS Designation, 
         employementdetails.PayPeriod AS PayPeriod, 
         employementdetails.GrossIncome AS GrossIncome, 
         employementdetails.WeekWorkingHours AS WeekWorkingHours, 
         employementdetails.EmploymentStartDate AS EmployementStartDate, 
         employementdetails.PreviousEmployer AS PreviousEmployer, 
         employementdetails.OtherEmployementType AS OtherEmployementType, 
         employementdetails.OtherEmployementDescription AS OtherEmployementDescription, 
         employementdetails.PreviousDesignation AS PreviousDesignation, 
         othersourceofincome.SourceType AS OtherIncomeSourceType, 
         othersourceofincome.PayPeriod AS OtherIncomeSourcePayPeriod, 
         othersourceofincome.GrossIncome AS OtherGrossIncomeValue, 
         othersourceofincome.WeekWorkingHours AS OtherIncomeSourceWorkingHours, 
         othersourceofincome.SourceofIncomeDescription AS OtherSourceOfIncomeDescription, 
         othersourceofincome.SourceOfIncomeName AS OtherSourceOfIncomeName, 
         customeraddress.Address_id AS Address_id, 
         customeraddress.Type_id AS Type_id, 
         address.Region_id AS Region_id, 
         address.City_id AS City_id, 
         address.addressLine1 AS AddressLine1, 
         address.addressLine2 AS AddressLine2, 
         address.addressLine3 AS AddressLine3, 
         address.zipCode AS ZipCode, 
         address.state AS State, 
         address.cityName AS city, 
         address.country AS Country
      FROM 
         [${dbxschemaname}].employementdetails  AS employementdetails 
            LEFT JOIN [${dbxschemaname}].othersourceofincome  AS othersourceofincome 
            ON employementdetails.Customer_id = othersourceofincome.Customer_id 
            LEFT JOIN [${dbxschemaname}].customeraddress  AS customeraddress 
            ON (customeraddress.Customer_id = employementdetails.Customer_id AND customeraddress.Type_id = 'ADR_TYPE_WORK') 
            INNER JOIN [${dbxschemaname}].address  AS address 
            ON address.id = customeraddress.Address_id
      WHERE employementdetails.Customer_id = @Customer_id

   END
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[get_customerAlertEntitlementForAlertTypes]  
	   @_alertTypes nvarchar(max)
	AS 
	   BEGIN

		  SET  XACT_ABORT  ON

		  SET  NOCOUNT  ON

		  SELECT 
			 dbxcustomeralertentitlement.AlertTypeId AS alertTypeId, 
			 dbxcustomeralertentitlement.Customer_id AS customerId, 
			 dbxcustomeralertentitlement.Value1 AS value1, 
			 dbxcustomeralertentitlement.Value2 AS value2, 
			 dbxcustomeralertentitlement.AccountId AS accountId
		  FROM [${dbxschemaname}].dbxcustomeralertentitlement
		  WHERE [${dbxschemaname}].FIND_IN_SET(dbxcustomeralertentitlement.AlertTypeId, @_alertTypes) <> 0

	   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[get_customerids_for_backendidentifiers]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[get_customerids_for_backendidentifiers]  
   @_backendIds nvarchar(max)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT backendidentifier.Customer_id AS customerId, backendidentifier.BackendId AS backendId
      FROM [${dbxschemaname}].backendidentifier
      WHERE [${dbxschemaname}].FIND_IN_SET(backendidentifier.BackendId, @_backendIds) <> 0

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[get_entitlements_for_customerids_and_alerttypes]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[get_entitlements_for_customerids_and_alerttypes]  
   @_alertTypes nvarchar(max),
   @_customerIds nvarchar(max)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT 
         dbxcustomeralertentitlement.AlertTypeId AS alertTypeId, 
         dbxcustomeralertentitlement.Customer_id AS customerId, 
         dbxcustomeralertentitlement.Value1 AS value1, 
         dbxcustomeralertentitlement.Value2 AS value2, 
         dbxcustomeralertentitlement.AccountId AS accountId
      FROM [${dbxschemaname}].dbxcustomeralertentitlement
      WHERE [${dbxschemaname}].FIND_IN_SET(dbxcustomeralertentitlement.AlertTypeId, @_alertTypes) <> 0 AND [${dbxschemaname}].FIND_IN_SET(dbxcustomeralertentitlement.Customer_id, @_customerIds) <> 0

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[get_filtered_companies_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[get_filtered_companies_proc]  
   @_searchText nvarchar(60)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT TOP (20) 
         organisation.id, 
         organisation.Type_Id, 
         organisation.Name, 
         organisation.Description, 
         organisation.BusinessType_id, 
         organisation.StatusId, 
         organisation.FaxId, 
         organisation.createdby, 
         organisation.createdts, 
         organisation.rejectedby, 
         organisation.rejectedts, 
         organisation.rejectedReason
      FROM [${dbxschemaname}].organisation
      WHERE organisation.Name LIKE N'%' + @_searchText + N'%'
         ORDER BY organisation.Name

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[get_monetary_actions_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[get_monetary_actions_proc]  
   @_featureActions nvarchar(max)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

	  DECLARE @monetaryActionList nvarchar(max)
      SET @monetaryActionList = 
         (

            SELECT String_agg(CAST(id as nvarchar(max)),',') 
            FROM [${dbxschemaname}].featureaction
            WHERE (featureaction.Type_id = 'MONETARY' AND [${dbxschemaname}].FIND_IN_SET(featureaction.id, @_featureActions) <> 0)

         )

      SELECT @monetaryActionList AS monetaryActions

   END

GO
CREATE PROCEDURE [${dbxschemaname}].[get_organisation_employees_proc]  
   @_organisationId nvarchar(50),
   @_filterColumnName nvarchar(50),
   @_filterColumnValue nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON
	  DECLARE @org_employees_list nvarchar(max)
	  DECLARE @customersList nvarchar(max)
	  

      SET @org_employees_list = 
         (
            SELECT String_Agg(CAST(Customer_id as nvarchar(max)) , ',') 
            FROM [${dbxschemaname}].organisationemployees
            WHERE (organisationemployees.Organization_id = @_organisationId)
         )
		 
      SET @org_employees_list = 
         CASE 
            WHEN @org_employees_list IS NULL  THEN ''
            ELSE @org_employees_list
         END

      IF @_filterColumnName = 'Ssn'
         BEGIN

            SET @customersList = 
               (

                  SELECT String_Agg(CAST(id  as nvarchar(max)) , ',')
                  FROM [${dbxschemaname}].customer
                  WHERE [${dbxschemaname}].FIND_IN_SET(customer.id, @org_employees_list) <> 0 AND [${dbxschemaname}].FIND_IN_SET(customer.Ssn, @_filterColumnValue) <> 0


               )

            SET @customersList = 
               CASE 
                  WHEN (@customersList IS NULL) THEN N''
                  ELSE @customersList
               END

         END

      SELECT @customersList AS employeesList
   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[get_valid_orgaccounts_list_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[get_valid_orgaccounts_list_proc]  
   @_accountsList nvarchar(max)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
	  declare @list nvarchar(max)
      SET @list = 
         (
            SELECT @_accountsList
         )
		declare @NONDBXAccounts varchar(max)
      SET @NONDBXAccounts = N''
	  declare @tableAccountsList nvarchar(max)
      SET @tableAccountsList = 
         (

            SELECT STRING_AGG(CAST(accounts.Account_id as nvarchar(max)) , ',')
            FROM [${dbxschemaname}].accounts
 
         )
		 declare @next bigint
		 declare @nextlen bigint
		 declare @value nvarchar(max)

      WHILE (1 = 1)
      
         BEGIN


            IF LEN(LTRIM(RTRIM(@list))) = 0 OR @list IS NULL
               BREAK
  
            SET @next = [${dbxschemaname}].substring_index(@list, N',', 1)

            SET @nextlen = LEN(@next)

            SET @value = LTRIM(RTRIM(@next))

            IF [${dbxschemaname}].FIND_IN_SET(@value, @tableAccountsList) = 0
    
               IF @NONDBXAccounts = 0
        
                  SET @NONDBXAccounts = @value
                 
               ELSE 
              
                  SET @NONDBXAccounts = (@NONDBXAccounts) + (N',') + (@value)
           
            SET @list = STUFF(@list, 1, @nextlen + 1, N'')
         
         END
		 declare @DBXAccounts nvarchar(max)
      SET @DBXAccounts = 
         (
     
            SELECT STRING_AGG(CAST(accounts.Account_id as nvarchar(max)) , ',')
            FROM [${dbxschemaname}].accounts
            WHERE [${dbxschemaname}].FIND_IN_SET(Account_id, @_accountsList) <> 0 AND (
               CASE 
                  WHEN (Organization_id) IS NULL THEN 1
                  ELSE 0
               END > 0 OR Organization_id = '')
        
         )
 
      IF (
         CASE 
            WHEN (@DBXAccounts) IS NULL THEN 1
            ELSE 0
         END <> 0 OR @DBXAccounts = '')
     
         SELECT @NONDBXAccounts AS accountsList
       
      ELSE 

         IF (
            CASE 
               WHEN (@NONDBXAccounts) IS NULL THEN 1
               ELSE 0
            END <> 0 OR @NONDBXAccounts = '')
           
            SELECT @DBXAccounts AS accountsList
            

         ELSE 
         
            SELECT (@DBXAccounts) + (N',') + (@NONDBXAccounts) AS accountsList
    
   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[getAllBackendIdentifiers]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[getAllBackendIdentifiers]
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT backendidentifier.Customer_id AS customerId, backendidentifier.BackendId AS backendId
      FROM [${dbxschemaname}].backendidentifier

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[getCustomerCommunicationData]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[getCustomerCommunicationData]  
   @_customers nvarchar(max)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT customercommunication.Customer_id AS custid, customercommunication.Value AS value, customercommunication.Type_id AS type
      FROM [${dbxschemaname}].customercommunication
      WHERE customercommunication.isPrimary = 1 AND [${dbxschemaname}].FIND_IN_SET(customercommunication.Customer_id, @_customers) <> 0

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[getCustomerInfo]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[getCustomerInfo]  
   @_customers nvarchar(max)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT customer.id AS custid, customer.FirstName AS fname, customer.LastName AS lname, customer.CountryCode AS country
      FROM [${dbxschemaname}].customer
      WHERE [${dbxschemaname}].FIND_IN_SET(customer.id, @_customers) <> 0
 
   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[getCustomersIdFromCoreId]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[getCustomersIdFromCoreId]  
   @_corecustomers nvarchar(max)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT backendidentifier.Customer_id AS custid, backendidentifier.BackendId AS corecustid
      FROM [${dbxschemaname}].backendidentifier
      WHERE [${dbxschemaname}].FIND_IN_SET(backendidentifier.BackendId, @_corecustomers) <> 0
 
   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[getRequestApprovers_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[getRequestApprovers_proc]  
   @_requestId nvarchar(max),
   @_status nvarchar(max)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      IF @_status IS NULL OR @_status = ''
         /*
         *   SSMA warning messages:
         *   M2SS0104: Non aggregated column REQUESTID is aggregated with Min(..) in Select, Orderby and Having clauses.
         *   M2SS0104: Non aggregated column FIRSTNAME is aggregated with Min(..) in Select, Orderby and Having clauses.
         *   M2SS0104: Non aggregated column LASTNAME is aggregated with Min(..) in Select, Orderby and Having clauses.
         */

         SELECT min(bb.requestId) AS requestId, cam.customerId AS approvers, min(c.FirstName) AS FirstName, min(c.LastName) AS LastName
         FROM 
            [${dbxschemaname}].bbrequest  AS bb 
               CROSS JOIN [${dbxschemaname}].requestapprovalmatrix  AS ram 
               CROSS JOIN [${dbxschemaname}].customerapprovalmatrix  AS cam 
               CROSS JOIN [${dbxschemaname}].customer  AS c
         WHERE 
            bb.requestId = ram.requestId AND 
            ram.approvalMatrixId = cam.approvalMatrixId AND 
            cam.customerId = c.id AND 
            CAST(bb.requestId AS nvarchar(max))= @_requestId
         GROUP BY cam.customerId
            ORDER BY cam.customerId
      ELSE 
         /*
         *   SSMA warning messages:
         *   M2SS0104: Non aggregated column FIRSTNAME is aggregated with Min(..) in Select, Orderby and Having clauses.
         *   M2SS0104: Non aggregated column LASTNAME is aggregated with Min(..) in Select, Orderby and Having clauses.
         */

         SELECT bb.createdby AS approvers, min(c.FirstName) AS FirstName, min(c.LastName) AS LastName
         FROM 
            [${dbxschemaname}].bbactedrequest  AS bb 
               CROSS JOIN [${dbxschemaname}].customer  AS c
         WHERE 
            bb.createdby = c.id AND 
            CAST(bb.requestId AS nvarchar(max)) = @_requestId AND 
            bb.status = @_status
         GROUP BY bb.createdby
            ORDER BY bb.createdby

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[group_actions_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[group_actions_proc]  
   @_groupId nvarchar(50),
   @_actionType nvarchar(50),
   @_actionId nvarchar(50),
   @_isOnlyPremissions varchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
	  declare @select_statement nvarchar(max)

	  IF (@_isOnlyPremissions = 'true')
		select DISTINCT [groupactionlimit].[Action_id] AS actionId FROM [${dbxschemaname}].[groupactionlimit] where [groupactionlimit].[Group_id] = @_groupId;
	ELSE
	 BEGIN

      SET @select_statement = (N'SELECT'+NCHAR(13)+NCHAR(10)+N'    groupactionlimit.Group_id AS groupId,'+NCHAR(13)+NCHAR(10)+N'    groupactionlimit.LimitType_id AS limitTyeId,'+NCHAR(13)+NCHAR(10)+N'    groupactionlimit.value AS value,'+NCHAR(13)+NCHAR(10)+N'    featureaction.id AS actionId,'+NCHAR(13)+NCHAR(10)+NCHAR(9)+N'featureaction.Type_id AS actionType,'+NCHAR(13)+NCHAR(10)+NCHAR(9)+N'featureaction.name AS actionName,'+NCHAR(13)+NCHAR(10)+NCHAR(9)+N'featureaction.description AS actionDescription,'+NCHAR(9)+NCHAR(13)+NCHAR(10)+N'    feature.id AS featureId'+NCHAR(13)+NCHAR(10)+N'FROM'+NCHAR(13)+NCHAR(10)+N'    ([${dbxschemaname}].groupactionlimit'+NCHAR(13)+NCHAR(10)+N'    LEFT JOIN [${dbxschemaname}].featureaction ON (featureaction.id = groupactionlimit.Action_id)'+NCHAR(13)+NCHAR(10)+N'    LEFT JOIN [${dbxschemaname}].feature ON (feature.id = featureaction.Feature_id))'+NCHAR(13)+NCHAR(10)+N'    where feature.Status_id = ''SID_FEATURE_ACTIVE'' and groupactionlimit.Group_id=') + ((QUOTENAME((@_groupId), '''')))
  
      IF (@_actionType <> '')

         SET @select_statement = (@select_statement) + (N' and featureaction.Type_id = ') + ((QUOTENAME((@_actionType), '''')))


      IF (@_actionId <> '')

         SET @select_statement = (@select_statement) + (N' and featureaction.id = ') + ((QUOTENAME((@_actionId), '''')))

	   EXEC(@select_statement)
	   END

   END
/****** Object:  StoredProcedure [${dbxschemaname}].[lead_assign_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/
CREATE PROCEDURE [${dbxschemaname}].[internal_user_access_on_given_customer_proc]
@_loggedinSystemUserId  nvarchar(50) ,
@_customerId  nvarchar(50) ,
@_customerUsername  nvarchar(50) 

AS
      SET  XACT_ABORT  ON
	  DECLARE @isCustomerAccessible varchar(10);
      SET  NOCOUNT  On
if(@_customerId = '' and @_customerUsername != '')
	SET @_customerId = (SELECT id from [${dbxschemaname}].customer where UserName = @_customerUsername)

if @_customerId in (SELECT customergroup.Customer_id FROM [${dbxschemaname}].systemuser 
LEFT JOIN [${dbxschemaname}].userrole ON userrole.User_id = systemuser.id
LEFT JOIN [${dbxschemaname}].userrolecustomerrole ON userrolecustomerrole.UserRole_id = userrole.Role_id
LEFT JOIN [${dbxschemaname}].customergroup ON customergroup.Group_id = userrolecustomerrole.CustomerRole_id
WHERE id = @_loggedinSystemUserId)
	select 'true' as isCustomerAccessible
ELSE  
	select 'false' as isCustomerAccessible
GO

CREATE PROCEDURE [${dbxschemaname}].[lead_archive_proc]  
   @_leadId nvarchar(50),
   @_leadClosureReason nvarchar(50),
   @_modifiedby nvarchar(50)
AS 

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DECLARE
         @lead_note_cursor_done bit = 0x0

      DECLARE


         @curr_leadnote_id varchar(50) = ''

      DECLARE
         @lead_note_count int = 0

      INSERT [${dbxschemaname}].archivedlead(
         id, 
         firstName, 
         middleName, 
         lastName, 
         salutation, 
         isCustomer, 
         customerId, 
         product_id, 
         csr_id, 
         status_id, 
         countryCode, 
         phoneNumber, 
         extension, 
         email, 
         closureReason, 
         createdby, 
         modifiedby, 
         createdts, 
         lastmodifiedts, 
         synctimestamp, 
         softdeleteflag)
         SELECT 
            lead.id, 
            lead.firstName, 
            lead.middleName, 
            lead.lastName, 
            lead.salutation, 
            lead.isCustomer, 
            lead.customerId, 
            lead.product_id, 
            lead.csr_id, 
            lead.status_id, 
            lead.countryCode, 
            lead.phoneNumber, 
            lead.extension, 
            lead.email, 
            lead.closureReason, 
            lead.createdby, 
            lead.modifiedby, 
            lead.createdts, 
            lead.lastmodifiedts, 
            lead.synctimestamp, 
            lead.softdeleteflag
         FROM [${dbxschemaname}].lead
         WHERE lead.id = @_leadId

      UPDATE [${dbxschemaname}].archivedlead
         SET 
            Status_id = 'SID_ARCHIVED', 
            closureReason = @_leadClosureReason, 
            lastmodifiedts = isnull(getdate(), getdate()), 
            modifiedby = @_modifiedby
      WHERE archivedlead.id = @_leadId

      SELECT @_leadId


         DECLARE
             leadnote_cursor CURSOR LOCAL FORWARD_ONLY FOR 
               SELECT leadnote.id
               FROM [${dbxschemaname}].leadnote
               WHERE leadnote.lead_Id = @_leadId

         OPEN leadnote_cursor

         WHILE (1 = 1)
         
            BEGIN

               FETCH leadnote_cursor
                   INTO @curr_leadnote_id

               IF @@FETCH_STATUS <> 0


                  SET @lead_note_cursor_done = 0x1

               IF NOT @lead_note_cursor_done <> 0
                  BEGIN

                     INSERT [${dbxschemaname}].archivedleadnote(
                        id, 
                        lead_Id, 
                        note, 
                        createdby, 
                        modifiedby, 
                        createdts, 
                        lastmodifiedts, 
                        synctimestamp, 
                        softdeleteflag)
                        SELECT 
                           leadnote.id, 
                           leadnote.lead_Id, 
                           leadnote.note, 
                           leadnote.createdby, 
                           leadnote.modifiedby, 
                           leadnote.createdts, 
                           leadnote.lastmodifiedts, 
                           leadnote.synctimestamp, 
                           leadnote.softdeleteflag
                        FROM [${dbxschemaname}].leadnote
                        WHERE leadnote.id = @curr_leadnote_id

                     DELETE 
                     FROM [${dbxschemaname}].leadnote
                     WHERE leadnote.id = @curr_leadnote_id

                  END

               IF @lead_note_cursor_done <> 0
                  BREAK

            END

         SET @lead_note_cursor_done = 0x0

         CLOSE leadnote_cursor

         DEALLOCATE leadnote_cursor

      DELETE 
      FROM [${dbxschemaname}].lead
      WHERE lead.id = @_leadId

      SET @lead_note_cursor_done = 0x0
GO

CREATE PROCEDURE [${dbxschemaname}].[lead_assign_proc]  
   @_leadIds nvarchar(max),
   @_csrID nvarchar(50),
   @_modifiedby nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
	  declare @updateStatement nvarchar(max)
      SET @updateStatement = 
         (N'UPDATE lead SET csr_id = ''')
          + 
         (@_csrID)
          + 
         (N''', modifiedby = ''')
          + 
         (@_modifiedby)
          + 
         (N''', lastmodifiedts = CURRENT_TIMESTAMP where id in (')
          + 
         ([${dbxschemaname}].func_escape_input_for_in_operator(@_leadIds))
          + 
         (N')')
	  execute (@updateStatement)
 
   END
GO
CREATE PROCEDURE [${dbxschemaname}].[lead_auto_archive_proc]
AS 

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DECLARE
         @lead_cursor_done bit = 0x0

      DECLARE
         @lead_note_cursor_done bit = 0x0

      DECLARE
         @curr_lead_id varchar(50) = ''

      DECLARE
         @curr_leadnote_id varchar(50) = ''

      DECLARE
         @lead_note_count int = 0

      DECLARE
          lead_cursor CURSOR LOCAL FORWARD_ONLY FOR 
            SELECT lead.id
            FROM [${dbxschemaname}].lead
            WHERE datediff(second, lead.lastmodifiedts, getdate()) >= (
               (
                  SELECT systemconfiguration.PropertyValue
                  FROM [${dbxschemaname}].systemconfiguration
                  WHERE systemconfiguration.PropertyName = 'LEAD_AUTO_ARCHIVE_PERIOD_IN_DAYS'
               ) * 24 * 60 * 60)

      OPEN lead_cursor
      WHILE (1 = 1)
      
         BEGIN

            FETCH lead_cursor
                INTO @curr_lead_id

            IF @@FETCH_STATUS <> 0
               SET @lead_cursor_done = 0x1

               SET @lead_note_cursor_done = 0x1

            IF NOT @lead_cursor_done <> 0
               BEGIN

                  INSERT [${dbxschemaname}].archivedlead(
                     id, 
                     firstName, 
                     middleName, 
                     lastName, 
                     salutation, 
                     isCustomer, 
                     customerId, 
                     product_id, 
                     csr_id, 
                     status_id, 
                     countryCode, 
                     phoneNumber, 
                     extension, 
                     email, 
                     closureReason, 
                     createdby, 
                     modifiedby, 
                     createdts, 
                     lastmodifiedts, 
                     synctimestamp, 
                     softdeleteflag)
                     SELECT 
                        lead.id, 
                        lead.firstName, 
                        lead.middleName, 
                        lead.lastName, 
                        lead.salutation, 
                        lead.isCustomer, 
                        lead.customerId, 
                        lead.product_id, 
                        lead.csr_id, 
                        lead.status_id, 
                        lead.countryCode, 
                        lead.phoneNumber, 
                        lead.extension, 
                        lead.email, 
                        lead.closureReason, 
                        lead.createdby, 
                        lead.modifiedby, 
                        lead.createdts, 
                        lead.lastmodifiedts, 
                        lead.synctimestamp, 
                        lead.softdeleteflag
                     FROM [${dbxschemaname}].lead
                     WHERE lead.id = @curr_lead_id

                  UPDATE [${dbxschemaname}].archivedlead
                     SET 
                        Status_id = 'SID_ARCHIVED', 
                        closureReason = N'Automatically archived'
                  WHERE archivedlead.id = @curr_lead_id

                  SELECT @curr_lead_id
                  BEGIN

                     DECLARE
                         leadnote_cursor CURSOR LOCAL FORWARD_ONLY FOR 
                           SELECT leadnote.id
                           FROM [${dbxschemaname}].leadnote
                           WHERE leadnote.lead_Id = @curr_lead_id

                     OPEN leadnote_cursor
                     WHILE (1 = 1)
                     
                        BEGIN

                           FETCH leadnote_cursor
                               INTO @curr_leadnote_id

                           IF @@FETCH_STATUS <> 0
                              SET @lead_cursor_done = 0x1

                              SET @lead_note_cursor_done = 0x1

                           IF NOT @lead_note_cursor_done <> 0
                              BEGIN

                                 INSERT [${dbxschemaname}].archivedleadnote(
                                    id, 
                                    lead_Id, 
                                    note, 
                                    createdby, 
                                    modifiedby, 
                                    createdts, 
                                    lastmodifiedts, 
                                    synctimestamp, 
                                    softdeleteflag)
                                    SELECT 
                                       leadnote.id, 
                                       leadnote.lead_Id, 
                                       leadnote.note, 
                                       leadnote.createdby, 
                                       leadnote.modifiedby, 
                                       leadnote.createdts, 
                                       leadnote.lastmodifiedts, 
                                       leadnote.synctimestamp, 
                                       leadnote.softdeleteflag
                                    FROM [${dbxschemaname}].leadnote
                                    WHERE leadnote.id = @curr_leadnote_id

                                 DELETE 
                                 FROM [${dbxschemaname}].leadnote
                                 WHERE leadnote.id = @curr_leadnote_id

                              END

                           IF @lead_note_cursor_done <> 0
                              BREAK

                        END


                     SET @lead_cursor_done = 0x0


                     SET @lead_note_cursor_done = 0x0

                     CLOSE leadnote_cursor

                     DEALLOCATE leadnote_cursor

                  END

                  DELETE 
                  FROM [${dbxschemaname}].lead
                  WHERE lead.id = @curr_lead_id

               END

            IF @lead_cursor_done <> 0
               BREAK

         END


      SET @lead_cursor_done = 0x0

      SET @lead_note_cursor_done = 0x0

      CLOSE lead_cursor

      DEALLOCATE lead_cursor
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[lead_notes_search_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[lead_notes_search_proc]  
   @_leadId nvarchar(50),
   @_leadStatusId nvarchar(50),
   @_sortOrder nvarchar(50),
   @_sortCriteria nvarchar(50),
   @_offset bigint,
   @_recordsPerPage bigint
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
	   declare @tableName nvarchar(max)
      SET @tableName = N'[${dbxschemaname}].leadnote'

      IF @_leadStatusId = 'SID_ARCHIVED'
 
         SET @tableName = N'[${dbxschemaname}].archivedleadnote'
		 declare @queryStatement nvarchar(max)
      SET @queryStatement = 
         (NCHAR(13)+NCHAR(10)+N'SELECT  ')
          + 
         (@tableName)
          + 
         (N'.id AS id,')
          + 
         (@tableName)
          + 
         (N'.lead_Id AS lead_Id,')
          + 
         (@tableName)
          + 
         (N'.note AS note,'+NCHAR(13)+NCHAR(10)+N'su1.FirstName AS createdByFirstName,'+NCHAR(13)+NCHAR(10)+N'su1.MiddleName AS createdByMiddleName,'+NCHAR(13)+NCHAR(10)+N'su1.LastName AS createdByLastName,'+NCHAR(13)+NCHAR(10)+N'su2.FirstName AS modifiedbyFirstName,'+NCHAR(13)+NCHAR(10)+N'su2.MiddleName AS modifiedbyMiddleName,'+NCHAR(13)+NCHAR(10)+N'su2.LastName AS modifiedbyLastName,')
          + 
         (@tableName)
          + 
         (N'.createdby AS createdby,')
          + 
         (@tableName)
          + 
         (N'.modifiedby AS modifiedby,')
          + 
         (@tableName)
          + 
         (N'.createdts AS createdts,')
          + 
         (@tableName)
          + 
         (N'.lastmodifiedts AS lastmodifiedts,')
          + 
         (@tableName)
          + 
         (N'.synctimestamp AS synctimestamp,')
          + 
         (@tableName)
          + 
         (N'.softdeleteflag AS softdeleteflag'+NCHAR(13)+NCHAR(10)+N'FROM (')
          + 
         (@tableName)
          + 
         (N''+NCHAR(13)+NCHAR(10)+N'        LEFT JOIN systemuser su1 ON ')
          + 
         (@tableName)
          + 
         (N'.createdby = su1.id'+NCHAR(13)+NCHAR(10)+N'        LEFT JOIN systemuser su2 ON ')
          + 
         (@tableName)
          + 
         (N'.modifiedby = su2.id'+NCHAR(13)+NCHAR(10)+N' ) where true')
   
      IF @_leadId <> ''
   
         SET @queryStatement = (@queryStatement) + (N' and ') + (@tableName) + (N'.lead_Id = ') + ((QUOTENAME((@_leadId), '''')))
        

      IF @_sortOrder <> 'asc' OR @_sortOrder <> 'desc'
         SET @_sortOrder = N'desc'

      IF @_sortCriteria = ''
         SET @_sortCriteria = N'lastmodifiedts'

 
      SET @queryStatement = 
         (@queryStatement)
          + 
         (N' order by ')
          + 
         (@tableName)
          + 
         (N'.')
          + 
         (@_sortCriteria)
          + 
         (N' ')
          + 
         (@_sortOrder)

      SET @queryStatement = (@queryStatement) + (N' OFFSET ') + (@_offset) + (N' ROWS FETCH NEXT ') + (@_recordsPerPage) + (N' ROWS ONLY ') 
      EXEC(@queryStatement)



   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[lead_search_count_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[lead_search_count_proc]  
   @_productId nvarchar(50),
   @_productType nvarchar(50),
   @_customerId nvarchar(50),
   @_leadType nvarchar(50),
   @_statusIds nvarchar(50),
   @_assignedCSRID nvarchar(50),
   @_modifiedStartDate nvarchar(50),
   @_modifiedEndDate nvarchar(50),
   @_phoneNumber nvarchar(50),
   @_emailAddress nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
	  declare @tableName nvarchar(max)
	  declare @queryStatement nvarchar(max)

      SET @tableName = N'[${dbxschemaname}].lead'

      IF @_statusIds = 'SID_ARCHIVED'

         SET @tableName = N'[${dbxschemaname}].archivedlead'

      SET @queryStatement = 
         (NCHAR(13)+NCHAR(10)+N' SELECT '+NCHAR(13)+NCHAR(10)+N'count(')
          + 
         (@tableName)
          + 
         (N'.id) AS count FROM'+NCHAR(13)+NCHAR(10)+N'        (')
          + 
         (@tableName)
          + 
         (N''+NCHAR(13)+NCHAR(10)+N'        LEFT JOIN [${dbxschemaname}].systemuser su1 ON ')
          + 
         (@tableName)
          + 
         (N'.csr_id = su1.id'+NCHAR(13)+NCHAR(10)+NCHAR(9)+NCHAR(9)+N'LEFT JOIN [${dbxschemaname}].product ON ')
          + 
         (@tableName)
          + 
         (N'.product_id = product.id'+NCHAR(13)+NCHAR(10)+N'        LEFT JOIN [${dbxschemaname}].systemuser su2 ON ')
          + 
         (@tableName)
          + 
         (N'.createdby = su2.id'+NCHAR(13)+NCHAR(10)+NCHAR(9)+NCHAR(9)+N'LEFT JOIN [${dbxschemaname}].systemuser su3 ON ')
          + 
         (@tableName)
          + 
         (N'.modifiedby = su3.id) where 1=1')
    

      IF @_productId <> ''
       
         SET @queryStatement = (@queryStatement) + (N' and ') + (@tableName) + (N'.product_id = ') + ((QUOTENAME((@_productId), '''')))
   

      IF @_productType <> ''
      
         SET @queryStatement = (@queryStatement) + (N' and ') + (N'product.Type_id = ') + ((QUOTENAME((@_productType), '''')))
         

      IF @_customerId <> ''
        
         SET @queryStatement = (@queryStatement) + (N' and ') + (@tableName) + (N'.customerId = ') + ((QUOTENAME((@_customerId), '''')))
        

      IF @_assignedCSRID <> ''
       
         SET @queryStatement = (@queryStatement) + (N' and ') + (@tableName) + (N'.csr_id = ') + ((QUOTENAME((@_assignedCSRID), '''')))
       

      IF @_phoneNumber <> ''
         
         SET @queryStatement = 
            (@queryStatement)
             + 
            (N' and ')
             + 
            (@tableName)
             + 
            (N'.phoneNumber like ')
             + 
            QUOTENAME((@_phoneNumber), '''')
             + '%'
        
      IF @_emailAddress <> ''
      
         SET @queryStatement = (@queryStatement) + (N' and ') + (@tableName) + (N'.email = ') + ((QUOTENAME((@_emailAddress), '''')))
        
      IF @_leadType = 'CUSTOMER'
         
         SET @queryStatement = (@queryStatement) + (N' and ') + (@tableName) + (N'.isCustomer = 1')
         
      ELSE 
         BEGIN
            IF @_leadType = 'NON-CUSTOMER'
             
               SET @queryStatement = (@queryStatement) + (N' and ') + (@tableName) + (N'.isCustomer = 0')
               
         END

      IF @_statusIds <> ''
      
         SET @queryStatement = 
            (@queryStatement)
             + 
            (N' and ')
             + 
            (@tableName)
             + 
            (N'.status_id in  (')
             + 
            ([${dbxschemaname}].func_escape_input_for_in_operator(@_statusIds))
             + 
            (N')')
        
      IF @_modifiedStartDate <> '' AND @_modifiedEndDate <> ''
     
         SET @queryStatement = 
            (@queryStatement)
             + 
            (' and CONVERT(DATE,')
             + 
            (@tableName)
             + 
            ('.lastmodifiedts,105) >=')
             + 
            (QUOTENAME(CONVERT(DATE,CAST(@_modifiedStartDate AS DATE),105),''''))
             + 
            (N' and  CONVERT(DATE,')
             + 
            (@tableName)
             + 
            (N'.lastmodifiedts,105) <=')
             + 
			(QUOTENAME(CONVERT(DATE,CAST(@_modifiedEndDate AS DATE),105),''''))
      EXEC(@queryStatement)
    END

GO
/****** Object:  StoredProcedure [${dbxschemaname}].[lead_search_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[lead_search_proc]  
   @_productId nvarchar(50),
   @_leadId nvarchar(50),
   @_productType nvarchar(50),
   @_customerId nvarchar(50),
   @_leadType nvarchar(50),
   @_statusIds nvarchar(50),
   @_assignedCSRID nvarchar(50),
   @_modifiedStartDate nvarchar(50),
   @_modifiedEndDate nvarchar(50),
   @_phoneNumber nvarchar(50),
   @_emailAddress nvarchar(50),
   @_sortOrder nvarchar(50),
   @_sortCriteria nvarchar(50),
   @_offset bigint,
   @_recordsPerPage bigint
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON
	  DECLARE @tableName nvarchar(max)
	  DECLARE @queryStatement nvarchar(max)


      SET @tableName = '[${dbxschemaname}].lead'
      IF @_statusIds = 'SID_ARCHIVED'
      SET @tableName = '[${dbxschemaname}].archivedlead'
         SET @queryStatement = ' SELECT '+(@tableName)+('.id AS id,')+(@tableName)+('.firstName AS firstName,')+ 
         (@tableName)+('.middleName AS middleName,')+(@tableName)+('.lastName AS lastName,')+ 
         (@tableName)+('.salutation AS salutation,')+(@tableName)+('.isCustomer AS isCustomer,')+ 
         (@tableName)+('.customerId AS customerId,customer.CustomerType_id AS customerType,')+ 
         (@tableName)+('.product_id AS productId,product.name AS productName,product.Type_id AS productType,')+ 
         (@tableName)+('.csr_id AS csrId,su1.FirstName AS assignedToFirstName,su1.MiddleName AS assignedToMiddleName,su1.LastName AS assignedToLastName,')+ 
         (@tableName)+('.status_id AS statusId,')+(@tableName)+('.closureReason AS closureReason,')+(@tableName)+('.countryCode AS countryCode,')+ 
         (@tableName)+('.phoneNumber AS phoneNumber,')+(@tableName)+('.extension AS extension,')+(@tableName)+('.email AS email,')+ 
         (@tableName)+('.createdby AS createdby,su2.FirstName AS createdByFirstName,su2.MiddleName AS createdByMiddleName,su2.LastName AS createdByLastName,
		 su3.FirstName AS modifiedbyFirstName,su3.MiddleName AS modifiedbyMiddleName,su3.LastName AS modifiedbyLastName,')+ 
         (@tableName)+('.modifiedby AS modifiedby,')+(@tableName)+('.createdts AS createdts,')+ 
         (@tableName)+('.lastmodifiedts AS lastmodifiedts,')+(@tableName)+('.synctimestamp AS synctimestamp,')+ 
         (@tableName)+('.softdeleteflag AS softdeleteflag FROM (')+(@tableName)+(' LEFT JOIN [${dbxschemaname}].systemuser su1 ON ')+ 
         (@tableName)+('.csr_id = su1.id LEFT JOIN [${dbxschemaname}].product ON ')+(@tableName)+('.product_id = product.id LEFT JOIN [${dbxschemaname}].systemuser su2 ON ')+ 
         (@tableName)+('.createdby = su2.id LEFT JOIN [${dbxschemaname}].systemuser su3 ON ')+(@tableName)+('.modifiedby = su3.id LEFT JOIN [${dbxschemaname}].customer ON ')+ 
         (@tableName)+('.customerId = customer.id) where 1=1 ')
     SELECT(@queryStatement)

      IF @_leadId <> ''
       
         SET @queryStatement = (@queryStatement) + (N' and ') + (@tableName) + (N'.id = ') + ((QUOTENAME((@_leadId), '''')))
         

      IF @_productId <> ''
        
         SET @queryStatement = (@queryStatement) + (N' and ') + (@tableName) + (N'.product_id = ') + ((QUOTENAME((@_productId), '''')))
         

      IF @_productType <> ''
       
         SET @queryStatement = (@queryStatement) + (N' and ') + (N'product.Type_id = ') + ((QUOTENAME((@_productType), '''')))
         
      IF @_customerId <> ''
       
         SET @queryStatement = (@queryStatement) + (N' and ') + (@tableName) + (N'.customerId = ') + ((QUOTENAME((@_customerId), '''')))
       
      IF @_assignedCSRID <> ''
      
         SET @queryStatement = (@queryStatement) + (N' and ') + (@tableName) + (N'.csr_id = ') + ((QUOTENAME((@_assignedCSRID), '''')))
        
      IF @_phoneNumber <> ''
        
         SET @queryStatement = 
            (@queryStatement)
             + 
            (N' and ')
             + 
            (@tableName)
             + 
            (N'.phoneNumber like (')
             + 
            ((QUOTENAME((@_phoneNumber+'%'), '''')))+')'
       
      IF @_emailAddress <> ''
      
         SET @queryStatement = (@queryStatement) + (N' and ') + (@tableName) + (N'.email = ') + ((QUOTENAME((@_emailAddress), '''')))
        
      IF @_leadType = 'CUSTOMER'
       
         SET @queryStatement = (@queryStatement) + (N' and ') + (@tableName) + (N'.isCustomer = 1')
         
      ELSE 
         BEGIN
            IF @_leadType = 'NON-CUSTOMER'      
               SET @queryStatement = (@queryStatement) + (N' and ') + (@tableName) + (N'.isCustomer = 0')
			END

      IF @_statusIds <> ''
         SET @queryStatement = 
            (@queryStatement)
             + 
            (N' and ')
             + 
            (@tableName)
             + 
            (N'.status_id in  (')
             + 
            ([${dbxschemaname}].func_escape_input_for_in_operator(@_statusIds))
             + 
            (N')')
       
      IF @_modifiedStartDate <> '' AND @_modifiedEndDate <> ''
       
         SET @queryStatement = 
            (@queryStatement)
             + 
            (N' and CONVERT(DATE,')
             + 
            (@tableName)
             + 
            (N'.lastmodifiedts,105) >=')
             + 
            (QUOTENAME(CONVERT(DATE,CAST(@_modifiedStartDate AS DATE),105),''''))
             + 
            (N' and  CONVERT(DATE,')
             + 
            (@tableName)
             + 
            (N'.lastmodifiedts,105) <=')
             + 
            (QUOTENAME(CONVERT(DATE,CAST(@_modifiedEndDate AS DATE),105),''''))
        
      IF @_sortOrder <> 'asc' AND @_sortOrder <> 'desc'
         SET @_sortOrder = N'desc'

      IF @_sortCriteria = ''
         SET @_sortCriteria = N'lastmodifiedts'

    
      SET @queryStatement = (@queryStatement) + (N' order by ') + (@_sortCriteria) + (N' ') + (@_sortOrder)
  
      SET @queryStatement = (@queryStatement) + (N' OFFSET ') + CAST(@_offset AS nvarchar(max)) + (N' ROWS FETCH NEXT ') + CAST(@_recordsPerPage AS nvarchar(max)) + (N' ROWS ONLY ')
           
	  SELECT(@queryStatement)
      EXEC(@queryStatement)

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[Location_address_suggestions_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/
CREATE PROCEDURE [${dbxschemaname}].[location_details_proc]  
   @_locationId char(50)
AS 

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DECLARE


         @workingHours varchar(2000)

      DECLARE


         @tempDayName varchar(2000)

      DECLARE


         @tempStartTime varchar(2000)

      DECLARE


         @tempEndTime varchar(2000)

      DECLARE

         @servicesList varchar(max)

      DECLARE
        @currencyList varchar(max)

      DECLARE
         @b int
		 declare @a varchar
		 declare @wh varchar

      DECLARE
         @pipeFlag int

      SET @a = ''
      SET @wh = ''
      SET @pipeFlag = 0
      
      DECLARE
          cur_1 CURSOR LOCAL FORWARD_ONLY FOR 
            SELECT dayschedule.WeekDayName, dayschedule.StartTime, dayschedule.EndTime
            FROM [${dbxschemaname}].dayschedule, [${dbxschemaname}].location
            WHERE location.WorkSchedule_id = dayschedule.WorkSchedule_id AND location.id = @_locationId

      OPEN cur_1

      WHILE (1 = 1)
      
         BEGIN

            SET @tempDayName = ''

            SET @tempStartTime = ''

            SET @tempEndTime = ''

            FETCH cur_1
                INTO @tempDayName, @tempStartTime, @tempEndTime

            IF @@FETCH_STATUS <> 0
               SET @b = 1

            IF @tempDayName <> ''
               BEGIN
                  IF @pipeFlag = 0

                     SET @a= 
                        (upper(left(@tempDayName, 1)))
                         + 
                        (lower(substring(@tempDayName, 2,DATALENGTH(@tempDayName))))
                         + 
                        (': ')
                         + 
                        (substring(@tempStartTime, 1, 5))
                         + 
                        ('AM')
                         + 
                        (' - ')
                         + 
                        (substring(@tempEndTime, 1, 5))
                         + 
                        ('PM')
      
                  ELSE 
         
                     SET @a = 
                        (' || ')
                         + 
                        (upper(left(@tempDayName, 1)))
                         + 
                        (lower(substring(@tempDayName, 2,DATALENGTH(@tempDayName))))
                         + 
                        (': ')
                         + 
                        (substring(@tempStartTime, 1, 5))
                         + 
                        ('AM')
                         + 
                        (' - ')
                         + 
                        (substring(@tempEndTime, 1, 5))
                         + 
                        ('PM')
  

                  SET @pipeFlag = 1

                  SET @a = 
                     (' || ')
                      + 
                     (@tempDayName)
                      + 
                     (': ')
                      + 
                     (@tempStartTime)
                      + 
                     ('AM')
                      + 
                     (' - ')
                      + 
                     (@tempEndTime)
                      + 
                     ('PM')

                  SET @wh = (@wh) + (' ') + (@a) + ('')
                 

               END

            IF @b = 1
               BREAK

         END

      CLOSE cur_1

      DEALLOCATE cur_1

      SET @workingHours = @wh



      SET @servicesList = 
         (
            SELECT String_agg(CAST(service.name as nvarchar(max)) , ' || ') WITHIN GROUP (ORDER BY service.name ASC) AS services
            FROM [${dbxschemaname}].service, [${dbxschemaname}].locationservice
            WHERE service.id = locationservice.Service_id AND locationservice.Location_id = @_locationId
         )

      SET @currencyList = 
         (
            SELECT String_agg(currency.code , ',') WITHIN GROUP (ORDER BY currency.code ASC) AS currencies
            FROM [${dbxschemaname}].currency, [${dbxschemaname}].locationcurrency
            WHERE currency.code = locationcurrency.currency_code AND locationcurrency.Location_id = @_locationId
         )

      SELECT 
         location.id AS locationId, 
         location.Type_id AS type, 
         location.Name AS informationTitle, 
         CASE location.Status_id
            WHEN N'SID_ACTIVE' THEN N'OPEN'
            ELSE N'CLOSED'
         END AS status, 
         location.DisplayName AS displayName, 
         location.Description AS description, 
         location.PhoneNumber AS phone, 
         location.EmailId AS email, 
         location.WorkingDays AS workingDays, 
         location.IsMainBranch AS isMainBranch, 
         location.MainBranchCode AS mainBranchCode, 
         address.addressLine1 AS addressLine1, 
         address.addressLine2 AS addressLine2, 
         address.addressLine3 AS addressLine3, 
         address.latitude AS latitude, 
         address.logitude AS longitude, 
         @workingHours AS workingHours, 
         @servicesList AS services, 
         @currencyList AS currencies
      FROM (([${dbxschemaname}].location 
         INNER JOIN [${dbxschemaname}].address 
         ON ((location.Address_id = address.id))))
      WHERE location.id = @_locationId
GO

CREATE PROCEDURE [${dbxschemaname}].[Location_address_suggestions_proc]  
   @_currLatitude nvarchar(50),
   @_currLongitude nvarchar(50),
   @_radius float(53),
   @_var nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DECLARE @rowLatitide varchar(2000)
      DECLARE @rowLongitude varchar(2000)
      DECLARE @b int
	  DECLARE @pipeFlag int
	  DECLARE @loc nvarchar(max)

      SET @_var = '%' + @_var + '%'
      SET @_radius = @_radius / 1000

      SELECT 
         [location].id AS locationId, 
		 ( SELECT DISTINCT (String_agg(( 
          UPPER(LEFT(dayschedule.weekdayname, 1))+
          LOWER(SUBSTRING(dayschedule.weekdayname,1, 2) )+ 
          ':'+
          SUBSTRING(CAST(dayschedule.StartTime AS nvarchar(max)), 1, 5)+
          '-'+
          SUBSTRING(CAST(dayschedule.endTime AS nvarchar(max)), 1, 5)
        ) , ' || ') )
               FROM [${dbxschemaname}].dayschedule, [${dbxschemaname}].[location]
               WHERE dayschedule.WorkSchedule_id = [location].WorkSchedule_id AND ([location].id) = [location].id ) AS workingHours, 
         location.Name AS informationTitle, 
         location.Description AS [description], 
         location.PhoneNumber AS phone, 
         location.EmailId AS email, 
         CASE [location].softdeleteflag
            WHEN 0 THEN N'OPEN'
            ELSE N'CLOSED'
         END AS [status], 
         CASE [location].Type_id
            WHEN N'ATM' THEN N'ATM'
            ELSE N'BRANCH'
         END AS type, 
         [location].Status_id AS isVisible, 
         
            (
               SELECT String_Agg(facility.[name], '||' ) WITHIN GROUP (ORDER BY facility.id ASC)
			   FROM ([${dbxschemaname}].facility 
                  CROSS JOIN [${dbxschemaname}].locationfacility)
               WHERE ((facility.id = (locationfacility.facility_id)) AND ((locationfacility.Location_id) = [location].id))) AS services, 
         
            (
               SELECT [address].cityName
               FROM [${dbxschemaname}].[address]
               WHERE [address].id = [location].Address_id
            ) AS city, 
         
            (
               SELECT region.[Name]
               FROM [${dbxschemaname}].region
               WHERE (region.id = [address].Region_id)
            ) AS Region, 
         
            (
               SELECT country.[Name]
               FROM [${dbxschemaname}].country
               WHERE ((country.id) = 
                  (
                     SELECT region.Country_id
                     FROM [${dbxschemaname}].region
                     WHERE (region.id = [address].Region_id)
                  ))
            ) AS country, 
         [address].addressLine1 AS addressLine1, 
         [address].addressLine2 AS addressLine2, 
         [address].addressLine3 AS addressLine3, 
         [address].zipCode AS zipCode, 
         [address].latitude AS latitude, 
         [address].logitude AS longitude, 
         (6371 * 2 * asin(sqrt(power(sin(((CAST([address].latitude AS float(53))) - abs(CAST(@_currLatitude AS float(53)))) * pi() / 180 / 2), 2) + cos((CAST(address.latitude AS float(53))) * pi() / 180) * cos(abs(CAST(@_currLatitude AS float(53))) * pi() / 180) * power(sin(((CAST(address.logitude AS float(53))) - (CAST(@_currLongitude AS float(53)))) * pi() / 180 / 2), 2)))) AS distance
      FROM [${dbxschemaname}].[address], [${dbxschemaname}].[location]
      WHERE [address].id = [location].Address_id
	  GROUP BY [${dbxschemaname}].[location].Address_id,[${dbxschemaname}].[address].latitude,[${dbxschemaname}].[address].logitude,[${dbxschemaname}].[address].[type],
	  [location].[Name],[${dbxschemaname}].[location].PhoneNumber,[${dbxschemaname}].[address].country,[${dbxschemaname}].[location].Status_id,[${dbxschemaname}].[address].Region_id,
	  [location].id,[location].[Description],[location].EmailId,[location].softdeleteflag,[location].[Type_id],[address].addressLine1,
	  [address].addressLine2,[address].addressLine3,[address].zipCode
      HAVING 
         ((6371 * 2 * asin(sqrt(power(sin(((CAST([address].latitude AS float(53))) - abs(CAST(@_currLatitude AS float(53)))) * pi() / 180 / 2), 2) + cos((CAST(address.latitude AS float(53))) * pi() / 180) * cos(abs(CAST(@_currLatitude AS float(53))) * pi() / 180) * power(sin(((CAST(address.logitude AS float(53))) - (CAST(@_currLongitude AS float(53)))) * pi() / 180 / 2), 2))))) <= @_radius AND 
         (((
               SELECT [address].cityName
               FROM [${dbxschemaname}].[address]
               WHERE [address].id = [${dbxschemaname}].[location].Address_id
            )) LIKE @_var OR 
         [address].[type] LIKE @_var OR 
         (SELECT region.[Name]
               FROM [${dbxschemaname}].region
               WHERE (region.id = [address].Region_id)) LIKE @_var OR 
         [location].[Name] LIKE @_var OR 
         [location].PhoneNumber LIKE @_var OR 
         [address].country LIKE @_var) AND 
         ([location].Status_id) = 'SID_ACTIVE'

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[location_range_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[location_range_proc]  
   @_currLatitude varchar(50),
   @_currLongitude varchar(50),
   @_radius int
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DECLARE


         @rowLatitide varchar(2000)

      DECLARE


         @rowLongitude varchar(2000)

      DECLARE
         @b int

      DECLARE
         @pipeFlag int


      SELECT 
         min(location.id) AS locationId, 
         
            (
               SELECT DISTINCT(String_agg((upper(LEFT(dayschedule.weekdayname, 1))+lower(SUBSTRING(dayschedule.weekdayname,2,DATALENGTH(dayschedule.weekdayname)))+ ':'+ 
          SUBSTRING(CAST(dayschedule.StartTime AS VARCHAR(max)), 1, 5)+'-'+ SUBSTRING(CAST(dayschedule.endTime AS VARCHAR(max)), 1, 5)) , ' || ')) 
               FROM [${dbxschemaname}].dayschedule, [${dbxschemaname}].location
               WHERE dayschedule.WorkSchedule_id = location.WorkSchedule_id AND location.id = location.id
            ) AS workingHours, 
         min(location.Name) AS informationTitle, 
         min(location.PhoneNumber) AS phone, 
         min(location.EmailId) AS email, 
         CASE min(location.Status_id)
            WHEN N'SID_ACTIVE' THEN N'OPEN'
            ELSE N'CLOSED'
         END AS status, 
         min(location.Type_id) AS type, 
		 min(location.status_id) AS isVisible,
         
            (  SELECT String_agg(CAST(facility.name as nvarchar(max)),'||') WITHIN GROUP (ORDER BY facility.id ASC)
               FROM [${dbxschemaname}].facility , [${dbxschemaname}].locationfacility
               WHERE facility.id = locationfacility.facility_id AND locationfacility.Location_id =min( location.id)
            )  AS services, 
         
            (
               SELECT city.Name
               FROM [${dbxschemaname}].city
               WHERE city.id = min(address.City_id)
            ) AS city, 
         min(address.addressLine1) AS addressLine1, 
         min(address.addressLine2) AS addressLine2, 
         min(address.addressLine3) AS addressLine3, 
         min(address.zipCode) AS zipCode, 
         min(address.latitude) AS latitude, 
         min(address.logitude) AS longitude, 
         (6371 * 2 * asin(sqrt(power(sin((CAST(min(address.latitude) AS float(53)) - abs(CAST(@_currLatitude AS float(53)))) * pi() / 180 / 2), 2) + cos(CAST(min(address.latitude) AS float(53)) * pi() / 180) * cos(abs(CAST(@_currLatitude AS float(53))) * pi() / 180) * power(sin((CAST(min(address.logitude) AS float(53)) - CAST(@_currLongitude AS float(53))) * pi() / 180 / 2), 2)))) AS distance
      FROM [${dbxschemaname}].address, [${dbxschemaname}].location
      WHERE address.id = location.Address_id
      having (6371 * 2 * asin(sqrt(power(sin((CAST(min(address.latitude) AS float(53)) - abs(CAST(@_currLatitude AS float(53)))) * pi() / 180 / 2), 2) + cos(CAST(min(address.latitude) AS float(53)) * pi() / 180) * cos(abs(CAST(@_currLatitude AS float(53))) * pi() / 180) * power(sin((CAST(min(address.logitude) AS float(53)) - CAST(@_currLongitude AS float(53))) * pi() / 180 / 2), 2)))) <= @_radius
      
   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[location_search_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[location_search_proc]  

   @_searchKeyword varchar(1000)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DECLARE
   
         @_next varchar(max) = NULL

      DECLARE
         @_nextlen int = NULL

      DECLARE
  
         @_value varchar(max) = NULL

      DECLARE
     
         @sql1 varchar(max)

      DECLARE
    
         @sqlFrontPart varchar(max)

      DECLARE
     
         @sqlresult varchar(max) = NULL

      DECLARE
         @counter int

      SET @sqlFrontPart = N'SELECT * FROM [${dbxschemaname}].locationdetails_view where '
  
      SET @counter = 1
    

      WHILE (1 = 1)
      
         BEGIN

            IF datalength(LTRIM(RTRIM(@_searchKeyword))) = 0 OR @_searchKeyword IS NULL
               BREAK

            SET @_next = [${dbxschemaname}].substring_index(@_searchKeyword, ',', 1)

            SET @_nextlen = datalength(@_next)

            SET @_value = lower(LTRIM(RTRIM(@_next)))

            SET @sql1 = NULL
      declare @var nvarchar
            SET @var = NULL
        
            SET @var = replace(LTRIM(RTRIM(@_value)), N' ', N'%')
          
            SET @sql1 = 
               (N'( LOWER(city) like (''')
                + 
               (N'%')
                + 
               (@var)
                + 
               (N'%')
                + 
               (N''')  or LOWER(addressLine1) like (''')
                + 
               (N'%')
                + 
               (@var)
                + 
               (N'%')
                + 
                + 
               (N''') or LOWER(addressLine2) like (''')
                + 
               (N'%')
                + 
               (@var)
                + 
               (N'%')
                + 
               (N''')  or LOWER(addressLine3) like (''')
                + 
               (N'%')
                + 
               (@var)
                + 
               (N'%')
                + 
               (N''')  or LOWER(country) like (''')
                + 
               (N'%')
                + 
               (@var)
                + 
               (N'%')
                + 
               (N''')  or LOWER(zipcode) like (''')
                + 
               (N'%')
                + 
               (@var)
                + 
               (N'%')
                + 
               (N'''))')
        
            IF @counter = 1
           
               SET @sqlresult = @sql1
               
            ELSE 
           
               SET @sqlresult = (@sql1) + (N' AND ') + (@sqlresult)
          
            SET @counter = @counter + 1
         
            SET @_searchKeyword = STUFF(@_searchKeyword, 1, @_nextlen + 1, N'')

         END

    
      SET @sqlresult = (N'(') + (@sqlFrontPart) + (@sqlresult) + (N')')
	  EXEC(@sqlresult)
   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[organisation_actions_create_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/
CREATE PROCEDURE [${dbxschemaname}].[mfa_c360_feature_get_proc]
AS 


      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT DISTINCT feature.id AS feature_id, feature.name AS feature_name, feature.App_id AS App_id
      FROM 
         [${dbxschemaname}].feature 
            INNER JOIN [${dbxschemaname}].featureaction 
            ON (feature.id = featureaction.Feature_id)
      WHERE featureaction.isMFAApplicable = 1 AND feature.Status_id = 'SID_FEATURE_ACTIVE'
         ORDER BY feature.id
GO

CREATE PROCEDURE [${dbxschemaname}].[organisation_actions_create_proc]  
   @_features nvarchar(max),
   @_organisationType nvarchar(50),
   @_organisationId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
	  SET  NOCOUNT  ON

	  DECLARE @limitvalue nvarchar(max)
	  DECLARE @id nvarchar(max)
      DECLARE @finished int = 0
	  DECLARE @featureActionId varchar(255) = N''
	  DECLARE @actionslist varchar(max) = N''
	  DECLARE @limitId varchar(255) = N''
	  DECLARE  @entryStatus int = 0
	  DECLARE @features_list nvarchar(max)

      SET @features_list = 
         (  SELECT String_agg(CAST(feature.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].feature 
            INNER JOIN [${dbxschemaname}].featureroletype 
            ON (featureroletype.Feature_id = feature.id AND featureroletype.RoleType_id = @_organisationType)
            WHERE ([${dbxschemaname}].FIND_IN_SET(feature.id, @_features)) <> 0)
 
      SET @features_list = CASE  WHEN (@features_list IS NULL) THEN N'' ELSE @features_list END
  
DECLARE actions CURSOR LOCAL FOR 
             ( SELECT featureaction.id
               FROM [${dbxschemaname}].featureaction
               WHERE [${dbxschemaname}].FIND_IN_SET(featureaction.Feature_id, @features_list) > 0
             )

      OPEN actions
	  FETCH NEXT FROM actions INTO @featureActionId
      WHILE (@@FETCH_STATUS=0)
			BEGIN
				  SET @entryStatus = 0
				  DECLARE limits CURSOR LOCAL FOR 
              ( SELECT actionlimit.LimitType_id
                FROM [${dbxschemaname}].actionlimit
                WHERE actionlimit.Action_id = @featureActionId
              )
                  OPEN limits
				  FETCH NEXT FROM limits INTO @limitId
                  WHILE (@@FETCH_STATUS=0)
                     BEGIN
							 SET @limitvalue = (  SELECT actionlimit.value FROM [${dbxschemaname}].actionlimit
                             WHERE actionlimit.Action_id = @featureActionId AND actionlimit.LimitType_id = @limitId)
                             SET @id = (SELECT left(newid(), 50))
                           
                              INSERT [${dbxschemaname}].organisationactionlimit(
                                 [${dbxschemaname}].organisationactionlimit.id, 
                                 [${dbxschemaname}].organisationactionlimit.Organisation_id, 
                                 [${dbxschemaname}].organisationactionlimit.Action_id, 
                                 [${dbxschemaname}].organisationactionlimit.LimitType_id, 
                                 [${dbxschemaname}].organisationactionlimit.[value])
                                 VALUES (
                                    @id, 
                                    @_organisationId, 
                                    @featureActionId, 
                                    @limitId, 
                                    @limitvalue)
                              SET @entryStatus = 1
							  FETCH NEXT FROM limits INTO @limitId
                              CONTINUE
                      END
                  CLOSE limits
                  DEALLOCATE limits
                  IF @entryStatus = 0
                     BEGIN
                        SET @id = (SELECT left(newid(), 50))
                     INSERT [${dbxschemaname}].organisationactionlimit([${dbxschemaname}].organisationactionlimit.id, [${dbxschemaname}].organisationactionlimit.Organisation_id, [${dbxschemaname}].organisationactionlimit.Action_id)
                     VALUES (@id, @_organisationId, @featureActionId)
                     END
                  SET @actionslist = @featureActionId + ',' + @actionslist
				  FETCH NEXT FROM actions INTO @featureActionId
                  CONTINUE
         END
      CLOSE actions
      DEALLOCATE actions
      SET @actionslist = (SELECT substring(@actionslist, 1,LEN(@actionslist)-1))
      SELECT @actionslist as actionslist
   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[organisation_actions_delete_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[organisation_actions_delete_proc]  
   @_features nvarchar(max),
   @_organisationType nvarchar(50),
   @_organisationId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DECLARE
         @finished int = 0

      DECLARE
      

         @featureActionId varchar(255) = N''

      DECLARE
       
         @actionslist varchar(max) = N''
		 declare @features_list nvarchar(max)
      SET @features_list = 
         (
        
            SELECT String_agg(CAST(feature.id as nvarchar(max)) , ',') 
            FROM 
               [${dbxschemaname}].feature 
                  INNER JOIN [${dbxschemaname}].featureroletype 
                  ON (featureroletype.Feature_id = feature.id AND featureroletype.RoleType_id = @_organisationType)
            WHERE ([${dbxschemaname}].FIND_IN_SET(feature.id, @_features) <> 0)
         

         )
   
      SET @features_list = 
         CASE 
            WHEN (@features_list IS NULL) THEN N''
            ELSE @features_list
         END
   declare @customer_list nvarchar(max)
      SET @customer_list = 
         (
           
            SELECT String_agg(CAST(Customer_id as nvarchar(max)) , ',') 
            FROM [${dbxschemaname}].organisationemployees
            WHERE (organisationemployees.Organization_id = @_organisationId)
        
         )
 
      SET @customer_list = 
         CASE 
            WHEN (@customer_list IS NULL) THEN N''
            ELSE @customer_list
         END
   
      DECLARE
          actions CURSOR LOCAL FORWARD_ONLY FOR 
             (
               SELECT featureaction.id
               FROM [${dbxschemaname}].featureaction
               WHERE [${dbxschemaname}].FIND_IN_SET(featureaction.Feature_id, @features_list) <> 0
             )
 
      OPEN actions
   

      WHILE (1 = 1)
      
         BEGIN

         
            FETCH actions
                INTO @featureActionId
            

            IF @@FETCH_STATUS <> 0
               SET @finished = 1

            IF @finished = 1
               BREAK
            ELSE 
               BEGIN


                  DELETE 
                  FROM [${dbxschemaname}].organisationactionlimit
                  WHERE (organisationactionlimit.Organisation_id = @_organisationId AND organisationactionlimit.Action_id = @featureActionId)

                  SET @actionslist = @featureActionId + N',' + @actionslist

                  CONTINUE

               END

         END

      CLOSE actions
      
      DEALLOCATE actions
     
      SET @actionslist = 
         (
            SELECT substring(@actionslist, 1, 
			case when LEN(@actionslist) = 0 then LEN(@actionslist) 
			else charindex(' ', @actionslist) -1 end)
         )

      DELETE 
      FROM [${dbxschemaname}].customeraction
      WHERE (
         customeraction.RoleType_id = @_organisationType AND 
         [${dbxschemaname}].FIND_IN_SET(customeraction.Action_id, @actionslist) <> 0 AND 
         [${dbxschemaname}].FIND_IN_SET(customeraction.Customer_id, @customer_list) <> 0)
    
      SELECT @actionslist

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[organisation_actions_get_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[organisation_actions_get_proc]  
   @_organisationId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
	  declare @actionList nvarchar(max)
      SET @actionList = 
         (

            SELECT DISTINCT(String_agg(CAST(organisationactionlimit.Action_id as nvarchar(max)) , ','))
            FROM [${dbxschemaname}].organisationactionlimit
            WHERE organisationactionlimit.Organisation_id = @_organisationId
  
         )

      SELECT @actionList AS actionslist

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[organisation_customeraccounts_delete_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[organisation_customeraccounts_delete_proc]  
   @_accounts nvarchar(max),
   @_organisationId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
	  declare @orgemployees_list nvarchar(max)
      SET @orgemployees_list = 
         (
    
            SELECT String_agg(CAST(organisationemployees.Customer_id as nvarchar(max)) , ',') 
            FROM [${dbxschemaname}].organisationemployees
            WHERE organisationemployees.Organization_id = @_organisationId
     
         )

      SET @orgemployees_list = 
         CASE 
            WHEN (@orgemployees_list IS NULL) THEN N''
            ELSE @orgemployees_list
         END

      SELECT @orgemployees_list

      DELETE 
      FROM [${dbxschemaname}].customeraction
      WHERE [${dbxschemaname}].FIND_IN_SET(customeraction.Account_id, @_accounts) <> 0 AND [${dbxschemaname}].FIND_IN_SET(customeraction.Customer_id, @orgemployees_list) <> 0

      DELETE 
      FROM [${dbxschemaname}].customeraccounts
      WHERE [${dbxschemaname}].FIND_IN_SET(customeraccounts.Account_id, @_accounts) <> 0 AND [${dbxschemaname}].FIND_IN_SET(customeraccounts.Customer_id, @orgemployees_list) <> 0
  
   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[organisation_features_create_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[organisation_features_create_proc]  
   @_features nvarchar(max),
   @_organisationType nvarchar(50),
   @_organisationId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
	  DECLARE @featuresList nvarchar(max)=''
      DECLARE
         @finished int = 0

      DECLARE

         @featureId varchar(255) = N''

      DECLARE

         @features_List varchar(max) = N''
		 
      SET @features_list = 
         (
 
            SELECT String_agg(CAST(feature.id as NVARCHAR(max)), ',') 
            FROM 
               [${dbxschemaname}].feature 
                  INNER JOIN [${dbxschemaname}].featureroletype 
                  ON (featureroletype.Feature_id = feature.id AND featureroletype.RoleType_id = @_organisationType)
            WHERE ([${dbxschemaname}].FIND_IN_SET(feature.id, @_features) <> 0)

         )

      SET @features_list = 
         CASE 
            WHEN (@features_list IS NULL) THEN N''
            ELSE @features_list
         END

      DECLARE
          features CURSOR LOCAL FORWARD_ONLY FOR 
             (
               SELECT feature.id
               FROM [${dbxschemaname}].feature
               WHERE [${dbxschemaname}].FIND_IN_SET(feature.id, @features_list) <> 0
             )
			 declare @id nvarchar(max)
			 open features
FETCH NEXT FROM features INTO @featureId
      WHILE (1 = 1)
      
         BEGIN
		IF @@FETCH_STATUS <> 0
               SET @finished = 1

            IF @finished = 1
               BREAK
            ELSE 
               BEGIN

                  SET @id = 
                     (
                        SELECT left(newid(), 50)
                     )

                  INSERT [${dbxschemaname}].organisationfeatures([${dbxschemaname}].organisationfeatures.id, [${dbxschemaname}].organisationfeatures.organisationId, [${dbxschemaname}].organisationfeatures.featureId)
                     VALUES (@id, @_organisationId, @featureId)
 
                  SET @featuresList = @featureId + N',' + @featuresList
				  FETCH NEXT FROM features INTO @featureId
                  CONTINUE

               END

         END

      CLOSE features

      DEALLOCATE features

      SET @featuresList = 
         (
            SELECT substring(@featuresList, 1, (len(@featuresList) - 1))
         )

      SELECT @featuresList as featuresList

   END

GO
/****** Object:  StoredProcedure [${dbxschemaname}].[organisation_features_delete_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[organisation_features_delete_proc]  
   @_features nvarchar(max),
   @_organisationType nvarchar(50),
   @_organisationId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DECLARE
         @finished int = 0

      DECLARE
         @featureId varchar(255) = N''

      DECLARE
         @featuresList varchar(max) = N''
		 declare @features_list nvarchar(max)
      SET @features_list = 
         (

            SELECT String_agg(CAST(feature.id as nvarchar(max)) , ',') 
            FROM 
               [${dbxschemaname}].feature 
                  INNER JOIN [${dbxschemaname}].featureroletype 
                  ON (featureroletype.Feature_id = feature.id AND featureroletype.RoleType_id = @_organisationType)
            WHERE ([${dbxschemaname}].FIND_IN_SET(feature.id, @_features) <> 0)
   
         )

      SET @features_list = 
         CASE 
            WHEN (@features_list IS NULL) THEN N''
            ELSE @features_list
         END

      DECLARE
          features CURSOR LOCAL FORWARD_ONLY FOR 
             (
               SELECT feature.id
               FROM [${dbxschemaname}].feature
               WHERE [${dbxschemaname}].FIND_IN_SET(feature.id, @features_list) <> 0
             )
 
      OPEN features

      WHILE (1 = 1)
      
         BEGIN

            FETCH features
                INTO @featureId
        

            IF @@FETCH_STATUS <> 0
               SET @finished = 1

            IF @finished = 1
               BREAK
            ELSE 
               BEGIN

         
                  DELETE 
                  FROM [${dbxschemaname}].organisationfeatures
                  WHERE (organisationfeatures.organisationId = @_organisationId AND organisationfeatures.featureId = @featureId)

                  SET @featuresList = @featureId + N',' + @featuresList

                  CONTINUE

               END

         END

      CLOSE features
 
      DEALLOCATE features
     


      SET @featuresList = 
         (
            SELECT substring(@featuresList, 1, 
			case when LEN(@featuresList) = 0 then LEN(@featuresList) 
			else LEN(@featuresList) -1 end)
         )

      SELECT @featuresList as featuresList

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[organisation_features_suspend_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[organisation_features_suspend_proc]  
   @_features nvarchar(max),
   @_organisationType nvarchar(50),
   @_organisationId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DECLARE
         @finished int = 0

      DECLARE

         @featureId varchar(255) = N''

      DECLARE

         @featuresList varchar(max) = N''

      UPDATE [${dbxschemaname}].organisationfeatures
         SET 
            featureStatus = NULL
      WHERE organisationfeatures.organisationId = @_organisationId
	  declare @features_list nvarchar(max)
      SET @features_list = 
         (

            SELECT String_agg(CAST(feature.id as nvarchar(max)) , ',') 
            FROM 
               [${dbxschemaname}].feature 
                  INNER JOIN [${dbxschemaname}].featureroletype 
                  ON (featureroletype.Feature_id = feature.id AND featureroletype.RoleType_id = @_organisationType)
            WHERE ([${dbxschemaname}].FIND_IN_SET(feature.id, @_features) <> 0)


         )

      SET @features_list = 
         CASE 
            WHEN (@features_list IS NULL) THEN N''
            ELSE @features_list
         END

      DECLARE
          features CURSOR LOCAL FORWARD_ONLY FOR 
             (
               SELECT feature.id
               FROM [${dbxschemaname}].feature
               WHERE [${dbxschemaname}].FIND_IN_SET(feature.id, @features_list) <> 0
             )

      OPEN features


      WHILE (1 = 1)
      
         BEGIN

            FETCH features
                INTO @featureId
       
            IF @@FETCH_STATUS <> 0
               SET @finished = 1

            IF @finished = 1
               BREAK
            ELSE 
               BEGIN

                  UPDATE [${dbxschemaname}].organisationfeatures
                     SET 
                        featureStatus = N'SID_FEATURE_SUSPENDED'
                  WHERE (organisationfeatures.organisationId = @_organisationId AND organisationfeatures.featureId = @featureId)

                  SET @featuresList = @featureId + N',' + @featuresList

                  CONTINUE

               END

         END

      CLOSE features

      DEALLOCATE features

      SET @featuresList = 
         (
            SELECT substring(@featuresList, 1, 
			case when LEN(@featuresList) = 0 then LEN(@featuresList) 
			else charindex(' ', @featuresList) -1 end)
         )

      SELECT @featuresList

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[organization_actions_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[organization_actions_proc]  
   @_organizationId nvarchar(50),
   @_actionType nvarchar(50),
   @_actionId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
	  declare @select_statement nvarchar(max)
      SET @select_statement = (N'SELECT'+NCHAR(13)+NCHAR(10)+NCHAR(9)+N'feature.id AS featureId,'+NCHAR(13)+NCHAR(10)+N'    feature.name AS featureName,'+NCHAR(13)+NCHAR(10)+N'    feature.description AS featureDescription,'+NCHAR(13)+NCHAR(10)+N'    feature.Status_id AS fiFeatureStatus,'+NCHAR(13)+NCHAR(10)+N'    organisationfeatures.featureStatus AS orgFeatureStatus,'+NCHAR(13)+NCHAR(10)+N'    featureaction.Type_id AS actionType,'+NCHAR(13)+NCHAR(10)+N'    featureaction.id AS actionId,'+NCHAR(13)+NCHAR(10)+N'    featureaction.name AS actionName,'+NCHAR(13)+NCHAR(10)+N'    featureaction.description AS actionDescription,'+NCHAR(9)+NCHAR(13)+NCHAR(10)+N'    featureaction.isAccountLevel AS isAccountLevel,'+NCHAR(13)+NCHAR(10)+NCHAR(9)+N'actionlimit.LimitType_id AS limitTypeId,'+NCHAR(13)+NCHAR(10)+N'    organisationactionlimit.value AS orgLimitValue,'+NCHAR(13)+NCHAR(10)+N'    actionlimit.value AS fiLimitValue'+NCHAR(13)+NCHAR(10)+NCHAR(9)+N'FROM'+NCHAR(13)+NCHAR(10)+NCHAR(9)+NCHAR(9)+N'([${dbxschemaname}].organisationactionlimit'+NCHAR(13)+NCHAR(10)+NCHAR(9)+NCHAR(9)+N'LEFT JOIN [${dbxschemaname}].featureaction ON (featureaction.id = organisationactionlimit.Action_id)'+NCHAR(13)+NCHAR(10)+NCHAR(9)+NCHAR(9)+N'LEFT JOIN [${dbxschemaname}].feature ON (feature.id = featureaction.Feature_id)'+NCHAR(13)+NCHAR(10)+N'        LEFT JOIN [${dbxschemaname}].organisationfeatures ON (organisationfeatures.featureId = feature.id)'+NCHAR(13)+NCHAR(10)+N'        LEFT JOIN [${dbxschemaname}].actionlimit ON (actionlimit.Action_id = organisationactionlimit.Action_id and actionlimit.LimitType_id = organisationactionlimit.LimitType_id))'+NCHAR(13)+NCHAR(10)+NCHAR(9)+N'where organisationactionlimit.Organisation_id = ') + ((QUOTENAME((@_organizationId), ''''))) + (N' and organisationfeatures.organisationId = ') + ((QUOTENAME((@_organizationId), ''''))) + (N'')
     
      IF (@_actionType <> '')
      
         SET @select_statement = (@select_statement) + (N' and featureaction.Type_id = ') + ((QUOTENAME((@_actionType), '''')))
        

      IF (@_actionId <> '')
        
         SET @select_statement = (@select_statement) + (N' and featureaction.id = ') + ((QUOTENAME((@_actionId), '''')))
         EXEC(@select_statement)


   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[reports_messages_received]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[reports_messages_received]  

   @from_date varchar(19),
   @to_date varchar(19),
   @category_id varchar(50),
   @csr_name varchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

	  DECLARE @queryStatement nvarchar(max)
	  
      SET @queryStatement = 'SELECT count(id) messages_received_count FROM [${dbxschemaname}].requestmessage where createdby not in ( select Username from [${dbxschemaname}].systemuser)'
   
      IF (@from_date <> '' AND @from_date <> '')
      SET @queryStatement = (@queryStatement) + (' and createdts >= ') + QUOTENAME(convert(datetime,STUFF(@from_date,11,0,' '),120),'''') + (' and createdts <= ') + QUOTENAME(convert(datetime,STUFF(@to_date,11,0,' '),120),'''')
	  

      IF (@category_id <> '')
      SET @queryStatement = (@queryStatement) + ' and CustomerRequest_id in (select id  from [${dbxschemaname}].customerrequest where RequestCategory_id = ' + ((QUOTENAME((@category_id), '''')))
         

      IF @csr_name <> ''
      
         SET @queryStatement = (@queryStatement) + (' and RepliedBy_id = ') + ((QUOTENAME((@csr_name), '''')))
	    EXEC(@queryStatement)
   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[reports_messages_sent]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[reports_messages_sent]  

   @from_date varchar(19),
   @to_date varchar(19),
   @category_id varchar(50),
   @csr_name varchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
	  declare @queryStatement nvarchar(max)

      SET @queryStatement = 'SELECT count(id) messages_sent_count FROM [${dbxschemaname}].requestmessage where createdby in (select Username from
	  [${dbxschemaname}].systemuser) '
 

      IF @from_date <> '' AND @from_date <> ''
      
         SET @queryStatement = (@queryStatement) + (' and createdts >= ') + QUOTENAME(convert(datetime,STUFF(@from_date,11,0,' '), 120),'''') + (N' and createdts <= ') + QUOTENAME(convert(datetime,STUFF(@to_date,11,0,' '), 120),'''')
      
      IF @category_id <> ''
      
         SET @queryStatement = (@queryStatement) + (' and CustomerRequest_id in (select id from [${dbxschemaname}].customerrequest where RequestCategory_id = ') + ((QUOTENAME((@category_id), ''''))) + ' )'
         
      IF @csr_name <> ''
         
         SET @queryStatement = (@queryStatement) + ('  and RepliedBy_id = ') + ((QUOTENAME((@csr_name), '''')))
	     EXEC(@queryStatement)
   END

GO
/****** Object:  StoredProcedure [${dbxschemaname}].[reports_threads_averageage]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[reports_threads_averageage]  

   @from_date varchar(19),
   @to_date varchar(19),
   @category_id varchar(50),
   @csr_name varchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON

	  declare @queryStatement nvarchar(max)
	  
      SET @queryStatement = N' select'+N' AVG(thread_life_records.thread_life) threads_averageage_count '+N'  from'+N'  ('+N'  select'+N'   DATEDIFF(day, MIN(createdts), MAX(createdts)) thread_life '+N' from'+N'  [${dbxschemaname}].requestmessage '+N'  where'+N' 1=1 '
  
      IF @from_date <> '' AND @from_date <> ''

         SET @queryStatement = (@queryStatement) + (N' '+N'  and createdts >= ') + (QUOTENAME(convert(datetime,STUFF(@from_date,11,0,' '), 120), '''')) + (N' and createdts <= ') + (QUOTENAME(convert(datetime,STUFF(@to_date,11,0,' '), 120), ''''))

      IF @category_id <> ''
      
         SET @queryStatement = (@queryStatement) + (N' '+N'  and CustomerRequest_id in '+N' ('+N' select'+N' id '+N' from'+N'  [${dbxschemaname}].customerrequest '+N'   where'+N' RequestCategory_id = ') + ((QUOTENAME((@category_id), ''''))) + (N'   )'+N'  ')
       
      IF @csr_name <> ''
        
         SET @queryStatement = (@queryStatement) + (N' '+N'  and RepliedBy_id = ') + ((QUOTENAME((@csr_name), '''')))
       
      SET @queryStatement = (@queryStatement) + (N' '+N' group by'+N'  CustomerRequest_id) thread_life_records')
	  EXEC(@queryStatement)

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[reports_threads_new]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[reports_threads_new]  
   @from_date varchar(19),
   @to_date varchar(19),
   @category_id varchar(50),
   @csr_name varchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON

	  declare @queryStatement nvarchar(max)

      SET @queryStatement = N' SELECT'+N' count(id) threads_new_count '+N' from'+N' [${dbxschemaname}].customerrequest '+N' where'+N' Status_id = ''SID_OPEN'''

      IF @from_date <> '' AND @from_date <> ''

         SET @queryStatement = (@queryStatement) + (N' '+N' and createdts >= ') + (QUOTENAME(convert(datetime,STUFF(@from_date,11,0,' '), 120), '''')) + (N' and createdts <= ') + (QUOTENAME(convert(datetime,STUFF(@to_date,11,0,' '), 120), ''''))
      
      IF @category_id <> ''
      
         SET @queryStatement = (@queryStatement) + (N' '+N' and RequestCategory_id = ') + ((QUOTENAME((@category_id), '''')))
       
      IF @csr_name <> ''
       
         SET @queryStatement = (@queryStatement) + (N' '+N' and AssignedTo = ') + ((QUOTENAME((@csr_name), '''')))
         EXEC(@queryStatement)
   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[reports_threads_resolved]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[reports_threads_resolved]  
   @from_date varchar(19),
   @to_date varchar(19),
   @category_id varchar(50),
   @csr_name varchar(50)

AS 
   BEGIN
      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON
	  
	  DECLARE @queryStatement nvarchar(max)

      SET @queryStatement ='SELECT count(id) threads_resolved_count from [${dbxschemaname}].customerrequest where Status_id = ''SID_RESOLVED'''
  
      IF @from_date <> '' AND @from_date <> ''

         SET @queryStatement = (@queryStatement) + ' and createdts >= ' + (QUOTENAME(convert(datetime,STUFF(@from_date,11,0,' '), 120), '''')) + (N' and createdts <= ') + (QUOTENAME(convert(datetime,STUFF(@to_date,11,0,' '), 120),''''))
         
      IF @category_id <> ''
       
         SET @queryStatement = (@queryStatement) + (N' '+N' and RequestCategory_id = ') + ((QUOTENAME((@category_id), '''')))
        
      IF @csr_name <> ''
        
         SET @queryStatement = (@queryStatement) + (N' '+N' and AssignedTo = ') + ((QUOTENAME((@csr_name), '''')))
        
		 EXEC(@queryStatement)
   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[retail_accounts_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [${dbxschemaname}].[requestmessages_proc]  
   @_customerRequestID nvarchar(50),
   @_fetchDraftMessages smallint
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
	  
      SELECT 
         requestmessage.id AS Message_id, 
         
            (
               SELECT count_big(messageattachment.id)
               FROM [${dbxschemaname}].messageattachment
               WHERE messageattachment.RequestMessage_id = requestmessage.id
            ) AS totalAttachments, 
         messageattachment.id AS Messageattachment_id, 
         messageattachment.Media_id AS Media_id, 
         media.Name AS Media_Name, 
         media.Type AS Media_Type, 
         media.Size AS Media_Size, 
         requestmessage.CustomerRequest_id AS CustomerRequest_id, 
         requestmessage.RepliedBy AS RepliedBy, 
         requestmessage.MessageDescription AS MessageDescription, 
         requestmessage.ReplySequence AS ReplySequence, 
         requestmessage.IsRead AS IsRead, 
         requestmessage.createdby AS createdby, 
         requestmessage.RepliedBy_Name AS createdby_name, 
         requestmessage.modifiedby AS modifiedby, 
         requestmessage.createdts AS createdts, 
         requestmessage.lastmodifiedts AS lastmodifiedts, 
         requestmessage.synctimestamp AS synctimestamp, 
         requestmessage.softdeleteflag AS softdeleteflag
      FROM (([${dbxschemaname}].requestmessage 
         LEFT JOIN [${dbxschemaname}].messageattachment 
         ON ((requestmessage.id = messageattachment.RequestMessage_id))) 
         LEFT JOIN [${dbxschemaname}].media 
         ON ((messageattachment.Media_id = media.id)))
      WHERE requestmessage.CustomerRequest_id = @_customerRequestID AND 
         CASE 
            WHEN (@_fetchDraftMessages = 1) THEN N'1'
            ELSE 
               CASE 
                  WHEN (requestmessage.IsRead <> 'draft') THEN 1
                  ELSE 0
               END
         END <> 0
		 ORDER BY requestmessage.ReplySequence ASC

   END
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[retail_accounts_proc]  
   @_customerId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

	  DECLARE @username nvarchar(max)

      SELECT @username =  customer.UserName
            FROM [${dbxschemaname}].customer
            WHERE customer.id = @_customerId

      IF (@username IS NULL)
         SET @username = ''

      SELECT Account_id, AccountName
      FROM [${dbxschemaname}].accounts
      WHERE ISJSON(AccountHolder)<>0 AND (JSON_VALUE(AccountHolder, '$.username') =@username)
   
   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[sp_deleteCoApplicantData]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[sp_deleteCoApplicantData]  

   @sectionsToDelete varchar(200),

   @applicationID varchar(100)
AS 
  BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
	  declare @FirstPart nvarchar(max)
	  declare @Query nvarchar(max)

      SET @FirstPart = N'update questionresponse set softdeleteflag =1 where QuerySectionQuestion_id in (select id from [${dbxschemaname}].querysectionquestion where QuerySection_id IN ('

      SET @Query = (@FirstPart) + (@sectionsToDelete) + (N')) AND questionresponse.QueryResponse_id = ''') + (@applicationID) + (N''';')
      EXEC(@Query)
	
   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[sp_getAllApplications]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[sp_getAllApplications]  
   @User_id nvarchar(50),
   @Status_id nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      IF @Status_id = 'PENDING'
    

         SELECT 
            queryresp.id AS id, 
            queryresp.QueryDefinition_id AS QueryDefinition_id, 
            queryresp.Customer_id AS User_id, 
            queryresp.CoBorrower_id AS CoBorrower_id, 
            queryresp.createdts AS StartDate, 
            queryresp.lastmodifiedts AS LastEditedDate, 
            queryresp.LoanProduct_id AS LoanProduct_id, 
            queryresp.Application_id AS Application_id, 
            loanProd.LoanType_id AS LoanType_id, 
            queryresp.Status_id AS Status, 
            querycoborrower.CoBorrower_Type AS CoBorrower_Type, 
            
               (
                 

                  SELECT CAST((N'[') + (NULL) + (N']') AS nchar(1))
                  FROM [${dbxschemaname}].questionresponse  AS questResp
                  WHERE questResp.QueryResponse_id = queryresp.id AND questResp.QuerySectionQuestion_id IN ( 
                     N'VA_LOAN_AMOUNT', 
                     N'VA_COBORROWER', 
                     N'CCA_CARDLIMIT', 
                     N'PA_LOANAMOUNT', 
                     N'PA_COBORROWER' )
               ) AS QuestionResponse
         FROM 
            [${dbxschemaname}].queryresponse  AS queryresp 
               INNER JOIN [${dbxschemaname}].loanproduct  AS loanProd 
               ON queryresp.LoanProduct_id = loanProd.id 
               LEFT JOIN [${dbxschemaname}].querycoborrower  AS querycoborrower 
               ON queryresp.id = querycoborrower.QueryResponse_id
         WHERE 
            (queryresp.Customer_id = @User_id) AND 
            queryresp.QueryDefinition_id IN ( N'PERSONAL_APPLICATION', N'VEHICLE_APPLICATION', N'CREDIT_CARD_APPLICATION' ) AND 
            queryresp.softdeleteflag = 0 
          UNION ALL
      
          (
           

            SELECT 
               queryresp.id AS id, 
               queryresp.QueryDefinition_id AS QueryDefinition_id, 
               queryresp.Customer_id AS User_id, 
               queryresp.CoBorrower_id AS CoBorrower_id, 
               queryresp.createdts AS StartDate, 
               queryresp.lastmodifiedts AS LastEditedDate, 
               queryresp.LoanProduct_id AS LoanProduct_id, 
               queryresp.Application_id AS Application_id, 
               loanProd.LoanType_id AS LoanType_id, 
               queryresp.Status_id AS STATUS, 
               querycoborrower.CoBorrower_Type AS CoBorrower_Type, 
               
                  (
                    

                     SELECT CAST((N'[') + (NULL) + (N']') AS nchar(10))
                     FROM [${dbxschemaname}].questionresponse  AS questResp
                     WHERE questResp.QueryResponse_id = queryresp.id AND questResp.QuerySectionQuestion_id IN ( 
                        N'VA_LOAN_AMOUNT', 
                        N'VA_COBORROWER', 
                        N'CCA_CARDLIMIT', 
                        N'PA_LOANAMOUNT', 
                        N'PA_COBORROWER' )
                  ) AS QuestionResponse
            FROM 
               [${dbxschemaname}].queryresponse  AS queryresp 
                  INNER JOIN [${dbxschemaname}].loanproduct  AS loanProd 
                  ON queryresp.LoanProduct_id = loanProd.id 
                  LEFT JOIN [${dbxschemaname}].querycoborrower  AS querycoborrower 
                  ON queryresp.id = querycoborrower.QueryResponse_id
            WHERE 
               (queryresp.CoBorrower_id = @User_id) AND 
               queryresp.QueryDefinition_id IN ( N'PERSONAL_APPLICATION', N'VEHICLE_APPLICATION', N'CREDIT_CARD_APPLICATION' ) AND 
               queryresp.softdeleteflag = 0
          )
         


         ORDER BY LastEditedDate DESC
      ELSE 
     

         SELECT 
            queryresp.id AS id, 
            queryresp.QueryDefinition_id AS QueryDefinition_id, 
            queryresp.Customer_id AS User_id, 
            queryresp.CoBorrower_id AS CoBorrower_id, 
            queryresp.createdts AS StartDate, 
            losapps.lastupdatedts AS LastEditedDate, 
            queryresp.LoanProduct_id AS LoanProduct_id, 
            queryresp.Application_id AS Application_id, 
            loanProd.LoanType_id AS LoanType_id, 
            queryresp.Status_id AS Status, 
            querycoborrower.CoBorrower_Type AS CoBorrower_Type, 
            
               (
                  

                  SELECT CAST((N'[') + (NULL) + (N']') AS nchar(10))
                  FROM [${dbxschemaname}].questionresponse  AS questResp
                  WHERE questResp.QueryResponse_id = queryresp.id AND questResp.QuerySectionQuestion_id IN ( 
                     N'VA_LOAN_AMOUNT', 
                     N'VA_COBORROWER', 
                     N'CCA_CARDLIMIT', 
                     N'PA_LOANAMOUNT', 
                     N'PA_COBORROWER' )
               ) AS QuestionResponse
         FROM 
            [${dbxschemaname}].queryresponse  AS queryresp 
               INNER JOIN [${dbxschemaname}].loanproduct  AS loanProd 
               ON queryresp.LoanProduct_id = loanProd.id 
               LEFT JOIN [${dbxschemaname}].querycoborrower  AS querycoborrower 
               ON queryresp.id = querycoborrower.QueryResponse_id 
               LEFT JOIN [${dbxschemaname}].losapplications  AS losapps 
               ON queryresp.id = losapps.QueryResponse_id
         WHERE 
            (queryresp.Customer_id = @User_id) AND 
            queryresp.Status_id = @Status_id AND 
            queryresp.QueryDefinition_id IN ( N'PERSONAL_APPLICATION', N'VEHICLE_APPLICATION', N'CREDIT_CARD_APPLICATION' ) AND 
            queryresp.softdeleteflag = 0 
          UNION ALL
        
          (
            
            SELECT 
               queryresp.id AS id, 
               queryresp.QueryDefinition_id AS QueryDefinition_id, 
               queryresp.Customer_id AS User_id, 
               queryresp.CoBorrower_id AS CoBorrower_id, 
               queryresp.createdts AS StartDate, 
               losapps.lastupdatedts AS LastEditedDate, 
               queryresp.LoanProduct_id AS LoanProduct_id, 
               queryresp.Application_id AS Application_id, 
               loanProd.LoanType_id AS LoanType_id, 
               queryresp.Status_id AS STATUS, 
               querycoborrower.CoBorrower_Type AS CoBorrower_Type, 
               
                  (
                    
                     SELECT CAST((N'[') + (NULL) + (N']') AS nchar(10))
                     FROM [${dbxschemaname}].questionresponse  AS questResp
                     WHERE questResp.QueryResponse_id = queryresp.id AND questResp.QuerySectionQuestion_id IN ( 
                        N'VA_LOAN_AMOUNT', 
                        N'VA_COBORROWER', 
                        N'CCA_CARDLIMIT', 
                        N'PA_LOANAMOUNT', 
                        N'PA_COBORROWER' )
                  ) AS QuestionResponse
            FROM 
               [${dbxschemaname}].queryresponse  AS queryresp 
                  INNER JOIN [${dbxschemaname}].loanproduct  AS loanProd 
                  ON queryresp.LoanProduct_id = loanProd.id 
                  LEFT JOIN [${dbxschemaname}].querycoborrower  AS querycoborrower 
                  ON queryresp.id = querycoborrower.QueryResponse_id 
                  LEFT JOIN [${dbxschemaname}].losapplications  AS losapps 
                  ON queryresp.id = losapps.QueryResponse_id
            WHERE 
               (queryresp.CoBorrower_id = @User_id) AND 
               queryresp.Status_id = @Status_id AND 
               queryresp.QueryDefinition_id IN ( N'PERSONAL_APPLICATION', N'VEHICLE_APPLICATION', N'CREDIT_CARD_APPLICATION' ) AND 
               queryresp.softdeleteflag = 0
          )
         

         ORDER BY LastEditedDate DESC
         

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[sp_getAPRs]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[sp_getAPRs]
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT loantype.id AS LoanType_id, loantype.APRValue AS APRValue, 
         (
            SELECT CAST((N'') + (NULL) + (N'') AS nchar(10))
            FROM [${dbxschemaname}].loanproduct  AS lp
            WHERE lp.LoanType_id = loantype.id
         ) AS LoanProduct
      FROM [${dbxschemaname}].loantype  AS loantype

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[sp_getCustomerPrequalifyPackage]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[sp_getCustomerPrequalifyPackage]  
   @Customer_id nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT 
         cpp.id AS id, 
         cpp.Customer_id AS Customer_id, 
         cpp.PrequalifyPackage_id AS PrequalifyPackage_id, 
         cpp.createdby AS createdby, 
         cpp.modifiedby AS modifiedby, 
         cpp.createdts AS createdts, 
         cpp.lastmodifiedts AS lastmodifiedts, 
         cpp.synctimestamp AS synctimestamp, 
         cpp.softdeleteflag AS softdeleteflag, 
         
            (
               SELECT CAST((N'') + (NULL) + (N'') AS nchar(10))
               FROM [${dbxschemaname}].prequalifypackage  AS pp
               WHERE pp.id = cpp.PrequalifyPackage_id
            ) AS PrequalifyPackage
      FROM [${dbxschemaname}].customerprequalifypackage  AS cpp
      WHERE cpp.Customer_id = @Customer_id
  
   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[sp_getDecisionFailureData]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[sp_getDecisionFailureData]  
   @id_list nvarchar(50),
   @job_id nvarchar(50),
   @top int,
   @skip int
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      IF @id_list IS NOT NULL
         
         SELECT 
            df.id, 
            df.job_id, 
            df.decision_id, 
            df.failureTriggerJob_id, 
            df.exception, 
            cdd.Customer_id, 
            cdd.CreditScore, 
            cdd.EmploymentType, 
            cdd.AnnualIncome, 
            cdd.AccountBalance, 
            cdd.Age, 
            cdd.PrequalifyScore
         FROM 
            [${dbxschemaname}].decisionfailure  AS df 
               INNER JOIN [${dbxschemaname}].customerdpdata  AS cdd 
               ON df.baseAttributeValue = cdd.Customer_id
         WHERE [${dbxschemaname}].FIND_IN_SET(cast(df.id as varchar(50)), @id_list) <> 0
            ORDER BY df.createdts, df.id
            OFFSET @skip ROWS
            FETCH NEXT @top ROWS ONLY
  
      ELSE 
         IF @job_id IS NOT NULL
            SELECT 
               df.id, 
               df.job_id, 
               df.decision_id, 
               cdd.Customer_id, 
               df.exception, 
               cdd.CreditScore, 
               cdd.EmploymentType, 
               cdd.AnnualIncome, 
               cdd.AccountBalance, 
               cdd.Age, 
               cdd.PrequalifyScore
            FROM 
               [${dbxschemaname}].decisionfailure  AS df 
                  INNER JOIN [${dbxschemaname}].customerdpdata  AS cdd 
                  ON df.baseAttributeValue = cdd.Customer_id
            WHERE df.job_id = @job_id
               ORDER BY df.createdts, df.id
               OFFSET @skip ROWS
               FETCH NEXT @top ROWS ONLY
         ELSE 
            SELECT 
               df.id, 
               df.job_id, 
               df.decision_id, 
               df.failureTriggerJob_id, 
               df.exception, 
               cdd.Customer_id, 
               cdd.CreditScore, 
               cdd.EmploymentType, 
               cdd.AnnualIncome, 
               cdd.AccountBalance, 
               cdd.Age, 
               cdd.PrequalifyScore
            FROM 
               [${dbxschemaname}].decisionfailure  AS df 
                  INNER JOIN [${dbxschemaname}].customerdpdata  AS cdd 
                  ON df.baseAttributeValue = cdd.Customer_id
            WHERE df.failureTriggerJob_id IS NULL
               ORDER BY df.createdts, df.id
               OFFSET @skip ROWS
               FETCH NEXT @top ROWS ONLY

   END
GO

CREATE PROCEDURE [${dbxschemaname}].[sp_getLoanAnswers]  
   @QueryResponseID nvarchar(100)
AS 

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT querySecQue.abstractName AS Question_FriendlyName, questionResp.id AS QuestionResponse_id, questionResp.ResponseValue AS Value, questionResp.OptionItem_id AS OptionItem_id
      FROM 
         [${dbxschemaname}].questionresponse  AS questionResp 
            INNER JOIN [${dbxschemaname}].querysectionquestion  AS querySecQue 
            ON querySecQue.id = questionResp.QuerySectionQuestion_id
      WHERE questionResp.QueryResponse_id = @QueryResponseID AND questionResp.softdeleteflag != 1
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[sp_getQueryAnswers]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/
CREATE PROCEDURE [${dbxschemaname}].[sp_getLoanAnswersSectionwise]  

   @QueryResponseID varchar(100),

   @QuerySection_id varchar(100)
AS 

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT querySecQue.abstractName AS Question_FriendlyName, questionResp.id AS QuestionResponse_id, questionResp.ResponseValue AS Value, questionResp.OptionItem_id AS OptionItem_id
      FROM 
         [${dbxschemaname}].questionresponse  AS questionResp 
            INNER JOIN [${dbxschemaname}].querysectionquestion  AS querySecQue 
            ON querySecQue.id = questionResp.QuerySectionQuestion_id
      WHERE 
         questionResp.QueryResponse_id = @QueryResponseID AND 
         questionResp.QuerySection_id = 
         (
            SELECT querysection.id
            FROM [${dbxschemaname}].querysection
            WHERE querysection.abstractname = @QuerySection_id AND querysection.QueryDefinition_id = questionResp.QueryDefinition_id
         ) AND 
         questionResp.softdeleteflag != 1
GO

CREATE PROCEDURE [${dbxschemaname}].[sp_getQueryAnswers]  

   @id varchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
	  declare @FirstPart nvarchar(max)
	  declare @Query nvarchar(max)

      SET @FirstPart = N'SELECT '+N'queryresp.id as id, '+N'queryresp.QueryDefinition_id as QueryDefinition_id,'+N'queryresp.Customer_id as Customer_id,'+
			N'queryresp.LoanProduct_id as LoanProduct_id,'+N'queryresp.Status_id as Status_id,'+N'queryresp.SubmitDate as SubmitDate,'+N'queryresp.ClosingDate as ClosingDate,'+
			N'queryresp.OverallPercentageCompletion as OverallPercentageCompletion,'+N'queryresp.createdby as createdby,'+N'queryresp.modifiedby as modifiedby,'+
			N'queryresp.createdts as createdts,'+N'queryresp.lastmodifiedts as lastmodifiedts,'+N'queryresp.synctimestamp as synctimestamp,'+N'queryresp.softdeleteflag as softdeleteflag,'+
			N'(select questResp.id as ''id'',questResp.QueryDefinition_id as ''QueryDefinition_id'','+
			N'questResp.QueryResponse_id as ''QueryResponse_id'',questResp.QuestionDefinition_id as ''QuestionDefinition_id'','+
			N'questResp.QuerySection_id as ''QuerySection_id'',questResp.ArrayIndex as ''ArrayIndex'',questResp.ResponseValue as ''ResponseValue'','+
			N'questResp.Unit as ''Unit'',questResp.createdby as ''createdby'',questResp.modifiedby as ''modifiedby'',questResp.createdts as ''createdts'','+
			N'questResp.lastmodifiedts as ''lastmodifiedts'',questResp.synctimestamp as ''synctimestamp'',questResp.softdeleteflag as ''softdeleteflag'''+
			N' FROM [${dbxschemaname}].[questionresponse] questResp WHERE questResp.QueryResponse_id = queryresp.id for json auto) AS QuestionResponse,'+
			N'(select cqss.id as ''id'',cqss.User_id as ''User_id'',cqss.QueryResponse_id as ''QueryResponse_id'','+
			N'cqss.QuerySection_id as ''QuerySection_id'',cqss.Status as ''Status'',cqss.PercentageCompletion as ''PercentageCompletion'','+
			N'cqss.LastQuerySectionQuestion_id as ''LastQuerySectionQuestion_id'',cqss.createdby as ''createdby'','+
			N'cqss.modifiedby as ''modifiedby'',cqss.createdts as ''createdts'',cqss.lastmodifiedts as ''lastmodifiedts'','+
			N'cqss.synctimestamp as ''synctimestamp'',cqss.softdeleteflag as ''softdeleteflag'''+
			N' FROM [${dbxschemaname}].[customerquerysectionstatus] cqss WHERE cqss.QueryResponse_id = queryresp.id for json auto) AS CustomerQuerySectionStatus'+
			N' FROM [${dbxschemaname}].[queryresponse] queryresp'+N' WHERE queryresp.id = '
     
      SET @Query = (@FirstPart) + (N'''') + (@id) + (N''';')
	  EXEC(@Query)
   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[sp_getQueryDefinition]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[sp_getQueryDefinition]  

   @id varchar(500),

   @Parent_id varchar(500)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
	  declare @FirstPart nvarchar(max)
      declare @SecondPart nvarchar(max)
	  declare @ThirdPart nvarchar(max)
	  declare @Query nvarchar(max)
      SET @FirstPart = N'SELECT '+NCHAR(13)+NCHAR(10)+N'    queryDef.id AS QueryDefinition_id,'+NCHAR(13)+NCHAR(10)+N'    queryDef.Name AS QueryDefinition_Name,'+NCHAR(13)+NCHAR(10)+N'    querySecQue.id AS QuerySectionQuestion_id,'+NCHAR(13)+NCHAR(10)+N'    querySecQue.ParentQuerySectionQuestion_id AS ParentQuerySectionQuestion_id,'+NCHAR(13)+NCHAR(10)+N'    querySecQue.ParentQuestionOptionValue AS ParentQuestionOptionValue,'+NCHAR(13)+NCHAR(10)+N'    querySecQue.Sequence AS QuerySectionQuestion_Sequence,'+NCHAR(13)+NCHAR(10)+N'    querySecQue.IsRequired AS QuerySectionQuestion_IsRequired,'+NCHAR(13)+NCHAR(10)+N'    querySec.id AS QuerySection_id,'+NCHAR(13)+NCHAR(10)+N'    querySec.Name AS QuerySection_Name,'+NCHAR(13)+NCHAR(10)+N'    querySec.Sequence AS QuerySection_Sequence,'+NCHAR(13)+NCHAR(10)+NCHAR(9)+N'querySec.Parent_id AS QuerySection_Parent_id,'+NCHAR(13)+NCHAR(10)+N'    querySec.ApplicantAllowedTo AS QuerySection_ApplicantAllowedTo,'+NCHAR(13)+NCHAR(10)+N'    querySec.IndCoApplicantAllowedTo AS QuerySection_IndCoApplicantAllowedTo,'+NCHAR(13)+NCHAR(10)+N'    querySec.JointCoApplicantAllowedTo AS QuerySection_JointCoApplicantAllowedTo,'+NCHAR(13)+NCHAR(10)+NCHAR(9)+N'questionDef.Name AS QuestionDefinition_Name,'+NCHAR(13)+NCHAR(10)+N'    questionDef.Label AS QuestionDefinition_Label,'+NCHAR(13)+NCHAR(10)+N'    questionDef.OtherLabel AS QuestionDefinition_OtherLabel,'+NCHAR(13)+NCHAR(10)+N'    questionDef.id AS QuestionDefinition_id,'+NCHAR(13)+NCHAR(10)+N'    optiongroup.Name AS OptionGroup_Name,'+NCHAR(13)+NCHAR(10)+N'    optiongroup.id AS OptionGroup_id,'+NCHAR(13)+NCHAR(10)+N'    optionitem.id AS Optionitem_id,'+NCHAR(13)+NCHAR(10)+N'    optionitem.DefaultValue AS Optionitem_DefaultValue,'+NCHAR(13)+NCHAR(10)+N'    optionitem.Sequence AS Optionitem_Sequence'+NCHAR(13)+NCHAR(10)+N'FROM'+NCHAR(13)+NCHAR(10)+N'    [${dbxschemaname}].querydefinition queryDef'+NCHAR(13)+NCHAR(10)+N'        LEFT JOIN'+NCHAR(13)+NCHAR(10)+N'    [${dbxschemaname}].querysectionquestion querySecQue ON querySecQue.QueryDefinition_id = queryDef.id'+NCHAR(13)+NCHAR(10)+N'        LEFT JOIN'+NCHAR(13)+NCHAR(10)+N'    [${dbxschemaname}].querysection querySec ON querySec.id = querySecQue.QuerySection_id'+NCHAR(13)+NCHAR(10)+N'        LEFT JOIN'+NCHAR(13)+NCHAR(10)+N'    [${dbxschemaname}].questiondefinition questionDef ON querySecQue.QuestionDefinition_id = questionDef.id'+NCHAR(13)+NCHAR(10)+N'        LEFT JOIN'+NCHAR(13)+NCHAR(10)+N'    [${dbxschemaname}].optiongroup optiongroup ON questionDef.OptionGroup_id = optiongroup.id'+NCHAR(13)+NCHAR(10)+N'        LEFT JOIN'+NCHAR(13)+NCHAR(10)+N'    [${dbxschemaname}].optionitem optionitem ON optiongroup.id = optionitem.OptionGroup_id'+NCHAR(13)+NCHAR(10)+N'WHERE'+NCHAR(13)+NCHAR(10)+N'    queryDef.id = '
    
      SET @SecondPart = (@FirstPart) + (N'''') + (@id) + (N'''') + (N' AND querySec.softdeleteflag = FALSE AND querySecQue.softdeleteflag = FALSE ')
      
      IF @Parent_id = 'COAPPLICANT'
         
         SET @ThirdPart = (@SecondPart) + (N'AND querySec.Parent_id IN (''COAPPLICANT'') ')
        
      ELSE 
         IF @Parent_id = 'APPLICANT'
          
            SET @ThirdPart = (@SecondPart) + (N'AND querySec.Parent_id IN (''APPLICANT'', ''GENERAL'') ')
            
         ELSE 
          
            SET @ThirdPart = (@SecondPart) + (N'')
            
      SET @Query = (@ThirdPart) + (N'ORDER BY QuerySection_Sequence,QuerySectionQuestion_Sequence,Optionitem_Sequence')
      EXEC(@Query)
   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[sp_getQueryResponse]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[sp_getQueryResponse]  

   @id varchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
	  declare @FirstPart nvarchar(max)
	  declare @Query nvarchar(max)
      SET @FirstPart = N'SELECT '+N'queryresp.id as id, '+N'queryresp.QueryDefinition_id as QueryDefinition_id,'+N'queryresp.Customer_id as User_id,'+N'queryresp.LoanProduct_id as LoanProduct_id,'+N'queryresp.Status_id as Status_id,'+
			N'queryresp.SubmitDate as SubmitDate,'+N'queryresp.ClosingDate as ClosingDate,'+N'queryresp.OverallPercentageCompletion as OverallPercentageCompletion,'
			+N'queryresp.createdby as createdby,'+N'queryresp.modifiedby as modifiedby,'+N'queryresp.createdts as createdts,'+N'queryresp.lastmodifiedts as lastmodifiedts,'+
			N'queryresp.synctimestamp as synctimestamp,'+N'queryresp.softdeleteflag as softdeleteflag,'+
			N'(select cqss.id as ''id'',cqss.User_id as ''User_id'', cqss.QueryResponse_id as ''QueryResponse_id'','+
			N'cqss.QuerySection_id as ''QuerySection_id'',cqss.Status as ''Status'',cqss.PercentageCompletion as ''PercentageCompletion'','+
			N'cqss.createdby as ''createdby'',cqss.modifiedby as ''modifiedby'',cqss.createdts as ''createdts'','+
			N'cqss.lastmodifiedts as ''lastmodifiedts'',cqss.synctimestamp as ''synctimestamp'',cqss.softdeleteflag as ''softdeleteflag'''+
			N' FROM [${dbxschemaname}].[customerquerysectionstatus] cqss WHERE cqss.QueryResponse_id = ''queryresp.id'' for json auto) AS CustomerQuerySectionStatus'+
			N' FROM [${dbxschemaname}].queryresponse queryresp'+N' WHERE queryresp.id = '
  
      SET @Query = (@FirstPart) + (N'''') + (@id) + (N''';')
	  EXEC(@Query)

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[sp_getQueryResponseApplicationList]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[sp_getQueryResponseApplicationList]  
   @id nvarchar(max),
   @Status_id nvarchar(max)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT 
         queryresp.id AS id, 
         queryresp.QueryDefinition_id AS QueryDefinition_id, 
         queryresp.OverallPercentageCompletion AS progress, 
         queryresp.Status_id AS Status_id, 
         queryresp.createdts AS createdTS, 
         queryresp.lastmodifiedts AS modifiedTS, 
         loantype.Description AS loantype, 
         queryresp.CoBorrower_id AS CoBorrower_id, 
         
            (
               SELECT CAST((N'') + (NULL) + (N'') AS nchar(10))
               FROM [${dbxschemaname}].questionresponse  AS questResp
               WHERE questResp.QueryResponse_id = queryresp.id
            ) AS QuestionResponse
      FROM 
         [${dbxschemaname}].queryresponse  AS queryresp 
            LEFT JOIN [${dbxschemaname}].loanproduct  AS loanproduct 
            ON loanproduct.id = queryresp.LoanProduct_id 
            LEFT JOIN [${dbxschemaname}].loantype  AS loantype 
            ON loantype.id = loanproduct.LoanType_id
      WHERE 
         (queryresp.Customer_id = @id OR queryresp.CoBorrower_id = @id) AND 
         queryresp.Status_id = @Status_id AND 
         (queryresp.QueryDefinition_id = 'PERSONAL_APPLICATION' OR queryresp.QueryDefinition_id = 'CREDIT_CARD_APPLICATION' OR queryresp.QueryDefinition_id = 'VEHICLE_APPLICATION')
         ORDER BY queryresp.lastmodifiedts DESC
 
   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[sp_getQuestionOptions]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[sp_getQuestionOptions]  
   @QueryDefinitionID nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON


      SELECT 
         querySec.Name AS QuerySection_Name, 
         querySec.abstractname AS QuerySection_FriendlyName, 
         querySec.Parent_id AS QuerySection_Parent_id, 
         querySec.ApplicantAllowedTo AS QuerySection_ApplicantAllowedTo, 
         querySec.IndCoApplicantAllowedTo AS QuerySection_IndCoApplicantAllowedTo, 
         querySec.JointCoApplicantAllowedTo AS QuerySection_JointCoApplicantAllowedTo, 
         ISNULL(
            (
               SELECT CAST((N'') + (NULL) + (N'') AS nchar(10))
               FROM [${dbxschemaname}].querysectionquestion  AS querysectionquestion
               WHERE querySec.id = querysectionquestion.QuerySection_id
            ), N'') AS QuestionDefinition
      FROM [${dbxschemaname}].querysection  AS querySec
      WHERE querySec.QueryDefinition_id = @QueryDefinitionID AND querySec.softdeleteflag = 0
         ORDER BY querySec.Sequence



   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[sp_getQuestionOptionsForSection]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].sp_getQuestionOptionsForSection(
@QueryDefinitionID varchar(50),
@QuerySectionID varchar(50))
AS
BEGIN
DECLARE @sub_query1 nvarchar(max)
DECLARE @sub_query2 nvarchar(max)

Select @sub_query1 ='['+ String_agg(CAST(( 'OptionItem_id:'+'"'+optionitem.id+'",'+
		 'OptionItem_DefaultValue:'+'"'+optionitem.DefaultValue+'",'+
		 'OptionItem_Note:'+'"'+optionitem.[Label]) as nvarchar(max)),',')+']'
            FROM 
               [${dbxschemaname}].optionitem optionitem 
            WHERE
               optionitem.OptionGroup_id in 
               (
                  SELECT 
                     OptionGroup_id 
                  FROM
                     [${dbxschemaname}].questiondefinition JOIN [${dbxschemaname}].querysectionquestion querysectionquestion ON
					 questiondefinition.id = querysectionquestion.QuestionDefinition_id JOIN [${dbxschemaname}].querysection querySec ON
					 querySec.id = querysectionquestion.QuerySection_id
                  WHERE
                    querySec.QueryDefinition_id = @QueryDefinitionID
		 AND querySec.abstractname = @QuerySectionID
         AND querySec.softdeleteflag = 0  
               )


SELECT @sub_query2 = '['+String_agg(CAST(('Question_FriendlyName:'+'"'+querysectionquestion.abstractName+'",'+
		 'ParentQuestion_FriendlyName:'+'"'+querysectionquestion.ParentQuerySectionQuestion_AbstractName+'",'+
		 'ParentQuestion_Value:'+'"'+querysectionquestion.ParentQuestionOptionValue+'",'+
		 'OptionItems:'+
         (@sub_query1)
 ) as nvarchar(max)),',')+']' 
      FROM
         [${dbxschemaname}].querysectionquestion querysectionquestion JOIN [${dbxschemaname}].querysection querySec ON
		 querySec.id = querysectionquestion.QuerySection_id
      WHERE
         querySec.QueryDefinition_id = @QueryDefinitionID
		 AND querySec.abstractname = @QuerySectionID
         AND querySec.softdeleteflag = 0 



SELECT 
      querySec.Name AS QuerySection_Name,
      querySec.abstractname AS QuerySection_FriendlyName,
      querySec.Parent_id AS QuerySection_Parent_id,
      querySec.ApplicantAllowedTo AS QuerySection_ApplicantAllowedTo,
      querySec.IndCoApplicantAllowedTo AS QuerySection_IndCoApplicantAllowedTo,
      querySec.JointCoApplicantAllowedTo AS QuerySection_JointCoApplicantAllowedTo,
      ISNULL(@sub_query2,'') AS QuestionDefinition 
      FROM
         [${dbxschemaname}].querysection querySec 
      WHERE
         querySec.QueryDefinition_id = @QueryDefinitionID
		 AND querySec.abstractname = @QuerySectionID
         AND querySec.softdeleteflag = 0 
		 ORDER BY
         querySec.[Sequence]
END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[sp_getQuestionResponse]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[sp_getQuestionResponse]  
  
   @QueryDefinition_id varchar(500),
   
   @User_id varchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
	  declare @basequery nvarchar(max)
	  declare @SubQueryFirst nvarchar(max)
      declare @MainQuery nvarchar(max)
	  declare @SubQuerySecond nvarchar(max)
      SET @basequery = N'SELECT '+NCHAR(13)+NCHAR(10)+NCHAR(13)+NCHAR(10)+N'    quesresp.id as id,'+NCHAR(13)+NCHAR(10)+NCHAR(13)+NCHAR(10)+N'    quesresp.QueryResponse_id as QueryResponse_id,'+NCHAR(13)+NCHAR(10)+NCHAR(13)+NCHAR(10)+N'    quesresp.QueryDefinition_id as QueryDefinition_id,'+NCHAR(13)+NCHAR(10)+NCHAR(13)+NCHAR(10)+N'    quesresp.QuestionDefinition_id as QuestionDefinition_id,'+NCHAR(13)+NCHAR(10)+NCHAR(13)+NCHAR(10)+N'    quesresp.QuerySection_id as QuerySection_id,'+NCHAR(13)+NCHAR(10)+NCHAR(13)+NCHAR(10)+N'    quesresp.ArrayIndex as ArrayIndex,'+NCHAR(13)+NCHAR(10)+NCHAR(13)+NCHAR(10)+N'    quesresp.ResponseValue as ResponseValue,'+NCHAR(13)+NCHAR(10)+NCHAR(13)+NCHAR(10)+N'    quesresp.Unit as Unit,'+NCHAR(13)+NCHAR(10)+NCHAR(13)+NCHAR(10)+N'    quesresp.createdby as createdby,'+NCHAR(13)+NCHAR(10)+NCHAR(13)+NCHAR(10)+N'    quesresp.modifiedby as modifiedby,'+NCHAR(13)+NCHAR(10)+NCHAR(13)+NCHAR(10)+N'    quesresp.createdts as createdts,'+NCHAR(13)+NCHAR(10)+NCHAR(13)+NCHAR(10)+N'    quesresp.lastmodifiedts as lastmodifiedts,'+NCHAR(13)+NCHAR(10)+NCHAR(13)+NCHAR(10)+N'    quesresp.synctimestamp as synctimestamp,'+NCHAR(13)+NCHAR(10)+NCHAR(13)+NCHAR(10)+N'    quesresp.softdeleteflag as softdeleteflag    '+NCHAR(13)+NCHAR(10)+NCHAR(13)+NCHAR(10)+N'    FROM [${dbxschemaname}].questionresponse quesresp '+NCHAR(13)+NCHAR(10)+NCHAR(13)+NCHAR(10)+N'    WHERE QueryResponse_id = (SELECT id from [${dbxschemaname}].queryresponse queryresp WHERE queryresp.QueryDefinition_id = '
    
      SET @SubQueryFirst = N'queryresp.Customer_id = '
     
      SET @SubQuerySecond = N'ORDER BY queryresp.lastmodifiedts DESC '
      
      SET @MainQuery = 
         (@basequery)
          + 
         (N'''')
          + 
         (@QueryDefinition_id)
          + 
         (N'''')
          + 
         (N' AND ')
          + 
         (@SubQueryFirst)
          + 
         (N'''')
          + 
         (@User_id)
          + 
         (N'''')
          + 
         (@SubQuerySecond)
          + 
         (N'OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY)')
      EXEC(@MainQuery)  

   END
GO
CREATE PROCEDURE [${dbxschemaname}].[sp_getSectionLoanAnswers]  
   @QueryResponseID nvarchar(100),
   @QuerySection_id nvarchar(100)
AS 


      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT querySecQue.abstractName AS Question_FriendlyName, questionResp.id AS QuestionResponse_id, questionResp.ResponseValue AS Value, questionResp.OptionItem_id AS OptionItem_id
      FROM 
         [${dbxschemaname}].questionresponse  AS questionResp 
            INNER JOIN [${dbxschemaname}].querysectionquestion  AS querySecQue 
            ON querySecQue.id = questionResp.QuerySectionQuestion_id
      WHERE 
         questionResp.QueryResponse_id = @QueryResponseID AND 
         questionResp.QuerySection_id = 
         (
            SELECT querysection.id
            FROM [${dbxschemaname}].querysection
            WHERE querysection.abstractname = @QuerySection_id AND querysection.QueryDefinition_id = questionResp.QueryDefinition_id
         ) AND 
         questionResp.softdeleteflag != 1
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[sp_update_tabledata]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[sp_update_tabledata]  
 
   @tablename varchar(255),
  

   @primarykeyfield varchar(255),
 

   @primarykeyvalue varchar(255),

   @fieldname varchar(255),
 
   @fieldvalue varchar(255)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
	  declare @MainQuery nvarchar(max)
	  
      SET @MainQuery = 
         (N'update [${dbxschemaname}].')
          + 
         (@tablename)
          + 
         (N' set ')
          + 
         (@fieldname)
          + 
         (N'=')
         + 
         (N'''')
		  + 
         (@fieldvalue)
          + 
         (N'''')
		  +
         (N' where ')
          + 
         (@primarykeyfield)
          + 
         (N'=')
          + 
         (N'''')
          + 
         (@primarykeyvalue)
          + 
         (N'''')
    EXEC(@MainQuery)

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[systemuser_permission_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[systemuser_permission_proc]  
   @_userId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT 
         up.Permission_id AS id, 
         p.Name AS name, 
         p.Status_id AS status, 
         p.PermissionValue AS PermissionValue, 
         p.softdeleteflag AS softdeleteflag
      FROM ([${dbxschemaname}].userpermission  AS up 
         INNER JOIN [${dbxschemaname}].permission  AS p 
         ON (up.Permission_id = p.id))
      WHERE up.User_id = @_userId
       UNION
      SELECT 
         p.id AS id, 
         p.Name AS name, 
         p.Status_id AS status, 
         p.PermissionValue AS PermissionValue, 
         p.softdeleteflag AS softdeleteflag
      FROM ([${dbxschemaname}].userrole  AS ur 
         INNER JOIN [${dbxschemaname}].role  AS r 
         ON (r.id = ur.Role_id) 
         INNER JOIN [${dbxschemaname}].rolepermission  AS rp 
         ON (ur.Role_id = rp.Role_id) 
         INNER JOIN [${dbxschemaname}].permission  AS p 
         ON (rp.Permission_id = p.id))
      WHERE ur.User_id = @_userId

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[transaction_proc_get]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[transaction_proc_get]  


   @transactions_query varchar(max)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
	  declare @stmt nvarchar(max)
      SET @stmt = (N'') + (@transactions_query)
	  EXEC(@stmt)
   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[updateRequestApprovalMatrix_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[updateRequestApprovalMatrix_proc]  
   @_requestId nvarchar(50),
   @_customerId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON
      DECLARE @countOfTemp int

      CREATE TABLE ##temp_request_table
      (
         request_id varchar(50) NULL, 
         approvalMatrixId varchar(50) NULL, 
         requestapprovalMatrixId varchar(50) NULL, 
         receivedApprovals varchar(50) NULL, 
         numberOfApprovals varchar(50) NULL, 
         customerId varchar(50) NULL
      )

      INSERT  INTO ##temp_request_table
         SELECT 
            bb.requestId, 
            am.id AS approvalMatrixId, 
            ram.id AS requestapprovalMatrixId, 
            ram.receivedApprovals, 
            ar.numberOfApprovals, 
            cam.customerId
         FROM [${dbxschemaname}].bbrequest  AS bb 
         JOIN [${dbxschemaname}].requestapprovalmatrix  AS ram on
	        bb.requestId = ram.requestId and CAST(bb.requestId as nvarchar(max)) = @_requestId
         JOIN [${dbxschemaname}].approvalmatrix  AS am on
	        ram.approvalMatrixId = am.id
         JOIN [${dbxschemaname}].customerapprovalmatrix  AS cam on
	        cam.customerId = @_customerId
	     JOIN [${dbxschemaname}].approvalrule  AS ar on
	        am.approvalruleId = ar.id
            
	
         SET @countOfTemp = 
            (
               SELECT count_big(*)
               FROM ##temp_request_table
            )
 
         IF @countOfTemp = 0
            SELECT -1 AS counter
         ELSE 
            BEGIN
               UPDATE ##temp_request_table
               SET 
                  numberOfApprovals = 
                     (
                        SELECT count_big(DISTINCT (cam.customerId))
                        FROM [${dbxschemaname}].customerapprovalmatrix  AS cam
                        WHERE ##temp_request_table.approvalmatrixId = cam.approvalMatrixId
                     )
            WHERE ##temp_request_table.numberOfApprovals = -1          
            UPDATE [${dbxschemaname}].requestapprovalmatrix
               SET 
                  receivedApprovals = [${dbxschemaname}].requestapprovalmatrix.receivedApprovals + 1
            WHERE [${dbxschemaname}].requestapprovalmatrix.id IN 
               (
                  SELECT  requestapprovalMatrixId
                  FROM ##temp_request_table
               )
            UPDATE ##temp_request_table
               SET 
                  receivedApprovals = receivedApprovals + 1
            SELECT count_big(*) AS counter
            FROM ##temp_request_table
            WHERE receivedApprovals >= numberOfApprovals
         END
		 DROP TABLE ##temp_request_table
   END



GO

CREATE PROCEDURE [${dbxschemaname}].[weekend_proc] as
DECLARE @startDate DATE
DECLARE @count int
set @startDate=GETDATE();
set @count = 0;
while @count < 100 
begin
if datepart(weekday,@startDate) = 6 or datepart(weekday,@startDate) = 5
begin
insert into [${dbxschemaname}].holidays(holidayDate,createdBy,modifiedby) values(@startDate,'priya','priya');
set @count = @count+1;
end

set @startDate= DATEADD (day,1,@startDate)
end
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[fetch_bwfile_domesticInternationalCount_proc]
	@_bulkwirefileID VARCHAR(50) 
AS
BEGIN
    IF(@_bulkwirefileID != '' AND @_bulkwirefileID IS NOT NULL)
		SELECT noOfDomesticTransactions,noOfInternationalTransactions FROM [${dbxschemaname}].bulkwirefiles WHERE bulkWireFileID = @_bulkwirefileID;
	ELSE
		SELECT 0 AS noOfDomesticTransactions, 0 AS noOfInternationalTransactions;
	END
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
	
CREATE PROCEDURE [${dbxschemaname}].[fetch_bwtemplate_domesticInternationalCount_proc]
	@_bulkwiretemplateID VARCHAR(50),
	@_bulkwiretemplatelineitemIDs VARCHAR(MAX)
AS
BEGIN
declare @DomCount nvarchar(max)
declare @InternationalCount nvarchar(max)

	IF (@_bulkwiretemplatelineitemIDs != '' AND @_bulkwiretemplatelineitemIDs IS NOT NULL)
	BEGIN
		SELECT @DomCount = COUNT(*) FROM [${dbxschemaname}].bulkwiretemplatelineitems WHERE [${dbxschemaname}].FIND_IN_SET(CAST(bulkWireTemplateLineItemID AS NVARCHAR(max)),@_bulkwiretemplatelineitemIDs)>0 AND bulkWireTransferType = 'Domestic' AND softdeleteflag = 0
		SELECT @InternationalCount = COUNT(*) FROM [${dbxschemaname}].bulkwiretemplatelineitems WHERE [${dbxschemaname}].FIND_IN_SET(CAST(bulkWireTemplateLineItemID AS NVARCHAR(max)),@_bulkwiretemplatelineitemIDs)>0 AND bulkWireTransferType = 'International' AND softdeleteflag = 0
		SELECT @DomCount AS noOfDomesticTransactions, @InternationalCount AS noOfInternationalTransactions;
	END
	ELSE IF(@_bulkwiretemplateID != '' AND @_bulkwiretemplateID IS NOT NULL)
		SELECT noOfDomesticTransactions,noOfInternationalTransactions FROM [${dbxschemaname}].bulkwiretemplate WHERE bulkWireTemplateID = @_bulkwiretemplateID;		
	ELSE
		SELECT 0 AS noOfDomesticTransactions, 0 AS noOfInternationalTransactions;
END
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[fetch_bulkwire_template_transct_detail_proc]
@bulkWireTemplateID varchar(50),
@searchString varchar(50),
@sortByParam varchar(50),
@sortOrder varchar(50),
@pageOffset int,
@pageSize int

AS
BEGIN

	SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
    declare @qfilter nvarchar(max)
	declare @orderBy nvarchar(max)
	declare @paginationQuery nvarchar(max)
	declare @searchQuery nvarchar(max)
	declare @defaultFilter nvarchar(max)
	declare @searchFilter nvarchar(max)
	declare @filter nvarchar(max)
	declare @select_statement nvarchar(max)

    SET @qfilter = 'bulkwiretemplatetransactdetails.bulkWireTemplateID = '''+ @bulkWireTemplateID+ ''' AND bulkwiretemplatetransactdetails.softdeleteflag = 0 '
      
    SET @sortByParam = case when (@sortByParam = '' OR @sortByParam is NULL) then 'transactionDate' else @sortByParam end
    SET @sortByParam = case when (@sortByParam = 'username') then 'firstname,lastname' else @sortByParam end
    SET @sortOrder = case when (@sortOrder = '' OR @sortOrder is NULL) then  'DESC' else @sortOrder end
    
    SET @searchString = case when (@searchString = '' OR @searchString is NULL) then '' else '''%'+@searchString+'%''' end    
    
    SET @orderBy = (' ORDER BY '+ @sortByParam + ' ' + @sortOrder)
    
    SET @paginationQuery = case when (@pageOffset is not NULL OR @pageOffset != '' AND @pageSize is not NULL OR @pageSize != '') then 
							' offset '+CAST(@pageOffset AS nvarchar(max))+'  rows fetch next '+CAST(@pageSize AS nvarchar(max))+' rows only ' else '' end
            
    
    SET @searchQuery = '(bulkwiretemplatetransactdetails.transactionDate LIKE '+@searchString+' OR bulkwiretemplatetransactdetails.totalCountOfTransactions LIKE '+@searchString+' OR  customer.firstname LIKE '+@searchString+' OR customer.lastname LIKE '+@searchString+')'
    


	SET @defaultFilter = @qfilter+ @orderBy + @paginationQuery

	SET @searchFilter = @qfilter+' AND '+@searchQuery+@orderBy

    SET @filter = case when @searchString = '' then @defaultFilter else @searchFilter end

	SET @select_statement = 'SELECT 
	    bulkwiretemplatetransactdetails.bulkWireTransactionID,
		bulkwiretemplatetransactdetails.bulkWireTemplateID, 
		bulkwiretemplatetransactdetails.initiatedBy, 
		bulkwiretemplatetransactdetails.createdts,
		bulkwiretemplatetransactdetails.lastmodifiedts,
		bulkwiretemplatetransactdetails.transactionDate, 
		bulkwiretemplatetransactdetails.totalCountOfTransactions,
		bulkwiretemplatetransactdetails.totalCountOfDomesticTransactions,
		bulkwiretemplatetransactdetails.totalCountOfInternationalTransactions,
		customer.FirstName as firstname,
		customer.LastName as lastname
    FROM
        ([${dbxschemaname}].bulkwiretemplatetransactdetails
		LEFT JOIN [${dbxschemaname}].customer ON ([${dbxschemaname}].bulkwiretemplatetransactdetails.initiatedBy = [${dbxschemaname}].customer.id))
		WHERE '+@filter
	EXEC(@select_statement)
  
END


GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[bulkwiretemplate_create_proc](
	@_bulkwiretemplateValues nvarchar(max),
	@_bulkwiretemplatelineitemValues nvarchar(max)
)
AS
BEGIN

	SET  XACT_ABORT  ON
     SET  NOCOUNT  ON

	DECLARE @index1 INT = 0;
	declare @query1 nvarchar(max)
	declare @msg nvarchar(max) 
	declare @id nvarchar(max)
	declare @val nvarchar(max)
	declare @query2 nvarchar(max)

	SET @_bulkwiretemplateValues = (SELECT REPLACE( @_bulkwiretemplateValues,'"',''''))
	SET @_bulkwiretemplatelineitemValues = (SELECT REPLACE( @_bulkwiretemplatelineitemValues,'"',''''))

	

	BEGIN TRY
	SET @query1 = 'INSERT INTO [${dbxschemaname}].bulkwiretemplate(bulkWireTemplateId,bulkWireTemplateName,noOfTransactions,noOfDomesticTransactions,noOfInternationalTransactions,createdBy,modifiedBy,company_id,createdts,lastmodifiedts,defaultFromAccount,defaultCurrency) VALUES ('+@_bulkwiretemplateValues+')'
	EXEC(@query1) 
	SET @val = [${dbxschemaname}].SUBSTRING_INDEX(@_bulkwiretemplateValues, ',', 1)
	SET @id = (SELECT REPLACE(RTRIM(LTRIM(@val)), '''',''))
	SET @query2 = 'INSERT INTO [${dbxschemaname}].bulkwiretemplatelineitems(bulkWireTemplateID,createdts,lastmodifiedts,swiftCode,bulkWireTransferType,transactionType,internationalRoutingNumber,recipientName,recipientAddressLine1,recipientAddressLine2,recipientCity,recipientState,recipientCountryName,recipientZipCode,recipientBankName,recipientBankAddress1,recipientBankAddress2,recipientBankZipCode,recipientBankcity,recipientBankstate,accountNickname,recipientAccountNumber,routingNumber,createdby,modifiedBy,payeeId,templateRecipientCategory) values '+@_bulkwiretemplatelineitemValues
	EXEC(@query2)
	
	select * from [${dbxschemaname}].bulkwiretemplate where bulkWireTemplateId=@id
	
	END TRY

	 BEGIN catch
	  SET @msg=(SELECT ERROR_MESSAGE())
	  SELECT @msg as ErrorMessage;
	 END catch

END
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].bulkwiretemplate_update_proc(
	@_bulkwiretemplateValues nvarchar(max),
	@_update_bulkwiretemplatelineitemValues nvarchar(max),
	@_insert_bulkwiretemplatelineitemValues nvarchar(max)
)
AS
BEGIN
	DECLARE @msg nvarchar(max)
	DECLARE @query0 nvarchar(max)
	DECLARE @query1 nvarchar(max)
	DECLARE @query2 nvarchar(max)
	DECLARE @bulkWiretemplateID nvarchar(max)
	DECLARE @DomCount int
	DECLARE @InternationalCount int
	DECLARE @totalCount int
	DECLARE @a nvarchar(max)

	BEGIN TRY
	IF (@_bulkwiretemplateValues IS NULL OR @_bulkwiretemplateValues = '' )
		SELECT '@_bulkwiretemplateValues CANNOT BE NULL OR EMPTY';
	ELSE
	BEGIN
	SET @a=@_bulkwiretemplateValues
	if((select count(*) from [${dbxschemaname}].bulkwiretemplate where bulkWireTemplateID=0) = 0)
	SET @query0 = 'INSERT INTO [${dbxschemaname}].bulkwiretemplate(bulkWireTemplateID,bulkWireTemplateName,createdBy,modifiedBy,lastmodifiedts,defaultFromAccount,defaultCurrency) VALUES ('+@_bulkwiretemplateValues+')'
	else
	SET @query0 = 'UPDATE [${dbxschemaname}].bulkwiretemplate set 
	bulkWireTemplateName = SUBSTRING('+@_bulkwiretemplateValues+',len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',2)),len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',1))),
	modifiedBy = SUBSTRING('+@_bulkwiretemplateValues+',len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',4)),len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',1))), 
	lastmodifiedts = SUBSTRING('+@_bulkwiretemplateValues+',len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',5)),len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',1)))), 
	defaultFromAccount = SUBSTRING('+@_bulkwiretemplateValues+',len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',6)),len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',1))), 
	defaultCurrency = SUBSTRING('+@_bulkwiretemplateValues+',len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',7)),len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',1)))'
	EXEC(@query0)

		IF (@_update_bulkwiretemplatelineitemValues IS NOT NULL AND @_update_bulkwiretemplatelineitemValues != '')
		begin
		if((select count(*) from [${dbxschemaname}].bulkwiretemplatelineitems where bulkWireTemplateLineItemID=0) = 0)
			SET @query1 = 'INSERT INTO [${dbxschemaname}].bulkwiretemplatelineitems(bulkWireTemplateLineItemID,bulkWireTemplateID,lastmodifiedts,swiftCode,bulkWireTransferType,transactionType,internationalRoutingNumber,recipientName,recipientAddressLine1,recipientAddressLine2,recipientCity,recipientState,recipientCountryName,recipientZipCode,recipientBankName,recipientBankAddress1,recipientBankAddress2,recipientBankZipCode,recipientBankcity,recipientBankstate,accountNickname,recipientAccountNumber,routingNumber,createdby,modifiedBy,payeeId,templateRecipientCategory) values ('+@_update_bulkwiretemplatelineitemValues+')'
		else
		SET @a=@_update_bulkwiretemplatelineitemValues
			SET @query1 = 'UPDATE [${dbxschemaname}].bulkwiretemplatelineitems set 
			lastmodifiedts = SUBSTRING('+@_update_bulkwiretemplatelineitemValues+',len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',3)),len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',1))), 
			swiftCode = SUBSTRING('+@_update_bulkwiretemplatelineitemValues+',len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',4)),len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',1))), 
			bulkWireTransferType = SUBSTRING('+@_update_bulkwiretemplatelineitemValues+',len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',5)),len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',1))), 
			transactionType = SUBSTRING('+@_update_bulkwiretemplatelineitemValues+',len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',6)),len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',1))), 
			internationalRoutingNumber = SUBSTRING('+@_update_bulkwiretemplatelineitemValues+',len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',7)),len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',1))), 
			recipientName = SUBSTRING('+@_update_bulkwiretemplatelineitemValues+',len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',8)),len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',1))), 
			recipientAddressLine1 = SUBSTRING('+@_update_bulkwiretemplatelineitemValues+',len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',9)),len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',1))), 
			recipientAddressLine2 = SUBSTRING('+@_update_bulkwiretemplatelineitemValues+',len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',10)),len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',1))), 
			recipientCity = SUBSTRING('+@_update_bulkwiretemplatelineitemValues+',len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',11)),len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',1))), 
			recipientState = SUBSTRING('+@_update_bulkwiretemplatelineitemValues+',len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',12)),len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',1))), 
			recipientCountryName = SUBSTRING('+@_update_bulkwiretemplatelineitemValues+',len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',13)),len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',1))), 
			recipientZipCode = SUBSTRING('+@_update_bulkwiretemplatelineitemValues+',len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',14)),len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',1))), 
			recipientBankName = SUBSTRING('+@_update_bulkwiretemplatelineitemValues+',len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',15)),len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',1))), 
			recipientBankAddress1 = SUBSTRING('+@_update_bulkwiretemplatelineitemValues+',len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',16)),len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',1))), 
			recipientBankAddress2 = SUBSTRING('+@_update_bulkwiretemplatelineitemValues+',len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',17)),len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',1))), 
			recipientBankZipCode = SUBSTRING('+@_update_bulkwiretemplatelineitemValues+',len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',18)),len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',1))), 
			recipientBankcity = SUBSTRING('+@_update_bulkwiretemplatelineitemValues+',len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',19)),len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',1))), 
			recipientBankstate = SUBSTRING('+@_update_bulkwiretemplatelineitemValues+',len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',20)),len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',1))), 
			accountNickname = SUBSTRING('+@_update_bulkwiretemplatelineitemValues+',len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',21)),len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',1))), 
			recipientAccountNumber = SUBSTRING('+@_update_bulkwiretemplatelineitemValues+',len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',22)),len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',1))), 
			routingNumber = SUBSTRING('+@_update_bulkwiretemplatelineitemValues+',len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',23)),len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',1))), 
			createdby = SUBSTRING('+@_update_bulkwiretemplatelineitemValues+',len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',24)),len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',1))), 
			modifiedBy = SUBSTRING('+@_update_bulkwiretemplatelineitemValues+',len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',25)),len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',1))), 
			payeeId = SUBSTRING('+@_update_bulkwiretemplatelineitemValues+',len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',26)),len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',1))), 
			templateRecipientCategory = SUBSTRING('+@_update_bulkwiretemplatelineitemValues+',len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',27)),len([${dbxschemaname}].SUBSTRING_INDEX(@a,'','',1)))'
			EXEC(@query1)
		end

		IF (@_insert_bulkwiretemplatelineitemValues IS NOT NULL AND @_insert_bulkwiretemplatelineitemValues != '' )
			SET @query2 = 'INSERT INTO bulkwiretemplatelineitems(bulkWireTemplateID,createdts,lastmodifiedts,swiftCode,bulkWireTransferType,transactionType,internationalRoutingNumber,recipientName,recipientAddressLine1,recipientAddressLine2,recipientCity,recipientState,recipientCountryName,recipientZipCode,recipientBankName,recipientBankAddress1,recipientBankAddress2,recipientBankZipCode,recipientBankcity,recipientBankstate,accountNickname,recipientAccountNumber,routingNumber,createdby,modifiedBy,payeeId,templateRecipientCategory) values ('+@_insert_bulkwiretemplatelineitemValues+')'
			EXEC(@query2)
	
		SET @bulkWiretemplateID = RTRIM(LTRIM ([${dbxschemaname}].SUBSTRING_INDEX(@_bulkwiretemplateValues, ',', 1)))
		SELECT @DomCount= COUNT(*) FROM [${dbxschemaname}].bulkwiretemplatelineitems WHERE bulkWireTemplateID = @bulkWiretemplateID AND bulkWireTransferType = 'Domestic' AND softdeleteflag = 0
		SELECT @InternationalCount=COUNT(*) FROM [${dbxschemaname}].bulkwiretemplatelineitems WHERE bulkWireTemplateID = @bulkWiretemplateID AND bulkWireTransferType = 'International' AND softdeleteflag = 0 
		SET @totalCount = @DomCount + @InternationalCount;
		
		UPDATE [${dbxschemaname}].bulkwiretemplate SET noOfTransactions = @totalCount, noOfDomesticTransactions = @DomCount, noOfInternationalTransactions = @InternationalCount WHERE bulkWireTemplateID = @bulkWiretemplateID
	
		SELECT * FROM [${dbxschemaname}].bulkwiretemplate WHERE bulkWireTemplateID = @bulkWiretemplateID
	END
	END TRY

	 BEGIN CATCH
	  set @msg=(SELECT ERROR_MESSAGE())
	  SELECT @msg as ErrorMessage;
	 END CATCH
END
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].userlinking_proc(
    @_combinedUser VARCHAR(50),
    @_otherUser VARCHAR(50),
    @_isCombinedUserBusiness VARCHAR(50)
)
AS
BEGIN
DECLARE @success_flag int
DECLARE @deviceIds nvarchar(max)
	BEGIN TRANSACTION
	UPDATE [${dbxschemaname}].customerrequest SET Customer_id = @_combinedUser
    WHERE Customer_id = @_otherUser
	UPDATE [${dbxschemaname}].cardaccountrequest SET Customer_id = @_combinedUser
    WHERE Customer_id = @_otherUser
	UPDATE [${dbxschemaname}].notificationcardinfo SET Customer_id = @_combinedUser
    WHERE Customer_id = @_otherUser
	SET @deviceIds = (select String_agg(CAST(id as nvarchar(max)),',') from [${dbxschemaname}].customerdevice where Customer_id = @_combinedUser);
    UPDATE [${dbxschemaname}].customerdevice SET Customer_id = @_combinedUser
    WHERE Customer_id = @_otherUser AND [${dbxschemaname}].FIND_IN_SET(id, @deviceIds) = 0
    
	UPDATE [${dbxschemaname}].payee SET [User_Id] = @_combinedUser WHERE [User_Id] = @_otherUser
    UPDATE [${dbxschemaname}].externalaccount SET [User_Id] = @_combinedUser WHERE [User_Id] = @_otherUser
    UPDATE [${dbxschemaname}].billpaypayee SET customerId = @_combinedUser WHERE customerId = @_otherUser
    UPDATE [${dbxschemaname}].wiretransferspayee SET customerId = @_combinedUser WHERE customerId = @_otherUser
    UPDATE [${dbxschemaname}].interbankpayee SET customerId = @_combinedUser WHERE customerId = @_otherUser
    UPDATE [${dbxschemaname}].intrabankpayee SET customerId = @_combinedUser WHERE customerId = @_otherUser
	UPDATE [${dbxschemaname}].internationalpayee SET customerId = @_combinedUser WHERE customerId = @_otherUser
	IF (@_isCombinedUserBusiness = 'false') 
		UPDATE [${dbxschemaname}].customeraction SET Customer_id = @_combinedUser
		WHERE Customer_id = @_otherUser	
	COMMIT TRANSACTION
END
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].userdelinking_proc(
@_newUser VARCHAR(50),
@_combinedUser VARCHAR(50)
)
AS
BEGIN
DECLARE @groupId nvarchar(max)
BEGIN TRANSACTION
	
    SET @groupId = (select Group_id from [${dbxschemaname}].customergroup join [${dbxschemaname}].membergroup on (customergroup.Group_id  = membergroup.id and membergroup.[Type_id] ='TYPE_ID_RETAIL') where Customer_id= @_combinedUser )
    DELETE FROM [${dbxschemaname}].customergroup WHERE Customer_id= @_combinedUser and Group_id = @groupId
    INSERT INTO [${dbxschemaname}].customergroup (Customer_id, Group_id) VALUES (@_newUser, @groupId)
    UPDATE [${dbxschemaname}].payee SET [User_Id] = @_newUser WHERE [User_Id] = @_combinedUser AND organizationId IS NULL
    UPDATE [${dbxschemaname}].externalaccount SET [User_id] = @_newUser WHERE [User_id] = @_combinedUser AND organizationId IS NULL
    UPDATE [${dbxschemaname}].billpaypayee SET customerId = @_newUser WHERE customerId = @_combinedUser AND isBusinessPayee = '0' AND companyId IS NULL
    UPDATE [${dbxschemaname}].wiretransferspayee SET customerId = @_newUser WHERE customerId = @_combinedUser AND isBusinessPayee = '0' AND companyId IS NULL
    UPDATE [${dbxschemaname}].interbankpayee SET customerId = @_newUser WHERE customerId = @_combinedUser AND isBusinessPayee = '0' AND companyId IS NULL
    UPDATE [${dbxschemaname}].intrabankpayee SET customerId = @_newUser WHERE customerId = @_combinedUser AND isBusinessPayee = '0' AND companyId IS NULL
    UPDATE [${dbxschemaname}].internationalpayee SET customerId = @_newUser WHERE customerId = @_combinedUser AND isBusinessPayee = '0' AND companyId IS NULL    
COMMIT TRANSACTION
END

GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].Update_BulkWireTemplateRecipient_Count(
	@_userID varchar(50),
	@_orgID varchar(50),
	@_payeeID varchar(50)
)
AS
BEGIN
MainLabel:
DECLARE @filterLineItemQuery nvarchar(max)
DECLARE @filterTemplateQuery nvarchar(max)
DECLARE @transferType nvarchar(max)
DECLARE @updateTransferTypeTransactions nvarchar(max)
DECLARE @updatelineitemQuery nvarchar(max)
DECLARE @updateTemplateQuery nvarchar(max)
DECLARE @updateTransferTypeTemplateQuery nvarchar(max)

	IF (@_userID IS NULL OR @_userID = '') AND (@_orgID IS NULL OR @_orgID = '') 
	BEGIN
		SELECT 'Both user_id and company_id cannot be empty' AS Response
		GOTO MainLabel$leave
	END
	IF (@_payeeID IS NULL OR @_payeeID = '') 
	BEGIN
		SELECT 'payeeId cannot be empty' AS Response
		GOTO MainLabel$leave
	END
	
	IF (@_orgID IS NOT NULL AND @_orgID != '')
	BEGIN
		SET @filterLineItemQuery = 'WHERE payeeId = '''+@_payeeID+''' AND bulkWireTemplateID IN (SELECT bulkWireTemplateID FROM [${dbxschemaname}].bulkwiretemplate WHERE company_id = '''+@_orgID+''')'
		SET @filterTemplateQuery = 'WHERE payeeId = '''+@_payeeID+''') AND company_id = '''+@_orgID+''''
	END
	ELSE
	BEGIN
		SET @filterLineItemQuery = 'WHERE payeeId = '''+@_payeeID+''' AND createdby = '''+@_userID+''''
		SET @filterTemplateQuery = 'WHERE payeeId = '''+@_payeeID+''' AND createdby = '''+@_userID+''')'
	END 
	
	SET @transferType = (SELECT TOP(1) bulkWireTransferType FROM [${dbxschemaname}].bulkwiretemplatelineitems WHERE payeeId = @_payeeID)
	
	IF (@transferType = 'Domestic')
		SET @updateTransferTypeTransactions = 'UPDATE [${dbxschemaname}].bulkwiretemplate SET noOfDomesticTransactions = noOfDomesticTransactions - 1 '
	ELSE
		SET @updateTransferTypeTransactions = 'UPDATE [${dbxschemaname}].bulkwiretemplate SET noOfInternationalTransactions = noOfInternationalTransactions - 1 '
	
	SET @updatelineitemQuery = 'UPDATE [${dbxschemaname}].bulkwiretemplatelineitems SET softdeleteflag = 1 '+@filterLineItemQuery
	
	SET @updateTemplateQuery = 'UPDATE [${dbxschemaname}].bulkwiretemplate SET noOfTransactions = noOfTransactions - 1 WHERE bulkWireTemplateID IN (SELECT bulkWireTemplateID FROM [${dbxschemaname}].bulkwiretemplatelineitems '+@filterTemplateQuery+')'
	
	SET @updateTransferTypeTemplateQuery = @updateTransferTypeTransactions+' WHERE bulkWireTemplateID IN (SELECT bulkWireTemplateID FROM [${dbxschemaname}].bulkwiretemplatelineitems '+@filterTemplateQuery+')'
	
	EXECUTE @updatelineitemQuery
	EXECUTE @updateTemplateQuery
	EXECUTE @updateTransferTypeTemplateQuery
	SELECT 'SUCCESS' AS Response;

MainLabel$leave:
END
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].fetch_unselectedPayees_proc(
	@_bulkwiretemplateID VARCHAR(50),
	@User_Id VARCHAR(50),
	@sortByParam VARCHAR(50),
	@sortOrder VARCHAR(50),
	@searchString VARCHAR(50),
	@isDomesticPermitted INT,
	@isInternationalPermitted INT
)
AS
BEGIN
MainLabel:
	DECLARE @searchAndOrderBy nvarchar(max)
	DECLARE @searchQuery nvarchar(max)
	DECLARE @permissionQuery nvarchar(max)
	DECLARE @query1 nvarchar(max)
	SET @sortByParam = case when @sortByParam = '' OR @sortByParam is NULL then 'nickName' else @sortByParam end
	SET @sortOrder = case when @sortOrder = '' OR @sortOrder is NULL then 'DESC' else @sortOrder end
	SET @searchAndOrderBy = ' ORDER BY '+ @sortByParam + ' '+ @sortOrder

	IF (@searchString != '' OR @searchString IS NOT NULL)
	BEGIN
		SET @searchString = '''%'+@searchString+'%'''
		SET @searchQuery = ' AND (name LIKE '+@searchString+' OR bankName LIKE '+@searchString+' OR wireAccountType LIKE '+@searchString+' OR firstName LIKE '+@searchString+' OR lastName LIKE '+@searchString+' OR nickName LIKE '+@searchString+')'
		SET @searchAndOrderBy = @searchQuery+ ' '+ @searchAndOrderBy
	END
	
	SET @permissionQuery = ''
	
	IF (@isDomesticPermitted = 0 AND @isInternationalPermitted = 1)
		SET @permissionQuery = ' AND wireAccountType = ''International'''
	ELSE IF (@isDomesticPermitted = 1 AND @isInternationalPermitted = 0)
		SET @permissionQuery = ' AND wireAccountType = ''Domestic'''
	ELSE IF (@isDomesticPermitted = 1 AND @isInternationalPermitted = 1)
		SET @permissionQuery = ' AND wireAccountType IN (''Domestic'', ''International'') '
	ELSE
	BEGIN
		SELECT ''
		GOTO MainLabel$leave
	END
	
	SET @query1 = 'SELECT * FROM [${dbxschemaname}].payee WHERE isWiredRecepient = 1 AND User_Id = '''+@User_Id+''' AND Id NOT IN (SELECT payeeId FROM [${dbxschemaname}].bulkwiretemplatelineitems WHERE bulkWireTemplateID = '''+@_bulkwiretemplateID+''' AND templateRecipientCategory = ''EXISTINGRECIPIENT'' AND softdeleteflag = 0) '+@permissionQuery+ @searchAndOrderBy
	EXECUTE @query1
MainLabel$leave:
END
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[fetch_bulkwiretemplatelineitems_proc]
@bulkWireTemplateID varchar(50),
@searchString VARCHAR(50),
@sortByParam varchar(50),
@sortOrder varchar(50),
@groupBy varchar(50)
AS
BEGIN

	  SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

DECLARE @lineItemId VARCHAR(50),@payeeId VARCHAR(50),@swiftCodeVal VARCHAR(50),@intRoutingNumVal VARCHAR(50),@recAccntnumVal VARCHAR(50),@routingNumVal VARCHAR(50),@recNicknameVal VARCHAR(50)
DECLARE @recNameVal VARCHAR(100),@recAddLine1Val VARCHAR(100),@recAddLine2Val VARCHAR(100),@recCityVal VARCHAR(100),@recStateVal VARCHAR(100),@recCountryVal VARCHAR(100),@recBankNameVal VARCHAR(100),@recBankAdd1Val VARCHAR(100),@recBankAdd2Val VARCHAR(100),@recBankCityVal VARCHAR(100),@recBankStateVal  VARCHAR(100)
DECLARE @recZipVal VARCHAR(20),@recBankZipVal VARCHAR(20)
DECLARE @isPayeeDeleted int = 1
DECLARE @finished int = 0
DECLARE @queryFilter nvarchar(max)
DECLARE @orderByWithGrouping nvarchar(max)
DECLARE @orderByWithoutGrouping nvarchar(max)
DECLARE @orderBy nvarchar(max)
DECLARE @searchQuery nvarchar(max)
DECLARE @defaultFilter nvarchar(max)
DECLARE @searchFilter nvarchar(max)
DECLARE @filter nvarchar(max)
DECLARE @select_statement nvarchar(max)
DECLARE curTemplateLineItem 
		CURSOR LOCAL FOR 
			SELECT bulkWireTemplateLineItemID FROM [${dbxschemaname}].bulkwiretemplatelineitems
            WHERE bulkWireTemplateID = @bulkWireTemplateID AND softdeleteflag = 0 AND templateRecipientCategory = 'EXISTINGRECIPIENT'
			OPEN curTemplateLineItem
			FETCH NEXT FROM curTemplateLineItem INTO @lineItemId
	while @@FETCH_STATUS = 0
	BEGIN
	    SET @payeeId = (SELECT bulkwiretemplatelineitems.payeeId FROM [${dbxschemaname}].bulkwiretemplatelineitems WHERE bulkwiretemplatelineitems.bulkWireTemplateLineItemID = @lineItemId)
		SET @isPayeeDeleted = (SELECT payee.softDelete FROM [${dbxschemaname}].payee WHERE payee.Id = @payeeId)
		IF(@isPayeeDeleted = 0)
		BEGIN
        SET @swiftCodeVal = (SELECT payee.swiftCode FROM [${dbxschemaname}].payee where payee.Id = @payeeId)
		SET @intRoutingNumVal = (SELECT payee.internationalRoutingCode FROM [${dbxschemaname}].payee where payee.Id = @payeeId)
		SET @recNameVal = (SELECT payee.[name] FROM [${dbxschemaname}].payee where payee.Id = @payeeId)
		SET @recAddLine1Val = (SELECT payee.addressLine1 FROM [${dbxschemaname}].payee where payee.Id = @payeeId)
		SET @recAddLine2Val = (SELECT payee.addressLine2 FROM [${dbxschemaname}].payee where payee.Id = @payeeId)
		SET @recCityVal = (SELECT payee.cityName FROM [${dbxschemaname}].payee where payee.Id = @payeeId)
		SET @recStateVal = (SELECT payee.[state] FROM [${dbxschemaname}].payee where payee.Id = @payeeId)
		SET @recCountryVal = (SELECT payee.country FROM [${dbxschemaname}].payee where payee.Id = @payeeId)
		SET @recZipVal = (SELECT payee.zipCode FROM [${dbxschemaname}].payee where payee.Id = @payeeId)
		SET @recBankNameVal = (SELECT payee.bankName FROM [${dbxschemaname}].payee where payee.Id = @payeeId)
		SET @recBankAdd1Val = (SELECT payee.bankAddressLine1 FROM [${dbxschemaname}].payee where payee.Id = @payeeId)
		SET @recBankAdd2Val = (SELECT payee.bankAddressLine2 FROM [${dbxschemaname}].payee where payee.Id = @payeeId)
		SET @recBankZipVal = (SELECT payee.bankZip FROM [${dbxschemaname}].payee where payee.Id = @payeeId)
		SET @recBankCityVal = (SELECT payee.bankCity FROM [${dbxschemaname}].payee where payee.Id = @payeeId)
		SET @recBankStateVal = (SELECT payee.bankState FROM [${dbxschemaname}].payee where payee.Id = @payeeId)
		SET @recNicknameVal = (SELECT payee.nickName FROM [${dbxschemaname}].payee where payee.Id = @payeeId)
		SET @recAccntnumVal = (SELECT payee.accountNumber FROM [${dbxschemaname}].payee where payee.Id = @payeeId)
		SET @routingNumVal = (SELECT payee.routingCode FROM [${dbxschemaname}].payee where payee.Id = @payeeId)
		UPDATE [${dbxschemaname}].bulkwiretemplatelineitems
        SET swiftCode = @swiftCodeVal,
		internationalRoutingNumber = @intRoutingNumVal,
		recipientName = @recNameVal,
		recipientAddressLine1 = @recAddLine1Val,
		recipientAddressLine2  = @recAddLine2Val,
        recipientCity = @recCityVal,
		recipientState = @recStateVal,
		recipientCountryName = @recCountryVal,
		recipientZipCode = @recZipVal,
		recipientBankName = @recBankNameVal,
		recipientBankAddress1 = @recBankAdd1Val,
        recipientBankAddress2 = @recBankAdd2Val,
		recipientBankZipCode = @recBankZipVal,
		recipientBankcity = @recBankCityVal,
		recipientBankstate = @recBankStateVal,
		accountNickname = @recNicknameVal,
		recipientAccountNumber= @recAccntnumVal,
		routingNumber = @routingNumVal
        WHERE bulkWireTemplateLineItemID = @lineItemId
		END
        ELSE
        UPDATE [${dbxschemaname}].bulkwiretemplatelineitems
        SET softdeleteflag = 1 WHERE bulkWireTemplateLineItemID = @lineItemId
	FETCH NEXT FROM curTemplateLineItem INTO @lineItemId
	END
	CLOSE curTemplateLineItem
	DEALLOCATE curTemplateLineItem
SET @queryFilter = 'bulkwiretemplatelineitems.bulkWireTemplateID = '''+ @bulkWireTemplateID+ ''' AND bulkwiretemplatelineitems.softdeleteflag = 0 '
SET @sortByParam = case when @sortByParam = '' OR @sortByParam is NULL then 'recipientName' else @sortByParam end
SET @sortOrder = case when @sortOrder = '' OR @sortOrder is NULL then 'ASC' else @sortOrder end
SET @searchString = case when @searchString = '' OR @searchString is NULL then '' else '''%'+@searchString+'%''' end
SET @orderByWithGrouping = ' ORDER BY '+@groupBy+','+@sortByParam+' '+@sortOrder
SET @orderByWithoutGrouping = ' ORDER BY '+@sortByParam+' '+@sortOrder
SET @orderBy = case when (@groupBy = '' OR @groupBy is NULL) then @orderByWithoutGrouping else @orderByWithGrouping end
SET @searchQuery = '(bulkwiretemplatelineitems.recipientName LIKE '+@searchString+' OR bulkwiretemplatelineitems.swiftCode LIKE '+@searchString+' OR bulkwiretemplatelineitems.recipientAccountNumber LIKE '+@searchString+' OR bulkwiretemplatelineitems.routingNumber LIKE '+@searchString+' OR bulkwiretemplatelineitems.internationalRoutingNumber LIKE '+@searchString+' OR
bulkwiretemplatelineitems.bulkWireTransferType LIKE '+@searchString+'  OR 
bulkwiretemplatelineitems.transactionType LIKE '+@searchString+'  OR bulkwiretemplatelineitems.recipientAddressLine1 LIKE '+@searchString+'  OR bulkwiretemplatelineitems.recipientAddressLine2 LIKE '+@searchString+'  OR bulkwiretemplatelineitems.recipientCity LIKE '+@searchString+'  OR bulkwiretemplatelineitems.recipientState LIKE '+@searchString+'  OR bulkwiretemplatelineitems.recipientZipCode LIKE '+@searchString+'  OR bulkwiretemplatelineitems.recipientBankName LIKE '+@searchString+'  OR bulkwiretemplatelineitems.recipientBankAddress1 LIKE '+@searchString+'  OR
bulkwiretemplatelineitems.recipientBankAddress2 LIKE '+@searchString+'  OR bulkwiretemplatelineitems.recipientBankcity LIKE '+@searchString+'  OR
bulkwiretemplatelineitems.recipientBankZipCode LIKE '+@searchString+'  OR bulkwiretemplatelineitems.recipientBankstate LIKE '+@searchString+')'
	
SET @defaultFilter = @queryFilter+ @orderBy

SET @searchFilter = @queryFilter+' AND '+@searchQuery+@orderBy

SET @filter = case when @searchString = '' then @defaultFilter else @searchFilter end

SET @select_statement = ('SELECT 
bulkwiretemplatelineitems.bulkWireTemplateLineItemID,
bulkwiretemplatelineitems.swiftCode,
bulkwiretemplatelineitems.recipientCountryName,
bulkwiretemplatelineitems.recipientName,
bulkwiretemplatelineitems.bulkWireTransferType,
bulkwiretemplatelineitems.transactionType,
bulkwiretemplatelineitems.internationalRoutingNumber,
bulkwiretemplatelineitems.recipientAddressLine1,
bulkwiretemplatelineitems.recipientAddressLine2,
bulkwiretemplatelineitems.recipientCity,
bulkwiretemplatelineitems.recipientState,
bulkwiretemplatelineitems.recipientZipCode,
bulkwiretemplatelineitems.recipientBankName,
bulkwiretemplatelineitems.recipientBankAddress1,
bulkwiretemplatelineitems.recipientBankAddress2,
bulkwiretemplatelineitems.recipientBankZipCode,
bulkwiretemplatelineitems.recipientBankcity,
bulkwiretemplatelineitems.recipientBankstate,
bulkwiretemplatelineitems.recipientAccountNumber,
bulkwiretemplatelineitems.routingNumber,
bulkwiretemplatelineitems.templateRecipientCategory,
bulkwiretemplatelineitems.accountNickname,
bulkwiretemplatelineitems.payeeId
FROM [${dbxschemaname}].bulkwiretemplatelineitems WHERE '+ @filter)
EXEC(@select_statement)
END


GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].combinedaccess_deleteAndUpdatePreferences
(@newCustomerId varchar(150),
@accountId_Del varchar(150),
@accountType_Del varchar(150),
@deactivatedCustomerId varchar(150))
AS
BEGIN
DECLARE @msg nvarchar(max)
BEGIN TRY
BEGIN TRANSACTION
 delete from [${dbxschemaname}].dbxcustomeralertentitlement  where  dbxcustomeralertentitlement.Customer_id = @deactivatedCustomerId and dbxcustomeralertentitlement.AccountId = @accountId_Del and dbxcustomeralertentitlement.AccountType = @accountType_Del;
 delete from [${dbxschemaname}].customeralertswitch where  customeralertswitch.Customer_id =  @deactivatedCustomerId and customeralertswitch.AccountID = @accountId_Del  and customeralertswitch.AccountType = @accountType_Del;
 delete from [${dbxschemaname}].customeralertcategorychannel where customeralertcategorychannel.Customer_id = @deactivatedCustomerId and customeralertcategorychannel.AccountId = @accountId_Del and customeralertcategorychannel.AccountType = @accountType_Del;
 update  [${dbxschemaname}].dbxcustomeralertentitlement  set dbxcustomeralertentitlement.Customer_id = @newCustomerId where dbxcustomeralertentitlement.Customer_id =   @deactivatedCustomerId;
 update  [${dbxschemaname}].customeralertswitch  set customeralertswitch.Customer_id = @newCustomerId where customeralertswitch.Customer_id =   @deactivatedCustomerId;
 update  [${dbxschemaname}].customeralertcategorychannel  set customeralertcategorychannel.Customer_id = @newCustomerId where customeralertcategorychannel.Customer_id = @deactivatedCustomerId ;
COMMIT TRANSACTION
END TRY
BEGIN CATCH
ROLLBACK
set @msg=(SELECT ERROR_MESSAGE())
SELECT @msg as ErrorMessage;
END CATCH
END
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].combinedaccess_updatePreferences_Delink(
@newCustomerId varchar(150), 
@accountIds varchar(200),
@combinedCustomerId varchar(150))
AS
BEGIN
DECLARE @msg nvarchar(max)
BEGIN TRY
BEGIN TRANSACTION
 update  [${dbxschemaname}].dbxcustomeralertentitlement  set dbxcustomeralertentitlement.Customer_id = @newCustomerId where dbxcustomeralertentitlement.Customer_id = @combinedCustomerId and [${dbxschemaname}].FIND_IN_SET(dbxcustomeralertentitlement.AccountId ,@accountIds)>0
 update  [${dbxschemaname}].customeralertswitch  set customeralertswitch.Customer_id = @newCustomerId where customeralertswitch.Customer_id = @combinedCustomerId and [${dbxschemaname}].FIND_IN_SET(customeralertswitch.AccountID,@accountIds)>0
 update  [${dbxschemaname}].customeralertcategorychannel  set customeralertcategorychannel.Customer_id = @newCustomerId where customeralertcategorychannel.Customer_id = @combinedCustomerId and [${dbxschemaname}].FIND_IN_SET(customeralertcategorychannel.AccountId,@accountIds)>0
COMMIT TRANSACTION
END TRY
BEGIN CATCH
ROLLBACK
set @msg=(SELECT ERROR_MESSAGE())
SELECT @msg as ErrorMessage;
END CATCH
END
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
