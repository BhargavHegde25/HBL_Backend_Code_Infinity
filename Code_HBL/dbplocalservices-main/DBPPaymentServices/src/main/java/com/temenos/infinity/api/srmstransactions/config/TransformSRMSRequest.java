package com.temenos.infinity.api.srmstransactions.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.Gson;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.srmstransactions.constants.SRMSTransactionsConstants;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class TransformSRMSRequest implements SRMSTransactionsConstants {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    /*
     * static holder design pattern to create singleton object
     */
    private static class Holder {
        static final TransformSRMSRequest INSTANCE = new TransformSRMSRequest();
    }

    public static TransformSRMSRequest getInstance() {
        return Holder.INSTANCE;
    }

    private TransformSRMSRequest() {

    }

    /*
     * Method to build SRMS Request Payload for Own Account Transfer
     */
    public HashMap<String, Object> OwnAccountTransfer(Map<String, Object> requestParams,
            DataControllerRequest request) {

        HashMap<String, Object> srmsParams = new HashMap<String, Object>();

        String frequency = (requestParams.get(PARAM_FREQUENCY_TYPE) != null)
                ? requestParams.get(PARAM_FREQUENCY_TYPE).toString() : "";
        String accountId = (requestParams.get(PARAM_FROM_ACCOUNT_NUMBER) != null)
                ? requestParams.get(PARAM_FROM_ACCOUNT_NUMBER).toString() : "";

        // Identify Type and SubType
        if (FREQUENCY_ONCE.equalsIgnoreCase(frequency)) {
            srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.ONE_TIME_OWN_ACOUNT_TRANSFER.getType());
            srmsParams.put(PARAM_SUB_TYPE, SRMSTypeSubTypeConfiguration.ONE_TIME_OWN_ACOUNT_TRANSFER.getSubType());
        } else {
            srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.RECURRING_OWN_ACOUNT_TRANSFER.getType());
            srmsParams.put(PARAM_SUB_TYPE, SRMSTypeSubTypeConfiguration.RECURRING_OWN_ACOUNT_TRANSFER.getSubType());
        }

        // Add Account Number in the request
        if (StringUtils.isNotBlank(accountId))
            srmsParams.put(ACCOUNT_ID, accountId);

        // Add Backend Request Body to the request
        Gson gson = new Gson();
        String json = gson.toJson(requestParams); // Convert Map to JSONObject
        srmsParams.put(PARAM_REQUEST_BODY, json.toString().replaceAll("\"", "'"));

        alert.prepareError("Own Account Transfer SRMS Input :" + srmsParams.toString()).log();

        return srmsParams;

    }

    /*
     * Method to build SRMS Request Payload for Same bank Transfer
     */
    public HashMap<String, Object> IntraBankTransfer(Map<String, Object> requestParams, DataControllerRequest request) {

        HashMap<String, Object> srmsParams = new HashMap<String, Object>();

        String frequency = (requestParams.get(PARAM_FREQUENCY_TYPE) != null)
                ? requestParams.get(PARAM_FREQUENCY_TYPE).toString() : "";
        String accountId = (requestParams.get(PARAM_FROM_ACCOUNT_NUMBER) != null)
                ? requestParams.get(PARAM_FROM_ACCOUNT_NUMBER).toString() : "";

        // Identify Type and SubType
        if (FREQUENCY_ONCE.equalsIgnoreCase(frequency)) {
            srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.INTRA_BANK_ACCOUNT_TRANSFER.getType());
            srmsParams.put(PARAM_SUB_TYPE, SRMSTypeSubTypeConfiguration.INTRA_BANK_ACCOUNT_TRANSFER.getSubType());
        } else {
            srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.RECURRING_INTRA_BANK_ACCOUNT_TRANSFER.getType());
            srmsParams.put(PARAM_SUB_TYPE,
                    SRMSTypeSubTypeConfiguration.RECURRING_INTRA_BANK_ACCOUNT_TRANSFER.getSubType());
        }

        // Add Account Number in the request
        if (StringUtils.isNotBlank(accountId))
            srmsParams.put(ACCOUNT_ID, accountId);

        // Add Backend Request Body to the request
        Gson gson = new Gson();
        String json = gson.toJson(requestParams); // Convert Map to JSONObject
        srmsParams.put(PARAM_REQUEST_BODY, json.toString().replaceAll("\"", "'"));

        alert.prepareError("Intra Bank Account Transfer SRMS Input :" + srmsParams.toString()).log();

        return srmsParams;

    }

    /*
     * Method to build SRMS Request Payload for Same bank Transfer
     */
    public HashMap<String, Object> InterBankTransfer(Map<String, Object> requestParams, DataControllerRequest request) {

        HashMap<String, Object> srmsParams = new HashMap<String, Object>();

        String frequency = (requestParams.get(PARAM_FREQUENCY_TYPE) != null)
                ? requestParams.get(PARAM_FREQUENCY_TYPE).toString() : "";
        String accountId = (requestParams.get(PARAM_FROM_ACCOUNT_NUMBER) != null)
                ? requestParams.get(PARAM_FROM_ACCOUNT_NUMBER).toString() : "";
        String paymentType = (requestParams.get(PARAM_PAYMENT_TYPE) != null)
                ? requestParams.get(PARAM_PAYMENT_TYPE).toString() : "";

        // Identify Type and SubType
        if (FREQUENCY_ONCE.equalsIgnoreCase(frequency)) {
            switch (paymentType) {
            // Case statements
            case TYPE_INSTPAY:
                srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.ONE_TIME_INTER_INSTANT_TRANSFER.getType());
                srmsParams.put(PARAM_SUB_TYPE,
                        SRMSTypeSubTypeConfiguration.ONE_TIME_INTER_INSTANT_TRANSFER.getSubType());
                break;
            case TYPE_SEPA:
                srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.ONE_TIME_INTER_SEPA_TRANSFER.getType());
                srmsParams.put(PARAM_SUB_TYPE, SRMSTypeSubTypeConfiguration.ONE_TIME_INTER_SEPA_TRANSFER.getSubType());
                break;
            case TYPE_FEDWIRE:
                srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.ONE_TIME_INTER_FEDWIRE_TRANSFER.getType());
                srmsParams.put(PARAM_SUB_TYPE,
                        SRMSTypeSubTypeConfiguration.ONE_TIME_INTER_FEDWIRE_TRANSFER.getSubType());
                break;
            case TYPE_ACH:
                srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.ONE_TIME_INTER_ACH_TRANSFER.getType());
                srmsParams.put(PARAM_SUB_TYPE, SRMSTypeSubTypeConfiguration.ONE_TIME_INTER_ACH_TRANSFER.getSubType());
                break;
            case TYPE_CHAPS:
                srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.ONE_TIME_INTER_CHAPS_TRANSFER.getType());
                srmsParams.put(PARAM_SUB_TYPE, SRMSTypeSubTypeConfiguration.ONE_TIME_INTER_CHAPS_TRANSFER.getSubType());
                break;
            case TYPE_FASTER_PAYMENT:
                srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.ONE_TIME_INTER_FASTER_TRANSFER.getType());
                srmsParams.put(PARAM_SUB_TYPE,
                        SRMSTypeSubTypeConfiguration.ONE_TIME_INTER_FASTER_TRANSFER.getSubType());
                break;
            case TYPE_RINGS:
                srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.ONE_TIME_INTER_RINGS_TRANSFER.getType());
                srmsParams.put(PARAM_SUB_TYPE, SRMSTypeSubTypeConfiguration.ONE_TIME_INTER_RINGS_TRANSFER.getSubType());
                break;
            // Default case statement
            default:
                srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.ONE_TIME_INTER_INSTANT_TRANSFER.getType());
                srmsParams.put(PARAM_SUB_TYPE,
                        SRMSTypeSubTypeConfiguration.ONE_TIME_INTER_INSTANT_TRANSFER.getSubType());
                break;
            }
        } else {
            switch (paymentType) {
            // Case statements
            case TYPE_INSTPAY:
                srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.RECURRING_INTER_INSTANT_TRANSFER.getType());
                srmsParams.put(PARAM_SUB_TYPE,
                        SRMSTypeSubTypeConfiguration.RECURRING_INTER_INSTANT_TRANSFER.getSubType());
                break;
            case TYPE_SEPA:
                srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.RECURRING_INTER_SEPA_TRANSFER.getType());
                srmsParams.put(PARAM_SUB_TYPE, SRMSTypeSubTypeConfiguration.RECURRING_INTER_SEPA_TRANSFER.getSubType());
                break;
            case TYPE_FEDWIRE:
                srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.RECURRING_INTER_FEDWIRE_TRANSFER.getType());
                srmsParams.put(PARAM_SUB_TYPE,
                        SRMSTypeSubTypeConfiguration.RECURRING_INTER_FEDWIRE_TRANSFER.getSubType());
                break;
            case TYPE_ACH:
                srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.RECURRING_INTER_ACH_TRANSFER.getType());
                srmsParams.put(PARAM_SUB_TYPE, SRMSTypeSubTypeConfiguration.RECURRING_INTER_ACH_TRANSFER.getSubType());
                break;
            case TYPE_CHAPS:
                srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.RECURRING_INTER_CHAPS_TRANSFER.getType());
                srmsParams.put(PARAM_SUB_TYPE,
                        SRMSTypeSubTypeConfiguration.RECURRING_INTER_CHAPS_TRANSFER.getSubType());
                break;
            case TYPE_FASTER_PAYMENT:
                srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.RECURRING_INTER_FASTER_TRANSFER.getType());
                srmsParams.put(PARAM_SUB_TYPE,
                        SRMSTypeSubTypeConfiguration.RECURRING_INTER_FASTER_TRANSFER.getSubType());
                break;
            case TYPE_RINGS:
                srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.RECURRING_INTER_RINGS_TRANSFER.getType());
                srmsParams.put(PARAM_SUB_TYPE,
                        SRMSTypeSubTypeConfiguration.RECURRING_INTER_RINGS_TRANSFER.getSubType());
                break;
            // Default case statement
            default:
                srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.RECURRING_INTER_INSTANT_TRANSFER.getType());
                srmsParams.put(PARAM_SUB_TYPE,
                        SRMSTypeSubTypeConfiguration.RECURRING_INTER_INSTANT_TRANSFER.getSubType());
                break;
            }
        }
        
        //Capitalize externalAccountNumber - ExternalAccountNumber
        String ExternalAccountNumber = (requestParams.get(PARAM_EXT_ACCT_NUMBER) != null)
                ? requestParams.get(PARAM_EXT_ACCT_NUMBER).toString() : "";
        if (StringUtils.isNotBlank(ExternalAccountNumber)) {
            requestParams.remove(PARAM_EXT_ACCT_NUMBER);
            requestParams.put(StringUtils.capitalize(PARAM_EXT_ACCT_NUMBER), ExternalAccountNumber);
        }
        
        // Add Account Number in the request
        if (StringUtils.isNotBlank(accountId))
            srmsParams.put(ACCOUNT_ID, accountId);

        // Add Backend Request Body to the request
        Gson gson = new Gson();
        String json = gson.toJson(requestParams); // Convert Map to JSONObject
        srmsParams.put(PARAM_REQUEST_BODY, json.toString().replaceAll("\"", "'"));

        alert.prepareError("Inter Bank Account Transfer SRMS Input :" + srmsParams.toString()).log();

        return srmsParams;

    }
    
    /*
     * Method to build SRMS Request Payload for Swift Transfer
     */
    public HashMap<String, Object> InternationalTransfer(Map<String, Object> requestParams, DataControllerRequest request) {

        HashMap<String, Object> srmsParams = new HashMap<String, Object>();

        String frequency = (requestParams.get(PARAM_FREQUENCY_TYPE) != null)
                ? requestParams.get(PARAM_FREQUENCY_TYPE).toString() : "";
        String accountId = (requestParams.get(PARAM_FROM_ACCOUNT_NUMBER) != null)
                ? requestParams.get(PARAM_FROM_ACCOUNT_NUMBER).toString() : "";

        // Identify Type and SubType
        if (FREQUENCY_ONCE.equalsIgnoreCase(frequency)) {
            srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.ONETIME_SWIFT_TRANSFER.getType());
            srmsParams.put(PARAM_SUB_TYPE, SRMSTypeSubTypeConfiguration.ONETIME_SWIFT_TRANSFER.getSubType());
        } else {
            srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.RECURRING_SWIFT_TRANSFER.getType());
            srmsParams.put(PARAM_SUB_TYPE, SRMSTypeSubTypeConfiguration.RECURRING_SWIFT_TRANSFER.getSubType());
        }
        
        //Capitalize externalAccountNumber - ExternalAccountNumber
        String ExternalAccountNumber = (requestParams.get(PARAM_EXT_ACCT_NUMBER) != null)
                ? requestParams.get(PARAM_EXT_ACCT_NUMBER).toString() : "";
        if (StringUtils.isNotBlank(ExternalAccountNumber)) {
            requestParams.remove(PARAM_EXT_ACCT_NUMBER);
            requestParams.put(StringUtils.capitalize(PARAM_EXT_ACCT_NUMBER), ExternalAccountNumber);
        }
        
        // Add Account Number in the request
        if (StringUtils.isNotBlank(accountId))
            srmsParams.put(ACCOUNT_ID, accountId);

        // Add Backend Request Body to the request
        Gson gson = new Gson();
        String json = gson.toJson(requestParams); // Convert Map to JSONObject
        srmsParams.put(PARAM_REQUEST_BODY, json.toString().replaceAll("\"", "'"));

        alert.prepareError("International Bank Account Transfer SRMS Input :" + srmsParams.toString()).log();

        return srmsParams;

    }
    
    /*
     * Method to build SRMS Request Payload forP2PTransfer
     */
    public HashMap<String, Object> P2PTransfer(Map<String, Object> requestParams, DataControllerRequest request) {

        HashMap<String, Object> srmsParams = new HashMap<String, Object>();

        String frequency = (requestParams.get(PARAM_FREQUENCY_TYPE) != null)
                ? requestParams.get(PARAM_FREQUENCY_TYPE).toString() : "";
        String accountId = (requestParams.get(PARAM_FROM_ACCOUNT_NUMBER) != null)
                ? requestParams.get(PARAM_FROM_ACCOUNT_NUMBER).toString() : "";

        // Identify Type and SubType
        if (FREQUENCY_ONCE.equalsIgnoreCase(frequency)) {
            srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.ONETIME_P2P_TRANSFER.getType());
            srmsParams.put(PARAM_SUB_TYPE, SRMSTypeSubTypeConfiguration.ONETIME_P2P_TRANSFER.getSubType());
        } else {
            srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.RECURRING_P2P_TRANSFER.getType());
            srmsParams.put(PARAM_SUB_TYPE, SRMSTypeSubTypeConfiguration.RECURRING_P2P_TRANSFER.getSubType());
        }
        
        //Capitalize externalAccountNumber - ExternalAccountNumber
        String ExternalAccountNumber = (requestParams.get(PARAM_EXT_ACCT_NUMBER) != null)
                ? requestParams.get(PARAM_EXT_ACCT_NUMBER).toString() : "";
        if (StringUtils.isNotBlank(ExternalAccountNumber)) {
            requestParams.remove(PARAM_EXT_ACCT_NUMBER);
            requestParams.put(StringUtils.capitalize(PARAM_EXT_ACCT_NUMBER), ExternalAccountNumber);
        }
        
        // Add Account Number in the request
        if (StringUtils.isNotBlank(accountId))
            srmsParams.put(ACCOUNT_ID, accountId);

        // Add Backend Request Body to the request
        Gson gson = new Gson();
        String json = gson.toJson(requestParams); // Convert Map to JSONObject
        srmsParams.put(PARAM_REQUEST_BODY, json.toString().replaceAll("\"", "'"));

        alert.prepareError("P2P Bank Account Transfer SRMS Input :" + srmsParams.toString()).log();

        return srmsParams;

    }

}
