package com.temenos.infinity.tradefinanceservices.preprocessor;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

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
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.CreateLetterOfCreditsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.constants.Constants;
import com.temenos.infinity.tradefinanceservices.dto.LetterOfCreditStatusDTO;

public class CreateLetterofCreditsPreProcessor implements DataPreProcessor2{
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		Log4j2Configurator.getInstance();
		
		CreateLetterOfCreditsBusinessDelegate letterOfCreditBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(CreateLetterOfCreditsBusinessDelegate.class);
		
		AccountBusinessDelegate accountBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountBusinessDelegate.class);
		String accountId = request.getParameter("chargesAccount") != null ? request.getParameter("chargesAccount") : "";
		if (StringUtils.isBlank(accountId)) {
			ErrorCodeEnum.ERR_12000.setErrorCode(result);
			return false;
		}

		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String createdby = CustomerSession.getCustomerId(customer);
		
		AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);

		// change the error codes
		 if(! authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction (createdby, Constants.CREATE_LOC_FEATURE_ACTION_ID, accountId, CustomerSession.IsCombinedUser(customer))) {
			 ErrorCodeEnum.ERR_12001.setErrorCode(result);
				return false;
			}
		 
		String validate =  request.getParameter("flowType") != null ? request.getParameter("flowType") : "";
		if("draft".equalsIgnoreCase(validate)) {
			request.addRequestParam_("signatoryApprovalRequired", "false");
			return true;
		} else {
			LetterOfCreditStatusDTO letterOfCreditDTO = new LetterOfCreditStatusDTO();
			letterOfCreditDTO.setAccountId(accountId);
			letterOfCreditDTO.setFeatureActionID(Constants.CREATE_LOC_FEATURE_ACTION_ID);
			letterOfCreditDTO.setCustomerId(createdby);
			
			CustomerAccountsDTO account = accountBusinessDelegate.getAccountDetails(createdby, accountId);
			String companyId = account.getOrganizationId();
			
			letterOfCreditDTO.setCompanyId(companyId);
			
			letterOfCreditDTO = letterOfCreditBusinessDelegate.validateForApprovals(letterOfCreditDTO, request);
			if (letterOfCreditDTO == null) {
				alert.prepareError("Error occurred while validating for approvals").log();
				ErrorCodeEnum.ERR_29018.setErrorCode(result);
				return false;
			}else if(letterOfCreditDTO.getDbpErrCode() != null || letterOfCreditDTO.getDbpErrMsg() != null){
				alert.prepareError("Error occurred while validating for approvals").log();
				result.addParam(new Param(DBPConstants.DBP_ERROR_CODE_KEY, letterOfCreditDTO.getDbpErrCode(), MWConstants.INT));
		        result.addParam(new Param(DBPConstants.DBP_ERROR_MESSAGE_KEY, letterOfCreditDTO.getDbpErrMsg(), MWConstants.STRING));
		        result.addParam(new Param(DBPConstants.FABRIC_OPSTATUS_KEY, "0", MWConstants.INT));
		        result.addParam(new Param(DBPConstants.FABRIC_HTTP_STATUS_CODE_KEY, "0", MWConstants.INT));
				return false;
			}
				else {
			
				if (letterOfCreditDTO.getStatus().equalsIgnoreCase("sent")) {
					request.addRequestParam_("signatoryApprovalRequired", "false");
					return true;
				} else if (letterOfCreditDTO.getStatus().equalsIgnoreCase("pending")) {
					request.addRequestParam_("signatoryApprovalRequired", "true");
					request.addRequestParam_("requestId", letterOfCreditDTO.getRequestId());
					request.addRequestParam_("isSelfApprovalFlag", Boolean.toString(letterOfCreditDTO.isSelfApproved()));
					return true;
				}
			}
			alert.prepareError("Error occurred while validating for approvals").log();
			ErrorCodeEnum.ERR_12000.setErrorCode(result);
			return false;
		}
	}

}
