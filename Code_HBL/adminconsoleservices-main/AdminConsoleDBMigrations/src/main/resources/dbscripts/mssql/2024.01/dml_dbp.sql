UPDATE [${dbxschemaname}].[configurations] SET [config_value]='2001' WHERE [config_key] = 'BUSINESS_SECTORID_LIST';
update  [${dbxschemaname}].service_permission_mapper set permissions = 'SCF_Anchor_Dashboard_View,Anchor_Funding_Request_View,View_Invoice_Donut_Chart' where id  = 'm7e34i71-3178-45em-820y-1352kml13907';
update  [${dbxschemaname}].service_permission_mapper set permissions = 'Anchor_Funding_Request_Create,Anchor_Funding_Request_Edit' where id  = 'm7e34i71-3178-45em-820y-1352lpo13907';
update  [${dbxschemaname}].service_permission_mapper set permissions = 'Anchor_Funding_Request_Cancel' where id  = 'm7e34i71-3178-45em-820y-1352tol12344';

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('l7e34i71-3178-22op-ucfv-1352tol12372', 'TradeSupplyFinance', 'AnchorFundingInvoices', 'GetAll', 'SCF_Anchor_Dashboard_View,Anchor_My_Invoices_View');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('h7e34i71-3178-28op-ucfv-1352tol12371', 'TradeSupplyFinance', 'CounterPartyFundingInvoices', 'GetAll', 'SCF_Counterparty_Dashboard_View');

update  [${dbxschemaname}].featureaction set name = 'Need Attention - Approve Invoice' where id  = 'Approve_Invoices_Anchor';
update  [${dbxschemaname}].actiondisplaynamedescription set displayName = 'Need Attention - Approve Invoice' where Locale_id  = 'de-DE';
update  [${dbxschemaname}].actiondisplaynamedescription set displayName = 'Need Attention - Approve Invoice' where Locale_id  = 'en-GB';
update  [${dbxschemaname}].actiondisplaynamedescription set displayName = 'Need Attention - Approve Invoice' where Locale_id  = 'en-US';
update  [${dbxschemaname}].actiondisplaynamedescription set displayName = 'Need Attention - Approve Invoice' where Locale_id  = 'es-ES';
update  [${dbxschemaname}].actiondisplaynamedescription set displayName = 'Need Attention - Approve Invoice' where Locale_id  = 'fr-FR';

INSERT INTO [${dbxschemaname}].[rrole] ([id], [createdts], [lastmodifiedts], [softdeleteflag]) 
VALUES ('Invoice_Pending_Approval_View', GETDATE(), GETDATE(), 0);

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [MFA_id], [isPrimary], [DisplaySequence], [dependency], [softdeleteflag], [accesspolicyId], [actionlevelId]) 
VALUES ('Invoice_Pending_Approval_View', 'SCF_Anchor_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Invoice_Pending_Approval_View', 'Need Attention - View Invoice Pending Approval', 'Need Attention for View Invoice Pending Approval', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) 
VALUES (NEWID(), 'GROUP_ADMINISTRATOR', 'Invoice_Pending_Approval_View', null, null, 'UID11', null, 0);

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) 
VALUES (NEWID(), 'GROUP_CREATOR', 'Invoice_Pending_Approval_View', null, null, 'UID11', null, 0);

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) 
VALUES ('TYPE_ID_BUSINESS', 'Invoice_Pending_Approval_View', null, null, GETDATE(), GETDATE(), GETDATE(), '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) 
VALUES (NEWID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'Invoice_Pending_Approval_View', GETDATE(), GETDATE(), GETDATE(), '0');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) 
VALUES ('Invoice_Pending_Approval_View', 'de-DE','Need Attention - View Invoice Pending Approval','Invoice Pending Approval');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) 
VALUES ('Invoice_Pending_Approval_View', 'en-GB','Need Attention - View Invoice Pending Approval','Invoice Pending Approval');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) 
VALUES ('Invoice_Pending_Approval_View', 'en-US','Need Attention - View Invoice Pending Approval','Invoice Pending Approval');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) 
VALUES ('Invoice_Pending_Approval_View', 'es-ES','Need Attention - View Invoice Pending Approval','Invoice Pending Approval');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) 
VALUES ('Invoice_Pending_Approval_View', 'fr-FR','Need Attention - View Invoice Pending Approval','Invoice Pending Approval');


INSERT INTO [${dbxschemaname}].[rrole] ([id], [createdts], [lastmodifiedts], [softdeleteflag]) 
VALUES ('Reject_Invoices_Anchor', GETDATE(), GETDATE(), 0);

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [MFA_id], [isPrimary], [DisplaySequence], [dependency], [softdeleteflag], [accesspolicyId], [actionlevelId]) 
VALUES ('Reject_Invoices_Anchor', 'SCF_Anchor_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Reject_Invoices_Anchor', 'Need attention - Reject Invoice', 'Reject Invoice Pending Approval', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) 
VALUES (NEWID(), 'GROUP_ADMINISTRATOR', 'Reject_Invoices_Anchor', null, null, 'UID11', null, 0);

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) 
VALUES (NEWID(), 'GROUP_CREATOR', 'Reject_Invoices_Anchor', null, null, 'UID11', null, 0);

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) 
VALUES ('TYPE_ID_BUSINESS', 'Reject_Invoices_Anchor', null, null, GETDATE(), GETDATE(), GETDATE(), '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) 
VALUES (NEWID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'Reject_Invoices_Anchor', GETDATE(), GETDATE(), GETDATE(), '0');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) 
VALUES ('Reject_Invoices_Anchor', 'de-DE','Need attention - Reject Invoice','Reject Invoices');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) 
VALUES ('Reject_Invoices_Anchor', 'en-GB','Need attention - Reject Invoice','Reject Invoices');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) 
VALUES ('Reject_Invoices_Anchor', 'en-US','Need attention - Reject Invoice','Reject Invoices');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) 
VALUES ('Reject_Invoices_Anchor', 'es-ES','Need attention - Reject Invoice','Reject Invoices');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) 
VALUES ('Reject_Invoices_Anchor', 'fr-FR','Need attention - Reject Invoice','Reject Invoices');


