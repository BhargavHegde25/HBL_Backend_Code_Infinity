INSERT INTO [${dbxschemaname}].[rrole] ([id]) VALUES ('ADD_USER_ANOTHER_ENTITY');

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId]) VALUES ('ADD_USER_ANOTHER_ENTITY','USER_MANAGEMENT','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','ADD_USER_ANOTHER_ENTITY','Add user to another entity','Add user to another entity',0,0,0,0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'0','SID_ACTION_ACTIVE','ADMIN','CUSTOMERID_LEVEL');


INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('ADD_USER_ANOTHER_ENTITY' ,'de-DE' ,'Add user to another entity','Add user to another entity' );
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('ADD_USER_ANOTHER_ENTITY' ,'en-GB' ,'Add user to another entity','Add user to another entity' ); 
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('ADD_USER_ANOTHER_ENTITY' ,'en-US' ,'Add user to another entity','Add user to another entity' ); 
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('ADD_USER_ANOTHER_ENTITY' ,'es-ES' ,'Add user to another entity','Add user to another entity' ); 
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('ADD_USER_ANOTHER_ENTITY' ,'fr-FR' ,'Add user to another entity','Add user to another entity' );

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id],[Action_id],[createdby],[modifiedby],[createdts],[lastmodifiedts],[synctimestamp],[softdeleteflag]) VALUES ('TYPE_ID_BUSINESS','ADD_USER_ANOTHER_ENTITY',null,null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'0');


INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'GROUP_ADMINISTRATOR', 'ADD_USER_ANOTHER_ENTITY', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'GROUP_CREATOR', 'ADD_USER_ANOTHER_ENTITY', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0'); 

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'ADD_USER_ANOTHER_ENTITY', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0'); 

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '5801fa32-a416-45b6-af01-b22e2de93777', 'ADD_USER_ANOTHER_ENTITY', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'ADD_USER_ANOTHER_ENTITY', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'bef2fe82-9c21-4ccb-b599-3308de18de44', 'ADD_USER_ANOTHER_ENTITY', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'ADD_USER_ANOTHER_ENTITY', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'ADD_USER_ANOTHER_ENTITY', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');



INSERT INTO [${dbxschemaname}].[dependentactions] ([actionId],[dependentactionId],[featureId],[actionName],[featureName]) VALUES ('ADD_USER_ANOTHER_ENTITY','USER_MANAGEMENT','USER_MANAGEMENT','Add user to another entity','Add user to another entity');
	
INSERT INTO [${dbxschemaname}].[dependentactions] ([actionId],[dependentactionId],[featureId],[actionName],[featureName]) VALUES ('ADD_USER_ANOTHER_ENTITY','USER_MANAGEMENT_VIEW','USER_MANAGEMENT','Add user to another entity','Add user to another entity');



INSERT INTO [${dbxschemaname}].[groupactionlimit]([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) VALUES (NEWID(), 'GROUP_ADMINISTRATOR', 'CLOSE_ACCOUNT-CREATE', null, null, 'UID11', null, 0);
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) VALUES (NEWID(), 'GROUP_ADMINISTRATOR', 'VIEW_CLOSED_ACCOUNT', null, null, 'UID11', null, 0);
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'CLOSE_ACCOUNT-CREATE', GETDATE(), GETDATE(), GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'VIEW_CLOSED_ACCOUNT', GETDATE(), GETDATE(), GETDATE(), '0');

UPDATE [${dbxschemaname}].[configurations] SET config_value = '{\"SAVINGS.PLAN\":\"Deposit\",\"DEPOSIT.CALL\":\"Deposit\",\"CURRENT.ACCOUNT\":\"Checking\",\"SS.ANNUAL\":\"Checking\",\"SS.ROLLOVER.01M\":\"Deposit\",\"NEGOTIABLE.LOAN\":\"Loan\",\"CURRENT.ACCOUNT.SME\":\"Checking\",\"PREMIUM.ACCOUNT\":\"Checking\",\"CURRENT.ACCOUNT.STUDENT\":\"Checking\",\"SAVINGS.ACCOUNT\":\"Savings\",\"CONS.SAVING\":\"Savings\",\"SAVINGS.SALARY.INFINITY\":\"Savings\",\"MORTGAGE.FLOATING\":\"Loan\",\"TERM.DEPOSIT\":\"Deposit\",\"CURRENT.ACCOUNT.STAFF\":\"Checking\",\"CURRENT.ACCOUNT.PREF\":\"Checking\",\"CONS.CHECKING\":\"Checking\",\"SAVINGS.ACCOUNT.WELCOME\":\"Savings\",\"SAVINGS.STANDARD.INFINITY\":\"Savings\",\"PREFER.ACCOUNT\":\"Checking\",\"STUDENT.ACCOUNT\":\"Checking\",\"MORTGAGE.FIX5Y.60LTV\":\"Loan\",\"DEPOSIT.SHORT\":\"Deposit\",\"DEPOSIT.5Y\":\"Deposit\",\"DEPOSIT.3Y\":\"Deposit\",\"ADVANCED.CHECKING.ACCOUNT\":\"Checking\",\"SAVINGS.ACCOUNT.PROMOTIONAL\":\"Savings\",\"SAVINGS.ACCOUNT.FCY\":\"Savings\",\"SAVINGS.ACCOUNT.MINOR\":\"Savings\",\"DEPOSIT.09M\":\"Deposit\",\"BASIC.CHECKING.ACCOUNT\":\"Checking\",\"SS.FIXED.TERM\":\"Deposit\",\"SS.MONTHLY\":\"Checking\",\"MORTGAGE\":\"Loan\",\"SS.SAVINGS.REGULAR\":\"Savings\",\"PERSONAL.LOAN\":\"Loan\",\"SS.PAYG\":\"Checking\",\"SAVINGS.PRIME.INFINITY\":\"Savings\",\"CONS.MM\":\"Savings\",\"DEPOSIT.LONG\":\"Deposit\",\"VEHICLE.LOAN\":\"Loan\",\"SAVINGS.DEFAULT\":\"Savings\",\"PREFERRED.CHECKING.ACCOUNT\":\"Checking\",\"SAVINGS.ACCOUNT.WLC\":\"Savings\",\"SS.SAVINGS.CHILD\":\"Savings\",\"SAVINGS.ACCOUNT.NOTICE\":\"Savings\",\"BONDS.A.6M\":\"Deposit\",\"BONDS.B.1Y\":\"Deposit\",\"BONDS.C.3Y\":\"Deposit\",\"CURRENT.ACCOUNT.GEN\":\"Checking\",\"CURRENT.ACCOUNT.LINK\":\"Checking\",\"CURRENT.DEFAULT\":\"Checking\",\"CURRENT.PARENT\":\"Checking\",\"CURRENT.PARENT.INFINITY\":\"Checking\",\"CURRENT.PARENT.PREF\":\"Checking\",\"CURRENT.PARENT.SME\":\"Checking\",\"CURRENT.PARENT.STD\":\"Checking\",\"CURRENT.SHADOW\":\"Checking\",\"DEPOSIT.03M\":\"Deposit\",\"DEPOSIT.06M\":\"Deposit\",\"DEPOSIT.12M\":\"Deposit\",\"DEPOSIT.18M\":\"Deposit\",\"DEPOSIT.2Y\":\"Deposit\",\"DEPOSIT.4Y\":\"Deposit\",\"DEPOSIT.DEFAULT\":\"Deposit\",\"DEPOSIT.MAT\":\"Deposit\",\"DEPOSIT.NEGOTIABLE\":\"Deposit\",\"DEPOSIT.PARENT\":\"Deposit\",\"EBKM.DEPOSIT\":\"Deposit\",\"EXT.BN.PARENT\":\"Deposit\",\"EXT.DEPOSIT.PARENT\":\"Deposit\",\"INSTALLMENT.12M\":\"Loan\",\"INSTALLMENT.3M\":\"Loan\",\"INSTALLMENT.6M\":\"Loan\",\"INSTALLMENT.LOAN.PARENT\":\"Loan\",\"MORTGAGE.ARM\":\"Mortgage\",\"MORTGAGE.CASHBACK\":\"Mortgage\",\"MORTGAGE.FACILITY.PARENT\":\"Mortgage\",\"MORTGAGE.FEP\":\"Mortgage\",\"MORTGAGE.LINK\":\"Mortgage\",\"MORTGAGE.OFFER\":\"Mortgage\",\"MORTGAGE.OFFSET\":\"Mortgage\",\"MORTGAGE.PARENT\":\"Mortgage\",\"MORTGAGE.SEASONAL\":\"Mortgage\",\"PERSONAL.LOAN.2W\":\"Loan\",\"PERSONAL.LOAN.FWD\":\"Loan\",\"PERSONAL.LOAN.LINK\":\"Loan\",\"SAVINGS.PACKAGE\":\"Savings\",\"SAVINGS.PARENT\":\"Savings\",\"SAVINGS.PARENT.INFINITY\":\"Savings\",\"SAVINGS.PARENT.PREF\":\"Savings\",\"SAVINGS.PARENT.STD\":\"Savings\",\"SMALL.BUSINESS.LOAN\":\"Loan\",\"SME.ACCOUNT\":\"Checking\",\"SSA.ACCOUNT\":\"Checking\",\"STAFF.ACCOUNT\":\"Savings\",\"CORP.CURRENT.ACCOUNT\":\"Checking\",\"CL.FACILITY\":\"Sprout\",\"STUDENT.LOAN\":\"Loan\",\"MORTGAGE.FACILITY\":\"mortgageFacility\",\"BB.SMALL.BUSINESS.LOAN\":\"Loan\",\"BB.PREMIUM.ACCOUNT\":\"Checking\",\"BB.STANDARD.ACCOUNT\":\"Checking\",\"BB.START.UP.ACCOUNT\":\"Checking\"}' WHERE (configuration_id = '172');

Go

-- DBP BUNDLE CONFIGURATION: RECEIVABLE BILLS CHART DATA
INSERT INTO [${dbxschemaname}].[configurations] ([configuration_id], [bundle_id], [config_type], [config_key], [description], [config_value], [target], [isPreLoginConfiguration])
VALUES ('TSF_1', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'RECEIVABLES_BILLS_CHART_DATA', 'Receivable Bills Chart Data', '{"billStatus":{"Draft":"Draft","Returned by Bank":"Returned by Bank","Approved":"Approved","Processing by Bank":"Processing","Submitted to Bank":"Processing","New":"Processing"},"billColorCode":{"Single":"#4176A4","Batch":"#A0BBD2"}}', 'CLIENT', '1');

-- SECURE MESSAGE CATEGORY / TRADESUPPLYFINANCE / BILLS
INSERT INTO [${dbxschemaname}].[requestcategory] ([id], [Name]) VALUES ('RCID_TSF_BILLS', 'Trade Supply Finance - Bills');

