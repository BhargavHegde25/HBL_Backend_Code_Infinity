package com.kony.adminconsole.service.business;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.DBPServices;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class EditSignatory  implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
        Result result = new Result();
        String UserName = requestInstance.getParameter("UserName");
        try {
        	
            if (requestInstance.getParameter("id") == null) {
                ErrorCodeEnum.ERR_20688.setErrorCode(result);
                return result;
            } else if (requestInstance.getParameter("Email") == null) {
                ErrorCodeEnum.ERR_20871.setErrorCode(result);
                return result;
            } else if (requestInstance.getParameter("Phone") == null) {
                ErrorCodeEnum.ERR_21010.setErrorCode(result);
                return result;
            }else if (requestInstance.getParameter("UserName") == null) {
                ErrorCodeEnum.ERR_20705.setErrorCode(result);
                return result;
            } else if (requestInstance.getParameter("accounts") == null) {
                ErrorCodeEnum.ERR_21008.setErrorCode(result);
                return result;
            } else if (requestInstance.getParameter("Role_id") == null) {
                ErrorCodeEnum.ERR_20521.setErrorCode(result);
                return result;
            }else if (requestInstance.getParameter("isAuthSignatory") == null) {
                ErrorCodeEnum.ERR_20550.setErrorCode(result);
                return result;
            }else if (requestInstance.getParameter("authSignatoryType") == null) {
                ErrorCodeEnum.ERR_20551.setErrorCode(result);
                return result;
            } else {
            	                
                
                String id = requestInstance.getParameter("id");
                String Email = requestInstance.getParameter("Email");
                String Phone = requestInstance.getParameter("Phone");
                String accounts = requestInstance.getParameter("accounts");
                String Role_id = requestInstance.getParameter("Role_id");
                String isAuthSignatory = requestInstance.getParameter("isAuthSignatory");
                String authSignatoryType = requestInstance.getParameter("authSignatoryType");
                String MiddleName = "";
                if (requestInstance.getParameter("MiddleName") != null) {
                    MiddleName = requestInstance.getParameter("MiddleName");
                }
                
                String DrivingLicenseNumber = "";
                if (requestInstance.getParameter("DrivingLicenseNumber") != null) {
                    DrivingLicenseNumber = requestInstance.getParameter("DrivingLicenseNumber");
                }
				String features = "";
				if (requestInstance.getParameter("features") != null) {
                    features = requestInstance.getParameter("features");
				}
				
                JSONObject createCustomerresponse = DBPServices.editSignatory(id, MiddleName, Email,
                        Phone, UserName, accounts, Role_id, DrivingLicenseNumber, features, isAuthSignatory, authSignatoryType,requestInstance);
                
                if (createCustomerresponse == null || !createCustomerresponse.has(FabricConstants.OPSTATUS)
                        || createCustomerresponse.getInt(FabricConstants.OPSTATUS) != 0) {
                    ErrorCodeEnum.ERR_21039.setErrorCode(result);
                    AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.CREATE,
                            ActivityStatusEnum.FAILED, "Authorized Signatory Edit Failed. username: " + UserName);
                    return result;
                }else if (createCustomerresponse.has("dbpErrMsg")) {
                    result.addParam(new Param("errMsg", createCustomerresponse.getString("dbpErrMsg"),
                            FabricConstants.STRING));
                    return result;
                }else {
                    result.addParam(new Param("status", "Success", FabricConstants.STRING));
                    result.addParam(new Param("opstatus", createCustomerresponse.get("opstatus").toString(),
                            FabricConstants.STRING));
                    result.addParam(new Param("success", createCustomerresponse.getString("success"), FabricConstants.STRING));
                    AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.CREATE,
                            ActivityStatusEnum.SUCCESSFUL,
                            "Authorized Signatory edit Successful. username: " + UserName);
                }                
            }
        } catch (Exception e) {
            alert.prepareError("Unexepected Error in create Customer ", e).log();
            result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.CREATE,
                    ActivityStatusEnum.FAILED, "Authorized Signatory edit Failed. username: " + UserName);
            ErrorCodeEnum.ERR_20001.setErrorCode(result);
        }
        return result;
    }

}
