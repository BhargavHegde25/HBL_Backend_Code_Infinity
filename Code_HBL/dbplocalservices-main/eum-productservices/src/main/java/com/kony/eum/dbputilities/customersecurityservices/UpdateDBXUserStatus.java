package com.kony.eum.dbputilities.customersecurityservices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.utils.CustomerSessionsUtil;

public class UpdateDBXUserStatus implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        StringBuilder validEntityList = new StringBuilder();
        String customerId;
        String status = inputParams.get("Status");
		if(!validateInputForCustomerStatusUpdate(inputParams, validEntityList, dcRequest, result)) {
			return result;
		}
        if (preProcess(inputParams, dcRequest, result)) {
            checkStatusIfActive(dcRequest, inputParams);
            putStatusId(inputParams);
            String validLegalEntities = validEntityList.toString(); 
			validLegalEntities = validLegalEntities.substring(0, validLegalEntities.length() - 1);
            inputParams.put("legalEntityList",validLegalEntities);
            inputParams.put("isOLB","false");
            HelperMethods.removeNullValues(inputParams);
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.UPDATE_USERSTATUS_BY_LEGALENTITY_PROC);
            customerId = inputParams.get("customerId");
            String userName = inputParams.get("UserName"); 
            if(status.equals("SUSPENDED")) {
            	updateDefaultLegalEntity(customerId, validLegalEntities, dcRequest);
				Result response = CustomerSessionsUtil.deleteActiveUserSessionsIfAny(customerId, userName, dcRequest);
				if (response.hasParamByName("errmsg") && response.getErrMsgParamValue() != null) {
					result.addParam(response.getParamByName("errmsg"));
					return result;
				}
            }
        } else {
        	return result;
        }
		updateCustomerStatusByLegalEntity(customerId, result, dcRequest);
        return postProcess(result, inputParams);

    }

    private void checkStatusIfActive(DataControllerRequest dcRequest, Map<String, String> inputParams)
            throws HttpCallException {
        String status = inputParams.get("Status");
        if ("ACTIVE".equalsIgnoreCase(status)) {
            String filter = "UserName" + DBPUtilitiesConstants.EQUAL + "'" + inputParams.get("UserName") + "'";
            Result result = new Result();
            result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CUSTOMER_GET);
            if (StringUtils.isBlank(HelperMethods.getFieldValue(result, "Password"))) {
                inputParams.put("statusId", "NEW");
            }
            return;
        }
        return;
    }

    private void putStatusId(Map<String, String> inputParams) throws HttpCallException {
        String status = inputParams.get("Status");
        Map<String, String> map = HelperMethods.getCustomerStatus();
        if (map.containsKey(status)) {
            inputParams.put("statusId", map.get(status));
        }
    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
            throws Exception {
    	String userName = inputParams.get("UserName");
        boolean check = true;
        String id = null;
        Result userRec = new Result();
        userRec = HelperMethods.getUserRecordByName(userName, dcRequest);
        id = HelperMethods.getFieldValue(userRec, "id");
        if(StringUtils.isBlank(id)) {
        	ErrorCodeEnum.ERR_10192.setErrorCode(result);
        	return false;
        }
        if (StringUtils.isNotBlank(id)) {
            inputParams.put("customerId", id);
            check = true;
        }

        if (("ACTIVE".equalsIgnoreCase(inputParams.get("Status")))
                || ("SUSPENDED".equalsIgnoreCase(inputParams.get("Status")))
                || "INACTIVE".equalsIgnoreCase(inputParams.get("Status"))) {
            check = true;
        } else {
            check = false;
        }
        return check;
    }

    private Result postProcess(Result result, Map<String, String> inputParams) {

        Result retResult = new Result();

        if (HelperMethods.hasRecords(result)) {
            // Param p = new Param("errorCode", "3400", DBPUtilitiesConstants.STRING_TYPE);
            // retResult.addParam(p);

            Param p = new Param("Status", inputParams.get("Status"), DBPConstants.FABRIC_STRING_CONSTANT_KEY);
            retResult.addParam(p);
            p = new Param("success", "User Status updated.", DBPConstants.FABRIC_STRING_CONSTANT_KEY);
            retResult.addParam(p);
        } else if (HelperMethods.hasError(result)) {
            // Param p = new Param("errorCode", "3401", DBPUtilitiesConstants.STRING_TYPE);
            // retResult.addParam(p);
            // p = new Param("errorMessage",HelperMethods.getError(result),DBPConstants.FABRIC_STRING_CONSTANT_KEY);
            // retResult.addParam(p);
            ErrorCodeEnum.ERR_10017.setErrorCode(retResult, HelperMethods.getError(result));
        } else {
            // Param p = new Param("errorCode", "3402", DBPUtilitiesConstants.STRING_TYPE);
            // retResult.addParam(p);
            // p = new Param("success","Records doesn't exists",DBPConstants.FABRIC_STRING_CONSTANT_KEY);
            // retResult.addParam(p);
            //
            // HelperMethods.setValidationMsg(HelperMethods.getError(result),retResult);
            ErrorCodeEnum.ERR_10018.setErrorCode(retResult);
        }

        return retResult;
	}

	private boolean validateInputForCustomerStatusUpdate(Map<String, String> inputParams,StringBuilder validLegalEntities,
			DataControllerRequest dcRequest, Result result) {
		String userName = inputParams.get("UserName");
		String status = inputParams.get("Status");
		String legalEntityIdList = inputParams.get("legalEntityList");
		JsonArray legalEntityIdJsonArray = null;
		try {
			legalEntityIdJsonArray = new JsonParser().parse(legalEntityIdList).getAsJsonArray();
		} catch (Exception e) {
			alert.prepareError("Json Validation Exception").log();
			ErrorCodeEnum.ERR_10050.setErrorCode(result);
			return false;
		}
		String regex = "[A-Za-z0-9\\s]{1,51}$";
		if (StringUtils.isBlank(userName)) {
			ErrorCodeEnum.ERR_10204.setErrorCode(result, "Mandatory field UserName cannot be Empty!");
			return false;
		}
		if (StringUtils.isBlank(legalEntityIdList)) {
			ErrorCodeEnum.ERR_10204.setErrorCode(result, "Mandatory field Legal Entity ID's cannot be Empty!");
			return false;
		}
		if (StringUtils.isBlank(status)) {
			ErrorCodeEnum.ERR_10204.setErrorCode(result, "Mandatory field Status cannot be Empty!");
			return false;
		}
		if (!validateRegex(regex, userName)) {
			ErrorCodeEnum.ERR_10333.setErrorCode(result, "UserName has Invalid Characters!");
			return false;
		}
		for (int i = 0; i < legalEntityIdJsonArray.size(); i++) {
			String legalEntityId = legalEntityIdJsonArray.get(i).getAsString();
			if (!validateRegex(regex, legalEntityId)) {
				ErrorCodeEnum.ERR_10333.setErrorCode(result, legalEntityId + " has Invalid Characters!");
				return false;
			} else {
				validLegalEntities.append(legalEntityId + DBPUtilitiesConstants.COMMA_SEPERATOR);
			}
		}
		Set<String> validStatus = HelperMethods.getCustomerStatus().keySet();
		if (!validStatus.contains(status)) {
			ErrorCodeEnum.ERR_10001.setErrorCode(result, "Invalid Status Id!!");
			return false;
		}
		return true;
	}
	private boolean validateRegex(String regex, String string) {
		string.trim();
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(string);
		return m.matches();
	}

	private void updateCustomerStatusByLegalEntity(String customerId, Result result, DataControllerRequest dcRequest)
			throws HttpCallException, ApplicationException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("customerId", customerId);
		JsonObject response = HelperMethods.callApiJson(dcRequest, params, HelperMethods.getHeaders(dcRequest),
				URLConstants.UPDATE_CUSTOMERSTATUS_DEFAULT_LEGALENTITY);
		boolean success = JSONUtil.hasKey(response, MWConstants.OPSTATUS)
				&& JSONUtil.getString(response, MWConstants.OPSTATUS).equalsIgnoreCase("0");
		if (!success) {
			throw new ApplicationException(ErrorCodeEnum.ERR_14008);
		}
	}
	private void updateDefaultLegalEntity(String customerId, String validLegalEntities, DataControllerRequest dcRequest ) throws HttpCallException{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customerId", customerId);
		params.put("legalEntityList", validLegalEntities);
		ServiceCallHelper.invokeServiceAndGetJson(dcRequest, params, null, URLConstants.UPDATE_DEFAULT_LEGALENTITY);
		//HelperMethods.callApi(dcRequest, params, HelperMethods.getHeaders(dcRequest),URLConstants.UPDATE_DEFAULT_LEGALENTITY);
	}
}