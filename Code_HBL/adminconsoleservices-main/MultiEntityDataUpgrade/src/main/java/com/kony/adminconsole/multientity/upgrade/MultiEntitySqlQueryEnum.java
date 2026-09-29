package com.kony.adminconsole.multientity.upgrade;

public enum MultiEntitySqlQueryEnum {
	
	MYSQL_UPGREADEPROCEDURE("call dbxdb.dataUpgradeForMultiEntityOLB(?,?)"),
	MYSQL_UPGREADEALERTSPROCEDURE("call dbxdb.dataUpgradeForMultiEntityAlerts(?)"),
	MYSQL_RECONCILEPROCEDRUE("call dbxdb.MultiEntityReconciliationProc()"),
	MYSQL_ALERTSDATAUPGRADERECONCILEPROCEDRUE("call dbxdb.alertsDataUpgradeReconcile()"),
	MYSQL_CUSTOMERDATAUPGRADEPROCEDURE("call dbxdb.customerDataUpgradeForMultiEntity()"),
	
	MSSQL_UPGREADEPROCEDURE("exec [dbxdb].[dataUpgradeForMultiEntityOLB] ?, ? "),
	MSSQL_UPGREADEALERTSPROCEDURE("exec [dbxdb].[dataUpgradeForMultiEntityAlerts] ? "),
	MSSQL_RECONCILEPROCEDRUE("exec [dbxdb].[MultiEntityReconciliationProc]"),
	MSSQL_ALERTSDATAUPGRADERECONCILEPROCEDRUE(" exec [dbxdb].[alertsDataUpgradeReconcile]"),
	MSSQL_CUSTOMERDATAUPGRADEPROCEDURE("exec [dbxdb].[customerDataUpgradeForMultiEntity]"),
	
	MYSQL_DELETETABLE("DROP TABLE `dbxdb`.`tablelist`"),
	MYSQL_DELETEALERTTABLE("DROP TABLE `dbxdb`.`alerttablelist`"),
	MYSQL_DELETEUPGRADESP("DROP PROCEDURE `dbxdb`.`dataUpgradeForMultiEntityOLB`"),
	MYSQL_DELETERECONCILESP("DROP PROCEDURE `dbxdb`.`MultiEntityReconciliationProc`"),
	MYSQL_DELETECUSTOMERUPGRADESP("DROP PROCEDURE `dbxdb`.`customerDataUpgradeForMultiEntity`"),
	MYSQL_DELETECUSTOMERUPGRADERECONCILESP("DROP PROCEDURE `dbxdb`.`customerUpgradeReconcileProc`"),
	MYSQL_DELETECREATESP("DROP PROCEDURE `dbxdb`.`MultiEntityMasterDataCreateProc`"),
	MYSQL_DELETEALERTSDATAUPGRADESP("DROP PROCEDURE `dbxdb`.`dataUpgradeForMultiEntityAlerts`"),
	MYSQL_DELETEALERTSDATAUPGRADERECONCILESP("DROP PROCEDURE `dbxdb`.`alertsDataUpgradeReconcile`"),
	MYSQL_DELETE202304MASTERDATACREATESP("DROP PROCEDURE `dbxdb`.`ME202304MasterDataCreateProc`"),
	MYSQL_DELETE202306MULTIENTITYDATAUPGRADE("DROP PROCEDURE `dbxdb`.`MultiEntityDataUpgrade202306`"),
	MYSQL_DELETEUPDATEACTIONLEVELOFOLDFEATUREACTIONS202306("DROP PROCEDURE `dbxdb`.`UpdateActionLevelOfOldFeatureActions202306`"),
		
	MSSQL_DELETETABLE("DROP TABLE [dbxdb].[tablelist]"),
	MSSQL_DELETEALERTTABLE("DROP TABLE [dbxdb].[alerttablelist]"),
	MSSQL_DELETEUPGRADESP("DROP PROCEDURE [dbxdb].[dataUpgradeForMultiEntityOLB]"),
	MSSQL_DELETERECONCILESP("DROP PROCEDURE [dbxdb].[MultiEntityReconciliationProc]"),
	MSSQL_DELETECUSTOMERUPGRADESP("DROP PROCEDURE [dbxdb].[customerDataUpgradeForMultiEntity]"),
	MSSQL_DELETECUSTOMERUPGRADERECONCILESP("DROP PROCEDURE [dbxdb].[customerUpgradeReconcileProc]"),
	MSSQL_DELETECREATESP("DROP PROCEDURE [dbxdb].[MultiEntityMasterDataCreateProc]"),
	MSSQL_DELETEALERTSDATAUPGRADESP("DROP PROCEDURE [dbxdb].[dataUpgradeForMultiEntityAlerts]"),
	MSSQL_DELETEALERTSDATAUPGRADERECONCILESP("DROP PROCEDURE [dbxdb].[alertsDataUpgradeReconcile]"),
	MSSQL_DELETE202304MASTERDATACREATESP("DROP PROCEDURE [dbxdb].[ME202304MasterDataCreateProc]"),
	MSSQL_DELETE202306MULTIENTITYDATAUPGRADE("DROP PROCEDURE [dbxdb].[MultiEntityDataUpgrade202306]"),
	MSSQL_DELETEUPDATEACTIONLEVELOFOLDFEATUREACTIONS202306("DROP PROCEDURE [dbxdb].[UpdateActionLevelOfOldFeatureActions202306]"),
	
	
	MYSQL_SCRIPTSFILEPATH("dbscripts/mysql/ddl_admin.sql"),
	MSSQL_SCRIPTSFILEPATH("dbscripts/mssql/ddl_admin.sql"),
	
