package com.temenos.dbx.usermanagement.resource.impl;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.kony.dbputilities.util.BundleConfigurationHandler;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.IntegrationTemplateURLFinder;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.eum.dbputilities.customersecurityservices.PasswordHistoryManagement;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.impl.ProfileManagementBackendDelegateImpl;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.ProfileManagementBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl.ProfileManagementBusinessDelegateImpl;
import com.temenos.dbx.eum.product.usermanagement.resource.api.InfinityUserManagementResource;
import com.temenos.dbx.eum.product.usermanagement.resource.api.ProfileManagementResource;
import com.temenos.dbx.eum.product.usermanagement.resource.impl.ProfileManagementResourceImpl;
import com.temenos.dbx.party.businessdelegate.api.PartyCustomerBusinessDelegate;
import com.temenos.dbx.party.utils.PartyConstants;
import com.temenos.dbx.party.utils.PartyUtils;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.MemberSearchBean;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.BackendIdentifiersBackendDelegate;
import com.temenos.dbx.product.usermanagement.backenddelegate.impl.BackendIdentifiersBackendDelegateimpl;
import com.temenos.dbx.product.utils.CustomerUtils;
import com.temenos.dbx.product.utils.DTOConstants;
import com.temenos.dbx.product.utils.DTOUtils;
import com.temenos.dbx.product.utils.InfinityConstants;
import com.temenos.dbx.usermanagement.constants.PartyProfileDetailsConstants;
import com.konylabs.middleware.dataobject.Param;

public class PartyProfileManagementResourceImpl implements ProfileManagementResource {

    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");    

