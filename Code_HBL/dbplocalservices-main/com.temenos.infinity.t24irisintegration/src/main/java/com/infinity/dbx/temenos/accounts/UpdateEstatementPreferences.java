package com.infinity.dbx.temenos.accounts;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.infinity.dbx.temenos.TemenosBaseService;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbputilities.util.TokenUtils;
import com.kony.dbx.objects.Account;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.api.processor.IdentityHandler;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.config.ArrangementsAPIServices;
import com.temenos.infinity.api.arrangements.constants.ErrorCodeEnum;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.commons.invocation.Executor;
import com.temenos.infinity.transact.tokenmanager.exception.CertificateNotRegisteredException;

/**
 * Java service to validate and update estatement preferences in t24
 *
 * @author suryaacharans
 *
 */
public class UpdateEstatementPreferences extends TemenosBaseService {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	@SuppressWarnings("unchecked")
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {

		Result result = new Result();
		Result emptyResult = new Result();
		try {
			HashMap<String, Object> params = (HashMap<String, Object>) inputArray[1];
			alert.prepareError("UpdateEstatementPreferences start request::"+request + "::response::"+response).log();
			if (params == null) {
				CommonUtils.setErrMsg(result, "No input parameters provided");
				CommonUtils.setOpStatusError(result);
				return result;
			}
			//Store Params in a variable
			String eStatementEnable = CommonUtils.getParamValue(params, AccountsConstants.PARAM_ESTATEMENT_TOGGLE);
			String accountId = CommonUtils.getParamValue(params, AccountsConstants.ACCOUNTID);

			if (!StringUtils.equalsIgnoreCase(eStatementEnable, "1")
					&& !StringUtils.equalsIgnoreCase(eStatementEnable, "0")) {
				result.addStringParam("errmsg", "Invalid EStatementEnable Value");
				result.addOpstatusParam(0);
				result.addHttpStatusCodeParam(200);
				return false;
			}
			String ARRANGEMENTS_BACKEND = getValueIfExists("ARRANGEMENTS_BACKEND");
			if (ARRANGEMENTS_BACKEND!= null) {
	        if (ARRANGEMENTS_BACKEND.equals("MOCK")) {
	        	return updateDBXResults(request, params);
	    	}
	        else if (ARRANGEMENTS_BACKEND.equals("MS")) {
	            
	            	String authToken = "";
	            	IdentityHandler identityHandler = request.getServicesManager().getIdentityHandler();
	    			Map<String, Object> userAttributes = identityHandler.getUserAttributes();
	    			String companyId = null;
	    			if(userAttributes != null && userAttributes.size() >0){
	    				companyId = (String)userAttributes.get("legalEntityId");
	    			}else {
	    				companyId = (String)userAttributes.get("companyId");
	    			} 
	    				
	            	//String companyId = ServerConfigurations.AMS_COMPANYID.getValueIfExists();            	

	            	try {
	            		Map<String, String> inputMap = new HashMap<>();
	        			inputMap.put("customerId",ArrangementsUtils.getUserAttributeFromIdentity(request, "customer_id"));
	        			authToken = TokenUtils.getAMSAuthToken(inputMap);
	                } catch (CertificateNotRegisteredException e) {
	                    alert.prepareError("Certificate Not Registered" + e).log();
	                } catch (Exception e) {
	                    alert.prepareError("Exception occured during generation of authToken " + e).log();
	                }
	            	TemenosUtils temenosUtils = TemenosUtils.getInstance();
	        		HashMap<String, Account> accounts = temenosUtils.getAccountsMapFromCache(request);


	        		if (accounts != null && StringUtils.isNotEmpty(eStatementEnable)) {
	        			Account account = accounts.get(accountId);
	        			
	        			//If account not found in cache
	        			if (account == null) {
	        				result.addStringParam("errmsg", "Invalid Account Number");
	        				result.addOpstatusParam(0);
	        				result.addHttpStatusCodeParam(200);
	        				return result;
	        			}
	        			
	        			String arrangementId = account.getArrangementId();
	        			if(companyId!= null) {
		            		if(!StringUtils.contains(companyId,arrangementId)) {
		            			arrangementId = companyId+"-"+arrangementId;
		            		}
		            	}
	        			request.addRequestParam_("extArrangementId", arrangementId);
	        			if(StringUtils.equals(eStatementEnable, "1"))
	        			request.addRequestParam_("estmtEnabled", "true");
	        			else
	        				request.addRequestParam_("estmtEnabled", "false");
	        			
	        		        Map<String, Object> inputMap = new HashMap<>();
	        		        Map<String, Object> headerMap = new HashMap<>();
	        		        inputMap.put("extArrangementId", arrangementId);
	        		        if(StringUtils.equals(eStatementEnable, "1"))
	        		        	inputMap.put("estmtEnabled", "true");
	    	        			else
	    	        				inputMap.put("estmtEnabled", "false");
	        		       // inputMap.put("estmtEnabled", CommonUtils.getParamValue(params, AccountsConstants.PARAM_ESTATEMENT_TOGGLE));
	        		        headerMap = ArrangementsUtils.generateSecurityHeaders(authToken, headerMap);
	        		        
	        		        String updateEstatementResponse = null;
	        		        try {
	        		        updateEstatementResponse = Executor.invokePassThroughServiceAndGetString(
	        		                    ArrangementsAPIServices.UPDATE_ESTATEMENT_PREFERENCES, inputMap, headerMap);
	        		        }
	        		        catch (Exception e) {
	        		            alert.prepareError("Unable to update estatement preferences " + e).log();
	        		            throw new ApplicationException(ErrorCodeEnum.ERR_20049);
	        		        }
	        		            result = JSONToResult.convert(updateEstatementResponse);
	        		            String status = result.getParamValueByName(AccountsConstants.PARAM_STATUS);
	        		            if(StringUtils.isNotEmpty(status)
	        							&& StringUtils.equalsIgnoreCase("AMS-0017", status)){
	        		            	return updateDBXResults(request, params);
	        							}
	        		        }
	        		}
	            	
	        	
	        
	        else {
			String operationName = null;
			HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
			request.addRequestParam_(AccountsConstants.PARAM_VALIDATE, "true");
			String serviceName = TemenosConstants.SERVICE_T24IRISARRANGEMENTSERVICES;
			operationName = TemenosConstants.OP_NAME_UPDATE_ESTMT_PREF;
			//T24 Update AA.ARR.STATEMENT validation call
			result = CommonUtils.callIntegrationService(request, params, serviceHeaders, serviceName, operationName,
					true);

			String status = result.getParamValueByName(AccountsConstants.PARAM_STATUS);
			String errMessage = result.getParamValueByName(AccountsConstants.PARAM_ERROR_MESSAGE);
			String errCode = result.getParamValueByName(AccountsConstants.PARAM_ERROR_CODE);
			// Process Error Message
			if ((StringUtils.isNotEmpty(status) && AccountsConstants.STATUS_FAILED.equalsIgnoreCase(status))
					|| (StringUtils.isNotBlank(errMessage)
							&& !StringUtils.equalsIgnoreCase("Record Not Changed", errMessage))) {
				alert.prepareError("T24 Error Message : " + errMessage).log();
				Result res = new Result();
				CommonUtils.setErrCode(res, errCode);
				CommonUtils.setErrMsg(res, "Estatement Preference error: " + errMessage);
				return res;
			} 
			// If success or record isn't change , perform the t24 call and update dbxdb
			else if (((StringUtils.isNotEmpty(status) && AccountsConstants.STATUS_SUCCESS.equalsIgnoreCase(status)))
					|| (StringUtils.isNotEmpty(errMessage)
							&& StringUtils.equalsIgnoreCase("Record Not Changed", errMessage))) {

				Result execResult = executePostValidation(request, params);
				execResult.addStringParam(AccountsConstants.PARAM_EXTERNAL_ID, accountId);
				return execResult;

			}
	        }
			}
			alert.prepareError("UpdateEstatementPreferences end request::"+request + "::response::"+response).log();
			
		} catch (Exception e) {
			Result errorResult = new Result();
			alert.prepareError("Exception Occured while updating e-statment preferences" + e).log();
			CommonUtils.setOpStatusError(result);
			CommonUtils.setErrMsg(errorResult, e.getMessage());
			return errorResult;
		}
		result.addOpstatusParam(0);
		result.addHttpStatusCodeParam(0);
		alert.prepareError("Exception occured , conditions not met").log();
		return emptyResult;
	}
	private Result updateDBXResults(DataControllerRequest request, HashMap<String, Object> params)
			throws Exception {

		Result res1 = new Result();
		String eStatementEnable = CommonUtils.getParamValue(params, AccountsConstants.PARAM_ESTATEMENT_TOGGLE);
		String accountId = CommonUtils.getParamValue(params, AccountsConstants.ACCOUNTID);
		String email = CommonUtils.getParamValue(params, TemenosConstants.PARAM_EMAIL);
		String loginUserId = CommonUtils.getUserAttributeFromIdentity(request, "customer_id"); 
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		HashMap<String, Object> headerParams = new HashMap<String, Object>();
		inputParams.put("$filter", "Customer_id eq " + loginUserId);
		request.addRequestParam_("$filter", "Customer_id eq " + loginUserId);
		Result readAccounts = CommonUtils.callIntegrationService(request, inputParams, headerParams,
				TemenosConstants.SERVICE_BACKEND_CERTIFICATE, TemenosConstants.OPERATION_CUSTOMER_ACCOUNTS_GET, true);
		Dataset getAccountsDS = readAccounts.getDatasetById(AccountsConstants.DS_CUSTOMERACCOUNTS);
		int flag = 0;
		String status;
		int accountFound = 0;
		if (getAccountsDS != null && !getAccountsDS.getAllRecords().isEmpty()) {
			alert.prepareError("if true").log();
			for (Record rec : getAccountsDS.getAllRecords()) {
				//if account is present, update it
				if (StringUtils.equalsIgnoreCase(rec.getParamValueByName(AccountsConstants.DB_ACCOUNTID),
						accountId)) {
					accountFound = 1;
					boolean valid = true;
					if (eStatementEnable.equalsIgnoreCase("1") && StringUtils.isBlank(email)) valid = false;
					if(valid) {
						String id = rec.getParamValueByName("id");
						flag = updateAccountinDbxDb(request, accountId, email, eStatementEnable,loginUserId,id);
						break;
					} 
					else {
						Result errorResult = new Result();
						CommonUtils.setOpStatusError(errorResult);
						CommonUtils.setErrMsg(errorResult, "Email required ");
						return errorResult;
					}
				}
				
			}
			//If not present create it
			 if(accountFound==0) {
					flag = createAccountinDbxDb(request, accountId, email, eStatementEnable, loginUserId);
				}
		}
		//If no Accounts are present for the user create it
		else {
			flag = createAccountinDbxDb(request, accountId, email, eStatementEnable, loginUserId);
		}
		if (flag != 0) {
			diagnostic.prepareDebug("DBXDB Accounts Updated with user Account Settings").log();
		     status = AccountsConstants.STATUS_SUCCESS;		     
		} else {
			diagnostic.prepareDebug("Couldnt Update DBXDB user Account Settings").log();
			status = AccountsConstants.STATUS_FAILED;
		}
		res1.addParam(AccountsConstants.PARAM_STATUS, status);
		res1.addParam(AccountsConstants.PARAM_EXTERNAL_ID, accountId);
		return res1;
	}