INSERT INTO [${dbxschemaname}].[rrole] ([id], [createdts], [lastmodifiedts], [softdeleteflag]) 
VALUES ('View_Invoice_Donut_Chart', GETDATE(), GETDATE(), 0);

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [MFA_id], [isPrimary], [DisplaySequence], [dependency], [softdeleteflag], [accesspolicyId], [actionlevelId]) 
VALUES ('View_Invoice_Donut_Chart', 'SCF_Counterparty_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'View_Invoice_Donut_Chart', 'My Invoices - View Invoice Donut Graph', 'Invoice Donut Graph for Counterparty Dashboard', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) 
VALUES (NEWID(), 'GROUP_ADMINISTRATOR', 'View_Invoice_Donut_Chart', null, null, 'UID11', null, 0);

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) 
VALUES (NEWID(), 'GROUP_CREATOR', 'View_Invoice_Donut_Chart', null, null, 'UID11', null, 0);

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) 
VALUES ('TYPE_ID_BUSINESS', 'View_Invoice_Donut_Chart', null, null, GETDATE(), GETDATE(), GETDATE(), '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) 
VALUES (NEWID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'View_Invoice_Donut_Chart', GETDATE(), GETDATE(), GETDATE(), '0');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) 
VALUES ('View_Invoice_Donut_Chart', 'de-DE','My Invoices - View Invoice Donut Graph','Invoice Donut Graph');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) 
VALUES ('View_Invoice_Donut_Chart', 'en-GB','My Invoices - View Invoice Donut Graph','Invoice Donut Graph');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) 
VALUES ('View_Invoice_Donut_Chart', 'en-US','My Invoices - View Invoice Donut Graph','Invoice Donut Graph');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) 
VALUES ('View_Invoice_Donut_Chart', 'es-ES','My Invoices - View Invoice Donut Graph','Invoice Donut Graph');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) 
VALUES ('View_Invoice_Donut_Chart', 'fr-FR','My Invoices - View Invoice Donut Graph','Invoice Donut Graph');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('u7e34i71-3178-23op-ucfv-1352tol12368', 'TradeSupplyFinance', 'AnchorFundingInvoices', 'Approve', 'Approve_Invoices_Anchor');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('l7e34i71-3178-22op-ucfv-1352tol12369', 'TradeSupplyFinance', 'AnchorFundingInvoices', 'Reject', 'Reject_Invoices_Anchor');

INSERT INTO [${dbxschemaname}].[rrole] ([id], [createdts], [lastmodifiedts], [softdeleteflag]) 
VALUES ('Anchor_My_Invoices_View', GETDATE(), GETDATE(), 0);

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [MFA_id], [isPrimary], [DisplaySequence], [dependency], [softdeleteflag], [accesspolicyId], [actionlevelId]) 
VALUES ('Anchor_My_Invoices_View', 'SCF_Anchor_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Anchor_My_Invoices_View', 'My Invoices - View Invoice', 'View My Invoices in Anchor Dashboard', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) 
VALUES (NEWID(), 'GROUP_ADMINISTRATOR', 'Anchor_My_Invoices_View', null, null, 'UID11', null, 0);

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) 
VALUES (NEWID(), 'GROUP_CREATOR', 'Anchor_My_Invoices_View', null, null, 'UID11', null, 0);

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) 
VALUES ('TYPE_ID_BUSINESS', 'Anchor_My_Invoices_View', null, null, GETDATE(), GETDATE(), GETDATE(), '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) 
VALUES (NEWID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'Anchor_My_Invoices_View', GETDATE(), GETDATE(), GETDATE(), '0');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) 
VALUES ('Anchor_My_Invoices_View', 'de-DE','My Invoices - View Invoice','View My Invoices in Anchor Dashboard');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) 
VALUES ('Anchor_My_Invoices_View', 'en-GB','My Invoices - View Invoice','View My Invoices in Anchor Dashboard');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) 
VALUES ('Anchor_My_Invoices_View', 'en-US','My Invoices - View Invoice','View My Invoices in Anchor Dashboard');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) 
VALUES ('Anchor_My_Invoices_View', 'es-ES','My Invoices - View Invoice','View My Invoices in Anchor Dashboard');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) 
VALUES ('Anchor_My_Invoices_View', 'fr-FR','My Invoices - View Invoice','View My Invoices in Anchor Dashboard');

INSERT INTO [${dbxschemaname}].[customeraction] ([id],[RoleType_id],[Customer_id],[Action_id],[isAllowed] ,[createdby] ,[modifiedby] ,[createdts] ,[lastmodifiedts] ,[synctimestamp] ,[softdeleteflag],[contractId],[coreCustomerId],[featureId],[companyLegalUnit])
VALUES( NEWID(),'TYPE_ID_BUSINESS','1002496540' ,'UPDATE_PRIMARY_ADDRESS',1,'','', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP  , CURRENT_TIMESTAMP  ,0  ,'7321457251','1578660'  ,'PROFILE_SETTINGS' ,'ALL');

