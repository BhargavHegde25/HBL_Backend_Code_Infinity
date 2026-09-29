package com.hbl.transactionlimitsengine.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.transactionslimitengine.businessdelegate.api.TransactionsLimitBusinessDelegate;
import com.dbp.transactionslimitengine.resource.impl.TransactionsLimitResourceImpl;
import com.dbp.transactionslimitengine.utils.HelperMethods;
import com.dbp.transactionslimitengine.utils.TransactionsLimitConstants;
import com.dbp.transactionslimitengine.utils.TransactionsLimitErrorCodesEnum;
import com.google.gson.JsonObject;
import com.hbl.transactionlimitsengine.constants.HBLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class TransactionsLimitResourceImplExtn extends TransactionsLimitResourceImpl{
	private static final Logger LOG = LogManager.getLogger(TransactionsLimitResourceImplExtn.class);

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
		String channel = request.getParameter("channel_name");
		LOG.debug("HBL:: TransactionsLimitResourceImplExtn ::limitGroup "+limitGroup+":channel:"+channel);
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
		TransactionsLimitBusinessDelegateImplExtn translimitBusinessDelegateExtn = new TransactionsLimitBusinessDelegateImplExtn();
		JsonObject responce=null;
		if(channel.equals(HBLConstants.MOBILE_BANKING)) {
			responce = translimitBusinessDelegateExtn.getTransactionMBLimits(featureactionid, companyid, date, roleid,
					customerid, accountid, limitGroup);
		LOG.debug("HBL:: TransactionsLimitResourceImplExtn :featureactionid:"+featureactionid+":responce: "+responce.toString());
		if (responce.has(HBLConstants.MB_DAILY))
			res.addParam(new Param(HBLConstants.MB_DAILY,
					responce.get(HBLConstants.MB_DAILY).getAsString(), "String"));
		if (responce.has(HBLConstants.MB_WEEKLY))
			res.addParam(new Param(HBLConstants.MB_WEEKLY,
					responce.get(HBLConstants.MB_WEEKLY).getAsString(), "String"));
		if (responce.has(HBLConstants.MB_MONTHLY))
			res.addParam(new Param(HBLConstants.MB_MONTHLY,
					responce.get(HBLConstants.MB_MONTHLY).getAsString(), "String"));
		if (responce.has(TransactionsLimitConstants.DBPERRMSG))
			return HelperMethods.returnResult(res, false,
					responce.get(TransactionsLimitConstants.DBPERRMSG).getAsString(),
					responce.get(TransactionsLimitConstants.DBPERRCODE).getAsString());

		if (!responce.has(HBLConstants.MB_DAILY) || !responce.has(HBLConstants.MB_WEEKLY)||!responce.has(HBLConstants.MB_MONTHLY)) {
			return HelperMethods.returnResult(res, false, "Unable to fetch limits, Refer logs for more details",
					TransactionsLimitErrorCodesEnum.ERROR_EXCEPTION.getErrCode());
		}
	}else {
		 responce = translimitBusinessDelegate.getTransactionLimits(featureactionid, companyid, date, roleid,
					customerid, accountid, limitGroup);
		LOG.debug("HBL:: TransactionsLimitResourceImplExtn :featureactionid:"+featureactionid+":responce: "+responce.toString());
		if (responce.has(TransactionsLimitConstants.DAILY))
			res.addParam(new Param(TransactionsLimitConstants.DAILY,
					responce.get(TransactionsLimitConstants.DAILY).getAsString(), "String"));
		if (responce.has(TransactionsLimitConstants.WEEKLY))
			res.addParam(new Param(TransactionsLimitConstants.WEEKLY,
					responce.get(TransactionsLimitConstants.WEEKLY).getAsString(), "String"));
		if (responce.has(HBLConstants.MONTHLY))
			res.addParam(new Param(HBLConstants.MONTHLY,
					responce.get(HBLConstants.MONTHLY).getAsString(), "String"));
		if (responce.has(TransactionsLimitConstants.DBPERRMSG))
			return HelperMethods.returnResult(res, false,
					responce.get(TransactionsLimitConstants.DBPERRMSG).getAsString(),
					responce.get(TransactionsLimitConstants.DBPERRCODE).getAsString());

		if (!responce.has(TransactionsLimitConstants.DAILY) || !responce.has(TransactionsLimitConstants.WEEKLY)||!responce.has(HBLConstants.MONTHLY)) {
			return HelperMethods.returnResult(res, false, "Unable to fetch limits, Refer logs for more details",
					TransactionsLimitErrorCodesEnum.ERROR_EXCEPTION.getErrCode());
		}
		
	}
		return HelperMethods.returnResult(res, true, "", "");
	}

	private static boolean isValidString(String text) {
		if (text == null)
			return true;
		return text.matches("[a-zA-Z0-9_:.-]*");
	}

}
