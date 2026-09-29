package com.kony.adminconsole.service.reports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * Service to Fetch the Report Definition
 * 
 * @author Rishi Gupta
 */
public class ReportDefinitionGetService implements JavaService2 {

	private static final String REPORT_ID_PARAM = "ReportId";


	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) {

		Result processedResult = new Result();

		try {
			// Read Inputs
			String reportId = requestInstance.getParameter(REPORT_ID_PARAM);
			diagnostic.prepareDebug("Received Report ID:" + reportId).log();
			
			// Validate Inputs
			if (StringUtils.isBlank(reportId)) {
				// Missing Report ID
				alert.prepareError("Missing Report ID").log();
				ErrorCodeEnum.ERR_20920.setErrorCode(processedResult);
				return processedResult;
			}

			// Fetch Logged In User Info
			String loggedInUser = StringUtils.EMPTY;
			UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(requestInstance);
			if (userDetailsBeanInstance != null) {
				loggedInUser = userDetailsBeanInstance.getId();
			}

			// Get Report Definition
			Record reportDefintionRecord = getReportDefintion(reportId, requestInstance);
			processedResult.addRecord(reportDefintionRecord);

			//Get Shared Users and roles
			processedResult = getSharedUsersAndRolesOfReport(reportId, requestInstance, processedResult);
			
		} catch (ApplicationException e) {
			Result errorResult = new Result();
			alert.prepareError("Application Exception. Checked Involved Operations. Exception Trace:", e).log();
			e.getErrorCodeEnum().setErrorCode(errorResult);
			return errorResult;
		} catch (Exception e) {
			Result errorResult = new Result();
			alert.prepareError("Exception in Fetching Customer Report Channel Preference. Exception:", e).log();
			ErrorCodeEnum.ERR_20914.setErrorCode(errorResult);
			return errorResult;
		}
		return processedResult;
	}

	/**
	 * Method to get basic attributes of Alert category as a Record
	 * 
	 * @param reportId
	 * @param acceptLanguage
	 * @param requestInstance
	 * @return Record containing Report Information
	 * @throws ApplicationException
	 */
	private Record getReportDefintion(String reportId, DataControllerRequest requestInstance) throws ApplicationException {

		if (requestInstance == null) {
			// Data Controller Request Instance is NULL. Throw Application Exception.
			alert.prepareError("Data Controller Request Instance is NULL. Throwing Application Exception.").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20927);
		}

		Record operationRecord = new Record();
		operationRecord.setId("reportDefinition");

		if (StringUtils.isBlank(reportId)) {
			// Missing Mandatory Input. Return Empty Record
			alert.prepareError("Report Id value is empty. Returning empty record.").log();
			return operationRecord;
		}

		
		// Construct Filter Query
		String filterQuery = "(reportId eq '" + reportId + "')";
		// Construct Input Map
		Map<String, String> paramaterMap = new HashMap<>();
		paramaterMap.put(ODataQueryConstants.FILTER, filterQuery);
		
		// Read Report 
		String readReportResponse = Executor.invokeService(ServiceURLEnum.REPORT_READ, paramaterMap, null, requestInstance);
		JSONObject readReportResponseJSON = CommonUtilities.getStringAsJSONObject(readReportResponse);
		if (readReportResponseJSON == null || !readReportResponseJSON.has(FabricConstants.OPSTATUS)
				|| readReportResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
				|| !readReportResponseJSON.has("report")) {
			// Failed CRUD Operation
			alert.prepareError("Failed to Read Report View").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20915);
		}

		diagnostic.prepareDebug("Read Report View").log();
		// Parse Response and construct result Record
		JSONArray reportsJSONArray = readReportResponseJSON.optJSONArray("report");
		
		// Construct Response Record
		diagnostic.prepareDebug("Constructing Response Record").log();
		if (reportsJSONArray != null && reportsJSONArray.length() > 0) {
			JSONObject currJSON;
			if (reportsJSONArray.optJSONObject(0) != null) {
				currJSON = reportsJSONArray.optJSONObject(0);
				operationRecord.addParam(new Param("reportId", currJSON.optString("id"), FabricConstants.STRING));
				operationRecord.addParam(new Param("reportSource", currJSON.optString("reportDataSourceId"), FabricConstants.STRING));
				operationRecord.addParam(new Param("reportName", currJSON.optString("name"), FabricConstants.STRING));
			}
		}

		diagnostic.prepareDebug("Returning success response").log();
		return operationRecord;
	}

	
	
	/**
	 * Method to fetch the Shared Users of Report
	 * 
	 * @param reportId
	 * @param requestInstance
	 * @param processedResult 
	 * @return Dataset containing Users
	 * @throws ApplicationException
	 */
	private Result getSharedUsersAndRolesOfReport(String reportId, DataControllerRequest requestInstance, Result processedResult) throws ApplicationException {		
		Dataset usersDataset = new Dataset();
		Dataset rolesDataset = new Dataset();
		rolesDataset.setId("roles");		
		usersDataset.setId("users");

		if (requestInstance == null) {
			// Data Controller Request Instance is NULL. Throw Application Exception.
			alert.prepareError("Data Controller Request Instance is NULL. Throwing Application Exception.").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20927);
		}
		if (StringUtils.isBlank(reportId)) {
			// Missing Mandatory Input. Return Empty Record
			alert.prepareError("Report Id value is empty. Returning empty record.").log();
			processedResult.addDataset(usersDataset);
			return processedResult;
		}
		diagnostic.prepareDebug("Fetching Shared Report users. Report Id:" + reportId).log();
		
		// Fetch Reports - Users and roles association
		Map<String, String> inputMap = new HashMap<>();
		inputMap.put(ODataQueryConstants.FILTER, "reportId eq '" + reportId + "'");
		String readSharedReportResponse = Executor.invokeService(ServiceURLEnum.SHAREDREPORT_READ, inputMap, null, requestInstance);
		JSONObject readSharedReportResponseJSON = CommonUtilities.getStringAsJSONObject(readSharedReportResponse);
		if (readSharedReportResponseJSON == null || !readSharedReportResponseJSON.has(FabricConstants.OPSTATUS)
				|| readSharedReportResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
				|| !readSharedReportResponseJSON.has("sharedreport")) {
			alert.prepareError("Failed to Read sharedreport").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20929);
		}
		JSONArray usersAndRolesArray = readSharedReportResponseJSON.optJSONArray("sharedreport");
		// Fetch the Users the Report is shared with
		diagnostic.prepareDebug("Fetched Shared Report users. Report Id:" + reportId).log();

		ArrayList<String> userList = new ArrayList<String>();
		ArrayList<String> roleList = new ArrayList<String>();
		if (usersAndRolesArray != null && usersAndRolesArray.length() > 0) {
			JSONObject currJSON;
			for (Object currObject : usersAndRolesArray) {
				if (currObject instanceof JSONObject) {
					Record currUserOrRoleRecord = new Record();
					currJSON = (JSONObject) currObject;
					if(!currJSON.optString("userId").isEmpty())
					{
						currUserOrRoleRecord.addParam(new Param("id", currJSON.optString("userId"), FabricConstants.STRING));
						userList.add(currJSON.optString("userId"));					
						usersDataset.addRecord(currUserOrRoleRecord);
					}else{
						currUserOrRoleRecord.addParam(new Param("id", currJSON.optString("roleId"), FabricConstants.STRING));
						roleList.add(currJSON.optString("roleId"));					
						rolesDataset.addRecord(currUserOrRoleRecord);
					}
				}
			}
			processedResult.addDataset(usersDataset);
			processedResult.addDataset(rolesDataset);
		}
		return processedResult;
	}

		
}

