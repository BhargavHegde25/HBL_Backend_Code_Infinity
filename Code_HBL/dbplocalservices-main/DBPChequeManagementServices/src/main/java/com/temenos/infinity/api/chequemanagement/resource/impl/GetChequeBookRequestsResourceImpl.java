package com.temenos.infinity.api.chequemanagement.resource.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.infinity.api.chequemanagement.businessdelegate.api.GetChequeBookBusinessDelegate;
import com.temenos.infinity.api.chequemanagement.constants.ChequeManagementConstants;
import com.temenos.infinity.api.chequemanagement.constants.Constants;
import com.temenos.infinity.api.chequemanagement.dto.BBRequestDTO;
import com.temenos.infinity.api.chequemanagement.dto.ChequeBook;
import com.temenos.infinity.api.chequemanagement.resource.api.GetCheuqeBookResource;
import com.temenos.infinity.api.chequemanagement.utils.AccountUtilities;
import com.temenos.infinity.api.chequemanagement.utils.ChequeManagementUtilities;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class GetChequeBookRequestsResourceImpl implements GetCheuqeBookResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Result getChequeBookRequests(ChequeBook chequeBook, DataControllerRequest request) {

        List<ChequeBook> chequeBookOrders = null;
        Result result = new Result();
        AccountUtilities ac = new AccountUtilities();
        String customerId = "";
        try {
            customerId = (String) request.getServicesManager().getIdentityHandler().getUserAttributes()
                    .get(ChequeManagementConstants.PARAM_CUSTOMER_ID);
        } catch (Exception e) {
            alert.prepareError("Unable to fetch the customer id from session" + e).log();
        }
        
        AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
		
        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		
        if (StringUtils.isBlank(customerId))
            return ErrorCodeEnum.ERR_26014.setErrorCode(new Result());

        if (!ac.validateInternalAccount(customerId, chequeBook.getAccountID())) {
            return ErrorCodeEnum.ERR_26008.setErrorCode(new Result());
        }
        
        String accountID = chequeBook.getAccountID();
        if (accountID.contains("-")) {
            accountID = ChequeManagementUtilities.RemoveCompanyId(accountID);
            chequeBook.setAccountID(accountID);
        }
        
        if(! authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction (customerId, Constants.FEATURE_ACTION_ID, accountID, CustomerSession.IsCombinedUser(customer))) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}
        
        try {

            GetChequeBookBusinessDelegate orderBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(GetChequeBookBusinessDelegate.class);
            chequeBookOrders = orderBusinessDelegate.getChequeBook(chequeBook, request);
            chequeBookOrders = _addRequestId(chequeBookOrders,accountID);
            JSONObject responseObj = new JSONObject();
            responseObj.put("ChequeBookRequests", chequeBookOrders);
            result = JSONToResult.convert(responseObj.toString());

        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            diagnostic.prepareDebug("Failed to fetch create cheque book request in OMS " + e).log();
            return ErrorCodeEnum.ERR_26004.setErrorCode(new Result());
        }
        return result;
    }
    
	private List<ChequeBook> _addRequestId(List<ChequeBook> chequeBookOrders, String accountId) {

		GetChequeBookBusinessDelegate orderBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(GetChequeBookBusinessDelegate.class);

		List<BBRequestDTO> bbRequests = orderBusinessDelegate.getBBRequests(accountId);
		if(bbRequests == null) {
			 alert.prepareError("Error occured while fetching bbrequests").log();
		}
		
		for(ChequeBook cheque : chequeBookOrders) {
			String transactionId = cheque.getChequeIssueId();
			for(BBRequestDTO bbRequest : bbRequests) {
				if(transactionId.equals(bbRequest.getTransactionId())) {
					cheque.setRequestId(bbRequest.getRequestId());
					break;
				}
			}
		}
		
		return chequeBookOrders;
	}

}
