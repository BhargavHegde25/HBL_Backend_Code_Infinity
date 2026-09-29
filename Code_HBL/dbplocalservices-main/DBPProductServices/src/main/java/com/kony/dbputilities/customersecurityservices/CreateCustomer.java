package com.kony.dbputilities.customersecurityservices;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
//import org.omg.CORBA.portable.ApplicationException;

import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.BCrypt;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.utils.ThreadExecutor;

public class CreateCustomer implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {

		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		inputParams.put("doNotSendOTP", "true");

		Result result1 = (Result) new VerifyDBXUserName().invoke(methodID, inputArray, dcRequest, dcResponse);
		Record record = result1.getRecordById(DBPUtilitiesConstants.USR_ATTR);

		if ((record != null)
				&& (HelperMethods.getFieldValue(record, DBPUtilitiesConstants.IS_USERNAME_EXISTS).equals("true"))) {
			ErrorCodeEnum.ERR_10041.setErrorCode(result);
			return result;
		}

		if (preProcess(inputParams, dcRequest, result)) {
			String memberId = inputParams.get("MemberId");
			if (StringUtils.isBlank(memberId)) {
				memberId = inputParams.get("membershipID");
			}
			inputParams.put("Membership_id", memberId);
			result1 = new Result();
			PasswordHistoryManagement pm = new PasswordHistoryManagement(dcRequest, false);
			if (StringUtils.isNotBlank(memberId)) {
				result1 = (Result) new GetMembershipOwnerDetails().invoke(methodID, inputArray, dcRequest, dcResponse);
				if (HelperMethods.hasRecords(result1)) {
					String id = HelperMethods.getFieldValue(result1, "Customer_id");
					String filter = "id" + DBPUtilitiesConstants.EQUAL + id + DBPUtilitiesConstants.AND + "UserName"
							+ DBPUtilitiesConstants.EQUAL + id;
					result1 = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
							URLConstants.CUSTOMERVERIFY_GET);
					if (HelperMethods.hasRecords(result1)
							&& "SID_CUS_NEW".equalsIgnoreCase(HelperMethods.getFieldValue(result1, "Status_id"))
							&& StringUtils.isBlank(HelperMethods.getFieldValue(result1, "Password"))) {
						HelperMethods.removeNullValues(inputParams);
						inputParams.put("id", id);
						inputParams.put("isEnrolled", "true");
						result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
								URLConstants.CUSTOMER_UPDATE);
						if (HelperMethods.hasRecords(result)) {
							Result retResult = new Result();
							HelperMethods.setSuccessMsg(DBPUtilitiesConstants.SUCCESS_MSG, retResult);
							CreateCustomerPreference.invoke(inputParams, dcRequest);
							retResult.addParam(new Param("id", inputParams.get("id"), "String"));
							retResult.addParam(new Param("redirectLink",
									URLFinder.getPathUrl(URLConstants.REDIRECTLINK, dcRequest), "String"));

							ThreadExecutor.getExecutor(dcRequest).execute(new Callable<Result>() {
								@Override
								public Result call() throws ApplicationException {
									Map<String, String> postParamMapGroup = new HashMap<>();
									postParamMapGroup.put("Customer_id", inputParams.get("id"));
									postParamMapGroup.put("Group_id", "DEFAULT_GROUP");
									try {
										return HelperMethods.callApi(dcRequest, postParamMapGroup,
												HelperMethods.getHeaders(dcRequest),
												URLConstants.CUSTOMER_GROUP_CREATE);
									} catch (HttpCallException e) {
										alert.prepareError(e.getMessage()).log();
									}

									return new Result();
								}

							});

							pm.makePasswordEntry(dcRequest, inputParams.get("id"), inputParams.get("Password"));
							return retResult;
						}
					}
				}
			}
		}
		return result;
	}

	private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result) {
		boolean status = true;

		String id = String.valueOf(HelperMethods.getNumericId());
		inputParams.put("id", id);

		String username = inputParams.get("UserName");
		String password = inputParams.get("Password");
		inputParams.put("inputPassword", password);

		if (StringUtils.isBlank(username)) {
			ErrorCodeEnum.ERR_10042.setErrorCode(result);
			return false;
		}
		if (StringUtils.isNotBlank(password)) {
			inputParams.put("Status_id", "SID_CUS_ACTIVE");
			String salt = BCrypt.gensalt(DBPUtilitiesConstants.SALT_NUMBER_OF_ROUNDS);
			String hashedPassword = BCrypt.hashpw(password, salt);
			inputParams.put("Password", hashedPassword);
		} else {
			inputParams.put("Status_id", "SID_CUS_NEW");
		}

		inputParams.put("CustomerType_id", "TYPE_ID_RETAIL");

		inputParams.put("FirstName", inputParams.get("LastName"));
		SimpleDateFormat idformatter = new SimpleDateFormat("yyMMddHHmmssSSS");

		inputParams.put("id", idformatter.format(new Date()));

		String role = inputParams.get(DBPUtilitiesConstants.ROLE);
		if (!StringUtils.isNotBlank(role)) {
			inputParams.put("Role", "BASIC");
		}
		inputParams.put("Bank_id", "1");
		HelperMethods.removeNullValues(inputParams);
		return status;
	}

}