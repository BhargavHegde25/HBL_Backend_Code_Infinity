package com.temenos.infinity.tradefinanceservices.config;


import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.kony.dbputilities.util.TokenUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.nimbusds.jose.JWSAlgorithm;
import com.temenos.infinity.api.commons.invocation.Executor;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.transact.tokenmanager.dto.BackendCertificate;
import com.temenos.infinity.transact.tokenmanager.exception.CertificateNotRegisteredException;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class TradeFinanceUtils {
    
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    private static class Holder {

        private static TradeFinanceUtils instance = new TradeFinanceUtils();
    }

    public static TradeFinanceUtils getInstance() {
        return Holder.instance;
    }

    private TradeFinanceUtils() {

    }
    
    public String generateDMSToken(DataControllerRequest request) throws Exception {
        String authToken = "";
        try {
        	Map<String, String> inputparamMap = new HashMap<>();
			inputparamMap.put("customerId",getUserAttributeFromIdentity(request, "customer_id"));
			authToken = TokenUtils.getDMSAuthToken(inputparamMap);
			
        } catch (CertificateNotRegisteredException e) {
            alert.prepareError("Certificate Not Registered" + e).log();
        } catch (Exception e) {
            alert.prepareError("Exception occurred during generation of authToken " + e).log();
        }
        return authToken;

    }
    
    /**
     * Get User Attributes from Identity Session
     * 
     * @param request
     * @param attributeName
     */ 
    public static String getUserAttributeFromIdentity(DataControllerRequest request, String attribute) {
        try {
            if (request.getServicesManager() != null && request.getServicesManager().getIdentityHandler() != null) {
                Map<String, Object> userMap = request.getServicesManager().getIdentityHandler().getUserAttributes();
                if (userMap.get(attribute) != null) {
                    String attributeValue = userMap.get(attribute) + "";

                    return attributeValue;
                }

            }
        } catch (Exception e) {
            alert.prepareError("Error occurred while fetching user attributes " + e).log();
        }

        return "";
    }
    
    /**
     * Get Backend Certificate from DB
     * 
     * @param backendName
     */    
    public static BackendCertificate getCertFromDB(String backendName) {
        BackendCertificate backendCertificate = new BackendCertificate();
        Map<String, Object> inputMap = new HashMap<>();
        StringBuffer queryString = new StringBuffer();
        queryString.append("BackendName" + " ");
        queryString.append("eq" + " ");
        queryString.append(backendName);

        inputMap.put("$filter", queryString.toString());
        String backendCertResponse = null;
        try {
            backendCertResponse =
                    Executor.invokeService(TradeFinanceAPIServices.SERVICE_BACKEND_CERTIFICATE, inputMap, null);
            diagnostic.prepareDebug("Backend Certificate" + backendCertResponse).log();
        } catch (Exception e1) {
            alert.prepareError("Service call to dbxdb failed "+e1.toString()).log();
        }
        JSONObject serviceResponseJSON = Utilities.convertStringToJSON(backendCertResponse);
        if (serviceResponseJSON == null) {
            alert.prepareError("EmptyResponse no backend certificate for MS").log();
        }
        if (serviceResponseJSON.has("backendcertificate")
                && serviceResponseJSON.getJSONArray("backendcertificate").length() > 0) {
            JSONObject certificateObj = serviceResponseJSON.getJSONArray("backendcertificate").getJSONObject(0);
            backendCertificate.setBackendName(certificateObj.getString("BackendName"));
            String certificateEncryptionKey = new String();
            try {
                certificateEncryptionKey = TradeFinanceServiceConfigurations.T24_PRIVATE_ENCRYPTION_KEY.getValue();
            } catch (Exception e) {
                alert.prepareError("Couldnt parse MS_PRIVATE_ENCRYPTION_KEY from environment "+ e.toString()).log();

            }

            if (StringUtils.isNotBlank(certificateEncryptionKey))
                backendCertificate.setCertificateEncryptionKey(certificateEncryptionKey);
            backendCertificate.setCertificatePrivateKey(certificateObj.getString("CertPrivateKey"));
            backendCertificate.setCertificatePublicKey(certificateObj.getString("CertPublicKey"));
            backendCertificate.setGetPublicKeyServiceURL("/sample/service");
            backendCertificate.setId(certificateObj.getString("id"));
            backendCertificate.setJwsAlgorithm(JWSAlgorithm.RS256);

        }

        return backendCertificate;

    }
    

}
