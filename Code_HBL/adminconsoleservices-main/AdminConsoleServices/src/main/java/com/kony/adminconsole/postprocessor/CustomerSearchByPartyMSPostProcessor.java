package com.kony.adminconsole.postprocessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CustomerSearchByPartyMSPostProcessor implements DataPostProcessor2 {

    @Override
    public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {
		Log4j2Configurator.getInstance();

        try {
            String name;
            String partyId = null;
            String id = null;
            Map<String, String> postParametersMap = new HashMap<>();
            Dataset recordDS = result.getDatasetById("records");
            if (recordDS == null) {
                return null;
            }
            if (recordDS != null) {
                List<Record> recordsList = recordDS.getAllRecords();
                int size = recordsList.size();
                String status = "Records returned: " + size;
                result.addParam("Status", status);
                result.addParam("TotalResultsFound", String.valueOf(size));
                result.addParam("SortVariable", "name");
                result.addIntParam("PageSize", 20);
                result.addIntParam("PageOffset", 0);
                result.addParam("SortDirection", "ASC");
                if (recordsList.size() != 0) {
                    for (Record record : recordsList) {
                        String Gender = record.getParamValueByName("Gender");
                        String Salutation = record.getParamValueByName("Salutation");
                        String CustomerType = record.getParamValueByName("CustomerTypeId");
                        String FirstName = record.getParamValueByName("FirstName");
                        String MiddleName = record.getParamValueByName("MiddleName");
                        String LastName = record.getParamValueByName("LastName");
                        String DateOfBirth = record.getParamValueByName("DateOfBirth");
                        if (StringUtils.isNotBlank(DateOfBirth))
                            DateOfBirth = DateOfBirth.substring(0, DateOfBirth.indexOf(" ") - 1);
                        String partyFlag = "2";
                        if (MiddleName != null)
                            name = FirstName + " " + MiddleName + " " + LastName;
                        else
                            name = FirstName + " " + LastName;
                        partyId = record.getParamValueByName("partyId");
                        String sequence = record.getParamValueByName("backEndIdentifier");
                        id = getCustomerId(partyId, request, sequence);
                        if (id != null) {
                            String username = getUsernameFromId(id, request);
                            if (username != null)
                                record.addParam("Username", username);
                        }

                        partyFlag = record.getParamValueByName("partyFlag");
                        if (partyFlag == null)
                            partyFlag = "2";
                        if (Gender != null)
                            Gender = getGenderById(Gender);
                        if (Salutation != null)
                            Salutation = getSalutationById(Salutation);
                        CustomerType = getCustomerTypeById(CustomerType, partyFlag);
                        String IdentityType = record.getParamValueByName("typeOfLegalIdentifier");
                        if (IdentityType != null && IdentityType.equalsIgnoreCase("1")) {
                            record.addParam("Ssn", record.getParamValueByName("legalIdentifierNumber"));
                        }
                        record.addParam("partyFlag", partyFlag);
                        record.addParam("DateOfBirth", DateOfBirth);
                        record.addParam("Gender", Gender);
                        record.addParam("Salutation", Salutation);
                        record.addParam("CustomerTypeId", CustomerType);
                        record.addParam("name", name);
                        record.addParam("id", id);

                        String phoneNum1 = record.getParamValueByName("phoneNum1");
                        String phoneNum2 = record.getParamValueByName("phoneNum2");
                        String phoneNum3 = record.getParamValueByName("phoneNum3");
                        if (StringUtils.isNotBlank(phoneNum1)) {
                            record.addParam("PrimaryPhoneNumber", phoneNum1);
                        }
                        if (StringUtils.isNotBlank(phoneNum2)) {
                            record.addParam("PrimaryPhoneNumber", phoneNum2);
                        }
                        if (StringUtils.isNotBlank(phoneNum3)) {
                            record.addParam("PrimaryPhoneNumber", phoneNum3);
                        }

                        String email1 = record.getParamValueByName("email1");
                        String email2 = record.getParamValueByName("email2");
                        String email3 = record.getParamValueByName("email3");
                        if (StringUtils.isNotBlank(email1)) {
                            record.addParam("PrimaryEmailAddress", email1);
                        }
                        if (StringUtils.isNotBlank(email2)) {
                            record.addParam("PrimaryEmailAddress", email2);
                        }
                        if (StringUtils.isNotBlank(email3)) {
                            record.addParam("PrimaryEmailAddress", email3);
                        }
                    }
                }
                if (size == 1 && partyId != null) {
                    postParametersMap.put("partyId", partyId);
                    String endPointResponse = Executor.invokeService(ServiceURLEnum.CUSTOMER_SEARCH_PARTY_MS,
                            postParametersMap, null, request);
                    if (endPointResponse != null) {
                        JSONObject serviceResponseJSON = CommonUtilities.getStringAsJSONObject(endPointResponse);
                        Record record = new Record();
                        JSONObject customerBasicInfoViewJSON =
                                serviceResponseJSON.getJSONObject("customerbasicinfo_view");
                        record = CommonUtilities.constructRecordFromJSONObject(customerBasicInfoViewJSON);
                        record.setId("customerbasicinfo_view");
                        result.addRecord(record);
                        JSONObject ConfigurationJSON = serviceResponseJSON.getJSONObject("Configuration");
                        Record recordConf = new Record();
                        recordConf = CommonUtilities.constructRecordFromJSONObject(ConfigurationJSON);
                        recordConf.setId("Configuration");
                        result.addRecord(recordConf);
                    }
                }
                if (size == 0) {
                    return null;
                }

            }
            return result;
        } catch (Exception e) {
            result.addParam(new Param("FailureReason", e.getMessage()));
            ErrorCodeEnum.ERR_20716.setErrorCode(result);
            return null;
        }
    }

    private String getUsernameFromId(String id, DataControllerRequest requestInstance) {
        Map<String, String> postParamsMap = new HashMap<>();
        postParamsMap.put(ODataQueryConstants.FILTER, "id eq '" + id + "'");
        postParamsMap.put(ODataQueryConstants.SELECT, "UserName");

        String serviceResponse = Executor.invokeService(ServiceURLEnum.CUSTOMER_READ, postParamsMap, null,
                requestInstance);
        if (serviceResponse != null) {
            JSONObject serviceResponseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);

            JSONArray serviceResponseArray = serviceResponseJSON.optJSONArray("customer");
            if (serviceResponseArray.length() != 0) {
                String Username = serviceResponseArray.optJSONObject(0).optString("UserName");
                return Username;
            }
        }
        return null;
    }

    private String getCustomerId(String BackendId, DataControllerRequest requestInstance, String sequence) {

        Map<String, String> postParamsMap = new HashMap<>();
        postParamsMap.put(ODataQueryConstants.FILTER, "BackendId eq '" + BackendId + "'");
        postParamsMap.put(ODataQueryConstants.SELECT, "Customer_id");

        String readBackendIdentifierResponse = Executor.invokeService(ServiceURLEnum.BACKENDIDENTIFIER_READ,
                postParamsMap, null, requestInstance);
        if (readBackendIdentifierResponse != null) {
            JSONObject serviceResponseJSON = CommonUtilities.getStringAsJSONObject(readBackendIdentifierResponse);

            JSONArray serviceResponseArray = serviceResponseJSON.optJSONArray("backendidentifier");
            if (serviceResponseArray.length() != 0) {
                String id = serviceResponseArray.optJSONObject(0).optString("Customer_id");
                return id;
            }
        }

        return null;
    }

    private String getCustomerTypeById(String customerType, String partyFlag) {
        if (customerType.equalsIgnoreCase("1") && partyFlag.equalsIgnoreCase("1"))
            return "TYPE_ID_PROSPECT";
        else if (customerType.equalsIgnoreCase("1") && partyFlag.equalsIgnoreCase("2"))
            return "TYPE_ID_RETAIL";
        else
            return "TYPE_ID_BUSINESS";
    }

    private String getSalutationById(String salutation) {
        if (salutation.equalsIgnoreCase("1"))
            return "Mr.";
        if (salutation.equalsIgnoreCase("2"))
            return "Mrs.";
        if (salutation.equalsIgnoreCase("3"))
            return "Miss.";
        return null;
    }

    private String getGenderById(String gender) {
        if (gender.equalsIgnoreCase("1")) {
            return "Female";
        } else if (gender.equalsIgnoreCase("2"))
            return "Male";

        return "Others";
    }

}
