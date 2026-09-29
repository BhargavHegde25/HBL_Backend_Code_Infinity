package com.temenos.infinity.api.arrangements.javaservice;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.TokenUtils;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.config.ServerConfigurations;
import com.temenos.infinity.api.arrangements.constants.ErrorCodeEnum;
import com.temenos.infinity.api.arrangements.constants.MSCertificateConstants;
import com.temenos.infinity.api.arrangements.resource.api.ArrangementsResource;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;
import com.temenos.infinity.api.arrangements.utils.CommonUtils;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.transact.tokenmanager.exception.CertificateNotRegisteredException;

/**
 * 
 * @author smugesh
 * @version 1.0 Java Service end point to fetch all the accounts of a particular
 *          customer
 */

public class GetAccountOverviewOperation implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();
		// Initializing of Accounts through Abstract factory method
		ArrangementsResource AccountsResource = DBPAPIAbstractFactoryImpl.getResource(ArrangementsResource.class);
		ServicesManager servicesManager;
		String backendIdentifiers = null;
		String authToken = "";
		servicesManager = request.getServicesManager();
		backendIdentifiers = (String) servicesManager.getIdentityHandler().getUserAttributes()
				.get("backendIdentifiers");
		// Get Customer id and type for business user
		String customerType = (String) servicesManager.getIdentityHandler().getUserAttributes().get("CustomerType_id");
		String customerID = (String) servicesManager.getIdentityHandler().getUserAttributes().get("customer_id");
		String companyId = CommonUtils.getCompanyId(request);
		request.addRequestParam_("companyid", companyId);
		request.getHeaderMap().put("companyid", companyId);

		// If product line is null then set default to ACCOUNT
		String productLineId = request.getParameter("productLineId");
		if (StringUtils.isBlank(productLineId)) {
			productLineId = "ACCOUNTS";
		}

		String AccountId = request.getParameter("accountID");
		if (StringUtils.isBlank(AccountId)) {
			return ErrorCodeEnum.ERR_20048.setErrorCode(new Result());
		}
		String ARRANGEMENTS_BACKEND = ServerConfigurations.ARRANGEMENTS_BACKEND.getValueIfExists();
		if (ARRANGEMENTS_BACKEND!= null) {
		if (ARRANGEMENTS_BACKEND.equals("MOCK")) {
        	Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        	inputParams.put("userId", customerID);
        	inputArray[1] = inputParams;
    		GetAccounts mockAccounts = new GetAccounts();
    		return mockAccounts.invoke(methodID, inputArray, request, response);
    	}
		else if (ARRANGEMENTS_BACKEND.equals("t24")) {
        	try {
        		HashMap<String, Object> headerParams = new HashMap<String, Object>();
        		HashMap<String, Object> inputParams = new HashMap<String, Object>();
        		inputParams.put("accountID", request.getParameter("accountID"));
				String accounts = DBPServiceExecutorBuilder.builder()
                        .withServiceId("T24ISExtra")
                        .withOperationId("getAccountDetails")
                        .withRequestParameters(inputParams).withRequestHeaders(headerParams)
                        .withDataControllerRequest(request).build().getResponse();
				JSONObject accJson = new JSONObject(accounts);
				JSONObject accountsRes = new JSONObject();
				accountsRes.put("Accounts",accJson);
				return JSONToResult.convert(accountsRes.toString());
            } catch (Exception e) {
            	alert.prepareError(e.toString()).log();
                throw new ApplicationException(ErrorCodeEnum.ERR_20041);
            }
    	}
		}
		try {
			Map<String, String> inputMap = new HashMap<>();
			inputMap.put("customerId",ArrangementsUtils.getUserAttributeFromIdentity(request, "customer_id"));
			authToken = TokenUtils.getAMSAuthToken(inputMap);
		} catch (CertificateNotRegisteredException e) {
			alert.prepareError("Certificate Not Registered" + e).log();
		} catch (Exception e) {
			alert.prepareError("Exception occured during generation of authToken " + e).log();
		}

		try {
			String backendUserId = null;
			if (StringUtils.isNotBlank(backendIdentifiers)) {
				backendUserId = getBackendId(backendIdentifiers, "T24",companyId);
			}
			/*
			 * if(StringUtils.isBlank(backendUserId)) { return
			 * ErrorCodeEnum.ERR_20040.setErrorCode(new Result()); }
			 */
			result = AccountsResource.getAccountOverview(backendUserId, customerType, customerID, productLineId,
					AccountId, request, authToken);
		} catch (Exception e) {
			alert.prepareError("Unable to fetch records " + e).log();
			return ErrorCodeEnum.ERR_20041.setErrorCode(new Result());
		}
		return result;
	}

	private static String getBackendId(String backendIdentifiers, String templateName,String companyId) {
		String BackendId = null;
		BackendId = getCoreId(backendIdentifiers, templateName, "customerId", "1",companyId);
		return BackendId;
	}

	private static String getCoreId(String backendIdentifiers, String BackendType, String IdentifierName,
			String SequenceNumber,String companyId) {
		String BackendId = null;
		if (StringUtils.isNotBlank(backendIdentifiers)) {
			JSONObject backendIdentifiersJSON = new JSONObject(backendIdentifiers);
			if (backendIdentifiersJSON.has(BackendType)) {
				JSONArray templateIdentifiers = backendIdentifiersJSON.getJSONArray(BackendType);

				for (int i = 0; i < templateIdentifiers.length(); i++) {
					String identifier_name = templateIdentifiers.getJSONObject(i).getString("identifier_name");
					String sequenceNumber = templateIdentifiers.getJSONObject(i).getString("sequence_number");
					String companyIdFromLogin = templateIdentifiers.getJSONObject(i).getString("CompanyId");
					if (BackendType.equals("T24")) {
                        if (IdentifierName.equals(identifier_name) && SequenceNumber.equals(sequenceNumber) ) {
                        
                        	if(companyIdFromLogin!=null && !"".equalsIgnoreCase(companyIdFromLogin)) {//TODO: This is a work around to support no company id in backend identifier
                        		if(companyId.equalsIgnoreCase(companyIdFromLogin)) { //TODO: Move this check to line 185 once comapnyId is coming in backendidentifier
                        			BackendId = templateIdentifiers.getJSONObject(i).getString("BackendId");
                        			break;
                        		}
                        		
                        	}else{
                        		BackendId = templateIdentifiers.getJSONObject(i).getString("BackendId");
                        		break;
                        	}
                      
                        	
                        }
                    } else {
						if (IdentifierName.equals(identifier_name)) {
							BackendId = templateIdentifiers.getJSONObject(0).getString("BackendId");
						}
					}
				}
			}
		}
		return BackendId;
	}

}
