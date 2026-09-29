package com.temenos.dbx.product.achservices.resource.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHTransactionBusinessDelegate;
import com.temenos.dbx.product.achservices.dto.ACHTransactionDTO;
import com.temenos.dbx.product.achservices.dto.ACHTransactionRecordDTO;
import com.temenos.dbx.product.achservices.resource.api.ACHTransactionRecordResource;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.ACHConstants;
import com.temenos.dbx.product.constants.FeatureAction;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;

public class ACHTransactionRecordImpl implements ACHTransactionRecordResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    ACHTransactionBusinessDelegate achDelegate = DBPAPIAbstractFactoryImpl.getInstance()
            .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ACHTransactionBusinessDelegate.class);

    @Override
    public Result fetchACHTransactionRecords(String methodName, Object[] inputArray, DataControllerRequest dataControllerRequest,
                                             DataControllerResponse dataControllerResponse) {

        Result result ;
        try {
            @SuppressWarnings("unchecked")
			HashMap<String, Object> requestMap = (HashMap<String, Object>) inputArray[1];

            Map<String, Object> customer = CustomerSession.getCustomerMap(dataControllerRequest);
            String customerId = CustomerSession.getCustomerId(customer);

            JSONObject requestObj = new JSONObject(requestMap);
            ACHTransactionDTO requestDTO = JSONUtils.parse(requestObj.toString(), ACHTransactionDTO.class);
            String transaction_id = requestDTO.getTransaction_id();
            if(transaction_id == null || "".equals(transaction_id)) {
                return ErrorCodeEnum.ERR_12042.setErrorCode(new Result());
            }
            
            List<String> requiredActionIds = Arrays.asList(FeatureAction.ACH_COLLECTION_VIEW, FeatureAction.ACH_PAYMENT_VIEW);
			String featureActionId = CustomerSession.getPermittedActionIds(dataControllerRequest, requiredActionIds);
			
			if(featureActionId == null) {
	     		return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}

//            try{
//                Map<String, Object> filterMap = new HashMap<>();
//                filterMap.put("_featureactionlist", featureActionId);
//                filterMap.put("_queryType", null);
//                filterMap.put("_filterByParam", null);
//                filterMap.put("_filterByValue", null);
//                filterMap.put("_searchString", null);
//                filterMap.put("_sortByParam", null);
//                filterMap.put("_sortOrder", null);
//                filterMap.put("_pageSize", null);
//                filterMap.put("_pageOffset", null);
//                JSONObject filterObj = new JSONObject(filterMap);
//                FilterDTO params = JSONUtils.parse(filterObj.toString(), FilterDTO.class);
//                List<ACHTransactionDTO> userTransactions = achDelegate.getACHTransactions(params, customerId, "", dataControllerRequest);
//                if(userTransactions == null){
//                    return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
//                }
//                boolean flag = false;
//                for(ACHTransactionDTO achTrxDTO : userTransactions){
//                    if(achTrxDTO.getTransaction_id().equals(transaction_id)){
//                        flag = true;
//                        break;
//                    }
//                }
//                if(!flag){
//                    return ErrorCodeEnum.ERR_12291.setErrorCode(new Result());
//                }
//            } catch(Exception e){
//                alert.prepareError("Exception occurred while fetching valid user trx: " + e).log();
//            }
			
			ACHTransactionDTO achDTO = achDelegate.fetchTransactionById(transaction_id, dataControllerRequest);
			if(achDTO == null) {
				alert.prepareError("Record Doesn't Exist").log();
	            return ErrorCodeEnum.ERR_12606.setErrorCode(new Result());
			}
			
			transaction_id = achDTO.getTransaction_id();
            List<ACHTransactionRecordDTO> responseDTO = achDelegate.fetchTransactionRecords(transaction_id);
            JSONArray records = new JSONArray(responseDTO);
            JSONObject resultObject = new JSONObject();
            resultObject.put(ACHConstants.TRANSACTION_RECORDS,records);
            result = JSONToResult.convert(resultObject.toString());
        }
        catch(Exception exp) {
            alert.prepareError("Exception occurred while fetching ACH Transaction Records",exp).log();
            return ErrorCodeEnum.ERR_12044.setErrorCode(new Result());
        }
        return result;
    }

}