    @Override
    public Result getCustomerForUserResponse(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {
        ProfileManagementResource managementResource = new ProfileManagementResourceImpl();
        Result result = managementResource.getCustomerForUserResponse(methodID, inputArray, dcRequest, dcResponse);

        Record record = HelperMethods.getRecord(result);
        Dataset dataset = getDataSetfromString("employments", record);
        record.addDataset(dataset);
        dataset = getDataSetfromString("identifiers", record);
        record.addDataset(dataset);

        return result;
    }

    private Dataset getDataSetfromString(String string, Record customer) {
        JsonElement elements = new JsonArray();
        Dataset dataset = new Dataset();
        dataset.setId(string);
        try {
            if (customer.getParamValueByName(string) != null
                    && (elements = new JsonParser().parse(customer.getParamValueByName(string))).isJsonArray()) {
                Record record = new Record();
                for (JsonElement element : elements.getAsJsonArray()) {
                    record = new Record();
                    for (Entry<String, JsonElement> param : element.getAsJsonObject().entrySet()) {
                        try {
                            record.addStringParam(param.getKey(), param.getValue().getAsString());
                        }
                        catch (Exception e) {
                            // TODO: handle exception
                        }
                    }
                    dataset.addRecord(record);
                }
                customer.removeParamByName(string);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return dataset;
    }

    @Override
    public Result getUserDetailsConcurrent(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {
        Result result = new Result();
        SessionScope.clear(dcRequest);
        Map<String, String> inputmap = HelperMethods.getInputParamMap(inputArray);
        String Username = inputmap.get("Username");
        String Customer_id = "";
        String legalEntityId = inputmap.get("legalEntityId");
        if(StringUtils.isBlank(legalEntityId)) {
        	legalEntityId = dcRequest.getParameter("legalEntityId");
        }
        
        Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromAPIDBPIdentityService(dcRequest);

        boolean IS_Integrated = IntegrationTemplateURLFinder.isIntegrated;

        if (!HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) {
            Customer_id = inputmap.get("Customer_id");
            if (StringUtils.isNotBlank(Customer_id)) {
                BackendIdentifiersBackendDelegateimpl backendDelegateimpl = new BackendIdentifiersBackendDelegateimpl();
                BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
                backendIdentifierDTO.setBackendId(Customer_id);
                backendIdentifierDTO
                        .setBackendType(PartyConstants.PARTY);

                DBXResult dbxResult = backendDelegateimpl.get(backendIdentifierDTO, dcRequest.getHeaderMap());
                if (dbxResult.getResponse() != null) {
                    backendIdentifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
                    Customer_id = backendIdentifierDTO.getCustomer_id();
                    legalEntityId = backendIdentifierDTO.getCompanyLegalUnit();
                }
            }
        }

        DBXResult dbxResult = new DBXResult();

        if (StringUtils.isBlank(Username) && StringUtils.isBlank(Customer_id)) {
            loggedInUserInfo = HelperMethods.getUserFromIdentityService(dcRequest);
            if (HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) {
                Username = loggedInUserInfo.get("UserName");
                Customer_id = loggedInUserInfo.get("Customer_id");
            }
        }

        if (StringUtils.isBlank(Customer_id)) {
            result.addParam(DBPUtilitiesConstants.VALIDATION_ERROR, "Unable to find userId");
            return result;
        }

        CustomerDTO customerDTO =
                new CustomerDTO();
        customerDTO.setId(Customer_id);
        customerDTO = (CustomerDTO) customerDTO.loadDTO();

        /* T24 call is not required in party ms integration
        boolean isCallingFromT24 = Boolean.parseBoolean(dcRequest.getParameter("isCallingFromT24"));
        
        if (customerDTO == null && !isCallingFromT24) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("Customer_id", Customer_id);
            try {
                result = ServiceCallHelper.invokeServiceAndGetResult(dcRequest, map,
                        HelperMethods.getHeaders(dcRequest), "services/T24ISExtra/getUserDetails");
                if (result.getDatasetById("user") != null) {
                    result.getDatasetById("user").setId("customer");
                }
            } catch (HttpCallException e) {
            	alert.prepareError("Exception", e).log();
                return new Result();
            }
        }*/

        BackendIdentifiersBackendDelegateimpl backendDelegateimpl = new BackendIdentifiersBackendDelegateimpl();
        BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
        backendIdentifierDTO.setCustomer_id(Customer_id);
        backendIdentifierDTO.setCompanyLegalUnit(legalEntityId);
        if (IS_Integrated) {
            backendIdentifierDTO
                    .setBackendType(IntegrationTemplateURLFinder.getBackendURL(InfinityConstants.BackendType));
        } else {
            backendIdentifierDTO.setBackendType(DTOConstants.CORE);
        }

        String coreCustomerID = "";
        dbxResult = backendDelegateimpl.get(backendIdentifierDTO, dcRequest.getHeaderMap());
        if (dbxResult.getResponse() != null) {
            backendIdentifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
            coreCustomerID = backendIdentifierDTO.getBackendId();
        }
        customerDTO.setCompanyLegalUnit(legalEntityId);
        ProfileManagementBusinessDelegate businessDelegate =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ProfileManagementBusinessDelegate.class);
        dbxResult = businessDelegate.getUserResponse(customerDTO, dcRequest.getHeaderMap());
        if (dbxResult.getResponse() != null) {
            JsonObject resultObject = (JsonObject) dbxResult.getResponse();
            JsonObject jsonObject = resultObject.has("customer") && resultObject.get("customer").isJsonArray()
                    && resultObject.get("customer").getAsJsonArray().size() > 0
                    && resultObject.get("customer").getAsJsonArray().get(0).isJsonObject()
                            ? resultObject.get("customer").getAsJsonArray().get(0).getAsJsonObject()
                            : new JsonObject();

            if (JSONUtil.isJsonNotNull(jsonObject) && JSONUtil.hasKey(jsonObject, "customers")
                    && jsonObject.get("customers").isJsonArray()
                    && JSONUtil.getJsonArrary(jsonObject, "customers").size() > 0) {

                JsonArray array = JSONUtil.getJsonArrary(jsonObject, "customers");
                for (JsonElement element : array) {
                    JsonObject coreCustomerObject =
                            element.isJsonObject() ? element.getAsJsonObject() : new JsonObject();
                    String coreCustomerId = coreCustomerObject.has("coreCustomerId")
                            ? coreCustomerObject.get("coreCustomerId").getAsString()
                            : "";
                    if (StringUtils.isNotBlank(coreCustomerId) && coreCustomerId.equalsIgnoreCase(coreCustomerID)) {
                        coreCustomerObject.addProperty("isPrimary", "true");
                    } else {
                        coreCustomerObject.addProperty("isPrimary", "false");
                    }
                }
            }
            result = JSONToResult.convert(resultObject.toString());
        }
        return result;
    }

    @Override
    public Result createCustomer(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) {
        ProfileManagementResource managementResource = new ProfileManagementResourceImpl();
        return managementResource.createCustomer(methodID, inputArray, request, response);
    }

    @Override
    public Result getUserDetailsToAdmin(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) {

        ProfileManagementResource managementResource = new ProfileManagementResourceImpl();
        return managementResource.getUserDetailsToAdmin(methodID, inputArray, request, response);
    }

    @Override
    public Result updateProfile(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        LegalEntityUtil.addCompanyIDToHeaders(dcRequest);
        Map<String, Object> headerMap = dcRequest.getHeaderMap();

        String customerID = "";
        String partyID = "";
        String legalEntityId = StringUtils.EMPTY;
        Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromAPIDBPIdentityService(dcRequest);
        if (!HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) {
            customerID = inputParams.get(DTOConstants.CUSTOMER_ID);
            legalEntityId = dcRequest.getParameter("legalEntityId");
            if (StringUtils.isEmpty(customerID)) {
                customerID = dcRequest.getParameter(DTOConstants.CUSTOMER_ID);
                partyID = inputParams.get(DTOConstants.PARTYID);
            }
        } else {
            customerID = HelperMethods.getCustomerIdFromSession(dcRequest);
            legalEntityId = dcRequest.getParameter("legalEntityId");
        }
        headerMap.put("companyid", legalEntityId);
        
        DBXResult dbxResult = new DBXResult();
        if (StringUtils.isNotBlank(partyID)) {
            BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
            backendIdentifierDTO.setBackendId(partyID);
            backendIdentifierDTO.setBackendType(DTOConstants.PARTY);
            backendIdentifierDTO.setCompanyLegalUnit(legalEntityId);
            try {
                dbxResult = DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class)
                        .get(backendIdentifierDTO, headerMap);
            } catch (ApplicationException e) {
                alert.prepareError("exception while fetching Backend Identifier", e).log();
            }
            if (dbxResult.getResponse() != null) {
                BackendIdentifierDTO identifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
                customerID = identifierDTO.getCustomer_id();
            }
        }

        if (StringUtils.isBlank(customerID) && StringUtils.isBlank(partyID)) {

            diagnostic.prepareDebug("Customer update is failed").log();
            // makeAuditEntry(inputArray, dcRequest, result, dcResponse, false, DTOConstants.PROSPECT);
            ErrorCodeEnum.ERR_10218.setErrorCode(result);
            return result;
        } else if (StringUtils.isBlank(customerID)) {
            customerID = partyID;
        }

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customerID);
        customerDTO = CustomerUtils.buildCustomerDTO(customerID, inputParams, dcRequest);
        if (dcRequest.containsKeyInRequest("source")) {
            if (dcRequest.getParameter("source").toString().contentEquals("UserManagement")) {

                if (dcRequest.containsKeyInRequest("operation")) {
                    customerDTO.setOperation(
                            dcRequest.getParameter("operation") != null ? dcRequest.getParameter("operation").toString()
                                    : "");
                }
                if (dcRequest.containsKeyInRequest("detailToBeUpdated")) {
                    customerDTO.setDetailToBeUpdated(dcRequest.getParameter("detailToBeUpdated") != null
                            ? dcRequest.getParameter("detailToBeUpdated").toString()
                            : "");
                }
                customerDTO.setSource(
                        dcRequest.getParameter("source") != null ? dcRequest.getParameter("source").toString() : "");

                if (dcRequest.containsKeyInRequest("deleteCommunicationID")) {
                    customerDTO.setCommunicationIDToBeDeleted(dcRequest.getParameter("deleteCommunicationID") != null
                            ? dcRequest.getParameter("deleteCommunicationID").toString()
                            : "");
                    customerDTO.setOperation("Delete");
                }
                if (dcRequest.containsKeyInRequest("deleteAddressID")) {
                    customerDTO.setAddressIDToBeDeleted(dcRequest.getParameter("deleteAddressID") != null
                            ? dcRequest.getParameter("deleteAddressID").toString()
                            : "");
                    customerDTO.setOperation("DeleteAddress");
                }
                customerDTO.setId(customerID);
                if (customerDTO.getOperation().contentEquals(PartyProfileDetailsConstants.PARAM_OPERATION_CREATEADDRESS)
                        || (customerDTO.getOperation()
                                .contentEquals(PartyProfileDetailsConstants.PARAM_OPERATION_CREATE))) {
                    if (!PartyUtils.allowPhoneEmailAddressCreation(customerDTO, headerMap)) {
                        ErrorCodeEnum.ERR_29026.setErrorCode(result);
                        return result;
                    }
                }
            }
        } else {
            if (dcRequest.containsKeyInRequest("deleteCommunicationID")
                    && StringUtils.isNotBlank(dcRequest.getParameter("deleteCommunicationID"))) {
                customerDTO.setCommunicationIDToBeDeleted(dcRequest.getParameter("deleteCommunicationID").toString());
                customerDTO.setOperation("Delete");
            } else if (dcRequest.containsKeyInRequest("deleteAddressID")
                    && StringUtils.isNotBlank(dcRequest.getParameter("deleteAddressID"))) {
                customerDTO.setAddressIDToBeDeleted(dcRequest.getParameter("deleteAddressID").toString());
                customerDTO.setOperation("DeleteAddress");
            }
        }
        customerDTO.setCompanyLegalUnit(legalEntityId);
        result = new Result();
        LegalEntityUtil.addCompanyIDToHeaders(dcRequest);
        ProfileManagementBusinessDelegate businessDelegate =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ProfileManagementBusinessDelegate.class);
        dbxResult = businessDelegate.updateCustomer(customerDTO, headerMap);

        if (dbxResult.getResponse() != null) {
            JsonObject jsonObject = (JsonObject) dbxResult.getResponse();
            result = JSONToResult.convert(jsonObject.toString());
        }

        return result;
    }

    
    @Override
    public Result searchCustomer(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {
 
        Result processedResult = new Result();

        // Sort validation
        if (StringUtils.isNotBlank(dcRequest.getParameter("_sortVariable"))
                && dcRequest.getParameter("_sortVariable").contains(" ")) {
            ErrorCodeEnum.ERR_10333.setErrorCode(processedResult);
            return processedResult;
        }
        if (StringUtils.isNotBlank(dcRequest.getParameter("_sortDirection"))
                && (dcRequest.getParameter("_sortDirection").contains(" ")
                        || (!dcRequest.getParameter("_sortDirection").equalsIgnoreCase("ASC")
                                && !dcRequest.getParameter("_sortDirection").equalsIgnoreCase("DESC")))) {
            ErrorCodeEnum.ERR_10334.setErrorCode(processedResult);
            return processedResult;
        }
        String legalEntityId = dcRequest.getParameter("_legalEntityId");
      
        MemberSearchBean memberSearchBean = new MemberSearchBean();
        memberSearchBean.setSearchType(dcRequest.getParameter("_searchType"));
        memberSearchBean.setMemberId(dcRequest.getParameter("_id"));
        memberSearchBean.setCustomerId(dcRequest.getParameter("_customerId"));
        memberSearchBean.setCustomerName(dcRequest.getParameter("_name"));
        memberSearchBean.setSsn(dcRequest.getParameter("_SSN"));
        memberSearchBean.setCustomerUsername(dcRequest.getParameter("_username"));
        memberSearchBean.setCustomerPhone(dcRequest.getParameter("_phone"));
        memberSearchBean.setCustomerEmail(dcRequest.getParameter("_email"));
        memberSearchBean.setIsStaffMember(dcRequest.getParameter("_IsStaffMember"));
        memberSearchBean.setCardorAccountnumber(dcRequest.getParameter("_cardorAccountnumber"));
        memberSearchBean.setTin(dcRequest.getParameter("_TIN"));
        memberSearchBean.setCustomerGroup(dcRequest.getParameter("_group"));
        memberSearchBean.setCustomerIDType(dcRequest.getParameter("_IDType"));
        memberSearchBean.setCustomerIDValue(dcRequest.getParameter("_IDValue"));
        memberSearchBean.setCustomerCompanyId(dcRequest.getParameter("_companyId"));
        memberSearchBean.setCustomerRequest(dcRequest.getParameter("_requestID"));
        memberSearchBean.setBranchIDS(dcRequest.getParameter("_branchIDS"));
        memberSearchBean.setProductIDS(dcRequest.getParameter("_productIDS"));
        memberSearchBean.setCityIDS(dcRequest.getParameter("_cityIDS"));
        memberSearchBean.setEntitlementIDS(dcRequest.getParameter("_entitlementIDS"));
        memberSearchBean.setGroupIDS(dcRequest.getParameter("_groupIDS"));
        memberSearchBean.setCustomerStatus(dcRequest.getParameter("_customerStatus"));
        memberSearchBean.setBeforeDate(dcRequest.getParameter("_before"));
        memberSearchBean.setAfterDate(dcRequest.getParameter("_after"));
        memberSearchBean.setSortVariable(dcRequest.getParameter("_sortVariable"));
        memberSearchBean.setSortDirection(dcRequest.getParameter("_sortDirection"));
        memberSearchBean.setPageOffset(dcRequest.getParameter("_pageOffset"));
        memberSearchBean.setPageSize(dcRequest.getParameter("_pageSize"));
        memberSearchBean.setDateOfBirth(dcRequest.getParameter("_dateOfBirth"));
        memberSearchBean.setCompanyLegalUnit(dcRequest.getParameter("_legalEntityId"));

        if (StringUtils.isBlank(memberSearchBean.getSearchType())) {
            ErrorCodeEnum.ERR_10334.setErrorCode(processedResult);
            return processedResult;

        }
        
        if(StringUtils.isBlank(legalEntityId)){
        	ErrorCodeEnum.ERR_29040.setErrorCode(processedResult);
        	return processedResult;
        }
		
        
        
        processedResult
                .addParam(new Param("SortVariable", memberSearchBean.getSortVariable(), Param.STRING_CONST));
        processedResult
                .addParam(new Param("SortDirection", memberSearchBean.getSortDirection(), Param.STRING_CONST));
        processedResult.addParam(
                new Param("PageOffset", String.valueOf(memberSearchBean.getPageOffset()), Param.STRING_CONST));
        processedResult.addParam(
                new Param("PageSize", String.valueOf(memberSearchBean.getPageSize()), Param.STRING_CONST));
        String id = dcRequest.getParameter("_id");
        String name = dcRequest.getParameter("_name");
        String SSN = dcRequest.getParameter("_SSN");
        String username = dcRequest.getParameter("_username");
        String phone = dcRequest.getParameter("_phone");
        String email = dcRequest.getParameter("_email");
        String accNo = dcRequest.getParameter("_cardorAccountnumber");
        String Tin = dcRequest.getParameter("_TIN");
        String IDType = dcRequest.getParameter("_IDType");
        String IDValue = dcRequest.getParameter("_IDValue");
        String CompanyId = dcRequest.getParameter("_companyId");
        String searchType = dcRequest.getParameter("_searchType");
        String dateOfBirth = dcRequest.getParameter("_dateOfBirth");
        String customerId = dcRequest.getParameter("_customerId");
        //legalEntityId = dcRequest.getParameter("_legalEntityId");
        /*if ((StringUtils.isBlank(id) || id.equalsIgnoreCase("null"))
                && (StringUtils.isBlank(name) || name.equalsIgnoreCase("null"))
                && (StringUtils.isBlank(dateOfBirth) || dateOfBirth.equalsIgnoreCase("null"))
                && (StringUtils.isBlank(SSN) || SSN.equalsIgnoreCase("null"))
                && (StringUtils.isBlank(username) || username.equalsIgnoreCase("null"))
                && (StringUtils.isBlank(phone) || phone.equalsIgnoreCase("null"))
                && (StringUtils.isBlank(email) || email.equalsIgnoreCase("null"))
                && (StringUtils.isBlank(accNo) || accNo.equalsIgnoreCase("null"))
                && (StringUtils.isBlank(Tin) || Tin.equalsIgnoreCase("null"))
                && (StringUtils.isBlank(IDType) || IDType.equalsIgnoreCase("null"))
                && (StringUtils.isBlank(IDValue) || IDValue.equalsIgnoreCase("null"))
                && (StringUtils.isBlank(CompanyId) || CompanyId.equalsIgnoreCase("null"))
                && (StringUtils.isBlank(customerId) || customerId.equalsIgnoreCase("null"))
                && (searchType.equalsIgnoreCase("CUSTOMER_SEARCH"))
                || ((StringUtils.isBlank(legalEntityId) || legalEntityId.equalsIgnoreCase("null")))){
            processedResult.addParam(new Param("TotalResultsFound", "0", Param.INT_CONST));
            Dataset recordsDS = new Dataset();
            recordsDS.setId("records");
            processedResult.addDataset(recordsDS);
            return processedResult;
        }*/
       
        if((!(StringUtils.isBlank(legalEntityId) || legalEntityId.equalsIgnoreCase("null")))
        	&& (StringUtils.isBlank(id) || id.equalsIgnoreCase("null"))
            && (StringUtils.isBlank(name) || name.equalsIgnoreCase("null"))
            && (StringUtils.isBlank(dateOfBirth) || dateOfBirth.equalsIgnoreCase("null"))
            && (StringUtils.isBlank(SSN) || SSN.equalsIgnoreCase("null"))
            && (StringUtils.isBlank(username) || username.equalsIgnoreCase("null"))
            && (StringUtils.isBlank(phone) || phone.equalsIgnoreCase("null"))
            && (StringUtils.isBlank(email) || email.equalsIgnoreCase("null"))
            && (StringUtils.isBlank(accNo) || accNo.equalsIgnoreCase("null"))
            && (StringUtils.isBlank(Tin) || Tin.equalsIgnoreCase("null"))
            && (StringUtils.isBlank(IDType) || IDType.equalsIgnoreCase("null"))
            && (StringUtils.isBlank(IDValue) || IDValue.equalsIgnoreCase("null"))
            && (StringUtils.isBlank(CompanyId) || CompanyId.equalsIgnoreCase("null"))
            && (StringUtils.isBlank(customerId) || customerId.equalsIgnoreCase("null"))
            && (searchType.equalsIgnoreCase("CUSTOMER_SEARCH"))){
        	ErrorCodeEnum.ERR_29041.setErrorCode(processedResult);
            return processedResult;
        	
        	
        }

        ProfileManagementBusinessDelegate businessDelegate =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ProfileManagementBusinessDelegate.class);

        Map<String, String> configurations = BundleConfigurationHandler
                .fetchBundleConfigurations(BundleConfigurationHandler.BUDLENAME_C360, dcRequest);
        PasswordHistoryManagement pm = new PasswordHistoryManagement(dcRequest, true);
        
        DBXResult dbxResult =
                businessDelegate.searchCustomer(configurations, memberSearchBean, dcRequest.getHeaderMap(), pm);

        if (dbxResult.getResponse() != null) {
            JsonObject jsonObject = (JsonObject) dbxResult.getResponse();
            Result result = JSONToResult.convert(jsonObject.toString());

            for (Param param : processedResult.getAllParams()) {
                result.addParam(param);
            }

            return result;
        } else {
            HelperMethods.addError(processedResult, dbxResult);
        }

        return processedResult;

    }


