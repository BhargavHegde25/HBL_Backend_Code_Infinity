package com.dbp.transactionslimitengine.utils;

public enum TransactionsLimitErrorCodesEnum {
	ERROR_INSECUREINPUT("18000", "Input contains insecure data."),
	ERROR_FEATUREORDATEMISSING("18001", "featureactionid or date is missing."),
	ERROR_COMPCUSTACCMISSING("18002", "companyid,accountid and customerid all cannot be null."),
	ERROR_INVALIDDATEFORMAT("18003", "Invalid date format, please provide yyyy-MM-dd format."),
	ERROR_INVALIDFEATUREID("18004", "Invalid featureactionid, no transactions exists with this featureactionid."),
	ERROR_EXCEPTION("18005", ""),
	ERROR_NOFEATUREACTIONSFORLIMITGROUP("18006", "No feature actions ids found for given limitGroupId"), 
	ERROR_LIMITORFEATUREORDATEMISSING("18007", "Limit group Id or featureActionId or date is missing");
	String errcode;
	String errmsg;

	private TransactionsLimitErrorCodesEnum(String errcode, String errmsg) {
		this.errcode = errcode;
		this.errmsg = errmsg;
	}

	public String getErrCode() {
		return this.errcode;
	}

	public String getErrMsg() {
		return this.errmsg;
	}

}
