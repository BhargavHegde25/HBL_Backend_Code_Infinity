package com.kony.adminconsole.service.contract.resource.impl;

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
import com.kony.adminconsole.service.contract.businessdelegate.api.ApprovalMatrixManageBusinessDelegate;
import com.kony.adminconsole.service.contract.resource.api.ApprovalMatrixManageResource;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.DBPServices;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

/**
 * @author kaushik.mondal
 *
 */

public class ApprovalMatrixManageResourceImpl implements ApprovalMatrixManageResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	private ApprovalMatrixManageBusinessDelegate approvalMatrixManageBusinessDelegate =
			DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalMatrixManageBusinessDelegate.class);

	private static final String INPUT_CONTRACT_ID = "contractId";
	private static final String INPUT_CIF = "cif";
	private static final String INPUT_ACCOUNTID = "accountId";
	private static final String INPUT_DISABLE = "disable";

	@Override
	public Result updateApprovalMatrixStatus(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException {

		Result result = new Result();
		String contractId = StringUtils.EMPTY;
		String cif = StringUtils.EMPTY;
		String disable = StringUtils.EMPTY;

		try {
			if ( StringUtils.isBlank(request.getParameter(INPUT_CONTRACT_ID)) ) {
				ErrorCodeEnum.ERR_21960.setErrorCode(result);
				return result;
			} else if (StringUtils.isBlank(request.getParameter(INPUT_CIF)) ) {
				ErrorCodeEnum.ERR_21798.setErrorCode(result);
				return result;
			} else if (StringUtils.isBlank(request.getParameter(INPUT_DISABLE)) ) {
				ErrorCodeEnum.ERR_22072.setErrorCode(result);
				return result;
			} else {
				String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(request);

				contractId = request.getParameter(INPUT_CONTRACT_ID);
				cif = request.getParameter(INPUT_CIF);
				disable = request.getParameter(INPUT_DISABLE);
				Map<String, Object> postParametersMap = new HashMap<>();
				postParametersMap.put(INPUT_CONTRACT_ID, contractId);
				postParametersMap.put(INPUT_CIF, cif);
				postParametersMap.put(INPUT_DISABLE, disable);
				JSONObject serviceResponse =
						approvalMatrixManageBusinessDelegate.updateApprovalMatrixStatus(postParametersMap,
								dbpServicesClaimsToken);
				if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
						|| serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
					ErrorCodeEnum.ERR_22073.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					AuditHandler.auditAdminActivity(request, ModuleNameEnum.CUSTOMERS, EventEnum.CREATE,
							ActivityStatusEnum.FAILED, "Update approval matrix status failed for cif : "+ cif
							+" and contractId: "+contractId);
					return result;
				} else if (serviceResponse.has("dbpErrMsg")) {
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					result.addParam(new Param("errMsg", serviceResponse.getString("dbpErrMsg"),
							FabricConstants.STRING));
					return result;
				} else {

					result = CommonUtilities.constructResultFromJSONObject(serviceResponse);

					AuditHandler.auditAdminActivity(request, ModuleNameEnum.CUSTOMERS, EventEnum.UPDATE,
							ActivityStatusEnum.SUCCESSFUL,
							"Successfully updated approval matrix status for cif : "+ cif + 
							" and contractId: "+contractId);

				}
			}
		} catch (Exception e) {
			alert.prepareError("Unexepected Error in update ApprovalMatrix status: ", e).log();
			result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
			ErrorCodeEnum.ERR_22073.setErrorCode(result);
			AuditHandler.auditAdminActivity(request, ModuleNameEnum.CUSTOMERS, EventEnum.UPDATE,
					ActivityStatusEnum.FAILED, "Update approval matrix status failed for cif : "+ cif
					+" and contractId: "+contractId);
		}
		return result;
	}

	@Override
	public Result isApprovalMatrixDisabled(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException {

		Result result = new Result();
		String contractId = StringUtils.EMPTY;
		String cif = StringUtils.EMPTY;

		try {
			if ( StringUtils.isBlank(request.getParameter(INPUT_CONTRACT_ID)) ) {
				ErrorCodeEnum.ERR_21960.setErrorCode(result);
				return result;
			} else if (StringUtils.isBlank(request.getParameter(INPUT_CIF)) ) {
				ErrorCodeEnum.ERR_21798.setErrorCode(result);
				return result;
			}else {
				String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(request);

				contractId = request.getParameter(INPUT_CONTRACT_ID);
				cif = request.getParameter(INPUT_CIF);
				Map<String, Object> postParametersMap = new HashMap<>();
				postParametersMap.put(INPUT_CONTRACT_ID, contractId);
				postParametersMap.put(INPUT_CIF, cif);
				JSONObject serviceResponse =
						approvalMatrixManageBusinessDelegate.isApprovalMatrixDisabled(postParametersMap,
								dbpServicesClaimsToken);
				if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
						|| serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
					ErrorCodeEnum.ERR_22074.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					AuditHandler.auditAdminActivity(request, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
							ActivityStatusEnum.FAILED, "failed to fetch approval matrix status  for cif : "+ cif
							+" and contractId: "+contractId);
					return result;
				} else if (serviceResponse.has("dbpErrMsg")) {
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					result.addParam(new Param("errMsg", serviceResponse.getString("dbpErrMsg"),
							FabricConstants.STRING));
					return result;
				} else {

					result = CommonUtilities.constructResultFromJSONObject(serviceResponse);

					AuditHandler.auditAdminActivity(request, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
							ActivityStatusEnum.SUCCESSFUL,
							"Successfully fetched approval matrix status for cif : "+ cif + 
							" and contractId: "+contractId);

				}
			}
		} catch (Exception e) {
			alert.prepareError("Unexepected Error in isApprovalMatrixDisabled : ", e).log();
			result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
			ErrorCodeEnum.ERR_22074.setErrorCode(result);
			AuditHandler.auditAdminActivity(request, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
					ActivityStatusEnum.FAILED, "failed to fetch approval matrix status  for cif : "+ cif
					+" and contractId: "+contractId);
		}
		return result;
	}

	@Override
	public Result getApprovalMatrix(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException {

		Result result = new Result();
		String contractId = StringUtils.EMPTY;
		String cif = StringUtils.EMPTY;
		String accountId = StringUtils.EMPTY;

		try {

			if ( StringUtils.isNotBlank(request.getParameter(INPUT_CONTRACT_ID)) ) {
				
				contractId = request.getParameter(INPUT_CONTRACT_ID);
			}
			
			if (StringUtils.isBlank(request.getParameter(INPUT_CIF)) ) {
				
				cif = request.getParameter(INPUT_CIF);
			}
			
			if (StringUtils.isBlank(request.getParameter(INPUT_ACCOUNTID)) ) {
				
				accountId = request.getParameter(INPUT_ACCOUNTID);
			}
			
			if(StringUtils.isAllBlank( new String[]{contractId, cif, accountId} )) {
				
				ErrorCodeEnum.ERR_22076.setErrorCode(result);
				return result;
			}
			
			String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(request);

			Map<String, Object> postParametersMap = new HashMap<>();
			postParametersMap.put(INPUT_CONTRACT_ID, contractId);
			postParametersMap.put(INPUT_CIF, cif);
			postParametersMap.put(INPUT_ACCOUNTID, accountId);
			
			JSONObject serviceResponse =
					approvalMatrixManageBusinessDelegate.getApprovalMatrix(postParametersMap,
							dbpServicesClaimsToken);
			
			if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
					|| serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
				ErrorCodeEnum.ERR_22075.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				AuditHandler.auditAdminActivity(request, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
						ActivityStatusEnum.FAILED, "failed to fetch approval matrix   for cif : "+ cif
						+" and contractId: "+contractId + " and accountId: "+accountId);
				return result;
			} else if (serviceResponse.has("dbpErrMsg")) {
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				result.addParam(new Param("errMsg", serviceResponse.getString("dbpErrMsg"),
						FabricConstants.STRING));
				return result;
			} else {

				result = CommonUtilities.constructResultFromJSONObject(serviceResponse);

				AuditHandler.auditAdminActivity(request, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
						ActivityStatusEnum.SUCCESSFUL,
						"Successfully fetched approval matrix for cif : "+ cif + 
						" and contractId: "+contractId + " and accountId: "+accountId);

			}
		} catch (Exception e) {
			alert.prepareError("Unexepected Error in getApprovalMatrix : ", e).log();
			result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
			ErrorCodeEnum.ERR_22075.setErrorCode(result);
			AuditHandler.auditAdminActivity(request, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
					ActivityStatusEnum.FAILED, "failed to fetch approval matrix   for cif : "+ cif
					+" and contractId: "+contractId + " and accountId: "+accountId);
		}
		return result;

	}

}
