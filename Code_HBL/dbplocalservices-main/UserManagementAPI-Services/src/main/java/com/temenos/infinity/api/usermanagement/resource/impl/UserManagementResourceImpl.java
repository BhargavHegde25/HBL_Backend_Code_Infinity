package com.temenos.infinity.api.usermanagement.resource.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.usermanagement.businessdelegate.api.UserManagementBusinessDelegate;
import com.temenos.infinity.api.usermanagement.constants.ErrorCodeEnum;
import com.temenos.infinity.api.usermanagement.dto.CustomerDetailsDTO;
import com.temenos.infinity.api.usermanagement.dto.UserAccountSettingsDTO;
import com.temenos.infinity.api.usermanagement.resource.api.UserManagementResource;
import com.temenos.infinity.api.usermanagement.utilities.CustomerSession;
import com.temenos.infinity.api.usermanagement.constants.UserAccountSettingConstants;

public class UserManagementResourceImpl implements UserManagementResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Result updateAccountDetails(UserAccountSettingsDTO userAccount, DataControllerRequest request, HashMap<String, Object> headerMap) {
		Result result = new Result();

		String accountId = userAccount.getAccountID();
		String eStatementEnable = userAccount.geteStatementEnable();
		String email = userAccount.getEmail();
		if (StringUtils.isBlank(accountId)) {
			return ErrorCodeEnum.ERR_200511.setErrorCode(new Result());
		}
		if (StringUtils.isBlank(eStatementEnable)) {
			return ErrorCodeEnum.ERR_200512.setErrorCode(new Result());
		}
		UserManagementBusinessDelegate orderBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(UserManagementBusinessDelegate.class);
		
        if (StringUtils.equals(eStatementEnable, "1")){
            if(StringUtils.isBlank(email))
                return ErrorCodeEnum.ERR_200513.setErrorCode(new Result());
            Map<String,Object> customer = CustomerSession.getCustomerMap(request);
            String customerId = CustomerSession.getCustomerId(customer);
            String username = CustomerSession.getCustomerName(customer);
            String bankId = CustomerSession.getBankId(customer);
            try {
				boolean isValidEmail = orderBusinessDelegate.isValidEmailId(email, username, customerId, bankId,headerMap);
			    if(!isValidEmail)
			    	return ErrorCodeEnum.ERR_200517.setErrorCode(new Result());
            } catch (Exception e) {
				// TODO Auto-generated catch block
            	alert.prepareError("Error occurred: ", e).log();
			}
        }
        
