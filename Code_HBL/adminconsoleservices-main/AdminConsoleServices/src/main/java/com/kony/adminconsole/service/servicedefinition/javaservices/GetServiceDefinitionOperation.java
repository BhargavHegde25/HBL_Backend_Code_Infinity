package com.kony.adminconsole.service.servicedefinition.javaservices;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.service.servicedefinition.resource.api.ServiceDefinitionResource;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.PermissionName;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class GetServiceDefinitionOperation implements JavaService2{
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();
		try {
			String isGetAllServDefs = request.getParameter("isGetAllServiceDefinitions");
			if((StringUtils.isBlank(isGetAllServDefs) || !isGetAllServDefs.equals("true"))) {
			
			if(StringUtils.isBlank(request.getParameter("legalEntityId"))) {
				ErrorCodeEnum.ERR_22232.setErrorCode(result);
				alert.prepareError("LegalEntityId cannot be null").log();
				return result;
			}
			String[] reqPermissions = {PermissionName.VIEW_SERVICEDEFINITION,PermissionName.CREATE_CONTRACT,
					PermissionName.UPDATE_CONTRACT, PermissionName.API_ACCESS};
			
			//added to handle multiple legal entities
				String logginUserLegalEntityIds = request.getParameter("legalEntityId");
				String[] legalEntityIdsList = logginUserLegalEntityIds.trim().split(",");
				if (legalEntityIdsList.length > 1) {
					for (String eachLegalEntity: legalEntityIdsList) {
						request.addRequestParam_("addedlegalEntityIdValue", eachLegalEntity.trim());
						if(!LoggedInUserHandler.hasAccessToLegalEntity(request,reqPermissions))
						{
							result.addParam(new Param("Status", "Get service definitions list failed", FabricConstants.STRING));
							ErrorCodeEnum.ERR_22231.setErrorCode(result);
							alert.prepareError("Logged in user do not have access to this legalEntity ").log();
							return result;
						}
				      }
				}
				else {
					if(!LoggedInUserHandler.hasAccessToLegalEntity(request,reqPermissions))
					{
						result.addParam(new Param("Status", "Get service definitions list failed", FabricConstants.STRING));
						ErrorCodeEnum.ERR_22231.setErrorCode(result);
						alert.prepareError("Logged in user do not have access to this legalEntity ").log();
						return result;
						
					}
					
				}
			}
			ServiceDefinitionResource serviceDefinitionResource = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(ServiceDefinitionResource.class);
			result = serviceDefinitionResource.fetchAllServiceDefinition(methodId, inputArray, request, response);
		}
		catch(Exception e) {
			alert.prepareError("Caught exception at invoke of GetServiceDefinitionOperation: ", e).log();
			return ErrorCodeEnum.ERR_21910.setErrorCode(new Result());
		}
		return result;
	}

}
