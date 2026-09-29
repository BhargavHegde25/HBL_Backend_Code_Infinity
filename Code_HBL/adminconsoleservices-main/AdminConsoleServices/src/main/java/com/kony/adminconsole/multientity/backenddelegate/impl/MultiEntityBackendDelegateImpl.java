package com.kony.adminconsole.multientity.backenddelegate.impl;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.MemoryManager;
import com.kony.adminconsole.multientity.backenddelegate.api.MultiEntityBackendDelegate;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;

import java.util.HashMap;
import java.util.Map;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

public class MultiEntityBackendDelegateImpl implements MultiEntityBackendDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	
	@Override
	public JSONObject getAllCompanyLegalUnits(String authToken, boolean singleentity, Object sessionId) throws DBPApplicationException {
		Map<String, Object> headerMap = new HashMap<>();
		Map<String, Object> postParametersMap = new HashMap<>();
		headerMap.put("backendToken", authToken);
		Object serviceRespcache = MemoryManager.getFromCache("companyLegalUnits"+ sessionId.toString());
		JSONObject result = new JSONObject();
		String groupsResponse = null;
		try {
			if (!singleentity) {
				if (serviceRespcache == "" || serviceRespcache == null) {
					String ordmsResponse = DBPServiceExecutorBuilder.builder().
											withServiceId(ServiceId.MULTI_ENTITY_MS).
											withOperationId(OperationName.OP_GET_COMPANY_LEGAL_UNITS).
											withRequestHeaders(headerMap).
											withRequestParameters(postParametersMap).
											build().
											getResponse();
					JSONObject serviceResponse = CommonUtilities.getStringAsJSONObject(ordmsResponse);
					if (serviceResponse != null && serviceResponse.has(FabricConstants.OPSTATUS)
							&& serviceResponse.getInt(FabricConstants.OPSTATUS) == 0) {
						JSONArray financialInstitutions = serviceResponse.getJSONArray("financialInstitutions");
						alert.prepareError("financialInstitutions" + financialInstitutions).log();
						for (int i = 0; i < financialInstitutions.length(); i++) {
								Object finobj = financialInstitutions.get(i);
								Object alternateId = ((JSONObject)finobj).getJSONObject("alternateReferences")
								.get("alternateId");
								((JSONObject)finobj).put("id", alternateId);
						}
						result.put("companyLegalUnits", financialInstitutions);
						MemoryManager.saveIntoCache("companyLegalUnits"+ sessionId.toString(), financialInstitutions.toString());
					}
				} else
				{
					result.put("companyLegalUnits", CommonUtilities.getStringAsJSONArray(serviceRespcache.toString()));
					alert.prepareError("Result" + result).log();
				}	
				return result;
			} else {
				return getAllCompanyLegalUnitsFromDB(true);
			}
		} catch (DBPApplicationException e) {
			alert.prepareError("Failed to get list of company legal units: ", e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Failed to get list of company legal units: ", e).log();
			return null;
		}
	} 
	
	@Override
	public JSONObject getAllCompanyLegalUnitsFromDB(boolean singleentity) throws DBPApplicationException {
		String groupsResponse = null;
		JSONObject result = new JSONObject();
		try {
			groupsResponse = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
	                		.withOperationId(OperationName.DB_FINANCIALINSTITUTION_GET_PROC)
	                		.withRequestParameters(null).build().getResponse();
			JSONObject ResponseJSON = CommonUtilities.getStringAsJSONObject(groupsResponse);
			if (ResponseJSON != null && ResponseJSON.has(FabricConstants.OPSTATUS)
	                     && ResponseJSON.getInt(FabricConstants.OPSTATUS) == 0 && ResponseJSON.has("records")) {
				JSONArray configurationsArray = (JSONArray) ResponseJSON.get("records");
				result.put("companyLegalUnits", configurationsArray);
				return result;
			}
			alert.prepareWarn("financialinstitution dataset is null. Failed to read database.");
			return null;
		} catch (Exception e) {
			alert.prepareError("Failed to get list of company legal units: ", e).log();
			return null;
		}
	}
}