package com.temenos.dbx.product.transactionservices.resource.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.RDCBusinessDelegate;
import com.temenos.dbx.product.transactionservices.dto.RDCDTO;
import com.temenos.dbx.product.transactionservices.resource.api.RDCResource;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;

public class RDCResourceImpl implements RDCResource{

	@Override
	public Result createRDC(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) {
		
		Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
		Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
		Result result = null;
		
		try {
			
			RDCBusinessDelegate rdcDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(RDCBusinessDelegate.class);
			
			@SuppressWarnings("unchecked")
			Map<String, Object> requestParams = (HashMap<String, Object>) inputArray[1];
			
			JSONObject requestObj = new JSONObject(requestParams);
			RDCDTO createDTO = JSONUtils.parse(requestObj.toString(), RDCDTO.class);
			
			RDCDTO resultDTO = rdcDelegate.createRDC(createDTO, request);
			
			if (resultDTO == null) {
				alert.prepareError("Error occured while Creating RDC").log();
				return ErrorCodeEnum.ERR_12611.setErrorCode(new Result());
			}

			if (StringUtils.isNotBlank(resultDTO.getDbpErrMsg())) {
				alert.prepareError("Error occurred while Creating RDC").log();
				return ErrorCodeEnum.ERR_00000.setErrorCode(new Result(), resultDTO.getDbpErrMsg());
			}
			
			if (StringUtils.isNotBlank(resultDTO.getErrmsg())) {
				alert.prepareError("Error occurred while Creating RDC").log();
				return ErrorCodeEnum.ERR_00000.setErrorCode(new Result(), resultDTO.getErrmsg());
			}
			
			JSONObject responseDTO = new JSONObject(resultDTO);
			result = JSONToResult.convert(responseDTO.toString());
		}
		catch(Exception exp) {
			alert.prepareError("Exception occurred while defining resource to create RDC", exp).log();
			return ErrorCodeEnum.ERR_12611.setErrorCode(new Result());
		}
		
		return result;

	}

}