INSERT INTO [${dbxschemaname}].[customeraction] ([id],[RoleType_id],[Customer_id],[Action_id],[isAllowed] ,[createdby] ,[modifiedby] ,[createdts] ,[lastmodifiedts] ,[synctimestamp] ,[softdeleteflag],[contractId],[coreCustomerId],[featureId],[companyLegalUnit])
VALUES( NEWID(),'TYPE_ID_BUSINESS','1002496540' ,'UPDATE_PRIMARY_ADDRESS',1,'','', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP  , CURRENT_TIMESTAMP  ,0  ,'7321457251','1425958'  ,'PROFILE_SETTINGS' ,'ALL');

INSERT INTO [${dbxschemaname}].[contractactionlimit] ([id] ,[contractId] ,[coreCustomerId] ,[policyId] ,[featureId] ,[actionId] ,[createdby] ,[modifiedby] ,[createdts] ,[lastmodifiedts] ,[synctimestamp] ,[softdeleteflag] ,[isNewAction] ,[isPortfolio] ,[companyLegalUnit])
VALUES ( NEWID() ,'7321457251' ,'1578660' ,'' ,'PROFILE_SETTINGS' ,'UPDATE_PRIMARY_ADDRESS','','' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,0 ,0 ,'' ,'ALL');

INSERT INTO [${dbxschemaname}].[contractactionlimit] ([id] ,[contractId] ,[coreCustomerId] ,[policyId] ,[featureId] ,[actionId] ,[createdby] ,[modifiedby] ,[createdts] ,[lastmodifiedts] ,[synctimestamp] ,[softdeleteflag] ,[isNewAction] ,[isPortfolio] ,[companyLegalUnit])
VALUES ( NEWID() ,'7321457251' ,'1425958' ,'' ,'PROFILE_SETTINGS' ,'UPDATE_PRIMARY_ADDRESS','','' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,0 ,0 ,'' ,'ALL');

UPDATE [${dbxschemaname}].[customer] set defaultLegalEntity = 'GB0010001' where id ='1002496540';
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('w7e34i71-3178-28op-ucfv-3244sdf54265', 'TradeSupplyFinance', 'AnchorFundingInvoices', 'DownloadSampleXlsx', 'Upload_Invoice_Anchor');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('w7e34i71-3178-23op-ucfv-3244sdf54266', 'TradeSupplyFinance', 'AnchorFundingInvoices', 'ParseBulkUploadedXlsx', 'Upload_Invoice_Anchor');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('w7e34i71-3178-22op-ucfv-3244sdf54267', 'TradeSupplyFinance', 'AnchorFundingInvoices', 'CreateBulkInvoices', 'Upload_Invoice_Anchor');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('w7e34i71-3178-27op-ucfv-3244sdf54268', 'TradeSupplyFinance', 'CounterPartyFundingInvoices', 'DownloadSampleXlsx', 'Upload_Invoice_Counterparty');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('w7e34i71-3178-29op-ucfv-3244sdf54269', 'TradeSupplyFinance', 'CounterPartyFundingInvoices', 'ParseBulkUploadedXlsx', 'Upload_Invoice_Counterparty');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('w7e34i71-3178-28op-ucfv-3244sdf54270', 'TradeSupplyFinance', 'CounterPartyFundingInvoices', 'CreateBulkInvoices', 'Upload_Invoice_Counterparty');

update  [${dbxschemaname}].featureaction set accesspolicyId = 'CREATE' where id  = 'CHANGE_REPAYMENT_ACCOUNT-CREATE';
update  [${dbxschemaname}].featureaction set accesspolicyId = 'CREATE' where id  = 'CHANGE_REPAYMENT_DAY-CREATE';

INSERT INTO [${dbxschemaname}].[rrole] ([id], [createdts], [lastmodifiedts], [softdeleteflag]) VALUES ('Payment_Allocations_Anchor_View', GETDATE(), GETDATE(), 0);

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [MFA_id], [isPrimary], [DisplaySequence], [dependency], [softdeleteflag], [accesspolicyId], [actionlevelId]) VALUES ('Payment_Allocations_Anchor_View', 'SCF_Anchor_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Payment_Allocations_Anchor_View', 'Payment Allocation tab : View Payment Allocation', 'View payment allocation tab of anchor dashboard', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) VALUES (CONVERT(NVARCHAR(36), NEWID()), 'GROUP_ADMINISTRATOR', 'Payment_Allocations_Anchor_View', null, null, 'UID11', null, 0);
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) VALUES (CONVERT(NVARCHAR(36), NEWID()), 'GROUP_CREATOR', 'Payment_Allocations_Anchor_View', null, null, 'UID11', null, 0);

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('TYPE_ID_BUSINESS', 'Payment_Allocations_Anchor_View', null, null, GETDATE(), GETDATE(), GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (CONVERT(NVARCHAR(36), NEWID()), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'Payment_Allocations_Anchor_View', GETDATE(), GETDATE(), GETDATE(), '0');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocations_Anchor_View', 'de-DE', 'Payment Allocation tab : View Payment Allocation', 'View payment allocation tab of anchor dashboard');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocations_Anchor_View', 'en-GB', 'Payment Allocation tab : View Payment Allocation', 'View payment allocation tab of anchor dashboard');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocations_Anchor_View', 'en-US', 'Payment Allocation tab : View Payment Allocation', 'View payment allocation tab of anchor dashboard');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocations_Anchor_View', 'es-ES', 'Payment Allocation tab : View Payment Allocation', 'View payment allocation tab of anchor dashboard');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocations_Anchor_View', 'fr-FR', 'Payment Allocation tab : View Payment Allocation', 'View payment allocation tab of anchor dashboard');

