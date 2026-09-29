package com.kony.dbputilities.transservices;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class RetrieveAttachments implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result attachmentsResult = new Result();
		Result attachments = new Result();
		Dataset ds = new Dataset();
		Map<String, String> dataMap = new HashMap<>();
		 Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String transactionId = inputParams.get("transactionId");
		
		if (isDMSIntegrationEnabled()) {
			dataMap.put("referenceId", transactionId);
			attachments = HelperMethods.callApi(dcRequest, dataMap, HelperMethods.getHeaders(dcRequest),
					URLConstants.DOCUMENT_STORAGE_SEARCH);
			if (attachments.getAllParams().size() > 0) {
				String fileInfoList = attachments.getAllParams().get(0).getValue();
				JSONArray files = null;
				if (StringUtils.isNotBlank(fileInfoList)) {
					files = new JSONArray(fileInfoList);
					for (int i = 0; i < files.length(); i++) {
						Record fileName = new Record();
						JSONObject obj = (JSONObject) files.get(i);
						fileName.addParam(new Param("fileName", obj.get("fileName").toString()));
						fileName.addParam(new Param("fileID",  obj.get("documentId").toString()));
						ds.addRecord(fileName);
					}
					ds.setId("fileNames");
					attachmentsResult.addDataset(ds);
				}
			}
		} else {
			dataMap.put(DBPUtilitiesConstants.FILTER, "transactionId " + DBPUtilitiesConstants.EQUAL + transactionId);
			attachments = HelperMethods.callApi(dcRequest, dataMap, HelperMethods.getHeaders(dcRequest),
					URLConstants.PAYMENT_FILES_GET);
			if (attachments.getAllDatasets().size() > 0) {
				List<Record> files = attachments.getAllDatasets().get(0).getAllRecords();
				for (Record file : files) {
					Record fileName = new Record();
					fileName.addParam(new Param("fileName", file.getParamValueByName("paymentFileName")));
					fileName.addParam(new Param("fileID", file.getParamValueByName("paymentFileID")));
					ds.addRecord(fileName);
				}
				ds.setId("fileNames");
				attachmentsResult.addDataset(ds);
			}
		}
		return attachmentsResult;
	}
	
	public boolean isDMSIntegrationEnabled() {
		ServicesManager serviceManager;
		try {
			serviceManager = ServicesManagerHelper.getServicesManager();
			ConfigurableParametersHelper configurableParametersHelper = serviceManager
					.getConfigurableParametersHelper();
			String isDMSIntegrationEnabled = configurableParametersHelper.getServerProperty("DMS_INTEGRATION_ENABLED");
			if(StringUtils.isBlank(isDMSIntegrationEnabled))
				return false;
			return BooleanUtils.toBoolean(configurableParametersHelper.getServerProperty("DMS_INTEGRATION_ENABLED"));
		} catch (Exception e) {
			alert.prepareError(e.getMessage()).log();
		}
		return true;
	}
}