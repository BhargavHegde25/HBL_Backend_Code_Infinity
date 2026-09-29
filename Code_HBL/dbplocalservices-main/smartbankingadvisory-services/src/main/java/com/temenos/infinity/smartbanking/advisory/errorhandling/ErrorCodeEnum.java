package com.temenos.infinity.smartbanking.advisory.errorhandling;

import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.smartbanking.advisory.constants.CommonConstants;


public enum ErrorCodeEnum {

	//JWT Token generation Error codes
	ERR_71000(71000, "JWT Token generation failed."),
	
	//Validation Error Codes
	
	ERR_81000(81000, "Validation failed: "),
	ERR_81002(81002, "Invalid data/parameters provided"),
	
	

	//Functional/Backend Error codes
	
	ERR_82000(82000, "Backend Failed: "),
	

    //Experience Api Specific Error Codes
    ERR_83000(83000, "Exception occured: ");
        
    
   
	
	private int errCode;
	private String errMsg;

	private ErrorCodeEnum(int errCode, String errMsg) {
		this.errCode = errCode;
		this.errMsg = errMsg;
	}

	public int getErrorCode() {
		return errCode;
	}

	public String getErrorMessage() {
		return errMsg;
	}

	public String getErrorCodeAsString() {
		return String.valueOf(errCode);
	}
	
	public void appendToErrorMessage(String stringToBeAppended) {
		this.errMsg+=". "+stringToBeAppended;
	}

	public Result constructResultObject() {
		Result result=new Result();
		return addAttributesToResultObject(result);
	}
	
	public Result updateResultObject(Result result) {
		if(result==null){
			return constructResultObject();
		}else{
			return addAttributesToResultObject(result);
		}
	}

	private Result addAttributesToResultObject(Result result) {
		result.addParam(new Param(CommonConstants.ERRCODE, this.getErrorCodeAsString()));
		result.addParam(new Param(CommonConstants.ERRMSG, this.errMsg));
		return result;
	}
	
}