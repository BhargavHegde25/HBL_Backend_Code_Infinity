package com.kony.auditlogservices.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;

public class Helpermethods {
	private Helpermethods() {
	}

	public static Result returnResult(boolean flag, String errmsg) {

		Result res = new Result();
		if (flag) {
			res.addParam(new Param(ArchivalConstants.SUCCESS, ArchivalConstants.TRUE, ArchivalConstants.STRING));
		} else {
			res.addParam(new Param(ArchivalConstants.SUCCESS, ArchivalConstants.FALSE, ArchivalConstants.STRING));
			res.addParam(new Param(ArchivalConstants.DBPERRMSG, errmsg, ArchivalConstants.STRING));
		}
		return res;
	}

	public static String getConfigProperty(String key) throws Exception {
		return EnvironmentConfigurationsHandler.getServerAppProperty(key);
	}

	public static Date calculateArchivingDate(Integer noofmonths) {

		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.MONTH, -noofmonths);
		return cal.getTime();

	}

}