    @Override
    public Result getBasicInformation(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {

        Result result = new Result();

        CustomerDTO customerDTO = new CustomerDTO();
        ProfileManagementResourceImpl resource = new ProfileManagementResourceImpl();

        customerDTO.setId(dcRequest.getParameter("Customer_id"));
        String customerId = dcRequest.getParameter("Customer_id");
        String legalEntityId = dcRequest.getParameter("legalEntityId");
        
        if(StringUtils.isBlank(customerId) || !HelperMethods.validateRegex(customerId) || !StringUtils.isAlphanumeric(customerId)) {
        	ErrorCodeEnum.ERR_10050.setErrorCode(result, "Invalid Customer Id");
        	return result;
        }
        
        if(StringUtils.isBlank(legalEntityId)) { 
        	ErrorCodeEnum.ERR_29040.setErrorCode(result);
        	return result;
        }
        //customerDTO.setCompanyLegalUnit(legalEntityId);
        customerDTO = (CustomerDTO) customerDTO.loadDTO();
        boolean isCustomerSearch = false;

        if (customerDTO == null) {
            isCustomerSearch = true;
            customerDTO = new CustomerDTO();
            customerDTO.setId(dcRequest.getParameter("Customer_id"));
            customerDTO.setUserName(dcRequest.getParameter("userName"));
            customerDTO.setCompanyLegalUnit(dcRequest.getParameter("legalEntityId"));
        } else if (customerDTO.getId().equals(customerDTO.getUserName())
                || resource.memberPresent(dcRequest.getParameter("Customer_id"), dcRequest.getHeaderMap())) {
            isCustomerSearch = true;
        }

        ProfileManagementBusinessDelegate businessDelegate =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ProfileManagementBusinessDelegate.class);
        PasswordHistoryManagement pm = new PasswordHistoryManagement(dcRequest, true);
        Map<String, String> configurations = BundleConfigurationHandler
                .fetchBundleConfigurations(BundleConfigurationHandler.BUDLENAME_C360, dcRequest);
        customerDTO.setCompanyLegalUnit(legalEntityId);
        DBXResult dbxResult = businessDelegate.getBasicInformation(configurations, customerDTO,
                dcRequest.getHeaderMap(), isCustomerSearch, pm);
        if (dbxResult.getResponse() != null) {
            JsonObject jsonObject = (JsonObject) dbxResult.getResponse();

            result = JSONToResult.convert(jsonObject.toString());
        }

        return result;
    }
    

