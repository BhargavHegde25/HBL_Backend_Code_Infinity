package com.kony.fabricreports.util;

public class ReportsConstants {
	private ReportsConstants() {

	}

	public static final String ACCOUNTS = "accounts";
	public static final String JASPERSESSION = "jasper_session";
	public static final String AWSELBSESSION = "awselb_session";
	public static final String ACCOUNTSGUID = "accountGuid";
	public static final String GET_LIST_OF_REPORTS = "getListOfReports";
	public static final String GET_FILTERS_FOR_REPORT = "getFiltersForReport";
	public static final String SHARED = "/shared_/";
	public static final String JASPERSESSIONFORPROCESS = "jasperSession";
	public static final String WHOAMI = "whoami";
	public static final String X_KONY_AUTH = "X-Kony-Authorization";
	public static final String ADHOC = "adhoc";
	public static final String USERGUID = "user_guid";
	public static final String VIEW_REPORT = "viewReport";
	public static final String DOWNLOAD_REPORT = "downloadReport";
	public static final String EXECUTIONS = "Executions";
	public static final String STRING = "String";
	public static final String HTML = "html";
	public static final String VALIDATIONERROR = "validationError";
	public static final int LOOPCOUNTVAL = 2;
	public static final String LOOPSEPARATORVAL = "###";
	public static final String PAGENUMVAL = "1" + "###" + "1";
	public static final Object METHODIDVAL = ReportsConstants.EXECUTIONS + "###" + ReportsConstants.VIEW_REPORT;
	public static final String VIEWREPORTORCHOPERATION = "viewFabricReportOrch";
	public static final String ORCHSERVICE = "FabricReportsOrch";
	public static final String VALUE = "value";

}
