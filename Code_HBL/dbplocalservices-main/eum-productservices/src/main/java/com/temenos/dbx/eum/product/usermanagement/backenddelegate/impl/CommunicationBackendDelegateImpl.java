package com.temenos.dbx.eum.product.usermanagement.backenddelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.infinity.dbx.dbp.jwt.auth.AuthConstants;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.IntegrationTemplateURLFinder;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.OperationName;
import com.kony.dbputilities.util.ServiceId;
import com.kony.dbputilities.util.URLConstants;
import com.kony.eum.dbputilities.util.ServiceCallHelper;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.CoreCustomerBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.BackendIdentifiersBackendDelegate;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.dto.CustomerCommunicationDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.MembershipDTO;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.CommunicationBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.impl.CommunicationBackendDelegateImpl;
import com.temenos.dbx.product.utils.DTOConstants;
import com.temenos.dbx.product.utils.DTOUtils;
import com.temenos.dbx.product.utils.InfinityConstants;

public class CommunicationBackendDelegateImpl implements CommunicationBackendDelegate {
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public DBXResult get(CustomerCommunicationDTO dto, Map<String, Object> headerMap) {
        DBXResult dbxResult = new DBXResult();

        List<CustomerCommunicationDTO> dtoList = new ArrayList<>();

        if (StringUtils.isBlank(dto.getCustomer_id())) {
            return dbxResult;
        }

        Map<String, Object> inputParams = new HashMap<>();
        String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + dto.getCustomer_id();
        
        if(StringUtils.isNotBlank(dto.getCompanyLegalUnit())){
        	filter += DBPUtilitiesConstants.AND + "companyLegalUnit" + DBPUtilitiesConstants.EQUAL + dto.getCompanyLegalUnit();
        }

        String select =
                "id,Type_id,isPrimary,Value,Extension,Description,phoneCountryCode,isTypeBusiness,isAlertsRequired";

        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        inputParams.put(DBPUtilitiesConstants.SELECT, select);

        JsonObject communicationJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
                URLConstants.CUSTOMER_COMMUNICATION_GET);

