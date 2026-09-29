package com.dbp.externalevent.utils;

import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class HelperMethods {
	private HelperMethods() {

	}

	public static Result returnResult(boolean flag, ExternalEventsEnum err) {
		Result res = new Result();
		if (flag) {
			res.addParam(new Param(ExternalEventsConstants.SUCCESS, ExternalEventsConstants.TRUE,
					ExternalEventsConstants.STRING));
		} else {
			res.addParam(new Param(ExternalEventsConstants.SUCCESS, ExternalEventsConstants.FALSE,
					ExternalEventsConstants.STRING));
			res.addParam(new Param(ExternalEventsConstants.DBPERRMSG, err.getErrMsg(), ExternalEventsConstants.STRING));
			res.addParam(
					new Param(ExternalEventsConstants.DBPERRCODE, err.getErrCode(), ExternalEventsConstants.STRING));
		}
		return res;
	}

}