    @Override
    public Result getByUserName(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) {

        Map<String, String> map = HelperMethods.getInputParamMap(inputArray);

        String userName = map.get("UserName");

        CustomerDTO customerDTO = new CustomerDTO();

        customerDTO.setUserName(userName);

        ProfileManagementBackendDelegateImpl backendDelegateImpl = new ProfileManagementBackendDelegateImpl();
        LegalEntityUtil.addCompanyIDToHeaders(request);
        DBXResult dbxResult = backendDelegateImpl.get(customerDTO, request.getHeaderMap());

        JsonObject jsonObject = (JsonObject) dbxResult.getResponse();
        JsonObject customer = new JsonObject();
        if (jsonObject.has(DBPDatasetConstants.DATASET_CUSTOMER)) {

            JsonArray customers = !jsonObject.get("customer").isJsonNull() ? jsonObject.get("customer").getAsJsonArray()
                    : new JsonArray();

            customer = customers.size() > 0 ? customers.get(0).getAsJsonObject() : new JsonObject();

            String statusId =
                    customer.has("Status_id") ? customer.get("Status_id").getAsString() : "";
            String customerID = customer.has("id") ? customer.get("id").getAsString() : "";

            if (StringUtils.isNotBlank(customerID)) {
                DTOUtils.loadDTOFromJson(customerDTO, customer, true);
                ProfileManagementBusinessDelegate businessDelegate =
                        DBPAPIAbstractFactoryImpl.getBusinessDelegate(ProfileManagementBusinessDelegate.class);

                dbxResult = businessDelegate.get(customerDTO, request.getHeaderMap());

                JsonObject party = (JsonObject) dbxResult.getResponse();

                for (Entry<String, JsonElement> entry : party.entrySet()) {
                    customer.add(entry.getKey(), entry.getValue());
                }
                customer.addProperty("Status_id", statusId);
            }
        }
        return JSONToResult.convert(jsonObject.toString());
    }

