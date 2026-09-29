package com.hbl.javaservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.hbl.postprocessors.AutoEnrollRetailCustomerPostprocessor;
import com.hbl.resource.impl.ContractBackendDelegateImplExtn;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.UserAgentUtil;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.ContractBusinessDelegate;

public class CreateNewChannelAccess implements JavaService2 {
	LoggerUtil logger = new LoggerUtil(CreateNewChannelAccess.class);
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Map<String, Object> inputParams = HelperMethods.getInputParamObjectMap(inputArray);
		String contractId=inputParams.get("contractId")!=null?inputParams.get("contractId").toString():"";
		String channelAccessRequestedFor=inputParams.get("requestedFor")!=null?inputParams.get("requestedFor").toString():"";
		String legalEntityId=inputParams.get("legalEntityId")!=null?inputParams.get("legalEntityId").toString():"";
		String currentChannel=inputParams.get("currentChannel")!=null?inputParams.get("currentChannel").toString():"";
		String isConsentProvided=inputParams.get("isConsentProvided")!=null?inputParams.get("isConsentProvided").toString():"";
		logger.debug("CreateNewChannelAccess:isConsentProvided:" + isConsentProvided);
		Result result= new Result();
		//if(StringUtils.isBlank(currentChannel)) {
		try {
		UserAgentUtil ua = new UserAgentUtil(request);
		currentChannel=ua.getChannel();
		}catch (Exception e) {
			return ErrorCodeEnum.ERR_10163.setErrorCode(new Result());
		}
		if(currentChannel.equalsIgnoreCase("desktop")) {
			currentChannel="ONLINE_BANKING";
		}else if(currentChannel.equalsIgnoreCase("mobile")){
			currentChannel="MOBILE_BANKING";
		}
		//}
		ContractBackendDelegateImplExtn contractBackendExtn= new ContractBackendDelegateImplExtn();
		legalEntityId=StringUtils.isBlank(legalEntityId)?EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE):null;
		try {
		boolean isUpdated =contractBackendExtn.updateContractChannelRequest(contractId,DBPUtilitiesConstants.CONTRACT_STATUS_PENDING, legalEntityId, channelAccessRequestedFor, currentChannel, isConsentProvided, request.getHeaderMap());
		if(isUpdated) {
		result.addStringParam("status", "REQUEST_SUBMITTED");
		result.addParam(new Param("httpStatusCode", "200"));
		result.addParam(new Param("opstatus", "0"));
		}else {
		result.addStringParam("status", "Failed");
		result.addParam(new Param("httpStatusCode", "200"));
		result.addParam(new Param("opstatus", "0"));
		}
		}catch (ApplicationException e) {
			result.addParam(new Param("dbpErrCode", e.getErrorCodeEnum().getErrorCodeAsString()));
			result.addParam(new Param("dbpErrMsg", e.getErrorCodeEnum().getMessage()));
			result.addParam(new Param("httpStatusCode", "500"));
			result.addParam(new Param("opstatus", "0"));
		}
		return result;
	}

}