-- FEATURE / TRADESUPPLYFINANCE / RECEIVABLE BILLS
INSERT INTO [${dbxschemaname}].[feature] ([id], [App_id], [name], [description], [Type_id], [Status_id], [Service_Fee], [DisplaySequence], [isPrimary], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('RECEIVABLE_BILLS', 'RETAIL_AND_BUSINESS_BANKING', 'Receivable Bills', 'View & Manage the Receivable Bills', 'NON_MONETARY', 'SID_FEATURE_ACTIVE', null, null, 0,  null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('RECEIVABLE_BILLS', 'de-DE', 'Receivable Bills', 'View & Manage the Receivable Bills', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('RECEIVABLE_BILLS', 'en-GB', 'Receivable Bills', 'View & Manage the Receivable Bills', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('RECEIVABLE_BILLS', 'en-US', 'Receivable Bills', 'View & Manage the Receivable Bills', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('RECEIVABLE_BILLS', 'es-ES', 'Receivable Bills', 'View & Manage the Receivable Bills', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('RECEIVABLE_BILLS', 'fr-FR', 'Receivable Bills', 'View & Manage the Receivable Bills', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[featureroletype] ([RoleType_id], [Feature_id]) VALUES ('TYPE_ID_BUSINESS', 'RECEIVABLE_BILLS');

-- ACTION / TRADESUPPLYFINANCE / RECEIVABLE BILLS / CREATE
INSERT INTO [${dbxschemaname}].[rrole] ([id], [createdts], [lastmodifiedts], [softdeleteflag]) VALUES ('RECEIVABLE_BILLS_CREATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [MFA_id], [isPrimary], [DisplaySequence], [dependency], [softdeleteflag], [accesspolicyId], [actionlevelId]) VALUES ('RECEIVABLE_BILLS_CREATE', 'RECEIVABLE_BILLS', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'RECEIVABLE_BILLS_CREATE', 'Manage the Receivable Bills', 'View the Receivable Bills', 1, 0, null, 0, 10, null, 0, 'CREATE', 'ACCOUNT_LEVEL');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) VALUES (NEWID(), 'GROUP_ADMINISTRATOR', 'RECEIVABLE_BILLS_CREATE', null, null, 'UID11', null, 0);
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) VALUES (NEWID(), 'GROUP_CREATOR', 'RECEIVABLE_BILLS_CREATE', null, null, 'UID11', null, 0);

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('TYPE_ID_BUSINESS', 'RECEIVABLE_BILLS_CREATE', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'RECEIVABLE_BILLS_CREATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'RECEIVABLE_BILLS_CREATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('RECEIVABLE_BILLS_CREATE', 'de-DE', 'Receivable Bills Create', 'Receivable Bills Create');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('RECEIVABLE_BILLS_CREATE', 'en-GB', 'Receivable Bills Create', 'Receivable Bills Create');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('RECEIVABLE_BILLS_CREATE', 'en-US', 'Receivable Bills Create', 'Receivable Bills Create');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('RECEIVABLE_BILLS_CREATE', 'es-ES', 'Receivable Bills Create', 'Receivable Bills Create');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('RECEIVABLE_BILLS_CREATE', 'fr-FR', 'Receivable Bills Create', 'Receivable Bills Create');

-- ACTION / TRADESUPPLYFINANCE / RECEIVABLE BILLS / UPDATE
INSERT INTO [${dbxschemaname}].[rrole] ([id], [createdts], [lastmodifiedts], [softdeleteflag]) VALUES ('RECEIVABLE_BILLS_UPDATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [MFA_id], [isPrimary], [DisplaySequence], [dependency], [softdeleteflag], [accesspolicyId], [actionlevelId]) VALUES ('RECEIVABLE_BILLS_UPDATE', 'RECEIVABLE_BILLS', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'RECEIVABLE_BILLS_UPDATE', 'Manage the Receivable Bills', 'View the Receivable Bills', 1, 0, null, 0, 10, null, 0, 'CREATE', 'ACCOUNT_LEVEL');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) VALUES (NEWID(), 'GROUP_ADMINISTRATOR', 'RECEIVABLE_BILLS_UPDATE', null, null, 'UID11', null, 0);
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) VALUES (NEWID(), 'GROUP_CREATOR', 'RECEIVABLE_BILLS_UPDATE', null, null, 'UID11', null, 0);

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('TYPE_ID_BUSINESS', 'RECEIVABLE_BILLS_UPDATE', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'RECEIVABLE_BILLS_UPDATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'RECEIVABLE_BILLS_UPDATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('RECEIVABLE_BILLS_UPDATE', 'de-DE', 'Receivable Bills Update', 'Receivable Bills Update');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('RECEIVABLE_BILLS_UPDATE', 'en-GB', 'Receivable Bills Update', 'Receivable Bills Update');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('RECEIVABLE_BILLS_UPDATE', 'en-US', 'Receivable Bills Update', 'Receivable Bills Update');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('RECEIVABLE_BILLS_UPDATE', 'es-ES', 'Receivable Bills Update', 'Receivable Bills Update');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('RECEIVABLE_BILLS_UPDATE', 'fr-FR', 'Receivable Bills Update', 'Receivable Bills Update');

-- ACTION / TRADESUPPLYFINANCE / RECEIVABLE BILLS / VIEW
INSERT INTO [${dbxschemaname}].[rrole] ([id], [createdts], [lastmodifiedts], [softdeleteflag]) VALUES ('RECEIVABLE_BILLS_VIEW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [MFA_id], [isPrimary], [DisplaySequence], [dependency], [softdeleteflag], [accesspolicyId], [actionlevelId]) VALUES ('RECEIVABLE_BILLS_VIEW', 'RECEIVABLE_BILLS', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'RECEIVABLE_BILLS_VIEW', 'Manage the Receivable Bills', 'View the Receivable Bills', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) VALUES (NEWID(), 'GROUP_ADMINISTRATOR', 'RECEIVABLE_BILLS_VIEW', null, null, 'UID11', null, 0);
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) VALUES (NEWID(), 'GROUP_CREATOR', 'RECEIVABLE_BILLS_VIEW', null, null, 'UID11', null, 0);

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('TYPE_ID_BUSINESS', 'RECEIVABLE_BILLS_VIEW', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'RECEIVABLE_BILLS_VIEW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'RECEIVABLE_BILLS_VIEW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('RECEIVABLE_BILLS_VIEW', 'de-DE', 'Receivable Bills View', 'Receivable Bills View');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('RECEIVABLE_BILLS_VIEW', 'en-GB', 'Receivable Bills View', 'Receivable Bills View');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('RECEIVABLE_BILLS_VIEW', 'en-US', 'Receivable Bills View', 'Receivable Bills View');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('RECEIVABLE_BILLS_VIEW', 'es-ES', 'Receivable Bills View', 'Receivable Bills View');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('RECEIVABLE_BILLS_VIEW', 'fr-FR', 'Receivable Bills View', 'Receivable Bills View');

-- CUSTOM VERBS / TRADESUPPLYFINANCE / ReceivableSingleBills
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('23047kia-3482-39yc-0102-0242lotsf001', 'TradeSupplyFinance', 'ReceivableSingleBills', 'saveBill', 'RECEIVABLE_BILLS_CREATE');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('23047kia-3482-39yc-0102-0242lotsf002', 'TradeSupplyFinance', 'ReceivableSingleBills', 'deleteBill', 'RECEIVABLE_BILLS_CREATE');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('23047kia-3482-39yc-0102-0242lotsf003', 'TradeSupplyFinance', 'ReceivableSingleBills', 'createBill', 'RECEIVABLE_BILLS_CREATE');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('23047kia-3482-39yc-0102-0242lotsf004', 'TradeSupplyFinance', 'ReceivableSingleBills', 'reviseBill', 'RECEIVABLE_BILLS_UPDATE');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('23047kia-3482-39yc-0102-0242lotsf007', 'TradeSupplyFinance', 'ReceivableSingleBills', 'requestBillCancellation', 'RECEIVABLE_BILLS_UPDATE');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('23047kia-3482-39yc-0102-0242lotsf005', 'TradeSupplyFinance', 'ReceivableSingleBills', 'getBill', 'RECEIVABLE_BILLS_VIEW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('23047kia-3482-39yc-0102-0242lotsf006', 'TradeSupplyFinance', 'ReceivableSingleBills', 'getBills', 'RECEIVABLE_BILLS_VIEW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('23047kia-3482-39yc-0102-0242lotsf008', 'TradeSupplyFinance', 'ReceivableSingleBills', 'generateBillReport', 'RECEIVABLE_BILLS_VIEW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('23047kia-3482-39yc-0102-0242lotsf009', 'TradeSupplyFinance', 'ReceivableSingleBills', 'generateBillsList', 'RECEIVABLE_BILLS_VIEW');

-- FEATURE / TRADESUPPLYFINANCE / RECEIVABLE BILLS
INSERT INTO [${dbxschemaname}].[feature] ([id], [App_id], [name], [description], [Type_id], [Status_id], [Service_Fee], [DisplaySequence], [isPrimary], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('RECEIVABLES_CSV_IMPORT', 'RETAIL_AND_BUSINESS_BANKING', 'Receivables CSV Import', 'Create & View the Receivable Bills From CSV', 'NON_MONETARY', 'SID_FEATURE_ACTIVE', null, null, 0,  null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('RECEIVABLES_CSV_IMPORT', 'de-DE', 'Receivables CSV Import', 'Create & View the Receivable Bills From CSV', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('RECEIVABLES_CSV_IMPORT', 'en-GB', 'Receivables CSV Import', 'Create & View the Receivable Bills From CSV', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('RECEIVABLES_CSV_IMPORT', 'en-US', 'Receivables CSV Import', 'Create & View the Receivable Bills From CSV', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('RECEIVABLES_CSV_IMPORT', 'es-ES', 'Receivables CSV Import', 'Create & View the Receivable Bills From CSV', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('RECEIVABLES_CSV_IMPORT', 'fr-FR', 'Receivables CSV Import', 'Create & View the Receivable Bills From CSV', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[featureroletype] ([RoleType_id], [Feature_id]) VALUES ('TYPE_ID_BUSINESS', 'RECEIVABLES_CSV_IMPORT');
-- ACTION / TRADESUPPLYFINANCE / RECEIVABLE CSV IMPORT / CREATE
INSERT INTO [${dbxschemaname}].[rrole] ([id], [createdts], [lastmodifiedts], [softdeleteflag]) VALUES ('RECEIVABLES_CSV_IMPORT_CREATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [MFA_id], [isPrimary], [DisplaySequence], [dependency], [softdeleteflag], [accesspolicyId], [actionlevelId]) VALUES ('RECEIVABLES_CSV_IMPORT_CREATE', 'RECEIVABLES_CSV_IMPORT', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'RECEIVABLES_CSV_IMPORT_CREATE', 'Manage the Receivable Bills by Importing CSV', 'View the Receivable Bills by Importing CSV', 1, 0, null, 0, 10, null, 0, 'CREATE', 'ACCOUNT_LEVEL');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) VALUES (NEWID(), 'GROUP_ADMINISTRATOR', 'RECEIVABLES_CSV_IMPORT_CREATE', null, null, 'UID11', null, 0);
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) VALUES (NEWID(), 'GROUP_CREATOR', 'RECEIVABLES_CSV_IMPORT_CREATE', null, null, 'UID11', null, 0);

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('TYPE_ID_BUSINESS', 'RECEIVABLES_CSV_IMPORT_CREATE', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'RECEIVABLES_CSV_IMPORT_CREATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'RECEIVABLES_CSV_IMPORT_CREATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('RECEIVABLES_CSV_IMPORT_CREATE', 'de-DE', 'Receivable Bills by Importing CSV - Create', 'Receivable Bills by Importing CSV - Create');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('RECEIVABLES_CSV_IMPORT_CREATE', 'en-GB', 'Receivable Bills by Importing CSV - Create', 'Receivable Bills by Importing CSV - Create');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('RECEIVABLES_CSV_IMPORT_CREATE', 'en-US', 'Receivable Bills by Importing CSV - Create', 'Receivable Bills by Importing CSV - Create');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('RECEIVABLES_CSV_IMPORT_CREATE', 'es-ES', 'Receivable Bills by Importing CSV - Create', 'Receivable Bills by Importing CSV - Create');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('RECEIVABLES_CSV_IMPORT_CREATE', 'fr-FR', 'Receivable Bills by Importing CSV - Create', 'Receivable Bills by Importing CSV - Create');

-- ACTION / TRADESUPPLYFINANCE / RECEIVABLE CSV IMPORT / UPDATE
INSERT INTO [${dbxschemaname}].[rrole] ([id], [createdts], [lastmodifiedts], [softdeleteflag]) VALUES ('RECEIVABLES_CSV_IMPORT_UPDATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [MFA_id], [isPrimary], [DisplaySequence], [dependency], [softdeleteflag], [accesspolicyId], [actionlevelId]) VALUES ('RECEIVABLES_CSV_IMPORT_UPDATE', 'RECEIVABLES_CSV_IMPORT', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'RECEIVABLES_CSV_IMPORT_UPDATE', 'Manage the Receivable Bills by Importing CSV', 'View the Receivable Bills by Importing CSV', 1, 0, null, 0, 10, null, 0, 'CREATE', 'ACCOUNT_LEVEL');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) VALUES (NEWID(), 'GROUP_ADMINISTRATOR', 'RECEIVABLES_CSV_IMPORT_UPDATE', null, null, 'UID11', null, 0);
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) VALUES (NEWID(), 'GROUP_CREATOR', 'RECEIVABLES_CSV_IMPORT_UPDATE', null, null, 'UID11', null, 0);

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('TYPE_ID_BUSINESS', 'RECEIVABLES_CSV_IMPORT_UPDATE', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'RECEIVABLES_CSV_IMPORT_UPDATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'RECEIVABLES_CSV_IMPORT_UPDATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('RECEIVABLES_CSV_IMPORT_UPDATE', 'de-DE', 'Receivable Bills by Importing CSV - Update', 'Receivable Bills by Importing CSV - Update');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('RECEIVABLES_CSV_IMPORT_UPDATE', 'en-GB', 'Receivable Bills by Importing CSV - Update', 'Receivable Bills by Importing CSV - Update');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('RECEIVABLES_CSV_IMPORT_UPDATE', 'en-US', 'Receivable Bills by Importing CSV - Update', 'Receivable Bills by Importing CSV - Update');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('RECEIVABLES_CSV_IMPORT_UPDATE', 'es-ES', 'Receivable Bills by Importing CSV - Update', 'Receivable Bills by Importing CSV - Update');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('RECEIVABLES_CSV_IMPORT_UPDATE', 'fr-FR', 'Receivable Bills by Importing CSV - Update', 'Receivable Bills by Importing CSV - Update');

-- ACTION / TRADESUPPLYFINANCE / RECEIVABLE CSV IMPORT / VIEW
INSERT INTO [${dbxschemaname}].[rrole] ([id], [createdts], [lastmodifiedts], [softdeleteflag]) VALUES ('RECEIVABLES_CSV_IMPORT_VIEW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [MFA_id], [isPrimary], [DisplaySequence], [dependency], [softdeleteflag], [accesspolicyId], [actionlevelId]) VALUES ('RECEIVABLES_CSV_IMPORT_VIEW', 'RECEIVABLES_CSV_IMPORT', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'RECEIVABLES_CSV_IMPORT_VIEW', 'Manage the Receivable Bills from Imported CSV', 'View the Receivable Bills from Imported CSV', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) VALUES (NEWID(), 'GROUP_ADMINISTRATOR', 'RECEIVABLES_CSV_IMPORT_VIEW', null, null, 'UID11', null, 0);
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [modifiedby], [softdeleteflag]) VALUES (NEWID(), 'GROUP_CREATOR', 'RECEIVABLES_CSV_IMPORT_VIEW', null, null, 'UID11', null, 0);

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('TYPE_ID_BUSINESS', 'RECEIVABLES_CSV_IMPORT_VIEW', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'RECEIVABLES_CSV_IMPORT_VIEW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'RECEIVABLES_CSV_IMPORT_VIEW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('RECEIVABLES_CSV_IMPORT_VIEW', 'de-DE', 'Receivable Bills from Imported CSV - View', 'Receivable Bills from Imported CSV - View');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('RECEIVABLES_CSV_IMPORT_VIEW', 'en-GB', 'Receivable Bills from Imported CSV - View', 'Receivable Bills from Imported CSV - View');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('RECEIVABLES_CSV_IMPORT_VIEW', 'en-US', 'Receivable Bills from Imported CSV - View', 'Receivable Bills from Imported CSV - View');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('RECEIVABLES_CSV_IMPORT_VIEW', 'es-ES', 'Receivable Bills from Imported CSV - View', 'Receivable Bills from Imported CSV - View');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('RECEIVABLES_CSV_IMPORT_VIEW', 'fr-FR', 'Receivable Bills from Imported CSV - View', 'Receivable Bills from Imported CSV - View');

-- CUSTOM VERBS / TRADESUPPLYFINANCE / ReceivablesCsvImport
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('23047kia-3482-39yc-0102-0242locsv001', 'TradeSupplyFinance', 'ReceivablesCsvImport', 'createBills', 'RECEIVABLES_CSV_IMPORT_CREATE');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('23047kia-3482-39yc-0102-0242locsv002', 'TradeSupplyFinance', 'ReceivablesCsvImport', 'submitCsvImport', 'RECEIVABLES_CSV_IMPORT_UPDATE');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('23047kia-3482-39yc-0102-0242locsv003', 'TradeSupplyFinance', 'ReceivablesCsvImport', 'getCsvImportById', 'RECEIVABLES_CSV_IMPORT_VIEW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('23047kia-3482-39yc-0102-0242locsv004', 'TradeSupplyFinance', 'ReceivablesCsvImport', 'getCsvImports', 'RECEIVABLES_CSV_IMPORT_VIEW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('23047kia-3482-39yc-0102-0242locsv005', 'TradeSupplyFinance', 'ReceivableSingleBills', 'submitImportedBill', 'RECEIVABLES_CSV_IMPORT_UPDATE');

-- ALERT CATEGORY: ALERT_CAT_TRADESUPPLYFINANCE
INSERT INTO [${dbxschemaname}].[dbxalertcategory] ([id], [Name], [accountLevel], [status_id], [DisplaySequence], [defaultFrequencyId], [defaultFrequencyTime]) VALUES ('ALERT_CAT_TRADESUPPLYFINANCE', 'Trade Supply Finance', '0', 'SID_ACTIVE', '6', 'DAILY', '10:00:00');
INSERT INTO [${dbxschemaname}].[dbxalertcategorytext] ([AlertCategoryId], [LanguageCode], [DisplayName], [Description]) VALUES ('ALERT_CAT_TRADESUPPLYFINANCE', 'en-US', 'Trade Supply Finance', 'Trade Supply Finance alerts are grouped here');

INSERT INTO [${dbxschemaname}].[alertcategorychannel] ([ChannelID], [AlertCategoryId]) VALUES ('CH_NOTIFICATION_CENTER', 'ALERT_CAT_TRADESUPPLYFINANCE');
INSERT INTO [${dbxschemaname}].[alertcategorychannel] ([ChannelID], [AlertCategoryId]) VALUES ('CH_PUSH_NOTIFICATION', 'ALERT_CAT_TRADESUPPLYFINANCE');

-- ALERT TYPE: TSF_RECEIVABLE_SINGLE_BILLS
INSERT INTO [${dbxschemaname}].[eventconsumertypes] ([ServiceId], [OperationId], [EventType]) VALUES ('Alerts', 'pushAlerts', 'TSF_RECEIVABLE_SINGLE_BILLS');
INSERT INTO [${dbxschemaname}].[eventtype] ([id], [Name], [ActivityType], [Description]) VALUES ('TSF_RECEIVABLE_SINGLE_BILLS', 'Receivable bills', 'CUSTOMER', 'Receivable bills');

INSERT INTO [${dbxschemaname}].[dbxalerttype] ([id], [Name], [AlertCategoryId], [isAccountLevel], [Status_id], [IsGlobal], [DisplaySequence]) VALUES ('TSF_RECEIVABLE_SINGLE_BILLS', 'Receivable bills', 'ALERT_CAT_TRADESUPPLYFINANCE', '0', 'SID_ACTIVE', '1', '1');
INSERT INTO [${dbxschemaname}].[dbxalerttypetext] ([AlertTypeId], [LanguageCode], [DisplayName], [Description]) VALUES ('TSF_RECEIVABLE_SINGLE_BILLS', 'en-US', 'Receivable bills', 'Receivable bills');
INSERT INTO [${dbxschemaname}].[alerttypeapp] ([AppId], [AlertTypeId]) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TSF_RECEIVABLE_SINGLE_BILLS');
INSERT INTO [${dbxschemaname}].[alerttypecustomertype] ([CustomerTypeId], [AlertTypeId]) VALUES ('TYPE_ID_BUSINESS', 'TSF_RECEIVABLE_SINGLE_BILLS');

INSERT INTO [${dbxschemaname}].[alerttypechannel] ([channelId], [alertTypeId]) VALUES ('CH_NOTIFICATION_CENTER', 'TSF_RECEIVABLE_SINGLE_BILLS');
INSERT INTO [${dbxschemaname}].[alerttypechannel] ([channelId], [alertTypeId]) VALUES ('CH_PUSH_NOTIFICATION', 'TSF_RECEIVABLE_SINGLE_BILLS');


-- ALERT SUBTYPE: TSF_RECEIVABLE_SINGLE_BILL_SUBMITTED
INSERT INTO [${dbxschemaname}].[eventsubtype] ([id], [eventtypeid], [Name], [externalSystem]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_SUBMITTED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Alert when Corporate user created a bill and submitted', '0');

INSERT INTO [${dbxschemaname}].[alertsubtype] ([id], [AlertTypeId], [Name], [isAccountLevel], [Status_id], [isGlobal], [recipienttype], [isAutoSubscribeEnabled], [externalSystem]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_SUBMITTED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Corporate user created a bill and submitted', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] ([alertSubTypeId], [languageCode], [displayName], [description]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_SUBMITTED', 'en-US', 'Corporate user created a bill and submitted', 'Corporate user created a bill and submitted');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] ([appId], [alertSubTypeId]) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TSF_RECEIVABLE_SINGLE_BILL_SUBMITTED');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] ([customerTypeId], [alertSubTypeId]) VALUES ('TYPE_ID_BUSINESS', 'TSF_RECEIVABLE_SINGLE_BILL_SUBMITTED');

INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId]) VALUES ('CH_NOTIFICATION_CENTER', 'TSF_RECEIVABLE_SINGLE_BILL_SUBMITTED');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId]) VALUES ('CH_PUSH_NOTIFICATION', 'TSF_RECEIVABLE_SINGLE_BILL_SUBMITTED');

INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text]) VALUES ('TSF1001', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_SUBMITTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your bill has been submitted successfully');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1002', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_SUBMITTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your bill has been submitted successfully', 'Corporate user created a bill and submitted');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1003', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_SUBMITTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your bill has been submitted successfully', 'Corporate user created a bill and submitted');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1004', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_SUBMITTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your bill has been submitted successfully', 'Corporate user created a bill and submitted');

-- ALERT SUBTYPE: TSF_RECEIVABLE_SINGLE_BILL_DELETED
INSERT INTO [${dbxschemaname}].[eventsubtype] ([id], [eventtypeid], [Name], [externalSystem]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_DELETED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Alert when Corporate user permanently deleted the record before submission for approval', '0');

INSERT INTO [${dbxschemaname}].[alertsubtype] ([id], [AlertTypeId], [Name], [isAccountLevel], [Status_id], [isGlobal], [recipienttype], [isAutoSubscribeEnabled], [externalSystem]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_DELETED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Corporate user permanently deleted the record before submission for approval', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] ([alertSubTypeId], [languageCode], [displayName], [description]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_DELETED', 'en-US', 'Corporate user permanently deleted the record before submission for approval', 'Corporate user permanently deleted the record before submission for approval');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] ([appId], [alertSubTypeId]) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TSF_RECEIVABLE_SINGLE_BILL_DELETED');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] ([customerTypeId], [alertSubTypeId]) VALUES ('TYPE_ID_BUSINESS', 'TSF_RECEIVABLE_SINGLE_BILL_DELETED');

INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId]) VALUES ('CH_NOTIFICATION_CENTER', 'TSF_RECEIVABLE_SINGLE_BILL_DELETED');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId]) VALUES ('CH_PUSH_NOTIFICATION', 'TSF_RECEIVABLE_SINGLE_BILL_DELETED');

INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text]) VALUES ('TSF1011', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_DELETED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your bill - [#]orderId[/#] has been deleted permanently');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1012', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_DELETED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your bill - [#]orderId[/#] has been deleted permanently', 'Corporate user permanently deleted the record before submission for approval');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1013', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_DELETED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your bill - [#]orderId[/#] has been deleted permanently', 'Corporate user permanently deleted the record before submission for approval');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1014', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_DELETED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your bill - [#]orderId[/#] has been deleted permanently', 'Corporate user permanently deleted the record before submission for approval');

-- ALERT SUBTYPE: TSF_RECEIVABLE_SINGLE_BILL_REVISION_SUBMITTED
INSERT INTO [${dbxschemaname}].[eventsubtype] ([id], [eventtypeid], [Name], [externalSystem]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_REVISION_SUBMITTED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Alert when Corporate user resubmit the updated/changed bills for approval', '0');

INSERT INTO [${dbxschemaname}].[alertsubtype] ([id], [AlertTypeId], [Name], [isAccountLevel], [Status_id], [isGlobal], [recipienttype], [isAutoSubscribeEnabled], [externalSystem]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_REVISION_SUBMITTED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Corporate user resubmit the updated/changed bills for approval', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] ([alertSubTypeId], [languageCode], [displayName], [description]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_REVISION_SUBMITTED', 'en-US', 'Corporate user resubmit the updated/changed bills for approval', 'Corporate user resubmit the updated/changed bills for approval');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] ([appId], [alertSubTypeId]) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TSF_RECEIVABLE_SINGLE_BILL_REVISION_SUBMITTED');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] ([customerTypeId], [alertSubTypeId]) VALUES ('TYPE_ID_BUSINESS', 'TSF_RECEIVABLE_SINGLE_BILL_REVISION_SUBMITTED');

INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId]) VALUES ('CH_NOTIFICATION_CENTER', 'TSF_RECEIVABLE_SINGLE_BILL_REVISION_SUBMITTED');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId]) VALUES ('CH_PUSH_NOTIFICATION', 'TSF_RECEIVABLE_SINGLE_BILL_REVISION_SUBMITTED');

INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text]) VALUES ('TSF1021', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_REVISION_SUBMITTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your revised bill had been submitted successfully');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1022', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_REVISION_SUBMITTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your revised bill had been submitted successfully', 'Corporate user resubmit the updated/changed bills for approval');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1023', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_REVISION_SUBMITTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your revised bill had been submitted successfully', 'Corporate user resubmit the updated/changed bills for approval');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1024', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_REVISION_SUBMITTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your revised bill had been submitted successfully', 'Corporate user resubmit the updated/changed bills for approval');

-- ALERT SUBTYPE: TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_SUBMITTED
INSERT INTO [${dbxschemaname}].[eventsubtype] ([id], [eventtypeid], [Name], [externalSystem]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_SUBMITTED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Alert when Corporate user submits a cancellation request for the bill', '0');

INSERT INTO [${dbxschemaname}].[alertsubtype] ([id], [AlertTypeId], [Name], [isAccountLevel], [Status_id], [isGlobal], [recipienttype], [isAutoSubscribeEnabled], [externalSystem]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_SUBMITTED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Corporate user submits a cancellation request for the bill', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] ([alertSubTypeId], [languageCode], [displayName], [description]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_SUBMITTED', 'en-US', 'Corporate user submits a cancellation request for the bill', 'Corporate user submits a cancellation request for the bill');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] ([appId], [alertSubTypeId]) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_SUBMITTED');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] ([customerTypeId], [alertSubTypeId]) VALUES ('TYPE_ID_BUSINESS', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_SUBMITTED');

INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId]) VALUES ('CH_NOTIFICATION_CENTER', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_SUBMITTED');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId]) VALUES ('CH_PUSH_NOTIFICATION', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_SUBMITTED');

INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text]) VALUES ('TSF1031', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_SUBMITTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your cancellation request has been submitted successfully');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1032', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_SUBMITTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your cancellation request has been submitted successfully', 'Corporate user submits a cancellation request for the bill');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1033', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_SUBMITTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your cancellation request has been submitted successfully', 'Corporate user submits a cancellation request for the bill');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1034', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_SUBMITTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your cancellation request has been submitted successfully', 'Corporate user submits a cancellation request for the bill');

-- ALERT SUBTYPE: TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DONE
INSERT INTO [${dbxschemaname}].[eventsubtype] ([id], [eventtypeid], [Name], [externalSystem]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DONE', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Alert when Corporate user selected a file for upload to create a bill', '0');

INSERT INTO [${dbxschemaname}].[alertsubtype] ([id], [AlertTypeId], [Name], [isAccountLevel], [Status_id], [isGlobal], [recipienttype], [isAutoSubscribeEnabled], [externalSystem]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DONE', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Corporate user selected a file for upload to create a bill', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] ([alertSubTypeId], [languageCode], [displayName], [description]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DONE', 'en-US', 'Corporate user selected a file for upload to create a bill', 'Corporate user selected a file for upload to create a bill');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] ([appId], [alertSubTypeId]) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DONE');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] ([customerTypeId], [alertSubTypeId]) VALUES ('TYPE_ID_BUSINESS', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DONE');

INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId]) VALUES ('CH_NOTIFICATION_CENTER', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DONE');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId]) VALUES ('CH_PUSH_NOTIFICATION', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DONE');

INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text]) VALUES ('TSF1041', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DONE', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your file import has done');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1042', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DONE', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your file import has done', 'Corporate user selected a file for upload to create a bill');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1043', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DONE', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your file import has done', 'Corporate user selected a file for upload to create a bill');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1044', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DONE', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your file import has done', 'Corporate user selected a file for upload to create a bill');

