package com.temenos.dbx.product.achservices.resource.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHTemplateBusinessDelegate;
import com.temenos.dbx.product.achservices.dto.BBTemplateDTO;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.dbx.product.achservices.dto.BBTemplateRecordDTO;
import com.temenos.dbx.product.achservices.resource.api.ACHTemplateRecordResource;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.FeatureAction;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;

public class ACHTemplateRecordResourceImpl implements ACHTemplateRecordResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    ACHTemplateBusinessDelegate achDelegate = DBPAPIAbstractFactoryImpl.getInstance()
            .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ACHTemplateBusinessDelegate.class);

    AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
            .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);

    @Override
    public Result fetchACHTemplateRecord(Object[] inputArray, DataControllerRequest dcRequest) {

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
            BBTemplateDTO requestDTO = JSONUtils.parse(requestObj.toString(), BBTemplateDTO.class);


            //BOLA : SecurityFix : Template Check :
            Map<String, Object> customer = CustomerSession.getCustomerMap(dcRequest);
            String userId = CustomerSession.getCustomerId(customer);
            FilterDTO filters = new FilterDTO();
            filters.set_featureactionlist(String.join(",", FeatureAction.ACH_COLLECTION_VIEW, FeatureAction.ACH_PAYMENT_VIEW));
            String templateID = Long.toString(requestDTO.getTemplateId());
            List<BBTemplateDTO> templates = achDelegate.fetchAllACHTemplates(userId, filters, templateID);
            if(templates == null || templates.size() == 0) {
                return ErrorCodeEnum.ERR_10308.setErrorCode(new Result());
            }

            List<BBTemplateRecordDTO> responseDTO = achDelegate.fetchTemplateRecords(requestDTO);
            JSONArray records = new JSONArray(JSONUtils.stringifyCollectionWithTypeInfo(responseDTO,BBTemplateRecordDTO.class));
            JSONObject resultObject = new JSONObject();
            resultObject.put("TemplateRecords",records);
            result = JSONToResult.convert(resultObject.toString());
        }
        catch(Exception exp) {
            alert.prepareError("Exception occurred while fetching ACH Template Records",exp).log();
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

        String templateType_id = (String) requestMap.get("TemplateId");
        if (templateType_id == null) {
            return ErrorCodeEnum.ERR_12037.setErrorCode(new Result());
        }


        /* check if input payload is null */
        try{
            Map<String, Object> customer = CustomerSession.getCustomerMap(dcRequest);
            String userId = CustomerSession.getCustomerId(customer);
            FilterDTO filters = new FilterDTO();
            filters.set_featureactionlist(String.join(",", FeatureAction.ACH_COLLECTION_VIEW, FeatureAction.ACH_PAYMENT_VIEW));

            List<BBTemplateDTO> templates = achDelegate.fetchAllACHTemplates(userId, filters, templateType_id);

            if(templates == null || templates.size() == 0) {
                return ErrorCodeEnum.ERR_13502.setErrorCode(new Result());
            }
        }catch(Exception exp){
            alert.prepareError("Exception while fetching ACH template").log();
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
