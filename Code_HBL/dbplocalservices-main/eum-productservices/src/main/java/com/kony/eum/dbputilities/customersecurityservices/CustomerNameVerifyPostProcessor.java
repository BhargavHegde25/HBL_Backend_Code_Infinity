package com.kony.eum.dbputilities.customersecurityservices;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CustomerNameVerifyPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest dcRequest, DataControllerResponse dcResponse)
			throws Exception {

		Result retValue = new Result();

		Record dbxUsrAttr = result.getRecordById(DBPUtilitiesConstants.USR_ATTR);
		Record coreUsrAttr = result.getRecordById(DBPUtilitiesConstants.CORE_ATTR);

		String dbxSuccess = HelperMethods.getFieldValue(dbxUsrAttr, DBPUtilitiesConstants.IS_USERNAME_EXISTS);
		String coreSuccess = HelperMethods.getFieldValue(coreUsrAttr, DBPUtilitiesConstants.IS_USERNAME_EXISTS);

		if (StringUtils.isNotBlank(dbxSuccess)) {
			for (Param param : dbxUsrAttr.getAllParams()) {
				retValue.addParam(param);
			}
			return retValue;
		} else if (StringUtils.isNotBlank(coreSuccess)) {
			for (Param param : coreUsrAttr.getAllParams()) {
				retValue.addParam(param);
			}
			return retValue;
		} else if (StringUtils.isNotBlank(HelperMethods.getFieldValue(result, "errmsg"))) {
			ErrorCodeEnum.ERR_10008.setErrorCode(retValue, "Failed to fetch Username Details");
			return retValue;
		} else if (dbxUsrAttr.getAllParams().size() > 0) {
			for (Param param : dbxUsrAttr.getAllParams()) {
				retValue.addParam(param);
			}
			return retValue;

		} else if (coreUsrAttr.getAllParams().size() > 0) {
			for (Param param : coreUsrAttr.getAllParams()) {
				retValue.addParam(param);
			}
			return retValue;
		}
		return retValue;
	}

}