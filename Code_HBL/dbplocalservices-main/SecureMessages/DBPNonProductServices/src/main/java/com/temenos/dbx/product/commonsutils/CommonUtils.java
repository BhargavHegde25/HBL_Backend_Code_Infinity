package com.temenos.dbx.product.commonsutils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Iterator;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.ServicesManagerHelper;

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
    
}
