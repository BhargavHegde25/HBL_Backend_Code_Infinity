package com.temenos.infinity.api.cards.javaservice;

import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.infinity.api.cards.constants.Constants;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class UpdateCard implements JavaService2 {
	private String isTypeBusiness = "0";
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CARD_UPDATE);
            result.addParam(DBPUtilitiesConstants.MSG_STATUS, DBPUtilitiesConstants.SUCCESS);
        } else {
            result.addParam(new Param("errmsg", "Not a valid user", "String"));
            result.addParam(new Param("errorMessage", "Not a valid user", "String"));
            result.addParam(DBPUtilitiesConstants.MSG_STATUS, DBPUtilitiesConstants.ERROR);
            ErrorCodeEnum.ERR_10152.setErrorCode(result);
        }
        return result;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {

        if (checkUserAlreadyExists(inputParams, dcRequest, result)) {

            boolean status = true;
            String action = (String) inputParams.get("Action");
            if(! isUserActionAuthorized(action, dcRequest)) {
            	return false;
            }
            if ("PinChange".equals(action)) {
                inputParams.remove("Action");
                inputParams.put("Id", inputParams.get("cardId"));
                inputParams.put("reason", inputParams.get("Reason"));
                inputParams.put("pinNumber", inputParams.get("newPin"));
                return true;
            }
            if ("updateWithdrawalLimit".equals(action)) {
                inputParams.remove("Action");
                inputParams.put("Id", inputParams.get("cardId"));
                inputParams.put("withdrawlLimit", inputParams.get("withdrawlLimit"));
                return true;
            }
            if ("updatePurchaseLimit".equals(action)) {
                inputParams.remove("Action");
                inputParams.put("Id", inputParams.get("cardId"));
                inputParams.put("purchaseLimit", inputParams.get("purchaseLimit"));
                return true;
            }
            if (status) {
                String cardStatus = getCardStatus(action);
                inputParams.remove("Action");
                inputParams.put("Id", inputParams.get("cardId"));
                inputParams.put("card_Status", cardStatus);
                inputParams.put("reason", inputParams.get("Reason"));
            }
            return status;

        } else {
            return false;
        }
    }

    private boolean isUserActionAuthorized(String action, DataControllerRequest dcRequest) {
    	String featureAction = null;
    	if ("Activate".equals(action)) {
    		AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
            return (authorizationChecksBusinessDelegate.isUserAuthorizedForPayeeOperations(Constants.CARD_MANAGEMENT_UNLOCK_CARD, isTypeBusiness, dcRequest.getHeaderMap(), dcRequest) ||
            		authorizationChecksBusinessDelegate.isUserAuthorizedForPayeeOperations(Constants.CARD_MANAGEMENT_ACTIVATE_CARD, isTypeBusiness, dcRequest.getHeaderMap(), dcRequest));
        } else if ("Deactivate".equals(action)) {
        	featureAction = Constants.CARD_MANAGEMENT_ACTIVATE_CARD;
        } else if ("Cancel".equals(action)) {
        	featureAction = Constants.CARD_MANAGEMENT_CANCEL_CARD;
        } else if ("Report Lost".equals(action)) {
        	featureAction = Constants.CARD_MANAGEMENT_REPORT_CARD_STOLEN;
        } else if ("Replace".equals(action)) {
        	featureAction = Constants.CARD_MANAGEMENT_REPLACE_CARD;
        } else if ("Lock".equals(action)) {
        	featureAction = Constants.CARD_MANAGEMENT_LOCK_CARD;
        } else if ("Cancel Request".equals(action)) {
        	featureAction = Constants.CARD_MANAGEMENT_CANCEL_CARD;
        } else if ("Replace Request".equals(action)) {
        	featureAction = Constants.CARD_MANAGEMENT_REPLACE_CARD;
        } else if ("PinChange".equals(action)) {
        	featureAction = Constants.CARD_MANAGEMENT_CHANGE_PIN;
        } else if ("updateWithdrawalLimit".equals(action)) {
        	featureAction = Constants.CARD_MANAGEMENT_UPDATE_WITHDRAWAL;
        } else if ("updatePurchaseLimit".equals(action)) {
        	featureAction = Constants.CARD_MANAGEMENT_UPDATE_PURCHASE;
        }
    	
    	if(featureAction != null) {
    		AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
            return authorizationChecksBusinessDelegate.isUserAuthorizedForPayeeOperations(featureAction, isTypeBusiness, dcRequest.getHeaderMap(), dcRequest);
    	}
    	
		return true;
	}

	private boolean checkUserAlreadyExists(Map<String, String> inputParams, DataControllerRequest dcRequest,
            Result result) throws HttpCallException {

        boolean status = true;

        String id = HelperMethods.getCustomerIdFromSession(dcRequest);
        String filter = "User_id" + DBPUtilitiesConstants.EQUAL + id + DBPUtilitiesConstants.AND + "Id"
                + DBPUtilitiesConstants.EQUAL + inputParams.get("cardId");

        Result chkResult = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.CARD_GET);

        if (HelperMethods.hasRecords(chkResult)) {
        	Record card = chkResult.getAllDatasets().get(0).getAllRecords().get(0);
        	isTypeBusiness = HelperMethods.getFieldValue(card, "isTypeBusiness");
        	if(! "1".equals(isTypeBusiness)) {
        		isTypeBusiness = "0";
        	}
            status = true;
        } else {

            status = false;
        }

        return status;
    }

    private String getCardStatus(String action) {
        String cardStatus = null;
        if ("Activate".equals(action)) {
            cardStatus = "Active";
        } else if ("Deactivate".equals(action)) {
            cardStatus = "Inactive";
        } else if ("Cancel".equals(action)) {
            cardStatus = "Cancelled";
        } else if ("Report Lost".equals(action)) {
            cardStatus = "Reported Lost";
        } else if ("Replace".equals(action)) {
            cardStatus = "Replaced";
        } else if ("Lock".equals(action)) {
            cardStatus = "Locked";
        } else if ("Cancel Request".equals(action)) {
            cardStatus = "Cancel Request Sent";
        } else if ("Replace Request".equals(action)) {
            cardStatus = "Replace Request Sent";
        }
        return cardStatus;
    }
}