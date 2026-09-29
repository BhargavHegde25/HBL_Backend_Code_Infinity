package com.bct.preprocessor;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bct.custom.constants.HBLConstants;
import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.utils.InfinityConstants;

public class ConnectIPSOtherbankTransferPreProcessor implements DataPreProcessor2{
	public static LoggerUtil logger = new LoggerUtil(ConnectIPSOtherbankTransferPreProcessor.class);
	public static String customerName=null;
	public  Boolean isValidFields=null;
	public  JSONArray errorArray = null;
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response, Result result)
			throws Exception {
		logger.debug("HBL:ConnectIPSOtherbankTransferPreProcessor: inputMap:"+inputMap);
		errorArray= new JSONArray();
		isValidFields= true;
	try{
		//customerName = (String) request.getServicesManager().getIdentityHandler().getUserAttributes().get(InfinityConstants.FirstName);
		validatePayload(inputMap); 
		if(isValidFields==true) {
		generateToken(inputMap, request);
		}
		else {
			logger.error("HBL:ConnectIPSOtherbankTransferPreProcessor:errorArray:"+errorArray);
			 Dataset ds = constructDatasetFromJSONArray(errorArray);
			 ds.setId("errorObj");
			 result.addDataset(ds);
			 isValidFields=false;
		  }
		}
	catch (Exception e) {
			logger.error("Exception occured while generating HBLTxnToken:"+e.getLocalizedMessage());
			JSONObject errorObject = new JSONObject();
			errorObject.put("dbpErrMsg", e.getLocalizedMessage());
			errorObject.put("dbpErrCode", e.toString());
			errorArray.put(errorObject);
			Dataset ds = constructDatasetFromJSONArray(errorArray);
			 ds.setId("errorObj");
			 result.addDataset(ds);
			 isValidFields=false;
		}
	logger.debug("HBL:ConnectIPSOtherbankTransferPreProcessor:Final input params:"+inputMap);
	return isValidFields;
	}
	public HashMap generateToken(HashMap inputMap, DataControllerRequest request) throws Exception {
		prepareBatchPayload(inputMap, request);
		String batchString=generateBatchString(inputMap);
		String txString=generateTransactionString(inputMap);
		String cipsApiUser=EnvironmentConfigurationsHandler.getServerProperty("CONNECTIPS_API_USER");//"HBL@999";
		logger.debug("HBL:ConnectIPSOtherbankTransferPreProcessor:generateToken:cipsApiUser :"+cipsApiUser);
		String tokenString=generateTokenString(batchString, txString, cipsApiUser);
		logger.debug("HBL:ConnectIPSOtherbankTransferPreProcessor:token String:"+tokenString);
		String contentToEncode = tokenString;
			String encodeContent = HBLTxnToken(contentToEncode);
			inputMap.put("token", encodeContent);
			logger.debug("HBL:ConnectIPSOtherbankTransferPreProcessor:token:"+encodeContent);
		return inputMap;
	 }
	public String HBLTxnToken(String contentToEncode) throws Exception {
		String cips_cert_path= EnvironmentConfigurationsHandler.getServerProperty("NPI_PFX_FILE_PATH");  //"C:\\HBL-Certificate\\Certificate\\HBL.pfx";
		String cips_cert_password = EnvironmentConfigurationsHandler.getServerProperty("NPI_PFX_FILE_PASSWORD"); //"123";
		logger.debug("HBL:ConnectIPSOtherbankTransferPreProcessor:HBLTxnToken:cips_cert_path :"+cips_cert_path);
		logger.debug("HBL:ConnectIPSOtherbankTransferPreProcessor:HBLTxnToken:cips_cert_password :"+cips_cert_password);
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
	public void validatePayload(HashMap inputMap) {
		HelperMethods.removeNullValues(inputMap);
		String amount=inputMap.get("amount")!=null?inputMap.get("amount").toString():"";
		String debtorAgent=inputMap.get("debtorAgent")!=null?inputMap.get("debtorAgent").toString():"";
		String debtorBranch=inputMap.get("debtorBranch")!=null?inputMap.get("debtorBranch").toString():"";
		String debtorName=inputMap.get("debtorName")!=null?inputMap.get("debtorName").toString():"";
		if(StringUtils.isBlank(debtorName)) {
			if(StringUtils.isNotBlank(customerName)) {
				debtorName=customerName;
				inputMap.put("debtorName", customerName);	
			}
		}
		String debtorAccount=inputMap.get("debtorAccount")!=null?inputMap.get("debtorAccount").toString():"";
		
		String creditorAgent=inputMap.get("creditorAgent")!=null?inputMap.get("creditorAgent").toString():"";
		String creditorBranch=inputMap.get("creditorBranch")!=null?inputMap.get("creditorBranch").toString():"";
		if(StringUtils.isBlank(creditorBranch)) {
			inputMap.put("creditorBranch", HBLConstants.CIPS_CREDITOR_BRANCH);	
		}
		String creditorName=inputMap.get("creditorName")!=null?inputMap.get("creditorName").toString(): "";
		String creditorAccount=inputMap.get("creditorAccount")!=null?inputMap.get("creditorAccount").toString():"";
		if(StringUtils.isBlank(amount)) {
				JSONObject errorObject = new JSONObject();
				logger.error("HBL:ConnectIPSOtherbankTransferPreProcessor:validatePayload:invalid amount:"+amount);
				errorObject.put("dbpErrMsg", HBLConstants.INVALID_AMOUNT);
				errorObject.put("dbpErrCode", HBLConstants.INVALID_AMOUNT);
				errorArray.put(errorObject);
				isValidFields= false;
		}
		if(StringUtils.isBlank(debtorAgent)) {
			JSONObject errorObject = new JSONObject();
			logger.error("HBL:ConnectIPSOtherbankTransferPreProcessor:validatePayload:invalid debtorAgent:"+debtorAgent);
			errorObject.put("dbpErrMsg", HBLConstants.INVALID_DEBTOR_AGENT);
			errorObject.put("dbpErrCode", HBLConstants.INVALID_DEBTOR_AGENT);
			errorArray.put(errorObject);
			isValidFields= false;
		}
		if(StringUtils.isBlank(debtorBranch)) {
			JSONObject errorObject = new JSONObject();
			logger.error("HBL:ConnectIPSOtherbankTransferPreProcessor:validatePayload:invalid debtorBranch:"+debtorBranch);
			errorObject.put("dbpErrMsg", HBLConstants.INVALID_DEBTOR_BRANCH);
			errorObject.put("dbpErrCode", HBLConstants.INVALID_DEBTOR_BRANCH);
			errorArray.put(errorObject);
			isValidFields= false;
		}
		if(StringUtils.isBlank(debtorName)) {
			JSONObject errorObject = new JSONObject();
			logger.error("HBL:ConnectIPSOtherbankTransferPreProcessor:validatePayload:invalid debtor name:"+debtorName);
			errorObject.put("dbpErrMsg", HBLConstants.INVALID_DEBTOR_NAME);
			errorObject.put("dbpErrCode", HBLConstants.INVALID_DEBTOR_NAME);
			errorArray.put(errorObject);
			isValidFields= false;
		}
		if(StringUtils.isBlank(debtorAccount)) {
			JSONObject errorObject = new JSONObject();
			logger.error("HBL:ConnectIPSOtherbankTransferPreProcessor:validatePayload:invalid debtorBranch:"+debtorAccount);
			errorObject.put("dbpErrMsg", HBLConstants.INVALID_DEBTOR_ACCOUNT);
			errorObject.put("dbpErrCode", HBLConstants.INVALID_DEBTOR_ACCOUNT);
			errorArray.put(errorObject);
			isValidFields= false;
		}
		
		if(StringUtils.isBlank(creditorAgent)) {
			JSONObject errorObject = new JSONObject();
			logger.error("HBL:ConnectIPSOtherbankTransferPreProcessor:validatePayload:invalid creditorAgent:"+creditorAgent);
			errorObject.put("dbpErrMsg", HBLConstants.INVALID_CREDTOR_AGENT);
			errorObject.put("dbpErrCode", HBLConstants.INVALID_CREDTOR_AGENT);
			errorArray.put(errorObject);
			isValidFields= false;
		}
		if(StringUtils.isBlank(creditorBranch)) {
			JSONObject errorObject = new JSONObject();
			logger.error("HBL:ConnectIPSOtherbankTransferPreProcessor:validatePayload:invalid creditorBranch:"+creditorBranch);
			errorObject.put("dbpErrMsg", HBLConstants.INVALID_CREDTOR_BRANCH);
			errorObject.put("dbpErrCode", HBLConstants.INVALID_CREDTOR_BRANCH);
			errorArray.put(errorObject);
			isValidFields= false;
		}
		if(StringUtils.isBlank(creditorName)) {
			JSONObject errorObject = new JSONObject();
			logger.error("HBL:ConnectIPSOtherbankTransferPreProcessor:validatePayload:invalid creditorName:"+creditorName);
			errorObject.put("dbpErrMsg", HBLConstants.INVALID_CREDTOR_NAME);
			errorObject.put("dbpErrCode", HBLConstants.INVALID_CREDTOR_NAME);
			errorArray.put(errorObject);
			isValidFields= false;
		}
		if(StringUtils.isBlank(creditorAccount)) {
			JSONObject errorObject = new JSONObject();
			logger.error("HBL:ConnectIPSOtherbankTransferPreProcessor:validatePayload:invalid creditorAccount:"+creditorAccount);
			errorObject.put("dbpErrMsg", HBLConstants.INVALID_CREDTOR_ACCOUNT);
			errorObject.put("dbpErrCode", HBLConstants.INVALID_CREDTOR_ACCOUNT);
			errorArray.put(errorObject);
			isValidFields= false;
		}
		logger.debug("HBL:ConnectIPSOtherbankTransferPreProcessor:validatePayload:isValidFields:"+isValidFields);
	}
	public HashMap  prepareBatchPayload(HashMap inputMap, DataControllerRequest dcRequest) {
		String randomid=String.valueOf(generateBatchId());
		
		inputMap.put("batchId", randomid);
		inputMap.put("batchAmount", inputMap.get("amount"));
		inputMap.put("batchCount", HBLConstants.CIPS_BATCH_COUNT);
		inputMap.put("batchCrncy", inputMap.get("currency")!=null?inputMap.get("currency").toString(): HBLConstants.CIPS_BATCH_CURRENCY);
		inputMap.put("categoryPurpose", HBLConstants.CIPS_CATEGORY_PURPOSE);
		inputMap.put("debtorAgent", inputMap.get("debtorAgent")!=null?inputMap.get("debtorAgent").toString(): HBLConstants.CIPS_DEBTOR_AGENT);
		inputMap.put("debtorBranch", inputMap.get("debtorBranch")!=null?inputMap.get("debtorBranch").toString(): HBLConstants.CIPS_DEBTOR_BRANCH);
		inputMap.put("debtorName", inputMap.get("debtorName"));	
		inputMap.put("debtorAccount", inputMap.get("debtorAccount"));
		
		inputMap.put("purpose", HBLConstants.CIPS_PURPOSE);
		inputMap.put("instructionId", randomid+"-1");
		inputMap.put("endToEndId", randomid);
		inputMap.put("amount", inputMap.get("amount"));
		inputMap.put("creditorAgent",inputMap.get("creditorAgent").toString());
		inputMap.put("creditorBranch",inputMap.get("creditorBranch").toString());
		inputMap.put("creditorName",inputMap.get("creditorName").toString());
		inputMap.put("creditorAccount",inputMap.get("creditorAccount").toString());
		inputMap.put("addenda4",inputMap.get("remarks")!=null?inputMap.get("remarks").toString():"");
		
		
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
	public String generateBatchString(HashMap payload){
		String batchId= payload.get("batchId").toString();
		String batchAmount= payload.get("batchAmount").toString();
		String batchCount= payload.get("batchCount").toString();
		String batchCrncy= payload.get("batchCrncy").toString();
		String debtorAgent= payload.get("debtorAgent").toString();
		String debtorBranch= payload.get("debtorBranch").toString();
		String debtorName=payload.get("debtorName").toString();
		String debtorAccount= payload.get("debtorAccount").toString();
		String paymentType= payload.get("paymentType")!=null? payload.get("paymentType").toString():"";
		String categoryPurpose= payload.get("categoryPurpose")!=null? payload.get("categoryPurpose").toString():"";
		String batchString=batchId+","+debtorAgent+","+debtorBranch+","+debtorAccount+","+batchAmount+","+batchCrncy;
		if(StringUtils.isNotBlank(paymentType) && paymentType.equalsIgnoreCase("IPS")) {
			batchString=batchString+","+categoryPurpose;
		}
		
		return batchString;
		
	}
	public String generateTransactionString(HashMap payload){
		String instructionId= payload.get("instructionId").toString();
		String endToEndId= payload.get("endToEndId").toString();
		String amount= payload.get("amount").toString();
		String creditorAgent= payload.get("creditorAgent").toString();
		String creditorBranch= payload.get("creditorBranch").toString();
		String creditorName= payload.get("creditorName").toString();
		String creditorAccount= payload.get("creditorAccount").toString();
		
		String TxString=instructionId+","+creditorAgent+","+creditorBranch+","+creditorAccount+","+amount;
		//String TxString=instructionId;
		return TxString;
		
	}
	public String generateTokenString(String batchString,String txString, String userId){
	
		
		String token=batchString+","+txString+","+userId;
		
		return token;
		
	}
	public String generateBatchId(){
		String batchId = "HBL" + "-" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		return batchId;
	}
	private  int generateRandomID() {
        SecureRandom rand = new SecureRandom();
        return (int) (100000 + (rand.nextFloat() * 900000000));
    }
	public  Record constructRecordFromJSONObject(JSONObject JSONObject) {
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
	public  Dataset constructDatasetFromJSONArray(JSONArray JSONArray) {
		Dataset dataset = new Dataset();
		for (int count = 0; count < JSONArray.length(); count++) {
			Record record = constructRecordFromJSONObject((JSONObject) JSONArray.get(count));
			dataset.addRecord(record);
		}
		return dataset;
	}
	

}