	MYSQL_SCRIPTS_V2304_FILEPATH("dbscripts/mysql/ddl_admin_v202304.sql"),
	MSSQL_SCRIPTS_V2304_FILEPATH("dbscripts/mssql/ddl_admin_v202304.sql"),

	MYSQL_SCRIPTS_V2306_FILEPATH("dbscripts/mysql/ddl_admin_v202306.sql"),
	MSSQL_SCRIPTS_V2306_FILEPATH("dbscripts/mssql/ddl_admin_v202306.sql"),
	
	MYSQL_CUSTOMERDATAUPGRADESP("call dbxdb.customerDataUpgradeForMultiEntity()"),
	MYSQL_CUSTOMERDATAUPGRADERECONCILESP("call dbxdb.customerUpgradeReconcileProc()"),

	MSSQL_CUSTOMERDATAUPGRADESP("exec [dbxdb].[customerDataUpgradeForMultiEntity]"),	
	MSSQL_CUSTOMERDATAUPGRADERECONCILESP("exec [dbxdb].[customerUpgradeReconcileProc]"),
	
	MYSQL_DATAINSERTPROCEDURE("call dbxdb.MultiEntityMasterDataCreateProc(?)"),
	MYSQL_202304MASTERDATAINSERTPROCEDURE("call dbxdb.ME202304MasterDataCreateProc(?)"),
	MYSQL_FEATURE_ACTION_UPDATE_PROCEDURE("call dbxdb.UpdateActionLevelOfOldFeatureActions202306()"),
	MYSQL_INSERT_FEATUREACTIONDATA_V2306_PROCEDURE("call dbxdb.MultiEntityDataUpgrade202306(?)"),
	MYSQL_FINANCIALINSTITUTE_DATAINSERT_PROC("call dbxdb.MultiEntityFinancialInstitueDataCreateProc(?,?,?,?,?,?,?,?,?,?,?,?,?)"),
	
	MSSQL_DATAINSERTPROCEDURE("exec [dbxdb].[MultiEntityMasterDataCreateProc] ? "),
	MSSQL_202304MASTERDATAINSERTPROCEDURE("exec [dbxdb].[ME202304MasterDataCreateProc] ? "),
	MSSQL_INSERT_FEATUREACTIONDATA_V2306_PROCEDURE("exec [dbxdb].[MultiEntityDataUpgrade202306] ?"),
	MSSQL_FEATURE_ACTION_UPDATE_PROCEDURE("exec [dbxdb].[UpdateActionLevelOfOldFeatureActions202306]"),
	MYSQL_FLYWAYSCHEMAVERSION("select version from dbxdb.flyway_schema_history order by installed_rank desc LIMIT 1"),
	MSSQL_FLYWAYSCHEMAVERSION("select TOP 1 version from dbxdb.flyway_schema_history order by installed_rank desc"),
	MSSQL_FINANCIALINSTITUTE_DATAINSERT_PROC("exec [dbxdb].[MultiEntityFinancialInstitueDataCreateProc] ?,?,?,?,?,?,?,?,?,?,?,?,? "),
	
	MYSQL_SINGLE_ENTITY_GET("select isSingleEntity from dbxdb.application"),
	MSSQL_SINGLE_ENTITY_GET("select isSingleEntity from dbxdb.application"),
	
	MYSQL_FINANCIAL_KEYS_GET("select distinct alternateKey from dbxdb.financialinstitutionaltkey"),
	MSSQL_FINANCIAL_KEYS_GET("select distinct alternateKey from dbxdb.financialinstitutionaltkey"),
	;

	
	String query;
	MultiEntitySqlQueryEnum(String query) {
		this.query=query;
	}
	public String getQuery() {
		return this.query;
	}
}
