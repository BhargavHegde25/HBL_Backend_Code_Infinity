package com.temenos.auth.usermanagement.resource.impl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Record;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.auth.admininteg.operation.GetLegalEntitiesOperation;
import com.temenos.auth.usermanagement.businessdelegate.api.AuthUserManagementBusinessDelegate;
import com.temenos.auth.usermanagement.resource.api.AuthUserManagementResource;

public class AuthUserManagementResourceImpl implements AuthUserManagementResource {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	/**
	 * 1) Fetches all legal entities from Spotlight.
	 * 2) Fetches active legal entities from database.
	 * 3) Populate legal entity details like description, region etc for the response.
	 */
	@Override
	public Result getCustomerActiveLegalEntities(String methodId, Object[] inputArray,
			DataControllerRequest requestInstance, DataControllerResponse responseInstance) 
			throws ApplicationException {
		try {
			
			/**
			 * Fetches all legal entities from Spotlight.
			 */
			Result allLegalEntities = getAllLegalEntities(methodId, inputArray,
					requestInstance, responseInstance);
			
			/**
			 * Fetches active legal entities from database.
			 */
			String customerId = HelperMethods.getCustomerIdFromSession(requestInstance);
			AuthUserManagementBusinessDelegate authBusinsesDelegate = DBPAPIAbstractFactoryImpl
					.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(AuthUserManagementBusinessDelegate.class);
			Result customerLegalEntity = authBusinsesDelegate.getCustomerActiveLegalEntities(customerId);
			
			/**
			 * Populate legal entity details like description, region etc for the response.
			 */
			if (HelperMethods.hasRecords(allLegalEntities)) {
				List<Record> customerLegalEntityRecords = customerLegalEntity
						.getDatasetById("customerlegalentity").getAllRecords();
				Set<String> customerLegalEntitySet = new HashSet<>();
				for(Record s : customerLegalEntityRecords) {
					customerLegalEntitySet.add(s.getParamValueByName("legalEntityId"));
				}
				Iterator<Record> allLegalEntitiesITR = allLegalEntities
						.getAllDatasets().get(0).getAllRecords().iterator();
				Result returnResult = new Result();
				Dataset returnDs = new Dataset();
				returnDs.setId("customerlegalentity");
				returnResult.addDataset(returnDs);
				Record temp = null;
				String leid = "";
				while(allLegalEntitiesITR.hasNext()) {
					temp = allLegalEntitiesITR.next();
					leid = temp.getParamValueByName("id");
					if(customerLegalEntitySet.contains(leid)) {
						returnDs.addRecord(temp);
					}
				}
				return returnResult;
			}
			diagnostic.prepareDebug("Spotlight has returned empty response.").log();
			
		}catch(ApplicationException e) {
			alert.prepareError("Exception", e).log();
			throw e;
		} catch(Exception e) {
			alert.prepareError("Exception",e).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_10220);
		}
		return ErrorCodeEnum.ERR_10220.setErrorCode(new Result());
	}

	/**
	 * fetches all legal entities from spotlight
	 */
	@Override
	public Result getAllLegalEntities(String methodId, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		return (Result) new GetLegalEntitiesOperation().invoke(methodId, inputArray,
				requestInstance, responseInstance);
	}

	/**
	 * fetches all features and permissions for the loggedin user and current legal entity id
	 */
	@Override
	public Result getCustomerFeatureAndPermissions(String methodId, Object[] inputArray,
			DataControllerRequest requestInstance, DataControllerResponse responseInstance) throws Exception {
		
		try {
			AuthUserManagementBusinessDelegate businessDelegate =
			        DBPAPIAbstractFactoryImpl.getBusinessDelegate(AuthUserManagementBusinessDelegate.class);
			String cacheKey = LegalEntityUtil.getCacheKeyForCurrentLegalEntityFeaturePermissions(requestInstance);
			Map<String, String> userInfo = HelperMethods.getUserFromIdentityService(requestInstance);
			String leId = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(requestInstance);
			return businessDelegate.getCustomerFeatureAndPermissions(leId, cacheKey, userInfo);
		} catch (ApplicationException e) {
			alert.prepareError("Exception while fetching permissions", e).log();
			throw e;
		}
	}

	@Override
	public Result updateTNCTimeStamp(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse responseInstance) throws Exception {
		Result result = new Result();
		Map<String, Object> inputParams = new HashMap<String, Object>();
		Map<String, Object> responseMap = new HashMap<String, Object>();
		ObjectMapper objMapper = new ObjectMapper();
		try {
			Map<String, Object> payloadMap = (Map<String, Object>) inputArray[1];
			if (payloadMap.get("configurationId") != null && !payloadMap.get("configurationId").toString().isEmpty()) {
				String currentTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
				inputParams.put("configuration_id", payloadMap.get("configurationId"));
				inputParams.put("tncTimeStamp", currentTimeStamp);
				JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, null,
						URLConstants.CONFIGURATION_TNC_TS_UPDATE);
				responseMap = objMapper.readValue(response.toString(), Map.class);
				if (Integer.parseInt(responseMap.get("updatedRecords").toString()) > 0) {
					result = JSONToResult.convert(JSONUtils.stringify(responseMap));
					result.addOpstatusParam(0);
					result.addHttpStatusCodeParam(200);
				} else {
					result.addOpstatusParam(1);
					result.addHttpStatusCodeParam(400);
				}
			} else {
				result.addOpstatusParam(1);
				result.addHttpStatusCodeParam(400);
				result.addStringParam("message", "configurationId is mandatory");
				alert.prepareError("configurationId is mandatory").log();
			}
		} catch (Exception e) {
			alert.prepareError("Failed to update the configuration" + e).log();
		}
		return result;
	}

}