INSERT INTO [${dbxschemaname}].[rrole] ([id], [createdts], [lastmodifiedts], [softdeleteflag]) VALUES ('Payment_Allocation_Anchor_Edit_Submitted', GETDATE(), GETDATE(), 0);

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [MFA_id], [isPrimary], [DisplaySequence], [dependency], [softdeleteflag], [accesspolicyId], [actionlevelId]) VALUES ('Payment_Allocation_Anchor_Edit_Submitted', 'SCF_Anchor_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Payment_Allocation_Anchor_Edit_Submitted', 'Payment Allocation tab : Edit Payment Allocation', 'Edit already submitted allocation Documentation for a particular cash receipt', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) VALUES (CONVERT(NVARCHAR(36), NEWID()), 'GROUP_ADMINISTRATOR', 'Payment_Allocation_Anchor_Edit_Submitted', null, null, 'UID11', null, 0);
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) VALUES (CONVERT(NVARCHAR(36), NEWID()), 'GROUP_CREATOR', 'Payment_Allocation_Anchor_Edit_Submitted', null, null, 'UID11', null, 0);

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('TYPE_ID_BUSINESS', 'Payment_Allocation_Anchor_Edit_Submitted', null, null, GETDATE(), GETDATE(), GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (CONVERT(NVARCHAR(36), NEWID()), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'Payment_Allocation_Anchor_Edit_Submitted', GETDATE(), GETDATE(), GETDATE(), '0');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Anchor_Edit_Submitted', 'de-DE', 'Payment Allocation tab : Edit Payment Allocation', 'Edit already submitted allocation Documentation for a particular cash receipt');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Anchor_Edit_Submitted', 'en-GB', 'Payment Allocation tab : Edit Payment Allocation', 'Edit already submitted allocation Documentation for a particular cash receipt');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Anchor_Edit_Submitted', 'en-US', 'Payment Allocation tab : Edit Payment Allocation', 'Edit already submitted allocation Documentation for a particular cash receipt');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Anchor_Edit_Submitted', 'es-ES', 'Payment Allocation tab : Edit Payment Allocation', 'Edit already submitted allocation Documentation for a particular cash receipt');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Anchor_Edit_Submitted', 'fr-FR', 'Payment Allocation tab : Edit Payment Allocation', 'Edit already submitted allocation Documentation for a particular cash receipt');

INSERT INTO [${dbxschemaname}].[rrole] ([id], [createdts], [lastmodifiedts], [softdeleteflag]) VALUES ('Payment_Allocation_Anchor_Request_Documentation', GETDATE(), GETDATE(), 0);

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [MFA_id], [isPrimary], [DisplaySequence], [dependency], [softdeleteflag], [accesspolicyId], [actionlevelId]) VALUES ('Payment_Allocation_Anchor_Request_Documentation', 'SCF_Anchor_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Payment_Allocation_Anchor_Request_Documentation', 'Pending Payment Allocation : Request Documentation for Allocation', 'Request to counterparty for submission of payment allocation documents', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) VALUES (CONVERT(NVARCHAR(36), NEWID()), 'GROUP_ADMINISTRATOR', 'Payment_Allocation_Anchor_Request_Documentation', null, null, 'UID11', null, 0);
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) VALUES (CONVERT(NVARCHAR(36), NEWID()), 'GROUP_CREATOR', 'Payment_Allocation_Anchor_Request_Documentation', null, null, 'UID11', null, 0);

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('TYPE_ID_BUSINESS', 'Payment_Allocation_Anchor_Request_Documentation', null, null, GETDATE(), GETDATE(), GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (CONVERT(NVARCHAR(36), NEWID()), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'Payment_Allocation_Anchor_Request_Documentation', GETDATE(), GETDATE(), GETDATE(), '0');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Anchor_Request_Documentation', 'de-DE', 'Pending Payment Allocation : Request Documentation for Allocation', 'Request to counterparty for submission of payment allocation documents');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Anchor_Request_Documentation', 'en-GB', 'Pending Payment Allocation : Request Documentation for Allocation', 'Request to counterparty for submission of payment allocation documents');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Anchor_Request_Documentation', 'en-US', 'Pending Payment Allocation : Request Documentation for Allocation', 'Request to counterparty for submission of payment allocation documents');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Anchor_Request_Documentation', 'es-ES', 'Pending Payment Allocation : Request Documentation for Allocation', 'Request to counterparty for submission of payment allocation documents');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Anchor_Request_Documentation', 'fr-FR', 'Pending Payment Allocation : Request Documentation for Allocation', 'Request to counterparty for submission of payment allocation documents');

INSERT INTO [${dbxschemaname}].[rrole] ([id], [createdts], [lastmodifiedts], [softdeleteflag]) VALUES ('Payment_Allocation_Anchor_Submit_Documentation', GETDATE(), GETDATE(), 0);

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [MFA_id], [isPrimary], [DisplaySequence], [dependency], [softdeleteflag], [accesspolicyId], [actionlevelId]) VALUES ('Payment_Allocation_Anchor_Submit_Documentation', 'SCF_Anchor_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Payment_Allocation_Anchor_Submit_Documentation', 'Pending Payment Allocation : Submit Documentation for Allocation', 'Submit Documentation for a particular cash receipt', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) VALUES (CONVERT(NVARCHAR(36), NEWID()), 'GROUP_ADMINISTRATOR', 'Payment_Allocation_Anchor_Submit_Documentation', null, null, 'UID11', null, 0);
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) VALUES (CONVERT(NVARCHAR(36), NEWID()), 'GROUP_CREATOR', 'Payment_Allocation_Anchor_Submit_Documentation', null, null, 'UID11', null, 0);

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('TYPE_ID_BUSINESS', 'Payment_Allocation_Anchor_Submit_Documentation', null, null, GETDATE(), GETDATE(), GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (CONVERT(NVARCHAR(36), NEWID()), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'Payment_Allocation_Anchor_Submit_Documentation', GETDATE(), GETDATE(), GETDATE(), '0');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Anchor_Submit_Documentation', 'de-DE', 'Pending Payment Allocation : Submit Documentation for Allocation', 'Submit Documentation for a particular cash receipt');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Anchor_Submit_Documentation', 'en-GB', 'Pending Payment Allocation : Submit Documentation for Allocation', 'Submit Documentation for a particular cash receipt');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Anchor_Submit_Documentation', 'en-US', 'Pending Payment Allocation : Submit Documentation for Allocation', 'Submit Documentation for a particular cash receipt');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Anchor_Submit_Documentation', 'es-ES', 'Pending Payment Allocation : Submit Documentation for Allocation', 'Submit Documentation for a particular cash receipt');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Anchor_Submit_Documentation', 'fr-FR', 'Pending Payment Allocation : Submit Documentation for Allocation', 'Submit Documentation for a particular cash receipt');

