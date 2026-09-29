package com.temenos.infinity.smartbanking.advisory.errorhandling;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.google.gson.JsonObject;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;


public class SBAException extends Exception {

	private static final long serialVersionUID=1L;
	private static final String DEFAULT_HTTP_ERROR_CODE = "400";
	private static final String DEFAULT_OP_STATUS = "8009";
	private String errCode;
	private String errMsg;
	private String httpErrCode;
	private String opStatus;
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	public SBAException(ErrorCodeEnum errorCodeEnum) {
		this();
		if(errorCodeEnum==null){
			alert.prepareError("Invalid input errorCodeEnum: null").log();
		}else{
			initializeCorporateException(errorCodeEnum);
		}
	}	

	private void initializeCorporateException(ErrorCodeEnum errorCodeEnum) {
		this.errCode=errorCodeEnum.getErrorCodeAsString();
		this.errMsg=errorCodeEnum.getErrorMessage();
	}
	
	public SBAException(String errMsg) {
		this.errMsg=errMsg;
	}
	
	public SBAException(String errCode,String errMsg) {
		this();
		this.errCode=errCode;
		this.errMsg=errMsg;
	}
	
	public SBAException(String errCode,String errMsg, String httpErrCode, String opStatus) {
		this.errCode=errCode;
		this.errMsg=errMsg;
		this.httpErrCode=httpErrCode;
		this.opStatus=opStatus;
	}
	
	public SBAException() {
		this.httpErrCode = DEFAULT_HTTP_ERROR_CODE;
		this.opStatus = DEFAULT_OP_STATUS;
	}

	public String getErrorCode() {
		return this.errCode;
	}

	public String getErrorMessage() {
		return this.errMsg;
	}
	
	public void appendToErrorMessage(String stringToBeAppended) {
		this.errMsg+=". "+stringToBeAppended;
	}
	
	//Keys
	public Result constructResultObject() {
		Result result=new Result();
		result.addParam(new Param("dbpErrCode", this.errCode));
		result.addParam(new Param("dbpErrMsg", this.errMsg));
		result.addHttpStatusCodeParam(this.httpErrCode);
		result.addOpstatusParam(this.opStatus);
		return result;
	}
	
	public JsonObject constructJSONObject() {
		JsonObject json = new JsonObject();
		json.addProperty("dbpErrCode", this.errCode);
		json.addProperty("dbpErrMsg", this.errMsg);
		json.addProperty("httpStatusCode", this.httpErrCode);
		json.addProperty("opstatus", this.opStatus);
		return json;
	}
	
	public Result updateResultObject(Result result) {
		result.addParam(new Param("dbpErrCode", this.errCode));
		result.addParam(new Param("dbpErrMsg", this.errMsg));
		result.addHttpStatusCodeParam(this.httpErrCode);
		result.addOpstatusParam(this.opStatus);
		return result;
	}
}