package com.hbl.productservicesExtn.utills;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.hbl.productservicesExtn.constants.HBLConstants;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.controller.DataControllerRequest;

public class GenerateNCHLPayload {
	public static LoggerUtil logger = new LoggerUtil(GenerateNCHLPayload.class);
	
	public static HashMap<String, Object> prepareOtherBankTransferPayload(HashMap inputMap, DataControllerRequest request) throws ApplicationException, Exception {
		HashMap<String, Object> requestInputMap = new HashMap<String, Object>();
		validatePayload(inputMap);
		preProcessPayload(inputMap, request);
		String token=generateToken(inputMap, request);
		JSONObject batchDetails = generateBatchPayload(inputMap);
		JSONArray transactionDetailList = generateCipsTransactionDetailList(inputMap);
			if (inputMap.get("paymentType")!=null && inputMap.get("paymentType").toString().equalsIgnoreCase("IPS")) {
			requestInputMap.put("nchlIpsBatchDetail", batchDetails);
			requestInputMap.put("nchlIpsTransactionDetailList", transactionDetailList);
			requestInputMap.put("token", token);
			requestInputMap.put("paymentType", "IPS");
			requestInputMap.put("paymentURL", "api/postnchlipsbatch");
			}else {
			requestInputMap.put("cipsBatchDetail", batchDetails);
			requestInputMap.put("cipsTransactionDetailList", transactionDetailList);
			requestInputMap.put("token", token);
			requestInputMap.put("paymentType", "CIPS");
			requestInputMap.put("paymentURL", "api/postcipsbatch");
			}
		return requestInputMap;
		
	}
	public static String generateToken(HashMap inputMap, DataControllerRequest request) throws Exception {
		String batchString=generateBatchString(inputMap);
		String txString=generateTransactionString(inputMap);
		String cipsApiUser=EnvironmentConfigurationsHandler.getServerProperty("CONNECTIPS_API_USER");//"HBL@999";
		logger.debug("HBL:GenerateNCHLPayload:generateToken:cipsApiUser :"+cipsApiUser);
		String tokenString=generateTokenString(batchString, txString, cipsApiUser);
		logger.debug("HBL:GenerateNCHLPayload:token String:"+tokenString);
		String contentToEncode = tokenString;
			String encodeContent = HBLTxnToken(contentToEncode);
			logger.debug("HBL:GenerateNCHLPayload:token:"+encodeContent);
		return encodeContent;
	 }
	public static HashMap preProcessPayload(HashMap inputMap, DataControllerRequest dcRequest) {
		String randomid=generateBatchId();
		inputMap.put("batchId", randomid);
		inputMap.put("batchAmount", inputMap.get("amount"));
		inputMap.put("batchCount", HBLConstants.CIPS_BATCH_COUNT);
		inputMap.put("categoryPurpose", HBLConstants.CIPS_CATEGORY_PURPOSE);
		inputMap.put("currency", inputMap.get("currency")!=null?inputMap.get("currency").toString(): HBLConstants.CIPS_BATCH_CURRENCY);
		inputMap.put("batchCrncy", inputMap.get("currency")!=null?inputMap.get("currency").toString(): HBLConstants.CIPS_BATCH_CURRENCY);
		inputMap.put("debtorAgent", inputMap.get("debtorAgent")!=null?inputMap.get("debtorAgent").toString(): HBLConstants.CIPS_DEBTOR_AGENT);
		inputMap.put("debtorBranch", inputMap.get("debtorBranch")!=null?inputMap.get("debtorBranch").toString(): HBLConstants.CIPS_DEBTOR_BRANCH);
		inputMap.put("debtorName", inputMap.get("debtorName"));	
		inputMap.put("debtorAccount", inputMap.get("debtorAccount"));
		
		//inputMap.put("purpose", HBLConstants.CIPS_PURPOSE);
		inputMap.put("instructionId", randomid+"-1");
		inputMap.put("endToEndId", randomid);
		String transactionCurrency=inputMap.get("transactionCurrency")!=null?inputMap.get("transactionCurrency").toString():"";
		inputMap.put("creditorAgent",inputMap.get("creditorAgent").toString());
		inputMap.put("creditorBranch",inputMap.get("creditorBranch").toString());
		inputMap.put("creditorName",inputMap.get("creditorName").toString());
		inputMap.put("creditorAccount",inputMap.get("creditorAccount").toString());
		inputMap.put("addenda4",inputMap.get("remarks")!=null?inputMap.get("remarks").toString():"");
		
		Double amount = Double.valueOf(inputMap.get("amount").toString());
		String domesticTransferThreshould= EnvironmentConfigurationsHandler.getServerProperty("DOMESTIC_TRANSFER_THRESHOULD_LIMIT");
		logger.debug("GenerateNCHLPayload:transactionAmount:"+amount+",domesticTransferThreshould"+domesticTransferThreshould);
		Double domesticTransferThreshouldLimit = Double.valueOf(domesticTransferThreshould);
		inputMap.put("paymentType","CIPS");
		if (amount >= domesticTransferThreshouldLimit)
		inputMap.put("paymentType","IPS");
		return inputMap;
		
	}
	public static String generateBatchString(HashMap payload){
		String batchId= payload.get("batchId").toString();
		String batchAmount= payload.get("batchAmount").toString();
		String batchCrncy= payload.get("batchCrncy").toString();
		String debtorAgent= payload.get("debtorAgent").toString();
		String debtorBranch= payload.get("debtorBranch").toString();
		String debtorAccount= payload.get("debtorAccount").toString();
		String paymentType= payload.get("paymentType")!=null? payload.get("paymentType").toString():"";
		String categoryPurpose= payload.get("categoryPurpose")!=null? payload.get("categoryPurpose").toString():"";
		String batchString=batchId+","+debtorAgent+","+debtorBranch+","+debtorAccount+","+batchAmount+","+batchCrncy;
		if(StringUtils.isNotBlank(paymentType) && paymentType.equalsIgnoreCase("IPS")) {
			batchString=batchString+","+categoryPurpose;
		}
		
		return batchString;
		
	}
	public static String generateTransactionString(HashMap payload){
		String instructionId= payload.get("instructionId").toString();
		String amount= payload.get("amount").toString();
		String creditorAgent= payload.get("creditorAgent").toString();
		String creditorBranch= payload.get("creditorBranch").toString();
		String creditorAccount= payload.get("creditorAccount").toString();
		
		String TxString=instructionId+","+creditorAgent+","+creditorBranch+","+creditorAccount+","+amount;
		//String TxString=instructionId;
		return TxString;
		
	}
	public static String generateTokenString(String batchString,String txString, String userId){
	
		
		String token=batchString+","+txString+","+userId;
		
		return token;
		
	}
	public static String generateBatchId(){
		String randomId=HelperMethods.getUniqueNumericString(16);
		String batchId = "HBL" + "-" +randomId ;
		return batchId;
	}
	public static String HBLTxnToken(String contentToEncode) throws Exception {
		String cips_cert_path= EnvironmentConfigurationsHandler.getServerProperty("NPI_PFX_FILE_PATH");  //"C:\\HBL-Certificate\\Certificate\\HBL.pfx";
		String cips_cert_password = EnvironmentConfigurationsHandler.getServerProperty("NPI_PFX_FILE_PASSWORD"); //"123";
		logger.debug("HBL:GenerateNCHLPayload:HBLTxnToken:cips_cert_path :"+cips_cert_path);
		logger.debug("HBL:GenerateNCHLPayload:HBLTxnToken:cips_cert_password :"+cips_cert_password);
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
	private static JSONObject generateBatchPayload(HashMap inputMap){
		JSONObject batchPayload = new JSONObject();
		batchPayload.put("batchId", inputMap.get("batchId"));
		batchPayload.put("batchAmount", inputMap.get("batchAmount"));
		batchPayload.put("batchCount", inputMap.get("batchCount"));
		batchPayload.put("batchCrncy", inputMap.get("batchCrncy"));
		batchPayload.put("categoryPurpose", inputMap.get("categoryPurpose"));
		batchPayload.put("debtorAgent", inputMap.get("debtorAgent"));
		batchPayload.put("debtorBranch", inputMap.get("debtorBranch"));
		batchPayload.put("debtorName", inputMap.get("debtorName"));	
		batchPayload.put("debtorAccount", inputMap.get("debtorAccount"));
		return batchPayload;
		
	}
	private static JSONArray generateCipsTransactionDetailList(HashMap inputMap){
		JSONArray transactionArray = new JSONArray();
		JSONObject batchPayload = new JSONObject();
		batchPayload.put("instructionId", inputMap.get("instructionId"));
		//batchPayload.put("purpose", HBLConstants.CIPS_PURPOSE);
		batchPayload.put("endToEndId", inputMap.get("endToEndId"));
		batchPayload.put("amount", inputMap.get("amount"));
		batchPayload.put("creditorAgent", inputMap.get("creditorAgent"));
		batchPayload.put("creditorBranch", inputMap.get("creditorBranch"));
		batchPayload.put("creditorName", inputMap.get("creditorName"));
		batchPayload.put("creditorAccount", inputMap.get("creditorAccount"));
		batchPayload.put("addenda4", inputMap.get("remarks")!=null?inputMap.get("remarks").toString():"");	
		transactionArray.put(batchPayload);
		return transactionArray;
	}
	public static void validatePayload(HashMap inputMap) throws ApplicationException {
		HelperMethods.removeNullValues(inputMap);
		String amount=inputMap.get("amount")!=null?inputMap.get("amount").toString():"";
		String debtorAgent=inputMap.get("debtorAgent")!=null?inputMap.get("debtorAgent").toString():"";
		String debtorBranch=inputMap.get("debtorBranch")!=null?inputMap.get("debtorBranch").toString():"";
		String debtorName=inputMap.get("debtorName")!=null?inputMap.get("debtorName").toString():"";
		String debtorAccount=inputMap.get("debtorAccount")!=null?inputMap.get("debtorAccount").toString():"";
		String creditorAgent=inputMap.get("creditorAgent")!=null?inputMap.get("creditorAgent").toString():"";
		String creditorBranch=inputMap.get("creditorBranch")!=null?inputMap.get("creditorBranch").toString():"";
//		if(StringUtils.isBlank(creditorBranch)) {
//			inputMap.put("creditorBranch", HBLConstants.CIPS_CREDITOR_BRANCH);	
//		}
		String creditorName=inputMap.get("creditorName")!=null?inputMap.get("creditorName").toString(): "";
		String creditorAccount=inputMap.get("creditorAccount")!=null?inputMap.get("creditorAccount").toString():"";
		if(StringUtils.isBlank(amount)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10703, HBLConstants.INVALID_AMOUNT);
		}
		if(StringUtils.isBlank(debtorAgent)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10703, HBLConstants.INVALID_DEBTOR_AGENT);
		}
		if(StringUtils.isBlank(debtorBranch)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10703,HBLConstants.INVALID_DEBTOR_BRANCH);
		}
		if(StringUtils.isBlank(debtorName)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10703,HBLConstants.INVALID_DEBTOR_NAME);
		}
		if(StringUtils.isBlank(debtorAccount)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10703,HBLConstants.INVALID_DEBTOR_ACCOUNT);
		}
		if(StringUtils.isBlank(creditorAgent)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10703,HBLConstants.INVALID_CREDTOR_AGENT);
		}
		if(StringUtils.isBlank(creditorBranch)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10703,HBLConstants.INVALID_CREDTOR_BRANCH);
		}
		if(StringUtils.isBlank(creditorName)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10703,HBLConstants.INVALID_CREDTOR_NAME);
		}
		if(StringUtils.isBlank(creditorAccount)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10703,HBLConstants.INVALID_CREDTOR_ACCOUNT);
		}
	}

}
