package com.kony.dbp.alertrouterservice.utils;

import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class HelperMethods {
	private HelperMethods() {

	}

	public static Result returnResult(boolean flag, ErrorCodeEnum err) {
		Result res = new Result();
		if (flag) {
			res.addParam(new Param(Constants.SUCCESS, Constants.TRUE, Constants.STRING));
		} else {
			res.addParam(new Param(Constants.SUCCESS, Constants.FALSE, Constants.STRING));
			res.addParam(new Param(Constants.DBPERRMSG, err.getErrMsg(), Constants.STRING));
			res.addParam(new Param(Constants.DBPERRCODE, err.getErrCode(), Constants.STRING));
		}
		return res;
	}

	public static Result returnResult(boolean flag, String msg) {
		Result res = new Result();
		if (flag) {
			res.addParam(new Param(Constants.SUCCESS, Constants.TRUE, Constants.STRING));
		} else {
			res.addParam(new Param(Constants.SUCCESS, Constants.FALSE, Constants.STRING));
			res.addParam(new Param(Constants.DBPERRMSG, msg, Constants.STRING));
		}
		return res;
	}
	
	public static Result returnResult(boolean flag, String code,String msg) {
		Result res = new Result();
		if (flag) {
			res.addParam(new Param(Constants.SUCCESS, Constants.TRUE, Constants.STRING));
		} else {
			res.addParam(new Param(Constants.SUCCESS, Constants.FALSE, Constants.STRING));
			res.addParam(new Param(Constants.DBPERRMSG, msg, Constants.STRING));
			res.addParam(new Param(Constants.DBPERRCODE, code, Constants.STRING));
		}
		return res;
	}

}
