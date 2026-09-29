package com.temenos.infinity.api.loanspayoff.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.nimbusds.jose.JWSAlgorithm;
import com.temenos.infinity.api.commons.invocation.Executor;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.loanspayoff.config.LoansPayoffAPIServices;
import com.temenos.infinity.api.loanspayoff.config.ServerConfigurations;
import com.temenos.infinity.transact.tokenmanager.dto.BackendCertificate;

public class LoansPayOffUtils {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

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
                    Executor.invokeService(LoansPayoffAPIServices.SERVICE_BACKEND_CERTIFICATE, inputMap, null);
            diagnostic.prepareDebug("SuryaaBC" + backendCertResponse).log();
        } catch (Exception e1) {
            alert.prepareError("Service call to dbxdb failed", e1.toString()).log();
        }
        JSONObject serviceResponseJSON = Utilities.convertStringToJSON(backendCertResponse);
        if (serviceResponseJSON == null) {
            alert.prepareError("EmptyResponse no backend certificate for t24").log();
        }
        if (serviceResponseJSON.has("backendcertificate")
                && serviceResponseJSON.getJSONArray("backendcertificate").length() > 0) {
            JSONObject certificateObj = serviceResponseJSON.getJSONArray("backendcertificate").getJSONObject(0);
            backendCertificate.setBackendName(certificateObj.getString("BackendName"));
            String certificateEncryptionKey = new String();
            try {
                certificateEncryptionKey = ServerConfigurations.T24_PRIVATE_ENCRYPTION_KEY.getValue();
            } catch (Exception e) {
                alert.prepareError("Couldnt parse T24_PRIVATE_ENCRYPTION_KEY from environment", e.toString()).log();

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

    public static String getUserAttributeFromIdentity(DataControllerRequest request, String attribute) {
        // TODO Auto-generated method stub
        try {
            if (request.getServicesManager() != null && request.getServicesManager().getIdentityHandler() != null) {
                Map<String, Object> userMap = request.getServicesManager().getIdentityHandler().getUserAttributes();
                if (userMap.get(attribute) != null) {
                    String attributeValue = userMap.get(attribute) + "";

                    return attributeValue;
                }

            }
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
        }

        return "";
    }

    public static String getT24BackendId(DataControllerRequest request) {
        ServicesManager servicesManager;
        String backendIdentifiers = null;
        try {
            servicesManager = request.getServicesManager();
            backendIdentifiers =
                    (String) servicesManager.getIdentityHandler().getUserAttributes().get("backendIdentifiers");
        } catch (Exception e) {
            return new String();
        }

        String backendUserId = null;
        if (StringUtils.isNotBlank(backendIdentifiers)) {
            backendUserId = getBackendId(backendIdentifiers, "T24");
            return backendUserId;
        } else
            return new String();

    }

    private static String getBackendId(String backendIdentifiers, String templateName) {
        String BackendId = null;
        BackendId = getCoreId(backendIdentifiers, templateName, "customerId", "1");
        return BackendId;
    }

    private static String getCoreId(String backendIdentifiers, String backendType, String identifierName,
            String SequenceNumber) {
        String BackendId = null;
        if (StringUtils.isNotBlank(backendIdentifiers)) {
            JSONObject backendIdentifiersJSON = new JSONObject(backendIdentifiers);
            if (backendIdentifiersJSON.has(backendType)) {
                JSONArray templateIdentifiers = backendIdentifiersJSON.getJSONArray(backendType);

                for (int i = 0; i < templateIdentifiers.length(); i++) {
                    String identifier_name = templateIdentifiers.getJSONObject(i).getString("identifier_name");
                    String sequenceNumber = templateIdentifiers.getJSONObject(i).getString("sequence_number");

                    if (backendType.equals("T24")) {
                        if (identifierName.equals(identifier_name) && SequenceNumber.equals(sequenceNumber)) {
                            BackendId = templateIdentifiers.getJSONObject(0).getString("BackendId");
                        }
                    } else {
                        if (identifierName.equals(identifier_name)) {
                            BackendId = templateIdentifiers.getJSONObject(0).getString("BackendId");
                        }
                    }
                }
            }
        }
        return BackendId;
    }
}
