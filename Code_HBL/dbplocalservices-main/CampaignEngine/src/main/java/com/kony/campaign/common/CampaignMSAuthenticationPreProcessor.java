package com.kony.campaign.common;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.kony.campaign.jwt.auth.Authentication;
import com.kony.campaign.util.CampaignUtil;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.kony.dbputilities.util.TokenUtils;

public class CampaignMSAuthenticationPreProcessor implements DataPreProcessor2 {	
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		return (setCloudAuthenticationHeaders(request, result) && setCampaignAuthenticationHeader(request, result));
	}
	public boolean setCampaignAuthenticationHeader(DataControllerRequest request, Result result) {
	    try {
	      Authentication authentication = Authentication.getInstance();
	      request.addRequestParam_("flowType", "PRELOGIN");
	      request.addRequestParam_("Authorization", TokenUtils.getMSAuthToken(request));
	    } catch (Exception e) {
	      alert.prepareError("Error while getting authtoken" + e).log();
	      result.addErrMsgParam("Error while fetching auth token " + e.getMessage());
	      return false;
	    } 
	    return true;
	  }
	  
	  public boolean setCloudAuthenticationHeaders(DataControllerRequest request, Result result) {
	    try {
	      String clouddeploymentType = CampaignUtil.getServerProperty("CAMPAIGN_DEPLOYMENT_PLATFORM", null);
	      if (StringUtils.isNotBlank(clouddeploymentType))
	        if ("AWS".equalsIgnoreCase(clouddeploymentType)) {
	          request.addRequestParam_("x-api-key", 
	              CampaignUtil.getServerProperty("CAMPAIGN_AUTHORIZATION_KEY", null));
	        } else if ("AZURE".equalsIgnoreCase(clouddeploymentType)) {
	          request.addRequestParam_("x-functions-key", 
	              CampaignUtil.getServerProperty("CAMPAIGN_AUTHORIZATION_KEY", null));
	        }  
	    } catch (Exception e) {
	      alert.prepareError("Error while setting cloud deployment authorization" + e).log();
	      result.addErrMsgParam("Error while setting deployment authorization header" + e.getMessage());
	      return false;
	    } 
	    return true;
	  }

}