INSERT INTO [${dbxschemaname}].[rrole] ([id], [createdts], [lastmodifiedts], [softdeleteflag]) VALUES ('Payment_Allocation_Anchor_View_Pending', GETDATE(), GETDATE(), 0);

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [MFA_id], [isPrimary], [DisplaySequence], [dependency], [softdeleteflag], [accesspolicyId], [actionlevelId]) VALUES ('Payment_Allocation_Anchor_View_Pending', 'SCF_Anchor_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Payment_Allocation_Anchor_View_Pending', 'Need Attention : View Pending Payment Allocation', 'View new cash receipts details received', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) VALUES (CONVERT(NVARCHAR(36), NEWID()), 'GROUP_ADMINISTRATOR', 'Payment_Allocation_Anchor_View_Pending', null, null, 'UID11', null, 0);
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) VALUES (CONVERT(NVARCHAR(36), NEWID()), 'GROUP_CREATOR', 'Payment_Allocation_Anchor_View_Pending', null, null, 'UID11', null, 0);

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('TYPE_ID_BUSINESS', 'Payment_Allocation_Anchor_View_Pending', null, null, GETDATE(), GETDATE(), GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (CONVERT(NVARCHAR(36), NEWID()), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'Payment_Allocation_Anchor_View_Pending', GETDATE(), GETDATE(), GETDATE(), '0');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Anchor_View_Pending', 'de-DE', 'Need Attention : View Pending Payment Allocation', 'View new cash receipts details received');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Anchor_View_Pending', 'en-GB', 'Need Attention : View Pending Payment Allocation', 'View new cash receipts details received');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Anchor_View_Pending', 'en-US', 'Need Attention : View Pending Payment Allocation', 'View new cash receipts details received');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Anchor_View_Pending', 'es-ES', 'Need Attention : View Pending Payment Allocation', 'View new cash receipts details received');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Anchor_View_Pending', 'fr-FR', 'Need Attention : View Pending Payment Allocation', 'View new cash receipts details received');

INSERT INTO [${dbxschemaname}].[rrole] ([id], [createdts], [lastmodifiedts], [softdeleteflag]) VALUES ('Payment_Allocation_Anchor_View_Submitted', GETDATE(), GETDATE(), 0);

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [MFA_id], [isPrimary], [DisplaySequence], [dependency], [softdeleteflag], [accesspolicyId], [actionlevelId]) VALUES ('Payment_Allocation_Anchor_View_Submitted', 'SCF_Anchor_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Payment_Allocation_Anchor_View_Submitted', 'Payment Allocation tab : View Submitted Allocation', 'View allocation documents submitted by counterparty', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) VALUES (CONVERT(NVARCHAR(36), NEWID()), 'GROUP_ADMINISTRATOR', 'Payment_Allocation_Anchor_View_Submitted', null, null, 'UID11', null, 0);
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) VALUES (CONVERT(NVARCHAR(36), NEWID()), 'GROUP_CREATOR', 'Payment_Allocation_Anchor_View_Submitted', null, null, 'UID11', null, 0);

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('TYPE_ID_BUSINESS', 'Payment_Allocation_Anchor_View_Submitted', null, null, GETDATE(), GETDATE(), GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (CONVERT(NVARCHAR(36), NEWID()), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'Payment_Allocation_Anchor_View_Submitted', GETDATE(), GETDATE(), GETDATE(), '0');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Anchor_View_Submitted', 'de-DE', 'Payment Allocation tab : View Submitted Allocation', 'View allocation documents submitted by counterparty');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Anchor_View_Submitted', 'en-GB', 'Payment Allocation tab : View Submitted Allocation', 'View allocation documents submitted by counterparty');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Anchor_View_Submitted', 'en-US', 'Payment Allocation tab : View Submitted Allocation', 'View allocation documents submitted by counterparty');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Anchor_View_Submitted', 'es-ES', 'Payment Allocation tab : View Submitted Allocation', 'View allocation documents submitted by counterparty');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Anchor_View_Submitted', 'fr-FR', 'Payment Allocation tab : View Submitted Allocation', 'View allocation documents submitted by counterparty');