    @Override
    public Result get(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) {
        ProfileManagementResource managementResource = new ProfileManagementResourceImpl();
        return managementResource.get(methodID, inputArray, request, response);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Result checkifUserEnrolled(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse dcResponse) {
        ProfileManagementResource managementResource = new ProfileManagementResourceImpl();
        return managementResource.checkifUserEnrolled(methodID, inputArray, request, dcResponse);
    }

    @Override
    public Object sendCustomerUnlockEmail(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse response) {
        ProfileManagementResource managementResource = new ProfileManagementResourceImpl();
        return managementResource.sendCustomerUnlockEmail(methodID, inputArray, dcRequest, response);
    }

    @Override
    public Result getAddressTypes(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) {

        ProfileManagementResource managementResource = new ProfileManagementResourceImpl();
        return managementResource.getAddressTypes(methodID, inputArray, request, response);
    }

    @Override
    public Object createRetailContract(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws ApplicationException {

        String customerId = HelperMethods.getInputParamMap(inputArray).get(InfinityConstants.customerId);
        String id = "";
        BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
        backendIdentifierDTO.setCustomer_id(customerId);
        backendIdentifierDTO.setBackendType(DTOConstants.PARTY);
        DBXResult dbxResult = new DBXResult();
        try {
            dbxResult = DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class)
                    .get(backendIdentifierDTO, request.getHeaderMap());
        } catch (ApplicationException e) {
            alert.prepareError("exception while fetching Backend Identifier", e).log();
        }
        if (dbxResult.getResponse() != null) {
            BackendIdentifierDTO identifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
            id = identifierDTO.getBackendId();
        } else {
            throw new ApplicationException(ErrorCodeEnum.ERR_10349, "Party Backend Identifier not present");
        }

        PartyCustomerBusinessDelegate businessDelegate =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(PartyCustomerBusinessDelegate.class);
        LegalEntityUtil.addCompanyIDToHeaders(request);
        businessDelegate.activateCustomer(id, request.getHeaderMap());

        InfinityUserManagementResource infinityUserManagementResource =
                DBPAPIAbstractFactoryImpl.getResource(InfinityUserManagementResource.class);
        return infinityUserManagementResource.createRetailContract(methodID, inputArray, request, response);
    }

    @Override
    public Result getUserResponseForAlerts(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) {
        ProfileManagementResource resource = new ProfileManagementResourceImpl();
        return resource.getUserResponseForAlerts(methodID, inputArray, request, response);
    }
    
	@Override
	public Result userIdSearchDetailed(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		try {
			Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
			String userName = inputParams.get("userId");
			CustomerDTO customerDTO = new CustomerDTO();
			customerDTO.setUserName(userName);
			customerDTO = (CustomerDTO) customerDTO.loadDTO();
			BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
			backendIdentifierDTO.setCustomer_id(customerDTO.getId());
			backendIdentifierDTO.setBackendType("PARTY");

			BackendIdentifiersBackendDelegateimpl backendDelegateimpl = new BackendIdentifiersBackendDelegateimpl();
			List<BackendIdentifierDTO> identifierDTOs = new ArrayList<>();

			DBXResult result = backendDelegateimpl.getList(backendIdentifierDTO, dcRequest.getHeaderMap());
			if (null != result && null != result.getResponse() && StringUtils.isBlank(result.getDbpErrMsg())
					&& StringUtils.isBlank(result.getDbpErrCode())) {
				identifierDTOs = (List<BackendIdentifierDTO>) result.getResponse();
			}
			return getCustomerRecords(identifierDTOs, dcRequest,userName);

		} catch (Exception e) {
			alert.prepareError("Error occured", e).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_10379);

		}
	}
    
