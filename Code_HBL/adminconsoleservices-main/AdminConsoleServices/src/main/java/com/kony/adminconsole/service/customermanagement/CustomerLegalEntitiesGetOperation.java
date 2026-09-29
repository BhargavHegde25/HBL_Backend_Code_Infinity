package com.kony.adminconsole.service.customermanagement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.MemoryManager;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.multientity.resource.api.MultiEntityResource;

import com.kony.adminconsole.utilities.DBPServices;
import com.kony.adminconsole.utilities.ErrorCodeEnum;

import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

public class CustomerLegalEntitiesGetOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		Result result = new Result();
		Dataset customerDataset = new Dataset();
		try {
			String authToken = requestInstance.getHeader(FabricConstants.X_KONY_AUTHORIZATION_HEADER);
			String customerid = requestInstance.getParameter("customerId");
			if (StringUtils.isBlank(customerid)) {
				ErrorCodeEnum.ERR_22203.setErrorCode(result);
				return result;
			}
			JSONObject searchCustomersResponse = null;
			searchCustomersResponse = DBPServices.customerLegalEntitiesGet(authToken, customerid, requestInstance);
			Record recordsDataset = new Record();
			if (searchCustomersResponse == null || !searchCustomersResponse.has(FabricConstants.OPSTATUS)
					|| searchCustomersResponse.getInt(FabricConstants.OPSTATUS) != 0) {
				return result;
			} else if (searchCustomersResponse.has("dbpErrMsg")) {
				result = CommonUtilities.constructResultFromJSONObject(searchCustomersResponse);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			} else {

				filterSearchedCustomerLegalEntities(requestInstance, searchCustomersResponse);
				recordsDataset = CommonUtilities.constructRecordFromJSONObject(searchCustomersResponse);
				customerDataset.addRecord(recordsDataset);
				customerDataset.setId("records");
				result.addDataset(customerDataset);

			}

		} catch (Exception e) {
			alert.prepareError("Caught exception at invoke of customerSearchByUsername: ", e).log();
			return ErrorCodeEnum.ERR_22203.setErrorCode(new Result());
		}
		return result;
	}

	private Set<String> getLoggedInUserLegalEntities(DataControllerRequest requestInstance) {
		Set<String> spotlightUserEntities = new HashSet<>();
		try {
			//String session_id = requestInstance.getSession().getId().toString();
			
			String userid  = null;
			if (requestInstance.getServicesManager() != null && requestInstance.getServicesManager().getIdentityHandler() != null) {
				userid = LoggedInUserHandler.getUserDetails(requestInstance).getId();
            }
			
			String legalEntityData = (String) MemoryManager.getFromCache("legalEntityToRoleMapping_" + userid);
			
			if (StringUtils.isNotBlank(legalEntityData)) {
				JsonElement legalEntityDataJsonEle = new JsonParser().parse(legalEntityData);
				if (legalEntityDataJsonEle != null && legalEntityDataJsonEle.isJsonObject()) {
					JsonObject legalEntityDataJsonObj = legalEntityDataJsonEle.getAsJsonObject();
					Set<Map.Entry<String, JsonElement>> elements = legalEntityDataJsonObj.entrySet();
					for (Map.Entry<String, JsonElement> legalEntity : elements) {
						spotlightUserEntities.add(legalEntity.getKey());
					}
				}
			}
		}

		catch (Exception e) {
			alert.prepareError("Error in getting legal entity from cache", e).log();
		}
		return spotlightUserEntities;
	}

	private void filterSearchedCustomerLegalEntities(DataControllerRequest requestInstance,
			JSONObject customerLegalEntities) {

		Set<String> spotlightUserEntities = getLoggedInUserLegalEntities(requestInstance);
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
		if (customerLegalEntities != null) {

			if (customerLegalEntities.has("legalEntities")) {
				JSONArray legalEntitiesArray = customerLegalEntities.getJSONArray("legalEntities");

				JSONArray newLegalEntitiesArray = new JSONArray();
				if (legalEntitiesArray != null && !legalEntitiesArray.isEmpty()) {
					for (int p = 0; p < legalEntitiesArray.length(); p++) {
						JSONObject customerLegalEntityJsonObj = legalEntitiesArray.getJSONObject(p);
						String LE1 = customerLegalEntityJsonObj.getString("legalEntity");
						/* Removing to unblock UI Teams */
						// if (spotlightUserEntities.contains(LE1)) {
						customerLegalEntityJsonObj.put("description", legalentityInfo.get(LE1));
						newLegalEntitiesArray.put(customerLegalEntityJsonObj);

						// }
					}

					customerLegalEntities.put("legalEntities", newLegalEntitiesArray);
				}
			}

		}
	}

}