INSERT INTO [${dbxschemaname}].[rrole] ([id], [createdts], [lastmodifiedts], [softdeleteflag]) VALUES ('Payment_Allocation_Counterparty_Edit_Submitted', GETDATE(), GETDATE(), 0);

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [MFA_id], [isPrimary], [DisplaySequence], [dependency], [softdeleteflag], [accesspolicyId], [actionlevelId]) VALUES ('Payment_Allocation_Counterparty_Edit_Submitted', 'SCF_Counterparty_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Payment_Allocation_Counterparty_Edit_Submitted', 'Payment Allocation Tab : Edit Submitted Allocation', 'Edit already submitted allocation Documentation for a particular cash receipt', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) VALUES (CONVERT(NVARCHAR(36), NEWID()), 'GROUP_ADMINISTRATOR', 'Payment_Allocation_Counterparty_Edit_Submitted', null, null, 'UID11', null, 0);
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) VALUES (CONVERT(NVARCHAR(36), NEWID()), 'GROUP_CREATOR', 'Payment_Allocation_Counterparty_Edit_Submitted', null, null, 'UID11', null, 0);

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('TYPE_ID_BUSINESS', 'Payment_Allocation_Counterparty_Edit_Submitted', null, null, GETDATE(), GETDATE(), GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (CONVERT(NVARCHAR(36), NEWID()), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'Payment_Allocation_Counterparty_Edit_Submitted', GETDATE(), GETDATE(), GETDATE(), '0');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Counterparty_Edit_Submitted', 'de-DE', 'Payment Allocation Tab : Edit Submitted Allocation', 'Edit already submitted allocation Documentation for a particular cash receipt');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Counterparty_Edit_Submitted', 'en-GB', 'Payment Allocation Tab : Edit Submitted Allocation', 'Edit already submitted allocation Documentation for a particular cash receipt');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Counterparty_Edit_Submitted', 'en-US', 'Payment Allocation Tab : Edit Submitted Allocation', 'Edit already submitted allocation Documentation for a particular cash receipt');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Counterparty_Edit_Submitted', 'es-ES', 'Payment Allocation Tab : Edit Submitted Allocation', 'Edit already submitted allocation Documentation for a particular cash receipt');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Counterparty_Edit_Submitted', 'fr-FR', 'Payment Allocation Tab : Edit Submitted Allocation', 'Edit already submitted allocation Documentation for a particular cash receipt');

INSERT INTO [${dbxschemaname}].[rrole] ([id], [createdts], [lastmodifiedts], [softdeleteflag]) VALUES ('Payment_Allocation_Counterparty_Submit_Documentation', GETDATE(), GETDATE(), 0);

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [MFA_id], [isPrimary], [DisplaySequence], [dependency], [softdeleteflag], [accesspolicyId], [actionlevelId]) VALUES ('Payment_Allocation_Counterparty_Submit_Documentation', 'SCF_Counterparty_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Payment_Allocation_Counterparty_Submit_Documentation', 'Pending Payment Allocation : Submit Documentation for Allocation', 'Submit allocation Documentation', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) VALUES (CONVERT(NVARCHAR(36), NEWID()), 'GROUP_ADMINISTRATOR', 'Payment_Allocation_Counterparty_Submit_Documentation', null, null, 'UID11', null, 0);
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) VALUES (CONVERT(NVARCHAR(36), NEWID()), 'GROUP_CREATOR', 'Payment_Allocation_Counterparty_Submit_Documentation', null, null, 'UID11', null, 0);

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('TYPE_ID_BUSINESS', 'Payment_Allocation_Counterparty_Submit_Documentation', null, null, GETDATE(), GETDATE(), GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (CONVERT(NVARCHAR(36), NEWID()), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'Payment_Allocation_Counterparty_Submit_Documentation', GETDATE(), GETDATE(), GETDATE(), '0');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Counterparty_Submit_Documentation', 'de-DE', 'Pending Payment Allocation : Submit Documentation for Allocation', 'Submit allocation Documentation');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Counterparty_Submit_Documentation', 'en-GB', 'Pending Payment Allocation : Submit Documentation for Allocation', 'Submit allocation Documentation');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Counterparty_Submit_Documentation', 'en-US', 'Pending Payment Allocation : Submit Documentation for Allocation', 'Submit allocation Documentation');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Counterparty_Submit_Documentation', 'es-ES', 'Pending Payment Allocation : Submit Documentation for Allocation', 'Submit allocation Documentation');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Counterparty_Submit_Documentation', 'fr-FR', 'Pending Payment Allocation : Submit Documentation for Allocation', 'Submit allocation Documentation');

INSERT INTO [${dbxschemaname}].[rrole] ([id], [createdts], [lastmodifiedts], [softdeleteflag]) VALUES ('Payment_Allocation_Counterparty_View', GETDATE(), GETDATE(), 0);

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [MFA_id], [isPrimary], [DisplaySequence], [dependency], [softdeleteflag], [accesspolicyId], [actionlevelId]) VALUES ('Payment_Allocation_Counterparty_View', 'SCF_Counterparty_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Payment_Allocation_Counterparty_View', 'Need Attention : View Payment Allocation', 'View cash receipts details', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) VALUES (CONVERT(NVARCHAR(36), NEWID()), 'GROUP_ADMINISTRATOR', 'Payment_Allocation_Counterparty_View', null, null, 'UID11', null, 0);
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) VALUES (CONVERT(NVARCHAR(36), NEWID()), 'GROUP_CREATOR', 'Payment_Allocation_Counterparty_View', null, null, 'UID11', null, 0);

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('TYPE_ID_BUSINESS', 'Payment_Allocation_Counterparty_View', null, null, GETDATE(), GETDATE(), GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (CONVERT(NVARCHAR(36), NEWID()), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'Payment_Allocation_Counterparty_View', GETDATE(), GETDATE(), GETDATE(), '0');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Counterparty_View', 'de-DE', 'Need Attention : View Payment Allocation', 'View cash receipts details');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Counterparty_View', 'en-GB', 'Need Attention : View Payment Allocation', 'View cash receipts details');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Counterparty_View', 'en-US', 'Need Attention : View Payment Allocation', 'View cash receipts details');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Counterparty_View', 'es-ES', 'Need Attention : View Payment Allocation', 'View cash receipts details');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Counterparty_View', 'fr-FR', 'Need Attention : View Payment Allocation', 'View cash receipts details');

