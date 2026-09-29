package com.temenos.infinity.smartbanking.advisory.utils;


import static com.temenos.infinity.smartbanking.advisory.constants.CommonConstants.HTTP_ERRCODE_400;
import static com.temenos.infinity.smartbanking.advisory.constants.CommonConstants.OPSTATUS_FAILURE;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

import com.temenos.infinity.smartbanking.advisory.errorhandling.ErrorCodeEnum;
import com.temenos.infinity.smartbanking.advisory.errorhandling.SBAException;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;

public class CommonUtils {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	
	public static boolean isSanitizeHTML(String untrustedHTML, boolean isSpecialCharsAllowed) {
        PolicyFactory policy = new HtmlPolicyBuilder()
        	.allowElements("a", "b", "blockquote", "br", "del", "div", "em", 
        			"font", "h1", "h2", "h3", "h4", "h5", "h6", "i", "input", "ins", "li", "mark", 
        			"ol", "p", "small", "span", "strong", "sub", "sup",
        			"table", "td", "tr", "u", "ul")
            .allowAttributes("href").onElements("a")
            .allowAttributes("color").onElements("font")
            .allowAttributes("id", "border", "style").onElements("table")
            .allowAttributes("height", "width").onElements("tr")
            .allowAttributes("height", "width").onElements("td")
            .allowAttributes("class", "id", "title").globally()
            .allowStandardUrlProtocols()
            .requireRelNofollowOnLinks()
            .allowStyling()
            .toFactory();

        String sanitizedStr = null;
        if(isSpecialCharsAllowed) {
        	sanitizedStr = policy.sanitize(untrustedHTML).replaceAll("&amp;", "&").replaceAll("&#34;", "\"").replaceAll("&#39;", "\'");
        } else {
        	sanitizedStr = policy.sanitize(untrustedHTML).replaceAll("&amp;", "&");
        }      
        if(sanitizedStr.equals(untrustedHTML))
        {
        	return true;
        }else {
        	return false;
        }
	}
	
	public static void constructAndThrowValidationException(String... message) throws SBAException
	{
		String msg = (message.length != 0) ? message[0] : "";
		throw new SBAException(ErrorCodeEnum.ERR_81000.getErrorCodeAsString(), 
				ErrorCodeEnum.ERR_81000.getErrorMessage()+ msg,
				HTTP_ERRCODE_400, OPSTATUS_FAILURE);
	}
	
	public static void constructAndThrowBackendException(String... message) throws SBAException
	{
		String msg = (message.length != 0) ? message[0] : "";
		throw new SBAException(ErrorCodeEnum.ERR_82000.getErrorCodeAsString(), 
				ErrorCodeEnum.ERR_82000.getErrorMessage()+ msg,
				HTTP_ERRCODE_400, OPSTATUS_FAILURE);
	}
	
	public static void constructAndThrowMiddlewareException(String... message) throws SBAException
	{
		String msg = (message.length != 0) ? message[0] : "";
		throw new SBAException(ErrorCodeEnum.ERR_83000.getErrorCodeAsString(), 
				ErrorCodeEnum.ERR_83000.getErrorMessage()+ msg,
				HTTP_ERRCODE_400, OPSTATUS_FAILURE);
	}
	
	public static String generateUniqueID(int length) {
		try {
			String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
	        String CHAR_UPPER = CHAR_LOWER.toUpperCase();
	        String NUMBER = "0123456789";
	        String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;
	       
	        SecureRandom secureRandomGenerator = SecureRandom.getInstance("SHA1PRNG", "SUN");	        		
	        
	        if (length < 1) throw new IllegalArgumentException();
	        StringBuilder sb = new StringBuilder(length);
	        
	        for (int i = 0; i < length; i++) {
	            // 0-62 (exclusive), random returns 0-61
	            int rndCharAt = secureRandomGenerator.nextInt(DATA_FOR_RANDOM_STRING.length());
	            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);

	            sb.append(rndChar);
	        }
	        return sb.toString();	
		} catch (Exception e) {
			return null;
		}
	}
	/**
     * 
     * @param dcRequest
     * @param inputParams
     * @param headerParams
     * @param url
     * @return
     * @throws HttpCallException
     */
    public static Result invokeIntegrationServiceAndGetResult(DataControllerRequest request, Map<String, Object> params,
            Map<String, Object> headers, String serviceName, String operationName) {
        try {
            Result result =
                    DBPServiceInvocationWrapper.invokeServiceAndGetResult(serviceName, null, operationName,
                            params, headers, request);
            
            printLog(null + "_" + serviceName + "_" + operationName, params, headers, result, null);
            return result;
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            return getExceptionMsgAsResult(null + "_" + serviceName + "_" + operationName +  e.getStackTrace());
        }
        
    }
    
    private static Result getExceptionMsgAsResult(String serviceURL) {
        Result result = new Result();
        StringBuilder message = new StringBuilder();
        message.append("Exception occured while invoking service with [ServiceId_ObjectId_OperationId] [")
                .append(serviceURL).append("]");
        result.addParam("errmsg", message.toString());
        return result;
    }

    private static void printLog(String URL, Map inputParams, Map headerParams, Result result, String response) {
        if (inputParams != null) {
            alert.prepareError("InputParams for call " + URL + " : " + inputParams).log();
        }
        if (headerParams != null) {
            alert.prepareError("HeaderParams for call " + URL + " : " + headerParams).log();
        }
        if (result != null) {
            alert.prepareError("Response from call " + URL + " : " + ResultToJSON.convert(result)).log();
        } else {
            alert.prepareError("Response from call " + URL + " : " + response).log();
        }
    }

}
