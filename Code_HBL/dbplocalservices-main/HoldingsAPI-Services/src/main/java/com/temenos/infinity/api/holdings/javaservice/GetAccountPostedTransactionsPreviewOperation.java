package com.temenos.infinity.api.holdings.javaservice;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.TokenUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.holdings.constants.ErrorCodeEnum;
import com.temenos.infinity.api.holdings.resource.api.AccountTransactionsResource;
import com.temenos.infinity.api.holdings.util.HoldingsUtils;
import com.temenos.infinity.api.holdings.util.TransactionTypeProperties;

public class GetAccountPostedTransactionsPreviewOperation implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();
		try {
			// Initializing of AccountTransactions through Abstract factory method
			AccountTransactionsResource AccountTransactionsResource = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(AccountTransactionsResource.class);

			String userName = request.getParameter("UserName");
			String did = request.getParameter("deviceID");
			String accountId = request.getParameter("accountID");
			String offset = request.getParameter("offset");
			String limit = request.getParameter("limit");
			Map<String, String> inputMap = new HashMap<>();
			inputMap.put("customerId",HoldingsUtils.getUserAttributeFromIdentity(request, "customer_id"));
			String authToken = TokenUtils.getHoldingsMSAuthToken(inputMap);
			HoldingsUtils.setCompanyIdToRequest(request);
			//Load Transaction Types
			TransactionTypeProperties props = new TransactionTypeProperties(request);
			try {
				result = AccountTransactionsResource.getAccountPostedTransactionsPreview(userName, did, offset, limit, accountId,
						request,authToken);
			} catch (Exception e) {
				return ErrorCodeEnum.ERR_20041.setErrorCode(new Result());
			}
		} catch (Exception e) {
			return ErrorCodeEnum.ERR_20041.setErrorCode(new Result());
		}
		return result;
	}

}
