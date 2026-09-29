package com.kony.adminconsole.service.customermanagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.utilities.CacheUtil;
import com.kony.adminconsole.utilities.DBPServices;
import com.kony.adminconsole.utilities.ErrorCodeEnum;

import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class SearchCustomerByUserName implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		Result result = new Result();
		Dataset customerDataset = new Dataset();
		try {
			String authToken = requestInstance.getHeader(FabricConstants.X_KONY_AUTHORIZATION_HEADER);
			String userId = requestInstance.getParameter("userId");
			if (StringUtils.isBlank(userId)) {
				ErrorCodeEnum.ERR_22081.setErrorCode(result);
				return result;
			}
			JSONObject searchCustomersResponse = null;
			searchCustomersResponse = DBPServices.searchByUserId(authToken, userId, requestInstance);
			Record recordsDataset = new Record();
			Dataset recordsDataset1 = new Dataset();
			if (searchCustomersResponse == null || !searchCustomersResponse.has(FabricConstants.OPSTATUS)
					|| searchCustomersResponse.getInt(FabricConstants.OPSTATUS) != 0) {
				recordsDataset.setId("Customer");
				Param recordsStatus = new Param("Status", "Records returned: 0", FabricConstants.STRING);
				result.addParam(recordsStatus);
				return result;
			} else if (searchCustomersResponse.has("dbpErrMsg")) {
				result = CommonUtilities.constructResultFromJSONObject(searchCustomersResponse);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			} else {
				if (searchCustomersResponse.has("records")
						&& searchCustomersResponse.getJSONArray("records").length() > 0) {
					JSONArray customerArray = searchCustomersResponse.getJSONArray("records");
					JSONObject customer = customerArray.getJSONObject(0);
					JSONArray legalEntities = new JSONArray();
					Set<String> spotlightUserEntities = CacheUtil.getLoggedInUserLegalEntities(requestInstance);
					List<String> customerLegalEntities = new ArrayList<String>();
					for (int i = 0; i < customerArray.length(); i++) {
						JSONObject customerJson = customerArray.getJSONObject(i);
						JSONObject legalEntitiesJson = new JSONObject();
						String legalEntityId = customerJson.getString("legalEntityId");
						legalEntitiesJson.put("legalEntityId", legalEntityId);
						if (legalEntityId.equals(customerJson.getString("homeLegalEntity"))) {
							legalEntitiesJson.put("isHomeLegalEntity", "true");
						} else {
							legalEntitiesJson.put("isHomeLegalEntity", "false");
						}
						legalEntities.put(legalEntitiesJson);
						customerLegalEntities.add(legalEntityId);

					}
					List<String> fianlLE = spotlightUserEntities.stream().distinct().filter(customerLegalEntities::contains)
							.collect(Collectors.toList());

					if (fianlLE.size() == 0) {
						ErrorCodeEnum.ERR_22233.setErrorCode(result);
						return result;
					}
                   
					recordsDataset = CommonUtilities.constructRecordFromJSONObject(customer);
					recordsDataset.removeParamsByName("legalEntityId");
					recordsDataset1 = CommonUtilities.constructDatasetFromJSONArray(legalEntities);
					recordsDataset.addDataset(recordsDataset1);
					recordsDataset1.setId("legalEntities");
					customerDataset.addRecord(recordsDataset);
					customerDataset.setId("records");
					result.addDataset(customerDataset);

				} else {
					recordsDataset.setId("Customer");
					Param recordsStatus = new Param("Status", "Records returned: 0", FabricConstants.STRING);
					result.addParam(recordsStatus);
					return result;
				}
			}

		} catch (Exception e) {
			alert.prepareError("Caught exception at invoke of customerSearchByUsername: ", e).log();
			return ErrorCodeEnum.ERR_20512.setErrorCode(new Result());
		}
		return result;
	}
}