INSERT INTO [${dbxschemaname}].[rrole] ([id], [createdts], [lastmodifiedts], [softdeleteflag]) VALUES ('Payment_Allocation_Counterparty_View_Submitted', GETDATE(), GETDATE(), 0);

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [MFA_id], [isPrimary], [DisplaySequence], [dependency], [softdeleteflag], [accesspolicyId], [actionlevelId]) VALUES ('Payment_Allocation_Counterparty_View_Submitted', 'SCF_Counterparty_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Payment_Allocation_Counterparty_View_Submitted', 'Payment Allocation Tab : View Payment Allocation', 'View submitted allocation Documentation for a particular cash receipt', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) VALUES (CONVERT(NVARCHAR(36), NEWID()), 'GROUP_ADMINISTRATOR', 'Payment_Allocation_Counterparty_View_Submitted', null, null, 'UID11', null, 0);
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) VALUES (CONVERT(NVARCHAR(36), NEWID()), 'GROUP_CREATOR', 'Payment_Allocation_Counterparty_View_Submitted', null, null, 'UID11', null, 0);

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('TYPE_ID_BUSINESS', 'Payment_Allocation_Counterparty_View_Submitted', null, null, GETDATE(), GETDATE(), GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (CONVERT(NVARCHAR(36), NEWID()), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'Payment_Allocation_Counterparty_View_Submitted', GETDATE(), GETDATE(), GETDATE(), '0');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Counterparty_View_Submitted', 'de-DE', 'Payment Allocation Tab : View Payment Allocation', 'View submitted allocation Documentation for a particular cash receipt');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Counterparty_View_Submitted', 'en-GB', 'Payment Allocation Tab : View Payment Allocation', 'View submitted allocation Documentation for a particular cash receipt');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Counterparty_View_Submitted', 'en-US', 'Payment Allocation Tab : View Payment Allocation', 'View submitted allocation Documentation for a particular cash receipt');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Counterparty_View_Submitted', 'es-ES', 'Payment Allocation Tab : View Payment Allocation', 'View submitted allocation Documentation for a particular cash receipt');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('Payment_Allocation_Counterparty_View_Submitted', 'fr-FR', 'Payment Allocation Tab : View Payment Allocation', 'View submitted allocation Documentation for a particular cash receipt');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (CONVERT(NVARCHAR(36), NEWID()), 'TradeSupplyFinance', 'PaymentAllocations', 'requestDocumentation', 'Payment_Allocation_Anchor_Request_Documentation');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (CONVERT(NVARCHAR(36), NEWID()), 'TradeSupplyFinance', 'PaymentAllocations', 'submitDocumentation', 'Payment_Allocation_Anchor_Edit_Submitted,Payment_Allocation_Anchor_Submit_Documentation,Payment_Allocation_Counterparty_Edit_Submitted,Payment_Allocation_Counterparty_Submit_Documentation');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (CONVERT(NVARCHAR(36), NEWID()), 'TradeSupplyFinance', 'PaymentAllocations', 'getPaymentAllocations', 'SCF_Anchor_Dashboard_View,Payment_Allocation_Anchor_View_Pending,Payment_Allocation_Anchor_View_Submitted,Payment_Allocation_Counterparty_View,Payment_Allocation_Counterparty_View_Submitted,Payment_Allocations_Anchor_View');

