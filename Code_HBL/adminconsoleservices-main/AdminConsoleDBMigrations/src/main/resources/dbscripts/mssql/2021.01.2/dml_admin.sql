USE [${dbxdbname}]
GO
INSERT INTO [${dbxschemaname}].[service_permission_mapper]([id],[service_name],[object_name],[operation],[permissions]) VALUES (NEWID() ,'KeyCloakObjService','KeycloakUsers','getKeycloakUsers','ALLOW,API_ACCESS');
GO

INSERT INTO [${dbxschemaname}].[service_permission_mapper]([id],[service_name],[object_name],[operation],[permissions]) VALUES (NEWID() ,'BulkWireObjects','BulkWireFile','initiateDownloadBulkWireSampleFile','ALLOW');
GO