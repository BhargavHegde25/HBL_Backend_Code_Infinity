package com.dbp.transactionslimitengine.resource.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.transactionslimitengine.businessdelegate.api.TransactionsLimitBusinessDelegate;
import com.dbp.transactionslimitengine.resource.api.TransactionsLimitResource;
import com.dbp.transactionslimitengine.utils.HelperMethods;
import com.dbp.transactionslimitengine.utils.TransactionsLimitConstants;
import com.dbp.transactionslimitengine.utils.TransactionsLimitErrorCodesEnum;
import com.google.gson.JsonObject;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class TransactionsLimitResourceImpl implements TransactionsLimitResource {
	private static final Logger LOG = LogManager.getLogger(TransactionsLimitResourceImpl.class);

	@Override
	public Result getTransactionLimits(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		Result res = new Result();
		String featureactionid = request.getParameter("featureactionid");
		String companyid = request.getParameter("companyid");
		String date = request.getParameter("date");
		String roleid = request.getParameter("roleid");
		String customerid = request.getParameter("customerid");
		String accountid = request.getParameter("accountid");
		String limitGroup = request.getParameter("limitgroupId");
		LOG.debug("limitGroup "+limitGroup);
		if (date == null)
			return HelperMethods.returnResult(res, false,
					TransactionsLimitErrorCodesEnum.ERROR_FEATUREORDATEMISSING.getErrMsg(),
					TransactionsLimitErrorCodesEnum.ERROR_FEATUREORDATEMISSING.getErrCode());
		if (date.length() < 10)
			return HelperMethods.returnResult(res, false,
					TransactionsLimitErrorCodesEnum.ERROR_INVALIDDATEFORMAT.getErrMsg(),
					TransactionsLimitErrorCodesEnum.ERROR_INVALIDDATEFORMAT.getErrCode());
		date = date.substring(0, 10);
		if (!isValidString(featureactionid) || !isValidString(companyid) || !isValidString(date)
				|| !isValidString(roleid) || !isValidString(customerid) || !isValidString(accountid)
				|| !isValidString(limitGroup)) {
			return HelperMethods.returnResult(res, false,
					TransactionsLimitErrorCodesEnum.ERROR_INSECUREINPUT.getErrMsg(),
					TransactionsLimitErrorCodesEnum.ERROR_INSECUREINPUT.getErrCode());
		}
		TransactionsLimitBusinessDelegate translimitBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(TransactionsLimitBusinessDelegate.class);
		JsonObject responce = translimitBusinessDelegate.getTransactionLimits(featureactionid, companyid, date, roleid,
				customerid, accountid, limitGroup);

		if (responce.has(TransactionsLimitConstants.DAILY))
			res.addParam(new Param(TransactionsLimitConstants.DAILY,
					responce.get(TransactionsLimitConstants.DAILY).getAsString(), "String"));
		if (responce.has(TransactionsLimitConstants.WEEKLY))
			res.addParam(new Param(TransactionsLimitConstants.WEEKLY,
					responce.get(TransactionsLimitConstants.WEEKLY).getAsString(), "String"));
		if (responce.has(TransactionsLimitConstants.DBPERRMSG))
			return HelperMethods.returnResult(res, false,
					responce.get(TransactionsLimitConstants.DBPERRMSG).getAsString(),
					responce.get(TransactionsLimitConstants.DBPERRCODE).getAsString());

		if (!responce.has(TransactionsLimitConstants.DAILY) || !responce.has(TransactionsLimitConstants.WEEKLY)) {
			return HelperMethods.returnResult(res, false, "Unable to fetch limits, Refer logs for more details",
					TransactionsLimitErrorCodesEnum.ERROR_EXCEPTION.getErrCode());
		}

		return HelperMethods.returnResult(res, true, "", "");
	}

	private static boolean isValidString(String text) {
		if (text == null)
			return true;
		return text.matches("[a-zA-Z0-9_:.-]*");
	}

}