	private Result getCustomerRecords(List<BackendIdentifierDTO> backendidentifiers, DataControllerRequest dcRequest,String userid) {

		JsonArray recordsArray = new JsonArray();
		ProfileManagementBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(ProfileManagementBusinessDelegate.class);
		JsonObject processedResult = new JsonObject();
		if(!backendidentifiers.isEmpty()) {
		for (BackendIdentifierDTO backedDTO : backendidentifiers) {
			if (backedDTO == null)
				continue;
			MemberSearchBean memberSearchBean = new MemberSearchBean();
			memberSearchBean.setSearchType("CUSTOMER_SEARCH");
			memberSearchBean.setCustomerId(backedDTO.getBackendId());
			memberSearchBean.setSortVariable("name");
			memberSearchBean.setSortDirection("ASC");
			memberSearchBean.setPageOffset("0");
			memberSearchBean.setPageSize("100");
			memberSearchBean.setCompanyLegalUnit(backedDTO.getCompanyLegalUnit());
			memberSearchBean.setPartyPassed(true);

			Map<String, String> configurations = BundleConfigurationHandler
					.fetchBundleConfigurations(BundleConfigurationHandler.BUDLENAME_C360, dcRequest);
			 PasswordHistoryManagement pm = new PasswordHistoryManagement(dcRequest, true);
			DBXResult dbxResult = businessDelegate.searchCustomer(configurations, memberSearchBean,
					dcRequest.getHeaderMap(), pm);

			if (dbxResult.getResponse() != null) {
				JsonObject jsonObject = (JsonObject) dbxResult.getResponse();
				JsonElement customerSearchRecords = jsonObject.get("records");
				if (customerSearchRecords != null && customerSearchRecords.isJsonArray()) {
					recordsArray.addAll(customerSearchRecords.getAsJsonArray());
				}
			}
		}
		}
		else {
			Map<String, String> configurations = BundleConfigurationHandler
					.fetchBundleConfigurations(BundleConfigurationHandler.BUDLENAME_C360, dcRequest);

			MemberSearchBean memberSearchBean = new MemberSearchBean();
			memberSearchBean.setSearchType("CUSTOMER_SEARCH");
			memberSearchBean.setCustomerUsername(userid);
			memberSearchBean.setSortVariable("name");
			memberSearchBean.setSortDirection("ASC");
			memberSearchBean.setPageOffset("0");
			memberSearchBean.setPageSize("100");
			PasswordHistoryManagement pm = new PasswordHistoryManagement(dcRequest, true);
			// memberSearchBean.setCompanyLegalUnit(backedDTO.getCompanyLegalUnit());
			DBXResult dbxResult = businessDelegate.searchCustomer(configurations, memberSearchBean,
					dcRequest.getHeaderMap(), pm);
			if (dbxResult.getResponse() != null) {
				JsonObject jsonObject = (JsonObject) dbxResult.getResponse();
				JsonElement customerSearchRecords = jsonObject.get("records");
				if (customerSearchRecords != null && customerSearchRecords.isJsonArray()) {
					recordsArray.addAll(customerSearchRecords.getAsJsonArray());
				}
			}
		}
		processedResult.add("records", recordsArray);
		return JSONToResult.convert(processedResult.toString());
	}

