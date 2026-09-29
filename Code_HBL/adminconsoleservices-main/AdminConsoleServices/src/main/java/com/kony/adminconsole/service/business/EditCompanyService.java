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

public class EditCompanyService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        Result result = new Result();
        try {
            if (requestInstance.getParameter("id") == null) {
                ErrorCodeEnum.ERR_21011.setErrorCode(result);
                return result;
            } else if (requestInstance.getParameter("Type") == null) {
                ErrorCodeEnum.ERR_21004.setErrorCode(result);
                return result;
            } else if (requestInstance.getParameter("Name") == null) {
                ErrorCodeEnum.ERR_21005.setErrorCode(result);
                return result;
            } else if (requestInstance.getParameter("Communication") == null) {
                ErrorCodeEnum.ERR_21006.setErrorCode(result);
                return result;
            } else if (requestInstance.getParameter("Address") == null) {
                ErrorCodeEnum.ERR_21007.setErrorCode(result);
                return result;
            } else if (requestInstance.getParameter("AccountsList") == null) {
                ErrorCodeEnum.ERR_21008.setErrorCode(result);
                return result;
            } else {
                String Id = requestInstance.getParameter("id");
                String Type = requestInstance.getParameter("Type");
                String Name = requestInstance.getParameter("Name");
                String Communication = requestInstance.getParameter("Communication");
                String Address = requestInstance.getParameter("Address");
                String AccountsList = requestInstance.getParameter("AccountsList");
				String Description = null, addedFeatures = null, updatedActionlimits = null;
				String removedFeatures = null, suspendedFeatures = null;
                if (requestInstance.getParameter("Description") != null) {
                    Description = requestInstance.getParameter("Description");
                }
				if (requestInstance.getParameter("addedFeatures") != null) {
					addedFeatures = requestInstance.getParameter("addedFeatures");
				}
				if (requestInstance.getParameter("removedFeatures") != null) {
					removedFeatures = requestInstance.getParameter("removedFeatures");
				}

				if (requestInstance.getParameter("suspendedFeatures") != null) {
					suspendedFeatures = requestInstance.getParameter("suspendedFeatures");
				}
				if (requestInstance.getParameter("updatedActionlimits") != null) {
					updatedActionlimits = requestInstance.getParameter("updatedActionlimits");
				}
				String faxId ="";
				if(StringUtils.isNotBlank("FaxId")) {
					faxId = requestInstance.getParameter("FaxId");
				}
                String Owner = null;
//                if (requestInstance.getParameter("Owner") != null) {
//                    Owner = requestInstance.getParameter("Owner");
//				}
				JSONObject editCompanyresponse = DBPServices.editCompany(Id, Type, Name, Description, Communication,
						Address, Owner, AccountsList, addedFeatures, removedFeatures, suspendedFeatures,
						updatedActionlimits,faxId, requestInstance);
				
                if (editCompanyresponse == null || !editCompanyresponse.has(FabricConstants.OPSTATUS)
                        || editCompanyresponse.getInt(FabricConstants.OPSTATUS) != 0) {
                    ErrorCodeEnum.ERR_21013.setErrorCode(result);
                    result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                    AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.UPDATE,
                            ActivityStatusEnum.FAILED, "Company Edit Failed");
                    return result;
                } else if (editCompanyresponse.has("dbpErrMsg")) {
                    result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                    result.addParam(
                            new Param("errMsg", editCompanyresponse.getString("dbpErrMsg"), FabricConstants.STRING));
                    AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.UPDATE,
                            ActivityStatusEnum.FAILED, "Company Edit Failed");
                    return result;
                } else {
                    result.addParam(new Param("status", "Success", FabricConstants.STRING));
                    result.addParam(new Param("opstatus", editCompanyresponse.get("opstatus").toString(),
                            FabricConstants.STRING));
                    result.addParam(new Param("message", editCompanyresponse.get("success").toString(),
                            FabricConstants.STRING));
                    AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.UPDATE,
                            ActivityStatusEnum.SUCCESSFUL, "Company Edited Successfully");
                }                
            }
        } catch (Exception e) {
            alert.prepareError("Unexepected Error in Edit CompanyService ", e).log();
            result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            ErrorCodeEnum.ERR_20001.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.UPDATE,
                    ActivityStatusEnum.FAILED, "Company Edit Failed");
        }
        return result;
    }
}