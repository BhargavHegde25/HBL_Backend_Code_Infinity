package com.temenos.dbx.eum.product.usermanagement.backenddelegate.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.constants.DBPConstants;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infinity.dbx.dbp.jwt.auth.AuthConstants;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.util.BundleConfigurationHandler;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPInputConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.IntegrationTemplateURLFinder;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbputilities.util.OperationName;
import com.kony.dbputilities.util.ServiceId;
import com.kony.dbputilities.util.StatusEnum;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.kony.eum.dbputilities.customersecurityservices.PasswordHistoryManagement;
import com.kony.eum.dbputilities.kms.KMSUtil;
import com.kony.eum.dbputilities.util.ServiceCallHelper;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.exceptions.MiddlewareException;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.CoreCustomerBackendDelegate;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.CoreCustomerBusinessDelegate;
import com.temenos.dbx.eum.product.contract.businessdelegate.impl.CoreCustomerBusinessDelegateImpl;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.BackendIdentifiersBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.ProfileManagementBackendDelegate;
import com.temenos.dbx.product.businessdelegate.api.SystemConfigurationBusinessDelegate;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.dto.CredentialCheckerDTO;
import com.temenos.dbx.product.dto.CustomerAddressDTO;
import com.temenos.dbx.product.dto.CustomerCommunicationDTO;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.CustomerGroupDTO;
import com.temenos.dbx.product.dto.CustomerLegalEntityDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.MemberSearchBean;
import com.temenos.dbx.product.dto.MembershipDTO;
import com.temenos.dbx.product.dto.PasswordLockoutSettingsDTO;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.CommunicationBackendDelegate;
import com.temenos.dbx.product.usermanagement.backenddelegate.impl.BackendIdentifiersBackendDelegateimpl;
import com.temenos.dbx.product.usermanagement.backenddelegate.impl.CommunicationBackendDelegateImpl;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.impl.ProfileManagementBackendDelegateImpl;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.BackendIdentifierBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CustomerGroupBusinessDelegate;
import com.temenos.dbx.product.usermanagement.backenddelegate.impl.UserManagementBackendDelegateImpl;
import com.temenos.dbx.product.utils.DTOConstants;
import com.temenos.dbx.product.utils.DTOUtils;
import com.temenos.dbx.product.utils.InfinityConstants;

public class ProfileManagementBackendDelegateImpl implements ProfileManagementBackendDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public DBXResult get(CustomerDTO customerDTO, Map<String, Object> headerMap) {

        DBXResult dbxResult = new DBXResult();

        Map<String, Object> inputParams = new HashMap<>();
        String filter = "UserName" + DBPUtilitiesConstants.EQUAL + "'" + customerDTO.getUserName() + "'";

        inputParams.put(DBPUtilitiesConstants.FILTER, filter);

