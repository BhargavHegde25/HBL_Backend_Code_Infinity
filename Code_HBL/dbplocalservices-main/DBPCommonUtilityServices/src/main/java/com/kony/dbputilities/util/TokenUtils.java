package com.kony.dbputilities.util;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.api.processor.IdentityHandler;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.kony.dbputilities.util.ServerConfigurations;
import com.temenos.infinity.transact.tokenmanager.exception.CertificateNotRegisteredException;
import com.temenos.infinity.transact.tokenmanager.jwt.TokenGenerator;

public class TokenUtils {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private JSONObject tokenobj;
	private String token;

	public TokenUtils(String token) {
		this.token = token;
		tokenobj = new JSONObject();
		decodeToken();
	}

	public void decodeToken() {
		if (this.token != null) {
			String[] parts = token.split("\\.");
			if (parts.length < 3) {
				diagnostic.prepareDebug("Invalid token.").log();
				return;
			}
			String res = new String(Base64.getUrlDecoder().decode(parts[1]));
			tokenobj = new JSONObject(res);
		} else {
			diagnostic.prepareDebug("Invalid token.").log();
		}
	}

	public String getValue(String key) {
		if (tokenobj.has(key))
			return tokenobj.getString(key);
		return null;
	}
	
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
        	alert.prepareError(e.toString()).log();
        	return "";
        }
        return "";
    }	
    
	public static String getT24AuthToken(DataControllerRequest request) {
		String authToken = "";
		String userid = "";
	    String userName = "";	
	    String flowType = request.containsKeyInRequest(DBPUtilitiesConstants.FLOW_TYPE) ? request.getParameter(DBPUtilitiesConstants.FLOW_TYPE) : "";
		String issuer = request.containsKeyInRequest("issuer") ? request.getParameter("issuer") : "";
		try {
		switch (flowType) {
		case DBPUtilitiesConstants.PRE_LOGIN_FLOW:
		case "":
			userName = DBPUtilitiesConstants.TRANSACT_PRELOGIN_USERNAME;
			userid = DBPUtilitiesConstants.TRANSACT_PRELOGIN_USERID;
			break;
		case DBPUtilitiesConstants.LOGIN_FLOW:
			userName = request.getParameter(DBPUtilitiesConstants.PARAM_USERNAME);
			userid = request.getParameter(DBPUtilitiesConstants.PARAM_DBX_USER_ID);
			break;
		case DBPUtilitiesConstants.POST_LOGIN_FLOW:
			userName = getUserAttributeFromIdentity(request, DBPUtilitiesConstants.PARAM_USERNAME);
			userid = getUserAttributeFromIdentity(request, DBPUtilitiesConstants.PARAM_CUSTOMER_ID);
			break;
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("productType", DBPUtilitiesConstants.PRODUCT_TYPE_T24);
		if(issuer!="")
			params.put("defaultIssuer", issuer);
		long tokenValidity = 0;
		tokenValidity = Long.parseLong(ServerConfigurations.MS_T24_AUTH_TOKEN_VALIDITY.getValueIfExists());
		authToken = TokenGenerator.generateAuthToken(DBPUtilitiesConstants.BACKENDCERTNAME,
				userName, userid, DBPUtilitiesConstants.TRANSACT_GENERIC_ROLEID,
				tokenValidity, true, params);
		} catch (Exception e) {
			alert.prepareError("error occured while generating token ", e).log();
		}
		return authToken;
	}
	
	public static String getT24AuthToken(Map<String, String> input, Map<String, Object> headerMap) {
		String authToken = "";
		String userid = "";
	    String userName = "";
		try {
			String flowType = input.get(DBPUtilitiesConstants.FLOW_TYPE);
			switch (flowType) {
			case DBPUtilitiesConstants.PRE_LOGIN_FLOW:
			case "":
				userName = DBPUtilitiesConstants.TRANSACT_PRELOGIN_USERNAME;
				userid = DBPUtilitiesConstants.TRANSACT_PRELOGIN_USERID;
				break;
			case DBPUtilitiesConstants.LOGIN_FLOW:
				userName = input.get(DBPUtilitiesConstants.PARAM_USERNAME);
				userid = input.get(DBPUtilitiesConstants.PARAM_DBX_USER_ID);
				break;
			case DBPUtilitiesConstants.POST_LOGIN_FLOW:
				IdentityHandler identityHandler = ServicesManagerHelper.getServicesManager().getIdentityHandler();
				Map<String, Object> userAttributes = identityHandler.getUserAttributes();
				if (userAttributes != null && userAttributes.size() > 0) {
					userName = (String) userAttributes.get(DBPUtilitiesConstants.PARAM_USERNAME);
					userid = identityHandler.getUserId();
				}
				break;
			}
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("productType", DBPUtilitiesConstants.PRODUCT_TYPE_T24);
			long tokenValidity = 0;
    		tokenValidity = Long.parseLong(ServerConfigurations.MS_T24_AUTH_TOKEN_VALIDITY.getValueIfExists());
			authToken = TokenGenerator.generateAuthToken(DBPUtilitiesConstants.BACKENDCERTNAME,
					userName, userid, DBPUtilitiesConstants.TRANSACT_GENERIC_ROLEID,
					tokenValidity, true, params);
		} catch (Exception e) {
			alert.prepareError("error occured while generating token ", e).log();
		}
		return authToken;
	}
	
	public static String getMSAuthToken(DataControllerRequest request) {
		String authToken = "";
		String userid = "";
	    String userName = "";	
		String flowType = request.getParameter(DBPUtilitiesConstants.FLOW_TYPE);
		try {
		switch (flowType) {
		case DBPUtilitiesConstants.PRE_LOGIN_FLOW:
		case "":
			userName = DBPUtilitiesConstants.MS_PRELOGIN_USERNAME;
			userid = DBPUtilitiesConstants.MS_PRELOGIN_USERID;
			break;
		case DBPUtilitiesConstants.LOGIN_FLOW:
			userName = request.getParameter(DBPUtilitiesConstants.PARAM_USERNAME);
			userid = request.getParameter(DBPUtilitiesConstants.PARAM_DBX_USER_ID);
			break;
		case DBPUtilitiesConstants.POST_LOGIN_FLOW:
			userName = getUserAttributeFromIdentity(request, DBPUtilitiesConstants.PARAM_USERNAME);
			userid = getUserAttributeFromIdentity(request, DBPUtilitiesConstants.PARAM_CUSTOMER_ID);
			break;
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("productType", DBPUtilitiesConstants.PRODUCT_TYPE_MS);
		long tokenValidity = 0;
		tokenValidity = Long.parseLong(ServerConfigurations.MS_T24_AUTH_TOKEN_VALIDITY.getValueIfExists());
		authToken = TokenGenerator.generateAuthToken(DBPUtilitiesConstants.BACKENDCERTNAME,
				userName, userid, DBPUtilitiesConstants.MS_GENERIC_ROLEID,
				tokenValidity, true, params);
		} catch (Exception e) {
			alert.prepareError("error occured while generating token ", e).log();
		}
		return authToken;
	}

	public static String getMSAuthToken(Map<String, String> input, Map<String, Object> headerMap) {
		String authToken = "";
		String userid = "";
	    String userName = "";
		try {
			String flowType = input.get(DBPUtilitiesConstants.FLOW_TYPE);
			switch (flowType) {
			case DBPUtilitiesConstants.PRE_LOGIN_FLOW:
			case "":
				userName = DBPUtilitiesConstants.MS_PRELOGIN_USERNAME;
				userid = DBPUtilitiesConstants.MS_PRELOGIN_USERID;
				break;
			case DBPUtilitiesConstants.LOGIN_FLOW:
				userName = input.get(DBPUtilitiesConstants.PARAM_USERNAME);
				userid = input.get(DBPUtilitiesConstants.PARAM_DBX_USER_ID);
				break;
			case DBPUtilitiesConstants.POST_LOGIN_FLOW:
				IdentityHandler identityHandler = ServicesManagerHelper.getServicesManager().getIdentityHandler();
				Map<String, Object> userAttributes = identityHandler.getUserAttributes();
				if (userAttributes != null && userAttributes.size() > 0) {
					userName = (String) userAttributes.get(DBPUtilitiesConstants.PARAM_USERNAME);
					userid = identityHandler.getUserId();
				}
				break;
			}
			String roleId = DBPUtilitiesConstants.MS_GENERIC_ROLEID;
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("productType", DBPUtilitiesConstants.PRODUCT_TYPE_MS);
			long tokenValidity = 0;
    		tokenValidity = Long.parseLong(ServerConfigurations.MS_T24_AUTH_TOKEN_VALIDITY.getValueIfExists());
			authToken = TokenGenerator.generateAuthToken(DBPUtilitiesConstants.BACKENDCERTNAME,
					userName, userid, DBPUtilitiesConstants.MS_GENERIC_ROLEID,
					tokenValidity, true, params);
		} catch (Exception e) {
			alert.prepareError("error occured while generating token ", e).log();
		}
		return authToken;
	}
	
	public static String getPartyMSAuthToken(DataControllerRequest request) {
		String authToken = "";
		String userid = "";
	    String userName = "";	
		String flowType = request.getParameter(DBPUtilitiesConstants.FLOW_TYPE);
		try {
		switch (flowType) {
		case DBPUtilitiesConstants.PRE_LOGIN_FLOW:
		case "":
			userName = DBPUtilitiesConstants.PARTY_MS_PRELOGIN_USERNAME;
			userid = DBPUtilitiesConstants.PARTY_MS_PRELOGIN_USERID;
			break;
		case DBPUtilitiesConstants.LOGIN_FLOW:
			userName = request.getParameter(DBPUtilitiesConstants.PARAM_USERNAME);
			userid = request.getParameter(DBPUtilitiesConstants.PARAM_DBX_USER_ID);
			break;
		case DBPUtilitiesConstants.POST_LOGIN_FLOW:
			IdentityHandler identityHandler = request.getServicesManager().getIdentityHandler();
			Map<String, Object> userAttributes = identityHandler.getUserAttributes();
			if (userAttributes != null && userAttributes.size() > 0) {
				userName = (String) userAttributes.get(DBPUtilitiesConstants.PARAM_USERNAME);
				userid = identityHandler.getUserId();
			}
			break;
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("productType", DBPUtilitiesConstants.PRODUCT_TYPE_MS);
		long tokenValidity = 0;
		tokenValidity = Long.parseLong(ServerConfigurations.MS_T24_AUTH_TOKEN_VALIDITY.getValueIfExists());
		authToken = TokenGenerator.generateAuthToken(DBPUtilitiesConstants.BACKENDCERTNAME,
				userName, userid, DBPUtilitiesConstants.MS_GENERIC_ROLEID,
				tokenValidity, true, params);
		} catch (Exception e) {
			alert.prepareError("error occured while generating token ", e).log();
		}
		return authToken;
	}

	public static String getCommonMSAuthToken() throws CertificateNotRegisteredException {
		String authToken = "";
		try {
		String userid = DBPUtilitiesConstants.PARTY_MS_PRELOGIN_USERID;
	    String userName = DBPUtilitiesConstants.PARTY_MS_PRELOGIN_USERNAME;	
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("productType", DBPUtilitiesConstants.PRODUCT_TYPE_MS);
		long tokenValidity = 0;
		tokenValidity = Long.parseLong(ServerConfigurations.MS_T24_AUTH_TOKEN_VALIDITY.getValueIfExists());
		authToken = TokenGenerator.generateAuthToken(DBPUtilitiesConstants.BACKENDCERTNAME,
				userName, userid, DBPUtilitiesConstants.MS_GENERIC_ROLEID,
				tokenValidity, true, params);
		} catch(Exception e) {
			alert.prepareError("error occured while generating token ", e).log();
		}return authToken;
	}

	public static String getAMSAuthToken(Map<String, String> input) throws CertificateNotRegisteredException{
		String authToken = "";
		try {
		String userid = input.containsKey("customerId") ? input.get("customerId") : DBPUtilitiesConstants.MS_PRELOGIN_USERID;
	    String userName = DBPUtilitiesConstants.AMS_USERNAME;	
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("productType", DBPUtilitiesConstants.PRODUCT_TYPE_MS);
		long tokenValidity = 0;
		tokenValidity = Long.parseLong(ServerConfigurations.MS_T24_AUTH_TOKEN_VALIDITY.getValueIfExists());
		authToken = TokenGenerator.generateAuthToken(DBPUtilitiesConstants.AMS_BACKENDCERTNAME,
				userName, userid, ServerConfigurations.AMS_ROLE_ID.getValueIfExists(),
				tokenValidity, true, params);
		} catch(Exception e) {
			alert.prepareError("error occured while generating token ", e).log();
		}
		return authToken;
	}

	public static String getHoldingsMSAuthToken(Map<String, String> input) throws CertificateNotRegisteredException {
		String authToken = "";
		try {
		String userid = input.get("customerId");
	    String userName = DBPUtilitiesConstants.AMS_USERNAME;	
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("productType", DBPUtilitiesConstants.PRODUCT_TYPE_MS);
		long tokenValidity = 0;
		tokenValidity = Long.parseLong(ServerConfigurations.MS_T24_AUTH_TOKEN_VALIDITY.getValueIfExists());
		authToken = TokenGenerator.generateAuthToken(DBPUtilitiesConstants.HOLDINGSMS_BACKENDCERTNAME,
				userName, userid, ServerConfigurations.HOLDINGS_ROLE_ID.getValueIfExists(),
				tokenValidity, true, params);
		} catch(Exception e) {
			alert.prepareError("error occured while generating token ", e).log();
		}
		return authToken;
	}

	public static String getPortfolioWealthMSAuthToken(Map<String, String> input) throws CertificateNotRegisteredException {
		String authToken = "";
		try {
		String userid = input.get("customerId");
	    String userName = input.get("userName");	
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("productType", DBPUtilitiesConstants.PRODUCT_TYPE_MS);
		long tokenValidity = 0;
		tokenValidity = Long.parseLong(ServerConfigurations.MS_T24_AUTH_TOKEN_VALIDITY.getValueIfExists());
		authToken = TokenGenerator.generateAuthToken(DBPUtilitiesConstants.PFWMS_BACKENDCERTNAME,
				userName, userid, DBPUtilitiesConstants.TRANSACT_GENERIC_ROLEID,
				tokenValidity, true, params);
		} catch(Exception e) {
			alert.prepareError("error occured while generating token ", e).log();
		}
		return authToken;
	}
	

	public static String getDMSAuthToken(Map<String, String> input) throws CertificateNotRegisteredException {
		String authToken = "";
		try {
		String userid = input.get("customerId");
	    String userName = DBPUtilitiesConstants.AMS_USERNAME;	
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("productType",  DBPUtilitiesConstants.PRODUCT_TYPE_DOCMS);
		params.put("defaultIssuer", DBPUtilitiesConstants.FABRIC_ISSUER);
		params.put("audienceType", DBPUtilitiesConstants.DOC_AUDTYPE);
		long tokenValidity = 0;
		tokenValidity = Long.parseLong(ServerConfigurations.MS_T24_AUTH_TOKEN_VALIDITY.getValueIfExists());
		authToken = TokenGenerator.generateAuthToken(DBPUtilitiesConstants.DMS_BACKEND,
				userName, userid, DBPUtilitiesConstants.DMS_ROLEID,
				tokenValidity, true, params);
		} catch(Exception e) {
			alert.prepareError("error occured while generating token ", e).log();
		}
		return authToken;
	}
	
	public static String getAccAggMSAuthToken(DataControllerRequest request) throws CertificateNotRegisteredException{
		String authToken = "";
		try {
		String userid = getUserAttributeFromIdentity(request, DBPUtilitiesConstants.PARAM_CUSTOMER_ID);
	    String userName = DBPUtilitiesConstants.AMS_USERNAME;	
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("productType", DBPUtilitiesConstants.PRODUCT_TYPE_MS);
		long tokenValidity = 0;
		tokenValidity = Long.parseLong(ServerConfigurations.MS_T24_AUTH_TOKEN_VALIDITY.getValueIfExists());
		authToken = TokenGenerator.generateAuthToken(DBPUtilitiesConstants.ACCAGGMS_BACKENDCERTNAME,
				userName, userid, ServerConfigurations.ACCAGG_ROLE_ID.getValueIfExists(),
				tokenValidity, true, params);
		} catch(Exception e) {
			alert.prepareError("error occured while generating token ", e).log();
		}
		return authToken;
	}
}