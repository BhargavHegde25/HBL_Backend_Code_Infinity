package com.temenos.infinity.api.arrangements.exception;

import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class DMSException extends Exception {

	private String httpStatusCode = "";
	private String errorMessage = "";
	
	public DMSException(String httpStatusCode, String errorMessage) {
		super("Http Status Code: "+httpStatusCode +"Error Message: "+ errorMessage);
		this.httpStatusCode = httpStatusCode;
		this.errorMessage = errorMessage;
	}

	public Result constructResultObject() {
		Result result=new Result();
		result.addOpstatusParam(-1);
		result.addHttpStatusCodeParam(httpStatusCode);
		result.addErrMsgParam(errorMessage);
		return result;
	}
}
