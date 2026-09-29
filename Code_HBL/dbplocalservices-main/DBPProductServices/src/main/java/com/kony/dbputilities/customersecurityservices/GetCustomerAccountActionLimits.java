package com.kony.dbputilities.customersecurityservices;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.kony.dbp.dto.AccountLimitDTO;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbp.handler.LimitsHandler;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetCustomerAccountActionLimits implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        try {
            String customerId = dcRequest.getParameter("customerId");
            String action = dcRequest.getParameter("action");
            String accountId = dcRequest.getParameter("accountId");

            if (StringUtils.isBlank(customerId)) {
                ErrorCodeEnum.ERR_13504.setErrorCode(result);
                return result;
            }
            if (StringUtils.isBlank(action)) {
                ErrorCodeEnum.ERR_13505.setErrorCode(result);
                return result;
            }
            if (StringUtils.isBlank(accountId)) {
                ErrorCodeEnum.ERR_13529.setErrorCode(result);
                return result;
            }
            
            boolean isBusinessAccount = false;
            
            Map<String, Object> inputParams = new HashMap<String, Object>();
    		inputParams.put(DBPUtilitiesConstants.FILTER, "Account_id" + DBPUtilitiesConstants.EQUAL + accountId
    				+
    				DBPUtilitiesConstants.AND + "IsOrganizationAccount" + DBPUtilitiesConstants.EQUAL + "1");

    		Result response = ServiceCallHelper.invokeServiceAndGetResult(dcRequest, inputParams,
    				HelperMethods.getHeaders(dcRequest), URLConstants.CUSTOMERACCOUNTS_GET);
    		if (HelperMethods.hasRecords(response)) {
    			List<Record> accounts = response.getAllDatasets().get(0).getAllRecords();
    			if(accounts.size() > 0) {
    				isBusinessAccount = true;
    			}
    		}
            AccountLimitDTO accountLimitDTO = new AccountLimitDTO();
			accountLimitDTO.setAccountId(accountId);
			accountLimitDTO.setBusinessAccount(isBusinessAccount);
			accountLimitDTO.setLimits(new HashMap<String, Double>());
			
			Map<String, AccountLimitDTO> accounts = new HashMap<String, AccountLimitDTO>();
			accounts.put(accountId, accountLimitDTO);

            Map<String, AccountLimitDTO> accountsMap = LimitsHandler.getCustomerActionLimits(accounts, action, customerId,
                    dcRequest, result);
            AccountLimitDTO accountDTO = accountsMap.get(accountId);
            Map<String, Double> limits = accountDTO.getLimits();
            
            Record limitsRecord = new Record();
            limitsRecord.setId("limits");
            for (Map.Entry<String, Double> limit : limits.entrySet()) {
                limitsRecord.addParam(
                        new Param(limit.getKey(), String.valueOf(limit.getValue()), MWConstants.STRING));
            }
            result.addRecord(limitsRecord);
           
        } catch (ApplicationException e) {
            e.getErrorCodeEnum().setErrorCode(result);
            alert.prepareError("Exception occured in GetCustomerLimits JAVA service. ApplicationException: ", e).log();
        } catch (Exception e) {
            ErrorCodeEnum.ERR_13503.setErrorCode(result);
            result.addParam(new Param("FailureReason", e.getMessage(), MWConstants.STRING));
            alert.prepareError("Exception occured in GetCustomerLimits JAVA service. Exception: ", e).log();
        }

        return result;
    }

}
