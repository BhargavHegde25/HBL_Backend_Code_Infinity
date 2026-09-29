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
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHTemplateBusinessDelegate;
import com.temenos.dbx.product.achservices.dto.BBTemplateRecordDTO;
import com.temenos.dbx.product.achservices.dto.BBTemplateSubRecordDTO;
import com.temenos.dbx.product.achservices.resource.api.ACHTemplateSubRecordResource;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.FeatureAction;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;

public class ACHTemplateSubRecordResourceImpl implements ACHTemplateSubRecordResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    ACHTemplateBusinessDelegate achDelegate = DBPAPIAbstractFactoryImpl.getInstance()
            .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ACHTemplateBusinessDelegate.class);

    AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
            .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);

    @Override
    public Result fetchACHTemplateSubRecord(Object[] inputArray, DataControllerRequest dcRequest) {
        Result result = new Result();
        try {
            @SuppressWarnings("unchecked")
			HashMap<String, Object> requestMap = (HashMap<String, Object>) inputArray[1];
            Result operationResult = _validateRequestParamsForFetchRecord(requestMap, dcRequest);

            if(null == operationResult || null != operationResult.getParamByName(ErrorCodeEnum.ERROR_CODE_KEY)
                    || null != operationResult.getParamByName(ErrorCodeEnum.ERROR_MESSAGE_KEY)) {
                return operationResult;
            }

            JSONObject requestObj = new JSONObject(requestMap);
            BBTemplateRecordDTO requestDTO = JSONUtils.parse(requestObj.toString(), BBTemplateRecordDTO.class);

            List<BBTemplateSubRecordDTO> responseDTO = achDelegate.fetchTemplateSubRecord(requestDTO);
            JSONArray records = new JSONArray(responseDTO);
            JSONObject resultObject = new JSONObject();
            resultObject.put("TemplateSubRecord",records);
            result = JSONToResult.convert(resultObject.toString());
        }
        catch(Exception exp) {
            alert.prepareError("Exception occurred while fetching ACH Template Sub Records",exp).log();
            return ErrorCodeEnum.ERR_13502.setErrorCode(new Result());
        }
        return result;
    }


    /**
     * This method checks validity of input payload and whether the user is authorized to carry out the transaction
     *@author KH2544
     *@version 1.0
     *@param requestMap contains the map of request input parameters
     *@param dcRequest request object which contains loggedIn user info
     * **/
    private Result _validateRequestParamsForFetchRecord(HashMap<String, Object> requestMap, DataControllerRequest dcRequest){
        Result result = new Result();

        String templateRecord_id = (String) requestMap.get("TemplateRecord_id");

        /* check if input payload is null */
        if (templateRecord_id == null) {
            return ErrorCodeEnum.ERR_12037.setErrorCode(new Result());
        }

        /* Authorization check to see if the user is authorized to view template*/
        List<String> requiredActionIds = Arrays.asList(FeatureAction.ACH_COLLECTION_VIEW_TEMPLATE, FeatureAction.ACH_PAYMENT_VIEW_TEMPLATE);
		String featureActionId = CustomerSession.getPermittedActionIds(dcRequest, requiredActionIds);
		
		if(featureActionId == null) {
     		return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
		}
        return result;
    }
}
