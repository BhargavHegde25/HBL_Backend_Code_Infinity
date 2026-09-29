package com.kony.adminconsole.service.business;

import org.apache.commons.lang3.StringUtils;
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

public class CreateCompanyService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        Result result = new Result();
        try {
            // Validate UserName
            if (requestInstance.getParameter("Type") == null) {
                ErrorCodeEnum.ERR_21004.setErrorCode(result);
                return result;
            } else if (requestInstance.getParameter("Name") == null) {
                ErrorCodeEnum.ERR_21005.setErrorCode(result);
                return result;
            } else if (StringUtils.isBlank(requestInstance.getParameter("Communication"))) {
                ErrorCodeEnum.ERR_21006.setErrorCode(result);
                return result;
            } else if (requestInstance.getParameter("Address") == null) {
                ErrorCodeEnum.ERR_21007.setErrorCode(result);
                return result;
            } else if (requestInstance.getParameter("AccountsList") == null) {
                ErrorCodeEnum.ERR_21008.setErrorCode(result);
                return result;
            } else {
                String Type = requestInstance.getParameter("Type");
                String Name = requestInstance.getParameter("Name");
                String Communication = requestInstance.getParameter("Communication");
                String Address = requestInstance.getParameter("Address");
                String AccountsList = requestInstance.getParameter("AccountsList");
                String actionlimits = requestInstance.getParameter("actionlimits");
                String features = requestInstance.getParameter("features");
                String Description = null;
                if (requestInstance.getParameter("Description") != null) {
                    Description = requestInstance.getParameter("Description");
                }
                String Owner = null;
                if (requestInstance.getParameter("Owner") != null) {
                    Owner = requestInstance.getParameter("Owner");
                }
                String Membership = null;
                if (requestInstance.getParameter("Membership") != null) {
                    Membership = requestInstance.getParameter("Membership");
                }
                String faxId ="";
				if(StringUtils.isNotBlank("FaxId")) {
					faxId = requestInstance.getParameter("FaxId");
				}
                JSONObject createCompanyresponse = DBPServices.createCompany(Type, Name, Description, Communication,
                        Address, Owner, Membership, AccountsList, actionlimits, features , faxId, requestInstance);

                if (createCompanyresponse == null || !createCompanyresponse.has(FabricConstants.OPSTATUS)
                        || createCompanyresponse.getInt(FabricConstants.OPSTATUS) != 0) {
                    ErrorCodeEnum.ERR_21012.setErrorCode(result);
                    result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                    AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.CREATE,
                            ActivityStatusEnum.FAILED, "Company Creation Failed");
                    return result;
                } else if (createCompanyresponse.has("dbpErrMsg")) {
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					result.addParam(new Param("errMsg", createCompanyresponse.getString("dbpErrMsg"),
							FabricConstants.STRING));
					return result;
                }else {
                    result.addParam(new Param("status", "Success", FabricConstants.STRING));
                    result.addParam(new Param("opstatus", createCompanyresponse.get("opstatus").toString(),
                            FabricConstants.STRING));
                    result.addParam(new Param("id", createCompanyresponse.getString("id"), FabricConstants.STRING));
                    AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.CREATE,
                            ActivityStatusEnum.SUCCESSFUL,
                            "Company Succefully Created:" + createCompanyresponse.getString("id"));
                }
            }
        } catch (Exception e) {
            alert.prepareError("Unexepected Error in create Company ", e).log();
            result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            ErrorCodeEnum.ERR_20001.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.CREATE,
                    ActivityStatusEnum.FAILED, "Company Creation Failed");
        }
        return result;
    }

}