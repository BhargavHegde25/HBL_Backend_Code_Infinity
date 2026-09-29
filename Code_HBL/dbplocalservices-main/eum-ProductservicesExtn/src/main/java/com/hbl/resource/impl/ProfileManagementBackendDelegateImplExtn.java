package com.hbl.resource.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.spi.ThrowableRendererSupport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hbl.businessdelegate.impl.CoreCustomerBusinessDelegateImplExtn;
import com.hbl.utility.DeviceInfo;
import com.infinity.dbx.dbp.jwt.auth.AuthConstants;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.util.BundleConfigurationHandler;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.IntegrationTemplateURLFinder;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.OperationName;
import com.kony.dbputilities.util.ServiceId;
import com.kony.dbputilities.util.StatusEnum;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.kony.eum.dbputilities.customersecurityservices.PasswordHistoryManagement;
import com.kony.eum.dbputilities.util.ServiceCallHelper;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.exceptions.MiddlewareException;
import com.nimbusds.jose.util.JSONStringUtils;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.CoreCustomerBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.BackendIdentifiersBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.impl.ProfileManagementBackendDelegateImpl;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.dto.CredentialCheckerDTO;
import com.temenos.dbx.product.dto.CustomerCommunicationDTO;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.CustomerLegalEntityDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.MemberSearchBean;
import com.temenos.dbx.product.dto.MembershipDTO;
import com.temenos.dbx.product.usermanagement.backenddelegate.impl.BackendIdentifiersBackendDelegateimpl;
import com.temenos.dbx.product.utils.DTOConstants;
import com.temenos.dbx.product.utils.InfinityConstants;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class ProfileManagementBackendDelegateImplExtn extends ProfileManagementBackendDelegateImpl {
	private static final Logger LOG = LogManager.getLogger(ProfileManagementBackendDelegateImplExtn.class);
    LoggerUtil logger = new LoggerUtil(ProfileManagementBackendDelegateImplExtn.class);
    private static final Alert alert = com.temenos.logger.Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = com.temenos.logger.Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    
    public DBXResult fetchHBLRetailCustomerDetails(Map<String, Object> payload, Map<String, Object> headersMap)
            throws ApplicationException {
        DBXResult response = new DBXResult();
        DBXResult coreCustomers = new DBXResult();
        JsonObject coreCustomer = new JsonObject();
        CoreCustomerBackendDelegateImplExtn coreCustomerBackendDelegateExtn = new CoreCustomerBackendDelegateImplExtn();
        try {
            coreCustomers = coreCustomerBackendDelegateExtn.searchHBLCoreCustomers(payload, headersMap);
            JsonArray coreCustomerRecords = new JsonArray();
            if (coreCustomers != null && coreCustomers.getResponse() != null) {
                coreCustomerRecords = (JsonArray) coreCustomers.getResponse();
            }
            if (coreCustomerRecords.size() > 1) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10804);
            }
            coreCustomer = coreCustomerRecords.get(0).getAsJsonObject();
        } catch (ApplicationException e) {
            logger.error("ApplicationException Occured while searching a customer",e);
            throw new ApplicationException(e.getErrorCodeEnum());
        }
        catch (Exception e) {
            logger.error("No details found");
            return response;
            // throw new ApplicationException(ErrorCodeEnum.ERR_10802);
        }

        String coreCustomerPhone = JSONUtil.getString(coreCustomer, "phone");
        String coreCustomerEmail = JSONUtil.getString(coreCustomer, "email");
        
        /*String phone=payload.get("phone")!=null ?payload.get("phone").toString() : "";
        String[] array = phone.split("-");
        String phoneNumber = array.length > 1 && StringUtils.isNotBlank(array[1]) ? array[1] : phone;
        if (coreCustomerPhone!=null && !coreCustomerPhone.contains(phoneNumber)) {
        	throw new ApplicationException(ErrorCodeEnum.ERR_10806);
        }
        */
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
	public DBXResult fetchHBLRetailCustomerDetails_old(Map<String, Object> payload, Map<String, Object> headersMap)
            throws ApplicationException {
        DBXResult response = new DBXResult();
        DBXResult coreCustomers = new DBXResult();
        JsonObject coreCustomer = new JsonObject();
        CoreCustomerBackendDelegateImplExtn coreCustomerBackendDelegateExtn = new CoreCustomerBackendDelegateImplExtn();
        try {
            coreCustomers = coreCustomerBackendDelegateExtn.searchHBLCoreCustomers(payload, headersMap);
            JsonArray coreCustomerRecords = new JsonArray();
            if (coreCustomers != null && coreCustomers.getResponse() != null) {
                coreCustomerRecords = (JsonArray) coreCustomers.getResponse();
            }
            if (coreCustomerRecords.size() > 1) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10804);
            }
            coreCustomer = coreCustomerRecords.get(0).getAsJsonObject();
        } catch (Exception e) {
            logger.error("No details found");
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
	//Can't Sign in flow
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
	        String customerFullName= customerDTO.getFullName();
	        String customerAccountNumber= customerDTO.getTaxID();//AccountNumber
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
	        JsonObject responseJson = new JsonObject();
	        String IS_Integrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);
	        if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")) {
	            CoreCustomerBusinessDelegateImplExtn corecustomerBD =
	                    new CoreCustomerBusinessDelegateImplExtn();
	            List<MembershipDTO> membershipList = new ArrayList<>();
	            try {
	                membershipList = corecustomerBD.getHBLMembershipDetails(customerFullName, customerAccountNumber, dateofbirth, legalEntityId, email, phone, headerMap);
	            } catch (ApplicationException e) {
	            	throw new ApplicationException(e.getErrorCodeEnum());
	            }
	            if(membershipList.size()>0) {
	            responseJson.addProperty("channelAccess", membershipList.get(0).getStatus());
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
	        JSONObject processObj = verifyChannelAccess(responseJson, null, headerMap);
			String isUserEligibleForResetPwd=processObj.has("isUserEligibleForResetPwd")?processObj.getString("isUserEligibleForResetPwd"):"";
			if(isUserEligibleForResetPwd.equalsIgnoreCase("No") || StringUtils.isEmpty(isUserEligibleForResetPwd)) {
				dbxResult.setDbpErrCode(processObj.getString("dbpErrCode"));
				dbxResult.setDbpErrMsg(processObj.getString("dbpErrMsg"));
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
	 public JSONObject verifyChannelAccess(JsonObject coreCustomerJson, DataControllerRequest dcRequest, Map<String, Object> inputMap) {
			String channelAccess = JSONUtil.getString(coreCustomerJson, "channelAccess");
			String channel=DeviceInfo.GetUserDeviceInfo(null, inputMap).get("channel_id").toString();
			logger.debug("hbl::ProfileManagementBackendDelegateImplExtn:checkEligibilityForCantSignIn channelAccess:"+channelAccess);
			logger.debug("hbl::ProfileManagementBackendDelegateImplExtn:checkEligibilityForCantSignIn channel name:"+channel);
			String errorMsg="";
			JSONObject enorllObject = new JSONObject();
			if(channelAccess.equalsIgnoreCase("ONLINE_BANKING_ONLY")) {
				if(channel.equalsIgnoreCase("desktop")) {
					enorllObject.put("isUserEligibleForResetPwd", "Yes");
					enorllObject.put("channelAccess", channelAccess);
				}else {
					errorMsg="Sorry! Can’t reset your password. Your Mobile banking access is currently disabled. Please contact customer support for assistance or visit your nearest branch to enable your access.";
					enorllObject.put("isUserEligibleForResetPwd", "No");
					enorllObject.put("dbpErrMsg", errorMsg);
					enorllObject.put("dbpErrCode", "30001");
					
				}
			}
			else if(channelAccess.equalsIgnoreCase("MOBILE_BANKING_ONLY")) {
				if(channel.equalsIgnoreCase("mobile")) {
					enorllObject.put("isUserEligibleForResetPwd", "Yes");
					enorllObject.put("channelAccess", channelAccess);
				}else {
					errorMsg="Sorry! Can’t reset your password. Your Online banking access is currently disabled. Please contact customer support for assistance or visit your nearest branch to enable your access.";
					enorllObject.put("isUserEligibleForResetPwd", "No");
					enorllObject.put("dbpErrMsg", errorMsg);
					enorllObject.put("dbpErrCode", "30002");
				}
			}
			else if(channelAccess.equalsIgnoreCase("BOTH")) {
				enorllObject.put("isUserEligibleForResetPwd", "Yes");
				enorllObject.put("channelAccess", channelAccess);
			}else {
				errorMsg="Sorry! Can’t reset your password. Your ebanking access is currently disabled. Please contact customer support for assistance or visit your nearest branch to enable your access.";
				enorllObject.put("isUserEligibleForResetPwd", "No");
				enorllObject.put("dbpErrMsg", errorMsg);
				enorllObject.put("dbpErrCode", "30003");
			}
			return enorllObject;
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
	        if (StringUtils.isNotBlank(memberSearchBean.getCustomerIDValue())) {
	            input.put("legalId", memberSearchBean.getCustomerIDValue());
	        }
	        if (StringUtils.isNotBlank(memberSearchBean.getCustomerIDType())) {
	            input.put("legalDocumentName", memberSearchBean.getCustomerIDType());
	        }
	        if (StringUtils.isNotBlank(memberSearchBean.getTin())) {
	            input.put("legalDocumentName","ID_PAN_NO");
	            input.put("legalId",memberSearchBean.getTin());
	        }
	        JsonArray partyJsonArray = new JsonArray();

	        if (StringUtils.isNotBlank(memberSearchBean.getCustomerId())) {
	            input.put("customerId", memberSearchBean.getCustomerId());
	        }

	        if (!input.isEmpty()){
	        	//String serviceId = "HBL_T24ISUser";
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
	                String name=
	                        (jsonObject.has("firstName") && !jsonObject.get("firstName").isJsonNull()
	                                && StringUtils.isNotBlank(jsonObject.get("firstName").getAsString())
	                                        ? jsonObject.get("firstName").getAsString()
	                                        : "")
	                                + " "
	                                + (jsonObject.has("lastName") && !jsonObject.get("lastName").isJsonNull()
	                                        && StringUtils.isNotBlank(jsonObject.get("lastName").getAsString())
	                                                ? jsonObject.get("lastName").getAsString()
	                                                : "");
	                jsonObject.addProperty("name",jsonObject.get("name")!=null?jsonObject.get("name").getAsString():name);
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
	@Override
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
	                //String serviceId = "HBL_T24ISUser";
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
//	                    customerViewJson.add("MaritalStatus_name", customerJson.get("maritalStatus"));
//	                    customerViewJson.add("EmployementStatus_name", customerJson.get("employmentStatus"));
	                    customerViewJson.add("MaritalStatus_id", customerJson.get("maritalStatus"));
	                    customerViewJson.add("EmployementStatus_id", customerJson.get("employmentStatus"));
//	                    customerViewJson.addProperty("sectorId", JSONUtil.getString(customerJson, "sectorId"));
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
	        diagnostic.debug("customerViewJson Full in Extn###"+ customerViewJson);
	        diagnostic.debug("customerViewJson in Extn###"+ customerViewJson.get("CustomerStatus_id").getAsString());
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
	        
	        diagnostic.debug("currentStatus in Extn###"+ currentStatus);

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

	                /*
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
	                */
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


}
