package com.kony.adminconsole.service.customerrole.javaservices;

import java.util.ArrayList;
import java.util.List;
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
public class GetGroupsOperation implements JavaService2{

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();
		try {
			if(StringUtils.isBlank(request.getParameter("legalEntityId"))) {
				ErrorCodeEnum.ERR_22232.setErrorCode(result);
				alert.prepareError("LegalEntityId cannot be null").log();
				return result;
			}
			String[] reqPermissions = {PermissionName.CREATE_GROUP,PermissionName.VIEW_GROUP,
					PermissionName.UPDATE_GROUP, PermissionName.API_ACCESS};
			if(!LoggedInUserHandler.hasAccessToLegalEntity(request,reqPermissions))
			{
				result.addParam(new Param("Status", "Get groups list failed", FabricConstants.STRING));
				ErrorCodeEnum.ERR_22231.setErrorCode(result);
				alert.prepareError("Logged in user do not have access to this legalEntity ").log();
				return result;
				
			}
			CustomerRoleResource customerroleresource = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(CustomerRoleResource.class);
			result = customerroleresource.fetchAllGroups(methodId, inputArray, request, response);
		}
		catch(Exception e) {
			alert.prepareError("Caught exception at invoke of GetGroupActionLimits : ", e).log();
			return ErrorCodeEnum.ERR_21953.setErrorCode(new Result());
		}
		return result;
	}

}
