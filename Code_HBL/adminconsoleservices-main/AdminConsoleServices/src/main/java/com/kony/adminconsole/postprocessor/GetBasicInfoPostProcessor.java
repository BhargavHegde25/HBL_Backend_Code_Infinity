package com.kony.adminconsole.postprocessor;

import java.util.HashMap;
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
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetBasicInfoPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Log4j2Configurator.getInstance();
		try {
			Record record = new Record();
			record.setId("Configuration");
			record.addParam("value", "1440");
			result.addRecord(record);

			Record recordResponse = result.getRecordById("customerbasicinfo_view");
			if(recordResponse.getAllParams().size()==0)
				return null;
			String employmentType = recordResponse.getParamValueByName("employmentType");
			if (employmentType != null) {
				if (employmentType.equalsIgnoreCase("1") || employmentType.equalsIgnoreCase("2")) {
					recordResponse.addParam("EmployementStatus_name", "Employed");
					recordResponse.addParam("EmployementStatus_id", "SID_EMPLOYED");
				} else if (employmentType.equalsIgnoreCase("3")) {
					recordResponse.addParam("EmployementStatus_name", "Student");
					recordResponse.addParam("EmployementStatus_id", "SID_STUDENT");
				} else if (employmentType.equalsIgnoreCase("4")) {
					recordResponse.addParam("EmployementStatus_name", "Individual");
					recordResponse.addParam("EmployementStatus_id", "SID_INDIVIDUAL");
				} else if (employmentType.equalsIgnoreCase("5")) {
					recordResponse.addParam("EmployementStatus_name", "Retired");
					recordResponse.addParam("EmployementStatus_id", "SID_RETIRED");
				}
			}
			String IdentityType = recordResponse.getParamValueByName("typeOfLegalIdentifier");
			if (IdentityType != null && IdentityType.equalsIgnoreCase("1")) {
				String SSN = recordResponse.getParamValueByName("legalIdentifierNumber");
				StringBuffer SSNMask = new StringBuffer();
				if (SSN != null && SSN.length() >= 4) {
					for (int i = 0; i < SSN.length(); i++) {
						if (i < SSN.length() - 4) {
							SSNMask.append("*");

						} else {
							SSNMask.append(SSN.charAt(i));
						}
					}
				}
				recordResponse.addParam("SSN", SSNMask.toString());
			}

			// Customer Type
			String customerType = recordResponse.getParamValueByName("partyType");
			String partyFlag = "2";

			if (recordResponse.getParamValueByName("partyFlag") != null)
				partyFlag = recordResponse.getParamValueByName("partyFlag");
			String description;
			String CustomerTypeName;
			String CustomerTypeId;
			String IsOlbAllowed;
			String isCustomerAccessiable;
			String IsEnrolledForOlb;
			if (customerType.equalsIgnoreCase("1")) {
				if (partyFlag.equalsIgnoreCase("1")) {
					description = "Prospect user";
					CustomerTypeName = "Prospect";
					CustomerTypeId = "TYPE_ID_PROSPECT";
					IsOlbAllowed = "false";
					isCustomerAccessiable = "false";
					IsEnrolledForOlb = "false";
					Record recordOlb = new Record();
					recordOlb.setId("OLBCustomerFlags");
					recordOlb.addParam("Status", "Active");
					recordResponse.addRecord(recordOlb);
				} else {
					description = "Retail Banking user";
					CustomerTypeName = "Retail";
					CustomerTypeId = "TYPE_ID_RETAIL";
					IsOlbAllowed = "true";
					isCustomerAccessiable = "true";
					IsEnrolledForOlb = "true";
					Record recordOlb = new Record();
					recordOlb.setId("OLBCustomerFlags");
					recordOlb.addParam("Status", "Active");
					recordResponse.addRecord(recordOlb);
				}

			} else {
				description = "Business Banking user";
				CustomerTypeName = "Business";
				CustomerTypeId = "TYPE_ID_BUSINESS";
				IsOlbAllowed = "false";
				isCustomerAccessiable = "false";
				IsEnrolledForOlb = "false";
				Record recordOlb = new Record();
				recordOlb.setId("OLBCustomerFlags");
				recordOlb.addParam("Status", "Active");
				recordResponse.addRecord(recordOlb);
			}
			recordResponse.addParam("CustomerType_Description", description);
			recordResponse.addParam("CustomerType_Name", CustomerTypeName);
			recordResponse.addParam("CustomerType_id", CustomerTypeId);
			recordResponse.addParam("IsOlbAllowed", IsOlbAllowed);
			recordResponse.addParam("isCustomerAccessiable", isCustomerAccessiable);
			recordResponse.addParam("IsEnrolledForOlb", IsEnrolledForOlb);

			// MaritalStatus

			String maritalStatus = recordResponse.getParamValueByName("maritalStatus");
			String MaritalStatus_name;
			String MaritalStatus_id;
			if (maritalStatus != null) {
				if (maritalStatus.equalsIgnoreCase("1")) {
					MaritalStatus_name = "Married";
					MaritalStatus_id = "SID_MARRIED";
				} else {
					MaritalStatus_name = "Single";
					MaritalStatus_id = "SID_SINGLE";

				}
				recordResponse.addParam("MaritalStatus_name", MaritalStatus_name);
				recordResponse.addParam("MaritalStatus_id", MaritalStatus_id);
			}
			// Gender

			String Gender = recordResponse.getParamValueByName("Gender");
			if (Gender != null) {
				if (Gender.equalsIgnoreCase("1")) {
					Gender = "Female";
				} else if (Gender.equalsIgnoreCase("2")) {
					Gender = "Male";
				} else {
					Gender = "Others";
				}
				recordResponse.addParam("Gender", Gender);
			}

			// Salutation
			String salutation = recordResponse.getParamValueByName("Salutation");
			if (salutation != null) {
				if (salutation.equalsIgnoreCase("1")) {
					salutation = "Mr.";
				} else if (salutation.equalsIgnoreCase("2")) {
					salutation = "Mrs.";
				} else {
					salutation = "Miss.";
				}
				recordResponse.addParam("Salutation", salutation);
			}
			// Names
			String FirstName = recordResponse.getParamValueByName("FirstName");
			String LastName = recordResponse.getParamValueByName("LastName");
			String MiddleName = recordResponse.getParamValueByName("MiddleName");
			String name;
			if (MiddleName != null)
				name = FirstName + " " + MiddleName + " " + LastName;
			else
				name = FirstName + " " + LastName;
			recordResponse.addParam("Name", name);

			// Fetch CustomerId and UserName
			String partyId = recordResponse.getParamValueByName("partyId");
			String id = getCustomerId(partyId, request);
			if (id != null) {
				String username = getUsernameFromId(id, request);
				if (username != null)
					recordResponse.addParam("Username", username);
			}
			recordResponse.addParam("Customer_id", id);

			// Fetch OrganizationId and Organization name for business users
			if (customerType.equalsIgnoreCase("2")) {
				String orgId = getOrganizationId(id, request);
				String orgName = StringUtils.EMPTY;
				if (orgId != null) {
					orgName = getOrganizationName(orgId, request);
				}
				recordResponse.addParam("organisation_id", orgId);
				if(StringUtils.isNotBlank(orgName))
					recordResponse.addParam("organisation_name", orgName);
			}
			return result;
		} catch (Exception e) {
			result.addParam(new Param("FailureReason", e.getMessage()));
			ErrorCodeEnum.ERR_20001.setErrorCode(result);
			return result;
		}
	}

	private String getOrganizationName(String orgId, DataControllerRequest requestInstance) {
		Map<String, String> postParamsMap = new HashMap<>();
		postParamsMap.put(ODataQueryConstants.FILTER, "id eq '" + orgId + "'");
		postParamsMap.put(ODataQueryConstants.SELECT, "Name");

		String serviceResponse = Executor.invokeService(ServiceURLEnum.ORGANISATION_READ, postParamsMap, null,
				requestInstance);
		if (serviceResponse != null) {
			JSONObject serviceResponseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);

			JSONArray serviceResponseArray = serviceResponseJSON.optJSONArray("organisation");
			if (serviceResponseArray.length() != 0) {
				String OrgName = serviceResponseArray.optJSONObject(0).optString("Name");
				return OrgName;
			}
		}
		return null;
	}

	private String getOrganizationId(String id, DataControllerRequest requestInstance) {
		Map<String, String> postParamsMap = new HashMap<>();
		postParamsMap.put(ODataQueryConstants.FILTER, "id eq '" + id + "'");
		postParamsMap.put(ODataQueryConstants.SELECT, "Organization_Id");

		String serviceResponse = Executor.invokeService(ServiceURLEnum.CUSTOMER_READ, postParamsMap, null,
				requestInstance);
		if (serviceResponse != null) {
			JSONObject serviceResponseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);

			JSONArray serviceResponseArray = serviceResponseJSON.optJSONArray("customer");
			if (serviceResponseArray.length() != 0) {
				String OrgId = serviceResponseArray.optJSONObject(0).optString("Organization_Id");
				return OrgId;
			}
		}
		return null;
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

	private String getCustomerId(String BackendId, DataControllerRequest requestInstance) {

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

}
