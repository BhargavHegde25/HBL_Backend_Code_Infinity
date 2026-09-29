package com.kony.adminconsole.service.customermanagement;

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

public class GetCustomerApplications implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

		@Override
		public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
				DataControllerResponse responseInstance) throws Exception {
			try {
				Result processedResult = new Result();
				Map<String, String> postParametersMap = new HashMap<String, String>();
				String customerId = requestInstance.getParameter("Customer_id");
				String applicationStatus = requestInstance.getParameter("Application_status");
				
				// Checking mandatory fields
				if (StringUtils.isBlank(customerId)) {
		            ErrorCodeEnum.ERR_20565.setErrorCode(processedResult);
		            return processedResult;	
	            }
				if (StringUtils.isBlank(applicationStatus)) {
		            ErrorCodeEnum.ERR_20148.setErrorCode(processedResult);
		            return processedResult;	
	            }
				if(applicationStatus.equals("started")) {
			        postParametersMap.put(ODataQueryConstants.FILTER, "Customer_id eq '" + customerId + 
			        		"' and ApplicationStatus eq 'InProgress'");
				}
				else {
			        postParametersMap.put(ODataQueryConstants.FILTER, "Customer_id eq '" + customerId + 
			        		"' and ApplicationStatus ne 'InProgress'");
				}
				String readResponse = Executor.invokeService(ServiceURLEnum.CUSTOMERAPPLICATION_READ,
		                postParametersMap, null, requestInstance);
		        JSONObject readResponseJSON = CommonUtilities.getStringAsJSONObject(readResponse);
		        JSONArray readResponseJSONArray = readResponseJSON.getJSONArray("customerapplication");
				
	            // -> Returning all applications w.r.t customerId  <-
	            Dataset dataset = new Dataset();
	            dataset.setId("customerapplications");

	            for (int i = 0; i < readResponseJSONArray.length(); i++) {
	                JSONObject currJSONObject = readResponseJSONArray.getJSONObject(i);
                    Record currRecord = new Record();
                    if (currJSONObject.length() != 0) {
                        for (String currKey : currJSONObject.keySet()) {
                            if (currJSONObject.has(currKey)) {
                                currRecord.addParam(
                                        new Param(currKey, currJSONObject.getString(currKey), FabricConstants.STRING));
                            }
                        }
                        dataset.addRecord(currRecord);
                    }
	            }

	            processedResult.addDataset(dataset);
				return processedResult;
			} catch (Exception e) {
				Result errorResult = new Result();
				diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
				ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
				return errorResult;
			}
		}

}
