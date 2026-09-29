package com.kony.dbx.identity;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class LoginPostProcessor implements DataPostProcessor2 {

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public Result execute(Result result, DataControllerRequest request, DataControllerResponse response) throws Exception {

		try	{

			// Grab the raw response & convert to JSON Object
			String rawResponse = response.getResponse();
			JSONObject jsonObj = new JSONObject(rawResponse);

			// Debug logging
			diagnostic.prepareDebug("Received JSON: " + jsonObj.toString()).log();

			
			// Setup to extract JSON
			JSONObject secAttribs;
			JSONObject userAttribs;
			String sessToken = "";
			String user_id = "";
			String customerId = "";
			String userName = "";
			String firstName = "";
			String lastName = "";
			String email = "";
			String phone = "";
			String dob = "";
			String ssn = "";
			String isEnrolled = "";
			String isNewBrowser = "";
			String lastLogin = "";
			String defAcctDeposit = "";
			String defAcctPayments = "";
			String defAcctTransfers = "";

			
			// Grab required JSON Objects
			if (jsonObj.has("security_attributes")) {
				secAttribs = jsonObj.getJSONObject("security_attributes");
				sessToken = secAttribs.getString("session_token");
			}			
			if (jsonObj.has("user_attributes")) {
				userAttribs = jsonObj.getJSONObject("user_attributes");
				user_id = userAttribs.getString("user_id"); 
				customerId = userAttribs.getString("ssn");
				userName = userAttribs.getString("userName");
				firstName = userAttribs.getString("userfirstname");
				lastName = userAttribs.getString("userlastname");
				email = userAttribs.getString("email");
				phone = userAttribs.getString("phone");
				dob = userAttribs.getString("dateOfBirth");
				ssn = userAttribs.getString("ssn");
				isEnrolled = String.valueOf(userAttribs.getBoolean("isEnrolled"));
				isNewBrowser = String.valueOf(userAttribs.getBoolean("isNewBrowser"));
				lastLogin = userAttribs.getString("lastlogintime");
				defAcctDeposit = userAttribs.getString("default_account_deposit");
				defAcctPayments = userAttribs.getString("default_account_payments");
				defAcctTransfers = userAttribs.getString("default_account_transfers");
			}
			
			// Create required parameters
			Param sessionParam = new Param("session_token", sessToken, Constants.PARAM_DATATYPE_STRING);
			Param userIdParam = new Param("user_id", user_id, Constants.PARAM_DATATYPE_STRING);
			Param custIdParam = new Param("customer_id", customerId, Constants.PARAM_DATATYPE_STRING);
			Param userNameParam = new Param("userName", userName, Constants.PARAM_DATATYPE_STRING);
			Param firstNameParam = new Param("userfirstname", firstName, Constants.PARAM_DATATYPE_STRING);
			Param lastNameParam = new Param("userlastname", lastName, Constants.PARAM_DATATYPE_STRING);
			Param emailParam = new Param("email", email, Constants.PARAM_DATATYPE_STRING);
			Param phoneParam = new Param("phone", phone, Constants.PARAM_DATATYPE_STRING);
			Param dobParam = new Param("dateOfBirth", dob, Constants.PARAM_DATATYPE_STRING);
			Param ssnParam = new Param("ssn", ssn, Constants.PARAM_DATATYPE_STRING);
			Param isEnrolledParam = new Param("isEnrolled", isEnrolled, Constants.PARAM_DATATYPE_STRING);
			Param isNewBrowserParam = new Param("isNewBrowser", isNewBrowser, Constants.PARAM_DATATYPE_STRING);
			Param lastLoginParam = new Param("lastlogintime", lastLogin, Constants.PARAM_DATATYPE_STRING);
			Param defAcctDepositParam = new Param("default_account_deposit", defAcctDeposit, Constants.PARAM_DATATYPE_STRING);
			Param defAcctPaymentsParam = new Param("default_account_payments", defAcctPayments, Constants.PARAM_DATATYPE_STRING);
			Param defAcctTransfersParam = new Param("default_account_transfers", defAcctTransfers, Constants.PARAM_DATATYPE_STRING);
			
			
			// Create user_attributes record
			Record userAttribsRec = new Record();
			userAttribsRec.setId("user_attributes");
			userAttribsRec.addParam(userIdParam);
			userAttribsRec.addParam(custIdParam);		
			userAttribsRec.addParam(userNameParam);
			userAttribsRec.addParam(firstNameParam);
			userAttribsRec.addParam(lastNameParam);
			userAttribsRec.addParam(phoneParam);
			userAttribsRec.addParam(dobParam);
			userAttribsRec.addParam(ssnParam);
			userAttribsRec.addParam(emailParam);
			userAttribsRec.addParam(defAcctDepositParam);
			userAttribsRec.addParam(defAcctPaymentsParam);
			userAttribsRec.addParam(defAcctTransfersParam);
			userAttribsRec.addParam(isEnrolledParam);
			userAttribsRec.addParam(isNewBrowserParam);
			userAttribsRec.addParam(lastLoginParam);
			

			// Create security_attributes record
			Record secAttribsRec = new Record();
			secAttribsRec.setId("security_attributes");
			secAttribsRec.addParam(sessionParam);
			
			// Populate the result
			Result newResult = new Result();
			newResult.addRecord(userAttribsRec);
			newResult.addRecord(secAttribsRec);
			newResult.addParam(new Param(Constants.PARAM_OP_STATUS, 
				Constants.PARAM_OP_STATUS_OK, Constants.PARAM_DATATYPE_INT));
			
			return newResult;			
		}
		catch (Exception e) {
			String msg = CommonUtils.createExceptiontext(e);
			Result errorResult = new Result();
			CommonUtils.setErrMsg(errorResult, msg);
			CommonUtils.setOpStatusError(errorResult);
			return errorResult;
		}
	}
}
