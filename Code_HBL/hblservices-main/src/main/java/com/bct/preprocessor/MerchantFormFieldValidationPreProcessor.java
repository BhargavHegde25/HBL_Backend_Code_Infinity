package com.bct.preprocessor;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bct.custom.constants.HBLConstants;
import com.bct.custom.exceptions.MerchantValidationException;
import com.dbp.core.constants.DBPConstants;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLFinder;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.utils.InfinityConstants;


public class MerchantFormFieldValidationPreProcessor implements DataPreProcessor2{
	public static LoggerUtil logger = new LoggerUtil(MerchantFormFieldValidationPreProcessor.class);
	public static String validationRulesContent =null;
	public static JSONArray  validationRules= null;
	public static String customerName=null;
	public static final String validationFileName="MerchantsFormFiledValidator.json";
	public static final String cipsApiUser=EnvironmentConfigurationsHandler.getServerProperty("CONNECTIPS_API_USER");//"HBL@999";
	public static final String cips_cert_path=EnvironmentConfigurationsHandler.getServerProperty("CONNECTIPS_CERT_PATH");//"C:\\HBL-Certificate\\Certificate\\HBL.pfx";
	public static final String cips_cert_password = "123";
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		validationRulesContent=readJsonFile();
		validationRules = new JSONArray(validationRulesContent);
		boolean isValidFields=true;
		JSONArray errorArray = new JSONArray();
		String appId=inputMap.get("appId").toString();
		logger.debug("HBL:MerchantFormFieldValidationPreProcessor: appId:"+appId);
		HelperMethods.removeNullValues(inputMap);
		List requestPayload = new ArrayList<>(inputMap.keySet());
		customerName = (String) request.getServicesManager().getIdentityHandler().getUserAttributes().get(InfinityConstants.FirstName);
		logger.debug("HBL:MerchantFormFieldValidationPreProcessor:actual validationRules Size:"+validationRules.length());
			 for(int j=0; j<validationRules.length();j++) {
					 if(validationRules.getJSONObject(j).getString("appId").equals(appId)){  
					  Map rulesMap=validationRules.getJSONObject(j).toMap();
					 logger.debug("HBL:MerchantFormFieldValidationPreProcessor:rulesMap"+rulesMap.toString());
					 for(int i=0;i<requestPayload.size();i++) {
						 if(rulesMap.get("fieldName").equals(requestPayload.get(i))){
					 //if(rulesMap.containsValue(requestPayload.get(i))){
					 logger.debug("HBL:MerchantFormFieldValidationPreProcessor:The Request is available in validationRules:"+validationRules.get(j));
					 try {
						validateRequest(j,requestPayload.get(i).toString(), inputMap); 
						} catch (MerchantValidationException e) {
							JSONObject errorObject = new JSONObject();
							logger.error("HBL:MerchantFormFieldValidationPreProcessor:validateRequest:errMsg"+e.getCustomMessage());
							result.addParam(new Param("dbpErrCode", e.getErrorCode()));
							result.addParam(new Param("dbpErrMsg",e.getCustomMessage()));
							errorObject.put("dbpErrMsg", e.getCustomMessage());
							errorObject.put("dbpErrCode", e.getErrorCode());
							errorArray.put(errorObject);
							isValidFields= false;
						}
				 	}
				 }
			 	  }
		 		}
		logger.debug("HBL:MerchantFormFieldValidationPreProcessor:is Validation Rules Passed:"+isValidFields);
		 if(!isValidFields) {
			 logger.error("HBL:MerchantFormFieldValidationPreProcessor:errorArray:"+errorArray);
			 Dataset ds = constructDatasetFromJSONArray(errorArray);
			 ds.setId("errorObj");
			 result.addDataset(ds);
		 }
		 else if(isValidFields) {
			 generateToken(inputMap, request);
			 request.setAttribute("lodgeBillpayRequest", inputMap);
			 request.addRequestParam_("lodgeBillpayRequest", inputMap.toString());
		 }
		 logger.debug("HBL:MerchantFormFieldValidationPreProcessor:final inputMap:"+inputMap);
		return isValidFields;
	}
	public static boolean validateRequest(int index, String requestParam, HashMap inputMap)
			throws MerchantValidationException {
		boolean isValid = true;
		String errMsg = "";
		String input = inputMap.get(requestParam) != null ? inputMap.get(requestParam).toString() : "";
		logger.debug("HBL:MerchantFormFieldValidationPreProcessor:validateRequest:requestParam:"+requestParam);
		logger.debug("HBL:MerchantFormFieldValidationPreProcessor:validateRequest:input:"+input);
		/*
		 * NULL validations
		 */
		if (StringUtils.isBlank(input)) {
			logger.error("HBL:MerchantFormFieldValidationPreProcessor:validateRequest:input:INVALID_INPUT");
			throw new MerchantValidationException(2001, HBLConstants.INVALID_INPUT);
		}
		String fieldName = "";
		if (validationRules != null) {
			JSONObject validationRule = validationRules.getJSONObject(index);
			fieldName = requestParam;//validationRule.has("fieldName") ? validationRule.get("fieldName").toString() : "";
			String ruleEngineDataType = validationRule.has("dataType") ? validationRule.get("dataType").toString() : "";
			String ruleLengthValue = validationRule.has("length") ? validationRule.get("length").toString() : "";
			String ruleMinLengthValue = validationRule.has("minLength") ? validationRule.get("minLength").toString(): "";
			String ruleEngineInputFormat=validationRule.has("inputFormat") ? validationRule.get("inputFormat").toString(): "";
			/**
			 * MIN Length Validations for all other types except C (currency=amount)
			 */
			//System.out.println("fieldName:"+fieldName);
			if (validationRule.has("minLength")) {
					Integer ruleLength = 0;
					ruleLength = Integer.valueOf(ruleMinLengthValue);
					isValid = input.length() >= ruleLength;
					errMsg = !isValid ? HBLConstants.INVALID_MIN_LENGTH + " of " + fieldName : "";
			}
			if (!isValid) {
				logger.error("HBL:MerchantFormFieldValidationPreProcessor:validateRequest:errMsg:" + errMsg);
				throw new MerchantValidationException(20002, errMsg);
			}
			/**
			 * MAX Length Validations for all other types except C (currency=amount)
			 */
			if (validationRule.has("length") ) {
				Integer ruleLength = 0;
					ruleLength = Integer.valueOf(ruleLengthValue);
					isValid = input.length() <= ruleLength;
					errMsg = !isValid ? HBLConstants.INVALID_MAX_LENGTH + " of " + fieldName : "";
			}
			if (!isValid) {
				logger.error("HBL:MerchantFormFieldValidationPreProcessor:validateRequest:errMsg:" + errMsg);
				throw new MerchantValidationException(20003, errMsg);
			}
			
			/*
			 * Date field Validation
			 * 
			 */
			if( validationRule.get("inputFormat")!=null && StringUtils.isNotBlank(validationRule.getString("inputFormat"))) {
				try {
				String newDate=isValidDate(input, ruleEngineInputFormat);
				System.out.println("newDate:"+newDate);
				}catch (Exception e) {
					logger.error("HBL:MerchantFormFieldValidationPreProcessor:validateRequest:date:"+e.getLocalizedMessage());
					throw new MerchantValidationException(20007,HBLConstants.INVALID_DATE);
				}
			}
			if (validationRule.get("dataType")!=null && StringUtils.isNotBlank(validationRule.getString("dataType"))) {
				/**
				 * Amount Validations
				 */
				if (ruleEngineDataType.equalsIgnoreCase("C") || ruleEngineDataType.equalsIgnoreCase("BigDecimal")) {
					try {
						BigDecimal bDecimal = BigDecimal.valueOf(Double.valueOf(input)).setScale(2);
						Integer ruleDecimalLength = Integer.valueOf(ruleLengthValue.split(",")[0]);
						Integer inputDecimalLength = bDecimal.toPlainString().split("[\\.]")[0].length();
						if (ruleDecimalLength != null) {
							isValid = inputDecimalLength <= ruleDecimalLength;
							errMsg = !isValid ? HBLConstants.INVALID_AMOUNT_RANGE + " of " + fieldName : "";
						}
					} catch (Exception e) {
						logger.error("HBL:MerchantFormFieldValidationPreProcessor:validateRequest:errMsg:"
								+ e.getLocalizedMessage());
						isValid = false;
						errMsg = !isValid ? HBLConstants.INVALID + " " + fieldName : "";
					}
				}
				/*if (ruleEngineDataType.equalsIgnoreCase("C")) {
					try {
						BigDecimal bDecimal = BigDecimal.valueOf(Double.valueOf(input)).setScale(2);
						Integer ruleDecimalLength = Integer.valueOf(ruleLengthValue.split(",")[0]);
						Integer inputDecimalLength = bDecimal.toPlainString().split("[\\.]")[0].length();
						if (ruleDecimalLength != null) {
							isValid = inputDecimalLength <= ruleDecimalLength;
							errMsg = !isValid ? HBLConstants.INVALID_AMOUNT_RANGE + " of " + fieldName : "";
						}
					} catch (Exception e) {
						logger.error("HBL:MerchantFormFieldValidationPreProcessor:validateRequest:errMsg:"
								+ e.getLocalizedMessage());
						isValid = false;
						errMsg = !isValid ? HBLConstants.INVALID + " " + fieldName : "";
					}
				}
				*/
				if (!isValid) {
					logger.error("HBL:MerchantFormFieldValidationPreProcessor:validateRequest:errMsg:" + errMsg);
					throw new MerchantValidationException(20004, errMsg);

				}
				/**
				 * Mobile Number Validations
				 */
				if (ruleEngineDataType.equalsIgnoreCase("M")) {
					String pattern = "\\d{10}";
					isValid = input.matches(pattern);
					errMsg = !isValid ? HBLConstants.INVALID + " " + fieldName : "";
				}
				if (!isValid) {
					logger.error("HBL:MerchantFormFieldValidationPreProcessor:validateRequest:errMsg:" + errMsg);
					throw new MerchantValidationException(20005, errMsg);
				}
				/**
				 * email Validations
				 */
				if (ruleEngineDataType.equalsIgnoreCase("E")) {
					String pattern = "^(.+)@(.+)$";
					isValid = input.matches(pattern);
					errMsg = !isValid ? HBLConstants.INVALID + " " + fieldName : "";
				}
				if (!isValid) {
					logger.error("HBL:MerchantFormFieldValidationPreProcessor:validateRequest:errMsg:" + errMsg);
					throw new MerchantValidationException(20006, errMsg);
				}
				/**
				 * Number/Integer Validations
				 */
				
				if (ruleEngineDataType.equalsIgnoreCase("N")) {
					try {
					BigInteger bInteger = BigInteger.valueOf(Integer.valueOf(input));
					}catch (Exception e) {
						logger.error("HBL:MerchantFormFieldValidationPreProcessor:validateRequest:errMsg:"
								+ e.getLocalizedMessage());
						isValid = false;
						errMsg = !isValid ? HBLConstants.INVALID + " " + fieldName : "";
					}
				}
				if (!isValid) {
					logger.error("HBL:MerchantFormFieldValidationPreProcessor:validateRequest:errMsg:" + errMsg);
					throw new MerchantValidationException(20007, errMsg);
				}

			}
		}
		return isValid;

	}
	public static String isValidDate(String date,String format) throws Exception {
		format=format.replaceAll("y", "u").replaceAll("m", "M");
		String newDate=null;
		
		  LocalDate ld = LocalDate.parse(date,
                  DateTimeFormatter.ofPattern(format)
                          .withResolverStyle(ResolverStyle.STRICT)
          );
		  newDate=ld.toString();
		  
		 return newDate;
	}
	 public static String readJsonFile() {
	        String arrayContent="[]";
	        try (InputStream is = URLFinder.class.getClassLoader().getResourceAsStream(validationFileName);
	                BufferedReader reader = new BufferedReader(new InputStreamReader(is));) {
	    	    	 JsonElement array = JsonParser.parseReader(reader);
	    	    	 arrayContent= array.getAsJsonArray().toString();
	    	    }
	    	    catch (FileNotFoundException e) {                                  
	    	    	logger.error("Exception Occured in HBL:MerchantFormFieldValidationPreProcessor:readJsonFile:error:" + e.getMessage());
	    	     } catch (IOException e) {                                    
	    	    	 logger.error("Exception Occured in HBL:MerchantFormFieldValidationPreProcessor:readJsonFile:error:" + e.getMessage());
	    	      }catch (Exception e) {
	    	    	  logger.error("Exception Occured in HBL:MerchantFormFieldValidationPreProcessor:readJsonFile:error:" + e.getMessage());
	    		}
	        return arrayContent;
	    }
	public static HashMap generateToken(HashMap inputMap, DataControllerRequest request) {
		prepareBatchPayload(inputMap, request);
		String batchString=generateBatchString(inputMap);
		String txString=generateTransactionString(inputMap);
		String tokenString=generateTokenString(batchString, txString, cipsApiUser);
		logger.debug("HBL:MerchantFormFieldValidationPreProcessor:token String:"+tokenString);
		String contentToEncode = tokenString;
		try {
			String encodeContent = HBLTxnToken(contentToEncode);
			inputMap.put("token", encodeContent);
			logger.debug("HBL:MerchantFormFieldValidationPreProcessor:token:"+encodeContent);
		} catch (Exception e) {
			logger.error("Exception occured while generating HBLTxnToken:"+e.getLocalizedMessage());
		}
		return inputMap;
	 }
	public static void main(String[] args) {
		HashMap inputMap = new HashMap();
		inputMap.put("debtorAccount", "124567");
		inputMap.put("debtorBranch", "1");
		inputMap.put("debtorName", "Lokeswararao");
		inputMap.put("appId", "MER-287-APP-2");
		inputMap.put("refId", "janaki");
		inputMap.put("remarks", "test address");
		inputMap.put("addenda3", "test");
		inputMap.put("particulars", "8186995299");
		inputMap.put("amount", "300");
		inputMap=generateToken(inputMap, null);
		System.out.println("HBL:MerchantFormFieldValidationPreProcessor:final inputMap:"+inputMap);
		String validationRulesContent = readJsonFile();
		JSONArray validationRulesArray = new JSONArray(validationRulesContent);
		
		JSONArray errorArray = new JSONArray();
		boolean isValidFields= true;
		HelperMethods.removeNullValues(inputMap);
		List requestPayload = new ArrayList<>(inputMap.keySet());
		Result result = new Result();
		String appId=inputMap.get("appId").toString();
		 
		for(int j=0; j<validationRulesArray.length();j++) {
			 if(validationRulesArray.getJSONObject(j).getString("appId").equals(appId)){  
			  Map rulesMap=validationRulesArray.getJSONObject(j).toMap();
			 for(int i=0;i<requestPayload.size();i++) {
				 if(rulesMap.get("fieldName").equals(requestPayload.get(i))){
					 //System.out.println("HBL:MerchantFormFieldValidationPreProcessor:The Request is available in validationRulesArray:"+validationRulesArray.get(j));
			 try {
				validateRequest(j,requestPayload.get(i).toString(), inputMap); 
				} catch (MerchantValidationException e) {
					JSONObject errorObject = new JSONObject();
					result.addParam(new Param("dbpErrCode", e.getErrorCode()));
					result.addParam(new Param("dbpErrMsg",e.getCustomMessage()));
					errorObject.put("dbpErrMsg", e.getCustomMessage());
					errorObject.put("dbpErrCode", e.getErrorCode());
					errorArray.put(errorObject);
					isValidFields= false;
				}
		 	}
		 }
	 	  }
		}
						 if(!isValidFields) {
							 System.out.println("errorArray:"+errorArray);
							 Dataset ds = constructDatasetFromJSONArray(errorArray);
							 ds.setId("errorObj");
							 result.addDataset(ds);
						 }
						 System.out.println("isValidFields:"+isValidFields);
							 
			}
	
	public static Record constructRecordFromJSONObject(JSONObject JSONObject) {
		Record response = new Record();
		if (JSONObject == null || JSONObject.length() == 0) {
			return response;
		}
		Iterator<String> keys = JSONObject.keys();

		while (keys.hasNext()) {
			String key = keys.next();
			if (JSONObject.get(key) instanceof String) {
				Param param = new Param(key, JSONObject.getString(key), DBPConstants.FABRIC_STRING_CONSTANT_KEY);
				response.addParam(param);

			} else if (JSONObject.get(key) instanceof Integer) {
				Param param = new Param(key, JSONObject.get(key).toString(), DBPConstants.FABRIC_INT_CONSTANT_KEY);
				response.addParam(param);

			} else if (JSONObject.get(key) instanceof Boolean) {
				Param param = new Param(key, JSONObject.get(key).toString(), DBPConstants.FABRIC_BOOLEAN_CONSTANT_KEY);
				response.addParam(param);

			} else if (JSONObject.get(key) instanceof JSONArray) {
				Dataset dataset = constructDatasetFromJSONArray(JSONObject.getJSONArray(key));
				dataset.setId(key);
				response.addDataset(dataset);
			}
		}
		return response;
	}
	public static Dataset constructDatasetFromJSONArray(JSONArray JSONArray) {
		Dataset dataset = new Dataset();
		for (int count = 0; count < JSONArray.length(); count++) {
			Record record = constructRecordFromJSONObject((JSONObject) JSONArray.get(count));
			dataset.addRecord(record);
		}
		return dataset;
	}
	public static String HBLTxnToken(String contentToEncode) throws Exception {
		String certPath = cips_cert_path;
        byte[] certStore = Files.readAllBytes(Paths.get(certPath));
        
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
       
		keyStore.load(new ByteArrayInputStream(certStore), cips_cert_password.toCharArray());
        
        String alias = keyStore.aliases().nextElement();
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, cips_cert_password.toCharArray());
        
        byte[] utf8Data = contentToEncode.getBytes("UTF-8");
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(utf8Data);
        
        byte[] signedData = signature.sign();
        return Base64.getEncoder().encodeToString(signedData);
    }
	public static String generateBatchString(HashMap payload){
		String batchId= payload.get("batchId").toString();
		String debtorAgent= payload.get("debtorAgent").toString();
		String debtorAccount= payload.get("debtorAccount")!=null?payload.get("debtorAccount").toString():"";
		String debtorBranch= payload.get("debtorBranch").toString();
		String batchAmount= payload.get("batchAmount")!=null?payload.get("batchAmount").toString():"";
		String batchCrncy= payload.get("batchCrncy").toString();
		
		String batchString=batchId+","+debtorAgent+","+debtorBranch+","+debtorAccount+","+batchAmount+","+batchCrncy;
		
		return batchString;
		
	}
	public static String generateTransactionString(HashMap payload){
		String instructionId= payload.get("instructionId").toString();
		String appId= payload.get("appId")!=null?payload.get("appId").toString():"";
		String refId= payload.get("refId")!=null?payload.get("refId").toString():"";
		
		String TxString=instructionId+","+appId+","+refId;
		
		return TxString;
		
	}
	public static String generateTokenString(String batchString,String txString, String userId){
	
		
		String token=batchString+","+txString+","+userId;
		
		return token;
		
	}
	public static HashMap  prepareBatchPayload(HashMap inputMap, DataControllerRequest dcRequest) {
		String randomid=String.valueOf(generateBatchId());
		inputMap.put("batchId", randomid);
		inputMap.put("instructionId", randomid+"-1");
		inputMap.put("endToEndId", randomid);
		if(inputMap.get("amount")!=null)
		inputMap.put("batchAmount", inputMap.get("amount"));
		inputMap.put("batchCount", HBLConstants.CIPS_BATCH_COUNT);
		inputMap.put("batchCrncy", HBLConstants.CIPS_BATCH_CURRENCY);
		inputMap.put("categoryPurpose", HBLConstants.CIPS_CATEGORY_PURPOSE);
		inputMap.put("debtorAgent", HBLConstants.CIPS_DEBTOR_AGENT);
		inputMap.put("debtorBranch", HBLConstants.CIPS_DEBTOR_BRANCH);
		if(customerName!=null && StringUtils.isNotBlank(customerName)) {
		inputMap.put("debtorName", customerName);	
		}
		if(inputMap.get("debtorAccount")!=null)
		inputMap.put("debtorAccount", inputMap.get("debtorAccount"));
		/*
		if(inputMap.get("appId")!=null)
		inputMap.put("appId", inputMap.get("appId"));
		if(inputMap.get("refId")!=null)
		inputMap.put("refId", inputMap.get("refId"));
		if(inputMap.get("amount")!=null)
		inputMap.put("amount", inputMap.get("amount"));
		if(inputMap.get("freeCode1")!=null)
		inputMap.put("freeCode1", inputMap.get("freeCode1"));
		*/
		
		/* OPTIONAL FIELDS
		inputMap.put("debtorIdType", "0001");
		inputMap.put("debtorIdValue", "123456");
		inputMap.put("debtorAddress", "Kathmandu Nepal");
		inputMap.put("debtorPhone", "+977-01-4255306");
		inputMap.put("debtorMobile", "+977-9812345678");
		inputMap.put("debtorEmail", "test@test.com");
		*/
		
		return inputMap;
		
	}
	public static String generateBatchId(){
		String batchId = "HBL" + "-" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		return batchId;
	}
	private static int generateRandomID() {
        SecureRandom rand = new SecureRandom();
        return (int) (100000 + (rand.nextFloat() * 900000000));
    }

}
