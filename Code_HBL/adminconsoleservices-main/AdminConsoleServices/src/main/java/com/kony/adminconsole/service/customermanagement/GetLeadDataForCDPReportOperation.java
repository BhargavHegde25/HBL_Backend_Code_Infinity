package com.kony.adminconsole.service.customermanagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutor;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.core.config.EnvironmentConfiguration;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

public class GetLeadDataForCDPReportOperation implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {

		Result result = new Result();

		String leadIds = request.getParameter("leadIds");
		JSONArray applicationDetails = new JSONArray();
		applicationDetails = new JSONArray(leadIds);
		String appIds = null;
		List<Map<String, Object>> appDetailsList = new ArrayList<>();
		appIds = prepareCommaSeperatedids(applicationDetails);

		Map<String, Object> appIdsReqMap = new HashMap<String, Object>();
		appIdsReqMap.put("trackingCode", appIds);
		appIdsReqMap.put("entityDefinitionCode", EnvironmentConfiguration.LEAD_ENTITY_DEFINITION.getValue(request));
		appIdsReqMap.put("loop_count", Integer.toString(applicationDetails.length()));

		Map<String, Object> appInfo = getLeadDataForCDPReport(appIdsReqMap);
		JSONArray jsonArray = new JSONArray();

		if (appInfo != null) {
			appDetailsList = (List<Map<String, Object>>) appInfo.get("LoopDataset");
			for(int i=0; i<appDetailsList.size(); i++) {
				jsonArray.put(appDetailsList.get(i).get("entityItems"));
			}
		}
		
		result.addParam("leadDetails", jsonArray.toString());
		result =  CommonUtilities.withSuccessParams(result);
		return result;
	}
	//
	public Map<String, Object> getLeadDataForCDPReport(Map<String, Object> requestParameters)
			throws DBPApplicationException, Exception {
		Map<String, Object> cdpReportDataResp = null;
		try {
			DBPServiceExecutor serviceExecutor = DBPServiceExecutorBuilder.builder()
					.withServiceId("ODMSOrchService").withOperationId("getEntitiesByApplicationIds")
					.withRequestParameters(requestParameters).build();
			Result result = serviceExecutor.getResult();
			ObjectMapper objectMapper = new ObjectMapper();
			cdpReportDataResp = objectMapper.readValue(ResultToJSON.convert(result), Map.class);		

		} catch (JSONException | DBPApplicationException de) {
			alert.prepareError("Error in GetLeadDataForCDPReportOperation : getLeadDataForCDPReport" + de.getMessage()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_22219);
		}
		return cdpReportDataResp;

	}
	//
	public static String prepareCommaSeperatedids(JSONArray ids) {
		String modifiedIds = null;
		if (ids != null) {
			for(Object id : ids) {
				if (modifiedIds != null) {
					modifiedIds = modifiedIds + "," + id;
				} else {
					modifiedIds = (String) id;
				}
			}
		}
		return modifiedIds;
	}
}