        if (StringUtils.isNotBlank(accountId) && StringUtils.isNotBlank(eStatementEnable))  {
			try {
				userAccount = orderBusinessDelegate.updateUserAccountSettings(userAccount, headerMap);

				if (StringUtils.isBlank(userAccount.getId())) {

					return ErrorCodeEnum.ERR_20055.setErrorCode(new Result());
				}
				if (StringUtils.isNotBlank(userAccount.getErrorMessage())) {
					String dbpErrCode = userAccount.getCode();
					String dbpErrMessage = userAccount.getErrorMessage();
					if (StringUtils.isNotBlank(dbpErrMessage)) {
						String msg = ErrorCodeEnum.ERR_20061.getMessage(userAccount.getId(),
								userAccount.getStatus(), dbpErrMessage);
						return ErrorCodeEnum.ERR_20061.setErrorCode(new Result(), msg);
					}
				}
				JSONObject updateAccountDTO = new JSONObject(userAccount);
				result = JSONToResult.convert(updateAccountDTO.toString());

			} catch (Exception e) {
				alert.prepareError(e.toString()).log();
				diagnostic.prepareDebug("Failed to update Account Settings in OMS " + e).log();
				return ErrorCodeEnum.ERR_20052.setErrorCode(new Result());
			}
		}
		return result;
	}

	@Override
	public Result updateCustomerDetails(CustomerDetailsDTO customerDetailsDTO, DataControllerRequest request, HashMap<String, Object> headerMap) {
		CustomerDetailsDTO customerDetailsOrderDTO = new CustomerDetailsDTO();
		Result result = new Result();
		String operation = customerDetailsDTO.getOperation();
		UserManagementBusinessDelegate orderBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(UserManagementBusinessDelegate.class);

		if (operation.contentEquals("Delete")) {
			String deleteCommunicationID = customerDetailsDTO.getDeleteCommunicationID();
			if (StringUtils.isBlank(deleteCommunicationID)) {
				return ErrorCodeEnum.ERR_20059.setErrorCode(new Result());
			}
		} else if (operation.contentEquals("DeleteAddress")) {
			String deleteAddressID = customerDetailsDTO.getDeleteAddressID();
			if (StringUtils.isBlank(deleteAddressID)) {
				return ErrorCodeEnum.ERR_20060.setErrorCode(new Result());
			}
		} else {

			String detailToBeUpdated = customerDetailsDTO.getDetailToBeUpdated();
			String phoneNumber = customerDetailsDTO.getPhoneNumber();
			String id = customerDetailsDTO.getId();
			String value = customerDetailsDTO.getValue();
			if(!StringUtils.isBlank(value)) {
				value = value.trim();
			}

			if (detailToBeUpdated.contentEquals("phoneNumbers")) {
				if (operation.contentEquals("Create")) {
					if (StringUtils.isBlank(phoneNumber)) {
						return ErrorCodeEnum.ERR_200514.setErrorCode(new Result());
					}
				}
				if (operation.contentEquals("Update")) {
					if (StringUtils.isBlank(phoneNumber)) {
						return ErrorCodeEnum.ERR_200514.setErrorCode(new Result());
					}
					if (StringUtils.isBlank(id)) {
						return ErrorCodeEnum.ERR_200515.setErrorCode(new Result());
					}
				}
			}

			if (detailToBeUpdated.contentEquals("EmailIds")) {
				if (operation.contentEquals("Create")) {
					if (StringUtils.isBlank(value)) {
						return ErrorCodeEnum.ERR_200513.setErrorCode(new Result());
					}
				}
				if (operation.contentEquals("Update")) {
					if (StringUtils.isBlank(value)) {
						return ErrorCodeEnum.ERR_200513.setErrorCode(new Result());
					}
					if (StringUtils.isBlank(id)) {
						return ErrorCodeEnum.ERR_200516.setErrorCode(new Result());
					}
				}
				
				Map<String,Object> customer = CustomerSession.getCustomerMap(request);
	            String customerId = CustomerSession.getCustomerId(customer);
	            String username = CustomerSession.getCustomerName(customer);
	            String bankId = CustomerSession.getBankId(customer);
            	
				try {
					JSONArray userEmailDetails = new JSONArray();
					userEmailDetails = orderBusinessDelegate.getUserEmailDetails(username, customerId, bankId,
							headerMap);
					if (userEmailDetails != null && userEmailDetails.length() != 0) {
						if (userEmailDetails.length()-1 == UserAccountSettingConstants.MAX_ALLOWED_EMAILIDS) {
							String errMsg = ErrorCodeEnum.ERR_200530.getMessage(
									String.valueOf(UserAccountSettingConstants.MAX_ALLOWED_EMAILIDS));
							return ErrorCodeEnum.ERR_200530.setErrorCode(new Result(), errMsg);
						} else {
							if (operation.contentEquals("Create")) {
								for (int index = 0; index < userEmailDetails.length(); index++) {
									JSONObject userEmailRecord = new JSONObject();
									userEmailRecord = userEmailDetails.getJSONObject(index);
									if (userEmailRecord.has("Value") && !value.isEmpty()
											&& userEmailRecord.optString("Value").equals(value)) {
										return ErrorCodeEnum.ERR_200531.setErrorCode(new Result());
									}
								}
							}
						}
					}
				} catch (Exception e) {
					alert.prepareError("Error occurred: ", e).log();
					diagnostic.prepareDebug("Failed to fetch customer details " + e).log();
					return ErrorCodeEnum.ERR_20058.setErrorCode(new Result());
				}
			}

		}
		try {
			customerDetailsOrderDTO = orderBusinessDelegate.updateCustomerDetails(customerDetailsDTO, headerMap);

			if (StringUtils.isBlank(customerDetailsOrderDTO.getOrderId())) {

				return ErrorCodeEnum.ERR_20057.setErrorCode(new Result());
			}
			if (StringUtils.isNotBlank(customerDetailsOrderDTO.getErrorMessage())) {
				String dbpErrCode = customerDetailsOrderDTO.getCode();
				String dbpErrMessage = customerDetailsOrderDTO.getErrorMessage();
				if (StringUtils.isNotBlank(dbpErrMessage)) {
					String msg = ErrorCodeEnum.ERR_20062.getMessage(customerDetailsOrderDTO.getOrderId(),
							customerDetailsOrderDTO.getStatus(), dbpErrMessage);
					return ErrorCodeEnum.ERR_20062.setErrorCode(new Result(), msg);
				}
			}
			JSONObject updatedCustomerDetailsDTO = new JSONObject(customerDetailsOrderDTO);
			result = JSONToResult.convert(updatedCustomerDetailsDTO.toString());

		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
			diagnostic.prepareDebug("Failed to update customer details in OMS" + e).log();
			return ErrorCodeEnum.ERR_20058.setErrorCode(new Result());
		}
		return result;
	}
	public String uploadDocsForChangeRequest(DataControllerRequest request) {
		//Result result =new Result();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		JSONObject requestPayload = getJSONFromRequest(request);
		dataMap.put("Documents", requestPayload.get("Documents"));
		UserManagementBusinessDelegate orderBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(UserManagementBusinessDelegate.class);
		String result=null;
		try {
			result = orderBusinessDelegate.uploadDocsForChangeRequest(dataMap);
		} catch (ApplicationException e) {
			alert.prepareError("Error in UserMAnagementAPIResourceImpl : uploadMultipleDocuments"+ e.getMessage()).log();
		}
		return result;

}
	public static JSONObject getJSONFromRequest(DataControllerRequest request) {
		JSONObject json = new JSONObject();
		request.getParameterNames().forEachRemaining(name -> {
			json.put(name, request.getParameter(name));
		});
		return json;
	}
}
