package com.dbp.transactionslimitengine.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.exceptions.MiddlewareException;

public class HelperMethods {
	private static final Logger logger = LogManager.getLogger(HelperMethods.class);

	private HelperMethods() {
	}

	public static String getConfigProperty(String key) {
		if (key == null || key.equals(""))
			return null;
		String value = null;
		try {
			value = ServicesManagerHelper.getServicesManager().getConfigurableParametersHelper().getServerProperty(key);
		} catch (MiddlewareException e) {
			logger.debug("Error occured in fetching environment config variable " + key + ":", e);
		}
		return value;
	}

	public static Result returnResult(Result result, boolean success, String errmsg, String errcode) {
		if (success) {
			result.addParam(new Param(TransactionsLimitConstants.SUCCESS, TransactionsLimitConstants.TRUE,
					TransactionsLimitConstants.STRING));
			return result;
		}
		result.addParam(new Param(TransactionsLimitConstants.DBPERRCODE, errcode, TransactionsLimitConstants.STRING));
		result.addParam(new Param(TransactionsLimitConstants.DBPERRMSG, errmsg, TransactionsLimitConstants.STRING));
		result.addParam(new Param(TransactionsLimitConstants.SUCCESS, TransactionsLimitConstants.FALSE,
				TransactionsLimitConstants.STRING));
		return result;
	}

}