	private Result executePostValidation(DataControllerRequest request, HashMap<String, Object> params)
			throws Exception {

		Result res1 = new Result();

		String operationName = null;
		HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
		String eStatementEnable = CommonUtils.getParamValue(params, AccountsConstants.PARAM_ESTATEMENT_TOGGLE);
		String accountId = CommonUtils.getParamValue(params, AccountsConstants.ACCOUNTID);
		String email = CommonUtils.getParamValue(params, TemenosConstants.PARAM_EMAIL);
		request.addRequestParam_(AccountsConstants.PARAM_VALIDATE, "false");
		String serviceName = TemenosConstants.SERVICE_T24IS_ACCOUNTS;
		operationName = TemenosConstants.OP_NAME_UPDATE_ESTMT_PREF;

		res1 = CommonUtils.callIntegrationService(request, params, serviceHeaders, serviceName, operationName, true);
		String status1 = res1.getParamValueByName(AccountsConstants.PARAM_STATUS);
		String errMessage = res1.getParamValueByName(AccountsConstants.PARAM_ERROR_MESSAGE);

		if ((StringUtils.isNotBlank(status1) && StringUtils.equalsIgnoreCase(status1, AccountsConstants.STATUS_SUCCESS))
				|| (StringUtils.isNotEmpty(errMessage)
						&& StringUtils.equalsIgnoreCase("Record Not Changed", errMessage))) {
			String loginUserId = CommonUtils.getUserAttributeFromIdentity(request, "customer_id"); 
			HashMap<String, Object> inputParams = new HashMap<String, Object>();
			HashMap<String, Object> headerParams = new HashMap<String, Object>();
			inputParams.put("$filter", "Customer_id eq " + loginUserId);
			request.addRequestParam_("$filter", "Customer_id eq " + loginUserId);
			//Check if account is present in dbxdb
			Result readAccounts = CommonUtils.callIntegrationService(request, inputParams, headerParams,
					TemenosConstants.SERVICE_BACKEND_CERTIFICATE, TemenosConstants.OPERATION_CUSTOMER_ACCOUNTS_GET, true);
			Dataset getAccountsDS = readAccounts.getDatasetById(AccountsConstants.DS_CUSTOMERACCOUNTS);
			int flag = 0;
			int accountFound = 0;
			if (getAccountsDS != null && !getAccountsDS.getAllRecords().isEmpty()) {
				alert.prepareError("if true").log();
				for (Record rec : getAccountsDS.getAllRecords()) {
					//if account is present, update it
					if (StringUtils.equalsIgnoreCase(rec.getParamValueByName(AccountsConstants.DB_ACCOUNTID),
							accountId)) {
						accountFound = 1;
						if ((StringUtils.equals(eStatementEnable, "1") && StringUtils.isNotBlank(email)) || (StringUtils.equals(eStatementEnable, "0"))) {
							String id = rec.getParamValueByName("id");
							
							flag = updateAccountinDbxDb(request, accountId, email, eStatementEnable,loginUserId,id);
							break;
						} 
					}
					
				}
				//If not present create it
				 if(accountFound==0) {
						flag = createAccountinDbxDb(request, accountId, email, eStatementEnable, loginUserId);
					}
			}
			//If no Accounts are present for the user create it
			else {
				flag = createAccountinDbxDb(request, accountId, email, eStatementEnable, loginUserId);
			}
			if (flag != 0)
				diagnostic.prepareDebug("DBXDB Accounts Updated with user Account Settings").log();
			else
				diagnostic.prepareDebug("Couldnt Update DBXDB user Account Settings").log();

		}
        
		return res1;
		
	}
	
