package com.kony.adminconsole.service.customer.resource.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.service.customer.businessdelegate.api.InfinityCustomerBusinessDelegate;
import com.kony.adminconsole.service.customer.resource.api.InfinityCustomerResource;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.DBPServices;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class InfinityCustomerResourceImpl implements InfinityCustomerResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	
	
	private static final String INPUT_ID = "id";
	private static final String INPUT_CORE_CUSTOMER_ID = "coreCustomerId";
	private static final String INPUT_PARTY_ID = "partyId";
	
	@Override
	public Result getInfinityAccounts(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException {
		
		Result result = new Result();
		String id = StringUtils.EMPTY;
		String coreCustomerId = StringUtils.EMPTY;
		String partyId = StringUtils.EMPTY;
		
		try {
			
			if (StringUtils.isNotBlank(request.getParameter(INPUT_ID))) {
				id = request.getParameter(INPUT_ID);
			}
			
			if (StringUtils.isNotBlank(request.getParameter(INPUT_CORE_CUSTOMER_ID))) {
				coreCustomerId = request.getParameter(INPUT_CORE_CUSTOMER_ID);
			}
			
			if (StringUtils.isNotBlank(request.getParameter(INPUT_PARTY_ID))) {
				partyId = request.getParameter(INPUT_PARTY_ID);
			}
			
			if(StringUtils.isAllBlank(id, coreCustomerId, partyId )) {
				ErrorCodeEnum.ERR_22131.setErrorCode(result);
				return result;
			}
			
			Map<String, Object> postParametersMap = new HashMap<>();
			postParametersMap.put("id", id);
			postParametersMap.put("coreCustomerId", coreCustomerId);
			postParametersMap.put("partyId", partyId);
			
			String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(request);
			InfinityCustomerBusinessDelegate infinityCustomerBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(InfinityCustomerBusinessDelegate.class);
			JSONObject servicrResponse = infinityCustomerBusinessDelegate
					.getInfinityAccounts(postParametersMap, dbpServicesClaimsToken);
			if (servicrResponse == null || !servicrResponse.has(FabricConstants.OPSTATUS)
					|| servicrResponse.getInt(FabricConstants.OPSTATUS) != 0) {
				ErrorCodeEnum.ERR_22130.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				AuditHandler.auditAdminActivity(request, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
						ActivityStatusEnum.FAILED, "Get infinity Accounts failed");
				return result;
			} else if (servicrResponse.has("dbpErrMsg")) {
				result = CommonUtilities.constructResultFromJSONObject(servicrResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			} else {
				
				result = CommonUtilities.constructResultFromJSONObject(servicrResponse);

				AuditHandler.auditAdminActivity(request, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
						ActivityStatusEnum.SUCCESSFUL, "Successfully fetched infinity Account details. id= " + id
						+" ,coreCustomerId= "+coreCustomerId +" ,partyId="+partyId);

			}
		} catch (Exception e) {
			alert.prepareError("Unexepected Error in getInfinityAccounts", e).log();
			result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
			ErrorCodeEnum.ERR_22130.setErrorCode(result);
		}
		return result;
	}

}
