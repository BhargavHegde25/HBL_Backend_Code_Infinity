package com.temenos.dbx.product.achservices.resource.impl;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHTransactionBusinessDelegate;
import com.temenos.dbx.product.achservices.dto.ACHTransactionRecordDTO;
import com.temenos.dbx.product.achservices.dto.ACHTransactionSubRecordDTO;
import com.temenos.dbx.product.achservices.resource.api.ACHTransactionSubRecordResource;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.ACHConstants;
import com.temenos.dbx.product.constants.FeatureAction;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;

public class ACHTransactionSubRecordImpl implements ACHTransactionSubRecordResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    ACHTransactionBusinessDelegate achDelegate = DBPAPIAbstractFactoryImpl.getInstance()
            .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ACHTransactionBusinessDelegate.class);

    @Override
    public Result fetchACHTransactionSubRecords(String methodName, Object[] inputArray, DataControllerRequest dataControllerRequest,
                                                DataControllerResponse dataControllerResponse){
        Result result ;
        try {
            @SuppressWarnings("unchecked")
			HashMap<String, Object> requestMap = (HashMap<String, Object>) inputArray[1];

            JSONObject requestObj = new JSONObject(requestMap);
            ACHTransactionRecordDTO requestDTO = JSONUtils.parse(requestObj.toString(), ACHTransactionRecordDTO.class);
            String transaction_record_id = Long.toString(requestDTO.getTransactionRecord_id());
            if(transaction_record_id == null || "".equals(transaction_record_id)){
                return ErrorCodeEnum.ERR_12043.setErrorCode(new Result());
            }
            
            List<String> requiredActionIds = Arrays.asList(FeatureAction.ACH_COLLECTION_VIEW, FeatureAction.ACH_PAYMENT_VIEW);
			String featureActionId = CustomerSession.getPermittedActionIds(dataControllerRequest, requiredActionIds);
			
			if(featureActionId == null) {
	     		return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}
            
            List<ACHTransactionSubRecordDTO> responseDTO = achDelegate.fetchTransactionSubRecords(transaction_record_id);
            JSONArray records = new JSONArray(responseDTO);
            JSONObject resultObject = new JSONObject();
            resultObject.put(ACHConstants.TRANSACTION_SUBRECORDS,records);
            result = JSONToResult.convert(resultObject.toString());
        }
        catch(Exception exp) {
            alert.prepareError("Exception occurred while fetching ACH Transaction Sub Records",exp).log();
            return ErrorCodeEnum.ERR_12045.setErrorCode(new Result());
        }
        return result;
    }
}