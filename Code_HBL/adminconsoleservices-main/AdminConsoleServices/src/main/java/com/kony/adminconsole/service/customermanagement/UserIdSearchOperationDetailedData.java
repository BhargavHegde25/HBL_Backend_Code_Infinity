package com.kony.adminconsole.service.customermanagement;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.multientity.resource.api.MultiEntityResource;
import com.kony.adminconsole.utilities.CacheUtil;
import com.kony.adminconsole.utilities.DBPServices;
import com.kony.adminconsole.utilities.ErrorCodeEnum;

import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
public class UserIdSearchOperationDetailedData implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		Result processedResult = new Result();
		try {
			String authToken = requestInstance.getHeader(FabricConstants.X_KONY_AUTHORIZATION_HEADER);
			String userId = requestInstance.getParameter("userId");
			if (StringUtils.isBlank(userId)) {
				ErrorCodeEnum.ERR_22081.setErrorCode(processedResult);
				return processedResult;
			}
			JSONObject customers = null;
			customers = DBPServices.UserIdSearchOperationDetailedData(authToken, userId, requestInstance);
			if (customers == null || (customers != null && customers.has("records")
					&& customers.getJSONArray("records").length() == 0)) {
				ErrorCodeEnum.ERR_22212.setErrorCode(processedResult);
				return processedResult;
			}
			if (customers !=null && customers.has("records")) {
				JSONArray recordsArray = customers.getJSONArray("records");
				recordsArray = filterSearchedCustomerLegalEntities(requestInstance, recordsArray);
				if(recordsArray.length() == 0) {
					ErrorCodeEnum.ERR_22213.setErrorCode(processedResult);
					return processedResult;
				}
				Dataset recordsDataset = CommonUtilities.constructDatasetFromJSONArray(recordsArray);
				recordsDataset.setId("records");
				Param recordsStatus = new Param("Status", "Records returned: " + recordsArray.length(),
						FabricConstants.STRING);
				processedResult.addDataset(recordsDataset);
				processedResult.addParam(recordsStatus);
			} else {
//				ErrorCodeEnum.ERR_20716.setErrorCode(processedResult);
				Dataset recordsDataset = new Dataset();
				recordsDataset.setId("records");
				processedResult.addDataset(recordsDataset);
				if (!processedResult.hasParamByName("TotalResultsFound")) {
					processedResult.addParam(new Param("TotalResultsFound", "0", FabricConstants.INT));
				}
				if (!processedResult.hasParamByName("Status")) {
					processedResult.addParam(new Param("Status", "Records returned: 0", FabricConstants.STRING));
				}
				return processedResult;

			}
		} catch (Exception e) {
			// TODO: handle exception
			alert.prepareError("Unexpected error", e).log();
			processedResult.addParam(new Param("FailureReason", e.getMessage()));
			ErrorCodeEnum.ERR_22211.setErrorCode(processedResult);
			return processedResult;
		}
		return processedResult;
	}
	
	private JSONArray filterSearchedCustomerLegalEntities(DataControllerRequest requestInstance, JSONArray recordsArray) {

		Set<String> spotlightUserEntities = CacheUtil.getLoggedInUserLegalEntities(requestInstance);
		MultiEntityResource companyLegalUnitResource = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(ResourceFactory.class).getResource(MultiEntityResource.class);

		Result companyLegalUnits = companyLegalUnitResource.getAllCompanyLegalUnits(requestInstance);
		JsonElement legalEntitiesElement = new JsonParser().parse(ResultToJSON.convert(companyLegalUnits));
		JsonArray legalEntitiesJsonArray = legalEntitiesElement.getAsJsonObject().get("companyLegalUnits")
				.getAsJsonArray();
		Map<String, String> legalentityInfo = new HashMap<>();
		for (int i = 0; i < legalEntitiesJsonArray.size(); i++) {
			String legalentityid = legalEntitiesJsonArray.get(i).getAsJsonObject().get("id").getAsString();
			String companyName = legalEntitiesJsonArray.get(i).getAsJsonObject().get("companyName").getAsString();
			legalentityInfo.put(legalentityid, companyName);
		}
		JSONArray newrecordsArray = new JSONArray();
		if (recordsArray != null && !recordsArray.isEmpty()) {
			for (int i = 0; i < recordsArray.length(); i++) {

				JSONObject customerJson = recordsArray.getJSONObject(i);
				String legalEntity = customerJson.getString("branchId");
				if(!spotlightUserEntities.contains(legalEntity))
					continue;
				newrecordsArray.put(customerJson);
				
				if (customerJson.has("legalEntities")) {
					JSONArray legalEntitiesArray = customerJson.getJSONArray("legalEntities");

					JSONArray newLegalEntitiesArray = new JSONArray();
					if (legalEntitiesArray != null && !legalEntitiesArray.isEmpty()) {
						for (int p = 0; p < legalEntitiesArray.length(); p++) {
							JSONObject customerLegalEntityJsonObj = legalEntitiesArray.getJSONObject(p);
							String LE1 = customerLegalEntityJsonObj.getString("legalEntity");
							if (spotlightUserEntities.contains(LE1)) {
								customerLegalEntityJsonObj.put("description", legalentityInfo.get(LE1));
								newLegalEntitiesArray.put(customerLegalEntityJsonObj);
							}
						}

						customerJson.put("legalEntities", newLegalEntitiesArray);
					}
				}
			}

		}
		return newrecordsArray;
	}
}
