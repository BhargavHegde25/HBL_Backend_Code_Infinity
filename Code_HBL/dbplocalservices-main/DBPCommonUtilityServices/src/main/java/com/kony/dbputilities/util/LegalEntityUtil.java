package com.kony.dbputilities.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infinity.dbx.dbp.jwt.auth.utils.TemenosUtils;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.dbx.product.utils.InfinityConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LegalEntityUtil {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static final String SUFFIX_CACHE_NAME = "_CURRENT_LEID";
	private static final int EXPIRY_TIME = (20 * 60); 

	private static Map<String, JSONObject> legalEntitiesMap = null;
	
	/*
	 * public static String getLegalEntityIdFromSessionOrCache(DataControllerRequest
	 * dcRequest){ String leid = ""; try { String session_token =
	 * HelperMethods.getSessionTokenFromIdentityService(dcRequest); leid = (String)
	 * MemoryManager.getFromCache(session_token+ SUFFIX_CACHE_NAME);
	 * diagnostic.prepareDebug("retieved leid "+leid).log();
	 * 
	 * } catch (Exception e) {
	 * alert.prepareError("exception while retieving leid from cache", e).log(); }
	 * return leid; }
	 * 
	 * public static boolean setCurrentLegalEntityIdInCache(DataControllerRequest
	 * dcRequest, String currentLegalEntity) { try { String session_token =
	 * HelperMethods.getSessionTokenFromIdentityService(dcRequest);
	 * MemoryManager.saveIntoCache( session_token+ SUFFIX_CACHE_NAME,
	 * currentLegalEntity, EXPIRY_TIME);
	 * diagnostic.prepareDebug("saving leid "+currentLegalEntity).log(); return
	 * true; } catch (Exception e) {
	 * alert.prepareError(" exception on caching currentLegalEntity ", e).log(); }
	 * return false; }
	 * 
	 * public static boolean setCurrentLegalEntityIdInCache(String sessionToken,
	 * String currentLegalEntity) { try {
	 * MemoryManager.saveIntoCache(sessionToken+SUFFIX_CACHE_NAME,
	 * currentLegalEntity, EXPIRY_TIME);
	 * diagnostic.prepareDebug("saving leid "+currentLegalEntity).log(); return
	 * true; } catch (Exception e) {
	 * alert.prepareError(" exception on caching currentLegalEntity ", e).log(); }
	 * return false; }
	 */
	
	public static String getLegalEntityIdFromSessionOrCache(DataControllerRequest dcRequest){
		String leid = "";
		try {
			String session_token = HelperMethods.getSessionTokenFromIdentityService(dcRequest);
			leid = (String) MemoryManager.getFromCache(session_token+
					SUFFIX_CACHE_NAME);
			alert.prepareError("retieved leid "+leid).log();
			alert.prepareError("retieved session_token "+session_token).log();
			if(StringUtils.isBlank(leid)) {
				alert.prepareError("LegalEntityId not found in cache so fetching from identity as it is single entity").log();
				if (null != dcRequest && null != dcRequest.getServicesManager()
						&& null != dcRequest.getServicesManager().getIdentityHandler()
						&& null != dcRequest.getServicesManager().getIdentityHandler().getUserAttributes()) {
					Map<String, Object> userAttributes = dcRequest.getServicesManager().getIdentityHandler().getUserAttributes();
					String defaultLegalEntity = null != userAttributes.get("defaultLegalEntity") ? userAttributes.get("defaultLegalEntity").toString() : "";
					String homeLegalEntity = null != userAttributes.get("homeLegalEntity") ? userAttributes.get("homeLegalEntity").toString() : "";
					leid = StringUtils.isNotBlank(defaultLegalEntity) ? defaultLegalEntity : homeLegalEntity;
					if(StringUtils.isNotBlank(leid)) {
						setCurrentLegalEntityIdInCache(dcRequest,leid);
					}else {
						alert.prepareError("Unable to fetch data from both identity session and cache");
					}
				}
			}
			
		} catch (Exception e) {
			alert.prepareError("exception while retieving leid from cache", e).log();
		}
		return leid;
	}
	
	/*public static String getLegalEntityIdFromSessionOrCache(FabricRequestManager fabricRequestManager) {
		String leid = "";
		try {
			String session_token = HelperMethods.getSessionTokenFromIdentityService(fabricRequestManager);
			leid = (String) MemoryManager.getFromCache(session_token + SUFFIX_CACHE_NAME);
			diagnostic.prepareDebug("retieved leid " + leid).log();
			if (null != fabricRequestManager && null != fabricRequestManager.getServicesManager()
					&& null != fabricRequestManager.getServicesManager().getIdentityHandler()
					&& null != fabricRequestManager.getServicesManager().getIdentityHandler().getUserAttributes()) {
				Map<String, Object> userAttributes = fabricRequestManager.getServicesManager().getIdentityHandler()
						.getUserAttributes();
				String defaultLegalEntity = null != userAttributes.get("defaultLegalEntity")
						? userAttributes.get("defaultLegalEntity").toString()
						: "";
				String homeLegalEntity = null != userAttributes.get("homeLegalEntity")
						? userAttributes.get("homeLegalEntity").toString()
						: "";
				leid = StringUtils.isNotBlank(defaultLegalEntity) ? defaultLegalEntity : homeLegalEntity;
				if (StringUtils.isBlank(leid)) {
					alert.prepareError("Unable to fetch data from both identity session and cache");
				}
			}
 
		} catch (Exception e) {
			alert.prepareError("exception while retieving leid from cache", e).log();
		}
		return leid;
	}*/
 
	public static boolean setCurrentLegalEntityIdInCache(DataControllerRequest dcRequest,
			String currentLegalEntity) {
		try {
			String session_token = HelperMethods.getSessionTokenFromIdentityService(dcRequest);
			alert.prepareError("retieved session_token "+session_token).log();
			MemoryManager.saveIntoCache( session_token+
					SUFFIX_CACHE_NAME, currentLegalEntity, EXPIRY_TIME);
			alert.prepareError("saving leid "+currentLegalEntity).log();
			return true;
		} catch (Exception e) {
			alert.prepareError(" exception on caching currentLegalEntity ", e).log();
		}
		return false;
	}
	
	public static boolean setCurrentLegalEntityIdInCache(String sessionToken,
			String currentLegalEntity) {
		try {
			MemoryManager.saveIntoCache(sessionToken+SUFFIX_CACHE_NAME,
					currentLegalEntity, EXPIRY_TIME);
			alert.prepareError("retieved session_token "+sessionToken).log();
			alert.prepareError("saving leid "+currentLegalEntity).log();
			return true;
		} catch (Exception e) {
			alert.prepareError(" exception on caching currentLegalEntity ", e).log();
		}
		return false;
	}
 
	
	
	public static String getCurrentLegalEntityIdFromCache(DataControllerRequest dcRequest) {
		  return TemenosUtils.getInstance().getDataFromCache(dcRequest, InfinityConstants.currentLegalEntityId).toString().replace("\"", "");
	}

	public static String getLegalEntityIdFromSession(DataControllerRequest dcRequest) {
		Result userAttributesResponse = new Result();

        String legalEntityId = "";

        try {
        	 userAttributesResponse = HelperMethods.callApi(dcRequest, null, HelperMethods.getHeaders(dcRequest), URLConstants.USER_ID_GET_IDNETITY);
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
        }

        if (userAttributesResponse.getNameOfAllParams().contains("legalEntityId")) {
        	legalEntityId = userAttributesResponse.getParamValueByName("legalEntityId");
        }
        return legalEntityId;

	}

	public static List<String> getAllCompanyLegalUnits(DataControllerRequest dcRequest) {
		
		Result legalEntityIds = new Result();
		try {
			
			legalEntityIds = ServiceCallHelper.invokeServiceAndGetResult(dcRequest, null, HelperMethods.getHeaders(dcRequest),
					URLConstants.LEGAL_ENTITY_ID_GET);
        } catch (HttpCallException e) {
            alert.prepareError(e.toString()).log();
        }
		
		JsonElement legalEntitiesElement = new JsonParser().parse(ResultToJSON.convert(legalEntityIds));
	    JsonArray legalEntitiesJsonArray = legalEntitiesElement.getAsJsonObject().get("companyLegalUnits").getAsJsonArray();
	    String companyLegalUnits = "";
	    for(int i = 0; i < legalEntitiesJsonArray.size(); i++)
	    {
	    	companyLegalUnits += legalEntitiesJsonArray.get(i).getAsJsonObject().get("id").getAsString() + ",";      	 
	    }
	    List<String> legalEntities = Arrays.asList(companyLegalUnits.split(","));

		return legalEntities;
		
	}
	
	
	public static Set<String> getAllCompanyLegalUnits() {
		Set<String> companyLegalUnits = new HashSet<>();
		try {
			/*String legalEntityIdsFrmCache = (String) MemoryManager
					.getFromCache(CacheConstants.ALL_LEGALENTITY_IDS);
			if(StringUtils.isNotBlank(legalEntityIdsFrmCache)) {
				for(String id : legalEntityIdsFrmCache.split(",")) {
					companyLegalUnits.add(id);
				}
				return companyLegalUnits;
			} else {*/
				JsonObject legalEntityIds = ServiceCallHelper.invokeServiceAndGetJson(null, new HashMap<>(),
						URLConstants.LEGAL_ENTITY_ID_GET);
				JsonArray legalEntitiesJsonArray = JSONUtil
						.getJsonArrary(legalEntityIds, "companyLegalUnits");
				for(JsonElement LEID:  legalEntitiesJsonArray) {
					companyLegalUnits.add(LEID.getAsJsonObject().get("id").getAsString()); 
				}
			/*}*/
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
        }
		return companyLegalUnits;
	}
	
	public static boolean isLegalEntityValid(String legalEntityId) {
		return getAllCompanyLegalUnits().contains(legalEntityId);
	}

	/**
	 * @param request DataController Instance containing payload and headers
	 *                This function extracts the legalEntityId from the payload and then adds it in the headers
	 */
	public static void addCompanyIDToHeaders( DataControllerRequest request) {
		if(request == null) {
			return;
		}
		String companyId = null;
		String parameter = request.getParameter(DBPUtilitiesConstants.LEGALENTITYID);
		if(parameter != null && !parameter.trim().isEmpty()) {
			companyId = parameter;
		}
		if(companyId == null) {
			companyId = getLegalEntityIdFromSessionOrCache(request);
		}
		if(request.getHeaderMap() != null) {
			request.getHeaderMap().put(DBPUtilitiesConstants.COMPANYID, StringUtils.isBlank(companyId) ? "" : companyId);
			request.getHeaderMap().put(DBPUtilitiesConstants.COMPANY_ID, StringUtils.isBlank(companyId) ? "" : companyId);
		}
	}
	public static Boolean checkForSuperAdmin(DataControllerRequest dcRequest) throws ApplicationException {

		Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromAPIDBPIdentityService(dcRequest);
		if (HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) {
			Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(dcRequest);
			if (!userPermissions.contains("USER_MANAGEMENT") && !userPermissions.contains("USER_MANAGEMENT_VIEW")) {
				throw new ApplicationException(ErrorCodeEnum.ERR_10051, "Authorization error. logged in userDoesn't have permission");
			}
			return false;
		}
		return true;
	}
	
	public static String getLegalEntityFromPayload(Map<String, String> inputParams, DataControllerRequest dcRequest) throws ApplicationException{
		
		String legalEntityId = StringUtils.isNotBlank(inputParams.get(InfinityConstants.legalEntityId)) ? inputParams.get(InfinityConstants.legalEntityId)
				: dcRequest.getParameter(InfinityConstants.legalEntityId);
		if (StringUtils.isBlank(legalEntityId)) {
			alert.prepareError("LegalEntity is mandatory!!").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_29040);
		}
		return legalEntityId;
	}

	public static boolean isSingleEntity() {
		Result appInfo = null;
		try {
			appInfo = ServiceCallHelper.invokeServiceAndGetResult(null, null, URLConstants.APPLICATION_GET,
					StringUtils.EMPTY);
		} catch (HttpCallException e) {
		}
		return (HelperMethods.getFieldValue(appInfo, "isSingleEntity").equals("1")
				|| HelperMethods.getFieldValue(appInfo, "isSingleEntity").equals("true"));

	}
	public static JsonObject addLegalEntityToPermissionsIfReq(JsonObject jsonObject, Boolean isSuperAdmin, Set<String> allLegalEntities,
			DataControllerRequest dcRequest) throws ApplicationException {

		String legalEntityId = "";
		Boolean isSingleEntity = false;
		Result appInfo = null;
		try {
			appInfo = ServiceCallHelper.invokeServiceAndGetResult(null, null, URLConstants.APPLICATION_GET,
			        StringUtils.EMPTY);
		} catch (HttpCallException e) {
		}
		isSingleEntity = HelperMethods.getFieldValue(appInfo, "isSingleEntity")=="false"? false:true;

		if (isSuperAdmin) {
			legalEntityId = jsonObject.has(InfinityConstants.legalEntityId) ? jsonObject.get(InfinityConstants.legalEntityId).getAsString(): null;
			if (StringUtils.isBlank(legalEntityId)) {
				alert.prepareError("LegalEntity is mandatory!!").log();
				throw new ApplicationException(ErrorCodeEnum.ERR_29040);
			}
		}

		else if (StringUtils.isBlank(getLegalEntityIdFromSessionOrCache(dcRequest)))
			throw new ApplicationException(ErrorCodeEnum.ERR_10001);
		else {
			legalEntityId = (jsonObject.has(InfinityConstants.legalEntityId) && isSingleEntity == false) ? jsonObject.get(InfinityConstants.legalEntityId).getAsString():getLegalEntityIdFromSessionOrCache(dcRequest);
			jsonObject.addProperty(InfinityConstants.legalEntityId, legalEntityId);
		}

		if (!allLegalEntities.contains(legalEntityId)) {
			alert.prepareError("Logged in user do not have access to this legalEntity ").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_12403);
		}
		return jsonObject;
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
					throw new ApplicationException(ErrorCodeEnum.ERR_29052);
				}
			} else {
				throw new ApplicationException(ErrorCodeEnum.ERR_29052);
			}
		} catch (JSONException je) {
			alert.prepareError("Error while parsing Utility/operations/LegalEntity/getLegalEntities response: " + je.getMessage()).log();
		} catch ( DBPApplicationException dbpae ) {
			alert.prepareError("failed to get legalEntity details " + dbpae.getMessage()).log();
		}
	}

	public static String getLegalEntityForCifAndContract(String cifId, String contractId) throws ApplicationException {
		if(StringUtils.isEmpty(cifId) || StringUtils.isEmpty(contractId)){
			return null;
		}
		String legalEntityId = null;
		try{
			Map<String, Object> reqParams = new HashMap<>();
			reqParams.put(DBPUtilitiesConstants.FILTER, "coreCustomerId" + DBPUtilitiesConstants.EQUAL + cifId +
					DBPUtilitiesConstants.AND + "contractId" + DBPUtilitiesConstants.EQUAL + contractId);
			JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(reqParams, new HashMap<>(), URLConstants.CONTRACTCORECUSTOMER_GET);
			if(response != null && response.has("contractcorecustomers")){
				JsonArray responseArr = JSONUtil.getJsonArrary(response, "contractcorecustomers");
				JsonObject cifContractRecord = responseArr.get(0).getAsJsonObject();
				if(cifContractRecord.has("companyLegalUnit") && !StringUtils.isEmpty(cifContractRecord.get("companyLegalUnit").getAsString())){
					legalEntityId = cifContractRecord.get("companyLegalUnit").getAsString();
				}
				else{
					alert.prepareError("No LegalEntity found for the given coreCustomerId and contractId!").log();
					throw new ApplicationException(ErrorCodeEnum.ERR_29051);
				}
			}
		} catch (Exception e){
			alert.prepareError("Failed to fetch legal entity from CONTRACTCORECUSTOMERS table: " + e).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_29051);
		}
		return legalEntityId;
	}

	public static String getLegalEntityCurrencyForCifAndContract(String cifId, String contractId) throws ApplicationException {
		String legalEntityId = getLegalEntityForCifAndContract(cifId, contractId);
		return getCurrencyForLegalEntity(legalEntityId);
	}
	
	public static String getCacheKeyForCurrentLegalEntityFeaturePermissions(DataControllerRequest dcRequest) {
		String session_token = HelperMethods.getSessionTokenFromIdentityService(dcRequest);
		return session_token + "_FEATURE_PERMISSIONS";
	}

	public static JSONObject getUserCurrentLegalEntityFeaturePermissions(DataControllerRequest dcRequest) {
		// This retrieves the user's current legal entity features and permissions from cache
		try {
			String featuresAndPermissions = ServiceCallHelper.invokeServiceAndGetString(null, null,
					URLConstants.LOGGEDIN_USER_PERMISSOINS, dcRequest.getHeader("x-kony-authorization"));
			diagnostic.prepareDebug("retieved current leid features and permissions::" + featuresAndPermissions).log();
			if(StringUtils.isNotBlank(featuresAndPermissions)) {
				return new JSONObject(featuresAndPermissions);
			}
		} catch (Exception e) {
			alert.prepareError("exception while retieving leid features permissions from cache", e).log();
		}
		return null;
	}
	
	public static JSONObject getUserCurrentLegalEntityFeaturePermissions(FabricRequestManager fabricRequestManager) {
		// This retrieves the user's current legal entity features and permissions from cache
		try {
			String featuresAndPermissions = ServiceCallHelper.invokeServiceAndGetString(null, null,
					URLConstants.LOGGEDIN_USER_PERMISSOINS, 
					fabricRequestManager.getHeadersHandler().getHeader("x-kony-authorization"));
			diagnostic.prepareDebug("retieved current leid features and permissions::" + featuresAndPermissions).log();
			if(StringUtils.isNotBlank(featuresAndPermissions)) {
				return new JSONObject(featuresAndPermissions);
			}
		} catch (Exception e) {
			alert.prepareError("exception while retieving leid features permissions from cache", e).log();
		}
		return null;
	}
}
