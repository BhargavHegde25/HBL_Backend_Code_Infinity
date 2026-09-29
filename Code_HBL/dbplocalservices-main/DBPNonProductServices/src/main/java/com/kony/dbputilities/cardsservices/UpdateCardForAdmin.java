package com.kony.dbputilities.cardsservices;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.constants.DBPConstants;
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

public class UpdateCardForAdmin implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CARD_UPDATE);
        }
        if (!HelperMethods.hasError(result)
                && HelperMethods.getFieldValue(result, DBPConstants.DBP_ERROR_CODE_KEY) != null) {
            result = new Result();
            result.addParam(new Param("status", "Status updated succesfully", "String"));
        } else {
            ErrorCodeEnum.ERR_10150.setErrorCode(result);
        }
        return result;
    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {

        if (StringUtils.isBlank(inputParams.get("username"))
                || StringUtils.isBlank(inputParams.get("maskedCardNumber"))) {
            ErrorCodeEnum.ERR_10150.setErrorCode(result);
            return false;
        }
        String adminId = HelperMethods.getAPIUserIdFromSession(dcRequest);
        String action = inputParams.get("action");
        boolean status = !"PinChange".equals(action);
        if (!HelperMethods.isAdmin(dcRequest, adminId)) {
            // HelperMethods.setValidationMsg("logged in user is not admin", dcRequest, result);
            ErrorCodeEnum.ERR_10151.setErrorCode(result, "logged in user is not admin");
            return false;
        }
        if (status) {
            String cardStatus = getCardStatus(action);
            inputParams.put("Id", getCardId(dcRequest, inputParams));
            inputParams.put("card_Status", cardStatus);
            inputParams.put("reason", inputParams.get("Reason"));
        }
        return status;
    }

    private String getCardId(DataControllerRequest dcRequest, Map<String, String> inputParams)
            throws HttpCallException {
        String userId = getUserId(dcRequest, inputParams.get("username"));
        String cardNumber = inputParams.get("maskedCardNumber");
        String last4 = cardNumber.substring(cardNumber.length() - 4, cardNumber.length());
        List<Record> cards = getCards(dcRequest, userId);
        if (null != cards) {
            for (Record card : cards) {
                if (HelperMethods.getFieldValue(card, "cardNumber").endsWith(last4)) {
                    return HelperMethods.getFieldValue(card, "Id");
                }
            }
        }
        return null;
    }

    private List<Record> getCards(DataControllerRequest dcRequest, String userId) throws HttpCallException {
        String filter = "User_id" + DBPUtilitiesConstants.EQUAL + userId;
        Result cards = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.CARDS_GET);
        if (HelperMethods.hasRecords(cards)) {
            return cards.getAllDatasets().get(0).getAllRecords();
        }
        return null;
    }

    private String getUserId(DataControllerRequest dcRequest, String userName) throws HttpCallException {
        String filter = "UserName" + DBPUtilitiesConstants.EQUAL + userName;
        Result user = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.USER_GET);
        return HelperMethods.getFieldValue(user, "id");
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
