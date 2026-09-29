package com.kony.dbputilities.customersecurityservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.kony.dbp.dto.AccountLimitDTO;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbp.handler.LimitsHandler;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.MWConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetCustomerLimits implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        try {
            String customerId = SessionScope.getCustomerIdFromIdentityScope(dcRequest);
            String action = dcRequest.getParameter("action");

            if (StringUtils.isBlank(customerId)) {
                ErrorCodeEnum.ERR_13504.setErrorCode(result);
                return result;
            }
            if (StringUtils.isBlank(action)) {
                ErrorCodeEnum.ERR_13505.setErrorCode(result);
                return result;
            }

            Map<String, AccountLimitDTO> accountsMap = LimitsHandler.getCustomerActionLimits(action, customerId,
                    dcRequest, result);

            // Prepare and return result
            Dataset limitsDataset = new Dataset("accounts");
            for (Map.Entry<String, AccountLimitDTO> e : accountsMap.entrySet()) {
                AccountLimitDTO accountLimitDTO = e.getValue();
                Map<String, Double> limits = accountLimitDTO.getLimits();

                Record record = new Record();
                record.addParam(new Param("accountId", accountLimitDTO.getAccountId()));

                Record limitsRecord = new Record();
                limitsRecord.setId("limits");
                for (Map.Entry<String, Double> limit : limits.entrySet()) {
                    limitsRecord.addParam(
                            new Param(limit.getKey(), String.valueOf(limit.getValue()), MWConstants.STRING));
                }
                record.addRecord(limitsRecord);
                limitsDataset.addRecord(record);
            }

            result.addDataset(limitsDataset);
        } catch (ApplicationException e) {
            e.getErrorCodeEnum().setErrorCode(result);
            alert.prepareError("Exception occured in GetCustomerLimits JAVA service. ApplicationException: ", e).log();
        } catch (Exception e) {
            ErrorCodeEnum.ERR_13503.setErrorCode(result);
            result.addParam(new Param("FailureReason", e.getMessage(), MWConstants.STRING));
            alert.prepareError("Exception occured in GetCustomerLimits JAVA service. Exception: ", e).log();
        }
        if (!HelperMethods.hasRecords(result)) {
            result = new Result();
            ErrorCodeEnum.ERR_13526.setErrorCode(result);
            return result;
        }

        return result;
    }

}
