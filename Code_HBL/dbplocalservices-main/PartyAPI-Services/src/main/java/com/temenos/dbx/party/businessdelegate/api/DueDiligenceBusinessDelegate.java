package com.temenos.dbx.party.businessdelegate.api;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.product.dto.DBXResult;

public interface DueDiligenceBusinessDelegate extends BusinessDelegate {

	DBXResult updateCitizenship(JSONArray citizenshiDetails, JSONArray residenceDetails, String partyID,
			Map<String, Object> map);

	DBXResult createTaxDetails(JSONObject taxDetails, String partyID, Map<String, Object> map);

	DBXResult createRegistrationDetails(JSONObject registrationDetails, String partyID, Map<String, Object> map, boolean isUpdate);

	DBXResult createAssetLiabiltyDetails(JSONObject assetLiabiltyDetails, String partyID,
			Map<String, Object> map, boolean isUpdate);

	DBXResult createFinancialInformationDetails(JSONObject financialInformation, String partyID,
			Map<String, Object> map, boolean isUpdate);

	DBXResult updateTaxDetails(JSONObject taxDetails, String partyID, Map<String, Object> map);

	DBXResult createEmploymentDetails(JSONObject employmentDetails, JSONObject addressDetails, String partyID,
			Map<String, Object> map);

	DBXResult updateEmploymentDetails(JSONObject employmentDetails, JSONObject addressDetails, String partyID,
			Map<String, Object> map);

	DBXResult getEmploymentDetails(String partyID, Map<String, Object> map);

	DBXResult getTaxDetails(String partyID, Map<String, Object> map);

	DBXResult getPartyDetails(String partyID, Map<String, Object> map);
	
	JSONArray getDueDiligenceData(String serviceURL, Map<String, Object> map, String key);

	DBXResult createFundsSource(JSONObject fundsSource, String partyID, Map<String, Object> map, boolean isUpdate);

}