	private Result getCustomerRecords(List<BackendIdentifierDTO> backendidentifiers, DataControllerRequest dcRequest) {

		JsonArray recordsArray = new JsonArray();
		JsonObject processedResult = new JsonObject();
		for (BackendIdentifierDTO backedDTO : backendidentifiers) {
			if (backedDTO == null)
				continue;
			MemberSearchBean memberSearchBean = new MemberSearchBean();
			memberSearchBean.setSearchType("CUSTOMER_SEARCH");
			memberSearchBean.setCustomerId(backedDTO.getBackendId());
			memberSearchBean.setSortVariable("name");
			memberSearchBean.setSortDirection("ASC");
			memberSearchBean.setPageOffset("0");
			memberSearchBean.setPageSize("100");
			memberSearchBean.setCompanyLegalUnit(backedDTO.getCompanyLegalUnit());

			ProfileManagementBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(ProfileManagementBusinessDelegate.class);

			Map<String, String> configurations = BundleConfigurationHandler
					.fetchBundleConfigurations(BundleConfigurationHandler.BUDLENAME_C360, dcRequest);
			PasswordHistoryManagement pm = new PasswordHistoryManagement(dcRequest, true);
			DBXResult dbxResult = businessDelegate.searchCustomer(configurations, memberSearchBean,
					dcRequest.getHeaderMap(), pm);

			if (dbxResult.getResponse() != null) {
				JsonObject jsonObject = (JsonObject) dbxResult.getResponse();
				JsonElement customerSearchRecords = jsonObject.get("records");
				if (customerSearchRecords != null && customerSearchRecords.isJsonArray()) {
					recordsArray.addAll(customerSearchRecords.getAsJsonArray());
				}
			}
		}
		processedResult.add("records", recordsArray);
		return JSONToResult.convert(processedResult.toString());
	}