        if (JSONUtil.isJsonNotNull(communicationJson)
                && JSONUtil.hasKey(communicationJson, DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION)
                && communicationJson.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION).isJsonArray()) {
            JsonArray communicationArray =
                    communicationJson.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION).getAsJsonArray();

            for (JsonElement element : communicationArray) {
                if (element.isJsonObject()) {
                    dto = (CustomerCommunicationDTO) DTOUtils.loadJsonObjectIntoObject(element.getAsJsonObject(),
                            CustomerCommunicationDTO.class, true);
                    if (null != dto) {
                        dtoList.add(dto);
                    }
                }

            }

            dbxResult.setResponse(dtoList);
        }

        return dbxResult;
    }

    @Override
    public DBXResult create(CustomerCommunicationDTO inputDTO,
            Map<String, Object> headersMap) {

        DBXResult dbxResult = new DBXResult();

        CustomerCommunicationDTO resultDTO = null;
        if (null == inputDTO || StringUtils.isBlank(inputDTO.getId())) {
            return dbxResult;
        }
        Map<String, Object> inputParams = DTOUtils.getParameterMap(inputDTO, true);
        HelperMethods.removeNullValues(inputParams);
        JsonObject communicationJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                URLConstants.CUSTOMERCOMMUNICATION_CREATE);
        if (JSONUtil.isJsonNotNull(communicationJson)
                && JSONUtil.hasKey(communicationJson, DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION)
                && communicationJson.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION).isJsonArray()) {
            JsonArray communicationArray =
                    communicationJson.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION).getAsJsonArray();
            JsonObject object =
                    communicationArray.size() > 0 ? communicationArray.get(0).getAsJsonObject() : new JsonObject();
            resultDTO = (CustomerCommunicationDTO) DTOUtils.loadJsonObjectIntoObject(object,
                    CustomerCommunicationDTO.class, true);

            dbxResult.setResponse(resultDTO);
        }

        return dbxResult;
    }

    @Override
    public DBXResult getPrimaryCommunicationForLogin(CustomerCommunicationDTO customerCommunicationDTO,
            Map<String, Object> headerMap) {
        DBXResult dbxResult = new DBXResult();
        JsonObject jsonObject = new JsonObject();
        dbxResult.setResponse(jsonObject);

        if (StringUtils.isBlank(customerCommunicationDTO.getCustomer_id())) {
            return dbxResult;
        }

        final String IS_Integrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);
        try {
            if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")) {
                return getPrimaryCommunicationDetailsForT24(customerCommunicationDTO, headerMap);
            }
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching customer communication details from T24"
                    + e.getMessage()).log();
        }
        
        Map<String, Object> inputParams = new HashMap<>();
        String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerCommunicationDTO.getCustomer_id()
                + DBPUtilitiesConstants.AND +
                "isPrimary" + DBPUtilitiesConstants.EQUAL + "1";

        inputParams.put(DBPUtilitiesConstants.FILTER, filter);

        JsonObject communicationJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
                URLConstants.CUSTOMER_COMMUNICATION_GET);

        if (communicationJson.has(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION)
                && !communicationJson.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION).isJsonNull()) {

            JsonArray array =
                    communicationJson.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION).getAsJsonArray();
            JsonObject jsonObject2 = new JsonObject();
            for (int i = 0; i < array.size(); i++) {
                JsonObject communiation = array.get(i).getAsJsonObject();

                if (communiation.has("Type_id")
                        && communiation.get("Type_id").getAsString().equals(DBPUtilitiesConstants.COMM_TYPE_EMAIL)) {
                    jsonObject2.add(DTOConstants.EMAIL, communiation.get("Value"));
                } else if (communiation.has("Type_id")
                        && communiation.get("Type_id").getAsString().equals(DBPUtilitiesConstants.COMM_TYPE_PHONE)) {
                    jsonObject2.add(DTOConstants.PHONE, communiation.get("Value"));
                    jsonObject2.add(DTOConstants.PHONECOUNTRYCODE, communiation.get("phoneCountryCode"));
                }
            }
            jsonObject.add(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION, jsonObject2);
            dbxResult.setResponse(jsonObject);
        }

        return dbxResult;
    }

    @Override
    public DBXResult getPrimaryCommunication(CustomerCommunicationDTO customerCommunicationDTO,
            Map<String, Object> headerMap) {
        DBXResult dbxResult = new DBXResult();
        JsonObject jsonObject = new JsonObject();
        dbxResult.setResponse(jsonObject);

        if (StringUtils.isBlank(customerCommunicationDTO.getCustomer_id())) {
            return dbxResult;
        }

        Map<String, Object> inputParams = new HashMap<>();
        String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerCommunicationDTO.getCustomer_id()
                + DBPUtilitiesConstants.AND +
                "isPrimary" + DBPUtilitiesConstants.EQUAL + "1";

        inputParams.put(DBPUtilitiesConstants.FILTER, filter);

        JsonObject communicationJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
                URLConstants.CUSTOMER_COMMUNICATION_GET);

        dbxResult.setResponse(communicationJson);

        return dbxResult;
    }

    @Override
    public DBXResult getCommunication(CustomerCommunicationDTO customerCommunicationDTO,
            Map<String, Object> headerMap) {
        DBXResult dbxResult = new DBXResult();
        JsonObject jsonObject = new JsonObject();
        dbxResult.setResponse(jsonObject);

        if (StringUtils.isBlank(customerCommunicationDTO.getCustomer_id())) {
            return dbxResult;
        }

        Map<String, Object> inputParams = new HashMap<>();
        String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerCommunicationDTO.getCustomer_id();

        inputParams.put(DBPUtilitiesConstants.FILTER, filter);

        JsonObject communicationJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
                URLConstants.CUSTOMER_COMMUNICATION_GET);

        dbxResult.setResponse(communicationJson);

        return dbxResult;
    }

    @Override
    public DBXResult getPrimaryCommunicationDetails(CustomerCommunicationDTO customerCommunicationDTO,
            Map<String, Object> headerMap) {

        DBXResult dbxResult = new DBXResult();
        JsonObject jsonObject = new JsonObject();
        dbxResult.setResponse(jsonObject);

        String customerId = customerCommunicationDTO.getCustomer_id();

        final String IS_Integrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);
        try {
            if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")) {
                return getPrimaryCommunicationDetailsForT24(customerCommunicationDTO, headerMap);
            }
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching customer communication details from T24"
                    + e.getMessage()).log();
        }

        if (StringUtils.isBlank(customerId)) {
            return dbxResult;
        }

        Map<String, Object> inputParams = new HashMap<>();
        String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId
                + DBPUtilitiesConstants.AND +
                "isPrimary" + DBPUtilitiesConstants.EQUAL + "1";

        inputParams.put(DBPUtilitiesConstants.FILTER, filter);

        JsonObject communicationJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
                URLConstants.CUSTOMER_COMMUNICATION_GET);

        if (communicationJson.has(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION)
                && !communicationJson.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION).isJsonNull()) {

            JsonArray array =
                    communicationJson.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION).getAsJsonArray();
            JsonObject jsonObject2 = new JsonObject();
            for (int i = 0; i < array.size(); i++) {
                JsonObject communiation = array.get(i).getAsJsonObject();

                if (communiation.has("Type_id")
                        && communiation.get("Type_id").getAsString().equals(DBPUtilitiesConstants.COMM_TYPE_EMAIL)) {
                    jsonObject2.add(DTOConstants.EMAIL, communiation.get("Value"));
                } else if (communiation.has("Type_id")
                        && communiation.get("Type_id").getAsString().equals(DBPUtilitiesConstants.COMM_TYPE_PHONE)) {
                    jsonObject2.add(DTOConstants.PHONE, communiation.get("Value"));
                    jsonObject2.add(DTOConstants.PHONECOUNTRYCODE, communiation.get("phoneCountryCode"));
                }
            }
            jsonObject.add(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION, jsonObject2);
            dbxResult.setResponse(jsonObject);
        }

        return dbxResult;
    }

    private DBXResult getPrimaryCommunicationDetailsForT24(CustomerCommunicationDTO customerCommunicationDTO,
            Map<String, Object> headersMap) {
        DBXResult dbxResult = new DBXResult();
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonObject2 = new JsonObject();
        Map<String, Object> inputParams = new HashMap<>();

        BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
        backendIdentifierDTO.setCustomer_id(customerCommunicationDTO.getCustomer_id());
        String coreCustomerId = "";
        String legalEntityId = "";
        backendIdentifierDTO.setBackendType(IntegrationTemplateURLFinder.getBackendURL(InfinityConstants.BackendType));
        try {
            dbxResult = DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class)
                    .get(backendIdentifierDTO, headersMap);
            BackendIdentifierDTO identifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
            if (identifierDTO != null) {
                coreCustomerId = identifierDTO.getBackendId();
                legalEntityId = identifierDTO.getCompanyLegalUnit();
            }
        } catch (ApplicationException e1) {
            alert.prepareError("Exception occured while fetching party backendidentifier ID"
                    + customerCommunicationDTO.getCustomer_id()).log();
        }
        
        if(StringUtils.isBlank(coreCustomerId)) {
            inputParams.put(DBPUtilitiesConstants.FILTER, "Customer_id" + DBPUtilitiesConstants.EQUAL + customerCommunicationDTO.getCustomer_id() + DBPUtilitiesConstants.AND +
                    "isPrimary" + DBPUtilitiesConstants.EQUAL + "1");
            JsonObject customerJson =
                    ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                            URLConstants.CUSTOMERCOMMUNICATION_GET);
            JsonArray array =
                    customerJson.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION).getAsJsonArray();
            for (int i = 0; i < array.size(); i++) {
                JsonObject communiation = array.get(i).getAsJsonObject();

                if (communiation.has("Type_id")
                        && communiation.get("Type_id").getAsString().equals(DBPUtilitiesConstants.COMM_TYPE_EMAIL)) {
                    jsonObject2.add(DTOConstants.EMAIL, communiation.get("Value"));
                } else if (communiation.has("Type_id")
                        && communiation.get("Type_id").getAsString().equals(DBPUtilitiesConstants.COMM_TYPE_PHONE)) {
                    jsonObject2.add(DTOConstants.PHONE, communiation.get("Value"));
                    jsonObject2.add(DTOConstants.PHONECOUNTRYCODE, communiation.get("phoneCountryCode"));
                }
            }
            jsonObject.add(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION, jsonObject2);
            dbxResult.setResponse(jsonObject);
        }
        else{
            CoreCustomerBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(CoreCustomerBusinessDelegate.class);
            MembershipDTO membershipDTO = new MembershipDTO();
            try {
                membershipDTO = businessDelegate.getMembershipDetails(coreCustomerId, legalEntityId, headersMap);
            } catch (ApplicationException e) {
               
                alert.prepareError("Exception occured while fetching the customer communication details").log();
                
            }
            jsonObject2.addProperty(DTOConstants.EMAIL, membershipDTO.getEmail());
            jsonObject2.addProperty(DTOConstants.PHONE, membershipDTO.getPhone());
            jsonObject2.addProperty("addressLine1", membershipDTO.getAddressLine1());
            jsonObject2.addProperty("addressLine2", membershipDTO.getAddressLine2());
            jsonObject2.addProperty("zipCode", membershipDTO.getZipCode());
            jsonObject2.addProperty("state", membershipDTO.getState());
            jsonObject2.addProperty("country", membershipDTO.getCountry());
            jsonObject2.addProperty("countrycode", membershipDTO.getCountry());
            jsonObject2.addProperty("CountryCode", membershipDTO.getCountry());
            jsonObject2.addProperty("cityName", membershipDTO.getCityName());
            jsonObject2.addProperty("Region_id", membershipDTO.getState());
            jsonObject2.addProperty("City_id", membershipDTO.getCityName());
            jsonObject2.addProperty("CountryCode", membershipDTO.getCountry());
            jsonObject2.addProperty("cityName", membershipDTO.getCityName());
            jsonObject2.addProperty("Region_id", membershipDTO.getState());
            jsonObject2.addProperty("City_id", membershipDTO.getCityName());
            jsonObject2.addProperty("taxId", membershipDTO.getTaxId());
            jsonObject2.addProperty("Ssn", membershipDTO.getTaxId());
            jsonObject2.addProperty("IDType_id", membershipDTO.getIDType_id());
            jsonObject2.addProperty("IDValue", membershipDTO.getIDValue());
            jsonObject2.addProperty("IDIssueDate", membershipDTO.getIDIssueDate());
            jsonObject2.addProperty("IDExpiryDate", membershipDTO.getIDExpiryDate());
            jsonObject.add(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION, jsonObject2);
            dbxResult.setResponse(jsonObject);
        }
        
        return dbxResult;
    }

    @Override
    public DBXResult getCommunicationDetails(CustomerCommunicationDTO customerCommunicationDTO,
            Map<String, Object> headerMap) {
        DBXResult dbxResult = new DBXResult();
        JsonObject jsonObject = new JsonObject();
        dbxResult.setResponse(jsonObject);

        if (StringUtils.isBlank(customerCommunicationDTO.getCustomer_id())) {
            return dbxResult;
        }

        Map<String, Object> inputParams = new HashMap<>();
        String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerCommunicationDTO.getCustomer_id()
                + DBPUtilitiesConstants.AND +
                "isPrimary" + DBPUtilitiesConstants.EQUAL + "0";

        inputParams.put(DBPUtilitiesConstants.FILTER, filter);

        JsonObject communicationJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
                URLConstants.CUSTOMER_COMMUNICATION_GET);

        dbxResult.setResponse(communicationJson);

        return dbxResult;
    }

    @Override
    public DBXResult getPrimaryMFACommunicationDetails(CustomerCommunicationDTO customerCommunicationDTO,
            Map<String, Object> headerMap) {
        DBXResult dbxResult = new DBXResult();
        if (StringUtils.isBlank(customerCommunicationDTO.getCustomer_id())) {
            return dbxResult;
        }
        Map<String, Object> inputParams = new HashMap<>();
        final String IS_Integrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);
        try {
            if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")) {
                DBXResult response = getMFACommunicationDetailsForT24(customerCommunicationDTO, headerMap);
                if (response != null && response.getResponse() != null)
                    return response;
            }
            String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerCommunicationDTO.getCustomer_id()
                    + DBPUtilitiesConstants.AND +
                    "isPrimary" + DBPUtilitiesConstants.EQUAL + "1";
            inputParams.put(DBPUtilitiesConstants.FILTER, filter);
            JsonObject communicationJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
                    URLConstants.CUSTOMER_COMMUNICATION_GET);
            dbxResult.setResponse(communicationJson);
        } catch (Exception e) {

        }
        return dbxResult;
    }

    private DBXResult getMFACommunicationDetailsForT24(CustomerCommunicationDTO customerCommunicationDTO,
            Map<String, Object> headersMap) {
        DBXResult response = new DBXResult();
        try {
            BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
            backendIdentifierDTO.setCustomer_id(customerCommunicationDTO.getCustomer_id());
            if (StringUtils.isNotBlank(customerCommunicationDTO.getCompanyLegalUnit())) {
				backendIdentifierDTO.setCompanyLegalUnit(customerCommunicationDTO.getCompanyLegalUnit());
			}
            DBXResult dbxResult = DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class)
                    .get(backendIdentifierDTO, headersMap);
            BackendIdentifierDTO identifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
            if (identifierDTO == null)
                return null;
            Map<String, Object> inputParams = new HashMap<>();
            inputParams.put("customerId", identifierDTO.getBackendId());
            HelperMethods.addJWTAuthHeader(headersMap, AuthConstants.PRE_LOGIN_FLOW);
            /*This BRANCH_ID_REFERENCE usage is defined as a fall back*/
            if(StringUtils.isBlank(identifierDTO.getCompanyLegalUnit())) {
            headersMap.put("companyId",
                    EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE));
            }
            else{
                headersMap.put("companyId",
                        identifierDTO.getCompanyLegalUnit());
            }
            JsonObject t24Response = ServiceCallHelper.invokeServiceAndGetJson(ServiceId.T24ISUSER_INTEGRATION_SERVICE,
                    null, OperationName.CORE_CUSTOMER_SEARCH,
                    inputParams, headersMap);

            JsonObject customerResponse = new JsonObject();
            if (!JSONUtil.hasKey(t24Response, DBPDatasetConstants.DATASET_CUSTOMERS)
                    || t24Response.get(DBPDatasetConstants.DATASET_CUSTOMERS).getAsJsonArray().size() < 0) {
                alert.prepareError("Exception occured while fetching the customer communication details").log();
            }
            customerResponse =
                    t24Response.get(DBPDatasetConstants.DATASET_CUSTOMERS).getAsJsonArray().get(0).getAsJsonObject();
            JsonArray communication = new JsonArray();
            JsonObject obj = new JsonObject();
            obj.addProperty("Type_id", DBPUtilitiesConstants.COMM_TYPE_EMAIL);
            obj.addProperty("Value", JSONUtil.getString(customerResponse, "email"));
            communication.add(obj);
            obj = new JsonObject();
            obj.addProperty("Type_id", DBPUtilitiesConstants.COMM_TYPE_PHONE);
            obj.addProperty("Value", JSONUtil.getString(customerResponse, "phone"));
            communication.add(obj);
            obj = new JsonObject();
            obj.addProperty("Type_id", InfinityConstants.ssn);
            obj.addProperty("Value", JSONUtil.getString(customerResponse,InfinityConstants.ssn));
            communication.add(obj);
            obj = new JsonObject();
            obj.addProperty("Type_id", InfinityConstants.dob);
            obj.addProperty("Value", JSONUtil.getString(customerResponse, InfinityConstants.dateOfBirth));
            communication.add(obj);
            JsonObject jsonObject = new JsonObject();
            jsonObject.add(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION, communication);
            response.setResponse(jsonObject);
        } catch (Exception e) {

        }
        return response;
    }
}
