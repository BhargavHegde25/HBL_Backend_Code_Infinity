package com.temenos.dbx.eum.product.javaservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.eum.product.usermanagement.resource.api.ProfileManagementResource;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.CustomerFlagStatus;
import com.temenos.dbx.product.utils.DTOUtils;

public class UpdateCustomerDetailsOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();
		ArrayList<Object> updatedInputArrDcReq = new ArrayList<Object>();

		Result result = new Result();
		String phoneNumber = null;
		try {
			ProfileManagementResource profileManagementResource = DBPAPIAbstractFactoryImpl
					.getResource(ProfileManagementResource.class);
			
			if (dcRequest.containsKeyInRequest("preferredContactMethod")
					|| dcRequest.containsKeyInRequest("preferredContactTime") || dcRequest.containsKeyInRequest("Salutation")) {
				CustomerDTO customerDTO = buildCustomerUpdate(dcRequest);
				if (DTOUtils.persistObject(customerDTO, dcRequest.getHeaderMap())) {
					result.addParam("success", "success");
					result.addParam("Status", "Operation successful");
					result.addParam("status", "Operation successful");
	            } else {
	                result.addParam("errmsg","Edit Operation Failed");
	            }
				return result;
			}
			
			if (dcRequest.containsKeyInRequest("source")) {
				if (dcRequest.getParameter("source").toString().contentEquals("UserManagement")) {
				    phoneNumber = dcRequest.getParameter("phoneNumber");
				    if(StringUtils.isBlank(phoneNumber)) {
				        phoneNumber =HelperMethods.getInputParamMap(inputArray).get("phoneNumber");
				    }
					inputArray = constructInputArray(inputArray, dcRequest);
				}
			}

			result = profileManagementResource.updateProfile(methodID, inputArray, dcRequest, dcResponse);

		} catch (Exception e) {
			alert.prepareError("Caught exception while creating Customer: " + e).log();
		}
		if (dcRequest.containsKeyInRequest("source")) {
            if (dcRequest.getParameter("source").toString().contentEquals("UserManagement")) {
                dcRequest.addRequestParam_("phoneNumber", phoneNumber);
                HelperMethods.getInputParamMap(inputArray).put("phoneNumber", phoneNumber);
            }
        }
		return result;
	}

	public static CustomerDTO buildCustomerUpdate(DataControllerRequest dcRequest) {
		String customerID = dcRequest.getParameter("Customer_id");
		CustomerDTO customerDTO = new CustomerDTO();
		if (StringUtils.isNotBlank(customerID)) {
            customerDTO = (CustomerDTO) DTOUtils.buildDTOFromDatabase(customerDTO, customerID, true);

            String maritalStauts = dcRequest.getParameter("MaritalStatus_id");

            if (StringUtils.isNotBlank(maritalStauts) && !maritalStauts.equals(customerDTO.getMaritalStatus_id())) {
                customerDTO.setIsChanged(true);
                customerDTO.setMaritalStatus_id(maritalStauts);
            }
               
            String salutation = dcRequest.getParameter("Salutation");  

            if (StringUtils.isNotBlank(salutation) && !salutation.equals(customerDTO.getSalutation())) {
                customerDTO.setIsChanged(true);
                customerDTO.setSalutation(salutation);
            }
            
            String EmployementStatus_id = dcRequest.getParameter("EmployementStatus_id");  

            if (StringUtils.isNotBlank(EmployementStatus_id) && !EmployementStatus_id.equals(customerDTO.getEmployementStatus_id())) {
                customerDTO.setIsChanged(true);
                customerDTO.setEmployementStatus_id(EmployementStatus_id);
            }

            String eagreementStatus = dcRequest.getParameter("eagreementStatus");
       

            if (StringUtils.isNotBlank(eagreementStatus)) {
                if ("1".equals(eagreementStatus)) {
                    customerDTO.setIsEagreementSigned(true);
                } else {
                    customerDTO.setIsEagreementSigned(false);
                }

                customerDTO.setIsChanged(true);
            }

            String preferredContactMethod = dcRequest.getParameter("preferredContactMethod");
            

            if (StringUtils.isNotBlank(preferredContactMethod)) {
                customerDTO.setPreferredContactMethod(preferredContactMethod);
                customerDTO.setIsChanged(true);
            }

            String preferredContactTime = dcRequest.getParameter("preferredContactTime");
            

            if (StringUtils.isNotBlank(preferredContactTime)) {
                customerDTO.setPreferredContactTime(preferredContactTime);
                customerDTO.setIsChanged(true);
            }

            String listOfRemovedRisks = dcRequest.getParameter("listOfRemovedRisks");
            String listOfAddedRisks = dcRequest.getParameter("listOfAddedRisks");
            
            JsonObject json = new JsonObject();
            if (StringUtils.isNotBlank(listOfAddedRisks)) {
                try {
                    json.add("ListAddedRisk", new JsonParser().parse(listOfAddedRisks).getAsJsonArray());
                } catch (Exception e) {
                }
            }
            if (StringUtils.isNotBlank(listOfRemovedRisks)) {
                try {
                    json.add("ListRemovedRisk", new JsonParser().parse(listOfRemovedRisks).getAsJsonArray());
                } catch (Exception e) {
                }
            }

            List<CustomerFlagStatus> customerFlagStatus = customerDTO.getCustomerFlagStatus();
            Set<String> list = HelperMethods.getRiskAcceptedValues();
            if (json.has("ListRemovedRisk") && json.get("ListRemovedRisk").isJsonArray()) {
                JsonArray jsonArray = json.getAsJsonArray("ListRemovedRisk");
                if (customerFlagStatus != null && customerFlagStatus.size() > 0 && jsonArray != null
                        && !jsonArray.isJsonNull()) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        try {
                            String riskValue = jsonArray.get(i).getAsString();
                            if (StringUtils.isNotBlank(riskValue) && list.contains(riskValue)) {
                                for (CustomerFlagStatus customerFlagStatus2 : customerFlagStatus) {
                                    if (riskValue.equals(customerFlagStatus2.getStatus_id())) {
                                        customerFlagStatus2.setChanged(true);
                                    }
                                }
                            }
                        } catch (Exception ex) {
                            ex.getMessage();
                        }
                    }
                }

            }

            if (json.has("ListAddedRisk") && json.get("ListAddedRisk").isJsonArray()) {
                JsonArray jsonArray = json.getAsJsonArray("ListAddedRisk");
                if (jsonArray != null && !jsonArray.isJsonNull()) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        try {
                            String riskValue = jsonArray.get(i).getAsString();
                            if (StringUtils.isNotBlank(riskValue) && list.contains(riskValue)) {
                                CustomerFlagStatus customerFlagStatus2 = new CustomerFlagStatus();
                                customerFlagStatus2.setCustomer_id(customerID);
                                customerFlagStatus2.setStatus_id(riskValue);
                                customerFlagStatus2.setNew(true);
                                customerDTO.setCustomerFlagStatus(customerFlagStatus2);
                            }
                        } catch (Exception ex) {
                            ex.getMessage();
                        }
                    }
                }

            }
            
		}
		return customerDTO;
	}
	
	public static Object[] constructInputArray(Object[] inputArray, DataControllerRequest dcRequest) {

		String operation = dcRequest.getParameter("operation") != null ? dcRequest.getParameter("operation") : "";
		String detailToBeUpdated = dcRequest.getParameter("detailToBeUpdated") != null
				? dcRequest.getParameter("detailToBeUpdated")
				: "";

		JSONArray customerDetailsArr = new JSONArray();
		JSONObject obj = new JSONObject();
		ArrayList<Object> updatedInputArrDcReq = new ArrayList<Object>();

		if (operation.contentEquals("Create")) {

			obj.put("isPrimary", dcRequest.getParameter("isPrimary"));
			obj.put("isAlertsRequired", dcRequest.getParameter("isAlertsRequired"));
			obj.put("Extension", dcRequest.getParameter("Extension"));

			if (detailToBeUpdated.contentEquals("phoneNumbers")) {
				obj.put("phoneNumber", dcRequest.getParameter("phoneNumber"));
				obj.put("phoneCountryCode", dcRequest.getParameter("phoneCountryCode"));

			} else if (detailToBeUpdated.contentEquals("EmailIds")) {
				obj.put("value", dcRequest.getParameter("value"));
			}
		}

		
		else if (operation.contentEquals("Update")) {
			obj.put("isPrimary", dcRequest.getParameter("isPrimary"));
			obj.put("isAlertsRequired", dcRequest.getParameter("isAlertsRequired"));
			obj.put("id", dcRequest.getParameter("communication_ID"));
			obj.put("isTypeBusiness", dcRequest.getParameter("isTypeBusiness"));

			if (detailToBeUpdated.contentEquals("phoneNumbers")) {
				obj.put("phoneNumber", dcRequest.getParameter("phoneNumber"));
				obj.put("phoneCountryCode", dcRequest.getParameter("phoneCountryCode"));
				obj.put("Extension", dcRequest.getParameter("Extension"));
			} else if (detailToBeUpdated.contentEquals("EmailIds")) {
				obj.put("value", dcRequest.getParameter("value"));
				if (dcRequest.getParameter("Extension") != null) {
					obj.put("Extension", dcRequest.getParameter("Extension"));
				}
			}

		}

		else if (detailToBeUpdated.contentEquals("Addresses")) {
			obj.put("Addr_type", dcRequest.getParameter("Addr_type"));
			obj.put("addrLine1", dcRequest.getParameter("addrLine1"));
			obj.put("addrLine2", dcRequest.getParameter("addrLine2"));
			obj.put("City_id", dcRequest.getParameter("City_id"));
			obj.put("ZipCode", dcRequest.getParameter("ZipCode"));
			obj.put("Region_id", dcRequest.getParameter("Region_id"));
			obj.put("isPrimary", dcRequest.getParameter("isPrimary"));
			obj.put("countryCode", dcRequest.getParameter("countryCode"));

			if (operation.contentEquals("UpdateAddress")) {
				obj.put("Addr_id", dcRequest.getParameter("Addr_id"));
			}

		}

		customerDetailsArr.put(obj);

		alert.prepareError("request from SRMS : "+obj).log();
		
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

		if (detailToBeUpdated.contentEquals("phoneNumbers")) {

			if (inputParams.containsKey("phoneNumbers")) {
				inputParams.put("PhoneNumbers", customerDetailsArr.toString());
				inputParams.put("phoneNumber", null);
			}

			inputArray[1] = inputParams;

		} else if (detailToBeUpdated.contentEquals("EmailIds")) {
			if (inputParams.containsKey("emailIds")) {
				inputParams.put("emailIds", customerDetailsArr.toString());
			}

			inputArray[1] = inputParams;

		} else if (detailToBeUpdated.contentEquals("Addresses")) {
			if (inputParams.containsKey("addresses")) {
				inputParams.put("addresses", customerDetailsArr.toString());
			}

			inputArray[1] = inputParams;
		}

		dcRequest.addRequestParam_("phoneNumber", null);
        HelperMethods.getInputParamMap(inputArray).remove("phoneNumber");
        
		return inputArray;
	}
}
