package com.temenos.dbx.party.businessdelegate.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.temenos.dbx.party.businessdelegate.api.DueDiligenceBusinessDelegate;
import com.temenos.dbx.party.utils.PartyPropertyConstants;
import com.temenos.dbx.party.utils.PartyURLFinder;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.utils.DTOConstants;
import com.temenos.dbx.product.utils.HTTPOperations;

public class DueDiligenceBusinessDelegateImpl implements DueDiligenceBusinessDelegate {

	/**
	 * A Logger object is used to log messages for a specific system or application
	 * component
	 */
	private LoggerUtil logger = new LoggerUtil(DueDiligenceBusinessDelegateImpl.class);
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	/**
	 * @param citizenshiDetails, residenceDetails : citizenshiDetails object.
	 * @param partyID:           Party ID.
	 * @param map                : Headers which have classname, prepostprocessor
	 *                           names and session ID, etc.
	 * @return :DBXResult object.
	 * @throws Exception
	 */
	public DBXResult updateCitizenship(JSONArray citizenshiDetails, JSONArray residenceDetails, String partyID,
			Map<String, Object> map) {
		DBXResult dbxResult = null;
		try {
			String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL)
					+ PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_GET, partyID);

			DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, partyURL, null, map);
			JSONObject getParty = null;
			try {
				getParty = new JSONObject(response.getResponse().toString());
				getParty.put(DTOConstants.CITIZENSHIP, citizenshiDetails);
				getParty.put(DTOConstants.RESIDENCE, residenceDetails);
			} catch (Exception e) {
				alert.prepareError("Caught exception while getting Party: ", e).log();
			}
			String party = getParty.toString();
			diagnostic.prepareDebug("getpartyDTO for update Party Service is : " + party).log();

			partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL)
					+ PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_UPDATE, partyID);

			response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.PUT, partyURL, party, map);

			dbxResult = getResultFromResponse(response);
		} catch (Exception e) {
			alert.prepareError("Error in UpdateDueDiligenceBusinessDelegateImpl-updateCitizenship : " + e.toString()).log();
		}
		return dbxResult;
	}

	@Override
	/**
	 * @param partyID : PartyID for which Due Diligences details need to be fetched.
	 * @param map     : Headers which have classname, prepostprocessor names and
	 *                session ID, etc.
	 * @return :DBXResult object.
	 * @throws Exception
	 */
	public DBXResult getPartyDetails(String partyID, Map<String, Object> map) {
		DBXResult dbxResult = new DBXResult();
		try {
			String employmentDetailsURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL)
					+ PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_GET, partyID);

			dbxResult = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, employmentDetailsURL, null, map);

			if (dbxResult.getResponse() != null) {
				JsonElement element = new JsonParser().parse((String) dbxResult.getResponse());
				JsonObject dueDiligenceObject = new JsonObject();
				if (element.isJsonObject()) {
					JsonObject responseObj = element.getAsJsonObject();
					if (responseObj.has("citizenships"))
						dueDiligenceObject.add("citizenships", responseObj.get("citizenships"));
					if (responseObj.has("taxDetails"))
						dueDiligenceObject.add("taxDetails", responseObj.get("taxDetails"));
					if (responseObj.has("residences"))
						dueDiligenceObject.add("residences", responseObj.get("residences"));
					if (responseObj.has("occupations"))
						dueDiligenceObject.add("occupations", responseObj.get("occupations"));
					if (responseObj.has("employments"))
						dueDiligenceObject.add("employments", responseObj.get("employments"));
					if (responseObj.has("addresses"))
						dueDiligenceObject.add("addresses", responseObj.get("addresses"));
					if (responseObj.has("partyId"))
						dueDiligenceObject.add("partyID", responseObj.get("partyId"));
				}
				if (element.isJsonObject()) {
					dbxResult.setResponse(dueDiligenceObject.getAsJsonObject().toString());
				} else if (element.isJsonArray()) {
					dbxResult.setResponse(element.getAsJsonArray().get(0).getAsJsonObject().toString());
				}
			}
		} catch (Exception e) {
			alert.prepareError("Error in UpdateDueDiligenceBusinessDelegateImpl-getPartyDetails : " + e.toString()).log();
		}
		return dbxResult;
	}

	@Override
	/**
	 * @param taxDetails : taxDetails Object.
	 * @param partyID    : PartyID for which taxDetails need to be updated
	 * @param map        : Headers which have classname, prepostprocessor names and
	 *                   session ID, etc.
	 * @return :DBXResult object.
	 * @throws Exception
	 */
	public DBXResult createTaxDetails(JSONObject taxDetails, String partyID, Map<String, Object> map) {
		DBXResult dbxResult = null;
		try {
			String taxDetailsURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL)
					+ PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_TAXDETAILS, partyID);

			DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.POST, taxDetailsURL,
					taxDetails.toString(), map);

			dbxResult = getResultFromResponse(response);
		} catch (Exception e) {
			alert.prepareError("Error in UpdateDueDiligenceBusinessDelegateImpl-createTaxDetails : " + e.toString()).log();
		}
		return dbxResult;
	}

	@Override
	/**
	 * @param taxDetails : taxDetails Object.
	 * @param partyID    : PartyID for which taxDetails need to be updated
	 * @param map        : Headers which have classname, prepostprocessor names and
	 *                   session ID, etc.
	 * @return :DBXResult object.
	 * @throws Exception
	 */
	public DBXResult updateTaxDetails(JSONObject taxDetails, String partyID, Map<String, Object> map) {
		DBXResult dbxResult = null;
		try {
			String taxDetailsURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL)
					+ PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_TAXDETAILS, partyID);

			DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.PUT, taxDetailsURL,
					taxDetails.toString(), map);

			dbxResult = getResultFromResponse(response);
		} catch (Exception e) {
			alert.prepareError("Error in UpdateDueDiligenceBusinessDelegateImpl-updateTaxDetails : " + e.toString()).log();
		}
		return dbxResult;
	}

	@Override
	/**
	 * @param taxDetails : taxDetails Object.
	 * @param partyID    : PartyID for which taxDetails need to be fetched.
	 * @param map        : Headers which have classname, prepostprocessor names and
	 *                   session ID, etc.
	 * @return :DBXResult object.
	 * @throws Exception
	 */
	public DBXResult getTaxDetails(String partyID, Map<String, Object> map) {
		DBXResult dbxResult = new DBXResult();
		try {
			String taxDetailsURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL)
					+ PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_TAXDETAILS, partyID);

			dbxResult = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, taxDetailsURL, null, map);

			if (dbxResult.getResponse() != null) {
				JsonElement element = new JsonParser().parse((String) dbxResult.getResponse());
				if (element.isJsonObject()) {
					dbxResult.setResponse(element.getAsJsonObject().toString());
				} else if (element.isJsonArray()) {
					dbxResult.setResponse(element.getAsJsonArray().get(0).getAsJsonObject().toString());
				}
			}
		} catch (Exception e) {
			alert.prepareError("Error in UpdateDueDiligenceBusinessDelegateImpl-getTaxDetails : " + e.toString()).log();
		}
		return dbxResult;
	}

	@Override
	/**
	 * @param employmentDetails : employmentDetails Object.
	 * @param partyID           : PartyID for which employmentDetails need to be
	 *                          created.
	 * @param map               : Headers which have classname, prepostprocessor
	 *                          names and session ID, etc.
	 * @return :DBXResult object.
	 * @throws Exception
	 */
	public DBXResult createEmploymentDetails(JSONObject employmentDetails, JSONObject addressDetails, String partyID,
			Map<String, Object> map) {
		DBXResult dbxResult = null;
		try {
			String employmentDetailsURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL)
					+ PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_EMPLOYMENTDETAILS, partyID);

			DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.POST, employmentDetailsURL,
					employmentDetails.toString(), map);
			if (response.getResponse() != null) {
				JsonElement element = new JsonParser().parse((String) response.getResponse());
				if (element.isJsonObject()) {
					JsonObject jsonObject = element.getAsJsonObject();
					String employmentReference = "";
					if (jsonObject.has("employmentReferences"))
						employmentReference = jsonObject.get("employmentReferences").toString();
					if (jsonObject.has("id")) {
						dbxResult = getResultFromResponse(response);
						if (addressDetails.has(DTOConstants.PARTY_ADDRESS)) {
							String addressDetailsURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL)
									+ PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_ADDRESS, partyID);
							addressDetails.getJSONArray(DTOConstants.PARTY_ADDRESS).getJSONObject(0).put("usePurpose",
									employmentReference.substring(1, employmentReference.length() - 1));
							if (addressDetails.getJSONArray(DTOConstants.PARTY_ADDRESS).getJSONObject(0)
									.has("addressesReference")
									&& !StringUtils.isBlank(addressDetails.getJSONArray(DTOConstants.PARTY_ADDRESS)
											.getJSONObject(0).getString("addressesReference"))) {
								response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.PUT,
										addressDetailsURL, addressDetails.toString(), map);
							} else {
								response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.POST,
										addressDetailsURL, addressDetails.toString(), map);
							}
							dbxResult = getResultFromResponse(response);
						}
					} else {
						dbxResult = getResultFromResponse(response);
					}
				} else {
					dbxResult = getResultFromResponse(response);
				}
			}

		} catch (Exception e) {
			alert.prepareError("Error in UpdateDueDiligenceBusinessDelegateImpl-createEmploymentDetails : " + e.toString()).log();
		}
		return dbxResult;
	}

	@Override
	/**
	 * @param employmentDetails : employmentDetails Object.
	 * @param partyID           : PartyID for which employmentDetails need to be
	 *                          updated.
	 * @param map               : Headers which have classname, prepostprocessor
	 *                          names and session ID, etc.
	 * @return :DBXResult object.
	 * @throws Exception
	 */
	public DBXResult updateEmploymentDetails(JSONObject employmentDetails, JSONObject addressDetails, String partyID,
			Map<String, Object> map) {
		DBXResult dbxResult = null;
		try {
			String employmentDetailsURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL)
					+ PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_EMPLOYMENTDETAILS, partyID);

			DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.PUT, employmentDetailsURL,
					employmentDetails.toString(), map);

			if (response.getResponse() != null) {
				JsonElement element = new JsonParser().parse((String) response.getResponse());
				if (element.isJsonObject()) {
					JsonObject jsonObject = element.getAsJsonObject();
					if (jsonObject.has("id")) {
						dbxResult = getResultFromResponse(response);
						if (addressDetails.has(DTOConstants.PARTY_ADDRESS)) {
							String addressDetailsURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL)
									+ PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_ADDRESS, partyID);
							response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.PUT, addressDetailsURL,
									addressDetails.toString(), map);
							dbxResult = getResultFromResponse(response);
						}
					} else {
						dbxResult = getResultFromResponse(response);
					}
				} else {
					dbxResult = getResultFromResponse(response);
				}
			}
		} catch (Exception e) {
			alert.prepareError("Error in UpdateDueDiligenceBusinessDelegateImpl-createEmploymentDetails : " + e.toString()).log();
		}
		return dbxResult;
	}

	@Override
	/**
	 * @param partyID : PartyID for which employmentDetails need to be updated.
	 * @param map     : Headers which have classname, prepostprocessor names and
	 *                session ID, etc.
	 * @return :DBXResult object.
	 * @throws Exception
	 */
	public DBXResult getEmploymentDetails(String partyID, Map<String, Object> map) {
		DBXResult dbxResult = new DBXResult();
		try {
			String employmentDetailsURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL)
					+ PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_EMPLOYMENTDETAILS, partyID);
			String addressDetailsURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL)
					+ PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_ADDRESS, partyID);

			dbxResult = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, employmentDetailsURL, null, map);
			DBXResult dbxResult2 = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, addressDetailsURL,
					null, map);
			if (dbxResult.getResponse() != null && dbxResult2.getResponse() != null) {
				JsonElement element = new JsonParser().parse((String) dbxResult.getResponse());
				JsonElement element2 = new JsonParser().parse((String) dbxResult2.getResponse());
				JsonObject employmentObject = new JsonObject();
				if (element.isJsonObject()) {
					if (element.getAsJsonObject().has(DTOConstants.EMPLOYMENTS))
						employmentObject.add(DTOConstants.EMPLOYMENTS,
								element.getAsJsonObject().get(DTOConstants.EMPLOYMENTS));
					if (element.getAsJsonObject().has(DTOConstants.OCCUPATIONS))
						employmentObject.add(DTOConstants.OCCUPATIONS,
								element.getAsJsonObject().get(DTOConstants.OCCUPATIONS));
				}
				if (element2.isJsonObject()) {
					if (element2.getAsJsonObject().has(DTOConstants.PARTYADDRESS))
						employmentObject.add(DTOConstants.PARTYADDRESS,
								element2.getAsJsonObject().get(DTOConstants.PARTYADDRESS));
					dbxResult.setResponse(employmentObject.toString());
				}
				if (element.isJsonArray() && element2.isJsonArray()) {
					dbxResult.setResponse(element.getAsJsonArray().get(0).getAsJsonObject().toString());
				}
			}
		} catch (Exception e) {
			alert.prepareError("Error in UpdateDueDiligenceBusinessDelegateImpl-getEmploymentDetails : " + e.toString()).log();
		}
		return dbxResult;
	}

	private DBXResult getResultFromResponse(DBXResult response) {
		String id = "";
		DBXResult dbxResult = new DBXResult();
		try {
			JsonElement jsonElement = new JsonParser().parse((String) response.getResponse());
			JsonObject jsonObject = null;
			if (jsonElement != null) {
				if (jsonElement.isJsonArray()) {
					JsonArray jsonArray = jsonElement.getAsJsonArray();
					jsonObject = (JsonObject) jsonArray.iterator().next();
				} else {
					jsonObject = new JsonParser().parse((String) response.getResponse()).getAsJsonObject();
				}
				if (jsonObject.has("id")) {
					id = jsonObject.get("id").getAsString();
				} else {
					if (jsonObject.has("message")) {
						dbxResult.setDbpErrMsg(jsonObject.get("message").getAsString());
					}
					if (jsonObject.has("status")) {
						dbxResult.setDbpErrCode(jsonObject.get("status").getAsString());
					}
				}
			}
		} catch (Exception e) {
			alert.prepareError("Caught exception in UpdateDueDiligenceBusinessDelegateImpl-getResultFromResponse : ", e).log();
			dbxResult.setDbpErrMsg(response.getDbpErrMsg());
			return dbxResult;
		}
		dbxResult.setResponse(id);
		return dbxResult;
	}

	@Override
	public DBXResult createRegistrationDetails(JSONObject registrationDetails, String partyID,
			Map<String, Object> map, boolean isUpdate) {
		String taxDetailsURL = URLFinder.getServerRuntimeProperty(PartyPropertyConstants.DUE_DILIGENCE_MS_BASE_URL)
				+ PartyURLFinder.getServiceUrl(PartyPropertyConstants.DUE_DILIGENCE_REGISTRATIONDETAILS, partyID);

		DBXResult dbxResult = HTTPOperations.sendHttpRequest(
				isUpdate ? HTTPOperations.operations.PUT : HTTPOperations.operations.POST, taxDetailsURL,
				registrationDetails.toString(), map);

		try {
			JsonObject jsonObject = new JsonParser().parse((String) dbxResult.getResponse()).getAsJsonObject();
			if (!jsonObject.has("partyId")) {
				if (jsonObject.has("message"))
					dbxResult.setDbpErrMsg(jsonObject.get("message").getAsString());
				if (jsonObject.has("status"))
					dbxResult.setDbpErrCode(jsonObject.get("status").getAsString());
			} else {
				dbxResult.setResponse(jsonObject.get("partyId").getAsString());
			}
		} catch (Exception e) {
			alert.prepareError("Error in UpdateDueDiligenceBusinessDelegateImpl-createRegistrationDetails : " + e.toString()).log();
			dbxResult.setDbpErrMsg(dbxResult.getDbpErrMsg());
		}
		return dbxResult;
	}

	@Override
	public DBXResult createAssetLiabiltyDetails(JSONObject assetLiabiltyDetails, String partyID,
			Map<String, Object> map, boolean isUpdate) {
		String taxDetailsURL = URLFinder.getServerRuntimeProperty(PartyPropertyConstants.DUE_DILIGENCE_MS_BASE_URL)
				+ PartyURLFinder.getServiceUrl(PartyPropertyConstants.DUE_DILIGENCE_ASSETLIABDETAILS, partyID);

		DBXResult dbxResult = HTTPOperations.sendHttpRequest(
				isUpdate ? HTTPOperations.operations.PUT : HTTPOperations.operations.POST, taxDetailsURL,
				assetLiabiltyDetails.toString(), map);

		try {
			JsonObject jsonObject = new JsonParser().parse((String) dbxResult.getResponse()).getAsJsonObject();
			if (!jsonObject.has("partyId")) {
				if (jsonObject.has("message"))
					dbxResult.setDbpErrMsg(jsonObject.get("message").getAsString());
				if (jsonObject.has("status"))
					dbxResult.setDbpErrCode(jsonObject.get("status").getAsString());
			} else {
				dbxResult.setResponse(jsonObject.get("partyId").getAsString());
			}
		} catch (Exception e) {
			alert.prepareError("Error in UpdateDueDiligenceBusinessDelegateImpl-createAssetLiabiltyDetailsDetails : "
					+ e.toString()).log();
			dbxResult.setDbpErrMsg(dbxResult.getDbpErrMsg());
		}
		return dbxResult;
	}

	@Override
	public DBXResult createFinancialInformationDetails(JSONObject financialInformation, String partyID,
			Map<String, Object> map, boolean isUpdate) {
		String taxDetailsURL = URLFinder.getServerRuntimeProperty(PartyPropertyConstants.DUE_DILIGENCE_MS_BASE_URL)
				+ PartyURLFinder.getServiceUrl(PartyPropertyConstants.DUE_DILIGENCE_FINANCIALINFORMATIONS, partyID);

		DBXResult dbxResult = HTTPOperations.sendHttpRequest(
				isUpdate ? HTTPOperations.operations.PUT : HTTPOperations.operations.POST, taxDetailsURL,
				financialInformation.toString(), map);

		try {
			JsonObject jsonObject = new JsonParser().parse((String) dbxResult.getResponse()).getAsJsonObject();
			if (!jsonObject.has("partyId")) {
				if (jsonObject.has("message"))
					dbxResult.setDbpErrMsg(jsonObject.get("message").getAsString());
				if (jsonObject.has("status"))
					dbxResult.setDbpErrCode(jsonObject.get("status").getAsString());
			} else {
				dbxResult.setResponse(jsonObject.get("partyId").getAsString());
			}
		} catch (Exception e) {
			alert.prepareError("Caught exception while createFinancialInformationDetails: ", e).log();
			dbxResult.setDbpErrMsg(dbxResult.getDbpErrMsg());
		}
		return dbxResult;
	}
	
	@Override
	public DBXResult createFundsSource(JSONObject fundsSource, String partyID,
			Map<String, Object> map, boolean isUpdate) {
		String sourceOfFundsUrl = URLFinder.getServerRuntimeProperty(PartyPropertyConstants.DUE_DILIGENCE_MS_BASE_URL)
				+ PartyURLFinder.getServiceUrl(PartyPropertyConstants.DUE_DILIGENCE_SOURCEOFFUNDS, partyID);

		DBXResult dbxResult = HTTPOperations.sendHttpRequest(
				isUpdate ? HTTPOperations.operations.PUT : HTTPOperations.operations.POST, sourceOfFundsUrl,
						fundsSource.toString(), map);
		try {
			JsonObject jsonObject = new JsonParser().parse((String) dbxResult.getResponse()).getAsJsonObject();
			if (!jsonObject.has("sourceOfFundsReference")) {
				if (jsonObject.has("message"))
					dbxResult.setDbpErrMsg(jsonObject.get("message").getAsString());
				if (jsonObject.has("status"))
					dbxResult.setDbpErrCode(jsonObject.get("status").getAsString());
			} else {
				dbxResult.setResponse(jsonObject.get("sourceOfFundsReference").getAsString());
			}
		} catch (Exception e) {
			alert.prepareError("Caught exception while createFundsSource: ", e).log();
			dbxResult.setDbpErrMsg(dbxResult.getDbpErrMsg());
		}
		return dbxResult;
	}

	@Override
	public JSONArray getDueDiligenceData(String serviceURL, Map<String, Object> map, String key) {
		JSONArray result = new JSONArray();
		String dueDiligenceURL = URLFinder.getServerRuntimeProperty(PartyPropertyConstants.DUE_DILIGENCE_MS_BASE_URL)
				+ serviceURL;
		DBXResult dbxResult = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, dueDiligenceURL, null, map);
		try {
			if (dbxResult.getResponse() != null) {
				result = new JSONObject((String) dbxResult.getResponse()).getJSONArray(key);
			}
		} catch (Exception e) {
			alert.prepareError("Caught exception while fetching dueDiligence Data for " + serviceURL + ": ", e).log();
			dbxResult.setDbpErrMsg(dbxResult.getDbpErrMsg());
		}
		return result;
	}

}