        JsonObject customer = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
                URLConstants.CUSTOMER_GET);

        dbxResult.setResponse(customer);

        return dbxResult;

    }

    @Override
    public DBXResult getCustomerForUserResponse(CustomerDTO customerDTO, Map<String, Object> headerMap) {

        DBXResult dbxResult = new DBXResult();

        customerDTO = (CustomerDTO) customerDTO.loadDTO();
        String leid = (String)headerMap.get("companyid");

        if (customerDTO != null) {

            Set<String> set = new HashSet<String>();

            String select =
                    "areUserAlertsTurnedOn,CustomerType_id,areAccountStatementTermsAccepted,areDepositTermsAccepted,"
                            + "isP2PActivated, isP2PSupported,isBillPaySupported,isBillPayActivated,isWireTransferActivated,"
                            + "isWireTransferEligible,FirstName,LastName,Gender,IsPinSet,DateOfBirth,NoOfDependents,"
                            + "SpouseName,Ssn,CountryCode,UserImage,UserImageURL,isEagreementSigned,id,UserName,MaritalStatus_id,"
                            + "Lastlogintime,Bank_id,isVIPCustomer,PreferredContactMethod,PreferredContactTime,MartialStatus,SpouseName,NoOfDependants,Gender,"
                            + "EmployementStatus_id,EmploymentInfo,companyLegalUnit,isQRPaymentActivated";

            String[] strings = select.split(",");
            set.addAll(Arrays.asList(strings));

            Map<String, String> map = DTOUtils.getResponseMap(customerDTO, set, true);

            CredentialCheckerDTO credentialCheckerDTO = new CredentialCheckerDTO();
            credentialCheckerDTO.setUserName(customerDTO.getUserName());
            credentialCheckerDTO.setLinktype(HelperMethods.CREDENTIAL_TYPE.ACTIVATION.toString());
            credentialCheckerDTO = (CredentialCheckerDTO) credentialCheckerDTO.loadDTO();

            map.put(DBPUtilitiesConstants.IS_CUSTOMER_ENROLLED, customerDTO.getIsEnrolled().toString());
            map.put(DBPUtilitiesConstants.CUSTOMER_STATUS, customerDTO.getStatus_id());
            if (credentialCheckerDTO == null) {
                map.put(DBPUtilitiesConstants.IS_ACTIVATION_LINK_SENT, "false");
            } else if (credentialCheckerDTO != null) {
                map.put(DBPUtilitiesConstants.IS_ACTIVATION_LINK_SENT, "true");
            }

            BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
            backendIdentifierDTO.setCustomer_id(customerDTO.getId());
            backendIdentifierDTO.setCompanyLegalUnit(leid);
            if (IntegrationTemplateURLFinder.isIntegrated) {
                backendIdentifierDTO
                        .setBackendType(IntegrationTemplateURLFinder.getBackendURL(InfinityConstants.BackendType));
            } else {
                backendIdentifierDTO.setBackendType(DTOConstants.CORE);
            }

            backendIdentifierDTO = (BackendIdentifierDTO) backendIdentifierDTO.loadDTO();

            if (backendIdentifierDTO != null) {
                map.put(InfinityConstants.isAssociated, "true");
                /*
                 * Commenting this extra un necessary call to T24, the below information 
                 * already fetched in main service.
                 * 
                 * */
                /*
                CoreCustomerBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
                        .getBusinessDelegate(CoreCustomerBusinessDelegate.class);
                MembershipDTO membershipDTO = new MembershipDTO();
                try {
                    membershipDTO = businessDelegate.getMembershipDetails(backendIdentifierDTO.getBackendId(),
                    		backendIdentifierDTO.getCompanyLegalUnit(), headerMap);
                } catch (ApplicationException e) {
                    // TODO Auto-generated catch block
                    alert.prepareError("Exception occured while fetching the customer communication details").log();
                   
                }
                if (StringUtils.isNotBlank(membershipDTO.getFirstName())) {
                    map.put(InfinityConstants.FirstName, membershipDTO.getFirstName());
                }
                if (StringUtils.isNotBlank(membershipDTO.getLastName())) {
                    map.put(InfinityConstants.LastName, membershipDTO.getLastName());
                }
                if (StringUtils.isNotBlank(membershipDTO.getDateOfBirth())) {
                    map.put("DateOfBirth", membershipDTO.getDateOfBirth());
                }
                */
            } else {
                map.put(InfinityConstants.isAssociated, "false");
            }

            dbxResult.setResponse(map);

            return dbxResult;
        }

        dbxResult.setDbpErrMsg("Unable to read Customer Data");

        return dbxResult;
    }

    @Override
    public DBXResult getUserResponse(CustomerDTO customerDTO, Map<String, Object> map) {

        JsonObject jsonObject = new JsonObject();

        String deviceId = getDeviceId(map);

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("Username", customerDTO.getUserName());
        inputParams.put("Customer_id", customerDTO.getId());
        inputParams.put("Bank_id", customerDTO.getBank_id());
        inputParams.put("deviceId", deviceId);
        inputParams.put("legalEntityId", customerDTO.getCompanyLegalUnit());

        DBXResult dbxResult = new DBXResult();
        try {
            jsonObject = ServiceCallHelper.invokeServiceAndGetJson(inputParams, map,
                    URLConstants.USEROBJECT_ORCHESTRATION_GET);
            jsonObject = orchestrationResultProcess(jsonObject);
            dbxResult.setResponse(jsonObject);
            return dbxResult;
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }

        return dbxResult;
    }

    private JsonObject orchestrationResultProcess(JsonObject result) {
        JsonObject finalResult = new JsonObject();
        JsonArray customerDataset = new JsonArray();
        JsonObject user = new JsonObject();
        if (result.has(DTOConstants.CUSTOMER_TABLE) && !result.get(DTOConstants.CUSTOMER_TABLE).isJsonNull()
                && result.get(DTOConstants.CUSTOMER_TABLE).isJsonArray()) {
            customerDataset = result.get(DTOConstants.CUSTOMER_TABLE).getAsJsonArray();
            user = customerDataset.get(0).getAsJsonObject();
        }

        if (user.has("LastName") && !user.get("LastName").isJsonNull()) {
            user.addProperty("userlastname", user.get("LastName").getAsString());
        }

        if (user.has("Ssn") && !user.get("Ssn").isJsonNull()) {
            user.addProperty("Ssn", user.get("Ssn").getAsString());
        }

        if (user.has("DateOfBirth") && !user.get("DateOfBirth").isJsonNull()) {
            user.addProperty("dateOfBirth", user.get("DateOfBirth").getAsString());
        }

        if (user.has("DateOfBirth") && !user.get("DateOfBirth").isJsonNull()) {
            user.addProperty("dateOfBirth", user.get("DateOfBirth").getAsString());
        }

        JsonObject jsonObject = new JsonObject();

        if (user.has("PreferredContactTime") && !user.get("PreferredContactTime").isJsonNull()) {
            jsonObject.add("PreferredContactTime", user.get("PreferredContactTime"));
        }

        if (user.has("PreferredContactMethod") && !user.get("PreferredContactMethod").isJsonNull()) {
            jsonObject.add("PreferredContactMethod", user.get("PreferredContactMethod"));
        }

        user.add("PreferredTimeAndMethod", jsonObject);

        if (result.has("CustomerPreferences") && !result.get("CustomerPreferences").isJsonNull()) {
            addParamsFromRecord(result.get("CustomerPreferences").getAsJsonObject(), user);
        }
        if (result.has("bankName") && !result.get("bankName").isJsonNull()) {
            user.addProperty("bankName", result.get("bankName").getAsString());
        }
        if (result.has("feedbackUserId") && !result.get("feedbackUserId").isJsonNull()) {
            user.addProperty("feedbackUserId", result.get("feedbackUserId").getAsString());
        }
        if (result.has("isSecurityQuestionConfigured") && !result.get("isSecurityQuestionConfigured").isJsonNull()) {
            user.addProperty("isSecurityQuestionConfigured", result.get("isSecurityQuestionConfigured").getAsString());
        }
        addCurrencyCode(user);

        if (result.has("ContactNumbers") && !result.get("ContactNumbers").isJsonNull()) {
            user.add("ContactNumbers", result.get("ContactNumbers").getAsJsonArray());
        }
        if (result.has("EmailIds") && !result.get("EmailIds").isJsonNull()) {
            user.add("EmailIds", result.get("EmailIds").getAsJsonArray());
        }
        if (result.has("Addresses") && !result.get("Addresses").isJsonNull()) {
            user.add("Addresses", result.get("Addresses").getAsJsonArray());
        }
        if (result.has("customers") && !result.get("customers").isJsonNull()) {
            user.add("customers", result.get("customers").getAsJsonArray());
        }

        JsonArray jsonArray = new JsonArray();
        jsonArray.add(user);

        finalResult.add(DTOConstants.CUSTOMER_TABLE, jsonArray);
        return finalResult;
    }

    private void addParamsFromRecord(JsonObject jsonObject, JsonObject user) {
        for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            user.add(entry.getKey(), entry.getValue());
        }
    }

    private String getDeviceId(Map<String, Object> headers) {
        String deviceId = null;
        if (headers != null && headers.containsKey(DBPUtilitiesConstants.X_KONY_REPORTING_PARAMS)) {
            String reportingParams = (String) headers.get(DBPUtilitiesConstants.X_KONY_REPORTING_PARAMS);
            if (StringUtils.isNotBlank(reportingParams)) {
                JSONObject reportingParamsJson = null;
                try {
                    reportingParamsJson = new JSONObject(
                            URLDecoder.decode(reportingParams, StandardCharsets.UTF_8.name()));
                } catch (JSONException | UnsupportedEncodingException e) {
                    alert.prepareError(e.getMessage()).log();
                }

                if (null != reportingParamsJson) {
                    deviceId = reportingParamsJson.optString("did");
                }
            }
        }

        return deviceId;
    }

    private void addCurrencyCode(JsonObject user) {
        String country = "";
        if (user.has("CountryCode") && !user.get("CountryCode").isJsonNull()) {
            user.addProperty(DBPUtilitiesConstants.CURRENCYCODE, HelperMethods.getCurrencyCode(country));
        }
        try {
            user.addProperty("lastlogintime",
                    HelperMethods.convertDateFormat(user.get("lastlogintime").getAsString(), "yyyy-MM-dd'T'HH:mm:ss"));
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }

    }

    @Override
    public DBXResult createCustomer(CustomerDTO customerDTO, Map<String, Object> headerMap) {

        DBXResult dbxResult = new DBXResult();

        if (DTOUtils.persistObject(customerDTO, headerMap)) {
            dbxResult.setResponse(customerDTO.getId());
        } else {
            dbxResult.setDbpErrMsg("Propect Creation Failed");
        }

        return dbxResult;

    }

    @Override
    public DBXResult updateCustomer(CustomerDTO customerDTO, Map<String, Object> headerMap) {
        DBXResult dbxResult = new DBXResult();
        JsonObject jsonObject = new JsonObject();
        JsonObject responseJsonObject = new JsonObject();
        customerDTO.setUserName(null);
        limitPhoneEmailAndAddressestoThree(customerDTO);
        if (DTOUtils.persistObject(customerDTO, headerMap)) {
            responseJsonObject.addProperty("success", "success");
            responseJsonObject.addProperty("Status", "Operation successful");
            responseJsonObject.addProperty("status", "Operation successful");
            dbxResult.setResponse(responseJsonObject);
            return dbxResult;
        } else {
            ErrorCodeEnum.ERR_10218.setErrorCode(responseJsonObject);
            responseJsonObject.addProperty(DBPUtilitiesConstants.VALIDATION_ERROR_CODE, "10218");
            responseJsonObject.addProperty(DBPUtilitiesConstants.VALIDATION_ERROR, "dbxCustomer update failed");
        }

        dbxResult.setResponse(responseJsonObject);
        return dbxResult;
    }

    private void limitPhoneEmailAndAddressestoThree(CustomerDTO customerDTO) {
        // TODO Auto-generated method stub

        int size = 0;
        boolean IS_Integrated = IntegrationTemplateURLFinder.isIntegrated;
        String profileSettingsBackend = EnvironmentConfigurationsHandler.getServerProperty("PROFILESETTINGS_BACKEND");
        if(profileSettingsBackend.equalsIgnoreCase("Mock")) {
        	IS_Integrated = false ;
        }
        if (IS_Integrated) {
            size = 2;
        } else {
            size = 3;
        }

        int phoneSize = 0;
        int emailSize = 0;
        int addressSize = 0;

        for (int i = 0; i < customerDTO.getCustomerCommuncation().size(); i++) {
            CustomerCommunicationDTO communicationDTO = customerDTO.getCustomerCommuncation().get(i);
            if (communicationDTO.getType_id().equals(HelperMethods.getCommunicationTypes().get("Phone"))) {
                if (phoneSize < size && !communicationDTO.isIsdeleted()) {
                    phoneSize++;
                } else if (!communicationDTO.isIsdeleted()) {
                    customerDTO.getCustomerCommuncation().remove(i);
                    i--;
                }
            } else if (communicationDTO.getType_id().equals(HelperMethods.getCommunicationTypes().get("Email"))) {
                if (emailSize < size && !communicationDTO.isIsdeleted()) {
                    emailSize++;
                } else if (!communicationDTO.isIsdeleted()) {
                    customerDTO.getCustomerCommuncation().remove(i);
                    i--;
                }
            }
        }

        for (int i = 0; i < customerDTO.getCustomerAddress().size(); i++) {
            CustomerAddressDTO addressDTO = customerDTO.getCustomerAddress().get(i);
            if (addressSize < size && !addressDTO.isIsdeleted()) {
                addressSize++;
            } else if (!addressDTO.isIsdeleted()) {
                customerDTO.getCustomerAddress().remove(i);
                i--;
            }
        }

    }

    @Override
    public DBXResult verifyCustomer(CustomerDTO customerDTO, Map<String, Object> headerMap, String deploymentPlatform)
            throws ApplicationException, JSONException, DBPApplicationException, MiddlewareException, UnsupportedEncodingException {

        DBXResult dbxResult = new DBXResult();
        JsonObject response = new JsonObject();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("_dateOfBirth", customerDTO.getDateOfBirth());
        String dateofbirth = customerDTO.getDateOfBirth();
        String legalEntityId = customerDTO.getLegalEntityId();
        inputParams.put("_legalEntityId", customerDTO.getLegalEntityId());
        String phone = "";
        String email = "";
        List<CustomerCommunicationDTO> commDTOS = new ArrayList<CustomerCommunicationDTO>();
        commDTOS = customerDTO.getCustomerCommuncation();
        for (CustomerCommunicationDTO dto : commDTOS) {
            if (DBPUtilitiesConstants.COMM_TYPE_EMAIL.equalsIgnoreCase(dto.getType_id())) {
                inputParams.put("_email", dto.getValue());
                email = dto.getValue();
            }
            if (DBPUtilitiesConstants.COMM_TYPE_PHONE.equalsIgnoreCase(dto.getType_id())) {
                inputParams.put("_phone", dto.getValue());
                phone = dto.getValue();
            }
        }
        
        String IS_Integrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);
        if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")) {
            CoreCustomerBusinessDelegate corecustomerBD =
                    DBPAPIAbstractFactoryImpl.getBusinessDelegate(CoreCustomerBusinessDelegate.class);
            List<MembershipDTO> membershipList = new ArrayList<>();
            try {
                membershipList = corecustomerBD.getMembershipDetails(dateofbirth, legalEntityId, email, phone, headerMap);
            } catch (Exception e) {

            }
            
            StringBuilder backendIdentifiers = new StringBuilder();
            for (MembershipDTO dto : membershipList) {
                if (StringUtils.isBlank(backendIdentifiers)) {
                    backendIdentifiers.append(dto.getId());
                    
                } else {
                    backendIdentifiers.append(DBPUtilitiesConstants.COMMA_SEPERATOR).append(dto.getId());
                }
            }
            /*String integration = EnvironmentConfigurationsHandler.getServerProperty("INTEGRATION_NAME");
            BackendIdentifierDTO backenddto = new BackendIdentifierDTO();
            backenddto.setBackendType(integration);
            backenddto.setCompanyLegalUnit(customerDTO.getLegalEntityId());
            BackendIdentifierBusinessDelegate backendidentifierBD = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(BackendIdentifierBusinessDelegate.class);
            String backendType = backendidentifierBD.getBackendType(backenddto,headerMap);
			*/
            String backendtype = EnvironmentConfigurationsHandler.getServerProperty("INTEGRATION_NAME");
            if(StringUtils.isBlank(backendtype))
            	backendtype = "CORE";
            backendtype = backendtype.toUpperCase();
            inputParams.put("_backendIdentifiers", backendIdentifiers.toString());
            inputParams.put("_backendType", backendtype);

        }
        JsonObject jsonObject = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
                URLConstants.VERIFY_USER_PROC);

        if (!jsonObject.has("records")) {
            dbxResult.setError(ErrorCodeEnum.ERR_10024);
            return dbxResult;
        }

        if (jsonObject.get("records").isJsonNull() || jsonObject.get("records").getAsJsonArray().size() <= 0) {
            response.addProperty(DBPUtilitiesConstants.IS_USER_EXISTS, "false");
            dbxResult.setResponse(response);
            return dbxResult;
        }        
		JsonArray customers = jsonObject.get("records").getAsJsonArray();
		if (customers.get(0).getAsJsonObject().get("CustomerType_id").getAsString()
				.equalsIgnoreCase(DBPUtilitiesConstants.TYPE_ID_PROSPECT)) {
			for (int i = 0; i < customers.size(); i++) {
				JsonObject record = customers.get(i).getAsJsonObject();
				String searchPath = null;
				searchPath = "${digitalProfileId}";
				JSONObject query = new JSONObject();
				query.put("value", record.get("id").getAsString());
				query.put("searchPath", searchPath);
				query.put("entityDefinitionCode",
						EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.ONBOARDING_ENTITY_DEFINTION));
				String encodedQuery = URLEncoder.encode(query.toString(), StandardCharsets.UTF_8.toString());
				
				if (StringUtils.isNotBlank(deploymentPlatform)) { // double encoding in case of azure deployment
					if (StringUtils.equals(deploymentPlatform, "azure"))
						encodedQuery = URLEncoder.encode(encodedQuery.toString(), StandardCharsets.UTF_8.toString());
				}
				
				Map<String, Object> mapPayload = new HashMap<String, Object>();
				mapPayload.put("query", encodedQuery);
				String responseStr = DBPServiceExecutorBuilder.builder()
						.withServiceId(com.temenos.dbx.eum.product.constants.ServiceId.DBP_DATA_STORAGE_APIS)
						.withOperationId(com.temenos.dbx.eum.product.constants.OperationName.DB_SEARCH_EXTRACT_QUERY)
						.withRequestParameters(mapPayload).build().getResponse();

				JSONObject searchExtractRes = new JSONObject(responseStr);
				if (searchExtractRes.optJSONArray("searchExtracts") != null) {
					JSONArray searchExtracts = searchExtractRes.getJSONArray("searchExtracts");
					JSONArray keys = new JSONArray();
					for (int j = 0; j < searchExtracts.length(); j++) {
						JSONObject object = searchExtracts.getJSONObject(j);
						if (!object.optString("key").isEmpty()) {
							keys.put(object.getString("key"));
						}
					}
					record.addProperty("keys", keys.toString());
				}
			}
		}
		
		for (int i = 0; i < customers.size(); i++) {
			JsonObject record = customers.get(i).getAsJsonObject();
			String activationToken = UUID.randomUUID().toString();
			Map<String, Object> map = new HashMap<>();
			map.put("id", activationToken);
			map.put("UserName", record.get("UserName").getAsString());
			map.put("linktype", HelperMethods.CREDENTIAL_TYPE.RESETPASSWORD.toString());
			map.put("createdts", HelperMethods.getCurrentTimeStamp());
			ServiceCallHelper.invokeServiceAndGetJson(map, headerMap, URLConstants.CREDENTIAL_CHECKER_CREATE);
			record.addProperty(MFAConstants.SECURITY_KEY, activationToken);
		}

		response.addProperty(DBPUtilitiesConstants.IS_USER_EXISTS, "true");
		response.add(DBPUtilitiesConstants.USR_ATTR, customers);
		dbxResult.setResponse(response);
		return dbxResult;
	}

    @Override
    public DBXResult searchCustomer(Map<String, String> configurations, MemberSearchBean memberSearchBean,
            Map<String, Object> headerMap, PasswordHistoryManagement pm) {
        DBXResult dbxResult = new DBXResult();
        JsonObject processedResult = new JsonObject();
        JsonObject searchResults = new JsonObject();
        String IS_Integrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);
        boolean isCustomerSearch = false;
        if (StringUtils.isNotBlank(memberSearchBean.getCustomerId())) {
            memberSearchBean.setMemberId(memberSearchBean.getCustomerId());
            isCustomerSearch = true;
            memberSearchBean.setCustomerId(null);
        }
       
        if (isCustomerSearch && StringUtils.isNotBlank(memberSearchBean.getMemberId())
                && !IntegrationTemplateURLFinder.isIntegrated
                && !memberPresent(memberSearchBean.getMemberId(), headerMap)) {
            dbxResult.setError(ErrorCodeEnum.ERR_10335);
            return dbxResult;
        } else if (!isCustomerSearch && StringUtils.isNotBlank(memberSearchBean.getMemberId())
                && !IntegrationTemplateURLFinder.isIntegrated
                && memberPresent(memberSearchBean.getMemberId(), headerMap)) {
            dbxResult.setError(ErrorCodeEnum.ERR_10335);
            return dbxResult;
        }

        if (memberSearchBean.getSearchType().equalsIgnoreCase(DTOConstants.APPLICANT_SEARCH)) {
            processedResult.addProperty("TotalResultsFound", 0);
            searchResults = searchCustomers(headerMap, memberSearchBean.getSearchType(), memberSearchBean);
        }
        if (memberSearchBean.getSearchType().equalsIgnoreCase(DTOConstants.GROUP_SEARCH)) {
            searchResults = searchCustomers(headerMap, memberSearchBean.getSearchType(), memberSearchBean);
            if (searchResults.has("records1") && searchResults.get("records1").getAsJsonArray().size() > 0) {
                if (searchResults.get("records1").getAsJsonArray().get(0).getAsJsonObject().has("SearchMatchs")) {
                    processedResult.addProperty("TotalResultsFound", searchResults.get("records1").getAsJsonArray()
                            .get(0).getAsJsonObject().get("SearchMatchs").getAsInt());
                } else {
                    processedResult.addProperty("TotalResultsFound", 0);
                }
            }
        }
        if (memberSearchBean.getSearchType().equalsIgnoreCase(DTOConstants.CUSTOMER_SEARCH)) {
            searchResults = searchCustomers(headerMap, memberSearchBean.getSearchType(), memberSearchBean);
            if (searchResults.has("records1") && searchResults.get("records1").getAsJsonArray().size() > 0) {
                if (searchResults.get("records1").getAsJsonArray().get(0).getAsJsonObject().has("SearchMatchs")) {
                    processedResult.addProperty("TotalResultsFound", searchResults.get("records1").getAsJsonArray()
                            .get(0).getAsJsonObject().get("SearchMatchs").getAsInt());
                } else {
                    processedResult.addProperty("TotalResultsFound", "0");
                }
            }
        }

        JsonArray recordsArray = new JsonArray();
        int erasureStatusCount = 0;
        if (searchResults.has("records")) {

            recordsArray = searchResults.get("records").getAsJsonArray();
            if(recordsArray.size() > 0) {
                JsonArray filteredRecordsArray = new JsonArray();
                for(JsonElement recordElementItem: recordsArray){
                    JsonObject recordObjItem = (JsonObject) recordElementItem;
                    if(recordObjItem.has("Status_id")
                        && recordObjItem.get("Status_id").getAsString().equals(StatusEnum.SID_CUS_ERASURE_INPROGRESS.name())
                        || recordObjItem.get("Status_id").getAsString().equals(StatusEnum.SID_CUS_ERASURE_COMPLETED.name())){
                        erasureStatusCount++;
                    } else {
                        filteredRecordsArray.add(recordObjItem);
                    }
                }
                recordsArray = filteredRecordsArray;
            }
            processedResult.add("records", recordsArray);

        }

        if ((StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")
                && (StringUtils.isBlank(memberSearchBean.getMemberId()) || isCustomerSearch))
                && !memberSearchBean.getIsMicroServiceFlow()) {
            processedResult = searchCustomerinT24(recordsArray, configurations, memberSearchBean, headerMap,
                    isCustomerSearch, pm);
            mergeResults(recordsArray, headerMap);
            mergeResultsFromLeadMS(recordsArray, headerMap);
        }

        if(erasureStatusCount > 0){
            processedResult.addProperty("ErasureStatusRecordsFound", true);
        }

        processedResult.addProperty("Status", "Records returned: " + recordsArray.size());

        processedResult.addProperty("TotalResultsFound", recordsArray.size());

        processedResult.add("records", recordsArray);
        if (recordsArray.size() == 1
                && memberSearchBean.getSearchType().equalsIgnoreCase(DTOConstants.CUSTOMER_SEARCH)
                && !memberSearchBean.getIsMicroServiceFlow()) {

            String customerId = recordsArray.get(0).getAsJsonObject().has("id")
                    ? recordsArray.get(0).getAsJsonObject().get("id").getAsString()
                    : recordsArray.get(0).getAsJsonObject().get(InfinityConstants.primaryCustomerId).getAsString();
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setId(customerId);
            customerDTO.setCompanyLegalUnit(memberSearchBean.getCompanyLegalUnit());
			dbxResult = getBasicInformation(configurations, customerDTO, headerMap, isCustomerSearch, pm);

            if (dbxResult.getResponse() != null) {
                JsonObject jsonObject = (JsonObject) dbxResult.getResponse();

                for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                    processedResult.add(entry.getKey(), entry.getValue());
                }
            }

        }
        Set<String> enrolledCustomers = new HashSet<>();
        for (JsonElement recordElement : recordsArray) {
            isAssociated(recordElement, headerMap);
            if (!Boolean.parseBoolean(
                    recordElement.getAsJsonObject().get(InfinityConstants.isProfileExist).getAsString())) {
                recordElement.getAsJsonObject().addProperty(InfinityConstants.isEnrolled, "false");
            }
            
			if (recordElement.getAsJsonObject().has(InfinityConstants.id)) {
				enrolledCustomers.add(recordElement.getAsJsonObject().get(InfinityConstants.id).getAsString());
			}
        }
        
        Map<String, Set<String>> infinitycustomerLegalEntities = CommonUtils.getCustomerLegalEntities(enrolledCustomers, headerMap);
        CommonUtils.generateLegalEntitiesForCustomers(recordsArray, infinitycustomerLegalEntities);
        dbxResult.setResponse(processedResult);
        if (recordsArray.size() == 0) {
            dbxResult.setError(ErrorCodeEnum.ERR_10335);
        }

        return dbxResult;
	}

	private String getLeadId(String id, Map<String, Object> headerMap) {
		BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
		backendIdentifierDTO.setCustomer_id(id);
		backendIdentifierDTO.setBackendType(DTOConstants.LEAD);
		DBXResult dbxResult = new DBXResult();
		try {
			dbxResult = DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class)
					.get(backendIdentifierDTO, headerMap);
		} catch (ApplicationException e) {
			alert.prepareError("Exception", e).log();
		}
		if (dbxResult.getResponse() != null) {
			return ((BackendIdentifierDTO) dbxResult.getResponse()).getBackendId();
		}
		return "";
	}

	public void mergeResultsFromLeadMS(JsonArray recordsArray, Map<String, Object> headerMap) {
		for (int i = 0; i < recordsArray.size(); i++) {
			JsonObject jsonObject = recordsArray.get(i).getAsJsonObject();
			String id = jsonObject.has(InfinityConstants.id) && !jsonObject.get(InfinityConstants.id).isJsonNull()
					? jsonObject.get(InfinityConstants.id).getAsString()
					: null;
			if (jsonObject.has("CustomerTypeId") && !jsonObject.get("CustomerTypeId").isJsonNull() && jsonObject.get("CustomerTypeId").getAsString().equalsIgnoreCase("TYPE_ID_PROSPECT")) {
				String leadId = getLeadId(id, headerMap);
				Map<String, Object> mapPayload = new HashMap<String, Object>();
				mapPayload.put("entityDefinitionCode", "lead");
				mapPayload.put("trackingCode", leadId);
				String responseStr = "";
				try {
					responseStr = DBPServiceExecutorBuilder.builder()
							.withServiceId(com.temenos.dbx.eum.product.constants.ServiceId.DBP_DATA_STORAGE_APIS)
							.withOperationId(com.temenos.dbx.eum.product.constants.OperationName.GET_ALL_ENTITY_ITEMS)
							.withRequestParameters(mapPayload).build().getResponse();
				} catch (DBPApplicationException e) {
					alert.prepareError("Exception", e).log();
				}

				if (StringUtils.isNotBlank(responseStr)) {
					JSONObject searchRes = new JSONObject(responseStr);
					JSONArray entityItems = searchRes.getJSONArray("entityItems");
					JSONObject leadEntityJSON = getEntityDataByName(entityItems, "Lead");
					JSONObject mobileJSON = getEntityDataByName(entityItems, "Address_Mobile_Home");
					JSONObject emailJSON = getEntityDataByName(entityItems, "Address_Email_Home");
					if (leadEntityJSON.has("firstName"))
						jsonObject.addProperty("FirstName", leadEntityJSON.optString("firstName"));

					if (leadEntityJSON.has("lastName"))
						jsonObject.addProperty("LastName", leadEntityJSON.optString("lastName"));

					if (leadEntityJSON.has("dateOfBirth"))
						jsonObject.addProperty("DateOfBirth", leadEntityJSON.optString("dateOfBirth"));

					if (mobileJSON.has("phoneNo"))
						jsonObject.addProperty("PrimaryPhoneNumber",
								mobileJSON.optString("iddPrefixPhone") + "-" + mobileJSON.optString("phoneNo"));

					if (emailJSON.has("electronicAddress"))
						jsonObject.addProperty("PrimaryEmailAddress", emailJSON.optString("electronicAddress"));

				}
				recordsArray.set(i, jsonObject);
			}
		}
	}
	
	private JSONObject getEntityDataByName(JSONArray entityItems, String entityName) {
		if (StringUtils.isEmpty(entityName))
			return new JSONObject(); // Entity Name would be empty if entity is not created yet

		Optional<Object> metadataEntity = StreamSupport.stream(entityItems.spliterator(), true)
				.filter(item -> ((JSONObject) item).getString("name").equalsIgnoreCase(entityName)).findFirst();

		JSONObject entityJSON;
		if (metadataEntity.isPresent())
			entityJSON = (JSONObject) metadataEntity.get();
		else
			return new JSONObject(); // Entity may not be created yet

		return new JSONObject(entityJSON.getString("entry"));
	}

	 
    public void mergeResults(JsonArray recordsArray, Map<String, Object> headerMap) {
        for (int i = 0; i < recordsArray.size(); i++) {
            JsonObject jsonObject = recordsArray.get(i).getAsJsonObject();
            String id = jsonObject.has(InfinityConstants.id) && !jsonObject.get(InfinityConstants.id).isJsonNull()
                    ? jsonObject.get(InfinityConstants.id).getAsString()
                    : null;
            for (int j = i + 1; j < recordsArray.size(); j++) {
                JsonObject t24Object = recordsArray.get(j).getAsJsonObject();
                String primaryCustomerId = t24Object.has(InfinityConstants.primaryCustomerId)
                        && !t24Object.get(InfinityConstants.primaryCustomerId).isJsonNull()
                                ? t24Object.get(InfinityConstants.primaryCustomerId).getAsString()
                                : null;
                if (StringUtils.isNotBlank(primaryCustomerId) && StringUtils.isNotBlank(id)) {
                    BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
                    backendIdentifierDTO.setCustomer_id(id);
                    backendIdentifierDTO.setBackendId(primaryCustomerId);
                    backendIdentifierDTO
                            .setBackendType(IntegrationTemplateURLFinder.getBackendURL(InfinityConstants.BackendType));
                    DBXResult dbxResult = new DBXResult();
                    try {
                        dbxResult =
                                DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class)
                                        .get(backendIdentifierDTO, headerMap);
                    } catch (ApplicationException e) {
                    	alert.prepareError("Exception", e).log();
                    }
                    if (dbxResult.getResponse() != null) {
                        for (Entry<String, JsonElement> featureJsonEntry : t24Object.entrySet()) {
                            jsonObject.add(featureJsonEntry.getKey(), featureJsonEntry.getValue());
                        }
                        jsonObject.addProperty(InfinityConstants.id, id);
                        recordsArray.remove(j);
                        break;
                    }
                }

            }
        }

    }

    public boolean memberPresent(String memberId, Map<String, Object> headerMap) {
        String filter = "id" + DBPUtilitiesConstants.EQUAL + memberId;

        Map<String, Object> input = new HashMap<String, Object>();
        input.put(DBPUtilitiesConstants.FILTER, filter);
        JsonObject result = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap, URLConstants.MEMBERSHIP_GET);

        if (result.has(DBPDatasetConstants.DATASET_MEMBERSHIP)) {
            JsonArray jsonArray = result.get(DBPDatasetConstants.DATASET_MEMBERSHIP).isJsonArray()
                    ? result.get(DBPDatasetConstants.DATASET_MEMBERSHIP).getAsJsonArray()
                    : new JsonArray();
            return jsonArray.size() > 0;
        }

        return false;
    }

    public void isAssociated(JsonElement recordElement, Map<String, Object> headerMap) {

        if (!recordElement.getAsJsonObject().has(InfinityConstants.isProfileExist)) {
            if (recordElement.getAsJsonObject().has(InfinityConstants.id)
                    && !recordElement.getAsJsonObject().get(InfinityConstants.id).isJsonNull()
                    && recordElement.getAsJsonObject().has(InfinityConstants.Username)
                    && !recordElement.getAsJsonObject().get(InfinityConstants.Username).isJsonNull()) {
                if (recordElement.getAsJsonObject().get(InfinityConstants.id).getAsString()
                        .equals(recordElement.getAsJsonObject().get(InfinityConstants.Username).getAsString())) {
                    recordElement.getAsJsonObject().add(InfinityConstants.primaryCustomerId,
                            recordElement.getAsJsonObject().get(InfinityConstants.id));
                    recordElement.getAsJsonObject().addProperty(InfinityConstants.isProfileExist, "false");
                    recordElement.getAsJsonObject().remove(InfinityConstants.id);
                } else {
                    recordElement.getAsJsonObject().addProperty(InfinityConstants.isProfileExist, "true");
                }
            } else {
                recordElement.getAsJsonObject().addProperty(InfinityConstants.isProfileExist, "false");
            }
        }
        String CustomerType_id = "";
        if (!recordElement.getAsJsonObject().has(InfinityConstants.id)
                || recordElement.getAsJsonObject().get(InfinityConstants.id).isJsonNull()) {
            recordElement.getAsJsonObject().addProperty(InfinityConstants.isAssociated, "false");
            return;
        }

        BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();

        backendIdentifierDTO.setCustomer_id(recordElement.getAsJsonObject().get(InfinityConstants.id).getAsString());
        if(JSONUtil.hasKey(recordElement.getAsJsonObject(), "branchId")) {
        	backendIdentifierDTO.setCompanyLegalUnit(recordElement.getAsJsonObject().get("branchId").getAsString());
        }else {
        	backendIdentifierDTO.setCompanyLegalUnit(recordElement.getAsJsonObject().get("companyLegalUnit").getAsString());
        }
        if (IntegrationTemplateURLFinder.isIntegrated) {
            backendIdentifierDTO.setIdentifier_name(
                    IntegrationTemplateURLFinder.getBackendURL(InfinityConstants.BackendCustomerIdentifierName));
            backendIdentifierDTO
                    .setBackendType(IntegrationTemplateURLFinder.getBackendURL(InfinityConstants.BackendType));
        } else {
            backendIdentifierDTO.setBackendType(DTOConstants.CORE);
        }

        try {
            DBXResult dbxResult = DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class)
                    .get(backendIdentifierDTO, headerMap);
            if (dbxResult.getResponse() != null) {
                BackendIdentifierDTO identifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
                recordElement.getAsJsonObject().addProperty(InfinityConstants.primaryCustomerId,
                        identifierDTO.getBackendId());
            } else {
                recordElement.getAsJsonObject().addProperty(InfinityConstants.primaryCustomerId,
                        backendIdentifierDTO.getCustomer_id());
            }
        } catch (ApplicationException e1) {
            // TODO Auto-generated catch block
            alert.prepareError("Error while fetching backend identifier for backend ID "
                    + recordElement.getAsJsonObject().get(InfinityConstants.id).getAsString()).log();
        }

        String customerId = recordElement.getAsJsonObject().get(InfinityConstants.id).getAsString();
        backendIdentifierDTO = new BackendIdentifierDTO();
        backendIdentifierDTO.setCustomer_id(customerId);
        if (IntegrationTemplateURLFinder.isIntegrated) {
            backendIdentifierDTO.setIdentifier_name(
                    IntegrationTemplateURLFinder.getBackendURL(InfinityConstants.BackendCustomerIdentifierName));
            backendIdentifierDTO
                    .setBackendType(IntegrationTemplateURLFinder.getBackendURL(InfinityConstants.BackendType));
        } else {
            backendIdentifierDTO.setBackendType(DTOConstants.CORE);
        }

        try {
            DBXResult dbxResult = DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class)
                    .get(backendIdentifierDTO, headerMap);
            if (dbxResult.getResponse() != null) {
                backendIdentifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
                recordElement.getAsJsonObject().addProperty(InfinityConstants.id,
                        backendIdentifierDTO.getCustomer_id());
                recordElement.getAsJsonObject().addProperty(InfinityConstants.Customer_id,
                        backendIdentifierDTO.getCustomer_id());
                CustomerDTO userDTO = (CustomerDTO) new CustomerDTO().loadDTO(backendIdentifierDTO.getCustomer_id());
                recordElement.getAsJsonObject().addProperty(InfinityConstants.isEnrolled, "" + userDTO.getIsEnrolled());
                recordElement.getAsJsonObject().addProperty(InfinityConstants.CustomerType_id,
                        userDTO.getCustomerType_id());
                customerId = backendIdentifierDTO.getCustomer_id();

                if (!IntegrationTemplateURLFinder.isIntegrated) {
                    CustomerCommunicationDTO customerCommunicationDTO = new CustomerCommunicationDTO();
                    customerCommunicationDTO.setCustomer_id(customerId);
                    CommunicationBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
                            .getBackendDelegate(CommunicationBackendDelegate.class);
                    dbxResult = backendDelegate.getPrimaryMFACommunicationDetails(customerCommunicationDTO, headerMap);
                    if (dbxResult.getResponse() != null) {
                        JsonObject jsonObject = (JsonObject) dbxResult.getResponse();
                        if (jsonObject.has(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION)) {
                            JsonElement jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION);
                            if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                                JsonArray jsonArray = jsonElement.getAsJsonArray();
                                for (int i = 0; i < jsonArray.size(); i++) {
                                    jsonObject = jsonArray.get(i).getAsJsonObject();
                                    if (jsonObject.get("Type_id").getAsString()
                                            .equals(HelperMethods.getCommunicationTypes().get("Phone"))) {
                                        recordElement.getAsJsonObject().addProperty(
                                                InfinityConstants.PrimaryPhoneNumber,
                                                jsonObject.has("Value") && !jsonObject.get("Value").isJsonNull()
                                                        ? jsonObject.get("Value").getAsString()
                                                        : null);
                                    } else if (jsonObject.get("Type_id").getAsString()
                                            .equals(HelperMethods.getCommunicationTypes().get("Email"))) {
                                        recordElement.getAsJsonObject().addProperty(
                                                InfinityConstants.PrimaryEmailAddress,
                                                jsonObject.has("Value") && !jsonObject.get("Value").isJsonNull()
                                                        ? jsonObject.get("Value").getAsString()
                                                        : null);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (ApplicationException e1) {
            // TODO Auto-generated catch block
            alert.prepareError("Error while fetching backend identifier for backend ID "
                    + recordElement.getAsJsonObject().get(InfinityConstants.id).getAsString()).log();
        }

        String filter = InfinityConstants.customerId + DBPUtilitiesConstants.EQUAL + customerId;
        Map<String, Object> input = new HashMap<String, Object>();
        input.put(DBPUtilitiesConstants.FILTER, filter);
        JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
                URLConstants.CONTRACT_CUSTOMERS_GET);
        if (response.has(DBPDatasetConstants.DATASET_CONTRACT_CUSTOMERS)) {
            JsonElement jsonElement = response.get(DBPDatasetConstants.DATASET_CONTRACT_CUSTOMERS);
            if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                for (JsonElement arrayElement : jsonElement.getAsJsonArray()) {
                    filter = InfinityConstants.id + DBPUtilitiesConstants.EQUAL
                            + arrayElement.getAsJsonObject().get(InfinityConstants.contractId).getAsString();
                    input = new HashMap<String, Object>();
                    input.put(DBPUtilitiesConstants.FILTER, filter);
                    JsonObject jsonObject = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
                            URLConstants.CONTRACT_GET);
                    input.clear();
                    if (jsonObject.has(DBPDatasetConstants.DATASET_CONTRACT)) {
                        jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CONTRACT);
                        if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                            JsonArray jsonArray = jsonElement.getAsJsonArray();
                            jsonObject = jsonArray.get(0).getAsJsonObject();
                            if (StringUtils.isBlank(CustomerType_id) || !CustomerType_id
                                    .contains(jsonObject.get(InfinityConstants.serviceType).getAsString())) {
                                if (StringUtils.isNotBlank(CustomerType_id)) {
                                    CustomerType_id += ",";
                                }
                                CustomerType_id += jsonObject.get(InfinityConstants.serviceType).getAsString();
                            }
                        }
                    }
                }
                recordElement.getAsJsonObject().addProperty(InfinityConstants.CustomerType_id, CustomerType_id);
                recordElement.getAsJsonObject().addProperty(InfinityConstants.CustomerTypeId, CustomerType_id);
                recordElement.getAsJsonObject().addProperty(InfinityConstants.isAssociated, "true");
            } else {
                recordElement.getAsJsonObject().addProperty(InfinityConstants.isAssociated, "false");
                getCustomerType(recordElement.getAsJsonObject(),
                        recordElement.getAsJsonObject().get(InfinityConstants.id).getAsString(), headerMap, backendIdentifierDTO.getCompanyLegalUnit());
                recordElement.getAsJsonObject().add(InfinityConstants.CustomerTypeId,
                        recordElement.getAsJsonObject().get(InfinityConstants.CustomerType_id));
            }
        }

    }

    public JsonObject searchCustomers(Map<String, Object> headerMap, String searchType,
            MemberSearchBean memberSearchBean) {
        Map<String, Object> searchPostParameters = new HashMap<String, Object>();

        searchPostParameters.put("_id", memberSearchBean.getMemberId());
        searchPostParameters.put("_name", memberSearchBean.getCustomerName());
        searchPostParameters.put("_username", memberSearchBean.getCustomerUsername());
        searchPostParameters.put("_SSN", memberSearchBean.getSsn());
        searchPostParameters.put("_phone", memberSearchBean.getCustomerPhone());
        searchPostParameters.put("_email", memberSearchBean.getCustomerEmail());
        searchPostParameters.put("_IsStaffMember", memberSearchBean.getIsStaffMember());
        searchPostParameters.put("_cardorAccountnumber", memberSearchBean.getCardorAccountnumber());
        searchPostParameters.put("_TIN", memberSearchBean.getTin());
        searchPostParameters.put("_group", memberSearchBean.getCustomerGroup());
        searchPostParameters.put("_IDType", memberSearchBean.getCustomerIDType());
        searchPostParameters.put("_IDValue", memberSearchBean.getCustomerIDValue());
        searchPostParameters.put("_companyId", memberSearchBean.getCustomerCompanyId());
        searchPostParameters.put("_requestID", memberSearchBean.getCustomerRequest());
        searchPostParameters.put("_branchIDS", memberSearchBean.getBranchIDS());
        searchPostParameters.put("_productIDS", memberSearchBean.getProductIDS());
        searchPostParameters.put("_cityIDS", memberSearchBean.getCityIDS());
        searchPostParameters.put("_entitlementIDS", memberSearchBean.getEntitlementIDS());
        searchPostParameters.put("_groupIDS", memberSearchBean.getGroupIDS());
        searchPostParameters.put("_customerStatus", memberSearchBean.getCustomerStatus());
        searchPostParameters.put("_before", memberSearchBean.getBeforeDate());
        searchPostParameters.put("_after", memberSearchBean.getAfterDate());
        searchPostParameters.put("_dateOfBirth", memberSearchBean.getDateOfBirth());
        searchPostParameters.put("_legalEntityId",memberSearchBean.getCompanyLegalUnit());
        if (HelperMethods.allEmpty(searchPostParameters)) {
            return new JsonObject();
        }
        searchPostParameters.put("_sortVariable", memberSearchBean.getSortVariable());
        searchPostParameters.put("_sortDirection", memberSearchBean.getSortDirection());
        searchPostParameters.put("_searchType", searchType);
        searchPostParameters.put("_pageOffset", String.valueOf(memberSearchBean.getPageOffset()));
        searchPostParameters.put("_pageSize", String.valueOf(memberSearchBean.getPageSize()));
        
        return ServiceCallHelper.invokeServiceAndGetJson(searchPostParameters, headerMap,
                URLConstants.CUSTOMER_SEARCH_PROC);
    }

    @Override
    public DBXResult getBasicInformation(Map<String, String> configurations, CustomerDTO customerDTO,
            Map<String, Object> headerMap, boolean isCustomerSearch, PasswordHistoryManagement pm) {
        DBXResult dbxResult = new DBXResult();
        String customerId = customerDTO.getId();
        String username = customerDTO.getUserName();
        String legalEntityId = customerDTO.getCompanyLegalUnit();
        
        JsonObject basicResult = new JsonObject();
      

        if (StringUtils.isBlank(customerId) && StringUtils.isNotBlank(legalEntityId)) {
            if (StringUtils.isBlank(username)) {
                ErrorCodeEnum.ERR_20612.setErrorCode(basicResult);
                basicResult.addProperty("Status", "Failure");
                dbxResult.setResponse(basicResult);
                return dbxResult;
            }
        }

        if (StringUtils.isBlank(customerId) && StringUtils.isNotBlank(legalEntityId)) {
            customerDTO = (CustomerDTO) customerDTO.loadDTO();
            if (customerDTO != null) {
                customerId = customerDTO.getId();
                legalEntityId = customerDTO.getCompanyLegalUnit();
            } else {
                ErrorCodeEnum.ERR_20688.setErrorCode(basicResult);
                basicResult.addProperty("Status", "Failure");
                dbxResult.setResponse(basicResult);
                return dbxResult;
            }
        }
        
        
       
        JsonObject customerViewJson = new JsonObject();

        Map<String, Object> postParametersMap = new HashMap<String, Object>();
        postParametersMap.put("_customerId", customerId);
        postParametersMap.put("_legalEntityId", legalEntityId);
        JsonObject jsonobject = ServiceCallHelper.invokeServiceAndGetJson(postParametersMap, headerMap,
                URLConstants.CUSTOMER_BASIC_INFO_PROC);
        String primaryCustomerId = "";
        final String IS_Integrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);
        if (jsonobject != null && jsonobject.has(Param.OPSTATUS) && jsonobject.get(Param.OPSTATUS).getAsInt() == 0
                && jsonobject.has("records")) {

            if (jsonobject.get("records").getAsJsonArray().size() == 0
                    && (StringUtils.isBlank(IS_Integrated) || !IS_Integrated.equalsIgnoreCase("true"))) {
                ErrorCodeEnum.ERR_20539.setErrorCode(basicResult);
                basicResult.addProperty("Status", "Failure");
                dbxResult.setResponse(basicResult);
                return dbxResult;
            } else if (jsonobject.get("records").getAsJsonArray().size() != 0) {
                customerViewJson = jsonobject.get("records").getAsJsonArray().get(0).getAsJsonObject();
            }

            if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")) {
                dbxResult = getBasicInfo(customerViewJson, configurations, customerDTO, headerMap, isCustomerSearch, pm);

                basicResult = ((JsonObject) dbxResult.getResponse());

                if (basicResult.has("customerbasicinfo_view")) {
                    basicResult = basicResult.has("customerbasicinfo_view")
                            && basicResult.get("customerbasicinfo_view").isJsonObject()
                                    ? basicResult.get("customerbasicinfo_view").getAsJsonObject()
                                    : new JsonObject();
                    for (Entry<String, JsonElement> entry : basicResult.entrySet()) {
                        customerViewJson.add(entry.getKey(), entry.getValue());
                    }

                    basicResult = new JsonObject();

                    basicResult.add("customerbasicinfo_view", customerViewJson);

                    dbxResult.setResponse(basicResult);

                    return dbxResult;
                }
            }

        } else {
            ErrorCodeEnum.ERR_20689.setErrorCode(basicResult);
            basicResult.addProperty("Status", "Failure");
            dbxResult.setResponse(basicResult);
            return dbxResult;
        }

        if (customerViewJson.has(InfinityConstants.Customer_id)) {
            customerViewJson.add(InfinityConstants.id, customerViewJson.get(InfinityConstants.Customer_id));
        }

        boolean isAssociated = false;

        if (StringUtils.isBlank(IS_Integrated) || !IS_Integrated.equalsIgnoreCase("true")) {
            BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();

            backendIdentifierDTO.setBackendType(DTOConstants.CORE);

            if (isCustomerSearch) {
                backendIdentifierDTO.setBackendId(customerDTO.getId());
               
            } else {
                backendIdentifierDTO.setCustomer_id(customerDTO.getId());
               
            }
            backendIdentifierDTO.setCompanyLegalUnit(customerDTO.getCompanyLegalUnit());
            
            try {
                dbxResult = DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class)
                        .get(backendIdentifierDTO, headerMap);
                if (dbxResult.getResponse() != null) {
                    BackendIdentifierDTO identifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
                    primaryCustomerId = identifierDTO.getBackendId();
                    customerId = identifierDTO.getCustomer_id();
                } else {
                	 
                    backendIdentifierDTO = new BackendIdentifierDTO();
                    backendIdentifierDTO.setBackendType(DTOConstants.CORE);
                    backendIdentifierDTO.setBackendId(customerDTO.getId());

                    dbxResult = DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class)
                            .get(backendIdentifierDTO, headerMap);
                    if (dbxResult.getResponse() != null) {
                        BackendIdentifierDTO identifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
                        primaryCustomerId = identifierDTO.getBackendId();
                       
                        customerId = identifierDTO.getCustomer_id();
                       
                    }
                }
            } catch (ApplicationException e1) {
                // TODO Auto-generated catch block
                alert.prepareError("Error while fetching backend identifier for backend ID " + customerDTO.getId()).log();
            }

            if (StringUtils.isBlank(primaryCustomerId)) {
                primaryCustomerId = customerViewJson.get("id").getAsString();
            }
        }

        if (customerViewJson.has(InfinityConstants.Username)
                && !customerViewJson.get(InfinityConstants.Username).isJsonNull()
                && customerViewJson.has(InfinityConstants.id)
                && !customerViewJson.get(InfinityConstants.id).isJsonNull()) {
            if (!customerViewJson.get(InfinityConstants.Username).getAsString()
                    .equals(customerViewJson.get(InfinityConstants.id).getAsString())) {
                isAssociated = true;
            }
        }

        customerViewJson.addProperty(InfinityConstants.primaryCustomerId, primaryCustomerId);

        if (StringUtils.isNotBlank(customerId)
                && (!customerId.equals(primaryCustomerId) || (customerId.equals(primaryCustomerId)
                        && customerViewJson.has("Username") && !customerViewJson.get("Username").isJsonNull()
                        && !customerId.equals(customerViewJson.get("Username").getAsString())))) {
            // CustomerDTO userDTO = (CustomerDTO) new CustomerDTO().loadDTO(customerId);
            customerViewJson.addProperty(InfinityConstants.isProfileExist, "true");
            // customerViewJson.addProperty(InfinityConstants.isEnrolled, "" + userDTO.getIsEnrolled());
            // customerViewJson.addProperty("CustomerStatus_id", userDTO.getStatus_id());
        } else if (customerId.equals(primaryCustomerId)) {
            customerViewJson.addProperty(InfinityConstants.isProfileExist, "false");
            // customerViewJson.addProperty(InfinityConstants.isEnrolled, "" + "false");
        }

        if (!isAssociated) {
            getCustomerType(customerViewJson, customerViewJson.get(InfinityConstants.id).getAsString(), headerMap, customerDTO.getCompanyLegalUnit());
            customerViewJson.remove(InfinityConstants.id);
            customerViewJson.remove(InfinityConstants.Customer_id);
        } else {
            isAssociated(customerViewJson, headerMap);
            customerViewJson.getAsJsonObject().addProperty(InfinityConstants.id, customerId);
            customerViewJson.addProperty("Customer_id", customerId);
        }

        String statusId = customerViewJson.has("CustomerStatus_id")
                ? customerViewJson.get("CustomerStatus_id").getAsString()
                : "";
        String isEnrolled = customerViewJson.has("isEnrolled") ? customerViewJson.get("isEnrolled").getAsString() : "";

        CredentialCheckerDTO credentialCheckerDTO = new CredentialCheckerDTO();
        credentialCheckerDTO.setUserName(customerDTO.getUserName());
        credentialCheckerDTO.setLinktype(HelperMethods.CREDENTIAL_TYPE.ACTIVATION.toString());
        credentialCheckerDTO = (CredentialCheckerDTO) credentialCheckerDTO.loadDTO();
        customerViewJson.addProperty(DBPUtilitiesConstants.IS_CUSTOMER_ENROLLED, isEnrolled);
        customerViewJson.addProperty(DBPUtilitiesConstants.CUSTOMER_STATUS, statusId);

        if (credentialCheckerDTO == null) {
            customerViewJson.addProperty(DBPUtilitiesConstants.IS_ACTIVATION_LINK_SENT, "false");
        } else if (credentialCheckerDTO != null) {
            customerViewJson.addProperty(DBPUtilitiesConstants.IS_ACTIVATION_LINK_SENT, "true");
        }

        customerViewJson.addProperty("isCustomerAccessiable", true);
        basicResult.add("customerbasicinfo_view", customerViewJson);

        JsonObject configuration = new JsonObject();
        configuration.addProperty("value", customerViewJson.get("accountLockoutTime").getAsString());
        basicResult.add("Configuration", configuration);
        String currentStatus;

        String lockedOnTS = "";
        if (customerViewJson.has("lockedOn")) {
            lockedOnTS = customerViewJson.get("lockedOn").getAsString();
        }
        if (customerViewJson.get("CustomerStatus_id").getAsString()
                .equalsIgnoreCase(StatusEnum.SID_CUS_LOCKED.name())) {
            currentStatus = "LOCKED";
        } else if (customerViewJson.get("CustomerStatus_id").getAsString()
                .equalsIgnoreCase(StatusEnum.SID_CUS_SUSPENDED.name())) {
            currentStatus = "SUSPENDED";
        } else if (customerViewJson.get("CustomerStatus_id").getAsString()
                .equalsIgnoreCase(StatusEnum.SID_CUS_ACTIVE.name())) {
            currentStatus = "ACTIVE";
        } else {
            currentStatus = "NEW";
        }

        if (customerViewJson.get("CustomerStatus_id").getAsString()
                .equalsIgnoreCase(StatusEnum.SID_CUS_LOCKED.name())) {

            if (StringUtils.isNotBlank(lockedOnTS)) {
                String lockDuration = "0";

                if (customerViewJson.has("accountLockoutTime")) {
                    lockDuration = customerViewJson.get("accountLockoutTime").getAsString();
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                Calendar elapsedLockedOnDate = Calendar.getInstance();
                try {
                    elapsedLockedOnDate.setTime(dateFormat.parse(lockedOnTS));
                } catch (ParseException e) {

                }
                elapsedLockedOnDate.add(Calendar.MINUTE, Integer.parseInt(lockDuration));
                Calendar currentDate = Calendar.getInstance();

              /*  if (elapsedLockedOnDate.before(currentDate)) {
                    // Time has been elapsed. Calling unlock service
                    postParametersMap = new HashMap<String, Object>();
                    postParametersMap.put("id", customerId);
                    postParametersMap.put("lockCount", "0");
                    postParametersMap.put("lockedOn", "");
                    JsonObject unlockResponse = ServiceCallHelper.invokeServiceAndGetJson(postParametersMap, headerMap,
                            URLConstants.CUSTOMER_UPDATE);

                    basicResult.addProperty("unlockStatus", unlockResponse.toString());
                    if (unlockResponse == null || !unlockResponse.has(Param.OPSTATUS)
                            || unlockResponse.get(Param.OPSTATUS).getAsInt() != 0) {
                        ErrorCodeEnum.ERR_20538.setErrorCode(basicResult);
                        dbxResult.setResponse(basicResult);
                        return dbxResult;
                    }
                    // set updated status
                    currentStatus = "ACTIVE";
                }
                */
            }
        }

        JsonObject statusResponse = new JsonObject();
        statusResponse.addProperty("LockedOn", lockedOnTS);
        statusResponse.addProperty("Status", currentStatus);
        customerViewJson.add("OLBCustomerFlags", statusResponse);
        if (customerViewJson.get("CustomerType_id").getAsString()
                .equalsIgnoreCase(HelperMethods.getCustomerTypes().get("Prospect"))) {
            // Add address in basic information if the customer is of type prospect

            postParametersMap = new HashMap<String, Object>();
            postParametersMap.put(DBPUtilitiesConstants.FILTER, "CustomerId eq '" + customerId + "'");
            postParametersMap.put(DBPUtilitiesConstants.SELECT,
                    "Address_id,AddressType,AddressLine1,AddressLine2,ZipCode,CityName,City_id,RegionName,Region_id,RegionCode,CountryName,Country_id,CountryCode,isPrimary");
            JsonObject readCustomerAddr = ServiceCallHelper.invokeServiceAndGetJson(postParametersMap, headerMap,
                    URLConstants.CUSTOMER_ADDRESS_VIEW_GET);

            if (readCustomerAddr == null || !readCustomerAddr.has(Param.OPSTATUS)
                    || readCustomerAddr.get(Param.OPSTATUS).getAsInt() != 0
                    || !readCustomerAddr.has("customeraddress_view")) {
                ErrorCodeEnum.ERR_20881.setErrorCode(basicResult);

                dbxResult.setResponse(basicResult);
                return dbxResult;

            }
            JsonArray addressDataset = readCustomerAddr.get("customeraddress_view").getAsJsonArray();
            customerViewJson.add("Addresses", addressDataset);

        }

        dbxResult =

                getCustomerRequestNotificationCount(customerDTO, headerMap);
        if (dbxResult.getResponse() != null) {
            JsonObject jsonObject = (JsonObject) dbxResult.getResponse();
            for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                customerViewJson.add(entry.getKey(), entry.getValue());
            }
        }

        basicResult.add("customerbasicinfo_view", customerViewJson);

        dbxResult.setResponse(basicResult);

        return dbxResult;
    }

    public void getCustomerType(JsonObject json, String id, Map<String, Object> headerMap, String LegalEntityId) {

        String CustomerType_id = "";

        if (IntegrationTemplateURLFinder.isIntegrated) {
            String filter = InfinityConstants.customerId + DBPUtilitiesConstants.EQUAL + id + 
            		DBPUtilitiesConstants.AND + InfinityConstants.LegalEntityId + DBPUtilitiesConstants.EQUAL + LegalEntityId;
            Map<String, Object> input = new HashMap<String, Object>();
            input.put(DBPUtilitiesConstants.FILTER, filter);
            JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
                    URLConstants.CONTRACT_CUSTOMERS_GET);
            if (response.has(DBPDatasetConstants.DATASET_CONTRACT_CUSTOMERS)) {
                JsonElement jsonElement = response.get(DBPDatasetConstants.DATASET_CONTRACT_CUSTOMERS);
                if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {

                    for (JsonElement arrayElement : jsonElement.getAsJsonArray()) {
                        filter = InfinityConstants.id + DBPUtilitiesConstants.EQUAL
                                + arrayElement.getAsJsonObject().get(InfinityConstants.contractId).getAsString();
                        input = new HashMap<String, Object>();
                        input.put(DBPUtilitiesConstants.FILTER, filter);
                        JsonObject jsonObject = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
                                URLConstants.CONTRACT_GET);
                        input.clear();
                        if (jsonObject.has(DBPDatasetConstants.DATASET_CONTRACT)) {
                            jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CONTRACT);
                            if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                                JsonArray jsonArray = jsonElement.getAsJsonArray();
                                jsonObject = jsonArray.get(0).getAsJsonObject();
                                if (StringUtils.isBlank(CustomerType_id) || !CustomerType_id
                                        .contains(jsonObject.get(InfinityConstants.serviceType).getAsString())) {
                                    if (StringUtils.isNotBlank(CustomerType_id)) {
                                        CustomerType_id += ",";
                                    }
                                    CustomerType_id += jsonObject.get(InfinityConstants.serviceType).getAsString();
                                }
                            }
                        }
                    }

                }
            }
        } else {
            String filter = "id" + DBPUtilitiesConstants.EQUAL + id;

            Map<String, Object> input = new HashMap<String, Object>();
            input.put(DBPUtilitiesConstants.FILTER, filter);
            JsonObject result = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
                    URLConstants.MEMBERSHIP_GET);

            if (result.has(DBPDatasetConstants.DATASET_MEMBERSHIP)) {
                JsonArray jsonArray = result.get(DBPDatasetConstants.DATASET_MEMBERSHIP).isJsonArray()
                        ? result.get(DBPDatasetConstants.DATASET_MEMBERSHIP).getAsJsonArray()
                        : new JsonArray();
                if (jsonArray.size() > 0) {
                    result = jsonArray.get(0).getAsJsonObject();
                    if (result.has(InfinityConstants.isBusinessType)
                            && !result.get(InfinityConstants.isBusinessType).isJsonNull()) {
                        if (Boolean.parseBoolean(result.get(InfinityConstants.isBusinessType).getAsString())
                                || "1".equals(result.get(InfinityConstants.isBusinessType).getAsString())) {
                            CustomerType_id = HelperMethods.getCustomerTypes().get("Business");

                        } else {
                            CustomerType_id = HelperMethods.getCustomerTypes().get("Retail");
                        }
                    }
                }
            }
        }

        if (StringUtils.isBlank(CustomerType_id) && json.has(InfinityConstants.CustomerType_id)
                && !json.get(InfinityConstants.CustomerType_id).isJsonNull()) {
            CustomerType_id = json.get(InfinityConstants.CustomerType_id).getAsString();
        } else if (StringUtils.isBlank(CustomerType_id) && json.has(InfinityConstants.CustomerType_Id)
                && !json.get(InfinityConstants.CustomerType_Id).isJsonNull()) {
            CustomerType_id = json.get(InfinityConstants.CustomerType_Id).getAsString();
        } else if (StringUtils.isBlank(CustomerType_id) && json.has(InfinityConstants.CustomerTypeId)
                && !json.get(InfinityConstants.CustomerTypeId).isJsonNull()) {
            CustomerType_id = json.get(InfinityConstants.CustomerTypeId).getAsString();
        }
        if (CustomerType_id.contains(HelperMethods.getCustomerTypes().get("Business"))) {
            json.addProperty("isBusiness", "true");
        } else {
            json.addProperty("isBusiness", "false");
        }

        json.addProperty("CustomerType_id", CustomerType_id);

    }

    public DBXResult getCustomerRequestNotificationCount(CustomerDTO customerDTO, Map<String, Object> headerMap) {
        DBXResult dbxResult = new DBXResult();
        JsonObject jsonObject = new JsonObject();
        if (customerDTO == null || StringUtils.isBlank(customerDTO.getId())) {
            // ErrorCodeEnum.ERR_20865.setErrorCode(jsonObject);
            dbxResult.setResponse(jsonObject);
            return dbxResult;
        }
        Map<String, Object> postParametersMap = new HashMap<String, Object>();
        postParametersMap.put(DBPUtilitiesConstants.FILTER, "customerId eq '" + customerDTO.getId() + "'");

        JsonObject result = ServiceCallHelper.invokeServiceAndGetJson(postParametersMap, headerMap,
                URLConstants.CARD_REQUEST_NOTIFICATION_COUNT_VIEW);

        if (result == null || !result.has("card_request_notification_count_view")) {
            // ErrorCodeEnum.ERR_20302.setErrorCode(jsonObject);
            dbxResult.setResponse(jsonObject);
            return dbxResult;
        }

        int totalRequestCount = 0, totalNotificationCount = 0;
        JsonArray countArray = result.get("card_request_notification_count_view").getAsJsonArray();
        JsonObject currRecordJSONObject = null;

        int currRequestCount;
        for (int indexVar = 0; indexVar < countArray.size(); indexVar++) {
            if (countArray.get(indexVar).isJsonObject()) {
                currRecordJSONObject = countArray.get(indexVar).getAsJsonObject();

                if (currRecordJSONObject.has("reqType")) {
                    currRequestCount = 0;
                    if (currRecordJSONObject.has("requestcount")) {
                        currRequestCount = currRecordJSONObject.get("requestcount").getAsInt();
                    }
                    if (currRecordJSONObject.has("reqType")) {
                        if (currRecordJSONObject.get("reqType").getAsString()
                                .equalsIgnoreCase(DTOConstants.REQUEST_TYPE_IDENTIFIER)) {
                            totalRequestCount += currRequestCount;
                        } else if (currRecordJSONObject.get("reqType").getAsString()
                                .equalsIgnoreCase(DTOConstants.NOTIFICATION_TYPE_IDENTIFIER)) {
                            totalNotificationCount += currRequestCount;
                        }
                    }
                }
            }

            jsonObject.addProperty("requestCount", String.valueOf(totalRequestCount));
            jsonObject.addProperty("notificationCount", String.valueOf(totalNotificationCount));

            dbxResult.setResponse(jsonObject);
        }

        return dbxResult;
    }

    @Override
    public DBXResult getCustomerDetailsToAdmin(CustomerDTO customerDTO, Map<String, Object> headerMap) {

        UserManagementBackendDelegateImpl backendDelegateImpl = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(UserManagementBackendDelegateImpl.class);
        DBXResult dbxResult = backendDelegateImpl.get(customerDTO, headerMap);

        if (dbxResult.getResponse() != null) {
            customerDTO = (CustomerDTO) dbxResult.getResponse();
        }

        String currencyCode = HelperMethods.getCurrencyCode(customerDTO.getCountryCode());

        try {
            customerDTO.setDateOfBirth(
                    HelperMethods.convertDateFormat(customerDTO.getDateOfBirth(), "yyyy-MM-dd'T'hh:mm:ss'Z'"));
        } catch (ParseException e) {
        	alert.prepareError("Exception", e).log();
        }

        JsonObject record = DTOUtils.getJsonObjectFromObject(customerDTO, true);
        record.addProperty("currencyCode", currencyCode);
        record.addProperty("dateOfBirth", record.get("DateOfBirth").getAsString());

        CommunicationBackendDelegateImpl backendDelegate = new CommunicationBackendDelegateImpl();

        CustomerCommunicationDTO communicationDTO = new CustomerCommunicationDTO();
        communicationDTO.setCustomer_id(customerDTO.getId());

        dbxResult = backendDelegate.get(communicationDTO, headerMap);
        if (dbxResult.getResponse() != null) {
            List<CustomerCommunicationDTO> dtoList = (List<CustomerCommunicationDTO>) dbxResult.getResponse();
            if (dtoList != null && dtoList.size() > 0) {
                for (CustomerCommunicationDTO rec : dtoList) {
                    if (rec.getIsPrimary()) {
                        String type = rec.getType_id();
                        String key = "";
                        if ("COMM_TYPE_EMAIL".equalsIgnoreCase(type)) {
                            key = "Email";
                        } else {
                            key = "Phone";
                        }
                        record.addProperty(key, rec.getValue());
                    }
                }
            }
        }

        dbxResult.setResponse(record);
        return dbxResult;
    }

    @Override
    public DBXResult checkifUserEnrolled(CustomerDTO customerDTO, Map<String, Object> headerMap) {

        DBXResult dbxResult = new DBXResult();
        JsonObject jsonObject = new JsonObject();
        dbxResult.setResponse(jsonObject);

        String filter = "";

        if (StringUtils.isNotBlank(customerDTO.getSsn())) {
            filter += "Ssn" + DBPUtilitiesConstants.EQUAL + customerDTO.getSsn();
        }

        if (StringUtils.isNotBlank(customerDTO.getLastName())) {
            if (StringUtils.isNotBlank(filter)) {
                filter += DBPUtilitiesConstants.AND;
            }

            filter += "LastName" + DBPUtilitiesConstants.EQUAL + customerDTO.getLastName();
        }

        if (StringUtils.isNotBlank(customerDTO.getDateOfBirth())) {
            if (StringUtils.isNotBlank(filter)) {
                filter += DBPUtilitiesConstants.AND;
            }

            filter += "DateOfBirth" + DBPUtilitiesConstants.EQUAL + customerDTO.getDateOfBirth();
        }

        Map<String, Object> inputParams = new HashMap<String, Object>();
        inputParams.put(DBPUtilitiesConstants.FILTER, filter);

        JsonObject customerObject = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
                URLConstants.CUSTOMER_GET);

        if (!customerObject.has("customer")) {
            dbxResult.setError(ErrorCodeEnum.ERR_10024);
            return dbxResult;
        }

        JsonArray customers = customerObject.get("customer").getAsJsonArray();

        JsonObject customer = null;

        if (customers.size() > 0) {
            for (int i = 0; i < customers.size(); i++) {
                if (!HelperMethods.getBusinessUserTypes()
                        .contains(customers.get(i).getAsJsonObject().get("CustomerType_id").getAsString())) {
                    customer = customers.get(i).getAsJsonObject();
                    break;
                }
            }
        }

        if (customer != null) {
            String isUserEnrolled = "true";
            Param p = null;
            boolean isEnrolled = Boolean.parseBoolean(customer.get(DBPUtilitiesConstants.IS_ENROLLED).getAsString());
            boolean isEnrolledFromSpotlight = Boolean
                    .parseBoolean(customer.get(DBPUtilitiesConstants.IS_ENROLLED_FROM_SPOTLIGHT).getAsString())
                    || "1".equals(customer.get(DBPUtilitiesConstants.IS_ENROLLED_FROM_SPOTLIGHT).getAsString());

            String password = new String();

            if (customer.getAsJsonObject().has("Password") && !customer.get("Password").isJsonNull()
                    && StringUtils.isNotBlank(customer.get("Password").getAsString())) {
                password = customer.get("Password").getAsString();
            }

            if (isEnrolled) {
                isUserEnrolled = "true";
                jsonObject.addProperty("result", "User Already Enrolled");
            } else if (StringUtils.isBlank(password)
                    || (StringUtils.isNotBlank(password) && !isEnrolledFromSpotlight)) {
                p = new Param("result", "User Not Enrolled", DBPConstants.FABRIC_STRING_CONSTANT_KEY);
                isUserEnrolled = "false";
                jsonObject.addProperty("result", "User Not Enrolled");
                String userId = customer.get("id").getAsString();
                JsonObject communication = getCommunicationData(userId, headerMap);
                JsonObject requestPayload = getRequestPayload(customerDTO);
                String communicationString = communication.toString();
                String requestPayloadString = requestPayload.toString();
                jsonObject.addProperty("communication", communicationString);
                jsonObject.addProperty("requestPayload", requestPayloadString);
            }

            jsonObject.addProperty("isUserEnrolled", isUserEnrolled);
        } else {
            JsonObject requestPayload = getRequestPayload(customerDTO);
            String requestPayloadString = requestPayload.toString();
            jsonObject.addProperty("requestPayload", requestPayloadString);
            jsonObject.addProperty(DBPUtilitiesConstants.VALIDATION_ERROR, "No Record Found");
        }

        dbxResult.setResponse(jsonObject);

        return dbxResult;
    }

    private JsonObject getRequestPayload(CustomerDTO customerDTO) {
        JsonObject payload = new JsonObject();
        payload.addProperty(DBPUtilitiesConstants.C_SSN, customerDTO.getSsn());
        payload.addProperty(DBPInputConstants.CUSTOMER_LAST_NAME, customerDTO.getLastName());
        payload.addProperty(DBPUtilitiesConstants.C_DOB, customerDTO.getDateOfBirth());
        return payload;
    }

    public JsonObject getCommunicationData(String user_id, Map<String, Object> headerMap) {

        JsonObject communication = new JsonObject();

        CommunicationBackendDelegateImpl backendDelegateImpl = new CommunicationBackendDelegateImpl();

        CustomerCommunicationDTO dto = new CustomerCommunicationDTO();
        dto.setCustomer_id(user_id);

        DBXResult dbxResult = backendDelegateImpl.get(dto, headerMap);

        List<CustomerCommunicationDTO> dtoList = (List<CustomerCommunicationDTO>) dbxResult.getResponse();

        JsonArray phone = new JsonArray();
        JsonArray email = new JsonArray();
        JsonObject contact = new JsonObject();

        if (dtoList != null && dtoList.size() > 0) {
            for (CustomerCommunicationDTO record : dtoList) {
                contact = new JsonObject();
                if (record.getType_id().equals(DBPUtilitiesConstants.COMM_TYPE_EMAIL)) {
                    contact.addProperty("unmasked", record.getValue());
                    if (record.getIsPrimary()) {
                        contact.addProperty("isPrimary", "true");
                    }
                    email.add(contact);
                } else {
                    String mobile = record.getValue();
                    contact.addProperty("unmasked", mobile);
                    if (record.getIsPrimary()) {
                        contact.addProperty("isPrimary", "true");
                    }
                    phone.add(contact);
                }
            }
            communication.add("phone", phone);
            communication.add("email", email);
        }

        return communication;
    }

    @Override
    public DBXResult sendCustomerUnlockEmail(CustomerDTO customerDTO, Map<String, Object> headerMap) {

        DBXResult dbxResult = new DBXResult();
        JsonObject jsonObject = new JsonObject();

        customerDTO = (CustomerDTO) customerDTO.loadDTO();

        if (customerDTO == null) {
            jsonObject.addProperty("mailRequestSent", "false");
            ErrorCodeEnum.ERR_10192.setErrorCode(jsonObject);
            dbxResult.setResponse(jsonObject);
            return dbxResult;
        }

        CommunicationBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(CommunicationBackendDelegate.class);
        CustomerCommunicationDTO communicationDTO = new CustomerCommunicationDTO();
        communicationDTO.setCustomer_id(customerDTO.getId());
        dbxResult = backendDelegate.getPrimaryCommunicationForLogin(communicationDTO, headerMap);
        String email = "";
        if (dbxResult.getResponse() != null) {
            JsonObject communicaiton = (JsonObject) dbxResult.getResponse();
            communicaiton = communicaiton.has(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION)
                    ? communicaiton.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION).getAsJsonObject()
                    : new JsonObject();
            email = communicaiton.has(DTOConstants.EMAIL) ? communicaiton.get(DTOConstants.EMAIL).getAsString() : "";
        }

        if (StringUtils.isBlank(email)) {
            jsonObject.addProperty("mailRequestSent", "false");
            ErrorCodeEnum.ERR_10193.setErrorCode(jsonObject);
            dbxResult.setResponse(jsonObject);
            return dbxResult;
        }

        String filter = "UserName" + DBPUtilitiesConstants.EQUAL + customerDTO.getUserName();
        Map<String, Object> inputParam = new HashMap<String, Object>();
        inputParam.put(DBPUtilitiesConstants.FILTER, filter);

        JsonObject checkerResult = ServiceCallHelper.invokeServiceAndGetJson(inputParam, headerMap,
                URLConstants.CREDENTIAL_CHECKER_GET);
        if (checkerResult.has(DBPDatasetConstants.DATASET_CREDENTIALCHECKER)) {

            JsonArray existingRecords = checkerResult.get(DBPDatasetConstants.DATASET_CREDENTIALCHECKER)
                    .getAsJsonArray();

            for (int i = 0; i < existingRecords.size(); i++) {

                if (existingRecords.get(i).getAsJsonObject().get("linktype").getAsString()
                        .equals(HelperMethods.CREDENTIAL_TYPE.UNLOCK.toString())) {
                    String existingToken = existingRecords.get(i).getAsJsonObject().get("id").getAsString();
                    inputParam = new HashMap<String, Object>();
                    inputParam.put("id", existingToken);
                    ServiceCallHelper.invokeServiceAndGetJson(inputParam, headerMap,
                            URLConstants.CREDENTIAL_CHECKER_DELETE);
                }
            }
        }

        String activationToken = UUID.randomUUID().toString();
        Map<String, Object> map = new HashMap<>();
        map.put("id", activationToken);
        map.put("UserName", customerDTO.getUserName());
        map.put("linktype", HelperMethods.CREDENTIAL_TYPE.UNLOCK.toString());
        map.put("createdts", HelperMethods.getCurrentTimeStamp());
        JsonObject createCredential = ServiceCallHelper.invokeServiceAndGetJson(map, headerMap,
                URLConstants.CREDENTIAL_CHECKER_CREATE);

        if (!HelperMethods.hasError(createCredential)) {

            String link;

            link = URLFinder.getServerRuntimeProperty(URLConstants.DBX_CUSTOMER_UNLOCK_LINK) + "?qp=UNLOCK_ACCOUNT";
                   // + new String(java.util.Base64.getEncoder().encode(activationToken.getBytes()));

            PasswordLockoutSettingsDTO settingsDTO = (PasswordLockoutSettingsDTO) new PasswordLockoutSettingsDTO()
                    .loadDTO();
            Map<String, Object> input = new HashMap<>();
            input.put("Subscribe", "true");
            input.put("FirstName", customerDTO.getFirstName());
            input.put("EmailType", "UNLOCK_CUSTOMER");
            input.put("LastName", customerDTO.getLastName());
            JSONObject addContext = new JSONObject();
            addContext.put("unlockAccountLink", link);
            addContext.put("userName", customerDTO.getUserName());
            addContext.put("linkExpiry", String.valueOf(Math.floorDiv(settingsDTO.getRecoveryEmailLinkValidity(), 60)));
            input.put("AdditionalContext", KMSUtil.getOTPContent(null, null, addContext));
            input.put("Email", email);

            headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
            HelperMethods.callApiAsync(input, headerMap, URLConstants.DBX_SEND_EMAIL_ORCH);
            jsonObject.addProperty("mailRequestSent", "true");
            dbxResult.setResponse(jsonObject);
            
            diagnostic.debug("Customer id in unlock flow ##:"+ customerDTO.getId());
            
            Map<String, Object> postParametersMap = new HashMap<String, Object>();
				
				 postParametersMap = new HashMap<String, Object>();
                 postParametersMap.put("id", customerDTO.getId());
                 postParametersMap.put("lockCount", "0");
                 //postParametersMap.put("lockedOn", "");
                 JsonObject unlockResponse = ServiceCallHelper.invokeServiceAndGetJson(postParametersMap, headerMap,
                         URLConstants.CUSTOMER_UPDATE);

                 //basicResult.addProperty("unlockStatus", unlockResponse.toString());
                 if (unlockResponse == null || !unlockResponse.has(Param.OPSTATUS)
                         || unlockResponse.get(Param.OPSTATUS).getAsInt() != 0) {
                	 diagnostic.debug("Unlock customer flow unlock count failed ##");
                 }

            return dbxResult;
        }

        jsonObject.addProperty("mailRequestSent", "false");
        dbxResult.setResponse(jsonObject);

        return dbxResult;
    }

    @Override
    public DBXResult fetchCustomerIdForEnrollment(CustomerDTO customerDTO, Map<String, Object> headerMap)
            throws ApplicationException {

        final String IS_Integrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);
        try {
            if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")) {
                return fetchCustomerIdForEnrollmentForT24(customerDTO, headerMap);
            }
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("Exception occured while enrolling customer in Infinity Digital banking through T24"
                    + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10738);
        }
        DBXResult result = new DBXResult();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put(DBPUtilitiesConstants.FILTER, "id" + DBPUtilitiesConstants.EQUAL + customerDTO.getId());
        try {
            boolean isPresentInDBXDB = false;
            String customerIdinDB = customerDTO.getId();
            String coreId = customerDTO.getId();
            inputParams.put(DBPUtilitiesConstants.FILTER, "id" + DBPUtilitiesConstants.EQUAL + customerIdinDB);
            JsonObject customerJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
                    URLConstants.CUSTOMER_GET);
            isPresentInDBXDB = JSONUtil.hasKey(customerJson, MWConstants.OPSTATUS)
                    && JSONUtil.getString(customerJson, MWConstants.OPSTATUS).equalsIgnoreCase("0")
                    && JSONUtil.hasKey(customerJson, DBPDatasetConstants.DATASET_CUSTOMER)
                    && customerJson.get(DBPDatasetConstants.DATASET_CUSTOMER).getAsJsonArray().size() > 0;

            BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();

            if (isPresentInDBXDB)
                backendIdentifierDTO.setCustomer_id(customerIdinDB);
            else
                backendIdentifierDTO.setBackendId(coreId);
            backendIdentifierDTO.setBackendType("CORE");
            BackendIdentifierDTO identifierDTO = new BackendIdentifierDTO();
            try {
                DBXResult dbxResult = DBPAPIAbstractFactoryImpl
                        .getBackendDelegate(BackendIdentifiersBackendDelegate.class)
                        .get(backendIdentifierDTO, headerMap);
                identifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
            } catch (ApplicationException e1) {
                alert.prepareError("Exception occured while fetching party backendidentifier ID" + customerDTO.getId()).log();
                return result;
            }
            if (identifierDTO != null) {
                customerIdinDB = identifierDTO.getCustomer_id();
                coreId = identifierDTO.getBackendId();
            }
            inputParams.clear();
            inputParams.put(DBPUtilitiesConstants.FILTER, "id" + DBPUtilitiesConstants.EQUAL + customerIdinDB);
            JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
                    URLConstants.CUSTOMER_GET);
            if (JSONUtil.hasKey(response, MWConstants.OPSTATUS)
                    && JSONUtil.getString(response, MWConstants.OPSTATUS).equalsIgnoreCase("0")
                    && JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_CUSTOMER)
                    && response.get(DBPDatasetConstants.DATASET_CUSTOMER).getAsJsonArray().size() > 0) {
                result.setResponse(DTOUtils.loadJsonObjectIntoObject(
                        response.get(DBPDatasetConstants.DATASET_CUSTOMER).getAsJsonArray().get(0).getAsJsonObject(),
                        CustomerDTO.class, true));
                CustomerDTO dto = (CustomerDTO) result.getResponse();
                if (dto.getIsEnrolled()) {
                    throw new ApplicationException(ErrorCodeEnum.ERR_10748);
                }
                if (!"1".equals(dto.getIsEnrolledFromSpotlight())) {
                    inputParams.clear();
                    inputParams.put("id", dto.getId());
                    inputParams.put("isEnrolledFromSpotlight", "1");
                    ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap, URLConstants.CUSTOMER_UPDATE);
                }
                CustomerGroupDTO customerGroupDTO = new CustomerGroupDTO();
                CustomerGroupBusinessDelegate customerGroup = DBPAPIAbstractFactoryImpl
                        .getBusinessDelegate(CustomerGroupBusinessDelegate.class);
                customerGroupDTO.setCustomerId(dto.getId());
                customerGroupDTO.setGroupId("DEFAULT_GROUP");
                List<CustomerGroupDTO> list = customerGroup.getCustomerGroup(customerGroupDTO, headerMap);
                if (list != null && list.isEmpty())
                    customerGroup.createCustomerGroup(customerGroupDTO, headerMap);
            }
        } catch (Exception e) {
            alert.prepareError("Exception occured while enrolling customer in Infinity Digital banking" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10738);
        }
        return result;
    }

    private DBXResult fetchCustomerIdForEnrollmentForT24(CustomerDTO customerDTO, Map<String, Object> headersMap)
            throws ApplicationException {
        DBXResult result = new DBXResult();
        CustomerDTO responseDTO = new CustomerDTO();
        Map<String, Object> inputParams = new HashMap<>();
        boolean isPresentInDBXDB = false;
        String customerIdinDB = customerDTO.getId();
        String t24Id = customerDTO.getId();

        if (StringUtils.isBlank(customerDTO.getId())) {
            return result;
        }

        inputParams.put(DBPUtilitiesConstants.FILTER, "id" + DBPUtilitiesConstants.EQUAL + customerIdinDB);
        JsonObject customerJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                URLConstants.CUSTOMER_GET);
        isPresentInDBXDB = JSONUtil.hasKey(customerJson, MWConstants.OPSTATUS)
                && JSONUtil.getString(customerJson, MWConstants.OPSTATUS).equalsIgnoreCase("0")
                && JSONUtil.hasKey(customerJson, DBPDatasetConstants.DATASET_CUSTOMER)
                && customerJson.get(DBPDatasetConstants.DATASET_CUSTOMER).getAsJsonArray().size() > 0;

        BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
        JsonObject customerrec = customerJson.get("customer").getAsJsonArray().get(0).getAsJsonObject();
        String homeLegalEntity = JSONUtil.getString(customerrec,DBPDatasetConstants.HOME_LEGALENTITY);
        
        if (isPresentInDBXDB)
        {
            backendIdentifierDTO.setCustomer_id(customerIdinDB);
            backendIdentifierDTO.setCompanyLegalUnit(homeLegalEntity);
        }
        else {
            backendIdentifierDTO.setBackendId(t24Id);
            backendIdentifierDTO.setCompanyLegalUnit(homeLegalEntity);
        }
        backendIdentifierDTO.setBackendType(IntegrationTemplateURLFinder.getBackendURL(InfinityConstants.BackendType));
        BackendIdentifierDTO identifierDTO = new BackendIdentifierDTO();
        try {
            DBXResult dbxResult = DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class)
                    .get(backendIdentifierDTO, headersMap);
            identifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
        } catch (ApplicationException e1) {
            alert.prepareError("Exception occured while fetching party backendidentifier ID" + customerDTO.getId()).log();
            return result;
        }
        if (identifierDTO != null) {
            customerIdinDB = identifierDTO.getCustomer_id();
            t24Id = identifierDTO.getBackendId();
        }
        try {
            if (!isPresentInDBXDB && identifierDTO == null) {

                HelperMethods.addJWTAuthHeader(headersMap, AuthConstants.PRE_LOGIN_FLOW);
                inputParams.put("customerId", t24Id);
                /* LegalEntity id not available */
                headersMap.put("companyId",
                        EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE));
                JsonObject t24Response = ServiceCallHelper.invokeServiceAndGetJson(
                        ServiceId.T24ISUSER_INTEGRATION_SERVICE, null, OperationName.CORE_CUSTOMER_SEARCH, inputParams,
                        headersMap);
                JsonObject customerResponse = new JsonObject();
                if (!JSONUtil.hasKey(t24Response, DBPDatasetConstants.DATASET_CUSTOMERS)
                        || t24Response.get(DBPDatasetConstants.DATASET_CUSTOMERS).getAsJsonArray().size() < 0) {
                    throw new ApplicationException(ErrorCodeEnum.ERR_10741);
                }
                customerResponse = t24Response.get(DBPDatasetConstants.DATASET_CUSTOMERS).getAsJsonArray().get(0)
                        .getAsJsonObject();

                SystemConfigurationBusinessDelegate systemConfigBD = DBPAPIAbstractFactoryImpl
                        .getBusinessDelegate(SystemConfigurationBusinessDelegate.class);
                Integer userNameLength = Integer
                        .parseInt(systemConfigBD.getSystemConfigurationValue("USERNAME_LENGTH", headersMap));
                String customerId = String.valueOf(HelperMethods.getNumericId(userNameLength));
                inputParams.put("id", customerId);
                inputParams.put("UserName", inputParams.get("id"));
                inputParams.put("Status_id", DBPUtilitiesConstants.CUSTOMER_STATUS_NEW);
                inputParams.put("isEnrolledFromSpotlight", "1");
                inputParams.put("DateOfBirth", JSONUtil.getString(customerResponse, "dateOfBirth"));
                ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap, URLConstants.CUSTOMER_CREATE);

                SimpleDateFormat idformatter = new SimpleDateFormat("yyMMddHHmmssSSS");
                CustomerCommunicationDTO commDTO = new CustomerCommunicationDTO();
                commDTO.setIsNew(true);
                inputParams.clear();
                inputParams.put("Customer_id", customerId);
                inputParams.put("id", "CUS_" + idformatter.format(new Date()));
                inputParams.put("Type_id", DBPUtilitiesConstants.COMM_TYPE_EMAIL);
                inputParams.put("Value", JSONUtil.getString(customerResponse, "email"));
                commDTO.persist(inputParams, headersMap);
                inputParams.clear();
                inputParams.put("Customer_id", customerId);
                inputParams.put("id", "CUS_" + idformatter.format(new Date()));
                inputParams.put("Type_id", DBPUtilitiesConstants.COMM_TYPE_PHONE);
                inputParams.put("Value", JSONUtil.getString(customerResponse, "phone"));
                commDTO.persist(inputParams, headersMap);

                inputParams.clear();
                inputParams.put("id", UUID.randomUUID().toString());
                inputParams.put("Customer_id", customerId);
                inputParams.put("customerId", "1");
                inputParams.put("BackendId", customerDTO.getId());
                inputParams.put("BackendType",
                        IntegrationTemplateURLFinder.getBackendURL(InfinityConstants.BackendType));
                inputParams.put("identifier_name",
                        IntegrationTemplateURLFinder.getBackendURL(InfinityConstants.BackendCustomerIdentifierName));
                inputParams.put("sequenceNumber", "9");
                ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                        URLConstants.BACKENDIDENTIFIER_CREATE);
                CustomerGroupDTO customerGroupDTO = new CustomerGroupDTO();
                CustomerGroupBusinessDelegate customerGroup = DBPAPIAbstractFactoryImpl
                        .getBusinessDelegate(CustomerGroupBusinessDelegate.class);
                customerGroupDTO.setCustomerId(customerId);
                customerGroupDTO.setGroupId("DEFAULT_GROUP");
                customerGroup.createCustomerGroup(customerGroupDTO, headersMap);
                responseDTO.setId(customerId);
                responseDTO.setFirstName(JSONUtil.getString(customerResponse, "firstName"));
                responseDTO.setLastName(JSONUtil.getString(customerResponse, "lastName"));
                responseDTO.setUserName(customerId);
                responseDTO.setHomeLegalEntity(homeLegalEntity);

            } else {
                inputParams.put(DBPUtilitiesConstants.FILTER, "id" + DBPUtilitiesConstants.EQUAL + customerIdinDB);
                if (t24Id != customerIdinDB) {
                    customerJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                            URLConstants.CUSTOMER_GET);
                }
                CustomerDTO infintyCustomerDTO = (CustomerDTO) DTOUtils.loadJsonObjectIntoObject(
                        customerJson.get("customer").getAsJsonArray().get(0).getAsJsonObject(), CustomerDTO.class,
                        true);
                String scaenabled = EnvironmentConfigurationsHandler.getValue("IS_SCA_ENABLED");
                if (StringUtils.isNotBlank(scaenabled) && !Boolean.valueOf(scaenabled)) {
                    if (infintyCustomerDTO.getIsEnrolled()) {
                        throw new ApplicationException(ErrorCodeEnum.ERR_10748);
                    }
                }
                responseDTO.setId(infintyCustomerDTO.getId());
                responseDTO.setUserName(infintyCustomerDTO.getUserName());
                responseDTO.setFirstName(infintyCustomerDTO.getFirstName());
                responseDTO.setLastName(infintyCustomerDTO.getLastName());
                responseDTO.setIsEnrolled(infintyCustomerDTO.getIsEnrolled());
                responseDTO.setHomeLegalEntity(infintyCustomerDTO.getHomeLegalEntity());
            }
            result.setResponse(responseDTO);
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("Exception occured while creating Infinity Digital profile" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10741);
        }
        return result;
    }

    @Override
    public DBXResult getAddressTypes(Map<String, Object> map) {

        JsonObject jsonObject = ServiceCallHelper.invokeServiceAndGetJson(new HashMap<String, Object>(), map,
                URLConstants.ADDRESSTYPES_GET);

        DBXResult dbxResult = new DBXResult();
        JsonObject result = new JsonObject();

        if (jsonObject.has(DBPDatasetConstants.DATASET_ADDRESSTYPE)
                && !jsonObject.get(DBPDatasetConstants.DATASET_ADDRESSTYPE).isJsonNull()) {
            dbxResult.setResponse(jsonObject);
        } else {
            ErrorCodeEnum.ERR_10751.setErrorCode(result);
        }

        return dbxResult;
    }

    public JsonObject searchCustomerinT24(JsonArray recordsArray, Map<String, String> configurations,
            MemberSearchBean memberSearchBean, Map<String, Object> headerMap, boolean isCustomerSearch, PasswordHistoryManagement pm) {
        DBXResult dbxResult = new DBXResult();
        JsonObject processedResult = new JsonObject();
        JsonObject membershipJson = null;

        Map<String, Object> input = new HashMap<String, Object>();
        HelperMethods.addJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);
        if (StringUtils.isNotBlank(memberSearchBean.getCustomerEmail())) {
            input.put("emailId", memberSearchBean.getCustomerEmail());
        }
        if (StringUtils.isNotBlank(memberSearchBean.getCustomerName())) {
            input.put("lastName", memberSearchBean.getCustomerName());
        }
        if (StringUtils.isNotBlank(memberSearchBean.getCustomerPhone())) {
            input.put("contactNumber", memberSearchBean.getCustomerPhone());
        }
        if (StringUtils.isNotBlank(memberSearchBean.getMemberId())) {
            input.put("customerId", memberSearchBean.getMemberId());
        }
        if (StringUtils.isNotBlank(memberSearchBean.getDateOfBirth())) {
            input.put("dateOfBirth", memberSearchBean.getDateOfBirth());
        }
        JsonArray partyJsonArray = new JsonArray();

        if (StringUtils.isNotBlank(memberSearchBean.getCustomerId())) {
            input.put("customerId", memberSearchBean.getCustomerId());
        }

        if (!input.isEmpty()){
        	 String serviceId = ServiceId.T24ISUSER_INTEGRATION_SERVICE;
             String operationName = OperationName.CORE_CUSTOMER_SEARCH;
        	if (StringUtils.isNotBlank(memberSearchBean.getCompanyLegalUnit())) {
           
                 headerMap.put("companyId",
            		   memberSearchBean.getCompanyLegalUnit());
                  membershipJson = ServiceCallHelper.invokeServiceAndGetJson(serviceId, null, operationName, input,
                    headerMap);
        	}
        	else {
        		/* fall back */
        		 headerMap.put("companyId",
                         EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE));
                 membershipJson = ServiceCallHelper.invokeServiceAndGetJson(serviceId, null, operationName, input,
                         headerMap);
        	}
            if (null != membershipJson && JSONUtil.hasKey(membershipJson, DBPDatasetConstants.DATASET_CUSTOMERS)
                    && membershipJson.get(DBPDatasetConstants.DATASET_CUSTOMERS).isJsonArray()) {
                partyJsonArray = membershipJson.get(DBPDatasetConstants.DATASET_CUSTOMERS).getAsJsonArray();
            }
        }
        
        
        Set<String> sectorIdList = HelperMethods
                .splitString(configurations.get(BundleConfigurationHandler.BUSINESS_SECTORID_LIST), ",");

        String id = "";
        if (partyJsonArray.size() > 0) {
            BackendIdentifiersBackendDelegateimpl backendDelegateimpl = new BackendIdentifiersBackendDelegateimpl();

            for (int i = 0; i < partyJsonArray.size(); i++) {
                JsonObject jsonObject = partyJsonArray.get(i).getAsJsonObject();
                
                jsonObject.add("DateOfBirth", jsonObject.get("dateOfBirth"));
                jsonObject.add("PrimaryEmailAddress", jsonObject.get("email"));
                jsonObject.add("PrimaryPhoneNumber", jsonObject.get("phone"));
                jsonObject.add("FirstName", jsonObject.get("firstName"));
                jsonObject.addProperty("name",
                        (jsonObject.has("firstName") && !jsonObject.get("firstName").isJsonNull()
                                && StringUtils.isNotBlank(jsonObject.get("firstName").getAsString())
                                        ? jsonObject.get("firstName").getAsString()
                                        : "")
                                + " "
                                + (jsonObject.has("lastName") && !jsonObject.get("lastName").isJsonNull()
                                        && StringUtils.isNotBlank(jsonObject.get("lastName").getAsString())
                                                ? jsonObject.get("lastName").getAsString()
                                                : ""));
                jsonObject.add("LastName", jsonObject.get("lastName"));
                jsonObject.add("ssn", jsonObject.get("ssn"));
                jsonObject.add("Ssn", jsonObject.get("ssn"));
                jsonObject.add("SSN", jsonObject.get("ssn"));
                jsonObject.add("Gender", jsonObject.get("gender"));
                jsonObject.add("MaritalStatus_name", jsonObject.get("maritalStatus"));
                jsonObject.add("EmployementStatus_name", jsonObject.get("employmentStatus"));
                id = jsonObject.get("id").getAsString();
                BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
                backendIdentifierDTO.setBackendId(id);
                backendIdentifierDTO
                        .setBackendType(IntegrationTemplateURLFinder.getBackendURL(InfinityConstants.BackendType));
                backendIdentifierDTO.setCompanyLegalUnit(memberSearchBean.getCompanyLegalUnit());
               
                dbxResult = backendDelegateimpl.get(backendIdentifierDTO, headerMap);
                if (dbxResult.getResponse() != null) {
                	
                    backendIdentifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
                    CustomerDTO customerDTO = new CustomerDTO();
                    customerDTO = (CustomerDTO) customerDTO.loadDTO(backendIdentifierDTO.getCustomer_id());
                    if ((customerDTO.getLockCount() + 1) >= pm.getAccountLockoutThreshold()) {
						jsonObject.addProperty("Status_id", StatusEnum.SID_CUS_LOCKED.name());
					} else {
						jsonObject.addProperty("Status_id", customerDTO.getStatus_id());
					}
                    jsonObject.addProperty(InfinityConstants.isEnrolled, customerDTO.getIsEnrolled().toString());
                    jsonObject.addProperty(InfinityConstants.Username, customerDTO.getUserName());
                    jsonObject.addProperty(InfinityConstants.isProfileExist, "true");
                    jsonObject.addProperty(InfinityConstants.primaryCustomerId,
                            jsonObject.get(InfinityConstants.id).getAsString());
                    jsonObject.addProperty(InfinityConstants.CustomerType_id, customerDTO.getCustomerType_id());
                    getCustomerType(jsonObject, customerDTO.getId(), headerMap, customerDTO.getCompanyLegalUnit());
                    jsonObject.addProperty("legalEntityId", customerDTO.getCompanyLegalUnit());
                    jsonObject.add("CustomerType_id", jsonObject.get("CustomerTypeId"));
                    jsonObject.addProperty(InfinityConstants.homeLegalEntity, customerDTO.getHomeLegalEntity());
                    jsonObject.addProperty(InfinityConstants.id, customerDTO.getId());
                    
                } else {
                	
                   
                    jsonObject.addProperty(InfinityConstants.isProfileExist, "false");
                    jsonObject.addProperty(InfinityConstants.isEnrolled, "false");
                    jsonObject.addProperty(InfinityConstants.primaryCustomerId,
                            jsonObject.get(InfinityConstants.id).getAsString());
                    jsonObject.remove(InfinityConstants.id);
                    String sectorId = JSONUtil.getString(jsonObject, "sectorId");
                    
                   
                    if (sectorIdList.contains(sectorId)) {
                        jsonObject.addProperty("CustomerType_id", HelperMethods.getCustomerTypes().get("Business"));
                        jsonObject.addProperty("isBusiness", "true");
                    } else {
                        jsonObject.addProperty("isBusiness", "false");
                        jsonObject.addProperty("CustomerType_id", HelperMethods.getCustomerTypes().get("Retail"));
                    }
                    jsonObject.addProperty("Status_id", StatusEnum.SID_CUS_NEW.name());
                    jsonObject.add("CustomerTypeId", jsonObject.get("CustomerType_id"));
                }
                
                jsonObject.addProperty("sectorName", "");
                jsonObject.addProperty("branchId", memberSearchBean.getCompanyLegalUnit());
                jsonObject.addProperty("branchName", "");
                recordsArray.add(jsonObject);
            }
        }
        
        recordsArray = sortJsonArray(memberSearchBean, recordsArray);
        
        processedResult.addProperty("Status", "Records returned: " + recordsArray.size());

        processedResult.addProperty("TotalResultsFound", recordsArray.size());

        processedResult.add("records", recordsArray);

        dbxResult.setResponse(processedResult);

        processedResult.addProperty("Status", "Records returned: " + recordsArray.size());
        
        return processedResult;
    }

    public JsonArray sortJsonArray(MemberSearchBean memberSearchBean, JsonArray jsonArray) {

        class SearchNode implements Comparable<SearchNode> {
            String value;
            JsonObject jsonObject;

            @Override
            public int compareTo(SearchNode o) {
                return this.value.compareTo(o.value);
            }
        }

        PriorityQueue<SearchNode> priorityQueue;

        if ("ASC".equals(memberSearchBean.getSortDirection())) {
            priorityQueue = new PriorityQueue<SearchNode>();
        } else {
            priorityQueue = new PriorityQueue<SearchNode>(Collections.reverseOrder());
        }

        SearchNode node = new SearchNode();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            node = new SearchNode();
            node.value = jsonObject.get(memberSearchBean.getSortVariable()).getAsString();
            node.jsonObject = jsonObject;
            priorityQueue.add(node);
        }

        JsonArray finalJsonArray = new JsonArray();

        jsonArray = new JsonArray();
        while (!priorityQueue.isEmpty()) {
            jsonArray.add(priorityQueue.remove().jsonObject);
        }

        int lowIndex = memberSearchBean.getPageOffset() * memberSearchBean.getPageSize();

        int highIndex = lowIndex + memberSearchBean.getPageSize();

        for (int i = lowIndex; i < highIndex && i < jsonArray.size(); i++) {
            finalJsonArray.add(jsonArray.get(i));
        }

        return finalJsonArray;
    }

    public DBXResult getBasicInfo(JsonObject customerViewJson, Map<String, String> configurations,
            CustomerDTO customerDTO, Map<String, Object> headerMap, boolean isCustomerSearch, PasswordHistoryManagement pm) {
        DBXResult dbxResult = new DBXResult();

        final String IS_Integrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);

        String customerId = customerDTO.getId();
        String username = customerDTO.getUserName();
        String legalEntityId = customerDTO.getCompanyLegalUnit();
        JsonObject basicResult = new JsonObject();

        if (StringUtils.isBlank(customerId)) {
            if (StringUtils.isBlank(username)) {
                ErrorCodeEnum.ERR_20612.setErrorCode(basicResult);
                basicResult.addProperty("Status", "Failure");
                dbxResult.setResponse(basicResult);
                return dbxResult;
            }
        }

        if (StringUtils.isBlank(customerId)) {
            customerDTO = (CustomerDTO) customerDTO.loadDTO();
            if (customerDTO != null) {
                customerId = customerDTO.getId();
            } else {
                ErrorCodeEnum.ERR_20688.setErrorCode(basicResult);
                basicResult.addProperty("Status", "Failure");
                dbxResult.setResponse(basicResult);
                return dbxResult;
            }
        }

        JsonObject resultJsonObject = new JsonObject();
        dbxResult.setResponse(resultJsonObject);
        String primaryCustomerId = "";
        BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();

        backendIdentifierDTO.setBackendType(IntegrationTemplateURLFinder.getBackendURL(InfinityConstants.BackendType));
        backendIdentifierDTO.setCompanyLegalUnit(customerDTO.getCompanyLegalUnit());

        if (isCustomerSearch) {
            backendIdentifierDTO.setBackendId(customerDTO.getId());
        } else {
            backendIdentifierDTO.setCustomer_id(customerDTO.getId());
        }
       
        
        boolean isProfileExists = false;
        String isEnrolled = "false";

        String customerStatus = StatusEnum.SID_CUS_NEW.name();

        String CustomerType_id = "";
        String companyId = "";
        try {
            dbxResult = DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class)
                    .get(backendIdentifierDTO, headerMap);
            if (dbxResult.getResponse() != null) {
                BackendIdentifierDTO identifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
                primaryCustomerId = identifierDTO.getBackendId();
                companyId = identifierDTO.getCompanyId();
                customerId = identifierDTO.getCustomer_id();
                isProfileExists = true;
                customerDTO = (CustomerDTO) (new CustomerDTO().loadDTO(identifierDTO.getCustomer_id()));
                isEnrolled = customerDTO.getIsEnrolled() + "";
                CustomerType_id = customerDTO.getCustomerType_id();
                customerStatus = customerDTO.getStatus_id();
            } else {
                backendIdentifierDTO = new BackendIdentifierDTO();
                backendIdentifierDTO
                        .setBackendType(IntegrationTemplateURLFinder.getBackendURL(InfinityConstants.BackendType));
                backendIdentifierDTO.setBackendId(customerDTO.getId());
                backendIdentifierDTO.setCompanyLegalUnit(customerDTO.getCompanyLegalUnit());
                dbxResult = DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class)
                        .get(backendIdentifierDTO, headerMap);
                if (dbxResult.getResponse() != null) {
                    BackendIdentifierDTO identifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
                    primaryCustomerId = identifierDTO.getBackendId();
                    companyId = identifierDTO.getCompanyId();
                    customerId = identifierDTO.getCustomer_id();
                    isProfileExists = true;
                    customerDTO = (CustomerDTO) (new CustomerDTO().loadDTO(identifierDTO.getCustomer_id()));
                    isEnrolled = customerDTO.getIsEnrolled() + "";
                    CustomerType_id = customerDTO.getCustomerType_id();
                    customerStatus = customerDTO.getStatus_id();
                } else {
                    customerDTO = new CustomerDTO();
                    customerDTO.setId(customerId);
                    customerDTO = (CustomerDTO) customerDTO.loadDTO();
                    if (customerDTO != null) {
                        isProfileExists = true;
                        isEnrolled = customerDTO.getIsEnrolled() + "";
                        customerStatus = customerDTO.getStatus_id();
                        CustomerType_id = customerDTO.getCustomerType_id();
                        primaryCustomerId = customerId;
                    } else {
                        primaryCustomerId = customerId;
                    }
                }
            }
        } catch (ApplicationException e1) {
            // TODO Auto-generated catch block
            alert.prepareError("Error while fetching backend identifier for backend ID " + customerDTO.getId()).log();
        }

        Map<String, Object> postParametersMap = new HashMap<String, Object>();
        postParametersMap.put("_customerId", customerId);
        postParametersMap.put("_legalEntityId", legalEntityId);

        JsonObject jsonobject = ServiceCallHelper.invokeServiceAndGetJson(postParametersMap, headerMap,
                URLConstants.CUSTOMER_BASIC_INFO_PROC);

        if (jsonobject != null && jsonobject.has(Param.OPSTATUS) && jsonobject.get(Param.OPSTATUS).getAsInt() == 0
                && jsonobject.has("records")) {

            if (jsonobject.get("records").getAsJsonArray().size() > 0) {
                customerViewJson = jsonobject.get("records").getAsJsonArray().get(0).getAsJsonObject();
            }

        } else {
            ErrorCodeEnum.ERR_20689.setErrorCode(basicResult);
            basicResult.addProperty("Status", "Failure");
            dbxResult.setResponse(basicResult);
            return dbxResult;
        }

        try {
            if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")) {
                HelperMethods.addJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);
                Map<String, Object> inputParams = new HashMap<String, Object>();
                inputParams.put("customerId", primaryCustomerId);
                

                JsonObject membershipJson = new JsonObject();
                String serviceId = ServiceId.T24ISUSER_INTEGRATION_SERVICE;
                String operationName = OperationName.CORE_CUSTOMER_SEARCH;
                if (StringUtils.isBlank(legalEntityId)) {
                	legalEntityId = EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE);
                }
                headerMap.put("companyId", legalEntityId);
                membershipJson = ServiceCallHelper.invokeServiceAndGetJson(serviceId, null, operationName, inputParams,
                        headerMap);
                

                JsonArray jsonArray = new JsonArray();

                if (null != membershipJson && JSONUtil.hasKey(membershipJson, DBPDatasetConstants.DATASET_CUSTOMERS)
                        && membershipJson.get(DBPDatasetConstants.DATASET_CUSTOMERS).isJsonArray()) {
                    jsonArray = membershipJson.get(DBPDatasetConstants.DATASET_CUSTOMERS).getAsJsonArray();
                    		
                }
                JsonObject customerJson = new JsonObject();
                if (jsonArray.size() > 0) {
                    customerJson = jsonArray.get(0).getAsJsonObject();
                    customerViewJson.add("DateOfBirth", customerJson.get("dateOfBirth"));
                    customerViewJson.add("PrimaryEmailAddress", customerJson.get("email"));
                    customerViewJson.add("PrimaryPhoneNumber", customerJson.get("phone"));
                    customerViewJson.add("FirstName", customerJson.get("firstName"));

                    if (customerViewJson.has("customerName") && !customerJson.get("customerName").isJsonNull()) {
                        customerViewJson.add("Name", customerJson.get("customerName"));
                    } else if (customerViewJson.has("name") && !customerJson.get("name").isJsonNull()) {
                        customerViewJson.add("Name", customerJson.get("name"));
                    } else {
                        customerViewJson.addProperty("Name",
                                (customerJson.has("firstName") && !customerJson.get("firstName").isJsonNull()
                                        && StringUtils.isNotBlank(customerJson.get("firstName").getAsString())
                                                ? customerJson.get("firstName").getAsString()
                                                : "")
                                        + " "
                                        + (customerJson.has("lastName") && !customerJson.get("lastName").isJsonNull()
                                                && StringUtils.isNotBlank(customerJson.get("lastName").getAsString())
                                                        ? customerJson.get("lastName").getAsString()
                                                        : ""));
                    }
                    customerViewJson.add("LastName", customerJson.get("lastName"));
                    customerViewJson.add("ssn", customerJson.get("ssn"));
                    customerViewJson.add("Ssn", customerJson.get("ssn"));
                    customerViewJson.add("SSN", customerJson.get("ssn"));
                    customerViewJson.add("Gender", customerJson.get("gender"));
//                    customerViewJson.add("MaritalStatus_name", customerJson.get("maritalStatus"));
//                    customerViewJson.add("EmployementStatus_name", customerJson.get("employmentStatus"));
                    customerViewJson.add("MaritalStatus_id", customerJson.get("maritalStatus"));
                    customerViewJson.add("EmployementStatus_id", customerJson.get("employmentStatus"));
//                    customerViewJson.addProperty("sectorId", JSONUtil.getString(customerJson, "sectorId"));
                    if (!customerViewJson.has(InfinityConstants.Customer_id)) {
                        customerViewJson.add("Customer_id", customerJson.get("id"));
                    }
                    customerViewJson.add("sectorId", customerJson.get("sectorId"));
                    customerViewJson.addProperty("sectorName", "");
                    customerViewJson.addProperty("branchId", legalEntityId);
                    customerViewJson.addProperty("branchName", "");
                    customerViewJson.addProperty("CustomerStatus_id", customerStatus);
                    customerViewJson.addProperty("legalEntityId",legalEntityId);

                }
            }

        } catch (Exception e) {
        }

        if (!customerViewJson.has(InfinityConstants.isProfileExist)) {
            customerViewJson.addProperty(InfinityConstants.isProfileExist, isProfileExists + "");
        }

        if (!customerViewJson.has(InfinityConstants.isEnrolled)) {
            customerViewJson.addProperty(InfinityConstants.isEnrolled, isEnrolled + "");
        }

        if (customerViewJson.has(InfinityConstants.Customer_id)) {
            customerViewJson.add(InfinityConstants.id, customerViewJson.get(InfinityConstants.Customer_id));
        }

        customerViewJson.addProperty("CustomerType_id", CustomerType_id);
        if (customerId.equals(primaryCustomerId) && !isProfileExists) {

            Set<String> sectorIdList = HelperMethods
                    .splitString(configurations.get(BundleConfigurationHandler.BUSINESS_SECTORID_LIST), ",");
            String sectorId = JSONUtil.getString(customerViewJson, "sectorId");
            if (sectorIdList.contains(sectorId)) {
                customerViewJson.addProperty("CustomerType_id", HelperMethods.getCustomerTypes().get("Business"));
                customerViewJson.addProperty("isBusiness", "true");
            } else {
                customerViewJson.addProperty("isBusiness", "false");
                customerViewJson.addProperty("CustomerType_id", HelperMethods.getCustomerTypes().get("Retail"));
            }

            customerViewJson.remove(InfinityConstants.id);
            customerViewJson.remove(InfinityConstants.Customer_id);
        } else {
            getCustomerType(customerViewJson, customerDTO.getId(), headerMap, customerDTO.getCompanyLegalUnit());
            customerViewJson.addProperty("id", customerId);
            customerViewJson.addProperty("Customer_id", customerId);
            
        }
		try {
			if (customerViewJson.has("Customer_id")) {
				int count = customerDTO.getLockCount();
				if (pm != null && ((count + 1) >= pm.getAccountLockoutThreshold())) {
					customerViewJson.addProperty("CustomerStatus_id", StatusEnum.SID_CUS_LOCKED.name());
				} else {

					String customerForlegalentity = customerViewJson.get("Customer_id").getAsString();
					CustomerLegalEntityDTO customerLegalEntityDTO = new CustomerLegalEntityDTO();
					customerLegalEntityDTO.setCustomer_id(customerForlegalentity);
					customerLegalEntityDTO.setLegalEntityId(legalEntityId);
					List<CustomerLegalEntityDTO> customerLegalEntityDTOs;
					customerLegalEntityDTOs = (List<CustomerLegalEntityDTO>) customerLegalEntityDTO.loadDTO();
					if (customerLegalEntityDTOs != null && customerLegalEntityDTOs.size() > 0)
						customerLegalEntityDTO = customerLegalEntityDTOs.get(0);
					if (StringUtils.isNotBlank(customerLegalEntityDTO.getStatus_id())) {
						customerViewJson.addProperty("CustomerStatus_id", customerLegalEntityDTO.getStatus_id());
					}

				}
			}
		} catch (Exception e) {
			alert.prepareError("Error here", e).log();
		}
        
        String statusId = customerViewJson.has("CustomerStatus_id")
                && !customerViewJson.get("CustomerStatus_id").isJsonNull()
                        ? customerViewJson.get("CustomerStatus_id").getAsString()
                        : HelperMethods.getCustomerStatus().get("NEW");
        isEnrolled = customerViewJson.has("isEnrolled") ? customerViewJson.get("isEnrolled").getAsString() : "false";

        if (StringUtils.isNotBlank(primaryCustomerId)) {
            customerViewJson.addProperty(InfinityConstants.primaryCustomerId, primaryCustomerId);
        } else {
            customerViewJson.add(InfinityConstants.primaryCustomerId, customerViewJson.get("id"));
        }

        String isEnrolledFromSpotlight = customerViewJson.has("isEnrolledFromSpotlight")
                ? customerViewJson.get("isEnrolledFromSpotlight").getAsString()
                : "1";

        customerViewJson.addProperty("isEnrolledFromSpotlight", isEnrolledFromSpotlight);

        CredentialCheckerDTO credentialCheckerDTO = null;
        if (customerDTO != null) {
            credentialCheckerDTO = new CredentialCheckerDTO();
            credentialCheckerDTO.setUserName(customerDTO.getUserName());
            credentialCheckerDTO.setLinktype(HelperMethods.CREDENTIAL_TYPE.ACTIVATION.toString());
            credentialCheckerDTO = (CredentialCheckerDTO) credentialCheckerDTO.loadDTO();
        }

        customerViewJson.addProperty(DBPUtilitiesConstants.IS_CUSTOMER_ENROLLED, isEnrolled);
        customerViewJson.addProperty(DBPUtilitiesConstants.CUSTOMER_STATUS, statusId);

        if (credentialCheckerDTO == null) {
            customerViewJson.addProperty(DBPUtilitiesConstants.IS_ACTIVATION_LINK_SENT, "false");
        } else if (credentialCheckerDTO != null) {
            customerViewJson.addProperty(DBPUtilitiesConstants.IS_ACTIVATION_LINK_SENT, "true");
        }

        customerViewJson.addProperty("isCustomerAccessiable", true);
        basicResult.add("customerbasicinfo_view", customerViewJson);

        JsonObject configuration = new JsonObject();
        if (customerViewJson.has("accountLockoutTime")) {
            configuration.addProperty("value", customerViewJson.get("accountLockoutTime").getAsString());
        } else {
            configuration.addProperty("value", "N/A");
            customerViewJson.addProperty("accountLockoutTime", "N/A");
        }

        basicResult.add("Configuration", configuration);
        String currentStatus;

        String lockedOnTS = "";
        if (customerViewJson.has("lockedOn")) {
            lockedOnTS = customerViewJson.get("lockedOn").getAsString();
        } else {
            configuration.addProperty("value", "N/A");
            customerViewJson.addProperty("lockedOn", "N/A");
        }

        customerViewJson.addProperty("CustomerStatus_id", statusId);
        if (customerViewJson.get("CustomerStatus_id").getAsString()
                .equalsIgnoreCase(StatusEnum.SID_CUS_LOCKED.name())) {
            currentStatus = "LOCKED";
        } else if (customerViewJson.get("CustomerStatus_id").getAsString()
                .equalsIgnoreCase(StatusEnum.SID_CUS_SUSPENDED.name())) {
            currentStatus = "SUSPENDED";
        } else if (customerViewJson.get("CustomerStatus_id").getAsString()
                .equalsIgnoreCase(StatusEnum.SID_CUS_ACTIVE.name())) {
            currentStatus = "ACTIVE";
        } else if(customerViewJson.get("CustomerStatus_id").getAsString()   
                .equalsIgnoreCase(StatusEnum.SID_CUS_INACTIVE.name())) {
        	currentStatus = "INACTIVE";
        } else {
            currentStatus = "NEW";
        }

        if (customerViewJson.get("CustomerStatus_id").getAsString()
                .equalsIgnoreCase(StatusEnum.SID_CUS_LOCKED.name())) {

            if (StringUtils.isNotBlank(lockedOnTS)) {
                String lockDuration = "0";

                if (customerViewJson.has("accountLockoutTime")) {
                    lockDuration = customerViewJson.get("accountLockoutTime").getAsString();
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                Calendar elapsedLockedOnDate = Calendar.getInstance();
                try {
                    elapsedLockedOnDate.setTime(dateFormat.parse(lockedOnTS));
                } catch (ParseException e) {
                }
                elapsedLockedOnDate.add(Calendar.MINUTE, Integer.parseInt(lockDuration));
                Calendar currentDate = Calendar.getInstance();

                if (elapsedLockedOnDate.before(currentDate)) {
                    // Time has been elapsed. Calling unlock service
                    postParametersMap = new HashMap<String, Object>();
                    postParametersMap.put("id", customerId);
                    postParametersMap.put("lockCount", "0");
                    postParametersMap.put("lockedOn", "");
                    JsonObject unlockResponse = ServiceCallHelper.invokeServiceAndGetJson(postParametersMap, headerMap,
                            URLConstants.CUSTOMER_UPDATE);

                    basicResult.addProperty("unlockStatus", unlockResponse.toString());
                    if (unlockResponse == null || !unlockResponse.has(Param.OPSTATUS)
                            || unlockResponse.get(Param.OPSTATUS).getAsInt() != 0) {
                        ErrorCodeEnum.ERR_20538.setErrorCode(basicResult);
                        dbxResult.setResponse(basicResult);
                        return dbxResult;
                    }
                    // set updated status
                    currentStatus = "ACTIVE";
                }
            }
        }

        JsonObject statusResponse = new JsonObject();
        statusResponse.addProperty("LockedOn", lockedOnTS);
        statusResponse.addProperty("Status", currentStatus);
        customerViewJson.add("OLBCustomerFlags", statusResponse);
		try {
			if (customerViewJson.has("Customer_id")) {
				String customerForlegalentity = customerViewJson.get("Customer_id").getAsString();
				getCustomerType(customerViewJson, customerForlegalentity, headerMap, legalEntityId);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
        if (customerViewJson.get("CustomerType_id").getAsString()
                .equalsIgnoreCase(HelperMethods.getCustomerTypes().get("Prospect"))) {
            // Add address in basic information if the customer is of type prospect

            postParametersMap = new HashMap<String, Object>();
            postParametersMap.put(DBPUtilitiesConstants.FILTER, "CustomerId eq '" + customerId + "'");
            postParametersMap.put(DBPUtilitiesConstants.SELECT,
                    "Address_id,AddressType,AddressLine1,AddressLine2,ZipCode,CityName,City_id,RegionName,Region_id,RegionCode,CountryName,Country_id,CountryCode,isPrimary");
            JsonObject readCustomerAddr = ServiceCallHelper.invokeServiceAndGetJson(postParametersMap, headerMap,
                    URLConstants.CUSTOMER_ADDRESS_VIEW_GET);

            if (readCustomerAddr == null || !readCustomerAddr.has(Param.OPSTATUS)
                    || readCustomerAddr.get(Param.OPSTATUS).getAsInt() != 0
                    || !readCustomerAddr.has("customeraddress_view")) {
                ErrorCodeEnum.ERR_20881.setErrorCode(basicResult);

                dbxResult.setResponse(basicResult);
                return dbxResult;

            }
            JsonArray addressDataset = readCustomerAddr.get("customeraddress_view").getAsJsonArray();
            customerViewJson.add("Addresses", addressDataset);

        }

        // if (HelperMethods.getBusinessUserTypes().contains(customerTypeId)) {
        // customerViewJson.add("customerbusinesstype",
        // getBusniessTypesandSignatories(customerId, headerMap));
        // }

        dbxResult = getCustomerRequestNotificationCount(customerDTO, headerMap);
        if (dbxResult.getResponse() != null) {
            JsonObject jsonObject = (JsonObject) dbxResult.getResponse();
            for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                customerViewJson.add(entry.getKey(), entry.getValue());
            }
        }

        basicResult.add("customerbasicinfo_view", customerViewJson);

        dbxResult.setResponse(basicResult);
        
       
        
        
       
        return dbxResult;
    }

    @Override
    public DBXResult fetchRetailCustomerDetails(CustomerDTO customerDTO, Map<String, Object> headersMap)
            throws ApplicationException {
        DBXResult response = new DBXResult();
        CoreCustomerBackendDelegate coreCustomerBackendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(CoreCustomerBackendDelegate.class);
        MembershipDTO membershipDTO = new MembershipDTO();
        membershipDTO.setName(customerDTO.getLastName());
        membershipDTO.setDateOfBirth(customerDTO.getDateOfBirth());
        membershipDTO.setTaxId(customerDTO.getTaxID());
        membershipDTO.setCompanyLegalUnit(customerDTO.getCompanyLegalUnit());;
        DBXResult coreCustomers = new DBXResult();
        JsonObject coreCustomer = new JsonObject();
        try {
            coreCustomers = coreCustomerBackendDelegate.searchCoreCustomers(membershipDTO, headersMap);
            JsonArray coreCustomerRecords = new JsonArray();
            if (coreCustomers != null && coreCustomers.getResponse() != null) {
                coreCustomerRecords = (JsonArray) coreCustomers.getResponse();
            }
            if (coreCustomerRecords.size() > 1) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10804);
            }
            coreCustomer = coreCustomerRecords.get(0).getAsJsonObject();
        } catch (Exception e) {
            alert.prepareError("No details found").log();
            return response;
            // throw new ApplicationException(ErrorCodeEnum.ERR_10802);
        }

        String coreCustomerPhone = JSONUtil.getString(coreCustomer, "phone");
        String coreCustomerEmail = JSONUtil.getString(coreCustomer, "email");
        String sectorId = JSONUtil.getString(coreCustomer, "sectorId");
        if (StringUtils.isBlank(coreCustomerPhone) || StringUtils.isBlank(coreCustomerEmail)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10805);
        }
        String businessSectorIdList =
                BundleConfigurationHandler.fetchConfigurationValueOnKey(BundleConfigurationHandler.BUNDLEID_C360,
                        BundleConfigurationHandler.BUSINESS_SECTORID_LIST, headersMap);
        Set<String> sectorIdList =
                HelperMethods.splitString(businessSectorIdList, ",");
        if (sectorIdList.contains(sectorId)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10803);
        }
        response.setResponse(coreCustomer);
        return response;
    }
    
    @Override
    public DBXResult userIdSearch(String userName, Map<String, Object> headerMap)
            throws ApplicationException {
    	DBXResult dbxResult = new DBXResult();
        try {
        	
        	Map<String, Object> postParametersMap = new HashMap<String, Object>();
            postParametersMap.put("_userName", userName);
            
            JsonObject jsonobject = ServiceCallHelper.invokeServiceAndGetJson(postParametersMap, headerMap,
                    URLConstants.USERID_SEARCH_PROC);
            if (jsonobject != null && jsonobject.has(Param.OPSTATUS) && jsonobject.get(Param.OPSTATUS).getAsInt() == 0
                    && jsonobject.has("records")) {
             dbxResult.setResponse(jsonobject);
               
            }
            
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10379);
        }
        return dbxResult;
		

       
    }

	@Override
	public DBXResult customerLegalEntitiesGet(CustomerDTO customerDTO, Map<String, Object> headerMap)
			throws ApplicationException {
		DBXResult dbxResult = new DBXResult();
		try {

			if (StringUtils.isBlank(customerDTO.getId()))
				return dbxResult;
			Map<String, Object> postParametersMap = new HashMap<String, Object>();
			postParametersMap.put("customerid", customerDTO.getId());
			JsonArray customerLegalEntitiesArray = new JsonArray();
			JsonObject customerLegalEntities = ServiceCallHelper.invokeServiceAndGetJson(postParametersMap, headerMap,
					URLConstants.CUSTOMER_LEGALENTITIES_GET_PROC);
			if (customerLegalEntities != null && customerLegalEntities.has(Param.OPSTATUS)
					&& customerLegalEntities.get(Param.OPSTATUS).getAsInt() == 0
					&& customerLegalEntities.has("records")) {

				customerLegalEntitiesArray = CommonUtils.generateLegalEntitiesForCustomers(customerLegalEntities);
			}
			customerLegalEntities = new JsonObject();
			customerLegalEntities.add("legalEntities", customerLegalEntitiesArray);
			dbxResult.setResponse(customerLegalEntities);
		}

		catch (Exception e) {
			alert.prepareError("Error occured",e).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_29054);
		}
		return dbxResult;
	}
}