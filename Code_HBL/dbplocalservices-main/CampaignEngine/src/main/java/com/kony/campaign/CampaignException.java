package com.kony.campaign;

public class CampaignException extends Exception {		
	
	private static final long serialVersionUID = 1L;
	private final int errorCode;

	public CampaignException(String string, Exception e,int errCode) {
		super(new StringBuilder(string)
				.append("").append(e.getMessage()).toString(),e);
		this.errorCode = errCode;
	}
	
	public CampaignException(String msg,int errorcode) {
		super(msg);
		this.errorCode = errorcode;
	}

	public int getErrorCode() {
		return errorCode;
	}
	
}
