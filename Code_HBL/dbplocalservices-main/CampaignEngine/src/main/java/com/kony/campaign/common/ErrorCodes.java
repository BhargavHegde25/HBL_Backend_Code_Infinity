package com.kony.campaign.common;

public enum ErrorCodes {
	
	ERR_17001(17001 ,"Invalid Inputs provided."),
	ERR_17002(17002 , "Error while fetching campaigns from microservice."),
	ERR_17003(17003 ,  "Error while processing ignore campaigns."),
	ERR_17004(17004 , "Error while processing default Campaigns."),
	ERR_17005(17005 , "Error while processing DataContext service."),
	ERR_17006(17006 , "Error while preparing online channel response."),
	ERR_17007(17007 , "Error while invoking Queue master service."),
	ERR_17008(17008 , "Error while preparing offline channel response."),
	ERR_17009(17009 , "Error while finding eligible campaign."),
	ERR_17010(17010 ,  "Error while performing operation on Cache."),
	ERR_17011(17011 , "Generic error while processign Campaign."),
	ERR_17012(17012 , "Error while inserting custcompletedcampaign"),
	ERR_17013(17013 , "Error while fetching campaigns.");
	
    public static final String ERROR_CODE_KEY = CampaignConstants.DBP_ERROR_CODE;
    public static final String ERROR_MESSAGE_KEY = CampaignConstants.DBP_ERROR_MESSAGE;
   
    private int errorCode;
    private String message;

    private ErrorCodes(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }



}
