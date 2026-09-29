package com.temenos.infinity.api.chequemanagement.preprocessor;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.MWConstants;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.businessdelegate.api.AccountBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.commons.dto.CustomerAccountsDTO;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.infinity.api.chequemanagement.businessdelegate.api.CreateChequeBookBusinessDelegate;
import com.temenos.infinity.api.chequemanagement.constants.Constants;
import com.temenos.infinity.api.chequemanagement.dto.ChequeBookStatusDTO;

public class CreateChequeBookRequestPreProcessor implements DataPreProcessor2{

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		Log4j2Configurator.getInstance();
		CreateChequeBookBusinessDelegate chequeBookBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(CreateChequeBookBusinessDelegate.class);
		AccountBusinessDelegate accountBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountBusinessDelegate.class);
		
		String accountId = request.getParameter("accountID") != null ? request.getParameter("accountID") : "";
		if (StringUtils.isBlank(accountId)) {
			ErrorCodeEnum.ERR_12000.setErrorCode(result);
			return false;
		}

		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String createdby = CustomerSession.getCustomerId(customer);
		
		AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);

		 if(! authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction (createdby, Constants.FEATURE_ACTION_ID, accountId, CustomerSession.IsCombinedUser(customer))) {
			 ErrorCodeEnum.ERR_12001.setErrorCode(result);
				return false;
			}
		 
		String validate =  request.getParameter("validate") != null ? request.getParameter("validate") : "";
		if("true".equalsIgnoreCase(validate)) {
			request.addRequestParam_("signatoryApprovalRequired", "false");
			return true;
		}else {
		ChequeBookStatusDTO chequeBookDTO = new ChequeBookStatusDTO();
		chequeBookDTO.setAccountId(accountId);
		chequeBookDTO.setFeatureActionID(Constants.FEATURE_ACTION_ID);
		chequeBookDTO.setCustomerId(createdby);
		
		CustomerAccountsDTO account = accountBusinessDelegate.getAccountDetails(createdby, accountId);
		String companyId = account.getOrganizationId();
		
		chequeBookDTO.setCompanyId(companyId);
		
		chequeBookDTO = chequeBookBusinessDelegate.validateForApprovals(chequeBookDTO, request);
		if (chequeBookDTO == null) {
			alert.prepareError("Error occured while validating for approvals").log();
			ErrorCodeEnum.ERR_12000.setErrorCode(result);
			return false;
		}else if(chequeBookDTO.getDbpErrCode() != null || chequeBookDTO.getDbpErrMsg() != null){
			alert.prepareError("Error occured while validating for approvals").log();
			result.addParam(new Param(DBPConstants.DBP_ERROR_CODE_KEY, chequeBookDTO.getDbpErrCode(), MWConstants.INT));
	        result.addParam(new Param(DBPConstants.DBP_ERROR_MESSAGE_KEY, chequeBookDTO.getDbpErrMsg(), MWConstants.STRING));
	        result.addParam(new Param(DBPConstants.FABRIC_OPSTATUS_KEY, "0", MWConstants.INT));
	        result.addParam(new Param(DBPConstants.FABRIC_HTTP_STATUS_CODE_KEY, "0", MWConstants.INT));
			return false;
		}
			else {
		
			if (chequeBookDTO.getStatus().equalsIgnoreCase("sent")) {
				request.addRequestParam_("signatoryApprovalRequired", "false");
				return true;
			} else if (chequeBookDTO.getStatus().equalsIgnoreCase("pending")) {
				request.addRequestParam_("signatoryApprovalRequired", "true");
				request.addRequestParam_("requestId", chequeBookDTO.getRequestId());
				request.addRequestParam_("isSelfApprovalFlag", Boolean.toString(chequeBookDTO.isSelfApproved()));
				return true;
			}
		}
		alert.prepareError("Error occured while validating for approvals").log();
		ErrorCodeEnum.ERR_12000.setErrorCode(result);
		return false;
		}
	}
		

}
