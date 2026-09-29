use [${dbxschemaname}];
ALTER TABLE [${dbxschemaname}].[groupactionlimit] ADD [isNewAction] bit not null DEFAULT 0 ;
GO
ALTER TABLE [${dbxschemaname}].[contractactionlimit] ADD [isNewAction] bit not null DEFAULT 0;
GO
ALTER TABLE [${dbxschemaname}].[contractfeatures] ADD [isNewFeature] bit not null DEFAULT 0 ;
GO
ALTER TABLE [${dbxschemaname}].[servicedefinitionactionlimit] ADD [isNewAction] bit not null DEFAULT 0 ;
GO