-- ALERT SUBTYPE: TSF_RECEIVABLE_SINGLE_BILL_IMPORT_FAILED
INSERT INTO [${dbxschemaname}].[eventsubtype] ([id], [eventtypeid], [Name], [externalSystem]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_IMPORT_FAILED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Alert when Corporate user uploaded a file and import has failed', '0');

INSERT INTO [${dbxschemaname}].[alertsubtype] ([id], [AlertTypeId], [Name], [isAccountLevel], [Status_id], [isGlobal], [recipienttype], [isAutoSubscribeEnabled], [externalSystem]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_IMPORT_FAILED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Corporate user uploaded a file and import has failed', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] ([alertSubTypeId], [languageCode], [displayName], [description]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_IMPORT_FAILED', 'en-US', 'Corporate user uploaded a file and import has failed', 'Corporate user uploaded a file and import has failed');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] ([appId], [alertSubTypeId]) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_FAILED');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] ([customerTypeId], [alertSubTypeId]) VALUES ('TYPE_ID_BUSINESS', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_FAILED');

INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId]) VALUES ('CH_NOTIFICATION_CENTER', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_FAILED');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId]) VALUES ('CH_PUSH_NOTIFICATION', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_FAILED');

INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text]) VALUES ('TSF1051', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_FAILED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Unfortunately, the file import has been failed.');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1052', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_FAILED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Unfortunately, the file import has been failed.', 'DESCRIPTIONN');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1053', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_FAILED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Unfortunately, the file import has been failed.', 'DESCRIPTIONN');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1054', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_FAILED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Unfortunately,the file import has been failed.', 'DESCRIPTIONN');

-- ALERT SUBTYPE: TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DELETED
INSERT INTO [${dbxschemaname}].[eventsubtype] ([id], [eventtypeid], [Name], [externalSystem]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DELETED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Alert when Corporate user can delete the already extracted details to create bill from the file import tab before review & submit action', '0');

INSERT INTO [${dbxschemaname}].[alertsubtype] ([id], [AlertTypeId], [Name], [isAccountLevel], [Status_id], [isGlobal], [recipienttype], [isAutoSubscribeEnabled], [externalSystem]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DELETED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Corporate user can delete the already extracted details to create bill from the file import tab before review & submit action', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] ([alertSubTypeId], [languageCode], [displayName], [description]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DELETED', 'en-US', 'Corporate user can delete the already extracted details to create bill from the file import tab before review & submit action', 'Corporate user can delete the already extracted details to create bill from the file import tab before review & submit action');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] ([appId], [alertSubTypeId]) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DELETED');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] ([customerTypeId], [alertSubTypeId]) VALUES ('TYPE_ID_BUSINESS', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DELETED');

INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId]) VALUES ('CH_NOTIFICATION_CENTER', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DELETED');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId]) VALUES ('CH_PUSH_NOTIFICATION', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DELETED');

INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text]) VALUES ('TSF1061', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DELETED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your file import has been deleted');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1062', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DELETED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your file import has been deleted', 'Corporate user can delete the already extracted details to create bill from the file import tab before review & submit action');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1063', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DELETED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your file import has been deleted', 'Corporate user can delete the already extracted details to create bill from the file import tab before review & submit action');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1064', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DELETED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your file import has been deleted', 'Corporate user can delete the already extracted details to create bill from the file import tab before review & submit action');

-- ALERT SUBTYPE: TSF_RECEIVABLE_SINGLE_BILL_IMPORTED_BILLS_SUBMITTED
INSERT INTO [${dbxschemaname}].[eventsubtype] ([id], [eventtypeid], [Name], [externalSystem]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_IMPORTED_BILLS_SUBMITTED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Alert when Corporate user reviewed and submitted all the bill created from the file uploaded', '0');

INSERT INTO [${dbxschemaname}].[alertsubtype] ([id], [AlertTypeId], [Name], [isAccountLevel], [Status_id], [isGlobal], [recipienttype], [isAutoSubscribeEnabled], [externalSystem]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_IMPORTED_BILLS_SUBMITTED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Corporate user reviewed and submitted all the bill created from the file uploaded', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] ([alertSubTypeId], [languageCode], [displayName], [description]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_IMPORTED_BILLS_SUBMITTED', 'en-US', 'Corporate user reviewed and submitted all the bill created from the file uploaded', 'Corporate user reviewed and submitted all the bill created from the file uploaded');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] ([appId], [alertSubTypeId]) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORTED_BILLS_SUBMITTED');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] ([customerTypeId], [alertSubTypeId]) VALUES ('TYPE_ID_BUSINESS', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORTED_BILLS_SUBMITTED');

INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId]) VALUES ('CH_NOTIFICATION_CENTER', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORTED_BILLS_SUBMITTED');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId]) VALUES ('CH_PUSH_NOTIFICATION', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORTED_BILLS_SUBMITTED');

INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text]) VALUES ('TSF1071', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORTED_BILLS_SUBMITTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your bills submitted successfully');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1072', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORTED_BILLS_SUBMITTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your bills submitted successfully', 'Corporate user reviewed and submitted all the bill created from the file uploaded');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1073', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORTED_BILLS_SUBMITTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your bills submitted successfully', 'Corporate user reviewed and submitted all the bill created from the file uploaded');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1074', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORTED_BILLS_SUBMITTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your bills submitted successfully', 'Corporate user reviewed and submitted all the bill created from the file uploaded');

-- ALERT SUBTYPE: TSF_RECEIVABLE_SINGLE_BILL_RETURNED_BY_BANK
INSERT INTO [${dbxschemaname}].[eventsubtype] ([id], [eventtypeid], [Name], [externalSystem]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_RETURNED_BY_BANK', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Alert when Submitted bill had been returned by the bank for some changes required', '0');

INSERT INTO [${dbxschemaname}].[alertsubtype] ([id], [AlertTypeId], [Name], [isAccountLevel], [Status_id], [isGlobal], [recipienttype], [isAutoSubscribeEnabled], [externalSystem]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_RETURNED_BY_BANK', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Submitted bill had been returned by the bank for some changes required', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] ([alertSubTypeId], [languageCode], [displayName], [description]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_RETURNED_BY_BANK', 'en-US', 'Submitted bill had been returned by the bank for some changes required', 'Submitted bill had been returned by the bank for some changes required');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] ([appId], [alertSubTypeId]) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TSF_RECEIVABLE_SINGLE_BILL_RETURNED_BY_BANK');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] ([customerTypeId], [alertSubTypeId]) VALUES ('TYPE_ID_BUSINESS', 'TSF_RECEIVABLE_SINGLE_BILL_RETURNED_BY_BANK');

INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId]) VALUES ('CH_NOTIFICATION_CENTER', 'TSF_RECEIVABLE_SINGLE_BILL_RETURNED_BY_BANK');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId]) VALUES ('CH_PUSH_NOTIFICATION', 'TSF_RECEIVABLE_SINGLE_BILL_RETURNED_BY_BANK');

INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text]) VALUES ('TSF1081', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_RETURNED_BY_BANK', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been returned by bank');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1082', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_RETURNED_BY_BANK', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been returned by bank', 'Submitted bill had been returned by the bank for some changes required');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1083', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_RETURNED_BY_BANK', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been returned by bank', 'Submitted bill had been returned by the bank for some changes required');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1084', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_RETURNED_BY_BANK', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been returned by bank', 'Submitted bill had been returned by the bank for some changes required');

-- ALERT SUBTYPE: TSF_RECEIVABLE_SINGLE_BILL_APPROVED
INSERT INTO [${dbxschemaname}].[eventsubtype] ([id], [eventtypeid], [Name], [externalSystem]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_APPROVED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Alert when Submitted bill had been Approved by the bank', '0');

INSERT INTO [${dbxschemaname}].[alertsubtype] ([id], [AlertTypeId], [Name], [isAccountLevel], [Status_id], [isGlobal], [recipienttype], [isAutoSubscribeEnabled], [externalSystem]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_APPROVED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Submitted bill had been Approved by the bank', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] ([alertSubTypeId], [languageCode], [displayName], [description]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_APPROVED', 'en-US', 'Submitted bill had been Approved by the bank', 'Submitted bill had been Approved by the bank');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] ([appId], [alertSubTypeId]) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TSF_RECEIVABLE_SINGLE_BILL_APPROVED');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] ([customerTypeId], [alertSubTypeId]) VALUES ('TYPE_ID_BUSINESS', 'TSF_RECEIVABLE_SINGLE_BILL_APPROVED');

INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId]) VALUES ('CH_NOTIFICATION_CENTER', 'TSF_RECEIVABLE_SINGLE_BILL_APPROVED');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId]) VALUES ('CH_PUSH_NOTIFICATION', 'TSF_RECEIVABLE_SINGLE_BILL_APPROVED');

INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text]) VALUES ('TSF1091', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_APPROVED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been approved by bank');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1092', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_APPROVED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been approved by bank', 'Submitted bill had been Approved by the bank');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1093', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_APPROVED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been approved by bank', 'Submitted bill had been Approved by the bank');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1094', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_APPROVED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been approved by bank', 'Submitted bill had been Approved by the bank');

-- ALERT SUBTYPE: TSF_RECEIVABLE_SINGLE_BILL_REJECTED
INSERT INTO [${dbxschemaname}].[eventsubtype] ([id], [eventtypeid], [Name], [externalSystem]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_REJECTED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Alert when Submited bill had been Rejected by the bank', '0');

INSERT INTO [${dbxschemaname}].[alertsubtype] ([id], [AlertTypeId], [Name], [isAccountLevel], [Status_id], [isGlobal], [recipienttype], [isAutoSubscribeEnabled], [externalSystem]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_REJECTED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Submited bill had been Rejected by the bank', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] ([alertSubTypeId], [languageCode], [displayName], [description]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_REJECTED', 'en-US', 'Submited bill had been Rejected by the bank', 'Submited bill had been Rejected by the bank');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] ([appId], [alertSubTypeId]) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TSF_RECEIVABLE_SINGLE_BILL_REJECTED');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] ([customerTypeId], [alertSubTypeId]) VALUES ('TYPE_ID_BUSINESS', 'TSF_RECEIVABLE_SINGLE_BILL_REJECTED');

INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId]) VALUES ('CH_NOTIFICATION_CENTER', 'TSF_RECEIVABLE_SINGLE_BILL_REJECTED');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId]) VALUES ('CH_PUSH_NOTIFICATION', 'TSF_RECEIVABLE_SINGLE_BILL_REJECTED');

INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text]) VALUES ('TSF1101', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_REJECTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been rejected by bank');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1102', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_REJECTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been rejected by bank', 'Submited bill had been Rejected by the bank');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1103', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_REJECTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been rejected by bank', 'Submited bill had been Rejected by the bank');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1104', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_REJECTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been rejected by bank', 'Submited bill had been Rejected by the bank');

-- ALERT SUBTYPE: TSF_RECEIVABLE_SINGLE_BILL_FINANCE_REQUESTED
INSERT INTO [${dbxschemaname}].[eventsubtype] ([id], [eventtypeid], [Name], [externalSystem]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_FINANCE_REQUESTED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Alert when Submitted bill had been approved but still it was not financed means', '0');

INSERT INTO [${dbxschemaname}].[alertsubtype] ([id], [AlertTypeId], [Name], [isAccountLevel], [Status_id], [isGlobal], [recipienttype], [isAutoSubscribeEnabled], [externalSystem]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_FINANCE_REQUESTED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Submitted bill had been approved but still it was not financed means', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] ([alertSubTypeId], [languageCode], [displayName], [description]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_FINANCE_REQUESTED', 'en-US', 'Submitted bill had been approved but still it was not financed means', 'Submitted bill had been approved but still it was not financed means');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] ([appId], [alertSubTypeId]) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TSF_RECEIVABLE_SINGLE_BILL_FINANCE_REQUESTED');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] ([customerTypeId], [alertSubTypeId]) VALUES ('TYPE_ID_BUSINESS', 'TSF_RECEIVABLE_SINGLE_BILL_FINANCE_REQUESTED');

INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId]) VALUES ('CH_NOTIFICATION_CENTER', 'TSF_RECEIVABLE_SINGLE_BILL_FINANCE_REQUESTED');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId]) VALUES ('CH_PUSH_NOTIFICATION', 'TSF_RECEIVABLE_SINGLE_BILL_FINANCE_REQUESTED');

INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text]) VALUES ('TSF1111', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_FINANCE_REQUESTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been still in finance requested status');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1112', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_FINANCE_REQUESTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been still in finance requested status', 'Submitted bill had been approved but still it was not financed means');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1113', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_FINANCE_REQUESTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been still in finance requested status', 'Submitted bill had been approved but still it was not financed means');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1114', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_FINANCE_REQUESTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been still in finance requested status', 'Submitted bill had been approved but still it was not financed means');

-- ALERT SUBTYPE: TSF_RECEIVABLE_SINGLE_BILL_FINANCED
INSERT INTO [${dbxschemaname}].[eventsubtype] ([id], [eventtypeid], [Name], [externalSystem]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_FINANCED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Alert when Submitted bill had been approved but still it was financed means', '0');

INSERT INTO [${dbxschemaname}].[alertsubtype] ([id], [AlertTypeId], [Name], [isAccountLevel], [Status_id], [isGlobal], [recipienttype], [isAutoSubscribeEnabled], [externalSystem]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_FINANCED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Submitted bill had been approved but still it was financed means', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] ([alertSubTypeId], [languageCode], [displayName], [description]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_FINANCED', 'en-US', 'Submitted bill had been approved but still it was financed means', 'Submitted bill had been approved but still it was financed means');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] ([appId], [alertSubTypeId]) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TSF_RECEIVABLE_SINGLE_BILL_FINANCED');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] ([customerTypeId], [alertSubTypeId]) VALUES ('TYPE_ID_BUSINESS', 'TSF_RECEIVABLE_SINGLE_BILL_FINANCED');

INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId]) VALUES ('CH_NOTIFICATION_CENTER', 'TSF_RECEIVABLE_SINGLE_BILL_FINANCED');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId]) VALUES ('CH_PUSH_NOTIFICATION', 'TSF_RECEIVABLE_SINGLE_BILL_FINANCED');

INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text]) VALUES ('TSF1121', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_FINANCED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been financed');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1122', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_FINANCED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been financed', 'Submitted bill had been approved but still it was financed means');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1123', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_FINANCED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been financed', 'Submitted bill had been approved but still it was financed means');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1124', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_FINANCED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been financed', 'Submitted bill had been approved but still it was financed means');

-- ALERT SUBTYPE: TSF_RECEIVABLE_SINGLE_BILL_SETTLED
INSERT INTO [${dbxschemaname}].[eventsubtype] ([id], [eventtypeid], [Name], [externalSystem]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_SETTLED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Alert when Submitted bill had been approved and finally it has been settled means', '0');

INSERT INTO [${dbxschemaname}].[alertsubtype] ([id], [AlertTypeId], [Name], [isAccountLevel], [Status_id], [isGlobal], [recipienttype], [isAutoSubscribeEnabled], [externalSystem]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_SETTLED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Submitted bill had been approved and finally it has been settled means', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] ([alertSubTypeId], [languageCode], [displayName], [description]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_SETTLED', 'en-US', 'Submitted bill had been approved and finally it has been settled means', 'Submitted bill had been approved and finally it has been settled means');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] ([appId], [alertSubTypeId]) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TSF_RECEIVABLE_SINGLE_BILL_SETTLED');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] ([customerTypeId], [alertSubTypeId]) VALUES ('TYPE_ID_BUSINESS', 'TSF_RECEIVABLE_SINGLE_BILL_SETTLED');

INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId]) VALUES ('CH_NOTIFICATION_CENTER', 'TSF_RECEIVABLE_SINGLE_BILL_SETTLED');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId]) VALUES ('CH_PUSH_NOTIFICATION', 'TSF_RECEIVABLE_SINGLE_BILL_SETTLED');

INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text]) VALUES ('TSF1131', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_SETTLED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been settled');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1132', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_SETTLED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been settled', 'Submitted bill had been approved and finally it has been settled means');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1133', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_SETTLED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been settled', 'Submitted bill had been approved and finally it has been settled means');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1134', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_SETTLED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been settled', 'Submitted bill had been approved and finally it has been settled means');

-- ALERT SUBTYPE: TSF_RECEIVABLE_SINGLE_BILL_CANCELLED
INSERT INTO [${dbxschemaname}].[eventsubtype] ([id], [eventtypeid], [Name], [externalSystem]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_CANCELLED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Alert when Submitted bill had been cancelled', '0');

INSERT INTO [${dbxschemaname}].[alertsubtype] ([id], [AlertTypeId], [Name], [isAccountLevel], [Status_id], [isGlobal], [recipienttype], [isAutoSubscribeEnabled], [externalSystem]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_CANCELLED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Submitted bill had been cancelled', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] ([alertSubTypeId], [languageCode], [displayName], [description]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_CANCELLED', 'en-US', 'Submitted bill had been cancelled', 'Submitted bill had been cancelled');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] ([appId], [alertSubTypeId]) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLED');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] ([customerTypeId], [alertSubTypeId]) VALUES ('TYPE_ID_BUSINESS', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLED');

INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId]) VALUES ('CH_NOTIFICATION_CENTER', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLED');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId]) VALUES ('CH_PUSH_NOTIFICATION', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLED');

INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text]) VALUES ('TSF1141', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been cancelled');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1142', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been cancelled', 'Submitted bill had been cancelled');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1143', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been cancelled', 'Submitted bill had been cancelled');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1144', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been cancelled', 'Submitted bill had been cancelled');

-- ALERT SUBTYPE: TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_APPROVED
INSERT INTO [${dbxschemaname}].[eventsubtype] ([id], [eventtypeid], [Name], [externalSystem]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_APPROVED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Alert when Submitted bill for request cancellation had been approved by the bank', '0');

INSERT INTO [${dbxschemaname}].[alertsubtype] ([id], [AlertTypeId], [Name], [isAccountLevel], [Status_id], [isGlobal], [recipienttype], [isAutoSubscribeEnabled], [externalSystem]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_APPROVED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Submitted bill for request cancellation had been approved by the bank', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] ([alertSubTypeId], [languageCode], [displayName], [description]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_APPROVED', 'en-US', 'Submitted bill for request cancellation had been approved by the bank', 'Submitted bill for request cancellation had been approved by the bank');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] ([appId], [alertSubTypeId]) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_APPROVED');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] ([customerTypeId], [alertSubTypeId]) VALUES ('TYPE_ID_BUSINESS', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_APPROVED');

INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId]) VALUES ('CH_NOTIFICATION_CENTER', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_APPROVED');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId]) VALUES ('CH_PUSH_NOTIFICATION', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_APPROVED');

INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text]) VALUES ('TSF1151', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_APPROVED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your cancellation request for bill - [#]orderId[/#] has been approved');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1152', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_APPROVED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your cancellation request for bill - [#]orderId[/#] has been approved', 'Submitted bill for request cancellation had been approved by the bank');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1153', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_APPROVED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your cancellation request for bill - [#]orderId[/#] has been approved', 'Submitted bill for request cancellation had been approved by the bank');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1154', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_APPROVED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your cancellation request for bill - [#]orderId[/#] has been approved', 'Submitted bill for request cancellation had been approved by the bank');

-- ALERT SUBTYPE: TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_REJECTED
INSERT INTO [${dbxschemaname}].[eventsubtype] ([id], [eventtypeid], [Name], [externalSystem]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_REJECTED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Alert when Submitted bill for request cancellation had been rejected by the bank', '0');

INSERT INTO [${dbxschemaname}].[alertsubtype] ([id], [AlertTypeId], [Name], [isAccountLevel], [Status_id], [isGlobal], [recipienttype], [isAutoSubscribeEnabled], [externalSystem]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_REJECTED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Submitted bill for request cancellation had been rejected by the bank', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] ([alertSubTypeId], [languageCode], [displayName], [description]) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_REJECTED', 'en-US', 'Submitted bill for request cancellation had been rejected by the bank', 'Submitted bill for request cancellation had been rejected by the bank');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] ([appId], [alertSubTypeId]) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_REJECTED');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] ([customerTypeId], [alertSubTypeId]) VALUES ('TYPE_ID_BUSINESS', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_REJECTED');

INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId]) VALUES ('CH_NOTIFICATION_CENTER', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_REJECTED');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId]) VALUES ('CH_PUSH_NOTIFICATION', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_REJECTED');

INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text]) VALUES ('TSF1161', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_REJECTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your cancellation request for bill - [#]orderId[/#] has been rejected');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1162', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_REJECTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your cancellation request for bill - [#]orderId[/#] has been rejected', 'Submitted bill for request cancellation had been rejected by the bank');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1163', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_REJECTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your cancellation request for bill - [#]orderId[/#] has been rejected', 'Submitted bill for request cancellation had been rejected by the bank');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Text], [Subject]) VALUES ('TSF1164', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_REJECTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your cancellation request for bill - [#]orderId[/#] has been rejected', 'Submitted bill for request cancellation had been rejected by the bank');

UPDATE [${dbxschemaname}].[configurations] SET config_value = '[\"Unhappy with our service\",\"Dissatisfied with our Product Offering\", \"Minimum Balance\/Charges are on Higher side\",\"Other\"]' WHERE (configuration_id = 'ACCL_1');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'GROUP_ADMINISTRATOR', 'SET_DEFAULT_ENTITY_PREFERENCE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'GROUP_CREATOR', 'SET_DEFAULT_ENTITY_PREFERENCE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'SET_DEFAULT_ENTITY_PREFERENCE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '5801fa32-a416-45b6-af01-b22e2de93777', 'SET_DEFAULT_ENTITY_PREFERENCE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'SET_DEFAULT_ENTITY_PREFERENCE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'bef2fe82-9c21-4ccb-b599-3308de18de44', 'SET_DEFAULT_ENTITY_PREFERENCE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'SET_DEFAULT_ENTITY_PREFERENCE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'SET_DEFAULT_ENTITY_PREFERENCE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

UPDATE [${dbxschemaname}].[configurations] SET config_value = '{\"SAVINGS.PLAN\":\"Deposit\",\"DEPOSIT.CALL\":\"Deposit\",\"CURRENT.ACCOUNT\":\"Checking\",\"SS.ANNUAL\":\"Checking\",\"SS.ROLLOVER.01M\":\"Deposit\",\"NEGOTIABLE.LOAN\":\"Loan\",\"CURRENT.ACCOUNT.SME\":\"Checking\",\"PREMIUM.ACCOUNT\":\"Checking\",\"CURRENT.ACCOUNT.STUDENT\":\"Checking\",\"SAVINGS.ACCOUNT\":\"Savings\",\"CONS.SAVING\":\"Savings\",\"SAVINGS.SALARY.INFINITY\":\"Savings\",\"MORTGAGE.FLOATING\":\"Loan\",\"TERM.DEPOSIT\":\"Deposit\",\"CURRENT.ACCOUNT.STAFF\":\"Checking\",\"CURRENT.ACCOUNT.PREF\":\"Checking\",\"CONS.CHECKING\":\"Checking\",\"SAVINGS.ACCOUNT.WELCOME\":\"Savings\",\"SAVINGS.STANDARD.INFINITY\":\"Savings\",\"PREFER.ACCOUNT\":\"Checking\",\"STUDENT.ACCOUNT\":\"Checking\",\"MORTGAGE.FIX5Y.60LTV\":\"Loan\",\"DEPOSIT.SHORT\":\"Deposit\",\"DEPOSIT.5Y\":\"Deposit\",\"DEPOSIT.3Y\":\"Deposit\",\"ADVANCED.CHECKING.ACCOUNT\":\"Checking\",\"SAVINGS.ACCOUNT.PROMOTIONAL\":\"Savings\",\"SAVINGS.ACCOUNT.FCY\":\"Savings\",\"SAVINGS.ACCOUNT.MINOR\":\"Savings\",\"DEPOSIT.09M\":\"Deposit\",\"BASIC.CHECKING.ACCOUNT\":\"Checking\",\"SS.FIXED.TERM\":\"Deposit\",\"SS.MONTHLY\":\"Checking\",\"MORTGAGE\":\"Loan\",\"SS.SAVINGS.REGULAR\":\"Savings\",\"PERSONAL.LOAN\":\"Loan\",\"SS.PAYG\":\"Checking\",\"SAVINGS.PRIME.INFINITY\":\"Savings\",\"CONS.MM\":\"Savings\",\"DEPOSIT.LONG\":\"Deposit\",\"VEHICLE.LOAN\":\"Loan\",\"SAVINGS.DEFAULT\":\"Savings\",\"PREFERRED.CHECKING.ACCOUNT\":\"Checking\",\"SAVINGS.ACCOUNT.WLC\":\"Savings\",\"SS.SAVINGS.CHILD\":\"Savings\",\"SAVINGS.ACCOUNT.NOTICE\":\"Savings\",\"BONDS.A.6M\":\"Deposit\",\"BONDS.B.1Y\":\"Deposit\",\"BONDS.C.3Y\":\"Deposit\",\"CURRENT.ACCOUNT.GEN\":\"Checking\",\"CURRENT.ACCOUNT.LINK\":\"Checking\",\"CURRENT.DEFAULT\":\"Checking\",\"CURRENT.PARENT\":\"Checking\",\"CURRENT.PARENT.INFINITY\":\"Checking\",\"CURRENT.PARENT.PREF\":\"Checking\",\"CURRENT.PARENT.SME\":\"Checking\",\"CURRENT.PARENT.STD\":\"Checking\",\"CURRENT.SHADOW\":\"Checking\",\"DEPOSIT.03M\":\"Deposit\",\"DEPOSIT.06M\":\"Deposit\",\"DEPOSIT.12M\":\"Deposit\",\"DEPOSIT.18M\":\"Deposit\",\"DEPOSIT.2Y\":\"Deposit\",\"DEPOSIT.4Y\":\"Deposit\",\"DEPOSIT.DEFAULT\":\"Deposit\",\"DEPOSIT.MAT\":\"Deposit\",\"DEPOSIT.NEGOTIABLE\":\"Deposit\",\"DEPOSIT.PARENT\":\"Deposit\",\"EBKM.DEPOSIT\":\"Deposit\",\"EXT.BN.PARENT\":\"Deposit\",\"EXT.DEPOSIT.PARENT\":\"Deposit\",\"INSTALLMENT.12M\":\"Loan\",\"INSTALLMENT.3M\":\"Loan\",\"INSTALLMENT.6M\":\"Loan\",\"INSTALLMENT.LOAN.PARENT\":\"Loan\",\"MORTGAGE.ARM\":\"Mortgage\",\"MORTGAGE.CASHBACK\":\"Mortgage\",\"MORTGAGE.FACILITY.PARENT\":\"Mortgage\",\"MORTGAGE.FEP\":\"Mortgage\",\"MORTGAGE.LINK\":\"Mortgage\",\"MORTGAGE.OFFER\":\"Mortgage\",\"MORTGAGE.OFFSET\":\"Mortgage\",\"MORTGAGE.PARENT\":\"Mortgage\",\"MORTGAGE.SEASONAL\":\"Mortgage\",\"PERSONAL.LOAN.2W\":\"Loan\",\"PERSONAL.LOAN.FWD\":\"Loan\",\"PERSONAL.LOAN.LINK\":\"Loan\",\"SAVINGS.PACKAGE\":\"Savings\",\"SAVINGS.PARENT\":\"Savings\",\"SAVINGS.PARENT.INFINITY\":\"Savings\",\"SAVINGS.PARENT.PREF\":\"Savings\",\"SAVINGS.PARENT.STD\":\"Savings\",\"SMALL.BUSINESS.LOAN\":\"Loan\",\"SME.ACCOUNT\":\"Checking\",\"SSA.ACCOUNT\":\"Checking\",\"STAFF.ACCOUNT\":\"Savings\",\"CORP.CURRENT.ACCOUNT\":\"Checking\",\"CL.FACILITY\":\"Sprout\",\"STUDENT.LOAN\":\"Loan\",\"MORTGAGE.FACILITY\":\"Mortgages\",\"BB.SMALL.BUSINESS.LOAN\":\"Loan\",\"BB.PREMIUM.ACCOUNT\":\"Checking\",\"BB.STANDARD.ACCOUNT\":\"Checking\",\"BB.START.UP.ACCOUNT\":\"Checking\",\"RES.SAVINGS.ACCOUNT-20200101\":\"Savings\",\"RES.NOTICE.ACCOUNT-20200101\":\"Savings\",\"RES.FCY.SAVINGS.ACCOUNT-20200101\":\"Savings\",\"RES.SAVINGS.PARENT-20200101\":\"Savings\",\"RES.CURRENT.ACCOUNT-20200101\":\"Checking\",\"RES.PREMIUM.ACCOUNT-20200101\":\"Checking\",\"RES.MULTI.CURRENCY.ACCOUNT-20200101\":\"Checking\",\"RES.CURRENT.PARENT-20200101\":\"Checking\",\"RES.MCY.SUB.ACCOUNT-20200101\":\"Checking\",\"RES.SHORT.TERM.AUTO.ROLL-20200101\":\"Deposit\",\"RES.SHORT.TERM.MANUAL.ROLL-20200101\":\"Deposit\",\"RES.LONG.TERM.FLEXI.PAY-20200101\":\"Deposit\",\"RES.LONG.TERM.HIGH.EARN-20200101\":\"Deposit\",\"RES.SAVINGS.PLAN-20200101\":\"Deposit\",\"RES.CONSUMER.LOAN-20200101\":\"Loan\",\"RES.CREDIT.LINE.PARENT-20200101\":\"Loan\",\"RES.MORTGAGE.FACILITIES-20210101\":\"Mortgages\",\"RES.10Y.FIXED.LINEAR-20210101\":\"Mortgages\",\"RES.2Y.FIXED.CONST-20210101\":\"Mortgages\",\"RES.5Y.TRACKER.CONST-20210101\":\"Mortgages\",\"RES.BNPL.FACILITY-20210101\":\"Loan\",\"RES.PAYIN.3-20210101\":\"Loan\",\"RES.PAYIN.3M-20210101\":\"Loan\",\"RES.PAST.PURCHASE-20210101\":\"Loan\",\"RES.CONSUMER.LOAN.PARENT-20200101\":\"Loan\",\"RES.CREDIT.LINE-20200101\":\"Loan\"}' WHERE (configuration_id = '172');

UPDATE [${dbxschemaname}].[service_permission_mapper] SET [permissions] = 'API_ACCESS,USER_MANAGEMENT_VIEW' WHERE
([id] = 'ggf944-f522-75364-63ea-5dd627e92281h1');

UPDATE [${dbxschemaname}].[service_permission_mapper] SET [permissions] = 'API_ACCESS,USER_MANAGEMENT_VIEW' WHERE ([id] = 'ggf944-f522-75364-63ea-5dd627e92281h2');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name],[object_name],[operation],[permissions]) VALUES
(NEWID(),'ExternalUserManagement','Contract','addNewFeaturesTOContractFromSD','API_ACCESS');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name],[object_name],[operation],[permissions]) VALUES
(NEWID(),'ExternalUserManagement','Contract','DBXCustomerCommunicationDetails','API_ACCESS');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name],[object_name],[operation],[permissions]) VALUES
(NEWID(),'ExternalUserManagement','Contract','getCoreCustomerDetails','API_ACCESS');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name],[object_name],[operation],[permissions]) VALUES
(NEWID(),'ExternalUserManagement','Contract','getCoreCustomerProductRolesFeatureActionLimits','API_ACCESS');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name],[object_name],[operation],[permissions]) VALUES
(NEWID(),'ExternalUserManagement','Contract','getProductPermissions','API_ACCESS');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name],[object_name],[operation],[permissions]) VALUES
(NEWID(),'ExternalUserManagement','Contract','getServiceDefinitionProductIdPermissions','API_ACCESS');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name],[object_name],[operation],[permissions]) VALUES
(NEWID(),'ExternalUserManagement','EnrollmentSecurity','generateCaptchaForEnrollment','ALLOW,API_ACCESS');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name],[object_name],[operation],[permissions]) VALUES
(NEWID(),'ExternalUserManagement','ExternalUsers','getCustomerIdentifiers','API_ACCESS,ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name],[object_name],[operation],[permissions]) VALUES
(NEWID(),'ExternalUserManagement','ExternalUsers_1','enrollRetailUser','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name],[object_name],[operation],[permissions]) VALUES
(NEWID(),'ExternalUserManagement','ExternalUsers_1','getInfinityUserAccountsForCorecustomer','API_ACCESS');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name],[object_name],[operation],[permissions]) VALUES
(NEWID(),'ExternalUserManagement','ExternalUsers_1','getInfinityUserServiceDefsRoles','API_ACCESS');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name],[object_name],[operation],[permissions]) VALUES
(NEWID(),'ExternalUserManagement','ExternalUsers_2','getAddressTypes','ALLOW,API_ACCESS');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name],[object_name],[operation],[permissions]) VALUES
(NEWID(),'ExternalUserManagement','search','CustomerLegalEntitiesGetOperation','ALLOW,API_ACCESS');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name],[object_name],[operation],[permissions]) VALUES
(NEWID(),'ExternalUserManagement','search','customerSearchByUserName','API_ACCESS');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name],[object_name],[operation],[permissions]) VALUES
(NEWID(),'ExternalUserManagement','search','UserIdSearchOperationDetailedData','API_ACCESS');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name],[object_name],[operation],[permissions]) VALUES
(NEWID(), 'Login', 'Users', 'getLegalEntities', 'ALLOW');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name],[object_name],[operation],[permissions]) VALUES
(NEWID(), 'Login', 'Security', 'verifyCaptcha', 'ALLOW');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name],[object_name],[operation],[permissions]) VALUES
(NEWID(), 'Login', 'Users', 'getFeaturesAndPermissions', 'ALLOW');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name],[object_name],[operation],[permissions]) VALUES
(NEWID(), 'Login', 'Users_2', 'regenerateActivationCode', 'ALLOW');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name],[object_name],[operation],[permissions]) VALUES
(NEWID(), 'SecureMessaging', 'Message', 'deleteAttachement', 'ALLOW');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name],[object_name],[operation],[permissions]) VALUES
(NEWID(), 'SecureMessaging', 'Message', 'get', 'ALLOW');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name],[object_name],[operation],[permissions]) VALUES
(NEWID(), 'MessageBinary', 'media', 'update', 'ALLOW');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name],[object_name],[operation],[permissions]) VALUES
(NEWID(), 'MessageBinary', 'media', 'get', 'ALLOW');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name],[object_name],[operation],[permissions]) VALUES
(NEWID(), 'MessageBinary', 'media', 'delete', 'ALLOW');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name],[object_name],[operation],[permissions]) VALUES
(NEWID(), 'MessageBinary', 'media', 'updateBinary', 'ALLOW');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name],[object_name],[operation],[permissions]) VALUES
(NEWID(), 'MessageBinary', 'media', 'deleteBinary', 'ALLOW');


