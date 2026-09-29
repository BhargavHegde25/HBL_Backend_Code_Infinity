package com.kony.adminconsole.service.customerrole.javaservices;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.service.customerrole.resource.api.CustomerRoleResource;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.PermissionName;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

/**
 * @author kruthi.manojna
 *
 */
public class DownloadGroupsListOperation implements JavaService2{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();
		Map<String, String> queryParamsMap = new HashMap<String, String>();
		try {
			queryParamsMap = (Map<String, String>) request.getAttribute("queryparams");
            String legalEntityId = queryParamsMap.get("legalEntityId");
            if(StringUtils.isBlank(legalEntityId)) {
                ErrorCodeEnum.ERR_22232.setErrorCode(result);
                alert.prepareError("LegalEntityId cannot be null").log();
                return result;
            }
            String[] reqPermissions = {PermissionName.VIEW_APP_CONTENT};
            if(!LoggedInUserHandler.hasAccessToLegalEntity(request,reqPermissions))
            {
                result.addParam(new Param("Status", "Download groups list failed", FabricConstants.STRING));
                ErrorCodeEnum.ERR_22231.setErrorCode(result);
                alert.prepareError("Logged in user do not have access to this legalEntity ").log();
                return result;                
            }
			CustomerRoleResource roleResource = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(CustomerRoleResource.class);
			result = roleResource.downloadGroupsList(methodId, inputArray, request, response);
		}
		catch(Exception e) {
			alert.prepareError("Caught exception at invoke of DownloadGroupsListOperation: ", e).log();
			return ErrorCodeEnum.ERR_21983.setErrorCode(new Result());
		}
		return result;
	}
	
}
