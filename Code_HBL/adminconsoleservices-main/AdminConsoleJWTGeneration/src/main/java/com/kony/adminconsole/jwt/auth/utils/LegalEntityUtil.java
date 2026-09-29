package com.kony.adminconsole.jwt.auth.utils;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.constants.TemenosConstantsC360;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.processor.IdentityHandler;
import com.konylabs.middleware.controller.DataControllerRequest;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import java.util.Map;


public class LegalEntityUtil {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
    public static void addCompanyIDToHeaders( DataControllerRequest request) {
        String legalEntityId;
        if(request.getParameter(TemenosConstantsC360.LEGAL_ENTITY_ID) != null) {
            legalEntityId = request.getParameter(TemenosConstantsC360.LEGAL_ENTITY_ID);
        } else {
            legalEntityId = getLegalEntityId(request);
        }
        Map<String, Object> headerMap = request.getHeaderMap();
        if(headerMap != null) {
            headerMap.put(TemenosConstantsC360.COMPANY_ID, legalEntityId);
        }
    }

    public static String getLegalEntityId(DataControllerRequest request){
        try {
            if(request == null) {
                return "";
            }
            ServicesManager servicesManager = request.getServicesManager();
            if(servicesManager == null) {
                return "";
            }
            IdentityHandler identityHandler = servicesManager.getIdentityHandler();
            if(identityHandler == null) {
                return "";
            }
            Map<String, Object> securityAttributes = identityHandler.getSecurityAttributes();
            if(securityAttributes == null || securityAttributes.isEmpty()) {
                return "";
            }
            JSONObject obj = CommonUtilities.getStringAsJSONObject(securityAttributes.get("raw_response").toString());
            Object loggedInUserLegalEntityID = null;
            if(obj != null && obj.length() > 0) {
                JSONObject user_attributes = CommonUtilities.getStringAsJSONObject(obj.optString("user_attributes"));
                if(user_attributes != null && user_attributes.length() > 0) {
                    loggedInUserLegalEntityID = user_attributes.optString("legalEntityId");
                }
            }
            if(loggedInUserLegalEntityID != null && StringUtils.isNotBlank(loggedInUserLegalEntityID.toString())) {
                diagnostic.prepareDebug("Adding legalEntityId from user attributes").log();
                return loggedInUserLegalEntityID.toString();
            }
            Map<String, Object> userAttributes = identityHandler.getUserAttributes();
            if(userAttributes == null) {
                return "";
            }
            loggedInUserLegalEntityID = userAttributes.get("legalEntityId");
            if(loggedInUserLegalEntityID != null && StringUtils.isNotBlank(loggedInUserLegalEntityID.toString())) {
                diagnostic.prepareDebug("Adding legalEntityId from user attributes").log();
                return loggedInUserLegalEntityID.toString();
            }
            diagnostic.prepareDebug("Unable to get legalEntityId from user attributes, defaulting to blank").log();
//            throw new RuntimeException("Unable to get legalEntityId from user attributes");
        } catch ( Exception e ) {
            alert.prepareError("Error while fetching legalEntityId from user attributes").log();
        }
        return "";
    }

}