	/**
	 * 
	 * To  Create dbxdb accounts table with account record using account id, userId ,email and estatementEnable option
	 * @param DataControllerRequest request
	 * @param String accountId
	 * @param String email
	 * @param String eStatementEnable
	 * @param String customerId
	 * @return int successFlag
	 * @throws Exception
	 */
	private int createAccountinDbxDb(DataControllerRequest request, String accountId, String email,
			String eStatementEnable, String loginUserId) throws Exception {
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
		inputParams.put("id", UUID.randomUUID().toString());
		inputParams.put("Account_id", accountId);
		inputParams.put("EStatementmentEnable", eStatementEnable);
		inputParams.put("email", email);
		inputParams.put("Customer_id", loginUserId);
		String serviceName = TemenosConstants.SERVICE_BACKEND_CERTIFICATE;
		String operationName = TemenosConstants.OP_CUSTOMERACCOUNTS_CREATE;
		Result result = CommonUtils.callIntegrationService(request, inputParams, serviceHeaders, serviceName,
				operationName, false);
		String errMessage = result.getParamValueByName(AccountsConstants.PARAM_ERROR_MESSAGE);
		if (StringUtils.isNotBlank(errMessage)) {
			alert.prepareError("Couldn't create entry in dbxDb accounts Table due to : " + errMessage).log();
			return 0;
		} else
			return 1;

	}
	
