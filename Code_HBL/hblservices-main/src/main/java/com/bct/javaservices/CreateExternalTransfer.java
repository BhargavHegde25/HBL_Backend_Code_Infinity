package com.bct.javaservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bct.custom.constants.HBLConstants;
import com.bct.custom.constants.HBLEnums;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.infinity.dbx.dbp.jwt.auth.utils.CommonUtils;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.dbx.product.constants.TransactionStatusEnum;
import com.temenos.dbx.product.transactionservices.resource.api.InterBankFundTransferResource;

public class CreateExternalTransfer implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(CreateExternalTransfer.class);

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest dcRequest, DataControllerResponse dcResponse)
			throws Exception {
		String referenceId = null;
		Result transactionResult = new Result();
		String operationName = null;
		String payeeVerificationStatus = "";
		String payeeVerificationErrMsg = "";
		try {
			HashMap<String, Object> params = (HashMap<String, Object>) inputArray[1];
			Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
			LOG.debug("CreateExternalTransfer:inputParams:"+inputParams);
			String serviceDifferentiator = inputParams.get("serviceName");
			String serviceName = HBLConstants.DBP_TRANSACTION_SERVICES;
			HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
			String verifyPayee = (String) params.get("verifyPayee");
			String isValidate = (String) params.get("validate");
			if (verifyPayee.equalsIgnoreCase("true")) {
				if (FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE
						.equalsIgnoreCase(serviceDifferentiator)) {
					operationName = HBLConstants.CREATE_INTER_BANK_FUND_TRANSACTION;
					transactionResult = createInterBankFundTransfer(methodId, inputArray, dcRequest, dcResponse);//CommonUtils.callIntegrationService(dcRequest, params, serviceHeaders, serviceName, operationName, true);
				}
				else if (FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE.equalsIgnoreCase(serviceDifferentiator)) {
					operationName = HBLConstants.CREATE_INTERNATIONAL_FUND_TRANSACTION;
					transactionResult = CommonUtils.callIntegrationService(dcRequest, params, serviceHeaders, serviceName,
							operationName, true);
				} else if (FeatureAction.INTRA_BANK_FUND_TRANSFER_CREATE.equalsIgnoreCase(serviceDifferentiator)) {
					operationName = HBLConstants.CREATE_INTRA_BANK_FUND_TRANSACTION;
					transactionResult = CommonUtils.callIntegrationService(dcRequest, params, serviceHeaders, serviceName,
							operationName, true);
				} else {
					return ErrorCodeEnum.ERR_29021.setErrorCode(new Result());
				}
			}
			
		}
		catch (Exception e) {
			LOG.error("Error occured while invoking CreateOneTimeTransaction: "+ e.getMessage());
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		return transactionResult;
	}
	private Result createInterBankFundTransfer(String methodId, Object[] inputArray, DataControllerRequest dcRequest, DataControllerResponse dcResponse) {
		Result transactionResult= new Result();
		LOG.debug("CreateExternalTransfer:createInterBankFundTransfer:inputArray:"+inputArray);
		try {
			InterBankFundTransferResource interBankTransactionResource = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(InterBankFundTransferResource.class);

			transactionResult = interBankTransactionResource.createTransaction(methodId, inputArray, dcRequest, dcResponse);
			String dbpErrMsg=transactionResult.getParamValueByName("dbpErrMsg");
			String dbpErrCode=transactionResult.getParamValueByName("dbpErrCode");
			String refId=transactionResult.getParamValueByName("referenceId");
			String status=transactionResult.getParamValueByName("status");
			LOG.debug("CreateExternalTransfer:createInterBankFundTransfer:transactionResult:"+ResultToJSON.convert(transactionResult));
			if(StringUtils.isNotBlank(dbpErrCode) || StringUtils.isNotBlank(dbpErrMsg) ) {
				transactionResult.addParam(new Param("status", "failed"));
				if(dbpErrMsg.equalsIgnoreCase(TransactionStatusEnum.REVERSED.getMessage()) || dbpErrMsg.equalsIgnoreCase(TransactionStatusEnum.REVERSAL_FAILED.getMessage())){
					transactionResult.addParam(new Param("status", "failed"));
					transactionResult.addHttpStatusCodeParam(200);
					transactionResult.addParam(new Param("errorMessage", HBLEnums.ERR_20001.getErrorMsg()));
					transactionResult.addParam(new Param("responseMessage", HBLEnums.ERR_20001.getErrorMsg()));
					transactionResult.removeParamByName("dbpErrMsg");
					transactionResult.removeParamByName("dbpErrCode");
					//return HBLEnums.ERR_20001.setErrorCode(transactionResult);
				}
				else if(StringUtils.isNotBlank(status) && status.equalsIgnoreCase(ErrorCodeEnum.ERR_10420.getMessage())) {
					 transactionResult.addParam(new Param("dbpErrCode", dbpErrCode));
					 transactionResult.addParam(new Param("dbpErrMsg", dbpErrMsg));
					 transactionResult.addParam(new Param("status", "failed"));
				}
				else if(dbpErrMsg.equalsIgnoreCase(ErrorCodeEnum.ERR_21210.getMessage())) {
						transactionResult.addParam(new Param("dbpErrCode", dbpErrCode));
						transactionResult.addParam(new Param("dbpErrMsg", dbpErrMsg));
						transactionResult.addParam(new Param("status", "failed"));
				}else if(dbpErrCode.equalsIgnoreCase(ErrorCodeEnum.ERR_10703.getErrorCodeAsString())) {
					transactionResult.addParam(new Param("dbpErrCode", dbpErrCode));
					transactionResult.addParam(new Param("dbpErrMsg", dbpErrMsg));
					transactionResult.addParam(new Param("status", "failed"));
				}
				else {
					transactionResult.addParam(new Param("status", "failed"));
					return HBLEnums.ERR_20000.setErrorCode(transactionResult);
				}
			}
			else if(StringUtils.isNotBlank(refId)){
				transactionResult.addParam(new Param("opstatus", "0"));
				transactionResult.addParam(new Param("status", "success"));
			}
			else if(StringUtils.isBlank(refId)) {
				transactionResult.addParam(new Param("opstatus", "0"));
				transactionResult.addParam(new Param("status", "failed"));
				HBLEnums.ERR_20000.setErrorCode(transactionResult);
				}

		} catch (Exception e) {
			LOG.error("Error occured while invoking CreateInterBankTransaction: "+ e.getMessage());
			transactionResult.addParam(new Param("status", "failed"));
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		return transactionResult;
	}
	

}