GO
/* Updating companyLegalUnit to ALL from GB0010001 for the feature/featureaction related tables */
UPDATE [${dbxschemaname}].[feature] SET [companyLegalUnit] = 'ALL' WHERE [id] IN ('PORTFOLIO_HEALTH', 'REVIEW_STRATEGY', 'INVESTMENT_PROPOSAL');

UPDATE [${dbxschemaname}].[featuredisplaynamedescription] SET [companyLegalUnit] = 'ALL' WHERE [Feature_id] IN ('PORTFOLIO_HEALTH', 'REVIEW_STRATEGY', 'INVESTMENT_PROPOSAL');

UPDATE [${dbxschemaname}].[featureroletype] SET [companyLegalUnit] = 'ALL' WHERE [Feature_id] IN ('PORTFOLIO_HEALTH', 'REVIEW_STRATEGY', 'INVESTMENT_PROPOSAL');

UPDATE [${dbxschemaname}].[featureaction] SET [companyLegalUnit] = 'ALL' WHERE [Feature_id] IN ('PORTFOLIO_HEALTH', 'REVIEW_STRATEGY', 'INVESTMENT_PROPOSAL');

UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [companyLegalUnit] = 'ALL' WHERE [Action_id] IN ('CHANGE_STRATEGY_CONFIRMATION', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT_CHART', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_VIEW', 'INVESTMENT_PROPOSAL_PAST_PROPOSAL_VIEW', 'INVESTMENT_PROPOSAL_VIEW', 'MY_STRATEGY_CHANGE_STRATEGY_VIEW', 'MY_STRATEGY_VIEW', 'PORTFOLIO_HEALTH_ASSET_ALLOCATION_VIEW', 'PORTFOLIO_HEALTH_CONTACT_ADVISOR_VIEW', 'PORTFOLIO_HEALTH_INVESTMENT_CONSTRAINTS_VIEW', 'PORTFOLIO_HEALTH_RECOMMENDED_INSTRUMENTS_VIEW', 'PORTFOLIO_HEALTH_RISK_ANALYSIS_VIEW', 'PORTFOLIO_HEALTH_SUMMARY_VIEW', 'PORTFOLIO_HEALTH_VIEW', 'PORTFOLIO_REVIEW_STRATEGY_VIEW', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT_CHART', 'RECOMMENDED_STRATEGY_CONFIRMATION', 'RECOMMENDED_STRATEGY_USE_VIEW', 'RECOMMENDED_STRATEGY_VIEW', 'STRATEGY_ALLOCATION_PERSONALIZE', 'STRATEGY_ALLOCATION_VIEW', 'SUITABILITY_PROFILE_REVIEW_VIEW', 'SUITABILITY_PROFILE_VIEW');

UPDATE [${dbxschemaname}].[featureactionroletype] SET [companyLegalUnit] = 'ALL' WHERE [Action_id] IN ('CHANGE_STRATEGY_CONFIRMATION', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT_CHART', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_VIEW', 'INVESTMENT_PROPOSAL_PAST_PROPOSAL_VIEW', 'INVESTMENT_PROPOSAL_VIEW', 'MY_STRATEGY_CHANGE_STRATEGY_VIEW', 'MY_STRATEGY_VIEW', 'PORTFOLIO_HEALTH_ASSET_ALLOCATION_VIEW', 'PORTFOLIO_HEALTH_CONTACT_ADVISOR_VIEW', 'PORTFOLIO_HEALTH_INVESTMENT_CONSTRAINTS_VIEW', 'PORTFOLIO_HEALTH_RECOMMENDED_INSTRUMENTS_VIEW', 'PORTFOLIO_HEALTH_RISK_ANALYSIS_VIEW', 'PORTFOLIO_HEALTH_SUMMARY_VIEW', 'PORTFOLIO_HEALTH_VIEW', 'PORTFOLIO_REVIEW_STRATEGY_VIEW', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT_CHART', 'RECOMMENDED_STRATEGY_CONFIRMATION', 'RECOMMENDED_STRATEGY_USE_VIEW', 'RECOMMENDED_STRATEGY_VIEW', 'STRATEGY_ALLOCATION_PERSONALIZE', 'STRATEGY_ALLOCATION_VIEW', 'SUITABILITY_PROFILE_REVIEW_VIEW', 'SUITABILITY_PROFILE_VIEW');

UPDATE [${dbxschemaname}].[compositeaction] SET [companyLegalUnit] = 'ALL' WHERE [Feature_id] IN ('PORTFOLIO_HEALTH', 'REVIEW_STRATEGY', 'INVESTMENT_PROPOSAL');

UPDATE [${dbxschemaname}].[servicedefinitionactionlimit] SET [companyLegalUnit] = 'ALL' WHERE [actionId] IN ('CHANGE_STRATEGY_CONFIRMATION', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT_CHART', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_VIEW', 'INVESTMENT_PROPOSAL_PAST_PROPOSAL_VIEW', 'INVESTMENT_PROPOSAL_VIEW', 'MY_STRATEGY_CHANGE_STRATEGY_VIEW', 'MY_STRATEGY_VIEW', 'PORTFOLIO_HEALTH_ASSET_ALLOCATION_VIEW', 'PORTFOLIO_HEALTH_CONTACT_ADVISOR_VIEW', 'PORTFOLIO_HEALTH_INVESTMENT_CONSTRAINTS_VIEW', 'PORTFOLIO_HEALTH_RECOMMENDED_INSTRUMENTS_VIEW', 'PORTFOLIO_HEALTH_RISK_ANALYSIS_VIEW', 'PORTFOLIO_HEALTH_SUMMARY_VIEW', 'PORTFOLIO_HEALTH_VIEW', 'PORTFOLIO_REVIEW_STRATEGY_VIEW', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT_CHART', 'RECOMMENDED_STRATEGY_CONFIRMATION', 'RECOMMENDED_STRATEGY_USE_VIEW', 'RECOMMENDED_STRATEGY_VIEW', 'STRATEGY_ALLOCATION_PERSONALIZE', 'STRATEGY_ALLOCATION_VIEW', 'SUITABILITY_PROFILE_REVIEW_VIEW', 'SUITABILITY_PROFILE_VIEW');

UPDATE [${dbxschemaname}].[groupactionlimit] SET [companyLegalUnit] = 'ALL' WHERE [Action_id] IN ('CHANGE_STRATEGY_CONFIRMATION', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT_CHART', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_VIEW', 'INVESTMENT_PROPOSAL_PAST_PROPOSAL_VIEW', 'INVESTMENT_PROPOSAL_VIEW', 'MY_STRATEGY_CHANGE_STRATEGY_VIEW', 'MY_STRATEGY_VIEW', 'PORTFOLIO_HEALTH_ASSET_ALLOCATION_VIEW', 'PORTFOLIO_HEALTH_CONTACT_ADVISOR_VIEW', 'PORTFOLIO_HEALTH_INVESTMENT_CONSTRAINTS_VIEW', 'PORTFOLIO_HEALTH_RECOMMENDED_INSTRUMENTS_VIEW', 'PORTFOLIO_HEALTH_RISK_ANALYSIS_VIEW', 'PORTFOLIO_HEALTH_SUMMARY_VIEW', 'PORTFOLIO_HEALTH_VIEW', 'PORTFOLIO_REVIEW_STRATEGY_VIEW', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT_CHART', 'RECOMMENDED_STRATEGY_CONFIRMATION', 'RECOMMENDED_STRATEGY_USE_VIEW', 'RECOMMENDED_STRATEGY_VIEW', 'STRATEGY_ALLOCATION_PERSONALIZE', 'STRATEGY_ALLOCATION_VIEW', 'SUITABILITY_PROFILE_REVIEW_VIEW', 'SUITABILITY_PROFILE_VIEW');

UPDATE [${dbxschemaname}].[actionlimit] SET [companyLegalUnit] = 'ALL' WHERE [Action_id] = 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE';
GO
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name],[object_name],[operation],[permissions]) VALUES
(NEWID(), 'ChequeManagement', 'ChequeBook', 'getChequeType', 'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name],[object_name],[operation],[permissions]) VALUES
(NEWID(), 'ExternalUserManagement', 'ExternalUsers', 'geRequestStatus', 'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name],[operation],[permissions]) VALUES
(NEWID(), 'ServiceRequestJavaService', 'triggerForStatus', 'ALLOW');
GO

UPDATE [${dbxschemaname}].[configurations] SET [config_value] = '{"referenceNumber":"20","currencyAmount":"32B","tolerancePercentage":"39A","maximumCreditAmount":"39B","currencyAdditionalPayableAmount":"39C","paymentTerms":"40A","availableWith":"41A","issueDate":"31C","expiryDate":"31D","expiryPlace":"31D","chargesAccount":"71D","commissionAccount":"NA","marginAccount":"NA","messageToBank":"NA","beneficiaryName":"59","beneficiaryAddress":"59","beneficiaryCity":"59","beneficiaryState":"59","beneficiaryZipCode":"59","beneficiaryBankName":"57A","beneficiaryBankAddress":"57A","beneficiaryBankCity":"57A","beneficiaryBankState":"57A","beneficiaryBankZipCode":"57A","placeOfTakingInCharge":"44A","portOfLoading":"44E","portOfDischarge":"44F","placeOfFinalDelivery":"44B","latestShipmentDate":"44C","transhipment":"43T","partialShipment":"43P","incoTerms":"44D","modeOfShipment":"NA","descriptionOfGoods":"45A","documentsRequired":"46A","additionalCondition":"47A","otherAdditionalCondition":"NA","charges":"71D","confirmationInstructions":"49","transferable":"NA","standByLC":"NA","uploadedDocuments":"NA","sequenceOfTotal":"27","applicableRules":"40E","applicant":"50","draftsAt":"42C","drawee":"42D","loadonBoard":"44A","detailsofcharges":"71B","periodforPresentation":"48","reimbursingBank":"53A","instructionsToThePay":"78","senderToReceiverInformation":"72"}' WHERE ([configuration_id] = '174');
UPDATE [${dbxschemaname}].[configurations] SET [config_value] = '{"Swift Enable":"True"}' WHERE ([configuration_id] = '175');
UPDATE [${dbxschemaname}].[configurations] SET [config_value] = '{"Swift Enable":"True"}' WHERE ([configuration_id] = '176');
UPDATE [${dbxschemaname}].[configurations] SET [config_value] = '{"New Sequence":"15A","Sequence of total":"27","Purpose of Message":"22A","New Sequence1":"15B","Undertaking Number":"15B","Date of issue":"30","Form of Undertaking":"22D","Applicable Rules":"40C","Expiry Type":"23B","Date of Expiry":"31E","Applicant":"50","Issue":"52A","Beneficiary":"59A","Advising Bank":"56A","Advise Through Bank":"57A","Undertaking Amount":"32B","Available With":"41A","Charges":"71D","Document and Presentation Instructions":"45C","Undertaking Terms and Conditions":"77U","Confirmation Instructions":"49","Governing Law and/or Placed of Jurisdiction":"44H","Automatic Extension Period":"23F","Automatic Extension Non-Extension Notification":"78","Automatic Extension Notification Period":"26E","Automatic Extension Final Expiry Date":"31S","Demand Indicator":"48B","Underlying Transaction Details":"45L","Delivery of Original Undertaking":"24E","Delivery ToCollection By":"24G"}' WHERE ([configuration_id] = '177');
UPDATE [${dbxschemaname}].[configurations] SET [config_value] = '[{"DisplayStatus":"Pending Requests","LCStatus":["New","Submitted to Bank","Processing by Bank","Returned by Bank"]},{"DisplayStatus":"Approved","LCStatus":["Approved"]},{"DisplayStatus":"Settled","LCStatus":["Partially Settled"]},{"DisplayStatus":"Rejected","LCStatus":["Rejected","Cancelled"]}]' WHERE ([configuration_id] = 'EXLC_1');
UPDATE [${dbxschemaname}].[configurations] SET [config_value] = '[{"type":"Performance","colorCode":"#E50033"},{"type":"BID","colorCode":"#FF8600"},{"type":"Advance","colorCode":"#229EAE"},{"type":"Shipping","colorCode":"#0971A8"}]' WHERE ([configuration_id] = 'GUA_1');
UPDATE [${dbxschemaname}].[configurations] SET [config_value] = '[{"DisplayStatus":"Pending","LCStatus":["Pending Cust Auth","Returned Cust Auth","Submitted to Bank","Returned by Bank","Processing with Bank"]},{"DisplayStatus":"Drafts","LCStatus":["Draft"]},{"DisplayStatus":"Approved","LCStatus":["Approved"]},{"DisplayStatus":"Rejected","LCStatus":["Rejected by Bank"]}]' WHERE ([configuration_id] = 'GUA_2');
UPDATE [${dbxschemaname}].[configurations] SET [config_value] = '[{"DisplayStatus":"Pending Requests","LCStatus":["Draft","Submitted to Bank","Processing by Bank","Returned by Bank"]},{"DisplayStatus":"Approved","LCStatus":["Approved"]},{"DisplayStatus":"Settled","LCStatus":["Partially Settled"]},{"DisplayStatus":"Rejected","LCStatus":["Rejected","Cancelled"]}]' WHERE ([configuration_id] = 'IMLC_1');
UPDATE [${dbxschemaname}].[configurations] SET [config_value] = '[{"DisplayStatus":"Pending requests","InwardStatus":["New","Processing by Bank","Submitted to Bank","Overdue","Pay Due","Returned by Bank"]},{"DisplayStatus":"Approved","InwardStatus":["Approved"]},{"DisplayStatus":"Settled","InwardStatus":["Settled"]},{"DisplayStatus":"Rejected","InwardStatus":["Rejected"]}]' WHERE ([configuration_id] = 'INCL_1');
UPDATE [${dbxschemaname}].[configurations] SET [config_value] = '[{"type":"sight","colorCode":"#FF8600"},{"type":"usance","colorCode":"#229EAE"}]' WHERE ([configuration_id] = 'INCL_2');
UPDATE [${dbxschemaname}].[configurations] SET [config_value] = '[{"DisplayStatus":"Processing","InwardStatus":["New","Processing by Bank","Submitted to Bank","Overdue","Pay Due"]},{"DisplayStatus":"Approved","InwardStatus":["Approved","Settled"]}]' WHERE ([configuration_id] = 'INCL_3');
UPDATE [${dbxschemaname}].[configurations] SET [config_value] = '[{"type":"Sight","colorCode":"#E50033"},{"type":"Acceptance","colorCode":"#FF8600"},{"type":"Deferred","colorCode":"#229EAE"},{"type":"Negotiation Sight","colorCode":"#0971A8"},{"type":"Negotiation Acceptance","colorCode":"#BDBDBD"}]' WHERE ([configuration_id] = 'LCPT_1');
UPDATE [${dbxschemaname}].[configurations] SET [config_value] = '[{"DisplayStatus":"Pending","outwardStatus":["Submitted to Bank","Returned by Bank","Processing by Bank","Overdue"]}, {"DisplayStatus":"Approved","outwardStatus":["Approved"]},{"DisplayStatus":"Settled","outwardStatus":["Settled"]},{"DisplayStatus":"Rejected","outwardStatus":["Rejected","Cancelled"]}]' WHERE ([configuration_id] = 'OUCL_1');
UPDATE [${dbxschemaname}].[configurations] SET [config_value] = '[{"DisplayStatus":"Pending Requests","LCStatus":["Pending Cust Auth","Returned Cust Auth","Submitted to Bank","Returned by Bank","Processing with Bank","New"]},{"DisplayStatus":"Approved","LCStatus":["Approved","Claim Extended"]},{"DisplayStatus":"Claim Honoured","LCStatus":["Claim Honoured"]},{"DisplayStatus":"Rejected","LCStatus":["Rejected"]}]' WHERE ([configuration_id] = 'RGUA_1');
GO

UPDATE [${dbxschemaname}].[configurations] SET [config_value] = '{"EU":[{"currency":"default","payMethod":["SWIFT"]},{"currency":"EUR","payMethod":["SEPA","INSTANT","SWIFT"]}],"UK":[{"currency":"default","payMethod":["SWIFT"]},{"currency":"GBP","payMethod":["Faster","CHAPS"]},{"currency":"EUR","payMethod":["SEPA","INSTANT","SWIFT"]}],"US":[{"currency":"default","payMethod":["SWIFT"]},{"currency":"USD","payMethod":["ACH","FEDWIRE"]}],"AU":[{"currency":"default","payMethod":["SWIFT"]},{"currency":"AUD","payMethod":["BECS","NPP"]}],"IN":[{"currency":"default","payMethod":["SWIFT"]},{"currency":"INR","payMethod":["NEFT","RTGS","IMPS"]}]}' WHERE ([configuration_id] = 'PAY_202');
UPDATE [${dbxschemaname}].[configurations] SET [config_value] = '["Unhappy with our service","Dissatisfied with our Product Offering", "Minimum Balance/Charges are on Higher side","Other"]' WHERE ([configuration_id] = 'ACCL_1');
GO