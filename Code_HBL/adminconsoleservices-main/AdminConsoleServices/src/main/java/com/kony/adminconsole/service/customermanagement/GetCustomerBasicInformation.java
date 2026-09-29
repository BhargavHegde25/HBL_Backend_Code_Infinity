package com.kony.adminconsole.service.customermanagement;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.exception.DBPAuthenticationException;
import com.kony.adminconsole.handler.CustomerHandler;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.LegalEntityUtil;
import com.kony.adminconsole.utilities.PermissionName;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

/**
 * This service will fetch basic information of a customer
 * 
 * @author Alahari Prudhvi Akhil (KH2346)
 * 
 */
public class GetCustomerBasicInformation implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
    public static final int DEFAULT_UNLOCK_COUNT = 4;

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
        Result processedResult = new Result();
        String customerId = requestInstance.getParameter("Customer_id");
        String username = requestInstance.getParameter("Customer_username");
        String legalEntityId = requestInstance.getParameter("legalEntityId");
        try {
        	if(StringUtils.isBlank(legalEntityId)) {
        		ErrorCodeEnum.ERR_22230.setErrorCode(processedResult);
        		return processedResult;
        	}
        	String[] reqPermissions = {PermissionName.VIEW_CUSTOMER};
			if(!LoggedInUserHandler.hasAccessToLegalEntity(requestInstance,reqPermissions))
            {
				processedResult.addParam(new Param("Status", "Get customer basic information operation failed", FabricConstants.STRING));
                ErrorCodeEnum.ERR_22231.setErrorCode(processedResult);
                alert.prepareError("Logged in user do not have access to this legalEntity ").log();
                return processedResult;
                
            }
            CustomerHandler.computeCustomerBasicInformation(requestInstance, processedResult, customerId, username, legalEntityId);

        } catch (DBPAuthenticationException dbpException) {
            alert.prepareError("DBP login failed. " + dbpException.getMessage()).log();
            ErrorCodeEnum.ERR_20933.setErrorCode(processedResult);
            processedResult.addParam(new Param("FailureReason", dbpException.getMessage(), FabricConstants.STRING));
        } catch (Exception e) {
            alert.prepareError("Unexpected error has occurred. " + e.getMessage()).log();
            ErrorCodeEnum.ERR_20717.setErrorCode(processedResult);
            processedResult.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
        }
        return processedResult;

    }

}