package com.temenos.dbx.product.commonsutils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.poi.util.StringUtil;

import com.temenos.dbx.product.utils.DTOConstants;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.kony.dbputilities.util.URLConstants;

import com.google.common.net.HttpHeaders;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.infinity.dbx.dbp.jwt.auth.utils.TemenosUtils;
import com.infinity.dbx.temenos.user.UserConstants;
import com.infinity.dbx.temenos.utils.ConvertJsonToResult;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.kms.KMSUtil;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.ehcache.ResultCache;
import com.konylabs.middleware.ehcache.ResultCacheImpl;
import com.konylabs.middleware.exceptions.MiddlewareException;

public class CommonUtils {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	/*
	 * Generates unique id as string.
	 * SecureRandom is used instead of Random to withstand a cryptographic attack.
	 */
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
	
	public static boolean isDMSIntegrationEnabled() {
		ServicesManager serviceManager;
		try {
			serviceManager = ServicesManagerHelper.getServicesManager();
			ConfigurableParametersHelper configurableParametersHelper = serviceManager
					.getConfigurableParametersHelper();
			String isDMSIntegrationEnabled = configurableParametersHelper.getServerProperty("DMS_INTEGRATION_ENABLED");
			if(StringUtils.isBlank(isDMSIntegrationEnabled))
				return false;
			return BooleanUtils.toBoolean(configurableParametersHelper.getServerProperty("DMS_INTEGRATION_ENABLED"));
		} catch (Exception e) {
			alert.prepareError(e.getMessage()).log();
		}
		return true;
	}
	
