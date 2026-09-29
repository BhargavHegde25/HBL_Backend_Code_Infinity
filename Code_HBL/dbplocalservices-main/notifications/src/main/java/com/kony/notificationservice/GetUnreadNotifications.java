package com.kony.notificationservice;

import java.util.Calendar;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetUnreadNotifications implements JavaService2 {

	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map inputParams = HelperMethods.getInputParamMap(inputArray);
		String legalEntityId = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(dcRequest);

		if (StringUtils.isBlank(legalEntityId)) {
			result.addErrMsgParam(ErrorCodeEnum.ERR_29040.getMessage());
			result.addOpstatusParam(-1);
			result.addHttpStatusCodeParam(400);
			return result;
		}

		if (preProcess(inputParams, dcRequest, result, legalEntityId)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.USER_NOTIFICATION_GET);
		}
		if (!HelperMethods.hasError(result)) {
			result = postProcess(result);
		}

		return result;
	}

	private Result postProcess(Result result) {
		Dataset ds = result.getAllDatasets().get(0);
		Result retVal = new Result();
		int count = 0;
		if (null != ds && null != ds.getAllRecords()) {
			count = ds.getAllRecords().size();
		}
		Dataset retds = new Dataset("notifications");
		Record rec = new Record();
		Param p = new Param(DBPUtilitiesConstants.UNREAD_COUNT, String.valueOf(count),
				DBPConstants.FABRIC_STRING_CONSTANT_KEY);
		rec.addParam(p);
		retds.addRecord(rec);
		retVal.addDataset(retds);
		return retVal;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result, String legalEntityId) {
		String userid = HelperMethods.getCustomerIdFromSession(dcRequest);
		Calendar cal = Calendar.getInstance();
		String toDate = HelperMethods.getFormattedTimeStamp(cal.getTime(), null);
		cal.add(Calendar.DATE, -30);
		String fromDate = HelperMethods.getFormattedTimeStamp(cal.getTime(), null);
		StringBuilder filter = new StringBuilder();
		filter.append("user_id").append(DBPUtilitiesConstants.EQUAL).append(userid);
		filter.append(DBPUtilitiesConstants.AND);
		filter.append("isRead").append(DBPUtilitiesConstants.EQUAL).append("0");
		filter.append(DBPUtilitiesConstants.AND);
		filter.append("companyLegalUnit").append(DBPUtilitiesConstants.EQUAL).append(legalEntityId);
		filter.append(DBPUtilitiesConstants.AND).append("(");
		filter.append("receivedDate").append(DBPUtilitiesConstants.GREATER_EQUAL).append(fromDate);
		filter.append(DBPUtilitiesConstants.AND);
		filter.append("receivedDate").append(DBPUtilitiesConstants.LESS_EQUAL).append(toDate).append(")");
		inputParams.put(DBPUtilitiesConstants.FILTER, filter.toString());
		return true;
	}
}
