package com.temenos.infinity.api.arrangements.javaservice;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.json.JSONArray;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.IntegrationTemplateURLFinder;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.TokenUtils;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbx.objects.Account;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.utils.DTOConstants;
import com.temenos.dbx.product.utils.InfinityConstants;
import com.temenos.infinity.api.arrangements.config.ArrangementsAPIServices;
import com.temenos.infinity.api.arrangements.config.ServerConfigurations;
import com.temenos.infinity.api.arrangements.constants.ErrorCodeEnum;
import com.temenos.infinity.api.arrangements.resource.api.ArrangementsResource;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;
import com.temenos.infinity.api.arrangements.utils.CommonUtils;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.commons.invocation.Executor;
import com.temenos.infinity.transact.tokenmanager.exception.CertificateNotRegisteredException;

/**
 * 
 * @version 1.0 Java Service end point to fetch account balances of given accounts or all accounts of hte customer
 */

public class GetLatestBalances implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();

		Result result = new Result();
		String authToken = "";

		Map<String, String> params = HelperMethods.getInputParamMap(inputArray);

		// Initializing of Accounts through Abstract factory method
		ArrangementsResource AccountsResource = DBPAPIAbstractFactoryImpl.getResource(ArrangementsResource.class);
		String companyId = null;
		String loginUserId = null;
		boolean isSuperAdmin = false;
		Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromAPIDBPIdentityService(request);
		if (HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) {
			loginUserId = HelperMethods.getCustomerIdFromSession(request);
		} else {
			loginUserId = params.containsKey(InfinityConstants.id)
					&& StringUtils.isNotBlank(params.get(InfinityConstants.id)) ? params.get(InfinityConstants.id)
							: request.getParameter(InfinityConstants.id);
			companyId = getCompanyIdFromDB(loginUserId, request);
			isSuperAdmin = true;
		}

		if (!isSuperAdmin && StringUtils.isNotBlank(loginUserId)) {
			companyId = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(request);
			if(StringUtils.isBlank(companyId)) {
				return com.kony.dbputilities.util.ErrorCodeEnum.ERR_29040.setErrorCode(result);
			}			
		}

		if (StringUtils.isBlank(loginUserId) && isSuperAdmin) {
			companyId = CommonUtils.getCompanyId(request);
		}

		// If product line is null then set default to ACCOUNT
		String productLineId = request.getParameter("productLineId");
		if (StringUtils.isBlank(productLineId)) {
			productLineId = "ACCOUNTS";
		}

		String AccountId = request.getParameter("Account_id");
		if(StringUtils.isNotBlank(AccountId)) {
			AccountId = getValidUserAccounts(loginUserId, AccountId, companyId, request);
			if(StringUtils.isBlank(AccountId)) {
				return ErrorCodeEnum.ERR_11024.setErrorCode(result);
			}
		}

		String ARRANGEMENTS_BACKEND = ServerConfigurations.ARRANGEMENTS_BACKEND.getValueIfExists();
		if (ARRANGEMENTS_BACKEND != null) {
			if (ARRANGEMENTS_BACKEND.equalsIgnoreCase("t24")) {
				try {
					HashMap<String, Object> headerParams = new HashMap<String, Object>();
					HashMap<String, Object> inputParams = new HashMap<String, Object>();

					if (StringUtils.isBlank(AccountId)) {
						StringBuilder accountIdsStr = new StringBuilder();
						TemenosUtils temenosUtils = TemenosUtils.getInstance();
						HashMap<String, Account> accounts = temenosUtils.getAccountsMapFromCache(request);
						if (accounts != null) {
							accounts.forEach((k, v) -> accountIdsStr.append(k + " "));
						} else {
							return ErrorCodeEnum.ERR_11024.setErrorCode(result);
						}
						AccountId = accountIdsStr.toString();
					}

					inputParams.put("Account_id", AccountId);
					headerParams.put("companyId", companyId);
					String responseT24 = Executor.invokeService(
							ArrangementsAPIServices.T24IRISARRANGEMENTSERVICES_GETLATESTBALANCES, inputParams,
							headerParams);
					result = JSONToResult.convert(responseT24);
					alert.prepareError("Response from T24 GetLatestBalances Service: " + responseT24).log();
					return result;
				} catch (Exception e) {
					alert.prepareError(e.toString()).log();
					return ErrorCodeEnum.ERR_20041.setErrorCode(new Result());
				}
			}
		}

		try {
			Map<String, String> inputMap = new HashMap<>();
			inputMap.put("customerId", ArrangementsUtils.getUserAttributeFromIdentity(request, "customer_id"));
			authToken = TokenUtils.getAMSAuthToken(inputMap);
			CommonUtils.setCompanyIdToRequest(request);
		} catch (CertificateNotRegisteredException e) {
			alert.prepareError("Certificate Not Registered" + e).log();
		} catch (Exception e) {
			alert.prepareError("Exception occured during generation of authToken " + e).log();
		}

		StringBuilder accountIdsStr = new StringBuilder();
		if (StringUtils.isBlank(AccountId)) {
			TemenosUtils temenosUtils = TemenosUtils.getInstance();
			HashMap<String, Account> accounts = temenosUtils.getAccountsMapFromCache(request);
			String legalEntity = companyId;
			if (accounts != null) {
				accounts.forEach((k, v) -> accountIdsStr.append(legalEntity + "-" + k + " "));
			} else {
				return ErrorCodeEnum.ERR_11024.setErrorCode(result);
			}
			diagnostic.prepareDebug("Accounts Input from cache:" + accountIdsStr).log();
		} else {
			String accountsIds[] = AccountId.split(" ");
			for (String accId : accountsIds) {
				accountIdsStr.append(companyId).append("-").append(accId).append(" ");
			}
			diagnostic.prepareDebug("Accounts from input:" + accountIdsStr).log();
		}
		try {
			result = AccountsResource.getLatestBalances(accountIdsStr.toString().trim(), authToken);
		} catch (Exception e) {
			alert.prepareError("Unable to fetch records from Backend", e).log();
			return ErrorCodeEnum.ERR_20041.setErrorCode(new Result());
		}
		return result;
	}

    private String getCompanyIdFromDB(String loginUserId, DataControllerRequest request) {
        BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
        backendIdentifierDTO.setBackendType(IntegrationTemplateURLFinder.getBackendURL(InfinityConstants.BackendType));
        backendIdentifierDTO.setCustomer_id(loginUserId);
        backendIdentifierDTO = (BackendIdentifierDTO) backendIdentifierDTO.loadDTO();
        return backendIdentifierDTO!= null ? backendIdentifierDTO.getCompanyId() : EnvironmentConfigurationsHandler.getServerProperty(DBPUtilitiesConstants.BRANCH_ID_REFERENCE);
    }
    
	private String getValidUserAccounts(String customerId, String accountIdList, String legalEntityId,
			DataControllerRequest request) {
		String validAccounts = "";
		try {
			String filter = DBPUtilitiesConstants.CUSTOMER_ID + DBPUtilitiesConstants.EQUAL + customerId
					+ DBPUtilitiesConstants.AND + DTOConstants.LEGALENTITYID + DBPUtilitiesConstants.EQUAL
					+ legalEntityId;
			String select = InfinityConstants.Account_id;
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put(DBPUtilitiesConstants.FILTER, filter);
			inputParams.put(DBPUtilitiesConstants.SELECT, select);
			String response = HelperMethods.callApiAndGetString(request, inputParams, HelperMethods.getHeaders(request),
					URLConstants.CUSTOMERACCOUNTS_GET);
			JSONObject accountsJson = new JSONObject(response);
			JSONArray accountsArray = accountsJson.getJSONArray(DBPDatasetConstants.DATASET_CUSTOMERACCOUNTS);
			for (int i = 0; i < accountsArray.length(); i++) {
				String accountId = accountsArray.getJSONObject(i).getString(InfinityConstants.Account_id);
				if (accountIdList.contains(accountId)) {
					validAccounts += accountId + " ";
				}
			}
		} catch (Exception e) {
			diagnostic.prepareDebug("Unable to fetch Customer accounts from Backend", e).log();
			return "";
		}
		return validAccounts;
	}
}
