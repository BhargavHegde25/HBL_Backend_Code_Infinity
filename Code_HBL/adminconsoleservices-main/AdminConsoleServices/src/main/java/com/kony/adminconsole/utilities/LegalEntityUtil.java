package com.kony.adminconsole.utilities;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.adminconsole.exception.ApplicationException;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class LegalEntityUtil {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	private static Map<String, JSONObject> legalEntitiesMap = null;

	public static String getLegalEntityId() {
		return "GB0010001";
	}
	
	private static void getAllLegalEntities() throws ApplicationException{
		try {
			String legalEntityRes = DBPServiceExecutorBuilder.builder()
					.withServiceId("MultiEntity")
					.withObjectId(null)
					.withOperationId("getCompanyLegalUnits")
					.build().getResponse();
			JSONObject legalEntityResponseJSON = new JSONObject(legalEntityRes);
			if (legalEntityResponseJSON.has("opstatus") && legalEntityResponseJSON.getInt("opstatus") == 0) {
				if (legalEntityResponseJSON.has("companyLegalUnits")) {
					JSONArray legalEntitiesJSON = legalEntityResponseJSON.getJSONArray("companyLegalUnits");
					legalEntitiesMap = new HashMap<>();
					for (Object obj : legalEntitiesJSON) {
						JSONObject legalEntityJSON = (JSONObject) obj;
						String legalEntityId = legalEntityJSON.optString("id", null);
						if (legalEntityId == null) {
							continue;
						}
						legalEntitiesMap.put(legalEntityId, legalEntityJSON);
					}
				} else {
					throw new ApplicationException(ErrorCodeEnum.ERR_22171);
				}
			} else {
				throw new ApplicationException(ErrorCodeEnum.ERR_22171);
			}
		} catch (JSONException je) {
			alert.prepareError("Error while parsing Utility/operations/LegalEntity/getLegalEntities response: " + je.getMessage()).log();
		} catch ( DBPApplicationException dbpae ) {
			alert.prepareError("failed to get legalEntity details " + dbpae.getMessage()).log();
		}
	}
	
	public static String getCurrencyForLegalEntity( String legalEntityId ) throws ApplicationException {
		if(legalEntitiesMap == null) {
			getAllLegalEntities();
		}
		if(legalEntitiesMap == null) {
			return "";
		}
		if(legalEntitiesMap.containsKey(legalEntityId)) {
			JSONObject obj = legalEntitiesMap.get(legalEntityId);
			return obj.optString("baseCurrency", "");
		}
		return "";
	}
}
