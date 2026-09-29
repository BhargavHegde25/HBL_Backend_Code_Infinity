package com.temenos.dbx.eum.product.usermanagement.resource.impl;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.sessionmanager.SessionTokenUtil;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.eum.dbputilities.customersecurityservices.CustomerLogin;
import com.kony.eum.dbputilities.customersecurityservices.GetCustomerPreferencesConcurrent;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CustomerIdentityAttributesBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.javaservice.CustomerGetByUserNameOperation;
import com.temenos.dbx.eum.product.usermanagement.resource.api.CustomerIdentityAttributesResource;
import com.temenos.dbx.product.dto.CustomerDTO;

public class CustomerIdentityAttributesResourceImpl implements CustomerIdentityAttributesResource {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Result getCustomerIdentityAttributes(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		final String INPUT_USERNAME = "userName";
		final String INPUT_USERID = "userId";
		Result result = new Result();
		try {
			Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
			String userName = StringUtils.isNotBlank(inputParams.get(INPUT_USERNAME)) ? inputParams.get(INPUT_USERNAME)
					: dcRequest.getParameter(INPUT_USERNAME);
			String userId = StringUtils.isNotBlank(inputParams.get(INPUT_USERID)) ? inputParams.get(INPUT_USERID)
					: dcRequest.getParameter(INPUT_USERID);
			if (StringUtils.isBlank(userName) && StringUtils.isBlank(userId)) {
				throw new ApplicationException(ErrorCodeEnum.ERR_10338);
			}

			CustomerIdentityAttributesBusinessDelegate identityAttributesBD = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(CustomerIdentityAttributesBusinessDelegate.class);
			CustomerDTO customerDTO = new CustomerDTO();
			customerDTO.setUserName(userName);
			customerDTO.setId(userId);

			JsonObject userAttributes = identityAttributesBD.getUserAttributes(customerDTO, dcRequest.getHeaderMap());
			JsonObject securityAttributes = identityAttributesBD.getSecurityAttributes(customerDTO,
					dcRequest.getHeaderMap());
			securityAttributes.addProperty("session_token", SessionTokenUtil.getNewSessionToken());
			JsonObject resultJson = new JsonObject();
			resultJson.add("user_attributes", userAttributes);
			resultJson.add("security_attributes", securityAttributes);
			result = JSONToResult.convert(resultJson.toString());

		} catch (ApplicationException e) {
			throw new ApplicationException(e.getErrorCodeEnum());
		} catch (Exception e) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10339);
		}
		return result;
	}

	@Override
	public Result getUserAttributes(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		try {
			String loggedInUserId = HelperMethods.getCustomerIdFromSession(dcRequest);

			CustomerDTO customerDTO = new CustomerDTO();
			customerDTO.setId(loggedInUserId);
			customerDTO = (CustomerDTO) customerDTO.loadDTO();

			Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
			inputParams.put("UserName", customerDTO.getUserName());
			inputArray[1] = inputParams;

			Result result = (Result) new CustomerGetByUserNameOperation().invoke(methodId, inputArray, dcRequest, null);
			return userAttributes(dcRequest, result, inputParams, methodId, dcResponse, inputArray);

		} catch (Exception e) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10820);
		}
	}

	private static Result userAttributes(DataControllerRequest dcRequest, Result result,
			Map<String, String> inputParams, String methodID, DataControllerResponse dcResponse, Object[] inputArray) {
		Result retVal = result;
		String Pin = "";
		Dataset ds = result.getDatasetById("customer");
		// Record sessionAttr = new Record();
		// sessionAttr.setId("security_attributes");

		Record usrAttr = ds.getRecord(0);

		usrAttr.setId(DBPUtilitiesConstants.USR_ATTR);
		usrAttr.addParam(new Param("customer_id", HelperMethods.getFieldValue(result, "id"), "String"));
		usrAttr.addParam(new Param("UserName", HelperMethods.getFieldValue(result, "UserName"), "String"));
		String fullName=HelperMethods.getFieldValue(result, "FullName");
		if(StringUtils.isNotBlank(fullName)) {
		usrAttr.addParam(new Param("FirstName", fullName, "String"));
		}
		// String token =
		// SessionManager.createSession(usrAttr.getParam("customer_id").getValue());

		Result cusComm = new Result();

		inputParams.put("id", HelperMethods.getFieldValue(result, "id"));
        String customer_id = HelperMethods.getFieldValue(result, "id");
		if (StringUtils.isNotBlank(HelperMethods.getDeviceId(dcRequest))) {
			usrAttr.addParam(new Param(DBPUtilitiesConstants.IS_DEVICE_REGISTERED, CustomerLogin
					.isDeviceRegistered(dcRequest, HelperMethods.getDeviceId(dcRequest), inputParams.get("id")) + ""));
		}

		cusComm = (Result) new GetCustomerPreferencesConcurrent().invoke(methodID, inputArray, dcRequest, dcResponse);

		diagnostic.prepareDebug("Response from CustomerPreferencesConcurrent : " + ResultToJSON.convert(cusComm)).log();
		for (Param param : cusComm.getAllParams()) {
			usrAttr.addParam(param);
		}

		usrAttr.addParam("userFirstName", usrAttr.getParamValueByName("FirstName"));
		usrAttr.addParam("userLastName", usrAttr.getParamValueByName("LastName"));
		usrAttr.addParam("gender", usrAttr.getParamValueByName("Gender"));
		usrAttr.addParam("isPinSet", usrAttr.getParamValueByName("IsPinSet"));
		usrAttr.addParam("noofdependents", usrAttr.getParamValueByName("NoOfDependents"));
		usrAttr.addParam("spousefirstname", usrAttr.getParamValueByName("SpouseName"));
		usrAttr.addParam("userImage", usrAttr.getParamValueByName("UserImage"));
		usrAttr.addParam("ssn", usrAttr.getParamValueByName("Ssn"));
		usrAttr.addParam("taxid", usrAttr.getParamValueByName("Ssn"));
		usrAttr.addParam("maritalstatus", usrAttr.getParamValueByName("MaritalStatus_id"));
		usrAttr.addParam("lastlogintime", usrAttr.getParamValueByName("Lastlogintime"));
		usrAttr.addParam("isCombinedUser", usrAttr.getParamValueByName("isCombinedUser"));
		usrAttr.addParam("organizationType", usrAttr.getParamValueByName("organizationType"));
		usrAttr.addParam(new Param("CSR_User_Id",
				StringUtils.isNotBlank(dcRequest.getAttribute("CSR_User_Id")) ? dcRequest.getAttribute("CSR_User_Id")
						: "",
				"String"));
		usrAttr.addParam(new Param("CSR_Role",
				StringUtils.isNotBlank(dcRequest.getAttribute("CSR_Role")) ? dcRequest.getAttribute("CSR_Role") : "",
				"String"));
		usrAttr.addParam(new Param("CSR_Name",
				StringUtils.isNotBlank(dcRequest.getAttribute("CSR_Name")) ? dcRequest.getAttribute("CSR_Name") : "",
				"String"));
		usrAttr.addParam(new Param("CSR_Username",
				StringUtils.isNotBlank(dcRequest.getAttribute("CSR_Username")) ? dcRequest.getAttribute("CSR_Username")
						: "",
				"String"));
		usrAttr.addParam(new Param("user_type",
				StringUtils.isNotBlank(dcRequest.getAttribute("CSRAssist_User_Type"))
						? dcRequest.getAttribute("CSRAssist_User_Type")
						: "",
				"String"));
		usrAttr.addParam(new Param("CustomerUsername",
				StringUtils.isNotBlank(dcRequest.getAttribute("CSRAssist_Customer_username"))
						? dcRequest.getAttribute("CSRAssist_Customer_username")
						: "",
				"String"));
		usrAttr.addParam(new Param("CustomerId",
				StringUtils.isNotBlank(dcRequest.getAttribute("CSRAssist_Customer_id"))
						? dcRequest.getAttribute("CSRAssist_Customer_id")
						: "",
				"String"));
		usrAttr.addParam(new Param("accountId",
				StringUtils.isNotBlank(dcRequest.getAttribute("accountId")) ? dcRequest.getAttribute("accountId") : "",
				"String"));
		usrAttr.addParam(new Param("customerTypeId",
				StringUtils.isNotBlank(HelperMethods.getFieldValue(result, "CustomerType_id"))
						? HelperMethods.getFieldValue(result, "CustomerType_id")
						: "",
				"String"));

//	        sessionAttr.addParam(new Param("permissions",
//	                StringUtils.isNotBlank(dcRequest.getAttribute("permissions")) ? dcRequest.getAttribute("permissions")
//	                        : "",
//	                "String"));
//
//	        sessionAttr.addParam(new Param("features",
//	                StringUtils.isNotBlank(dcRequest.getAttribute("features")) ? dcRequest.getAttribute("features") : "",
//	                "String"));

		String isCSRAssistMode = new String();
		if (StringUtils.isNotBlank(dcRequest.getParameter("isCSRAssistMode"))) {
			isCSRAssistMode = dcRequest.getParameter("isCSRAssistMode");
		}

		if (StringUtils.isBlank(isCSRAssistMode)) {
			usrAttr.addParam(new Param("isCSRAssistMode", "false", "String"));
		} else {
			usrAttr.addParam(new Param("isCSRAssistMode", "true", "String"));
		}

		try {
			usrAttr.addParam(new Param("Lastlogintime", HelperMethods.convertDateFormat(
					HelperMethods.getFieldValue(result, "CurrentLoginTime"), "yyyy-MM-dd'T'HH:mm:ss"), "String"));
		} catch (ParseException e) {

			alert.prepareError("Caught exception while converting DateFormat: ", e).log();
		}

		String filterQuery = "Customer_id" + DBPUtilitiesConstants.EQUAL
				+ HelperMethods.getFieldValue(usrAttr, "customer_id");
		Result backendIdentifiers = new Result();
		try {
			backendIdentifiers = HelperMethods.callGetApi(dcRequest, filterQuery, HelperMethods.getHeaders(dcRequest),
					URLConstants.BACKENDIDENTIFIER_GET);
		} catch (HttpCallException e) {

			alert.prepareError("Caught exception while getting Backend Identifiers: ", e).log();
		}

		Map<String, String> backend_identifiers = new HashMap<String, String>();
		if (HelperMethods.hasRecords(backendIdentifiers)) {
			backend_identifiers = getBackendIdentifiers(backendIdentifiers);
			usrAttr.addParam(new Param("backendIdentifiers", backend_identifiers.get("backendIdentifier"), "String"));
			if (StringUtils.isNotEmpty(backend_identifiers.get("companyId")))
				usrAttr.addParam(new Param("companyId", backend_identifiers.get("companyId"), "String"));
		}
		if (StringUtils.isBlank(usrAttr.getParamValueByName("companyId"))) {
			usrAttr.addParam(new Param("companyId",
					EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE)));
		}
		//adding missing fields
		usrAttr.addParam(new Param("organizationType",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("organizationType"))
						? usrAttr.getParamValueByName("organizationType")
						: "",
				"String"));
		
		usrAttr.addParam(new Param("Location_id",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("Location_id"))
						? usrAttr.getParamValueByName("Location_id")
						: "",
				"String"));		
		usrAttr.addParam(new Param("country",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("country"))
						? usrAttr.getParamValueByName("country")
						: "",
				"String"));		
		usrAttr.addParam(new Param("taxId",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("taxId"))
						? usrAttr.getParamValueByName("taxId")
						: "",
				"String"));		
		usrAttr.addParam(new Param("default_account_payments",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("default_account_payments"))
						? usrAttr.getParamValueByName("default_account_payments")
						: "",
				"String"));		
		usrAttr.addParam(new Param("Organization_Id",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("Organization_Id"))
						? usrAttr.getParamValueByName("Organization_Id")
						: "",
				"String"));		
		usrAttr.addParam(new Param("CountryCode",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("CountryCode"))
						? usrAttr.getParamValueByName("CountryCode")
						: "",
				"String"));		
		usrAttr.addParam(new Param("areDepositTermsAccepted",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("areDepositTermsAccepted"))
						? usrAttr.getParamValueByName("areDepositTermsAccepted")
						: "",
				"String"));		
		usrAttr.addParam(new Param("MaritalStatus_id",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("MaritalStatus_id"))
						? usrAttr.getParamValueByName("MaritalStatus_id")
						: "",
				"String"));		
		usrAttr.addParam(new Param("Is_MemberEligibile",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("Is_MemberEligibile"))
						? usrAttr.getParamValueByName("Is_MemberEligibile")
						: "",
				"String"));		
		usrAttr.addParam(new Param("OlbEnrolmentStatus_id",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("OlbEnrolmentStatus_id"))
						? usrAttr.getParamValueByName("OlbEnrolmentStatus_id")
						: "",
				"String"));		
		usrAttr.addParam(new Param("RegistrationLink",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("RegistrationLink"))
						? usrAttr.getParamValueByName("RegistrationLink")
						: "",
				"String"));		
		usrAttr.addParam(new Param("default_account_transfers",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("default_account_transfers"))
						? usrAttr.getParamValueByName("default_account_transfers")
						: "",
				"String"));		
		usrAttr.addParam(new Param("default_account_deposit",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("default_account_deposit"))
						? usrAttr.getParamValueByName("default_account_deposit")
						: "",
				"String"));		
		usrAttr.addParam(new Param("IDExpiryDate",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("IDExpiryDate"))
						? usrAttr.getParamValueByName("IDExpiryDate")
						: "",
				"String"));		
		usrAttr.addParam(new Param("Is_BBOA",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("Is_BBOA"))
						? usrAttr.getParamValueByName("Is_BBOA")
						: "",
				"String"));		
		usrAttr.addParam(new Param("IDType_id",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("IDType_id"))
						? usrAttr.getParamValueByName("IDType_id")
						: "",
				"String"));		
		usrAttr.addParam(new Param("UserImageURL",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("UserImageURL"))
						? usrAttr.getParamValueByName("UserImageURL")
						: "",
				"String"));		
		usrAttr.addParam(new Param("MemberEligibilityData",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("MemberEligibilityData"))
						? usrAttr.getParamValueByName("MemberEligibilityData")
						: "",
				"String"));		
		usrAttr.addParam(new Param("UserImage",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("UserImage"))
						? usrAttr.getParamValueByName("UserImage")
						: "",
				"String"));		
		usrAttr.addParam(new Param("Salutation",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("Salutation"))
						? usrAttr.getParamValueByName("Salutation")
						: "",
				"String"));		
		usrAttr.addParam(new Param("countrycode",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("countrycode"))
						? usrAttr.getParamValueByName("countrycode")
						: "",
				"String"));		
		usrAttr.addParam(new Param("unsuccessfulLoginAttempts",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("unsuccessfulLoginAttempts"))
						? usrAttr.getParamValueByName("unsuccessfulLoginAttempts")
						: "",
				"String"));		
		usrAttr.addParam(new Param("RegLinkValidity",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("RegLinkValidity"))
						? usrAttr.getParamValueByName("RegLinkValidity")
						: "",
				"String"));		
		usrAttr.addParam(new Param("areAccountStatementTermsAccepted",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("areAccountStatementTermsAccepted"))
						? usrAttr.getParamValueByName("areAccountStatementTermsAccepted")
						: "",
				"String"));		
		usrAttr.addParam(new Param("CustomerType",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("CustomerType"))
						? usrAttr.getParamValueByName("CustomerType")
						: "",
				"String"));		
		usrAttr.addParam(new Param("IDValue",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("IDValue"))
						? usrAttr.getParamValueByName("IDValue")
						: "",
				"String"));		
		usrAttr.addParam(new Param("SpouseName",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("SpouseName"))
						? usrAttr.getParamValueByName("SpouseName")
						: "",
				"String"));		
		usrAttr.addParam(new Param("AtionProfile_id",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("AtionProfile_id"))
						? usrAttr.getParamValueByName("AtionProfile_id")
						: "",
				"String"));		
		usrAttr.addParam(new Param("IDIssueDate",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("IDIssueDate"))
						? usrAttr.getParamValueByName("IDIssueDate")
						: "",
				"String"));		
		usrAttr.addParam(new Param("RegLinkResendCount",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("RegLinkResendCount"))
						? usrAttr.getParamValueByName("RegLinkResendCount")
						: "",
				"String"));		
		usrAttr.addParam(new Param("IDState",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("IDState"))
						? usrAttr.getParamValueByName("IDState")
						: "",
				"String"));		

		usrAttr.addParam(new Param("SFDC_accountId",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("SFDC_accountId"))
						? usrAttr.getParamValueByName("SFDC_accountId")
						: "",
				"String"));		
		usrAttr.addParam(new Param("CreditUnionMemberSince",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("CreditUnionMemberSince"))
						? usrAttr.getParamValueByName("CreditUnionMemberSince")
						: "",
				"String"));		
		usrAttr.addParam(new Param("phoneCountryCode",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("phoneCountryCode"))
						? usrAttr.getParamValueByName("phoneCountryCode")
						: "",
				"String"));		
		usrAttr.addParam(new Param("Role",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("Role"))
						? usrAttr.getParamValueByName("Role")
						: "",
				"String"));		
		usrAttr.addParam(new Param("IsCoreIdentityScope",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("IsCoreIdentityScope"))
						? usrAttr.getParamValueByName("IsCoreIdentityScope")
						: "",
				"String"));		
		usrAttr.addParam(new Param("isDeviceRegistered",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("isDeviceRegistered"))
						? usrAttr.getParamValueByName("isDeviceRegistered")
						: "",
				"String"));		
		usrAttr.addParam(new Param("IDCountry",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("IDCountry"))
						? usrAttr.getParamValueByName("IDCountry")
						: "",
				"String"));		
		usrAttr.addParam(new Param("SecurityImage_id",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("SecurityImage_id"))
						? usrAttr.getParamValueByName("SecurityImage_id")
						: "",
				"String"));		
		usrAttr.addParam(new Param("sbaEnrolmentStatus",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("sbaEnrolmentStatus"))
						? usrAttr.getParamValueByName("sbaEnrolmentStatus")
						: "",
				"String"));		
		usrAttr.addParam(new Param("FullName",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("FullName"))
						? usrAttr.getParamValueByName("FullName")
						: "",
				"String"));		
		usrAttr.addParam(new Param("MiddleName",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("MiddleName"))
						? usrAttr.getParamValueByName("MiddleName")
						: "",
				"String"));		
		
		usrAttr.addParam(new Param("AddressLine2",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("AddressLine2"))
						? usrAttr.getParamValueByName("AddressLine2")
						: "",
				"String"));		
		usrAttr.addParam(new Param("AddressLine1",
				StringUtils.isNotBlank(usrAttr.getParamValueByName("AddressLine1"))
						? usrAttr.getParamValueByName("AddressLine1")
						: "",
				"String"));		
		
        
		usrAttr.addParam("user_id", customer_id);
		usrAttr.addParam("customer_id", customer_id);
		usrAttr.addParam("email", usrAttr.getParamValueByName("Email"));
		usrAttr.addParam("state", usrAttr.getParamValueByName("Region_id"));
		usrAttr.addParam("ISDCode", usrAttr.getParamValueByName("isdcode"));
		usrAttr.addParam("last_name", usrAttr.getParamValueByName("LastName"));
		usrAttr.addParam("legalEntityId", usrAttr.getParamValueByName("companyLegalUnit"));

		
		  boolean eSignAgreementRequired = false;
          boolean isEagreementSigned =  "true".equalsIgnoreCase(HelperMethods.getFieldValue(usrAttr, "isEagreementSigned"));
          
          if (!isEagreementSigned && HelperMethods.isBusinessUserType(usrAttr.getParamValueByName("CustomerType_id"))) {
              try {
				eSignAgreementRequired = isUserEsignAgreementReq(usrAttr, dcRequest);
			} catch (HttpCallException e) {
				alert.prepareError("Caught exception for eSignAgreementRequired: ", e).log();
			}
          }

		usrAttr.addParam(new Param("isEAgreementRequired", "" + eSignAgreementRequired, "String"));
		usrAttr.addParam(new Param("isEagreementSigned", "" + isEagreementSigned, "String"));

	
           
        usrAttr.removeParamByName("softdeleteflag");
   		usrAttr.removeParamByName("lastmodifiedts");
   		usrAttr.removeParamByName("Session_id");
   		usrAttr.removeParamByName("customerTypeId");	
   		usrAttr.removeParamByName("showBillPayFromAccPopup");
   		usrAttr.removeParamByName("isEnrolledFromSpotlight");	
   		usrAttr.removeParamByName("IsEnrolledForOlb");
   		usrAttr.removeParamByName("Password");

   		usrAttr.removeParamByName("synctimestamp");
   		usrAttr.removeParamByName("lockCount");
   		usrAttr.removeParamByName("isHeavyUser");
   		usrAttr.removeParamByName("isChanged");	
   		usrAttr.removeParamByName("userLastName");
   		usrAttr.removeParamByName("City_id");	
   		usrAttr.removeParamByName("isSignatory");
   		usrAttr.removeParamByName("addressLine2");
   		usrAttr.removeParamByName("isNew");
   		usrAttr.removeParamByName("companyLegalUnit");
   		usrAttr.removeParamByName("createdts");
   		usrAttr.removeParamByName("userFirstName");	
   		usrAttr.removeParamByName("id");
   		
   		Pin = usrAttr.getParamValueByName("Pin");
   		diagnostic.prepareDebug("Customer Pin ##: " + Pin).log();
   		if(StringUtils.isNotBlank(Pin)) {
   			diagnostic.prepareDebug("inside Pin ##: " + Pin).log();
   			usrAttr.addParam("Pin", maskSSN("Pin"));
   		}else {
   			diagnostic.prepareDebug("inside else Pin ##: " + Pin).log();
   		  usrAttr.removeParamByName("Pin");
   		}
   		
		retVal.addRecord(usrAttr);
		retVal.removeDatasetById("customer");
		diagnostic.prepareDebug("Response from userAttributes : " + ResultToJSON.convert(retVal)).log();
		return retVal;
	}
	
	public static String maskSSN(String value) {
        if (StringUtils.isBlank(value))
            return "";
        StringBuilder sb = new StringBuilder();
        sb.append("******");
        return sb.toString();
    }
	
	private static Map<String, String> getBackendIdentifiers(Result backendIdentifiers) {

		Map<String, String> backend_identifiers = new HashMap<String, String>();
		String returnCompanyId = "";
		List<Record> identifiers = backendIdentifiers.getAllDatasets().get(0).getAllRecords();

		JSONObject json = new JSONObject();

		for (Record record : identifiers) {

			String backendType = HelperMethods.getFieldValue(record, "BackendType");

			Map<String, String> map = new HashMap<>();
			map.put("sequence_number", String.valueOf(HelperMethods.getFieldValue(record, "sequenceNumber")));
			map.put("BackendId", HelperMethods.getFieldValue(record, "BackendId"));
			map.put("identifier_name", HelperMethods.getFieldValue(record, "identifier_name"));
			String companyId = HelperMethods.getFieldValue(record, "CompanyId");
			map.put("CompanyId", companyId);
			map.put("contractId", HelperMethods.getFieldValue(record, "contractId"));
			map.put("contractTypeId", HelperMethods.getFieldValue(record, "contractTypeId"));
			if (StringUtils.isNotEmpty(companyId) && StringUtils.isEmpty(returnCompanyId))
				returnCompanyId = companyId;

			if (json.has(backendType)) {

				JSONArray value = json.getJSONArray(backendType);
				value.put(map);
			} else {

				JSONArray value = new JSONArray();
				value.put(map);
				json.put(backendType, value);
			}
		}
		backend_identifiers.put("backendIdentifier", String.valueOf(json));
		backend_identifiers.put("companyId", returnCompanyId);
		return backend_identifiers;
	}
	
	  private static boolean isUserEsignAgreementReq(Record user, DataControllerRequest dcRequest) throws HttpCallException {

	        Map<String, String> input = new HashMap<>();
	        input.put("_customerId", HelperMethods.getFieldValue(user, "id"));

	        JsonObject response = HelperMethods.callApiJson(dcRequest, input, HelperMethods.getHeaders(dcRequest),
	                URLConstants.CUSTOMER_EAGREEMENT_GET);
	        if (!response.has("opstatus") || response.get("opstatus").getAsInt() != 0 || !response.has("records")) {
	            alert.prepareError("Exception occured while fetching the customer eagreement status").log();
	            return false;
	        }
	        if (response.has("records") && response.get("records").getAsJsonArray().size() != 0
	                && response.get("records").getAsJsonArray().get(0) != null && response.get("records").getAsJsonArray()
	                        .get(0).getAsJsonObject().get("isEAgreementActive") != null) {
	            return response.get("records").getAsJsonArray().get(0).getAsJsonObject().get("isEAgreementActive")
	                    .getAsBoolean();
	        }

	        return false;
	    }

}
