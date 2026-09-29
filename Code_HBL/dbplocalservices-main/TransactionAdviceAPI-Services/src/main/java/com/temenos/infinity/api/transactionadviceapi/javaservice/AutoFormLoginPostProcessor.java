package com.temenos.infinity.api.transactionadviceapi.javaservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.http.cookie.Cookie;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class AutoFormLoginPostProcessor implements DataPostProcessor2 {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Log4j2Configurator.getInstance();
		Result finalResult = new Result();
		List<Cookie> cookies = response.getCookies();
		Map<String, String> cookieList = new HashMap<String, String>();
		if(cookies.isEmpty())
		{
			alert.prepareError("No Cookies Found in Response").log();
			
		}
		for (Cookie cookie : cookies) {
			cookieList.put(cookie.getName(), cookie.getValue());
			finalResult.addParam(new Param(cookie.getName(), cookie.getValue(), "string"));
		}

		return finalResult;

	}

}
