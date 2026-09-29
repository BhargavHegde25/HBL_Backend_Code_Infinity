package com.temenos.infinity.api.chequemanagement.postprocessor;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.chequemanagement.backenddelegate.api.ApproveCheckBookRequestBackendDelegate;
import com.temenos.infinity.api.chequemanagement.businessdelegate.api.CreateChequeBookBusinessDelegate;
import com.temenos.infinity.api.chequemanagement.constants.Constants;
import com.temenos.infinity.api.chequemanagement.dto.BBRequestDTO;
import com.temenos.infinity.api.chequemanagement.dto.ChequeBookAction;
import com.temenos.infinity.api.chequemanagement.dto.ChequeBookStatusDTO;

public class CreateChequeBookRequestPostProcessor implements DataPostProcessor2{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Log4j2Configurator.getInstance();
		
		CreateChequeBookBusinessDelegate chequeBookBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(CreateChequeBookBusinessDelegate.class);
		
		String signatoryApprovalRequired = request.getParameter("signatoryApprovalRequired")!= null ? request.getParameter("signatoryApprovalRequired") : "";
		String backendEndId = request.getParameter("backendEndId")!= null ? request.getParameter("backendEndId") : "";
		String isSelfApprovalFlag = request.getParameter("isSelfApprovalFlag") != null ? request.getParameter("isSelfApprovalFlag") : "";
		String isSrmsFailed = request.getParameter("isSrmsFailed") != null ? request.getParameter("isSrmsFailed") : "";
		
		if ("true".equalsIgnoreCase(signatoryApprovalRequired)) {
			String requestId = request.getParameter("requestId") != null ? request.getParameter("requestId") : "";
			
			if ("true".equalsIgnoreCase(isSrmsFailed)) {
               if(!chequeBookBusinessDelegate.deleteChequeFromApprovalQueue(requestId)) {
            	   alert.prepareError("Error occured while deleting the request id from approval queue").log();
				   return ErrorCodeEnum.ERR_26023.setErrorCode(new Result());
               }
			} else {
				
				String featureActionId = Constants.FEATURE_ACTION_ID;
				boolean isSeflApproval = Boolean.parseBoolean(isSelfApprovalFlag);
				
				ChequeBookStatusDTO chequeBook = chequeBookBusinessDelegate.updateBackendIdInApprovalQueue(requestId,
						backendEndId, isSeflApproval, featureActionId, request);
				if (chequeBook == null) {
				     alert.prepareError("Error occured while updating the backend id in approval queue").log();
					return ErrorCodeEnum.ERR_26022.setErrorCode(new Result());
				}
				if(chequeBook.getDbpErrCode() != null || chequeBook.getDbpErrMsg() != null){
					result.addParam(new Param("dbpErrCode", chequeBook.getDbpErrCode()));
					result.addParam(new Param("dbpErrMsg", chequeBook.getDbpErrMsg()));
					return result;
				}
				
				if(Constants.SENT.equalsIgnoreCase(chequeBook.getStatus())) {
					ApproveCheckBookRequestBackendDelegate approveBackendDelegate = DBPAPIAbstractFactoryImpl
			                .getBackendDelegate(ApproveCheckBookRequestBackendDelegate.class);
					
					ChequeBookAction chequeBookOrder = new ChequeBookAction();
					chequeBookOrder.setComments("Approved");
					chequeBookOrder.setRequestId(backendEndId);
					chequeBookOrder.setSignatoryAction("true");
					chequeBookOrder = approveBackendDelegate.approveChequeBookRequest(chequeBookOrder, request); 
					if(chequeBookOrder == null) {
						 alert.prepareError("Error occured while updating the backend id in approval queue").log();
						 return ErrorCodeEnum.ERR_26022.setErrorCode(new Result());
					}
					result.addParam("status", "Request placed");
					
				}
			}
		}
		return result;
	}

}