	/*
	 * Generates unique id as string with hyphen in intervals(EX - for interval 4 -> egft-er4g-ert64-kkio, for interval 2 -> er-u7-s2,etc)
	 * SecureRandom is used instead of Random to withstand a cryptographic attack.
	 */
	public static String generateUniqueIDHyphenSeperated(int interval, int length) {
		try {
			String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
	        String CHAR_UPPER = CHAR_LOWER.toUpperCase();
	        String NUMBER = "0123456789";
	        String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;
	       
	        SecureRandom secureRandomGenerator = SecureRandom.getInstance("SHA1PRNG", "SUN");	        		
	        
	        if (length < 1) throw new IllegalArgumentException();
	        StringBuilder sb = new StringBuilder(length);
	        int intervalCheck = 0;
	        for (int i = 0; i < length; i++) {
	            // 0-62 (exclusive), random returns 0-61
	            int rndCharAt = secureRandomGenerator.nextInt(DATA_FOR_RANDOM_STRING.length());
	            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);
	            intervalCheck++;
	            if(intervalCheck == interval)
	            	{
	            	  sb.append("-");
	            	  intervalCheck = 0;
	            	}
	            sb.append(rndChar);
	        }
	        return sb.toString();	
		} catch (Exception e) {
			return null;
		}
	}
	
	
	public static JSONArray getFirstOccuringArray(JSONObject obj) {
		
		if(StringUtils.isNotBlank(obj.optString("dbpErrMsg"))) {
			JSONArray array = new JSONArray();
			array.put(obj);
			return array;
		}

		Iterator<String> keys = obj.keySet().iterator();
		while(keys.hasNext()) {
			try {
				return obj.getJSONArray(keys.next());
			}
			catch(JSONException e) {
				//do nothing;
			}
		}
		return null;
	}

	/*
	 * Method to create file and write decoded base64 contents to it.
	 * @param fileContent - base64 encoded file content
	 * @param fileBaseName - file base name
	 * @param fileExtension - file extension
	 * @return file - created file
	 */
    public static File constructFileObjectFromBase64String(String fileContent, String fileBaseName, String fileExtension) {
    	File file = null;
		String fileDir = System.getProperty("java.io.tmpdir");
		try {
			file = new File(fileDir, fileBaseName + "." + fileExtension);
			byte[] fileContents = Base64.getDecoder().decode(fileContent);
			BufferedOutputStream bos = new BufferedOutputStream (new FileOutputStream(file));
			bos.write(fileContents);
			bos.flush();
			bos.close();
		}
		catch (Exception e) {
			alert.prepareError("Exception while decoding and writing into file: ", e).log();
			if (file != null) {
				file.delete();
			}
			return null;
		}
		return file;
	}
	
	public String getMaskedValue(String accountNumber) {
        String lastFourDigits;
        if (StringUtils.isNotBlank(accountNumber)) {
            if (accountNumber.length() > 4) {
                lastFourDigits = accountNumber.substring(accountNumber.length() - 4);
                accountNumber = "XXXX" + lastFourDigits;
            } else {
                accountNumber = "XXXX" + accountNumber;
            }
        }
        return accountNumber;
    }
    
	public static Object retreiveFromSession(String key, DataControllerRequest dcRequest) {
		try {
			ServicesManager servicesManager = dcRequest.getServicesManager();
			String sessionId = dcRequest.getHeader(Constants.DEVICE_ID);
			return retriveDataFromCache(servicesManager, key, sessionId);
		} catch (Exception e) {
			alert.prepareError("Exception occured:" + e).log();
			return null;
		}

	}
	/**
	 * retrieves data from cache
	 * 
	 * @param servicesManager
	 * @param result
	 * @param key
	 * @param sessionId
	 */
	private static Object retriveDataFromCache(ServicesManager servicesManager, String key, String sessionId) {
		Object result = null;
		try {
			String cacheKey = "";
			String userId = servicesManager.getIdentityHandler() != null
					? servicesManager.getIdentityHandler().getUserId()
					: "";
			cacheKey = StringUtils.isNotBlank(userId) && !Constants.USER_ID_ANONYMOUS.equalsIgnoreCase(userId) ? userId
					: sessionId;
			cacheKey = cacheKey + "_" + key;
			ResultCache resultCache = servicesManager.getResultCache();
			String valueInCache = "";
			try {
				valueInCache = resultCache.retrieveFromCache(cacheKey) != null
						? (String) resultCache.retrieveFromCache(cacheKey)
						: null;
			} catch (Exception e) {
				try {
					valueInCache = (String) ServicesManagerHelper.getServicesManager().getResultCache()
							.retrieveFromCache(cacheKey);
				} catch (MiddlewareException e1) {
					alert.prepareError(e1.toString()).log();
					valueInCache = (String) ResultCacheImpl.getInstance().retrieveFromCache(cacheKey);
				}
			}
			/*if (StringUtils.isNotBlank(valueInCache)) {
				Gson gson = new Gson();
				Type type = new TypeToken<HashMap<String, Object>>() {
				}.getType();
				Map<String, Object> resultMap = gson.fromJson(valueInCache, type);
				if (resultMap != null) {
					if (resultMap.get(key) != null) {
						result = resultMap.get(key);
					}
				}
			}*/
			result = valueInCache;
		} catch (Exception e) {
			alert.prepareError("Exception occured while retrieving from cache" + e).log();
			return null;
		}
		return result;
	}
	
	/******* HBL Card Payment Email Template releated Code ***/
	/******* Start 
	 * @throws Exception *******/
	
	public static void triggerCardPaymentEmail(DataControllerRequest request, Result result)
			throws Exception {
		String customerId = getUserAttributeFromIdentity(request, "customer_id");
		diagnostic.debug("customerId :###" + customerId);
		String email = getCustomerEmialfromCore(request,customerId);
		diagnostic.debug("email :###" + email);
		String date =  new SimpleDateFormat("yyyyMMdd").format(new Date());
		String nickName = "-";
		if(StringUtils.isNotBlank(request.getParameter("nickName")))
			nickName = request.getParameter("nickName");
		String transactionId = result.getParamValueByName("referenceId");
		String transactionAmount = request.getParameter("amount");
		String transactionDate = date;
		String remarks = request.getParameter("transactionsNotes");
		String cardNumber = request.getParameter("cardNumber");
		String cardAccNumber = request.getParameter("cardAccNumber");
		cardAccNumber = maskAccountNumber(cardAccNumber, 0, cardAccNumber.length() - 4, "X");
		String debtorName = nickName;
		String accountNumber = request.getParameter("fromAccountNumber");
		accountNumber = maskAccountNumber(accountNumber, 0, accountNumber.length() - 4, "X");
		String creditorname = "Himalayan Bank Ltd.";

		String emailTemplate = "CARD_PAYMENT_EMAIL_TEMPLATE";

		diagnostic.debug("triggerEmail value:" + email);
		Map<String, String> input = new HashMap<>();
		input.put("Subscribe", "true");
		input.put("EmailType", emailTemplate);
		JSONObject addContext = new JSONObject();
		addContext.put("transactionId", transactionId);
		addContext.put("transactionAmount", transactionAmount);
		addContext.put("transactionDate", transactionDate);
		addContext.put("remarks", remarks);
		addContext.put("cardNumber", cardNumber);
		addContext.put("cardAccNumber", cardAccNumber);
		addContext.put("debtorName", debtorName);
		addContext.put("accountNumber", accountNumber);
		addContext.put("creditorname", creditorname);

		input.put("AdditionalContext", KMSUtil.getOTPContent(null, null, addContext));
		input.put("Email", email);
		Map<String, String> headers = HelperMethods.getHeaders(request);
		headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
		HelperMethods.callApi(request, input, headers, URLConstants.DBX_SEND_EMAIL_ORCH);
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
	
	public static String getCustomerEmialfromCore(DataControllerRequest request, String cutomerid) throws Exception {
		String email = "";
		String mobile = "";
		HashMap<String, Object> inputParams = new HashMap<>();
		String cif = fetchCifId(request, cutomerid);
		inputParams.put("userID", cif);
		request.addRequestParam_("userID", cif);

		JSONObject customerInfo = new JSONObject();
		diagnostic.debug("cif value in  getCustomerEmialfromCore### :" + cif);
		JSONArray accounts = new JSONArray();

		HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
		

		Result result =  com.infinity.dbx.dbp.jwt.auth.utils.CommonUtils.callIntegrationService(request, inputParams, serviceHeaders,"T24ISUser", "getuserDetalFromSpotlight", false);
		Dataset userDS = result.getDatasetById(UserConstants.DATASET_USER);
		diagnostic.debug("userDS value in  getCustomerEmialfromCore### :" + userDS);
		if (userDS != null && !userDS.getAllRecords().isEmpty()) {

			JSONArray array = ResultToJSON.convertDataset(userDS);
			customerInfo = array.getJSONObject(0);
			accounts = customerInfo.getJSONArray("contactDetails");

			for (int i = 0; i < accounts.length(); i++) {
				if (accounts.getJSONObject(i).getString("contactType").equalsIgnoreCase("EMAIL")) {
					email = accounts.getJSONObject(i).getString("contactData");
					diagnostic.debug("Email val ### :" + email);
				} else if (accounts.getJSONObject(i).getString("contactType").equalsIgnoreCase("MOBILE")) {
					String prefix = accounts.getJSONObject(i).getString("iddPrefixPhone");
					String ph = accounts.getJSONObject(i).getString("contactData");
					diagnostic.debug("Phone val ### :" + ph);
					diagnostic.debug("prefix val ### :" + prefix);
					mobile = prefix + " " + ph;
				}
			}
		}

		return email;
	}
	
	public static String fetchCifId(DataControllerRequest dcRequest, String customerId) throws HttpCallException {
		String cifId = "";
		String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId + DBPUtilitiesConstants.AND
				+ DTOConstants.BACKENDTYPE + DBPUtilitiesConstants.EQUAL + DTOConstants.T24;
		Result result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
				URLConstants.BACKENDIDENTIFIER_GET);
		cifId = HelperMethods.getFieldValue(result, DTOConstants.BACKENDID);
		diagnostic.debug("cifId ### :" + cifId);
		return cifId;
	}
	
	public static String maskAccountNumber(String data, int fromIndex, int toIndex, String maskWith) {
		if (StringUtils.isNotBlank(data)) {
			StringBuilder maskedPart = new StringBuilder();
			if (StringUtils.isNotBlank(data)) {
				for (int i = fromIndex; i < toIndex; i++)
					maskedPart.append(maskWith);
				return data.replace(data.substring(fromIndex, toIndex), maskedPart.toString());
			}
			return data;
		} else {
			return data;
		}
	}
	
	/*** End ****/
}
