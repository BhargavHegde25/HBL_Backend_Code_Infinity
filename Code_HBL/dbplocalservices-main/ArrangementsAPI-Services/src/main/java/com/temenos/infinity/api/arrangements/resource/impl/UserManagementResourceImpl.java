package com.temenos.infinity.api.arrangements.resource.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;
import org.json.JSONArray;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.ehcache.ResultCache;
import com.konylabs.middleware.exceptions.MiddlewareException;
import com.temenos.infinity.api.arrangements.businessdelegate.api.UserManagementBusinessDelegate;
import com.temenos.infinity.api.arrangements.constants.ErrorCodeEnum;
import com.temenos.infinity.api.arrangements.dto.CustomerDetailsDTO;
import com.temenos.infinity.api.arrangements.dto.UserAccountSettingsDTO;
import com.temenos.infinity.api.arrangements.memorymgmt.MemoryManager;
import com.temenos.infinity.api.arrangements.memorymgmt.SessionMap;
import com.temenos.infinity.api.arrangements.resource.api.UserManagementResource;
import com.temenos.infinity.api.arrangements.utils.CustomerSession;

public class UserManagementResourceImpl implements UserManagementResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static final String USERDETAILS = "USER_DETAILS";

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
				boolean isValidEmail = isValidEmailId(email, username, customerId, bankId,headerMap);
			    if(!isValidEmail)
			    	return ErrorCodeEnum.ERR_200517.setErrorCode(new Result());
            } catch (Exception e) {
				alert.prepareError(e.toString()).log();
			}
        }
        if (StringUtils.equals(eStatementEnable, "0")){
            if(StringUtils.isNotBlank(email))
            {
            Map<String,Object> customer = CustomerSession.getCustomerMap(request);
            String customerId = CustomerSession.getCustomerId(customer);
            String username = CustomerSession.getCustomerName(customer);
            String bankId = CustomerSession.getBankId(customer);
            try {
				boolean isValidEmail = isValidEmailId(email, username, customerId, bankId,headerMap);
			    if(!isValidEmail)
			    	return ErrorCodeEnum.ERR_200517.setErrorCode(new Result());
            } catch (Exception e) {
				alert.prepareError(e.toString()).log();
			}
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

	private boolean isValidEmailId(String email, String username, String customerId, String bankId,
			HashMap<String, Object> headerMap) {
		ResultCache resultCache;
		try {
			resultCache = ServicesManagerHelper.getServicesManager().getResultCache();
            String value = (String) resultCache.retrieveFromCache(USERDETAILS + customerId);
            JSONObject user = new JSONObject(value);
            JSONArray emailIds = (JSONArray) user.get("EmailIds");
            for(int i=0;i<emailIds.length();i++) {
            	JSONObject emailId = emailIds.getJSONObject(i);
            	String val = emailId.getString("Value");
            	if (email.equals(val)) {
            		return true;
            	}
            }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			alert.prepareError("Error during isValidEmailId:"+e.getMessage()).log();
		}
		return false;
	}

	@Override
	public Result updateCustomerDetails(CustomerDetailsDTO customerDetailsDTO, HashMap<String, Object> headerMap) {
		CustomerDetailsDTO customerDetailsOrderDTO = new CustomerDetailsDTO();
		Result result = new Result();
		String operation = customerDetailsDTO.getOperation();

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
			}

		}
		try {
			UserManagementBusinessDelegate orderBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(UserManagementBusinessDelegate.class);
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

}