    @Override
   	public Result userIdSearch(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
   			DataControllerResponse dcResponse) throws ApplicationException {
   		Result result = new Result();
   		try {
   			Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
   			String userName = inputParams.get("userId");
   			 ProfileManagementBusinessDelegate businessDelegate =
   		                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ProfileManagementBusinessDelegate.class);
                
   			 DBXResult dbxResult =
   		                businessDelegate.userIdSearch(userName, dcRequest.getHeaderMap());
   			  if (dbxResult.getResponse() != null) {
   		            JsonObject jsonObject = (JsonObject) dbxResult.getResponse();
   		            result = JSONToResult.convert(jsonObject.toString());
   		            

   			  }
   		            
   			
   		} catch (Exception e) {
   			throw new ApplicationException(ErrorCodeEnum.ERR_10379);

   		}
   		return result;
   	}

	@Override
	public Result customerLegalEntitiesGet(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse dcResponse) throws ApplicationException {
		Result result = new Result();

		Map<String, String> map = HelperMethods.getInputParamMap(inputArray);

		Iterator<String> iterator = request.getParameterNames();

		JsonObject inputObject = new JsonObject();

		while (iterator.hasNext()) {
			String key = iterator.next();
			if ((!map.containsKey(key) || StringUtils.isBlank(map.get(key)))
					&& StringUtils.isNotBlank(request.getParameter(key))) {
				map.put(key, request.getParameter(key));
			}
		}

		String customerId = map.get(InfinityConstants.customerId);
		
		Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromAPIDBPIdentityService(request);
		if (HelperMethods.isPSD2Agent(request)) {
			inputObject.addProperty(InfinityConstants.isSuperAdmin, "true");
		} else if (HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) {
			Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
			if (!userPermissions.contains("USER_MANAGEMENT")) {
				ErrorCodeEnum.ERR_10051.setErrorCode(result);
				return result;
			}

			inputObject.addProperty(InfinityConstants.isSuperAdmin, "false");
		} else {
			inputObject.addProperty(InfinityConstants.isSuperAdmin, "true");
		}
		
		if(!CommonUtils.validInput(customerId))
        {
        	throw new ApplicationException(ErrorCodeEnum.ERR_10050);
        }
		
		 ProfileManagementBusinessDelegate businessDelegate =
	                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ProfileManagementBusinessDelegate.class);

	        Map<String, String> configurations = BundleConfigurationHandler
	                .fetchBundleConfigurations(BundleConfigurationHandler.BUDLENAME_C360, request);

	        CustomerDTO customerDTO = new CustomerDTO();
	        customerDTO.setId(customerId);
	        DBXResult dbxResult =
	                businessDelegate.customerLegalEntitiesGet(customerDTO, request.getHeaderMap());
	        if (dbxResult.getResponse() != null) {
	            JsonObject jsonObject = (JsonObject) dbxResult.getResponse();
	            result = JSONToResult.convert(jsonObject.toString());

	            return result;
	        } 

		return result;
		
	}


}
