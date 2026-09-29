package com.kony.adminconsole.service.business;

import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.MapUtil;
import com.kony.adminconsole.commons.utils.MemoryManager;
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

public class CreateSignatory implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
        Result result = new Result();
        String UserName = requestInstance.getParameter("UserName");
        try {
        	
            if (requestInstance.getParameter("Organization_id") == null) {
                ErrorCodeEnum.ERR_21011.setErrorCode(result);
                return result;
            } else if (requestInstance.getParameter("Email") == null) {
                ErrorCodeEnum.ERR_20871.setErrorCode(result);
                return result;
            } else if (requestInstance.getParameter("Ssn") == null) {
                ErrorCodeEnum.ERR_21009.setErrorCode(result);
                return result;
            } else if (requestInstance.getParameter("Phone") == null) {
                ErrorCodeEnum.ERR_21010.setErrorCode(result);
                return result;
            } else if (requestInstance.getParameter("FirstName") == null) {
                ErrorCodeEnum.ERR_20869.setErrorCode(result);
                return result;
            } else if (requestInstance.getParameter("LastName") == null) {
                ErrorCodeEnum.ERR_20870.setErrorCode(result);
                return result;
            } else if (requestInstance.getParameter("DateOfBirth") == null) {
                ErrorCodeEnum.ERR_20878.setErrorCode(result);
                return result;
            } else if (requestInstance.getParameter("UserName") == null) {
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
            } else if (requestInstance.getParameter("serviceKey") == null) {
            	ErrorCodeEnum.ERR_20720.setErrorCode(result);
                return result;
            } else {
            	String infoData = (String)MemoryManager.getFromCache(requestInstance.getParameter("serviceKey"));
            	Map<String,String> infoMap = MapUtil.stringToMap(infoData);
            	String FirstName = requestInstance.getParameter("FirstName");
            	if(!FirstName.equals(infoMap.get("firstName"))) {
            		ErrorCodeEnum.ERR_20380.setErrorCode(result);
                    return result;
            	}
            	
                String LastName = requestInstance.getParameter("LastName");
                if(!LastName.equals(infoMap.get("lastName"))) {
            		ErrorCodeEnum.ERR_20380.setErrorCode(result);
                    return result;
            	}
                String DateOfBirth = requestInstance.getParameter("DateOfBirth");
                if(!DateOfBirth.equals(infoMap.get("dateOfBirth"))) {
            		ErrorCodeEnum.ERR_20380.setErrorCode(result);
                    return result;
            	}
                
                String Ssn = requestInstance.getParameter("Ssn");
                if(!Ssn.equals(infoMap.get("ssn"))) {
            		ErrorCodeEnum.ERR_20380.setErrorCode(result);
                    return result;
            	}
                String backendId = infoMap.get("id");
                
                String Organization_id = requestInstance.getParameter("Organization_id");
                String Email = requestInstance.getParameter("Email");
                String Phone = requestInstance.getParameter("Phone");
                String accounts = requestInstance.getParameter("accounts");
                String Role_id = requestInstance.getParameter("Role_id");
                String isAuthSignatory = requestInstance.getParameter("isAuthSignatory");
                String authSignatoryType = requestInstance.getParameter("authSignatoryType");
                String MiddleName = null;
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
				
                JSONObject createCustomerresponse = DBPServices.createSignatory(Organization_id, Email, Ssn,
                        Phone, FirstName, LastName, DateOfBirth, UserName, accounts, Role_id, MiddleName,
                        DrivingLicenseNumber, features, isAuthSignatory, authSignatoryType, backendId, requestInstance);
                
                if (createCustomerresponse == null || !createCustomerresponse.has(FabricConstants.OPSTATUS)
                        || createCustomerresponse.getInt(FabricConstants.OPSTATUS) != 0) {
                    ErrorCodeEnum.ERR_21038.setErrorCode(result);
                    AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.CREATE,
                            ActivityStatusEnum.FAILED, "Authorized Signatory Creation Failed. username: " + UserName);
                    return result;
                }else if (createCustomerresponse.has("dbpErrMsg")) {
                    result.addParam(new Param("errMsg", createCustomerresponse.getString("dbpErrMsg"),
                            FabricConstants.STRING));
                    return result;
                }else {
                    result.addParam(new Param("status", "Success", FabricConstants.STRING));
                    result.addParam(new Param("opstatus", createCustomerresponse.get("opstatus").toString(),
                            FabricConstants.STRING));
                    result.addParam(new Param("id", createCustomerresponse.getString("id"), FabricConstants.STRING));
                    AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.CREATE,
                            ActivityStatusEnum.SUCCESSFUL,
                            "Authorized Signatory Creation Successful. username: " + UserName);
                    MemoryManager.removeFromCache(infoData);
                }                
            }
        } catch (Exception e) {
            alert.prepareError("Unexepected Error in create Customer ", e).log();
            result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.CREATE,
                    ActivityStatusEnum.FAILED, "Authorized Signatory Creation Failed. username: " + UserName);
            ErrorCodeEnum.ERR_20001.setErrorCode(result);
        }
        return result;
    }

}
