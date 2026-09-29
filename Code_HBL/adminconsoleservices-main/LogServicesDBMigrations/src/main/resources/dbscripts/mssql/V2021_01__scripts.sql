USE [${logdbname}]
GO
ALTER TABLE [${logschemaname}].[adminactivity] ADD [eventData] [nvarchar](max) NULL
GO