GO
INSERT INTO  [${dbxschemaname}].[rrole]([id]) VALUES ('EARLY_PARTIAL_REPAYMENT-CREATE');
INSERT INTO  [${dbxschemaname}].[rrole]([id]) VALUES ('EARLY_PARTIAL_REPAYMENT-VIEW');
INSERT INTO  [${dbxschemaname}].[feature]([id], [App_id], [name], [description], [Type_id], [Status_id], [DisplaySequence], [isPrimary]) VALUES ('PARTIAL_REPAYMENT', 'RETAIL_AND_BUSINESS_BANKING', 'Early Partial Repayment', 'Early Partial Repayment', 'NON_MONETARY', 'SID_FEATURE_ACTIVE', '0', '0');
INSERT INTO  [${dbxschemaname}].[featuredisplaynamedescription]([Feature_id], [Locale_id], [displayName], [displayDescription]) VALUES ('PARTIAL_REPAYMENT', 'en-GB', 'Early Partial Repayment', 'Early Partial Repayment');
INSERT INTO  [${dbxschemaname}].[featuredisplaynamedescription]([Feature_id], [Locale_id], [displayName], [displayDescription]) VALUES ('PARTIAL_REPAYMENT', 'de-DE', 'Early Partial Repayment', 'Early Partial Repayment');
INSERT INTO  [${dbxschemaname}].[featuredisplaynamedescription]([Feature_id], [Locale_id], [displayName], [displayDescription]) VALUES ('PARTIAL_REPAYMENT', 'en-US', 'Early Partial Repayment', 'Early Partial Repayment');
INSERT INTO  [${dbxschemaname}].[featuredisplaynamedescription]([Feature_id], [Locale_id], [displayName], [displayDescription]) VALUES ('PARTIAL_REPAYMENT', 'es-ES', 'Early Partial Repayment', 'Early Partial Repayment');
INSERT INTO  [${dbxschemaname}].[featuredisplaynamedescription]([Feature_id], [Locale_id], [displayName], [displayDescription]) VALUES ('PARTIAL_REPAYMENT', 'fr-FR', 'Early Partial Repayment', 'Early Partial Repayment');
INSERT INTO  [${dbxschemaname}].[featureroletype]([RoleType_id], [Feature_id]) VALUES ('TYPE_ID_BUSINESS','PARTIAL_REPAYMENT');
INSERT INTO  [${dbxschemaname}].[featureroletype]([RoleType_id], [Feature_id]) VALUES ('TYPE_ID_WEALTH','PARTIAL_REPAYMENT');
INSERT INTO  [${dbxschemaname}].[featureroletype]([RoleType_id], [Feature_id]) VALUES ('TYPE_ID_RETAIL','PARTIAL_REPAYMENT');
INSERT INTO [${dbxschemaname}].[featureaction]([id],[Feature_id],[App_id],[Type_id],[Rrole_id],[name],[description],[isAccountLevel],[isMFAApplicable],[isPrimary],[DisplaySequence],[createdts],[lastmodifiedts],[synctimestamp],[softdeleteflag],[status],[accesspolicyId],[actionlevelId]) VALUES ('EARLY_PARTIAL_REPAYMENT-CREATE' ,'PARTIAL_REPAYMENT' ,'RETAIL_AND_BUSINESS_BANKING' ,'NON_MONETARY' ,'EARLY_PARTIAL_REPAYMENT-CREATE' ,'Create Early Partial Repayment' ,'Create Early Partial Repayment' ,'0', '0', '0', '0', GETDATE(), GETDATE(), GETDATE(),'0','SID_ACTION_ACTIVE','CREATE','CUSTOMERID_LEVEL') ;
INSERT INTO [${dbxschemaname}].[featureaction]([id],[Feature_id],[App_id],[Type_id],[Rrole_id],[name],[description],[isAccountLevel],[isMFAApplicable],[isPrimary],[DisplaySequence],[createdts],[lastmodifiedts],[synctimestamp],[softdeleteflag],[status],[accesspolicyId],[actionlevelId]) VALUES ('EARLY_PARTIAL_REPAYMENT-VIEW' ,'PARTIAL_REPAYMENT' ,'RETAIL_AND_BUSINESS_BANKING' ,'NON_MONETARY' ,'EARLY_PARTIAL_REPAYMENT-VIEW' ,'View Early Partial Repayment' ,'View Early Partial Repayment' ,'0', '0', '0', '0', GETDATE(), GETDATE(), GETDATE(),'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL') ;
INSERT INTO  [${dbxschemaname}].[actiondisplaynamedescription]([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('EARLY_PARTIAL_REPAYMENT-CREATE','en-US','Create Early Partial Repayment', 'Create Early Partial Repayment');
INSERT INTO  [${dbxschemaname}].[actiondisplaynamedescription]([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('EARLY_PARTIAL_REPAYMENT-VIEW','en-US','View Early Partial Repayment', 'View Early Partial Repayment');
INSERT INTO  [${dbxschemaname}].[featureactionroletype]([RoleType_id], [Action_id]) VALUES ('TYPE_ID_BUSINESS','EARLY_PARTIAL_REPAYMENT-CREATE');
INSERT INTO  [${dbxschemaname}].[featureactionroletype]([RoleType_id], [Action_id]) VALUES ('TYPE_ID_WEALTH','EARLY_PARTIAL_REPAYMENT-CREATE');
INSERT INTO  [${dbxschemaname}].[featureactionroletype]([RoleType_id], [Action_id]) VALUES ('TYPE_ID_RETAIL','EARLY_PARTIAL_REPAYMENT-CREATE');
INSERT INTO  [${dbxschemaname}].[featureactionroletype]([RoleType_id], [Action_id]) VALUES ('TYPE_ID_BUSINESS','EARLY_PARTIAL_REPAYMENT-VIEW');
INSERT INTO  [${dbxschemaname}].[featureactionroletype]([RoleType_id], [Action_id]) VALUES ('TYPE_ID_WEALTH','EARLY_PARTIAL_REPAYMENT-VIEW');
INSERT INTO  [${dbxschemaname}].[featureactionroletype]([RoleType_id], [Action_id]) VALUES ('TYPE_ID_RETAIL','EARLY_PARTIAL_REPAYMENT-VIEW');
INSERT INTO  [${dbxschemaname}].[dependentactions]([actionId], [dependentactionId], [featureId], [actionName], [featureName]) VALUES ('EARLY_PARTIAL_REPAYMENT-CREATE', 'EARLY_PARTIAL_REPAYMENT-CREATE', 'PARTIAL_REPAYMENT', 'Create Early Partial Repayment', 'Early Partial Repayment');
INSERT INTO  [${dbxschemaname}].[dependentactions]([actionId], [dependentactionId], [featureId], [actionName], [featureName]) VALUES ('EARLY_PARTIAL_REPAYMENT-VIEW', 'EARLY_PARTIAL_REPAYMENT-VIEW', 'PARTIAL_REPAYMENT', 'View Early Partial Repayment', 'Early Partial Repayment');
INSERT INTO  [${dbxschemaname}].[compositeaction]([id], [Permission_id], [Action_id], [Feature_id], [isEnabled]) VALUES ('CAID410', 'PID45', 'EARLY_PARTIAL_REPAYMENT-CREATE', 'PARTIAL_REPAYMENT', '1');
INSERT INTO  [${dbxschemaname}].[compositeaction]([id], [Permission_id], [Action_id], [Feature_id], [isEnabled]) VALUES ('CAID411', 'PID45', 'EARLY_PARTIAL_REPAYMENT-VIEW', 'PARTIAL_REPAYMENT', '1');