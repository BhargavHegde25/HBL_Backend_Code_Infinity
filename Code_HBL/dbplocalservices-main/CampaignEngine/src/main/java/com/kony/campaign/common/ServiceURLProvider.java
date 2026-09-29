package com.kony.campaign.common;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.kony.campaign.util.CampaignUtil;
import com.konylabs.middleware.common.URLProvider;
import com.konylabs.middleware.controller.DataControllerRequest;

public class ServiceURLProvider implements URLProvider {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final String KEY_ENDS_MARKER = "_$_";

    @Override
    public String execute(String operationURL, DataControllerRequest requestInstance) {
        try {
            String targetURL = null;
            String propertyKey = operationURL.substring(
                    operationURL.indexOf(KEY_ENDS_MARKER) + KEY_ENDS_MARKER.length(),
                    operationURL.lastIndexOf(KEY_ENDS_MARKER));
            String baseURL = operationURL.substring(0,
                    operationURL.lastIndexOf(KEY_ENDS_MARKER) + KEY_ENDS_MARKER.length());
            targetURL = operationURL.replace(baseURL,          		
                   CampaignUtil.getServerProperty(propertyKey, null));
            return targetURL;
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }
        return null;
    }
}