	/**
	 * 
	 * To  update dbxdb accounts table with email and estatementEnable option
	 * @param DataControllerRequest request
	 * @param String accountId
	 * @param String email
	 * @param String eStatementEnable
	 * @param String customerId
	 * @param String primaryId
	 * @return int successFlag
	 * @throws Exception
	 */
	private int updateAccountinDbxDb(DataControllerRequest request, String accountId, String email,
			String eStatementEnable, String loginUserId,String primaryId) throws Exception {
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
		inputParams.put("Account_id", accountId);
		inputParams.put("EStatementmentEnable", eStatementEnable);
		inputParams.put("email", email);
		inputParams.put("Customer_id", loginUserId);
		inputParams.put("id", primaryId);
		String serviceName = TemenosConstants.SERVICE_BACKEND_CERTIFICATE;
		String operationName = TemenosConstants.OP_CUSTOMERACCOUNTS_UPDATE;
		Result result = CommonUtils.callIntegrationService(request, inputParams, serviceHeaders, serviceName,
				operationName, false);
		String errMessage = result.getParamValueByName(AccountsConstants.PARAM_ERROR_MESSAGE);
		if (StringUtils.isNotBlank(errMessage)) {
			alert.prepareError("Couldn't create entry in dbxDb accounts Table due to : " + errMessage).log();
			return 0;
		} else if (StringUtils.isNotEmpty(result.getParamValueByName("updatedRecords"))) {
			try {
				if (Integer.parseInt(result.getParamValueByName("updatedRecords")) > 0)
					return 1;
			} catch (Exception e) {
				diagnostic.prepareDebug("Couldn't Parse updated records Integer from String").log();
				return 1;
			}
		}
		

		return 0;

	}
	public String getValueIfExists(String key) {
		try {
			ServicesManager servicesManager = ServicesManagerHelper.getServicesManager();
			return EnvironmentConfigurationsHandler.getServerAppProperty(key, servicesManager);
		} catch (Exception e) {
			return "";
		}
	}

}